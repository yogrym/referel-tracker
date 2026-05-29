package com.grymprojects.openbeta.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.grymprojects.openbeta.dto.AdminUserResponseDto;
import com.grymprojects.openbeta.service.AdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {


  private final AdminService adminService;

    @GetMapping("/users")
    @PreAuthorize("hasAnyRole('ADMIN', 'DEV')")
    public List<AdminUserResponseDto> getAllUsers() {
        return adminService.fetchUsers();
    }
}
