package com.powerbi.api.config;

import com.powerbi.api.service.CookieService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * Filter for handling JWT-based authentication.
 * Extracts JWT tokens from cookies, validates them, and sets the authentication context.
 * 
 * If a valid refresh token is found, it refreshes the access token.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final CookieService cookieService;

    /**
     * Constructor for JwtAuthenticationFilter. 
     * Not a Spring Boot bean, hence the need for this contructor. Might want to convert this into a bean
     *
     * @param jwtUtil the utility class for handling JWT operations
     * @param cookieService the service for managing cookies
     */
    public JwtAuthenticationFilter(JwtUtil jwtUtil, CookieService cookieService) {
        this.jwtUtil = jwtUtil;
        this.cookieService = cookieService;
    }

    /**
     * Filters incoming requests to validate JWT tokens and set the security context.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @param filterChain the filter chain
     * @throws ServletException if a servlet error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        // Look for the token in cookies
        Cookie[] cookies = request.getCookies();
        String token = null;
        String refreshToken = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                }
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                }
            }
        }

        if (refreshToken != null) {
            try {
                token = cookieService.refreshTokens(request, response);  // Refresh tokens if refresh token is valid
            } catch (Exception e) {
                // Handle the error (invalid refresh token, etc.)
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid refresh token");
                return;
            }
        }

        if (token != null) {
            String username = jwtUtil.getUsernameFromToken(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if (jwtUtil.validateToken(token, username)) {
                    User userDetails = new User(username, "", Collections.emptyList());

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
