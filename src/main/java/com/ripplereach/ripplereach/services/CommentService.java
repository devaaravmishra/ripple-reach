package com.ripplereach.ripplereach.services;

import com.ripplereach.ripplereach.models.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    Comment createComment(Long postId, Long userId, String content);
    Comment updateComment(Long commentId, String content);
    void deleteComment(Long commentId);
    Page<Comment> getCommentsByPostId(Long postId, Pageable pageable);
    Page<Comment> getCommentsByUserId(Long userId, Pageable pageable);
}
