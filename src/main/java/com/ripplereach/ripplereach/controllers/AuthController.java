package com.ripplereach.ripplereach.controllers;

import com.ripplereach.ripplereach.dtos.*;
import com.ripplereach.ripplereach.mappers.UserMapper;
import com.ripplereach.ripplereach.models.User;
import com.ripplereach.ripplereach.services.AuthService;
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
  public ResponseEntity<RegisterResponseDto> register(
      @Valid @RequestBody RegisterRequestDto registerRequestDto) {
    authService.verifyIdToken(registerRequestDto.getIdToken());
    User userRequestEntity = userMapper.mapToUser(registerRequestDto);

    User userResponseEntity = authService.register(userRequestEntity);
    AuthResponseDto authResponseDto = authService.generateAuthenticationToken(userResponseEntity);

    RegisterResponseDto registerResponseDto =
        RegisterResponseDto.builder()
            .message("User created successfully")
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
  public ResponseEntity<AuthResponseDto> refreshTokens(
      @Valid @RequestBody RefreshTokenRequestDto refreshTokenRequest) {
    AuthResponseDto authResponseDto = authService.refreshToken(refreshTokenRequest);

    return ResponseEntity.status(HttpStatus.OK).body(authResponseDto);
  }

  @PostMapping("/logout")
  @SecurityRequirement(name = "Bearer Authentication")
  public ResponseEntity<String> logout(@Valid @RequestBody LogoutRequestDto logoutRequestDto) {
    authService.logout(logoutRequestDto);

    return ResponseEntity.status(HttpStatus.OK).body("Logged out successfully!!");
  }
}
