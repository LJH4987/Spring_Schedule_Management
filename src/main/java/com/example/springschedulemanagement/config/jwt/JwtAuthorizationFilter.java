package com.example.springschedulemanagement.config.jwt;

import com.example.springschedulemanagement.exception.custom.auth.AccessDeniedException;
import com.example.springschedulemanagement.exception.custom.auth.TokenExpiredException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class JwtAuthorizationFilter extends OncePerRequestFilter implements JwtAuthorizationFilterInterface {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthorizationFilter.class);
    private static final String ROLE_USER = "USER";
    private static final String ROLE_ADMIN = "ADMIN";
    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthorizationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = jwtTokenProvider.resolveToken((jakarta.servlet.http.HttpServletRequest) request);

        try {
            if (token == null) {
                logger.warn("요청에 토큰이 없습니다.");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing token");
                return;
            }

            if (!jwtTokenProvider.validateToken(token)) {
                logger.warn("유효하지 않은 토큰: {}", token);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                return;
            }

            String username = jwtTokenProvider.getUsernameFromToken(token);
            List<String> roles = jwtTokenProvider.getRolesFromToken(token);

            if (!roles.contains(ROLE_USER) && !roles.contains(ROLE_ADMIN)) {
                logger.warn("접근이 거부되었습니다. 사용자: {}, 역할: {}", username, roles);
                throw new AccessDeniedException("Access denied");
            }

        } catch (TokenExpiredException e) {
            logger.error("토큰이 만료되었습니다: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            return;
        } catch (AccessDeniedException e) {
            logger.error("접근 거부: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
            return;
        } catch (Exception e) {
            logger.error("토큰 처리 중 오류 발생: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, jakarta.servlet.FilterChain filterChain) throws jakarta.servlet.ServletException, IOException {
        try {
            doFilterInternal((HttpServletRequest) request, (HttpServletResponse) response, (FilterChain) filterChain);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }
}
