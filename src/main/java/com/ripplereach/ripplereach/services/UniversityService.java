package com.ripplereach.ripplereach.services;

import com.ripplereach.ripplereach.models.University;

import java.util.Optional;

public interface UniversityService {
    University create(String universityName);
    Optional<University> findByName(String universityName);
}
