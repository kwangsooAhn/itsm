/**
 * 첨부파일연결매핑
 */
DROP TABLE IF EXISTS awf_file_own_map cascade;

CREATE TABLE awf_file_own_map
(
	own_id varchar(128) NOT NULL,
	file_seq bigint NOT NULL,
	CONSTRAINT awf_file_own_map_pk PRIMARY KEY (own_id, file_seq),
	CONSTRAINT awf_file_own_map_fk FOREIGN KEY (file_seq) REFERENCES awf_file_loc (seq)
);

COMMENT ON TABLE awf_file_own_map IS '첨부파일연결매핑';
COMMENT ON COLUMN awf_file_own_map.own_id IS '파일소유아이디';
COMMENT ON COLUMN awf_file_own_map.file_seq IS '파일관리번호';