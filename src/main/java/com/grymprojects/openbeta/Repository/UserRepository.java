package com.grymprojects.openbeta.Repository;

import com.grymprojects.openbeta.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    User findByUserName(String username);

    Optional<User> findByEmailOrUserName(String email, String username);

    boolean existsByEmail(String email);

    boolean existsByUserName(String username);

    boolean existsByEmailOrUserName(String email, String username);

}
