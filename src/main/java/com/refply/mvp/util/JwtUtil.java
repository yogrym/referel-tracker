package com.refply.mvp.util;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.refply.mvp.entity.ConsumerEntity;
import com.refply.mvp.entity.UserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    @Value("${jwt.secretkey}")
    private String secretKey;

    public SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateToken(UserEntity user) {
        return Jwts.builder()
                .subject(String.valueOf(user.getId()))
                .claim("email", user.getEmail())
                .claim("name", user.getName())
                .claim("role", user.getRoles())
                .claim("phone", user.getPhone())
                .claim("status", user.getStatus())
                .claim("createdAt", user.getCreatedAt() != null ? user.getCreatedAt().toString() : null)
                .claim("updatedAt", user.getUpdatedAt() != null ? user.getUpdatedAt().toString() : null)
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSignKey())
                .compact();

    }

    public String generateCsmToken(ConsumerEntity consumer) {
        return Jwts.builder()
                .subject(String.valueOf(consumer.getId()))
                .claim("email", consumer.getEmail())
                .claim("name", consumer.getUserName())
                .claim("role", consumer.getRole())
                .claim("phone", consumer.getPhone())
                .claim("createdAt", consumer.getCreatedAt() != null ? consumer.getCreatedAt().toString() : null)
                .claim("updatedAt", consumer.getUpdatedAt() != null ? consumer.getUpdatedAt().toString() : null)
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSignKey())
                .compact();

    }

    public String generateRefreshToken(UserEntity user) {
        return Jwts.builder()
                .subject(String.valueOf(user.getId()))
                .claim("email", user.getEmail())
                .claim("name", user.getName())
                .claim("role", user.getRoles())
                .claim("status", user.getStatus())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
                .signWith(getSignKey())
                .compact();
    }

     public String generateCsmRefreshToken(ConsumerEntity consumer) {
        return Jwts.builder()
                .subject(String.valueOf(consumer.getId()))
                .claim("email", consumer.getEmail())
                .claim("name", consumer.getUserName())
                .claim("role", consumer.getRole())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
                .signWith(getSignKey())
                .compact();
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.get("email", String.class);

    }
}
