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
public class LoginResponse {
  private String message;
  private User user;
  private AuthResponse auth;
}
