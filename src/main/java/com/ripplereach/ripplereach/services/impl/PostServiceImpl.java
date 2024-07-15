package com.ripplereach.ripplereach.services.impl;

import com.ripplereach.ripplereach.dtos.*;
import com.ripplereach.ripplereach.exceptions.RippleReachException;
import com.ripplereach.ripplereach.mappers.Mapper;
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
    private final UpvoteRepository upvoteRepository;
    private final UserService userService;
    private final AuthService authService;
    private final CommunityService communityService;
    private final ImageService imageService;
    private final Mapper<Post, PostResponse> postResponseMapper;
    private final Mapper<Post, PostResponseByCommunity> postCommunityResponseMapper;
    private final Mapper<Community, CommunityResponse> communityResponseMapper;

    @Override
    @Transactional
    public PostResponse create(PostRequest postRequest) {
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
                    .title(postRequest.getTitle().trim())
                    .content(postRequest.getContent().trim())
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

            PostResponse postResponse = postResponseMapper.mapTo(savedPost);
            postResponse.setUpvotedByUser(isUpvotedByLoggedInUser(savedPost.getId()));

            return postResponse;
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
    public Page<PostResponse> findAll(String search, Pageable pageable) {
        try {
            Specification<Post> spec = Specification.where(null);
            
            search = removeAllWhitespace(search);
            if (!search.isEmpty()) {
                spec = spec.and(PostSpecification.containsTextInTitleOrContentOrCommunity(search));
            }

            return postRepository.findAll(spec, pageable).map(post -> {
                PostResponse postResponse = postResponseMapper.mapTo(post);
                boolean isUpvoted = isUpvotedByLoggedInUser(post.getId());
                System.out.println("is upvoted: " + isUpvoted);
                postResponse.setUpvotedByUser(isUpvoted);
                return postResponse;
            });
        } catch (RuntimeException ex) {
            log.error("Error while retrieving all posts", ex);
            throw new RippleReachException("Error while retrieving all posts!");
        }
    }
    
    @Override
    public CommunityPostsResponse findAllByCommunity(Long communityId, String search, Pageable pageable) {
        try {
            Specification<Post> spec = Specification.where((root, query, cb) ->
                    cb.equal(root.get("community").get("id"), communityId)
            );

            search = removeAllWhitespace(search);
            if (!search.isEmpty()) {
                spec = spec.and(PostSpecification.containsTextInTitleOrContentOrCommunity(search));
            }

            Page<PostResponseByCommunity> posts = postRepository.findAll(spec, pageable)
                    .map(post -> {
                        PostResponseByCommunity postResponse = postCommunityResponseMapper.mapTo(post);
                        postResponse.setUpvotedByUser(isUpvotedByLoggedInUser(post.getId()));
                        return postResponse;
                    });

            Community community = communityService.findById(communityId);

            return CommunityPostsResponse
                    .builder()
                    .posts(posts)
                    .community(communityResponseMapper.mapTo(community))
                    .build();

        } catch (RuntimeException ex) {
            log.error("Error while retrieving all posts");
            throw new RippleReachException("Error while retrieving all posts!");
        }
    }

    @Override
    public Page<PostResponse> findAllByAuthor(Long authorId, String search, Pageable pageable) {
        try {
            Specification<Post> spec = Specification.where((root, query, cb) ->
                    cb.equal(root.get("author").get("id"), authorId)
            );

            search = removeAllWhitespace(search);
            if (!search.isEmpty()) {
                spec = spec.and(PostSpecification.containsTextInTitleOrContentOrCommunity(search));
            }

            return postRepository.findAll(spec, pageable)
                    .map(post -> {
                        PostResponse postResponse = postResponseMapper.mapTo(post);
                        postResponse.setUpvotedByUser(isUpvotedByLoggedInUser(post.getId()));
                        return postResponse;
                    });
        } catch (RuntimeException ex) {
            log.error("Error while retrieving all posts");
            throw new RippleReachException("Error while retrieving all posts!");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PostResponse findById(Long postId) {
        try {
            Post post = getPostById(postId);

            return postResponseMapper.mapTo(post);
        } catch (EntityNotFoundException ex) {
            log.error("Post not found with id: {}", postId);
            throw ex;
        } catch (RuntimeException ex) {
            throw new RippleReachException(ex.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Post getPostById(Long postId) {
        try {
            return postRepository.findById(postId)
                    .orElseThrow(() -> new EntityNotFoundException("Post not found"));
        } catch (EntityNotFoundException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            log.error("Error while retrieving post with id: {}", postId);
            throw new RippleReachException("Error while retrieving post with id:" + postId);
        }
    }

    @Override
    @Transactional
    public PostResponse update(Long postId, PostRequest postRequest) {
        try {
            Post post = getPostById(postId);

            // check whether the authenticated user is updating the post
            Jwt principal = (Jwt) authService.getAuthenticatedUser().getPrincipal();
            if (!principal.getSubject().equals(post.getAuthor().getPhone())) {
                log.error("Unable to update the post, access forbidden attempted by userId: {}",
                        postRequest.getAuthorId());
                throw new AccessDeniedException("Unable to update the post, access forbidden");
            }

            post.setTitle(postRequest.getTitle());
            post.setContent(postRequest.getContent());

            Post updatedPost = postRepository.save(post);

            log.info("Post with id {} updated successfully", postId);

            PostResponse postResponse = postResponseMapper.mapTo(updatedPost);
            postResponse.setUpvotedByUser(isUpvotedByLoggedInUser(updatedPost.getId()));

            return postResponse;
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

    @Override
    @Transactional
    public void incrementUpvotes(Long postId) {
        try {
            Post post = getPostById(postId);
            post.setTotalUpvotes(post.getTotalUpvotes() + 1);
            postRepository.save(post);
        } catch (EntityNotFoundException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            log.error("Unable to upvote the post with id: {}", postId, ex);
            throw new RippleReachException("Unable to upvote the post with id: " + postId);
        }
    }

    @Override
    @Transactional
    public void decrementUpvotes(Long postId) {
        try {
            Post post = getPostById(postId);
            post.setTotalUpvotes(Math.max(post.getTotalUpvotes() - 1, 0));
            postRepository.save(post);
        } catch (EntityNotFoundException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            log.error("Unable to remove upvote from the post with id: {}", postId, ex);
            throw new RippleReachException("Unable to remove upvote from the post with id: " + postId);
        }
    }

    private boolean isUpvotedByLoggedInUser(Long targetId) {
        try {
            if (!authService.isLoggedIn()) {
                return false;
            }

            User currentUser = authService.getCurrentUser();
            System.out.println("current user id: " + currentUser.getId() + " post id: " + targetId);

            return upvoteRepository.existsByUserIdAndPostId(currentUser.getId(), targetId);
        } catch (EntityNotFoundException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            throw new RippleReachException(ex.getMessage());
        }
    }

    private static String removeAllWhitespace(String search) {
        return search.replaceAll("[\\p{Z}\\s\"']", "");
    }
}
