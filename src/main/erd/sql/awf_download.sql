/**
 * 자료실
 */
DROP TABLE IF EXISTS awf_download cascade;

CREATE TABLE awf_download
(
	download_id varchar(128) NOT NULL,
	download_seq bigint DEFAULT nextval('awf_download_seq') NOT NULL,
	download_category varchar(100) NOT NULL,
	download_title varchar(128) NOT NULL,
	views bigint DEFAULT 0 NOT NULL,
	create_user_key varchar(128) NOT NULL,
	create_dt timestamp NOT NULL,
	update_user_key varchar(128),
	update_dt timestamp,
	CONSTRAINT awf_download_pk PRIMARY KEY (download_id),
	CONSTRAINT awf_download_uk UNIQUE (download_seq)
);

COMMENT ON TABLE awf_download IS '자료실';
COMMENT ON COLUMN awf_download.download_id IS '자료아이디';
COMMENT ON COLUMN awf_download.download_seq IS '자료번호';
COMMENT ON COLUMN awf_download.download_category IS '자료카테고리';
COMMENT ON COLUMN awf_download.download_title IS '자료제목';
COMMENT ON COLUMN awf_download.views IS '조회수';
COMMENT ON COLUMN awf_download.create_user_key IS '등록자';
COMMENT ON COLUMN awf_download.create_dt IS '등록일';
COMMENT ON COLUMN awf_download.update_user_key IS '수정자';
COMMENT ON COLUMN awf_download.update_dt IS '수정일';