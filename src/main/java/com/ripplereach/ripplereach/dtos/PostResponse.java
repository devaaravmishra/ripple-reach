package com.ripplereach.ripplereach.dtos;

import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
  private Long id;
  private String title;
  private String content;
  private UserResponse author;
  private CommunityResponse community;
  private List<PostAttachmentResponse> attachments;
  private Long totalUpvotes;
  private Long totalComments;
  private String link;
  boolean isUpvotedByUser;
  private Instant createdAt;
  private Instant updatedAt;
}
