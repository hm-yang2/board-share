package com.powerbi.api.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.powerbi.api.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Value("${spring.cloud.azure.active-directory.profile.tenant-id}")
    private String tenantId;

    @Value("${spring.cloud.azure.active-directory.credential.client-id}")
    private String clientId;

    @Value("${spring.cloud.azure.active-directory.credential.client-secret}")
    private String clientSecret;

    @Value("${azure.redirect-uri}")
    private String redirectUri;

    @Value("${jwt.expiration}")
    private int expiration;

    @Value("${jwt.refreshExpiration}")
    private int refreshExpiration;

    @GetMapping("/login")
    public Map<String, String> login() {
        String azureUrl = String.format(
                "https://login.microsoftonline.com/%s/oauth2/v2.0/authorize?client_id=%s&response_type=code&redirect_uri=%s&response_mode=query&scope=openid%%20profile%%20email",
                tenantId, clientId, redirectUri
        );
        Map<String, String> response = new HashMap<>();
        response.put("url", azureUrl);
        return response;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> request, HttpServletResponse response) {
        String authorizationCode = request.get("code");
        // Extract username from authorization code
        String username = getAccessToken(authorizationCode);

        if (username == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "No email found in token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        //Search for user, if user does not exist, create new user

        String token = jwtUtil.generateToken(username);
        String refreshToken = jwtUtil.generateRefreshToken(username);

        // Set JWT tokens as cookies
        Cookie tokenCookie = new Cookie("token", token);
        tokenCookie.setHttpOnly(true); // Protect from JS access
        tokenCookie.setPath("/");
        tokenCookie.setMaxAge(expiration);

        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(refreshExpiration);

        response.addCookie(tokenCookie);
        response.addCookie(refreshTokenCookie);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(@RequestBody Map<String, String> request, HttpServletResponse response) {
        String refreshToken = request.get("refreshToken");
        String username = jwtUtil.getUsernameFromToken(refreshToken);

        if (jwtUtil.validateToken(refreshToken, username)) {
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
            response.addCookie(refreshTokenCookie);

            response.addCookie(tokenCookie);
            response.addCookie(refreshTokenCookie);
            return ResponseEntity.ok().build();
        }
        throw new RuntimeException("Invalid refresh token");
    }

    private String getAccessToken(String authorizationCode) {
        // Exchange code for ID token
        String tokenEndpoint = "https://login.microsoftonline.com/{tenant-id}/oauth2/v2.0/token";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("code", authorizationCode);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");

        Map<String, Object> tokenResponse = webClientBuilder.build()
                .post()
                .uri(tokenEndpoint)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(params))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

        String idToken = (String) Objects.requireNonNull(tokenResponse).get("id_token");

        // Parse ID token to get username
        DecodedJWT jwt = JWT.decode(idToken);
        return jwt.getClaim("email").asString();
    }
}

