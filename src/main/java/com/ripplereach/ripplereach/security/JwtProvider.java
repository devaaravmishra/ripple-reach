package com.ripplereach.ripplereach.security;

import java.time.Instant;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtProvider {

  private final JwtEncoder jwtEncoder;

  @Getter
  @Value("${jwt.expiration.time}")
  private Long jwtExpirationInMillis;

  @Value("${spring.application.name}")
  private String issuer;

  public String generateToken(Authentication authentication) {
    User principal = (User) authentication.getPrincipal();
    return generateTokenWithUserName(principal.getUsername(), principal.getAuthorities());
  }

  public String generateTokenWithUserName(
      String username, Collection<? extends GrantedAuthority> authorities) {
    String roles =
        authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

    JwtClaimsSet claims =
        JwtClaimsSet.builder()
            .issuer(issuer)
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plusMillis(jwtExpirationInMillis))
            .subject(username)
            .claim("scope", roles)
            .claim("username", username)
            .build();

    return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
  }
}
