/**
 * 카테고리 관리
 */
DROP TABLE IF EXISTS portal_board_category cascade;

CREATE TABLE portal_board_category
(
	board_category_id varchar(128) NOT NULL,
	board_admin_id varchar(128) NOT NULL,
	board_category_name varchar(128) NOT NULL,
	board_category_sort int,
	create_user_key varchar(128) NOT NULL,
	create_dt timestamp NOT NULL,
	update_user_key varchar(128),
	update_dt timestamp,
	CONSTRAINT portal_board_category_pk PRIMARY KEY (board_category_id),
	CONSTRAINT portal_board_category_fk FOREIGN KEY (board_admin_id) REFERENCES portal_board_admin (board_admin_id)
);

COMMENT ON TABLE portal_board_category IS '카테고리 관리';
COMMENT ON COLUMN portal_board_category.board_category_id IS '카테고리 번호';
COMMENT ON COLUMN portal_board_category.board_admin_id IS '게시판 관리 번호';
COMMENT ON COLUMN portal_board_category.board_category_name IS '카테고리 명';
COMMENT ON COLUMN portal_board_category.board_category_sort IS '정렬순서';
COMMENT ON COLUMN portal_board_category.create_user_key IS '등록자';
COMMENT ON COLUMN portal_board_category.create_dt IS '등록일';
COMMENT ON COLUMN portal_board_category.update_user_key IS '수정자';
COMMENT ON COLUMN portal_board_category.update_dt IS '수정일';