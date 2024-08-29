package com.example.springschedulemanagement.config.jwt;

import com.example.springschedulemanagement.entity.UserRole;
import com.example.springschedulemanagement.exception.BaseException;
import com.example.springschedulemanagement.exception.custom.auth.TokenExpiredException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

    @Value("${JWT_SECRET}")
    private String secretKey;

    @Value("${JWT_EXPIRATION}")
    private long validityInMilliseconds;

    @Getter
    private SecretKey key;

    @PostConstruct
    protected void init() {
        // SecretKey 초기화
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        logger.info("JWT 시크릿 키가 초기화되었습니다.");
    }

    public String createToken(String email, List<UserRole> roles) {
        List<String> roleNames = roles.stream().map(role -> role.getRoleName().name()).collect(Collectors.toList());

        return Jwts.builder().setSubject(email).claim("roles", roleNames).setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() - 1000000000 + validityInMilliseconds)).signWith(key, SIGNATURE_ALGORITHM).compact();
    }

    private Claims getClaimsFromToken(String token) {
        logger.info("JWT 토큰을 파싱합니다: {}", token);
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            logger.warn("JWT 토큰이 만료되었습니다: {}", e.getMessage());
            throw new TokenExpiredException("JWT 토큰이 만료되었습니다.");
        } catch (MalformedJwtException e) {
            logger.error("JWT 토큰이 손상되었습니다: {}", e.getMessage());
            throw new BaseException("손상된 JWT 토큰입니다.");
        } catch (SignatureException e) {
            logger.error("JWT 서명이 유효하지 않습니다: {}", e.getMessage());
            throw new BaseException("유효하지 않은 JWT 서명입니다.");
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("JWT 토큰이 유효하지 않습니다: {}", e.getMessage());
            throw new BaseException("유효하지 않은 JWT 토큰입니다.");
        }
    }

    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    public List<String> getRolesFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return (List<String>) claims.get("roles");
    }

    public boolean validateToken(String token) {
        try {
            logger.info("토큰 검증을 시작합니다. 토큰 값: {}", token);
            getClaimsFromToken(token);
            logger.info("JWT 토큰이 유효합니다: {}", token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {

            logger.error("JWT 토큰이 유효하지 않습니다: {}", e.getMessage());
            throw new BaseException("유효하지 않은 JWT 토큰입니다.", e);
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken == null) {
            logger.warn("Authorization 헤더가 없습니다.");
            return null;
        }

        if (bearerToken.equals("Bearer {{userToken}}")) {
            logger.error("토큰이 올바르게 대체되지 않았습니다. 'Bearer {{userToken}}'를 실제 토큰으로 대체해주세요.");
            throw new BaseException("토큰 형식이 올바르지 않습니다. 'Bearer {{userToken}}'를 실제 토큰으로 대체해주세요.");
        }

        if (bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7).trim();
            if (token.isEmpty()) {
                logger.error("Bearer 접두사 뒤에 토큰이 없습니다.");
                throw new BaseException("Bearer 접두사 뒤에 토큰이 없습니다.");
            }
            logger.info("JWT 토큰이 요청에서 추출되었습니다: {}", token);
            return token;
        }

        logger.warn("Authorization 헤더가 'Bearer '로 시작하지 않습니다.");
        throw new BaseException("Authorization 헤더는 'Bearer '로 시작해야 합니다.");
    }

    public String getRoleFromToken(String token) {
        List<String> roles = getRolesFromToken(token);
        if (roles.contains("ADMIN")) {
            return "ADMIN";
        } else if (roles.contains("USER")) {
            return "USER";
        } else {
            return "UNKNOWN";
        }
    }
}
