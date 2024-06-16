package com.ripplereach.ripplereach.dtos;

import com.ripplereach.ripplereach.models.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetAllCategoryResponseDto {
    private List<Category> categories;
}
