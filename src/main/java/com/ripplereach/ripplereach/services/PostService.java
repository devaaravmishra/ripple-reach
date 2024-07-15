package com.ripplereach.ripplereach.services;

import com.ripplereach.ripplereach.dtos.CommunityPostsResponse;
import com.ripplereach.ripplereach.dtos.PostRequest;
import com.ripplereach.ripplereach.dtos.PostResponse;
import com.ripplereach.ripplereach.models.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {
    PostResponse create(PostRequest postRequest);
    Page<PostResponse> findAll(String search, Pageable pageable);
    CommunityPostsResponse findAllByCommunity(Long communityId, String search, Pageable pageable);
    Page<PostResponse> findAllByAuthor(Long authorId, String search, Pageable pageable);
    PostResponse findById(Long postId);
    Post getPostById(Long postId);
    PostResponse update(Long postId, PostRequest postRequest);
    void delete(Long postId);
    void incrementUpvotes(Long postId);
    void decrementUpvotes(Long postId);
    void updatePostTotalComments(Post post);
}
