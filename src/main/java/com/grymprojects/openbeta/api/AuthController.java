package com.grymprojects.openbeta.api;

import com.grymprojects.openbeta.dto.LoginRequestDto;
import com.grymprojects.openbeta.dto.LoginResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto request) {

        return ResponseEntity.ok(
                LoginResponseDto.builder()
                        .status("success")
                        .message("Login endpoint ready")
                        .build());
    }
}