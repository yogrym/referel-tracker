package com.refply.mvp.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.refply.mvp.entity.ConsumerEntity;

public interface ConsumerRepo {

    public interface  ConsumerRepository extends  JpaRepository<ConsumerEntity,Long>{

    boolean existsByEmail(String email);

    Optional<ConsumerEntity> findByEmail(String email);

    Optional<ConsumerEntity> findById(Long id);
        
    }
}