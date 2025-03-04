package com.powerbi.api.service;

import com.powerbi.api.config.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CookieService {
    @Value("${jwt.expiration}")
    private int expiration;

    @Value("${jwt.refreshExpiration}")
    private int refreshExpiration;

    @Autowired
    private JwtUtil jwtUtil;

    public String refreshTokens(HttpServletRequest request, HttpServletResponse response) {
        // Get the refresh token from cookies
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        if (refreshToken == null) {
            throw new RuntimeException("Refresh token not found in cookies");
        }

        String username = jwtUtil.getUsernameFromToken(refreshToken);

        if (jwtUtil.validateToken(refreshToken, username)) {
            // Generate new tokens
            String newToken = jwtUtil.generateToken(username);
            String newRefreshToken = jwtUtil.generateRefreshToken(username);

            // Set the new access token
            Cookie tokenCookie = new Cookie("token", newToken);
            tokenCookie.setHttpOnly(true);
            tokenCookie.setPath("/");
            tokenCookie.setMaxAge(expiration);

            // Set new refresh token
            Cookie refreshTokenCookie = new Cookie("refreshToken", newRefreshToken);
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setPath("/");
            refreshTokenCookie.setMaxAge(refreshExpiration);

            // Add the cookies to the response
            response.addCookie(tokenCookie);
            response.addCookie(refreshTokenCookie);
            return newToken;
        } else {
            throw new RuntimeException("Invalid refresh token");
        }
    }
}
