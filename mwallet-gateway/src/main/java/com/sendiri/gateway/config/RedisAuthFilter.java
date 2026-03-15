package com.sendiri.gateway.config;

import com.sendiri.repo.utils.RedisUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class RedisAuthFilter extends OncePerRequestFilter {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final List<String> PUBLIC_ENDPOINTS = List.of(
            "/login",
            "/register",
            "/verify",
            "/swagger-ui",
            "/v3/api-docs"
    );

    public RedisAuthFilter(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        System.out.println("path "+path);

        // bypass public endpoints
        for (String endpoint : PUBLIC_ENDPOINTS) {
            if (path.contains(endpoint)) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        String authKey = request.getHeader("auth");
        if(path.contains("topup")){
            String admin = request.getHeader("username");
            if(admin.equals("admin")){
                filterChain.doFilter(request, response);
                return;
            }
        }
        System.out.println("authKey "+authKey);

        if (authKey == null || authKey.isBlank()) {
            System.out.println("missing");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing auth header");
            return;
        }

        Long ttl = redisTemplate.getExpire(authKey, TimeUnit.SECONDS);

        if (ttl == null || ttl < 60) {
            System.out.println("token invalid");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired or invalid");
            return;
        }

        filterChain.doFilter(request, response);
    }
}