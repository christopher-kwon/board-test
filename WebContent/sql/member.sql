
drop table member;
--1. index.jsp���� �����մϴ�.
--2. ������ ���� admin, ��� 1234�� ����ϴ�.
--3. ����� ������ 3�� ����ϴ�.

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
values('admin', '1234', '������', 99, '��', 'admin@admin.com');


select count(*) from member

