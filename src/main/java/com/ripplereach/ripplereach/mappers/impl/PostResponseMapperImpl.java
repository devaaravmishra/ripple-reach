package com.ripplereach.ripplereach.mappers.impl;

import com.ripplereach.ripplereach.dtos.PostResponse;
import com.ripplereach.ripplereach.mappers.Mapper;
import com.ripplereach.ripplereach.models.Post;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PostResponseMapperImpl implements Mapper<Post, PostResponse> {

    private ModelMapper modelMapper;

    @Override
    public PostResponse mapTo(Post post) {
        return modelMapper.map(post, PostResponse.class);
    }

    @Override
    public Post mapFrom(PostResponse postResponseDto) {
        return modelMapper.map(postResponseDto, Post.class);
    }
}
