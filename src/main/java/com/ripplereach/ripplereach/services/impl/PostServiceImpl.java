package com.ripplereach.ripplereach.services.impl;

import com.ripplereach.ripplereach.dtos.PostRequestDto;
import com.ripplereach.ripplereach.exceptions.RippleReachException;
import com.ripplereach.ripplereach.models.Post;
import com.ripplereach.ripplereach.models.PostAttachment;
import com.ripplereach.ripplereach.models.User;
import com.ripplereach.ripplereach.repositories.PostRepository;
import com.ripplereach.ripplereach.services.AuthService;
import com.ripplereach.ripplereach.services.ImageService;
import com.ripplereach.ripplereach.services.PostService;
import com.ripplereach.ripplereach.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final ImageService imageService;

    @Override
    @Transactional
    public Post create(PostRequestDto postRequestDto) {
        try {
            User author = userService.findById(postRequestDto.getAuthorId());

            // check whether the authenticated user is creating the post
            Jwt principal = (Jwt) authService.getAuthenticatedUser().getPrincipal();
            if (!principal.getSubject().equals(author.getPhone())) {
                log.error("Unable to create a post, access forbidden attempted by userId: {}",
                        postRequestDto.getAuthorId());
                throw new AccessDeniedException("Unable to create a post, access forbidden");
            }

            List<PostAttachment> attachments = new ArrayList<>();
            if (postRequestDto.getAttachments() != null) {
                for (MultipartFile file : postRequestDto.getAttachments()) {
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
                    .title(postRequestDto.getTitle())
                    .content(postRequestDto.getContent())
                    .author(author)
                    .attachments(attachments)
                    .link(postRequestDto.getLink())
                    .build();

            Post savedPost = postRepository.save(post);

            log.info("Post with id {} created successfully for user id {}",
                    savedPost.getId(), postRequestDto.getAuthorId());

            return savedPost;
        } catch (EntityNotFoundException | AccessDeniedException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            log.error("Error while creating post for user id: {}", postRequestDto.getAuthorId(), ex);
            throw new RippleReachException("Error while creating post!");
        }
    }

    private String saveFile(MultipartFile file) {
        return imageService.saveImage(file);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Post> getAll() {
        try {
            return postRepository.findAll();
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
    public Post update(Long postId, PostRequestDto postRequestDto) {
        try {
            Post post = findById(postId);
            post.setTitle(postRequestDto.getTitle());
            post.setContent(postRequestDto.getContent());

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
}
