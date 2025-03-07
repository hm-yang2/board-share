package com.powerbi.api.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.powerbi.api.config.JwtUtil;
import com.powerbi.api.model.User;
import com.powerbi.api.service.CookieService;
import com.powerbi.api.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
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
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
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

    @Autowired
    private UserService userService;

    @Autowired
    private CookieService cookieService;

    @Autowired
    private JwtUtil jwtUtil;

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
    public ResponseEntity<Map<String, String>> login(
            @RequestBody Map<String, String> request,
            HttpServletResponse response
    ) {
        String authorizationCode = request.get("code");
        // Extract email from authorization code
        String email = null;
        email = getAccessToken(authorizationCode);

        if (email == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "No email found in token");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        //Search for user, if user does not exist, create new user
        if (userService.getAllUsers(email).isEmpty()) {
            User user = userService.createUser(email);
        }

        String token = jwtUtil.generateToken(email);
        String refreshToken = jwtUtil.generateRefreshToken(email);

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
    public ResponseEntity<Map<String, String>> refresh(HttpServletRequest request, HttpServletResponse response) {
        try {
            // Call the TokenService method to handle the refresh logic
            cookieService.refreshTokens(request, response);

            // Optionally, return a response body with the new tokens for client-side handling
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("message", "Tokens refreshed successfully");

            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    /**
     * Exchange microsoft authorization code for access token
     * and get the email from the access token
     * @param authorizationCode String
     * @return email String
     */
    private String getAccessToken(@NotNull String authorizationCode) {
        try {
            // Exchange code for ID token
            String tokenEndpoint = "https://login.microsoftonline.com/" + tenantId + "/oauth2/v2.0/token";

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
                    .bodyValue(params)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                    })
                    .block();

            String idToken = (String) Objects.requireNonNull(tokenResponse).get("id_token");

            // Parse ID token to get username
            DecodedJWT decodedJWT = JWT.decode(idToken);

            // Extract email claim
            return decodedJWT.getClaim("email").asString();
        } catch (WebClientException | NullPointerException e) {
            return null;
        }
    }
}

