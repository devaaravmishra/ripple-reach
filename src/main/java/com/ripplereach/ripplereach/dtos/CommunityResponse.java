package com.ripplereach.ripplereach.dtos;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ripplereach.ripplereach.models.Category;
import com.ripplereach.ripplereach.serializers.ImageUrlSerializer;
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

  @JsonSerialize(using = ImageUrlSerializer.class)
  private String imageUrl;

  private Instant createdAt;
  private Instant updatedAt;
  private Category category;
  private String slug;
}
