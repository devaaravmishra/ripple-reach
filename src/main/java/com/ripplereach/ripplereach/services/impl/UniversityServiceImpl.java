package com.ripplereach.ripplereach.services.impl;

import com.ripplereach.ripplereach.exceptions.RippleReachException;
import com.ripplereach.ripplereach.models.University;
import com.ripplereach.ripplereach.repositories.UniversityRepository;
import com.ripplereach.ripplereach.services.UniversityService;
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
public class UniversityServiceImpl implements UniversityService {

    private final UniversityRepository universityRepository;

    @Override
    @Transactional
    public University create(String universityName) {
       try {
           Optional<University> universityOptional = getUniversityByName(universityName);

           if (universityOptional.isPresent()) {
               return universityOptional.get();
           }

           University university = University
                   .builder()
                   .name(universityName)
                   .build();

           log.info("Successfully created university with name {}", universityName);

           return universityRepository.save(university);
       } catch (RuntimeException ex) {
           log.error("Error while saving university with name {}", universityName);
           throw new RippleReachException("Error while saving university with name: " + universityName);
       }
    }

    private Optional<University> getUniversityByName(String universityName) {
        try {
            return universityRepository.findByName(universityName);
        } catch (RuntimeException ex) {
            log.error("Error while finding university with name {}", universityName, ex);
            throw new RippleReachException("Error while finding university with name: " + universityName);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<University> findByName(String universityName) {
        return getUniversityByName(universityName);
    }
}
