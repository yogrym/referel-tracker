package com.grymprojects.openbeta.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.grymprojects.openbeta.Repository.UserRepository;
import com.grymprojects.openbeta.dto.AdminUserResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    public List<AdminUserResponseDto> fetchUsers() {
        return userRepository.findAll()
                .stream()
                .map(AdminUserResponseDto::from)
                .toList();
    }
}
