
package com.pet.management.controller;

import com.pet.management.model.Clerk;
import com.pet.management.repository.ClerkRepository;
import com.pet.management.security.JwtUtil;
import com.pet.management.security.ClerkDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final ClerkRepository clerkRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClerkDetailsService clerkDetailsService;

    public AuthController(JwtUtil jwtUtil,
                         ClerkRepository clerkRepository, 
                         PasswordEncoder passwordEncoder,
                         ClerkDetailsService clerkDetailsService) {
        this.jwtUtil = jwtUtil;
        this.clerkRepository = clerkRepository;
        this.passwordEncoder = passwordEncoder;
        this.clerkDetailsService = clerkDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // 手动验证用户
            Clerk clerk = clerkRepository.findByUsername(loginRequest.getUsername()).orElse(null);
            
            if (clerk == null) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "用户名或密码错误");
                return ResponseEntity.badRequest().body(error);
            }

            // 验证密码
            if (!passwordEncoder.matches(loginRequest.getPassword(), clerk.getPassword())) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "用户名或密码错误");
                return ResponseEntity.badRequest().body(error);
            }

            // 检查用户是否启用
            if (!clerk.getEnabled()) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "账户已被禁用");
                return ResponseEntity.badRequest().body(error);
            }

            // 加载 UserDetails 并生成 token
            UserDetails userDetails = clerkDetailsService.loadUserByUsername(loginRequest.getUsername());
            String token = jwtUtil.generateToken(userDetails);

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("username", userDetails.getUsername());
            response.put("clerkName", clerk.getClerkName());
            response.put("clerkId", clerk.getId());
            response.put("role", clerk.getRole());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "用户名或密码错误");
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.badRequest().body(Map.of("message", "未登录"));
        }

        String username = authentication.getName();
        Clerk clerk = clerkRepository.findByUsername(username).orElse(null);

        Map<String, Object> response = new HashMap<>();
        response.put("username", username);
        if (clerk != null) {
            response.put("clerkName", clerk.getClerkName());
            response.put("clerkId", clerk.getId());
            response.put("role", clerk.getRole());
        }

        return ResponseEntity.ok(response);
    }

    public static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
