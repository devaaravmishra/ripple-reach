package com.ripplereach.ripplereach.controllers;

import com.ripplereach.ripplereach.constants.Messages;
import com.ripplereach.ripplereach.dtos.*;
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
@Tag(name = "Auth", description = "The Auth API. Contains all the operations that can be performed for authentication and authorization.")
@AllArgsConstructor
public class AuthController {

  private final AuthService authService;
  private final UserMapper userMapper;

  @PostMapping("/register")
  @Operation(description = "API which let user create a fresh account if they didn't have one before.")
  public ResponseEntity<RegisterResponseDto> register(
      @Valid @RequestBody RegisterRequestDto registerRequestDto) {
    authService.verifyIdToken(registerRequestDto.getIdToken());
    User userRequestEntity = userMapper.mapToUser(registerRequestDto);

    User userResponseEntity = authService.register(userRequestEntity);
    AuthResponseDto authResponseDto = authService.generateAuthenticationToken(userResponseEntity);

    RegisterResponseDto registerResponseDto =
        RegisterResponseDto.builder()
            .message(Messages.USER_CREATED_SUCCESSFULLY)
            .user(userResponseEntity)
            .auth(authResponseDto)
            .build();

    return new ResponseEntity<>(registerResponseDto, HttpStatus.OK);
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponseDto> login(
      @Valid @RequestBody LoginRequestDto loginRequestDto) {
    authService.verifyIdToken(loginRequestDto.getIdToken());
    LoginResponseDto loginResponseDto = authService.login(loginRequestDto);

    return new ResponseEntity<>(loginResponseDto, HttpStatus.OK);
  }

  @PostMapping("/refresh/token")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(description = "API which generates new auth token with the help of refresh token.")
  public ResponseEntity<AuthResponseDto> refreshTokens(
      @Valid @RequestBody RefreshTokenRequestDto refreshTokenRequest) {
    AuthResponseDto authResponseDto = authService.refreshToken(refreshTokenRequest);

    return ResponseEntity.status(HttpStatus.OK).body(authResponseDto);
  }

  @PostMapping("/logout")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(description = "API which let users log out of their current session by invalidating their auth tokens.")
  public ResponseEntity<String> logout(@Valid @RequestBody LogoutRequestDto logoutRequestDto) {
    authService.logout(logoutRequestDto);

    return ResponseEntity.status(HttpStatus.OK).body(Messages.LOGGED_OUT_SUCCESSFULLY);
  }
}
