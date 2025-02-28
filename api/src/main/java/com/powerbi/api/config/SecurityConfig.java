package com.powerbi.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(
            (authorize) ->
                authorize
                .requestMatchers("/api/auth/login", "/api/auth/refresh").permitAll()
                .anyRequest().authenticated()
        );

        http.addFilterBefore(new JwtAuthenticationFilter(new JwtUtil()), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

