package com.grymprojects.openbeta.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.grymprojects.openbeta.model.Consumer;
import com.grymprojects.openbeta.model.ConsumerRefreshToken;

public interface ConsumerTokenRepository extends JpaRepository<ConsumerRefreshToken,Long> {
    
    @EntityGraph(attributePaths = "cnsm")
    Optional<ConsumerRefreshToken> findByTokenHash(String tokenHash);

    List<ConsumerRefreshToken> findAllByCnsmAndRevokedFalse(Consumer cnsm);
}
