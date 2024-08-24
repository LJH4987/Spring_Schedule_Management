package com.example.springschedulemanagement.repository;

import com.example.springschedulemanagement.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends GenericRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
}
