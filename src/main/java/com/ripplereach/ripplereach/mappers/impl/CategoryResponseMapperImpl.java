package com.ripplereach.ripplereach.mappers.impl;

import com.ripplereach.ripplereach.dtos.CategoryResponse;
import com.ripplereach.ripplereach.mappers.Mapper;
import com.ripplereach.ripplereach.models.Category;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CategoryResponseMapperImpl implements Mapper<Category, CategoryResponse> {
  private ModelMapper modelMapper;

  @Override
  public Category mapFrom(CategoryResponse categoryResponse) {
    return modelMapper.map(categoryResponse, Category.class);
  }

  @Override
  public CategoryResponse mapTo(Category category) {
    return modelMapper.map(category, CategoryResponse.class);
  }
}
