package com.ripplereach.ripplereach.services;

import com.ripplereach.ripplereach.enums.UpvoteType;

public interface UpvoteService {
  void upvotePost(Long postId, Long userId);

  void upvoteComment(Long commentId, Long userId);

  void removeUpvoteFromComment(Long commentId, Long userId);

  void removeUpvoteFromPost(Long postId, Long userId);

  boolean upvoteExists(Long targetId, Long userId, UpvoteType upvoteType);
}
