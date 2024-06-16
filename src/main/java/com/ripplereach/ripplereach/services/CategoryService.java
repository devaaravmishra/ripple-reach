package com.ripplereach.ripplereach.services;

import com.ripplereach.ripplereach.models.Category;

import java.util.List;

public interface CategoryService {
    Category create(String name, String desc);

    List<Category> getAll();

    Category findById(Long categoryId);

    Category update(Long categoryId, String name, String description);

    void delete(Long categoryId);

    Category partialUpdate(Long categoryId, String name, String description);
}
