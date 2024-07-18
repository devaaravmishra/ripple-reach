package com.ripplereach.ripplereach.repositories;

import com.ripplereach.ripplereach.models.Category;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
  Optional<Category> findByName(String name);

  Optional<Category> findByIdOrName(Long id, String name);

  boolean existsByName(String name);
}
