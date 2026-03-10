
package com.pet.management.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.pet.management.security.Permission;
import com.pet.management.security.Role;

import jakarta.annotation.PostConstruct;

@Service
public class RolePermissionService {

    private Map<Role, List<Permission>> rolePermissions;

    @PostConstruct
    public void init() {
        rolePermissions = new HashMap<>();
        
        // 初始化管理员拥有所有权限
        List<Permission> adminPermissions = new ArrayList<>();
        for (Permission permission : Permission.values()) {
            adminPermissions.add(permission);
        }
        rolePermissions.put(Role.ADMIN, adminPermissions);
        
        // 初始化经理权限
        List<Permission> managerPermissions = new ArrayList<>();
        managerPermissions.add(Permission.CUSTOMER_READ);
        managerPermissions.add(Permission.CUSTOMER_WRITE);
        managerPermissions.add(Permission.CLERK_READ);
        managerPermissions.add(Permission.CLERK_WRITE);
        managerPermissions.add(Permission.TRANSACTION_READ);
        managerPermissions.add(Permission.TRANSACTION_WRITE);
        managerPermissions.add(Permission.APPOINTMENT_READ);
        managerPermissions.add(Permission.APPOINTMENT_WRITE);
        managerPermissions.add(Permission.REPORT_READ);
        managerPermissions.add(Permission.REPORT_WRITE);
        managerPermissions.add(Permission.DASHBOARD_READ);
        rolePermissions.put(Role.MANAGER, managerPermissions);
        
        // 初始化员工权限
        List<Permission> staffPermissions = new ArrayList<>();
        staffPermissions.add(Permission.CUSTOMER_READ);
        staffPermissions.add(Permission.CUSTOMER_WRITE);
        staffPermissions.add(Permission.TRANSACTION_READ);
        staffPermissions.add(Permission.TRANSACTION_WRITE);
        staffPermissions.add(Permission.APPOINTMENT_READ);
        staffPermissions.add(Permission.APPOINTMENT_WRITE);
        staffPermissions.add(Permission.DASHBOARD_READ);
        rolePermissions.put(Role.STAFF, staffPermissions);
    }

    public Map<Role, List<Permission>> getAllRolePermissions() {
        return new HashMap<>(rolePermissions);
    }

    public List<Permission> getPermissionsByRole(Role role) {
        return new ArrayList<>(rolePermissions.getOrDefault(role, new ArrayList<>()));
    }

    public void updateRolePermissions(Role role, List<Permission> permissions) {
        rolePermissions.put(role, new ArrayList<>(permissions));
    }

    public boolean hasPermission(Role role, Permission permission) {
        List<Permission> permissions = rolePermissions.get(role);
        return permissions != null && permissions.contains(permission);
    }
}
