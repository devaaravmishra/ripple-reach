package com.ripplereach.ripplereach.controllers;

import com.ripplereach.ripplereach.dtos.*;
import com.ripplereach.ripplereach.mappers.UserMapper;
import com.ripplereach.ripplereach.models.User;
import com.ripplereach.ripplereach.services.AuthService;
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
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> register(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        User userRequestEntity = userMapper.mapToUser(registerRequestDto);

        User userResponseEntity = authService.register(userRequestEntity);
        AuthResponseDto authResponseDto = authService.generateAuthenticationToken(userResponseEntity);

        RegisterResponseDto registerResponseDto = RegisterResponseDto
                .builder()
                .message("User created successfully")
                .user(userResponseEntity)
                .auth(authResponseDto)
                .build();

        return new ResponseEntity<>(registerResponseDto, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto loginResponseDto = authService.login(loginRequestDto);

        return new ResponseEntity<>(loginResponseDto, HttpStatus.OK);
    }

    @PostMapping("/refresh/token")
    public ResponseEntity<AuthResponseDto> refreshTokens(@Valid @RequestBody RefreshTokenRequestDto refreshTokenRequest) {
        AuthResponseDto authResponseDto = authService.refreshToken(refreshTokenRequest);

        return ResponseEntity.status(HttpStatus.OK).body(authResponseDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody LogoutRequestDto logoutRequestDto) {
        authService.logout(logoutRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body("Logged out successfully!!");
    }
}
