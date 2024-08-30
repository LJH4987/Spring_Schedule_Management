package com.example.springschedulemanagement.config.jwt;

import com.example.springschedulemanagement.exception.custom.auth.AccessDeniedException;
import com.example.springschedulemanagement.exception.custom.auth.InvalidTokenException;
import com.example.springschedulemanagement.exception.custom.auth.MissingTokenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthorizationFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthorizationFilter.class);
    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthorizationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public void validateAdminToken(String token) {
        validateTokenAndRole(token, "ADMIN");
    }

    public void validateUserOrAdminToken(String token) {
        validateTokenAndRole(token, "USER", "ADMIN");
    }

    public void validateTokenAndRole(String token, String... allowedRoles) {
        if (token == null || token.trim().isEmpty()) {
            logger.error("토큰이 제공되지 않았습니다. 로그인이 필요합니다.");
            throw new MissingTokenException("토큰이 제공되지 않았습니다. 로그인이 필요합니다.");
        }

        if (!jwtTokenProvider.validateToken(token)) {
            logger.error("유효하지 않은 토큰입니다. 다시 로그인해 주세요.");
            throw new InvalidTokenException("유효하지 않은 토큰입니다. 다시 로그인해 주세요.");
        }

        if (!jwtTokenProvider.isRoleAllowed(token, allowedRoles)) {
            String userRole = jwtTokenProvider.getRoleFromToken(token);
            logger.error("사용자에게 필요한 권한이 없습니다. 필요한 권한: {}, 사용자 권한: {}", String.join(", ", allowedRoles), userRole);
            throw new AccessDeniedException("권한이 부족합니다, 더 높은 권한이 필요합니다.");
        }
    }
}