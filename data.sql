-- 더미 데이터

-- user 테이블에 더미 데이터 삽입
INSERT INTO user (name, email, hashPassword)
VALUES ('철수', 'chulsoo@example.com', 'hashedpassword1'),  -- 철수
       ('영희', 'younghee@example.com', 'hashedpassword2'), -- 영희
       ('민수', 'minsoo@example.com', 'hashedpassword3'),   -- 민수
       ('지수', 'jisoo@example.com', 'hashedpassword4'),    -- 지수
       ('현수', 'hyunsoo@example.com', 'hashedpassword5'),  -- 현수
       ('은지', 'eunji@example.com', 'hashedpassword6');
-- 은지

-- schedule 테이블에 더미 데이터 삽입
INSERT INTO schedule (title, description)
VALUES ('영희와 회의', '프로젝트 진행 상황 논의'),       -- 철수와 영희가 참여
       ('철수와 점심', '새로 오픈한 한식당에서 점심 식사'),  -- 철수가 참여
       ('헬스장 방문', '저녁 운동 세션'),            -- 민수가 참여
       ('지수와의 회의', '프로젝트 최종 발표 준비 논의'),   -- 지수가 참여
       ('현수와의 점심', '새로 생긴 이탈리안 레스토랑 방문'), -- 현수가 참여
       ('독서 모임', '매주 토요일 독서 토론');
-- 은지가 참여

-- userschedule 테이블에 더미 데이터 삽입
INSERT INTO userschedule (userId, scheduleId)
VALUES (1, 1), -- 철수가 '영희와 회의'에 참석
       (2, 1), -- 영희가 '영희와 회의'에 참석
       (1, 2), -- 철수가 '철수와 점심'에 참석
       (3, 3), -- 민수가 '헬스장 방문'에 참석
       (4, 4), -- 지수가 '지수와의 회의'에 참석
       (5, 5), -- 현수가 '현수와의 점심'에 참석
       (6, 6);
-- 은지가 '독서 모임'에 참석

-- comment 테이블에 더미 데이터 삽입
INSERT INTO comment (content, scheduleId, userId)
VALUES ('회의가 기대됩니다!', 1, 1),      -- 철수의 '영희와 회의'에 대한 댓글
       ('보고서를 잊지 마세요.', 1, 2),    -- 영희의 '영희와 회의'에 대한 댓글
       ('새로운 식당이 기대돼요.', 2, 1),   -- 철수의 '철수와 점심'에 대한 댓글
       ('지수의 아이디어 좋네요!', 4, 4),   -- 지수의 '지수와의 회의'에 대한 댓글
       ('파스타가 정말 맛있었습니다.', 5, 5), -- 현수의 '현수와의 점심'에 대한 댓글
       ('이번 책 정말 흥미로웠어요.', 6, 6);
-- 은지의 '독서 모임'에 대한 댓글

-- refreshtoken 테이블에 더미 데이터 삽입
INSERT INTO refreshtoken (userId, token, expiryDate)
VALUES (1, 'refreshTokenForChulsoo', DATE_ADD(NOW(), INTERVAL 1 DAY)),  -- 철수의 리프레시 토큰
       (2, 'refreshTokenForYounghee', DATE_ADD(NOW(), INTERVAL 1 DAY)), -- 영희의 리프레시 토큰
       (3, 'refreshTokenForMinsoo', DATE_ADD(NOW(), INTERVAL 1 DAY)),   -- 민수의 리프레시 토큰
       (4, 'refreshTokenForJisoo', DATE_ADD(NOW(), INTERVAL 1 DAY)),    -- 지수의 리프레시 토큰
       (5, 'refreshTokenForHyunsoo', DATE_ADD(NOW(), INTERVAL 1 DAY)),  -- 현수의 리프레시 토큰
       (6, 'refreshTokenForEunji', DATE_ADD(NOW(), INTERVAL 1 DAY));
-- 은지의 리프레시 토큰

-- userrole 테이블에 더미 데이터 삽입
INSERT INTO userrole (userId, role)
VALUES (1, 'ADMIN'), -- 철수에게 관리자 권한 할당
       (2, 'USER'),  -- 영희에게 일반 사용자 권한 할당
       (3, 'USER'),  -- 민수에게 일반 사용자 권한 할당
       (4, 'USER'),  -- 지수에게 일반 사용자 권한 할당
       (5, 'USER'),  -- 현수에게 일반 사용자 권한 할당
       (6, 'ADMIN'); -- 은지에게 관리자 권한 할당