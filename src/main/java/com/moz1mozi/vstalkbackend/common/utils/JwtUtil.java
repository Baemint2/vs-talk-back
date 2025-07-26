package com.moz1mozi.vstalkbackend.common.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Getter
@Component
public class JwtUtil {

    public static final String ISSUER = "moz1mozi.com";
    public static final int EXP_SHORT = 15 * 60 * 1000; // 15분
    public static final int EXP_LONG = 60 * 60 * 1000;  // 1시간
    public static final int REFRESH_EXP = 7 * 24 * 60 * 60 * 1000; // 7일
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER = "Authorization";

    private SecretKey key;

    @Value("${jwt.secret}")
    private String secretKey;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public String createAccessToken(Authentication auth) {
        String username = auth.getName();
        String authorities = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .issuer(ISSUER)
                .subject(username)
                .expiration(new Date(System.currentTimeMillis() + EXP_LONG))
                .claim("username", username)
                .claim("role", authorities)
                .signWith(key)
                .compact();
    }

    public String createRefreshToken(Authentication auth) {
        String username = auth.getName();

        return Jwts.builder()
                .issuer(ISSUER)
                .subject(username)
                .expiration(new Date(System.currentTimeMillis() + REFRESH_EXP))
                .signWith(key)
                .compact();
    }

    public Claims verify(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token.replace(TOKEN_PREFIX, ""))
                    .getPayload();
        } catch (ExpiredJwtException e) {
            log.error("JWT expired at: {}. Current time: {}", e.getClaims().getExpiration(), new Date());
            throw e;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            return null;
        }
    }

    public int getExpiryDurationFromToken(String token) {
        Claims claims = verify(token);
        long expirationMillis = claims.getExpiration().getTime();
        long currentMillis = System.currentTimeMillis();
        return (int) ((expirationMillis - currentMillis) / 1000);
    }

    public String getUsernameFromToken(String token) {
        Claims claims = verify(token);
        log.info("getUsernameFromToken : {} {}", token, claims.getSubject());
        return claims.getSubject();
    }

    public UserDetails getUserDetailsFromToken(String token) {
        Claims claims = verify(token);
        String username = claims.getSubject();
        return new User(username, "", List.of());
    }

    public boolean validateToken(String authToken) {
        try {
            verify(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }

    public void setTokensAndCookies(String accessToken, String refreshToken, HttpServletResponse response) {
        // JWT에서 직접 만료 시간을 추출하여 쿠키의 유효기간을 설정
        int accessTokenMaxAge = getExpiryDurationFromToken(accessToken);
        int refreshTokenMaxAge = getExpiryDurationFromToken(refreshToken);
        // 쿠키에 새 토큰 저장
        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setHttpOnly(false);
        accessTokenCookie.setMaxAge(accessTokenMaxAge);
        response.addCookie(accessTokenCookie);

        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setHttpOnly(false);
        refreshTokenCookie.setMaxAge(refreshTokenMaxAge);
        response.addCookie(refreshTokenCookie);

        log.info("새로운 AccessToken: {}", accessToken);
        log.info("새로운 RefreshToken: {}", refreshToken);
    }
}
