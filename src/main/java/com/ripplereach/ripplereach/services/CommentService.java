package com.ripplereach.ripplereach.services;

import com.ripplereach.ripplereach.dtos.CommentResponse;
import com.ripplereach.ripplereach.models.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    CommentResponse createComment(Long postId, Long userId, String content);
    CommentResponse updateComment(Long commentId, String content);
    void deleteComment(Long commentId);
    void incrementUpvotes(Long commentId);
    void decrementUpvotes(Long commentId);
    CommentResponse findCommentById(Long commentId);
    Comment getCommentById(Long commentId);
    Page<CommentResponse> getCommentsByPostId(Long postId, Pageable pageable);
    Page<CommentResponse> getCommentsByUserId(Long userId, Pageable pageable);
}
