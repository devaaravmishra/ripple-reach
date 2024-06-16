package com.ripplereach.ripplereach.controllers;

import com.ripplereach.ripplereach.constants.Messages;
import com.ripplereach.ripplereach.dtos.UserPartialUpdateRequestDto;
import com.ripplereach.ripplereach.dtos.UserResponseDto;
import com.ripplereach.ripplereach.dtos.UserUpdateRequestDto;
import com.ripplereach.ripplereach.mappers.Mapper;
import com.ripplereach.ripplereach.mappers.UserMapper;
import com.ripplereach.ripplereach.models.User;
import com.ripplereach.ripplereach.services.UserService;
import com.ripplereach.ripplereach.utilities.AvatarGenerator;
import com.ripplereach.ripplereach.utilities.UsernameGenerator;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User", description = "The User API. Contains all the operations that can be performed on a user.")
@AllArgsConstructor
public class UserController {
  private final UserService userService;
  private final UserMapper userMapper;
  private final Mapper<User, UserResponseDto> userResponseMapper;

  @GetMapping("/generate-usernames")
  @Operation(
          summary = "Generate Usernames",
          description = "Generate a list of random usernames."
  )
  public ResponseEntity<List<String>> generateUsernames(
      @RequestParam(defaultValue = "10") int count) {
    List<String> usernames = UsernameGenerator.generateUsernames(count);
    return ResponseEntity.ok(usernames);
  }

  @GetMapping("/generate-avatar")
  @Operation(
          summary = "Generate Avatar",
          description = "Generates a random avatar."
  )
  public ResponseEntity<String> generateAvatar() {
    String avatar = AvatarGenerator.generateRandomAvatar();
    return ResponseEntity.ok(avatar);
  }

  @GetMapping("/{username}")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
          summary = "Get User by Username",
          description = "Retrieves a user by their username."
  )
  public ResponseEntity<UserResponseDto> getUser(@PathVariable String username) {
    User userEntity = userService.findByUsername(username);
    UserResponseDto userResponseDto = userResponseMapper.mapTo(userEntity);

    return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
  }

  @PutMapping("/{userId}")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
          summary = "Update User",
          description = "Updates an existing user by their ID."
  )
  public ResponseEntity<UserResponseDto> updateUser
          (@PathVariable Long userId, @Valid @RequestBody UserUpdateRequestDto userUpdateRequestDto) {
    User userEntity = userMapper.mapToUser(userUpdateRequestDto);
    User userResponseEntity = userService.update(userId, userEntity);

    UserResponseDto userResponseDto = userResponseMapper.mapTo(userResponseEntity);
    return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
  }

  @PatchMapping(path = "/{userId}")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
          summary = "Partial Update User",
          description = "Partially updates an existing user by their ID."
  )
  public ResponseEntity<UserResponseDto> partialUpdateUser
          (@PathVariable Long userId, @RequestBody UserPartialUpdateRequestDto userPartialUpdateRequestDto) {
    User userEntity = userMapper.mapToUser(userPartialUpdateRequestDto);
    User userResponseEntity = userService.partialUpdate(userId, userEntity);

    UserResponseDto userResponseDto = userResponseMapper.mapTo(userResponseEntity);
    return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
  }

  @DeleteMapping("/{phone}")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
          summary = "Delete User by Phone",
          description = "Deletes an existing user by their phone number."
  )
  public ResponseEntity<String> deleteUserByPhone(@PathVariable String phone) {
    userService.deleteByPhone(phone);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Messages.USER_DELETED_SUCCESSFULLY);
  }

  @DeleteMapping("/{username}")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
          summary = "Delete User by Username",
          description = "Deletes an existing user by their username."
  )
  public ResponseEntity<String> deleteUserByUsername(@PathVariable String username) {
    userService.deleteByUsername(username);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Messages.USER_DELETED_SUCCESSFULLY);
  }

  @DeleteMapping("/{userId}")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
          summary = "Delete User by ID",
          description = "Deletes an existing user by their ID."
  )
  public ResponseEntity<String> deleteUserById(@PathVariable Long userId) {
    userService.deleteByUserId(userId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
