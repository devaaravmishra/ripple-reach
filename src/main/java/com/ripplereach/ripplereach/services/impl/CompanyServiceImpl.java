package com.ripplereach.ripplereach.services.impl;

import com.ripplereach.ripplereach.exceptions.RippleReachException;
import com.ripplereach.ripplereach.models.Company;
import com.ripplereach.ripplereach.repositories.CompanyRepository;
import com.ripplereach.ripplereach.services.CompanyService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    @Override
    @Transactional
    public Company create(String companyName) {
        try {
            Optional<Company> companyOptional = getCompanyByName(companyName);

            if (companyOptional.isPresent()) {
                return companyOptional.get();
            }

            Company company = Company
                    .builder()
                    .name(companyName)
                    .build();

            log.info("Successfully created company with name {}", companyName);

            return companyRepository.save(company);
        } catch (RuntimeException ex) {
            log.error("Error while saving company with name {}", companyName, ex);
            throw new RippleReachException("Error while saving company with name: " + companyName);
        }
    }

    private Optional<Company> getCompanyByName(String companyName) {
        try {
            return companyRepository.findByName(companyName);
        } catch (RuntimeException ex) {
            log.error("Error while finding company with name {}", companyName, ex);
            throw new RippleReachException("Error while finding company with name: " + companyName);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Company> findByName(String companyName) {
        return getCompanyByName(companyName);
    }
}
