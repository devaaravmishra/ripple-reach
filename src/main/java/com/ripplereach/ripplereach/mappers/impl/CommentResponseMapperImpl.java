package com.ripplereach.ripplereach.mappers.impl;

import com.ripplereach.ripplereach.dtos.CommentResponse;
import com.ripplereach.ripplereach.mappers.Mapper;
import com.ripplereach.ripplereach.models.Comment;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CommentResponseMapperImpl implements Mapper<Comment, CommentResponse> {

  private ModelMapper modelMapper;

  @Override
  public Comment mapFrom(CommentResponse commentResponse) {
    return modelMapper.map(commentResponse, Comment.class);
  }

  @Override
  public CommentResponse mapTo(Comment comment) {
    return modelMapper.map(comment, CommentResponse.class);
  }
}
