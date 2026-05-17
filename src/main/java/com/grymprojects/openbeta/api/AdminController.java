package com.grymprojects.openbeta.api;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grymprojects.openbeta.Repository.UserRepository;
import com.grymprojects.openbeta.dto.AdminUserResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public List<AdminUserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(AdminUserResponseDto::from)
                .toList();
    }
}
