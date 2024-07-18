package com.ripplereach.ripplereach.services;

import com.ripplereach.ripplereach.models.Upvote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UpvoteRepository extends JpaRepository<Upvote, Long> {
  boolean existsByUserIdAndPostId(Long userId, Long postId);

  boolean existsByUserIdAndCommentId(Long userId, Long commentId);

  void deleteByUserIdAndPostId(Long userId, Long postId);

  void deleteByUserIdAndCommentId(Long userId, Long commentId);
}
