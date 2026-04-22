-- ============================================================================
-- LEGEND — 진급전설 김부장 프로젝트 통합 DB 초기화 스크립트
-- ============================================================================
-- 파일 구성:
--   [PART 1] DB 생성 + 사용자 권한 부여
--   [PART 2] DDL — 테이블 생성 (13개) + FK 제약
--   [PART 3] DUMMY DATA — 팀 테스트용 시드 데이터
--
-- [실행 방법]
--   1) MySQL root 또는 SUPER 권한 계정으로 접속
--      $ mysql -u root -p
--   2) 이 파일을 source 또는 Workbench 에서 전체 실행
--      mysql> source legend_full_setup.sql;
--      (또는 Workbench에서 Ctrl + Shift + Enter)
--
-- [주의사항]
--   - Spring Boot 애플리케이션이 실행 중이면 반드시 "종료" 후 실행
--   - 이미 legend DB가 존재하면 데이터는 TRUNCATE 후 다시 시드됨
--   - 스키마 자체를 완전 초기화하려면 STEP 1-A의 DROP 주석을 해제할 것
--
-- [계정 / 접속 정보]
--   DB:              legend
--   애플리케이션 계정: 'legend'@'%'  / PW: 'legend1234'
--   ※ 운영 배포 시 반드시 비밀번호 변경할 것
-- ============================================================================


-- ============================================================================
-- [PART 1] 데이터베이스 생성 + 권한 부여
-- ============================================================================

-- (선택) 완전 초기화가 필요할 때만 아래 줄 주석 해제
-- DROP DATABASE IF EXISTS legend;

CREATE DATABASE IF NOT EXISTS legend
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

-- 애플리케이션용 사용자 생성 (이미 있으면 통과)
CREATE USER IF NOT EXISTS 'legend'@'%'         IDENTIFIED BY 'legend1234';
CREATE USER IF NOT EXISTS 'legend'@'localhost' IDENTIFIED BY 'legend1234';

-- legend DB 에 대한 전체 권한 부여
GRANT ALL PRIVILEGES ON legend.* TO 'legend'@'%';
GRANT ALL PRIVILEGES ON legend.* TO 'legend'@'localhost';

FLUSH PRIVILEGES;

USE legend;


-- ============================================================================
-- [PART 2] DDL — 테이블 생성 (13개) + FK 제약
-- ============================================================================
SET NAMES utf8mb4;

-- ── 테이블 생성 (PK 선언 포함) ──────────────────────────────────────────────

CREATE TABLE IF NOT EXISTS `VACATION_HISTORY` (
                                                  `vacation_history_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                                  `user_id` BIGINT NOT NULL,
                                                  `used_date` DATE NOT NULL COMMENT '사용 날짜',
                                                  `deducted_amount` INT NULL DEFAULT 1 COMMENT '차감 개수',
                                                  `purpose` ENUM('ETC', 'SICK', 'SELF_IMPROVEMENT') NULL COMMENT '휴가 사유(기타, 질병, 자기계발)',
    `detail_purpose` VARCHAR(225) NULL COMMENT '상세 사유'
    );

CREATE TABLE IF NOT EXISTS `QUESTION_SUBMISSIONS` (
                                                      `submission_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                                      `question_id` BIGINT NOT NULL,
                                                      `user_id` BIGINT NOT NULL COMMENT '문제 푼 사람',
                                                      `is_correct` BOOLEAN NOT NULL,
                                                      `submitted_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
                                                      `selected_answer` INT NOT NULL
);

CREATE TABLE IF NOT EXISTS `ENROLLMENTS` (
                                             `enrollment_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                             `user_id` BIGINT NOT NULL,
                                             `course_id` BIGINT NOT NULL,
                                             `status` VARCHAR(20) NULL DEFAULT 'IN_PROGRESS' COMMENT '수강 상태',
    `deadline_date` DATETIME NULL COMMENT '타임어택 마감',
    `start_at` DATETIME NULL,
    `finish_date` DATETIME NULL,
    `progress` INT DEFAULT 0
    );

CREATE TABLE IF NOT EXISTS `QUESTIONS` (
                                           `question_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                           `user_id` BIGINT NOT NULL COMMENT '문제 출제자',
                                           `course_id` BIGINT NOT NULL,
                                           `section_id` BIGINT NOT NULL,
                                           `title` VARCHAR(255) NOT NULL,
    `answer` INT NOT NULL COMMENT '정답',
    `created_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
    `view_count` BIGINT NULL DEFAULT 0,
    `option1` VARCHAR(255) NOT NULL,
    `option2` VARCHAR(255) NOT NULL,
    `option3` VARCHAR(255) NOT NULL,
    `option4` VARCHAR(255) NOT NULL,
    `option5` VARCHAR(255) NOT NULL
    );

CREATE TABLE IF NOT EXISTS `COURSES` (
                                         `course_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                         `user_id` BIGINT NULL COMMENT '등록 관리자',
                                         `title` VARCHAR(255) NOT NULL COMMENT '과목명',
    `instructor_name` VARCHAR(100) NULL COMMENT '교수명',
    `description` VARCHAR(500) NULL,
    `duedate` INT NULL
    );

CREATE TABLE IF NOT EXISTS `FREE_BOARDS` (
                                             `post_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                             `user_id` BIGINT NOT NULL,
                                             `title` VARCHAR(255) NOT NULL,
    `content` TEXT NOT NULL,
    `created_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
    `view_count` BIGINT NULL DEFAULT 0
    );

CREATE TABLE IF NOT EXISTS `SECTIONS` (
                                          `section_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                          `course_id` BIGINT NOT NULL,
                                          `title` VARCHAR(255) NOT NULL COMMENT '영상 제목',
    `video_url` VARCHAR(500) NULL COMMENT '영상 경로',
    `upload_success` BOOLEAN NULL DEFAULT FALSE,
    `note` TEXT
    );

