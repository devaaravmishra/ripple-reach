package com.ripplereach.ripplereach.services.impl;

import com.ripplereach.ripplereach.dtos.*;
import com.ripplereach.ripplereach.exceptions.RippleReachException;
import com.ripplereach.ripplereach.models.RefreshToken;
import com.ripplereach.ripplereach.models.User;
import com.ripplereach.ripplereach.security.JwtProvider;
import com.ripplereach.ripplereach.services.AuthService;
import com.ripplereach.ripplereach.services.RefreshTokenService;
import com.ripplereach.ripplereach.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserService userService;
  private final RefreshTokenService refreshTokenService;
  private final FirebaseAuthServiceImpl firebaseAuthService;
  private final JwtProvider jwtProvider;
  private final AuthenticationManager authenticationManager;

  @Override
  @Transactional
  public User register(User user) {
      return userService.create(user);
  }

  @Override
  @Transactional
  public LoginResponseDto login(LoginRequestDto loginRequestDto) {
      User userEntity = userService.findByPhone(loginRequestDto.getPhone());
      AuthResponseDto authResponseDto = generateAuthenticationToken(userEntity);

      return LoginResponseDto.builder()
          .message("Success")
          .user(userEntity)
          .auth(authResponseDto)
          .build();
  }

  @Override
  @Transactional
  public void logout(LogoutRequestDto logoutRequestDto) {
    RefreshToken refreshToken = refreshTokenService
            .findByToken(logoutRequestDto.getRefreshToken());

    refreshTokenService.deleteRefreshToken(refreshToken.getToken());
  }

  @Override
  @Transactional(readOnly = true)
  public String verifyIdToken(String idToken) {
    return firebaseAuthService.verifyIdToken(idToken);
  }

  @Override
  @Transactional
  public AuthResponseDto generateAuthenticationToken(User user) {
    log.info("Attempting to authenticate user with phone: {}", user.getPhone());

    try {
      Authentication authenticate = authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(user.getPhone(), user.getPhone()));

      SecurityContextHolder.getContext().setAuthentication(authenticate);

      String token = jwtProvider.generateToken(authenticate);
      RefreshToken refreshToken = refreshTokenService.generateRefreshToken();

      return AuthResponseDto.builder()
              .token(token)
              .refreshToken(refreshToken.getToken())
              .expiresAt(Instant.ofEpochSecond(jwtProvider.getJwtExpirationInMillis()))
              .username(user.getUsername())
              .build();
    } catch (Exception e) {
      log.error("Authentication failed: {}", e.getMessage());
      throw new RippleReachException("Authentication failed", e);
    }
  }

  @Override
  public Authentication getAuthenticatedUser() {
    try {
      return SecurityContextHolder.getContext().getAuthentication();
    } catch (RuntimeException ex) {
      log.error("Error while getting authenticated user");
      throw new RippleReachException("Error while getting authenticated user");
    }
  }

  @Override
  @Transactional(readOnly = true)
  public AuthResponseDto refreshToken(RefreshTokenRequestDto refreshTokenRequest) {
    try {
      Authentication authentication = SecurityContextHolder
              .getContext()
              .getAuthentication();

      refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
      String token = jwtProvider.generateToken(authentication);

      return AuthResponseDto.builder()
          .token(token)
          .refreshToken(refreshTokenRequest.getRefreshToken())
          .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
          .username(refreshTokenRequest.getUsername())
          .build();
    } catch (EntityNotFoundException ex) {
      throw ex;
    } catch (RuntimeException ex) {
      throw new RippleReachException(ex.getMessage());
    }
  }

  public boolean isLoggedIn() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return !(authentication instanceof AnonymousAuthenticationToken)
        && authentication.isAuthenticated();
  }
}
