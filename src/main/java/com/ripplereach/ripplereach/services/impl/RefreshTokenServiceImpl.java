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

import java.time.Instant;
import java.util.UUID;

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
            refreshTokenRepository.findByToken(token)
                    .orElseThrow(() -> new EntityNotFoundException("Invalid refresh Token"));
        } catch (EntityNotFoundException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            log.error("Error while validating refresh token: ",  ex);
            throw new RippleReachException("Error while validating refresh token");
        }
    }

    @Override
    public void deleteRefreshToken(String token) {
        try {
            refreshTokenRepository.deleteByToken(token);
        } catch (RuntimeException ex) {
            log.error("Error while deleting refresh token", ex);
            throw new RippleReachException("Error while deleting token");
        }
    }
}
