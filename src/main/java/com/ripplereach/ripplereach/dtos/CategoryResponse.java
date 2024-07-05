package com.ripplereach.ripplereach.dtos;

import com.ripplereach.ripplereach.models.Community;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

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
