package com.ripplereach.ripplereach.dtos;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
  private Long id;
  private String content;
  private PostResponse post;
  private UserResponse author;
  private Long totalUpvotes;
  boolean isUpvotedByUser;
  private Instant createdAt;
  private Instant updatedAt;
}
