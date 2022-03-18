-- 초기 값 생성
insert into USER values(90001, sysdate(), 'User1', 'test1111', '701010-1111111');
insert into USER values(90002, sysdate(), 'User2', 'test2222', '801010-1111111');
insert into USER values(90003, sysdate(), 'User3', 'test2222', '901010-1111111');


-- Post 초기 값 생성
insert into POST values (10001, 'My first post', 90001);
insert into POST values (10002, 'My second post', 90001);