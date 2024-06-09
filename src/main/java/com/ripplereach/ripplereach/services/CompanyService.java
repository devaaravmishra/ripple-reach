package com.ripplereach.ripplereach.services;

import com.ripplereach.ripplereach.models.Company;

import java.util.Optional;

public interface CompanyService {
    Company create(String companyName);
    Optional<Company> findByName(String companyName);
}
