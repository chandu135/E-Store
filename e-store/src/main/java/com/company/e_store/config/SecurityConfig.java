package com.company.e_store.config;

import com.company.e_store.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/auth/**", "/api/categories/**", "/h2-console/**")  // Ignore CSRF for these
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/api/categories/**", "/h2-console/**").permitAll()  // Public endpoints
                        .anyRequest().authenticated()  // All other endpoints require authentication
                )
                .headers(headers -> headers
                        .frameOptions().disable()  // ✅ Required for H2 console to render in a frame
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // Disable session management (stateless)
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);  // Add JWT filter before Spring's default filter

        return http.build();
    }
}