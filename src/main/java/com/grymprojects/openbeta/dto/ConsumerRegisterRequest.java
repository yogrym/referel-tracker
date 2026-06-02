package com.grymprojects.openbeta.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

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
    @JsonProperty("mobile")
    private String mobileNo;

    @NotBlank
    private String adress;
    
    @NotBlank(message = "portal code is required")
    @JsonProperty("portalcode")
    private String businessPortalCode;
    
    @NotBlank
    @JsonProperty("domainame")
    private String businessDomainName;
}
