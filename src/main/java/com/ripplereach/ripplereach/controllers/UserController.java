package com.ripplereach.ripplereach.controllers;

import com.ripplereach.ripplereach.dtos.UserPartialUpdateRequest;
import com.ripplereach.ripplereach.dtos.UserResponse;
import com.ripplereach.ripplereach.dtos.UserUpdateRequest;
import com.ripplereach.ripplereach.mappers.Mapper;
import com.ripplereach.ripplereach.mappers.UserMapper;
import com.ripplereach.ripplereach.models.User;
import com.ripplereach.ripplereach.services.UserService;
import com.ripplereach.ripplereach.utilities.AvatarGenerator;
import com.ripplereach.ripplereach.utilities.UsernameGenerator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(
    name = "User",
    description = "The User API. Contains all the operations that can be performed on a user.")
@AllArgsConstructor
public class UserController {
  private final UserService userService;
  private final UserMapper userMapper;
  private final Mapper<User, UserResponse> userResponseMapper;

  @GetMapping("/generate-usernames")
  @Operation(summary = "Generate Usernames", description = "Generate a list of random usernames.")
  public ResponseEntity<List<String>> generateUsernames(
      @RequestParam(defaultValue = "10") int count) {
    List<String> usernames = UsernameGenerator.generateUsernames(count);
    return ResponseEntity.ok(usernames);
  }

  @GetMapping("/generate-avatar")
  @Operation(summary = "Generate Avatar", description = "Generates a random avatar.")
  public ResponseEntity<String> generateAvatar() {
    String avatar = AvatarGenerator.generateRandomAvatar();
    return ResponseEntity.ok(avatar);
  }

  @GetMapping("/{username}")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(summary = "Get User by Username", description = "Retrieves a user by their username.")
  public ResponseEntity<UserResponse> getUser(@PathVariable String username) {
    User userEntity = userService.findByUsername(username);
    UserResponse userResponse = userResponseMapper.mapTo(userEntity);

    return new ResponseEntity<>(userResponse, HttpStatus.OK);
  }

  @PutMapping("/{userId}")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(summary = "Update User", description = "Updates an existing user by their ID.")
  public ResponseEntity<UserResponse> updateUser(
      @PathVariable Long userId, @Valid @RequestBody UserUpdateRequest userUpdateRequest) {
    User userEntity = userMapper.mapToUser(userUpdateRequest);
    User userResponseEntity = userService.update(userId, userEntity);

    UserResponse userResponse = userResponseMapper.mapTo(userResponseEntity);
    return ResponseEntity.status(HttpStatus.OK).body(userResponse);
  }

  @PatchMapping(path = "/{userId}")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "Partial Update User",
      description = "Partially updates an existing user by their ID.")
  public ResponseEntity<UserResponse> partialUpdateUser(
      @PathVariable Long userId, @RequestBody UserPartialUpdateRequest userPartialUpdateRequest) {
    User userEntity = userMapper.mapToUser(userPartialUpdateRequest);
    User userResponseEntity = userService.partialUpdate(userId, userEntity);

    UserResponse userResponse = userResponseMapper.mapTo(userResponseEntity);
    return ResponseEntity.status(HttpStatus.OK).body(userResponse);
  }

  @DeleteMapping("/phone/{phone}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "Delete User by Phone",
      description = "Deletes an existing user by their phone number.")
  public void deleteUserByPhone(
      @PathVariable String phone, @RequestParam(defaultValue = "false") boolean hardDelete) {
    userService.deleteByPhone(phone, hardDelete);
  }

  @DeleteMapping("/username/{username}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "Delete User by Username",
      description = "Deletes an existing user by their username.")
  public void deleteUserByUsername(
      @PathVariable String username, @RequestParam(defaultValue = "false") boolean hardDelete) {
    userService.deleteByUsername(username, hardDelete);
  }

  @DeleteMapping("/{userId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(summary = "Delete User by ID", description = "Deletes an existing user by their ID.")
  public void deleteUserById(
      @PathVariable Long userId, @RequestParam(defaultValue = "false") boolean hardDelete) {
    userService.deleteById(userId, hardDelete);
  }
}
