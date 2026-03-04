
package com.pet.management.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pet.management.security.Permission;
import com.pet.management.security.Role;

@RestController
@RequestMapping("/api/roles")
public class ApiRoleController {

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
        
        // 角色权限映射
        Map<String, String[]> rolePermissions = new HashMap<>();
        rolePermissions.put("ADMIN", new String[]{"所有权限"});
        rolePermissions.put("MANAGER", new String[]{"员工管理", "报告查看", "交易查看", "顾客管理"});
        rolePermissions.put("STAFF", new String[]{"日常业务操作", "顾客查看", "交易操作"});
        
        result.put("rolePermissions", rolePermissions);
        
        return result;
    }
}

