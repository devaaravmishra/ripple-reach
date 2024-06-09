package com.ripplereach.ripplereach.repositories;

import com.ripplereach.ripplereach.models.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findUserByPhone(String phone);
  Optional<User> findUserByUserId(Long userId);
  Optional<User> findUserByUsername(String username);
}
