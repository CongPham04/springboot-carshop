package com.carshop.oto_shop.services;


import com.carshop.oto_shop.common.exceptions.AppException;
import com.carshop.oto_shop.common.exceptions.ErrorCode;
import com.carshop.oto_shop.dto.auth.JwtResponse;
import com.carshop.oto_shop.dto.auth.LoginRequest;
import com.carshop.oto_shop.dto.auth.SignupRequest;
import com.carshop.oto_shop.entities.Account;
import com.carshop.oto_shop.entities.User;
import com.carshop.oto_shop.repositories.AccountRepository;
import com.carshop.oto_shop.repositories.UserRepository;
import com.carshop.oto_shop.security.jwt.JwtTokenProvider;
import com.carshop.oto_shop.security.models.CustomUserDetails;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public AuthService(AccountRepository accountRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
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
        if (refreshToken == null || !jwtTokenProvider.validateToken(refreshToken)) {
            throw new AppException(ErrorCode.INVALID_OR_EXPIRED_REFRESH_TOKEN);
        }

        String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
        String role = jwtTokenProvider.getRoleFromToken(refreshToken);
        String newAccessToken = jwtTokenProvider.generateToken(username, role);

        // ⚠️ Không cần tạo refreshToken mới ở đây (vì vẫn còn hạn)
        return new JwtResponse(newAccessToken, null);
    }

    @Transactional
    public void register(SignupRequest request) {
        String username = request.getUsername();
        String fullName = request.getFullName();

        // Kiểm tra có phải email không (rất đơn giản, chỉ cần chứa @)
        boolean isEmail = EMAIL_PATTERN.matcher(username).matches();

        // Step 1: Create Account
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
        Account savedAccount = accountRepository.save(account);
        logger.info("Created account with ID: {}", savedAccount.getAccountId());

        // Step 2: Create User linked to Account
        User user = new User();
        user.setAccount(savedAccount);
        user.setFullName(fullName);

        User savedUser = userRepository.save(user);
        logger.info("Created user with ID: {} for account: {}", savedUser.getUserId(), savedAccount.getAccountId());
    }


}
