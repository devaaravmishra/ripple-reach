package com.ripplereach.ripplereach.services;

import com.ripplereach.ripplereach.enums.RoleName;
import com.ripplereach.ripplereach.models.Role;

import java.util.List;

public interface RoleService {
    Role createRole(String name);
    Role updateRole(Long roleId, String name);
    void deleteRole(Long roleId);
    List<Role> getAllRoles();
    Role getRoleById(Long roleId);
    Role getRoleByName(String name);
    Role createRoleIfNotExists(RoleName name);
}
