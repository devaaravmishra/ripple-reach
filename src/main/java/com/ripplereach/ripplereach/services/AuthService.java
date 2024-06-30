package com.ripplereach.ripplereach.services;

import com.ripplereach.ripplereach.dtos.*;
import com.ripplereach.ripplereach.models.User;
import org.springframework.security.core.Authentication;

public interface AuthService {
  User register(User user);
  LoginResponseDto login(LoginRequestDto loginRequestDto);
  void logout(LogoutRequestDto logoutRequestDto);
  AuthResponseDto generateAuthenticationToken(User user);
  Authentication getAuthenticatedUser();
  AuthResponseDto refreshToken(RefreshTokenRequestDto refreshTokenRequestDto);
  String verifyIdToken(String idToken);
}
