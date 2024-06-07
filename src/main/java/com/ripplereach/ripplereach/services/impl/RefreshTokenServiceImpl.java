package com.ripplereach.ripplereach.services.impl;

import com.ripplereach.ripplereach.exceptions.RippleReachException;
import com.ripplereach.ripplereach.models.RefreshToken;
import com.ripplereach.ripplereach.repositories.RefreshTokenRepository;
import com.ripplereach.ripplereach.services.RefreshTokenService;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class RefreshTokenServiceImpl implements RefreshTokenService {

  private final RefreshTokenRepository refreshTokenRepository;

  @Override
  public RefreshToken generateRefreshToken() {
    try {
      RefreshToken refreshToken = new RefreshToken();
      refreshToken.setToken(UUID.randomUUID().toString());
      refreshToken.setCreatedDate(Instant.now());

      return refreshTokenRepository.save(refreshToken);
    } catch (RuntimeException ex) {
      log.error("Error while generating refresh token: ", ex);
      throw new RippleReachException("Error while generating refresh token");
    }
  }

  @Override
  public void validateRefreshToken(String token) {
    try {
      refreshTokenRepository
          .findByToken(token)
          .orElseThrow(() -> new EntityNotFoundException("Invalid refresh Token"));
    } catch (EntityNotFoundException ex) {
      throw ex;
    } catch (RuntimeException ex) {
      log.error("Error while validating refresh token: ", ex);
      throw new RippleReachException("Error while validating refresh token");
    }
  }

  @Override
  public RefreshToken findByToken(String token) {
    try {
      Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findByToken(token);

      if (refreshTokenOptional.isEmpty()) {
        throw new RuntimeException();
      }

      return refreshTokenOptional.get();
    } catch (RuntimeException ex) {
      throw new EntityNotFoundException("Invalid refresh token!");
    }
  }

  @Override
  public void deleteRefreshToken(String token) {
    try {
      refreshTokenRepository.deleteByToken(token);
      log.info("Refresh token deleted successfully");
    } catch (RuntimeException ex) {
      log.error("Error while deleting refresh token", ex);
      throw new RippleReachException("Error while deleting token");
    }
  }
}
