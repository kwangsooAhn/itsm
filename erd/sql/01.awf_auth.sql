/**
 * 권한
 */
DROP TABLE IF EXISTS awf_auth cascade;

CREATE TABLE awf_auth
(
	auth_id varchar(100) NOT NULL,
	auth_name varchar(128) NOT NULL,
	auth_desc text,
	create_user_key varchar(128),
	create_dt timestamp,
	update_user_key varchar(128),
	update_dt timestamp,
	CONSTRAINT awf_auth_pk PRIMARY KEY (auth_id),
	CONSTRAINT awf_auth_uk1 UNIQUE (auth_name)
);

COMMENT ON TABLE awf_auth IS '권한';
COMMENT ON COLUMN awf_auth.auth_id IS '권한아이디';
COMMENT ON COLUMN awf_auth.auth_name IS '권한명';
COMMENT ON COLUMN awf_auth.auth_desc IS '권한설명';
COMMENT ON COLUMN awf_auth.create_user_key IS '등록자';
COMMENT ON COLUMN awf_auth.create_dt IS '등록일';
COMMENT ON COLUMN awf_auth.update_user_key IS '수정자';
COMMENT ON COLUMN awf_auth.update_dt IS '수정일';

insert into awf_auth values ('general', '일반 사용', '신청서 작성 및 FAQ/자료실/공지사항을 조회할 수 있고, 게시판에 글을 등록하거나 조회할 수 있습니다.', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('workflow.manage', '업무흐름 관리', '문서양식, 프로세스를 편집할 수 있으며, 관련된 업무흐름을 설정할 수 있습니다.', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('workflow.expire', '업무 취소', '관련된 업무흐름을 처리할 수 있습니다.', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('cmdb.manage', 'CMDB 관리', 'CI 속성항목, 유형 등 CMDB 관련 설정을 할 수 있습니다.', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('cmdb.view', 'CMDB 조회', 'CMDB 내용을 검색하여 조회할 수 있습니다.', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('report.manage', '보고서 관리', '사용자 정의 차트를 편집하고, 보고서 템플릿을 생성할 수 있습니다.', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('report.view', '보고서 조회', '보고서 내용을 검색하여 조회할 수 있습니다.', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('system.manage', '시스템 관리', '"사용자, 역할, 스케줄러, 게시판, 코드 관리 권한을 가집니다.', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('portal.manage', '포털 관리', 'FAQ 등록, 게시판 생성, 공지사항 등록 등 포털과 관련된 설정이 가능합니다.', '0509e09412534a6e98f04ca79abb6424', now(), null, null);