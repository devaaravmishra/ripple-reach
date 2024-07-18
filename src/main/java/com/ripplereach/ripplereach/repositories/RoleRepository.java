package com.ripplereach.ripplereach.repositories;

import com.ripplereach.ripplereach.enums.RoleName;
import com.ripplereach.ripplereach.models.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(RoleName roleName);
}