CREATE TABLE IF NOT EXISTS `PAYMENTS` (
                                          `payment_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                          `user_id` BIGINT NOT NULL,
                                          `amount` INT NOT NULL COMMENT '결제 금액',
                                          `status` BOOLEAN NOT NULL COMMENT '결제 상태',
                                          `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `COMMENTS` (
                                          `comment_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                          `post_id` BIGINT NULL,
                                          `user_id` BIGINT NOT NULL,
                                          `question_id` BIGINT NULL,
                                          `content` VARCHAR(200) NOT NULL,
    `created_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS `LOGIN_HISTORY` (
                                               `history_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                               `user_id` BIGINT NOT NULL,
                                               `is_success` BOOLEAN NOT NULL COMMENT '성공 여부',
                                               `fail_reason` VARCHAR(50) NULL COMMENT '실패 사유',
    `created_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS `ATTENDANCE` (
                                            `attendance_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                            `user_id` BIGINT NOT NULL,
                                            `target_date` DATETIME NOT NULL COMMENT '출결 날짜',
                                            `status` ENUM('PRESENT', 'LATE', 'ABSENT', 'EXCUSED') NOT NULL COMMENT '출결(출근, 지각, 결근, 공결)'
    );

CREATE TABLE IF NOT EXISTS `SECTION_PROGRESS` (
                                                  `progress_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                                  `enrollment_id` BIGINT NOT NULL,
                                                  `section_id` BIGINT NOT NULL,
                                                  `is_completed` BOOLEAN NULL DEFAULT FALSE COMMENT '수강 완료 여부'
);

CREATE TABLE IF NOT EXISTS `USERS` (
                                       `user_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                       `email` VARCHAR(255) NOT NULL COMMENT '로그인 아이디',
    `password` VARCHAR(255) NOT NULL COMMENT '비밀번호',
    `name` VARCHAR(50) NOT NULL COMMENT '이름',
    `birth_date` DATE NULL,
    `role` VARCHAR(20) NOT NULL DEFAULT 'USER' COMMENT '권한(USER, ADMIN)',
    `point` INT NULL DEFAULT 0 COMMENT '진급 점수',
    `rank` VARCHAR(20) NOT NULL DEFAULT '인턴' COMMENT '등급',
    `login_fail_count` INT NULL DEFAULT 0 COMMENT '로그인 실패 횟수',
    `is_locked` BOOLEAN NULL DEFAULT FALSE COMMENT '계정 잠금 여부',
    `created_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '가입일',
    `deleted_at` DATETIME NULL COMMENT '탈퇴일',
    `vacation_coupon` INT NULL DEFAULT 0 COMMENT '보유 연차',
    `identify_question` VARCHAR(100) NULL COMMENT '식별번호(암호화)',
    `identify_answer` VARCHAR(100) NULL COMMENT '식별질문 주관식 답변',
    `is_paid` BOOLEAN DEFAULT 0
    );


-- ── FK 제약조건 (테이블이 모두 생성된 후 안전하게 관계 매핑) ────────────────
-- ※ 재실행 시 중복 생성 에러 방지를 위해 information_schema 체크로 감쌈

DROP PROCEDURE IF EXISTS add_fk_if_absent;
DELIMITER $$
CREATE PROCEDURE add_fk_if_absent(
    IN p_table VARCHAR(64),
    IN p_name  VARCHAR(64),
    IN p_ddl   TEXT
)
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.TABLE_CONSTRAINTS
        WHERE CONSTRAINT_SCHEMA = DATABASE()
          AND TABLE_NAME        = p_table
          AND CONSTRAINT_NAME   = p_name
          AND CONSTRAINT_TYPE   = 'FOREIGN KEY'
    ) THEN
        SET @sql := p_ddl;
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
END IF;
END$$
DELIMITER ;

CALL add_fk_if_absent('COURSES',              'FK_COURSES_USER',          'ALTER TABLE `COURSES` ADD CONSTRAINT `FK_COURSES_USER` FOREIGN KEY (`user_id`) REFERENCES `USERS` (`user_id`)');
CALL add_fk_if_absent('SECTIONS',             'FK_SECTIONS_COURSE',       'ALTER TABLE `SECTIONS` ADD CONSTRAINT `FK_SECTIONS_COURSE` FOREIGN KEY (`course_id`) REFERENCES `COURSES` (`course_id`)');
CALL add_fk_if_absent('ENROLLMENTS',          'FK_ENROLLMENTS_USER',      'ALTER TABLE `ENROLLMENTS` ADD CONSTRAINT `FK_ENROLLMENTS_USER` FOREIGN KEY (`user_id`) REFERENCES `USERS` (`user_id`)');
CALL add_fk_if_absent('ENROLLMENTS',          'FK_ENROLLMENTS_COURSE',    'ALTER TABLE `ENROLLMENTS` ADD CONSTRAINT `FK_ENROLLMENTS_COURSE` FOREIGN KEY (`course_id`) REFERENCES `COURSES` (`course_id`)');
CALL add_fk_if_absent('SECTION_PROGRESS',     'FK_PROGRESS_ENROLLMENT',   'ALTER TABLE `SECTION_PROGRESS` ADD CONSTRAINT `FK_PROGRESS_ENROLLMENT` FOREIGN KEY (`enrollment_id`) REFERENCES `ENROLLMENTS` (`enrollment_id`)');
CALL add_fk_if_absent('SECTION_PROGRESS',     'FK_PROGRESS_SECTION',      'ALTER TABLE `SECTION_PROGRESS` ADD CONSTRAINT `FK_PROGRESS_SECTION` FOREIGN KEY (`section_id`) REFERENCES `SECTIONS` (`section_id`)');
CALL add_fk_if_absent('QUESTIONS',            'FK_QUESTIONS_USER',        'ALTER TABLE `QUESTIONS` ADD CONSTRAINT `FK_QUESTIONS_USER` FOREIGN KEY (`user_id`) REFERENCES `USERS` (`user_id`)');
CALL add_fk_if_absent('QUESTIONS',            'FK_QUESTIONS_COURSE',      'ALTER TABLE `QUESTIONS` ADD CONSTRAINT `FK_QUESTIONS_COURSE` FOREIGN KEY (`course_id`) REFERENCES `COURSES` (`course_id`)');
CALL add_fk_if_absent('QUESTIONS',            'FK_QUESTIONS_SECTION',     'ALTER TABLE `QUESTIONS` ADD CONSTRAINT `FK_QUESTIONS_SECTION` FOREIGN KEY (`section_id`) REFERENCES `SECTIONS` (`section_id`)');
CALL add_fk_if_absent('QUESTION_SUBMISSIONS', 'FK_SUBMISSIONS_QUESTION',  'ALTER TABLE `QUESTION_SUBMISSIONS` ADD CONSTRAINT `FK_SUBMISSIONS_QUESTION` FOREIGN KEY (`question_id`) REFERENCES `QUESTIONS` (`question_id`)');
CALL add_fk_if_absent('QUESTION_SUBMISSIONS', 'FK_SUBMISSIONS_USER',      'ALTER TABLE `QUESTION_SUBMISSIONS` ADD CONSTRAINT `FK_SUBMISSIONS_USER` FOREIGN KEY (`user_id`) REFERENCES `USERS` (`user_id`)');
CALL add_fk_if_absent('FREE_BOARDS',          'FK_FREE_BOARDS_USER',      'ALTER TABLE `FREE_BOARDS` ADD CONSTRAINT `FK_FREE_BOARDS_USER` FOREIGN KEY (`user_id`) REFERENCES `USERS` (`user_id`)');
CALL add_fk_if_absent('COMMENTS',             'FK_COMMENTS_POST',         'ALTER TABLE `COMMENTS` ADD CONSTRAINT `FK_COMMENTS_POST` FOREIGN KEY (`post_id`) REFERENCES `FREE_BOARDS` (`post_id`)');
CALL add_fk_if_absent('COMMENTS',             'FK_COMMENTS_QUESTION',     'ALTER TABLE `COMMENTS` ADD CONSTRAINT `FK_COMMENTS_QUESTION` FOREIGN KEY (`question_id`) REFERENCES `QUESTIONS` (`question_id`)');
CALL add_fk_if_absent('COMMENTS',             'FK_COMMENTS_USER',         'ALTER TABLE `COMMENTS` ADD CONSTRAINT `FK_COMMENTS_USER` FOREIGN KEY (`user_id`) REFERENCES `USERS` (`user_id`)');
CALL add_fk_if_absent('PAYMENTS',             'FK_PAYMENTS_USER',         'ALTER TABLE `PAYMENTS` ADD CONSTRAINT `FK_PAYMENTS_USER` FOREIGN KEY (`user_id`) REFERENCES `USERS` (`user_id`)');
CALL add_fk_if_absent('ATTENDANCE',           'FK_ATTENDANCE_USER',       'ALTER TABLE `ATTENDANCE` ADD CONSTRAINT `FK_ATTENDANCE_USER` FOREIGN KEY (`user_id`) REFERENCES `USERS` (`user_id`)');
CALL add_fk_if_absent('VACATION_HISTORY',     'FK_VACATION_USER',         'ALTER TABLE `VACATION_HISTORY` ADD CONSTRAINT `FK_VACATION_USER` FOREIGN KEY (`user_id`) REFERENCES `USERS` (`user_id`)');
CALL add_fk_if_absent('LOGIN_HISTORY',        'FK_LOGIN_HISTORY_USER',    'ALTER TABLE `LOGIN_HISTORY` ADD CONSTRAINT `FK_LOGIN_HISTORY_USER` FOREIGN KEY (`user_id`) REFERENCES `USERS` (`user_id`)');

DROP PROCEDURE IF EXISTS add_fk_if_absent;


-- ============================================================================
-- [PART 3] DUMMY DATA — 팀 테스트용 시드 데이터 (6명 유저 시나리오)
-- ============================================================================
USE legend;
SET NAMES utf8mb4;
SET time_zone = '+09:00';


-- ============================================================================
-- STEP 0. 스키마 정리 — questions.content 컬럼 방어적 제거
-- ============================================================================
SET @col_exists := (
  SELECT COUNT(*) FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME   = 'questions'
    AND COLUMN_NAME  = 'content'
);
SET @ddl := IF(@col_exists > 0,
  'ALTER TABLE questions DROP COLUMN content',
  'DO 0'
);
PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


-- ============================================================================
-- STEP 1. 기존 데이터 전부 비우기 (FK 체크 OFF → TRUNCATE → ON 복원)
-- ============================================================================
SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE payments;
TRUNCATE TABLE vacation_history;
TRUNCATE TABLE attendance;
TRUNCATE TABLE comments;
TRUNCATE TABLE free_boards;
TRUNCATE TABLE question_submissions;
TRUNCATE TABLE questions;
TRUNCATE TABLE section_progress;
TRUNCATE TABLE enrollments;
TRUNCATE TABLE sections;
TRUNCATE TABLE courses;
TRUNCATE TABLE login_history;
TRUNCATE TABLE users;

SET FOREIGN_KEY_CHECKS = 1;


-- ============================================================================
-- STEP 2. 더미데이터 INSERT
-- ============================================================================

-- ───────────────────────────────────────────────
-- [1/10] USERS — 6명
-- ───────────────────────────────────────────────
INSERT INTO `users` (
    email, password, name, birth_date, `role`, is_paid, point, `rank`,
    login_fail_count, is_locked, created_at, deleted_at,
    vacation_coupon, identify_question, identify_answer
) VALUES
-- user 1: 인턴1 김하윤 (결제완료 → 4/19 잠김)
('kimhy0512@gmail.com',   '$2b$10$it2hpfUXWvXDqUPkVnRFn.VQCRRAzkms.fsht/G9rwlCIYiE8YkFG', '김하윤', '2001-05-12', 'USER',       TRUE,   12, '인턴',     5, TRUE,  '2026-03-17 10:22:08', NULL,  3, '어릴 적 별명은?',        '하니'),
-- user 2: 인턴2 박서준 (미결제)
('parksj0403@naver.com',  '$2b$10$it2hpfUXWvXDqUPkVnRFn.VQCRRAzkms.fsht/G9rwlCIYiE8YkFG', '박서준', '2002-09-03', 'USER',       FALSE,   0, '인턴',     0, FALSE, '2026-04-10 21:05:44', NULL,  3, '가장 좋아하는 물건은?',  '닌텐도 스위치'),
-- user 3: 인턴3 이지우 (정상, 활발)
('jiwoo2511@gmail.com',   '$2b$10$it2hpfUXWvXDqUPkVnRFn.VQCRRAzkms.fsht/G9rwlCIYiE8YkFG', '이지우', '2000-11-25', 'USER',       TRUE,   17, '인턴',     0, FALSE, '2026-04-12 09:08:17', NULL,  3, '졸업한 초등학교 이름은?', '반포초등학교'),
-- user 4: 대리 정민재
('mjjeong88@naver.com',   '$2b$10$it2hpfUXWvXDqUPkVnRFn.VQCRRAzkms.fsht/G9rwlCIYiE8YkFG', '정민재', '1988-06-15', 'USER',       TRUE,  185, '대리',     0, FALSE, '2024-07-20 13:44:02', NULL, 12, '어릴 적 별명은?',        '감자'),
-- user 5: 부장 최성호
('shchoi77@hanmail.net',  '$2b$10$it2hpfUXWvXDqUPkVnRFn.VQCRRAzkms.fsht/G9rwlCIYiE8YkFG', '최성호', '1977-03-22', 'USER',       TRUE,  455, '부장',     0, FALSE, '2023-02-11 08:17:33', NULL, 18, '가장 좋아하는 물건은?',  '골프 퍼터'),
-- user 6: 운영자(USER,ADMIN) 강수빈 — 서비스 오픈부터 상주
('admin@legendkim.co',    '$2b$10$it2hpfUXWvXDqUPkVnRFn.VQCRRAzkms.fsht/G9rwlCIYiE8YkFG', '강수빈', '1990-08-10', 'USER,ADMIN', TRUE,    0, '정년퇴직', 0, FALSE, '2022-09-01 09:00:00', NULL, 15, '졸업한 초등학교 이름은?', '청운초등학교');


-- ───────────────────────────────────────────────
-- [2/10] login_history — 39건
--   * user 1 김하윤: 4/1~4/17 정상 → 4/19 5연속 실패 → 잠금
--   * user 2 박서준: 가입 직후 1회만 (결제 안하고 이탈)
--   * user 3 이지우: 4/12 가입 이후 매일 접속
--   * user 4 정민재: 최근 10일 꾸준
--   * user 5 최성호: 최근 10일 꾸준
--   * user 6 관리자: 매일 상주 (여러 번)
-- ───────────────────────────────────────────────
INSERT INTO `login_history` (user_id, is_success, fail_reason, created_at) VALUES
-- user 1 인턴1 김하윤 — 4/1 ~ 4/17 정상, 4/19 잠김 시나리오
(1, TRUE,  NULL,             '2026-04-01 08:52:14'),
(1, TRUE,  NULL,             '2026-04-03 09:15:22'),
(1, TRUE,  NULL,             '2026-04-07 08:48:37'),
(1, TRUE,  NULL,             '2026-04-10 09:02:11'),
(1, TRUE,  NULL,             '2026-04-14 08:55:44'),
(1, TRUE,  NULL,             '2026-04-17 09:01:08'),
(1, FALSE, '비밀번호 불일치', '2026-04-19 02:33:17'),
(1, FALSE, '비밀번호 불일치', '2026-04-19 02:33:41'),
(1, FALSE, '비밀번호 불일치', '2026-04-19 02:34:09'),
(1, FALSE, '비밀번호 불일치', '2026-04-19 02:34:36'),
(1, FALSE, '계정 잠금',       '2026-04-19 02:35:02'),
(1, FALSE, '계정 잠금',       '2026-04-20 09:17:44'),
(1, FALSE, '계정 잠금',       '2026-04-22 10:08:15'),
-- user 2 인턴2 박서준 — 가입 직후 1회만
(2, TRUE,  NULL,             '2026-04-10 21:05:44'),
-- user 3 인턴3 이지우 — 4/12 가입 후 매일
(3, TRUE,  NULL,             '2026-04-12 09:08:17'),
(3, TRUE,  NULL,             '2026-04-13 08:44:22'),
(3, TRUE,  NULL,             '2026-04-14 10:05:39'),
(3, TRUE,  NULL,             '2026-04-15 09:12:57'),
(3, TRUE,  NULL,             '2026-04-17 08:55:11'),
(3, TRUE,  NULL,             '2026-04-20 09:03:44'),
(3, TRUE,  NULL,             '2026-04-21 10:21:08'),
(3, TRUE,  NULL,             '2026-04-22 09:02:19'),
-- user 4 대리 정민재
(4, TRUE,  NULL,             '2026-04-13 08:22:44'),
(4, TRUE,  NULL,             '2026-04-14 08:11:03'),
(4, TRUE,  NULL,             '2026-04-16 08:38:57'),
(4, TRUE,  NULL,             '2026-04-17 08:27:18'),
(4, TRUE,  NULL,             '2026-04-20 08:15:29'),
(4, TRUE,  NULL,             '2026-04-22 08:04:11'),
-- user 5 부장 최성호
(5, TRUE,  NULL,             '2026-04-13 07:55:08'),
(5, TRUE,  NULL,             '2026-04-15 07:48:33'),
(5, TRUE,  NULL,             '2026-04-17 07:52:21'),
(5, TRUE,  NULL,             '2026-04-20 07:44:19'),
(5, TRUE,  NULL,             '2026-04-22 07:51:07'),
-- user 6 관리자
(6, TRUE,  NULL,             '2026-04-15 08:05:11'),
(6, TRUE,  NULL,             '2026-04-17 08:12:47'),
(6, TRUE,  NULL,             '2026-04-19 09:33:55'),
(6, TRUE,  NULL,             '2026-04-20 08:01:19'),
(6, TRUE,  NULL,             '2026-04-21 08:03:44'),
(6, TRUE,  NULL,             '2026-04-22 08:07:22');


-- ───────────────────────────────────────────────
-- [3/10] courses (4개) + sections (22개)
--   * 강사 user_id = 6 (관리자) — 부장이 수강하는 꼬임 방지
--   * instructor_name: 외부 강사 이름 (가상)
-- ───────────────────────────────────────────────
INSERT INTO `courses` (user_id, title, instructor_name, description, duedate) VALUES
                                                                                  (6, '신입사원 생존 매뉴얼', '김선영', '인사·복장·메일·회의록까지, 신입이 반드시 알아야 할 90일의 기본기',      30),
                                                                                  (6, '엑셀 실무 마스터',     '박준호', 'VLOOKUP / 피벗테이블 / IFS / 대시보드까지, 실무 엑셀 완전 정복',         45),
                                                                                  (6, '보고서 작성의 기술',   '이지훈', '임원에게 통하는 두괄식 보고서와 데이터 시각화 실전편',                    21),
                                                                                  (6, '회의 주도법',          '최유진', '아젠다 설계부터 결론·액션아이템까지, 시간 낭비 없는 회의 만들기',        14);

INSERT INTO `sections` (course_id, title, video_url, upload_success, note) VALUES
-- Course 1 (section_id 1~6)
(1, 'OT: 회사에서 살아남는다는 것',    'https://cdn.legendkim.co/v/c1-s1.mp4',  TRUE,  '수강 전 읽기 자료: 회사생활의 기본기 3p'),
(1, '첫인사와 자기소개',               'https://cdn.legendkim.co/v/c1-s2.mp4',  TRUE,  '30초 자기소개 템플릿 첨부'),
(1, '비즈니스 이메일 기본',            'https://cdn.legendkim.co/v/c1-s3.mp4',  TRUE,  '제목-본문-서명 3요소 체크리스트'),
(1, '복장과 자리 매너',                'https://cdn.legendkim.co/v/c1-s4.mp4',  TRUE,  NULL),
(1, '회의록 작성 실전',                'https://cdn.legendkim.co/v/c1-s5.mp4',  TRUE,  '회의록 양식 download'),
(1, '선배에게 물어보는 법',            'https://cdn.legendkim.co/v/c1-s6.mp4',  TRUE,  '질문 전 스스로 점검할 것 5가지'),
-- Course 2 (section_id 7~13)
(2, '엑셀 워크시트 기초',              'https://cdn.legendkim.co/v/c2-s1.mp4',  TRUE,  '단축키 cheat sheet 포함'),
(2, 'VLOOKUP / XLOOKUP 완전정복',      'https://cdn.legendkim.co/v/c2-s2.mp4',  TRUE,  '실습파일 vlookup_basic.xlsx'),
(2, '피벗테이블 A to Z',               'https://cdn.legendkim.co/v/c2-s3.mp4',  TRUE,  NULL),
(2, 'IF 중첩 vs IFS',                  'https://cdn.legendkim.co/v/c2-s4.mp4',  TRUE,  '조건 3개 이상은 IFS 권장'),
(2, '데이터 유효성 검사',              'https://cdn.legendkim.co/v/c2-s5.mp4',  TRUE,  NULL),
(2, '조건부 서식 실전',                'https://cdn.legendkim.co/v/c2-s6.mp4',  TRUE,  NULL),
(2, '대시보드 만들기',                 'https://cdn.legendkim.co/v/c2-s7.mp4',  FALSE, '인코딩 재작업 중 (2026-04-18~)'),
-- Course 3 (section_id 14~18)
(3, '보고서 구조 설계',                'https://cdn.legendkim.co/v/c3-s1.mp4',  TRUE,  NULL),
(3, '두괄식 vs 미괄식',                'https://cdn.legendkim.co/v/c3-s2.mp4',  TRUE,  '임원 보고는 두괄식 원칙'),
(3, '데이터 시각화 기본',              'https://cdn.legendkim.co/v/c3-s3.mp4',  TRUE,  '막대/선/원 차트 선택 기준'),
(3, '임원 보고 스타일',                'https://cdn.legendkim.co/v/c3-s4.mp4',  TRUE,  NULL),
(3, '피드백 반영 워크플로우',          'https://cdn.legendkim.co/v/c3-s5.mp4',  TRUE,  NULL),
-- Course 4 (section_id 19~22)
(4, '아젠다 설계',                     'https://cdn.legendkim.co/v/c4-s1.mp4',  TRUE,  '안건별 소요시간 권장치표'),
(4, '회의 시간 관리',                  'https://cdn.legendkim.co/v/c4-s2.mp4',  TRUE,  NULL),
(4, '반대 의견 조율',                  'https://cdn.legendkim.co/v/c4-s3.mp4',  TRUE,  '상대 발언 요약-확인 스크립트'),
(4, '결론 도출과 액션아이템',          'https://cdn.legendkim.co/v/c4-s4.mp4',  TRUE,  '액션아이템 = 담당자 + 마감일 필수');


-- ───────────────────────────────────────────────
-- [4/10] enrollments (9건)
--   enrollment_id 1~3  부장(user5) : 완료 1 + 수강중 2
--   enrollment_id 4~6  대리(user4) : 완료 1 + 수강중 2
--   enrollment_id 7    인턴1(user1): 수강중 1 (잠겼지만 enrollment는 남음)
--   enrollment_id 8~9  인턴3(user3): 수강중 2
-- ───────────────────────────────────────────────
INSERT INTO `enrollments` (user_id, course_id, status, deadline_date, start_at, finish_date, progress) VALUES
-- 부장 user5
(5, 2, 'COMPLETED',   '2025-11-29 09:30:00', '2025-10-15 09:30:22', '2025-11-25 22:33:14', 100),
(5, 3, 'IN_PROGRESS', '2026-04-26 10:22:00', '2026-04-05 10:22:17',  NULL,                   65),
(5, 4, 'IN_PROGRESS', '2026-04-24 14:15:00', '2026-04-10 14:15:00',  NULL,                   50),
-- 대리 user4
(4, 1, 'COMPLETED',   '2024-09-02 18:22:00', '2024-08-03 18:22:05', '2024-08-28 21:15:40', 100),
(4, 2, 'IN_PROGRESS', '2026-04-24 20:05:00', '2026-03-10 20:05:33',  NULL,                   70),
(4, 3, 'IN_PROGRESS', '2026-04-23 19:44:00', '2026-04-02 19:44:12',  NULL,                   40),
-- 인턴1 user1 (잠겼지만 수강기록 유지)
(1, 1, 'IN_PROGRESS', '2026-04-25 10:30:00', '2026-03-26 10:30:00',  NULL,                   55),
-- 인턴3 user3
(3, 1, 'IN_PROGRESS', '2026-05-12 09:15:00', '2026-04-12 09:15:11',  NULL,                   40),
(3, 2, 'IN_PROGRESS', '2026-05-30 20:33:00', '2026-04-15 20:33:27',  NULL,                   15);


-- ───────────────────────────────────────────────
-- [5/10] section_progress (53건)
-- ───────────────────────────────────────────────
INSERT INTO `section_progress` (enrollment_id, section_id, is_completed) VALUES
-- enrollment 1 (부장 course2 완료) — section 7~13 전부 TRUE
(1, 7, TRUE), (1, 8, TRUE), (1, 9, TRUE), (1, 10, TRUE), (1, 11, TRUE), (1, 12, TRUE), (1, 13, TRUE),
-- enrollment 2 (부장 course3 수강중, 65%) — 14,15,16 TRUE / 17,18 FALSE
(2, 14, TRUE), (2, 15, TRUE), (2, 16, TRUE), (2, 17, FALSE), (2, 18, FALSE),
-- enrollment 3 (부장 course4 수강중, 50%) — 19,20 TRUE / 21,22 FALSE
(3, 19, TRUE), (3, 20, TRUE), (3, 21, FALSE), (3, 22, FALSE),
-- enrollment 4 (대리 course1 완료) — 1~6 전부 TRUE
(4, 1, TRUE), (4, 2, TRUE), (4, 3, TRUE), (4, 4, TRUE), (4, 5, TRUE), (4, 6, TRUE),
-- enrollment 5 (대리 course2 수강중, 70%) — 7,8,9,10,11 TRUE / 12,13 FALSE
(5, 7, TRUE), (5, 8, TRUE), (5, 9, TRUE), (5, 10, TRUE), (5, 11, TRUE), (5, 12, FALSE), (5, 13, FALSE),
-- enrollment 6 (대리 course3 수강중, 40%) — 14,15 TRUE / 16,17,18 FALSE
(6, 14, TRUE), (6, 15, TRUE), (6, 16, FALSE), (6, 17, FALSE), (6, 18, FALSE),
-- enrollment 7 (인턴1 course1 수강중, 55%) — 1,2,3 TRUE / 4,5,6 FALSE
(7, 1, TRUE), (7, 2, TRUE), (7, 3, TRUE), (7, 4, FALSE), (7, 5, FALSE), (7, 6, FALSE),
-- enrollment 8 (인턴3 course1 수강중, 40%) — 1,2 TRUE / 3,4,5,6 FALSE
(8, 1, TRUE), (8, 2, TRUE), (8, 3, FALSE), (8, 4, FALSE), (8, 5, FALSE), (8, 6, FALSE),
-- enrollment 9 (인턴3 course2 수강중, 15%) — 7 TRUE / 8~13 FALSE
(9, 7, TRUE), (9, 8, FALSE), (9, 9, FALSE), (9, 10, FALSE), (9, 11, FALSE), (9, 12, FALSE), (9, 13, FALSE);


-- ───────────────────────────────────────────────
-- [6/10] questions (8문제) + question_submissions (8건)
--   ★ content 컬럼 제거됨 (팀 컨벤션)
--   * 부장(user5) 출제: Q1~Q6 (하위 직급이 풀도록)
--   * 대리(user4) 출제: Q7~Q8 (인턴급이 풀도록)
-- ───────────────────────────────────────────────
INSERT INTO `questions` (user_id, course_id, section_id, title, option1, option2, option3, option4, option5, answer, created_at, view_count) VALUES
-- Q1: 부장 출제, c1/s1
(5, 1,  1, '첫 출근일 가장 먼저 해야 할 행동은 무엇인가요?',
 '메일함부터 정리한다', '직속 선배와 인사를 나눈다', '업무 시스템 계정을 발급받는다',
 '회사 건물을 한 바퀴 돈다', '대표님께 직접 찾아가 인사한다', 2, '2026-03-18 10:03:22', 54),
-- Q2: 부장 출제, c1/s3
(5, 1,  3, '비즈니스 이메일에서 가장 먼저 점검해야 할 요소는?',
 '이모지 사용 여부', '수신자 이름과 직함', '서명 이미지의 해상도',
 '본문 글자 크기', '첨부파일의 용량', 2, '2026-03-18 10:22:55', 42),
-- Q3: 부장 출제, c2/s8
(5, 2,  8, 'VLOOKUP 대비 XLOOKUP의 장점이 아닌 것은?',
 '왼쪽 방향으로 조회 가능', '근사치가 기본 매칭', '기본값 지정 가능',
 '열 인덱스 번호 불필요', '오류 처리가 내장됨', 2, '2026-03-22 14:05:40', 68),
-- Q4: 부장 출제, c2/s9
(5, 2,  9, '피벗테이블에서 "값" 영역이 하는 역할은?',
 '원본 필드를 필터링', '행으로 표시할 기준 설정', '선택한 숫자형 데이터를 집계',
 '시각화 차트를 생성', '계산된 필드에 조건 부여', 3, '2026-03-22 14:18:01', 59),
-- Q5: 부장 출제, c3/s14
(5, 3, 14, '보고서 도입부에 가장 먼저 배치해야 할 내용은?',
 '참고 문헌', '데이터 수집 방법', '결론 또는 핵심 메시지',
 '작성자 소개', '앞으로의 일정', 3, '2026-04-06 09:11:02', 31),
-- Q6: 부장 출제, c3/s16
(5, 3, 16, '막대그래프 사용이 가장 적합한 데이터는?',
 '시간에 따른 비율 변화', '범주 간 값 비교', '두 변수의 상관관계',
 '지리적 분포', '누적 합계 추세', 2, '2026-04-06 09:24:39', 28),
-- Q7: 대리 출제, c1/s5 (인턴급이 풀도록)
(4, 1,  5, '회의록 작성 시 반드시 포함되어야 하는 항목은?',
 '참석자 전원의 의상', '회의실의 온도', '결정사항과 담당자',
 '회의 중 나눈 농담', '참석자의 SNS 계정', 3, '2026-04-08 20:15:09', 22),
-- Q8: 대리 출제, c4/s22
(4, 4, 22, '액션아이템에 반드시 포함되어야 할 정보는?',
 '담당자와 마감일', '참석자의 성별', '회의실 온도',
 '회의 주최자의 기분', '선호 음료', 1, '2026-04-11 19:33:48', 17);

INSERT INTO `question_submissions` (question_id, user_id, selected_answer, is_correct, submitted_at) VALUES
-- 대리 user4 풀이 (부장 출제 Q3~Q6) — 본인 수강 course 2, 3 관련
(3, 4, 2, TRUE,  '2026-04-03 21:12:44'),
(4, 4, 3, TRUE,  '2026-04-07 20:45:09'),
(5, 4, 3, TRUE,  '2026-04-14 22:01:57'),
(6, 4, 2, TRUE,  '2026-04-17 21:33:15'),
-- 인턴1 user1 풀이 (부장 Q1, 대리 Q7) — 4/17 이전 (잠김 전)
(1, 1, 2, TRUE,  '2026-04-10 09:15:33'),
(7, 1, 3, FALSE, '2026-04-15 10:22:48'),
-- 인턴3 user3 풀이 (부장 Q1~Q2, 대리 Q7) — 4/12 이후
(1, 3, 2, TRUE,  '2026-04-13 21:07:38'),
(2, 3, 2, TRUE,  '2026-04-16 22:44:27');


-- ───────────────────────────────────────────────
-- [7/10] free_boards (7개) + comments (20개 — 자유 10 + 문제 10)
--   * 부장(user5)  : 글 2개 / 다른 글에 댓글 상호
--   * 대리(user4)  : 글 2개 / 다른 글에 댓글 상호
--   * 인턴3(user3) : 글 3개 (질문 활발) / 다른 글에 댓글
--   * 부장은 대리 출제 문제 게시판(Q7, Q8)에 댓글만 (3개)
-- ───────────────────────────────────────────────
INSERT INTO `free_boards` (user_id, title, content, created_at, view_count) VALUES
-- post_id 1: 부장 글
(5, '엑셀 실무 마스터 완주 후기 - 부장급도 새로 배웁니다',
 '작년 말에 완주했는데 생각보다 얻은 게 많아서 뒤늦게라도 후기 남깁니다.\n20년 넘게 엑셀 쓰면서도 XLOOKUP / IFS는 제대로 몰랐어요. 실무에서 바로 써먹는 중입니다.\n직급 막론하고 추천드립니다.',
 '2026-04-13 20:15:33', 47),
-- post_id 2: 부장 글
(5, '보고서 두괄식 - 정말 임원들이 좋아합니다',
 '보고서 작성 강의 60% 정도 듣고 있는데, 두괄식 부분만 따로 연습해도 효과가 큽니다.\n이번 주 월요일 보고에서 "결론이 앞에 있어서 좋았다"는 피드백 직접 들었습니다.\n아직 안 들어보신 분들 꼭 참고하세요.',
 '2026-04-20 11:44:08', 28),
-- post_id 3: 대리 글
(4, '엑셀 VLOOKUP 학습 중인데 질문 있습니다',
 '엑셀 강의 70% 왔는데, 거래처 테이블 매칭할 때 VLOOKUP 쓰다가 자꾸 #N/A가 뜹니다.\n조회값 앞뒤 공백은 TRIM으로 처리했는데도 일부 행에서만 오류네요. 비슷한 경험 있으신 분 조언 부탁드립니다.',
 '2026-04-17 19:22:17', 35),
-- post_id 4: 대리 글
(4, '신입사원 코스 다시 복습해도 좋습니다',
 '이 코스 작년에 완주했는데, 후배 멘토링 하려다 보니 다시 들여다볼 일이 생겼습니다.\n기본기는 반복이 답이네요. 대리급도 가끔 돌아보면 많이 얻습니다.',
 '2026-04-19 14:33:41', 19),
-- post_id 5: 인턴3 글
(3, '질문이 너무 많은 신입은 민폐인가요?',
 '입사한 지 2주 됐는데 선배님들께 질문을 하루에 10번 이상은 하는 것 같아요.\n메모해두고 한꺼번에 여쭙는 게 나을지, 그때그때 여쭙는 게 나을지 조언 부탁드립니다 ㅠㅠ',
 '2026-04-14 22:08:55', 72),
-- post_id 6: 인턴3 글
(3, '엑셀 왕초보인데 어디서부터 시작해야 할까요?',
 '엑셀 실무 마스터 코스 시작했는데 VLOOKUP부터 벽이 느껴집니다.\n기초부터 다시 잡아야 할 거 같은데, 단축키 외우기부터 시작하면 될까요?',
 '2026-04-17 21:17:03', 44),
-- post_id 7: 인턴3 글
(3, '회의록 양식 파일 어디서 다운받나요?',
 '신입사원 코스 s5에서 회의록 양식 download라고 쓰여있는데 링크를 못 찾겠습니다.\n혹시 위치 아시는 분 알려주세요!',
 '2026-04-21 10:33:22', 16);

INSERT INTO `comments` (post_id, question_id, user_id, content, created_at) VALUES
-- ── 자유게시판 댓글 (post_id 지정)
-- post 1 (부장 글, 엑셀 후기)
(1, NULL, 4, '저도 이번 주에 시작했습니다. XLOOKUP이 궁금했는데 부장님 후기 보고 더 기대됩니다.', '2026-04-14 09:22:18'),
(1, NULL, 3, '선배님 후기 보고 동기부여 받고 갑니다. 저도 언젠가 완주하겠습니다!',                 '2026-04-15 20:05:41'),
-- post 2 (부장 글, 두괄식)
(2, NULL, 4, '지금 보고서 코스 수강 중인데, 두괄식 파트만 이미 써먹고 있습니다. 공감 갑니다.', '2026-04-20 15:11:33'),
-- post 3 (대리 글, VLOOKUP 질문)
(3, NULL, 5, '유형 불일치 가능성 확인해보세요. 텍스트로 보이는 숫자 vs 실제 숫자. VALUE() 씌워보세요.', '2026-04-17 20:44:52'),
(3, NULL, 3, '저도 같은 문제 겪었는데 댓글 보고 배워갑니다. 감사합니다!',                         '2026-04-18 09:03:17'),
-- post 4 (대리 글, 신입 코스 복습)
(4, NULL, 5, '기본기는 반복이 답이라는 말 100% 공감합니다. 우리 팀 대리들도 한번씩 돌려봐야겠네요.',  '2026-04-19 18:22:45'),
-- post 5 (인턴3 글, 질문 민폐)
(5, NULL, 5, '질문은 많이 할수록 좋습니다. 다만 10분 정도 스스로 찾아본 뒤 물어보면 더 환영받을 거예요.', '2026-04-15 08:33:27'),
(5, NULL, 4, '상황을 5줄로 정리해서 "제가 이걸 이렇게 해봤는데 안 됩니다" 형식으로 물어보면 좋습니다.',   '2026-04-15 13:17:09'),
-- post 6 (인턴3 글, 엑셀 왕초보)
(6, NULL, 4, 'F4 (절대참조), Ctrl+D/R (복사)부터 익히세요. VLOOKUP은 그 다음이 훨씬 수월해집니다.', '2026-04-18 10:05:44'),
-- post 7 (인턴3 글, 회의록 양식)
(7, NULL, 5, '공용 드라이브 /shared/templates/meeting_minutes.docx 에 있습니다. 팀 내에서도 쓰는 양식입니다.', '2026-04-21 14:22:08'),

-- ── 문제 게시판 댓글 (question_id 지정)
-- 부장 출제 Q1~Q6: 풀이한 학습자들의 소감/질문 댓글
-- (NULL, Q, user) 형식 — 대리는 Q3~Q6, 인턴1/3는 본인 푼 문제에만
(NULL, 3, 4, 'XLOOKUP 기본 동작이 정확히 일치라는 점 처음 알았습니다. VLOOKUP처럼 근사치 나올까봐 매번 마지막 인자 0 붙이던 게 기억나네요.', '2026-04-03 21:20:55'),
(NULL, 4, 4, '피벗테이블 행/열/값/필터 4영역 구조 다시 정리됩니다. 값 = 집계. 이제 헷갈리지 않을 것 같습니다.',                            '2026-04-07 20:50:33'),
(NULL, 1, 1, '저도 2번이 정답이라 생각했는데 맞춰서 다행입니다. 첫날 인사가 진짜 중요한 것 같아요.',                                    '2026-04-10 09:20:11'),
(NULL, 1, 3, '첫날 진짜 떨렸는데 선택지 2번 맞혔어요. 선배님들께 인사 돌리는 게 기본이라는 걸 다시 확인했습니다.',                      '2026-04-13 21:11:02'),
(NULL, 5, 4, '보고서 도입부 = 결론 또는 핵심 메시지. 두괄식 원칙 강조점이 여기에도 그대로 적용되네요.',                                 '2026-04-14 22:08:44'),
(NULL, 2, 3, '수신자 이름과 직함 확인... 지금까지 대충 넘겼던 부분이네요. 앞으론 꼭 체크하겠습니다.',                                    '2026-04-16 22:48:19'),
(NULL, 6, 4, '범주 간 값 비교 = 막대그래프. 시계열 추이는 선 그래프. 기본 매칭 외워두면 시각화 선택 고민이 줄어드네요.',                 '2026-04-17 21:41:55'),

-- 대리 출제 Q7~Q8에 부장의 피드백 댓글
(NULL, 7, 5, '회의록 핵심 3요소 중 "결정사항-담당자-마감일" 강조점이 좋네요. 좋은 문제입니다.',           '2026-04-09 08:44:18'),
(NULL, 7, 5, '보기 4번이 다소 모호한데, "회의 중 나눈 농담"은 좀 더 명확하게 표현하는 것도 좋겠습니다.',  '2026-04-09 08:48:33'),
(NULL, 8, 5, '액션아이템 정답에 "우선순위"도 함께 요구하는 확장 문항 만들어 보시죠. 실무에서 필요합니다.', '2026-04-12 10:15:22');


-- ───────────────────────────────────────────────
-- [8/10] attendance (69건)
--   * 부장/대리: 4/1~4/22 영업일 16일 (출근/지각/결근/공결 1+ 포함 + 연차 1)
--   * 인턴1: 4/1~4/17 영업일 13일 (4/19 잠김, 인턴이라 공결 없음)
--   * 인턴3: 4/13~4/22 영업일 8일 (공결 없음)
--   * 관리자: 4/1~4/22 영업일 16일 전부 PRESENT
--   * 인턴2: 미결제 → 출결 없음
--
--   2026년 4월 영업일 (총 16일):
--   4/1(수), 4/2(목), 4/3(금),
--   4/6(월), 4/7(화), 4/8(수), 4/9(목), 4/10(금),
--   4/13(월), 4/14(화), 4/15(수), 4/16(목), 4/17(금),
--   4/20(월), 4/21(화), 4/22(수)
-- ───────────────────────────────────────────────
INSERT INTO `attendance` (user_id, target_date, status) VALUES
-- ── 부장 user5 (16일) : PRESENT 12, LATE 1, ABSENT 1, EXCUSED 2(외근+연차)
(5, '2026-04-01 08:52:11', 'PRESENT'),
(5, '2026-04-02 08:48:33', 'PRESENT'),
(5, '2026-04-03 10:14:22', 'LATE'),
(5, '2026-04-06 08:50:17', 'PRESENT'),
(5, '2026-04-07 08:44:55', 'PRESENT'),
(5, '2026-04-08 08:47:19', 'PRESENT'),
(5, '2026-04-09 00:00:00', 'ABSENT'),
(5, '2026-04-10 08:52:03', 'PRESENT'),
(5, '2026-04-13 07:55:08', 'PRESENT'),
(5, '2026-04-14 00:00:00', 'EXCUSED'),
(5, '2026-04-15 07:48:33', 'PRESENT'),
(5, '2026-04-16 07:51:44', 'PRESENT'),
(5, '2026-04-17 00:00:00', 'EXCUSED'),
(5, '2026-04-20 07:44:19', 'PRESENT'),
(5, '2026-04-21 07:49:22', 'PRESENT'),
(5, '2026-04-22 07:51:07', 'PRESENT'),
-- ── 대리 user4 (16일) : PRESENT 12, LATE 1, ABSENT 1, EXCUSED 2(외근+연차)
(4, '2026-04-01 08:33:17', 'PRESENT'),
(4, '2026-04-02 10:05:44', 'LATE'),
(4, '2026-04-03 08:41:22', 'PRESENT'),
(4, '2026-04-06 08:28:55', 'PRESENT'),
(4, '2026-04-07 00:00:00', 'ABSENT'),
(4, '2026-04-08 08:35:17', 'PRESENT'),
(4, '2026-04-09 08:22:44', 'PRESENT'),
(4, '2026-04-10 08:44:09', 'PRESENT'),
(4, '2026-04-13 00:00:00', 'EXCUSED'),
(4, '2026-04-14 08:11:03', 'PRESENT'),
(4, '2026-04-15 00:00:00', 'EXCUSED'),
(4, '2026-04-16 08:38:57', 'PRESENT'),
(4, '2026-04-17 08:27:18', 'PRESENT'),
(4, '2026-04-20 08:15:29', 'PRESENT'),
(4, '2026-04-21 08:19:41', 'PRESENT'),
(4, '2026-04-22 08:04:11', 'PRESENT'),
-- ── 인턴1 user1 (13일, 4/1~4/17) : PRESENT 11, LATE 1, ABSENT 1, (공결 없음)
(1, '2026-04-01 08:52:14', 'PRESENT'),
(1, '2026-04-02 10:15:22', 'LATE'),
(1, '2026-04-03 09:15:22', 'PRESENT'),
(1, '2026-04-06 09:02:41', 'PRESENT'),
(1, '2026-04-07 00:00:00', 'ABSENT'),
(1, '2026-04-08 09:08:33', 'PRESENT'),
(1, '2026-04-09 09:11:05', 'PRESENT'),
(1, '2026-04-10 09:02:11', 'PRESENT'),
(1, '2026-04-13 08:55:49', 'PRESENT'),
(1, '2026-04-14 08:55:44', 'PRESENT'),
(1, '2026-04-15 09:03:22', 'PRESENT'),
(1, '2026-04-16 08:58:17', 'PRESENT'),
(1, '2026-04-17 09:01:08', 'PRESENT'),
-- ── 인턴3 user3 (8일, 4/13~4/22) : PRESENT 6, LATE 1, ABSENT 1, (공결 없음)
(3, '2026-04-13 08:44:22', 'PRESENT'),
(3, '2026-04-14 10:05:39', 'LATE'),
(3, '2026-04-15 09:12:57', 'PRESENT'),
(3, '2026-04-16 00:00:00', 'ABSENT'),
(3, '2026-04-17 08:55:11', 'PRESENT'),
(3, '2026-04-20 09:03:44', 'PRESENT'),
(3, '2026-04-21 10:21:08', 'PRESENT'),
(3, '2026-04-22 09:02:19', 'PRESENT'),
-- ── 관리자 user6 (16일) : PRESENT 13, LATE 1, ABSENT 1, EXCUSED 1
(6, '2026-04-01 08:02:11', 'PRESENT'),
(6, '2026-04-02 08:05:33', 'PRESENT'),
(6, '2026-04-03 08:01:44', 'PRESENT'),
(6, '2026-04-06 08:07:22', 'PRESENT'),
(6, '2026-04-07 09:48:18', 'LATE'),
(6, '2026-04-08 08:04:55', 'PRESENT'),
(6, '2026-04-09 00:00:00', 'EXCUSED'),
(6, '2026-04-10 08:02:44', 'PRESENT'),
(6, '2026-04-13 08:06:11', 'PRESENT'),
(6, '2026-04-14 00:00:00', 'ABSENT'),
(6, '2026-04-15 08:05:11', 'PRESENT'),
(6, '2026-04-16 08:03:55', 'PRESENT'),
(6, '2026-04-17 08:12:47', 'PRESENT'),
(6, '2026-04-20 08:01:19', 'PRESENT'),
(6, '2026-04-21 08:03:44', 'PRESENT'),
(6, '2026-04-22 08:07:22', 'PRESENT');


-- ───────────────────────────────────────────────
-- [9/10] vacation_history (2건)
--   * 부장 user5: 4/17 연차 (SELF_IMPROVEMENT - 골프 대회 참가)
--   * 대리 user4: 4/15 연차 (SICK - 병원 진료)
-- ───────────────────────────────────────────────
INSERT INTO `vacation_history` (user_id, used_date, deducted_amount, purpose, detail_purpose) VALUES
                                                                                                  (5, '2026-04-17', 1, 'SELF_IMPROVEMENT', '아마추어 골프 대회 참가'),
                                                                                                  (4, '2026-04-15', 1, 'SICK',             '병원 진료 (정기검진)');


-- ───────────────────────────────────────────────
-- [10/10] payments (4건) — 전원 1200만원, 인턴2/관리자 제외
--   * 부장 user5: 2023-02-11 (초기 합류)
--   * 대리 user4: 2024-07-20
--   * 인턴1 user1: 2026-03-17 (가입일)
--   * 인턴3 user3: 2026-04-12 (가입일)
-- ───────────────────────────────────────────────
INSERT INTO `payments` (user_id, amount, status, created_at) VALUES
                                                                 (5, 12000000, TRUE, '2023-02-11'),
                                                                 (4, 12000000, TRUE, '2024-07-20'),
                                                                 (1, 12000000, TRUE, '2026-03-17'),
                                                                 (3, 12000000, TRUE, '2026-04-12');


-- ============================================================================
-- STEP 3. 검증 — 기대 카운트와 일치해야 함
-- ============================================================================
SELECT '검증 결과' AS title;

SELECT
    tbl, actual, expected,
    CASE WHEN actual = expected THEN 'OK' ELSE 'FAIL' END AS result
FROM (
         SELECT 'users'                AS tbl, (SELECT COUNT(*) FROM users)                AS actual,  6 AS expected
         UNION ALL SELECT 'login_history',        (SELECT COUNT(*) FROM login_history),        39
         UNION ALL SELECT 'courses',              (SELECT COUNT(*) FROM courses),               4
         UNION ALL SELECT 'sections',             (SELECT COUNT(*) FROM sections),             22
         UNION ALL SELECT 'enrollments',          (SELECT COUNT(*) FROM enrollments),           9
         UNION ALL SELECT 'section_progress',     (SELECT COUNT(*) FROM section_progress),     53
         UNION ALL SELECT 'questions',            (SELECT COUNT(*) FROM questions),             8
         UNION ALL SELECT 'question_submissions', (SELECT COUNT(*) FROM question_submissions),  8
         UNION ALL SELECT 'free_boards',          (SELECT COUNT(*) FROM free_boards),           7
         UNION ALL SELECT 'comments',             (SELECT COUNT(*) FROM comments),             20
         UNION ALL SELECT 'attendance',           (SELECT COUNT(*) FROM attendance),           69
         UNION ALL SELECT 'vacation_history',     (SELECT COUNT(*) FROM vacation_history),      2
         UNION ALL SELECT 'payments',             (SELECT COUNT(*) FROM payments),              4
     ) t
ORDER BY FIELD(tbl,
               'users','login_history','courses','sections','enrollments','section_progress',
               'questions','question_submissions','free_boards','comments',
               'attendance','vacation_history','payments');

-- 계정 요약
SELECT user_id, email, name, `rank`, point, `role`, is_paid, is_locked
FROM users
ORDER BY
    CASE WHEN `role` LIKE '%ADMIN%' THEN 0 ELSE 1 END,
    point ASC;

-- ============================================================================
-- 끝. 테스트 계정:
--   - 인턴1 (잠김):   kimhy0512@gmail.com
--   - 인턴2 (미결제): parksj0403@naver.com
--   - 인턴3 (활발):   jiwoo2511@gmail.com
--   - 대리:           mjjeong88@naver.com
--   - 부장:           shchoi77@hanmail.net
--   - 관리자:         admin@legendkim.co
--   - 비밀번호 공통:  "password"
-- ============================================================================