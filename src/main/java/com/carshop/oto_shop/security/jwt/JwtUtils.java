package com.carshop.oto_shop.security.jwt;

import jakarta.servlet.http.HttpServletRequest;

public class JwtUtils {

    // Lấy token từ header "Authorization"
    public static String getTokenFromHeader(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
