package com.ripplereach.ripplereach.dtos;

import com.ripplereach.ripplereach.models.Community;
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
public class CategoryResponse {
  private Long id;
  private String name;
  private String description;
  private Instant createdAt;
  private Instant updatedAt;
  private String slug;
  private List<Community> communities;
}
