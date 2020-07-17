/**
 * 사용자정의코드
 */
DROP TABLE IF EXISTS awf_custom_code cascade;

CREATE TABLE awf_custom_code
(
	custom_code_id varchar(128) NOT NULL,
	custom_code_name varchar(128) NOT NULL,
	type varchar(128) DEFAULT 'table' NOT NULL,
	target_table varchar(128),
	search_column varchar(128),
	value_column varchar(128),
	p_code varchar(128),
	condition varchar(512),
	create_user_key varchar(128) NOT NULL,
	create_dt timestamp NOT NULL,
	update_user_key varchar(128),
	update_dt timestamp,
	CONSTRAINT awf_custom_code_pk PRIMARY KEY (custom_code_id),
	CONSTRAINT awf_custom_code_uk UNIQUE (custom_code_name)
);

COMMENT ON TABLE awf_custom_code IS '사용자정의코드';
COMMENT ON COLUMN awf_custom_code.custom_code_id IS '사용자코드아이디';
COMMENT ON COLUMN awf_custom_code.custom_code_name IS '사용자정의코드이름';
COMMENT ON COLUMN awf_custom_code.type IS '타입';
COMMENT ON COLUMN awf_custom_code.target_table IS '대상테이블';
COMMENT ON COLUMN awf_custom_code.search_column IS '검색컬럼';
COMMENT ON COLUMN awf_custom_code.value_column IS '값컬럼';
COMMENT ON COLUMN awf_custom_code.p_code IS '부모코드';
COMMENT ON COLUMN awf_custom_code.condition IS '조건';
COMMENT ON COLUMN awf_custom_code.create_user_key IS '등록자';
COMMENT ON COLUMN awf_custom_code.create_dt IS '등록일';
COMMENT ON COLUMN awf_custom_code.update_user_key IS '수정자';
COMMENT ON COLUMN awf_custom_code.update_dt IS '수정일';

