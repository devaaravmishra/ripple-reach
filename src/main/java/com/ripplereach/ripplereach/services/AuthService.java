package com.ripplereach.ripplereach.services;

import com.ripplereach.ripplereach.dtos.*;
import com.ripplereach.ripplereach.models.User;
import org.springframework.security.core.Authentication;

public interface AuthService {
  RegisterResponse register(User user);

  LoginResponse login(LoginRequest loginRequest);

  void logout(LogoutRequest logoutRequest);

  AuthResponse generateAuthenticationToken(User user);

  Authentication getAuthenticatedUser();

  User getCurrentUser();

  AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

  String verifyIdToken(String idToken);

  boolean isLoggedIn();
}
