package com.grymprojects.openbeta.service;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grymprojects.openbeta.Repository.BusinessOnboardingRepository;
import com.grymprojects.openbeta.Repository.UserRepository;
import com.grymprojects.openbeta.dto.OnboardRequest;
import com.grymprojects.openbeta.dto.UsernameChangeWebhookDto;
import com.grymprojects.openbeta.model.BusinessOnboarding;
import com.grymprojects.openbeta.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GlobalService {

    private final UserRepository userRepository;
    private final BusinessOnboardingRepository businessOnboardingRepository;

    @Transactional
    public ResponseEntity<Map<String, String>> completeOnboard(String userEmail, OnboardRequest request) {
        User user = userRepository.findByEmail(userEmail);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("status", "error", "message", "User not found"));
        }

        if (Boolean.TRUE.equals(user.getOnboardingStatus())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("status", "error", "message", "Onboarding is already complete"));
        }

        if (businessOnboardingRepository.existsByBusinessEmail(request.getBusinessEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("status", "error", "message", "Business email is already registered"));
        }

        if (request.getGstNumber() != null
                && !request.getGstNumber().isBlank()
                && businessOnboardingRepository.existsByGstNumber(request.getGstNumber())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("status", "error", "message", "GST number is already registered"));
        }

        BusinessOnboarding onboarding = new BusinessOnboarding();
        onboarding.setBusinessName(request.getBusinessName());
        onboarding.setBusinessEmail(request.getBusinessEmail());
        onboarding.setBusinessAddress(request.getBusinessAddress());
        onboarding.setCity(request.getCity());
        onboarding.setState(request.getState());
        onboarding.setPincode(request.getPincode());
        onboarding.setWebAddress(request.getWebAddress());
        onboarding.setGstNumber(request.getGstNumber());
        onboarding.setBusinessRole(request.getBusinessRole());
        onboarding.setUser(user);

        businessOnboardingRepository.save(onboarding);

        user.setOnboardingStatus(true);
        userRepository.save(user);

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Onboarding completed successfully"));
    }

    public ResponseEntity<Map<String, String>> checkUsernameChange(UsernameChangeWebhookDto webhook) {
        User user = userRepository.findById(webhook.getUserId()).orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("status", "error", "message", "User not found"));
        }

        // Check if current username matches the old username provided
        if (!user.getUserName().equals(webhook.getOldUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("status", "error", "message", "Current username does not match the provided old username"));
        }

        // Check if new username is different from old username
        if (webhook.getOldUsername().equals(webhook.getNewUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("status", "error", "message", "New username must be different from the old username"));
        }

        // Log the username change attempt
        System.out.println("Username change webhook triggered - User ID: " + webhook.getUserId() +
                ", Old Username: " + webhook.getOldUsername() +
                ", New Username: " + webhook.getNewUsername());

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Username change validated successfully",
                "userId", webhook.getUserId().toString(),
                "oldUsername", webhook.getOldUsername(),
                "newUsername", webhook.getNewUsername()));
    }
}
