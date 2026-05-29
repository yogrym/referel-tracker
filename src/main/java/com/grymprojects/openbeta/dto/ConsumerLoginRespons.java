package com.grymprojects.openbeta.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Builder
@AllArgsConstructor
@Getter
@Setter
public class ConsumerLoginRespons {
    private String status;
    private String message;
    private String token;
    private String refreshToken;
}
