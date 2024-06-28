package com.ripplereach.ripplereach.mappers.impl;

import com.ripplereach.ripplereach.dtos.CommentResponseDto;
import com.ripplereach.ripplereach.mappers.Mapper;
import com.ripplereach.ripplereach.models.Comment;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CommentResponseMapperImpl implements Mapper<Comment, CommentResponseDto> {

    private ModelMapper modelMapper;

    @Override
    public Comment mapFrom(CommentResponseDto commentResponseDto) {
        return modelMapper.map(commentResponseDto, Comment.class);
    }

    @Override
    public CommentResponseDto mapTo(Comment comment) {
        return modelMapper.map(comment, CommentResponseDto.class);
    }
}
