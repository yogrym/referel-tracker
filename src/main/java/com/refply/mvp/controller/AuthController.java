package com.refply.mvp.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.context.SecurityContextHolder;

import com.refply.mvp.dto.ConsumerSignUpDto;
import com.refply.mvp.entity.ConsumerEntity;
import com.refply.mvp.entity.LoginDataEntity;
import com.refply.mvp.entity.UserEntity;
import com.refply.mvp.service.SignupResult;
import com.refply.mvp.service.AuthService;
import com.refply.mvp.util.JwtUtil;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDataEntity request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            Optional<UserEntity> user = userService.returnUserDetails(request.getEmail());

            String token = jwtUtil.generateToken(user.get());
            String refreshToken = jwtUtil.generateRefreshToken(user.get());
            return ResponseEntity
                    .ok(Map.of("message", "Login successful", "token",
                     token, "refreshToken", refreshToken, "profile" , user.get().getIsProfileComplete()));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(Map.of("message", "Invalid email or password"));
        }
    }


    @PostMapping("/csm-signup")
    public ResponseEntity<?> csmSignup(@RequestBody ConsumerSignUpDto data){
        SignupResult result =  userService.consumerSignupService(data);

        switch (result){
            case SUCCESS :
           ConsumerEntity consumerEntity = userService.findCsmByEmail(data); /*this function finds and return the 
           consumerEntity form the database  */ 

            String token = jwtUtil.generateCsmToken(consumerEntity);
            String refreshToken = jwtUtil.generateCsmRefreshToken(consumerEntity);

               return ResponseEntity.ok(Map.of("message", "user signed up","idToken :",token,"refereshToken:",refreshToken));
            case USER_ALREADY_EXISTS:
                return ResponseEntity.status(409).body(Map.of("message", "User already exists"));
            default:
                return ResponseEntity.internalServerError().body(Map.of("message", "Unknown error"));
        }
        
        }
    

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserEntity userEntity) {
        SignupResult result = userService.signupService(userEntity);

        switch (result) {
            case SUCCESS:
                return ResponseEntity.ok(Map.of("message", "User signed up successfully"));
            case INVALID_DATA:
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid email or password"));
            case USER_ALREADY_EXISTS:
                return ResponseEntity.status(409).body(Map.of("message", "User already exists"));
            default:
                return ResponseEntity.internalServerError().body(Map.of("message", "Unknown error"));
        }
    }

    @GetMapping("/refresh")
    public ResponseEntity<?> refresh() {

        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String newToken = jwtUtil.generateToken(user);

        return ResponseEntity.ok(Map.of(
                "idToken", newToken));
    }

    @PutMapping("/update-email")
    public ResponseEntity<?> updateEmail(@RequestBody Map<String, String> request) {

        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String newEmail = request.get("email").trim();

        if (newEmail == null || newEmail.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email field cannot be empty"));
        }

        try {

            if (userService.checkEmailExists(newEmail)) {
                return ResponseEntity.status(409).body(Map.of("message", "Email already exists"));
            }

            user.setEmail(newEmail);
            userService.updateEmail(user);

            return ResponseEntity.ok(Map.of("message", "Email updated successfully", "email", newEmail));

        } catch (Exception e) {

            return ResponseEntity.internalServerError().body(Map.of("message", "An unexpected error occurred"));
        }
    }

}
