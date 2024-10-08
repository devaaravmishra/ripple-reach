package com.ripplereach.ripplereach.services.impl;

import com.ripplereach.ripplereach.enums.UpvoteType;
import com.ripplereach.ripplereach.exceptions.RippleReachException;
import com.ripplereach.ripplereach.models.Comment;
import com.ripplereach.ripplereach.models.Post;
import com.ripplereach.ripplereach.models.Upvote;
import com.ripplereach.ripplereach.models.User;
import com.ripplereach.ripplereach.services.*;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class UpvoteServiceImpl implements UpvoteService {
  private final UpvoteRepository upvoteRepository;
  private final PostService postService;
  private final CommentService commentService;
  private final AuthService authService;

  @Override
  @Transactional
  public void upvotePost(Long postId, Long userId) {
    try {
      User currentUser = authService.getCurrentUser();

      if (!currentUser.getId().equals(userId)) {
        log.error("Unauthorized access to postId: {}, by user Id: {}", postId, userId);
        throw new AccessDeniedException("Unauthorized user!");
      }

      handleUpvote(postId, userId, "post", UpvoteType.POST);
    } catch (RuntimeException ex) {
      throw ex;
    }
  }

  @Override
  @Transactional
  public void removeUpvoteFromPost(Long postId, Long userId) {
    try {
      User currentUser = authService.getCurrentUser();

      if (!currentUser.getId().equals(userId)) {
        log.error("Unauthorized access to postId: {}, by user Id: {}", postId, userId);
        throw new AccessDeniedException("Unauthorized user!");
      }

      handleRemoveUpvote(postId, userId, "post", UpvoteType.POST);
    } catch (RuntimeException ex) {
      throw ex;
    }
  }

  @Override
  @Transactional
  public void upvoteComment(Long commentId, Long userId) {
    try {
      User currentUser = authService.getCurrentUser();

      if (!currentUser.getId().equals(userId)) {
        log.error("Unauthorized access to commentId: {}, by user Id: {}", commentId, userId);
        throw new AccessDeniedException("Unauthorized user!");
      }

      handleUpvote(commentId, userId, "comment", UpvoteType.COMMENT);
    } catch (RuntimeException ex) {
      throw ex;
    }
  }

  @Override
  @Transactional
  public void removeUpvoteFromComment(Long commentId, Long userId) {
    try {
      User currentUser = authService.getCurrentUser();

      if (!currentUser.getId().equals(userId)) {
        log.error("Unauthorized access to commentId: {}, by user Id: {}", commentId, userId);
        throw new AccessDeniedException("Unauthorized user!");
      }

      handleRemoveUpvote(commentId, userId, "comment", UpvoteType.COMMENT);
    } catch (RuntimeException ex) {
      throw ex;
    }
  }

  private void handleUpvote(Long targetId, Long userId, String targetType, UpvoteType upvoteType) {
    try {
      if (upvoteType.equals(UpvoteType.POST)) {
        postService.incrementUpvotes(targetId);
      } else if (upvoteType.equals(UpvoteType.COMMENT)) {
        commentService.incrementUpvotes(targetId);
      }

      if (upvoteExists(targetId, userId, upvoteType)) {
        log.error("User {} already upvoted {}", userId, targetType);
        throw new EntityExistsException("You have already upvoted this " + targetType + ".");
      }

      Upvote upvote = createUpvote(targetId, userId, upvoteType);
      upvoteRepository.save(upvote);
      log.info("User {} upvoted {}", userId, targetType);
    } catch (EntityExistsException | EntityNotFoundException ex) {
      throw ex;
    } catch (RuntimeException ex) {
      log.error(
          "Error while upvoting {} with id: {} by user id: {}", targetType, targetId, userId, ex);
      throw new RippleReachException("Error while upvoting " + targetType);
    }
  }

  private void handleRemoveUpvote(
      Long targetId, Long userId, String targetType, UpvoteType upvoteType) {
    try {
      if (upvoteType.equals(UpvoteType.POST)) {
        postService.decrementUpvotes(targetId);
      } else if (upvoteType.equals(UpvoteType.COMMENT)) {
        commentService.decrementUpvotes(targetId);
      }

      if (!upvoteExists(targetId, userId, upvoteType)) {
        log.error("User {} has not upvoted {}", userId, upvoteType);
        throw new EntityNotFoundException("You have not upvoted this " + upvoteType + ".");
      }

      deleteUpvote(targetId, userId, upvoteType);
      log.info("User {} removed upvote from {}", userId, targetType);
    } catch (EntityNotFoundException ex) {
      log.error("Upvote not found for user {} on {}", userId, targetType, ex);
      throw new EntityNotFoundException("Upvote not found for this " + targetType + ".");
    } catch (RuntimeException ex) {
      log.error(
          "Error while removing upvote from {} with id: {} by user id: {}",
          targetType,
          targetId,
          userId,
          ex);
      throw new RippleReachException("Error while removing upvote from " + targetType);
    }
  }

  @Override
  @Transactional(readOnly = true)
  public boolean upvoteExists(Long targetId, Long userId, UpvoteType upvoteType) {
    return switch (upvoteType) {
      case POST -> upvoteRepository.existsByUserIdAndPostId(userId, targetId);
      case COMMENT -> upvoteRepository.existsByUserIdAndCommentId(userId, targetId);
      default -> throw new IllegalArgumentException("Invalid upvote type");
    };
  }

  private Upvote createUpvote(Long targetId, Long userId, UpvoteType upvoteType) {
    User user = User.builder().id(userId).build();
    Upvote.UpvoteBuilder upvoteBuilder = Upvote.builder().user(user);

    return switch (upvoteType) {
      case POST -> {
        Post post = Post.builder().id(targetId).build();
        yield upvoteBuilder.post(post).build();
      }
      case COMMENT -> {
        Comment comment = Comment.builder().id(targetId).build();
        yield upvoteBuilder.comment(comment).build();
      }
      default -> throw new IllegalArgumentException("Invalid upvote type");
    };
  }

  private void deleteUpvote(Long targetId, Long userId, UpvoteType upvoteType) {
    switch (upvoteType) {
      case POST:
        upvoteRepository.deleteByUserIdAndPostId(userId, targetId);
        break;
      case COMMENT:
        upvoteRepository.deleteByUserIdAndCommentId(userId, targetId);
        break;
      default:
        throw new IllegalArgumentException("Invalid upvote type");
    }
  }
}
