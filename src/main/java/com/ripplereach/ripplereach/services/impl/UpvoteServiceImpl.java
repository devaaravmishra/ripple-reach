package com.ripplereach.ripplereach.services.impl;

import com.ripplereach.ripplereach.enums.UpvoteType;
import com.ripplereach.ripplereach.exceptions.RippleReachException;
import com.ripplereach.ripplereach.models.Comment;
import com.ripplereach.ripplereach.models.Post;
import com.ripplereach.ripplereach.models.Upvote;
import com.ripplereach.ripplereach.models.User;
import com.ripplereach.ripplereach.services.UpvoteRepository;
import com.ripplereach.ripplereach.services.UpvoteService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class UpvoteServiceImpl implements UpvoteService {
    private final UpvoteRepository upvoteRepository;

    @Override
    @Transactional
    public void upvotePost(Long postId, Long userId) {
        handleUpvote(postId, userId, "post", UpvoteType.POST);
    }

    @Override
    @Transactional
    public void removeUpvoteFromPost(Long postId, Long userId) {
        handleRemoveUpvote(postId, userId, "post", UpvoteType.POST);
    }

    @Override
    @Transactional
    public void upvoteComment(Long commentId, Long userId) {
        handleUpvote(commentId, userId, "comment", UpvoteType.COMMENT);
    }

    @Override
    @Transactional
    public void removeUpvoteFromComment(Long commentId, Long userId) {
        handleRemoveUpvote(commentId, userId, "comment", UpvoteType.COMMENT);
    }

    private void handleUpvote(Long targetId, Long userId, String targetType, UpvoteType upvoteType) {
        if (upvoteExists(targetId, userId, upvoteType)) {
            log.error("User {} already upvoted {}", userId, targetType);
            throw new EntityExistsException("You have already upvoted this " + targetType + ".");
        }

        try {
            Upvote upvote = createUpvote(targetId, userId, upvoteType);
            upvoteRepository.save(upvote);
            log.info("User {} upvoted {}", userId, targetType);
        } catch (EntityExistsException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            log.error("Error while upvoting {} with id: {} by user id: {}", targetType, targetId, userId, ex);
            throw new RippleReachException("Error while upvoting " + targetType);
        }
    }

    private void handleRemoveUpvote(Long targetId, Long userId, String targetType, UpvoteType upvoteType) {
        try {
            deleteUpvote(targetId, userId, upvoteType);
            log.info("User {} removed upvote from {}", userId, targetType);
        } catch (EntityNotFoundException ex) {
            log.error("Upvote not found for user {} on {}", userId, targetType, ex);
            throw new EntityNotFoundException("Upvote not found for this " + targetType + ".");
        } catch (RuntimeException ex) {
            log.error("Error while removing upvote from {} with id: {} by user id: {}", targetType, targetId, userId, ex);
            throw new RippleReachException("Error while removing upvote from " + targetType);
        }
    }

    private boolean upvoteExists(Long targetId, Long userId, UpvoteType upvoteType) {
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
