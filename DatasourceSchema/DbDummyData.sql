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