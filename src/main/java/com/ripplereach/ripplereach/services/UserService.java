package com.ripplereach.ripplereach.services;

import com.ripplereach.ripplereach.dtos.UserResponse;
import com.ripplereach.ripplereach.models.User;
import java.util.Set;

public interface UserService {
  UserResponse findByUsername(String username);

  boolean existsByUsername(String username);

  Set<String> findExistingUsernames(Set<String> usernames);

  UserResponse findByPhone(String phone);

  UserResponse create(User user);

  UserResponse findById(Long userId);

  UserResponse update(Long userId, User user);

  UserResponse partialUpdate(Long id, User user);

  User getUserByPhone(String phone);

  User getUserByUsername(String username);

  User getUserById(Long id);

  void deleteByUsername(String username, boolean hardDelete);

  void deleteById(Long userId, boolean hardDelete);

  void deleteByPhone(String phone, boolean hardDelete);
}
