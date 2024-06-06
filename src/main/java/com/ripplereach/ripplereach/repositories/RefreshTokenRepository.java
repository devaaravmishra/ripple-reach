package com.ripplereach.ripplereach.repositories;

import com.ripplereach.ripplereach.models.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    public Optional<RefreshToken> findByToken(String token);

    public void deleteByToken(String token);
}