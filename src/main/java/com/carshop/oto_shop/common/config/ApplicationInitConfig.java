package com.carshop.oto_shop.common.config;

import com.carshop.oto_shop.entities.Account;
import com.carshop.oto_shop.entities.User;
import com.carshop.oto_shop.enums.AccountStatus;
import com.carshop.oto_shop.enums.Gender;
import com.carshop.oto_shop.enums.Role;
import com.carshop.oto_shop.repositories.AccountRepository;
import com.carshop.oto_shop.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Configuration
public class ApplicationInitConfig {
    private final static Logger log = LoggerFactory.getLogger(ApplicationInitConfig.class);
    private final PasswordEncoder passwordEncoder;

    public ApplicationInitConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    ApplicationRunner initAdmin(AccountRepository accountRepository, UserRepository userRepository) {
        return args -> {
            String defaultAdminUsername = "admin";
            String defaultAdminEmail = "admin@gmail.com";

            // Kiểm tra tài khoản admin đã tồn tại chưa
            boolean exists = accountRepository.existsByUsername(defaultAdminUsername)
                    || accountRepository.existsByEmail(defaultAdminEmail);

            if (!exists) {
                // === 1️⃣ Tạo account admin ===
                Account adminAccount = new Account();
                adminAccount.setUsername(defaultAdminUsername);
                adminAccount.setEmail(defaultAdminEmail);
                adminAccount.setPassword(passwordEncoder.encode("admin"));
                adminAccount.setRole(Role.ADMIN);
                adminAccount.setStatus(AccountStatus.ACTIVE);
                accountRepository.save(adminAccount);

                // === 2️⃣ Tạo user tương ứng ===
                User adminUser = new User();
                adminUser.setAccount(adminAccount);
                adminUser.setFullName("Administrator");
                adminUser.setGender(Gender.MALE);
                adminUser.setPhone("1900123123");
                adminUser.setAddress("Hà Nội, Việt Nam");
                adminUser.setDob(LocalDate.of(1990, 1, 1));
                userRepository.save(adminUser);

                log.warn("Default account created 'admin' (password: admin).");
            } else {
                log.info("Default admin account already exists, skip creation step.");
            }
        };
    }
}
