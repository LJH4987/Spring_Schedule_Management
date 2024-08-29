package com.example.springschedulemanagement.service.impl;

import com.example.springschedulemanagement.dto.UserDTO;
import com.example.springschedulemanagement.entity.User;
import com.example.springschedulemanagement.exception.BaseException;
import com.example.springschedulemanagement.exception.custom.database.EmailAlreadyExistsException;
import com.example.springschedulemanagement.exception.custom.resource.UserNotFoundException;
import com.example.springschedulemanagement.repository.UserRepository;
import com.example.springschedulemanagement.service.UserService;
import com.example.springschedulemanagement.config.security.PasswordEncoder;
import com.example.springschedulemanagement.service.mapper.UserMapper;
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
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        try {
            checkEmailExistence(userDTO.getEmail());

            userDTO.setHashedPassword(PasswordEncoder.encodePassword(userDTO.getHashedPassword()));
            User user = userMapper.toEntity(userDTO);
            User savedUser = userRepository.save(user);

            ServiceLoggingUtil.logInfo(UserServiceImpl.class, "사용자가 성공적으로 생성되었습니다. 사용자 ID: {}", savedUser.getId());
            return userMapper.toDTO(savedUser);
        } catch (EmailAlreadyExistsException e) {
            ServiceLoggingUtil.logError(UserServiceImpl.class, "사용자 생성 중 오류가 발생했습니다: {}", userDTO.getEmail(), e);
            throw e;
        } catch (Exception e) {
            ServiceLoggingUtil.logError(UserServiceImpl.class, "예상치 못한 오류가 발생했습니다. 사용자 생성 중 문제가 발생했습니다: {}", userDTO.getEmail(), e);
            throw new BaseException("예상치 못한 오류가 발생했습니다. 사용자 생성 중 문제가 발생했습니다. 나중에 다시 시도해주세요.", e);
        }
    }

    @Override
    public Optional<UserDTO> getUserById(Long id) {
        try {
            ServiceLoggingUtil.logInfo(UserServiceImpl.class, "사용자 조회 중입니다. 사용자 ID: {}", id);

            return userRepository.findById(id)
                    .map(userMapper::toDTO)
                    .or(() -> {
                        ServiceLoggingUtil.logWarn(UserServiceImpl.class, "해당 ID를 가진 사용자를 찾을 수 없습니다: {}", id);
                        return Optional.empty();
                    });
        } catch (Exception e) {
            ServiceLoggingUtil.logError(UserServiceImpl.class, "예상치 못한 오류가 발생했습니다. 사용자 조회 중 문제가 발생했습니다. 사용자 ID: {}", id, e);
            throw new BaseException("예상치 못한 오류가 발생했습니다. 사용자 조회 중 문제가 발생했습니다. 나중에 다시 시도해주세요.", e);
        }
    }

    @Override
    public List<UserDTO> getAllUsers() {
        try {
            ServiceLoggingUtil.logInfo(UserServiceImpl.class, "모든 사용자 조회 중입니다.");

            return userRepository.findAll()
                    .stream()
                    .map(userMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            ServiceLoggingUtil.logError(UserServiceImpl.class, "예상치 못한 오류가 발생했습니다. 모든 사용자 조회 중 문제가 발생했습니다.", e);
            throw new BaseException("예상치 못한 오류가 발생했습니다. 모든 사용자 조회 중 문제가 발생했습니다. 나중에 다시 시도해주세요.", e);
        }
    }

    @Override
    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        try {
            User user = findUserById(id);
            checkEmailConflict(userDTO, user);

            user.setName(userDTO.getName());
            user.setEmail(userDTO.getEmail());
            user.setUpdatedDate(LocalDateTime.now());
            if (userDTO.getHashedPassword() != null) {
                user.setHashPassword(PasswordEncoder.encodePassword(userDTO.getHashedPassword()));
            }

            User updatedUser = userRepository.save(user);
            ServiceLoggingUtil.logInfo(UserServiceImpl.class, "사용자가 성공적으로 수정되었습니다. 사용자 ID: {}", updatedUser.getId());

            return userMapper.toDTO(updatedUser);
        } catch (UserNotFoundException | EmailAlreadyExistsException e) {
            ServiceLoggingUtil.logError(UserServiceImpl.class, "사용자 수정 중 오류가 발생했습니다. 사용자 ID: {}", id, e);
            throw e;
        } catch (Exception e) {
            ServiceLoggingUtil.logError(UserServiceImpl.class, "예상치 못한 오류가 발생했습니다. 사용자 수정 중 문제가 발생했습니다. 사용자 ID: {}", id, e);
            throw new BaseException("예상치 못한 오류가 발생했습니다. 사용자 수정 중 문제가 발생했습니다. 나중에 다시 시도해주세요.", e);
        }
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        try {
            User user = findUserById(id);

            userRepository.delete(user);
            ServiceLoggingUtil.logInfo(UserServiceImpl.class, "사용자가 성공적으로 삭제되었습니다. 사용자 ID: {}", id);
        } catch (UserNotFoundException e) {
            ServiceLoggingUtil.logError(UserServiceImpl.class, "사용자 삭제 중 오류가 발생했습니다. 사용자 ID: {}", id, e);
            throw e;
        } catch (Exception e) {
            ServiceLoggingUtil.logError(UserServiceImpl.class, "예상치 못한 오류가 발생했습니다. 사용자 삭제 중 문제가 발생했습니다. 사용자 ID: {}", id, e);
            throw new BaseException("예상치 못한 오류가 발생했습니다. 사용자 삭제 중 문제가 발생했습니다. 나중에 다시 시도해주세요.", e);
        }
    }

    private void checkEmailExistence(String email) {
        if (userRepository.existsByEmail(email)) {
            ServiceLoggingUtil.logWarn(UserServiceImpl.class, "이미 존재하는 이메일입니다: {}", email);
            throw new EmailAlreadyExistsException("이미 존재하는 이메일입니다: " + email);
        }
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다. 사용자 ID: " + id));
    }

    private void checkEmailConflict(UserDTO userDTO, User user) {
        if (!user.getEmail().equals(userDTO.getEmail()) && userRepository.existsByEmail(userDTO.getEmail())) {
            ServiceLoggingUtil.logWarn(UserServiceImpl.class, "이미 존재하는 이메일입니다: {}", userDTO.getEmail());
            throw new EmailAlreadyExistsException("이미 존재하는 이메일입니다: " + userDTO.getEmail());
        }
    }

}
