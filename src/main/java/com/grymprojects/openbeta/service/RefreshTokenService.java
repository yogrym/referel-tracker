package com.grymprojects.openbeta.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HexFormat;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grymprojects.openbeta.Repository.RefreshTokenRepository;
import com.grymprojects.openbeta.model.RefreshToken;
import com.grymprojects.openbeta.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void save(User user, String refreshToken, Jwt jwt) {
        refreshTokenRepository.save(RefreshToken.builder()
                .tokenHash(hash(refreshToken))
                .user(user)
                .expiresAt(jwt.getExpiresAt())
                .build());
    }

    @Transactional
    public RefreshToken validateStoredToken(String refreshToken, Jwt jwt) {
        RefreshToken storedToken = refreshTokenRepository.findByTokenHash(hash(refreshToken))
                .orElseThrow(() -> new JwtException("Refresh token is not active"));

        if (Boolean.TRUE.equals(storedToken.getRevoked())) {
            revokeAllActiveTokens(storedToken.getUser());
            throw new JwtException("Refresh token is revoked");
        }

        Instant expiresAt = storedToken.getExpiresAt();
        if (expiresAt == null || expiresAt.isBefore(Instant.now())) {
            revoke(storedToken);
            throw new JwtException("Refresh token is expired");
        }

        if (!storedToken.getUser().getEmail().equals(jwt.getClaimAsString("email"))) {
            revokeAllActiveTokens(storedToken.getUser());
            throw new JwtException("Refresh token user mismatch");
        }

        return storedToken;
    }

    @Transactional
    public void revoke(RefreshToken refreshToken) {
        refreshToken.setRevoked(true);
        refreshToken.setRevokedAt(Instant.now());
        refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public void revoke(String refreshToken) {
        refreshTokenRepository.findByTokenHash(hash(refreshToken))
                .ifPresent(this::revoke);
    }

    @Transactional
    public void revokeAllActiveTokens(User user) {
        refreshTokenRepository.findAllByUserAndRevokedFalse(user)
                .forEach(this::revoke);
    }

    private String hash(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is not available", exception);
        }
    }
}
