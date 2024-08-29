package com.example.springschedulemanagement.service.mapper;

import com.example.springschedulemanagement.dto.UserDTO;
import com.example.springschedulemanagement.entity.User;
import com.example.springschedulemanagement.exception.BaseException;
import com.example.springschedulemanagement.service.utility.ServiceLoggingUtil;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserDTO userDTO) {
        if (userDTO == null) {
            ServiceLoggingUtil.logError(UserMapper.class, "유효하지 않은 UserDTO입니다.");
            throw new IllegalArgumentException("유효하지 않은 UserDTO입니다.");
        }

        try {
            User user = new User();
            user.setId(userDTO.getId());
            user.setName(userDTO.getName());
            user.setEmail(userDTO.getEmail());
            user.setHashPassword(userDTO.getHashedPassword());
            user.setCreatedDate(userDTO.getCreatedDate());
            user.setUpdatedDate(userDTO.getUpdatedDate());
            ServiceLoggingUtil.logDebug(UserMapper.class, "UserDTO를 User 엔티티로 변환했습니다. 유저 이메일: {}", userDTO.getEmail());
            return user;

        } catch (Exception e) {
            ServiceLoggingUtil.logError(UserMapper.class, "UserDTO를 User 엔티티로 변환하는 중 오류가 발생했습니다. 유저 이메일: {}", userDTO.getEmail(), e);
            throw new RuntimeException("UserDTO를 User 엔티티로 변환하는 중 문제가 발생했습니다.", e);
        }
    }

    public UserDTO toDTO(User user) {
        if (user == null) {
            ServiceLoggingUtil.logError(UserMapper.class, "유효하지 않은 User 엔티티입니다.");
            throw new IllegalArgumentException("유효하지 않은 User 엔티티입니다.");
        }

        try {
            UserDTO userDTO = new UserDTO(
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getHashPassword(),
                    user.getCreatedDate(),
                    user.getUpdatedDate()
            );

            ServiceLoggingUtil.logDebug(UserMapper.class, "User 엔티티를 UserDTO로 변환했습니다. 유저 ID: {}", user.getId());
            return userDTO;

        } catch (Exception e) {
            ServiceLoggingUtil.logError(UserMapper.class, "User 엔티티를 UserDTO로 변환하는 중 오류가 발생했습니다. 유저 ID: {}", user.getId(), e);
            throw new BaseException("User 엔티티를 UserDTO로 변환하는 중 문제가 발생했습니다.", e);
        }
    }
}