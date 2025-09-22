package com.carshop.oto_shop.security.jwt;

import com.carshop.oto_shop.security.services.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider,
                                   CustomUserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            if(isBypassToken(request)){
                filterChain.doFilter(request, response);
                return;
            }
            // Lấy token từ header Authorization
            String token = JwtUtils.getTokenFromHeader(request);

            if (token != null) {
                // Validate token
                boolean valid = jwtTokenProvider.validateToken(token);
                if (valid) {
                    String username = jwtTokenProvider.getUsernameFromToken(token);

                    // Nếu chưa có Authentication trong context thì load user và set
                    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                } else {
                    logger.debug("JWT token invalid for request [{}]", request.getRequestURI());
                }
            }
        } catch (Exception ex) {
            // Không ném exception ra để đảm bảo filter chain vẫn chạy; log để debug.
            logger.error("Could not set user authentication in security context", ex);
        }

        // Tiếp tục chain
        filterChain.doFilter(request, response);
    }

    private boolean isBypassToken(HttpServletRequest request) {
        final List<Pair<String, String>> bypassTokens = Arrays.asList(
                Pair.of("/api/cars", "GET"),
                Pair.of("/api/car-categories", "GET"),
                Pair.of("/api/auth/register", "POST"),
                Pair.of("/api/auth/login", "POST")
        );

        String requestPath = request.getServletPath();
        String requestMethod = request.getMethod();

        return bypassTokens.stream()
                .anyMatch(p -> requestPath.startsWith(p.getLeft()) && requestMethod.equalsIgnoreCase(p.getRight()));
    }
}
