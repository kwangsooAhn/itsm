/**
 * 자료실
 */
DROP TABLE IF EXISTS awf_archive cascade;

CREATE TABLE awf_archive
(
    archive_id varchar(128) NOT NULL,
    archive_seq bigint DEFAULT nextval('awf_archive_seq') NOT NULL,
    archive_category varchar(100) NOT NULL,
    archive_title varchar(128) NOT NULL,
    views bigint DEFAULT 0 NOT NULL,
    create_user_key varchar(128),
    create_dt timestamp,
    update_user_key varchar(128),
    update_dt timestamp,
    CONSTRAINT awf_archive_pk PRIMARY KEY (archive_id),
    CONSTRAINT awf_archive_uk UNIQUE (archive_seq)
);

COMMENT ON TABLE awf_archive IS '자료실';
COMMENT ON COLUMN awf_archive.archive_id IS '자료아이디';
COMMENT ON COLUMN awf_archive.archive_seq IS '자료번호';
COMMENT ON COLUMN awf_archive.archive_category IS '자료카테고리';
COMMENT ON COLUMN awf_archive.archive_title IS '자료제목';
COMMENT ON COLUMN awf_archive.views IS '조회수';
COMMENT ON COLUMN awf_archive.create_user_key IS '등록자';
COMMENT ON COLUMN awf_archive.create_dt IS '등록일';
COMMENT ON COLUMN awf_archive.update_user_key IS '수정자';
COMMENT ON COLUMN awf_archive.update_dt IS '수정일';
