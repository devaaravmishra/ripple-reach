package com.ripplereach.ripplereach.repositories;

import com.ripplereach.ripplereach.models.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  public Optional<RefreshToken> findByToken(String token);

  public void deleteByToken(String token);
}
