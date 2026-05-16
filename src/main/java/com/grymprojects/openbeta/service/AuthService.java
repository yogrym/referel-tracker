package com.grymprojects.openbeta.service;

import org.springframework.stereotype.Service;

import com.grymprojects.openbeta.Repository.UserRepository;
import com.grymprojects.openbeta.dto.LoginRequestDto;
import com.grymprojects.openbeta.dto.LoginResponseDto;
import com.grymprojects.openbeta.dto.RegisterRequest;
import com.grymprojects.openbeta.dto.RegisterRespons;
import com.grymprojects.openbeta.model.User;
import com.grymprojects.openbeta.util.BcryptPasswordEncoder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepo;

    // signup user login here

    public RegisterRespons registerUser(RegisterRequest data) {
        if (userRepo.existsByEmailOrUserName(data.getEmail(), data.getUsername())) {
            return new RegisterRespons("error",
                    "An account is already associated with this email or username. Please log in instead.", "false");
        } else {
            User registerUser = User.builder()
                    .userName(data.getUsername())
                    .email(data.getEmail())
                    .passwordHash(BcryptPasswordEncoder.encodePassword(data.getPassword()))
                    .build();

            userRepo.save(registerUser);

            return new RegisterRespons("success", "Account registered successfully.", "false");
        }
    }

    public LoginResponseDto loginUser(LoginRequestDto data) {
        User user = userRepo.findByEmailOrUserName(data.getUserNameOrEmail(), data.getUserNameOrEmail())
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

        return LoginResponseDto.builder()
                .status("success")
                .message("Login successful")
                .build();
    }
}
