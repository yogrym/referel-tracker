package com.grymprojects.openbeta.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponsDto {
    private String status;
    private String message;
    private String isOnBoardComplete;
}
