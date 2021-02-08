
drop table member;
--1. index.jsp에서 시작합니다.
--2. 관리자 계정 admin, 비번 1234를 만듭니다.
--3. 사용자 계정을 3개 만듭니다.

create table member(
id varchar2(12),
password varchar2(10),
name varchar2(15),
age number(2),
gender varchar2(3),
email varchar2(30),
PRIMARY KEY(id)
);

select * from member;

insert into member 
values('admin', '1234', '관리자', 99, '남', 'admin@admin.com');


select count(*) from member

