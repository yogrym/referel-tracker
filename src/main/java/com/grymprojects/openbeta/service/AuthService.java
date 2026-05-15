package com.grymprojects.openbeta.service;

import org.springframework.stereotype.Service;

import com.grymprojects.openbeta.Repository.UserRepository;
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
        if (userRepo.existsByEmail(data.getEmail()) || userRepo.existsByUserName(data.getUsername())) {
            return new RegisterRespons("error",
                    "An account is already associated with this email. Please log in instead.", "false");
        } else {
            User registerUser = User.builder()
                    .userName(data.getEmail())
                    .email(data.getEmail())
                    .passwordHash(BcryptPasswordEncoder.encodePassword(data.getPassword()))
                    .build();

            userRepo.save(registerUser);

            return new RegisterRespons("success", "Account registered successfully.", "false");
        }
    }
}
