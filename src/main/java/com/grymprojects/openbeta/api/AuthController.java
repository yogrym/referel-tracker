package com.grymprojects.openbeta.api;

import com.grymprojects.openbeta.dto.LoginRequestDto;
import com.grymprojects.openbeta.dto.LoginResponseDto;
import com.grymprojects.openbeta.dto.RegisterRequest;
import com.grymprojects.openbeta.dto.RegisterRespons;
import com.grymprojects.openbeta.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

  @PostMapping("/register")
public ResponseEntity<RegisterRespons> registerUser(@Valid @RequestBody RegisterRequest request) {
    RegisterRespons response = authService.registerUser(request);

    if ("error".equals(response.getStatus())) {
        return ResponseEntity.badRequest().body(response);
    }

    return ResponseEntity.ok(response);
}



    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto request) {

        return ResponseEntity.ok(
                LoginResponseDto.builder()
                        .status("success")
                        .message("Login endpoint ready")
                        .build());
    }
}
