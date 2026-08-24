package com.example.carereport.security;

import com.example.carereport.entity.UserAccount;
import com.example.carereport.repository.UserAccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserAccountRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserAccountRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // アプリ起動時に自動
    @Override
    public void run(String... args) {
        
        // ① 管理者アカウント（ADMIN）
        if (userRepository.findByLoginId("admin").isEmpty()) {
            UserAccount admin = new UserAccount();
            admin.setLoginId("admin");
            admin.setPassword(passwordEncoder.encode("admin123")); 
            admin.setRole("ROLE_ADMIN"); // 管理者権限
            userRepository.save(admin);
        }

        // ② 一般スタッフアカウント（USER）
        if (userRepository.findByLoginId("staff").isEmpty()) {
            UserAccount staff = new UserAccount();
            staff.setLoginId("staff");
            staff.setPassword(passwordEncoder.encode("staff123"));
            staff.setRole("ROLE_USER"); // 一般権限
            userRepository.save(staff);
        }
    }
}