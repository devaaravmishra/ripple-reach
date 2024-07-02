package com.ripplereach.ripplereach.mappers.impl;

import com.ripplereach.ripplereach.dtos.UserResponse;
import com.ripplereach.ripplereach.mappers.Mapper;
import com.ripplereach.ripplereach.models.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserResponseMapperImpl implements Mapper<User, UserResponse> {
    private final ModelMapper modelMapper;

    @Override
    public User mapFrom(UserResponse userResponse) {
        return modelMapper.map(userResponse, User.class);
    }

    @Override
    public UserResponse mapTo(User user) {
        return modelMapper.map(user, UserResponse.class);
    }
}
