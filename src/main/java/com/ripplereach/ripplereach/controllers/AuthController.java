package com.ripplereach.ripplereach.controllers;

import com.ripplereach.ripplereach.constants.Messages;
import com.ripplereach.ripplereach.dtos.*;
import com.ripplereach.ripplereach.mappers.Mapper;
import com.ripplereach.ripplereach.mappers.UserMapper;
import com.ripplereach.ripplereach.models.User;
import com.ripplereach.ripplereach.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(
    name = "Auth",
    description =
        "The Auth API. Contains all the operations that can be performed for authentication and"
            + " authorization.")
@AllArgsConstructor
public class AuthController {

  private final AuthService authService;
  private final UserMapper userMapper;
  private final Mapper<User, UserResponse> userResponseMapper;

  @PostMapping("/register")
  @Operation(
      summary = "User Registration",
      description = "API which lets a user create a fresh account if they didn't have one before.")
  public ResponseEntity<RegisterResponse> register(
      @Valid @RequestBody RegisterRequest registerRequest) {
    authService.verifyIdToken(registerRequest.getIdToken());
    User userRequestEntity = userMapper.mapToUser(registerRequest);

    User userResponseEntity = authService.register(userRequestEntity);
    AuthResponse authResponse = authService.generateAuthenticationToken(userResponseEntity);

    RegisterResponse registerResponse =
        RegisterResponse.builder()
            .message(Messages.USER_CREATED_SUCCESSFULLY)
            .user(userResponseMapper.mapTo(userResponseEntity))
            .auth(authResponse)
            .build();

    return new ResponseEntity<>(registerResponse, HttpStatus.OK);
  }

  @PostMapping("/login")
  @Operation(
      summary = "User Login",
      description = "API which lets a user log in with their credentials.")
  public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
    //    authService.verifyIdToken(loginRequest.getIdToken());
    LoginResponse loginResponse = authService.login(loginRequest);

    return new ResponseEntity<>(loginResponse, HttpStatus.OK);
  }

  @PostMapping("/refresh/token")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "Refresh Token",
      description = "API which generates a new auth token with the help of a refresh token.")
  public ResponseEntity<AuthResponse> refreshTokens(
      @Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
    AuthResponse authResponse = authService.refreshToken(refreshTokenRequest);

    return ResponseEntity.status(HttpStatus.OK).body(authResponse);
  }

  @PostMapping("/logout")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "User Logout",
      description =
          "API which lets users log out of their current session by invalidating their auth"
              + " tokens.")
  public ResponseEntity<String> logout(@Valid @RequestBody LogoutRequest logoutRequest) {
    authService.logout(logoutRequest);

    return ResponseEntity.status(HttpStatus.OK).body(Messages.LOGGED_OUT_SUCCESSFULLY);
  }
}
