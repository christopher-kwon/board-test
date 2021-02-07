drop table board cascade constraints;

create table board(
board_num number, 
board_name varchar2(30),
board_pass varchar2(30),
board_subject varchar2(300),
board_content varchar2(4000),
board_file varchar2(50),
board_re_ref number,
board_re_lev number,
board_re_seq number,
board_readcount number,
board_date DATE default sysdate,
primary key(board_num)
);

select max(board_num)+1 from board

select nvl(max(board_num), 0)+1 from board

select * from board;