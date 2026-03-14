package com.sendiri.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Matikan CSRF karena Gateway biasanya Stateless (API)
            .csrf(AbstractHttpConfigurer::disable)
            
            // 2. Matikan CORS jika Anda ingin mengaturnya di level Gateway/Routes
            .cors(AbstractHttpConfigurer::disable)

            // 3. Pastikan tidak ada session yang disimpan (Stateless)
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // 4. PERMIT ALL: Izinkan semua request tanpa kecuali
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )

            // 5. Matikan Form Login dan Basic Auth agar tidak muncul popup "Sign In"
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }
}