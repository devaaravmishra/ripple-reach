package com.ripplereach.ripplereach.services;

import com.ripplereach.ripplereach.models.User;

public interface UserService {
    User findByUsername(String username);
    User findByPhone(String phone);
    User create(User user);
    User findByUserId(Long userId);
    User update(Long userId, User user);
    User partialUpdate(Long id, User user);
    void deleteByUsername(String username);
    void deleteByUserId(Long userId);
    void deleteByPhone(String phone);
}
