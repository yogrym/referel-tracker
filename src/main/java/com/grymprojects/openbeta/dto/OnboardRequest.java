package com.grymprojects.openbeta.dto;

import com.grymprojects.openbeta.enums.BusinessRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OnboardRequest {

    @NotBlank(message = "Business name is required")
    private String businessName;

    @NotBlank(message = "Business email is required")
    @Email(message = "Invalid business email format")
    private String businessEmail;

    private String businessAddress;

    private String city;

    private String state;

    private String pincode;

    private String webAddress;

    private String gstNumber;

    @NotNull(message = "Business role is required")
    private BusinessRole businessRole;
}
