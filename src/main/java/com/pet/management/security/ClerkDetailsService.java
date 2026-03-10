
package com.pet.management.security;

import com.pet.management.model.Clerk;
import com.pet.management.repository.ClerkRepository;
import com.pet.management.service.RolePermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClerkDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(ClerkDetailsService.class);

    @Autowired
    private ClerkRepository clerkRepository;

    @Autowired
    private RolePermissionService rolePermissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Clerk clerk = clerkRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在: " + username));

        if (!clerk.getEnabled()) {
            throw new UsernameNotFoundException("用户已被禁用: " + username);
        }

        // 将角色转换为Spring Security的权限格式（ROLE_前缀）
        Role role = clerk.getRole() != null ? clerk.getRole() : Role.STAFF;
        
        // 创建权限列表
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        
        // 添加角色权限
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
        
        // 添加具体的权限
        List<Permission> permissions = rolePermissionService.getPermissionsByRole(role);
        for (Permission permission : permissions) {
            authorities.add(new SimpleGrantedAuthority(permission.name()));
        }
        
        logger.info("用户 {} 登录 - 角色: {}, 权限: {}", username, role, permissions);
        
        return new User(
                clerk.getUsername(),
                clerk.getPassword(),
                authorities
        );
    }
}

