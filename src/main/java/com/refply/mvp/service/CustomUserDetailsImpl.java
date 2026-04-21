package com.refply.mvp.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.refply.mvp.Repository.UserRepo;
import com.refply.mvp.entity.RoleEnum;
import com.refply.mvp.entity.UserEntity;

@Service
public class CustomUserDetailsImpl implements UserDetailsService {

        @Autowired
        private UserRepo userRepo;

        @Override
        public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
                UserEntity userEntity = userRepo.findByEmail(email)
                                .orElseThrow(() -> new UsernameNotFoundException(
                                                "User not found with email: " + email));

                List<String> roles = userEntity.getRoles();
                String[] roleArray = (roles != null && !roles.isEmpty())
                                ? roles.toArray(new String[0])
                                : new String[] { RoleEnum.OWNER.toString() };

                return User.builder()
                                .username(userEntity.getEmail())
                                .password(userEntity.getPassword())
                                .roles(roleArray)
                                .build();
        }
}
