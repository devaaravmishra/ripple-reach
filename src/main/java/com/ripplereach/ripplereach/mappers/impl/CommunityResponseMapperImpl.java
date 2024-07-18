package com.ripplereach.ripplereach.mappers.impl;

import com.ripplereach.ripplereach.dtos.CommunityResponse;
import com.ripplereach.ripplereach.mappers.Mapper;
import com.ripplereach.ripplereach.models.Community;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CommunityResponseMapperImpl implements Mapper<Community, CommunityResponse> {
  private ModelMapper modelMapper;

  @Override
  public Community mapFrom(CommunityResponse communityResponse) {
    return modelMapper.map(communityResponse, Community.class);
  }

  @Override
  public CommunityResponse mapTo(Community community) {
    return modelMapper.map(community, CommunityResponse.class);
  }
}
