package com.ripplereach.ripplereach.services.impl;

import com.ripplereach.ripplereach.enums.RoleName;
import com.ripplereach.ripplereach.exceptions.RippleReachException;
import com.ripplereach.ripplereach.models.Role;
import com.ripplereach.ripplereach.repositories.RoleRepository;
import com.ripplereach.ripplereach.services.RoleService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {
  private final RoleRepository roleRepository;

  @Override
  @Transactional
  public Role createRole(RoleName roleName) {
    try {
      Optional<Role> existingRole = roleRepository.findByName(roleName);
      if (existingRole.isPresent()) {
        return existingRole.get();
      }

      Role role = Role.builder().name(roleName).build();

      Role savedRole = roleRepository.save(role);

      log.info("Created role with id: {} and name: {}", savedRole.getId(), savedRole.getName());

      return savedRole;
    } catch (EntityExistsException ex) {
      log.error("Role creation failed. Role already exists with name: {}", roleName, ex);
      throw ex;
    } catch (RuntimeException ex) {
      log.error("Error while creating role with name: {}", roleName, ex);
      throw new RippleReachException("Error while creating role with name: " + roleName);
    }
  }

  @Override
  @Transactional
  public Role updateRole(Long roleId, String name) {
    try {
      Role role =
          roleRepository
              .findById(roleId)
              .orElseThrow(() -> new EntityNotFoundException("Role not found with id: " + roleId));

      role.setName(RoleName.valueOf(name));
      Role updatedRole = roleRepository.save(role);

      log.info("Updated role with id: {} to name: {}", updatedRole.getId(), updatedRole.getName());

      return updatedRole;
    } catch (EntityNotFoundException ex) {
      log.error("Role update failed. Role not found with id: {}", roleId, ex);
      throw ex;
    } catch (RuntimeException ex) {
      log.error("Error while updating role with id: {}", roleId, ex);
      throw new RippleReachException("Error while updating role with id: " + roleId);
    }
  }

  @Override
  @Transactional
  public void deleteRole(Long roleId) {
    try {
      if (!roleRepository.existsById(roleId)) {
        log.error("Role not found with id: {}", roleId);
        throw new EntityNotFoundException("Role not found with id: " + roleId);
      }

      roleRepository.deleteById(roleId);
      log.info("Deleted role with id: {}", roleId);
    } catch (EntityNotFoundException ex) {
      log.error("Role deletion failed. Role not found with id: {}", roleId, ex);
      throw ex;
    } catch (RuntimeException ex) {
      log.error("Error while deleting role with id: {}", roleId, ex);
      throw new RippleReachException("Error while deleting role with id: " + roleId);
    }
  }

  @Override
  @Transactional(readOnly = true)
  public List<Role> getAllRoles() {
    try {
      return roleRepository.findAll();
    } catch (RuntimeException ex) {
      log.error("Error while fetching all roles", ex);
      throw new RippleReachException("Error while fetching all roles");
    }
  }

  @Override
  @Transactional(readOnly = true)
  public Role getRoleById(Long roleId) {
    try {
      return roleRepository
          .findById(roleId)
          .orElseThrow(() -> new EntityNotFoundException("Role not found with id: " + roleId));
    } catch (EntityNotFoundException ex) {
      log.error("Role fetch failed. Role not found with id: {}", roleId, ex);
      throw ex;
    } catch (RuntimeException ex) {
      log.error("Error while fetching role with id: {}", roleId, ex);
      throw new RippleReachException("Error while fetching role with id: " + roleId);
    }
  }

  @Override
  @Transactional(readOnly = true)
  public Role getRoleByName(RoleName roleName) {
    try {
      return roleRepository
          .findByName(roleName)
          .orElseThrow(() -> new EntityNotFoundException("Role not found with name: " + roleName));
    } catch (EntityNotFoundException ex) {
      log.error("Role fetch failed. Role not found with name: {}", roleName, ex);
      throw ex;
    } catch (RuntimeException ex) {
      log.error("Error while fetching role with name: {}", roleName, ex);
      throw new RippleReachException("Error while fetching role with name: " + roleName);
    }
  }

  @Override
  @Transactional
  public Role createRoleIfNotExists(RoleName roleName) {
    try {
      Role role =
          roleRepository
              .findByName(roleName)
              .orElseGet(
                  () -> {
                    Role newRole = Role.builder().name(roleName).build();
                    return roleRepository.save(newRole);
                  });

      log.info(
          "Role with id: {}, name: {} is created or already exists", role.getId(), role.getName());

      return role;
    } catch (RuntimeException ex) {
      log.error("Error while creating or fetching role with name: {}", roleName, ex);
      throw new RippleReachException(
          "Error while creating or fetching role with name: " + roleName);
    }
  }
}
