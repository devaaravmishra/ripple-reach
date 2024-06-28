package com.ripplereach.ripplereach.repositories;

import com.ripplereach.ripplereach.models.University;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UniversityRepository extends JpaRepository<University, Long> {
  Optional<University> findByName(String name);
}
