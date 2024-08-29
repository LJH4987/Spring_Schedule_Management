package com.example.springschedulemanagement.service;

import com.example.springschedulemanagement.dto.UserRoleDTO;

import java.util.List;
import java.util.Optional;

public interface UserRoleService {
    Optional<UserRoleDTO> getRoleById(Long id);
    List<UserRoleDTO> getAllRoles();
    UserRoleDTO assignRole(Long id, UserRoleDTO userRoleDTO);
}