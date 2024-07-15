package com.ripplereach.ripplereach.services.impl;

import com.ripplereach.ripplereach.dtos.CommentResponse;
import com.ripplereach.ripplereach.exceptions.RippleReachException;
import com.ripplereach.ripplereach.mappers.Mapper;
import com.ripplereach.ripplereach.models.Comment;
import com.ripplereach.ripplereach.models.Post;
import com.ripplereach.ripplereach.models.User;
import com.ripplereach.ripplereach.repositories.CommentRepository;
import com.ripplereach.ripplereach.services.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UpvoteRepository upvoteRepository;
    private final PostService postService;
    private final UserService userService;
    private final AuthService authService;
    private final Mapper<Comment, CommentResponse> commentResponseMapper;


    @Override
    @Transactional
    public CommentResponse createComment(Long postId, Long userId, String content) {
        try {
            Post post = postService.getPostById(postId);
            User user = userService.findById(userId);

            User currentUser = authService.getCurrentUser();

            if (!user.getId().equals(currentUser.getId())) {
                throw new AccessDeniedException("Unauthorized access!");
            }

            Comment comment = Comment.builder()
                    .post(post)
                    .author(user)
                    .content(content)
                    .totalUpvotes(0L)
                    .build();


            post.setTotalComments(post.getTotalComments() + 1);
            postService.updatePostTotalComments(post);

            Comment savedComment = commentRepository.save(comment);

            log.info("Comment with commentId: {} is created within post with postId: {} by user with userId: {}",
                    savedComment.getId(), postId, userId);

            CommentResponse commentResponse = commentResponseMapper.mapTo(savedComment);
            commentResponse.setUpvotedByUser(isUpvotedByLoggedInUser(savedComment.getId()));

            return commentResponse;
        } catch (EntityNotFoundException | AccessDeniedException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            log.error("Error while creating the comment for postId: {} and userId: {}", postId, userId);
            throw new RippleReachException("Error while creating the comment!");
        }
    }

    @Override
    @Transactional
    public CommentResponse updateComment(Long commentId, String content) {
        try {
            Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new EntityNotFoundException("Comment not found with id: " + commentId));

            User currentUser = authService.getCurrentUser();

            Long postId = comment.getPost().getId();
            Long userId = comment.getAuthor().getId();

            if (!userId.equals(currentUser.getId())) {
                throw new AccessDeniedException("Unauthorized access!");
            }

            comment.setContent(content);
            Comment updatedComment = commentRepository.save(comment);

            log.info("Comment with commentId: {} within post with postId: {} by user with userId: {} is updated successfully!",
                    commentId, postId, userId
            );

            CommentResponse commentResponse = commentResponseMapper.mapTo(updatedComment);
            commentResponse.setUpvotedByUser(isUpvotedByLoggedInUser(updatedComment.getId()));

            return commentResponse;
        } catch (EntityNotFoundException | AccessDeniedException ex) {
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
    public CommentResponse findCommentById(Long commentId) {
        try {
            Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(() ->
                            new EntityNotFoundException("Comment not found with id: " + commentId));

            return commentResponseMapper.mapTo(comment);
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
    public Comment getCommentById(Long commentId) {
        try {
            return commentRepository.findById(commentId)
                    .orElseThrow(() -> new EntityNotFoundException("Post not found"));
        } catch (EntityNotFoundException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            log.error("Error while retrieving post with id: {}", commentId);
            throw new RippleReachException("Error while retrieving post with id:" + commentId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommentResponse> getCommentsByPostId(Long postId, Pageable pageable) {
        try {
            return commentRepository.findByPostId(postId, pageable)
                    .map(comment -> {
                        CommentResponse commentResponse = commentResponseMapper.mapTo(comment);
                        commentResponse.setUpvotedByUser(isUpvotedByLoggedInUser(commentResponse.getId()));
                        return commentResponse;
                    });
        } catch (RuntimeException ex) {
            log.error("Error while retrieving comments for postId: {}", postId);
            throw new RippleReachException("Error while retrieving comments for the post!");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommentResponse> getCommentsByUserId(Long userId, Pageable pageable) {
        try {
            return commentRepository.findByAuthorId(userId, pageable)
                    .map(comment -> {
                        CommentResponse commentResponse = commentResponseMapper.mapTo(comment);
                        commentResponse.setUpvotedByUser(isUpvotedByLoggedInUser(commentResponse.getId()));
                        return commentResponse;
                    });
        } catch (RuntimeException ex) {
            log.error("Error while retrieving comments for userId: {}", userId);
            throw new RippleReachException("Error while retrieving comments for the user!");
        }
    }

    @Override
    @Transactional
    public void incrementUpvotes(Long commentId) {
        try {
            Comment comment = getCommentById(commentId);
            comment.setTotalUpvotes(comment.getTotalUpvotes() + 1);
            commentRepository.save(comment);
        } catch (EntityNotFoundException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            log.error("Unable to upvote the comment with id: {}", commentId, ex);
            throw new RippleReachException("Unable to upvote the comment with id: " + commentId);
        }
    }

    @Override
    @Transactional
    public void decrementUpvotes(Long commentId) {
        try {
            Comment comment = getCommentById(commentId);
            comment.setTotalUpvotes(comment.getTotalUpvotes() - 1);
            commentRepository.save(comment);
        } catch (EntityNotFoundException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            log.error("Unable to remove upvote from the comment with id: {}", commentId, ex);
            throw new RippleReachException("Unable to remove upvote from the comment with id: " + commentId);
        }
    }

    private boolean isUpvotedByLoggedInUser(Long targetId) {
        try {
            if (!authService.isLoggedIn()) {
                return false;
            }

            User currentUser = authService.getCurrentUser();
            return upvoteRepository.existsByUserIdAndCommentId(currentUser.getId(), targetId);
        } catch (RuntimeException ex) {
            throw new RippleReachException(ex.getMessage());
        }
    }
}
