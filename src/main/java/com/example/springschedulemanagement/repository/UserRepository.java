package com.example.springschedulemanagement.repository;

import com.example.springschedulemanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsById(Long id);

    boolean existsByEmail(String email);
}
