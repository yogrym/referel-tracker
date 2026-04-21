package com.refply.mvp.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.refply.mvp.entity.ReferelProgramEntity;

@Repository
public interface ReferelProgramRepo extends JpaRepository<ReferelProgramEntity, Long> {

    boolean existsByUrl(String url);

    Optional<ReferelProgramEntity> findByUrl(String url);

}
