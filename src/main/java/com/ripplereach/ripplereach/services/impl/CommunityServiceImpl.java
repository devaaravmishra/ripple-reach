package com.ripplereach.ripplereach.services.impl;

import com.ripplereach.ripplereach.dtos.CommunityUpdateRequestDto;
import com.ripplereach.ripplereach.dtos.CommunityRequestDto;
import com.ripplereach.ripplereach.exceptions.RippleReachException;
import com.ripplereach.ripplereach.models.Category;
import com.ripplereach.ripplereach.models.Community;
import com.ripplereach.ripplereach.repositories.CategoryRepository;
import com.ripplereach.ripplereach.repositories.CommunityRepository;
import com.ripplereach.ripplereach.services.CommunityService;
import com.ripplereach.ripplereach.services.ImageService;
import com.ripplereach.ripplereach.utilities.SlugUtil;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class CommunityServiceImpl implements CommunityService {
    private final CommunityRepository communityRepository;
    private final CategoryRepository categoryRepository;
    private final ImageService imageService;
    private final Integer IMAGE_SIZE = 5 * 1024 * 1024;

    @Override
    @Transactional
    public Community create(CommunityRequestDto communityRequestDto) {
        validateCommunityRequest(communityRequestDto);

        if (communityRequestDto.getCategoryId() == null) {
            log.error("Missing field categoryId.");
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing field categoryId.");
        }

        String communityName = communityRequestDto.getName();
        boolean communityExists = communityRepository.existsByName(communityName);

        if (communityExists) {
            log.error("Community with name: {}, already exists!", communityName);
            throw new EntityExistsException("Community already exists with name: " + communityName);
        }

        Category category = findCategoryById(communityRequestDto.getCategoryId());

        Community community = buildCommunity(communityRequestDto, category, new Community());

        return saveCommunity(community);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Community> getAll() {
        try {
            return communityRepository.findAll();
        } catch (RuntimeException ex) {
            log.error("Error while fetching all communities", ex);
            throw new RippleReachException("Error while fetching all communities", ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Community findById(Long communityId) {
        Optional<Community> community = findCommunityById(communityId);

        if (community.isEmpty()) {
            log.error("Community with communityId: {} doesn't exists!", communityId);
            throw new EntityNotFoundException("Can't find community with communityId: " + communityId);
        }

        return community.get();
    }

    @Override
    @Transactional
    public Community update(Long communityId, CommunityRequestDto communityRequestDto) {
        validateCommunityRequest(communityRequestDto);

        Community existingCommunity = findCommunityById(communityId)
                .orElseThrow(() -> new EntityNotFoundException("Can't find community with communityId: " + communityId));

        Category category = findCategoryById(communityRequestDto.getCategoryId());

        Community updatedCommunity = buildCommunity(communityRequestDto, category, existingCommunity);

        return saveCommunity(updatedCommunity);
    }

    @Override
    @Transactional
    public Community partialUpdate(Long communityId, CommunityUpdateRequestDto communityUpdateRequestDto) {
        Community existingCommunity = findCommunityById(communityId)
                .orElseThrow(() -> new EntityNotFoundException("Can't find community with communityId: " + communityId));

        updateCommunityFields(existingCommunity, communityUpdateRequestDto);

        return saveCommunity(existingCommunity);
    }

    @Override
    @Transactional
    public void delete(Long communityId) {
        Community community = findCommunityById(communityId)
                .orElseThrow(() -> {
                    log.error("Community with communityId: {} doesn't exist!", communityId);
                    return new EntityNotFoundException("Can't find community with communityId: " + communityId);
                });

        communityRepository.delete(community);
        log.info("Community with id: {} deleted successfully!", communityId);
    }

    private Optional<Community> findCommunityById(Long communityId) {
        try {
           return communityRepository.findById(communityId);
        } catch (RuntimeException ex) {
           log.error("Error while finding community with id: {}", communityId);
           throw new RippleReachException("Error while finding community with id: " + communityId);
        }
    }

    private Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    log.error("Category with categoryId: {} doesn't exist!", categoryId);
                    return new EntityNotFoundException("Can't find category with categoryId: " + categoryId);
                });
    }

    private void validateCommunityRequest(CommunityRequestDto communityRequestDto) {
        if (communityRequestDto.getName() == null || communityRequestDto.getName().isEmpty()) {
            log.error("Community name is missing");
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Community name is required");
        }
        if (communityRequestDto.getDesc() == null || communityRequestDto.getDesc().isEmpty()) {
            log.error("Community description is missing");
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Community description is required");
        }
        if (communityRequestDto.getImage() != null && communityRequestDto.getImage().getSize() > IMAGE_SIZE) {
            log.error("Community image size exceeds the limit: " + IMAGE_SIZE);
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Community image size exceeds the limit: " + IMAGE_SIZE);
        }
    }

    private Community buildCommunity(CommunityRequestDto dto, Category category, Community community) {
        community.setName(dto.getName());
        community.setSlug(SlugUtil.createWithUnderscore(dto.getName()));
        community.setDescription(dto.getDesc());
        community.setCategory(category);

        if (dto.getImage() != null) {
            String imageUrl = imageService.saveImage(dto.getImage());
            community.setImageUrl(imageUrl);
        }

        return community;
    }

    private Community saveCommunity(Community community) {
        try {
            Community savedCommunity = communityRepository.save(community);
            log.info("Community with id: {}, name: {} saved successfully!", savedCommunity.getId(), savedCommunity.getName());
            return savedCommunity;
        } catch (RuntimeException ex) {
            log.error("Error while saving community: {}", community, ex);
            throw new RippleReachException("Error while saving community: " + community.getName(), ex);
        }
    }

    private void updateCommunityFields(Community community, CommunityUpdateRequestDto dto) {
        if (dto.getName() != null && !dto.getName().isEmpty()) {
            community.setName(dto.getName());
        }
        if (dto.getDesc() != null && !dto.getDesc().isEmpty()) {
            community.setDescription(dto.getDesc());
        }
        if (dto.getImage() != null) {
            String imageUrl = imageService.saveImage(dto.getImage());
            community.setImageUrl(imageUrl);
        }
    }
}
