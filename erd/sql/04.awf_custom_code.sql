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
    session_key varchar(128),
	create_user_key varchar(128),
	create_dt timestamp,
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
COMMENT ON COLUMN awf_custom_code.session_key IS '세션 사용 시 기본 값';
COMMENT ON COLUMN awf_custom_code.create_user_key IS '등록자';
COMMENT ON COLUMN awf_custom_code.create_dt IS '등록일';
COMMENT ON COLUMN awf_custom_code.update_user_key IS '수정자';
COMMENT ON COLUMN awf_custom_code.update_dt IS '수정일';

INSERT INTO awf_custom_code VALUES ('40288a19736b46fb01736b89e46c0008', '사용자 이름 검색', 'table', 'awf_user', 'user_name', 'user_key', null, '[{"conditionKey":"use_yn","conditionOperator":"equal","conditionValue":"true"}]', 'userName', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_custom_code values ('40288a19736b46fb01736b89e46c0009', '사용자 부서 검색', 'code', null, null, null, 'department.group', null ,'departmentName', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_custom_code values ('40288ab777dd21b50177dd52781e0000', '데이터베이스', 'code', null, null, null, 'cmdb.db.kind', null ,'', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_custom_code values ('40288a19736b46fb01736b89e46c0010', '서비스데스크 - 단순문의 : 서비스 항목', 'code', null, null, null, 'form.template.serviceDesk.inquiry.category', null ,'', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_custom_code values ('40288a19736b46fb01736b89e46c0011', '서비스데스크 - 장애신고 : 장애유형', 'code', null, null, null, 'form.template.serviceDesk.incident.category', null ,'', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_custom_code values ('40288a19736b46fb01736b89e46c0012', '서비스데스크 - 서비스요청 : 요청구분', 'code', null, null, null, 'form.template.serviceDesk.request.category', null ,'', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_custom_code values ('4028b22f7c96584f017c970ef75e0035', '어플리케이션 변경관리 -관련 서비스', 'code', null, null, null, 'form.template.changeControl.app.relatedService', null, '','0509e09412534a6e98f04ca79abb6424',  now(), null, null);
