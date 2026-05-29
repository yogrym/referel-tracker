package com.grymprojects.openbeta.dto;

import org.springframework.stereotype.Component;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ConsumerRegisterRequest {
    
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String name;

    @NotBlank
    private String mobileNo;

    @NotBlank
    private String adress;
}
