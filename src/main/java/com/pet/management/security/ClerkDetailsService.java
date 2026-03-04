
package com.pet.management.security;

import com.pet.management.model.Clerk;
import com.pet.management.repository.ClerkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class ClerkDetailsService implements UserDetailsService {

    @Autowired
    private ClerkRepository clerkRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Clerk clerk = clerkRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在: " + username));

        if (!clerk.getEnabled()) {
            throw new UsernameNotFoundException("用户已被禁用: " + username);
        }

        // 将角色转换为Spring Security的权限格式（ROLE_前缀）
        String role = clerk.getRole() != null ? clerk.getRole().name() : Role.STAFF.name();
        
        return new User(
                clerk.getUsername(),
                clerk.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
        );
    }
}

