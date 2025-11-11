package com.carshop.oto_shop.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
       config.setAllowedOrigins(List.of("http://localhost:3000")); // FE
//       config.setAllowedOrigins(List.of("http://localhost:*", "https://*.ngrok-free.app"));
//        config.setAllowedOrigins(List.of("https://2b4d45d22d2b.ngrok-free.app"));
        System.out.println("🌐 CORS request handled for origin: " + config.getAllowedOriginPatterns());
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        // ✅ Thêm dòng này để đảm bảo expose cookies/token về FE
        config.addExposedHeader("Authorization");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        System.out.println("✅ CORS Config loaded successfully!");
        return source;
    }
}
