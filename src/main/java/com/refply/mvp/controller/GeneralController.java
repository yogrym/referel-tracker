package com.refply.mvp.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.refply.mvp.dto.CreateProgramRequestDto;
import com.refply.mvp.entity.BusinessProfileEntity;
import com.refply.mvp.entity.ReferelProgramEntity;
import com.refply.mvp.entity.UserEntity;
import java.util.List;
import com.refply.mvp.service.GeneralUserService;

@RestController
@RequestMapping("/api/v1/general") // this controller for business use only
public class GeneralController {

    @Autowired
    private GeneralUserService genService;

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok(Map.of("message", "test passed >0<0"));
    }

    @PostMapping("/onboarding")
    public ResponseEntity<?> onboarding(@RequestBody BusinessProfileEntity profileData) {

        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        BusinessProfileEntity profile = genService.setProfileComplete(user, profileData);

        if (profile != null) {
            return ResponseEntity.ok(Map.of("message", "Profile completed successfully", "profile", profile));
        } else {
            return ResponseEntity.status(500).body(Map.of("message", "Failed to complete profile"));
        }

    }


      @GetMapping("/get-business-profile")
    public ResponseEntity<?> getBusinessProfiles() {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<BusinessProfileEntity> profiles = genService.getBusinessProfiles(user);

        if (profiles != null) {
            return ResponseEntity.ok(Map.of("message : ", profiles));
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/create-program")
    public ResponseEntity<?> createProgram(@RequestBody CreateProgramRequestDto request) {
        if (request == null || request.getBusinessId() == null || request.getProgram() == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "businessId and program are required"));
        }

        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ReferelProgramEntity program = genService.createNewProgram(request.getProgram(), user, request.getBusinessId());

        if (program != null) {
            return ResponseEntity.ok(Map.of("message", "Program created successfully", "program", program));
        }

        return ResponseEntity.status(404).body(Map.of("message", "Business profile not found"));
    }

  

    @GetMapping("/ref/{uniq-code}")
    public ResponseEntity<?> loadReferProgram(@PathVariable("uniq-code") String uniqCode) {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        ReferelProgramEntity program = genService.loadReferProgramByCode(uniqCode, user);

        if (program != null) {
            return ResponseEntity.ok(Map.of("error", "Refer program found", "program", Map.of("details:", program)));
        }

        return ResponseEntity.status(404).body(Map.of("message", "nothing found"));
    }



}
