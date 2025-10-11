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
                        // ================== Swagger ==================
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()

                        // ================== AUTH ==================
                        .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()

                        // ================== HOME (public) ==================
                        .requestMatchers(HttpMethod.GET,
                                "/api/home/**",
                                "/api/meta/**",
                                "/api/search/**").permitAll()

                        // ================== CARS ==================
                        // Public
                        .requestMatchers(HttpMethod.GET, "/api/cars/**").permitAll()
                        // Admin
                        .requestMatchers(HttpMethod.POST, "/api/cars/**").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/api/cars/**").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/cars/**").hasRole(Role.ADMIN.name())

                        // ================== CAR DETAILS ==================
                        // Public
                        .requestMatchers(HttpMethod.GET, "/api/car-details/**").permitAll()
                        // Admin
                        .requestMatchers(HttpMethod.POST, "/api/car-details/**").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/api/car-details/**").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/car-details/**").hasRole(Role.ADMIN.name())

                        // ================== USERS ==================
                        // Public avatar
                        .requestMatchers(HttpMethod.GET, "/api/users/avatar/image/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/username/{username}").hasAnyRole(Role.ADMIN.name(), Role.USER.name())
                        // Admin management
                        .requestMatchers(HttpMethod.POST, "/api/users/**").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/api/users/**").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/api/users/**").hasRole(Role.ADMIN.name())

                        // ================== ORDERS ==================
                        // User + Admin
                        .requestMatchers(HttpMethod.POST, "/api/orders/**").hasAnyRole(Role.ADMIN.name(), Role.USER.name())
                        .requestMatchers(HttpMethod.GET, "/api/orders/**").hasAnyRole(Role.ADMIN.name(), Role.USER.name())
                        // Admin only
                        .requestMatchers(HttpMethod.PATCH, "/api/orders/**").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/orders/**").hasRole(Role.ADMIN.name())

                        // ================== PAYMENTS ==================
                        // User + Admin
                        .requestMatchers(HttpMethod.POST, "/api/payments/**").hasAnyRole(Role.ADMIN.name(), Role.USER.name())
                        .requestMatchers(HttpMethod.GET, "/api/payments/**").hasAnyRole(Role.ADMIN.name(), Role.USER.name())
                        // Admin only
                        .requestMatchers(HttpMethod.PATCH, "/api/payments/**").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/payments/**").hasRole(Role.ADMIN.name())

                        // ================== PROMOTIONS ==================
                        // Public
                        .requestMatchers(HttpMethod.GET, "/api/promotions/**").permitAll()
                        // Admin
                        .requestMatchers(HttpMethod.POST, "/api/promotions/**").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/api/promotions/**").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/promotions/**").hasRole(Role.ADMIN.name())

                        // ================== NEWS ==================
                        // Public
                        .requestMatchers(HttpMethod.GET, "/api/news/**").permitAll()
                        // Admin
                        .requestMatchers(HttpMethod.POST, "/api/news/**").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/api/news/**").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/news/**").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.PATCH, "/api/news/**").hasRole(Role.ADMIN.name())

                        // ================== ADMIN DASHBOARD ==================
                        .requestMatchers(HttpMethod.GET, "/api/admin/**").hasRole(Role.ADMIN.name())

                        // ================== Any other ==================
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
