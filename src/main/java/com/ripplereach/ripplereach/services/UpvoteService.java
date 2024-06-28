package com.ripplereach.ripplereach.services;

public interface UpvoteService {
    void upvotePost(Long postId, Long userId);
    void upvoteComment(Long commentId, Long userId);
    void removeUpvoteFromComment(Long commentId, Long userId);
    void removeUpvoteFromPost(Long postId, Long userId);
}
