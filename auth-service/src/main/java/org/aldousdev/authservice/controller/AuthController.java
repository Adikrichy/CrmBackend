package org.aldousdev.authservice.controller;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.aldousdev.authservice.dto.request.LoginRequest;
import org.aldousdev.authservice.dto.request.RegisterRequest;
import org.aldousdev.authservice.dto.response.TokenResponse;
import org.aldousdev.authservice.service.AuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(
            @RequestBody RegisterRequest request,
            HttpServletResponse response) {
        var tokens = authService.register(request);
        addTokensToCookies(response, tokens);
        return ResponseEntity.ok(TokenResponse.builder()
                .message("Registration successful")
                .tokenType("Bearer")
                .expiresIn(tokens.getExpiresIn())
                .build());
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(
            @RequestBody LoginRequest request,
            HttpServletResponse response) {
        var tokens = authService.login(request);
        addTokensToCookies(response, tokens);
        return ResponseEntity.ok(TokenResponse.builder()
                .message("Login successful")
                .tokenType("Bearer")
                .expiresIn(tokens.getExpiresIn())
                .build());
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshToken(
            @CookieValue("refresh_token") String refreshToken,
            HttpServletResponse response) {
        var tokens = authService.refreshToken(refreshToken);
        addTokensToCookies(response, tokens);
        return ResponseEntity.ok(TokenResponse.builder()
                .message("Token refresh successful")
                .tokenType("Bearer")
                .expiresIn(tokens.getExpiresIn())
                .build());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        clearTokensFromCookies(response);
        return ResponseEntity.ok().build();
    }

    private void addTokensToCookies(HttpServletResponse response, TokenResponse tokens) {
        // Access Token Cookie
        ResponseCookie accessTokenCookie = ResponseCookie.from("access_token", tokens.getAccessToken())
                .httpOnly(true)
                .secure(true) // Только для HTTPS
                .path("/")
                .maxAge(tokens.getExpiresIn())
                .sameSite("Strict")
                .build();

        // Refresh Token Cookie
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token", tokens.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(30 * 24 * 60 * 60) // 30 дней
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
    }

    private void clearTokensFromCookies(HttpServletResponse response) {
        // Очистка Access Token
        ResponseCookie accessTokenCookie = ResponseCookie.from("access_token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        // Очистка Refresh Token
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
    }
}