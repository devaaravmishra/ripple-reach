package com.ripplereach.ripplereach.services.impl;

import com.ripplereach.ripplereach.exceptions.RippleReachException;
import com.ripplereach.ripplereach.models.RefreshToken;
import com.ripplereach.ripplereach.repositories.RefreshTokenRepository;
import com.ripplereach.ripplereach.services.RefreshTokenService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class RefreshTokenServiceImpl implements RefreshTokenService {

  private final RefreshTokenRepository refreshTokenRepository;

  @Override
  @Transactional
  public RefreshToken generateRefreshToken() {
    try {
      RefreshToken refreshToken = new RefreshToken();
      refreshToken.setToken(UUID.randomUUID().toString());

      return refreshTokenRepository.save(refreshToken);
    } catch (RuntimeException ex) {
      log.error("Error while generating refresh token: ", ex);
      throw new RippleReachException("Error while generating refresh token");
    }
  }

  @Override
  @Transactional(readOnly = true)
  public void validateRefreshToken(String token) {
    try {
      getRefreshToken(token);
    } catch (EntityNotFoundException ex) {
      throw ex;
    } catch (RuntimeException ex) {
      log.error("Error while validating refresh token: ", ex);
      throw new RippleReachException("Error while validating refresh token");
    }
  }

  @Override
  @Transactional(readOnly = true)
  public RefreshToken findByToken(String token) {
    try {
      return getRefreshToken(token);
    } catch (EntityNotFoundException ex) {
      log.error("Invalid refresh token: {}", token);
      throw ex;
    } catch (RuntimeException ex) {
      log.error("Error while fetching token: {}", token);
      throw new RippleReachException("Error while fetching token: " + token);
    }
  }

  @Override
  @Transactional
  public void deleteRefreshToken(String token) {
    try {
      String refreshToken = getRefreshToken(token).getToken();
      refreshTokenRepository.deleteByToken(refreshToken);
      log.info("Refresh token: {} deleted successfully", refreshToken);
    } catch (RuntimeException ex) {
      log.error("Error while deleting refresh token", ex);
      throw new RippleReachException("Error while deleting token");
    }
  }

  private RefreshToken getRefreshToken(String token) {
    Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken(token);

    if (refreshToken.isEmpty()) {
      log.error("Invalid refresh Token: {}", token);
      throw new EntityNotFoundException("Invalid refresh token: " + token);
    }

    return refreshToken.get();
  }
}
