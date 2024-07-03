package com.ripplereach.ripplereach.services;

import com.ripplereach.ripplereach.dtos.PostRequest;
import com.ripplereach.ripplereach.models.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {
    Post create(PostRequest postRequest);
    Page<Post> findAll(Pageable pageable);
    Page<Post> findAllByCommunity(Long communityId, Pageable pageable);
    Post findById(Long postId);
    Post update(Long postId, PostRequest postRequest);
    void delete(Long postId);
    void incrementUpvotes(Long postId);
    void decrementUpvotes(Long postId);
    void updatePostTotalComments(Post post);
}
