package com.ripplereach.ripplereach.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
  private String message;
  private UserResponse user;
  private AuthResponse auth;
}
