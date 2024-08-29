package com.example.springschedulemanagement.service.mapper;

import com.example.springschedulemanagement.dto.UserRoleDTO;
import com.example.springschedulemanagement.entity.UserRole;
import com.example.springschedulemanagement.exception.BaseException;
import com.example.springschedulemanagement.service.utility.ServiceLoggingUtil;

public class UserRoleMapper {

    public static UserRoleDTO toDTO(UserRole role) {
        try {
            if (role == null) {
                throw new BaseException("유효하지 않은 UserRole 엔티티입니다.");
            }

            UserRoleDTO roleDTO = new UserRoleDTO();
            roleDTO.setId(role.getId());
            roleDTO.setRoleName(role.getRoleName());
            roleDTO.setUserId(role.getUser().getId());
            roleDTO.setUserName(role.getUser().getName());
            roleDTO.setCreatedDate(role.getCreatedDate());
            roleDTO.setUpdatedDate(role.getUpdatedDate());

            ServiceLoggingUtil.logDebug(UserRoleMapper.class, "UserRole 엔티티를 UserRoleDTO로 변환했습니다. 역할 이름: {}", role.getRoleName());
            return roleDTO;

        } catch (Exception e) {
            ServiceLoggingUtil.logError(UserRoleMapper.class, "UserRole 엔티티를 UserRoleDTO로 변환하는 중 오류가 발생했습니다.", e);
            throw new BaseException("UserRole 엔티티를 UserRoleDTO로 변환하는 중 문제가 발생했습니다.", e);
        }
    }

    public static UserRole toEntity(UserRoleDTO roleDTO) {
        try {
            if (roleDTO == null) {
                throw new BaseException("유효하지 않은 UserRoleDTO입니다.");
            }

            UserRole role = new UserRole();
            role.setId(roleDTO.getId());
            role.setRoleName(roleDTO.getRoleName());
            role.setCreatedDate(roleDTO.getCreatedDate());
            role.setUpdatedDate(roleDTO.getUpdatedDate());

            ServiceLoggingUtil.logDebug(UserRoleMapper.class, "UserRoleDTO를 UserRole 엔티티로 변환했습니다. 역할 이름: {}", roleDTO.getRoleName());
            return role;

        } catch (Exception e) {
            ServiceLoggingUtil.logError(UserRoleMapper.class, "UserRoleDTO를 UserRole 엔티티로 변환하는 중 오류가 발생했습니다.", e);
            throw new BaseException("UserRoleDTO를 UserRole 엔티티로 변환하는 중 문제가 발생했습니다.", e);
        }
    }
}