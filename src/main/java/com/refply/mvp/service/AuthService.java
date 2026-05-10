package com.refply.mvp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.refply.mvp.Repository.ConsumerRepo;
import com.refply.mvp.Repository.UserRepo;
import com.refply.mvp.dto.ConsumerSignUpDto;
import com.refply.mvp.entity.ConsumerEntity;
import com.refply.mvp.entity.RoleEnum;
import com.refply.mvp.entity.UserEntity;

@Service
public class AuthService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ConsumerRepo csmRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public SignupResult signupService(UserEntity user) {
        if (user.getEmail() == null || user.getPassword() == null) {
            return SignupResult.INVALID_DATA;
        }

        if (userRepo.existsByEmail(user.getEmail())) {
            return SignupResult.USER_ALREADY_EXISTS;
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getIsProfileComplete() == null) {
            user.setIsProfileComplete(false);
        }

        user.setRoles(List.of(RoleEnum.OWNER.toString()));
        userRepo.save(user);
        return SignupResult.SUCCESS;
    }

    public Optional<UserEntity> returnUserDetails(String email) {
        return userRepo.findByEmail(email);

    }

    public void updateEmail(UserEntity user) {
        userRepo.save(user);
    }

    public UserEntity findByEmail(String email) {
        UserEntity user = userRepo.findByEmail(email).get();

        if (user != null) {
            return user;
        }
        return null;
    }

    public boolean checkEmailExists(String email) {
        return userRepo.existsByEmail(email);
    }




    // consumer signup service
    public SignupResult consumerSignupService(ConsumerSignUpDto data) {
        if (data == null || data.getEmail() == null || data.getUserName() == null) {
            return SignupResult.INVALID_DATA;
        }

        if (csmRepo.existsByEmail(data.getEmail()) || csmRepo.existsByPhone(data.getPhone())) {
            return SignupResult.USER_ALREADY_EXISTS;
        }

        ConsumerEntity consumer = ConsumerEntity.builder()
                .userName(data.getUserName())
                .email(data.getEmail())
                .phone(data.getPhone())
                .build();

        csmRepo.save(consumer);
        return SignupResult.SUCCESS;
    }


    public ConsumerEntity findCsmByEmail(ConsumerSignUpDto data) {
        Optional <ConsumerEntity> tempConsumer = csmRepo.findByEmail(data.getEmail());
        ConsumerEntity consumer = tempConsumer.get();
        if(tempConsumer.isEmpty()){
           
            return consumer;
        } 
        return consumer;
    }
    

}
