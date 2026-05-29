package com.grymprojects.openbeta.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grymprojects.openbeta.model.Consumer;

public interface ConsumerRepository extends JpaRepository<Consumer, Long> {

    Consumer findByEmail(String email);

    boolean existsByEmail(String email);

}
