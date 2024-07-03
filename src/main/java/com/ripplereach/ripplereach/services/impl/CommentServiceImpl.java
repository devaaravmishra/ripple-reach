package com.ripplereach.ripplereach.services.impl;

import com.ripplereach.ripplereach.exceptions.RippleReachException;
import com.ripplereach.ripplereach.models.Comment;
import com.ripplereach.ripplereach.models.Post;
import com.ripplereach.ripplereach.models.User;
import com.ripplereach.ripplereach.repositories.CommentRepository;
import com.ripplereach.ripplereach.services.CommentService;
import com.ripplereach.ripplereach.services.PostService;
import com.ripplereach.ripplereach.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostService postService;
    private final UserService userService;

    @Override
    @Transactional
    public Comment createComment(Long postId, Long userId, String content) {
        try {
            Post post = postService.findById(postId);
            User user = userService.findById(userId);

            Comment comment = Comment.builder()
                    .post(post)
                    .author(user)
                    .content(content)
                    .build();


            post.setTotalComments(post.getTotalComments() + 1);
            postService.updatePostTotalComments(post);

            Comment commentResponse = commentRepository.save(comment);

            log.info("Comment with commentId: {} is created within post with postId: {} by user with userId: {}",
                    commentResponse.getId(), postId, userId);

            return commentResponse;
        } catch (EntityNotFoundException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            log.error("Error while creating the comment for postId: {} and userId: {}", postId, userId);
            throw new RippleReachException("Error while creating the comment!");
        }
    }

    @Override
    @Transactional
    public Comment updateComment(Long commentId, String content) {
        try {
            Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new EntityNotFoundException("Comment not found with id: " + commentId));

            comment.setContent(content);
            Comment updatedComment = commentRepository.save(comment);

            Long postId = updatedComment.getPost().getId();
            Long userId = updatedComment.getAuthor().getId();

            log.info("Comment with commentId: {} within post with postId: {} by user with userId: {} is updated successfully!",
                    commentId, postId, userId
            );

            return updatedComment;
        } catch (EntityNotFoundException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            log.error("Error while updating the comment with id: {}", commentId);
            throw new RippleReachException("Error while updating the comment!");
        }
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        try {
            Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new EntityNotFoundException("Comment not found with id: " + commentId));

            // Decrement the comment count in the post
            Post post = comment.getPost();
            post.setTotalComments(Math.max(post.getTotalComments() - 1, 0));
            postService.updatePostTotalComments(post);

            commentRepository.delete(comment);

            log.info("Comment with commentId: {} is deleted successfully", commentId);
        } catch (EntityNotFoundException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            log.error("Error while deleting the comment with id: {}", commentId);
            throw new RippleReachException("Error while deleting the comment!");
        }
    }

    @Override
    public Comment getCommentById(Long commentId) {
        try {
            return commentRepository.findById(commentId)
                    .orElseThrow(() ->
                            new EntityNotFoundException("Comment not found with id: " + commentId));

        } catch (EntityNotFoundException ex) {
            log.error("Comment not found with id: {}", commentId);
            throw ex;
        } catch (RuntimeException ex) {
            log.error("Error while retrieving comment with id: {}", commentId);
            throw new RippleReachException("Error while retrieving comment with id: " + commentId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Comment> getCommentsByPostId(Long postId, Pageable pageable) {
        try {
            return commentRepository.findByPostId(postId, pageable);
        } catch (RuntimeException ex) {
            log.error("Error while retrieving comments for postId: {}", postId);
            throw new RippleReachException("Error while retrieving comments for the post!");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Comment> getCommentsByUserId(Long userId, Pageable pageable) {
        try {
            return commentRepository.findByAuthorId(userId, pageable);
        } catch (RuntimeException ex) {
            log.error("Error while retrieving comments for userId: {}", userId);
            throw new RippleReachException("Error while retrieving comments for the user!");
        }
    }

    @Transactional
    public void incrementUpvotes(Long commentId) {
        Comment comment = getCommentById(commentId);
        comment.setTotalUpvotes(comment.getTotalUpvotes() + 1);
        commentRepository.save(comment);
    }

    @Transactional
    public void decrementUpvotes(Long commentId) {
        Comment comment = getCommentById(commentId);
        comment.setTotalUpvotes(comment.getTotalUpvotes() - 1);
        commentRepository.save(comment);
    }
}
