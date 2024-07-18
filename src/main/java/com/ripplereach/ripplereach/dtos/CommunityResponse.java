package com.ripplereach.ripplereach.dtos;

import com.ripplereach.ripplereach.models.Category;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommunityResponse {
  private Long id;
  private String name;
  private String description;
  private String imageUrl;
  private Instant createdAt;
  private Instant updatedAt;
  private Category category;
  private String slug;
}
