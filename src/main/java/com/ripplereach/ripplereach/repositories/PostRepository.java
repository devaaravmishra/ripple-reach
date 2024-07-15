package com.ripplereach.ripplereach.repositories;

import com.ripplereach.ripplereach.models.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PostRepository extends JpaRepository<Post, Long>,
        JpaSpecificationExecutor<Post>,
        PagingAndSortingRepository<Post, Long> {
}
