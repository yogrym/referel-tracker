package com.grymprojects.openbeta.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsernameChangeWebhookDto {
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    @NotBlank(message = "Old username cannot be blank")
    private String oldUsername;
    
    @NotBlank(message = "New username cannot be blank")
    private String newUsername;
}
