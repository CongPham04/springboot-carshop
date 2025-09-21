package com.carshop.oto_shop.controllers;

import com.carshop.oto_shop.common.response.ApiResponse;
import com.carshop.oto_shop.dto.auth.JwtResponse;
import com.carshop.oto_shop.dto.auth.LoginRequest;
import com.carshop.oto_shop.dto.auth.SignupRequest;
import com.carshop.oto_shop.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> login(@RequestBody LoginRequest loginRequest) {
        JwtResponse jwt = authService.login(loginRequest);
        return ResponseEntity.ok(ApiResponse.success("Đăng nhập thành công!", jwt));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<JwtResponse>> refresh(@RequestParam String refreshToken) {
        JwtResponse jwt = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(ApiResponse.success("Làm mới token thành công!", jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody SignupRequest signupRequest) {
        authService.register(signupRequest);
        return ResponseEntity.ok(ApiResponse.success("Đăng ký thành công"));
    }
}
