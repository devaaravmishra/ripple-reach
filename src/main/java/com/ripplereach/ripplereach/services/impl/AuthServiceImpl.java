package com.ripplereach.ripplereach.services.impl;

import com.ripplereach.ripplereach.dtos.*;
import com.ripplereach.ripplereach.exceptions.RippleReachException;
import com.ripplereach.ripplereach.models.RefreshToken;
import com.ripplereach.ripplereach.models.User;
import com.ripplereach.ripplereach.security.JwtProvider;
import com.ripplereach.ripplereach.services.AuthService;
import com.ripplereach.ripplereach.services.RefreshTokenService;
import com.ripplereach.ripplereach.services.UserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserService userService;
  private final RefreshTokenService refreshTokenService;
  private final FirebaseAuthServiceImpl firebaseAuthService;
  private final JwtProvider jwtProvider;

  @Override
  @Transactional
  public User register(User user) {
    try {
      return userService.create(user);
    } catch (EntityExistsException ex) {
      throw ex;
    } catch (RuntimeException ex) {
      log.error("Error registering user: ", ex);
      throw new RippleReachException("Error occurred while registering user", ex);
    }
  }

  @Override
  @Transactional
  public LoginResponseDto login(LoginRequestDto loginRequestDto) {
    try {
      User userEntity = userService.findByPhone(loginRequestDto.getPhone());
      AuthResponseDto authResponseDto = generateAuthenticationToken(userEntity);

      return LoginResponseDto.builder()
          .message("Success")
          .user(userEntity)
          .auth(authResponseDto)
          .build();
    } catch (RuntimeException ex) {
      log.error("Error while authenticating user");
      throw new RippleReachException("Error while authenticating user");
    }
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
    try {
      String token = jwtProvider.generateTokenWithUserName(user.getUsername());
      RefreshToken refreshToken = refreshTokenService.generateRefreshToken();

      return AuthResponseDto.builder()
          .token(token)
          .refreshToken(refreshToken.getToken())
          .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
          .username(user.getUsername())
          .build();
    } catch (RuntimeException ex) {
      throw new RippleReachException("Error while generating auth token");
    }
  }

  @Override
  @Transactional(readOnly = true)
  public AuthResponseDto refreshToken(RefreshTokenRequestDto refreshTokenRequest) {
    try {
      refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
      String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUsername());

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
