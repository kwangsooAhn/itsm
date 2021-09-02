/**
 * 사용자정의코드테이블
 */
DROP TABLE IF EXISTS awf_custom_code_table cascade;

CREATE TABLE awf_custom_code_table
(
	custom_code_table varchar(128) NOT NULL,
	custom_code_table_name varchar(128) NOT NULL,
	CONSTRAINT awf_custom_code_table_pk PRIMARY KEY (custom_code_table)
);

COMMENT ON TABLE awf_custom_code_table IS '사용자정의코드테이블';
COMMENT ON COLUMN awf_custom_code_table.custom_code_table IS '테이블';
COMMENT ON COLUMN awf_custom_code_table.custom_code_table_name IS '테이블이름';

insert into awf_custom_code_table values ('awf_user', '사용자 정보 테이블');
insert into awf_custom_code_table values ('awf_role', '역할 정보 테이블');
