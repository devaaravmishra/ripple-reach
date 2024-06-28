package com.ripplereach.ripplereach.services.impl;

import com.ripplereach.ripplereach.dtos.PostRequestDto;
import com.ripplereach.ripplereach.exceptions.RippleReachException;
import com.ripplereach.ripplereach.models.Post;
import com.ripplereach.ripplereach.models.User;
import com.ripplereach.ripplereach.repositories.PostRepository;
import com.ripplereach.ripplereach.repositories.UserRepository;
import com.ripplereach.ripplereach.services.PostService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Post create(PostRequestDto postRequestDto) {
        try {
            User author = userRepository.findById(postRequestDto.getAuthorId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));

            Post post = Post.builder()
                    .title(postRequestDto.getTitle())
                    .content(postRequestDto.getContent())
                    .author(author)
                    .build();

            Post savedPost = postRepository.save(post);

            log.info("Post with id {} created successfully for user id {}",
                    savedPost.getId(), postRequestDto.getAuthorId());

            return savedPost;
        } catch (EntityNotFoundException ex) {
            log.error("User not found with id: {}", postRequestDto.getAuthorId());
            throw ex;
        } catch (RuntimeException ex) {
            log.error("Error while creating post for user id: {}", postRequestDto.getAuthorId());
            throw new RippleReachException("Error while creating post!");
        }
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
}
