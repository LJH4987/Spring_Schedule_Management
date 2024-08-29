package com.example.springschedulemanagement.service;

import com.example.springschedulemanagement.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);
    Optional<UserDTO> getUserById(Long id);
    List<UserDTO> getAllUsers();
    UserDTO updateUser(Long id, UserDTO userDTO);
    void deleteUser(Long id);
}