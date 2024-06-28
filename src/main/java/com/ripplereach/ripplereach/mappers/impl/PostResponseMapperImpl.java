package com.ripplereach.ripplereach.mappers.impl;

import com.ripplereach.ripplereach.dtos.PostResponseDto;
import com.ripplereach.ripplereach.mappers.Mapper;
import com.ripplereach.ripplereach.models.Post;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PostResponseMapperImpl implements Mapper<Post, PostResponseDto> {

    private ModelMapper modelMapper;

    @Override
    public PostResponseDto mapTo(Post post) {
        return modelMapper.map(post, PostResponseDto.class);
    }

    @Override
    public Post mapFrom(PostResponseDto postResponseDto) {
        return modelMapper.map(postResponseDto, Post.class);
    }
}
