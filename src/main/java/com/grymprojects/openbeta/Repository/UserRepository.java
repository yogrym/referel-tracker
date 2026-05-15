package com.grymprojects.openbeta.Repository;

import com.grymprojects.openbeta.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    User findByUserName(String username);

    boolean existsByEmail(String email);

    boolean existsByUserName(String username);

}
