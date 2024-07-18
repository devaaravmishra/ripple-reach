package com.ripplereach.ripplereach.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommunityPostsResponse {
  private CommunityResponse community;
  private Page<PostResponseByCommunity> posts;
}
