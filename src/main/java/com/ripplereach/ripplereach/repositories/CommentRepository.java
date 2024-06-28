package com.ripplereach.ripplereach.repositories;

import com.ripplereach.ripplereach.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>,
        JpaSpecificationExecutor<Comment>,
        PagingAndSortingRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);

    List<Comment> findByAuthorId(Long authorId);
}
