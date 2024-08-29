-- 기존 데이터베이스 삭제 (필요한 경우 초기화용)
DROP DATABASE IF EXISTS schedulemanagementdb;


-- 데이터베이스 생성 및 사용
CREATE DATABASE IF NOT EXISTS schedulemanagementdb;
USE schedulemanagementdb;


-- 기존 테이블 삭제 (재실행 시 중복 문제 방지)
DROP TABLE IF EXISTS Comment;
DROP TABLE IF EXISTS User_Schedule;
DROP TABLE IF EXISTS Schedule;
DROP TABLE IF EXISTS User_Role;
DROP TABLE IF EXISTS User;

-- User 테이블 생성
CREATE TABLE User
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(50)  NOT NULL,
    email         VARCHAR(100) NOT NULL UNIQUE,
    hash_password VARCHAR(100) NOT NULL,
    created_date  DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_date  DATETIME DEFAULT NULL
);

CREATE INDEX idx_user_email ON User (email);

-- Schedule 테이블 생성
CREATE TABLE Schedule
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    title         VARCHAR(100) NOT NULL,
    description   TEXT,
    created_date  DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_date  DATETIME DEFAULT NULL,
    weather       VARCHAR(255),
    date          VARCHAR(10),
    user_id       BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User (id) ON DELETE CASCADE
);

CREATE INDEX idx_schedule_user_id ON Schedule (user_id);

-- User_Schedule 테이블 생성
CREATE TABLE User_Schedule
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id       BIGINT NOT NULL,
    schedule_id   BIGINT NOT NULL,
    created_date  DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_date  DATETIME DEFAULT NULL,
    FOREIGN KEY (user_id) REFERENCES User (id) ON DELETE CASCADE,
    FOREIGN KEY (schedule_id) REFERENCES Schedule (id) ON DELETE CASCADE,
    UNIQUE (user_id, schedule_id)
);

CREATE INDEX idx_user_schedule_user_id ON User_Schedule (user_id);
CREATE INDEX idx_user_schedule_schedule_id ON User_Schedule (schedule_id);

-- Comment 테이블 생성
CREATE TABLE Comment
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    content       TEXT   NOT NULL,
    created_date  DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_date  DATETIME DEFAULT NULL,
    schedule_id   BIGINT NOT NULL,
    user_id       BIGINT NOT NULL,
    FOREIGN KEY (schedule_id) REFERENCES Schedule (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES User (id) ON DELETE CASCADE
);

CREATE INDEX idx_comment_schedule_id ON Comment (schedule_id);
CREATE INDEX idx_comment_user_id ON Comment (user_id);

-- UserRole 테이블 생성
CREATE TABLE User_Role
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id       BIGINT NOT NULL,
    role_name     VARCHAR(50) NOT NULL,
    created_date  DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_date  DATETIME DEFAULT NULL,
    FOREIGN KEY (user_id) REFERENCES User (id) ON DELETE CASCADE,
    CONSTRAINT unique_user_role UNIQUE (user_id, role_name)
);

CREATE INDEX idx_user_role_user_id ON User_Role (user_id);
CREATE INDEX idx_user_role_role_name ON User_Role (role_name);

-- 시퀀스 재설정 (레코드 초기화)
-- 실제 사용 시 에는 레거시 값 초기화 및 데이터 무결성 주의할 것
ALTER TABLE User AUTO_INCREMENT = 1;
ALTER TABLE Comment AUTO_INCREMENT = 1;
ALTER TABLE User_Schedule AUTO_INCREMENT = 1;
ALTER TABLE Schedule AUTO_INCREMENT = 1;
ALTER TABLE User_Role AUTO_INCREMENT = 1;