insert into center
(center_id, center_name, center_address, created_dt, modified_dt)
values (1, '잠실점', '서울 송파구 XX동 YY', now(), null)
     , (2, '인천점', '인천 미추홀구 XX동 YY', now(), null)
     , (3, '수원점', '경기 수원시 XX구 YY', now(), null);

insert into program
(program_id, program_name, center_id, created_dt, modified_dt)
values (1, '퍼즐놀이', 1, now(), null)
     , (2, '물감놀이', 1, now(), null)
     , (3, '그물놀이터', 1, now(), null)
     , (4, '퍼즐놀이', 2, now(), null)
     , (5, '물감놀이', 2, now(), null)
     , (6, '그물놀이터', 2, now(), null)
     , (7, '퍼즐놀이', 3, now(), null)
     , (8, '물감놀이', 3, now(), null)
     , (9, '그물놀이터', 3, now(), null);

insert into parent
(parent_id, parent_name, email, created_dt, modified_dt)
values (1, '박부모', 'testPark@naver.com', now(), null)
     , (2, '김부모', 'testKim@google.com', now(), null)
     , (3, '이부모', 'testLee@google.com', now(), null);

insert into child
(child_id, child_name, parent_id, created_dt, modified_dt)
values (1, '박철수', 1, now(),null)
     , (2, '김미영', 2, now(), null)
     , (3, '김지훈', 2, now(), null)
     , (4, '이대한', 3, now(), null)
     , (5, '이민국', 3, now(), null)
     , (6, '이만세', 3, now(), null);


INSERT INTO program_schedule (prgoram_schedule_id, created_dt, modified_dt, reservation_cnt,
                                    start_date, start_time, program_id)
VALUES (1, now(), null, 0, '2024-02-23', '17:00:00', 1)
     , (2, now(), null, 0, '2024-02-24', '17:00:00', 1)
     , (3, now(), null, 0, '2024-02-25', '17:00:00', 1)
     , (4, now(), null, 0, '2024-02-26', '17:00:00', 1)
     , (5, now(), null, 0, '2024-02-27', '14:00:00', 2)
     , (6, now(), null, 0, '2024-03-08', '14:00:00', 2)
     , (7, now(), null, 0, '2024-03-09', '14:00:00', 2)
     , (8, now(), null, 0, '2024-03-13', '14:00:00', 2)
     , (9, now(), null, 0, '2024-05-03', '11:00:00', 3)
     , (10,now(), null, 0, '2024-07-07', '11:00:00', 3)
     , (11,now(), null, 0, '2024-09-12', '11:00:00', 3)
     , (12,now(), null, 0, '2024-10-08', '11:00:00', 3);