package com.ripplereach.ripplereach.services;

import com.ripplereach.ripplereach.dtos.PostRequestDto;
import com.ripplereach.ripplereach.models.Post;

import java.util.List;

public interface PostService {
    Post create(PostRequestDto postRequestDto);
    List<Post> getAll();
    Post findById(Long postId);
    Post update(Long postId, PostRequestDto postRequestDto);
    void delete(Long postId);
    void updatePostTotalComments(Post post);
}
