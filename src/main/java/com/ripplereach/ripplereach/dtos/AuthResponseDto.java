package com.ripplereach.ripplereach.dtos;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDto {
  private String token;
  private String refreshToken;
  private Instant expiresAt;
  private String username;
}
