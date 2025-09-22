package com.carshop.oto_shop.services;


import com.carshop.oto_shop.common.exceptions.AppException;
import com.carshop.oto_shop.common.exceptions.ErrorCode;
import com.carshop.oto_shop.dto.auth.JwtResponse;
import com.carshop.oto_shop.dto.auth.LoginRequest;
import com.carshop.oto_shop.dto.auth.SignupRequest;
import com.carshop.oto_shop.entities.Account;
import com.carshop.oto_shop.enums.AccountStatus;
import com.carshop.oto_shop.repositories.AccountRepository;
import com.carshop.oto_shop.security.jwt.JwtTokenProvider;
import com.carshop.oto_shop.security.models.CustomUserDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class AuthService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    public AuthService(AccountRepository accountRepository ,PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public JwtResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        // Lấy thông tin user đã xác thực
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        String role = userDetails.getRole();
        String accessToken = jwtTokenProvider.generateToken(username, role);
        String refreshToken = jwtTokenProvider.generateRefreshToken(username, role);

        return new JwtResponse(accessToken, refreshToken);
    }

    public JwtResponse refreshToken(String refreshToken) {
        if (jwtTokenProvider.validateToken(refreshToken)) {
            String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
            String role = jwtTokenProvider.getRoleFromToken(refreshToken);
            String newAccessToken = jwtTokenProvider.generateToken(username, role);
            return new JwtResponse(newAccessToken, refreshToken);
        }
        throw new AppException(ErrorCode.INVALID_OR_EXPIRED_REFRESH_TOKEN);
    }

    public void register(SignupRequest request) {
        String username = request.getUsername();

        // Kiểm tra có phải email không (rất đơn giản, chỉ cần chứa @)
        boolean isEmail = EMAIL_PATTERN.matcher(username).matches();
        Account account = new Account();
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        if (isEmail) {
            // Kiểm tra email đã tồn tại chưa
            if (accountRepository.existsByEmail(username)) {
                throw new AppException(ErrorCode.ACCOUNT_NOT_FOUND);
            }
            account.setEmail(username);
        } else {
            // Kiểm tra username đã tồn tại chưa
            if (accountRepository.existsByUsername(username)) {
                throw new AppException(ErrorCode.USER_NOT_FOUND);
            }
            account.setUsername(username);
        }
        // role/status sẽ mặc định trong @PrePersist của Account nếu bạn để null
        accountRepository.save(account);
    }


}
