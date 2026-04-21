package com.refply.mvp.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.refply.mvp.entity.UserEntity;
import com.refply.mvp.service.AdminService;

@RestController
@RequestMapping("/api/v1/super")  // this controller for admin use only
public class SuperUserController {

   @Autowired
   private AdminService adminService;

   @GetMapping("/users")
   public ResponseEntity<?> getUser() {
      try {
         List<UserEntity> users = adminService.getUser();
         return ResponseEntity.ok(users);
      } catch (Exception e) {
         return ResponseEntity.badRequest().body(Map.of("error :", e.getMessage()));
      }

   }
}
