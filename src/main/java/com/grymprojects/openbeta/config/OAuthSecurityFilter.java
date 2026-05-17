package com.grymprojects.openbeta.config;

import java.util.Base64;
import java.util.Collection;
import java.util.List;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.nimbusds.jose.jwk.source.ImmutableSecret;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

@Configuration
public class OAuthSecurityFilter {

    @Bean
    public JwtEncoder jwtEncoder(SecretKey jwtSecretKey) {
        return new NimbusJwtEncoder(new ImmutableSecret<>(jwtSecretKey));
    }

    @Bean
    public JwtDecoder jwtDecoder(SecretKey jwtSecretKey) {
        NimbusJwtDecoder decoder = buildJwtDecoder(jwtSecretKey);
        OAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<>(
                JwtValidators.createDefaultWithIssuer("affiliate-system"),
                this::validateAccessToken);
        decoder.setJwtValidator(validator);
        return decoder;
    }

    @Bean
    public JwtDecoder refreshJwtDecoder(SecretKey jwtSecretKey) {
        NimbusJwtDecoder decoder = buildJwtDecoder(jwtSecretKey);
        decoder.setJwtValidator(JwtValidators.createDefaultWithIssuer("affiliate-system"));
        return decoder;
    }

    @Bean
    public SecretKey jwtSecretKey(@Value("${jwt.secret}") String jwtSecret) {
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
        return new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(this::extractAuthorities);
        return converter;
    }

    private NimbusJwtDecoder buildJwtDecoder(SecretKey jwtSecretKey) {
        return NimbusJwtDecoder.withSecretKey(jwtSecretKey)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }

    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        String role = jwt.getClaimAsString("role");

        if (role == null || role.isBlank()) {
            return List.of();
        }

        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    private OAuth2TokenValidatorResult validateAccessToken(Jwt jwt) {
        if ("access".equals(jwt.getClaimAsString("type"))) {
            return OAuth2TokenValidatorResult.success();
        }

        OAuth2Error error = new OAuth2Error("invalid_token", "Token type must be access", null);
        return OAuth2TokenValidatorResult.failure(error);
    }
}
