
package com.pet.management.initializer;

import com.pet.management.model.Clerk;
import com.pet.management.repository.ClerkRepository;
import com.pet.management.security.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AdminDataInitializer implements CommandLineRunner {

    @Autowired
    private ClerkRepository clerkRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 检查是否已存在管理员账户
        if (clerkRepository.findByUsername("admin").isEmpty()) {
            Clerk admin = new Clerk();
            admin.setClerkName("系统管理员");
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setPhone("13800000000");
            admin.setCommissionRate(BigDecimal.valueOf(0.05));
            admin.setEnabled(true);
            admin.setRole(Role.ADMIN);
            admin.setNotes("默认管理员账户");

            clerkRepository.save(admin);
            System.out.println("默认管理员账户已创建: admin / admin123");
        }
    }
}
