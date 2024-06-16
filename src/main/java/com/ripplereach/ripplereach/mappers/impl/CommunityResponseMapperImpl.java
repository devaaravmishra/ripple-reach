package com.ripplereach.ripplereach.mappers.impl;

import com.ripplereach.ripplereach.dtos.CategoryResponseDto;
import com.ripplereach.ripplereach.dtos.CommunityResponseDto;
import com.ripplereach.ripplereach.mappers.Mapper;
import com.ripplereach.ripplereach.models.Category;
import com.ripplereach.ripplereach.models.Community;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CommunityResponseMapperImpl implements Mapper<Community, CommunityResponseDto> {
    private ModelMapper modelMapper;

    @Override
    public Community mapFrom(CommunityResponseDto communityResponseDto) {
        return modelMapper.map(communityResponseDto, Community.class);
    }

    @Override
    public CommunityResponseDto mapTo(Community community) {
        return modelMapper.map(community, CommunityResponseDto.class);
    }
}
