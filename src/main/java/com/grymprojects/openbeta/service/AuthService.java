package com.grymprojects.openbeta.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;

import com.grymprojects.openbeta.Repository.BusinessOnboardingRepository;
import com.grymprojects.openbeta.Repository.ConsumerBusinessMembershipRepository;
import com.grymprojects.openbeta.Repository.ConsumerRepository;
import com.grymprojects.openbeta.Repository.UserRepository;
import com.grymprojects.openbeta.dto.ConsumerLoginRequest;
import com.grymprojects.openbeta.dto.ConsumerLoginRespons;
import com.grymprojects.openbeta.dto.ConsumerRegisterRequest;
import com.grymprojects.openbeta.dto.LoginRequestDto;
import com.grymprojects.openbeta.dto.LoginResponseDto;
import com.grymprojects.openbeta.dto.RefreshTokenRequestDto;
import com.grymprojects.openbeta.dto.RegisterRequestDto;
import com.grymprojects.openbeta.dto.ConsumerRegisterRespons;
import com.grymprojects.openbeta.dto.RegisterResponsDto;
import com.grymprojects.openbeta.model.BusinessOnboarding;
import com.grymprojects.openbeta.model.RefreshToken;
import com.grymprojects.openbeta.model.User;
import com.grymprojects.openbeta.model.Consumer;
import com.grymprojects.openbeta.model.ConsumerBusinessMembership;
import com.grymprojects.openbeta.util.BcryptPasswordEncoder;
import com.grymprojects.openbeta.util.DomainNameUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepo;
    private final ConsumerRepository consumerRepo;
    private final BusinessOnboardingRepository businessOnboardingRepo;
    private final ConsumerBusinessMembershipRepository consumerBusinessMembershipRepo;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    // signup user login here

    public RegisterResponsDto registerUser(RegisterRequestDto data) {
        if (userRepo.existsByEmailOrUserName(data.getEmail(), data.getUsername())) {
            return new RegisterResponsDto("error",
                    "An account is already associated with this email or username. Please log in instead.", "false");
        } else {
            User registerUser = User.builder()
                    .userName(data.getUsername())
                    .email(data.getEmail())
                    .passwordHash(BcryptPasswordEncoder.encodePassword(data.getPassword()))
                    .build();

            userRepo.save(registerUser);

            return new RegisterResponsDto("success", "Account registered successfully.", "false");
        }
    }

    @Transactional
    public LoginResponseDto loginUser(LoginRequestDto data) {
        User user = userRepo.findByEmailOrUserName(data.getEmail(), data.getEmail())
                .orElse(null);

        if (user == null) {
            return LoginResponseDto.builder()
                    .status("error")
                    .message("Invalid username/email or password")
                    .build();
        }

        if (!Boolean.TRUE.equals(user.getEnabled())) {
            return LoginResponseDto.builder()
                    .status("error")
                    .message("Account is disabled")
                    .build();
        }

        if (!BcryptPasswordEncoder.matches(data.getPassword(), user.getPasswordHash())) {
            return LoginResponseDto.builder()
                    .status("error")
                    .message("Invalid username/email or password")
                    .build();
        }

        return createTokenResponse(user, "Login successful");
    }

    @Transactional
    public LoginResponseDto refreshToken(RefreshTokenRequestDto data) {
        try {
            Jwt jwt = jwtService.validateRefreshToken(data.getRefreshToken());
            RefreshToken storedToken = refreshTokenService.validateStoredToken(data.getRefreshToken(), jwt);
            User user = storedToken.getUser();

            if (!Boolean.TRUE.equals(user.getEnabled())) {
                refreshTokenService.revokeAllActiveTokens(user);
                return LoginResponseDto.builder()
                        .status("error")
                        .message("Account is disabled")
                        .build();
            }

            refreshTokenService.revoke(storedToken);
            return createTokenResponse(user, "Token refreshed successfully");
        } catch (JwtException exception) {
            return LoginResponseDto.builder()
                    .status("error")
                    .message(exception.getMessage())
                    .build();
        }
    }

    @Transactional
    public LoginResponseDto logout(RefreshTokenRequestDto data) {
        refreshTokenService.revoke(data.getRefreshToken());

        return LoginResponseDto.builder()
                .status("success")
                .message("Logged out successfully")
                .build();
    }

    private LoginResponseDto createTokenResponse(User user, String message) {

        String token = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        Jwt refreshJwt = jwtService.decodeRefreshToken(refreshToken);
        refreshTokenService.save(user, refreshToken, refreshJwt);

        return LoginResponseDto.builder()
                .status("success")
                .message(message)
                .token(token)
                .refreshToken(refreshToken)
                .build();
    }


    //for consumer 
     private ConsumerLoginRespons createTokenResponsConsumer(Consumer cnsm, String message) {

        String token = jwtService.generateAccessTokenForConsumer(cnsm);
        String refreshToken = jwtService.generateRefreshToken(cnsm);
        Jwt refreshJwt = jwtService.decodeRefreshToken(refreshToken);
        refreshTokenService.save(cnsm, refreshToken, refreshJwt);

        return ConsumerLoginRespons.builder()
                .status("success")
                .message(message)
                .token(token)
                .refreshToken(refreshToken)
                .build();
    }



    @Transactional
    public ConsumerRegisterRespons registerCnsm(ConsumerRegisterRequest data) { 

        BusinessOnboarding business = findBusinessForConsumerSignup(data);

        if (business == null) {
            return new ConsumerRegisterRespons("error", "Invalid affiliate portal link");
        }

        if(consumerRepo.existsByEmail(data.getEmail())){
            return new  ConsumerRegisterRespons("error",
            "An account is already associated with this email or username. Please log in instead.");
        }

        Consumer registerConsumer = Consumer.builder()
                  .email(data.getEmail())
                  .password(BcryptPasswordEncoder.encodePassword(data.getPassword()))
                  .mobile(data.getMobileNo())
                  .build();


       consumerRepo.save(registerConsumer);
       consumerBusinessMembershipRepo.save(ConsumerBusinessMembership.builder()
                  .consumer(registerConsumer)
                  .business(business)
                  .build());
       return new ConsumerRegisterRespons("success","account created successfull");

    }

    private BusinessOnboarding findBusinessForConsumerSignup(ConsumerRegisterRequest data) {
        if (data.getBusinessPortalCode() != null && !data.getBusinessPortalCode().isBlank()) {
            return businessOnboardingRepo.findByPortalCode(data.getBusinessPortalCode()).orElse(null);
        }

        String domainName = DomainNameUtils.normalize(data.getBusinessDomainName());

        if (domainName == null) {
            return null;
        }

        return businessOnboardingRepo.findByDomainName(domainName).orElse(null);
    }


    public ConsumerLoginRespons LoginConsumer(ConsumerLoginRequest data) {
        if(!consumerRepo.existsByEmail(data.getEmail())){
             return new ConsumerLoginRespons("error","this email dose't exists",null,null);
        }

        Consumer filledConsumer = consumerRepo.findByEmail(data.getEmail());
        

        if(filledConsumer == null || !BcryptPasswordEncoder.matches(data.getPassword(), filledConsumer.getPassword())) {
            return new ConsumerLoginRespons("error","invalid username or password",null,null);
        }

        return createTokenResponsConsumer(filledConsumer, "Login sucessfull");

        


    }
}
