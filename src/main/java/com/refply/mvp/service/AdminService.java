package com.refply.mvp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.refply.mvp.Repository.UserRepo;
import com.refply.mvp.entity.UserEntity;

@Service
public class AdminService {
    
   @Autowired
   private UserRepo userRepo;
    
    public List<UserEntity> getUser(){
       List<UserEntity> users = userRepo.findAll();
       return users;
    }
}
