package com.ripplereach.ripplereach.mappers.impl;

import com.ripplereach.ripplereach.dtos.PostResponseByCommunity;
import com.ripplereach.ripplereach.mappers.Mapper;
import com.ripplereach.ripplereach.models.Post;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PostResponseByCommunityMapperImpl implements Mapper<Post, PostResponseByCommunity> {
  private ModelMapper modelMapper;

  @Override
  public Post mapFrom(PostResponseByCommunity postResponseByCommunity) {
    return modelMapper.map(postResponseByCommunity, Post.class);
  }

  @Override
  public PostResponseByCommunity mapTo(Post post) {
    return modelMapper.map(post, PostResponseByCommunity.class);
  }
}
