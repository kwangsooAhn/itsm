/**
 * 게시판 관리
 */
DROP TABLE IF EXISTS portal_board_admin cascade;

CREATE TABLE portal_board_admin
(
	board_admin_id varchar(128) NOT NULL,
	board_admin_title varchar(512) NOT NULL,
	board_admin_desc text,
	board_admin_sort int,
	board_use_yn boolean,
	reply_yn boolean,
	comment_yn boolean,
	category_yn boolean,
	attach_yn boolean,
	attach_file_size bigint,
	create_user_key varchar(128),
	create_dt timestamp,
	update_user_key varchar(128),
	update_dt timestamp,
	CONSTRAINT portal_board_admin_pk PRIMARY KEY (board_admin_id)
);

COMMENT ON TABLE portal_board_admin IS '게시판 관리';
COMMENT ON COLUMN portal_board_admin.board_admin_id IS '게시판 관리 번호';
COMMENT ON COLUMN portal_board_admin.board_admin_title IS '게시판관리 제목';
COMMENT ON COLUMN portal_board_admin.board_admin_desc IS '게시판 관리 설명';
COMMENT ON COLUMN portal_board_admin.board_admin_sort IS '게시판 순서';
COMMENT ON COLUMN portal_board_admin.board_use_yn IS '게시판 사용여부';
COMMENT ON COLUMN portal_board_admin.reply_yn IS '답글 사용여부';
COMMENT ON COLUMN portal_board_admin.comment_yn IS '댓글 사용여부';
COMMENT ON COLUMN portal_board_admin.category_yn IS '카테고리 사용여부';
COMMENT ON COLUMN portal_board_admin.attach_yn IS '첨부파일 사용여부';
COMMENT ON COLUMN portal_board_admin.attach_file_size IS '첨부파일 용량';
COMMENT ON COLUMN portal_board_admin.create_user_key IS '등록자';
COMMENT ON COLUMN portal_board_admin.create_dt IS '등록일';
COMMENT ON COLUMN portal_board_admin.update_user_key IS '수정자';
COMMENT ON COLUMN portal_board_admin.update_dt IS '수정일';

insert into portal_board_admin values ('40288a19736b46fb01736b718cb60001', '기본 게시판', null, 1, true, true, true, true, true, 10, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
