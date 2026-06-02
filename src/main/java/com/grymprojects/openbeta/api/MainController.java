package com.grymprojects.openbeta.api;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grymprojects.openbeta.dto.OnboardRequest;
import com.grymprojects.openbeta.dto.UsernameChangeWebhookDto;
import com.grymprojects.openbeta.service.GlobalService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MainController {

    private final GlobalService globalService;

    @PostMapping("/onboard/complete")
    public ResponseEntity<Map<String, String>> completeOnboard(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody OnboardRequest request) {
        return globalService.completeOnboard(jwt.getSubject(), request);
    }


    

    

    
}
