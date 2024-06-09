package com.ripplereach.ripplereach.services;

import com.ripplereach.ripplereach.dtos.*;
import com.ripplereach.ripplereach.models.User;

public interface AuthService {
  User register(User user);

  LoginResponseDto login(LoginRequestDto loginRequestDto);

  void logout(LogoutRequestDto logoutRequestDto);

  AuthResponseDto generateAuthenticationToken(User user);

  AuthResponseDto refreshToken(RefreshTokenRequestDto refreshTokenRequestDto);

  String verifyIdToken(String idToken);
}
