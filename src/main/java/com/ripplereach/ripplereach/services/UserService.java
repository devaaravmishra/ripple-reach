package com.ripplereach.ripplereach.services;

import com.ripplereach.ripplereach.models.User;
import java.util.Set;

public interface UserService {
  User findByUsername(String username);

  boolean existsByUsername(String username);

  Set<String> findExistingUsernames(Set<String> usernames);

  User findByPhone(String phone);

  User create(User user);

  User findById(Long userId);

  User update(Long userId, User user);

  User partialUpdate(Long id, User user);

  void deleteByUsername(String username, boolean hardDelete);

  void deleteById(Long userId, boolean hardDelete);

  void deleteByPhone(String phone, boolean hardDelete);
}
