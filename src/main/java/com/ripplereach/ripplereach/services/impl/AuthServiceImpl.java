package com.ripplereach.ripplereach.services.impl;

import com.ripplereach.ripplereach.dtos.*;
import com.ripplereach.ripplereach.exceptions.RippleReachException;
import com.ripplereach.ripplereach.models.Company;
import com.ripplereach.ripplereach.models.RefreshToken;
import com.ripplereach.ripplereach.models.University;
import com.ripplereach.ripplereach.models.User;
import com.ripplereach.ripplereach.repositories.CompanyRepository;
import com.ripplereach.ripplereach.repositories.UniversityRepository;
import com.ripplereach.ripplereach.repositories.UserRepository;
import com.ripplereach.ripplereach.security.JwtProvider;
import com.ripplereach.ripplereach.services.AuthService;
import com.ripplereach.ripplereach.services.RefreshTokenService;
import com.ripplereach.ripplereach.utilities.HashUtils;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.Optional;
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

  private final UserRepository userRepository;
  private final CompanyRepository companyRepository;
  private final UniversityRepository universityRepository;
  private final RefreshTokenService refreshTokenService;
  private final JwtProvider jwtProvider;

  @Transactional
  @Override
  public User register(User user) {
    try {
      String oneWayPhoneHex = HashUtils.generateHash(user.getPhone());

      // Check if this user already exists or not
      Optional<User> existingUser =
          userRepository
              .findUserByPhone(oneWayPhoneHex)
              .or(() -> userRepository.findUserByUsername(user.getUsername()));

      if (existingUser.isPresent()) {
        log.info("User with phone ${} already exists", oneWayPhoneHex);
        throw new EntityExistsException("User with these credentials already exists");
      }

      // Now check whether the user idToken is a valid Firebase IdToken (This means this user is
      // verified)
      //
      //

      user.setPhone(oneWayPhoneHex);

      if (user.getCompany() != null) {
        Optional<Company> company = companyRepository.findByName(user.getCompany().getName());

        company.ifPresent(user::setCompany);

        if (company.isEmpty()) {
          user.getCompany().setCreatedAt(Instant.now());
          companyRepository.save(user.getCompany());
        }

        // Set the user's profession if he/she is working somewhere
        user.setProfession(user.getProfession());
      } else if (user.getUniversity().getName() != null) {
        Optional<University> university =
            universityRepository.findByName(user.getUniversity().getName());

        university.ifPresent(user::setUniversity);

        if (university.isEmpty()) {
          user.getUniversity().setCreatedAt(Instant.now());
          universityRepository.save(user.getUniversity());
        }
      }

      user.setIsVerified(false);
      user.setCreatedAt(Instant.now());

      return userRepository.save(user);
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
      // Turn the phone number into one way hash
      String oneWayPhoneHex = HashUtils.generateHash(loginRequestDto.getPhone());

      // find whether we have user with these credentials
      Optional<User> userOptional = userRepository.findUserByPhone(oneWayPhoneHex);

      if (userOptional.isEmpty()) {
        log.info("User with these credentials does not exists!");
        throw new EntityNotFoundException("User with these credentials does not exists!");
      }

      AuthResponseDto authResponseDto = generateAuthenticationToken(userOptional.get());

      return LoginResponseDto.builder()
          .message("Success")
          .user(userOptional.get())
          .auth(authResponseDto)
          .build();
    } catch (EntityNotFoundException ex) {
      throw ex;
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
