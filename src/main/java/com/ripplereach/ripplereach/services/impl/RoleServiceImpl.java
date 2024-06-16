package com.ripplereach.ripplereach.services.impl;

import com.ripplereach.ripplereach.enums.RoleName;
import com.ripplereach.ripplereach.models.Role;
import com.ripplereach.ripplereach.repositories.RoleRepository;
import com.ripplereach.ripplereach.services.RoleService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public Role createRole(String name) {
        if (roleRepository.findByName(name).isPresent()) {
            log.error("Role already exists with name: {}", name);
            throw new EntityExistsException("Role already exists with name: " + name);
        }

        Role role = Role.builder()
                .name(RoleName.valueOf(name))
                .build();

        return roleRepository.save(role);
    }

    @Override
    @Transactional
    public Role updateRole(Long roleId, String name) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("Role not found with id: " + roleId));

        role.setName(RoleName.valueOf(name));
        return roleRepository.save(role);
    }

    @Override
    @Transactional
    public void deleteRole(Long roleId) {
        if (!roleRepository.existsById(roleId)) {
            log.error("Role not found with id: {}", roleId);
            throw new EntityNotFoundException("Role not found with id: " + roleId);
        }

        roleRepository.deleteById(roleId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Role getRoleById(Long roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("Role not found with id: " + roleId));
    }

    @Override
    @Transactional(readOnly = true)
    public Role getRoleByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Role not found with name: " + name));
    }
}
