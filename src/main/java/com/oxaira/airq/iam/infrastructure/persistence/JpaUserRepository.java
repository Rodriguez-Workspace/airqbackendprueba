package com.oxaira.airq.iam.infrastructure.persistence;

import com.oxaira.airq.iam.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaUserRepository
        extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

}