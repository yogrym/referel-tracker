package com.grymprojects.openbeta.api;

import com.grymprojects.openbeta.dto.LoginRequestDto;
import com.grymprojects.openbeta.dto.LoginResponseDto;
import com.grymprojects.openbeta.dto.RefreshTokenRequestDto;
import com.grymprojects.openbeta.dto.RegisterRequestDto;
import com.grymprojects.openbeta.dto.RegisterResponsDto;
import com.grymprojects.openbeta.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponsDto> registerUser(@Valid @RequestBody RegisterRequestDto request) {
        RegisterResponsDto response = authService.registerUser(request);

        if ("error".equals(response.getStatus())) {
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        LoginResponseDto response = authService.loginUser(request);

        if ("error".equals(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refresh(@Valid @RequestBody RefreshTokenRequestDto request) {
        LoginResponseDto response = authService.refreshToken(request);

        if ("error".equals(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<LoginResponseDto> logout(@Valid @RequestBody RefreshTokenRequestDto request) {
        return ResponseEntity.ok(authService.logout(request));
    }
}
