package com.carshop.oto_shop.common.config;

import com.carshop.oto_shop.enums.Role;
import com.carshop.oto_shop.security.handers.AccessDeniedHandlerImpl;
import com.carshop.oto_shop.security.handers.AuthEntryPointJwt;
import com.carshop.oto_shop.security.jwt.JwtAuthenticationFilter;
import com.carshop.oto_shop.security.services.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;


@Configuration
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final AuthEntryPointJwt authEntryPointJwt;
    private final AccessDeniedHandlerImpl accessDeniedHandler;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter,
                          CustomUserDetailsService userDetailsService,
                          PasswordEncoder passwordEncoder,
                          AuthEntryPointJwt authEntryPointJwt,
                          AccessDeniedHandlerImpl accessDeniedHandler) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.authEntryPointJwt = authEntryPointJwt;
        this.accessDeniedHandler = accessDeniedHandler;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // tắt CSRF (dành cho Postman test API)
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authEntryPointJwt)
                        .accessDeniedHandler(accessDeniedHandler))
                .authorizeHttpRequests(auth -> auth
                        // Cho phép truy cập swagger mà không cần login
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST,
                                "/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/api/cars/**", "/api/car-details/**").permitAll()

                        // Car and Account APIs - ADMIN only
                        .requestMatchers(HttpMethod.POST,
                                "/api/cars/**", "/api/accounts/**", "/api/car-details/**").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT,
                                "/api/cars/**", "/api/accounts/**", "/api/car-details/**").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE,
                                "/api/cars/**", "/api/accounts/**", "/api/car-details/**").hasRole(Role.ADMIN.name())

                        // User APIs - ADMIN + USER
                        .requestMatchers(HttpMethod.POST,
                                "/api/users/**").hasAnyRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT,
                                "/api/users/**").hasAnyRole(Role.ADMIN.name(), Role.USER.name())
                        .requestMatchers(HttpMethod.DELETE,
                                "/api/users/**").hasAnyRole(Role.ADMIN.name())

                        // Order APIs
                        .requestMatchers(HttpMethod.POST,
                                "/api/orders/**").hasAnyRole(Role.ADMIN.name(), Role.USER.name())
                        .requestMatchers(HttpMethod.GET,
                                "/api/orders/**").hasAnyRole(Role.ADMIN.name(), Role.USER.name())
                        .requestMatchers(HttpMethod.PUT,
                                "/api/orders/**").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.PATCH,
                                "/api/orders/**").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE,
                                "/api/orders/**").hasRole(Role.ADMIN.name())

                        // Order Detail APIs
                        .requestMatchers(HttpMethod.GET,
                                "/api/order-details/**").hasAnyRole(Role.ADMIN.name(), Role.USER.name())
                        .requestMatchers(HttpMethod.DELETE,
                                "/api/order-details/**").hasRole(Role.ADMIN.name())

                        // Payment APIs
                        .requestMatchers(HttpMethod.POST,
                                "/api/payments/**").hasAnyRole(Role.ADMIN.name(), Role.USER.name())
                        .requestMatchers(HttpMethod.GET,
                                "/api/payments/**").hasAnyRole(Role.ADMIN.name(), Role.USER.name())
                        .requestMatchers(HttpMethod.PATCH,
                                "/api/payments/**").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE,
                                "/api/payments/**").hasRole(Role.ADMIN.name())

                        .anyRequest().authenticated()

                );
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
