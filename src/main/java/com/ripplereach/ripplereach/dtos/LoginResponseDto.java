package com.ripplereach.ripplereach.dtos;

import com.ripplereach.ripplereach.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {
    private String message;
    private User user;
    private AuthResponseDto auth;
}
