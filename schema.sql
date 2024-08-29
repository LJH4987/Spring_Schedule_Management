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



-- 더미 데이터

-- User 테이블에 더미 데이터 삽입
INSERT INTO User (name, email, hash_password, created_date, updated_date)
VALUES ('철수', 'chulsoo@example.com', '$2a$10$Q.PLVRAdag2AGRZO8VoMH.hs9vKG1kR/7OKahYVqOrJ7YoBsHPWLG', NOW(), NOW()),  -- 철수
       ('영희', 'younghee@example.com', '$2a$10$Wb7qhuvndTtS4OvfFkCntuwvtPFP92jjxZJsFy5k8QSAhjHeXH0GS', NOW(), NOW()), -- 영희
       ('민수', 'minsoo@example.com', '$2a$10$Ci1ZBckV56grGA7bCnkjNewdWCFtO6vd1dsHv.C.3z7kyti/3SFXC', NOW(), NOW()),   -- 민수
       ('지수', 'jisoo@example.com', '$2a$10$SWtymNgatUzMjVtwl33E0.NPcdMuQ/.RWqGD89DRzP4EmVMqQ9LN.', NOW(), NOW()),    -- 지수
       ('현수', 'hyunsoo@example.com', '$2a$10$MJheQgkLobIuOvso98D3lOfHnXy3zIh0w6sep02yKtSAqijR1TCee', NOW(), NOW()),  -- 현수
       ('은지', 'eunji@example.com', 'NoHashedPassword123', NOW(), NOW());                                             -- 은지

-- Schedule 테이블에 더미 데이터 삽입
INSERT INTO Schedule (user_id, title, description, created_date, updated_date, weather, date)
VALUES (2, '영희와 회의', '프로젝트 진행 상황 논의', NOW(), NOW(), 'Sunny', '01-01'),     -- 영희가 작성
       (1, '철수와 점심', '새로 오픈한 한식당에서 점심 식사', NOW(), NOW(), 'Cloudy', '01-02'), -- 철수가 작성
       (3, '헬스장 방문', '저녁 운동 세션', NOW(), NOW(), 'Rainy', '01-03'),             -- 민수가 작성
       (4, '지수와의 회의', '프로젝트 최종 발표 준비 논의', NOW(), NOW(), 'Snowy', '01-04'),  -- 지수가 작성
       (5, '현수와의 점심', '새로 생긴 이탈리안 레스토랑 방문', NOW(), NOW(), 'Windy', '01-05'), -- 현수가 작성
       (6, '독서 모임', '매주 토요일 독서 토론', NOW(), NOW(), 'Foggy', '01-06');       -- 은지가 작성

-- User_Schedule 테이블에 더미 데이터 삽입
INSERT INTO User_Schedule (user_id, schedule_id, created_date, updated_date)
VALUES (1, 1, NOW(), NOW()), -- 철수가 '영희와 회의'에 참석
       (2, 1, NOW(), NOW()), -- 영희가 '영희와 회의'에 참석
       (1, 2, NOW(), NOW()), -- 철수가 '철수와 점심'에 참석
       (3, 3, NOW(), NOW()), -- 민수가 '헬스장 방문'에 참석
       (4, 4, NOW(), NOW()), -- 지수가 '지수와의 회의'에 참석
       (5, 5, NOW(), NOW()), -- 현수가 '현수와의 점심'에 참석
       (6, 6, NOW(), NOW()); -- 은지가 '독서 모임'에 참석

-- Comment 테이블에 더미 데이터 삽입
INSERT INTO Comment (content, schedule_id, user_id, created_date, updated_date)
VALUES ('회의가 기대됩니다!', 1, 1, NOW(), NOW()),        -- 철수의 '영희와 회의'에 대한 댓글
       ('보고서를 잊지 마세요.', 1, 2, NOW(), NOW()),      -- 영희의 '영희와 회의'에 대한 댓글
       ('새로운 식당이 기대돼요.', 2, 1, NOW(), NOW()),    -- 철수의 '철수와 점심'에 대한 댓글
       ('지수의 아이디어 좋네요!', 4, 4, NOW(), NOW()),     -- 지수의 '지수와의 회의'에 대한 댓글
       ('파스타가 정말 맛있었습니다.', 5, 5, NOW(), NOW()),  -- 현수의 '현수와의 점심'에 대한 댓글
       ('이번 책 정말 흥미로웠어요.', 6, 6, NOW(), NOW());  -- 은지의 '독서 모임'에 대한 댓글

-- User_Role 테이블에 더미 데이터 삽입
INSERT INTO User_Role (user_id, role_name, created_date, updated_date)
VALUES (1, 'ADMIN', NOW(), NOW()), -- 철수에게 관리자 권한 할당
       (2, 'USER', NOW(), NOW()),  -- 영희에게 일반 사용자 권한 할당
       (3, 'USER', NOW(), NOW()),  -- 민수에게 일반 사용자 권한 할당
       (4, 'USER', NOW(), NOW()),  -- 지수에게 일반 사용자 권한 할당
       (5, 'USER', NOW(), NOW()),  -- 현수에게 일반 사용자 권한 할당
       (6, 'ADMIN', NOW(), NOW()); -- 은지에게 관리자 권한 할당
