package com.example.springschedulemanagement.repository;

import com.example.springschedulemanagement.entity.User;

import java.util.Optional;

public interface UserRepository extends GenericRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
