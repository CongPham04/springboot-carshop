package com.carshop.oto_shop.controllers;

import com.carshop.oto_shop.common.response.ApiResponse;
import com.carshop.oto_shop.dto.auth.JwtResponse;
import com.carshop.oto_shop.dto.auth.LoginRequest;
import com.carshop.oto_shop.dto.auth.SignupRequest;
import com.carshop.oto_shop.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "AuthController")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Login", description = "API login")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> login(@RequestBody LoginRequest loginRequest) {
        JwtResponse jwt = authService.login(loginRequest);

        // ✅ Gửi refreshToken qua cookie HTTP-only
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", jwt.getRefreshToken())
                .httpOnly(true)
                .secure(false) // ⚠️ đổi thành true khi deploy HTTPS
                .path("/api/auth/refresh")
                .maxAge(7 * 24 * 60 * 60) // 7 ngày
                .sameSite("Strict")
                .build();

        // Chỉ trả accessToken cho frontend
        JwtResponse responseBody = new JwtResponse(jwt.getToken(), null);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(ApiResponse.success("Đăng nhập thành công!", responseBody));
    }

    @Operation(summary = "Refresh Token", description = "Làm mới access token bằng refresh token trong cookie")
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<JwtResponse>> refresh(@CookieValue(value = "refreshToken", required = false) String refreshToken) {
        JwtResponse jwt = authService.refreshToken(refreshToken);

        // Gia hạn cookie nếu muốn
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false)
                .path("/api/auth/refresh")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(ApiResponse.success("Làm mới token thành công!", jwt));
    }

    @Operation(summary = "Signup", description = "API register new account and user profile.")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody SignupRequest signupRequest) {
        authService.register(signupRequest);
        return ResponseEntity.ok(ApiResponse.success("Đăng ký thành công"));
    }

    // ✅ Logout: Xoá cookie refreshToken
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        ResponseCookie clearCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(false)
                .path("/api/auth/refresh")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, clearCookie.toString())
                .body(ApiResponse.success("Đăng xuất thành công"));
    }
}
