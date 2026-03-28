-- 기본 리스트
INSERT INTO reminder_list (id, name, color, default_list, created_at, updated_at)
VALUES (1, '미리 알림', '#007AFF', true, NOW(), NOW());

-- 사용자 리스트
INSERT INTO reminder_list (id, name, color, default_list, created_at, updated_at)
VALUES (2, '개인', '#FF6B6B', false, NOW(), NOW());

INSERT INTO reminder_list (id, name, color, default_list, created_at, updated_at)
VALUES (3, '업무', '#34C759', false, NOW(), NOW());

-- 리마인더: 미리 알림
INSERT INTO reminder (id, title, notes, due_date, priority, completed, completed_at, list_id, created_at, updated_at)
VALUES (1, '우유 사기', '저지방으로', '2026-03-28 18:00:00', 'HIGH', false, NULL, 1, NOW(), NOW());

INSERT INTO reminder (id, title, notes, due_date, priority, completed, completed_at, list_id, created_at, updated_at)
VALUES (2, '택배 수령', NULL, '2026-03-28 12:00:00', 'MEDIUM', false, NULL, 1, NOW(), NOW());

INSERT INTO reminder (id, title, notes, due_date, priority, completed, completed_at, list_id, created_at, updated_at)
VALUES (3, '이메일 확인', NULL, '2026-03-27 09:00:00', 'NONE', true, '2026-03-27 09:30:00', 1, NOW(), NOW());

-- 리마인더: 개인
INSERT INTO reminder (id, title, notes, due_date, priority, completed, completed_at, list_id, created_at, updated_at)
VALUES (4, '운동하기', '헬스장 30분', '2026-03-29 07:00:00', 'LOW', false, NULL, 2, NOW(), NOW());

INSERT INTO reminder (id, title, notes, due_date, priority, completed, completed_at, list_id, created_at, updated_at)
VALUES (5, '독서 1시간', '클린 코드 3장', '2026-03-30 21:00:00', 'MEDIUM', false, NULL, 2, NOW(), NOW());

INSERT INTO reminder (id, title, notes, due_date, priority, completed, completed_at, list_id, created_at, updated_at)
VALUES (6, '병원 예약', NULL, NULL, 'NONE', false, NULL, 2, NOW(), NOW());

-- 리마인더: 업무
INSERT INTO reminder (id, title, notes, due_date, priority, completed, completed_at, list_id, created_at, updated_at)
VALUES (7, '분기 보고서 작성', 'Q1 실적 정리', '2026-03-31 17:00:00', 'HIGH', false, NULL, 3, NOW(), NOW());

INSERT INTO reminder (id, title, notes, due_date, priority, completed, completed_at, list_id, created_at, updated_at)
VALUES (8, '팀 미팅 준비', '발표 자료 준비', '2026-03-29 10:00:00', 'HIGH', false, NULL, 3, NOW(), NOW());

INSERT INTO reminder (id, title, notes, due_date, priority, completed, completed_at, list_id, created_at, updated_at)
VALUES (9, '코드 리뷰', 'PR #42 리뷰', '2026-03-28 15:00:00', 'MEDIUM', false, NULL, 3, NOW(), NOW());

INSERT INTO reminder (id, title, notes, due_date, priority, completed, completed_at, list_id, created_at, updated_at)
VALUES (10, '회의록 정리', NULL, '2026-03-27 18:00:00', 'LOW', true, '2026-03-27 18:30:00', 3, NOW(), NOW());
