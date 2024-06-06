package com.ripplereach.ripplereach.services;

import com.ripplereach.ripplereach.models.RefreshToken;

public interface RefreshTokenService {
    public RefreshToken generateRefreshToken();
    public void validateRefreshToken(String token);
    public void deleteRefreshToken(String token);
}
