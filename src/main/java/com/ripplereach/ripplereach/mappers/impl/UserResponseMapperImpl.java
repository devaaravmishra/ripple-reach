package com.ripplereach.ripplereach.mappers.impl;

import com.ripplereach.ripplereach.dtos.UserResponseDto;
import com.ripplereach.ripplereach.mappers.Mapper;
import com.ripplereach.ripplereach.models.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserResponseMapperImpl implements Mapper<User, UserResponseDto> {
    private final ModelMapper modelMapper;

    @Override
    public User mapFrom(UserResponseDto userResponseDto) {
        return modelMapper.map(userResponseDto, User.class);
    }

    @Override
    public UserResponseDto mapTo(User user) {
        return modelMapper.map(user, UserResponseDto.class);
    }
}
