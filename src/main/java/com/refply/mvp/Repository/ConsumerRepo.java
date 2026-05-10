package com.refply.mvp.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.refply.mvp.entity.ConsumerEntity;
import com.refply.mvp.entity.UserEntity;


   @Repository
    public interface  ConsumerRepo extends  JpaRepository<ConsumerEntity,Long>{

    boolean existsByEmail(String email);
    boolean existsByPhone(int phone);

    Optional<ConsumerEntity> findByEmail(String email);

    Optional<ConsumerEntity> findById(Long id);
        
    }


