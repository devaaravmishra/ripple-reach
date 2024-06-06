package com.ripplereach.ripplereach.repositories;

import com.ripplereach.ripplereach.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findUserByPhone(String phone);
    public Optional<User> findUserByUsername(String username);
    public void deleteUserByPhone(String phone);
}
