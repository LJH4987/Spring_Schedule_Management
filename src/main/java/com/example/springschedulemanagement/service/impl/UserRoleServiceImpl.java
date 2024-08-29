package com.example.springschedulemanagement.service.impl;

import com.example.springschedulemanagement.dto.UserRoleDTO;
import com.example.springschedulemanagement.dto.enums.RoleName;
import com.example.springschedulemanagement.entity.UserRole;
import com.example.springschedulemanagement.exception.BaseException;
import com.example.springschedulemanagement.exception.custom.auth.DuplicateRoleAssignmentException;
import com.example.springschedulemanagement.exception.custom.resource.RoleNotFoundException;
import com.example.springschedulemanagement.repository.UserRoleRepository;
import com.example.springschedulemanagement.service.UserRoleService;
import com.example.springschedulemanagement.service.mapper.UserRoleMapper;
import com.example.springschedulemanagement.service.utility.ServiceLoggingUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository roleRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UserRoleDTO> getAllRoles() {
        try {
            ServiceLoggingUtil.logInfo(UserRoleServiceImpl.class, "모든 권한을 조회 중입니다.");

            List<UserRoleDTO> roles = roleRepository.findAll()
                    .stream()
                    .map(UserRoleMapper::toDTO)
                    .collect(Collectors.toList());

            ServiceLoggingUtil.logInfo(UserRoleServiceImpl.class, "모든 권한 조회가 완료되었습니다.");
            return roles;

        } catch (Exception e) {
            ServiceLoggingUtil.logError(UserRoleServiceImpl.class, "모든 권한 조회 중 오류가 발생했습니다.", e);
            throw new BaseException("모든 권한 조회 중 문제가 발생했습니다. 나중에 다시 시도해주세요.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserRoleDTO> getRoleById(Long id) {
        try {
            ServiceLoggingUtil.logInfo(UserRoleServiceImpl.class, "권한을 조회 중입니다. 역할 ID: {}", id);

            Optional<UserRoleDTO> roleDTO = roleRepository.findById(id)
                    .map(UserRoleMapper::toDTO);

            if (roleDTO.isPresent()) {
                ServiceLoggingUtil.logInfo(UserRoleServiceImpl.class, "권한 조회 성공. 역할 ID: {}", id);
            } else {
                ServiceLoggingUtil.logWarn(UserRoleServiceImpl.class, "해당 ID를 가진 권한을 찾을 수 없습니다. 권한 ID: {}", id);
            }

            return roleDTO;

        } catch (Exception e) {
            ServiceLoggingUtil.logError(UserRoleServiceImpl.class, "권한 조회 중 예상치 못한 오류가 발생했습니다. 권한 ID: {}", id, e);
            throw new BaseException("권한 조회 중 문제가 발생했습니다. 나중에 다시 시도해주세요.", e);
        }
    }

    @Override
    @Transactional
    public UserRoleDTO assignRole(Long id, UserRoleDTO roleDTO) {
        try {
            ServiceLoggingUtil.logInfo(UserRoleServiceImpl.class, "권한을 부여 중입니다. 역할 ID: {}", id);

            UserRole role = findRoleById(id);

            if (role.getRoleName().equals(roleDTO.getRoleName())) {
                throw new DuplicateRoleAssignmentException("이미 사용자는 해당 권한을 가지고 있습니다.");
            }

            validateDuplicateRoleAssignment(roleDTO.getUserId(), roleDTO.getRoleName());

            updateRoleDetails(role, roleDTO);

            UserRole updatedRole = roleRepository.save(role);
            ServiceLoggingUtil.logInfo(UserRoleServiceImpl.class, "권한이 성공적으로 수정되었습니다. 역할 ID: {}", id);

            return UserRoleMapper.toDTO(updatedRole);

        } catch (RoleNotFoundException | DuplicateRoleAssignmentException e) {
            ServiceLoggingUtil.logError(UserRoleServiceImpl.class, "권한 부여에 실패했습니다. 역할 ID: {}", id, e);
            throw e;
        } catch (Exception e) {
            ServiceLoggingUtil.logError(UserRoleServiceImpl.class, "권한 부여 중 예상치 못한 오류가 발생했습니다. 권한 ID: {}", id, e);
            throw new BaseException("권한 부여 중 문제가 발생했습니다. 나중에 다시 시도해주세요.", e);
        }
    }


    private UserRole findRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("해당 ID를 가진 권한을 찾을 수 없습니다: " + id));
    }

    private void updateRoleDetails(UserRole role, UserRoleDTO roleDTO) {
        role.setRoleName(roleDTO.getRoleName());
        role.setUpdatedDate(LocalDateTime.now());
    }

    private void validateDuplicateRoleAssignment(Long userId, RoleName roleName) {
        if (roleRepository.existsByUserIdAndRoleName(userId, roleName)) {
            throw new DuplicateRoleAssignmentException("이미 해당 사용자에게 해당 권한이 할당되어 있습니다." + " 사용자ID: " + userId + ", 권한: " + roleName);
        }
    }

}