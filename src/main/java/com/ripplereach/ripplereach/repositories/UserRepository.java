package com.ripplereach.ripplereach.repositories;

import com.ripplereach.ripplereach.models.User;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByPhone(String phone);

  @Query("SELECT u.username FROM User u WHERE u.username IN :usernames")
  Set<String> findExistingUsernames(@Param("usernames") Set<String> usernames);

  Optional<User> findByUsername(String username);
}
