package com.ripplereach.ripplereach.repositories;

import com.ripplereach.ripplereach.models.Company;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
  public Optional<Company> findByName(String name);
}
