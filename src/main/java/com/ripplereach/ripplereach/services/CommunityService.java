package com.ripplereach.ripplereach.services;

import com.ripplereach.ripplereach.dtos.CommunityUpdateRequestDto;
import com.ripplereach.ripplereach.dtos.CommunityRequestDto;
import com.ripplereach.ripplereach.models.Community;

import java.util.List;

public interface CommunityService {
    Community create(CommunityRequestDto communityRequestDto);
    List<Community> getAll();
    Community findById(Long communityId);
    Community update(Long communityId, CommunityRequestDto communityRequestDto);
    Community partialUpdate(Long communityId, CommunityUpdateRequestDto communityUpdateRequestDto);
    void delete(Long communityId);
}
