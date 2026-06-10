package com.auth.helpers;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class JwtCookieService {
    public ResponseCookie createAccessCookie(
            String accessToken) {

        return ResponseCookie
                .from("accessToken", accessToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("Strict")
                .maxAge(Duration.ofMinutes(15))
                .build();
    }

    public ResponseCookie createRefreshCookie(
            String refreshToken) {

        return ResponseCookie
                .from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("Strict")
                .maxAge(Duration.ofDays(30))
                .build();
    }
}
