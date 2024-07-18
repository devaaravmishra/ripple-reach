package com.ripplereach.ripplereach.repositories;

import com.ripplereach.ripplereach.models.University;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UniversityRepository extends JpaRepository<University, Long> {
  Optional<University> findByName(String name);
}
