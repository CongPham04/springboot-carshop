package com.carshop.oto_shop.common.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class JwtSecretChecker {
    private static final Logger log = LoggerFactory.getLogger(JwtSecretChecker.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    @PostConstruct
    public void printSecret() {
        log.info("JWT_SECRET loaded: {}... (length={})",
                jwtSecret.substring(0, 10),
                jwtSecret.length());
    }
}
