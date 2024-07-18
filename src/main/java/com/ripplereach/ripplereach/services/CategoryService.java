package com.ripplereach.ripplereach.services;

import com.ripplereach.ripplereach.models.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
  Category create(String name, String desc);

  Page<Category> findAll(Pageable pageable);

  Category findById(Long categoryId);

  Category update(Long categoryId, String name, String description);

  void delete(Long categoryId);

  Category partialUpdate(Long categoryId, String name, String description);
}
