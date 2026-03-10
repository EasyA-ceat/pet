
package com.pet.management.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.pet.management.security.Permission;
import com.pet.management.security.Role;
import com.pet.management.service.RolePermissionService;

@RestController
@RequestMapping("/api/roles")
public class ApiRoleController {

    @Autowired
    private RolePermissionService rolePermissionService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public Map<String, Object> getRolesAndPermissions() {
        Map<String, Object> result = new HashMap<>();
        
        // 获取所有角色
        Map<String, String> roles = new HashMap<>();
        for (Role role : Role.values()) {
            roles.put(role.name(), role.getDisplayName());
        }
        result.put("roles", roles);
        
        // 获取所有权限
        Map<String, String> permissions = new HashMap<>();
        for (Permission permission : Permission.values()) {
            permissions.put(permission.name(), permission.getDisplayName());
        }
        result.put("permissions", permissions);
        
        // 从服务获取角色权限映射
        Map<String, List<String>> rolePermissions = new HashMap<>();
        for (Role role : Role.values()) {
            List<String> perms = rolePermissionService.getPermissionsByRole(role)
                    .stream()
                    .map(Permission::name)
                    .collect(Collectors.toList());
            rolePermissions.put(role.name(), perms);
        }
        result.put("rolePermissions", rolePermissions);
        
        return result;
    }

    @PutMapping("/{roleName}/permissions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateRolePermissions(
            @PathVariable String roleName,
            @RequestBody List<String> permissionNames) {
        try {
            Role role = Role.valueOf(roleName);
            List<Permission> permissions = permissionNames.stream()
                    .map(Permission::valueOf)
                    .collect(Collectors.toList());
            
            rolePermissionService.updateRolePermissions(role, permissions);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "权限更新成功");
            
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "无效的角色或权限名称");
            return ResponseEntity.badRequest().body(error);
        }
    }
}

