package com.ripplereach.ripplereach.services;

import com.ripplereach.ripplereach.models.Comment;

import java.util.List;

public interface CommentService {
    Comment createComment(Long postId, Long userId, String content);
    Comment updateComment(Long commentId, String content);
    void deleteComment(Long commentId);
    List<Comment> getCommentsByPostId(Long postId);
    List<Comment> getCommentsByUserId(Long userId);
}
