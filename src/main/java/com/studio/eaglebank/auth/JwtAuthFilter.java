package com.studio.eaglebank.auth;

import com.studio.eaglebank.config.constants.WhitelistedEndpoint;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private static final Set<WhitelistedEndpoint> WHITELISTED_ENDPOINTS = Set.of(
            new WhitelistedEndpoint("/v1/users", "POST"),
            new WhitelistedEndpoint("/v1/auth", "POST"));

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        if (WHITELISTED_ENDPOINTS.contains(new WhitelistedEndpoint(path, method))) {
            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing or invalid Authorization header");
            return;
        }

        String token = header.substring(7);

        if (!jwtService.isTokenValid(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or expired token");
            return;
        }

        String userId = jwtService.extractUserId(token);
        request.setAttribute("userId", userId);
        filterChain.doFilter(request, response);
    }
}
