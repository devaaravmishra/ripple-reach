package com.ripplereach.ripplereach.services.impl;

import com.ripplereach.ripplereach.constants.Messages;
import com.ripplereach.ripplereach.dtos.*;
import com.ripplereach.ripplereach.exceptions.RippleReachException;
import com.ripplereach.ripplereach.mappers.Mapper;
import com.ripplereach.ripplereach.models.RefreshToken;
import com.ripplereach.ripplereach.models.User;
import com.ripplereach.ripplereach.security.JwtProvider;
import com.ripplereach.ripplereach.services.AuthService;
import com.ripplereach.ripplereach.services.RefreshTokenService;
import com.ripplereach.ripplereach.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
  private final Mapper<User, UserResponse> userResponseMapper;
  private final RefreshTokenService refreshTokenService;
  private final FirebaseAuthServiceImpl firebaseAuthService;
  private final JwtProvider jwtProvider;
  private final AuthenticationManager authenticationManager;

  @Override
  @Transactional
  public RegisterResponse register(User user) {
    UserResponse userResponse = userService.create(user);

    User UserEntity = userResponseMapper.mapFrom(userResponse);
    AuthResponse authResponse = generateAuthenticationToken(UserEntity);

    return RegisterResponse.builder()
        .message(Messages.USER_CREATED_SUCCESSFULLY)
        .user(userResponse)
        .auth(authResponse)
        .build();
  }

  @Override
  @Transactional
  public LoginResponse login(LoginRequest loginRequest) {
    UserResponse userResponse = userService.findByPhone(loginRequest.getPhone());

    User UserEntity = userResponseMapper.mapFrom(userResponse);
    AuthResponse authResponse = generateAuthenticationToken(UserEntity);

    return LoginResponse.builder().message("Success").user(userResponse).auth(authResponse).build();
  }

  @Override
  @Transactional
  public void logout(LogoutRequest logoutRequest) {
    RefreshToken refreshToken = refreshTokenService.findByToken(logoutRequest.getRefreshToken());

    refreshTokenService.deleteRefreshToken(refreshToken.getToken());
  }

  @Override
  @Transactional(readOnly = true)
  public String verifyIdToken(String idToken) {
    return firebaseAuthService.verifyIdToken(idToken);
  }

  @Override
  @Transactional
  public AuthResponse generateAuthenticationToken(User user) {
    log.info("Attempting to authenticate user with phone: {}", user.getPhone());

    try {
      Authentication authenticate =
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(user.getPhone(), user.getPhone()));

      SecurityContextHolder.getContext().setAuthentication(authenticate);

      String token = jwtProvider.generateToken(authenticate);
      RefreshToken refreshToken = refreshTokenService.generateRefreshToken();

      return AuthResponse.builder()
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
      return getAuthentication();
    } catch (RuntimeException ex) {
      log.error("Error while getting authenticated user");
      throw new RippleReachException("Error while getting authenticated user");
    }
  }

  @Override
  public User getCurrentUser() {
    try {
      Authentication authentication = getAuthentication();
      return userService.getUserByPhone(authentication.getName());
    } catch (EntityNotFoundException ex) {
      throw ex;
    } catch (RuntimeException ex) {
      log.error("Error while fetching logged-in user", ex);
      throw new RippleReachException("Error while fetching logged-in user!");
    }
  }

  private static Authentication getAuthentication() {
    return SecurityContextHolder.getContext().getAuthentication();
  }

  @Override
  @Transactional
  public AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
    try {
      refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());

      String username =
          Optional.ofNullable(refreshTokenRequest.getUsername()).map(String::trim).orElse("");

      User existingUser =
          username.isEmpty()
              ? userService.getUserByPhone(getAuthenticatedUser().getName())
              : userService.getUserByUsername(username);

      return generateAuthenticationToken(existingUser);
    } catch (EntityNotFoundException ex) {
      throw ex;
    } catch (RuntimeException ex) {
      log.error(
          "Error generating token for refreshToken: {} {}",
          refreshTokenRequest.getRefreshToken(),
          ex.getMessage());
      throw new RippleReachException(
          "Error generating token for refreshToken: " + refreshTokenRequest.getRefreshToken());
    }
  }

  @Override
  public boolean isLoggedIn() {
    Authentication authentication = getAuthentication();
    return !(authentication instanceof AnonymousAuthenticationToken)
        && authentication.isAuthenticated();
  }
}
