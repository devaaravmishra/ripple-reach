package com.ripplereach.ripplereach.services;

import com.ripplereach.ripplereach.enums.RoleName;
import com.ripplereach.ripplereach.models.Role;

import java.util.List;

public interface RoleService {
    Role createRole(RoleName roleName);
    Role updateRole(Long roleId, String name);
    void deleteRole(Long roleId);
    List<Role> getAllRoles();
    Role getRoleById(Long roleId);
    Role getRoleByName(RoleName roleName);
    Role createRoleIfNotExists(RoleName name);
}
