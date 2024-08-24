-- 기존 데이터베이스 삭제 (필요한 경우 초기화용)
DROP DATABASE IF EXISTS schedulemanagementdb;

-- 데이터베이스 생성 및 사용
CREATE DATABASE IF NOT EXISTS schedulemanagementdb;
USE schedulemanagementdb;

-- 기존 테이블 삭제 (재실행 시 중복 문제 방지)
DROP TABLE IF EXISTS Comment;
DROP TABLE IF EXISTS UserSchedule;
DROP TABLE IF EXISTS Schedule;
DROP TABLE IF EXISTS UserRole;
DROP TABLE IF EXISTS RefreshToken;
DROP TABLE IF EXISTS User;
DROP TABLE IF EXISTS Weather;

-- User 테이블 생성
CREATE TABLE User
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,                             -- 사용자 고유 ID
    name         VARCHAR(50)  NOT NULL,                                         -- 사용자 이름 (최대 50자)
    email        VARCHAR(100) NOT NULL UNIQUE,                                  -- 사용자 이메일 (최대 100자, 중복 불가)
    hashPassword VARCHAR(255) NOT NULL,                                         -- 암호화된 비밀번호 (BCrypt, 최대 255자)
    createdDate  DATETIME DEFAULT CURRENT_TIMESTAMP,                            -- 생성 일시 (기본값: 현재 시간)
    updatedDate  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- 수정 일시 (자동 업데이트)
);

-- User 테이블에 인덱스 추가
CREATE INDEX idx_user_email ON User (email);

-- Schedule 테이블 생성
CREATE TABLE Schedule
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,                             -- 일정 고유 ID
    title       VARCHAR(100) NOT NULL,                                         -- 일정 제목 (최대 100자)
    description TEXT,                                                          -- 일정 설명 (길이 제한 없음)
    createdDate DATETIME DEFAULT CURRENT_TIMESTAMP,                            -- 생성 일시 (기본값: 현재 시간)
    updatedDate DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- 수정 일시 (자동 업데이트)
);

-- Schedule 테이블에 인덱스 추가
CREATE INDEX idx_schedule_title ON Schedule (title);

-- UserSchedule 테이블 생성 (N:M 관계 매핑)
CREATE TABLE UserSchedule
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,                              -- 관계 고유 ID
    userId      BIGINT NOT NULL,                                                -- 사용자 ID (외래키)
    scheduleId  BIGINT NOT NULL,                                                -- 일정 ID (외래키)
    createdDate DATETIME DEFAULT CURRENT_TIMESTAMP,                             -- 생성 일시 (기본값: 현재 시간)
    updatedDate DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- 수정 일시 (자동 업데이트)
    FOREIGN KEY (userId) REFERENCES User (id) ON DELETE CASCADE,                -- 사용자 삭제 시 연관 데이터도 삭제
    FOREIGN KEY (scheduleId) REFERENCES Schedule (id) ON DELETE CASCADE,        -- 일정 삭제 시 연관 데이터도 삭제
    UNIQUE (userId, scheduleId)                                                 -- 동일한 사용자와 일정의 중복 관계 방지
);

-- UserSchedule 테이블에 인덱스 추가
CREATE INDEX idx_user_schedule_userId ON UserSchedule (userId);
CREATE INDEX idx_user_schedule_scheduleId ON UserSchedule (scheduleId);

-- Comment 테이블 생성
CREATE TABLE Comment
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,                              -- 댓글 고유 ID
    content     TEXT   NOT NULL,                                                -- 댓글 내용 (길이 제한 없음)
    createdDate DATETIME DEFAULT CURRENT_TIMESTAMP,                             -- 생성 일시 (기본값: 현재 시간)
    updatedDate DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- 수정 일시 (자동 업데이트)
    scheduleId  BIGINT NOT NULL,                                                -- 연관 일정 ID (외래키)
    userId      BIGINT NOT NULL,                                                -- 작성자 ID (외래키)
    FOREIGN KEY (scheduleId) REFERENCES Schedule (id) ON DELETE CASCADE,        -- 일정 삭제 시 연관 댓글도 삭제
    FOREIGN KEY (userId) REFERENCES User (id) ON DELETE CASCADE                 -- 사용자 삭제 시 연관 댓글도 삭제
);

-- Comment 테이블에 인덱스 추가
CREATE INDEX idx_comment_scheduleId ON Comment (scheduleId);
CREATE INDEX idx_comment_userId ON Comment (userId);

-- RefreshToken 테이블 생성
CREATE TABLE RefreshToken
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,               -- 리프레시 토큰 고유 ID
    userId     BIGINT       NOT NULL,                           -- 사용자 ID (외래키)
    token      VARCHAR(255) NOT NULL,                           -- 리프레시 토큰 (최대 255자)
    expiryDate DATETIME     NOT NULL,                           -- 만료 일시
    FOREIGN KEY (userId) REFERENCES User (id) ON DELETE CASCADE -- 사용자 삭제 시 연관 리프레시 토큰도 삭제
);

-- RefreshToken 테이블에 인덱스 추가
CREATE INDEX idx_refreshToken_userId ON RefreshToken (userId);

-- UserRole 테이블 생성
CREATE TABLE UserRole
(
    id     BIGINT AUTO_INCREMENT PRIMARY KEY,                   -- 권한 고유 ID
    userId BIGINT      NOT NULL,                                -- 사용자 ID (외래키)
    role   VARCHAR(50) NOT NULL,                                -- 권한 이름 (예: ADMIN, USER)
    FOREIGN KEY (userId) REFERENCES User (id) ON DELETE CASCADE -- 사용자 삭제 시 연관 권한도 삭제
);

-- UserRole 테이블에 인덱스 추가
CREATE INDEX idx_userRole_userId ON UserRole (userId);
CREATE INDEX idx_userRole_role ON UserRole (role);

-- Weather 테이블 생성
CREATE TABLE Weather
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,               -- 날씨 정보 고유 ID
    scheduleId         BIGINT       NOT NULL,                           -- 일정 ID (외래키)
    temperature DOUBLE NOT NULL,                                        -- 일정 날짜의 기온
    weatherDescription VARCHAR(255) NOT NULL,                           -- 날씨 설명 (최대 255자)
    FOREIGN KEY (scheduleId) REFERENCES Schedule (id) ON DELETE CASCADE -- 일정 삭제 시 연관 날씨 정보도 삭제
);

-- Weather 테이블에 인덱스 추가
CREATE INDEX idx_weather_scheduleId ON Weather (scheduleId);