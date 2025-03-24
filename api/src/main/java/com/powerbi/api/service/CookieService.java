package com.powerbi.api.service;

import com.powerbi.api.config.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for managing cookies related to JWT tokens.
 * Handles operations such as refreshing access and refresh tokens stored in cookies.
 */
@Service
public class CookieService {
    @Value("${jwt.expiration}")
    private int expiration;

    @Value("${jwt.refreshExpiration}")
    private int refreshExpiration;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Refreshes the access and refresh tokens using the refresh token stored in cookies.
     * Validates the refresh token and generates new tokens if valid.
     * The new tokens are added to the response as cookies.
     *
     * @param request  the HTTP request containing the cookies
     * @param response the HTTP response to which the new cookies will be added
     * @return the new access token
     * @throws RuntimeException if the refresh token is not found or is invalid
     */
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
