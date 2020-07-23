/**
 * 공지사항
 */
DROP TABLE IF EXISTS portal_notice cascade;

CREATE TABLE portal_notice
(
	notice_no varchar(128) NOT NULL,
	notice_title varchar(256) NOT NULL,
	notice_contents varchar(4096) NOT NULL,
	pop_yn boolean NOT NULL,
	pop_strt_dt timestamp,
	pop_end_dt timestamp,
	pop_width int,
	pop_height int,
	top_notice_yn boolean NOT NULL,
	top_notice_strt_dt timestamp,
	top_notice_end_dt timestamp,
	create_user_key varchar(128),
	create_dt timestamp,
	update_user_key varchar(128),
	update_dt timestamp,
	CONSTRAINT portal_notice_pk PRIMARY KEY (notice_no)
);

COMMENT ON TABLE portal_notice IS '공지사항';
COMMENT ON COLUMN portal_notice.notice_no IS '공지사항 번호';
COMMENT ON COLUMN portal_notice.notice_title IS '공지사항 제목';
COMMENT ON COLUMN portal_notice.notice_contents IS '공지사항 내용';
COMMENT ON COLUMN portal_notice.pop_yn IS '팝업등록 여부';
COMMENT ON COLUMN portal_notice.pop_strt_dt IS '팝업 시작일시';
COMMENT ON COLUMN portal_notice.pop_end_dt IS '팝업 종료일시';
COMMENT ON COLUMN portal_notice.pop_width IS '팝업 넓이';
COMMENT ON COLUMN portal_notice.pop_height IS '팝업 높이';
COMMENT ON COLUMN portal_notice.top_notice_yn IS '상단등록 여부';
COMMENT ON COLUMN portal_notice.top_notice_strt_dt IS '상단등록 시작일시';
COMMENT ON COLUMN portal_notice.top_notice_end_dt IS '상단등록 종료일시';
COMMENT ON COLUMN portal_notice.create_user_key IS '등록자';
COMMENT ON COLUMN portal_notice.create_dt IS '등록일';
COMMENT ON COLUMN portal_notice.update_user_key IS '수정자';
COMMENT ON COLUMN portal_notice.update_dt IS '수정일';
