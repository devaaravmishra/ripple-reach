package com.ripplereach.ripplereach.mappers.impl;

import com.ripplereach.ripplereach.dtos.CategoryResponseDto;
import com.ripplereach.ripplereach.mappers.Mapper;
import com.ripplereach.ripplereach.models.Category;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CategoryResponseMapperImpl implements Mapper<Category, CategoryResponseDto> {
    private ModelMapper modelMapper;

    @Override
    public Category mapFrom(CategoryResponseDto categoryResponseDto) {
        return modelMapper.map(categoryResponseDto, Category.class);
    }

    @Override
    public CategoryResponseDto mapTo(Category category) {
        return modelMapper.map(category, CategoryResponseDto.class);
    }
}
