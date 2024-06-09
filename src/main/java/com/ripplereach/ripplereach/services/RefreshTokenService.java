package com.ripplereach.ripplereach.services;

import com.ripplereach.ripplereach.models.RefreshToken;

public interface RefreshTokenService {
  RefreshToken generateRefreshToken();
  void validateRefreshToken(String token);
  RefreshToken findByToken(String token);
  void deleteRefreshToken(String token);
}
