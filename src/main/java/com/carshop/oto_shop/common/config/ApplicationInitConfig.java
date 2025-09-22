package com.carshop.oto_shop.common.config;

import com.carshop.oto_shop.entities.Account;
import com.carshop.oto_shop.enums.AccountStatus;
import com.carshop.oto_shop.enums.Role;
import com.carshop.oto_shop.repositories.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationInitConfig {
    private final static Logger log = LoggerFactory.getLogger(ApplicationInitConfig.class);
    private final PasswordEncoder passwordEncoder;
    public ApplicationInitConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    ApplicationRunner initAdmin(AccountRepository accountRepository) {
        return args -> {
            String defaultAdminUsername = "admin";
            String defaultAdminEmail = "admin@gmail.com";
            // kiểm tra đã tồn tại chưa
            boolean exists = accountRepository.existsByUsername(defaultAdminUsername)
                    || accountRepository.existsByEmail(defaultAdminEmail);
            if (!exists) {
                Account admin = new Account();
                admin.setUsername(defaultAdminUsername);
                admin.setEmail(defaultAdminEmail);
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setRole(Role.ADMIN);
                admin.setStatus(AccountStatus.ACTIVE);
                accountRepository.save(admin);
                log.warn("Người dùng quản trị đã được tạo với mật khẩu mặc định: admin, vui lòng thay đổi!");
            } else {
                log.info("Tài khoản quản trị mặc định đã tồn tại, bỏ qua bước tạo.");
            }
        };
    }
}
