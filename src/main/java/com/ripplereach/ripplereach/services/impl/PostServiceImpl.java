package com.ripplereach.ripplereach.services.impl;

import com.ripplereach.ripplereach.dtos.PostRequest;
import com.ripplereach.ripplereach.exceptions.RippleReachException;
import com.ripplereach.ripplereach.models.Community;
import com.ripplereach.ripplereach.models.Post;
import com.ripplereach.ripplereach.models.PostAttachment;
import com.ripplereach.ripplereach.models.User;
import com.ripplereach.ripplereach.repositories.PostRepository;
import com.ripplereach.ripplereach.services.*;
import com.ripplereach.ripplereach.specifications.PostSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserService userService;
    private final AuthService authService;
    private final CommunityService communityService;
    private final ImageService imageService;

    @Override
    @Transactional
    public Post create(PostRequest postRequest) {
        try {
            User author = userService.findById(postRequest.getAuthorId());

            // check whether the authenticated user is creating the post
            Jwt principal = (Jwt) authService.getAuthenticatedUser().getPrincipal();
            if (!principal.getSubject().equals(author.getPhone())) {
                log.error("Unable to create a post, access forbidden attempted by userId: {}",
                        postRequest.getAuthorId());
                throw new AccessDeniedException("Unable to create a post, access forbidden");
            }

            Community community = communityService.findById(postRequest.getCommunityId());

            List<PostAttachment> attachments = new ArrayList<>();
            if (postRequest.getAttachments() != null) {
                for (MultipartFile file : postRequest.getAttachments()) {
                    String fileName = file.getOriginalFilename();
                    String fileUrl = saveFile(file); // Implement this method to save the file and return its URL
                    PostAttachment attachment = PostAttachment.builder()
                        .fileName(fileName)
                        .url(fileUrl)
                        .build();
                    attachments.add(attachment);
                }
            }


            Post post = Post.builder()
                    .title(postRequest.getTitle())
                    .content(postRequest.getContent())
                    .author(author)
                    .attachments(attachments)
                    .link(postRequest.getLink())
                    .totalComments(0L)
                    .totalUpvotes(0L)
                    .community(community)
                    .build();

            Post savedPost = postRepository.save(post);

            log.info("Post with id {} created successfully for user id {}",
                    savedPost.getId(), postRequest.getAuthorId());

            return savedPost;
        } catch (EntityNotFoundException | AccessDeniedException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            log.error("Error while creating post for user id: {}", postRequest.getAuthorId(), ex);
            throw new RippleReachException("Error while creating post!");
        }
    }

    private String saveFile(MultipartFile file) {
        return imageService.saveImage(file);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> findAll(String search, Pageable pageable) {
        try {
            Specification<Post> spec = Specification.where(null);
            
            search = removeAllWhitespace(search);
            if (!search.isEmpty()) {
                spec = spec.and(PostSpecification.containsTextInTitleOrContentOrCommunity(search));
            }

            return postRepository.findAll(spec, pageable);
        } catch (RuntimeException ex) {
            log.error("Error while retrieving all posts", ex);
            throw new RippleReachException("Error while retrieving all posts!");
        }
    }
    
    @Override
    public Page<Post> findAllByCommunity(Long communityId, String search, Pageable pageable) {
        try {
            Specification<Post> spec = Specification.where((root, query, cb) ->
                    cb.equal(root.get("community").get("id"), communityId)
            );

            search = removeAllWhitespace(search);
            if (!search.isEmpty()) {
                spec = spec.and(PostSpecification.containsTextInTitleOrContentOrCommunity(search));
            }

            return postRepository.findAll(spec, pageable);
        } catch (RuntimeException ex) {
            log.error("Error while retrieving all posts");
            throw new RippleReachException("Error while retrieving all posts!");
        }
    }

    @Override
    public Page<Post> findAllByAuthor(Long authorId, String search, Pageable pageable) {
        try {
            Specification<Post> spec = Specification.where((root, query, cb) ->
                    cb.equal(root.get("author").get("id"), authorId)
            );

            search = removeAllWhitespace(search);
            if (!search.isEmpty()) {
                spec = spec.and(PostSpecification.containsTextInTitleOrContentOrCommunity(search));
            }

            return postRepository.findAll(spec, pageable);
        } catch (RuntimeException ex) {
            log.error("Error while retrieving all posts");
            throw new RippleReachException("Error while retrieving all posts!");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Post findById(Long postId) {
        try {
            return postRepository.findById(postId)
                    .orElseThrow(() -> new EntityNotFoundException("Post not found"));
        } catch (EntityNotFoundException ex) {
            log.error("Post not found with id: {}", postId);
            throw ex;
        } catch (RuntimeException ex) {
            log.error("Error while retrieving post with id: {}", postId);
            throw new RippleReachException("Error while retrieving post!");
        }
    }

    @Override
    @Transactional
    public Post update(Long postId, PostRequest postRequest) {
        try {
            Post post = findById(postId);
            post.setTitle(postRequest.getTitle());
            post.setContent(postRequest.getContent());

            Post updatedPost = postRepository.save(post);

            log.info("Post with id {} updated successfully", postId);

            return updatedPost;
        } catch (EntityNotFoundException ex) {
            log.error("Post not found with id: {}", postId);
            throw ex;
        } catch (RuntimeException ex) {
            log.error("Error while updating post with id: {}", postId);
            throw new RippleReachException("Error while updating post!");
        }
    }

    @Override
    @Transactional
    public void delete(Long postId) {
        try {
            postRepository.deleteById(postId);
            log.info("Post with id {} deleted successfully", postId);
        } catch (RuntimeException ex) {
            log.error("Error while deleting post with id: {}", postId);
            throw new RippleReachException("Error while deleting post!");
        }
    }

    @Override
    @Transactional
    public void updatePostTotalComments(Post post) {
        try {
            postRepository.save(post);
            log.info("Post with id {} comment count updated to {}", post.getId(), post.getTotalComments());
        } catch (RuntimeException ex) {
            log.error("Error while updating comment count for post with id: {}", post.getId());
            throw new RippleReachException("Error while updating comment count for post!");
        }
    }

    @Transactional
    @Override
    public void incrementUpvotes(Long postId) {
        Post post = findById(postId);
        post.setTotalUpvotes(post.getTotalUpvotes() + 1);
        postRepository.save(post);
    }

    @Transactional
    @Override
    public void decrementUpvotes(Long postId) {
        Post post = findById(postId);
        post.setTotalUpvotes(post.getTotalUpvotes() - 1);
        postRepository.save(post);
    }

    private static String removeAllWhitespace(String search) {
        return search.replaceAll("[\\p{Z}\\s\"']", "");
    }
}
