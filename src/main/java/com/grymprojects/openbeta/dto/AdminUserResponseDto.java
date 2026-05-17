package com.grymprojects.openbeta.dto;

import java.sql.Timestamp;

import com.grymprojects.openbeta.enums.Role;
import com.grymprojects.openbeta.enums.subscriptionType;
import com.grymprojects.openbeta.model.User;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminUserResponseDto {

    private Long id;
    private String userName;
    private String email;
    private Role role;
    private subscriptionType subscriptionType;
    private Boolean onboardingStatus;
    private Boolean enabled;
    private Timestamp createdTime;
    private Timestamp updateTimestamp;

    public static AdminUserResponseDto from(User user) {
        return AdminUserResponseDto.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .email(user.getEmail())
                .role(user.getRole())
                .subscriptionType(user.getSubscriptionType())
                .onboardingStatus(user.getOnboardingStatus())
                .enabled(user.getEnabled())
                .createdTime(user.getCreatedTime())
                .updateTimestamp(user.getUpdateTimestamp())
                .build();
    }
}
