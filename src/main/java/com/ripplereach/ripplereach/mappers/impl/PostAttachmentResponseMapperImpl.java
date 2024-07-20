package com.ripplereach.ripplereach.mappers.impl;

import com.ripplereach.ripplereach.dtos.PostAttachmentResponse;
import com.ripplereach.ripplereach.mappers.Mapper;
import com.ripplereach.ripplereach.models.PostAttachment;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PostAttachmentResponseMapperImpl
    implements Mapper<PostAttachment, PostAttachmentResponse> {

  private final ModelMapper modelMapper;

  @Override
  public PostAttachment mapFrom(PostAttachmentResponse postAttachmentResponse) {
    return modelMapper.map(postAttachmentResponse, PostAttachment.class);
  }

  @Override
  public PostAttachmentResponse mapTo(PostAttachment postAttachment) {
    return modelMapper.map(postAttachment, PostAttachmentResponse.class);
  }
}
