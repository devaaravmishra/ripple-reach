package com.ripplereach.ripplereach.repositories;

import com.ripplereach.ripplereach.models.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CommunityRepository extends JpaRepository<Community, Long>, PagingAndSortingRepository<Community, Long> {
    boolean existsByName(String name);
}
