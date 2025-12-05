package com.eventboard.repository;

import com.eventboard.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by email (used for login or validation)
    Optional<User> findByEmail(String email);
}
