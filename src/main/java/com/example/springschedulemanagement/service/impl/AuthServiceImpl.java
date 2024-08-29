package com.example.springschedulemanagement.service.impl;

import com.example.springschedulemanagement.dto.AuthUserDTO;
import com.example.springschedulemanagement.dto.enums.RoleName;
import com.example.springschedulemanagement.entity.User;
import com.example.springschedulemanagement.entity.UserRole;
import com.example.springschedulemanagement.exception.BaseException;
import com.example.springschedulemanagement.exception.custom.auth.AuthenticationException;
import com.example.springschedulemanagement.exception.custom.database.EmailAlreadyExistsException;
import com.example.springschedulemanagement.repository.UserRepository;
import com.example.springschedulemanagement.config.jwt.JwtTokenProvider;
import com.example.springschedulemanagement.config.security.PasswordEncoder;
import com.example.springschedulemanagement.repository.UserRoleRepository;
import com.example.springschedulemanagement.service.AuthService;
import com.example.springschedulemanagement.service.mapper.AuthServiceMapper;
import com.example.springschedulemanagement.service.utility.ServiceLoggingUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthServiceMapper authServiceMapper;

    @Override
    @Transactional
    public AuthUserDTO register(AuthUserDTO authUserDTO) {
        ServiceLoggingUtil.logInfo(AuthServiceImpl.class, "새로운 사용자 등록 시도: 이메일 {}", authUserDTO.getEmail());

        if (userRepository.existsByEmail(authUserDTO.getEmail())) {
            ServiceLoggingUtil.logWarn(AuthServiceImpl.class, "이미 존재하는 이메일로 등록 시도: {}", authUserDTO.getEmail());
            throw new EmailAlreadyExistsException("이미 존재하는 이메일입니다: " + authUserDTO.getEmail());
        }

        try {
            String hashedPassword = PasswordEncoder.encodePassword(authUserDTO.getHashedPassword());
            authUserDTO.setHashedPassword(hashedPassword);

            User user = authServiceMapper.toEntity(authUserDTO);
            UserRole userRole = createUserRole(user, RoleName.USER);

            user.addRole(userRole);

            User savedUser = userRepository.save(user);
            userRoleRepository.save(userRole);
            ServiceLoggingUtil.logInfo(AuthServiceImpl.class, "사용자 등록 성공: 이메일 {}", savedUser.getEmail());

            return authServiceMapper.toDTO(savedUser);

        } catch (EmailAlreadyExistsException e) {
            throw e;
        } catch  (Exception e) {
            ServiceLoggingUtil.logError(AuthServiceImpl.class, "사용자 등록 중 오류 발생: 이메일 {}", authUserDTO.getEmail(), e);
            throw new BaseException("사용자 등록 중 오류가 발생했습니다. 나중에 다시 시도해주세요.", e);
        }
    }

    @Override
    public String login(String email, String password) {
        ServiceLoggingUtil.logInfo(AuthServiceImpl.class, "사용자 로그인 시도: 이메일 {}", email);

        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> {
                        ServiceLoggingUtil.logWarn(AuthServiceImpl.class, "로그인 실패 - 이메일을 찾을 수 없음: {}", email);
                        return new AuthenticationException("이메일 또는 비밀번호가 잘못되었습니다.");
                    });

            if (!PasswordEncoder.matches(password, user.getHashPassword())) {
                ServiceLoggingUtil.logWarn(AuthServiceImpl.class, "로그인 실패 - 잘못된 비밀번호: {}", email);
                throw new AuthenticationException("이메일 또는 비밀번호가 잘못되었습니다.");
            }

            String token = jwtTokenProvider.createToken(user.getEmail(), user.getRoles());
            ServiceLoggingUtil.logInfo(AuthServiceImpl.class, "사용자 로그인 성공: 이메일 {}", email);
            return token;

        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            ServiceLoggingUtil.logError(AuthServiceImpl.class, "사용자 로그인 중 오류 발생: 이메일 {}", email, e);
            throw new BaseException("로그인 중 오류가 발생했습니다. 나중에 다시 시도해주세요.", e);
        }
    }

    private UserRole createUserRole(User user, RoleName roleName) {
        UserRole userRole = new UserRole();
        userRole.setRoleName(roleName);
        userRole.setUser(user);
        return userRole;
    }
}
