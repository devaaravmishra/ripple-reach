package com.ripplereach.ripplereach.services;

import com.ripplereach.ripplereach.dtos.CommunityRequest;
import com.ripplereach.ripplereach.dtos.CommunityUpdateRequest;
import com.ripplereach.ripplereach.models.Community;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommunityService {
    Community create(CommunityRequest communityRequest);
    Page<Community> findAll(Pageable pageable);
    Community findById(Long communityId);
    Community update(Long communityId, CommunityRequest communityRequest);
    Community partialUpdate(Long communityId, CommunityUpdateRequest communityUpdateRequest);
    void delete(Long communityId);
}
