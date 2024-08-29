package com.example.springschedulemanagement.repository;

import com.example.springschedulemanagement.dto.enums.RoleName;
import com.example.springschedulemanagement.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    boolean existsByUserIdAndRoleName(Long userId, RoleName roleName);

}

