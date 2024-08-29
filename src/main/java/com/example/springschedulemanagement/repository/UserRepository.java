package com.example.springschedulemanagement.repository;

import com.example.springschedulemanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.email = :email")
    Optional<User> findByEmail(String email);

    boolean existsById(Long id);

    boolean existsByEmail(String email);

}
