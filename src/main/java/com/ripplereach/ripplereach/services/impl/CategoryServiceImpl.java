package com.ripplereach.ripplereach.services.impl;

import com.ripplereach.ripplereach.exceptions.RippleReachException;
import com.ripplereach.ripplereach.models.Category;
import com.ripplereach.ripplereach.repositories.CategoryRepository;
import com.ripplereach.ripplereach.services.CategoryService;
import com.ripplereach.ripplereach.utilities.SlugUtil;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
  private final CategoryRepository categoryRepository;

  @Override
  @Transactional
  public Category create(String name, String desc) {
    validateCategoryNameAndDescription(name, desc);

    if (categoryRepository.existsByName(name)) {
      log.error("Category already exists with name: {}", name);
      throw new EntityExistsException("Category already exists with name: " + name);
    }

    Category category = buildCategory(name, desc);
    return saveCategory(category);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Category> findAll(Pageable pageable) {
    try {
      return categoryRepository.findAll(pageable);
    } catch (RuntimeException ex) {
      log.error("Error while fetching categories!", ex);
      throw new RippleReachException("Error while fetching categories!");
    }
  }

  @Override
  @Transactional(readOnly = true)
  public Category findById(Long categoryId) {
    return getCategoryById(categoryId)
        .orElseThrow(
            () -> {
              log.error("Can't find category with categoryId: {}", categoryId);
              return new EntityNotFoundException(
                  "Can't find category with categoryId: " + categoryId);
            });
  }

  @Override
  @Transactional
  public Category partialUpdate(Long categoryId, String name, String description) {
    Category existingCategory =
        getCategoryById(categoryId)
            .orElseThrow(
                () -> {
                  log.error("Can't find category with categoryId: {}", categoryId);
                  return new EntityNotFoundException(
                      "Can't find category with categoryId: " + categoryId);
                });

    if (!isFieldEmpty(name)) {
      if (categoryRepository.existsByName(name)) {
        log.error("Category already exists with name: {}", name);
        throw new EntityExistsException("Category already exists with name: " + name);
      }

      existingCategory.setName(name);
      existingCategory.setSlug(SlugUtil.createWithUnderscore(name));
    }

    if (!isFieldEmpty(description)) {
      existingCategory.setDescription(description);
    }

    return saveCategory(existingCategory);
  }

  @Override
  @Transactional
  public Category update(Long categoryId, String name, String description) {
    validateCategoryNameAndDescription(name, description);

    Optional<Category> existingCategory = getCategoryById(categoryId);

    boolean anotherCategoryAlreadyExists =
        existingCategory.isEmpty() && categoryRepository.existsByName(name);

    if (anotherCategoryAlreadyExists) {
      log.error("Category already exists with name: {}", name);
      throw new EntityExistsException("Category already exists with name: " + name);
    }

    Category category = existingCategory.orElse(buildCategory(name, description));
    category.setName(name);
    category.setDescription(description);
    category.setSlug(SlugUtil.createWithUnderscore(name));
    category.setId(categoryId);

    return saveCategory(category);
  }

  @Override
  @Transactional
  public void delete(Long categoryId) {
    if (!categoryRepository.existsById(categoryId)) {
      log.error("Can't find category with categoryId: {}", categoryId);
      throw new EntityNotFoundException("Can't find category with id: " + categoryId);
    }

    categoryRepository.deleteById(categoryId);
    log.info("Category with categoryId: {} is deleted successfully!", categoryId);
  }

  private Optional<Category> getCategoryById(Long categoryId) {
    try {
      return categoryRepository.findById(categoryId);
    } catch (RuntimeException ex) {
      log.error("Error while retrieving category with categoryId: {}", categoryId);
      throw new RippleReachException(
          "Error while retrieving category with categoryId: " + categoryId);
    }
  }

  private Optional<Category> getCategoryByIdOrName(Long categoryId, String name) {
    try {
      return categoryRepository.findByIdOrName(categoryId, name);
    } catch (RuntimeException ex) {
      log.error("Error while retrieving category with categoryId: {}, name: {}", categoryId, name);
      throw new RippleReachException(
          "Error while retrieving category with categoryId: " + categoryId);
    }
  }

  private void validateCategoryNameAndDescription(String name, String description) {
    if (isFieldEmpty(name) || isFieldEmpty(description)) {
      log.error("Unable to save category, invalid request!");
      throw new HttpClientErrorException(
          HttpStatus.BAD_REQUEST, "Unable to save category, invalid request!");
    }
  }

  private boolean isFieldEmpty(String value) {
    return value == null || value.trim().isEmpty();
  }

  private Category buildCategory(String name, String description) {
    return Category.builder()
        .name(name)
        .description(description)
        .slug(SlugUtil.createWithUnderscore(name))
        .build();
  }

  private Category saveCategory(Category category) {
    try {
      Category savedCategory = categoryRepository.save(category);
      log.info(
          "Category with categoryId: {}, name: {} saved successfully!",
          savedCategory.getId(),
          savedCategory.getName());
      return savedCategory;
    } catch (RuntimeException ex) {
      log.error("Error while saving category with name: {}", category.getName(), ex);
      throw new RippleReachException(
          "Error while saving category with name: " + category.getName());
    }
  }
}
