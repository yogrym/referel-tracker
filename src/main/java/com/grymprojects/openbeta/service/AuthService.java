package com.grymprojects.openbeta.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;

import com.grymprojects.openbeta.Repository.UserRepository;
import com.grymprojects.openbeta.dto.LoginRequestDto;
import com.grymprojects.openbeta.dto.LoginResponseDto;
import com.grymprojects.openbeta.dto.RefreshTokenRequestDto;
import com.grymprojects.openbeta.dto.RegisterRequestDto;
import com.grymprojects.openbeta.dto.RegisterResponsDto;
import com.grymprojects.openbeta.model.RefreshToken;
import com.grymprojects.openbeta.model.User;
import com.grymprojects.openbeta.util.BcryptPasswordEncoder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepo;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    // signup user login here

    public RegisterResponsDto registerUser(RegisterRequestDto data) {
        if (userRepo.existsByEmailOrUserName(data.getEmail(), data.getUsername())) {
            return new RegisterResponsDto("error",
                    "An account is already associated with this email or username. Please log in instead.", "false");
        } else {
            User registerUser = User.builder()
                    .userName(data.getUsername())
                    .email(data.getEmail())
                    .passwordHash(BcryptPasswordEncoder.encodePassword(data.getPassword()))
                    .build();

            userRepo.save(registerUser);

            return new RegisterResponsDto("success", "Account registered successfully.", "false");
        }
    }

    @Transactional
    public LoginResponseDto loginUser(LoginRequestDto data) {
        User user = userRepo.findByEmailOrUserName(data.getEmail(), data.getEmail())
                .orElse(null);

        if (user == null) {
            return LoginResponseDto.builder()
                    .status("error")
                    .message("Invalid username/email or password")
                    .build();
        }

        if (!Boolean.TRUE.equals(user.getEnabled())) {
            return LoginResponseDto.builder()
                    .status("error")
                    .message("Account is disabled")
                    .build();
        }

        if (!BcryptPasswordEncoder.matches(data.getPassword(), user.getPasswordHash())) {
            return LoginResponseDto.builder()
                    .status("error")
                    .message("Invalid username/email or password")
                    .build();
        }

        return createTokenResponse(user, "Login successful");
    }

    @Transactional
    public LoginResponseDto refreshToken(RefreshTokenRequestDto data) {
        try {
            Jwt jwt = jwtService.validateRefreshToken(data.getRefreshToken());
            RefreshToken storedToken = refreshTokenService.validateStoredToken(data.getRefreshToken(), jwt);
            User user = storedToken.getUser();

            if (!Boolean.TRUE.equals(user.getEnabled())) {
                refreshTokenService.revokeAllActiveTokens(user);
                return LoginResponseDto.builder()
                        .status("error")
                        .message("Account is disabled")
                        .build();
            }

            refreshTokenService.revoke(storedToken);
            return createTokenResponse(user, "Token refreshed successfully");
        } catch (JwtException exception) {
            return LoginResponseDto.builder()
                    .status("error")
                    .message(exception.getMessage())
                    .build();
        }
    }

    @Transactional
    public LoginResponseDto logout(RefreshTokenRequestDto data) {
        refreshTokenService.revoke(data.getRefreshToken());

        return LoginResponseDto.builder()
                .status("success")
                .message("Logged out successfully")
                .build();
    }

    private LoginResponseDto createTokenResponse(User user, String message) {

        String token = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        Jwt refreshJwt = jwtService.decodeRefreshToken(refreshToken);
        refreshTokenService.save(user, refreshToken, refreshJwt);

        return LoginResponseDto.builder()
                .status("success")
                .message(message)
                .token(token)
                .refreshToken(refreshToken)
                .build();
    }
}
