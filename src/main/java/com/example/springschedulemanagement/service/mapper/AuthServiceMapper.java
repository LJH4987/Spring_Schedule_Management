package com.example.springschedulemanagement.service.mapper;

import com.example.springschedulemanagement.dto.AuthUserDTO;
import com.example.springschedulemanagement.entity.User;
import com.example.springschedulemanagement.exception.BaseException;
import com.example.springschedulemanagement.service.utility.ServiceLoggingUtil;
import org.springframework.stereotype.Component;

@Component
public class AuthServiceMapper {

    public User toEntity(AuthUserDTO authUserDTO) {
        try {
            if (authUserDTO == null) {
                throw new IllegalArgumentException("유효하지 않은 AuthUserDTO입니다.");
            }

            User user = new User();
            user.setId(authUserDTO.getId());
            user.setName(authUserDTO.getName());
            user.setEmail(authUserDTO.getEmail());
            user.setHashPassword(authUserDTO.getHashedPassword());

            ServiceLoggingUtil.logDebug(AuthServiceMapper.class, "AuthUserDTO를 User 엔티티로 변환했습니다. 유저 이메일 : {}", authUserDTO.getEmail());
            return user;

        } catch (Exception e) {
            ServiceLoggingUtil.logError(AuthServiceMapper.class, "AuthUserDTO를 User 엔티티로 변환하는 중 오류가 발생했습니다.", e);
            throw new BaseException("AuthUserDTO를 User 엔티티로 변환하는 중 문제가 발생했습니다.", e);
        }
    }


    public AuthUserDTO toDTO(User user) {
        try {
            if (user == null) {
                throw new IllegalArgumentException("유효하지 않은 User 엔티티입니다.");
            }

            AuthUserDTO authUserDTO = new AuthUserDTO(
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getHashPassword()
            );

            ServiceLoggingUtil.logDebug(AuthServiceMapper.class, "User 엔티티를 AuthUserDTO로 변환했습니다. 유저 ID : {}", user.getId());
            return authUserDTO;

        } catch (Exception e) {
            ServiceLoggingUtil.logError(AuthServiceMapper.class, "User 엔티티를 AuthUserDTO로 변환하는 중 오류가 발생했습니다.", e);
            throw new BaseException("User 엔티티를 AuthUserDTO로 변환하는 중 문제가 발생했습니다.", e);
        }
    }

}