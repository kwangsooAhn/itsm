/* Drop Sequences */

DROP SEQUENCE IF EXISTS awf_download_seq cascade;
DROP SEQUENCE IF EXISTS awf_file_loc_seq cascade;
DROP SEQUENCE IF EXISTS hibernate_sequence cascade;
DROP SEQUENCE IF EXISTS portal_board_seq cascade;
DROP SEQUENCE IF EXISTS schedule_history_seq cascade;


/* Create Sequences */
CREATE SEQUENCE awf_download_seq INCREMENT 1 MINVALUE 1 START 1;
CREATE SEQUENCE awf_file_loc_seq INCREMENT 1 MINVALUE 1 START 1;
CREATE SEQUENCE hibernate_sequence INCREMENT 1 MINVALUE 1 START 1;
CREATE SEQUENCE portal_board_seq INCREMENT 1 MINVALUE 1 START 1;
CREATE SEQUENCE schedule_history_seq INCREMENT 1 MINVALUE 1 START 1;

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

insert into awf_auth values ('action.cancel', '문서 취소', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('action.terminate', '문서 종결', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('auth.create', '권한 등록', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('auth.delete', '권한 삭제', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('auth.read', '권한 조회', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('auth.update', '권한 변경', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('board.admin.create', '게시판 관리 등록', '게시판 관리 등록 권한', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('board.admin.delete', '게시판 관리 삭제', '게시판 관리 삭제 권한', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('board.admin.read', '게시판 관리 조회', '게시판 관리 조회 권한', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('board.admin.update', '게시판 관리 변경', '게시판 관리 변경 권한', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('board.create', '게시판 등록', '게시판 등록 권한', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('board.delete', '게시판 삭제', '게시판 삭제 권한', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('board.read', '게시판 조회', '게시판 조회 권한', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('board.update', '게시판 변경', '게시판 변경 권한', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('chart.read', '통계 차트 조회', '통계 차트 조회 권한', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('chart.create', '통계 차트 등록', '통계 차트 등록 권한', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('chart.update', '통계 차트 수정', '통계 차트 수정 권한', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('chart.delete', '통계 차트 삭제', '통계 차트 삭제 권한', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('cmdb.attribute.read', 'CMDB Attribute 조회', 'CMDB Attribute 조회 권한', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('cmdb.attribute.create', 'CMDB Attribute 생성', 'CMDB Attribute 생성 권한', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('cmdb.attribute.update', 'CMDB Attribute 변경', 'CMDB Attribute 변경 권한', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('cmdb.attribute.delete', 'CMDB Attribute 삭제', 'CMDB Attribute 삭제 권한', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('cmdb.ci.read', 'CMDB CI 조회', 'CMDB CI 조회 권한', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('cmdb.ci.create', 'CMDB CI 생성', 'CMDB CI 생성 권한', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('cmdb.ci.update', 'CMDB CI 변경', 'CMDB CI 변경 권한', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('cmdb.ci.delete', 'CMDB CI 삭제', 'CMDB CI 삭제 권한', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('cmdb.class.read', 'CMDB Class 조회', 'CMDB Class 조회 권한', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('cmdb.class.create', 'CMDB Class 생성', 'CMDB Class 생성 권한', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('cmdb.class.update', 'CMDB Class 변경', 'CMDB Class 변경 권한', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('cmdb.class.delete', 'CMDB Class 삭제', 'CMDB Class 삭제 권한', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('cmdb.type.read', 'CMDB Type 조회', 'CMDB Type 조회 권한', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('cmdb.type.create', 'CMDB Type 생성', 'CMDB Type 생성 권한', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('cmdb.type.update', 'CMDB Type 변경', 'CMDB Type 변경 권한', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('cmdb.type.delete', 'CMDB Type 삭제', 'CMDB Type 삭제 권한', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('code.create', '코드 등록', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('code.delete', '코드 삭제', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('code.read', '코드 조회', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('code.update', '코드 수정', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('custom.code.create', '커스텀 코드 등록', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('custom.code.delete', '커스텀 코드 삭제', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('custom.code.read', '커스텀 코드 조회', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('custom.code.update', '커스텀 코드 수정', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('document.admin.create', '업무흐름 등록', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('document.admin.delete', '업무흐름 삭제', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('document.admin.read', '업무흐름 조회', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('document.admin.update', '업무흐름 수정', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('document.create', '신청서 등록', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('document.delete', '신청서 삭제', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('document.read', '신청서 조회', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('document.read.admin', '신청서 조회(admin)', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('document.update', '신청서수정', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('download.create', '자료실 등록', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('download.delete', '자료실 삭제', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('download.read', '자료실 조회', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('download.update', '자료실 수정', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('faq.create', 'FAQ 등록', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('faq.delete', 'FAQ 삭제', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('faq.read', 'FAQ 조회', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('faq.update', 'FAQ 변경', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('file.read', '파일관련 데이터 조회', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('form.create', '문서양식 등록', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('form.delete', '문서양식 삭제', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('form.read', '문서양식 조회', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('form.update', '문서양식 변경', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('image.create', '이미지 업로드', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('image.delete', '이미지 삭제', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('image.read', '이미지 조회', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('image.update', '이미지 수정', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('notice.create', '공지사항 등록', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('notice.delete', '공지사항 삭제', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('notice.read', '공지사항 조회', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('notice.update', '공지사항 변경', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('numbering.pattern.create', '패턴 등록', '패턴 등록 권한', '0509e09412534a6e98f04ca79abb6424', now(), null , null);
insert into awf_auth values ('numbering.pattern.delete', '패턴 삭제', '패턴 삭제 권한', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('numbering.pattern.read', '패턴 조회', '패턴 조회 권한', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('numbering.pattern.update', '패턴 수정', '패턴 수정 권한', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('numbering.rule.create', '문서번호 등록', '문서번호 등록 권한', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('numbering.rule.delete', '문서번호 삭제', '문서번호 삭제 권한', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('numbering.rule.read', '문서번호 조회', '문서번호 조회 권한', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('numbering.rule.update', '문서번호 수정', '문서번호 수정 권한', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('process.create', '프로세스 등록', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('process.delete', '프로세스 삭제', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('process.read', '프로세스 조회', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('process.update', '프로세스 변경', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('role.create', '역할 등록', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('role.delete', '역할 삭제', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('role.read', '역할 조회', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('role.update', '역할 변경', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('scheduler.create', '스케줄러 등록', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('scheduler.delete', '스케줄러 삭제', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('scheduler.execute', '스케줄러 실행', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('scheduler.read', '스케줄러 조회', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('scheduler.update', '스케줄러 변경', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('token.create', '처리할 문서 등록', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('token.read', '처리할 문서 조회', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('user.create', '사용자 추가', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('user.delete', '사용자 삭제', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('user.read', '사용자 조회', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('user.update', '사용자 변경', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);


/**
 * 아바타
 */
DROP TABLE IF EXISTS awf_avatar cascade;

/**
 * 코드정보
 */
DROP TABLE IF EXISTS awf_code cascade;

CREATE TABLE awf_code
(
	code varchar(100) NOT NULL,
	p_code varchar(100),
	code_value varchar(256),
	code_name varchar(128),
	code_desc varchar(512),
	editable boolean default false,
	level integer,
	seq_num integer,
	create_user_key varchar(128),
	create_dt timestamp,
	update_user_key varchar(128),
	update_dt timestamp,
	CONSTRAINT awf_code_pk PRIMARY KEY (code)
);

COMMENT ON TABLE awf_code IS '코드정보';
COMMENT ON COLUMN awf_code.code IS '코드';
COMMENT ON COLUMN awf_code.p_code IS '부모코드';
COMMENT ON COLUMN awf_code.code_value IS '코드 값';
COMMENT ON COLUMN awf_code.code_name IS '코드 명';
COMMENT ON COLUMN awf_code.code_desc IS '코드 설명';
COMMENT ON COLUMN awf_code.editable IS '수정가능여부';
COMMENT ON COLUMN awf_code.level IS '코드 레벨';
COMMENT ON COLUMN awf_code.seq_num IS '정렬 순서';
COMMENT ON COLUMN awf_code.create_user_key IS '등록자';
COMMENT ON COLUMN awf_code.create_dt IS '등록일';
COMMENT ON COLUMN awf_code.update_user_key IS '수정자';
COMMENT ON COLUMN awf_code.update_dt IS '수정일';

insert into awf_code values ('cmdb.db.kind', '', null, '데이터베이스', 'cmdb 데이터데이스 종류', TRUE, 1, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('cmdb.db.kind.oracle', 'cmdb.db.kind', 'oracle', 'Oracle', 'cmdb 데이터데이스 종류', TRUE, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('cmdb.db.kind.postgresql', 'cmdb.db.kind', 'postgresql', 'Postresql', '', TRUE, 2, 2,  '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('cmdb.db.kind.mssql', 'cmdb.db.kind', 'mssql','MSSQL', '', TRUE, 2, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('cmdb.db.kind.mysql', 'cmdb.db.kind', 'mysql','MYSQL', '', TRUE, 2, 4, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('cmdb.db.kind.tibero', 'cmdb.db.kind', 'tibero','TIBERO', '', TRUE, 2, 5, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('cmdb.db.kind.redis', 'cmdb.db.kind', 'redis','Redis', '', TRUE, 2, 6, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('cmdb.db.kind.rac', 'cmdb.db.kind', 'rac','RAC', '', TRUE, 2, 7, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('cmdb.db.kind.db2', 'cmdb.db.kind', 'db2','DB2', '', TRUE, 2, 8, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('cmdb.db.kind.altibase', 'cmdb.db.kind', 'altibase', 'Altibase', '', TRUE, 2, 9, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('cmdb.db.kind.sysbase', 'cmdb.db.kind', 'sysbase','SYBASE', '', TRUE, 2, 10, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('cmdb.db.kind.sysbaseiq', 'cmdb.db.kind', 'sysbaseiq', 'SYBASEIQ', '', TRUE, 2, 11, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('cmdb.db.kind.mongodb', 'cmdb.db.kind', 'mongodb', 'MONGODB', '', TRUE, 2, 12, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('cmdb.db.kind.ppas', 'cmdb.db.kind', 'ppas', 'PPAS', '', TRUE, 2, 13, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('cmdb.db.kind.cubrid', 'cmdb.db.kind', 'cubrid', 'CUBRID', '', TRUE, 2, 14, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('cmdb.db.kind.mariadb', 'cmdb.db.kind', 'mariadb', 'MARIADB', '', TRUE, 2, 15, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('cmdb.db.kind.informix', 'cmdb.db.kind', 'informix', 'INFORMIX', '', TRUE, 2, 16, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('root', null, null, 'ROOT', null, false, 0, 0, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('assignee', 'root', null, '담당자', null, false, 1, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('assignee.type', 'assignee', null, '담당자 타입', null, false, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('assignee.type.assignee', 'assignee.type', null, '지정 담당자', null, false, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('assignee.type.candidate.groups', 'assignee.type', null, '담당자 후보그룹', null, false, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('assignee.type.candidate.users', 'assignee.type', null, '담당자 후보목록', null, false, 3, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('document', 'root', null, '신청서', null, false, 1, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('document.group', 'document', null, '신청서 목록', null, true, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('servicedesk.incident', 'document.group', '장애문의', '장애문의', null, true, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('servicedesk.inquiry', 'document.group', '단순문의', '단순문의', null, true, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('servicedesk.request', 'document.group', '서비스요청', '서비스요청', null, true, 3, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('document.status', 'document', null, '신청서 상태', null, false, 2, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('document.status.temporary', 'document.status', '임시', '임시', null, false, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('document.status.destroy', 'document.status', '폐기', '폐기', null, false, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('document.status.use', 'document.status', '사용', '사용', null, false, 3, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('download', 'root', null, '자료실', null, true, 1, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('download.category', 'download', null, '자료실 카테고리', null, true, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('download.category.companyPolicy', 'download.category', null, '회사규정', null, true, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('download.category.etc', 'download.category', null, '기타', null, true, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('faq', 'root', null, 'FAQ', null, false, 1, 4, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('faq.category', 'faq', null, 'FAQ 카테고리', null, false, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('faq.category.etc', 'faq.category', null, '기타', null, false, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('faq.category.setting', 'faq.category', null, '설정', null, false, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('faq.category.techSupport', 'faq.category', null, '기술지원', null, false, 3, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('form', 'root', null, '문서양식', null, false, 1, 5, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('form.lang', 'form', null, '문서양식 언어', null, false, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('form.lang.ko', 'form.lang', null, '한국어', null, false, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('numbering', 'root', null, '문서번호 규칙 패턴', null, false, 1, 6, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('numbering.pattern', 'numbering', null, '문서규칙 패턴', null, false, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('numbering.pattern.format', 'numbering.pattern', null, '문서규칙 포맷', null, false, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('numbering.pattern.format.yyyyMMdd', 'numbering.pattern.format', 'yyyyMMdd', '날짜형패턴', null, false, 4, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('numbering.pattern.format.yyyyddMM', 'numbering.pattern.format', 'yyyyddMM', '날짜형패턴', null, false, 4, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('numbering.pattern.format.MMddyyyy', 'numbering.pattern.format', 'MMddyyyy', '날짜형패턴', null, false, 4, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('numbering.pattern.format.ddMMyyyy', 'numbering.pattern.format', 'ddMMyyyy', '날짜형패턴', null, false, 4, 4, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('reception_type', 'root', '접수유형', null, null, true, 1, 7, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('email', 'reception_type', '이메일', '이메일', null, true, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('post', 'reception_type', '우편', '우편', null, true, 2, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('telephone', 'reception_type', '전화', '전화', null, true, 2, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('scheduler', 'root', null, '스케줄러', null, false, 1, 13, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('scheduler.taskType', 'scheduler', null, '작업 유형', null, false, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('scheduler.taskType.class', 'scheduler.taskType', 'class', 'CLASS', null, false, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('scheduler.taskType.query', 'scheduler.taskType', 'query', 'QUERY', null, false, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('scheduler.executeCycleType', 'scheduler', null, '실행 유형', null, false, 2, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('scheduler.executeCycleType.fixedDelay', 'scheduler.executeCycleType', 'fixedDelay', 'FIXED_DELAY', null, false, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('scheduler.executeCycleType.fixedRate', 'scheduler.executeCycleType', 'fixedRate', 'FIXED_RATE', null, false, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('scheduler.executeCycleType.cron', 'scheduler.executeCycleType', 'cron', 'CRON', null, false, 3, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('script', 'root', null, null, null, false, 1, 8, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('script.type', 'script', 'script.type', 'Script Type', null, false, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('script.type.cmdb', 'script.type', 'script.type.cmdb', '[CMDB] CI 반영', null, false, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('script.type.document.attachFile', 'script.type', 'script.type.document.attachFile', '[문서편집] 첨부파일', null, false, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('token', 'root', null, '토큰 관련 코드', null, false, 1, 9, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('token.status', 'token', null, '토큰 상태 코드', null, false, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('token.status.cancel', 'token.status', null, '취소', null, false, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('token.status.finish', 'token.status', null, '처리 완료', null, false, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('token.status.reject', 'token.status', null, '반려', null, false, 3, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('token.status.running', 'token.status', null, '진행 중', null, false, 3, 4, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('token.status.terminate', 'token.status', null, '종료', null, false, 3, 5, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('token.status.waiting', 'token.status', null, '대기 중', null, false, 3, 6, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('token.status.withdraw', 'token.status', null, '회수', null, false, 3, 7, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user', 'root', null, '사용자', null, false,1,10, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.id', 'user', null, '아이디', null, false,2,1, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.name', 'user', null, '이름', null, false,2,2, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.department', 'user', null, '부서', null, false,2,3, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.position', 'user', null, '직책', null, false,2,4, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.extensionNumber', 'user', null, '내선번호', null, false,2,5, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.date', 'user', null, '사용자 날짜 포맷', null, false,2,6, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.date.yyyymmdd', 'user.date', 'yyyy-MM-dd', null, null, false,3,1, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.date.ddmmyyyy', 'user.date', 'dd-MM-yyyy', null, null, false,3,2, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.date.mmddyyyy', 'user.date', 'MM-dd-yyyy', null, null, false,3,3, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.date.yyyyddmm', 'user.date', 'yyyy-dd-MM', null, null, false,3,4, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.default', 'user', null, '기본 값', null, false,2,7, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.default.menu', 'user.default', null, '기본 메뉴', null, false,3,1, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.default.menu.dashboard', 'user.default.menu', 'dashboard', '개인 현황판 메뉴 아이디', null, true,4,1, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.default.role', 'user.default', null, '기본 역할', null, false,3,2, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.default.role.users.general', 'user.default.role', 'users.general', '역할 - 사용자 일반', null, false,4,1, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.default.url', 'user.default', null, '기본 URL', null, false,3,3, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.default.url.dashboard', 'user.default.url', '/dashboard/view', '개인 현황판 URL', null, true,4,1, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.lang', 'user', null, '언어', null, false,2,8, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.lang.ko', 'user.lang', 'ko', '한국어', null, false,3,1, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.lang.en', 'user.lang', 'en', '영어', null, false,3,2, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.platform', 'user', null, '플랫폼', null, false,2,9, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.platform.alice', 'user.platform', null, 'Alice', null, false,3,1, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.platform.google', 'user.platform', null, 'Google', null, false,3,2, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.platform.kakao', 'user.platform', null, 'Kakao', null, false,3,3, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.search', 'user', null, '검색 목록', null, false,2,10, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.search.department', 'user.search', null, '부서', null, false,3,1, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.search.extensionNumber', 'user.search', null, '내선번호', null, false,3,2, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.search.id', 'user.search', null, '아이디', null, false,3,3, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.search.mobileNumber', 'user.search', null, '핸드폰 번호', null, false,3,4, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.search.name', 'user.search', null, '이름', null, false,3,5, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.search.officeNumber', 'user.search', null, '사무실 번호', null, false,3,6, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.search.position', 'user.search', null, '직책', null, false,3,7, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.status', 'user', null, '계정 상태', null, false,2,11, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.status.certified', 'user.status', null, '인증 완료', null, false,3,1, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.status.signup', 'user.status', null, '가입', null, false,3,2, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.theme', 'user', null, '테마', null, false,2,12, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.theme.default', 'user.theme', 'default', '기본 테마', null, false,3,1, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.theme.dark', 'user.theme', 'dark', '어두운 테마', null, false,3,2, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.time', 'user', null, '사용자 시간 포맷', null, false,2,13, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('24', 'user.time', 'HH:mm', null, null, false,3,1, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('12', 'user.time', 'hh:mm a', null, null, false,3,2, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('department', 'root', null, '부서 관리', null, false,1,11, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('department.group', 'department', null, '부서 명', null, false,2,1, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('department.group.design', 'department.group', 'DESIGN', 'DESIGN', null, false,3,1, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('department.group.itsm', 'department.group', 'ITSM', 'ITSM', null, false,3,2, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('department.group.tc', 'department.group', 'TC', 'TC', null, false,3,3, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('version', 'root', null, null, null, false, 1, 12, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('version.workflow', 'version', '20200515', null, null, false, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('customCode', 'root', null, '커스텀코드', null, false, 1, 13, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('customCode.type', 'customCode', null, '신청서 목록', null, true, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('customCode.type.table', 'customCode.type', 'table', '테이블', null, true, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('customCode.type.code', 'customCode.type', 'code', '코드', null, true, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('chart', 'root', null, 'CHART', null, false, 1, 14, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('chart.type', 'chart', null, 'CHART TYPE', null, true, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('chart.type.basicLine', 'chart.type', 'chart.basicLine', 'Basic Line Chart', null, true, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('chart.type.pie', 'chart.type', 'chart.pie', 'Pie Chart', null, true, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('chart.type.stackedColumn', 'chart.type', 'chart.stackedColumn', 'Stacked Column Chart', null, true, 3, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('chart.operation', 'chart', null, 'CHART OPERATION', null, true, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('chart.operation.count', 'chart.operation', 'count', '카운트', null, true, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('chart.operation.percent', 'chart.operation', 'percent', '퍼센트', null, true, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('chart.unit', 'chart', null, 'CHART UNIT', null, true, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('chart.unit.year', 'chart.unit', 'Y', '년', null, true, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('chart.unit.month', 'chart.unit', 'M', '월', null, true, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('chart.unit.day', 'chart.unit', 'D', '일', null, true, 3, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('chart.unit.hour', 'chart.unit', 'H', '시간', null, true, 3, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);

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
COMMENT ON COLUMN awf_custom_code.create_user_key IS '등록자';
COMMENT ON COLUMN awf_custom_code.create_dt IS '등록일';
COMMENT ON COLUMN awf_custom_code.update_user_key IS '수정자';
COMMENT ON COLUMN awf_custom_code.update_dt IS '수정일';

insert into awf_custom_code values ('40288a19736b46fb01736b89e46c0008', '사용자 이름 검색', 'table', 'awf_user', 'user_name', 'user_key', null, '{}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_custom_code values ('40288ab777dd21b50177dd52781e0000', '데이터베이스', 'code', null, null, null, 'cmdb.db.kind', null, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
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

/**
 * 사용자정의코드컬럼
 */
DROP TABLE IF EXISTS awf_custom_code_column cascade;

CREATE TABLE awf_custom_code_column
(
	custom_code_table varchar(128) NOT NULL,
	custom_code_type varchar(100) NOT NULL,
	custom_code_column varchar(128) NOT NULL,
	custom_code_column_name varchar(128) NOT NULL,
	CONSTRAINT awf_custom_code_column_pk PRIMARY KEY (custom_code_table, custom_code_type, custom_code_column),
	CONSTRAINT awf_custom_code_column_fk FOREIGN KEY (custom_code_table) REFERENCES awf_custom_code_table (custom_code_table)
);

COMMENT ON TABLE awf_custom_code_column IS '사용자정의코드컬럼';
COMMENT ON COLUMN awf_custom_code_column.custom_code_table IS '테이블';
COMMENT ON COLUMN awf_custom_code_column.custom_code_type IS '타입';
COMMENT ON COLUMN awf_custom_code_column.custom_code_column IS '컬럼';
COMMENT ON COLUMN awf_custom_code_column.custom_code_column_name IS '컬럼이름';

insert into awf_custom_code_column values ('awf_role', 'value', 'role_id', '역할 아이디');
insert into awf_custom_code_column values ('awf_role', 'search', 'role_name', '역할명');
insert into awf_custom_code_column values ('awf_role', 'search', 'role_id', '역할 아이디');
insert into awf_custom_code_column values ('awf_user', 'search', 'department', '사용자 부서');
insert into awf_custom_code_column values ('awf_user', 'search', 'user_name', '사용자 이름');
insert into awf_custom_code_column values ('awf_user', 'search', 'position', '사용자 직급');
insert into awf_custom_code_column values ('awf_user', 'value', 'user_name', '사용자 이름');
insert into awf_custom_code_column values ('awf_user', 'value', 'user_key', '사용자 식별키');

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
	create_user_key varchar(128),
	create_dt timestamp,
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

/**
 * 파일관리
 */
DROP TABLE IF EXISTS awf_file_loc cascade;

CREATE TABLE awf_file_loc
(
	seq bigint NOT NULL,
	file_owner varchar(128),
	uploaded boolean DEFAULT 'false',
	uploaded_location varchar(512),
	random_name varchar(512),
	origin_name varchar(512),
	file_size bigint,
	sort int,
	create_user_key varchar(128),
	create_dt timestamp,
	update_user_key varchar(128),
	update_dt timestamp,
	CONSTRAINT awf_file_loc_pk PRIMARY KEY (seq)
);

COMMENT ON TABLE awf_file_loc IS '파일관리';
COMMENT ON COLUMN awf_file_loc.seq IS '파일관리번호';
COMMENT ON COLUMN awf_file_loc.file_owner IS '파일소유자';
COMMENT ON COLUMN awf_file_loc.uploaded IS '파일업로드여부';
COMMENT ON COLUMN awf_file_loc.uploaded_location IS '업로드경로';
COMMENT ON COLUMN awf_file_loc.random_name IS '난수화된파일명';
COMMENT ON COLUMN awf_file_loc.origin_name IS '원본파일명';
COMMENT ON COLUMN awf_file_loc.file_size IS '파일크기';
COMMENT ON COLUMN awf_file_loc.sort IS '정렬순서';
COMMENT ON COLUMN awf_file_loc.create_user_key IS '등록자';
COMMENT ON COLUMN awf_file_loc.create_dt IS '등록일';
COMMENT ON COLUMN awf_file_loc.update_user_key IS '수정자';
COMMENT ON COLUMN awf_file_loc.update_dt IS '수정일';

/**
 * 파일확장자관리
 */
DROP TABLE IF EXISTS awf_file_name_extension cascade;

CREATE TABLE awf_file_name_extension
(
	file_name_extension varchar(128) NOT NULL,
	file_content_type varchar(128) NOT NULL,
	create_user_key varchar(128),
	create_dt timestamp,
	update_user_key varchar(128),
	update_dt timestamp,
	CONSTRAINT awf_file_name_extension_pk PRIMARY KEY (file_name_extension)
);

COMMENT ON TABLE awf_file_name_extension IS '파일확장자관리';
COMMENT ON COLUMN awf_file_name_extension.file_name_extension IS '파일확장자명';
COMMENT ON COLUMN awf_file_name_extension.file_content_type IS '파일컨텐트타입';
COMMENT ON COLUMN awf_file_name_extension.create_user_key IS '등록자';
COMMENT ON COLUMN awf_file_name_extension.create_dt IS '등록일시';
COMMENT ON COLUMN awf_file_name_extension.update_user_key IS '수정자';
COMMENT ON COLUMN awf_file_name_extension.update_dt IS '수정일시';

insert into awf_file_name_extension values ('TXT', 'text/plain', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_file_name_extension values ('PDF', 'application/pdf', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_file_name_extension values ('JPG', 'image/jpeg', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_file_name_extension values ('JPEG', 'image/jpeg', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_file_name_extension values ('PNG', 'image/png', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_file_name_extension values ('GIF', 'image/gif', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_file_name_extension values ('DOC', 'application/msword', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_file_name_extension values ('DOCX', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_file_name_extension values ('XLS', 'application/vnd.ms-excel', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_file_name_extension values ('XLSX', 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_file_name_extension values ('PPTX', 'application/vnd.openxmlformats-officedocument.presentationml.presentation', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_file_name_extension values ('ZIP', 'application/zip', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_file_name_extension values ('HWP', 'application/x-tika-msoffice', '0509e09412534a6e98f04ca79abb6424', now(), null, null);

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
/**
 * 접근허용IP정보
 */
DROP TABLE IF EXISTS awf_ip_verification cascade;

CREATE TABLE awf_ip_verification
(
	ip_addr varchar(128) NOT NULL,
	ip_explain varchar(512),
	create_user_key varchar(128),
	create_dt timestamp,
	update_user_key varchar(128),
	update_dt timestamp,
	CONSTRAINT awf_ip_verification_pk PRIMARY KEY (ip_addr)
);

COMMENT ON TABLE awf_ip_verification IS '접근허용IP정보';
COMMENT ON COLUMN awf_ip_verification.ip_addr IS 'IP주소';
COMMENT ON COLUMN awf_ip_verification.ip_explain IS '설명';
COMMENT ON COLUMN awf_ip_verification.create_user_key IS '등록자';
COMMENT ON COLUMN awf_ip_verification.create_dt IS '등록일';
COMMENT ON COLUMN awf_ip_verification.update_user_key IS '수정자';
COMMENT ON COLUMN awf_ip_verification.update_dt IS '수정일';

insert into awf_ip_verification values ('0:0:0:0:0:0:0:1');
insert into awf_ip_verification values ('192.*.*.*');
/**
 * 메뉴정보
 */
DROP TABLE IF EXISTS awf_menu cascade;

CREATE TABLE awf_menu
(
	menu_id varchar(100) NOT NULL,
	p_menu_id varchar(100) NOT NULL,
	url varchar(512) NOT NULL,
	sort int DEFAULT 0 NOT NULL,
	use_yn boolean DEFAULT 'true' NOT NULL,
	CONSTRAINT awf_menu_pk PRIMARY KEY (menu_id)
);

COMMENT ON TABLE awf_menu IS '메뉴정보';
COMMENT ON COLUMN awf_menu.menu_id IS '메뉴아이디';
COMMENT ON COLUMN awf_menu.p_menu_id IS '부모메뉴아이디';
COMMENT ON COLUMN awf_menu.url IS '요청url';
COMMENT ON COLUMN awf_menu.sort IS '정렬순서';
COMMENT ON COLUMN awf_menu.use_yn IS '사용여부';

insert into awf_menu values ('dashboard', 'menu', '/dashboard/view', 1,TRUE);
insert into awf_menu values ('token', 'menu', '/tokens/search', 2,TRUE);
insert into awf_menu values ('document', 'menu', '/documents/search', 3,TRUE);
insert into awf_menu values ('notice', 'menu', '/notices/search', 4,TRUE);
insert into awf_menu values ('faq', 'menu', '/faqs/search', 5,TRUE);
insert into awf_menu values ('download', 'menu', '/downloads/search', 6,TRUE);
insert into awf_menu values ('board', 'menu', '/boards/articles/search', 7,TRUE);
insert into awf_menu values ('chart', 'menu', '/charts/search', 8,TRUE);
insert into awf_menu values ('config', 'menu', '', 9,TRUE);
insert into awf_menu values ('config.user', 'config', '/users/search', 1,TRUE);
insert into awf_menu values ('config.auth', 'config', '/auths/edit', 2,TRUE);
insert into awf_menu values ('config.role', 'config', '/roles/edit', 3,TRUE);
insert into awf_menu values ('config.boardAdmin', 'config', '/boards/search', 4,TRUE);
insert into awf_menu values ('config.code', 'config', '/codes/edit', 5,TRUE);
insert into awf_menu values ('config.scheduler', 'config', '/schedulers/search', 6,TRUE);
insert into awf_menu values ('workflow', 'menu', '', 10,TRUE);
insert into awf_menu values ('workflow.process', 'workflow', '/processes/search', 1,TRUE);
insert into awf_menu values ('workflow.form', 'workflow', '/forms/search', 2,TRUE);
insert into awf_menu values ('workflow.workflowAdmin', 'workflow', '/workflows/search', 3,TRUE);
insert into awf_menu values ('workflow.customCode', 'workflow', '/custom-codes/search', 4,TRUE);
insert into awf_menu values ('workflow.image', 'workflow', '/images', 5,TRUE);
insert into awf_menu values ('workflow.numberingPattern', 'workflow', '/numberingPatterns/edit', 6, true);
insert into awf_menu values ('workflow.numberingRule', 'workflow', '/numberingRules/edit', 7, true);
insert into awf_menu values ('cmdb', 'menu', '', 11, 'TRUE');
insert into awf_menu values ('cmdb.attribute', 'cmdb', '/cmdb/attributes/search', 1, 'TRUE');
insert into awf_menu values ('cmdb.class', 'cmdb', '/cmdb/class/edit', 2, 'TRUE');
insert into awf_menu values ('cmdb.type', 'cmdb', '/cmdb/types/edit', 3, 'TRUE');
insert into awf_menu values ('cmdb.ci', 'cmdb', '/cmdb/cis/search', 4, 'TRUE');

/**
 * 권한별메뉴매핑
 */

DROP TABLE IF EXISTS awf_menu_auth_map cascade;

CREATE TABLE awf_menu_auth_map
(
	menu_id varchar(100) NOT NULL,
	auth_id varchar(100) NOT NULL,
	CONSTRAINT awf_menu_auth_map_pk PRIMARY KEY (menu_id, auth_id),
	CONSTRAINT awf_menu_auth_map_fk1 FOREIGN KEY (menu_id) REFERENCES awf_menu (menu_id),
	CONSTRAINT awf_menu_auth_map_fk2 FOREIGN KEY (auth_id) REFERENCES awf_auth (auth_id)
);

COMMENT ON TABLE awf_menu_auth_map IS '권한별메뉴매핑';
COMMENT ON COLUMN awf_menu_auth_map.menu_id IS '메뉴아이디';
COMMENT ON COLUMN awf_menu_auth_map.auth_id IS '권한아이디';

insert into awf_menu_auth_map values ('chart', 'chart.read');
insert into awf_menu_auth_map values ('config', 'user.read');
insert into awf_menu_auth_map values ('config.auth', 'auth.read');
insert into awf_menu_auth_map values ('config.boardAdmin', 'board.admin.update');
insert into awf_menu_auth_map values ('config.boardAdmin', 'board.admin.create');
insert into awf_menu_auth_map values ('config.boardAdmin', 'board.admin.read');
insert into awf_menu_auth_map values ('config.boardAdmin', 'board.admin.delete');
insert into awf_menu_auth_map values ('config.code', 'code.update');
insert into awf_menu_auth_map values ('config.code', 'code.delete');
insert into awf_menu_auth_map values ('config.code', 'code.create');
insert into awf_menu_auth_map values ('config.code', 'code.read');
insert into awf_menu_auth_map values ('config.role', 'role.read');
insert into awf_menu_auth_map values ('config.user', 'user.read');
insert into awf_menu_auth_map values ('config.scheduler', 'scheduler.create');
insert into awf_menu_auth_map values ('config.scheduler', 'scheduler.delete');
insert into awf_menu_auth_map values ('config.scheduler', 'scheduler.read');
insert into awf_menu_auth_map values ('config.scheduler', 'scheduler.update');
insert into awf_menu_auth_map values ('dashboard', 'document.read');
insert into awf_menu_auth_map values ('document', 'document.read');
insert into awf_menu_auth_map values ('document', 'document.create');
insert into awf_menu_auth_map values ('token', 'token.create');
insert into awf_menu_auth_map values ('token', 'token.read');
insert into awf_menu_auth_map values ('board', 'board.create');
insert into awf_menu_auth_map values ('board', 'board.read');
insert into awf_menu_auth_map values ('board', 'board.update');
insert into awf_menu_auth_map values ('board', 'board.delete');
insert into awf_menu_auth_map values ('download', 'download.delete');
insert into awf_menu_auth_map values ('download', 'download.update');
insert into awf_menu_auth_map values ('download', 'download.read');
insert into awf_menu_auth_map values ('download', 'download.create');
insert into awf_menu_auth_map values ('faq', 'faq.update');
insert into awf_menu_auth_map values ('faq', 'faq.read');
insert into awf_menu_auth_map values ('faq', 'faq.delete');
insert into awf_menu_auth_map values ('faq', 'faq.create');
insert into awf_menu_auth_map values ('notice', 'notice.update');
insert into awf_menu_auth_map values ('notice', 'notice.create');
insert into awf_menu_auth_map values ('notice', 'notice.read');
insert into awf_menu_auth_map values ('notice', 'notice.delete');
insert into awf_menu_auth_map values ('workflow', 'process.delete');
insert into awf_menu_auth_map values ('workflow', 'process.update');
insert into awf_menu_auth_map values ('workflow', 'custom.code.create');
insert into awf_menu_auth_map values ('workflow', 'custom.code.delete');
insert into awf_menu_auth_map values ('workflow', 'custom.code.read');
insert into awf_menu_auth_map values ('workflow', 'custom.code.update');
insert into awf_menu_auth_map values ('workflow', 'document.admin.create');
insert into awf_menu_auth_map values ('workflow', 'document.admin.delete');
insert into awf_menu_auth_map values ('workflow', 'document.admin.read');
insert into awf_menu_auth_map values ('workflow', 'document.admin.update');
insert into awf_menu_auth_map values ('workflow', 'form.create');
insert into awf_menu_auth_map values ('workflow', 'form.delete');
insert into awf_menu_auth_map values ('workflow', 'form.read');
insert into awf_menu_auth_map values ('workflow', 'form.update');
insert into awf_menu_auth_map values ('workflow', 'image.create');
insert into awf_menu_auth_map values ('workflow', 'image.delete');
insert into awf_menu_auth_map values ('workflow', 'image.read');
insert into awf_menu_auth_map values ('workflow', 'image.update');
insert into awf_menu_auth_map values ('workflow', 'process.create');
insert into awf_menu_auth_map values ('workflow', 'process.read');
insert into awf_menu_auth_map values ('workflow.customCode', 'custom.code.read');
insert into awf_menu_auth_map values ('workflow.customCode', 'custom.code.update');
insert into awf_menu_auth_map values ('workflow.customCode', 'custom.code.delete');
insert into awf_menu_auth_map values ('workflow.customCode', 'custom.code.create');
insert into awf_menu_auth_map values ('workflow.workflowAdmin', 'document.admin.create');
insert into awf_menu_auth_map values ('workflow.workflowAdmin', 'document.admin.update');
insert into awf_menu_auth_map values ('workflow.workflowAdmin', 'document.admin.delete');
insert into awf_menu_auth_map values ('workflow.workflowAdmin', 'document.admin.read');
insert into awf_menu_auth_map values ('workflow.form', 'form.delete');
insert into awf_menu_auth_map values ('workflow.form', 'form.create');
insert into awf_menu_auth_map values ('workflow.form', 'form.read');
insert into awf_menu_auth_map values ('workflow.form', 'form.update');
insert into awf_menu_auth_map values ('workflow.image', 'image.delete');
insert into awf_menu_auth_map values ('workflow.image', 'image.update');
insert into awf_menu_auth_map values ('workflow.image', 'image.read');
insert into awf_menu_auth_map values ('workflow.image', 'image.create');
insert into awf_menu_auth_map values ('workflow.numberingPattern', 'numbering.pattern.delete');
insert into awf_menu_auth_map values ('workflow.numberingPattern', 'numbering.pattern.create');
insert into awf_menu_auth_map values ('workflow.numberingPattern', 'numbering.pattern.read');
insert into awf_menu_auth_map values ('workflow.numberingPattern', 'numbering.pattern.update');
insert into awf_menu_auth_map values ('workflow.numberingRule', 'numbering.rule.delete');
insert into awf_menu_auth_map values ('workflow.numberingRule', 'numbering.rule.create');
insert into awf_menu_auth_map values ('workflow.numberingRule', 'numbering.rule.read');
insert into awf_menu_auth_map values ('workflow.numberingRule', 'numbering.rule.update');
insert into awf_menu_auth_map values ('workflow.process', 'process.delete');
insert into awf_menu_auth_map values ('workflow.process', 'process.create');
insert into awf_menu_auth_map values ('workflow.process', 'process.update');
insert into awf_menu_auth_map values ('workflow.process', 'process.read');
insert into awf_menu_auth_map values ('cmdb', 'cmdb.attribute.read');
insert into awf_menu_auth_map values ('cmdb', 'cmdb.type.read');
insert into awf_menu_auth_map values ('cmdb.attribute', 'cmdb.attribute.read');
insert into awf_menu_auth_map values ('cmdb.ci', 'cmdb.ci.read');
insert into awf_menu_auth_map values ('cmdb.class', 'cmdb.class.read');
insert into awf_menu_auth_map values ('cmdb.type', 'cmdb.type.read');

/**
 * 알림
 */
DROP TABLE IF EXISTS awf_notification cascade;

CREATE TABLE awf_notification
(
	notification_id varchar(128) NOT NULL,
	received_user varchar(128) NOT NULL,
	title varchar(128) NOT NULL,
	message varchar(1024),
	instance_id varchar(128),
	confirm_yn boolean DEFAULT 'false',
	display_yn boolean DEFAULT 'false',
	create_user_key varchar(128),
	create_dt timestamp,
	update_user_key varchar(128),
	update_dt timestamp,
	CONSTRAINT awf_notification_pk PRIMARY KEY (notification_id)
);

COMMENT ON TABLE awf_notification IS '알림';
COMMENT ON COLUMN awf_notification.notification_id IS '알림아이디';
COMMENT ON COLUMN awf_notification.received_user IS '수신사용자';
COMMENT ON COLUMN awf_notification.title IS '제목';
COMMENT ON COLUMN awf_notification.message IS '메시지';
COMMENT ON COLUMN awf_notification.instance_id IS '인스턴스아이디';
COMMENT ON COLUMN awf_notification.confirm_yn IS '확인여부';
COMMENT ON COLUMN awf_notification.display_yn IS '표시여부';
COMMENT ON COLUMN awf_notification.create_user_key IS '등록자';
COMMENT ON COLUMN awf_notification.create_dt IS '등록일';
COMMENT ON COLUMN awf_notification.update_user_key IS '수정자';
COMMENT ON COLUMN awf_notification.update_dt IS '수정일';

/**
 * 넘버링정보
 */
DROP TABLE IF EXISTS awf_numbering_rule cascade;

CREATE TABLE awf_numbering_rule
(
	numbering_id varchar(128) NOT NULL,
	numbering_name varchar(255) NOT NULL,
	numbering_desc text,
	latest_value text,
	latest_date timestamp,
	CONSTRAINT awf_numbering_rule_pk PRIMARY KEY (numbering_id)
);

COMMENT ON TABLE awf_numbering_rule IS '넘버링정보';
COMMENT ON COLUMN awf_numbering_rule.numbering_id IS '넘버링아이디';
COMMENT ON COLUMN awf_numbering_rule.numbering_name IS '넘버링명';
COMMENT ON COLUMN awf_numbering_rule.numbering_desc IS '넘버링설명';
COMMENT ON COLUMN awf_numbering_rule.latest_value IS '최근값';
COMMENT ON COLUMN awf_numbering_rule.latest_date IS '최근날짜';

insert into awf_numbering_rule values ('60211d93621zd1f126241s053c890122', 'TEST-yyyyMMdd-000', '테스트 문서');
insert into awf_numbering_rule values ('40125c91714df6c325714e053c890125', 'CSR-yyyyMMdd-000', '신청서 작성시 발생한 문서번호');
insert into awf_numbering_rule values ('40288ab7772dae0301772dbca28a0004', 'SAT-yyyyMMdd-000', '만족도문서번호');
insert into awf_numbering_rule values ('40288ab777f04ed90177f05e5ad7000a', 'CFG-yyyyMMdd-000', '구성관리 문서번호');
insert into awf_numbering_rule values ('4028b8817880d833017880f34ae10003', 'REL_yyyyMMdd-000', '릴리즈관리 문서번호');
insert into awf_numbering_rule values ('4028b25d7886e2d801788704dd8e0002', 'RFC-yyyyMMdd-000', '인프라, 어플리케이션 변경관리에서 사용되는 문서번호');

/**
 * 넘버링패턴정보
 */
DROP TABLE IF EXISTS awf_numbering_pattern cascade;

CREATE TABLE awf_numbering_pattern
(
	pattern_id varchar(128) NOT NULL,
	pattern_name varchar(255) NOT NULL,
	pattern_type varchar(100) NOT NULL,
	pattern_value text,
	CONSTRAINT awf_numbering_pattern_pk PRIMARY KEY (pattern_id)
);

COMMENT ON TABLE awf_numbering_pattern IS '넘버링패턴정보';
COMMENT ON COLUMN awf_numbering_pattern.pattern_id IS '패턴아이디';
COMMENT ON COLUMN awf_numbering_pattern.pattern_name IS '패턴이름';
COMMENT ON COLUMN awf_numbering_pattern.pattern_type IS '패턴타입';
COMMENT ON COLUMN awf_numbering_pattern.pattern_value IS '패턴설정값';

insert into awf_numbering_pattern values ('8a112d61751fs6f325714q053c421411', 'Prefix', 'numbering.pattern.text', '{"value":"Test"}');
insert into awf_numbering_pattern values ('8a112d61751fs6f325714q053c421412', 'Date', 'numbering.pattern.date', '{"code":"pattern.format.yyyyMMdd"}');
insert into awf_numbering_pattern values ('8a112d61751fs6f325714q053c421413', 'Sequence', 'numbering.pattern.sequence', '{"digit":3,"start-with":1,"full-fill":"Y"}');
insert into awf_numbering_pattern values ('7a112d61751fs6f325714q053c421411', '문서 Prefix', 'numbering.pattern.text', '{"value":"CSR"}');
insert into awf_numbering_pattern values ('7a112d61751fs6f325714q053c421412', '문서 날짜', 'numbering.pattern.date', '{"code":"pattern.format.yyyyMMdd"}');
insert into awf_numbering_pattern values ('7a112d61751fs6f325714q053c421413', '문서 시퀀스', 'numbering.pattern.sequence', '{"digit":3,"start-with":1,"full-fill":"Y"}');
insert into awf_numbering_pattern values ('40288ab7772dae0301772dba75b10003', '만족도 Prefix', 'numbering.pattern.text', '{"value":"SAT"}' );
insert into awf_numbering_pattern values ('40288ab777f04ed90177f05dc2110009', '구성관리 Prefix', 'numbering.pattern.text', '{"value":"CFG"}');
insert into awf_numbering_pattern values ('4028b8817880d833017880f26a920002', '릴리즈관리 Prefix', 'numbering.pattern.text', '{"value":"REL"}');
insert into awf_numbering_pattern values ('4028b25d7886e2d801788703c8a00001', '변경관리 PreFix', 'numbering.pattern.text', '{"value":"RFC"}');
/**
 * 역할
 */
DROP TABLE IF EXISTS awf_role cascade;

CREATE TABLE awf_role
(
	role_id varchar(100) NOT NULL,
	role_name varchar(128) NOT NULL UNIQUE,
	role_desc text,
	create_user_key varchar(128),
	create_dt timestamp,
	update_user_key varchar(128),
	update_dt timestamp,
	CONSTRAINT awf_role_pk PRIMARY KEY (role_id),
	CONSTRAINT awf_role_uk UNIQUE (role_name)
);

COMMENT ON TABLE awf_role IS '역할';
COMMENT ON COLUMN awf_role.role_id IS '역할아이디';
COMMENT ON COLUMN awf_role.role_name IS '역할명';
COMMENT ON COLUMN awf_role.role_desc IS '역할설명';
COMMENT ON COLUMN awf_role.create_user_key IS '등록자';
COMMENT ON COLUMN awf_role.create_dt IS '등록일';
COMMENT ON COLUMN awf_role.update_user_key IS '수정자';
COMMENT ON COLUMN awf_role.update_dt IS '수정일';

insert into awf_role values ('document', '문서처리', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_role values ('document.manager', '문서처리 관리자', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_role values ('board.admin.manager', '게시판 관리자', '게시판 관리자', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_role values ('role.all', '역할 관리자', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_role values ('service.manager', '서비스요청 관리자', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_role values ('notice.all', '공지사항 관리자', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_role values ('users.general', '사용자일반', 'sdg', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_role values ('role.view', '역할 사용자', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_role values ('service.user', '서비스요청 사용자', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_role values ('notice.view', '공지사항 사용자', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_role values ('admin', '관리자', '전체관리자', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_role values ('users.manager', '사용자관리자', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_role values ('faq.all', 'FAQ관리자', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_role values ('auth.all', '권한 관리자', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);

/**
 * 역할권한매핑
 */

DROP TABLE IF EXISTS awf_role_auth_map cascade;

CREATE TABLE awf_role_auth_map
(
    role_id varchar(100) NOT NULL,	
	auth_id varchar(100) NOT NULL,
	CONSTRAINT awf_role_auth_map_pk PRIMARY KEY (role_id, auth_id),
	CONSTRAINT awf_role_auth_map_fk1 FOREIGN KEY (role_id) REFERENCES awf_role (role_id),
	CONSTRAINT awf_role_auth_map_fk2 FOREIGN KEY (auth_id) REFERENCES awf_auth (auth_id)
	
);

COMMENT ON TABLE awf_role_auth_map IS '역할권한매핑';
COMMENT ON COLUMN awf_role_auth_map.auth_id IS '권한아이디';
COMMENT ON COLUMN awf_role_auth_map.role_id IS '역할아이디';

insert into awf_role_auth_map values ('admin', 'chart.read');
insert into awf_role_auth_map values ('admin', 'chart.create');
insert into awf_role_auth_map values ('admin', 'chart.update');
insert into awf_role_auth_map values ('admin', 'chart.delete');
insert into awf_role_auth_map values ('admin', 'document.admin.create');
insert into awf_role_auth_map values ('admin', 'code.delete');
insert into awf_role_auth_map values ('admin', 'code.update');
insert into awf_role_auth_map values ('admin', 'code.read');
insert into awf_role_auth_map values ('admin', 'code.create');
insert into awf_role_auth_map values ('admin', 'board.admin.delete');
insert into awf_role_auth_map values ('admin', 'board.admin.update');
insert into awf_role_auth_map values ('admin', 'board.admin.read');
insert into awf_role_auth_map values ('admin', 'board.admin.create');
insert into awf_role_auth_map values ('admin', 'board.read');
insert into awf_role_auth_map values ('admin', 'cmdb.attribute.read');
insert into awf_role_auth_map values ('admin', 'cmdb.attribute.create');
insert into awf_role_auth_map values ('admin', 'cmdb.attribute.update');
insert into awf_role_auth_map values ('admin', 'cmdb.attribute.delete');
insert into awf_role_auth_map values ('admin', 'cmdb.ci.read');
insert into awf_role_auth_map values ('admin', 'cmdb.ci.create');
insert into awf_role_auth_map values ('admin', 'cmdb.ci.update');
insert into awf_role_auth_map values ('admin', 'cmdb.ci.delete');
insert into awf_role_auth_map values ('admin', 'cmdb.class.read');
insert into awf_role_auth_map values ('admin', 'cmdb.class.create');
insert into awf_role_auth_map values ('admin', 'cmdb.class.update');
insert into awf_role_auth_map values ('admin', 'cmdb.class.delete');
insert into awf_role_auth_map values ('admin', 'cmdb.type.read');
insert into awf_role_auth_map values ('admin', 'cmdb.type.create');
insert into awf_role_auth_map values ('admin', 'cmdb.type.update');
insert into awf_role_auth_map values ('admin', 'cmdb.type.delete');
insert into awf_role_auth_map values ('admin', 'custom.code.create');
insert into awf_role_auth_map values ('admin', 'custom.code.read');
insert into awf_role_auth_map values ('admin', 'custom.code.update');
insert into awf_role_auth_map values ('admin', 'custom.code.delete');
insert into awf_role_auth_map values ('admin', 'board.update');
insert into awf_role_auth_map values ('admin', 'board.delete');
insert into awf_role_auth_map values ('admin', 'document.read.admin');
insert into awf_role_auth_map values ('admin', 'image.delete');
insert into awf_role_auth_map values ('admin', 'image.update');
insert into awf_role_auth_map values ('admin', 'image.create');
insert into awf_role_auth_map values ('admin', 'image.read');
insert into awf_role_auth_map values ('admin', 'document.create');
insert into awf_role_auth_map values ('admin', 'document.update');
insert into awf_role_auth_map values ('admin', 'notice.update');
insert into awf_role_auth_map values ('admin', 'notice.delete');
insert into awf_role_auth_map values ('admin', 'faq.read');
insert into awf_role_auth_map values ('admin', 'faq.update');
insert into awf_role_auth_map values ('admin', 'document.read');
insert into awf_role_auth_map values ('admin', 'faq.delete');
insert into awf_role_auth_map values ('admin', 'document.admin.read');
insert into awf_role_auth_map values ('admin', 'user.read');
insert into awf_role_auth_map values ('admin', 'user.create');
insert into awf_role_auth_map values ('admin', 'user.update');
insert into awf_role_auth_map values ('admin', 'user.delete');
insert into awf_role_auth_map values ('admin', 'token.create');
insert into awf_role_auth_map values ('admin', 'notice.create');
insert into awf_role_auth_map values ('admin', 'notice.read');
insert into awf_role_auth_map values ('admin', 'role.read');
insert into awf_role_auth_map values ('admin', 'role.create');
insert into awf_role_auth_map values ('admin', 'role.update');
insert into awf_role_auth_map values ('admin', 'role.delete');
insert into awf_role_auth_map values ('admin', 'token.read');
insert into awf_role_auth_map values ('admin', 'faq.create');
insert into awf_role_auth_map values ('admin', 'file.read');
insert into awf_role_auth_map values ('admin', 'download.create');
insert into awf_role_auth_map values ('admin', 'download.read');
insert into awf_role_auth_map values ('admin', 'board.create');
insert into awf_role_auth_map values ('admin', 'document.delete');
insert into awf_role_auth_map values ('admin', 'download.update');
insert into awf_role_auth_map values ('admin', 'download.delete');
insert into awf_role_auth_map values ('admin', 'auth.update');
insert into awf_role_auth_map values ('admin', 'auth.read');
insert into awf_role_auth_map values ('admin', 'auth.delete');
insert into awf_role_auth_map values ('admin', 'auth.create');
insert into awf_role_auth_map values ('admin', 'process.delete');
insert into awf_role_auth_map values ('admin', 'process.update');
insert into awf_role_auth_map values ('admin', 'process.create');
insert into awf_role_auth_map values ('admin', 'process.read');
insert into awf_role_auth_map values ('admin', 'form.delete');
insert into awf_role_auth_map values ('admin', 'form.update');
insert into awf_role_auth_map values ('admin', 'form.create');
insert into awf_role_auth_map values ('admin', 'form.read');
insert into awf_role_auth_map values ('admin', 'document.admin.delete');
insert into awf_role_auth_map values ('admin', 'document.admin.update');
insert into awf_role_auth_map values ('admin', 'numbering.pattern.create');
insert into awf_role_auth_map values ('admin', 'numbering.pattern.delete');
insert into awf_role_auth_map values ('admin', 'numbering.pattern.read');
insert into awf_role_auth_map values ('admin', 'numbering.pattern.update');
insert into awf_role_auth_map values ('admin', 'numbering.rule.create');
insert into awf_role_auth_map values ('admin', 'numbering.rule.delete');
insert into awf_role_auth_map values ('admin', 'numbering.rule.read');
insert into awf_role_auth_map values ('admin', 'numbering.rule.update');
insert into awf_role_auth_map values ('admin', 'scheduler.create');
insert into awf_role_auth_map values ('admin', 'scheduler.delete');
insert into awf_role_auth_map values ('admin', 'scheduler.execute');
insert into awf_role_auth_map values ('admin', 'scheduler.read');
insert into awf_role_auth_map values ('admin', 'scheduler.update');
insert into awf_role_auth_map values ('auth.all', 'auth.create');
insert into awf_role_auth_map values ('auth.all', 'auth.delete');
insert into awf_role_auth_map values ('auth.all', 'auth.update');
insert into awf_role_auth_map values ('auth.all', 'auth.read');
insert into awf_role_auth_map values ('board.admin.manager', 'board.admin.delete');
insert into awf_role_auth_map values ('board.admin.manager', 'board.admin.create');
insert into awf_role_auth_map values ('board.admin.manager', 'board.admin.read');
insert into awf_role_auth_map values ('board.admin.manager', 'board.admin.update');
insert into awf_role_auth_map values ('document', 'token.read');
insert into awf_role_auth_map values ('document', 'document.read');
insert into awf_role_auth_map values ('document', 'document.delete');
insert into awf_role_auth_map values ('document', 'document.create');
insert into awf_role_auth_map values ('document', 'document.update');
insert into awf_role_auth_map values ('document', 'token.create');
insert into awf_role_auth_map values ('document.manager', 'action.cancel');
insert into awf_role_auth_map values ('document.manager', 'action.terminate');
insert into awf_role_auth_map values ('faq.all', 'form.create');
insert into awf_role_auth_map values ('faq.all', 'role.delete');
insert into awf_role_auth_map values ('faq.all', 'role.update');
insert into awf_role_auth_map values ('faq.all', 'role.read');
insert into awf_role_auth_map values ('faq.all', 'process.read');
insert into awf_role_auth_map values ('faq.all', 'role.create');
insert into awf_role_auth_map values ('faq.all', 'document.update');
insert into awf_role_auth_map values ('faq.all', 'process.delete');
insert into awf_role_auth_map values ('faq.all', 'document.create');
insert into awf_role_auth_map values ('faq.all', 'notice.delete');
insert into awf_role_auth_map values ('notice.all', 'notice.update');
insert into awf_role_auth_map values ('notice.all', 'notice.delete');
insert into awf_role_auth_map values ('notice.all', 'notice.create');
insert into awf_role_auth_map values ('notice.all', 'notice.read');
insert into awf_role_auth_map values ('notice.view', 'notice.delete');
insert into awf_role_auth_map values ('notice.view', 'notice.create');
insert into awf_role_auth_map values ('notice.view', 'notice.read');
insert into awf_role_auth_map values ('notice.view', 'notice.update');
insert into awf_role_auth_map values ('role.all', 'role.read');
insert into awf_role_auth_map values ('role.all', 'role.delete');
insert into awf_role_auth_map values ('role.all', 'role.update');
insert into awf_role_auth_map values ('role.all', 'role.create');
insert into awf_role_auth_map values ('role.view', 'role.read');
insert into awf_role_auth_map values ('role.view', 'role.create');
insert into awf_role_auth_map values ('role.view', 'role.delete');
insert into awf_role_auth_map values ('role.view', 'role.update');
insert into awf_role_auth_map values ('service.manager', 'notice.read');
insert into awf_role_auth_map values ('service.manager', 'faq.create');
insert into awf_role_auth_map values ('service.manager', 'faq.update');
insert into awf_role_auth_map values ('service.manager', 'faq.delete');
insert into awf_role_auth_map values ('service.manager', 'form.read');
insert into awf_role_auth_map values ('service.manager', 'user.read');
insert into awf_role_auth_map values ('service.manager', 'user.create');
insert into awf_role_auth_map values ('service.manager', 'role.create');
insert into awf_role_auth_map values ('service.manager', 'role.update');
insert into awf_role_auth_map values ('service.manager', 'process.delete');
insert into awf_role_auth_map values ('service.manager', 'process.update');
insert into awf_role_auth_map values ('service.manager', 'process.create');
insert into awf_role_auth_map values ('service.manager', 'process.read');
insert into awf_role_auth_map values ('service.manager', 'form.delete');
insert into awf_role_auth_map values ('service.manager', 'form.update');
insert into awf_role_auth_map values ('service.manager', 'form.create');
insert into awf_role_auth_map values ('service.manager', 'user.update');
insert into awf_role_auth_map values ('service.manager', 'user.delete');
insert into awf_role_auth_map values ('service.manager', 'faq.read');
insert into awf_role_auth_map values ('service.manager', 'notice.update');
insert into awf_role_auth_map values ('service.manager', 'notice.delete');
insert into awf_role_auth_map values ('service.manager', 'notice.create');
insert into awf_role_auth_map values ('service.manager', 'role.read');
insert into awf_role_auth_map values ('service.manager', 'role.delete');
insert into awf_role_auth_map values ('service.user', 'form.read');
insert into awf_role_auth_map values ('service.user', 'user.delete');
insert into awf_role_auth_map values ('service.user', 'faq.delete');
insert into awf_role_auth_map values ('service.user', 'user.read');
insert into awf_role_auth_map values ('service.user', 'faq.update');
insert into awf_role_auth_map values ('service.user', 'user.create');
insert into awf_role_auth_map values ('service.user', 'user.update');
insert into awf_role_auth_map values ('service.user', 'faq.create');
insert into awf_role_auth_map values ('service.user', 'faq.read');
insert into awf_role_auth_map values ('service.user', 'process.delete');
insert into awf_role_auth_map values ('service.user', 'process.update');
insert into awf_role_auth_map values ('service.user', 'process.create');
insert into awf_role_auth_map values ('service.user', 'process.read');
insert into awf_role_auth_map values ('service.user', 'form.delete');
insert into awf_role_auth_map values ('service.user', 'form.update');
insert into awf_role_auth_map values ('service.user', 'form.create');
insert into awf_role_auth_map values ('service.user', 'role.delete');
insert into awf_role_auth_map values ('service.user', 'role.update');
insert into awf_role_auth_map values ('service.user', 'notice.update');
insert into awf_role_auth_map values ('service.user', 'notice.delete');
insert into awf_role_auth_map values ('service.user', 'role.create');
insert into awf_role_auth_map values ('service.user', 'role.read');
insert into awf_role_auth_map values ('service.user', 'notice.create');
insert into awf_role_auth_map values ('service.user', 'notice.read');
insert into awf_role_auth_map values ('users.general', 'board.read');
insert into awf_role_auth_map values ('users.general', 'code.read');
insert into awf_role_auth_map values ('users.general', 'notice.read');
insert into awf_role_auth_map values ('users.general', 'faq.read');
insert into awf_role_auth_map values ('users.general', 'token.read');
insert into awf_role_auth_map values ('users.general', 'token.create');
insert into awf_role_auth_map values ('users.general', 'document.read');
insert into awf_role_auth_map values ('users.general', 'document.create');
insert into awf_role_auth_map values ('users.general', 'download.read');
insert into awf_role_auth_map values ('users.manager', 'process.create');
insert into awf_role_auth_map values ('users.manager', 'process.delete');
insert into awf_role_auth_map values ('users.manager', 'role.delete');
insert into awf_role_auth_map values ('users.manager', 'process.read');
insert into awf_role_auth_map values ('users.manager', 'process.update');
insert into awf_role_auth_map values ('users.manager', 'auth.read');
insert into awf_role_auth_map values ('users.manager', 'form.delete');
insert into awf_role_auth_map values ('users.manager', 'form.read');
insert into awf_role_auth_map values ('users.manager', 'form.create');
insert into awf_role_auth_map values ('users.manager', 'form.update');
insert into awf_role_auth_map values ('users.manager', 'notice.update');
insert into awf_role_auth_map values ('users.manager', 'notice.delete');
insert into awf_role_auth_map values ('users.manager', 'faq.read');
insert into awf_role_auth_map values ('users.manager', 'faq.create');
insert into awf_role_auth_map values ('users.manager', 'faq.update');
insert into awf_role_auth_map values ('users.manager', 'faq.delete');
insert into awf_role_auth_map values ('users.manager', 'user.read');
insert into awf_role_auth_map values ('users.manager', 'user.create');
insert into awf_role_auth_map values ('users.manager', 'user.update');
insert into awf_role_auth_map values ('users.manager', 'user.delete');
insert into awf_role_auth_map values ('users.manager', 'auth.delete');
insert into awf_role_auth_map values ('users.manager', 'auth.update');
insert into awf_role_auth_map values ('users.manager', 'auth.create');
insert into awf_role_auth_map values ('users.manager', 'notice.read');
insert into awf_role_auth_map values ('users.manager', 'notice.create');
insert into awf_role_auth_map values ('users.manager', 'role.read');
insert into awf_role_auth_map values ('users.manager', 'role.create');
insert into awf_role_auth_map values ('users.manager', 'role.update');


/**
 * 문서번호패턴맵핑
 */
DROP TABLE IF EXISTS awf_rule_pattern_map cascade;

CREATE TABLE awf_rule_pattern_map
(
	numbering_id varchar(128) NOT NULL,
	pattern_id varchar(128) NOT NULL,
    pattern_order int NOT NULL,
	CONSTRAINT awf_rule_pattern_map_pk PRIMARY KEY (numbering_id, pattern_id, pattern_order),
	CONSTRAINT awf_rule_pattern_map_fk1 FOREIGN KEY (numbering_id) REFERENCES awf_numbering_rule (numbering_id),
	CONSTRAINT awf_rule_pattern_map_fk2 FOREIGN KEY (pattern_id) REFERENCES awf_numbering_pattern (pattern_id)
);

COMMENT ON TABLE awf_rule_pattern_map IS '문서번호 패턴 맵핑 테이블';
COMMENT ON COLUMN awf_rule_pattern_map.numbering_id IS '문서번호 아이디';
COMMENT ON COLUMN awf_rule_pattern_map.pattern_id IS '패턴 아이디';
COMMENT ON COLUMN awf_rule_pattern_map.pattern_order IS '패턴 순서';

insert into awf_rule_pattern_map values ('60211d93621zd1f126241s053c890122', '8a112d61751fs6f325714q053c421411', 0);
insert into awf_rule_pattern_map values ('60211d93621zd1f126241s053c890122', '8a112d61751fs6f325714q053c421412', 1);
insert into awf_rule_pattern_map values ('60211d93621zd1f126241s053c890122', '8a112d61751fs6f325714q053c421413', 2);
insert into awf_rule_pattern_map values ('40125c91714df6c325714e053c890125', '7a112d61751fs6f325714q053c421411', 0);
insert into awf_rule_pattern_map values ('40125c91714df6c325714e053c890125', '7a112d61751fs6f325714q053c421412', 1);
insert into awf_rule_pattern_map values ('40125c91714df6c325714e053c890125', '7a112d61751fs6f325714q053c421413', 2);
insert into awf_rule_pattern_map values ('40288ab7772dae0301772dbca28a0004', '40288ab7772dae0301772dba75b10003', 0);
insert into awf_rule_pattern_map values ('40288ab7772dae0301772dbca28a0004', '8a112d61751fs6f325714q053c421412', 1);
insert into awf_rule_pattern_map values ('40288ab7772dae0301772dbca28a0004', '8a112d61751fs6f325714q053c421413', 2);
insert into awf_rule_pattern_map values ('40288ab777f04ed90177f05e5ad7000a', '40288ab777f04ed90177f05dc2110009', 0);
insert into awf_rule_pattern_map values ('40288ab777f04ed90177f05e5ad7000a', '7a112d61751fs6f325714q053c421412', 1);
insert into awf_rule_pattern_map values ('40288ab777f04ed90177f05e5ad7000a', '8a112d61751fs6f325714q053c421413', 2);
insert into awf_rule_pattern_map values ('4028b8817880d833017880f34ae10003', '4028b8817880d833017880f26a920002', 0);
insert into awf_rule_pattern_map values ('4028b8817880d833017880f34ae10003', '7a112d61751fs6f325714q053c421412', 1);
insert into awf_rule_pattern_map values ('4028b8817880d833017880f34ae10003', '7a112d61751fs6f325714q053c421413', 2);
insert into awf_rule_pattern_map values ('4028b25d7886e2d801788704dd8e0002', '4028b25d7886e2d801788703c8a00001', 0);
insert into awf_rule_pattern_map values ('4028b25d7886e2d801788704dd8e0002', '7a112d61751fs6f325714q053c421412', 1);
insert into awf_rule_pattern_map values ('4028b25d7886e2d801788704dd8e0002', '8a112d61751fs6f325714q053c421413', 2);

/**
 * 스케줄작업정보
 */
DROP TABLE IF EXISTS awf_scheduled_task_mst cascade;

CREATE TABLE awf_scheduled_task_mst
(
	task_id varchar(128) NOT NULL,
	task_name varchar(128),
	task_type varchar(100),
	task_desc varchar(512),
	use_yn boolean default true,
	editable boolean default true,
	execute_class varchar(512),
	execute_query varchar(1024),
	execute_cycle_type varchar(100),
	execute_cycle_period bigint,
	cron_expression varchar(128),
    args varchar(128),
	create_user_key varchar(128),
	create_dt timestamp,
	update_user_key varchar(128),
	update_dt timestamp,
	CONSTRAINT awf_scheduled_task_mst_pk PRIMARY KEY (task_id)
);

COMMENT ON TABLE awf_scheduled_task_mst IS '스케줄작업정보';
COMMENT ON COLUMN awf_scheduled_task_mst.task_id IS '작업아이디';
COMMENT ON COLUMN awf_scheduled_task_mst.task_name IS '작업명';
COMMENT ON COLUMN awf_scheduled_task_mst.task_type IS '작업유형';
COMMENT ON COLUMN awf_scheduled_task_mst.task_desc IS '작업설명';
COMMENT ON COLUMN awf_scheduled_task_mst.use_yn IS '사용여부';
COMMENT ON COLUMN awf_scheduled_task_mst.editable IS '수정가능여부';
COMMENT ON COLUMN awf_scheduled_task_mst.execute_class IS '실행클래스';
COMMENT ON COLUMN awf_scheduled_task_mst.execute_query IS '실행쿼리';
COMMENT ON COLUMN awf_scheduled_task_mst.execute_cycle_type IS '실행주기유형';
COMMENT ON COLUMN awf_scheduled_task_mst.execute_cycle_period IS '실행주기간격';
COMMENT ON COLUMN awf_scheduled_task_mst.cron_expression IS '크론표현식';
COMMENT ON COLUMN awf_scheduled_task_mst.args IS 'arguments';
COMMENT ON COLUMN awf_scheduled_task_mst.create_user_key IS '등록자';
COMMENT ON COLUMN awf_scheduled_task_mst.create_dt IS '등록일';
COMMENT ON COLUMN awf_scheduled_task_mst.update_user_key IS '수정자';
COMMENT ON COLUMN awf_scheduled_task_mst.update_dt IS '수정일';

insert into awf_scheduled_task_mst values ('4021c26175cd53df0175bb545fb30000', '임시 첨부 파일 삭제', 'class', null, 'TRUE', 'FALSE', 'co.brainz.framework.scheduling.task.DeleteTempFile', null, 'cron', 0, '0 0 12 * * *', null, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_scheduled_task_mst values ('4142d16273df21de0175be515fc20000', 'CMDB CI 임시데이터 삭제', 'class', null, 'TRUE', 'FALSE', 'co.brainz.framework.scheduling.task.DeleteTempCIData', null, 'cron', 0, '0 0 12 * * *', 'postgresql, 10.0.10.175, 32784, itsm, itsm, itsm123', '0509e09412534a6e98f04ca79abb6424', now(), null, null);

/**
 * 타임존정보
 */
DROP TABLE IF EXISTS awf_timezone cascade;

CREATE TABLE awf_timezone
(
	timezone_id varchar(128) NOT NULL,
	timezone_value varchar(128),
	CONSTRAINT awf_timezone_pk PRIMARY KEY (timezone_id)
);

COMMENT ON TABLE awf_timezone IS '타임존정보';
COMMENT ON COLUMN awf_timezone.timezone_id IS '타임존아이디';
COMMENT ON COLUMN awf_timezone.timezone_value IS '타임존 값';

insert into awf_timezone values ('(GMT+12:00) Fiji, Kamchatka, Marshall Is.', 'Etc/GMT-12');
insert into awf_timezone values ('(GMT+12:45) Chatham Islands', 'Pacific/Chatham');
insert into awf_timezone values ('(GMT+13:00) Nukualofa', 'Pacific/Tongatapu');
insert into awf_timezone values ('(GMT+14:00) Kiritimati', 'Pacific/Kiritimati');
insert into awf_timezone values ('(GMT+12:00) Auckland, Wellington', 'Pacific/Auckland');
insert into awf_timezone values ('(GMT+11:30) Norfolk Island', 'Pacific/Norfolk');
insert into awf_timezone values ('(GMT-09:30) Marquesas Islands', 'Pacific/Marquesas');
insert into awf_timezone values ('(GMT-09:00) Gambier Islands', 'Pacific/Gambier');
insert into awf_timezone values ('(GMT-11:00) Midway Island, Samoa', 'Pacific/Midway');
insert into awf_timezone values ('(GMT-10:00) Hawaii-Aleutian', 'America/Adak');
insert into awf_timezone values ('(GMT-08:00) Tijuana, Baja California', 'America/Ensenada');
insert into awf_timezone values ('(GMT-08:00) Pacific Time (US & Canada)', 'America/Los_Angeles');
insert into awf_timezone values ('(GMT-07:00) Mountain Time (US & Canada)', 'America/Denver');
insert into awf_timezone values ('(GMT-07:00) Chihuahua, La Paz, Mazatlan', 'America/Chihuahua');
insert into awf_timezone values ('(GMT-07:00) Arizona', 'America/Dawson_Creek');
insert into awf_timezone values ('(GMT-06:00) Saskatchewan, Central America', 'America/Belize');
insert into awf_timezone values ('(GMT-06:00) Guadalajara, Mexico City, Monterrey', 'America/Cancun');
insert into awf_timezone values ('(GMT-06:00) Central Time (US & Canada)', 'America/Chicago');
insert into awf_timezone values ('(GMT-05:00) Eastern Time (US & Canada)', 'America/New_York');
insert into awf_timezone values ('(GMT-05:00) Cuba', 'America/Havana');
insert into awf_timezone values ('(GMT-05:00) Bogota, Lima, Quito, Rio Branco', 'America/Bogota');
insert into awf_timezone values ('(GMT-04:30) Caracas', 'America/Caracas');
insert into awf_timezone values ('(GMT-04:00) Santiago', 'America/Santiago');
insert into awf_timezone values ('(GMT-04:00) La Paz', 'America/La_Paz');
insert into awf_timezone values ('(GMT-04:00) Brazil', 'America/Campo_Grande');
insert into awf_timezone values ('(GMT-04:00) Atlantic Time (Goose Bay)', 'America/Goose_Bay');
insert into awf_timezone values ('(GMT-04:00) Atlantic Time (Canada)', 'America/Glace_Bay');
insert into awf_timezone values ('(GMT-03:30) Newfoundland', 'America/St_Johns');
insert into awf_timezone values ('(GMT-03:00) UTC-3', 'America/Araguaina');
insert into awf_timezone values ('(GMT-03:00) Montevideo', 'America/Montevideo');
insert into awf_timezone values ('(GMT-03:00) Miquelon, St. Pierre', 'America/Miquelon');
insert into awf_timezone values ('(GMT-03:00) Greenland', 'America/Godthab');
insert into awf_timezone values ('(GMT-03:00) Buenos Aires', 'America/Argentina/Buenos_Aires');
insert into awf_timezone values ('(GMT-03:00) Brasilia', 'America/Sao_Paulo');
insert into awf_timezone values ('(GMT-02:00) Mid-Atlantic', 'America/Noronha');
insert into awf_timezone values ('(GMT+03:30) Tehran', 'Asia/Tehran');
insert into awf_timezone values ('(GMT+04:00) Abu Dhabi, Muscat', 'Asia/Dubai');
insert into awf_timezone values ('(GMT+04:00) Yerevan', 'Asia/Yerevan');
insert into awf_timezone values ('(GMT+04:30) Kabul', 'Asia/Kabul');
insert into awf_timezone values ('(GMT+05:00) Ekaterinburg', 'Asia/Yekaterinburg');
insert into awf_timezone values ('(GMT+05:00) Tashkent', 'Asia/Tashkent');
insert into awf_timezone values ('(GMT+05:30) Chennai, Kolkata, Mumbai, New Delhi', 'Asia/Kolkata');
insert into awf_timezone values ('(GMT+05:45) Kathmandu', 'Asia/Katmandu');
insert into awf_timezone values ('(GMT+06:00) Astana, Dhaka', 'Asia/Dhaka');
insert into awf_timezone values ('(GMT+06:00) Novosibirsk', 'Asia/Novosibirsk');
insert into awf_timezone values ('(GMT+06:30) Yangon (Rangoon)', 'Asia/Rangoon');
insert into awf_timezone values ('(GMT+07:00) Bangkok, Hanoi, Jakarta', 'Asia/Bangkok');
insert into awf_timezone values ('(GMT+07:00) Krasnoyarsk', 'Asia/Krasnoyarsk');
insert into awf_timezone values ('(GMT+08:00) Beijing, Chongqing, Hong Kong, Urumqi', 'Asia/Hong_Kong');
insert into awf_timezone values ('(GMT+08:00) Irkutsk, Ulaan Bataar', 'Asia/Irkutsk');
insert into awf_timezone values ('(GMT+09:00) Osaka, Sapporo, Tokyo', 'Asia/Tokyo');
insert into awf_timezone values ('(GMT+09:00) Seoul', 'Asia/Seoul');
insert into awf_timezone values ('(GMT+09:00) Yakutsk', 'Asia/Yakutsk');
insert into awf_timezone values ('(GMT+10:00) Vladivostok', 'Asia/Vladivostok');
insert into awf_timezone values ('(GMT+02:00) Syria', 'Asia/Damascus');
insert into awf_timezone values ('(GMT+02:00) Jerusalem', 'Asia/Jerusalem');
insert into awf_timezone values ('(GMT+02:00) Gaza', 'Asia/Gaza');
insert into awf_timezone values ('(GMT+02:00) Beirut', 'Asia/Beirut');
insert into awf_timezone values ('(GMT+11:00) Magadan', 'Asia/Magadan');
insert into awf_timezone values ('(GMT+12:00) Anadyr, Kamchatka', 'Asia/Anadyr');
insert into awf_timezone values ('(GMT+08:00) Perth', 'Australia/Perth');
insert into awf_timezone values ('(GMT+08:45) Eucla', 'Australia/Eucla');
insert into awf_timezone values ('(GMT+09:30) Adelaide', 'Australia/Adelaide');
insert into awf_timezone values ('(GMT+09:30) Darwin', 'Australia/Darwin');
insert into awf_timezone values ('(GMT+10:00) Brisbane', 'Australia/Brisbane');
insert into awf_timezone values ('(GMT+10:00) Hobart', 'Australia/Hobart');
insert into awf_timezone values ('(GMT+10:30) Lord Howe Island', 'Australia/Lord_Howe');
insert into awf_timezone values ('(GMT-04:00) Faukland Islands', 'Atlantic/Stanley');
insert into awf_timezone values ('(GMT-01:00) Cape Verde Is', 'Atlantic/Cape_Verde');
insert into awf_timezone values ('(GMT-01:00) Azores', 'Atlantic/Azores');
insert into awf_timezone values ('(GMT+01:00) West Central Africa', 'Africa/Algiers');
insert into awf_timezone values ('(GMT) Monrovia, Reykjavik', 'Africa/Abidjan');
insert into awf_timezone values ('(GMT+02:00) Cairo', 'Africa/Cairo');
insert into awf_timezone values ('(GMT+02:00) Harare, Pretoria', 'Africa/Blantyre');
insert into awf_timezone values ('(GMT+03:00) Nairobi', 'Africa/Addis_Ababa');
insert into awf_timezone values ('(GMT-06:00) Easter Island', 'Chile/EasterIsland');
insert into awf_timezone values ('(GMT) Belfast', 'Europe/Belfast');
insert into awf_timezone values ('(GMT) Dublin', 'Europe/Dublin');
insert into awf_timezone values ('(GMT) Lisbon', 'Europe/Lisbon');
insert into awf_timezone values ('(GMT) London', 'Europe/London');
insert into awf_timezone values ('(GMT+01:00) Amsterdam, Berlin, Bern, Rome, Stockholm, Vienna', 'Europe/Amsterdam');
insert into awf_timezone values ('(GMT+01:00) Belgrade, Bratislava, Budapest, Ljubljana, Prague', 'Europe/Belgrade');
insert into awf_timezone values ('(GMT+01:00) Brussels, Copenhagen, Madrid, Paris', 'Europe/Brussels');
insert into awf_timezone values ('(GMT+02:00) Minsk', 'Europe/Minsk');
insert into awf_timezone values ('(GMT+03:00) Moscow, St. Petersburg, Volgograd', 'Europe/Moscow');
insert into awf_timezone values ('(GMT-08:00) Pitcairn Islands', 'Etc/GMT+8');
insert into awf_timezone values ('(GMT-10:00) Hawaii', 'Etc/GMT+10');
insert into awf_timezone values ('(GMT+11:00) Solomon Is., New Caledonia', 'Etc/GMT-11');
insert into awf_timezone values ('(GMT-09:00) Alaska', 'America/Anchorage');
insert into awf_timezone values ('(GMT+02:00) Windhoek', 'Africa/Windhoek');

/**
 * URL별메소드명
 */
DROP TABLE IF EXISTS awf_url cascade;

CREATE TABLE awf_url
(
    url varchar(512) NOT NULL,
    method varchar(16) NOT NULL,
    url_desc varchar(256),
    is_required_auth boolean DEFAULT 'true',
    CONSTRAINT awf_url_pk PRIMARY KEY (url, method)
);

COMMENT ON TABLE awf_url IS 'URL별메소드명';
COMMENT ON COLUMN awf_url.url IS '요청url';
COMMENT ON COLUMN awf_url.method IS 'method';
COMMENT ON COLUMN awf_url.url_desc IS '설명';
COMMENT ON COLUMN awf_url.is_required_auth IS '권한 필수여부';

insert into awf_url values ('/auths/edit', 'get', '역할  설정 뷰를 호출', 'TRUE');
insert into awf_url values ('/auths', 'get', '권한 관리 목록', 'TRUE');
insert into awf_url values ('/boards', 'get', '게시판 관리 리스트 호출화면', 'TRUE');
insert into awf_url values ('/boards/new', 'get', '게시판 관리 신규 등록', 'TRUE');
insert into awf_url values ('/boards/search', 'get', '게시판 관리 리스트 조회 화면', 'TRUE');
insert into awf_url values ('/boards/{id}/edit', 'get', '게시판 관리 상세 편집', 'TRUE');
insert into awf_url values ('/boards/{id}/view', 'get', '게시판 관리 상세 조회 화면', 'TRUE');
insert into awf_url values ('/boards/articles', 'get', '게시판 리스트 호출 화면', 'TRUE');
insert into awf_url values ('/boards/articles/search', 'get', '게시판 리스트 화면', 'TRUE');
insert into awf_url values ('/boards/articles/search/param', 'get', '게시판 리스트 화면', 'TRUE');
insert into awf_url values ('/boards/articles/{id}/comments', 'get', '게시판 댓글 조회', 'TRUE');
insert into awf_url values ('/boards/articles/{id}/edit', 'get', '게시판 편집', 'TRUE');
insert into awf_url values ('/boards/articles/{id}/new', 'get', '게시판 신규 등록', 'TRUE');
insert into awf_url values ('/boards/articles/{id}/reply/edit', 'get', '게시판 답글 편집', 'TRUE');
insert into awf_url values ('/boards/articles/{id}/view', 'get', '게시판 상세 조회 화면', 'TRUE');
insert into awf_url values ('/certification/certifiedmail', 'get', '메일 발송', 'FALSE');
insert into awf_url values ('/certification/fileupload', 'post', '회원가입 아바타 이미지 업로드', 'FALSE');
insert into awf_url values ('/certification', 'post', '회원 가입 요청(인증 메일 발송 포함)', 'FALSE');
insert into awf_url values ('/certification/signup', 'get', '회원 가입 화면 호출', 'FALSE');
insert into awf_url values ('/certification/status', 'get', '메일 인증 상태/재발송 요청 화면', 'FALSE');
insert into awf_url values ('/certification/valid', 'get', '메일 인증', 'FALSE');
insert into awf_url values ('/charts', 'get', '통계 차트 목록', 'TRUE');
insert into awf_url values ('/charts/search', 'get', '통계 차트 목록 조회 화면', 'TRUE');
insert into awf_url values ('/charts/new', 'get', '통계 차트 등록 화면', 'TRUE');
insert into awf_url values ('/charts/{id}/edit', 'get', '통계 차트 수정 화면', 'TRUE');
insert into awf_url values ('/charts/{id}/view', 'get', '통계 차트 조회 화면', 'TRUE');
insert into awf_url values ('/cmdb/attributes', 'get', 'CMDB Attribute 관리 목록', 'TRUE');
insert into awf_url values ('/cmdb/attributes/new', 'get', 'CMDB Attribute 등록 화면', 'TRUE');
insert into awf_url values ('/cmdb/attributes/search', 'get', 'CMDB Attribute 관리 조회 화면', 'TRUE');
insert into awf_url values ('/cmdb/attributes/{id}/edit', 'get', 'CMDB Attribute 수정 화면', 'TRUE');
insert into awf_url values ('/cmdb/attributes/{id}/view', 'get', 'CMDB Attribute 보기 화면', 'TRUE');
insert into awf_url values ('/cmdb/class/edit', 'get', 'CMDB Class 편집 화면', 'TRUE');
insert into awf_url values ('/cmdb/class/view-pop/attributes', 'get', 'CMDB Class Attribute 모달 리스트 화면', 'TRUE');
insert into awf_url values ('/cmdb/types', 'get', 'CMDB Type 관리', 'TRUE');
insert into awf_url values ('/cmdb/types/edit', 'get', 'CMDB Type 관리 화면', 'TRUE');
insert into awf_url values ('/cmdb/cis', 'get', 'CMDB CI 조회 목록', 'TRUE');
insert into awf_url values ('/cmdb/cis/search', 'get', 'CMDB CI 조회 목록 화면', 'TRUE');
insert into awf_url values ('/cmdb/cis/component/new', 'get', 'CMDB CI 등록 화면', 'FALSE');
insert into awf_url values ('/cmdb/cis/component/edit', 'post', 'CMDB CI 수정 화면', 'FALSE');
insert into awf_url values ('/cmdb/cis/component/view', 'get', 'CMDB CI 보기 화면', 'FALSE');
insert into awf_url values ('/cmdb/cis/{id}/view', 'get', 'CMDB CI 보기 화면', 'TRUE');
insert into awf_url values ('/cmdb/cis/component/list', 'get', 'CMDB CI 리스트 조회 팝업 화면', 'FALSE');
insert into awf_url values ('/codes/edit', 'get', '코드 편집 화면', 'TRUE');
insert into awf_url values ('/custom-codes', 'get', '사용자 정의 코드 리스트 화면', 'TRUE');
insert into awf_url values ('/custom-codes/new', 'get', '사용자 정의 코드 신규 등록 화면', 'TRUE');
insert into awf_url values ('/custom-codes/search', 'get', '사용자 정의 코드 리스트 호출 화면', 'TRUE');
insert into awf_url values ('/custom-codes/{id}/edit', 'get', '사용자 정의 코드 수정 화면', 'TRUE');
insert into awf_url values ('/custom-codes/{id}/search', 'get', '커스텀 코드 데이터 조회 화면', 'TRUE');
insert into awf_url values ('/custom-codes/{id}/view', 'get', '사용자 정의 코드 상세 정보 화면', 'TRUE');
insert into awf_url values ('/dashboard/view', 'get', '대시보드 상세 정보 화면', 'TRUE');
insert into awf_url values ('/workflows', 'get', '업무흐름 리스트 화면', 'TRUE');
insert into awf_url values ('/workflows/new', 'get', '신청서 생성 화면', 'TRUE');
insert into awf_url values ('/workflows/search', 'get', '업무흐름 데이터 + 목록화면', 'TRUE');
insert into awf_url values ('/workflows/{id}/edit', 'get', '신청서 수정 화면', 'TRUE');
insert into awf_url values ('/documents', 'get', '신청서 리스트 화면', 'FALSE');
insert into awf_url values ('/documents/search', 'get', '신청서 리스트 호출 화면', 'FALSE');
insert into awf_url values ('/documents/{id}/print', 'get', '신청서 프린트 화면', 'TRUE');
insert into awf_url values ('/downloads', 'get', '자료실 리스트 화면', 'TRUE');
insert into awf_url values ('/downloads/new', 'get', '자료실 신규 등록', 'TRUE');
insert into awf_url values ('/downloads/search', 'get', '자료실 리스트 호출 화면', 'TRUE');
insert into awf_url values ('/downloads/{id}/edit', 'get', '자료실 편집', 'TRUE');
insert into awf_url values ('/downloads/{id}/view', 'get', '자료실 상세 조회 화면', 'TRUE');
insert into awf_url values ('/faqs', 'get', 'FAQ 목록 조회', 'TRUE');
insert into awf_url values ('/faqs/new', 'get', 'FAQ 등록', 'TRUE');
insert into awf_url values ('/faqs/search', 'get', 'FAQ 검색 화면 호출', 'TRUE');
insert into awf_url values ('/faqs/{id}/edit', 'get', 'FAQ 수정', 'TRUE');
insert into awf_url values ('/faqs/{id}/view', 'get', 'FAQ 보기', 'TRUE');
insert into awf_url values ('/form/{id}/edit', 'get', '폼 디자이너 편집화면', 'TRUE');
insert into awf_url values ('/form/{id}/view', 'get', '폼 디자이너 상세화면', 'TRUE');
insert into awf_url values ('/form/{id}/preview', 'get', '폼 디자이너 미리보기 화면', 'TRUE');
insert into awf_url values ('/forms', 'get', '폼 리스트 화면', 'TRUE');
insert into awf_url values ('/forms/search', 'get', '폼 리스트 검색 호출 화면', 'TRUE');
insert into awf_url values ('/images', 'get', '이미지 관리 화면', 'TRUE');
insert into awf_url values ('/notices', 'get', '공지사항 목록', 'TRUE');
insert into awf_url values ('/notices/new', 'get', '공지사항 신규 등록 화면', 'TRUE');
insert into awf_url values ('/notices/search', 'get', '공지사항 검색 화면 호출 처리', 'TRUE');
insert into awf_url values ('/notices/{id}/edit', 'get', '공지사항 편집 화면', 'TRUE');
insert into awf_url values ('/notices/{id}/view', 'get', '공지사항 상세 화면', 'TRUE');
insert into awf_url values ('/notices/{id}/view-pop', 'get', '공지사항 팝업 화면', 'FALSE');
insert into awf_url values ('/notifications', 'get', '알림 리스트 화면', 'FALSE');
insert into awf_url values ('/numberingPatterns', 'get', '패턴 관리 목록 뷰', 'TRUE');
insert into awf_url values ('/numberingPatterns/edit', 'get', '패턴 편집 화면', 'TRUE');
insert into awf_url values ('/numberingRules', 'get', '문서번호 관리 목록 뷰', 'TRUE');
insert into awf_url values ('/numberingRules/edit', 'get', '문서번호 편집 화면', 'TRUE');
insert into awf_url values ('/oauth/{service}/callback', 'get', 'OAuth 로그인 응답 콜백', 'TRUE');
insert into awf_url values ('/oauth/{service}/login', 'get', 'OAuth 로그인 화면 호출', 'TRUE');
insert into awf_url values ('/portals', 'get', '포탈 조회', 'FALSE');
insert into awf_url values ('/portals/browserguide', 'get', '포탈 브라우저 안내', 'FALSE');
insert into awf_url values ('/portals/downloads', 'get', '포달 자료실 리스트', 'FALSE');
insert into awf_url values ('/portals/downloads/{downloadId}/view', 'get', '포탈 자료실 상세조회', 'FALSE');
insert into awf_url values ('/portals/downloads/search', 'get', '포탈 자료실 조회', 'FALSE');
insert into awf_url values ('/portals/faqs', 'get', '포탈 FAQ 상세조회', 'FALSE');
insert into awf_url values ('/portals/faqs/{faqId}/view', 'get', '포탈 FAQ 리스트', 'FALSE');
insert into awf_url values ('/portals/main', 'get', '포탈', 'FALSE');
insert into awf_url values ('/portals/notices', 'get', '포탈 공지사항 리스트', 'FALSE');
insert into awf_url values ('/portals/notices/{noticeId}/view', 'get', '포탈 공지사항 상세 조회', 'FALSE');
insert into awf_url values ('/portals/notices/search', 'get', '포탈 공지사항 조회', 'FALSE');
insert into awf_url values ('/processes', 'get', '프로세스 목록', 'TRUE');
insert into awf_url values ('/processes/search', 'get', '프로세스 리스트 검색 호출 화면', 'TRUE');
insert into awf_url values ('/process/{id}/edit', 'get', '프로세스 디자이너 편집 화면' ,'TRUE');
insert into awf_url values ('/process/{id}/view', 'get', '프로세스 디자이너 보기 화면' ,'TRUE');
insert into awf_url values ('/process/{id}/status', 'get', '프로세스 상태', 'TRUE');
insert into awf_url values ('/rest/auths', 'get', '권한 전체 목록 조회', 'TRUE');
insert into awf_url values ('/rest/auths', 'post', '권한 등록', 'TRUE');
insert into awf_url values ('/rest/auths/{id}', 'get', '권한 상세 정보 조회', 'TRUE');
insert into awf_url values ('/rest/auths/{id}', 'put', '권한 수정', 'TRUE');
insert into awf_url values ('/rest/auths/{id}', 'delete', '권한 삭제', 'TRUE');
insert into awf_url values ('/rest/boards', 'post', '게시판 관리 등록', 'TRUE');
insert into awf_url values ('/rest/boards', 'put', '게시판 관리 변경', 'TRUE');
insert into awf_url values ('/rest/boards/{id}/view', 'get', '게시판 관리 상세정보', 'TRUE');
insert into awf_url values ('/rest/boards/{id}', 'delete', '게시판 관리 삭제', 'TRUE');
insert into awf_url values ('/rest/boards/articles', 'post', '게시판 등록', 'TRUE');
insert into awf_url values ('/rest/boards/articles', 'put', '게시판 변경', 'TRUE');
insert into awf_url values ('/rest/boards/articles/comments', 'put', '게시판 댓글 수정', 'TRUE');
insert into awf_url values ('/rest/boards/articles/comments', 'post', '게시판 댓글 등록', 'TRUE');
insert into awf_url values ('/rest/boards/articles/comments/{id}', 'delete', '게시판 댓글 삭제', 'TRUE');
insert into awf_url values ('/rest/boards/articles/reply', 'post', '게시판 답글 등록', 'TRUE');
insert into awf_url values ('/rest/boards/articles/{id}', 'delete', '게시판 삭제', 'TRUE');
insert into awf_url values ('/rest/charts', 'post', '통계 차트 등록', 'TRUE');
insert into awf_url values ('/rest/charts/{id}', 'put', '통계 차트 수정', 'TRUE');
insert into awf_url values ('/rest/charts/{id}', 'delete', '통계 차트 삭제', 'TRUE');
insert into awf_url values ('/rest/charts/{id}/preview', 'post', '통계 차트 미리보기', 'TRUE');
insert into awf_url values ('/rest/cmdb/attributes', 'get', 'CMDB Attribute 관리 목록 조회', 'TRUE');
insert into awf_url values ('/rest/cmdb/attributes', 'post', 'CMDB Attribute 등록', 'TRUE');
insert into awf_url values ('/rest/cmdb/attributes/{id}', 'put', 'CMDB Attribute 수정', 'TRUE');
insert into awf_url values ('/rest/cmdb/attributes/{id}', 'delete', 'CMDB Attribute 삭제', 'TRUE');
insert into awf_url values ('/rest/cmdb/cis/{id}/data', 'post', 'CI 컴포넌트 - CI 세부 정보 등록', 'FALSE');
insert into awf_url values ('/rest/cmdb/cis/data', 'delete', 'CI 컴포넌트 - CI 세부 정보 삭제', 'FALSE');
insert into awf_url values ('/rest/cmdb/classes', 'get', 'CMDB Class 리스트', 'TRUE');
insert into awf_url values ('/rest/cmdb/classes', 'post', 'CMDB Class 등록', 'TRUE');
insert into awf_url values ('/rest/cmdb/classes/{id}', 'get', 'CMDB Class 단일 조회', 'TRUE');
insert into awf_url values ('/rest/cmdb/classes/{id}', 'put', 'CMDB Class 수정', 'TRUE');
insert into awf_url values ('/rest/cmdb/classes/{id}', 'delete', 'CMDB Class 삭제', 'TRUE');
insert into awf_url values ('/rest/cmdb/classes/{id}/attributes', 'get', 'CI 컴포넌트 - CI CLASS에 따른 세부 속성 조회', 'FALSE');
insert into awf_url values ('/rest/cmdb/types', 'get', 'CMDB Type 조회', 'TRUE');
insert into awf_url values ('/rest/cmdb/types', 'post', 'CMDB Type 등록', 'TRUE');
insert into awf_url values ('/rest/cmdb/types/{id}', 'get', 'CMDB Type 단일 조회', 'TRUE');
insert into awf_url values ('/rest/cmdb/types/{id}', 'put', 'CMDB Type 수정', 'TRUE');
insert into awf_url values ('/rest/cmdb/types/{id}', 'delete', 'CMDB Type 삭제', 'TRUE');
insert into awf_url values ('/rest/codes', 'post', '코드 등록', 'TRUE');
insert into awf_url values ('/rest/codes', 'get', '코드 전체 조회', 'TRUE');
insert into awf_url values ('/rest/codes/{id}', 'put', '코드 수정', 'TRUE');
insert into awf_url values ('/rest/codes/{id}', 'get', '코드 상세 조회', 'TRUE');
insert into awf_url values ('/rest/codes/{id}', 'delete', '코드 삭제', 'TRUE');
insert into awf_url values ('/rest/comments', 'post', 'Comment 저장', 'FALSE');
insert into awf_url values ('/rest/comments/{id}', 'delete', 'Comment 삭제', 'FALSE');
insert into awf_url values ('/rest/custom-codes', 'get', '커스텀 코드 조회', 'FALSE');
insert into awf_url values ('/rest/custom-codes', 'put', '커스텀 코드 수정', 'TRUE');
insert into awf_url values ('/rest/custom-codes', 'post', '커스텀 코드 등록', 'TRUE');
insert into awf_url values ('/rest/custom-codes/{id}', 'delete', '커스텀 코드 삭제', 'TRUE');
insert into awf_url values ('/rest/custom-codes/{id}', 'get', '커스텀코드 목록 조회', 'TRUE');
insert into awf_url values ('/rest/documents', 'get', '신청서 문서 목록 조회', 'TRUE');
insert into awf_url values ('/rest/workflows', 'post', '신청서 작성', 'TRUE');
insert into awf_url values ('/rest/workflows/{id}', 'delete', '신청서 삭제', 'TRUE');
insert into awf_url values ('/rest/workflows/{id}', 'get', '신청서 데이터 조회', 'TRUE');
insert into awf_url values ('/rest/workflows/{id}', 'put', '신청서 수정', 'TRUE');
insert into awf_url values ('/rest/workflows/{id}/display', 'put', '신청서 디스플레이 데이터 저장', 'TRUE');
insert into awf_url values ('/rest/workflows/{id}/display', 'get', '신청서 디스플레이 데이터 조회', 'TRUE');
insert into awf_url values ('/rest/documents/{id}/data', 'get', '신청서의 문서 데이터 조회', 'TRUE');
insert into awf_url values ('/rest/downloads', 'post', '자료실 등록', 'TRUE');
insert into awf_url values ('/rest/downloads', 'put', '자료실 변경', 'TRUE');
insert into awf_url values ('/rest/downloads/{id}', 'delete', '자료실 삭제', 'TRUE');
insert into awf_url values ('/rest/faqs', 'post', 'FAQ 등록 처리', 'TRUE');
insert into awf_url values ('/rest/faqs/{id}', 'put', 'FAQ 수정 처리', 'TRUE');
insert into awf_url values ('/rest/faqs/{id}', 'get', 'FAQ 상세 조회', 'TRUE');
insert into awf_url values ('/rest/faqs/{id}', 'delete', 'FAQ 삭제 처리', 'TRUE');
insert into awf_url values ('/rest/filenameextensions', 'get', '파일 확장자목록', 'FALSE');
insert into awf_url values ('/rest/folders', 'post', '폴더 등록', 'FALSE');
insert into awf_url values ('/rest/folders/{id}', 'delete', '폴더 삭제', 'FALSE');
insert into awf_url values ('/rest/form/{id}', 'delete', '폼 디자이너 삭제', 'TRUE');
insert into awf_url values ('/rest/form/{id}/data', 'get', '폼 디자이너 세부 정보 불러오기', 'TRUE');
insert into awf_url values ('/rest/form/{id}/data', 'put', '폼 디자이너 세부 정보 저장', 'TRUE');
insert into awf_url values ('/rest/forms', 'post', '폼 디자이너 기본 정보 저장 / 다른 이름 저장 처리', 'TRUE');
insert into awf_url values ('/rest/forms/{id}', 'put', '폼 디자이너 기본 정보 수정', 'TRUE');
insert into awf_url values ('/rest/forms/{id}/data', 'get', '문서양식 데이터조회', 'TRUE');
insert into awf_url values ('/rest/images', 'post', '이미지 업로드', 'TRUE');
insert into awf_url values ('/rest/images', 'put', '이미지명 수정', 'TRUE');
insert into awf_url values ('/rest/images/{id}', 'get', '이미지 조회', 'FALSE');
insert into awf_url values ('/rest/images/{id}', 'delete', '이미지 삭제', 'TRUE');
insert into awf_url values ('/rest/images', 'get', '이미지 전체 조회', 'FALSE');
insert into awf_url values ('/rest/notices', 'post', '공지사항 등록', 'TRUE');
insert into awf_url values ('/rest/notices/{id}', 'delete', '공지사항 삭제', 'TRUE');
insert into awf_url values ('/rest/notices/{id}', 'put', '공지사항 수정', 'TRUE');
insert into awf_url values ('/rest/notifications/{id}', 'delete', '알림 리스트 삭제', 'FALSE');
insert into awf_url values ('/rest/notifications/{id}/confirm', 'put', '알림 리스트 확인 여부 수정', 'FALSE');
insert into awf_url values ('/rest/notifications/{id}/display', 'put', '알림 리스트 표시 여부 수정', 'FALSE');
insert into awf_url values ('/rest/numberingPatterns', 'get', '패턴 리스트', 'TRUE');
insert into awf_url values ('/rest/numberingPatterns', 'post', '패턴 등록', 'TRUE');
insert into awf_url values ('/rest/numberingPatterns/{id}', 'get', '패턴 세부 조회', 'TRUE');
insert into awf_url values ('/rest/numberingPatterns/{id}', 'put', '패턴 정보 변경', 'TRUE');
insert into awf_url values ('/rest/numberingPatterns/{id}', 'delete', '패턴 삭제', 'TRUE');
insert into awf_url values ('/rest/numberingRules', 'get', '문서번호 리스트', 'TRUE');
insert into awf_url values ('/rest/numberingRules', 'post', '문서번호 등록', 'TRUE');
insert into awf_url values ('/rest/numberingRules/{id}', 'get', '문서번호 세부 조회', 'TRUE');
insert into awf_url values ('/rest/numberingRules/{id}', 'put', '문서번호 정보 변경', 'TRUE');
insert into awf_url values ('/rest/numberingRules/{id}', 'delete', '문서번호 삭제', 'TRUE');
insert into awf_url values ('/rest/portals', 'get', '포탈 조회 (페이징)', 'FALSE');
insert into awf_url values ('/rest/portals/filedownload', 'get', '포탈 상세 파일 리스트 조회', 'FALSE');
insert into awf_url values ('/rest/portals/filenameextensions', 'get', '포탈 첨부파일 확장자 조회', 'FALSE');
insert into awf_url values ('/rest/portals/files', 'get', '포탈 상세 파일 리스트 조회', 'FALSE');
insert into awf_url values ('/rest/portals/top', 'get', '포탈 첫화면 Top 조회', 'FALSE');
insert into awf_url values ('/rest/processes', 'post', '프로세스 디자이너 기본 정보 저장 / 다른이름 저장 처리', 'TRUE');
insert into awf_url values ('/rest/processes/{id}', 'put', '프로세스 수정', 'TRUE');
insert into awf_url values ('/rest/processes/all', 'get', '발행 상태인 프로세스 목록 조회', 'TRUE');
insert into awf_url values ('/rest/process/{id}', 'delete', '프로세스 디자이너 삭제', 'TRUE');
insert into awf_url values ('/rest/process/{id}/data', 'put', '프로세스 디자이너 수정', 'TRUE');
insert into awf_url values ('/rest/process/{id}/data', 'get', '프로세스 디자이너 불러오기', 'TRUE');
insert into awf_url values ('/rest/process/{id}/simulation', 'put', '프로세스 시뮬레이션', 'TRUE');
insert into awf_url values ('/rest/processes/{id}/data', 'get', '프로세스 기본데이터 조회', 'TRUE');
insert into awf_url values ('/rest/roles', 'post', '역할 등록', 'TRUE');
insert into awf_url values ('/rest/roles', 'get', '역할 전체 목록 조회', 'TRUE');
insert into awf_url values ('/rest/roles/{id}', 'get', '역할 상제 정보 조회', 'TRUE');
insert into awf_url values ('/rest/roles/{id}', 'put', '역할 수정', 'TRUE');
insert into awf_url values ('/rest/roles/{id}', 'delete', '역할 삭제', 'TRUE');
insert into awf_url values ('/rest/schedulers', 'post', '스케줄러 등록', 'TRUE');
insert into awf_url values ('/rest/schedulers/{id}', 'delete', '스케줄러 삭제', 'TRUE');
insert into awf_url values ('/rest/schedulers/{id}', 'put', '스케줄러 수정', 'TRUE');
insert into awf_url values ('/rest/schedulers/{id}/execute', 'post', '스케줄러 실행', 'TRUE');
insert into awf_url values ('/rest/tags', 'post', 'Tag 저장', 'FALSE');
insert into awf_url values ('/rest/tags/{id}', 'delete', 'Tag 삭제', 'FALSE');
insert into awf_url values ('/rest/tokens/data', 'post', 'token 신규 등록', 'TRUE');
insert into awf_url values ('/rest/tokens/{id}/data', 'get', '처리할 문서 상세 데이터', 'TRUE');
insert into awf_url values ('/rest/tokens/{id}/data', 'put', 'token 수정', 'TRUE');
insert into awf_url values ('/rest/users', 'post', '사용자 등록', 'TRUE');
insert into awf_url values ('/rest/users/all', 'get', '전체 사용자 목록 조회', 'TRUE');
insert into awf_url values ('/rest/users/{userkey}/all', 'put', '사용자가 자신의 정보를 업데이트', 'TRUE');
insert into awf_url values ('/rest/users/{userkey}/info', 'put', '사용자가 다른 사용자의 정보를 업데이트', 'FALSE');
insert into awf_url values ('/rest/users/{userkey}/resetpassword', 'put', '사용자 비밀번호 초기화', 'TRUE');
insert into awf_url values ('/roles/edit', 'get', '역할 설정 뷰 호출', 'TRUE');
insert into awf_url values ('/roles', 'get', '역할 관리 목록 뷰 호출', 'TRUE');
insert into awf_url values ('/schedulers', 'get', '스케줄러 리스트 화면', 'TRUE');
insert into awf_url values ('/schedulers/new', 'get', '스케줄러 신규 등록 화면', 'TRUE');
insert into awf_url values ('/schedulers/search', 'get', '스케줄러 리스트 화면 호출', 'TRUE');
insert into awf_url values ('/schedulers/{id}/edit', 'get', '스케줄러 상세 수정 화면', 'TRUE');
insert into awf_url values ('/schedulers/{id}/history', 'get', '스케줄러 이력 리스트 모달 화면', 'TRUE');
insert into awf_url values ('/schedulers/{id}/view', 'get', '스케줄러 상세 조회 화면', 'TRUE');
insert into awf_url values ('/tokens', 'get', '처리할 문서 리스트 조회', 'FALSE');
insert into awf_url values ('/tokens/search', 'get', '로그인시 인증여부 체크 및 처리할 문서 페이지 이동', 'FALSE');
insert into awf_url values ('/tokens/view-pop/documents', 'get', '관련문서 리스트', 'TRUE');
insert into awf_url values ('/tokens/{id}/edit', 'get', 'NULL', 'TRUE');
insert into awf_url values ('/tokens/{id}/edit-tab', 'get', '문서 오른쪽 탭 정보', 'TRUE');
insert into awf_url values ('/tokens/{id}/view', 'get', 'NULL', 'TRUE');
insert into awf_url values ('/tokens/{id}/view-tab', 'get', '문서 오른쪽 탭 정보', 'TRUE');
insert into awf_url values ('/tokens/{id}/print', 'get', '처리할 문서 프린트 화면', 'TRUE');
insert into awf_url values ('/tokens/{id}/view-pop', 'get', '관련문서 팝업 화면', 'TRUE');
insert into awf_url values ('/users', 'get', '사용자 조회 목록 화면', 'TRUE');
insert into awf_url values ('/users/new', 'get', '사용자 등록 화면', 'TRUE');
insert into awf_url values ('/users/search', 'get', '사용자 검색, 목록 등 메인이 되는 조회 화면', 'TRUE');
insert into awf_url values ('/users/{userkey}/edit', 'get', '사용자 정보 수정 화면', 'TRUE');
insert into awf_url values ('/users/{userkey}/editself', 'get', '사용자 자기 정보 수정 화면', 'FALSE');

/**
 * URL별권한매핑
 */

DROP TABLE IF EXISTS awf_url_auth_map cascade;

CREATE TABLE awf_url_auth_map
(
    url varchar(512) NOT NULL,
    method varchar(16) NOT NULL,
    auth_id varchar(100) NOT NULL,
    CONSTRAINT awf_url_auth_map_pk PRIMARY KEY (url, method, auth_id),
    CONSTRAINT awf_url_auth_map_fk1 FOREIGN KEY (url, method) REFERENCES awf_url (url, method),
    CONSTRAINT awf_url_auth_map_fk2 FOREIGN KEY (auth_id) REFERENCES awf_auth (auth_id)
);

COMMENT ON TABLE awf_url_auth_map IS 'URL별권한매핑';
COMMENT ON COLUMN awf_url_auth_map.url IS '요청url';
COMMENT ON COLUMN awf_url_auth_map.method IS 'method';
COMMENT ON COLUMN awf_url_auth_map.auth_id IS '권한아이디';

insert into awf_url_auth_map values ('/auths/edit', 'get', 'auth.create');
insert into awf_url_auth_map values ('/auths/edit', 'get', 'auth.delete');
insert into awf_url_auth_map values ('/auths/edit', 'get', 'auth.update');
insert into awf_url_auth_map values ('/auths/edit', 'get', 'auth.read');
insert into awf_url_auth_map values ('/auths', 'get', 'auth.create');
insert into awf_url_auth_map values ('/auths', 'get', 'auth.delete');
insert into awf_url_auth_map values ('/auths', 'get', 'auth.update');
insert into awf_url_auth_map values ('/auths', 'get', 'auth.read');
insert into awf_url_auth_map values ('/boards', 'get', 'board.admin.create');
insert into awf_url_auth_map values ('/boards', 'get', 'board.admin.delete');
insert into awf_url_auth_map values ('/boards', 'get', 'board.admin.update');
insert into awf_url_auth_map values ('/boards', 'get', 'board.admin.read');
insert into awf_url_auth_map values ('/boards/new', 'get', 'board.admin.create');
insert into awf_url_auth_map values ('/boards/search', 'get', 'board.admin.update');
insert into awf_url_auth_map values ('/boards/search', 'get', 'board.admin.read');
insert into awf_url_auth_map values ('/boards/search', 'get', 'board.admin.delete');
insert into awf_url_auth_map values ('/boards/search', 'get', 'board.admin.create');
insert into awf_url_auth_map values ('/boards/{id}/edit', 'get', 'board.admin.update');
insert into awf_url_auth_map values ('/boards/{id}/edit', 'get', 'board.admin.create');
insert into awf_url_auth_map values ('/boards/{id}/view', 'get', 'board.admin.read');
insert into awf_url_auth_map values ('/boards/{id}/view', 'get', 'board.admin.delete');
insert into awf_url_auth_map values ('/boards/{id}/view', 'get', 'board.admin.create');
insert into awf_url_auth_map values ('/boards/{id}/view', 'get', 'board.admin.update');
insert into awf_url_auth_map values ('/boards/articles', 'get', 'board.create');
insert into awf_url_auth_map values ('/boards/articles', 'get', 'board.read');
insert into awf_url_auth_map values ('/boards/articles', 'get', 'board.update');
insert into awf_url_auth_map values ('/boards/articles', 'get', 'board.delete');
insert into awf_url_auth_map values ('/boards/articles/search', 'get', 'board.delete');
insert into awf_url_auth_map values ('/boards/articles/search', 'get', 'board.create');
insert into awf_url_auth_map values ('/boards/articles/search', 'get', 'board.read');
insert into awf_url_auth_map values ('/boards/articles/search', 'get', 'board.update');
insert into awf_url_auth_map values ('/boards/articles/search/param', 'get', 'board.update');
insert into awf_url_auth_map values ('/boards/articles/search/param', 'get', 'board.create');
insert into awf_url_auth_map values ('/boards/articles/search/param', 'get', 'board.read');
insert into awf_url_auth_map values ('/boards/articles/search/param', 'get', 'board.delete');
insert into awf_url_auth_map values ('/boards/articles/{id}/comments', 'get', 'board.read');
insert into awf_url_auth_map values ('/boards/articles/{id}/edit', 'get', 'board.create');
insert into awf_url_auth_map values ('/boards/articles/{id}/edit', 'get', 'board.update');
insert into awf_url_auth_map values ('/boards/articles/{id}/new', 'get', 'board.create');
insert into awf_url_auth_map values ('/boards/articles/{id}/reply/edit', 'get', 'board.create');
insert into awf_url_auth_map values ('/boards/articles/{id}/view', 'get', 'board.update');
insert into awf_url_auth_map values ('/boards/articles/{id}/view', 'get', 'board.create');
insert into awf_url_auth_map values ('/boards/articles/{id}/view', 'get', 'board.read');
insert into awf_url_auth_map values ('/boards/articles/{id}/view', 'get', 'board.delete');
insert into awf_url_auth_map values ('/charts', 'get', 'chart.read');
insert into awf_url_auth_map values ('/charts/search', 'get', 'chart.read');
insert into awf_url_auth_map values ('/charts/new', 'get', 'chart.create');
insert into awf_url_auth_map values ('/charts/new', 'get', 'chart.update');
insert into awf_url_auth_map values ('/charts/{id}/edit', 'get', 'chart.create');
insert into awf_url_auth_map values ('/charts/{id}/edit', 'get', 'chart.update');
insert into awf_url_auth_map values ('/charts/{id}/view', 'get', 'chart.read');
insert into awf_url_auth_map values ('/cmdb/attributes', 'get', 'cmdb.attribute.read');
insert into awf_url_auth_map values ('/cmdb/attributes/new', 'get', 'cmdb.attribute.create');
insert into awf_url_auth_map values ('/cmdb/attributes/search', 'get', 'cmdb.attribute.read');
insert into awf_url_auth_map values ('/cmdb/attributes/{id}/edit', 'get', 'cmdb.attribute.create');
insert into awf_url_auth_map values ('/cmdb/attributes/{id}/edit', 'get', 'cmdb.attribute.update');
insert into awf_url_auth_map values ('/cmdb/attributes/{id}/view', 'get', 'cmdb.attribute.read');
insert into awf_url_auth_map values ('/cmdb/class/edit', 'get', 'cmdb.class.read');
insert into awf_url_auth_map values ('/cmdb/class/view-pop/attributes', 'get', 'cmdb.class.read');
insert into awf_url_auth_map values ('/cmdb/cis', 'get', 'cmdb.ci.read');
insert into awf_url_auth_map values ('/cmdb/cis/search', 'get', 'cmdb.ci.read');
insert into awf_url_auth_map values ('/cmdb/cis/{id}/view', 'get', 'form.read');
insert into awf_url_auth_map values ('/cmdb/types', 'get', 'cmdb.type.read');
insert into awf_url_auth_map values ('/cmdb/types/edit', 'get', 'cmdb.type.read');
insert into awf_url_auth_map values ('/codes/edit', 'get', 'code.delete');
insert into awf_url_auth_map values ('/codes/edit', 'get', 'code.update');
insert into awf_url_auth_map values ('/codes/edit', 'get', 'code.read');
insert into awf_url_auth_map values ('/codes/edit', 'get', 'code.create');
insert into awf_url_auth_map values ('/custom-codes', 'get', 'custom.code.create');
insert into awf_url_auth_map values ('/custom-codes', 'get', 'custom.code.read');
insert into awf_url_auth_map values ('/custom-codes', 'get', 'custom.code.update');
insert into awf_url_auth_map values ('/custom-codes', 'get', 'custom.code.delete');
insert into awf_url_auth_map values ('/custom-codes/new', 'get', 'custom.code.create');
insert into awf_url_auth_map values ('/custom-codes/search', 'get', 'custom.code.update');
insert into awf_url_auth_map values ('/custom-codes/search', 'get', 'custom.code.create');
insert into awf_url_auth_map values ('/custom-codes/search', 'get', 'custom.code.read');
insert into awf_url_auth_map values ('/custom-codes/search', 'get', 'custom.code.delete');
insert into awf_url_auth_map values ('/custom-codes/{id}/edit', 'get', 'custom.code.create');
insert into awf_url_auth_map values ('/custom-codes/{id}/edit', 'get', 'custom.code.update');
insert into awf_url_auth_map values ('/custom-codes/{id}/search', 'get', 'document.create');
insert into awf_url_auth_map values ('/custom-codes/{id}/search', 'get', 'document.read');
insert into awf_url_auth_map values ('/custom-codes/{id}/search', 'get', 'document.delete');
insert into awf_url_auth_map values ('/custom-codes/{id}/search', 'get', 'document.update');
insert into awf_url_auth_map values ('/custom-codes/{id}/view', 'get', 'custom.code.delete');
insert into awf_url_auth_map values ('/custom-codes/{id}/view', 'get', 'custom.code.read');
insert into awf_url_auth_map values ('/custom-codes/{id}/view', 'get', 'custom.code.update');
insert into awf_url_auth_map values ('/custom-codes/{id}/view', 'get', 'custom.code.create');
insert into awf_url_auth_map values ('/dashboard/view', 'get', 'document.read');
insert into awf_url_auth_map values ('/workflows', 'get', 'document.admin.delete');
insert into awf_url_auth_map values ('/workflows', 'get', 'document.admin.read');
insert into awf_url_auth_map values ('/workflows', 'get', 'document.admin.create');
insert into awf_url_auth_map values ('/workflows', 'get', 'document.admin.update');
insert into awf_url_auth_map values ('/workflows/new', 'get', 'document.admin.create');
insert into awf_url_auth_map values ('/workflows/search', 'get', 'document.admin.update');
insert into awf_url_auth_map values ('/workflows/search', 'get', 'document.admin.read');
insert into awf_url_auth_map values ('/workflows/search', 'get', 'document.admin.create');
insert into awf_url_auth_map values ('/workflows/search', 'get', 'document.admin.delete');
insert into awf_url_auth_map values ('/workflows/{id}/edit', 'get', 'document.admin.update');
insert into awf_url_auth_map values ('/workflows/{id}/edit', 'get', 'document.admin.create');
insert into awf_url_auth_map values ('/documents/search', 'get', 'document.read');
insert into awf_url_auth_map values ('/documents/{id}/print', 'get', 'document.read');
insert into awf_url_auth_map values ('/downloads', 'get', 'download.update');
insert into awf_url_auth_map values ('/downloads', 'get', 'download.delete');
insert into awf_url_auth_map values ('/downloads', 'get', 'download.create');
insert into awf_url_auth_map values ('/downloads', 'get', 'download.read');
insert into awf_url_auth_map values ('/downloads/new', 'get', 'download.create');
insert into awf_url_auth_map values ('/downloads/search', 'get', 'download.update');
insert into awf_url_auth_map values ('/downloads/search', 'get', 'download.read');
insert into awf_url_auth_map values ('/downloads/search', 'get', 'download.create');
insert into awf_url_auth_map values ('/downloads/search', 'get', 'download.delete');
insert into awf_url_auth_map values ('/downloads/{id}/edit', 'get', 'download.create');
insert into awf_url_auth_map values ('/downloads/{id}/edit', 'get', 'download.update');
insert into awf_url_auth_map values ('/downloads/{id}/view', 'get', 'download.delete');
insert into awf_url_auth_map values ('/downloads/{id}/view', 'get', 'download.create');
insert into awf_url_auth_map values ('/downloads/{id}/view', 'get', 'download.read');
insert into awf_url_auth_map values ('/downloads/{id}/view', 'get', 'download.update');
insert into awf_url_auth_map values ('/faqs', 'get', 'faq.read');
insert into awf_url_auth_map values ('/faqs/new', 'get', 'faq.create');
insert into awf_url_auth_map values ('/faqs/search', 'get', 'faq.read');
insert into awf_url_auth_map values ('/faqs/{id}/edit', 'get', 'faq.update');
insert into awf_url_auth_map values ('/faqs/{id}/edit', 'get', 'faq.delete');
insert into awf_url_auth_map values ('/faqs/{id}/edit', 'get', 'faq.create');
insert into awf_url_auth_map values ('/faqs/{id}/view', 'get', 'faq.read');
insert into awf_url_auth_map values ('/form/{id}/edit', 'get', 'form.read');
insert into awf_url_auth_map values ('/form/{id}/preview', 'get', 'form.read');
insert into awf_url_auth_map values ('/form/{id}/view', 'get', 'form.read');
insert into awf_url_auth_map values ('/forms', 'get', 'form.read');
insert into awf_url_auth_map values ('/forms/search', 'get', 'form.read');
insert into awf_url_auth_map values ('/images', 'get', 'image.read');
insert into awf_url_auth_map values ('/notices', 'get', 'notice.read');
insert into awf_url_auth_map values ('/notices/new', 'get', 'notice.create');
insert into awf_url_auth_map values ('/notices/search', 'get', 'notice.delete');
insert into awf_url_auth_map values ('/notices/search', 'get', 'notice.update');
insert into awf_url_auth_map values ('/notices/search', 'get', 'notice.create');
insert into awf_url_auth_map values ('/notices/search', 'get', 'notice.read');
insert into awf_url_auth_map values ('/notices/{id}/edit', 'get', 'notice.create');
insert into awf_url_auth_map values ('/notices/{id}/edit', 'get', 'notice.update');
insert into awf_url_auth_map values ('/notices/{id}/view', 'get', 'notice.read');
insert into awf_url_auth_map values ('/notices/{id}/view-pop', 'get', 'notice.read');
insert into awf_url_auth_map values ('/numberingPatterns', 'get', 'numbering.pattern.read');
insert into awf_url_auth_map values ('/numberingPatterns/edit', 'get', 'numbering.pattern.update');
insert into awf_url_auth_map values ('/numberingPatterns/edit', 'get', 'numbering.pattern.read');
insert into awf_url_auth_map values ('/numberingPatterns/edit', 'get', 'numbering.pattern.create');
insert into awf_url_auth_map values ('/numberingPatterns/edit', 'get', 'numbering.pattern.delete');
insert into awf_url_auth_map values ('/numberingRules', 'get', 'numbering.rule.read');
insert into awf_url_auth_map values ('/numberingRules/edit', 'get', 'numbering.rule.update');
insert into awf_url_auth_map values ('/numberingRules/edit', 'get', 'numbering.rule.read');
insert into awf_url_auth_map values ('/numberingRules/edit', 'get', 'numbering.rule.create');
insert into awf_url_auth_map values ('/numberingRules/edit', 'get', 'numbering.rule.delete');
insert into awf_url_auth_map values ('/processes', 'get', 'process.read');
insert into awf_url_auth_map values ('/processes', 'get', 'process.delete');
insert into awf_url_auth_map values ('/processes', 'get', 'process.update');
insert into awf_url_auth_map values ('/processes', 'get', 'process.create');
insert into awf_url_auth_map values ('/processes/search', 'get', 'process.read');
insert into awf_url_auth_map values ('/processes/search', 'get', 'process.delete');
insert into awf_url_auth_map values ('/processes/search', 'get', 'process.create');
insert into awf_url_auth_map values ('/processes/search', 'get', 'process.update');
insert into awf_url_auth_map values ('/process/{id}/view', 'get', 'process.create');
insert into awf_url_auth_map values ('/process/{id}/view', 'get', 'process.update');
insert into awf_url_auth_map values ('/process/{id}/edit', 'get', 'process.create');
insert into awf_url_auth_map values ('/process/{id}/edit', 'get', 'process.update');
insert into awf_url_auth_map values ('/process/{id}/status', 'get', 'document.read');
insert into awf_url_auth_map values ('/rest/auths', 'post', 'auth.create');
insert into awf_url_auth_map values ('/rest/auths', 'get', 'auth.delete');
insert into awf_url_auth_map values ('/rest/auths', 'get', 'auth.update');
insert into awf_url_auth_map values ('/rest/auths', 'get', 'auth.create');
insert into awf_url_auth_map values ('/rest/auths', 'get', 'auth.read');
insert into awf_url_auth_map values ('/rest/auths/{id}', 'get', 'auth.update');
insert into awf_url_auth_map values ('/rest/auths/{id}', 'get', 'auth.read');
insert into awf_url_auth_map values ('/rest/auths/{id}', 'get', 'auth.create');
insert into awf_url_auth_map values ('/rest/auths/{id}', 'get', 'auth.delete');
insert into awf_url_auth_map values ('/rest/auths/{id}', 'put', 'auth.update');
insert into awf_url_auth_map values ('/rest/auths/{id}', 'delete', 'auth.delete');
insert into awf_url_auth_map values ('/rest/boards', 'put', 'board.admin.update');
insert into awf_url_auth_map values ('/rest/boards', 'post', 'board.admin.create');
insert into awf_url_auth_map values ('/rest/boards/{id}/view', 'get', 'board.create');
insert into awf_url_auth_map values ('/rest/boards/{id}/view', 'get', 'board.update');
insert into awf_url_auth_map values ('/rest/boards/{id}', 'delete', 'board.admin.delete');
insert into awf_url_auth_map values ('/rest/boards/articles', 'put', 'board.update');
insert into awf_url_auth_map values ('/rest/boards/articles', 'post', 'board.create');
insert into awf_url_auth_map values ('/rest/boards/articles/comments', 'put', 'board.update');
insert into awf_url_auth_map values ('/rest/boards/articles/comments', 'post', 'board.create');
insert into awf_url_auth_map values ('/rest/boards/articles/comments/{id}', 'delete', 'board.delete');
insert into awf_url_auth_map values ('/rest/boards/articles/reply', 'post', 'board.create');
insert into awf_url_auth_map values ('/rest/boards/articles/{id}', 'delete', 'board.delete');
insert into awf_url_auth_map values ('/rest/charts', 'post', 'chart.create');
insert into awf_url_auth_map values ('/rest/charts/{id}', 'put', 'chart.update');
insert into awf_url_auth_map values ('/rest/charts/{id}', 'delete', 'chart.delete');
insert into awf_url_auth_map values ('/rest/charts/{id}/preview', 'post', 'chart.create');
insert into awf_url_auth_map values ('/rest/charts/{id}/preview', 'post', 'chart.update');
insert into awf_url_auth_map values ('/rest/cmdb/attributes', 'get', 'cmdb.attribute.read');
insert into awf_url_auth_map values ('/rest/cmdb/attributes', 'post', 'cmdb.attribute.create');
insert into awf_url_auth_map values ('/rest/cmdb/attributes/{id}', 'put', 'cmdb.attribute.update');
insert into awf_url_auth_map values ('/rest/cmdb/attributes/{id}', 'delete', 'cmdb.attribute.delete');
insert into awf_url_auth_map values ('/rest/cmdb/classes', 'get', 'cmdb.class.read');
insert into awf_url_auth_map values ('/rest/cmdb/classes', 'get', 'cmdb.class.create');
insert into awf_url_auth_map values ('/rest/cmdb/classes', 'get', 'cmdb.class.update');
insert into awf_url_auth_map values ('/rest/cmdb/classes', 'get', 'cmdb.class.delete');
insert into awf_url_auth_map values ('/rest/cmdb/classes', 'post', 'cmdb.class.create');
insert into awf_url_auth_map values ('/rest/cmdb/classes/{id}', 'get', 'cmdb.class.read');
insert into awf_url_auth_map values ('/rest/cmdb/classes/{id}', 'get', 'cmdb.class.create');
insert into awf_url_auth_map values ('/rest/cmdb/classes/{id}', 'get', 'cmdb.class.update');
insert into awf_url_auth_map values ('/rest/cmdb/classes/{id}', 'get', 'cmdb.class.delete');
insert into awf_url_auth_map values ('/rest/cmdb/classes/{id}', 'put', 'cmdb.class.update');
insert into awf_url_auth_map values ('/rest/cmdb/classes/{id}', 'delete', 'cmdb.class.delete');
insert into awf_url_auth_map values ('/rest/cmdb/classes/{id}/attributes', 'get', 'form.read');
insert into awf_url_auth_map values ('/rest/cmdb/classes/{id}/attributes', 'get', 'form.update');
insert into awf_url_auth_map values ('/rest/cmdb/types', 'get', 'cmdb.type.read');
insert into awf_url_auth_map values ('/rest/cmdb/types', 'get', 'cmdb.type.create');
insert into awf_url_auth_map values ('/rest/cmdb/types', 'get', 'cmdb.type.update');
insert into awf_url_auth_map values ('/rest/cmdb/types', 'get', 'cmdb.type.delete');
insert into awf_url_auth_map values ('/rest/cmdb/types', 'post', 'cmdb.type.create');
insert into awf_url_auth_map values ('/rest/cmdb/types/{id}', 'get', 'cmdb.type.read');
insert into awf_url_auth_map values ('/rest/cmdb/types/{id}', 'get', 'cmdb.type.create');
insert into awf_url_auth_map values ('/rest/cmdb/types/{id}', 'get', 'cmdb.type.update');
insert into awf_url_auth_map values ('/rest/cmdb/types/{id}', 'get', 'cmdb.type.delete');
insert into awf_url_auth_map values ('/rest/cmdb/types/{id}', 'put', 'cmdb.type.update');
insert into awf_url_auth_map values ('/rest/cmdb/types/{id}', 'delete', 'cmdb.type.delete');
insert into awf_url_auth_map values ('/rest/codes', 'get', 'code.read');
insert into awf_url_auth_map values ('/rest/codes', 'post', 'code.create');
insert into awf_url_auth_map values ('/rest/codes', 'get', 'code.delete');
insert into awf_url_auth_map values ('/rest/codes', 'get', 'code.update');
insert into awf_url_auth_map values ('/rest/codes', 'get', 'code.create');
insert into awf_url_auth_map values ('/rest/codes/{id}', 'get', 'code.create');
insert into awf_url_auth_map values ('/rest/codes/{id}', 'get', 'code.update');
insert into awf_url_auth_map values ('/rest/codes/{id}', 'get', 'code.delete');
insert into awf_url_auth_map values ('/rest/codes/{id}', 'get', 'code.read');
insert into awf_url_auth_map values ('/rest/codes/{id}', 'put', 'code.update');
insert into awf_url_auth_map values ('/rest/codes/{id}', 'delete', 'code.delete');
insert into awf_url_auth_map values ('/rest/custom-codes', 'post', 'custom.code.delete');
insert into awf_url_auth_map values ('/rest/custom-codes', 'put', 'custom.code.create');
insert into awf_url_auth_map values ('/rest/custom-codes', 'post', 'custom.code.create');
insert into awf_url_auth_map values ('/rest/custom-codes', 'get', 'custom.code.delete');
insert into awf_url_auth_map values ('/rest/custom-codes', 'get', 'custom.code.create');
insert into awf_url_auth_map values ('/rest/custom-codes', 'put', 'custom.code.read');
insert into awf_url_auth_map values ('/rest/custom-codes', 'post', 'custom.code.read');
insert into awf_url_auth_map values ('/rest/custom-codes', 'get', 'custom.code.read');
insert into awf_url_auth_map values ('/rest/custom-codes', 'put', 'custom.code.update');
insert into awf_url_auth_map values ('/rest/custom-codes', 'post', 'custom.code.update');
insert into awf_url_auth_map values ('/rest/custom-codes', 'get', 'custom.code.update');
insert into awf_url_auth_map values ('/rest/custom-codes', 'put', 'custom.code.delete');
insert into awf_url_auth_map values ('/rest/custom-codes/{id}', 'get', 'form.read');
insert into awf_url_auth_map values ('/rest/custom-codes/{id}', 'get', 'form.update');
insert into awf_url_auth_map values ('/rest/custom-codes/{id}', 'delete', 'custom.code.delete');
insert into awf_url_auth_map values ('/rest/documents', 'get', 'process.update');
insert into awf_url_auth_map values ('/rest/documents', 'get', 'process.create');
insert into awf_url_auth_map values ('/rest/workflows', 'post', 'document.admin.create');
insert into awf_url_auth_map values ('/rest/workflows/{id}', 'delete', 'document.admin.create');
insert into awf_url_auth_map values ('/rest/workflows/{id}', 'get', 'document.admin.create');
insert into awf_url_auth_map values ('/rest/workflows/{id}', 'put', 'document.admin.create');
insert into awf_url_auth_map values ('/rest/workflows/{id}', 'get', 'document.admin.update');
insert into awf_url_auth_map values ('/rest/workflows/{id}', 'put', 'document.admin.update');
insert into awf_url_auth_map values ('/rest/workflows/{id}', 'delete', 'document.admin.update');
insert into awf_url_auth_map values ('/rest/workflows/{id}', 'delete', 'document.admin.delete');
insert into awf_url_auth_map values ('/rest/workflows/{id}', 'put', 'document.admin.delete');
insert into awf_url_auth_map values ('/rest/workflows/{id}', 'get', 'document.admin.delete');
insert into awf_url_auth_map values ('/rest/workflows/{id}/display', 'put', 'document.admin.create');
insert into awf_url_auth_map values ('/rest/workflows/{id}/display', 'get', 'document.admin.create');
insert into awf_url_auth_map values ('/rest/documents/{id}/data', 'get', 'document.create');
insert into awf_url_auth_map values ('/rest/downloads', 'post', 'download.update');
insert into awf_url_auth_map values ('/rest/downloads', 'put', 'download.update');
insert into awf_url_auth_map values ('/rest/downloads', 'post', 'download.read');
insert into awf_url_auth_map values ('/rest/downloads', 'put', 'download.read');
insert into awf_url_auth_map values ('/rest/downloads', 'put', 'download.create');
insert into awf_url_auth_map values ('/rest/downloads', 'post', 'download.delete');
insert into awf_url_auth_map values ('/rest/downloads', 'post', 'download.create');
insert into awf_url_auth_map values ('/rest/downloads', 'put', 'download.delete');
insert into awf_url_auth_map values ('/rest/downloads/{id}', 'delete', 'download.delete');
insert into awf_url_auth_map values ('/rest/faqs', 'post', 'faq.create');
insert into awf_url_auth_map values ('/rest/faqs/{id}', 'put', 'faq.update');
insert into awf_url_auth_map values ('/rest/faqs/{id}', 'get', 'faq.read');
insert into awf_url_auth_map values ('/rest/faqs/{id}', 'delete', 'faq.delete');
insert into awf_url_auth_map values ('/rest/filenameextensions', 'get', 'file.read');
insert into awf_url_auth_map values ('/rest/form/{id}', 'delete', 'form.delete');
insert into awf_url_auth_map values ('/rest/form/{id}/data', 'get', 'form.read');
insert into awf_url_auth_map values ('/rest/form/{id}/data', 'get', 'form.create');
insert into awf_url_auth_map values ('/rest/form/{id}/data', 'get', 'form.update');
insert into awf_url_auth_map values ('/rest/form/{id}/data', 'put', 'form.create');
insert into awf_url_auth_map values ('/rest/form/{id}/data', 'put', 'form.update');
insert into awf_url_auth_map values ('/rest/forms', 'post', 'form.create');
insert into awf_url_auth_map values ('/rest/forms', 'post', 'form.delete');
insert into awf_url_auth_map values ('/rest/forms/{id}', 'put', 'form.update');
insert into awf_url_auth_map values ('/rest/forms/{id}/data', 'get', 'form.create');
insert into awf_url_auth_map values ('/rest/forms/{id}/data', 'get', 'form.update');
insert into awf_url_auth_map values ('/rest/images', 'put', 'image.update');
insert into awf_url_auth_map values ('/rest/images', 'post', 'image.create');
insert into awf_url_auth_map values ('/rest/images/{id}', 'delete', 'image.delete');
insert into awf_url_auth_map values ('/rest/images', 'get', 'form.create');
insert into awf_url_auth_map values ('/rest/images', 'get', 'form.read');
insert into awf_url_auth_map values ('/rest/images', 'get', 'form.update');
insert into awf_url_auth_map values ('/rest/images', 'get', 'form.delete');
insert into awf_url_auth_map values ('/rest/notices', 'post', 'notice.create');
insert into awf_url_auth_map values ('/rest/notices/{id}', 'delete', 'notice.delete');
insert into awf_url_auth_map values ('/rest/notices/{id}', 'put', 'notice.update');
insert into awf_url_auth_map values ('/rest/numberingPatterns', 'get', 'numbering.pattern.read');
insert into awf_url_auth_map values ('/rest/numberingPatterns', 'get', 'numbering.pattern.create');
insert into awf_url_auth_map values ('/rest/numberingPatterns', 'get', 'numbering.pattern.update');
insert into awf_url_auth_map values ('/rest/numberingPatterns', 'get', 'numbering.pattern.delete');
insert into awf_url_auth_map values ('/rest/numberingPatterns', 'post', 'numbering.pattern.create');
insert into awf_url_auth_map values ('/rest/numberingPatterns/{id}', 'get', 'numbering.pattern.read');
insert into awf_url_auth_map values ('/rest/numberingPatterns/{id}', 'get', 'numbering.pattern.create');
insert into awf_url_auth_map values ('/rest/numberingPatterns/{id}', 'get', 'numbering.pattern.update');
insert into awf_url_auth_map values ('/rest/numberingPatterns/{id}', 'get', 'numbering.pattern.delete');
insert into awf_url_auth_map values ('/rest/numberingPatterns/{id}', 'put', 'numbering.pattern.update');
insert into awf_url_auth_map values ('/rest/numberingPatterns/{id}', 'delete', 'numbering.pattern.delete');
insert into awf_url_auth_map values ('/rest/numberingRules', 'get', 'numbering.rule.read');
insert into awf_url_auth_map values ('/rest/numberingRules', 'get', 'numbering.rule.create');
insert into awf_url_auth_map values ('/rest/numberingRules', 'get', 'numbering.rule.update');
insert into awf_url_auth_map values ('/rest/numberingRules', 'get', 'numbering.rule.delete');
insert into awf_url_auth_map values ('/rest/numberingRules', 'post', 'numbering.rule.create');
insert into awf_url_auth_map values ('/rest/numberingRules/{id}', 'get', 'numbering.rule.read');
insert into awf_url_auth_map values ('/rest/numberingRules/{id}', 'get', 'numbering.rule.create');
insert into awf_url_auth_map values ('/rest/numberingRules/{id}', 'get', 'numbering.rule.update');
insert into awf_url_auth_map values ('/rest/numberingRules/{id}', 'get', 'numbering.rule.delete');
insert into awf_url_auth_map values ('/rest/numberingRules/{id}', 'put', 'numbering.rule.update');
insert into awf_url_auth_map values ('/rest/numberingRules/{id}', 'delete', 'numbering.rule.delete');
insert into awf_url_auth_map values ('/rest/processes', 'post', 'process.read');
insert into awf_url_auth_map values ('/rest/processes/{id}', 'put', 'process.update');
insert into awf_url_auth_map values ('/rest/process/{id}/data', 'get', 'process.create');
insert into awf_url_auth_map values ('/rest/process/{id}/data', 'get', 'process.update');
insert into awf_url_auth_map values ('/rest/process/{id}/data', 'put', 'process.update');
insert into awf_url_auth_map values ('/rest/process/{id}', 'delete', 'process.delete');
insert into awf_url_auth_map values ('/rest/process/{id}/simulation', 'put', 'process.create');
insert into awf_url_auth_map values ('/rest/processes/all', 'get', 'process.read');
insert into awf_url_auth_map values ('/rest/processes/{id}/data', 'get', 'process.read');
insert into awf_url_auth_map values ('/rest/roles', 'get', 'role.create');
insert into awf_url_auth_map values ('/rest/roles', 'get', 'role.read');
insert into awf_url_auth_map values ('/rest/roles', 'post', 'role.create');
insert into awf_url_auth_map values ('/rest/roles', 'get', 'role.delete');
insert into awf_url_auth_map values ('/rest/roles', 'get', 'role.update');
insert into awf_url_auth_map values ('/rest/roles/{id}', 'delete', 'role.delete');
insert into awf_url_auth_map values ('/rest/roles/{id}', 'get', 'role.read');
insert into awf_url_auth_map values ('/rest/roles/{id}', 'get', 'role.create');
insert into awf_url_auth_map values ('/rest/roles/{id}', 'get', 'role.update');
insert into awf_url_auth_map values ('/rest/roles/{id}', 'get', 'role.delete');
insert into awf_url_auth_map values ('/rest/roles/{id}', 'put', 'role.update');
insert into awf_url_auth_map values ('/rest/schedulers', 'post', 'scheduler.create');
insert into awf_url_auth_map values ('/rest/schedulers/{id}', 'delete', 'scheduler.delete');
insert into awf_url_auth_map values ('/rest/schedulers/{id}', 'put', 'scheduler.update');
insert into awf_url_auth_map values ('/rest/schedulers/{id}/execute', 'post', 'scheduler.execute');
insert into awf_url_auth_map values ('/rest/tokens/data', 'post', 'token.create');
insert into awf_url_auth_map values ('/rest/tokens/{id}/data', 'get', 'token.create');
insert into awf_url_auth_map values ('/rest/tokens/{id}/data', 'put', 'token.create');
insert into awf_url_auth_map values ('/rest/users', 'post', 'user.create');
insert into awf_url_auth_map values ('/rest/users/all', 'get', 'user.read');
insert into awf_url_auth_map values ('/rest/users/{userkey}/all', 'put', 'user.update');
insert into awf_url_auth_map values ('/rest/users/{userkey}/all', 'put', 'user.read');
insert into awf_url_auth_map values ('/rest/users/{userkey}/info', 'put', 'user.read');
insert into awf_url_auth_map values ('/rest/users/{userkey}/info', 'put', 'user.update');
insert into awf_url_auth_map values ('/rest/users/{userkey}/resetpassword', 'put', 'user.read');
insert into awf_url_auth_map values ('/rest/users/{userkey}/resetpassword', 'put', 'user.update');
insert into awf_url_auth_map values ('/roles/edit', 'get', 'role.update');
insert into awf_url_auth_map values ('/roles/edit', 'get', 'role.read');
insert into awf_url_auth_map values ('/roles/edit', 'get', 'role.create');
insert into awf_url_auth_map values ('/roles/edit', 'get', 'role.delete');
insert into awf_url_auth_map values ('/roles', 'get', 'role.read');
insert into awf_url_auth_map values ('/schedulers', 'get', 'scheduler.create');
insert into awf_url_auth_map values ('/schedulers', 'get', 'scheduler.delete');
insert into awf_url_auth_map values ('/schedulers', 'get', 'scheduler.read');
insert into awf_url_auth_map values ('/schedulers', 'get', 'scheduler.update');
insert into awf_url_auth_map values ('/schedulers/new', 'get', 'scheduler.create');
insert into awf_url_auth_map values ('/schedulers/search', 'get', 'scheduler.create');
insert into awf_url_auth_map values ('/schedulers/search', 'get', 'scheduler.delete');
insert into awf_url_auth_map values ('/schedulers/search', 'get', 'scheduler.read');
insert into awf_url_auth_map values ('/schedulers/search', 'get', 'scheduler.update');
insert into awf_url_auth_map values ('/schedulers/{id}/edit', 'get', 'scheduler.delete');
insert into awf_url_auth_map values ('/schedulers/{id}/edit', 'get', 'scheduler.update');
insert into awf_url_auth_map values ('/schedulers/{id}/history', 'get', 'scheduler.read');
insert into awf_url_auth_map values ('/schedulers/{id}/view', 'get', 'scheduler.read');
insert into awf_url_auth_map values ('/tokens', 'get', 'token.create');
insert into awf_url_auth_map values ('/tokens', 'get', 'token.read');
insert into awf_url_auth_map values ('/tokens/search', 'get', 'token.read');
insert into awf_url_auth_map values ('/tokens/search', 'get', 'token.create');
insert into awf_url_auth_map values ('/tokens/view-pop/documents', 'get', 'token.create');
insert into awf_url_auth_map values ('/tokens/view-pop/documents', 'get', 'token.read');
insert into awf_url_auth_map values ('/tokens/{id}/edit', 'get', 'token.create');
insert into awf_url_auth_map values ('/tokens/{id}/edit-tab', 'get', 'token.create');
insert into awf_url_auth_map values ('/tokens/{id}/view', 'get', 'token.read');
insert into awf_url_auth_map values ('/tokens/{id}/view-tab', 'get', 'token.read');
insert into awf_url_auth_map values ('/tokens/{id}/print', 'get', 'token.read');
insert into awf_url_auth_map values ('/tokens/{id}/view-pop', 'get', 'token.read');
insert into awf_url_auth_map values ('/tokens/{id}/view-pop', 'get', 'token.create');
insert into awf_url_auth_map values ('/users', 'get', 'user.update');
insert into awf_url_auth_map values ('/users', 'get', 'user.delete');
insert into awf_url_auth_map values ('/users', 'get', 'user.read');
insert into awf_url_auth_map values ('/users', 'get', 'user.create');
insert into awf_url_auth_map values ('/users/new', 'get', 'user.read');
insert into awf_url_auth_map values ('/users/new', 'get', 'user.create');
insert into awf_url_auth_map values ('/users/new', 'get', 'user.update');
insert into awf_url_auth_map values ('/users/new', 'get', 'user.delete');
insert into awf_url_auth_map values ('/users/search', 'get', 'user.read');
insert into awf_url_auth_map values ('/users/search', 'get', 'user.create');
insert into awf_url_auth_map values ('/users/search', 'get', 'user.update');
insert into awf_url_auth_map values ('/users/search', 'get', 'user.delete');
insert into awf_url_auth_map values ('/users/{userkey}/edit', 'get', 'user.read');
insert into awf_url_auth_map values ('/users/{userkey}/edit', 'get', 'user.update');
insert into awf_url_auth_map values ('/users/{userkey}/editself', 'get', 'user.read');
insert into awf_url_auth_map values ('/users/{userkey}/editself', 'get', 'user.update');

/**
 * 사용자정보
 */
DROP TABLE IF EXISTS awf_user cascade;

CREATE TABLE awf_user
(
	user_key varchar(128) NOT NULL,
	user_id  varchar(128) UNIQUE,
	user_name  varchar(128),
	password varchar(128),
	email  varchar(128) NOT NULL,
	use_yn boolean DEFAULT 'true',
	try_login_count int DEFAULT 0,
	expired_dt date,
	position varchar(128),
	department varchar(128),
	certification_code varchar(128),
	status varchar(100),
	office_number varchar(128),
	mobile_number varchar(128),
	platform varchar(100),
	timezone varchar(100),
	oauth_key varchar(256),
	lang varchar(100),
	time_format varchar(100),
	theme varchar(100) DEFAULT 'default',
	create_user_key varchar(128),
	create_dt timestamp,
	update_user_key varchar(128),
	update_dt timestamp,
	avatar_type varchar(100),
	avatar_value varchar(512),
	uploaded boolean DEFAULT 'false',
	uploaded_location varchar(512),
	CONSTRAINT awf_user_pk PRIMARY KEY (user_key),
	CONSTRAINT awf_user_uk UNIQUE (user_id )
);

COMMENT ON TABLE awf_user IS '사용자정보';
COMMENT ON COLUMN awf_user.user_key IS '사용자키';
COMMENT ON COLUMN awf_user.user_id  IS '사용자아이디';
COMMENT ON COLUMN awf_user.user_name  IS '사용자명';
COMMENT ON COLUMN awf_user.password IS '비밀번호';
COMMENT ON COLUMN awf_user.email  IS '이메일';
COMMENT ON COLUMN awf_user.use_yn IS '사용여부';
COMMENT ON COLUMN awf_user.try_login_count IS '로그인시도회수';
COMMENT ON COLUMN awf_user.expired_dt IS '유효기간';
COMMENT ON COLUMN awf_user.position IS '직책';
COMMENT ON COLUMN awf_user.department IS '부서';
COMMENT ON COLUMN awf_user.certification_code IS '인증코드';
COMMENT ON COLUMN awf_user.status IS '상태';
COMMENT ON COLUMN awf_user.office_number IS '사무실번호';
COMMENT ON COLUMN awf_user.mobile_number IS '핸드폰번호';
COMMENT ON COLUMN awf_user.platform IS '가입플랫폼';
COMMENT ON COLUMN awf_user.timezone IS '시간대정보';
COMMENT ON COLUMN awf_user.oauth_key IS 'OAUTH인증키';
COMMENT ON COLUMN awf_user.lang IS '언어';
COMMENT ON COLUMN awf_user.time_format IS '시간포맷';
COMMENT ON COLUMN awf_user.theme IS '테마';
COMMENT ON COLUMN awf_user.create_user_key IS '등록자';
COMMENT ON COLUMN awf_user.create_dt IS '등록일시';
COMMENT ON COLUMN awf_user.update_user_key IS '수정자';
COMMENT ON COLUMN awf_user.update_dt IS '수정일시';
COMMENT ON COLUMN awf_user.avatar_type IS '아바타 종류';
COMMENT ON COLUMN awf_user.avatar_value IS '아바타 value';
COMMENT ON COLUMN awf_user.uploaded IS '업로드 여부';
COMMENT ON COLUMN awf_user.uploaded_location IS '업로드 경로';

insert into awf_user values ('0509e09412534a6e98f04ca79abb6424', 'admin', 'ADMIN', '$2a$10$BG5U2Mmk1pkbQSzv8p8sY.guCC10C/hfutcH/0XGLDIIWxutMHT46', 'admin@gmail.com', TRUE, 0, now() + interval '3 month', null, null, 'KEAKvaudICgcbRwNaTTNSQ2XSvIcQyTdKdlYo80qvyQjbN5fAd', 'user.status.certified', null, null, 'user.platform.alice', 'Asia/Seoul', null, 'ko', 'yyyy-MM-dd HH:mm', 'default', '0509e09412534a6e98f04ca79abb6424', now(), null, null, 'FILE', 'img_avatar_01.png', FALSE, '');
insert into awf_user values ('system', 'system', 'system', '', 'system@gmail.com', TRUE, 0, now() + interval '3 month', null, null, 'KEAKvaudICgcbRwNaTTNSQ2XSvIcQyTdKdlYo80qvyQjbN5fAd', 'user.status.certified', null, null, 'user.platform.alice', 'Asia/Seoul', null, 'ko', 'yyyy-MM-dd HH:mm', 'default', '0509e09412534a6e98f04ca79abb6424', now(), null, null, 'FILE', 'img_avatar_01.png', FALSE, '');

/**
 * 사용자역할매핑
 */
DROP TABLE IF EXISTS awf_user_role_map cascade;

CREATE TABLE awf_user_role_map
(
	user_key varchar(128) NOT NULL,
	role_id varchar(100) NOT NULL,
	CONSTRAINT awf_user_role_map_pk PRIMARY KEY (user_key, role_id),
	CONSTRAINT awf_user_role_map_fk1 FOREIGN KEY (user_key) REFERENCES awf_user (user_key),
	CONSTRAINT awf_user_role_map_fk2 FOREIGN KEY (role_id) REFERENCES awf_role (role_id)
);

COMMENT ON TABLE awf_user_role_map IS '사용자역할매핑';
COMMENT ON COLUMN awf_user_role_map.user_key IS '사용자키';
COMMENT ON COLUMN awf_user_role_map.role_id IS '역할아이디';

insert into awf_user_role_map values ('0509e09412534a6e98f04ca79abb6424', 'admin');
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

insert into portal_board_admin values ('40288a19736b46fb01736b718cb60001', '자유 게시판', null, 1, true, true, true, false, true, 1024, '0509e09412534a6e98f04ca79abb6424', now(), null, null);

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
	create_user_key varchar(128),
	create_dt timestamp,
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

/**
 * 게시판
 */
DROP TABLE IF EXISTS portal_board cascade;

CREATE TABLE portal_board
(
	board_id varchar(128) NOT NULL,
	board_admin_id varchar(128) NOT NULL,
	board_category_id varchar(128),
	board_seq bigint NOT NULL,
	board_title varchar(512) NOT NULL,
	board_contents text,
	board_group_id bigint NOT NULL,
	board_level_id bigint NOT NULL,
	board_order_seq bigint NOT NULL,
	create_user_key varchar(128),
	create_dt timestamp,
	update_user_key varchar(128),
	update_dt timestamp,
	CONSTRAINT portal_board_pk PRIMARY KEY (board_id),
	CONSTRAINT portal_board_fk1 FOREIGN KEY (board_admin_id) REFERENCES portal_board_admin (board_admin_id),
	CONSTRAINT portal_board_fk2 FOREIGN KEY (board_category_id) REFERENCES portal_board_category (board_category_id)
);

COMMENT ON TABLE portal_board IS '게시판';
COMMENT ON COLUMN portal_board.board_id IS '게시판 번호';
COMMENT ON COLUMN portal_board.board_admin_id IS '게시판 관리 번호';
COMMENT ON COLUMN portal_board.board_category_id IS '카테고리 번호';
COMMENT ON COLUMN portal_board.board_seq IS '게시판 번호';
COMMENT ON COLUMN portal_board.board_title IS '게시판 제목';
COMMENT ON COLUMN portal_board.board_contents IS '게시판 내용';
COMMENT ON COLUMN portal_board.board_group_id IS '게시판 답글 시퀀스 부모 번호';
COMMENT ON COLUMN portal_board.board_level_id IS '게시판 답글 계층 번호';
COMMENT ON COLUMN portal_board.board_order_seq IS '게시판 답글 정렬 번호 ';
COMMENT ON COLUMN portal_board.create_user_key IS '등록자';
COMMENT ON COLUMN portal_board.create_dt IS '등록일';
COMMENT ON COLUMN portal_board.update_user_key IS '수정자';
COMMENT ON COLUMN portal_board.update_dt IS '수정일';

/**
 * 게시판 댓글
 */
DROP TABLE IF EXISTS portal_board_comment cascade;
 
CREATE TABLE portal_board_comment
(
	board_comment_id varchar(128) NOT NULL,
	board_id varchar(128) NOT NULL,
	board_comment_contents varchar(512) NOT NULL,
	create_user_key varchar(128),
	create_dt timestamp,
	update_user_key varchar(128),
	update_dt timestamp,
	CONSTRAINT portal_board_comment_pk PRIMARY KEY (board_comment_id),
	CONSTRAINT portal_board_comment_fk FOREIGN KEY (board_id) REFERENCES portal_board (board_id)
);

COMMENT ON TABLE portal_board_comment IS '게시판 댓글';
COMMENT ON COLUMN portal_board_comment.board_comment_id IS '댓글 번호';
COMMENT ON COLUMN portal_board_comment.board_id IS '게시판 번호';
COMMENT ON COLUMN portal_board_comment.board_comment_contents IS '댓글 내용';
COMMENT ON COLUMN portal_board_comment.create_user_key IS '등록자';
COMMENT ON COLUMN portal_board_comment.create_dt IS '등록일';
COMMENT ON COLUMN portal_board_comment.update_user_key IS '수정자';
COMMENT ON COLUMN portal_board_comment.update_dt IS '수정일';

/**
 * 게시판 조회
 */
DROP TABLE IF EXISTS portal_board_read cascade;

CREATE TABLE portal_board_read
(
	board_id varchar(128) NOT NULL,
	board_read_count int NOT NULL,
	CONSTRAINT portal_board_read_pk PRIMARY KEY (board_id),
	CONSTRAINT portal_board_read_fk FOREIGN KEY (board_id) REFERENCES portal_board (board_id)
);

COMMENT ON TABLE portal_board_read IS '게시판 조회';
COMMENT ON COLUMN portal_board_read.board_id IS '게시판 번호';
COMMENT ON COLUMN portal_board_read.board_read_count IS '조회수';
/**
 * FAQ정보
 */
DROP TABLE IF EXISTS portal_faq cascade;

CREATE TABLE portal_faq
(
	faq_id varchar(128) NOT NULL,
	faq_group varchar(100) NOT NULL,
    faq_title varchar(512) NOT NULL,
	faq_content text NOT NULL,
	create_user_key varchar(128),
	create_dt timestamp,
	update_user_key varchar(128),
	update_dt timestamp,
	CONSTRAINT portal_faq_pk PRIMARY KEY (faq_id)
);

COMMENT ON TABLE portal_faq IS 'FAQ정보';
COMMENT ON COLUMN portal_faq.faq_id IS 'FAQ아이디';
COMMENT ON COLUMN portal_faq.faq_group IS 'FAQ 그룹';
COMMENT ON COLUMN portal_faq.faq_title IS 'FAQ 제목';
COMMENT ON COLUMN portal_faq.faq_content IS 'FAQ 내용';
COMMENT ON COLUMN portal_faq.create_user_key IS '등록자';
COMMENT ON COLUMN portal_faq.create_dt IS '등록일';
COMMENT ON COLUMN portal_faq.update_user_key IS '수정자';
COMMENT ON COLUMN portal_faq.update_dt IS '수정일';

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

/**
 * 문서양식정보
 */
DROP TABLE IF EXISTS wf_form cascade;

CREATE TABLE wf_form
(
	form_id varchar(128) NOT NULL,
	form_name varchar(256) NOT NULL,
	form_desc varchar(256),
	form_status varchar(100) DEFAULT 'form.status.edit' NOT NULL,
	create_user_key varchar(128),
	create_dt timestamp,
	update_user_key varchar(128),
	update_dt timestamp,
	CONSTRAINT wf_form_pk PRIMARY KEY (form_id)
);

COMMENT ON TABLE wf_form IS '문서양식정보';
COMMENT ON COLUMN wf_form.form_id IS '문서양식아이디';
COMMENT ON COLUMN wf_form.form_name IS '문서양식이름';
COMMENT ON COLUMN wf_form.form_desc IS '문서양식설명';
COMMENT ON COLUMN wf_form.form_status IS '문서양식상태';
COMMENT ON COLUMN wf_form.create_user_key IS '생성자';
COMMENT ON COLUMN wf_form.create_dt IS '생성일시';
COMMENT ON COLUMN wf_form.update_user_key IS '수정자';
COMMENT ON COLUMN wf_form.update_dt IS '수정일시';

insert into wf_form values('4028b25d787736640178773f645b0003', '만족도 - 단순문의', '만족도 단순문의 문서양식입니다.', 'form.status.use', '0509e09412534a6e98f04ca79abb6424', now());
insert into wf_form values('4028b25d787736640178773857920000', '서비스데스크 - 단순문의', '단순문의 문서양식입니다.', 'form.status.use', '0509e09412534a6e98f04ca79abb6424', now());
insert into wf_form values('4028b25d77ed7e6f0177ed8daba10001','구성관리','구성관리 문서 양식 입니다.','form.status.use','0509e09412534a6e98f04ca79abb6424', now());
insert into wf_form values('4028b881787e3e8c01787e5018880000', '릴리즈관리', '릴리즈관리 문서 양식 입니다.', 'form.status.use', '0509e09412534a6e98f04ca79abb6424', now());
insert into wf_form values('4028b25d78870b0901788770dc400024', '인프라 변경관리', '인프라 변경관리 문서양식입니다.', 'form.status.use', '0509e09412534a6e98f04ca79abb6424', now());
insert into wf_form values('4028b25d7888a7f4017888f9e6af0001', '어플리케이션 변경관리', '어플리케이션 변경관리 문서양식입니다.', 'form.status.use', '0509e09412534a6e98f04ca79abb6424', now());
insert into wf_form values('4028b25d788c4f8601788c8b8adc0003', '만족도 - 서비스 요청', '만족도 서비스 요청 양식입니다.', 'form.status.use', '0509e09412534a6e98f04ca79abb6424', now());
insert into wf_form values('4028b25d788c4f8601788c8601f00002', '서비스데크스 - 서비스요청서', '변경관리 요청서 입니다. (인프라변경관리, 어플리케이션 변경관리 프로세스를 진행 할 수 있습니다)', 'form.status.use', '0509e09412534a6e98f04ca79abb6424', now());
/**
 * 프로세스정보
 */
DROP TABLE IF EXISTS wf_process cascade;

CREATE TABLE wf_process
(
	process_id varchar(128) NOT NULL,
	process_name varchar(256) NOT NULL,
	process_status varchar(100) NOT NULL,
	process_desc varchar(256),
	create_user_key varchar(128),
	create_dt timestamp,
	update_user_key varchar(128),
	update_dt timestamp,
	CONSTRAINT wf_process_pk PRIMARY KEY (process_id)
);

COMMENT ON TABLE wf_process IS '프로세스정보';
COMMENT ON COLUMN wf_process.process_id IS '프로세스아이디';
COMMENT ON COLUMN wf_process.process_name IS '프로세스이름';
COMMENT ON COLUMN wf_process.process_status IS '프로세스상태';
COMMENT ON COLUMN wf_process.process_desc IS '프로세스설명';
COMMENT ON COLUMN wf_process.create_user_key IS '생성자';
COMMENT ON COLUMN wf_process.create_dt IS '생성일시';
COMMENT ON COLUMN wf_process.update_user_key IS '수정자';
COMMENT ON COLUMN wf_process.update_dt IS '수정일시';

insert into wf_process values('4028b25d787736640178773e71480002', '만족도 - 단순문의', 'process.status.use', '만족도 단순문의 프로세스입니다.', '0509e09412534a6e98f04ca79abb6424', now());
insert into wf_process values('4028b25d78778da6017877b9df60000f', '서비스데스크 - 단순문의', 'process.status.use', '서비스데스크  단순문의 프로세스 입니다.', '0509e09412534a6e98f04ca79abb6424', now());
insert into wf_process values('40288ab777f04ed90177f057ca410000', '구성관리','process.status.use', '구성관리 프로세스입니다.', '0509e09412534a6e98f04ca79abb6424', now());
insert into wf_process values('40288ab77878ea67017878eb3dc30000', '릴리즈관리', 'process.status.use', '릴리즈관리 프로세스입니다.', '0509e09412534a6e98f04ca79abb6424', now());
insert into wf_process values('4028b25d78870b0901788766663a0023', '인프라변경관리', 'process.status.use', '인프라변경관리 프로세스입니다.', '0509e09412534a6e98f04ca79abb6424', now());
insert into wf_process values('4028b25d7888a7f4017888b1cde90000', '어플리케이션변경관리', 'process.status.use', '어플리케이션변경관리', '0509e09412534a6e98f04ca79abb6424', now());
insert into wf_process values('4028b25d788c4f8601788c905a790004', '만족도 - 서비스요청', 'process.status.use', '만족도 서비스 요청 프로세스입니다.', '0509e09412534a6e98f04ca79abb6424', now());
insert into wf_process values('4028b25d788c4f8601788c7e678a0001', '서비스데스크 - 서비스요청', 'process.status.use', '서비스데스크 - 서비스요청 프로세스입니다.', '0509e09412534a6e98f04ca79abb6424', now());
/**
 * 신청서정보
 */
DROP TABLE IF EXISTS wf_document cascade;

CREATE TABLE wf_document
(
	document_id varchar(128) NOT NULL,
	document_name varchar(256) NOT NULL,
	document_desc varchar(256),
	process_id varchar(128) NOT NULL,
	form_id varchar(128) NOT NULL,
	document_status varchar(100) DEFAULT 'document.status.use',
	numbering_id varchar(128),
	document_color varchar(128),
	document_type varchar(100) NOT NULL,
	document_group varchar(100),
    document_icon varchar(100),
	create_user_key varchar(128),
	create_dt timestamp,
	update_user_key varchar(128),
	update_dt timestamp,
	CONSTRAINT wf_document_pk PRIMARY KEY (document_id),
	CONSTRAINT wf_document_fk1 FOREIGN KEY (process_id) REFERENCES wf_process (process_id),
	CONSTRAINT wf_document_fk2 FOREIGN KEY (form_id) REFERENCES wf_form (form_id),
	CONSTRAINT wf_document_fk3 FOREIGN KEY (numbering_id) REFERENCES awf_numbering_rule (numbering_id)
);

COMMENT ON TABLE wf_document IS '신청서정보';
COMMENT ON COLUMN wf_document.document_id IS '신청서아이디';
COMMENT ON COLUMN wf_document.document_name IS '신청서이름';
COMMENT ON COLUMN wf_document.document_desc IS '신청서설명';
COMMENT ON COLUMN wf_document.process_id IS '프로세스아이디';
COMMENT ON COLUMN wf_document.form_id IS '문서양식아이디';
COMMENT ON COLUMN wf_document.document_status IS '문서상태';
COMMENT ON COLUMN wf_document.numbering_id IS '넘버링아이디';
COMMENT ON COLUMN wf_document.document_color IS '문서색상';
COMMENT ON COLUMN wf_document.document_type IS '문서종류';
COMMENT ON COLUMN wf_document.document_group IS '신청서그룹';
COMMENT ON COLUMN wf_document.document_icon IS '신청서아이콘';
COMMENT ON COLUMN wf_document.create_user_key IS '생성자';
COMMENT ON COLUMN wf_document.create_dt IS '생성일시';
COMMENT ON COLUMN wf_document.update_user_key IS '수정자';
COMMENT ON COLUMN wf_document.update_dt IS '수정일시';

insert into wf_document values ('4028b25d78778da6017877aff7e40001','만족도 - 단순문의','만족도  단순문의 입니다.','4028b25d787736640178773e71480002','4028b25d787736640178773f645b0003','document.status.use','40288ab7772dae0301772dbca28a0004','#586872','workflow','',NULL,'0509e09412534a6e98f04ca79abb6424',now());
insert into wf_document values ('4028b25d78778da6017877bb3d3c0010','단순문의','서비스데스크 단순문의 신청서 입니다.','4028b25d78778da6017877b9df60000f','4028b25d787736640178773857920000','document.status.use','40125c91714df6c325714e053c890125','#F1C40F','application-form','servicedesk.inquiry','img_document_11.png','0509e09412534a6e98f04ca79abb6424',now());
insert into wf_document values ('40288ab777f04ed90177f05f01d1000b','CI 신청서','CI를 등록/수정/삭제를 할 수 있는 구성관리 신청서 입니다.','40288ab777f04ed90177f057ca410000','4028b25d77ed7e6f0177ed8daba10001','document.status.use','40288ab777f04ed90177f05e5ad7000a','#825A2C','application-form','','img_document_06.png','0509e09412534a6e98f04ca79abb6424',now());
insert into wf_document values ('4028b8817880d833017880f5cafc0004', '릴리즈관리', '릴리즈관리 업무흐름입니다.', '40288ab77878ea67017878eb3dc30000', '4028b881787e3e8c01787e5018880000', 'document.status.use', '4028b8817880d833017880f34ae10003', '#9B59B6', 'workflow', '', NULL, '0509e09412534a6e98f04ca79abb6424', now());
insert into wf_document values ('4028b25d78870b0901788772ffe20025','인프라변경관리','인프라변경관리 업무흐름입니다.','4028b25d78870b0901788766663a0023','4028b25d78870b0901788770dc400024','document.status.use','4028b25d7886e2d801788704dd8e0002','#1ABC9C','workflow','',NULL,'0509e09412534a6e98f04ca79abb6424', now());
insert into wf_document values ('4028b25d7888a7f40178893cfe7f0002', '어플리케이션변경관리', '어플리케이션변경관리 업무흐름입니다.', '4028b25d7888a7f4017888b1cde90000', '4028b25d7888a7f4017888f9e6af0001', 'document.status.use', '4028b25d7886e2d801788704dd8e0002', '#3498DB', 'workflow', '', NULL, '0509e09412534a6e98f04ca79abb6424', now());
insert into wf_document values ('4028b25d788c4f8601788c9779b60005', '만족도 - 서비스요청', '만족도 서비스요청 입니다.', '4028b25d788c4f8601788c905a790004', '4028b25d788c4f8601788c8b8adc0003', 'document.status.use', '40288ab7772dae0301772dbca28a0004', '#586872', 'workflow', '', NULL, '0509e09412534a6e98f04ca79abb6424', now());
insert into wf_document values ('4028b25d788c4f8601788ca3a7020006', '서비스 요청', '변경관리(인프라, 어플리케이션) 신청서를 작성 할 수 있습니다.', '4028b25d788c4f8601788c7e678a0001', '4028b25d788c4f8601788c8601f00002', 'document.status.use', '40125c91714df6c325714e053c890125', '#F1C40F', 'application-form', 'servicedesk.request', 'img_document_07.png', '0509e09412534a6e98f04ca79abb6424', now());
/**
 * 인스턴스정보
 */
DROP TABLE IF EXISTS wf_instance cascade;

CREATE TABLE wf_instance
(
	instance_id varchar(128) NOT NULL,
	document_id varchar(128) NOT NULL,
	instance_status varchar(100) NOT NULL,
	instance_start_dt timestamp NOT NULL,
	instance_end_dt timestamp,
	instance_create_user_key varchar(128),
	p_token_id varchar(128),
	document_no varchar(128) NOT NULL,
	CONSTRAINT wf_instance_pk PRIMARY KEY (instance_id),
	CONSTRAINT wf_instance_fk FOREIGN KEY (document_id) REFERENCES wf_document (document_id)
);

COMMENT ON TABLE wf_instance IS '인스턴스정보';
COMMENT ON COLUMN wf_instance.instance_id IS '인스턴스아이디';
COMMENT ON COLUMN wf_instance.document_id IS '신청서아이디';
COMMENT ON COLUMN wf_instance.instance_status IS '인스턴스상태';
COMMENT ON COLUMN wf_instance.instance_start_dt IS '인스턴스시작일시';
COMMENT ON COLUMN wf_instance.instance_end_dt IS '인스턴스종료일시';
COMMENT ON COLUMN wf_instance.instance_create_user_key IS '인스턴스생성자';
COMMENT ON COLUMN wf_instance.p_token_id IS '호출토큰아이디';
COMMENT ON COLUMN wf_instance.document_no IS '문서번호';

/**
 * 의견
 */
DROP TABLE IF EXISTS wf_comment cascade;

CREATE TABLE wf_comment
(
	comment_id varchar(128) NOT NULL,
	instance_id varchar(128) NOT NULL,
	content text,
	create_user_key varchar(128),
	create_dt timestamp,
	CONSTRAINT wf_comment_pk PRIMARY KEY (comment_id),
	CONSTRAINT wf_comment_fk FOREIGN KEY (instance_id) REFERENCES wf_instance (instance_id)
);

COMMENT ON TABLE wf_comment IS '의견';
COMMENT ON COLUMN wf_comment.comment_id IS '의견아이디';
COMMENT ON COLUMN wf_comment.instance_id IS '인스턴스아이디';
COMMENT ON COLUMN wf_comment.content IS '내용';
COMMENT ON COLUMN wf_comment.create_user_key IS '생성자';
COMMENT ON COLUMN wf_comment.create_dt IS '생성일시';

/**
 * 컴포넌트정보
 */
DROP TABLE IF EXISTS wf_component cascade;

CREATE TABLE wf_component
(
	component_id varchar(128) NOT NULL,
	form_id varchar(128) NOT NULL,
	component_type varchar(100) NOT NULL,
	mapping_id varchar(128),
	is_topic boolean DEFAULT 'false',
	CONSTRAINT wf_component_pk PRIMARY KEY (component_id),
	CONSTRAINT wf_component_fk FOREIGN KEY (form_id) REFERENCES wf_form (form_id)
);

COMMENT ON TABLE wf_component IS '컴포넌트정보';
COMMENT ON COLUMN wf_component.component_id IS '컴포넌트아이디';
COMMENT ON COLUMN wf_component.form_id IS '문서양식아이디';
COMMENT ON COLUMN wf_component.component_type IS '컴포넌트종류';
COMMENT ON COLUMN wf_component.mapping_id IS '매핑아이디';
COMMENT ON COLUMN wf_component.is_topic IS '토픽여부';

insert into wf_component values ('53dc0f34890f4c48a7ebd410169623b6','4028b25d787736640178773f645b0003','label','', FALSE);
insert into wf_component values ('caaf208e3d6c4ea0a4ec4d1fa63fc81c','4028b25d787736640178773f645b0003','divider','', FALSE);
insert into wf_component values ('f79d816a6ae748aabc1c58748b65c119','4028b25d787736640178773f645b0003','accordion-start','', FALSE);
insert into wf_component values ('cf96b8a2488c4b84a90002b01f80aeba','4028b25d787736640178773f645b0003','datetime','id1', FALSE);
insert into wf_component values ('aca126bd4f6958bce2cb5e6e607251d0','4028b25d787736640178773f645b0003','custom-code','user', FALSE);
insert into wf_component values ('c088820086ef49488a3033eb84566fa5','4028b25d787736640178773f645b0003','inputbox','id3', FALSE);
insert into wf_component values ('5e6444cb4fad489fa76cacd2b8a26108','4028b25d787736640178773f645b0003','dropdown','id4', FALSE);
insert into wf_component values ('3c592c92c4bc4868a4b585f8c9d9c360','4028b25d787736640178773f645b0003','dropdown','id5', FALSE);
insert into wf_component values ('a6ba20dc101143c1b6bcc21c159cc1dd','4028b25d787736640178773f645b0003','datetime','id6', FALSE);
insert into wf_component values ('ad4c94b7f64a4d01bafef76fbbb444bc','4028b25d787736640178773f645b0003','custom-code','assignee', FALSE);
insert into wf_component values ('ee6e34704b094873926c3918ae00bfbd','4028b25d787736640178773f645b0003','inputbox','id7', TRUE);
insert into wf_component values ('5b8529ddbd40462e824439a753b2c153','4028b25d787736640178773f645b0003','textbox','id8', FALSE);
insert into wf_component values ('74b8c62595f544eaa47d7c8b6ea8560e','4028b25d787736640178773f645b0003','fileupload','id9', FALSE);
insert into wf_component values ('4e1c394c09c84cb2a20b20492f2a3cb0','4028b25d787736640178773f645b0003','accordion-end','', FALSE);
insert into wf_component values ('022540d758ec405ba45d70f8df0c03fc','4028b25d787736640178773f645b0003','accordion-start','', FALSE);
insert into wf_component values ('36b04439545649a79824d742d8d48a61','4028b25d787736640178773f645b0003','datetime','id12', FALSE);
insert into wf_component values ('489d27f3f1eb454780256befc0167cf6','4028b25d787736640178773f645b0003','textbox','id13', FALSE);
insert into wf_component values ('e5f66323f50b498189d7ebccfaf3c4a9','4028b25d787736640178773f645b0003','fileupload','id14', FALSE);
insert into wf_component values ('27e376940afc4568aa228f8663318ea0','4028b25d787736640178773f645b0003','accordion-end','', FALSE);
insert into wf_component values ('357f6d029a3d420d9fefba4e5a6ca9df','4028b25d787736640178773f645b0003','accordion-start','', FALSE);
insert into wf_component values ('b28cb8265d134217b7336704c3554314','4028b25d787736640178773f645b0003','radio','id15', FALSE);
insert into wf_component values ('4ba00478892f4195a10597b3090d887c','4028b25d787736640178773f645b0003','textbox','', FALSE);
insert into wf_component values ('974aa952322d4800ab68d7efaa5c921f','4028b25d787736640178773f645b0003','fileupload','id17', FALSE);
insert into wf_component values ('130111f086244dc393963a75a480631e','4028b25d787736640178773f645b0003','accordion-end','', FALSE);
insert into wf_component values ('e4a75d472ad94be0abc41b7be5c60a06','4028b25d787736640178773857920000','label','',FALSE);
insert into wf_component values ('29fc1c91ed9145c2ab4569db715ea461','4028b25d787736640178773857920000','divider','',FALSE);
insert into wf_component values ('6b20477610204c32ac7460143405299f','4028b25d787736640178773857920000','accordion-start','',FALSE);
insert into wf_component values ('763a02453da34238a611908d427b03a4','4028b25d787736640178773857920000','datetime','id1',FALSE);
insert into wf_component values ('a64a8b2b177f51b09134faf7d148a4bb','4028b25d787736640178773857920000','custom-code','user',FALSE);
insert into wf_component values ('189aafb3a9404ec489607fef8b9f8560','4028b25d787736640178773857920000','inputbox','id3',FALSE);
insert into wf_component values ('80060ae8be4b42a9883bc7b7dd96632b','4028b25d787736640178773857920000','dropdown','id4',FALSE);
insert into wf_component values ('a02d6fbdc20e46b08c95453f6d25c311','4028b25d787736640178773857920000','dropdown','id5',FALSE);
insert into wf_component values ('0177a25c92ad43aebd50bfc45d32fc02','4028b25d787736640178773857920000','datetime','id6',FALSE);
insert into wf_component values ('303f8e1f7e814953a759ff93ee336f65','4028b25d787736640178773857920000','custom-code','assignee',FALSE);
insert into wf_component values ('8fe7d5f6c16a49788420fe648a9a4cb2','4028b25d787736640178773857920000','inputbox','id7',true);
insert into wf_component values ('63c2bbf75cb9456a9de83c2f668ad181','4028b25d787736640178773857920000','textbox','id8',FALSE);
insert into wf_component values ('cec8377af21641e1a2a274db6654aa88','4028b25d787736640178773857920000','fileupload','id9',FALSE);
insert into wf_component values ('de47121e6034445598ff4c216168781e','4028b25d787736640178773857920000','accordion-end','',FALSE);
insert into wf_component values ('7b53c1640c1640ca889f6e44ac92a256','4028b25d787736640178773857920000','accordion-start','',FALSE);
insert into wf_component values ('2b2f880adf2f434c8b71d57f3439129e','4028b25d787736640178773857920000','datetime','id10',FALSE);
insert into wf_component values ('568e2d7388614f349756a9d55f9fbf82','4028b25d787736640178773857920000','textbox','id11',FALSE);
insert into wf_component values ('fc2d992990704ad7be4eb8fb52839a0b','4028b25d787736640178773857920000','accordion-end','',FALSE);
insert into wf_component values ('cf757877eb04473492014366b49d7aef','4028b25d787736640178773857920000','accordion-start','',FALSE);
insert into wf_component values ('4601ce00de0a463098f4d4c1a020028f','4028b25d787736640178773857920000','datetime','id12',FALSE);
insert into wf_component values ('c2a95ab676214ffa82571c8d24047f1a','4028b25d787736640178773857920000','textbox','id13',FALSE);
insert into wf_component values ('c57c3c17b8b34783ba385fa33d96570c','4028b25d787736640178773857920000','fileupload','id14',FALSE);
insert into wf_component values ('f2bf25aa9b87468ba02398838d110d9d','4028b25d787736640178773857920000','custom-code','assignee2',FALSE);
insert into wf_component values ('aec9c3f0a2cc456c9870e7cda1b6980a','4028b25d787736640178773857920000','accordion-end','',FALSE);
insert into wf_component values ('f4639d8fee6a4796baf464b8f55105e5','4028b25d787736640178773857920000','accordion-start','',FALSE);
insert into wf_component values ('9cfaa92789b64c7da46c205995416821','4028b25d787736640178773857920000','datetime','id15',FALSE);
insert into wf_component values ('05df4c3c4c2641f082b8cb181327511d','4028b25d787736640178773857920000','textbox','id16',FALSE);
insert into wf_component values ('50871faf2bf74d2b8198b0049c9eab72','4028b25d787736640178773857920000','accordion-end','',FALSE);
insert into wf_component values ('a0438d6ebeb3e76391de4ca2591b891d','4028b25d77ed7e6f0177ed8daba10001','dropdown','',FALSE);
insert into wf_component values ('a083bcc9a0679f6bc9cde83eb4205161','4028b25d77ed7e6f0177ed8daba10001','accordion-end','',FALSE);
insert into wf_component values ('a15a240317ee972e96532bf01836fd5c','4028b25d77ed7e6f0177ed8daba10001','custom-code','assignee0',FALSE);
insert into wf_component values ('a1709377086e44e4e319ba3c2aec93c4','4028b25d77ed7e6f0177ed8daba10001','ci','cmdb',FALSE);
insert into wf_component values ('a1bbab51ba6b903eea7834b64ff2018e','4028b25d77ed7e6f0177ed8daba10001','textbox','',FALSE);
insert into wf_component values ('a20306ee9477e23c3b460bdb459f5f89','4028b25d77ed7e6f0177ed8daba10001','dropdown','',FALSE);
insert into wf_component values ('a293e254d00208aeb1afb8d78c0d0737','4028b25d77ed7e6f0177ed8daba10001','datetime','',FALSE);
insert into wf_component values ('a2ef0bbeda3884c8111f82abeb242d6f','4028b25d77ed7e6f0177ed8daba10001','accordion-start','',FALSE);
insert into wf_component values ('a40b7f5aaa39c16cf44d8db83f3f2a5b','4028b25d77ed7e6f0177ed8daba10001','fileupload','',FALSE);
insert into wf_component values ('a4c6a3dd66d0cc36d108ff8cc6904fd7','4028b25d77ed7e6f0177ed8daba10001','textbox','',FALSE);
insert into wf_component values ('a563dbfc8286f3ed75f47b8e778390fb','4028b25d77ed7e6f0177ed8daba10001','fileupload','',FALSE);
insert into wf_component values ('a640301d7e67dc92c279f83bec7358be','4028b25d77ed7e6f0177ed8daba10001','datetime','',FALSE);
insert into wf_component values ('a661cc2ef029e637a72124a4b7c5f32f','4028b25d77ed7e6f0177ed8daba10001','datetime','',FALSE);
insert into wf_component values ('a7bc03e8abd70100a86e43338e308054','4028b25d77ed7e6f0177ed8daba10001','custom-code','assignee1',FALSE);
insert into wf_component values ('a7fb73ce3c61d3d600ab135d6a6a8684','4028b25d77ed7e6f0177ed8daba10001','textbox','',FALSE);
insert into wf_component values ('a82c5acd1b0ec0bba083d76135d39e5a','4028b25d77ed7e6f0177ed8daba10001','accordion-start','',FALSE);
insert into wf_component values ('a87535522638ab7240e972d44d66ef49','4028b25d77ed7e6f0177ed8daba10001','accordion-end','',FALSE);
insert into wf_component values ('a90620f766dedb2beebbe776eff556ec','4028b25d77ed7e6f0177ed8daba10001','datetime','',FALSE);
insert into wf_component values ('a9611c745a6716665c5e7690b872310b','4028b25d77ed7e6f0177ed8daba10001','custom-code','assignee2',FALSE);
insert into wf_component values ('a9b580dd25da55ce3ce347bcef0169d5','4028b25d77ed7e6f0177ed8daba10001','divider','',FALSE);
insert into wf_component values ('a9ef096c157fab2ec319efcf467b568a','4028b25d77ed7e6f0177ed8daba10001','inputbox','',TRUE);
insert into wf_component values ('aa54cd88560b84b902653fd90336ee1e','4028b25d77ed7e6f0177ed8daba10001','accordion-start','',FALSE);
insert into wf_component values ('aa5a75f1766bc691fa0c066202c776ae','4028b25d77ed7e6f0177ed8daba10001','textbox','',FALSE);
insert into wf_component values ('abc34c5409954d3c792f2209f3347cdf','4028b25d77ed7e6f0177ed8daba10001','accordion-end','',FALSE);
insert into wf_component values ('ad5e4623a663879158834cbdddd032e5','4028b25d77ed7e6f0177ed8daba10001','accordion-end','',FALSE);
insert into wf_component values ('ad6afd860d2cda7a78bf93f4e23c8efd','4028b25d77ed7e6f0177ed8daba10001','label','',FALSE);
insert into wf_component values ('ae615f2ac0d145ef25d5e7253c12940d','4028b25d77ed7e6f0177ed8daba10001','accordion-start','',FALSE);
insert into wf_component values ('aff3e77073f75d2385e2347ed996f412','4028b25d77ed7e6f0177ed8daba10001','datetime','',FALSE);
insert into wf_component values('a30a607c9b0d1fd73b1944aad77c4c04','4028b881787e3e8c01787e5018880000','label','', FALSE);
insert into wf_component values('a8898c594ccf9b411f92b6f27d90dd02','4028b881787e3e8c01787e5018880000','divider','', FALSE);
insert into wf_component values('af3a26739df10f18ac585d2a4e6cf448','4028b881787e3e8c01787e5018880000','accordion-start','', FALSE);
insert into wf_component values('a2d59c6c2668e4124c2883bf31d02ae2','4028b881787e3e8c01787e5018880000','datetime','rel_req_dt', FALSE);
insert into wf_component values('a918ef651427c247593817d91ae15c7a','4028b881787e3e8c01787e5018880000','custom-code','rel_req_user', FALSE);
insert into wf_component values('a826936604aa74a3744c0c726666b2e0','4028b881787e3e8c01787e5018880000','dropdown','rel_service', FALSE);
insert into wf_component values('a6badd9de24643c8b48cc10cd15c65c5','4028b881787e3e8c01787e5018880000','dropdown','rel_impact', FALSE);
insert into wf_component values('abf089973153fbd0a7a0fbec451bd000','4028b881787e3e8c01787e5018880000','dropdown','rel_emergency', FALSE);
insert into wf_component values('a840a2f8e71e9098f432b31148be5779','4028b881787e3e8c01787e5018880000','radio','rel_service_stop', FALSE);
insert into wf_component values('ab336835f267f16eb2e9a765c9f3d562','4028b881787e3e8c01787e5018880000','datetime','rel_schedule_start', FALSE);
insert into wf_component values('a457cdd3533cd9c491fc5f60f117ff59','4028b881787e3e8c01787e5018880000','datetime','rel_schedule_end', FALSE);
insert into wf_component values('a929ede9bc5e1c9b55e188afa2029798','4028b881787e3e8c01787e5018880000','inputbox','rel_title', true);
insert into wf_component values('a4fcf5278decc41890827790aa95380e','4028b881787e3e8c01787e5018880000','accordion-end','', FALSE);
insert into wf_component values('a93f8a596c3ffa3ad6265bd36b923dc0','4028b881787e3e8c01787e5018880000','accordion-start','', FALSE);
insert into wf_component values('a4edc285df4003747ec99b7d0d9e6a42','4028b881787e3e8c01787e5018880000','datetime','rel_accept_dt', FALSE);
insert into wf_component values('a04e548325ed8ae31a385a702f69ea10','4028b881787e3e8c01787e5018880000','custom-code','rel_manager', FALSE);
insert into wf_component values('a755edd239869a2fc974151f1e7144fe','4028b881787e3e8c01787e5018880000','textbox','rel_accept_opinion', FALSE);
insert into wf_component values('a7cdb1c39349387fcf58359210e696ea','4028b881787e3e8c01787e5018880000','accordion-end','', FALSE);
insert into wf_component values('a1241ec6aabdb13fe23fd435b3fbbb0b','4028b881787e3e8c01787e5018880000','accordion-start','', FALSE);
insert into wf_component values('a75e73ba2ee7b2298b704c4f56cb65e4','4028b881787e3e8c01787e5018880000','textbox','rel_notification', FALSE);
insert into wf_component values('ad05da15bfa32cd50cdd3e778ae0f250','4028b881787e3e8c01787e5018880000','textbox','rel_fail_plan', FALSE);
insert into wf_component values('aa273932115bd7d96b008e12330d8570','4028b881787e3e8c01787e5018880000','textbox','rel_test_plan', FALSE);
insert into wf_component values('abdd7d23019680a27eefafa153cd2f5c','4028b881787e3e8c01787e5018880000','textbox','rel_review', FALSE);
insert into wf_component values('a042929dd4bbca295a5e14f7c3479630','4028b881787e3e8c01787e5018880000','dynamic-row-table','rel_detail_plan', FALSE);
insert into wf_component values('a5ea3ab70d3dbfe3a9203182eec97d99','4028b881787e3e8c01787e5018880000','textbox','rel_prepare_job', FALSE);
insert into wf_component values('a1019e08196f6803f8573d04348783b2','4028b881787e3e8c01787e5018880000','textbox','rel_detail_job', FALSE);
insert into wf_component values('acc1f172a41984869823f4495e441ab0','4028b881787e3e8c01787e5018880000','textbox','rel_main_dept', FALSE);
insert into wf_component values('a83ca580daa743c23e422b952ca035a8','4028b881787e3e8c01787e5018880000','textbox','rel_prepare_job_plan', FALSE);
insert into wf_component values('af7313650122e67929324cf6c85a0ccd','4028b881787e3e8c01787e5018880000','fileupload','rel_before_file', FALSE);
insert into wf_component values('ac1c201dc1018243e0aa3bcc7b6fc60a','4028b881787e3e8c01787e5018880000','accordion-end','', FALSE);
insert into wf_component values('a5decb84258850cbb3ca9b5789ddb755','4028b881787e3e8c01787e5018880000','accordion-start','', FALSE);
insert into wf_component values('abcf0ce4f3b0265e4d233ec373b66cb8','4028b881787e3e8c01787e5018880000','textbox','rel_req_opinion', FALSE);
insert into wf_component values('a7802771c99c12360b754662e3ff8955','4028b881787e3e8c01787e5018880000','custom-code','rel_approver', FALSE);
insert into wf_component values('ad66a326f401e3f3bdc469b363f91822','4028b881787e3e8c01787e5018880000','accordion-end','', FALSE);
insert into wf_component values('a710e227eb81b4a36da4d555e97a5096','4028b881787e3e8c01787e5018880000','accordion-start','', FALSE);
insert into wf_component values('a11e0b9db990bab2241122481f268de4','4028b881787e3e8c01787e5018880000','datetime','rel_approval_dt', FALSE);
insert into wf_component values('a910bacf282f3590922cd9670be1d215','4028b881787e3e8c01787e5018880000','textbox','rel_approval_opinion', FALSE);
insert into wf_component values('a8d276ead25d55cb2786bf28a72faab0','4028b881787e3e8c01787e5018880000','accordion-end','', FALSE);
insert into wf_component values('a87520f43ca016202b01d899a8866f53','4028b881787e3e8c01787e5018880000','accordion-start','', FALSE);
insert into wf_component values('af750337e7d5560447d6a47157d75b68','4028b881787e3e8c01787e5018880000','dynamic-row-table','rel_detail_result', FALSE);
insert into wf_component values('a8a24ba85095c550505962b36d98c3be','4028b881787e3e8c01787e5018880000','accordion-end','', FALSE);
insert into wf_component values('a251ce5051063fbf6aba8b1f4a4d7d94','4028b881787e3e8c01787e5018880000','accordion-start','', FALSE);
insert into wf_component values('ad60787db1a601ea12f17b15ae6daf7b','4028b881787e3e8c01787e5018880000','datetime','rel_work_start_dt', FALSE);
insert into wf_component values('a049fd4ac6c927ccde29ae160ee5d846','4028b881787e3e8c01787e5018880000','datetime','rel_work_end_dt', FALSE);
insert into wf_component values('af082fb00f2e71c2a27dcc88874d3f57','4028b881787e3e8c01787e5018880000','inputbox','rel_related_dept', FALSE);
insert into wf_component values('acf974f8c63805a90a194b0dd7665129','4028b881787e3e8c01787e5018880000','inputbox','rel_cooperative_company', FALSE);
insert into wf_component values('a99fe87fb279b045646027a0a14b8930','4028b881787e3e8c01787e5018880000','radio','rel_result', FALSE);
insert into wf_component values('ad40cc3d789c45285bbfe6c3c2e1bcd7','4028b881787e3e8c01787e5018880000','radio','rel_service_effect', FALSE);
insert into wf_component values('a96d3f6b33923e6c45471970d5d235ca','4028b881787e3e8c01787e5018880000','radio','rel_monitor', FALSE);
insert into wf_component values('af325a2f3a2f56413461687d7cba8f20','4028b881787e3e8c01787e5018880000','textbox','rel_result_content', FALSE);
insert into wf_component values('a628a084f571bac3f6f51e333e3a8884','4028b881787e3e8c01787e5018880000','fileupload','rel_result_file', FALSE);
insert into wf_component values('a3452770c3511a852cc1c862d5f787b4','4028b881787e3e8c01787e5018880000','accordion-end','', FALSE);
insert into wf_component values ('68369e91d73748c295be7c942b2a228d', '4028b25d78870b0901788770dc400024', 'label', '', FALSE);
insert into wf_component values ('0da02528c3574a5a90047944fc99b103', '4028b25d78870b0901788770dc400024', 'divider', '', FALSE);
insert into wf_component values ('81a12988c7894037b6ffeae5fd024133', '4028b25d78870b0901788770dc400024', 'accordion-start', '', FALSE);
insert into wf_component values ('b95569d56dba467a9ebf9ac0d8ca9e09', '4028b25d78870b0901788770dc400024', 'datetime', 'sd_req_chg_dt', FALSE);
insert into wf_component values ('46d4314b543c40f9845a87b3ee6db3d9', '4028b25d78870b0901788770dc400024', 'custom-code', 'sd_manager_user', FALSE);
insert into wf_component values ('4b499d11338341fe94f0fdb48f97533e', '4028b25d78870b0901788770dc400024', 'inputbox', 'sd_manager_user_dept', FALSE);
insert into wf_component values ('d1cedc4c71824025b77a73aab7e6859b', '4028b25d78870b0901788770dc400024', 'dropdown', 'rel_service', FALSE);
insert into wf_component values ('b7cb55ee46f047c7a11d2bfef92551de', '4028b25d78870b0901788770dc400024', 'datetime', 'sd_finish_dt', FALSE);
insert into wf_component values ('4c55f52ad993410dad36868802ca81b8', '4028b25d78870b0901788770dc400024', 'custom-code', 'rel_req_user', FALSE);
insert into wf_component values ('f7f6fac98fed4c539b9535dabea7f03d', '4028b25d78870b0901788770dc400024', 'inputbox', 'rel_title', true);
insert into wf_component values ('78a67f7784984dc5b503424a4253b195', '4028b25d78870b0901788770dc400024', 'textbox', 'sd_req_cmt', FALSE);
insert into wf_component values ('d60b8b4468874dbab765fcca3bbbcb2f', '4028b25d78870b0901788770dc400024', 'ci', 'chg_req_ci', FALSE);
insert into wf_component values ('4e4059b95b3040079aa476af9bf7c7c6', '4028b25d78870b0901788770dc400024', 'fileupload', 'chg_req_attach_file', FALSE);
insert into wf_component values ('e8c4685f990c4a39ac51e2d776d8158e', '4028b25d78870b0901788770dc400024', 'accordion-end', '', FALSE);
insert into wf_component values ('70b7b53f0038466aa66373e7e62c464b', '4028b25d78870b0901788770dc400024', 'accordion-start', '', FALSE);
insert into wf_component values ('6afdcd6948bc40639c26a037f3844a06', '4028b25d78870b0901788770dc400024', 'datetime', 'chg_accept_dt', FALSE);
insert into wf_component values ('126d1bc226764910863a2db5198fa3a1', '4028b25d78870b0901788770dc400024', 'dropdown', 'rel_impact', FALSE);
insert into wf_component values ('a65ec7e28d5548ab8948c9b45242ad92', '4028b25d78870b0901788770dc400024', 'dropdown', 'rel_emergency', FALSE);
insert into wf_component values ('e8a4310cfc1d4abdb50677ebd19fde93', '4028b25d78870b0901788770dc400024', 'textbox', 'chg_accept_cmt', FALSE);
insert into wf_component values ('1fabe796629a4a1fbb566ee0fe13f8bd', '4028b25d78870b0901788770dc400024', 'accordion-end', '', FALSE);
insert into wf_component values ('8135a66efebc4afa83790369d27fe803', '4028b25d78870b0901788770dc400024', 'accordion-start', '', FALSE);
insert into wf_component values ('77bdc771389f43b8b10a0b5b8bb0466c', '4028b25d78870b0901788770dc400024', 'datetime', 'rel_req_dt', FALSE);
insert into wf_component values ('4168f17234ca464a9e53c31b47cf2c26', '4028b25d78870b0901788770dc400024', 'custom-code', 'rel_manager', FALSE);
insert into wf_component values ('f9a79b39fac041f8979eb394e6d17b0d', '4028b25d78870b0901788770dc400024', 'datetime', 'rel_schedule_start', FALSE);
insert into wf_component values ('70082734af8a49efb7f23c3be21af0d4', '4028b25d78870b0901788770dc400024', 'datetime', 'rel_schedule_end', FALSE);
insert into wf_component values ('b845c1bbc48443bfab1f556d9b5d2f96', '4028b25d78870b0901788770dc400024', 'radio', 'rel_service_stop', FALSE);
insert into wf_component values ('fb9736fc3e364a2bbde711d7baa4c86a', '4028b25d78870b0901788770dc400024', 'textbox', 'chg_job_plan', FALSE);
insert into wf_component values ('88adaa894c034517a6b8e5faca94aee1', '4028b25d78870b0901788770dc400024', 'textbox', 'chg_fail_plan', FALSE);
insert into wf_component values ('ad9bbf828a0640c1870f517830298fc9', '4028b25d78870b0901788770dc400024', 'textbox', 'chg_test_plan', FALSE);
insert into wf_component values ('2567d59617ff4c3abb9c8df6dc021de9', '4028b25d78870b0901788770dc400024', 'textbox', 'chg_etc_cmt', FALSE);
insert into wf_component values ('f5e9c0f86f0b43d887978787f78aaffe', '4028b25d78870b0901788770dc400024', 'ci', 'chg_plan_ci', FALSE);
insert into wf_component values ('1d10da32376d48a4aeec5bdd5855064d', '4028b25d78870b0901788770dc400024', 'fileupload', 'chg_plan_attach_file', FALSE);
insert into wf_component values ('566d27971d424f6ebcac7e906af5f522', '4028b25d78870b0901788770dc400024', 'accordion-end', '', FALSE);
insert into wf_component values ('3b863fc7f81149c5a7fdbc3c3fb17864', '4028b25d78870b0901788770dc400024', 'accordion-start', '', FALSE);
insert into wf_component values ('6e14fc72a91c454585d95d7a5b1d6740', '4028b25d78870b0901788770dc400024', 'inputbox', 'cab_meet_place', FALSE);
insert into wf_component values ('1e83c938aa794f7d9ac0b4be129fa8b8', '4028b25d78870b0901788770dc400024', 'datetime', 'cab_meet_dt', FALSE);
insert into wf_component values ('7e8c9bc421ab4822bf51f435ee27f07c', '4028b25d78870b0901788770dc400024', 'inputbox', 'cab_accept_user', FALSE);
insert into wf_component values ('a70d29a292f24b1d91db3f0af886808c', '4028b25d78870b0901788770dc400024', 'inputbox', 'cab_title', FALSE);
insert into wf_component values ('16efb22300754fa8912c4ae96f7d880d', '4028b25d78870b0901788770dc400024', 'textbox', 'cab_meet_content', FALSE);
insert into wf_component values ('a63b6c2b2db2430984cabe3f9664f65e', '4028b25d78870b0901788770dc400024', 'textbox', 'cab_meet_result', FALSE);
insert into wf_component values ('6bea8026826443818b96302898b59eb0', '4028b25d78870b0901788770dc400024', 'accordion-end', '', FALSE);
insert into wf_component values ('72e1568e821049e3a559bfcfa8608c5f', '4028b25d78870b0901788770dc400024', 'accordion-start', '', FALSE);
insert into wf_component values ('df358ee2bb18415da12d61b7623bc00f', '4028b25d78870b0901788770dc400024', 'textbox', 'chg_job_plan_cmt', FALSE);
insert into wf_component values ('2f47ce3e9f4541c6b597af1f1c894114', '4028b25d78870b0901788770dc400024', 'custom-code', 'chg_job_plan_approver', FALSE);
insert into wf_component values ('8b2ba2fdc8804624836a93800b96f1c0', '4028b25d78870b0901788770dc400024', 'accordion-end', '', FALSE);
insert into wf_component values ('5f853fd6493a440bb87bad6608c3cfa0', '4028b25d78870b0901788770dc400024', 'accordion-start', '', FALSE);
insert into wf_component values ('bd131acab7844bbc86daddfccd0b9424', '4028b25d78870b0901788770dc400024', 'datetime', 'chg_job_plan_approval_dt', FALSE);
insert into wf_component values ('cb31c114117944bbba596e01bd4a2baf', '4028b25d78870b0901788770dc400024', 'textbox', 'chg_job_plan_approval_opinion', FALSE);
insert into wf_component values ('adbd729b6ec447769e11cbdaee0a90bf', '4028b25d78870b0901788770dc400024', 'accordion-end', '', FALSE);
insert into wf_component values ('493a61cf4eec43ce93799ab060a8e31a', '4028b25d78870b0901788770dc400024', 'accordion-start', '', FALSE);
insert into wf_component values ('a434c0324f3d4f96ae77fc35d3386b23', '4028b25d78870b0901788770dc400024', 'radio', 'chg_result', FALSE);
insert into wf_component values ('be386a19c49b4aeb8c5aedc0313a9df5', '4028b25d78870b0901788770dc400024', 'textbox', 'chg_result_cmt', FALSE);
insert into wf_component values ('adeb5fc671fd411d899bcd5f815ada53', '4028b25d78870b0901788770dc400024', 'fileupload', 'chg_result_attach_file', FALSE);
insert into wf_component values ('2b93c546bf814c908a92a40316ebf0fb', '4028b25d78870b0901788770dc400024', 'accordion-end', '', FALSE);
insert into wf_component values ('adcf595a433043c1bb41f0d5fd649504', '4028b25d78870b0901788770dc400024', 'accordion-start', '', FALSE);
insert into wf_component values ('bf83695a89684561bb21bb0f8b22a619', '4028b25d78870b0901788770dc400024', 'textbox', 'chg_result_cmt', FALSE);
insert into wf_component values ('2e5f623b4b244f21b7230364252acc05', '4028b25d78870b0901788770dc400024', 'custom-code', 'chg_result_approver', FALSE);
insert into wf_component values ('52fb168ce6db474b8507e77144e256b2', '4028b25d78870b0901788770dc400024', 'accordion-end', '', FALSE);
insert into wf_component values ('fc4c35ef2332453ba18cc39bef378b26', '4028b25d78870b0901788770dc400024', 'accordion-start', '', FALSE);
insert into wf_component values ('2d805698376849bfb4d0e28b389477df', '4028b25d78870b0901788770dc400024', 'datetime', 'chg_result_approval_dt', FALSE);
insert into wf_component values ('215cada954bf44e1b0f03f02e44c63db', '4028b25d78870b0901788770dc400024', 'textbox', 'chg_result_approval_opinion', FALSE);
insert into wf_component values ('6b7e7e14001b42d9957cfd1e3aeca64a', '4028b25d78870b0901788770dc400024', 'accordion-end', '', FALSE);
insert into wf_component values ('863aab52c69b48f3ad40fa378ed1f136', '4028b25d7888a7f4017888f9e6af0001', 'label', '', FALSE);
insert into wf_component values ('a8af055054d14330bc32ebde17bca561', '4028b25d7888a7f4017888f9e6af0001', 'divider', '', FALSE);
insert into wf_component values ('f945571183c5475591ee2abe96db657a', '4028b25d7888a7f4017888f9e6af0001', 'accordion-start', '', FALSE);
insert into wf_component values ('5619598930384c76bbfd12f246a7ed2d', '4028b25d7888a7f4017888f9e6af0001', 'datetime', 'sd_req_chg_dt', FALSE);
insert into wf_component values ('ab56589b5c154ee9829054081516f211', '4028b25d7888a7f4017888f9e6af0001', 'custom-code', 'sd_manager_user', FALSE);
insert into wf_component values ('c89f663fad094c749810e6e268645bae', '4028b25d7888a7f4017888f9e6af0001', 'inputbox', 'sd_manager_user_dept', FALSE);
insert into wf_component values ('d4fa12ff8a7a462992865e419bffcd93', '4028b25d7888a7f4017888f9e6af0001', 'dropdown', 'rel_service', FALSE);
insert into wf_component values ('d49d626d97ae47938928bdf2162bd54a', '4028b25d7888a7f4017888f9e6af0001', 'datetime', 'sd_finish_dt', FALSE);
insert into wf_component values ('8cbae4ebe4e34e0199dc0f7e521bb8b3', '4028b25d7888a7f4017888f9e6af0001', 'custom-code', 'rel_req_user', FALSE);
insert into wf_component values ('06aa9e655148405ea88fc65d1af2f07d', '4028b25d7888a7f4017888f9e6af0001', 'inputbox', 'rel_title', true);
insert into wf_component values ('8face0b52c974369bfc2d4f3daac42c3', '4028b25d7888a7f4017888f9e6af0001', 'textbox', 'sd_req_cmt', FALSE);
insert into wf_component values ('5aa90376561f44c7b23474f80a3b9bb3', '4028b25d7888a7f4017888f9e6af0001', 'ci', 'chg_req_ci', FALSE);
insert into wf_component values ('fdb3b5aec66d4d0286aa39d5306905f1', '4028b25d7888a7f4017888f9e6af0001', 'fileupload', 'chg_req_attach_file', FALSE);
insert into wf_component values ('132843274c11498abf2d384d0b7cd778', '4028b25d7888a7f4017888f9e6af0001', 'accordion-end', '', FALSE);
insert into wf_component values ('c72bc134e9b94e6aadb9c043a4e51e26', '4028b25d7888a7f4017888f9e6af0001', 'accordion-start', '', FALSE);
insert into wf_component values ('58f8ef1520d8432783c849060943bd7d', '4028b25d7888a7f4017888f9e6af0001', 'datetime', 'cha_accept_dt', FALSE);
insert into wf_component values ('d3e9b29dd46c4e0b9fb5993534de4f2a', '4028b25d7888a7f4017888f9e6af0001', 'dropdown', 'rel_impact', FALSE);
insert into wf_component values ('3defaf1ea63c4ad5a46572da0497bff7', '4028b25d7888a7f4017888f9e6af0001', 'dropdown', 'rel_emergency', FALSE);
insert into wf_component values ('dca5ef1fac3c42bab5407f3211857f6e', '4028b25d7888a7f4017888f9e6af0001', 'textbox', 'cha_accept_cmt', FALSE);
insert into wf_component values ('fc5df21295ff456e81044a0c4f6e2678', '4028b25d7888a7f4017888f9e6af0001', 'accordion-end', '', FALSE);
insert into wf_component values ('893d7bf6289e49c4b02542b33faedeec', '4028b25d7888a7f4017888f9e6af0001', 'accordion-start', '', FALSE);
insert into wf_component values ('4c50c453a14d4df6b07d2cb5b83f6353', '4028b25d7888a7f4017888f9e6af0001', 'datetime', 'rel_req_dt', FALSE);
insert into wf_component values ('636bc689c20440489786d068719c0b8a', '4028b25d7888a7f4017888f9e6af0001', 'custom-code', 'rel_manager', FALSE);
insert into wf_component values ('4a28390d2e4c48b9b55c13048f584910', '4028b25d7888a7f4017888f9e6af0001', 'datetime', 'rel_schedule_start', FALSE);
insert into wf_component values ('dbaa5d0552dd489390a5c58883f9637b', '4028b25d7888a7f4017888f9e6af0001', 'datetime', 'rel_schedule_end', FALSE);
insert into wf_component values ('03ef421c8aff438fabd8802c27c3f9a0', '4028b25d7888a7f4017888f9e6af0001', 'radio', 'rel_service_stop', FALSE);
insert into wf_component values ('a1a5b7e4cb47a9bcd8c2683ab5ea2670', '4028b25d7888a7f4017888f9e6af0001', 'textbox', 'cha_req_analysis_cmt', FALSE);
insert into wf_component values ('a141173392185a23c919f2efed84a9ab', '4028b25d7888a7f4017888f9e6af0001', 'dynamic-row-table', 'cha_dev_schedule', FALSE);
insert into wf_component values ('a3dd667185aa125018e81cdf90493e44', '4028b25d7888a7f4017888f9e6af0001', 'custom-code', 'cha_analysis_manger', FALSE);
insert into wf_component values ('ad0227ad0079b5dca85f452fe33b807d', '4028b25d7888a7f4017888f9e6af0001', 'custom-code', 'cha_implement_manager', FALSE);
insert into wf_component values ('abeee66f95e85ae97e684ee0b56da431', '4028b25d7888a7f4017888f9e6af0001', 'custom-code', 'cha_tester_manager', FALSE);
insert into wf_component values ('a26001b80809c72b05238532ff989e51', '4028b25d7888a7f4017888f9e6af0001', 'custom-code', 'sd_manager', FALSE);
insert into wf_component values ('a95e849bce093c1cbea7a887c8a750fe', '4028b25d7888a7f4017888f9e6af0001', 'dynamic-row-table', 'cha_resource_info', FALSE);
insert into wf_component values ('9793a1313c6046a281253143f24d64ac', '4028b25d7888a7f4017888f9e6af0001', 'ci', 'cha_plan_ci', FALSE);
insert into wf_component values ('30d12a1db5b94969b349d7ea24d574c5', '4028b25d7888a7f4017888f9e6af0001', 'fileupload', 'cha_plan_attach_file', FALSE);
insert into wf_component values ('5e462c62e5bb44dcae86fa61e939198f', '4028b25d7888a7f4017888f9e6af0001', 'accordion-end', '', FALSE);
insert into wf_component values ('f8ffe25354e74196a90dfef3652a3e7d', '4028b25d7888a7f4017888f9e6af0001', 'accordion-start', '', FALSE);
insert into wf_component values ('639243d7d02a446e8d383ec3d452ffa3', '4028b25d7888a7f4017888f9e6af0001', 'textbox', 'cha_job_plan_cmt', FALSE);
insert into wf_component values ('2d872066d25f4681b31a77bf6495ab12', '4028b25d7888a7f4017888f9e6af0001', 'custom-code', 'cha_job_plan_approver', FALSE);
insert into wf_component values ('44eb35f7b3904d928dc502430e98f665', '4028b25d7888a7f4017888f9e6af0001', 'accordion-end', '', FALSE);
insert into wf_component values ('a5553eb7480f43c29f971b0e93804a02', '4028b25d7888a7f4017888f9e6af0001', 'accordion-start', '', FALSE);
insert into wf_component values ('2cb8e92e4da8405d9e9363d651051ed0', '4028b25d7888a7f4017888f9e6af0001', 'datetime', 'cha_job_plan_approval_dt', FALSE);
insert into wf_component values ('c8dc1e3013c941dda44aee2ad8ff5bff', '4028b25d7888a7f4017888f9e6af0001', 'textbox', 'cha_job_plan_approval_opinion', FALSE);
insert into wf_component values ('c5e47b036bf54e64b7c974a7770034ad', '4028b25d7888a7f4017888f9e6af0001', 'accordion-end', '', FALSE);
insert into wf_component values ('aa0e47d9df139ef6a0a30c6cbf1a20f0', '4028b25d7888a7f4017888f9e6af0001', 'accordion-start', '', FALSE);
insert into wf_component values ('a9a5213c5d2ff5d04a93f5d22b214c0a', '4028b25d7888a7f4017888f9e6af0001', 'datetime', 'cha_analysis_dt', FALSE);
insert into wf_component values ('acacad22fad99755cd0668c7eb00040d', '4028b25d7888a7f4017888f9e6af0001', 'textbox', 'cha_analysis_cmt', FALSE);
insert into wf_component values ('aba545803a0b342007bb193abbb414e5', '4028b25d7888a7f4017888f9e6af0001', 'fileupload', 'cha_analysis_attach_file', FALSE);
insert into wf_component values ('a7d9c0535f49834bcc5d01fd3d1ad7df', '4028b25d7888a7f4017888f9e6af0001', 'accordion-end', '', FALSE);
insert into wf_component values ('aba91068bc801ce3f2dbfda7c3a313a2', '4028b25d7888a7f4017888f9e6af0001', 'accordion-start', '', FALSE);
insert into wf_component values ('a125e83ece531b99d031c78847d340a8', '4028b25d7888a7f4017888f9e6af0001', 'datetime', 'cha_implement_startdt', FALSE);
insert into wf_component values ('a8c08b7ff66e94bab4ae5c9ddac77b07', '4028b25d7888a7f4017888f9e6af0001', 'datetime', 'cha_implement_endD', FALSE);
insert into wf_component values ('a82c3def831961f635c6bcb8b8e848a2', '4028b25d7888a7f4017888f9e6af0001', 'inputbox', 'cha_implement_wd', FALSE);
insert into wf_component values ('af4e80182ff3fea073b396d9e8ff1c2b', '4028b25d7888a7f4017888f9e6af0001', 'dynamic-row-table', 'cha_implement_resource', FALSE);
insert into wf_component values ('a12f03fca3786b969a923f8aad6e1d81', '4028b25d7888a7f4017888f9e6af0001', 'fileupload', 'cha_implement_attach_file', FALSE);
insert into wf_component values ('a916992162c9f0747690fabc416098c3', '4028b25d7888a7f4017888f9e6af0001', 'accordion-end', '', FALSE);
insert into wf_component values ('aaa8bef0d513aa0ac8d4d44c53ef7790', '4028b25d7888a7f4017888f9e6af0001', 'accordion-start', '', FALSE);
insert into wf_component values ('a6bec26d5bfc73007ff79910d9aff2aa', '4028b25d7888a7f4017888f9e6af0001', 'datetime', 'cha_implement_test_startdt', FALSE);
insert into wf_component values ('aa2580d3afc5bbfc9e4ad7ba8dad963c', '4028b25d7888a7f4017888f9e6af0001', 'datetime', 'cha_implement_test_enddt', FALSE);
insert into wf_component values ('a1a84b18bde06171fdd4edf59e1cafd9', '4028b25d7888a7f4017888f9e6af0001', 'inputbox', 'cha_implement_test_wd', FALSE);
insert into wf_component values ('a6aedfc56ed8c741c9f6ee5e1c3ff322', '4028b25d7888a7f4017888f9e6af0001', 'dynamic-row-table', 'cha_implement_unittest_item', FALSE);
insert into wf_component values ('ada9f8e74c0997508637cdc2d428fb94', '4028b25d7888a7f4017888f9e6af0001', 'accordion-end', '', FALSE);
insert into wf_component values ('af4eef76e3a542f194ac156ac6dcc06f', '4028b25d7888a7f4017888f9e6af0001', 'accordion-start', '', FALSE);
insert into wf_component values ('aff0f64fc7d9689291e3d32c4fe8047b', '4028b25d7888a7f4017888f9e6af0001', 'datetime', 'cha_Integrated_test_startdt', FALSE);
insert into wf_component values ('a5dbb70aac1337c3e2206a3dbd0074b6', '4028b25d7888a7f4017888f9e6af0001', 'datetime', 'cha_Integrated_test_enddt', FALSE);
insert into wf_component values ('ac4213bdbdfa0f574e5575ba6e7ef8a0', '4028b25d7888a7f4017888f9e6af0001', 'inputbox', 'cha_Integrated_test_wd', FALSE);
insert into wf_component values ('a980fefca465ad7fb3bdc83cfed1cd2c', '4028b25d7888a7f4017888f9e6af0001', 'dynamic-row-table', 'cha_Integrated_test_item', FALSE);
insert into wf_component values ('a7e8276db9ef05adcc538678a63b326b', '4028b25d7888a7f4017888f9e6af0001', 'accordion-end', '', FALSE);
insert into wf_component values ('a0d3ea466d5aeeb00d502d8d1b615f6b', '4028b25d7888a7f4017888f9e6af0001', 'accordion-start', '', FALSE);
insert into wf_component values ('ac4be8fdff1752cbcbec33be1c2d99d8', '4028b25d7888a7f4017888f9e6af0001', 'inputbox', 'sd_manager_test_req', FALSE);
insert into wf_component values ('a9038fef67bfc858282dee6faf72bde8', '4028b25d7888a7f4017888f9e6af0001', 'inputbox', 'sd_manager_related_ground', FALSE);
insert into wf_component values ('a6a05fb218d3c9c70fd30a1e8c84fca0', '4028b25d7888a7f4017888f9e6af0001', 'textbox', 'sd_manager_test_cmt', FALSE);
insert into wf_component values ('a87d67e48b0a396f09253dd92daaae2a', '4028b25d7888a7f4017888f9e6af0001', 'accordion-end', '', FALSE);
insert into wf_component values ('adfc19b1f9e03fb6f5b6a58eb992f635', '4028b25d7888a7f4017888f9e6af0001', 'accordion-start', '', FALSE);
insert into wf_component values ('a0a8f65a8c3669bc39b3311b54ebea81', '4028b25d7888a7f4017888f9e6af0001', 'datetime', 'cha_filed_test_startdt', FALSE);
insert into wf_component values ('ac5c39724c6d72da19d4a277f2fc0861', '4028b25d7888a7f4017888f9e6af0001', 'datetime', 'cha_filed_test_enddt', FALSE);
insert into wf_component values ('a038232a86c32bf93cc847fb86920d33', '4028b25d7888a7f4017888f9e6af0001', 'dynamic-row-table', 'cha_filed_test_item', FALSE);
insert into wf_component values ('ad2545081cc9fa330aec2ff41dd87685', '4028b25d7888a7f4017888f9e6af0001', 'textbox', '', FALSE);
insert into wf_component values ('a98f121f7440d96267a34f3c4c4b3b7b', '4028b25d7888a7f4017888f9e6af0001', 'accordion-end', '', FALSE);
insert into wf_component values ('50c30230083b426b88de35afe81bae4b', '4028b25d7888a7f4017888f9e6af0001', 'accordion-start', '', FALSE);
insert into wf_component values ('0481303245e348268b8618b25daa6960', '4028b25d7888a7f4017888f9e6af0001', 'radio', 'cha_result', FALSE);
insert into wf_component values ('69619296316a44c79f6dd78af653e16f', '4028b25d7888a7f4017888f9e6af0001', 'textbox', 'cha_result_cmt', FALSE);
insert into wf_component values ('d86bfcc74be147d4ae7b0433d7b8a42a', '4028b25d7888a7f4017888f9e6af0001', 'fileupload', 'cha_result_attach_file', FALSE);
insert into wf_component values ('e2bff84339b3467e99c8b957fd14c057', '4028b25d7888a7f4017888f9e6af0001', 'accordion-end', '', FALSE);
insert into wf_component values ('a8d8d72b5692446995ddd37989766156', '4028b25d7888a7f4017888f9e6af0001', 'accordion-start', '', FALSE);
insert into wf_component values ('4777a6b2ef2b43908c065319c2bfa516', '4028b25d7888a7f4017888f9e6af0001', 'textbox', 'cha_result_cmt', FALSE);
insert into wf_component values ('12ee5318a80f4c4e97091a770f9fb59c', '4028b25d7888a7f4017888f9e6af0001', 'custom-code', 'cha_result_approver', FALSE);
insert into wf_component values ('51c1befbe967406da6c774207edfb620', '4028b25d7888a7f4017888f9e6af0001', 'accordion-end', '', FALSE);
insert into wf_component values ('c865d34e6b5547eb9e519fef05ef2dcd', '4028b25d7888a7f4017888f9e6af0001', 'accordion-start', '', FALSE);
insert into wf_component values ('20174e4a10174aae9f23152b2e3b1b31', '4028b25d7888a7f4017888f9e6af0001', 'datetime', 'cha_result_approval_dt', FALSE);
insert into wf_component values ('e13a94297f794fe99403f95e716f9343', '4028b25d7888a7f4017888f9e6af0001', 'textbox', 'cha_result_approval_opinion', FALSE);
insert into wf_component values ('f0d1968e9bc94888a829cfd9d10fccb5', '4028b25d7888a7f4017888f9e6af0001', 'accordion-end', '', FALSE);
insert into wf_component values ('0cab5ed20996478bac575355d01d7b69', '4028b25d788c4f8601788c8b8adc0003', 'label', '', FALSE);
insert into wf_component values ('9dd57b7354f546a6b3c15c1577e875d5', '4028b25d788c4f8601788c8b8adc0003', 'divider', '', FALSE);
insert into wf_component values ('c275441e4c394193b0ca136b59f44d65', '4028b25d788c4f8601788c8b8adc0003', 'accordion-start', '', FALSE);
insert into wf_component values ('139bf4f5584445ce8bb3deb79eb5a630', '4028b25d788c4f8601788c8b8adc0003', 'datetime', 'sd_req_chg_dt', FALSE);
insert into wf_component values ('7db0f91f130c45be89a6ea5e99c66dbe', '4028b25d788c4f8601788c8b8adc0003', 'custom-code', 'user', FALSE);
insert into wf_component values ('c8108daef968491a9520c942984049b3', '4028b25d788c4f8601788c8b8adc0003', 'inputbox', 'user_dept', FALSE);
insert into wf_component values ('5b0b6965e53049a7934a0d665f0f99da', '4028b25d788c4f8601788c8b8adc0003', 'dropdown', 'rel_service', FALSE);
insert into wf_component values ('d143565bc73043c99bf2ac72a80afe86', '4028b25d788c4f8601788c8b8adc0003', 'datetime', 'sd_finish_dt', FALSE);
insert into wf_component values ('aac6679f5ee349d4aec894fbc3bfb279', '4028b25d788c4f8601788c8b8adc0003', 'custom-code', 'sd_manager_user', FALSE);
insert into wf_component values ('fb9772a1d88342e9aa30ef2c4f729b76', '4028b25d788c4f8601788c8b8adc0003', 'inputbox', 'rel_title', true);
insert into wf_component values ('0cc84deed3424cbab82867fefcfb716e', '4028b25d788c4f8601788c8b8adc0003', 'textbox', 'sd_req_cmt', FALSE);
insert into wf_component values ('e7ac6934423047d99e744ca42ba80a5e', '4028b25d788c4f8601788c8b8adc0003', 'ci', 'chg_req_ci', FALSE);
insert into wf_component values ('1997e740e5c247a6b8b2a4a435fc2bc0', '4028b25d788c4f8601788c8b8adc0003', 'fileupload', 'chg_req_attach_file', FALSE);
insert into wf_component values ('d1855ada4f3a493db550efb28e80b985', '4028b25d788c4f8601788c8b8adc0003', 'accordion-end', '', FALSE);
insert into wf_component values ('aff338d65894932caaff8f9b5b137564', '4028b25d788c4f8601788c8b8adc0003', 'accordion-start', '', FALSE);
insert into wf_component values ('a841dc0bd9de9054efec9568400748f3', '4028b25d788c4f8601788c8b8adc0003', 'radio', 'id15', FALSE);
insert into wf_component values ('a1daf5eb5c56523e7a975a9114954bfa', '4028b25d788c4f8601788c8b8adc0003', 'textbox', '', FALSE);
insert into wf_component values ('a745892a840f00340bf7b267d3a63e90', '4028b25d788c4f8601788c8b8adc0003', 'fileupload', 'id17', FALSE);
insert into wf_component values ('aa17a892ba968a4a3c2cb6eedea8cc9d', '4028b25d788c4f8601788c8b8adc0003', 'accordion-end', '', FALSE);
insert into wf_component values ('8e94b33c57044bf6aa072c49fd30d3b1', '4028b25d788c4f8601788c8601f00002', 'label', '', FALSE);
insert into wf_component values ('f707bec669fb4d75b3f7e85c089c74ae', '4028b25d788c4f8601788c8601f00002', 'divider', '', FALSE);
insert into wf_component values ('dce4c9e0702744a9b75f66e2925e6671', '4028b25d788c4f8601788c8601f00002', 'accordion-start', '', FALSE);
insert into wf_component values ('43d5564b1f6c44f0ab8976d505fc09b3', '4028b25d788c4f8601788c8601f00002', 'datetime', 'sd_req_chg_dt', FALSE);
insert into wf_component values ('a41a893e79af4e60ae84f07f9b09aaf0', '4028b25d788c4f8601788c8601f00002', 'custom-code', 'user', FALSE);
insert into wf_component values ('c614d28cf0ff46099480ba82f2f3684e', '4028b25d788c4f8601788c8601f00002', 'inputbox', 'user_dept', FALSE);
insert into wf_component values ('c2185527b27841bf9f9aa292d11c760e', '4028b25d788c4f8601788c8601f00002', 'dropdown', 'rel_service', FALSE);
insert into wf_component values ('afc02b23a7f34e578b431da9cdaf41a3', '4028b25d788c4f8601788c8601f00002', 'datetime', 'sd_finish_dt', FALSE);
insert into wf_component values ('1ce826a47a1545ebaba8d5527b0a98e0', '4028b25d788c4f8601788c8601f00002', 'custom-code', 'sd_manager_user', FALSE);
insert into wf_component values ('e4c435744d944ed6a8126c08c1c0fff9', '4028b25d788c4f8601788c8601f00002', 'inputbox', 'rel_title', true);
insert into wf_component values ('244307c8dd204f9cbffbb29107c80e73', '4028b25d788c4f8601788c8601f00002', 'textbox', 'sd_req_cmt', FALSE);
insert into wf_component values ('28e014bfbc8b46c784e5f4858c8e476d', '4028b25d788c4f8601788c8601f00002', 'ci', 'chg_req_ci', FALSE);
insert into wf_component values ('6069b42a0ee048489063e19915e08e46', '4028b25d788c4f8601788c8601f00002', 'fileupload', 'chg_req_attach_file', FALSE);
insert into wf_component values ('8c3a8a79321a4f70ab17b16e4da9caa6', '4028b25d788c4f8601788c8601f00002', 'accordion-end', '', FALSE);
insert into wf_component values ('4dafd004cbc944b7996522ffe6b7d809', '4028b25d788c4f8601788c8601f00002', 'accordion-start', '', FALSE);
insert into wf_component values ('41193073002544a29e985d2157c2867e', '4028b25d788c4f8601788c8601f00002', 'datetime', 'sd_accept_dt', FALSE);
insert into wf_component values ('5b312c6e95c7499ea0671a6afa3381d4', '4028b25d788c4f8601788c8601f00002', 'inputbox', 'sd_manager_user_dept', FALSE);
insert into wf_component values ('2b2b34fdcb64411999b201fa09f3bf81', '4028b25d788c4f8601788c8601f00002', 'textbox', 'sd_accept_cmt', FALSE);
insert into wf_component values ('d861305d80a045ed8d9a755d5a6c5dda', '4028b25d788c4f8601788c8601f00002', 'custom-code', 'rel_req_user', FALSE);
insert into wf_component values ('3d9de87a9e0c4c88aecff1f2db9d14d3', '4028b25d788c4f8601788c8601f00002', 'accordion-end', '', FALSE);
insert into wf_component values ('3e1423aabfd24e259a521546f8c10c97', '4028b25d788c4f8601788c8601f00002', 'accordion-start', '', FALSE);
insert into wf_component values ('a1c5167618f641d4906997e4ca2e2952', '4028b25d788c4f8601788c8601f00002', 'textbox', 'sd_result_cmt', FALSE);
insert into wf_component values ('0226c90f73c644fabe92b22e259eb7a7', '4028b25d788c4f8601788c8601f00002', 'custom-code', 'sd_approver', FALSE);
insert into wf_component values ('5ee83a02fecb4fc79eca4c5e2c70d27a', '4028b25d788c4f8601788c8601f00002', 'accordion-end', '', FALSE);
insert into wf_component values ('3141b9fa06ab400bbef50a95e004e3a9', '4028b25d788c4f8601788c8601f00002', 'accordion-start', '', FALSE);
insert into wf_component values ('eedbef4d73d94377bba70b6c41df2c86', '4028b25d788c4f8601788c8601f00002', 'datetime', 'sd_approval_dt', FALSE);
insert into wf_component values ('68dbfd222e8548d4b9302efa2131836f', '4028b25d788c4f8601788c8601f00002', 'textbox', 'sd_approval_opinion', FALSE);
insert into wf_component values ('25f616d3be6b4723b4bbe0b9891c29a2', '4028b25d788c4f8601788c8601f00002', 'accordion-end', '', FALSE);
/**
 *
 */
DROP TABLE IF EXISTS wf_component_data cascade;

CREATE TABLE wf_component_data
(
    component_id varchar(128) NOT NULL,
    attribute_id varchar(100) NOT NULL,
    attribute_value text NOT NULL,
    attribute_order int,
    CONSTRAINT wf_component_data_pk PRIMARY KEY (component_id, attribute_id),
    CONSTRAINT wf_component_data_fk FOREIGN KEY (component_id) REFERENCES wf_component (component_id)
);

COMMENT ON TABLE wf_component_data IS '컴포넌트세부설정';
COMMENT ON COLUMN wf_component_data.component_id IS '컴포넌트아이디';
COMMENT ON COLUMN wf_component_data.attribute_id IS '속성아이디';
COMMENT ON COLUMN wf_component_data.attribute_value IS '속성값';
COMMENT ON COLUMN wf_component_data.attribute_order IS '속성순서';

insert into wf_component_data values ('a0438d6ebeb3e76391de4ca2591b891d', 'option', '[{"seq":"1","name":"전화","value":"phone"},{"seq":"2","name":"메일","value":"mail"},{"seq":"3","name":"전자결재문서","value":"electronic "},{"seq":"4","name":"자체","value":"self"}]');
insert into wf_component_data values ('a0438d6ebeb3e76391de4ca2591b891d', 'display', '{"column":"4","order":6}');
insert into wf_component_data values ('a0438d6ebeb3e76391de4ca2591b891d', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"접수경로"}');
insert into wf_component_data values ('a083bcc9a0679f6bc9cde83eb4205161', 'label', '{"size":"20","color":"rgba(40,50,56,1)","bold":"Y","italic":"N","underline":"N","align":"left","text":"요청 내역"}');
insert into wf_component_data values ('a083bcc9a0679f6bc9cde83eb4205161', 'display', '{"startId":"ae615f2ac0d145ef25d5e7253c12940d","thickness":"1","color":"rgba(235, 235, 235, 1)","order":14}');
insert into wf_component_data values ('a15a240317ee972e96532bf01836fd5c', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"신청자"}');
insert into wf_component_data values ('a15a240317ee972e96532bf01836fd5c', 'display', '{"column":"10","customCode":"40288a19736b46fb01736b89e46c0008","default":"session|userName|이름","buttonText":"검색","order":5}');
insert into wf_component_data values ('a1709377086e44e4e319ba3c2aec93c4', 'display', '{"column":"12","isEditable":true,"border":"rgba(235, 235, 235, 1)","order":12}');
insert into wf_component_data values ('a1709377086e44e4e319ba3c2aec93c4', 'header', '{"size":"16","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"left"}');
insert into wf_component_data values ('a1709377086e44e4e319ba3c2aec93c4', 'label', '{"position":"top","column":"12","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"CI 변경 내역"}');
insert into wf_component_data values ('a1bbab51ba6b903eea7834b64ff2018e', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"승인의견"}');
insert into wf_component_data values ('a1bbab51ba6b903eea7834b64ff2018e', 'display', '{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":27}');
insert into wf_component_data values ('a1bbab51ba6b903eea7834b64ff2018e', 'validate', '{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('a20306ee9477e23c3b460bdb459f5f89', 'display', '{"column":"4","order":7}');
insert into wf_component_data values ('a20306ee9477e23c3b460bdb459f5f89', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"변경구분"}');
insert into wf_component_data values ('a20306ee9477e23c3b460bdb459f5f89', 'option', '[{"seq":"1","name":"인프라 업데이트","value":"infra"},{"seq":"2","name":"릴리즈 업데이트","value":"rel"},{"seq":"3","name":"외부연동 업데이트","value":"external"},{"seq":"4","name":"신규장비 입고","value":"new"}]');
insert into wf_component_data values ('a293e254d00208aeb1afb8d78c0d0737', 'validate', '{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('a293e254d00208aeb1afb8d78c0d0737', 'display', '{"column":"10","default":"now","order":26}');
insert into wf_component_data values ('a293e254d00208aeb1afb8d78c0d0737', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"승인일시"}');
insert into wf_component_data values ('a2ef0bbeda3884c8111f82abeb242d6f', 'display', '{"endId":"abc34c5409954d3c792f2209f3347cdf","thickness":"1","color":"rgba(235, 235, 235, 1)","order":15}');
insert into wf_component_data values ('a2ef0bbeda3884c8111f82abeb242d6f', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"접수 내역"}');
insert into wf_component_data values ('a40b7f5aaa39c16cf44d8db83f3f2a5b', 'display', '{"column":"10","order":22}');
insert into wf_component_data values ('a40b7f5aaa39c16cf44d8db83f3f2a5b', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"첨부파일"}');
insert into wf_component_data values ('a4c6a3dd66d0cc36d108ff8cc6904fd7', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"처리내용"}');
insert into wf_component_data values ('a4c6a3dd66d0cc36d108ff8cc6904fd7', 'validate', '{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('a4c6a3dd66d0cc36d108ff8cc6904fd7', 'display', '{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":21}');
insert into wf_component_data values ('a563dbfc8286f3ed75f47b8e778390fb', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"첨부파일"}');
insert into wf_component_data values ('a563dbfc8286f3ed75f47b8e778390fb', 'display', '{"column":"10","order":13}');
insert into wf_component_data values ('a640301d7e67dc92c279f83bec7358be', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"접수일시"}');
insert into wf_component_data values ('a640301d7e67dc92c279f83bec7358be', 'validate', '{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('a640301d7e67dc92c279f83bec7358be', 'display', '{"column":"10","default":"now","order":16}');
insert into wf_component_data values ('a661cc2ef029e637a72124a4b7c5f32f', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"처리일시"}');
insert into wf_component_data values ('a661cc2ef029e637a72124a4b7c5f32f', 'display', '{"column":"10","default":"now","order":20}');
insert into wf_component_data values ('a661cc2ef029e637a72124a4b7c5f32f', 'validate', '{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('a7bc03e8abd70100a86e43338e308054', 'display', '{"column":"10","customCode":"40288a19736b46fb01736b89e46c0008","default":"session|userName|이름","buttonText":"검색","order":9}');
insert into wf_component_data values ('a7bc03e8abd70100a86e43338e308054', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"담당자"}');
insert into wf_component_data values ('a7fb73ce3c61d3d600ab135d6a6a8684', 'validate', '{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('a7fb73ce3c61d3d600ab135d6a6a8684', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"요청내역"}');
insert into wf_component_data values ('a7fb73ce3c61d3d600ab135d6a6a8684', 'display', '{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":11}');
insert into wf_component_data values ('a82c5acd1b0ec0bba083d76135d39e5a', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"구성 변경 처리 내역"}');
insert into wf_component_data values ('a82c5acd1b0ec0bba083d76135d39e5a', 'display', '{"endId":"a87535522638ab7240e972d44d66ef49","thickness":"1","color":"rgba(235, 235, 235, 1)","order":19}');
insert into wf_component_data values ('a87535522638ab7240e972d44d66ef49', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"구성 변경 처리 내역"}');
insert into wf_component_data values ('a87535522638ab7240e972d44d66ef49', 'display', '{"startId":"a82c5acd1b0ec0bba083d76135d39e5a","thickness":"1","color":"rgba(235, 235, 235, 1)","order":24}');
insert into wf_component_data values ('a90620f766dedb2beebbe776eff556ec', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"신청일시"}');
insert into wf_component_data values ('a90620f766dedb2beebbe776eff556ec', 'validate', '{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('a90620f766dedb2beebbe776eff556ec', 'display', '{"column":"10","default":"now","order":4}');
insert into wf_component_data values ('a9611c745a6716665c5e7690b872310b', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"승인자"}');
insert into wf_component_data values ('a9611c745a6716665c5e7690b872310b', 'display', '{"column":"10","customCode":"40288a19736b46fb01736b89e46c0008","default":"session|userName|이름","buttonText":"검색","order":23}');
insert into wf_component_data values ('a9b580dd25da55ce3ce347bcef0169d5', 'display', '{"thickness":"5","color":"rgba(88,104,114,1)","type":"solid","order":2}');
insert into wf_component_data values ('a9ef096c157fab2ec319efcf467b568a', 'validate', '{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"}');
insert into wf_component_data values ('a9ef096c157fab2ec319efcf467b568a', 'display', '{"placeholder":"","column":"10","default":"none|","order":10}');
insert into wf_component_data values ('a9ef096c157fab2ec319efcf467b568a', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"제목"}');
insert into wf_component_data values ('aa54cd88560b84b902653fd90336ee1e', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"승인 내역"}');
insert into wf_component_data values ('aa54cd88560b84b902653fd90336ee1e', 'display', '{"endId":"ad5e4623a663879158834cbdddd032e5","thickness":"1","color":"rgba(235, 235, 235, 1)","order":25}');
insert into wf_component_data values ('aa5a75f1766bc691fa0c066202c776ae', 'validate', '{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('aa5a75f1766bc691fa0c066202c776ae', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"접수의견"}');
insert into wf_component_data values ('aa5a75f1766bc691fa0c066202c776ae', 'display', '{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":17}');
insert into wf_component_data values ('abc34c5409954d3c792f2209f3347cdf', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"접수 내역"}');
insert into wf_component_data values ('abc34c5409954d3c792f2209f3347cdf', 'display', '{"startId":"a2ef0bbeda3884c8111f82abeb242d6f","thickness":"1","color":"rgba(235, 235, 235, 1)","order":18}');
insert into wf_component_data values ('ad5e4623a663879158834cbdddd032e5', 'display', '{"startId":"aa54cd88560b84b902653fd90336ee1e","thickness":"1","color":"rgba(235, 235, 235, 1)","order":28}');
insert into wf_component_data values ('ad5e4623a663879158834cbdddd032e5', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"승인 내역"}');
insert into wf_component_data values ('ad6afd860d2cda7a78bf93f4e23c8efd', 'display', '{"size":"40","color":"rgba(52,152,219,1)","bold":"Y","italic":"N","underline":"N","align":"left","text":"CI 신청서","order":1}');
insert into wf_component_data values ('ae615f2ac0d145ef25d5e7253c12940d', 'label', '{"size":"20","color":"rgba(40,50,56,1)","bold":"Y","italic":"N","underline":"N","align":"left","text":"요청 내역"}');
insert into wf_component_data values ('ae615f2ac0d145ef25d5e7253c12940d', 'display', '{"endId":"a083bcc9a0679f6bc9cde83eb4205161","thickness":"1","color":"rgba(235, 235, 235, 1)","order":3}');
insert into wf_component_data values ('aff3e77073f75d2385e2347ed996f412', 'display', '{"column":"10","default":"now","order":8}');
insert into wf_component_data values ('aff3e77073f75d2385e2347ed996f412', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"완료희망일시"}');
insert into wf_component_data values ('aff3e77073f75d2385e2347ed996f412', 'validate', '{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('a64a8b2b177f51b09134faf7d148a4bb', 'display', '{"column":"10","customCode":"40288a19736b46fb01736b89e46c0008","default":"session|userName|이름","buttonText":"검색","order":5}');
insert into wf_component_data values ('a64a8b2b177f51b09134faf7d148a4bb', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"신청자"}');
insert into wf_component_data values ('189aafb3a9404ec489607fef8b9f8560', 'display', '{"placeholder":"","column":"10","default":"select|department|부서","order":6}');
insert into wf_component_data values ('189aafb3a9404ec489607fef8b9f8560', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"신청부서"}');
insert into wf_component_data values ('189aafb3a9404ec489607fef8b9f8560', 'validate', '{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"}');
insert into wf_component_data values ('80060ae8be4b42a9883bc7b7dd96632b', 'display', '{"column":"4","order":7}');
insert into wf_component_data values ('80060ae8be4b42a9883bc7b7dd96632b', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"관련서비스"}');
insert into wf_component_data values ('80060ae8be4b42a9883bc7b7dd96632b', 'option', '[{"seq":"1","name":"홈페이지","value":"homepage"},{"seq":"2","name":"전자결재시스템","value":"eps"},{"seq":"3","name":"출입관리시스템","value":"acs"},{"seq":"4","name":"ERP","value":"erp"},{"seq":"5","name":"ITSM","value":"itsm"},{"seq":"6","name":"기타","value":"etd"}]');
insert into wf_component_data values ('a02d6fbdc20e46b08c95453f6d25c311', 'display', '{"column":"4","order":8}');
insert into wf_component_data values ('a02d6fbdc20e46b08c95453f6d25c311', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"문의유형"}');
insert into wf_component_data values ('a02d6fbdc20e46b08c95453f6d25c311', 'option', '[{"seq":"1","name":"사무기기 사용문의","value":"oe"},{"seq":"2","name":"서비스요청 처리현황 문의","value":"sr"},{"seq":"3","name":"업무시스템 사용문의","value":"bs"},{"seq":"4","name":"불만접수","value":"di"},{"seq":"5","name":"기타","value":"etc"}]');
insert into wf_component_data values ('0177a25c92ad43aebd50bfc45d32fc02', 'display', '{"column":"10","default":"now","order":9}');
insert into wf_component_data values ('0177a25c92ad43aebd50bfc45d32fc02', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"완료희망일시"}');
insert into wf_component_data values ('0177a25c92ad43aebd50bfc45d32fc02', 'validate', '{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('303f8e1f7e814953a759ff93ee336f65', 'display', '{"column":"10","customCode":"40288a19736b46fb01736b89e46c0008","default":"none","buttonText":"검색","order":10}');
insert into wf_component_data values ('303f8e1f7e814953a759ff93ee336f65', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"담당자"}');
insert into wf_component_data values ('8fe7d5f6c16a49788420fe648a9a4cb2', 'display', '{"placeholder":"","column":"10","default":"none|","order":11}');
insert into wf_component_data values ('8fe7d5f6c16a49788420fe648a9a4cb2', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"제목"}');
insert into wf_component_data values ('8fe7d5f6c16a49788420fe648a9a4cb2', 'validate', '{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"}');
insert into wf_component_data values ('63c2bbf75cb9456a9de83c2f668ad181', 'display', '{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":12}');
insert into wf_component_data values ('63c2bbf75cb9456a9de83c2f668ad181', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"문의내용"}');
insert into wf_component_data values ('63c2bbf75cb9456a9de83c2f668ad181', 'validate', '{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('cec8377af21641e1a2a274db6654aa88', 'display', '{"column":"10","order":13}');
insert into wf_component_data values ('cec8377af21641e1a2a274db6654aa88', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"첨부파일"}');
insert into wf_component_data values ('de47121e6034445598ff4c216168781e', 'display', '{"startId":"6b20477610204c32ac7460143405299f","thickness":"1","color":"rgba(235, 235, 235, 1)","order":14}');
insert into wf_component_data values ('de47121e6034445598ff4c216168781e', 'label', '{"size":"20","color":"rgba(40,50,56,1)","bold":"Y","italic":"N","underline":"N","align":"left","text":"요청 내역"}');
insert into wf_component_data values ('7b53c1640c1640ca889f6e44ac92a256', 'display', '{"endId":"fc2d992990704ad7be4eb8fb52839a0b","thickness":"1","color":"rgba(235, 235, 235, 1)","order":15}');
insert into wf_component_data values ('7b53c1640c1640ca889f6e44ac92a256', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"접수 내역"}');
insert into wf_component_data values ('2b2f880adf2f434c8b71d57f3439129e', 'display', '{"column":"10","default":"now","order":16}');
insert into wf_component_data values ('2b2f880adf2f434c8b71d57f3439129e', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"접수일시"}');
insert into wf_component_data values ('2b2f880adf2f434c8b71d57f3439129e', 'validate', '{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('568e2d7388614f349756a9d55f9fbf82', 'display', '{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":17}');
insert into wf_component_data values ('568e2d7388614f349756a9d55f9fbf82', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"접수의견"}');
insert into wf_component_data values ('568e2d7388614f349756a9d55f9fbf82', 'validate', '{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('fc2d992990704ad7be4eb8fb52839a0b', 'display', '{"startId":"7b53c1640c1640ca889f6e44ac92a256","thickness":"1","color":"rgba(235, 235, 235, 1)","order":18}');
insert into wf_component_data values ('fc2d992990704ad7be4eb8fb52839a0b', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"접수 내역"}');
insert into wf_component_data values ('cf757877eb04473492014366b49d7aef', 'display', '{"endId":"aec9c3f0a2cc456c9870e7cda1b6980a","thickness":"1","color":"rgba(235, 235, 235, 1)","order":19}');
insert into wf_component_data values ('cf757877eb04473492014366b49d7aef', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"처리 내역"}');
insert into wf_component_data values ('4601ce00de0a463098f4d4c1a020028f', 'display', '{"column":"10","default":"now","order":20}');
insert into wf_component_data values ('4601ce00de0a463098f4d4c1a020028f', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"처리일시"}');
insert into wf_component_data values ('4601ce00de0a463098f4d4c1a020028f', 'validate', '{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('c2a95ab676214ffa82571c8d24047f1a', 'display', '{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":21}');
insert into wf_component_data values ('c2a95ab676214ffa82571c8d24047f1a', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"문의답변"}');
insert into wf_component_data values ('c2a95ab676214ffa82571c8d24047f1a', 'validate', '{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('c57c3c17b8b34783ba385fa33d96570c', 'display', '{"column":"10","order":22}');
insert into wf_component_data values ('c57c3c17b8b34783ba385fa33d96570c', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"첨부파일"}');
insert into wf_component_data values ('f2bf25aa9b87468ba02398838d110d9d', 'display', '{"column":"10","customCode":"40288a19736b46fb01736b89e46c0008","default":"none","buttonText":"검색","order":23}');
insert into wf_component_data values ('f2bf25aa9b87468ba02398838d110d9d', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"승인자"}');
insert into wf_component_data values ('aec9c3f0a2cc456c9870e7cda1b6980a', 'display', '{"startId":"cf757877eb04473492014366b49d7aef","thickness":"1","color":"rgba(235, 235, 235, 1)","order":24}');
insert into wf_component_data values ('aec9c3f0a2cc456c9870e7cda1b6980a', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"처리 내역"}');
insert into wf_component_data values ('f4639d8fee6a4796baf464b8f55105e5', 'display', '{"endId":"50871faf2bf74d2b8198b0049c9eab72","thickness":"1","color":"rgba(235, 235, 235, 1)","order":25}');
insert into wf_component_data values ('f4639d8fee6a4796baf464b8f55105e5', 'label', '{"size":"20","color":"rgba(63,75,86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"승인 내역"}');
insert into wf_component_data values ('9cfaa92789b64c7da46c205995416821', 'display', '{"column":"10","default":"now","order":26}');
insert into wf_component_data values ('9cfaa92789b64c7da46c205995416821', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"승인일시"}');
insert into wf_component_data values ('9cfaa92789b64c7da46c205995416821', 'validate', '{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('05df4c3c4c2641f082b8cb181327511d', 'display', '{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":27}');
insert into wf_component_data values ('05df4c3c4c2641f082b8cb181327511d', 'label', '{"position":"left","column":"2","size":"16","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"승인의견"}');
insert into wf_component_data values ('05df4c3c4c2641f082b8cb181327511d', 'validate', '{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('50871faf2bf74d2b8198b0049c9eab72', 'display', '{"startId":"f4639d8fee6a4796baf464b8f55105e5","thickness":"1","color":"rgba(235, 235, 235, 1)","order":28}');
insert into wf_component_data values ('50871faf2bf74d2b8198b0049c9eab72', 'label', '{"size":"20","color":"rgba(63,75,86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"승인 내역"}');
insert into wf_component_data values ('53dc0f34890f4c48a7ebd410169623b6', 'display', '{"size":"40","color":"rgba(52,152,219,1)","bold":"Y","italic":"N","underline":"N","align":"left","text":"단순문의 만족도 평가","order":1}');
insert into wf_component_data values ('caaf208e3d6c4ea0a4ec4d1fa63fc81c', 'display', '{"thickness":"5","color":"rgba(88,104,114,1)","type":"solid","order":2}');
insert into wf_component_data values ('f79d816a6ae748aabc1c58748b65c119', 'display', '{"endId":"4e1c394c09c84cb2a20b20492f2a3cb0","thickness":"1","color":"rgba(235, 235, 235, 1)","order":3}');
insert into wf_component_data values ('f79d816a6ae748aabc1c58748b65c119', 'label', '{"size":"20","color":"rgba(40,50,56,1)","bold":"Y","italic":"N","underline":"N","align":"left","text":"요청 내역"}');
insert into wf_component_data values ('cf96b8a2488c4b84a90002b01f80aeba', 'display', '{"column":"10","default":"now","order":4}');
insert into wf_component_data values ('cf96b8a2488c4b84a90002b01f80aeba', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"신청일시"}');
insert into wf_component_data values ('cf96b8a2488c4b84a90002b01f80aeba', 'validate', '{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('aca126bd4f6958bce2cb5e6e607251d0', 'display', '{"column":"10","customCode":"40288ab777dd21b50177dd52781e0000","default":"none","buttonText":"검색","order":5}');
insert into wf_component_data values ('aca126bd4f6958bce2cb5e6e607251d0', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"신청자"}');
insert into wf_component_data values ('c088820086ef49488a3033eb84566fa5', 'display', '{"placeholder":"","column":"10","default":"select|department|부서","order":6}');
insert into wf_component_data values ('c088820086ef49488a3033eb84566fa5', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"신청부서"}');
insert into wf_component_data values ('c088820086ef49488a3033eb84566fa5', 'validate', '{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"}');
insert into wf_component_data values ('5e6444cb4fad489fa76cacd2b8a26108', 'display', '{"column":"4","order":7}');
insert into wf_component_data values ('5e6444cb4fad489fa76cacd2b8a26108', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"관련서비스"}');
insert into wf_component_data values ('5e6444cb4fad489fa76cacd2b8a26108', 'option', '[{"seq":"1","name":"홈페이지","value":"homepage"},{"seq":"2","name":"전자결재시스템","value":"eps"},{"seq":"3","name":"출입관리시스템","value":"acs"},{"seq":"4","name":"ERP","value":"erp"},{"seq":"5","name":"ITSM","value":"itsm"},{"seq":"6","name":"기타","value":"etd"}]');
insert into wf_component_data values ('3c592c92c4bc4868a4b585f8c9d9c360', 'display', '{"column":"4","order":8}');
insert into wf_component_data values ('3c592c92c4bc4868a4b585f8c9d9c360', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"문의유형"}');
insert into wf_component_data values ('3c592c92c4bc4868a4b585f8c9d9c360', 'option', '[{"seq":"1","name":"사무기기 사용문의","value":"oe"},{"seq":"2","name":"서비스요청 처리현황 문의","value":"sr"},{"seq":"3","name":"업무시스템 사용문의","value":"bs"},{"seq":"4","name":"불만접수","value":"di"},{"seq":"5","name":"기타","value":"etc"}]');
insert into wf_component_data values ('a6ba20dc101143c1b6bcc21c159cc1dd', 'display', '{"column":"10","default":"now","order":9}');
insert into wf_component_data values ('a6ba20dc101143c1b6bcc21c159cc1dd', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"완료희망일시"}');
insert into wf_component_data values ('a6ba20dc101143c1b6bcc21c159cc1dd', 'validate', '{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('ad4c94b7f64a4d01bafef76fbbb444bc', 'display', '{"column":"10","customCode":"40288a19736b46fb01736b89e46c0008","default":"none","buttonText":"검색","order":10}');
insert into wf_component_data values ('ad4c94b7f64a4d01bafef76fbbb444bc', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"담당자"}');
insert into wf_component_data values ('ee6e34704b094873926c3918ae00bfbd', 'display', '{"placeholder":"","column":"10","default":"none|","order":11}');
insert into wf_component_data values ('ee6e34704b094873926c3918ae00bfbd', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"제목"}');
insert into wf_component_data values ('ee6e34704b094873926c3918ae00bfbd', 'validate', '{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"}');
insert into wf_component_data values ('5b8529ddbd40462e824439a753b2c153', 'display', '{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":12}');
insert into wf_component_data values ('5b8529ddbd40462e824439a753b2c153', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"문의내용"}');
insert into wf_component_data values ('5b8529ddbd40462e824439a753b2c153', 'validate', '{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('74b8c62595f544eaa47d7c8b6ea8560e', 'display', '{"column":"10","order":13}');
insert into wf_component_data values ('74b8c62595f544eaa47d7c8b6ea8560e', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"첨부파일"}');
insert into wf_component_data values ('4e1c394c09c84cb2a20b20492f2a3cb0', 'display', '{"startId":"f79d816a6ae748aabc1c58748b65c119","thickness":"1","color":"rgba(235, 235, 235, 1)","order":14}');
insert into wf_component_data values ('4e1c394c09c84cb2a20b20492f2a3cb0', 'label', '{"size":"20","color":"rgba(40,50,56,1)","bold":"Y","italic":"N","underline":"N","align":"left","text":"요청 내역"}');
insert into wf_component_data values ('022540d758ec405ba45d70f8df0c03fc', 'display', '{"endId":"27e376940afc4568aa228f8663318ea0","thickness":"1","color":"rgba(235, 235, 235, 1)","order":15}');
insert into wf_component_data values ('022540d758ec405ba45d70f8df0c03fc', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"답변 내역"}');
insert into wf_component_data values ('36b04439545649a79824d742d8d48a61', 'display', '{"column":"10","default":"now","order":16}');
insert into wf_component_data values ('36b04439545649a79824d742d8d48a61', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"답변일시"}');
insert into wf_component_data values ('36b04439545649a79824d742d8d48a61', 'validate', '{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('489d27f3f1eb454780256befc0167cf6', 'display', '{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":17}');
insert into wf_component_data values ('489d27f3f1eb454780256befc0167cf6', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"답변내용"}');
insert into wf_component_data values ('489d27f3f1eb454780256befc0167cf6', 'validate', '{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('e5f66323f50b498189d7ebccfaf3c4a9', 'display', '{"column":"10","order":18}');
insert into wf_component_data values ('e5f66323f50b498189d7ebccfaf3c4a9', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"첨부파일"}');
insert into wf_component_data values ('27e376940afc4568aa228f8663318ea0', 'display', '{"startId":"022540d758ec405ba45d70f8df0c03fc","thickness":"1","color":"rgba(235, 235, 235, 1)","order":19}');
insert into wf_component_data values ('27e376940afc4568aa228f8663318ea0', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"답변 내역"}');
insert into wf_component_data values ('357f6d029a3d420d9fefba4e5a6ca9df', 'display', '{"endId":"130111f086244dc393963a75a480631e","thickness":"1","color":"rgba(235, 235, 235, 1)","order":20}');
insert into wf_component_data values ('357f6d029a3d420d9fefba4e5a6ca9df', 'label', '{"size":"20","color":"rgba(63,75,86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"만족도 내역"}');
insert into wf_component_data values ('b28cb8265d134217b7336704c3554314', 'display', '{"column":"10","direction":"horizontal","position":"right","order":21}');
insert into wf_component_data values ('b28cb8265d134217b7336704c3554314', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"만족도"}');
insert into wf_component_data values ('b28cb8265d134217b7336704c3554314', 'option', '[{"seq":"1","name":"매우만족","value":"5"},{"seq":"2","name":"만족","value":"4"},{"seq":"3","name":"보통","value":"3"},{"seq":"4","name":"불만족","value":"2"},{"seq":"5","name":"매우불만족","value":"1"}]');
insert into wf_component_data values ('4ba00478892f4195a10597b3090d887c', 'display', '{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":22}');
insert into wf_component_data values ('4ba00478892f4195a10597b3090d887c', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"평가의견"}');
insert into wf_component_data values ('4ba00478892f4195a10597b3090d887c', 'validate', '{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('974aa952322d4800ab68d7efaa5c921f', 'display', '{"column":"10","order":23}');
insert into wf_component_data values ('974aa952322d4800ab68d7efaa5c921f', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"첨부파일"}');
insert into wf_component_data values ('130111f086244dc393963a75a480631e', 'display', '{"startId":"357f6d029a3d420d9fefba4e5a6ca9df","thickness":"1","color":"rgba(235, 235, 235, 1)","order":24}');
insert into wf_component_data values ('130111f086244dc393963a75a480631e', 'label', '{"size":"20","color":"rgba(63,75,86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"만족도 내역"}');
insert into wf_component_data values ('e4a75d472ad94be0abc41b7be5c60a06', 'display', '{"size":"40","color":"rgba(52,152,219,1)","bold":"Y","italic":"N","underline":"N","align":"left","text":"단순문의","order":1}');
insert into wf_component_data values ('29fc1c91ed9145c2ab4569db715ea461', 'display', '{"thickness":"5","color":"rgba(88,104,114,1)","type":"solid","order":2}');
insert into wf_component_data values ('6b20477610204c32ac7460143405299f', 'display', '{"endId":"de47121e6034445598ff4c216168781e","thickness":"1","color":"rgba(235, 235, 235, 1)","order":3}');
insert into wf_component_data values ('6b20477610204c32ac7460143405299f', 'label', '{"size":"20","color":"rgba(40,50,56,1)","bold":"Y","italic":"N","underline":"N","align":"left","text":"요청 내역"}');
insert into wf_component_data values ('763a02453da34238a611908d427b03a4', 'display', '{"column":"10","default":"now","order":4}');
insert into wf_component_data values ('763a02453da34238a611908d427b03a4', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"신청일시"}');
insert into wf_component_data values ('763a02453da34238a611908d427b03a4', 'validate', '{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('a042929dd4bbca295a5e14f7c3479630', 'display', '{"column":"12","rowMin":"1","rowMax":"15","border":"rgba(235, 235, 235, 1)","order":25}');
insert into wf_component_data values ('a042929dd4bbca295a5e14f7c3479630', 'drTableColumns', '[{"type":"inputbox","display":{"align":"center","placeholder":""},"validate":{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"10"},"width":"7","text":"작업자"},{"type":"inputbox","display":{"align":"center","placeholder":""},"validate":{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"},"width":"8","text":"예상 시작일"},{"type":"inputbox","display":{"align":"center","placeholder":""},"validate":{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"},"width":"8","text":"예상 종료일"},{"type":"inputbox","display":{"align":"center","placeholder":""},"validate":{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"},"width":"12","text":"작업내용"}]');
insert into wf_component_data values ('a042929dd4bbca295a5e14f7c3479630', 'label', '{"position":"top","column":"12","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"상세일정등록"}');
insert into wf_component_data values ('a042929dd4bbca295a5e14f7c3479630', 'header', '{"size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"center"}');
insert into wf_component_data values ('a049fd4ac6c927ccde29ae160ee5d846', 'display', '{"column":"10","default":"now","order":45}');
insert into wf_component_data values ('a049fd4ac6c927ccde29ae160ee5d846', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"실제 종료일"}');
insert into wf_component_data values ('a049fd4ac6c927ccde29ae160ee5d846', 'validate', '{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('a04e548325ed8ae31a385a702f69ea10', 'display', '{"column":"10","customCode":"40288a19736b46fb01736b89e46c0008","default":"none","buttonText":"검색","order":17}');
insert into wf_component_data values ('a04e548325ed8ae31a385a702f69ea10', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"접수자"}');
insert into wf_component_data values ('a1019e08196f6803f8573d04348783b2', 'display', '{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":27}');
insert into wf_component_data values ('a1019e08196f6803f8573d04348783b2', 'validate', '{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('a1019e08196f6803f8573d04348783b2', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"세부작업내용"}');
insert into wf_component_data values ('a11e0b9db990bab2241122481f268de4', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"승인일시"}');
insert into wf_component_data values ('a11e0b9db990bab2241122481f268de4', 'validate', '{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('a11e0b9db990bab2241122481f268de4', 'display', '{"column":"10","default":"now","order":37}');
insert into wf_component_data values ('a1241ec6aabdb13fe23fd435b3fbbb0b', 'display', '{"endId":"ac1c201dc1018243e0aa3bcc7b6fc60a","thickness":"1","color":"rgba(235, 235, 235, 1)","order":20}');
insert into wf_component_data values ('a1241ec6aabdb13fe23fd435b3fbbb0b', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"작업 계획"}');
insert into wf_component_data values ('a251ce5051063fbf6aba8b1f4a4d7d94', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"완료 보고서"}');
insert into wf_component_data values ('a251ce5051063fbf6aba8b1f4a4d7d94', 'display', '{"endId":"a3452770c3511a852cc1c862d5f787b4","thickness":"1","color":"rgba(235, 235, 235, 1)","order":43}');
insert into wf_component_data values ('a2d59c6c2668e4124c2883bf31d02ae2', 'validate', '{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('a2d59c6c2668e4124c2883bf31d02ae2', 'display', '{"column":"10","default":"now","order":4}');
insert into wf_component_data values ('a2d59c6c2668e4124c2883bf31d02ae2', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"신청일시"}');
insert into wf_component_data values ('a30a607c9b0d1fd73b1944aad77c4c04', 'display', '{"size":"40","color":"rgba(52,152,219,1)","bold":"Y","italic":"N","underline":"N","align":"left","text":"릴리즈관리","order":1}');
insert into wf_component_data values ('a3452770c3511a852cc1c862d5f787b4', 'display', '{"startId":"a251ce5051063fbf6aba8b1f4a4d7d94","thickness":"1","color":"rgba(235, 235, 235, 1)","order":53}');
insert into wf_component_data values ('a3452770c3511a852cc1c862d5f787b4', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"완료 보고서"}');
insert into wf_component_data values ('a457cdd3533cd9c491fc5f60f117ff59', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"변경예정종료"}');
insert into wf_component_data values ('a457cdd3533cd9c491fc5f60f117ff59', 'display', '{"column":"10","default":"now","order":12}');
insert into wf_component_data values ('a457cdd3533cd9c491fc5f60f117ff59', 'validate', '{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('a4edc285df4003747ec99b7d0d9e6a42', 'validate', '{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('a4edc285df4003747ec99b7d0d9e6a42', 'display', '{"column":"10","default":"now","order":16}');
insert into wf_component_data values ('a4edc285df4003747ec99b7d0d9e6a42', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"접수일시"}');
insert into wf_component_data values ('a4fcf5278decc41890827790aa95380e', 'display', '{"startId":"af3a26739df10f18ac585d2a4e6cf448","thickness":"1","color":"rgba(235, 235, 235, 1)","order":14}');
insert into wf_component_data values ('a4fcf5278decc41890827790aa95380e', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"요청 내역"}');
insert into wf_component_data values ('a5decb84258850cbb3ca9b5789ddb755', 'display', '{"endId":"ad66a326f401e3f3bdc469b363f91822","thickness":"1","color":"rgba(235, 235, 235, 1)","order":32}');
insert into wf_component_data values ('a5decb84258850cbb3ca9b5789ddb755', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"승인 요청"}');
insert into wf_component_data values ('a5ea3ab70d3dbfe3a9203182eec97d99', 'display', '{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":26}');
insert into wf_component_data values ('a5ea3ab70d3dbfe3a9203182eec97d99', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"사전준비작업"}');
insert into wf_component_data values ('a5ea3ab70d3dbfe3a9203182eec97d99', 'validate', '{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('a628a084f571bac3f6f51e333e3a8884', 'display', '{"column":"10","order":52}');
insert into wf_component_data values ('a628a084f571bac3f6f51e333e3a8884', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"첨부파일"}');
insert into wf_component_data values ('a6badd9de24643c8b48cc10cd15c65c5', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"영향도"}');
insert into wf_component_data values ('a6badd9de24643c8b48cc10cd15c65c5', 'display', '{"column":"4","order":8}');
insert into wf_component_data values ('a6badd9de24643c8b48cc10cd15c65c5', 'option', '[{"seq":"1","name":"아주높음","value":"veryHigh"},{"seq":"2","name":"높음","value":"high"},{"seq":"3","name":"중간","value":"middle"},{"seq":"4","name":"낮음","value":"low"}]');
insert into wf_component_data values ('a710e227eb81b4a36da4d555e97a5096', 'display', '{"endId":"a8d276ead25d55cb2786bf28a72faab0","thickness":"1","color":"rgba(235, 235, 235, 1)","order":36}');
insert into wf_component_data values ('a710e227eb81b4a36da4d555e97a5096', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"승인 내역"}');
insert into wf_component_data values ('a755edd239869a2fc974151f1e7144fe', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"접수의견"}');
insert into wf_component_data values ('a755edd239869a2fc974151f1e7144fe', 'validate', '{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('a755edd239869a2fc974151f1e7144fe', 'display', '{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":18}');
insert into wf_component_data values ('a75e73ba2ee7b2298b704c4f56cb65e4', 'display', '{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":21}');
insert into wf_component_data values ('a75e73ba2ee7b2298b704c4f56cb65e4', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"통보계획"}');
insert into wf_component_data values ('a75e73ba2ee7b2298b704c4f56cb65e4', 'validate', '{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('a7802771c99c12360b754662e3ff8955', 'display', '{"column":"10","customCode":"40288a19736b46fb01736b89e46c0008","default":"none","buttonText":"검색","order":34}');
insert into wf_component_data values ('a7802771c99c12360b754662e3ff8955', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"승인자"}');
insert into wf_component_data values ('a7cdb1c39349387fcf58359210e696ea', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"접수 내역"}');
insert into wf_component_data values ('a7cdb1c39349387fcf58359210e696ea', 'display', '{"startId":"a93f8a596c3ffa3ad6265bd36b923dc0","thickness":"1","color":"rgba(235, 235, 235, 1)","order":19}');
insert into wf_component_data values ('a826936604aa74a3744c0c726666b2e0', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"관련서비스"}');
insert into wf_component_data values ('a826936604aa74a3744c0c726666b2e0', 'display', '{"column":"4","order":7}');
insert into wf_component_data values ('a826936604aa74a3744c0c726666b2e0', 'option', '[{"seq":"1","name":"홈페이지","value":"homepage"},{"seq":"2","name":"전자결재시스템","value":"eps"},{"seq":"3","name":"출입관리시스템","value":"acs"},{"seq":"4","name":"ERP","value":"erp"},{"seq":"5","name":"ITSM","value":"itsm"},{"seq":"6","name":"기타","value":"etd"}]');
insert into wf_component_data values ('a83ca580daa743c23e422b952ca035a8', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"작업전 점검 계획"}');
insert into wf_component_data values ('a83ca580daa743c23e422b952ca035a8', 'display', '{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":29}');
insert into wf_component_data values ('a83ca580daa743c23e422b952ca035a8', 'validate', '{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('a840a2f8e71e9098f432b31148be5779', 'option', '[{"seq":"1","name":"중단","value":"y"},{"seq":"2","name":"미중단","value":"n"}]');
insert into wf_component_data values ('a840a2f8e71e9098f432b31148be5779', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"서비스중단"}');
insert into wf_component_data values ('a840a2f8e71e9098f432b31148be5779', 'display', '{"column":"10","direction":"horizontal","position":"right","order":10}');
insert into wf_component_data values ('a87520f43ca016202b01d899a8866f53', 'display', '{"endId":"a8a24ba85095c550505962b36d98c3be","thickness":"1","color":"rgba(235, 235, 235, 1)","order":40}');
insert into wf_component_data values ('a87520f43ca016202b01d899a8866f53', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"작업 결과서"}');
insert into wf_component_data values ('a8898c594ccf9b411f92b6f27d90dd02', 'display', '{"thickness":"5","color":"rgba(88,104,114,1)","type":"solid","order":2}');
insert into wf_component_data values ('a8a24ba85095c550505962b36d98c3be', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"작업 결과서"}');
insert into wf_component_data values ('a8a24ba85095c550505962b36d98c3be', 'display', '{"startId":"a87520f43ca016202b01d899a8866f53","thickness":"1","color":"rgba(235, 235, 235, 1)","order":42}');
insert into wf_component_data values ('a8d276ead25d55cb2786bf28a72faab0', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"승인 내역"}');
insert into wf_component_data values ('a8d276ead25d55cb2786bf28a72faab0', 'display', '{"startId":"a710e227eb81b4a36da4d555e97a5096","thickness":"1","color":"rgba(235, 235, 235, 1)","order":39}');
insert into wf_component_data values ('a910bacf282f3590922cd9670be1d215', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"승인의견"}');
insert into wf_component_data values ('a910bacf282f3590922cd9670be1d215', 'validate', '{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('a910bacf282f3590922cd9670be1d215', 'display', '{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":38}');
insert into wf_component_data values ('a918ef651427c247593817d91ae15c7a', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"신청자"}');
insert into wf_component_data values ('a918ef651427c247593817d91ae15c7a', 'display', '{"column":"10","customCode":"40288a19736b46fb01736b89e46c0008","default":"none","buttonText":"검색","order":5}');
insert into wf_component_data values ('a929ede9bc5e1c9b55e188afa2029798', 'display', '{"placeholder":"","column":"10","default":"none|","order":13}');
insert into wf_component_data values ('a929ede9bc5e1c9b55e188afa2029798', 'validate', '{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"}');
insert into wf_component_data values ('a929ede9bc5e1c9b55e188afa2029798', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"제목"}');
insert into wf_component_data values ('a93f8a596c3ffa3ad6265bd36b923dc0', 'display', '{"endId":"a7cdb1c39349387fcf58359210e696ea","thickness":"1","color":"rgba(235, 235, 235, 1)","order":15}');
insert into wf_component_data values ('a93f8a596c3ffa3ad6265bd36b923dc0', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"접수 내역"}');
insert into wf_component_data values ('a96d3f6b33923e6c45471970d5d235ca', 'display', '{"column":"10","direction":"horizontal","position":"right","order":50}');
insert into wf_component_data values ('a96d3f6b33923e6c45471970d5d235ca', 'option', '[{"seq":"1","name":"Yes","value":"yes"},{"seq":"2","name":"No","value":"no"}]');
insert into wf_component_data values ('a96d3f6b33923e6c45471970d5d235ca', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"모니터링 여부"}');
insert into wf_component_data values ('a99fe87fb279b045646027a0a14b8930', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"작업결과"}');
insert into wf_component_data values ('a99fe87fb279b045646027a0a14b8930', 'option', '[{"seq":"1","name":"성공 종결처리","value":"sucess"},{"seq":"2","name":"실패 종결처리","value":"fail"}]');
insert into wf_component_data values ('a99fe87fb279b045646027a0a14b8930', 'display', '{"column":"10","direction":"horizontal","position":"right","order":48}');
insert into wf_component_data values ('aa273932115bd7d96b008e12330d8570', 'display', '{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":23}');
insert into wf_component_data values ('aa273932115bd7d96b008e12330d8570', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"시험계획"}');
insert into wf_component_data values ('aa273932115bd7d96b008e12330d8570', 'validate', '{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('ab336835f267f16eb2e9a765c9f3d562', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"변경예정시작"}');
insert into wf_component_data values ('ab336835f267f16eb2e9a765c9f3d562', 'validate', '{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('ab336835f267f16eb2e9a765c9f3d562', 'display', '{"column":"10","default":"now","order":11}');
insert into wf_component_data values ('abcf0ce4f3b0265e4d233ec373b66cb8', 'display', '{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":33}');
insert into wf_component_data values ('abcf0ce4f3b0265e4d233ec373b66cb8', 'validate', '{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('abcf0ce4f3b0265e4d233ec373b66cb8', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"요청의견"}');
insert into wf_component_data values ('abdd7d23019680a27eefafa153cd2f5c', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"검토의견"}');
insert into wf_component_data values ('abdd7d23019680a27eefafa153cd2f5c', 'validate', '{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('abdd7d23019680a27eefafa153cd2f5c', 'display', '{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":24}');
insert into wf_component_data values ('abf089973153fbd0a7a0fbec451bd000', 'display', '{"column":"4","order":9}');
insert into wf_component_data values ('abf089973153fbd0a7a0fbec451bd000', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"긴급도"}');
insert into wf_component_data values ('abf089973153fbd0a7a0fbec451bd000', 'option', '[{"seq":"1","name":"1일 이내 처리","value":"1"},{"seq":"2","name":"3일 이내 처리","value":"2"},{"seq":"3","name":"7일 이내 처리","value":"3"},{"seq":"4","name":"7일 이상","value":"4"}]');
insert into wf_component_data values ('ac1c201dc1018243e0aa3bcc7b6fc60a', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"작업 계획"}');
insert into wf_component_data values ('ac1c201dc1018243e0aa3bcc7b6fc60a', 'display', '{"startId":"a1241ec6aabdb13fe23fd435b3fbbb0b","thickness":"1","color":"rgba(235, 235, 235, 1)","order":31}');
insert into wf_component_data values ('acc1f172a41984869823f4495e441ab0', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"관리부서 협의"}');
insert into wf_component_data values ('acc1f172a41984869823f4495e441ab0', 'validate', '{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('acc1f172a41984869823f4495e441ab0', 'display', '{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":28}');
insert into wf_component_data values ('acf974f8c63805a90a194b0dd7665129', 'validate', '{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"}');
insert into wf_component_data values ('acf974f8c63805a90a194b0dd7665129', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"협력사"}');
insert into wf_component_data values ('acf974f8c63805a90a194b0dd7665129', 'display', '{"placeholder":"","column":"10","default":"none|","order":47}');
insert into wf_component_data values ('ad05da15bfa32cd50cdd3e778ae0f250', 'display', '{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":22}');
insert into wf_component_data values ('ad05da15bfa32cd50cdd3e778ae0f250', 'validate', '{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('ad05da15bfa32cd50cdd3e778ae0f250', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"실패시 복구 방안"}');
insert into wf_component_data values ('ad40cc3d789c45285bbfe6c3c2e1bcd7', 'display', '{"column":"10","direction":"horizontal","position":"right","order":49}');
insert into wf_component_data values ('ad40cc3d789c45285bbfe6c3c2e1bcd7', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"서비스 영향"}');
insert into wf_component_data values ('ad40cc3d789c45285bbfe6c3c2e1bcd7', 'option', '[{"seq":"1","name":"Yes","value":"yes"},{"seq":"2","name":"No","value":"no"}]');
insert into wf_component_data values ('ad60787db1a601ea12f17b15ae6daf7b', 'validate', '{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('ad60787db1a601ea12f17b15ae6daf7b', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"실제 시작일"}');
insert into wf_component_data values ('ad60787db1a601ea12f17b15ae6daf7b', 'display', '{"column":"10","default":"now","order":44}');
insert into wf_component_data values ('ad66a326f401e3f3bdc469b363f91822', 'display', '{"startId":"a5decb84258850cbb3ca9b5789ddb755","thickness":"1","color":"rgba(235, 235, 235, 1)","order":35}');
insert into wf_component_data values ('ad66a326f401e3f3bdc469b363f91822', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"승인 요청"}');
insert into wf_component_data values ('af082fb00f2e71c2a27dcc88874d3f57', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"관련부서"}');
insert into wf_component_data values ('af082fb00f2e71c2a27dcc88874d3f57', 'display', '{"placeholder":"","column":"10","default":"none|","order":46}');
insert into wf_component_data values ('af082fb00f2e71c2a27dcc88874d3f57', 'validate', '{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"}');
insert into wf_component_data values ('af325a2f3a2f56413461687d7cba8f20', 'display', '{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":51}');
insert into wf_component_data values ('af325a2f3a2f56413461687d7cba8f20', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"작업결과 내용"}');
insert into wf_component_data values ('af325a2f3a2f56413461687d7cba8f20', 'validate', '{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('af3a26739df10f18ac585d2a4e6cf448', 'display', '{"endId":"a4fcf5278decc41890827790aa95380e","thickness":"1","color":"rgba(235, 235, 235, 1)","order":3}');
insert into wf_component_data values ('af3a26739df10f18ac585d2a4e6cf448', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"요청 내역"}');
insert into wf_component_data values ('af7313650122e67929324cf6c85a0ccd', 'display', '{"column":"10","order":30}');
insert into wf_component_data values ('af7313650122e67929324cf6c85a0ccd', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"첨부파일"}');
insert into wf_component_data values ('af750337e7d5560447d6a47157d75b68', 'label', '{"position":"top","column":"12","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"상세일정등록"}');
insert into wf_component_data values ('af750337e7d5560447d6a47157d75b68', 'header', '{"size":"16","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"center"}');
insert into wf_component_data values ('af750337e7d5560447d6a47157d75b68', 'drTableColumns', '[{"type":"inputbox","display":{"align":"center","placeholder":""},"validate":{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"},"width":"7","text":"작업자"},{"type":"inputbox","display":{"align":"center","placeholder":""},"validate":{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"},"width":"8","text":"실제 시작일"},{"type":"inputbox","display":{"align":"center","placeholder":""},"validate":{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"},"width":"8","text":"실제 종료일"},{"type":"inputbox","display":{"align":"center","placeholder":""},"validate":{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"},"width":"7","text":"작업 결과"}]');
insert into wf_component_data values ('af750337e7d5560447d6a47157d75b68', 'display', '{"column":"12","rowMin":"1","rowMax":"15","border":"rgba(235, 235, 235, 1)","order":41}');
insert into wf_component_data values ('68369e91d73748c295be7c942b2a228d', 'display', '{"size":"40","color":"rgba(52,152,219,1)","bold":"Y","italic":"N","underline":"N","align":"left","text":"인프라 변경관리","order":1}');
insert into wf_component_data values ('0da02528c3574a5a90047944fc99b103', 'display', '{"thickness":"5","color":"rgba(63, 75, 86, 1)","type":"solid","order":2}');
insert into wf_component_data values ('81a12988c7894037b6ffeae5fd024133', 'display', '{"endId":"e8c4685f990c4a39ac51e2d776d8158e","thickness":"1","color":"rgba(235, 235, 235, 1)","order":3}');
insert into wf_component_data values ('81a12988c7894037b6ffeae5fd024133', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"요청 내역"}');
insert into wf_component_data values ('b95569d56dba467a9ebf9ac0d8ca9e09', 'display', '{"column":"10","default":"now","order":4}');
insert into wf_component_data values ('b95569d56dba467a9ebf9ac0d8ca9e09', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"신청일시"}');
insert into wf_component_data values ('b95569d56dba467a9ebf9ac0d8ca9e09', 'validate', '{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('46d4314b543c40f9845a87b3ee6db3d9', 'display', '{"column":"10","customCode":"40288a19736b46fb01736b89e46c0008","default":"none","buttonText":"검색","order":5}');
insert into wf_component_data values ('46d4314b543c40f9845a87b3ee6db3d9', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"신청자"}');
insert into wf_component_data values ('4b499d11338341fe94f0fdb48f97533e', 'display', '{"placeholder":"","column":"10","default":"none|","order":6}');
insert into wf_component_data values ('4b499d11338341fe94f0fdb48f97533e', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"신청부서"}');
insert into wf_component_data values ('4b499d11338341fe94f0fdb48f97533e', 'validate', '{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"}');
insert into wf_component_data values ('d1cedc4c71824025b77a73aab7e6859b', 'display', '{"column":"4","order":7}');
insert into wf_component_data values ('d1cedc4c71824025b77a73aab7e6859b', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"관련서비스"}');
insert into wf_component_data values ('d1cedc4c71824025b77a73aab7e6859b', 'option', '[{"seq":"1","name":"홈페이지","value":"homepage"},{"seq":"2","name":"전자결재시스템","value":"eps"},{"seq":"3","name":"출입관리시스템","value":"acs"},{"seq":"4","name":"ERP","value":"erp"},{"seq":"5","name":"ITSM","value":"itsm"},{"seq":"6","name":"기타","value":"etd"}]');
insert into wf_component_data values ('b7cb55ee46f047c7a11d2bfef92551de', 'display', '{"column":"10","default":"now","order":8}');
insert into wf_component_data values ('b7cb55ee46f047c7a11d2bfef92551de', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"완료희망일시"}');
insert into wf_component_data values ('b7cb55ee46f047c7a11d2bfef92551de', 'validate', '{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('4c55f52ad993410dad36868802ca81b8', 'display', '{"column":"10","customCode":"40288a19736b46fb01736b89e46c0008","default":"none","buttonText":"검색","order":9}');
insert into wf_component_data values ('4c55f52ad993410dad36868802ca81b8', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"담당자"}');
insert into wf_component_data values ('f7f6fac98fed4c539b9535dabea7f03d', 'display', '{"placeholder":"","column":"10","default":"none|","order":10}');
insert into wf_component_data values ('f7f6fac98fed4c539b9535dabea7f03d', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"제목"}');
insert into wf_component_data values ('f7f6fac98fed4c539b9535dabea7f03d', 'validate', '{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"}');
insert into wf_component_data values ('78a67f7784984dc5b503424a4253b195', 'display', '{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":11}');
insert into wf_component_data values ('78a67f7784984dc5b503424a4253b195', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"요청내용"}');
insert into wf_component_data values ('78a67f7784984dc5b503424a4253b195', 'validate', '{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('d60b8b4468874dbab765fcca3bbbcb2f', 'display', '{"column":"12","isEditable":false,"border":"rgba(235, 235, 235, 1)","order":12}');
insert into wf_component_data values ('d60b8b4468874dbab765fcca3bbbcb2f', 'label', '{"position":"top","column":"12","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"관련CI"}');
insert into wf_component_data values ('d60b8b4468874dbab765fcca3bbbcb2f', 'header', '{"size":"16","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"left"}');
insert into wf_component_data values ('4e4059b95b3040079aa476af9bf7c7c6', 'display', '{"column":"10","order":13}');
insert into wf_component_data values ('4e4059b95b3040079aa476af9bf7c7c6', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"첨부파일"}');
insert into wf_component_data values ('e8c4685f990c4a39ac51e2d776d8158e', 'display', '{"startId":"81a12988c7894037b6ffeae5fd024133","thickness":"1","color":"rgba(235, 235, 235, 1)","order":14}');
insert into wf_component_data values ('e8c4685f990c4a39ac51e2d776d8158e', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"요청 내역"}');
insert into wf_component_data values ('70b7b53f0038466aa66373e7e62c464b', 'display', '{"endId":"1fabe796629a4a1fbb566ee0fe13f8bd","thickness":"1","color":"rgba(235, 235, 235, 1)","order":15}');
insert into wf_component_data values ('70b7b53f0038466aa66373e7e62c464b', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"접수 내역"}');
insert into wf_component_data values ('6afdcd6948bc40639c26a037f3844a06', 'display', '{"column":"10","default":"now","order":16}');
insert into wf_component_data values ('6afdcd6948bc40639c26a037f3844a06', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"접수일시"}');
insert into wf_component_data values ('6afdcd6948bc40639c26a037f3844a06', 'validate', '{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('126d1bc226764910863a2db5198fa3a1', 'display', '{"column":"4","order":17}');
insert into wf_component_data values ('126d1bc226764910863a2db5198fa3a1', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"영향도"}');
insert into wf_component_data values ('126d1bc226764910863a2db5198fa3a1', 'option', '[{"seq":"1","name":"아주높음","value":"veryHigh"},{"seq":"2","name":"높음","value":"high"},{"seq":"3","name":"중간","value":"middle"},{"seq":"4","name":"낮음","value":"low"}]');
insert into wf_component_data values ('a65ec7e28d5548ab8948c9b45242ad92', 'display', '{"column":"4","order":18}');
insert into wf_component_data values ('a65ec7e28d5548ab8948c9b45242ad92', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"긴급도"}');
insert into wf_component_data values ('a65ec7e28d5548ab8948c9b45242ad92', 'option', '[{"seq":1,"name":"1일 이내 처리","value":"1"},{"seq":2,"name":"3일 이내 처리","value":"2"},{"seq":3,"name":"7일 이내 처리","value":"3"},{"seq":4,"name":"7일 이상","value":"4"}]');
insert into wf_component_data values ('e8a4310cfc1d4abdb50677ebd19fde93', 'display', '{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":19}');
insert into wf_component_data values ('e8a4310cfc1d4abdb50677ebd19fde93', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"접수의견"}');
insert into wf_component_data values ('e8a4310cfc1d4abdb50677ebd19fde93', 'validate', '{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('1fabe796629a4a1fbb566ee0fe13f8bd', 'display', '{"startId":"70b7b53f0038466aa66373e7e62c464b","thickness":"1","color":"rgba(235, 235, 235, 1)","order":20}');
insert into wf_component_data values ('1fabe796629a4a1fbb566ee0fe13f8bd', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"접수 내역"}');
insert into wf_component_data values ('8135a66efebc4afa83790369d27fe803', 'display', '{"endId":"566d27971d424f6ebcac7e906af5f522","thickness":"1","color":"rgba(235, 235, 235, 1)","order":21}');
insert into wf_component_data values ('8135a66efebc4afa83790369d27fe803', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"변경 계획서"}');
insert into wf_component_data values ('77bdc771389f43b8b10a0b5b8bb0466c', 'display', '{"column":"10","default":"now","order":22}');
insert into wf_component_data values ('77bdc771389f43b8b10a0b5b8bb0466c', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"변경 계획서 작성일시"}');
insert into wf_component_data values ('77bdc771389f43b8b10a0b5b8bb0466c', 'validate', '{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('4168f17234ca464a9e53c31b47cf2c26', 'display', '{"column":"10","customCode":"40288a19736b46fb01736b89e46c0008","default":"none","buttonText":"검색","order":23}');
insert into wf_component_data values ('4168f17234ca464a9e53c31b47cf2c26', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"릴리즈담당자"}');
insert into wf_component_data values ('f9a79b39fac041f8979eb394e6d17b0d', 'display', '{"column":"10","default":"now","order":24}');
insert into wf_component_data values ('f9a79b39fac041f8979eb394e6d17b0d', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"변경예정시작"}');
insert into wf_component_data values ('f9a79b39fac041f8979eb394e6d17b0d', 'validate', '{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('70082734af8a49efb7f23c3be21af0d4', 'display', '{"column":"10","default":"now","order":25}');
insert into wf_component_data values ('70082734af8a49efb7f23c3be21af0d4', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"변경예정종료"}');
insert into wf_component_data values ('70082734af8a49efb7f23c3be21af0d4', 'validate', '{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('b845c1bbc48443bfab1f556d9b5d2f96', 'display', '{"column":"10","direction":"horizontal","position":"right","order":26}');
insert into wf_component_data values ('b845c1bbc48443bfab1f556d9b5d2f96', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"서비스중단"}');
insert into wf_component_data values ('b845c1bbc48443bfab1f556d9b5d2f96', 'option', '[{"seq":"1","name":"중단","value":"y"},{"seq":"2","name":"미중단","value":"n"}]');
insert into wf_component_data values ('fb9736fc3e364a2bbde711d7baa4c86a', 'display', '{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":27}');
insert into wf_component_data values ('fb9736fc3e364a2bbde711d7baa4c86a', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"변경작업계획"}');
insert into wf_component_data values ('fb9736fc3e364a2bbde711d7baa4c86a', 'validate', '{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('88adaa894c034517a6b8e5faca94aee1', 'display', '{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":28}');
insert into wf_component_data values ('88adaa894c034517a6b8e5faca94aee1', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"실패시 복구방안"}');
insert into wf_component_data values ('88adaa894c034517a6b8e5faca94aee1', 'validate', '{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('ad9bbf828a0640c1870f517830298fc9', 'display', '{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":29}');
insert into wf_component_data values ('ad9bbf828a0640c1870f517830298fc9', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"시험계획"}');
insert into wf_component_data values ('ad9bbf828a0640c1870f517830298fc9', 'validate', '{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('2567d59617ff4c3abb9c8df6dc021de9', 'display', '{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":30}');
insert into wf_component_data values ('2567d59617ff4c3abb9c8df6dc021de9', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"기타의견"}');
insert into wf_component_data values ('2567d59617ff4c3abb9c8df6dc021de9', 'validate', '{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('f5e9c0f86f0b43d887978787f78aaffe', 'display', '{"column":"12","isEditable":false,"border":"rgba(235, 235, 235, 1)","order":31}');
insert into wf_component_data values ('f5e9c0f86f0b43d887978787f78aaffe', 'label', '{"position":"top","column":"12","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"관련CI"}');
insert into wf_component_data values ('f5e9c0f86f0b43d887978787f78aaffe', 'header', '{"size":"16","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"left"}');
insert into wf_component_data values ('1d10da32376d48a4aeec5bdd5855064d', 'display', '{"column":"10","order":32}');
insert into wf_component_data values ('1d10da32376d48a4aeec5bdd5855064d', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"첨부파일"}');
insert into wf_component_data values ('566d27971d424f6ebcac7e906af5f522', 'display', '{"startId":"8135a66efebc4afa83790369d27fe803","thickness":"1","color":"rgba(235, 235, 235, 1)","order":33}');
insert into wf_component_data values ('566d27971d424f6ebcac7e906af5f522', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"변경 계획서"}');
insert into wf_component_data values ('3b863fc7f81149c5a7fdbc3c3fb17864', 'display', '{"endId":"6bea8026826443818b96302898b59eb0","thickness":"1","color":"rgba(235, 235, 235, 1)","order":34}');
insert into wf_component_data values ('3b863fc7f81149c5a7fdbc3c3fb17864', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"left","text":"변경 자문 회의록"}');
insert into wf_component_data values ('6e14fc72a91c454585d95d7a5b1d6740', 'display', '{"placeholder":"","column":"10","default":"none|","order":35}');
insert into wf_component_data values ('6e14fc72a91c454585d95d7a5b1d6740', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"회의장소"}');
insert into wf_component_data values ('6e14fc72a91c454585d95d7a5b1d6740', 'validate', '{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"}');
insert into wf_component_data values ('1e83c938aa794f7d9ac0b4be129fa8b8', 'display', '{"column":"10","default":"now","order":36}');
insert into wf_component_data values ('1e83c938aa794f7d9ac0b4be129fa8b8', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"회의일시"}');
insert into wf_component_data values ('1e83c938aa794f7d9ac0b4be129fa8b8', 'validate', '{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('7e8c9bc421ab4822bf51f435ee27f07c', 'display', '{"placeholder":"","column":"10","default":"none|","order":37}');
insert into wf_component_data values ('7e8c9bc421ab4822bf51f435ee27f07c', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"참석자"}');
insert into wf_component_data values ('7e8c9bc421ab4822bf51f435ee27f07c', 'validate', '{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"}');
insert into wf_component_data values ('a70d29a292f24b1d91db3f0af886808c', 'display', '{"placeholder":"","column":"10","default":"none|","order":38}');
insert into wf_component_data values ('a70d29a292f24b1d91db3f0af886808c', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"주제"}');
insert into wf_component_data values ('a70d29a292f24b1d91db3f0af886808c', 'validate', '{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"}');
insert into wf_component_data values ('16efb22300754fa8912c4ae96f7d880d', 'display', '{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":39}');
insert into wf_component_data values ('16efb22300754fa8912c4ae96f7d880d', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"협의사항"}');
insert into wf_component_data values ('16efb22300754fa8912c4ae96f7d880d', 'validate', '{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('a63b6c2b2db2430984cabe3f9664f65e', 'display', '{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":40}');
insert into wf_component_data values ('a63b6c2b2db2430984cabe3f9664f65e', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"회의결과"}');
insert into wf_component_data values ('a63b6c2b2db2430984cabe3f9664f65e', 'validate', '{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('6bea8026826443818b96302898b59eb0', 'display', '{"startId":"3b863fc7f81149c5a7fdbc3c3fb17864","thickness":"1","color":"rgba(235, 235, 235, 1)","order":41}');
insert into wf_component_data values ('6bea8026826443818b96302898b59eb0', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"left","text":"변경 자문 회의록"}');
insert into wf_component_data values ('72e1568e821049e3a559bfcfa8608c5f', 'display', '{"endId":"8b2ba2fdc8804624836a93800b96f1c0","thickness":"1","color":"rgba(235, 235, 235, 1)","order":42}');
insert into wf_component_data values ('72e1568e821049e3a559bfcfa8608c5f', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"left","text":"승인 요청"}');
insert into wf_component_data values ('df358ee2bb18415da12d61b7623bc00f', 'display', '{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":43}');
insert into wf_component_data values ('df358ee2bb18415da12d61b7623bc00f', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"요청의견"}');
insert into wf_component_data values ('df358ee2bb18415da12d61b7623bc00f', 'validate', '{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('2f47ce3e9f4541c6b597af1f1c894114', 'display', '{"column":"10","customCode":"40288a19736b46fb01736b89e46c0008","default":"none","buttonText":"검색","order":44}');
insert into wf_component_data values ('2f47ce3e9f4541c6b597af1f1c894114', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"승인자"}');
insert into wf_component_data values ('8b2ba2fdc8804624836a93800b96f1c0', 'display', '{"startId":"72e1568e821049e3a559bfcfa8608c5f","thickness":"1","color":"rgba(235, 235, 235, 1)","order":45}');
insert into wf_component_data values ('8b2ba2fdc8804624836a93800b96f1c0', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"left","text":"승인 요청"}');
insert into wf_component_data values ('5f853fd6493a440bb87bad6608c3cfa0', 'display', '{"endId":"adbd729b6ec447769e11cbdaee0a90bf","thickness":"1","color":"rgba(235, 235, 235, 1)","order":46}');
insert into wf_component_data values ('5f853fd6493a440bb87bad6608c3cfa0', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"left","text":"승인 내역"}');
insert into wf_component_data values ('bd131acab7844bbc86daddfccd0b9424', 'display', '{"column":"10","default":"now","order":47}');
insert into wf_component_data values ('bd131acab7844bbc86daddfccd0b9424', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"승인일시"}');
insert into wf_component_data values ('bd131acab7844bbc86daddfccd0b9424', 'validate', '{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('cb31c114117944bbba596e01bd4a2baf', 'display', '{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":48}');
insert into wf_component_data values ('cb31c114117944bbba596e01bd4a2baf', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"승인의견"}');
insert into wf_component_data values ('cb31c114117944bbba596e01bd4a2baf', 'validate', '{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('adbd729b6ec447769e11cbdaee0a90bf', 'display', '{"startId":"5f853fd6493a440bb87bad6608c3cfa0","thickness":"1","color":"rgba(235, 235, 235, 1)","order":49}');
insert into wf_component_data values ('adbd729b6ec447769e11cbdaee0a90bf', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"left","text":"승인 내역"}');
insert into wf_component_data values ('493a61cf4eec43ce93799ab060a8e31a', 'display', '{"endId":"2b93c546bf814c908a92a40316ebf0fb","thickness":"1","color":"rgba(235, 235, 235, 1)","order":50}');
insert into wf_component_data values ('493a61cf4eec43ce93799ab060a8e31a', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"left","text":"변경 완료 보고서"}');
insert into wf_component_data values ('a434c0324f3d4f96ae77fc35d3386b23', 'display', '{"column":"10","direction":"horizontal","position":"right","order":51}');
insert into wf_component_data values ('a434c0324f3d4f96ae77fc35d3386b23', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"변경작업결과"}');
insert into wf_component_data values ('a434c0324f3d4f96ae77fc35d3386b23', 'option', '[{"seq":"1","name":"성공종결","value":"s"},{"seq":"2","name":"실패종결","value":"f"}]');
insert into wf_component_data values ('be386a19c49b4aeb8c5aedc0313a9df5', 'display', '{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":52}');
insert into wf_component_data values ('be386a19c49b4aeb8c5aedc0313a9df5', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"내용"}');
insert into wf_component_data values ('be386a19c49b4aeb8c5aedc0313a9df5', 'validate', '{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('adeb5fc671fd411d899bcd5f815ada53', 'display', '{"column":"10","order":53}');
insert into wf_component_data values ('adeb5fc671fd411d899bcd5f815ada53', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"첨부파일"}');
insert into wf_component_data values ('2b93c546bf814c908a92a40316ebf0fb', 'display', '{"startId":"493a61cf4eec43ce93799ab060a8e31a","thickness":"1","color":"rgba(235, 235, 235, 1)","order":54}');
insert into wf_component_data values ('2b93c546bf814c908a92a40316ebf0fb', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"left","text":"변경 완료 보고서"}');
insert into wf_component_data values ('adcf595a433043c1bb41f0d5fd649504', 'display', '{"endId":"52fb168ce6db474b8507e77144e256b2","thickness":"1","color":"rgba(235, 235, 235, 1)","order":55}');
insert into wf_component_data values ('adcf595a433043c1bb41f0d5fd649504', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"left","text":"승인 요청"}');
insert into wf_component_data values ('bf83695a89684561bb21bb0f8b22a619', 'display', '{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":56}');
insert into wf_component_data values ('bf83695a89684561bb21bb0f8b22a619', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"요청의견"}');
insert into wf_component_data values ('bf83695a89684561bb21bb0f8b22a619', 'validate', '{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('2e5f623b4b244f21b7230364252acc05', 'display', '{"column":"10","customCode":"40288a19736b46fb01736b89e46c0008","default":"none","buttonText":"검색","order":57}');
insert into wf_component_data values ('2e5f623b4b244f21b7230364252acc05', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"승인자"}');
insert into wf_component_data values ('52fb168ce6db474b8507e77144e256b2', 'display', '{"startId":"adcf595a433043c1bb41f0d5fd649504","thickness":"1","color":"rgba(235, 235, 235, 1)","order":58}');
insert into wf_component_data values ('52fb168ce6db474b8507e77144e256b2', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"left","text":"승인 요청"}');
insert into wf_component_data values ('fc4c35ef2332453ba18cc39bef378b26', 'display', '{"endId":"6b7e7e14001b42d9957cfd1e3aeca64a","thickness":"1","color":"rgba(235, 235, 235, 1)","order":59}');
insert into wf_component_data values ('fc4c35ef2332453ba18cc39bef378b26', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"left","text":"승인 내역"}');
insert into wf_component_data values ('2d805698376849bfb4d0e28b389477df', 'display', '{"column":"10","default":"now","order":60}');
insert into wf_component_data values ('2d805698376849bfb4d0e28b389477df', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"승인일시"}');
insert into wf_component_data values ('2d805698376849bfb4d0e28b389477df', 'validate', '{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('215cada954bf44e1b0f03f02e44c63db', 'display', '{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":61}');
insert into wf_component_data values ('215cada954bf44e1b0f03f02e44c63db', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"승인의견"}');
insert into wf_component_data values ('215cada954bf44e1b0f03f02e44c63db', 'validate', '{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('6b7e7e14001b42d9957cfd1e3aeca64a', 'display', '{"startId":"fc4c35ef2332453ba18cc39bef378b26","thickness":"1","color":"rgba(235, 235, 235, 1)","order":62}');
insert into wf_component_data values ('6b7e7e14001b42d9957cfd1e3aeca64a', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"left","text":"승인 내역"}');
insert into wf_component_data values ('863aab52c69b48f3ad40fa378ed1f136', 'display', '{"size":"40","color":"rgba(52,152,219,1)","bold":"Y","italic":"N","underline":"N","align":"left","text":"어플리케이션 변경관리","order":1}');
insert into wf_component_data values ('a8af055054d14330bc32ebde17bca561', 'display', '{"thickness":"5","color":"rgba(63, 75, 86, 1)","type":"solid","order":2}');
insert into wf_component_data values ('f945571183c5475591ee2abe96db657a', 'display', '{"endId":"132843274c11498abf2d384d0b7cd778","thickness":"1","color":"rgba(235, 235, 235, 1)","order":3}');
insert into wf_component_data values ('f945571183c5475591ee2abe96db657a', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"요청 내역"}');
insert into wf_component_data values ('5619598930384c76bbfd12f246a7ed2d', 'display', '{"column":"10","default":"now","order":4}');
insert into wf_component_data values ('5619598930384c76bbfd12f246a7ed2d', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"신청일시"}');
insert into wf_component_data values ('5619598930384c76bbfd12f246a7ed2d', 'validate', '{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('ab56589b5c154ee9829054081516f211', 'display', '{"column":"10","customCode":"40288a19736b46fb01736b89e46c0008","default":"none","buttonText":"검색","order":5}');
insert into wf_component_data values ('ab56589b5c154ee9829054081516f211', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"신청자"}');
insert into wf_component_data values ('c89f663fad094c749810e6e268645bae', 'display', '{"placeholder":"","column":"10","default":"none|","order":6}');
insert into wf_component_data values ('c89f663fad094c749810e6e268645bae', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"신청부서"}');
insert into wf_component_data values ('c89f663fad094c749810e6e268645bae', 'validate', '{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"}');
insert into wf_component_data values ('d4fa12ff8a7a462992865e419bffcd93', 'display', '{"column":"4","order":7}');
insert into wf_component_data values ('d4fa12ff8a7a462992865e419bffcd93', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"관련서비스"}');
insert into wf_component_data values ('d4fa12ff8a7a462992865e419bffcd93', 'option', '[{"seq":"1","name":"홈페이지","value":"homepage"},{"seq":"2","name":"전자결재시스템","value":"eps"},{"seq":"3","name":"출입관리시스템","value":"acs"},{"seq":"4","name":"ERP","value":"erp"},{"seq":"5","name":"ITSM","value":"itsm"},{"seq":"6","name":"기타","value":"etd"}]');
insert into wf_component_data values ('d49d626d97ae47938928bdf2162bd54a', 'display', '{"column":"10","default":"now","order":8}');
insert into wf_component_data values ('d49d626d97ae47938928bdf2162bd54a', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"완료희망일시"}');
insert into wf_component_data values ('d49d626d97ae47938928bdf2162bd54a', 'validate', '{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('8cbae4ebe4e34e0199dc0f7e521bb8b3', 'display', '{"column":"10","customCode":"40288a19736b46fb01736b89e46c0008","default":"none","buttonText":"검색","order":9}');
insert into wf_component_data values ('8cbae4ebe4e34e0199dc0f7e521bb8b3', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"담당자"}');
insert into wf_component_data values ('06aa9e655148405ea88fc65d1af2f07d', 'display', '{"placeholder":"","column":"10","default":"none|","order":10}');
insert into wf_component_data values ('06aa9e655148405ea88fc65d1af2f07d', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"제목"}');
insert into wf_component_data values ('06aa9e655148405ea88fc65d1af2f07d', 'validate', '{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"}');
insert into wf_component_data values ('8face0b52c974369bfc2d4f3daac42c3', 'display', '{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":11}');
insert into wf_component_data values ('8face0b52c974369bfc2d4f3daac42c3', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"요청내용"}');
insert into wf_component_data values ('8face0b52c974369bfc2d4f3daac42c3', 'validate', '{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('5aa90376561f44c7b23474f80a3b9bb3', 'display', '{"column":"12","isEditable":false,"border":"rgba(235, 235, 235, 1)","order":12}');
insert into wf_component_data values ('5aa90376561f44c7b23474f80a3b9bb3', 'label', '{"position":"top","column":"12","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"관련CI"}');
insert into wf_component_data values ('5aa90376561f44c7b23474f80a3b9bb3', 'header', '{"size":"16","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"left"}');
insert into wf_component_data values ('fdb3b5aec66d4d0286aa39d5306905f1', 'display', '{"column":"10","order":13}');
insert into wf_component_data values ('fdb3b5aec66d4d0286aa39d5306905f1', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"첨부파일"}');
insert into wf_component_data values ('132843274c11498abf2d384d0b7cd778', 'display', '{"startId":"f945571183c5475591ee2abe96db657a","thickness":"1","color":"rgba(235, 235, 235, 1)","order":14}');
insert into wf_component_data values ('132843274c11498abf2d384d0b7cd778', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"요청 내역"}');
insert into wf_component_data values ('c72bc134e9b94e6aadb9c043a4e51e26', 'display', '{"endId":"fc5df21295ff456e81044a0c4f6e2678","thickness":"1","color":"rgba(235, 235, 235, 1)","order":15}');
insert into wf_component_data values ('c72bc134e9b94e6aadb9c043a4e51e26', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"접수 내역"}');
insert into wf_component_data values ('58f8ef1520d8432783c849060943bd7d', 'display', '{"column":"10","default":"now","order":16}');
insert into wf_component_data values ('58f8ef1520d8432783c849060943bd7d', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"접수일시"}');
insert into wf_component_data values ('58f8ef1520d8432783c849060943bd7d', 'validate', '{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('d3e9b29dd46c4e0b9fb5993534de4f2a', 'display', '{"column":"4","order":17}');
insert into wf_component_data values ('d3e9b29dd46c4e0b9fb5993534de4f2a', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"영향도"}');
insert into wf_component_data values ('d3e9b29dd46c4e0b9fb5993534de4f2a', 'option', '[{"seq":"1","name":"아주높음","value":"veryHigh"},{"seq":"2","name":"높음","value":"high"},{"seq":"3","name":"중간","value":"middle"},{"seq":"4","name":"낮음","value":"low"}]');
insert into wf_component_data values ('3defaf1ea63c4ad5a46572da0497bff7', 'display', '{"column":"4","order":18}');
insert into wf_component_data values ('3defaf1ea63c4ad5a46572da0497bff7', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"긴급도"}');
insert into wf_component_data values ('3defaf1ea63c4ad5a46572da0497bff7', 'option', '[{"seq":1,"name":"1일 이내 처리","value":"1"},{"seq":2,"name":"3일 이내 처리","value":"2"},{"seq":3,"name":"7일 이내 처리","value":"3"},{"seq":4,"name":"7일 이상","value":"4"}]');
insert into wf_component_data values ('dca5ef1fac3c42bab5407f3211857f6e', 'display', '{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":19}');
insert into wf_component_data values ('a8c08b7ff66e94bab4ae5c9ddac77b07', 'display', '{"column":"10","default":"now","order":52}');
insert into wf_component_data values ('dca5ef1fac3c42bab5407f3211857f6e', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"접수의견"}');
insert into wf_component_data values ('dca5ef1fac3c42bab5407f3211857f6e', 'validate', '{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('fc5df21295ff456e81044a0c4f6e2678', 'display', '{"startId":"c72bc134e9b94e6aadb9c043a4e51e26","thickness":"1","color":"rgba(235, 235, 235, 1)","order":20}');
insert into wf_component_data values ('fc5df21295ff456e81044a0c4f6e2678', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"접수 내역"}');
insert into wf_component_data values ('893d7bf6289e49c4b02542b33faedeec', 'display', '{"endId":"5e462c62e5bb44dcae86fa61e939198f","thickness":"1","color":"rgba(235, 235, 235, 1)","order":21}');
insert into wf_component_data values ('893d7bf6289e49c4b02542b33faedeec', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"개발 계획서"}');
insert into wf_component_data values ('4c50c453a14d4df6b07d2cb5b83f6353', 'display', '{"column":"10","default":"now","order":22}');
insert into wf_component_data values ('4c50c453a14d4df6b07d2cb5b83f6353', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"변경 계획서 작성일시"}');
insert into wf_component_data values ('4c50c453a14d4df6b07d2cb5b83f6353', 'validate', '{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('636bc689c20440489786d068719c0b8a', 'display', '{"column":"10","customCode":"40288a19736b46fb01736b89e46c0008","default":"none","buttonText":"검색","order":23}');
insert into wf_component_data values ('636bc689c20440489786d068719c0b8a', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"릴리즈담당자"}');
insert into wf_component_data values ('4a28390d2e4c48b9b55c13048f584910', 'display', '{"column":"10","default":"now","order":24}');
insert into wf_component_data values ('4a28390d2e4c48b9b55c13048f584910', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"변경예정시작"}');
insert into wf_component_data values ('4a28390d2e4c48b9b55c13048f584910', 'validate', '{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('dbaa5d0552dd489390a5c58883f9637b', 'display', '{"column":"10","default":"now","order":25}');
insert into wf_component_data values ('dbaa5d0552dd489390a5c58883f9637b', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"변경예정종료"}');
insert into wf_component_data values ('dbaa5d0552dd489390a5c58883f9637b', 'validate', '{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('03ef421c8aff438fabd8802c27c3f9a0', 'display', '{"column":"10","direction":"horizontal","position":"right","order":26}');
insert into wf_component_data values ('03ef421c8aff438fabd8802c27c3f9a0', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"서비스중단"}');
insert into wf_component_data values ('03ef421c8aff438fabd8802c27c3f9a0', 'option', '[{"seq":"1","name":"중단","value":"y"},{"seq":"2","name":"미중단","value":"n"}]');
insert into wf_component_data values ('a1a5b7e4cb47a9bcd8c2683ab5ea2670', 'display', '{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":27}');
insert into wf_component_data values ('a1a5b7e4cb47a9bcd8c2683ab5ea2670', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"변경요청분석내용"}');
insert into wf_component_data values ('a1a5b7e4cb47a9bcd8c2683ab5ea2670', 'validate', '{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('a141173392185a23c919f2efed84a9ab', 'display', '{"column":"12","rowMin":"1","rowMax":"15","border":"rgba(235, 235, 235, 1)","order":28}');
insert into wf_component_data values ('a141173392185a23c919f2efed84a9ab', 'label', '{"position":"top","column":"12","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"개발투입일정"}');
insert into wf_component_data values ('a141173392185a23c919f2efed84a9ab', 'header', '{"size":"16","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"center"}');
insert into wf_component_data values ('a141173392185a23c919f2efed84a9ab', 'drTableColumns', '[{"type":"inputbox","display":{"align":"center","placeholder":""},"validate":{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"},"width":"12","text":"구분"},{"type":"inputbox","display":{"align":"center","placeholder":""},"validate":{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"},"width":"12","text":"투입인원"},{"type":"inputbox","display":{"align":"center","placeholder":""},"validate":{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"},"width":"12","text":"From"},{"type":"inputbox","display":{"align":"center","placeholder":""},"validate":{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"},"width":"12","text":"To"},{"type":"inputbox","display":{"align":"center","placeholder":""},"validate":{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"},"width":"12","text":"workDay"}]');
insert into wf_component_data values ('a3dd667185aa125018e81cdf90493e44', 'display', '{"column":"10","customCode":"40288a19736b46fb01736b89e46c0008","default":"none","buttonText":"검색","order":29}');
insert into wf_component_data values ('a3dd667185aa125018e81cdf90493e44', 'label', '{"position":"left","column":"2","size":"16","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"분석담당자"}');
insert into wf_component_data values ('ad0227ad0079b5dca85f452fe33b807d', 'display', '{"column":"10","customCode":"40288a19736b46fb01736b89e46c0008","default":"none","buttonText":"검색","order":30}');
insert into wf_component_data values ('ad0227ad0079b5dca85f452fe33b807d', 'label', '{"position":"left","column":"2","size":"16","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"구현담당자"}');
insert into wf_component_data values ('abeee66f95e85ae97e684ee0b56da431', 'display', '{"column":"10","customCode":"40288a19736b46fb01736b89e46c0008","default":"none","buttonText":"검색","order":31}');
insert into wf_component_data values ('abeee66f95e85ae97e684ee0b56da431', 'label', '{"position":"left","column":"2","size":"16","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"테스트담당자"}');
insert into wf_component_data values ('a26001b80809c72b05238532ff989e51', 'display', '{"column":"10","customCode":"40288a19736b46fb01736b89e46c0008","default":"none","buttonText":"검색","order":32}');
insert into wf_component_data values ('a26001b80809c72b05238532ff989e51', 'label', '{"position":"left","column":"2","size":"16","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"현업담당자"}');
insert into wf_component_data values ('a95e849bce093c1cbea7a887c8a750fe', 'display', '{"column":"12","rowMin":"1","rowMax":"15","border":"rgba(235, 235, 235, 1)","order":33}');
insert into wf_component_data values ('a95e849bce093c1cbea7a887c8a750fe', 'label', '{"position":"top","column":"12","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"자원입력"}');
insert into wf_component_data values ('a95e849bce093c1cbea7a887c8a750fe', 'header', '{"size":"16","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"center"}');
insert into wf_component_data values ('a8c08b7ff66e94bab4ae5c9ddac77b07', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"투입종료일시"}');
insert into wf_component_data values ('a8c08b7ff66e94bab4ae5c9ddac77b07', 'validate', '{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('a95e849bce093c1cbea7a887c8a750fe', 'drTableColumns', '[{"type":"inputbox","display":{"align":"center","placeholder":""},"validate":{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"},"width":"12","text":"자원ID"},{"type":"inputbox","display":{"align":"center","placeholder":""},"validate":{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"},"width":"12","text":"자원명"},{"type":"inputbox","display":{"align":"center","placeholder":""},"validate":{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"},"width":"12","text":"자원위치"},{"type":"inputbox","display":{"align":"center","placeholder":""},"validate":{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"},"width":"4","text":"버전"}]');
insert into wf_component_data values ('9793a1313c6046a281253143f24d64ac', 'display', '{"column":"12","isEditable":false,"border":"rgba(235, 235, 235, 1)","order":34}');
insert into wf_component_data values ('9793a1313c6046a281253143f24d64ac', 'label', '{"position":"top","column":"12","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"관련CI"}');
insert into wf_component_data values ('9793a1313c6046a281253143f24d64ac', 'header', '{"size":"16","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"left"}');
insert into wf_component_data values ('30d12a1db5b94969b349d7ea24d574c5', 'display', '{"column":"10","order":35}');
insert into wf_component_data values ('30d12a1db5b94969b349d7ea24d574c5', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"첨부파일"}');
insert into wf_component_data values ('5e462c62e5bb44dcae86fa61e939198f', 'display', '{"startId":"893d7bf6289e49c4b02542b33faedeec","thickness":"1","color":"rgba(235, 235, 235, 1)","order":36}');
insert into wf_component_data values ('5e462c62e5bb44dcae86fa61e939198f', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"개발 계획서"}');
insert into wf_component_data values ('f8ffe25354e74196a90dfef3652a3e7d', 'display', '{"endId":"44eb35f7b3904d928dc502430e98f665","thickness":"1","color":"rgba(235, 235, 235, 1)","order":37}');
insert into wf_component_data values ('f8ffe25354e74196a90dfef3652a3e7d', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"left","text":"승인 요청"}');
insert into wf_component_data values ('639243d7d02a446e8d383ec3d452ffa3', 'display', '{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":38}');
insert into wf_component_data values ('639243d7d02a446e8d383ec3d452ffa3', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"요청의견"}');
insert into wf_component_data values ('639243d7d02a446e8d383ec3d452ffa3', 'validate', '{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('2d872066d25f4681b31a77bf6495ab12', 'display', '{"column":"10","customCode":"40288a19736b46fb01736b89e46c0008","default":"none","buttonText":"검색","order":39}');
insert into wf_component_data values ('2d872066d25f4681b31a77bf6495ab12', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"승인자"}');
insert into wf_component_data values ('44eb35f7b3904d928dc502430e98f665', 'display', '{"startId":"f8ffe25354e74196a90dfef3652a3e7d","thickness":"1","color":"rgba(235, 235, 235, 1)","order":40}');
insert into wf_component_data values ('44eb35f7b3904d928dc502430e98f665', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"left","text":"승인 요청"}');
insert into wf_component_data values ('a5553eb7480f43c29f971b0e93804a02', 'display', '{"endId":"c5e47b036bf54e64b7c974a7770034ad","thickness":"1","color":"rgba(235, 235, 235, 1)","order":41}');
insert into wf_component_data values ('a5553eb7480f43c29f971b0e93804a02', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"left","text":"승인 내역"}');
insert into wf_component_data values ('2cb8e92e4da8405d9e9363d651051ed0', 'display', '{"column":"10","default":"now","order":42}');
insert into wf_component_data values ('2cb8e92e4da8405d9e9363d651051ed0', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"승인일시"}');
insert into wf_component_data values ('2cb8e92e4da8405d9e9363d651051ed0', 'validate', '{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('c8dc1e3013c941dda44aee2ad8ff5bff', 'display', '{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":43}');
insert into wf_component_data values ('c8dc1e3013c941dda44aee2ad8ff5bff', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"승인의견"}');
insert into wf_component_data values ('c8dc1e3013c941dda44aee2ad8ff5bff', 'validate', '{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('c5e47b036bf54e64b7c974a7770034ad', 'display', '{"startId":"a5553eb7480f43c29f971b0e93804a02","thickness":"1","color":"rgba(235, 235, 235, 1)","order":44}');
insert into wf_component_data values ('c5e47b036bf54e64b7c974a7770034ad', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"left","text":"승인 내역"}');
insert into wf_component_data values ('aa0e47d9df139ef6a0a30c6cbf1a20f0', 'display', '{"endId":"a7d9c0535f49834bcc5d01fd3d1ad7df","thickness":"1","color":"rgba(235, 235, 235, 1)","order":45}');
insert into wf_component_data values ('aa0e47d9df139ef6a0a30c6cbf1a20f0', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"설계 검증 검토 내역"}');
insert into wf_component_data values ('a9a5213c5d2ff5d04a93f5d22b214c0a', 'display', '{"column":"10","default":"now","order":46}');
insert into wf_component_data values ('a9a5213c5d2ff5d04a93f5d22b214c0a', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"작성일시"}');
insert into wf_component_data values ('a9a5213c5d2ff5d04a93f5d22b214c0a', 'validate', '{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('acacad22fad99755cd0668c7eb00040d', 'display', '{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":47}');
insert into wf_component_data values ('acacad22fad99755cd0668c7eb00040d', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"검토내용"}');
insert into wf_component_data values ('acacad22fad99755cd0668c7eb00040d', 'validate', '{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('aba545803a0b342007bb193abbb414e5', 'display', '{"column":"10","order":48}');
insert into wf_component_data values ('aba545803a0b342007bb193abbb414e5', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"첨부파일"}');
insert into wf_component_data values ('a7d9c0535f49834bcc5d01fd3d1ad7df', 'display', '{"startId":"aa0e47d9df139ef6a0a30c6cbf1a20f0","thickness":"1","color":"rgba(235, 235, 235, 1)","order":49}');
insert into wf_component_data values ('a7d9c0535f49834bcc5d01fd3d1ad7df', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"설계 검증 검토 내역"}');
insert into wf_component_data values ('aba91068bc801ce3f2dbfda7c3a313a2', 'display', '{"endId":"a916992162c9f0747690fabc416098c3","thickness":"1","color":"rgba(235, 235, 235, 1)","order":50}');
insert into wf_component_data values ('aba91068bc801ce3f2dbfda7c3a313a2', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"구현 내역"}');
insert into wf_component_data values ('a125e83ece531b99d031c78847d340a8', 'display', '{"column":"10","default":"now","order":51}');
insert into wf_component_data values ('a125e83ece531b99d031c78847d340a8', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"투입시작일시"}');
insert into wf_component_data values ('a125e83ece531b99d031c78847d340a8', 'validate', '{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('a82c3def831961f635c6bcb8b8e848a2', 'display', '{"placeholder":"","column":"10","default":"none|","order":53}');
insert into wf_component_data values ('a82c3def831961f635c6bcb8b8e848a2', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"workDay"}');
insert into wf_component_data values ('a82c3def831961f635c6bcb8b8e848a2', 'validate', '{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"}');
insert into wf_component_data values ('af4e80182ff3fea073b396d9e8ff1c2b', 'display', '{"column":"12","rowMin":"1","rowMax":"15","border":"rgba(235, 235, 235, 1)","order":54}');
insert into wf_component_data values ('af4e80182ff3fea073b396d9e8ff1c2b', 'label', '{"position":"top","column":"12","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"자원입력"}');
insert into wf_component_data values ('af4e80182ff3fea073b396d9e8ff1c2b', 'header', '{"size":"16","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"center"}');
insert into wf_component_data values ('af4e80182ff3fea073b396d9e8ff1c2b', 'drTableColumns', '[{"type":"inputbox","display":{"align":"center","placeholder":""},"validate":{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"},"width":"12","text":"자원ID"},{"type":"inputbox","display":{"align":"center","placeholder":""},"validate":{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"},"width":"12","text":"자원명"},{"type":"inputbox","display":{"align":"center","placeholder":""},"validate":{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"},"width":"12","text":"자원위치"},{"type":"inputbox","display":{"align":"center","placeholder":""},"validate":{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"},"width":"4","text":"버전"}]');
insert into wf_component_data values ('a12f03fca3786b969a923f8aad6e1d81', 'display', '{"column":"10","order":55}');
insert into wf_component_data values ('a12f03fca3786b969a923f8aad6e1d81', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"첨부파일"}');
insert into wf_component_data values ('a916992162c9f0747690fabc416098c3', 'display', '{"startId":"aba91068bc801ce3f2dbfda7c3a313a2","thickness":"1","color":"rgba(235, 235, 235, 1)","order":56}');
insert into wf_component_data values ('a916992162c9f0747690fabc416098c3', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"구현 내역"}');
insert into wf_component_data values ('aaa8bef0d513aa0ac8d4d44c53ef7790', 'display', '{"endId":"ada9f8e74c0997508637cdc2d428fb94","thickness":"1","color":"rgba(235, 235, 235, 1)","order":57}');
insert into wf_component_data values ('aaa8bef0d513aa0ac8d4d44c53ef7790', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"단위 테스트 내역"}');
insert into wf_component_data values ('a6bec26d5bfc73007ff79910d9aff2aa', 'display', '{"column":"10","default":"now","order":58}');
insert into wf_component_data values ('a6bec26d5bfc73007ff79910d9aff2aa', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"투입시작일시"}');
insert into wf_component_data values ('a6bec26d5bfc73007ff79910d9aff2aa', 'validate', '{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('aa2580d3afc5bbfc9e4ad7ba8dad963c', 'display', '{"column":"10","default":"now","order":59}');
insert into wf_component_data values ('aa2580d3afc5bbfc9e4ad7ba8dad963c', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"투입종료일시"}');
insert into wf_component_data values ('aa2580d3afc5bbfc9e4ad7ba8dad963c', 'validate', '{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('a1a84b18bde06171fdd4edf59e1cafd9', 'display', '{"placeholder":"","column":"10","default":"none|","order":60}');
insert into wf_component_data values ('a1a84b18bde06171fdd4edf59e1cafd9', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"workDay"}');
insert into wf_component_data values ('a1a84b18bde06171fdd4edf59e1cafd9', 'validate', '{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"}');
insert into wf_component_data values ('a6aedfc56ed8c741c9f6ee5e1c3ff322', 'display', '{"column":"12","rowMin":"1","rowMax":"15","border":"rgba(235, 235, 235, 1)","order":61}');
insert into wf_component_data values ('a6aedfc56ed8c741c9f6ee5e1c3ff322', 'label', '{"position":"top","column":"12","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"단위테스트 항목"}');
insert into wf_component_data values ('a6aedfc56ed8c741c9f6ee5e1c3ff322', 'header', '{"size":"16","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"center"}');
insert into wf_component_data values ('a6aedfc56ed8c741c9f6ee5e1c3ff322', 'drTableColumns', '[{"type":"inputbox","display":{"align":"center","placeholder":""},"validate":{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"},"width":"4","text":"구분"},{"type":"inputbox","display":{"align":"center","placeholder":""},"validate":{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"},"width":"12","text":"항목"},{"type":"inputbox","display":{"align":"center","placeholder":""},"validate":{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"},"width":"4","text":"결과"}]');
insert into wf_component_data values ('ada9f8e74c0997508637cdc2d428fb94', 'display', '{"startId":"aaa8bef0d513aa0ac8d4d44c53ef7790","thickness":"1","color":"rgba(235, 235, 235, 1)","order":62}');
insert into wf_component_data values ('ada9f8e74c0997508637cdc2d428fb94', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"단위 테스트 내역"}');
insert into wf_component_data values ('af4eef76e3a542f194ac156ac6dcc06f', 'display', '{"endId":"a7e8276db9ef05adcc538678a63b326b","thickness":"1","color":"rgba(235, 235, 235, 1)","order":63}');
insert into wf_component_data values ('af4eef76e3a542f194ac156ac6dcc06f', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"통합 테스트 내역"}');
insert into wf_component_data values ('aff0f64fc7d9689291e3d32c4fe8047b', 'display', '{"column":"10","default":"now","order":64}');
insert into wf_component_data values ('aff0f64fc7d9689291e3d32c4fe8047b', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"투입시작일시"}');
insert into wf_component_data values ('aff0f64fc7d9689291e3d32c4fe8047b', 'validate', '{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('a5dbb70aac1337c3e2206a3dbd0074b6', 'display', '{"column":"10","default":"now","order":65}');
insert into wf_component_data values ('a5dbb70aac1337c3e2206a3dbd0074b6', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"투입종료일시"}');
insert into wf_component_data values ('a5dbb70aac1337c3e2206a3dbd0074b6', 'validate', '{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('ac4213bdbdfa0f574e5575ba6e7ef8a0', 'display', '{"placeholder":"","column":"10","default":"none|","order":66}');
insert into wf_component_data values ('ac4213bdbdfa0f574e5575ba6e7ef8a0', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"workDay"}');
insert into wf_component_data values ('ac4213bdbdfa0f574e5575ba6e7ef8a0', 'validate', '{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"}');
insert into wf_component_data values ('a980fefca465ad7fb3bdc83cfed1cd2c', 'display', '{"column":"12","rowMin":"1","rowMax":"15","border":"rgba(235, 235, 235, 1)","order":67}');
insert into wf_component_data values ('a980fefca465ad7fb3bdc83cfed1cd2c', 'label', '{"position":"top","column":"12","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"통합테스트항목"}');
insert into wf_component_data values ('d86bfcc74be147d4ae7b0433d7b8a42a', 'display', '{"column":"10","order":83}');
insert into wf_component_data values ('a980fefca465ad7fb3bdc83cfed1cd2c', 'header', '{"size":"16","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"center"}');
insert into wf_component_data values ('a980fefca465ad7fb3bdc83cfed1cd2c', 'drTableColumns', '[{"type":"inputbox","display":{"align":"center","placeholder":""},"validate":{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"},"width":"4","text":"구분"},{"type":"inputbox","display":{"align":"center","placeholder":""},"validate":{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"},"width":"12","text":"항목"},{"type":"inputbox","display":{"align":"center","placeholder":""},"validate":{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"},"width":"4","text":"결과"}]');
insert into wf_component_data values ('a7e8276db9ef05adcc538678a63b326b', 'display', '{"startId":"af4eef76e3a542f194ac156ac6dcc06f","thickness":"1","color":"rgba(235, 235, 235, 1)","order":68}');
insert into wf_component_data values ('a7e8276db9ef05adcc538678a63b326b', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"통합 테스트 내역"}');
insert into wf_component_data values ('a0d3ea466d5aeeb00d502d8d1b615f6b', 'display', '{"endId":"a87d67e48b0a396f09253dd92daaae2a","thickness":"1","color":"rgba(235, 235, 235, 1)","order":69}');
insert into wf_component_data values ('a0d3ea466d5aeeb00d502d8d1b615f6b', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"현업 테스트 요청"}');
insert into wf_component_data values ('ac4be8fdff1752cbcbec33be1c2d99d8', 'display', '{"placeholder":"","column":"10","default":"none|","order":70}');
insert into wf_component_data values ('ac4be8fdff1752cbcbec33be1c2d99d8', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"IT 요구명"}');
insert into wf_component_data values ('ac4be8fdff1752cbcbec33be1c2d99d8', 'validate', '{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"}');
insert into wf_component_data values ('a9038fef67bfc858282dee6faf72bde8', 'display', '{"placeholder":"","column":"10","default":"none|","order":71}');
insert into wf_component_data values ('a9038fef67bfc858282dee6faf72bde8', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"관련근거"}');
insert into wf_component_data values ('a9038fef67bfc858282dee6faf72bde8', 'validate', '{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"}');
insert into wf_component_data values ('a6a05fb218d3c9c70fd30a1e8c84fca0', 'display', '{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":72}');
insert into wf_component_data values ('a6a05fb218d3c9c70fd30a1e8c84fca0', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"요청의견"}');
insert into wf_component_data values ('a6a05fb218d3c9c70fd30a1e8c84fca0', 'validate', '{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('a87d67e48b0a396f09253dd92daaae2a', 'display', '{"startId":"a0d3ea466d5aeeb00d502d8d1b615f6b","thickness":"1","color":"rgba(235, 235, 235, 1)","order":73}');
insert into wf_component_data values ('a87d67e48b0a396f09253dd92daaae2a', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"현업 테스트 요청"}');
insert into wf_component_data values ('adfc19b1f9e03fb6f5b6a58eb992f635', 'display', '{"endId":"a98f121f7440d96267a34f3c4c4b3b7b","thickness":"1","color":"rgba(235, 235, 235, 1)","order":74}');
insert into wf_component_data values ('adfc19b1f9e03fb6f5b6a58eb992f635', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"현업 테스트 내역"}');
insert into wf_component_data values ('a0a8f65a8c3669bc39b3311b54ebea81', 'display', '{"column":"10","default":"now","order":75}');
insert into wf_component_data values ('a0a8f65a8c3669bc39b3311b54ebea81', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"테스트 시작일시"}');
insert into wf_component_data values ('a0a8f65a8c3669bc39b3311b54ebea81', 'validate', '{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('ac5c39724c6d72da19d4a277f2fc0861', 'display', '{"column":"10","default":"now","order":76}');
insert into wf_component_data values ('ac5c39724c6d72da19d4a277f2fc0861', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"테스트종료일시"}');
insert into wf_component_data values ('ac5c39724c6d72da19d4a277f2fc0861', 'validate', '{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('a038232a86c32bf93cc847fb86920d33', 'display', '{"column":"12","rowMin":"1","rowMax":"15","border":"rgba(235, 235, 235, 1)","order":77}');
insert into wf_component_data values ('a038232a86c32bf93cc847fb86920d33', 'label', '{"position":"top","column":"12","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"테스트 항목"}');
insert into wf_component_data values ('a038232a86c32bf93cc847fb86920d33', 'header', '{"size":"16","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"center"}');
insert into wf_component_data values ('a038232a86c32bf93cc847fb86920d33', 'drTableColumns', '[{"type":"inputbox","display":{"align":"center","placeholder":""},"validate":{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"},"width":"3","text":"구분"},{"type":"inputbox","display":{"align":"center","placeholder":""},"validate":{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"},"width":"12","text":"항목"},{"type":"inputbox","display":{"align":"center","placeholder":""},"validate":{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"},"width":"4","text":"결과"}]');
insert into wf_component_data values ('ad2545081cc9fa330aec2ff41dd87685', 'display', '{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":78}');
insert into wf_component_data values ('ad2545081cc9fa330aec2ff41dd87685', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"테스트의견"}');
insert into wf_component_data values ('ad2545081cc9fa330aec2ff41dd87685', 'validate', '{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('a98f121f7440d96267a34f3c4c4b3b7b', 'display', '{"startId":"adfc19b1f9e03fb6f5b6a58eb992f635","thickness":"1","color":"rgba(235, 235, 235, 1)","order":79}');
insert into wf_component_data values ('a98f121f7440d96267a34f3c4c4b3b7b', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"현업 테스트 내역"}');
insert into wf_component_data values ('50c30230083b426b88de35afe81bae4b', 'display', '{"endId":"e2bff84339b3467e99c8b957fd14c057","thickness":"1","color":"rgba(235, 235, 235, 1)","order":80}');
insert into wf_component_data values ('50c30230083b426b88de35afe81bae4b', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"left","text":"변경 완료 보고서"}');
insert into wf_component_data values ('0481303245e348268b8618b25daa6960', 'display', '{"column":"10","direction":"horizontal","position":"right","order":81}');
insert into wf_component_data values ('0481303245e348268b8618b25daa6960', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"변경작업결과"}');
insert into wf_component_data values ('0481303245e348268b8618b25daa6960', 'option', '[{"seq":"1","name":"성공종결","value":"s"},{"seq":"2","name":"실패종결","value":"f"}]');
insert into wf_component_data values ('69619296316a44c79f6dd78af653e16f', 'display', '{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":82}');
insert into wf_component_data values ('69619296316a44c79f6dd78af653e16f', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"내용"}');
insert into wf_component_data values ('69619296316a44c79f6dd78af653e16f', 'validate', '{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('d86bfcc74be147d4ae7b0433d7b8a42a', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"첨부파일"}');
insert into wf_component_data values ('e2bff84339b3467e99c8b957fd14c057', 'display', '{"startId":"50c30230083b426b88de35afe81bae4b","thickness":"1","color":"rgba(235, 235, 235, 1)","order":84}');
insert into wf_component_data values ('e2bff84339b3467e99c8b957fd14c057', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"left","text":"변경 완료 보고서"}');
insert into wf_component_data values ('a8d8d72b5692446995ddd37989766156', 'display', '{"endId":"51c1befbe967406da6c774207edfb620","thickness":"1","color":"rgba(235, 235, 235, 1)","order":85}');
insert into wf_component_data values ('a8d8d72b5692446995ddd37989766156', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"left","text":"승인 요청"}');
insert into wf_component_data values ('4777a6b2ef2b43908c065319c2bfa516', 'display', '{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":86}');
insert into wf_component_data values ('4777a6b2ef2b43908c065319c2bfa516', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"요청의견"}');
insert into wf_component_data values ('4777a6b2ef2b43908c065319c2bfa516', 'validate', '{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('12ee5318a80f4c4e97091a770f9fb59c', 'display', '{"column":"10","customCode":"40288a19736b46fb01736b89e46c0008","default":"none","buttonText":"검색","order":87}');
insert into wf_component_data values ('12ee5318a80f4c4e97091a770f9fb59c', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"승인자"}');
insert into wf_component_data values ('51c1befbe967406da6c774207edfb620', 'display', '{"startId":"a8d8d72b5692446995ddd37989766156","thickness":"1","color":"rgba(235, 235, 235, 1)","order":88}');
insert into wf_component_data values ('51c1befbe967406da6c774207edfb620', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"left","text":"승인 요청"}');
insert into wf_component_data values ('c865d34e6b5547eb9e519fef05ef2dcd', 'display', '{"endId":"f0d1968e9bc94888a829cfd9d10fccb5","thickness":"1","color":"rgba(235, 235, 235, 1)","order":89}');
insert into wf_component_data values ('c865d34e6b5547eb9e519fef05ef2dcd', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"left","text":"승인 내역"}');
insert into wf_component_data values ('20174e4a10174aae9f23152b2e3b1b31', 'display', '{"column":"10","default":"now","order":90}');
insert into wf_component_data values ('20174e4a10174aae9f23152b2e3b1b31', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"승인일시"}');
insert into wf_component_data values ('20174e4a10174aae9f23152b2e3b1b31', 'validate', '{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('e13a94297f794fe99403f95e716f9343', 'display', '{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":91}');
insert into wf_component_data values ('e13a94297f794fe99403f95e716f9343', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"승인의견"}');
insert into wf_component_data values ('e13a94297f794fe99403f95e716f9343', 'validate', '{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('f0d1968e9bc94888a829cfd9d10fccb5', 'display', '{"startId":"c865d34e6b5547eb9e519fef05ef2dcd","thickness":"1","color":"rgba(235, 235, 235, 1)","order":92}');
insert into wf_component_data values ('f0d1968e9bc94888a829cfd9d10fccb5', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"left","text":"승인 내역"}');
insert into wf_component_data values ('0cab5ed20996478bac575355d01d7b69', 'display', '{"size":"40","color":"rgba(52,152,219,1)","bold":"Y","italic":"N","underline":"N","align":"left","text":"서비스 요청서 만족도 평가","order":1}');
insert into wf_component_data values ('9dd57b7354f546a6b3c15c1577e875d5', 'display', '{"thickness":"5","color":"rgba(63, 75, 86, 1)","type":"solid","order":2}');
insert into wf_component_data values ('c275441e4c394193b0ca136b59f44d65', 'display', '{"endId":"d1855ada4f3a493db550efb28e80b985","thickness":"1","color":"rgba(235, 235, 235, 1)","order":3}');
insert into wf_component_data values ('c275441e4c394193b0ca136b59f44d65', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"요청 내역"}');
insert into wf_component_data values ('139bf4f5584445ce8bb3deb79eb5a630', 'display', '{"column":"10","default":"now","order":4}');
insert into wf_component_data values ('139bf4f5584445ce8bb3deb79eb5a630', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"신청일시"}');
insert into wf_component_data values ('139bf4f5584445ce8bb3deb79eb5a630', 'validate', '{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('7db0f91f130c45be89a6ea5e99c66dbe', 'display', '{"column":"10","customCode":"40288a19736b46fb01736b89e46c0008","default":"none","buttonText":"검색","order":5}');
insert into wf_component_data values ('7db0f91f130c45be89a6ea5e99c66dbe', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"신청자"}');
insert into wf_component_data values ('c8108daef968491a9520c942984049b3', 'display', '{"placeholder":"","column":"10","default":"none|","order":6}');
insert into wf_component_data values ('c8108daef968491a9520c942984049b3', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"신청부서"}');
insert into wf_component_data values ('c8108daef968491a9520c942984049b3', 'validate', '{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"}');
insert into wf_component_data values ('5b0b6965e53049a7934a0d665f0f99da', 'display', '{"column":"4","order":7}');
insert into wf_component_data values ('5b0b6965e53049a7934a0d665f0f99da', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"관련서비스"}');
insert into wf_component_data values ('5b0b6965e53049a7934a0d665f0f99da', 'option', '[{"seq":"1","name":"홈페이지","value":"homepage"},{"seq":"2","name":"전자결재시스템","value":"eps"},{"seq":"3","name":"출입관리시스템","value":"acs"},{"seq":"4","name":"ERP","value":"erp"},{"seq":"5","name":"ITSM","value":"itsm"},{"seq":"6","name":"기타","value":"etd"}]');
insert into wf_component_data values ('d143565bc73043c99bf2ac72a80afe86', 'display', '{"column":"10","default":"now","order":8}');
insert into wf_component_data values ('d143565bc73043c99bf2ac72a80afe86', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"완료희망일시"}');
insert into wf_component_data values ('d143565bc73043c99bf2ac72a80afe86', 'validate', '{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('aac6679f5ee349d4aec894fbc3bfb279', 'display', '{"column":"10","customCode":"40288a19736b46fb01736b89e46c0008","default":"none","buttonText":"검색","order":9}');
insert into wf_component_data values ('aac6679f5ee349d4aec894fbc3bfb279', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"담당자"}');
insert into wf_component_data values ('fb9772a1d88342e9aa30ef2c4f729b76', 'display', '{"placeholder":"","column":"10","default":"none|","order":10}');
insert into wf_component_data values ('fb9772a1d88342e9aa30ef2c4f729b76', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"제목"}');
insert into wf_component_data values ('fb9772a1d88342e9aa30ef2c4f729b76', 'validate', '{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"}');
insert into wf_component_data values ('0cc84deed3424cbab82867fefcfb716e', 'display', '{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":11}');
insert into wf_component_data values ('0cc84deed3424cbab82867fefcfb716e', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"요청내용"}');
insert into wf_component_data values ('0cc84deed3424cbab82867fefcfb716e', 'validate', '{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('e7ac6934423047d99e744ca42ba80a5e', 'display', '{"column":"12","isEditable":false,"border":"rgba(235, 235, 235, 1)","order":12}');
insert into wf_component_data values ('e7ac6934423047d99e744ca42ba80a5e', 'label', '{"position":"top","column":"12","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"관련CI"}');
insert into wf_component_data values ('e7ac6934423047d99e744ca42ba80a5e', 'header', '{"size":"16","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"left"}');
insert into wf_component_data values ('1997e740e5c247a6b8b2a4a435fc2bc0', 'display', '{"column":"10","order":13}');
insert into wf_component_data values ('1997e740e5c247a6b8b2a4a435fc2bc0', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"첨부파일"}');
insert into wf_component_data values ('d1855ada4f3a493db550efb28e80b985', 'display', '{"startId":"c275441e4c394193b0ca136b59f44d65","thickness":"1","color":"rgba(235, 235, 235, 1)","order":14}');
insert into wf_component_data values ('d1855ada4f3a493db550efb28e80b985', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"요청 내역"}');
insert into wf_component_data values ('aff338d65894932caaff8f9b5b137564', 'display', '{"endId":"aa17a892ba968a4a3c2cb6eedea8cc9d","thickness":"1","color":"rgba(235, 235, 235, 1)","order":15}');
insert into wf_component_data values ('aff338d65894932caaff8f9b5b137564', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"만족도 내역"}');
insert into wf_component_data values ('a841dc0bd9de9054efec9568400748f3', 'display', '{"column":"10","direction":"horizontal","position":"right","order":16}');
insert into wf_component_data values ('a841dc0bd9de9054efec9568400748f3', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"만족도"}');
insert into wf_component_data values ('a841dc0bd9de9054efec9568400748f3', 'option', '[{"seq":"1","name":"매우만족","value":"5"},{"seq":"2","name":"만족","value":"4"},{"seq":"3","name":"보통","value":"3"},{"seq":"4","name":"불만족","value":"2"},{"seq":"5","name":"매우불만족","value":"1"}]');
insert into wf_component_data values ('a1daf5eb5c56523e7a975a9114954bfa', 'display', '{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":17}');
insert into wf_component_data values ('a1daf5eb5c56523e7a975a9114954bfa', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"평가의견"}');
insert into wf_component_data values ('a1daf5eb5c56523e7a975a9114954bfa', 'validate', '{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('a745892a840f00340bf7b267d3a63e90', 'display', '{"column":"10","order":18}');
insert into wf_component_data values ('a745892a840f00340bf7b267d3a63e90', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"첨부파일"}');
insert into wf_component_data values ('aa17a892ba968a4a3c2cb6eedea8cc9d', 'display', '{"startId":"aff338d65894932caaff8f9b5b137564","thickness":"1","color":"rgba(235, 235, 235, 1)","order":19}');
insert into wf_component_data values ('aa17a892ba968a4a3c2cb6eedea8cc9d', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"만족도 내역"}');
insert into wf_component_data values ('8e94b33c57044bf6aa072c49fd30d3b1', 'display', '{"size":"40","color":"rgba(52,152,219,1)","bold":"Y","italic":"N","underline":"N","align":"left","text":"서비스 요청서","order":1}');
insert into wf_component_data values ('f707bec669fb4d75b3f7e85c089c74ae', 'display', '{"thickness":"5","color":"rgba(63, 75, 86, 1)","type":"solid","order":2}');
insert into wf_component_data values ('dce4c9e0702744a9b75f66e2925e6671', 'display', '{"endId":"8c3a8a79321a4f70ab17b16e4da9caa6","thickness":"1","color":"rgba(235, 235, 235, 1)","order":3}');
insert into wf_component_data values ('dce4c9e0702744a9b75f66e2925e6671', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"요청 내역"}');
insert into wf_component_data values ('43d5564b1f6c44f0ab8976d505fc09b3', 'display', '{"column":"10","default":"now","order":4}');
insert into wf_component_data values ('43d5564b1f6c44f0ab8976d505fc09b3', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"신청일시"}');
insert into wf_component_data values ('43d5564b1f6c44f0ab8976d505fc09b3', 'validate', '{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('a41a893e79af4e60ae84f07f9b09aaf0', 'display', '{"column":"10","customCode":"40288a19736b46fb01736b89e46c0008","default":"session|userName|이름","buttonText":"검색","order":5}');
insert into wf_component_data values ('a41a893e79af4e60ae84f07f9b09aaf0', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"신청자"}');
insert into wf_component_data values ('c614d28cf0ff46099480ba82f2f3684e', 'display', '{"placeholder":"","column":"10","default":"select|department|부서","order":6}');
insert into wf_component_data values ('c614d28cf0ff46099480ba82f2f3684e', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"신청부서"}');
insert into wf_component_data values ('c614d28cf0ff46099480ba82f2f3684e', 'validate', '{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"}');
insert into wf_component_data values ('c2185527b27841bf9f9aa292d11c760e', 'display', '{"column":"4","order":7}');
insert into wf_component_data values ('c2185527b27841bf9f9aa292d11c760e', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"관련서비스"}');
insert into wf_component_data values ('c2185527b27841bf9f9aa292d11c760e', 'option', '[{"seq":"1","name":"홈페이지","value":"homepage"},{"seq":"2","name":"전자결재시스템","value":"eps"},{"seq":"3","name":"출입관리시스템","value":"acs"},{"seq":"4","name":"ERP","value":"erp"},{"seq":"5","name":"ITSM","value":"itsm"},{"seq":"6","name":"기타","value":"etd"}]');
insert into wf_component_data values ('afc02b23a7f34e578b431da9cdaf41a3', 'display', '{"column":"10","default":"now","order":8}');
insert into wf_component_data values ('afc02b23a7f34e578b431da9cdaf41a3', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"완료희망일시"}');
insert into wf_component_data values ('afc02b23a7f34e578b431da9cdaf41a3', 'validate', '{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('1ce826a47a1545ebaba8d5527b0a98e0', 'display', '{"column":"10","customCode":"40288a19736b46fb01736b89e46c0008","default":"none","buttonText":"검색","order":9}');
insert into wf_component_data values ('1ce826a47a1545ebaba8d5527b0a98e0', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"담당자"}');
insert into wf_component_data values ('e4c435744d944ed6a8126c08c1c0fff9', 'display', '{"placeholder":"","column":"10","default":"none|","order":10}');
insert into wf_component_data values ('e4c435744d944ed6a8126c08c1c0fff9', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"제목"}');
insert into wf_component_data values ('e4c435744d944ed6a8126c08c1c0fff9', 'validate', '{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"}');
insert into wf_component_data values ('244307c8dd204f9cbffbb29107c80e73', 'display', '{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":11}');
insert into wf_component_data values ('244307c8dd204f9cbffbb29107c80e73', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"요청내용"}');
insert into wf_component_data values ('244307c8dd204f9cbffbb29107c80e73', 'validate', '{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('28e014bfbc8b46c784e5f4858c8e476d', 'display', '{"column":"12","isEditable":false,"border":"rgba(235, 235, 235, 1)","order":12}');
insert into wf_component_data values ('28e014bfbc8b46c784e5f4858c8e476d', 'label', '{"position":"top","column":"12","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"관련CI"}');
insert into wf_component_data values ('28e014bfbc8b46c784e5f4858c8e476d', 'header', '{"size":"16","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"left"}');
insert into wf_component_data values ('6069b42a0ee048489063e19915e08e46', 'display', '{"column":"10","order":13}');
insert into wf_component_data values ('6069b42a0ee048489063e19915e08e46', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"첨부파일"}');
insert into wf_component_data values ('8c3a8a79321a4f70ab17b16e4da9caa6', 'display', '{"startId":"dce4c9e0702744a9b75f66e2925e6671","thickness":"1","color":"rgba(235, 235, 235, 1)","order":14}');
insert into wf_component_data values ('8c3a8a79321a4f70ab17b16e4da9caa6', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"요청 내역"}');
insert into wf_component_data values ('4dafd004cbc944b7996522ffe6b7d809', 'display', '{"endId":"3d9de87a9e0c4c88aecff1f2db9d14d3","thickness":"1","color":"rgba(235, 235, 235, 1)","order":15}');
insert into wf_component_data values ('4dafd004cbc944b7996522ffe6b7d809', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"접수 내역"}');
insert into wf_component_data values ('41193073002544a29e985d2157c2867e', 'display', '{"column":"10","default":"now","order":16}');
insert into wf_component_data values ('41193073002544a29e985d2157c2867e', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"접수일시"}');
insert into wf_component_data values ('41193073002544a29e985d2157c2867e', 'validate', '{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('5b312c6e95c7499ea0671a6afa3381d4', 'display', '{"placeholder":"","column":"10","default":"select|department|부서","order":17}');
insert into wf_component_data values ('5b312c6e95c7499ea0671a6afa3381d4', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"접수부서"}');
insert into wf_component_data values ('5b312c6e95c7499ea0671a6afa3381d4', 'validate', '{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"}');
insert into wf_component_data values ('2b2b34fdcb64411999b201fa09f3bf81', 'display', '{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":18}');
insert into wf_component_data values ('2b2b34fdcb64411999b201fa09f3bf81', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"접수의견"}');
insert into wf_component_data values ('2b2b34fdcb64411999b201fa09f3bf81', 'validate', '{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('d861305d80a045ed8d9a755d5a6c5dda', 'display', '{"column":"10","customCode":"40288a19736b46fb01736b89e46c0008","default":"none","buttonText":"검색","order":19}');
insert into wf_component_data values ('d861305d80a045ed8d9a755d5a6c5dda', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"변경 담당자"}');
insert into wf_component_data values ('3d9de87a9e0c4c88aecff1f2db9d14d3', 'display', '{"startId":"4dafd004cbc944b7996522ffe6b7d809","thickness":"1","color":"rgba(235, 235, 235, 1)","order":20}');
insert into wf_component_data values ('3d9de87a9e0c4c88aecff1f2db9d14d3', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"접수 내역"}');
insert into wf_component_data values ('3e1423aabfd24e259a521546f8c10c97', 'display', '{"endId":"5ee83a02fecb4fc79eca4c5e2c70d27a","thickness":"1","color":"rgba(235, 235, 235, 1)","order":21}');
insert into wf_component_data values ('3e1423aabfd24e259a521546f8c10c97', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"승인 요청"}');
insert into wf_component_data values ('a1c5167618f641d4906997e4ca2e2952', 'display', '{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":22}');
insert into wf_component_data values ('a1c5167618f641d4906997e4ca2e2952', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"요청의견"}');
insert into wf_component_data values ('a1c5167618f641d4906997e4ca2e2952', 'validate', '{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('0226c90f73c644fabe92b22e259eb7a7', 'display', '{"column":"10","customCode":"40288a19736b46fb01736b89e46c0008","default":"none","buttonText":"검색","order":23}');
insert into wf_component_data values ('0226c90f73c644fabe92b22e259eb7a7', 'label', '{"position":"left","column":"2","size":"16","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"승인자"}');
insert into wf_component_data values ('5ee83a02fecb4fc79eca4c5e2c70d27a', 'display', '{"startId":"3e1423aabfd24e259a521546f8c10c97","thickness":"1","color":"rgba(235, 235, 235, 1)","order":24}');
insert into wf_component_data values ('5ee83a02fecb4fc79eca4c5e2c70d27a', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"승인 요청"}');
insert into wf_component_data values ('3141b9fa06ab400bbef50a95e004e3a9', 'display', '{"endId":"25f616d3be6b4723b4bbe0b9891c29a2","thickness":"1","color":"rgba(235, 235, 235, 1)","order":25}');
insert into wf_component_data values ('3141b9fa06ab400bbef50a95e004e3a9', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"승인 내역"}');
insert into wf_component_data values ('eedbef4d73d94377bba70b6c41df2c86', 'display', '{"column":"10","default":"now","order":26}');
insert into wf_component_data values ('eedbef4d73d94377bba70b6c41df2c86', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"승인일시"}');
insert into wf_component_data values ('eedbef4d73d94377bba70b6c41df2c86', 'validate', '{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('68dbfd222e8548d4b9302efa2131836f', 'display', '{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":27}');
insert into wf_component_data values ('68dbfd222e8548d4b9302efa2131836f', 'label', '{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"승인의견"}');
insert into wf_component_data values ('68dbfd222e8548d4b9302efa2131836f', 'validate', '{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('25f616d3be6b4723b4bbe0b9891c29a2', 'display', '{"startId":"3141b9fa06ab400bbef50a95e004e3a9","thickness":"1","color":"rgba(235, 235, 235, 1)","order":28}');
insert into wf_component_data values ('25f616d3be6b4723b4bbe0b9891c29a2', 'label', '{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"승인 내역"}');
/**
 * 엘리먼트정보
 */
DROP TABLE IF EXISTS wf_element cascade;

CREATE TABLE wf_element
(
	element_id varchar(256) NOT NULL,
	process_id varchar(128) NOT NULL,
	element_type varchar(100) NOT NULL,
	element_name varchar(256),
	element_desc varchar(1024),
	notification boolean DEFAULT 'N',
	element_config text,
	display_info text,
	CONSTRAINT wf_element_pk PRIMARY KEY (element_id),
	CONSTRAINT wf_element_fk FOREIGN KEY (process_id) REFERENCES wf_process (process_id)
);

COMMENT ON TABLE wf_element IS '엘리먼트정보';
COMMENT ON COLUMN wf_element.element_id IS '엘리먼트아이디';
COMMENT ON COLUMN wf_element.process_id IS '프로세스아이디';
COMMENT ON COLUMN wf_element.element_type IS '엘리먼트종류';
COMMENT ON COLUMN wf_element.element_name IS '엘리먼트이름';
COMMENT ON COLUMN wf_element.element_desc IS '엘리먼트설명';
COMMENT ON COLUMN wf_element.notification IS '알람여부';
COMMENT ON COLUMN wf_element.element_config IS '엘리먼트설정데이터';
COMMENT ON COLUMN wf_element.display_info IS '출력정보';

insert into wf_element values ('01430d9b3cfe427b89f9203185e35530', '40288ab777f04ed90177f057ca410000', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('1318f304ca2a420e9c6756438daa4e3a', '40288ab777f04ed90177f057ca410000', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('2a8341aa1dcb4ab7ab89271020c748c8', '40288ab777f04ed90177f057ca410000', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('4f296c1468f3422b8c59c97151e2c476', '40288ab777f04ed90177f057ca410000', 'userTask', '신청서 작성', '', false, '', '{"width":160,"height":40,"position-x":210,"position-y":200}');
insert into wf_element values ('70e8f5da83584cba81bd9ff597963c4a', '40288ab777f04ed90177f057ca410000', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('957ac0be921c4337999fcbca2f22a92d', '40288ab777f04ed90177f057ca410000', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('a34268ba767d48e7ab7db9e7297e7300', '40288ab777f04ed90177f057ca410000', 'userTask', '구성변경 처리', '', false, '', '{"width":160,"height":40,"position-x":850,"position-y":200}');
insert into wf_element values ('a4aa971e1f952df93f07e932ab25fbf6', '40288ab777f04ed90177f057ca410000', 'scriptTask', 'CMDB 적용', '', false, '', '{"width":160,"height":40,"position-x":1060,"position-y":280}');
insert into wf_element values ('a4bd7d4950b4226ea3dfc20bf15193ff', '40288ab777f04ed90177f057ca410000', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('a508191382c644b289c01cf32bed8722', '40288ab777f04ed90177f057ca410000', 'userTask', '변경결과 승인', '', true, '', '{"width":160,"height":40,"position-x":850,"position-y":280}');
insert into wf_element values ('a9a0c170a6ff5ae938f128a726fc2a60', '40288ab777f04ed90177f057ca410000', 'commonEnd', '종료', '', false, '', '{"width":40,"height":40,"position-x":1210,"position-y":280}');
insert into wf_element values ('ad56214bb68e7b749f7d336e14172321', '40288ab777f04ed90177f057ca410000', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('b0dd93b1c9864edc8f048713c0d07e12', '40288ab777f04ed90177f057ca410000', 'manualTask', '접수', '', false, '', '{"width":160,"height":40,"position-x":630,"position-y":200}');
insert into wf_element values ('bff880e180164c4f9cedfab4ba282f28', '40288ab777f04ed90177f057ca410000', 'userTask', '신청서검토', '', true, '', '{"width":160,"height":40,"position-x":420,"position-y":200}');
insert into wf_element values ('d099b9ac855a4e7f9a641906549dbcb0', '40288ab777f04ed90177f057ca410000', 'commonStart', '', '', false, '', '{"width":40,"height":40,"position-x":70,"position-y":200}');
insert into wf_element values ('2cb0c129cf054cb1b240f69d47066ff5', '4028b25d787736640178773e71480002', 'commonEnd', '종료', '', false, '', '{"width":40,"height":40,"position-x":520,"position-y":200}');
insert into wf_element values ('32b06c8749f1466aa448c2ff6ccf0fdb', '4028b25d787736640178773e71480002', 'userTask', '만족도평가', '', true, '', '{"width":160,"height":40,"position-x":320,"position-y":200}');
insert into wf_element values ('526362ce18804a71a38c63800ff12656', '4028b25d787736640178773e71480002', 'arrowConnector', '만족도평가', '', false, '', '{}');
insert into wf_element values ('62ee1df53a95473298186031f55f8130', '4028b25d787736640178773e71480002', 'commonStart', '', '', false, '', '{"width":40,"height":40,"position-x":120,"position-y":200}');
insert into wf_element values ('ad56880a70b34398ad693d0ac12b179d', '4028b25d787736640178773e71480002', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('079c5509ce2b4ce2973fbe5d6cebb061', '4028b25d78778da6017877b9df60000f', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('1cc092ef9cb04b9398d5e0687e78ae8f', '4028b25d78778da6017877b9df60000f', 'signalSend', '만족도평가', '', false, '', '{"width":40,"height":40,"position-x":1010,"position-y":280}');
insert into wf_element values ('1db3433a1715460e899f5188e8b80940', '4028b25d78778da6017877b9df60000f', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('237ba276e53a40b5a4d62fba388d76f9', '4028b25d78778da6017877b9df60000f', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('5da2b19767f745c4af6cce63614954f0', '4028b25d78778da6017877b9df60000f', 'userTask', '신청서검토', '', true, '', '{"width":160,"height":40,"position-x":420,"position-y":200}');
insert into wf_element values ('86a7a22385ab434b81fe597665865763', '4028b25d78778da6017877b9df60000f', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('90bfe543a47344c98188e2e09ecb50b5', '4028b25d78778da6017877b9df60000f', 'commonStart', '', '', false, '', '{"width":40,"height":40,"position-x":70,"position-y":200}');
insert into wf_element values ('9cbc9c049d4741c2bbe8f44d0c7e8de7', '4028b25d78778da6017877b9df60000f', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('9fa61b785b944e87b02e9fa753b4c0c5', '4028b25d78778da6017877b9df60000f', 'userTask', '승인', '', true, '', '{"width":160,"height":40,"position-x":850,"position-y":280}');
insert into wf_element values ('aaaf500639bd45d880d0f768bcc91507', '4028b25d78778da6017877b9df60000f', 'commonEnd', '종료', '', false, '', '{"width":40,"height":40,"position-x":1110,"position-y":280}');
insert into wf_element values ('bcdd50f874ea4ea0954ee8c51bf12fbe', '4028b25d78778da6017877b9df60000f', 'manualTask', '접수', '', false, '', '{"width":160,"height":40,"position-x":630,"position-y":200}');
insert into wf_element values ('d01e5d4e5f33495eb239ca868f4511bc', '4028b25d78778da6017877b9df60000f', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('d13b5beee66a4ac7b79fc0bc28abe0cb', '4028b25d78778da6017877b9df60000f', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('d3e931756f15444fa36732356e01a816', '4028b25d78778da6017877b9df60000f', 'userTask', '처리', '', false, '', '{"width":160,"height":40,"position-x":850,"position-y":200}');
insert into wf_element values ('f64ad538612043189e288b43d4bbd7ce', '4028b25d78778da6017877b9df60000f', 'userTask', '신청서 작성', '', false, '', '{"width":160,"height":40,"position-x":210,"position-y":200}');
insert into wf_element values ('a06f9fbcebe8760486d8db746a025bea', '40288ab77878ea67017878eb3dc30000', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('a1127cf2ba198dfdd24745876e4616cb', '40288ab77878ea67017878eb3dc30000', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('a1184c7bed5efd25324bc0d70b1bfdf5', '40288ab77878ea67017878eb3dc30000', 'exclusiveGateway', '', '', false, '', '{"width":34,"height":34,"position-x":580,"position-y":210}');
insert into wf_element values ('a24aa3c4a99fd3d8a4510286045a2f28', '40288ab77878ea67017878eb3dc30000', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('a258e0bfcfae24e99f75be596efc173e', '40288ab77878ea67017878eb3dc30000', 'userTask', '완료보고서', '', false, '', '{"width":160,"height":40,"position-x":1390,"position-y":310}');
insert into wf_element values ('a29b7166bf3d1e6eb1f743433e1a222a', '40288ab77878ea67017878eb3dc30000', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('a33729f914943aab418ea1e7adad0ee4', '40288ab77878ea67017878eb3dc30000', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('a51a85edc794cb15dbf60bf98790a8b0', '40288ab77878ea67017878eb3dc30000', 'arrowConnector', '일반', '', false, '', '{"mid-point":[580,120],"text-point":[-45,-24]}');
insert into wf_element values ('a61866d755fa520ac6fcca0245f91001', '40288ab77878ea67017878eb3dc30000', 'manualTask', '이행작업', '', false, '', '{"width":160,"height":40,"position-x":1190,"position-y":210}');
insert into wf_element values ('a71cf915028b1242f725d2fa4ebeb711', '40288ab77878ea67017878eb3dc30000', 'arrowConnector', '', '', false, '', '{"mid-point":[1030,120]}');
insert into wf_element values ('a82fb95a6550c3cd5dadedd7f597c598', '40288ab77878ea67017878eb3dc30000', 'userTask', '작업결과서', '', false, '', '{"width":160,"height":40,"position-x":1190,"position-y":310}');
insert into wf_element values ('a8a1e1db38cdfdbaa692e14db009c224', '40288ab77878ea67017878eb3dc30000', 'userTask', '승인', '', true, '', '{"width":160,"height":40,"position-x":930,"position-y":120}');
insert into wf_element values ('a905a563d43599def0638503401bcad0', '40288ab77878ea67017878eb3dc30000', 'exclusiveGateway', '', '', false, '', '{"width":34,"height":34,"position-x":1030,"position-y":210}');
insert into wf_element values ('a92c43aedb89519f0c7b31278063788b', '40288ab77878ea67017878eb3dc30000', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('a9fb5f654c0b856e1975997e9bcdb1c9', '40288ab77878ea67017878eb3dc30000', 'arrowConnector', '긴급', '', false, '', '{"text-point":[-6,12]}');
insert into wf_element values ('aa87e149e75de05f31f28fb9309e91df', '40288ab77878ea67017878eb3dc30000', 'userTask', '작업 계획서', '', false, '', '{"width":160,"height":40,"position-x":430,"position-y":210}');
insert into wf_element values ('aaa5103974ec9059469823d84d8ce109', '40288ab77878ea67017878eb3dc30000', 'userTask', '승인요청', '', false, '', '{"width":160,"height":40,"position-x":720,"position-y":120}');
insert into wf_element values ('ab0f37a1b3f2a2618fbf2be136104ed2', '40288ab77878ea67017878eb3dc30000', 'commonEnd', '종료', '', false, '', '{"width":40,"height":40,"position-x":1390,"position-y":400}');
insert into wf_element values ('ac339f9a492736b1cbe9aac6db3721a0', '40288ab77878ea67017878eb3dc30000', 'commonStart', '', '', false, '', '{"width":40,"height":40,"position-x":50,"position-y":210}');
insert into wf_element values ('ac4c84456b64ae08c1cfeb56234e36d5', '40288ab77878ea67017878eb3dc30000', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('aeacaa70d21347699f5c311163d27085', '40288ab77878ea67017878eb3dc30000', 'userTask', '접수', '', true, '', '{"width":160,"height":40,"position-x":200,"position-y":210}');
insert into wf_element values ('afb41eb311d9c3e7248870e304fc9789', '40288ab77878ea67017878eb3dc30000', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('04fd8aee89814e479ffb8058b115b466', '4028b25d78870b0901788766663a0023', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('120af605678049f3b2d2b267231d19cc', '4028b25d78870b0901788766663a0023', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('131128229e894ff9afe21a2b7cc27c28', '4028b25d78870b0901788766663a0023', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('20212f1027974110ab50990680b2a0e5', '4028b25d78870b0901788766663a0023', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('203038f6a6c54dec9824275a4c093b61', '4028b25d78870b0901788766663a0023', 'userTask', '승인요청', '', false, '', '{"width":160,"height":40,"position-x":609,"position-y":59}');
insert into wf_element values ('23bd54bbcaf642a487726b6f0dac4b4c', '4028b25d78870b0901788766663a0023', 'userTask', '승인요청', '', false, '', '{"width":160,"height":40,"position-x":1399,"position-y":239}');
insert into wf_element values ('24a2398bc10d4021b3e55f54cc500c50', '4028b25d78870b0901788766663a0023', 'userTask', '접수', '', false, '', '{"width":160,"height":40,"position-x":189,"position-y":59}');
insert into wf_element values ('2e322bca68bd49e6af7015a7381b66ed', '4028b25d78870b0901788766663a0023', 'exclusiveGateway', '', '', false, '', '{"width":34,"height":34,"position-x":1029,"position-y":239}');
insert into wf_element values ('328c7d65847a4089a2d7d22b1b6bd7d1', '4028b25d78870b0901788766663a0023', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('4248c2b5ca9c4b2bb61f640e80c1b3dc', '4028b25d78870b0901788766663a0023', 'commonStart', '', '', false, '', '{"width":40,"height":40,"position-x":49,"position-y":59}');
insert into wf_element values ('5a1417623338462eaf9533a0c23f0455', '4028b25d78870b0901788766663a0023', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('6b04ce8129964511a4491a67e1aedc50', '4028b25d78870b0901788766663a0023', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('7d9dbf9ad83b4e5681c0a6839501039f', '4028b25d78870b0901788766663a0023', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('7ebf8681283b4707b4301277fb6bf9b6', '4028b25d78870b0901788766663a0023', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('80e3a384a64446e0939f44c6e0d062a2', '4028b25d78870b0901788766663a0023', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('906a57e38491441fa61401a8964f8398', '4028b25d78870b0901788766663a0023', 'subprocess', '릴리즈관리', '', false, '', '{"width":152,"height":40,"position-x":889,"position-y":199}');
insert into wf_element values ('9aa8d1454f7a4a2caebaa18586338405', '4028b25d78870b0901788766663a0023', 'exclusiveGateway', '', '', false, '', '{"width":34,"height":34,"position-x":749,"position-y":239}');
insert into wf_element values ('9d50c868a38e4eb5b4620a97a4c8fa95', '4028b25d78870b0901788766663a0023', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('a618f89d6cbb4611aeb5e99bf9bb9df8', '4028b25d78870b0901788766663a0023', 'userTask', '승인', '', true, '', '{"width":160,"height":40,"position-x":1399,"position-y":319}');
insert into wf_element values ('a74d06e6a768ff1626847b9ec3a84235', '4028b25d78870b0901788766663a0023', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('a7f7a90781ba158a2b53052ffd59bf15', '4028b25d78870b0901788766663a0023', 'userTask', '처리', '', false, '', '{"width":160,"height":40,"position-x":609,"position-y":239}');
insert into wf_element values ('ab20cca4f90b42c6a6ea4394e88594e4', '4028b25d78870b0901788766663a0023', 'arrowConnector', '', '', false, '', '{"mid-point":[609,199]}');
insert into wf_element values ('ab9efe0f11dd45bab9df92b4a0d5a1e0', '4028b25d78870b0901788766663a0023', 'userTask', '처리완료', '', false, '', '{"width":160,"height":40,"position-x":889,"position-y":279}');
insert into wf_element values ('bc2dcad9f1c441a7ac1a425beffb7772', '4028b25d78870b0901788766663a0023', 'userTask', '변경계획서', '', false, '', '{"width":160,"height":40,"position-x":399,"position-y":59}');
insert into wf_element values ('c9d735a5aca04835ba7f09f63f768a58', '4028b25d78870b0901788766663a0023', 'userTask', '변경결과 등록', '', false, '', '{"width":160,"height":40,"position-x":1189,"position-y":239}');
insert into wf_element values ('d0d6c6e94f574915bd88257467b6434c', '4028b25d78870b0901788766663a0023', 'userTask', '승인', '', true, '', '{"width":160,"height":40,"position-x":609,"position-y":149}');
insert into wf_element values ('e7ae188b6dee468babdfed470859539c', '4028b25d78870b0901788766663a0023', 'commonEnd', '종료', '', false, '', '{"width":40,"height":40,"position-x":1399,"position-y":399}');
insert into wf_element values ('ecf774011d9c4c0ab353194c3470dfa2', '4028b25d78870b0901788766663a0023', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('a03344086e04e8796d32ac1aca535873', '4028b25d7888a7f4017888b1cde90000', 'userTask', '처리', '', false, '', '{"width":160,"height":40,"position-x":180,"position-y":310}');
insert into wf_element values ('a050b0dd20e9172ff66fcd28eae58721', '4028b25d7888a7f4017888b1cde90000', 'userTask', '접수', '', false, '', '{"width":160,"height":40,"position-x":180,"position-y":90}');
insert into wf_element values ('a0961206778ce5c701a67eef8d70fcc2', '4028b25d7888a7f4017888b1cde90000', 'subprocess', '릴리즈관리', '', false, '', '{"width":152,"height":40,"position-x":340,"position-y":360}');
insert into wf_element values ('a09963d298d5f89e30ca9b1ce7ebb532', '4028b25d7888a7f4017888b1cde90000', 'commonEnd', '종료', '', false, '', '{"width":40,"height":40,"position-x":1020,"position-y":480}');
insert into wf_element values ('a0b7e0c8b75059e6336a6cf603088a32', '4028b25d7888a7f4017888b1cde90000', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('a144e8c56fbf610796ff294246fe618d', '4028b25d7888a7f4017888b1cde90000', 'userTask', '현업테스트', '', false, '', '{"width":160,"height":40,"position-x":800,"position-y":240}');
insert into wf_element values ('a1767e857822c6cd01140b01193ebc52', '4028b25d7888a7f4017888b1cde90000', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('a1e4e7ef954bf6807f50d49851bb9dfb', '4028b25d7888a7f4017888b1cde90000', 'userTask', '구현/단위테스트', '', false, '', '{"width":160,"height":40,"position-x":180,"position-y":240}');
insert into wf_element values ('a24b4f6637307d25e413cc4ab724b16a', '4028b25d7888a7f4017888b1cde90000', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('a2999d5ea3e6e95c2a88a4048e94e6b0', '4028b25d7888a7f4017888b1cde90000', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('a2a56cf5159365210509dde33ba66fdf', '4028b25d7888a7f4017888b1cde90000', 'arrowConnector', '', '', false, '', '{"mid-point":[180,200]}');
insert into wf_element values ('a2c31eed801f9034c6f83edd0098a348', '4028b25d7888a7f4017888b1cde90000', 'userTask', '변경결과등록', '', false, '', '{"width":160,"height":40,"position-x":610,"position-y":390}');
insert into wf_element values ('a2e1ceeddc40fdbe1aabc7970f2ea492', '4028b25d7888a7f4017888b1cde90000', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('a3b9cdeb28e3dfd8cd447e999fb1fa1d', '4028b25d7888a7f4017888b1cde90000', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('a3d438fc9e0d635e6e7d86e203e8c5d7', '4028b25d7888a7f4017888b1cde90000', 'userTask', '현업테스트요청', '', false, '', '{"width":160,"height":40,"position-x":600,"position-y":240}');
insert into wf_element values ('a4e5a1853ac7156dd593cdcf6282d0d5', '4028b25d7888a7f4017888b1cde90000', 'userTask', '승인요청', '', false, '', '{"width":160,"height":40,"position-x":820,"position-y":390}');
insert into wf_element values ('a66f901d3eee16dc209632d5e5b7d75d', '4028b25d7888a7f4017888b1cde90000', 'commonStart', '', '', false, '', '{"width":40,"height":40,"position-x":50,"position-y":90}');
insert into wf_element values ('a6f88189bc6eea031259fe9b2a323e64', '4028b25d7888a7f4017888b1cde90000', 'userTask', '통합테스트', '', false, '', '{"width":160,"height":40,"position-x":390,"position-y":240}');
insert into wf_element values ('a70b423ca1fdf0f263672fb517fd21e5', '4028b25d7888a7f4017888b1cde90000', 'userTask', '승인요청', '', false, '', '{"width":160,"height":40,"position-x":600,"position-y":90}');
insert into wf_element values ('a7496301877e9d5d75fdf131d9f53bb0', '4028b25d7888a7f4017888b1cde90000', 'userTask', '개발계획서', '', false, '', '{"width":160,"height":40,"position-x":390,"position-y":90}');
insert into wf_element values ('a784ad9fd8658a836e6add496073295e', '4028b25d7888a7f4017888b1cde90000', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('a78f898a761809ab5c7cdaed87831605', '4028b25d7888a7f4017888b1cde90000', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('a8127b76277fc9e270b38bb66c15cbd6', '4028b25d7888a7f4017888b1cde90000', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('a81e635a2ab87151638f7a4831371cf4', '4028b25d7888a7f4017888b1cde90000', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('a81f0fc380f4a1a91aab9f8e40e8ffb2', '4028b25d7888a7f4017888b1cde90000', 'arrowConnector', '', '', false, '', '{"mid-point":[930,90],"target-point":[930,160]}');
insert into wf_element values ('aa2b8fd9d2b5812b3643e7a029f2fb7e', '4028b25d7888a7f4017888b1cde90000', 'userTask', '승인', '', false, '', '{"width":160,"height":40,"position-x":1020,"position-y":390}');
insert into wf_element values ('aa40acc642a50794b6ca1a40847ca573', '4028b25d7888a7f4017888b1cde90000', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('ab154b54952a199e68d63a7bb3177e0d', '4028b25d7888a7f4017888b1cde90000', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('ab1f576b0f30af5a67777ff1be67a90b', '4028b25d7888a7f4017888b1cde90000', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('abe525539de50a9ede8f432a991777a0', '4028b25d7888a7f4017888b1cde90000', 'exclusiveGateway', '', '', false, '', '{"width":34,"height":34,"position-x":180,"position-y":390}');
insert into wf_element values ('acb90e940ac03a947df7456d4f0c7967', '4028b25d7888a7f4017888b1cde90000', 'userTask', '처리완료', '', false, '', '{"width":160,"height":40,"position-x":340,"position-y":440}');
insert into wf_element values ('ad2e01e6ad438741b5fb7f543bd4ccc4', '4028b25d7888a7f4017888b1cde90000', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('ad4b094d7973b990ce47a890ce7e9732', '4028b25d7888a7f4017888b1cde90000', 'userTask', '설계검토', '', false, '', '{"width":160,"height":40,"position-x":180,"position-y":160}');
insert into wf_element values ('ae011f32321d467be8285b06002a891f', '4028b25d7888a7f4017888b1cde90000', 'arrowConnector', '', '', false, '', '{"mid-point":[930,240],"target-point":[930,310]}');
insert into wf_element values ('af0efa5526bc03dd8e2972d7239d6e29', '4028b25d7888a7f4017888b1cde90000', 'exclusiveGateway', '', '', false, '', '{"width":34,"height":34,"position-x":480,"position-y":390}');
insert into wf_element values ('af104e036cbb3dc685df86b88beb2657', '4028b25d7888a7f4017888b1cde90000', 'userTask', '승인', '', false, '', '{"width":160,"height":40,"position-x":800,"position-y":90}');
insert into wf_element values ('af7e4f166906386d39c345206a8ff1d5', '4028b25d7888a7f4017888b1cde90000', 'arrowConnector', '', '', false, '', '{"mid-point":[1020,430]}');
insert into wf_element values ('afccdbd546c84e6c5a791e30e014c83d', '4028b25d7888a7f4017888b1cde90000', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('a26d17f8cee448629486d2c222c8941b', '4028b25d788c4f8601788c7e678a0001', 'commonEnd', '종료', '', false, '', '{"width":40,"height":40,"position-x":1120,"position-y":380}');
insert into wf_element values ('a2b1a92cc7dc218e600b3d94e5b55f3f', '4028b25d788c4f8601788c7e678a0001', 'subprocess', '인프라', '', false, '', '{"width":152,"height":40,"position-x":760,"position-y":140}');
insert into wf_element values ('a4df5578630328debe1109f956aa8e64', '4028b25d788c4f8601788c7e678a0001', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('a576bed79ec1e898945a3ad14170bf5a', '4028b25d788c4f8601788c7e678a0001', 'exclusiveGateway', '', '', false, '', '{"width":34,"height":34,"position-x":940,"position-y":90}');
insert into wf_element values ('a6a9b76969a592d2dc32386b6aabe804', '4028b25d788c4f8601788c7e678a0001', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('a872caff4f0f32efb99bd23ac353fd34', '4028b25d788c4f8601788c7e678a0001', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('a8b4a594e51d22bc0412f96b2df042c8', '4028b25d788c4f8601788c7e678a0001', 'userTask', '접수', '', true, '', '{"width":160,"height":40,"position-x":440,"position-y":90}');
insert into wf_element values ('a8cc714075684188d65292c614323742', '4028b25d788c4f8601788c7e678a0001', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('a90c52c504704f2e9701c1ce19905ea7', '4028b25d788c4f8601788c7e678a0001', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('a98da74f954267149a667854a1f37c62', '4028b25d788c4f8601788c7e678a0001', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('a9d0ec58426d02286d11fe4e5ce6ef1f', '4028b25d788c4f8601788c7e678a0001', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('a9e771c5f8ba717f4096f883c7502309', '4028b25d788c4f8601788c7e678a0001', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('aa4952e410a69f6abc62d9d29c38d317', '4028b25d788c4f8601788c7e678a0001', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('ac0409214a212091ee70866dd1163ca2', '4028b25d788c4f8601788c7e678a0001', 'userTask', '승인', '', true, '', '{"width":160,"height":40,"position-x":1120,"position-y":180}');
insert into wf_element values ('ace10e7a9128597f74c8f33f4efb4af6', '4028b25d788c4f8601788c7e678a0001', 'signalSend', '만족도', '', false, '', '{"width":40,"height":40,"position-x":1120,"position-y":270}');
insert into wf_element values ('acfac5054c1b2da3dff103c143f3fb01', '4028b25d788c4f8601788c7e678a0001', 'commonStart', '', '', false, '', '{"width":40,"height":40,"position-x":90,"position-y":90}');
insert into wf_element values ('ad6b44dd201e5175ec8f9bddc0441415', '4028b25d788c4f8601788c7e678a0001', 'userTask', '승인요청', '', true, '', '{"width":160,"height":40,"position-x":1120,"position-y":90}');
insert into wf_element values ('add19d953a0c003cc3e24a745493a46d', '4028b25d788c4f8601788c7e678a0001', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('ae0977a001af0c36ec899bda5f37a299', '4028b25d788c4f8601788c7e678a0001', 'exclusiveGateway', '', '', false, '', '{"width":34,"height":34,"position-x":590,"position-y":90}');
insert into wf_element values ('ae599233b126d46d322594dc52902b29', '4028b25d788c4f8601788c7e678a0001', 'userTask', '신청서작성', '', false, '', '{"width":160,"height":40,"position-x":230,"position-y":90}');
insert into wf_element values ('ae5b5af4478d83bf9d2f13b8a8aa282a', '4028b25d788c4f8601788c7e678a0001', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('aff18768b0b37a3c29ec9803aab6c1f5', '4028b25d788c4f8601788c7e678a0001', 'subprocess', '어플리케이션', '', false, '', '{"width":152,"height":40,"position-x":760,"position-y":50}');
insert into wf_element values ('3a09ff6344194ec9af9a9488edcee914', '4028b25d788c4f8601788c905a790004', 'commonStart', '', '', false, '', '{"width":40,"height":40,"position-x":120,"position-y":200}');
insert into wf_element values ('98fe51cafe4948b2a10f47159dc6e06b', '4028b25d788c4f8601788c905a790004', 'commonEnd', '종료', '', false, '', '{"width":40,"height":40,"position-x":520,"position-y":200}');
insert into wf_element values ('a5e70d31eadc438f9f88bb065e89fb88', '4028b25d788c4f8601788c905a790004', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('c8285b3efe4649038cea85496b0f4548', '4028b25d788c4f8601788c905a790004', 'arrowConnector', '만족도평가', '', false, '', '{}');
insert into wf_element values ('dff53d10bd754541993d556b15b8c019', '4028b25d788c4f8601788c905a790004', 'userTask', '만족도평가', '', true, '', '{"width":160,"height":40,"position-x":320,"position-y":200}');
/**
 * 문서출력정보
 */
DROP TABLE IF EXISTS wf_document_display cascade;

CREATE TABLE wf_document_display
(
	document_id varchar(128) NOT NULL,
	component_id varchar(128) NOT NULL,
	element_id varchar(256) NOT NULL,
	display varchar(100) DEFAULT 'editable' NOT NULL,
	CONSTRAINT wf_document_display_pk PRIMARY KEY (document_id, component_id, element_id),
	CONSTRAINT wf_document_display_fk1 FOREIGN KEY (document_id) REFERENCES wf_document (document_id),
	CONSTRAINT wf_document_display_fk2 FOREIGN KEY (component_id) REFERENCES wf_component (component_id),
	CONSTRAINT wf_document_display_fk3 FOREIGN KEY (element_id) REFERENCES wf_element (element_id)
);

COMMENT ON TABLE wf_document_display IS '문서출력정보';
COMMENT ON COLUMN wf_document_display.document_id IS '신청서아이디';
COMMENT ON COLUMN wf_document_display.component_id IS '컴포넌트아이디';
COMMENT ON COLUMN wf_document_display.element_id IS '엘리먼트아이디';
COMMENT ON COLUMN wf_document_display.display IS '엘리먼트별컴포넌트출력정보';

insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a0438d6ebeb3e76391de4ca2591b891d', '4f296c1468f3422b8c59c97151e2c476', 'editable');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a0438d6ebeb3e76391de4ca2591b891d', 'a508191382c644b289c01cf32bed8722', 'readonly');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a0438d6ebeb3e76391de4ca2591b891d', 'a34268ba767d48e7ab7db9e7297e7300', 'readonly');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a0438d6ebeb3e76391de4ca2591b891d', 'bff880e180164c4f9cedfab4ba282f28', 'editable');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a083bcc9a0679f6bc9cde83eb4205161', 'a508191382c644b289c01cf32bed8722', 'readonly');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a083bcc9a0679f6bc9cde83eb4205161', '4f296c1468f3422b8c59c97151e2c476', 'editable');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a083bcc9a0679f6bc9cde83eb4205161', 'bff880e180164c4f9cedfab4ba282f28', 'readonly');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a083bcc9a0679f6bc9cde83eb4205161', 'a34268ba767d48e7ab7db9e7297e7300', 'readonly');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a15a240317ee972e96532bf01836fd5c', 'bff880e180164c4f9cedfab4ba282f28', 'editable');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a15a240317ee972e96532bf01836fd5c', 'a508191382c644b289c01cf32bed8722', 'readonly');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a15a240317ee972e96532bf01836fd5c', 'a34268ba767d48e7ab7db9e7297e7300', 'readonly');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a15a240317ee972e96532bf01836fd5c', '4f296c1468f3422b8c59c97151e2c476', 'editable');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a1709377086e44e4e319ba3c2aec93c4', 'bff880e180164c4f9cedfab4ba282f28', 'editableRequired');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a1709377086e44e4e319ba3c2aec93c4', 'a508191382c644b289c01cf32bed8722', 'readonly');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a1709377086e44e4e319ba3c2aec93c4', 'a34268ba767d48e7ab7db9e7297e7300', 'editableRequired');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a1709377086e44e4e319ba3c2aec93c4', '4f296c1468f3422b8c59c97151e2c476', 'editableRequired');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a1bbab51ba6b903eea7834b64ff2018e', '4f296c1468f3422b8c59c97151e2c476', 'hidden');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a1bbab51ba6b903eea7834b64ff2018e', 'bff880e180164c4f9cedfab4ba282f28', 'hidden');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a1bbab51ba6b903eea7834b64ff2018e', 'a34268ba767d48e7ab7db9e7297e7300', 'hidden');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a1bbab51ba6b903eea7834b64ff2018e', 'a508191382c644b289c01cf32bed8722', 'editableRequired');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a20306ee9477e23c3b460bdb459f5f89', 'a508191382c644b289c01cf32bed8722', 'readonly');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a20306ee9477e23c3b460bdb459f5f89', '4f296c1468f3422b8c59c97151e2c476', 'editable');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a20306ee9477e23c3b460bdb459f5f89', 'bff880e180164c4f9cedfab4ba282f28', 'editable');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a20306ee9477e23c3b460bdb459f5f89', 'a34268ba767d48e7ab7db9e7297e7300', 'readonly');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a293e254d00208aeb1afb8d78c0d0737', '4f296c1468f3422b8c59c97151e2c476', 'hidden');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a293e254d00208aeb1afb8d78c0d0737', 'a508191382c644b289c01cf32bed8722', 'readonly');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a293e254d00208aeb1afb8d78c0d0737', 'a34268ba767d48e7ab7db9e7297e7300', 'hidden');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a293e254d00208aeb1afb8d78c0d0737', 'bff880e180164c4f9cedfab4ba282f28', 'hidden');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a2ef0bbeda3884c8111f82abeb242d6f', 'bff880e180164c4f9cedfab4ba282f28', 'editable');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a2ef0bbeda3884c8111f82abeb242d6f', 'a508191382c644b289c01cf32bed8722', 'readonly');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a2ef0bbeda3884c8111f82abeb242d6f', 'a34268ba767d48e7ab7db9e7297e7300', 'readonly');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a2ef0bbeda3884c8111f82abeb242d6f', '4f296c1468f3422b8c59c97151e2c476', 'hidden');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a40b7f5aaa39c16cf44d8db83f3f2a5b', '4f296c1468f3422b8c59c97151e2c476', 'hidden');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a40b7f5aaa39c16cf44d8db83f3f2a5b', 'a34268ba767d48e7ab7db9e7297e7300', 'editable');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a40b7f5aaa39c16cf44d8db83f3f2a5b', 'a508191382c644b289c01cf32bed8722', 'readonly');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a40b7f5aaa39c16cf44d8db83f3f2a5b', 'bff880e180164c4f9cedfab4ba282f28', 'hidden');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a4c6a3dd66d0cc36d108ff8cc6904fd7', '4f296c1468f3422b8c59c97151e2c476', 'hidden');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a4c6a3dd66d0cc36d108ff8cc6904fd7', 'a508191382c644b289c01cf32bed8722', 'readonly');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a4c6a3dd66d0cc36d108ff8cc6904fd7', 'a34268ba767d48e7ab7db9e7297e7300', 'editableRequired');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a4c6a3dd66d0cc36d108ff8cc6904fd7', 'bff880e180164c4f9cedfab4ba282f28', 'hidden');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a563dbfc8286f3ed75f47b8e778390fb', '4f296c1468f3422b8c59c97151e2c476', 'editable');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a563dbfc8286f3ed75f47b8e778390fb', 'a508191382c644b289c01cf32bed8722', 'readonly');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a563dbfc8286f3ed75f47b8e778390fb', 'a34268ba767d48e7ab7db9e7297e7300', 'readonly');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a563dbfc8286f3ed75f47b8e778390fb', 'bff880e180164c4f9cedfab4ba282f28', 'readonly');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a640301d7e67dc92c279f83bec7358be', 'a508191382c644b289c01cf32bed8722', 'readonly');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a640301d7e67dc92c279f83bec7358be', '4f296c1468f3422b8c59c97151e2c476', 'hidden');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a640301d7e67dc92c279f83bec7358be', 'bff880e180164c4f9cedfab4ba282f28', 'readonly');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a640301d7e67dc92c279f83bec7358be', 'a34268ba767d48e7ab7db9e7297e7300', 'readonly');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a661cc2ef029e637a72124a4b7c5f32f', '4f296c1468f3422b8c59c97151e2c476', 'hidden');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a661cc2ef029e637a72124a4b7c5f32f', 'a508191382c644b289c01cf32bed8722', 'readonly');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a661cc2ef029e637a72124a4b7c5f32f', 'a34268ba767d48e7ab7db9e7297e7300', 'readonly');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a661cc2ef029e637a72124a4b7c5f32f', 'bff880e180164c4f9cedfab4ba282f28', 'hidden');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a7bc03e8abd70100a86e43338e308054', 'bff880e180164c4f9cedfab4ba282f28', 'editableRequired');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a7bc03e8abd70100a86e43338e308054', 'a508191382c644b289c01cf32bed8722', 'readonly');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a7bc03e8abd70100a86e43338e308054', 'a34268ba767d48e7ab7db9e7297e7300', 'readonly');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a7bc03e8abd70100a86e43338e308054', '4f296c1468f3422b8c59c97151e2c476', 'editableRequired');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a7fb73ce3c61d3d600ab135d6a6a8684', '4f296c1468f3422b8c59c97151e2c476', 'editable');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a7fb73ce3c61d3d600ab135d6a6a8684', 'a508191382c644b289c01cf32bed8722', 'readonly');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a7fb73ce3c61d3d600ab135d6a6a8684', 'a34268ba767d48e7ab7db9e7297e7300', 'readonly');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a7fb73ce3c61d3d600ab135d6a6a8684', 'bff880e180164c4f9cedfab4ba282f28', 'readonly');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a82c5acd1b0ec0bba083d76135d39e5a', 'a508191382c644b289c01cf32bed8722', 'editable');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a82c5acd1b0ec0bba083d76135d39e5a', '4f296c1468f3422b8c59c97151e2c476', 'hidden');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a82c5acd1b0ec0bba083d76135d39e5a', 'a34268ba767d48e7ab7db9e7297e7300', 'editable');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a82c5acd1b0ec0bba083d76135d39e5a', 'bff880e180164c4f9cedfab4ba282f28', 'hidden');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a87535522638ab7240e972d44d66ef49', 'a508191382c644b289c01cf32bed8722', 'editable');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a87535522638ab7240e972d44d66ef49', '4f296c1468f3422b8c59c97151e2c476', 'hidden');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a87535522638ab7240e972d44d66ef49', 'bff880e180164c4f9cedfab4ba282f28', 'hidden');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a87535522638ab7240e972d44d66ef49', 'a34268ba767d48e7ab7db9e7297e7300', 'editable');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a90620f766dedb2beebbe776eff556ec', 'a34268ba767d48e7ab7db9e7297e7300', 'readonly');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a90620f766dedb2beebbe776eff556ec', 'a508191382c644b289c01cf32bed8722', 'readonly');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a90620f766dedb2beebbe776eff556ec', 'bff880e180164c4f9cedfab4ba282f28', 'readonly');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a90620f766dedb2beebbe776eff556ec', '4f296c1468f3422b8c59c97151e2c476', 'readonly');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a9611c745a6716665c5e7690b872310b', '4f296c1468f3422b8c59c97151e2c476', 'hidden');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a9611c745a6716665c5e7690b872310b', 'a508191382c644b289c01cf32bed8722', 'readonly');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a9611c745a6716665c5e7690b872310b', 'a34268ba767d48e7ab7db9e7297e7300', 'editableRequired');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a9611c745a6716665c5e7690b872310b', 'bff880e180164c4f9cedfab4ba282f28', 'hidden');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a9b580dd25da55ce3ce347bcef0169d5', 'bff880e180164c4f9cedfab4ba282f28', 'editable');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a9b580dd25da55ce3ce347bcef0169d5', 'a508191382c644b289c01cf32bed8722', 'editable');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a9b580dd25da55ce3ce347bcef0169d5', 'a34268ba767d48e7ab7db9e7297e7300', 'editable');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a9b580dd25da55ce3ce347bcef0169d5', '4f296c1468f3422b8c59c97151e2c476', 'editable');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a9ef096c157fab2ec319efcf467b568a', '4f296c1468f3422b8c59c97151e2c476', 'editableRequired');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a9ef096c157fab2ec319efcf467b568a', 'bff880e180164c4f9cedfab4ba282f28', 'readonly');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a9ef096c157fab2ec319efcf467b568a', 'a34268ba767d48e7ab7db9e7297e7300', 'readonly');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'a9ef096c157fab2ec319efcf467b568a', 'a508191382c644b289c01cf32bed8722', 'readonly');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'aa54cd88560b84b902653fd90336ee1e', 'bff880e180164c4f9cedfab4ba282f28', 'hidden');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'aa54cd88560b84b902653fd90336ee1e', '4f296c1468f3422b8c59c97151e2c476', 'hidden');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'aa54cd88560b84b902653fd90336ee1e', 'a34268ba767d48e7ab7db9e7297e7300', 'hidden');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'aa54cd88560b84b902653fd90336ee1e', 'a508191382c644b289c01cf32bed8722', 'editable');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'aa5a75f1766bc691fa0c066202c776ae', '4f296c1468f3422b8c59c97151e2c476', 'hidden');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'aa5a75f1766bc691fa0c066202c776ae', 'a508191382c644b289c01cf32bed8722', 'readonly');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'aa5a75f1766bc691fa0c066202c776ae', 'a34268ba767d48e7ab7db9e7297e7300', 'readonly');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'aa5a75f1766bc691fa0c066202c776ae', 'bff880e180164c4f9cedfab4ba282f28', 'editable');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'abc34c5409954d3c792f2209f3347cdf', '4f296c1468f3422b8c59c97151e2c476', 'hidden');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'abc34c5409954d3c792f2209f3347cdf', 'a508191382c644b289c01cf32bed8722', 'readonly');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'abc34c5409954d3c792f2209f3347cdf', 'a34268ba767d48e7ab7db9e7297e7300', 'readonly');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'abc34c5409954d3c792f2209f3347cdf', 'bff880e180164c4f9cedfab4ba282f28', 'editable');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'ad5e4623a663879158834cbdddd032e5', 'a508191382c644b289c01cf32bed8722', 'editable');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'ad5e4623a663879158834cbdddd032e5', '4f296c1468f3422b8c59c97151e2c476', 'hidden');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'ad5e4623a663879158834cbdddd032e5', 'a34268ba767d48e7ab7db9e7297e7300', 'hidden');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'ad5e4623a663879158834cbdddd032e5', 'bff880e180164c4f9cedfab4ba282f28', 'hidden');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'ad6afd860d2cda7a78bf93f4e23c8efd', '4f296c1468f3422b8c59c97151e2c476', 'editable');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'ad6afd860d2cda7a78bf93f4e23c8efd', 'a508191382c644b289c01cf32bed8722', 'editable');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'ad6afd860d2cda7a78bf93f4e23c8efd', 'a34268ba767d48e7ab7db9e7297e7300', 'editable');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'ad6afd860d2cda7a78bf93f4e23c8efd', 'bff880e180164c4f9cedfab4ba282f28', 'editable');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'ae615f2ac0d145ef25d5e7253c12940d', '4f296c1468f3422b8c59c97151e2c476', 'editable');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'ae615f2ac0d145ef25d5e7253c12940d', 'a508191382c644b289c01cf32bed8722', 'editable');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'ae615f2ac0d145ef25d5e7253c12940d', 'a34268ba767d48e7ab7db9e7297e7300', 'editable');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'ae615f2ac0d145ef25d5e7253c12940d', 'bff880e180164c4f9cedfab4ba282f28', 'editable');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'aff3e77073f75d2385e2347ed996f412', '4f296c1468f3422b8c59c97151e2c476', 'editableRequired');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'aff3e77073f75d2385e2347ed996f412', 'a508191382c644b289c01cf32bed8722', 'readonly');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'aff3e77073f75d2385e2347ed996f412', 'a34268ba767d48e7ab7db9e7297e7300', 'readonly');
insert into wf_document_display values ('40288ab777f04ed90177f05f01d1000b', 'aff3e77073f75d2385e2347ed996f412', 'bff880e180164c4f9cedfab4ba282f28', 'editableRequired');
insert into wf_document_display values ('4028b25d78778da6017877aff7e40001', '53dc0f34890f4c48a7ebd410169623b6', '32b06c8749f1466aa448c2ff6ccf0fdb', 'editable');
insert into wf_document_display values ('4028b25d78778da6017877aff7e40001', 'caaf208e3d6c4ea0a4ec4d1fa63fc81c', '32b06c8749f1466aa448c2ff6ccf0fdb', 'editable');
insert into wf_document_display values ('4028b25d78778da6017877aff7e40001', 'f79d816a6ae748aabc1c58748b65c119', '32b06c8749f1466aa448c2ff6ccf0fdb', 'editable');
insert into wf_document_display values ('4028b25d78778da6017877aff7e40001', 'cf96b8a2488c4b84a90002b01f80aeba', '32b06c8749f1466aa448c2ff6ccf0fdb', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877aff7e40001', 'aca126bd4f6958bce2cb5e6e607251d0', '32b06c8749f1466aa448c2ff6ccf0fdb', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877aff7e40001', 'c088820086ef49488a3033eb84566fa5', '32b06c8749f1466aa448c2ff6ccf0fdb', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877aff7e40001', '5e6444cb4fad489fa76cacd2b8a26108', '32b06c8749f1466aa448c2ff6ccf0fdb', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877aff7e40001', '3c592c92c4bc4868a4b585f8c9d9c360', '32b06c8749f1466aa448c2ff6ccf0fdb', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877aff7e40001', 'a6ba20dc101143c1b6bcc21c159cc1dd', '32b06c8749f1466aa448c2ff6ccf0fdb', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877aff7e40001', 'ad4c94b7f64a4d01bafef76fbbb444bc', '32b06c8749f1466aa448c2ff6ccf0fdb', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877aff7e40001', 'ee6e34704b094873926c3918ae00bfbd', '32b06c8749f1466aa448c2ff6ccf0fdb', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877aff7e40001', '5b8529ddbd40462e824439a753b2c153', '32b06c8749f1466aa448c2ff6ccf0fdb', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877aff7e40001', '74b8c62595f544eaa47d7c8b6ea8560e', '32b06c8749f1466aa448c2ff6ccf0fdb', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877aff7e40001', '4e1c394c09c84cb2a20b20492f2a3cb0', '32b06c8749f1466aa448c2ff6ccf0fdb', 'editable');
insert into wf_document_display values ('4028b25d78778da6017877aff7e40001', '022540d758ec405ba45d70f8df0c03fc', '32b06c8749f1466aa448c2ff6ccf0fdb', 'editable');
insert into wf_document_display values ('4028b25d78778da6017877aff7e40001', '36b04439545649a79824d742d8d48a61', '32b06c8749f1466aa448c2ff6ccf0fdb', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877aff7e40001', '489d27f3f1eb454780256befc0167cf6', '32b06c8749f1466aa448c2ff6ccf0fdb', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877aff7e40001', 'e5f66323f50b498189d7ebccfaf3c4a9', '32b06c8749f1466aa448c2ff6ccf0fdb', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877aff7e40001', '27e376940afc4568aa228f8663318ea0', '32b06c8749f1466aa448c2ff6ccf0fdb', 'editable');
insert into wf_document_display values ('4028b25d78778da6017877aff7e40001', '357f6d029a3d420d9fefba4e5a6ca9df', '32b06c8749f1466aa448c2ff6ccf0fdb', 'editable');
insert into wf_document_display values ('4028b25d78778da6017877aff7e40001', 'b28cb8265d134217b7336704c3554314', '32b06c8749f1466aa448c2ff6ccf0fdb', 'editableRequired');
insert into wf_document_display values ('4028b25d78778da6017877aff7e40001', '4ba00478892f4195a10597b3090d887c', '32b06c8749f1466aa448c2ff6ccf0fdb', 'editableRequired');
insert into wf_document_display values ('4028b25d78778da6017877aff7e40001', '974aa952322d4800ab68d7efaa5c921f', '32b06c8749f1466aa448c2ff6ccf0fdb', 'editable');
insert into wf_document_display values ('4028b25d78778da6017877aff7e40001', '130111f086244dc393963a75a480631e', '32b06c8749f1466aa448c2ff6ccf0fdb', 'editable');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', 'e4a75d472ad94be0abc41b7be5c60a06', 'f64ad538612043189e288b43d4bbd7ce', 'editable');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', 'e4a75d472ad94be0abc41b7be5c60a06', '5da2b19767f745c4af6cce63614954f0', 'editable');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', 'e4a75d472ad94be0abc41b7be5c60a06', 'd3e931756f15444fa36732356e01a816', 'editable');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', 'e4a75d472ad94be0abc41b7be5c60a06', '9fa61b785b944e87b02e9fa753b4c0c5', 'editable');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '29fc1c91ed9145c2ab4569db715ea461', 'f64ad538612043189e288b43d4bbd7ce', 'editable');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '29fc1c91ed9145c2ab4569db715ea461', '5da2b19767f745c4af6cce63614954f0', 'editable');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '29fc1c91ed9145c2ab4569db715ea461', 'd3e931756f15444fa36732356e01a816', 'editable');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '29fc1c91ed9145c2ab4569db715ea461', '9fa61b785b944e87b02e9fa753b4c0c5', 'editable');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '6b20477610204c32ac7460143405299f', 'f64ad538612043189e288b43d4bbd7ce', 'editable');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '6b20477610204c32ac7460143405299f', '5da2b19767f745c4af6cce63614954f0', 'editable');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '6b20477610204c32ac7460143405299f', 'd3e931756f15444fa36732356e01a816', 'editable');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '6b20477610204c32ac7460143405299f', '9fa61b785b944e87b02e9fa753b4c0c5', 'editable');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '763a02453da34238a611908d427b03a4', 'f64ad538612043189e288b43d4bbd7ce', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '763a02453da34238a611908d427b03a4', '5da2b19767f745c4af6cce63614954f0', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '763a02453da34238a611908d427b03a4', 'd3e931756f15444fa36732356e01a816', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '763a02453da34238a611908d427b03a4', '9fa61b785b944e87b02e9fa753b4c0c5', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', 'a64a8b2b177f51b09134faf7d148a4bb', 'f64ad538612043189e288b43d4bbd7ce', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', 'a64a8b2b177f51b09134faf7d148a4bb', '5da2b19767f745c4af6cce63614954f0', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', 'a64a8b2b177f51b09134faf7d148a4bb', 'd3e931756f15444fa36732356e01a816', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', 'a64a8b2b177f51b09134faf7d148a4bb', '9fa61b785b944e87b02e9fa753b4c0c5', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '189aafb3a9404ec489607fef8b9f8560', 'f64ad538612043189e288b43d4bbd7ce', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '189aafb3a9404ec489607fef8b9f8560', '5da2b19767f745c4af6cce63614954f0', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '189aafb3a9404ec489607fef8b9f8560', 'd3e931756f15444fa36732356e01a816', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '189aafb3a9404ec489607fef8b9f8560', '9fa61b785b944e87b02e9fa753b4c0c5', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '80060ae8be4b42a9883bc7b7dd96632b', 'f64ad538612043189e288b43d4bbd7ce', 'editable');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '80060ae8be4b42a9883bc7b7dd96632b', '5da2b19767f745c4af6cce63614954f0', 'editable');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '80060ae8be4b42a9883bc7b7dd96632b', 'd3e931756f15444fa36732356e01a816', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '80060ae8be4b42a9883bc7b7dd96632b', '9fa61b785b944e87b02e9fa753b4c0c5', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', 'a02d6fbdc20e46b08c95453f6d25c311', 'f64ad538612043189e288b43d4bbd7ce', 'editable');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', 'a02d6fbdc20e46b08c95453f6d25c311', '5da2b19767f745c4af6cce63614954f0', 'editable');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', 'a02d6fbdc20e46b08c95453f6d25c311', 'd3e931756f15444fa36732356e01a816', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', 'a02d6fbdc20e46b08c95453f6d25c311', '9fa61b785b944e87b02e9fa753b4c0c5', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '0177a25c92ad43aebd50bfc45d32fc02', 'f64ad538612043189e288b43d4bbd7ce', 'editableRequired');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '0177a25c92ad43aebd50bfc45d32fc02', '5da2b19767f745c4af6cce63614954f0', 'editableRequired');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '0177a25c92ad43aebd50bfc45d32fc02', 'd3e931756f15444fa36732356e01a816', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '0177a25c92ad43aebd50bfc45d32fc02', '9fa61b785b944e87b02e9fa753b4c0c5', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '303f8e1f7e814953a759ff93ee336f65', 'f64ad538612043189e288b43d4bbd7ce', 'editableRequired');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '303f8e1f7e814953a759ff93ee336f65', '5da2b19767f745c4af6cce63614954f0', 'editableRequired');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '303f8e1f7e814953a759ff93ee336f65', 'd3e931756f15444fa36732356e01a816', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '303f8e1f7e814953a759ff93ee336f65', '9fa61b785b944e87b02e9fa753b4c0c5', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '8fe7d5f6c16a49788420fe648a9a4cb2', 'f64ad538612043189e288b43d4bbd7ce', 'editableRequired');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '8fe7d5f6c16a49788420fe648a9a4cb2', '5da2b19767f745c4af6cce63614954f0', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '8fe7d5f6c16a49788420fe648a9a4cb2', 'd3e931756f15444fa36732356e01a816', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '8fe7d5f6c16a49788420fe648a9a4cb2', '9fa61b785b944e87b02e9fa753b4c0c5', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '63c2bbf75cb9456a9de83c2f668ad181', 'f64ad538612043189e288b43d4bbd7ce', 'editable');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '63c2bbf75cb9456a9de83c2f668ad181', '5da2b19767f745c4af6cce63614954f0', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '63c2bbf75cb9456a9de83c2f668ad181', 'd3e931756f15444fa36732356e01a816', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '63c2bbf75cb9456a9de83c2f668ad181', '9fa61b785b944e87b02e9fa753b4c0c5', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', 'cec8377af21641e1a2a274db6654aa88', 'f64ad538612043189e288b43d4bbd7ce', 'editable');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', 'cec8377af21641e1a2a274db6654aa88', '5da2b19767f745c4af6cce63614954f0', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', 'cec8377af21641e1a2a274db6654aa88', 'd3e931756f15444fa36732356e01a816', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', 'cec8377af21641e1a2a274db6654aa88', '9fa61b785b944e87b02e9fa753b4c0c5', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', 'de47121e6034445598ff4c216168781e', 'f64ad538612043189e288b43d4bbd7ce', 'editable');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', 'de47121e6034445598ff4c216168781e', '5da2b19767f745c4af6cce63614954f0', 'editable');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', 'de47121e6034445598ff4c216168781e', 'd3e931756f15444fa36732356e01a816', 'editable');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', 'de47121e6034445598ff4c216168781e', '9fa61b785b944e87b02e9fa753b4c0c5', 'editable');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '7b53c1640c1640ca889f6e44ac92a256', 'f64ad538612043189e288b43d4bbd7ce', 'hidden');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '7b53c1640c1640ca889f6e44ac92a256', '5da2b19767f745c4af6cce63614954f0', 'editable');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '7b53c1640c1640ca889f6e44ac92a256', 'd3e931756f15444fa36732356e01a816', 'editable');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '7b53c1640c1640ca889f6e44ac92a256', '9fa61b785b944e87b02e9fa753b4c0c5', 'editable');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '2b2f880adf2f434c8b71d57f3439129e', 'f64ad538612043189e288b43d4bbd7ce', 'hidden');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '2b2f880adf2f434c8b71d57f3439129e', '5da2b19767f745c4af6cce63614954f0', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '2b2f880adf2f434c8b71d57f3439129e', 'd3e931756f15444fa36732356e01a816', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '2b2f880adf2f434c8b71d57f3439129e', '9fa61b785b944e87b02e9fa753b4c0c5', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '568e2d7388614f349756a9d55f9fbf82', 'f64ad538612043189e288b43d4bbd7ce', 'hidden');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '568e2d7388614f349756a9d55f9fbf82', '5da2b19767f745c4af6cce63614954f0', 'editableRequired');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '568e2d7388614f349756a9d55f9fbf82', 'd3e931756f15444fa36732356e01a816', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '568e2d7388614f349756a9d55f9fbf82', '9fa61b785b944e87b02e9fa753b4c0c5', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', 'fc2d992990704ad7be4eb8fb52839a0b', 'f64ad538612043189e288b43d4bbd7ce', 'hidden');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', 'fc2d992990704ad7be4eb8fb52839a0b', '5da2b19767f745c4af6cce63614954f0', 'editable');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', 'fc2d992990704ad7be4eb8fb52839a0b', 'd3e931756f15444fa36732356e01a816', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', 'fc2d992990704ad7be4eb8fb52839a0b', '9fa61b785b944e87b02e9fa753b4c0c5', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', 'cf757877eb04473492014366b49d7aef', 'f64ad538612043189e288b43d4bbd7ce', 'hidden');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', 'cf757877eb04473492014366b49d7aef', '5da2b19767f745c4af6cce63614954f0', 'hidden');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', 'cf757877eb04473492014366b49d7aef', 'd3e931756f15444fa36732356e01a816', 'editable');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', 'cf757877eb04473492014366b49d7aef', '9fa61b785b944e87b02e9fa753b4c0c5', 'editable');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '4601ce00de0a463098f4d4c1a020028f', 'f64ad538612043189e288b43d4bbd7ce', 'hidden');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '4601ce00de0a463098f4d4c1a020028f', '5da2b19767f745c4af6cce63614954f0', 'hidden');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '4601ce00de0a463098f4d4c1a020028f', 'd3e931756f15444fa36732356e01a816', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '4601ce00de0a463098f4d4c1a020028f', '9fa61b785b944e87b02e9fa753b4c0c5', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', 'c2a95ab676214ffa82571c8d24047f1a', 'f64ad538612043189e288b43d4bbd7ce', 'hidden');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', 'c2a95ab676214ffa82571c8d24047f1a', '5da2b19767f745c4af6cce63614954f0', 'hidden');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', 'c2a95ab676214ffa82571c8d24047f1a', 'd3e931756f15444fa36732356e01a816', 'editableRequired');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', 'c2a95ab676214ffa82571c8d24047f1a', '9fa61b785b944e87b02e9fa753b4c0c5', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', 'c57c3c17b8b34783ba385fa33d96570c', 'f64ad538612043189e288b43d4bbd7ce', 'hidden');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', 'c57c3c17b8b34783ba385fa33d96570c', '5da2b19767f745c4af6cce63614954f0', 'hidden');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', 'c57c3c17b8b34783ba385fa33d96570c', 'd3e931756f15444fa36732356e01a816', 'editable');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', 'c57c3c17b8b34783ba385fa33d96570c', '9fa61b785b944e87b02e9fa753b4c0c5', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', 'f2bf25aa9b87468ba02398838d110d9d', 'f64ad538612043189e288b43d4bbd7ce', 'hidden');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', 'f2bf25aa9b87468ba02398838d110d9d', '5da2b19767f745c4af6cce63614954f0', 'hidden');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', 'f2bf25aa9b87468ba02398838d110d9d', 'd3e931756f15444fa36732356e01a816', 'editableRequired');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', 'f2bf25aa9b87468ba02398838d110d9d', '9fa61b785b944e87b02e9fa753b4c0c5', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', 'aec9c3f0a2cc456c9870e7cda1b6980a', 'f64ad538612043189e288b43d4bbd7ce', 'hidden');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', 'aec9c3f0a2cc456c9870e7cda1b6980a', '5da2b19767f745c4af6cce63614954f0', 'hidden');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', 'aec9c3f0a2cc456c9870e7cda1b6980a', 'd3e931756f15444fa36732356e01a816', 'editable');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', 'aec9c3f0a2cc456c9870e7cda1b6980a', '9fa61b785b944e87b02e9fa753b4c0c5', 'editable');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', 'f4639d8fee6a4796baf464b8f55105e5', 'f64ad538612043189e288b43d4bbd7ce', 'hidden');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', 'f4639d8fee6a4796baf464b8f55105e5', '5da2b19767f745c4af6cce63614954f0', 'hidden');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', 'f4639d8fee6a4796baf464b8f55105e5', 'd3e931756f15444fa36732356e01a816', 'hidden');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', 'f4639d8fee6a4796baf464b8f55105e5', '9fa61b785b944e87b02e9fa753b4c0c5', 'editable');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '9cfaa92789b64c7da46c205995416821', 'f64ad538612043189e288b43d4bbd7ce', 'hidden');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '9cfaa92789b64c7da46c205995416821', '5da2b19767f745c4af6cce63614954f0', 'hidden');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '9cfaa92789b64c7da46c205995416821', 'd3e931756f15444fa36732356e01a816', 'hidden');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '9cfaa92789b64c7da46c205995416821', '9fa61b785b944e87b02e9fa753b4c0c5', 'readonly');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '05df4c3c4c2641f082b8cb181327511d', 'f64ad538612043189e288b43d4bbd7ce', 'hidden');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '05df4c3c4c2641f082b8cb181327511d', '5da2b19767f745c4af6cce63614954f0', 'hidden');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '05df4c3c4c2641f082b8cb181327511d', 'd3e931756f15444fa36732356e01a816', 'hidden');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '05df4c3c4c2641f082b8cb181327511d', '9fa61b785b944e87b02e9fa753b4c0c5', 'editableRequired');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '50871faf2bf74d2b8198b0049c9eab72', 'f64ad538612043189e288b43d4bbd7ce', 'hidden');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '50871faf2bf74d2b8198b0049c9eab72', '5da2b19767f745c4af6cce63614954f0', 'hidden');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '50871faf2bf74d2b8198b0049c9eab72', 'd3e931756f15444fa36732356e01a816', 'hidden');
insert into wf_document_display values ('4028b25d78778da6017877bb3d3c0010', '50871faf2bf74d2b8198b0049c9eab72', '9fa61b785b944e87b02e9fa753b4c0c5', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a042929dd4bbca295a5e14f7c3479630', 'a258e0bfcfae24e99f75be596efc173e', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a042929dd4bbca295a5e14f7c3479630', 'aeacaa70d21347699f5c311163d27085', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a042929dd4bbca295a5e14f7c3479630', 'aa87e149e75de05f31f28fb9309e91df', 'editableRequired');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a042929dd4bbca295a5e14f7c3479630', 'aaa5103974ec9059469823d84d8ce109', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a042929dd4bbca295a5e14f7c3479630', 'a8a1e1db38cdfdbaa692e14db009c224', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a042929dd4bbca295a5e14f7c3479630', 'a82fb95a6550c3cd5dadedd7f597c598', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a049fd4ac6c927ccde29ae160ee5d846', 'aa87e149e75de05f31f28fb9309e91df', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a049fd4ac6c927ccde29ae160ee5d846', 'aaa5103974ec9059469823d84d8ce109', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a049fd4ac6c927ccde29ae160ee5d846', 'a8a1e1db38cdfdbaa692e14db009c224', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a049fd4ac6c927ccde29ae160ee5d846', 'aeacaa70d21347699f5c311163d27085', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a049fd4ac6c927ccde29ae160ee5d846', 'a82fb95a6550c3cd5dadedd7f597c598', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a049fd4ac6c927ccde29ae160ee5d846', 'a258e0bfcfae24e99f75be596efc173e', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a04e548325ed8ae31a385a702f69ea10', 'aaa5103974ec9059469823d84d8ce109', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a04e548325ed8ae31a385a702f69ea10', 'aeacaa70d21347699f5c311163d27085', 'editableRequired');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a04e548325ed8ae31a385a702f69ea10', 'a258e0bfcfae24e99f75be596efc173e', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a04e548325ed8ae31a385a702f69ea10', 'a82fb95a6550c3cd5dadedd7f597c598', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a04e548325ed8ae31a385a702f69ea10', 'a8a1e1db38cdfdbaa692e14db009c224', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a04e548325ed8ae31a385a702f69ea10', 'aa87e149e75de05f31f28fb9309e91df', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a1019e08196f6803f8573d04348783b2', 'aaa5103974ec9059469823d84d8ce109', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a1019e08196f6803f8573d04348783b2', 'aeacaa70d21347699f5c311163d27085', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a1019e08196f6803f8573d04348783b2', 'aa87e149e75de05f31f28fb9309e91df', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a1019e08196f6803f8573d04348783b2', 'a8a1e1db38cdfdbaa692e14db009c224', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a1019e08196f6803f8573d04348783b2', 'a82fb95a6550c3cd5dadedd7f597c598', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a1019e08196f6803f8573d04348783b2', 'a258e0bfcfae24e99f75be596efc173e', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a11e0b9db990bab2241122481f268de4', 'a82fb95a6550c3cd5dadedd7f597c598', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a11e0b9db990bab2241122481f268de4', 'a8a1e1db38cdfdbaa692e14db009c224', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a11e0b9db990bab2241122481f268de4', 'aaa5103974ec9059469823d84d8ce109', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a11e0b9db990bab2241122481f268de4', 'aa87e149e75de05f31f28fb9309e91df', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a11e0b9db990bab2241122481f268de4', 'aeacaa70d21347699f5c311163d27085', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a11e0b9db990bab2241122481f268de4', 'a258e0bfcfae24e99f75be596efc173e', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a1241ec6aabdb13fe23fd435b3fbbb0b', 'aeacaa70d21347699f5c311163d27085', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a1241ec6aabdb13fe23fd435b3fbbb0b', 'a258e0bfcfae24e99f75be596efc173e', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a1241ec6aabdb13fe23fd435b3fbbb0b', 'a82fb95a6550c3cd5dadedd7f597c598', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a1241ec6aabdb13fe23fd435b3fbbb0b', 'a8a1e1db38cdfdbaa692e14db009c224', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a1241ec6aabdb13fe23fd435b3fbbb0b', 'aaa5103974ec9059469823d84d8ce109', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a1241ec6aabdb13fe23fd435b3fbbb0b', 'aa87e149e75de05f31f28fb9309e91df', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a251ce5051063fbf6aba8b1f4a4d7d94', 'aeacaa70d21347699f5c311163d27085', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a251ce5051063fbf6aba8b1f4a4d7d94', 'a258e0bfcfae24e99f75be596efc173e', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a251ce5051063fbf6aba8b1f4a4d7d94', 'a82fb95a6550c3cd5dadedd7f597c598', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a251ce5051063fbf6aba8b1f4a4d7d94', 'a8a1e1db38cdfdbaa692e14db009c224', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a251ce5051063fbf6aba8b1f4a4d7d94', 'aaa5103974ec9059469823d84d8ce109', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a251ce5051063fbf6aba8b1f4a4d7d94', 'aa87e149e75de05f31f28fb9309e91df', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a2d59c6c2668e4124c2883bf31d02ae2', 'aeacaa70d21347699f5c311163d27085', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a2d59c6c2668e4124c2883bf31d02ae2', 'a258e0bfcfae24e99f75be596efc173e', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a2d59c6c2668e4124c2883bf31d02ae2', 'a82fb95a6550c3cd5dadedd7f597c598', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a2d59c6c2668e4124c2883bf31d02ae2', 'a8a1e1db38cdfdbaa692e14db009c224', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a2d59c6c2668e4124c2883bf31d02ae2', 'aaa5103974ec9059469823d84d8ce109', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a2d59c6c2668e4124c2883bf31d02ae2', 'aa87e149e75de05f31f28fb9309e91df', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a30a607c9b0d1fd73b1944aad77c4c04', 'aa87e149e75de05f31f28fb9309e91df', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a30a607c9b0d1fd73b1944aad77c4c04', 'aaa5103974ec9059469823d84d8ce109', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a30a607c9b0d1fd73b1944aad77c4c04', 'a8a1e1db38cdfdbaa692e14db009c224', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a30a607c9b0d1fd73b1944aad77c4c04', 'a82fb95a6550c3cd5dadedd7f597c598', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a30a607c9b0d1fd73b1944aad77c4c04', 'a258e0bfcfae24e99f75be596efc173e', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a30a607c9b0d1fd73b1944aad77c4c04', 'aeacaa70d21347699f5c311163d27085', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a3452770c3511a852cc1c862d5f787b4', 'aeacaa70d21347699f5c311163d27085', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a3452770c3511a852cc1c862d5f787b4', 'a82fb95a6550c3cd5dadedd7f597c598', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a3452770c3511a852cc1c862d5f787b4', 'a8a1e1db38cdfdbaa692e14db009c224', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a3452770c3511a852cc1c862d5f787b4', 'aaa5103974ec9059469823d84d8ce109', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a3452770c3511a852cc1c862d5f787b4', 'aa87e149e75de05f31f28fb9309e91df', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a3452770c3511a852cc1c862d5f787b4', 'a258e0bfcfae24e99f75be596efc173e', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a457cdd3533cd9c491fc5f60f117ff59', 'aa87e149e75de05f31f28fb9309e91df', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a457cdd3533cd9c491fc5f60f117ff59', 'aeacaa70d21347699f5c311163d27085', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a457cdd3533cd9c491fc5f60f117ff59', 'aaa5103974ec9059469823d84d8ce109', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a457cdd3533cd9c491fc5f60f117ff59', 'a8a1e1db38cdfdbaa692e14db009c224', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a457cdd3533cd9c491fc5f60f117ff59', 'a82fb95a6550c3cd5dadedd7f597c598', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a457cdd3533cd9c491fc5f60f117ff59', 'a258e0bfcfae24e99f75be596efc173e', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a4edc285df4003747ec99b7d0d9e6a42', 'aa87e149e75de05f31f28fb9309e91df', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a4edc285df4003747ec99b7d0d9e6a42', 'aeacaa70d21347699f5c311163d27085', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a4edc285df4003747ec99b7d0d9e6a42', 'a258e0bfcfae24e99f75be596efc173e', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a4edc285df4003747ec99b7d0d9e6a42', 'a82fb95a6550c3cd5dadedd7f597c598', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a4edc285df4003747ec99b7d0d9e6a42', 'a8a1e1db38cdfdbaa692e14db009c224', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a4edc285df4003747ec99b7d0d9e6a42', 'aaa5103974ec9059469823d84d8ce109', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a4fcf5278decc41890827790aa95380e', 'a258e0bfcfae24e99f75be596efc173e', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a4fcf5278decc41890827790aa95380e', 'aeacaa70d21347699f5c311163d27085', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a4fcf5278decc41890827790aa95380e', 'a82fb95a6550c3cd5dadedd7f597c598', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a4fcf5278decc41890827790aa95380e', 'a8a1e1db38cdfdbaa692e14db009c224', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a4fcf5278decc41890827790aa95380e', 'aaa5103974ec9059469823d84d8ce109', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a4fcf5278decc41890827790aa95380e', 'aa87e149e75de05f31f28fb9309e91df', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a5decb84258850cbb3ca9b5789ddb755', 'a258e0bfcfae24e99f75be596efc173e', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a5decb84258850cbb3ca9b5789ddb755', 'a82fb95a6550c3cd5dadedd7f597c598', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a5decb84258850cbb3ca9b5789ddb755', 'a8a1e1db38cdfdbaa692e14db009c224', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a5decb84258850cbb3ca9b5789ddb755', 'aaa5103974ec9059469823d84d8ce109', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a5decb84258850cbb3ca9b5789ddb755', 'aa87e149e75de05f31f28fb9309e91df', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a5decb84258850cbb3ca9b5789ddb755', 'aeacaa70d21347699f5c311163d27085', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a5ea3ab70d3dbfe3a9203182eec97d99', 'a258e0bfcfae24e99f75be596efc173e', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a5ea3ab70d3dbfe3a9203182eec97d99', 'aeacaa70d21347699f5c311163d27085', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a5ea3ab70d3dbfe3a9203182eec97d99', 'aa87e149e75de05f31f28fb9309e91df', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a5ea3ab70d3dbfe3a9203182eec97d99', 'aaa5103974ec9059469823d84d8ce109', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a5ea3ab70d3dbfe3a9203182eec97d99', 'a8a1e1db38cdfdbaa692e14db009c224', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a5ea3ab70d3dbfe3a9203182eec97d99', 'a82fb95a6550c3cd5dadedd7f597c598', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a628a084f571bac3f6f51e333e3a8884', 'aeacaa70d21347699f5c311163d27085', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a628a084f571bac3f6f51e333e3a8884', 'a82fb95a6550c3cd5dadedd7f597c598', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a628a084f571bac3f6f51e333e3a8884', 'a8a1e1db38cdfdbaa692e14db009c224', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a628a084f571bac3f6f51e333e3a8884', 'aaa5103974ec9059469823d84d8ce109', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a628a084f571bac3f6f51e333e3a8884', 'aa87e149e75de05f31f28fb9309e91df', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a628a084f571bac3f6f51e333e3a8884', 'a258e0bfcfae24e99f75be596efc173e', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a6badd9de24643c8b48cc10cd15c65c5', 'aeacaa70d21347699f5c311163d27085', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a6badd9de24643c8b48cc10cd15c65c5', 'a258e0bfcfae24e99f75be596efc173e', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a6badd9de24643c8b48cc10cd15c65c5', 'a82fb95a6550c3cd5dadedd7f597c598', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a6badd9de24643c8b48cc10cd15c65c5', 'a8a1e1db38cdfdbaa692e14db009c224', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a6badd9de24643c8b48cc10cd15c65c5', 'aaa5103974ec9059469823d84d8ce109', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a6badd9de24643c8b48cc10cd15c65c5', 'aa87e149e75de05f31f28fb9309e91df', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a710e227eb81b4a36da4d555e97a5096', 'a258e0bfcfae24e99f75be596efc173e', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a710e227eb81b4a36da4d555e97a5096', 'aeacaa70d21347699f5c311163d27085', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a710e227eb81b4a36da4d555e97a5096', 'aa87e149e75de05f31f28fb9309e91df', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a710e227eb81b4a36da4d555e97a5096', 'aaa5103974ec9059469823d84d8ce109', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a710e227eb81b4a36da4d555e97a5096', 'a8a1e1db38cdfdbaa692e14db009c224', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a710e227eb81b4a36da4d555e97a5096', 'a82fb95a6550c3cd5dadedd7f597c598', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a755edd239869a2fc974151f1e7144fe', 'aeacaa70d21347699f5c311163d27085', 'editableRequired');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a755edd239869a2fc974151f1e7144fe', 'aa87e149e75de05f31f28fb9309e91df', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a755edd239869a2fc974151f1e7144fe', 'aaa5103974ec9059469823d84d8ce109', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a755edd239869a2fc974151f1e7144fe', 'a8a1e1db38cdfdbaa692e14db009c224', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a755edd239869a2fc974151f1e7144fe', 'a82fb95a6550c3cd5dadedd7f597c598', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a755edd239869a2fc974151f1e7144fe', 'a258e0bfcfae24e99f75be596efc173e', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a75e73ba2ee7b2298b704c4f56cb65e4', 'aeacaa70d21347699f5c311163d27085', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a75e73ba2ee7b2298b704c4f56cb65e4', 'aa87e149e75de05f31f28fb9309e91df', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a75e73ba2ee7b2298b704c4f56cb65e4', 'aaa5103974ec9059469823d84d8ce109', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a75e73ba2ee7b2298b704c4f56cb65e4', 'a8a1e1db38cdfdbaa692e14db009c224', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a75e73ba2ee7b2298b704c4f56cb65e4', 'a82fb95a6550c3cd5dadedd7f597c598', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a75e73ba2ee7b2298b704c4f56cb65e4', 'a258e0bfcfae24e99f75be596efc173e', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a7802771c99c12360b754662e3ff8955', 'aeacaa70d21347699f5c311163d27085', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a7802771c99c12360b754662e3ff8955', 'a258e0bfcfae24e99f75be596efc173e', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a7802771c99c12360b754662e3ff8955', 'a82fb95a6550c3cd5dadedd7f597c598', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a7802771c99c12360b754662e3ff8955', 'a8a1e1db38cdfdbaa692e14db009c224', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a7802771c99c12360b754662e3ff8955', 'aaa5103974ec9059469823d84d8ce109', 'editableRequired');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a7802771c99c12360b754662e3ff8955', 'aa87e149e75de05f31f28fb9309e91df', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a7cdb1c39349387fcf58359210e696ea', 'aa87e149e75de05f31f28fb9309e91df', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a7cdb1c39349387fcf58359210e696ea', 'aeacaa70d21347699f5c311163d27085', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a7cdb1c39349387fcf58359210e696ea', 'aaa5103974ec9059469823d84d8ce109', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a7cdb1c39349387fcf58359210e696ea', 'a8a1e1db38cdfdbaa692e14db009c224', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a7cdb1c39349387fcf58359210e696ea', 'a82fb95a6550c3cd5dadedd7f597c598', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a7cdb1c39349387fcf58359210e696ea', 'a258e0bfcfae24e99f75be596efc173e', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a826936604aa74a3744c0c726666b2e0', 'aa87e149e75de05f31f28fb9309e91df', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a826936604aa74a3744c0c726666b2e0', 'a258e0bfcfae24e99f75be596efc173e', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a826936604aa74a3744c0c726666b2e0', 'a82fb95a6550c3cd5dadedd7f597c598', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a826936604aa74a3744c0c726666b2e0', 'a8a1e1db38cdfdbaa692e14db009c224', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a826936604aa74a3744c0c726666b2e0', 'aaa5103974ec9059469823d84d8ce109', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a826936604aa74a3744c0c726666b2e0', 'aeacaa70d21347699f5c311163d27085', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a83ca580daa743c23e422b952ca035a8', 'a258e0bfcfae24e99f75be596efc173e', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a83ca580daa743c23e422b952ca035a8', 'aeacaa70d21347699f5c311163d27085', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a83ca580daa743c23e422b952ca035a8', 'aa87e149e75de05f31f28fb9309e91df', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a83ca580daa743c23e422b952ca035a8', 'aaa5103974ec9059469823d84d8ce109', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a83ca580daa743c23e422b952ca035a8', 'a8a1e1db38cdfdbaa692e14db009c224', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a83ca580daa743c23e422b952ca035a8', 'a82fb95a6550c3cd5dadedd7f597c598', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a840a2f8e71e9098f432b31148be5779', 'a258e0bfcfae24e99f75be596efc173e', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a840a2f8e71e9098f432b31148be5779', 'aeacaa70d21347699f5c311163d27085', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a840a2f8e71e9098f432b31148be5779', 'aa87e149e75de05f31f28fb9309e91df', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a840a2f8e71e9098f432b31148be5779', 'aaa5103974ec9059469823d84d8ce109', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a840a2f8e71e9098f432b31148be5779', 'a8a1e1db38cdfdbaa692e14db009c224', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a840a2f8e71e9098f432b31148be5779', 'a82fb95a6550c3cd5dadedd7f597c598', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a87520f43ca016202b01d899a8866f53', 'a258e0bfcfae24e99f75be596efc173e', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a87520f43ca016202b01d899a8866f53', 'aeacaa70d21347699f5c311163d27085', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a87520f43ca016202b01d899a8866f53', 'aa87e149e75de05f31f28fb9309e91df', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a87520f43ca016202b01d899a8866f53', 'aaa5103974ec9059469823d84d8ce109', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a87520f43ca016202b01d899a8866f53', 'a8a1e1db38cdfdbaa692e14db009c224', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a87520f43ca016202b01d899a8866f53', 'a82fb95a6550c3cd5dadedd7f597c598', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a8898c594ccf9b411f92b6f27d90dd02', 'a258e0bfcfae24e99f75be596efc173e', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a8898c594ccf9b411f92b6f27d90dd02', 'a82fb95a6550c3cd5dadedd7f597c598', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a8898c594ccf9b411f92b6f27d90dd02', 'a8a1e1db38cdfdbaa692e14db009c224', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a8898c594ccf9b411f92b6f27d90dd02', 'aaa5103974ec9059469823d84d8ce109', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a8898c594ccf9b411f92b6f27d90dd02', 'aa87e149e75de05f31f28fb9309e91df', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a8898c594ccf9b411f92b6f27d90dd02', 'aeacaa70d21347699f5c311163d27085', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a8a24ba85095c550505962b36d98c3be', 'a82fb95a6550c3cd5dadedd7f597c598', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a8a24ba85095c550505962b36d98c3be', 'aeacaa70d21347699f5c311163d27085', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a8a24ba85095c550505962b36d98c3be', 'aa87e149e75de05f31f28fb9309e91df', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a8a24ba85095c550505962b36d98c3be', 'aaa5103974ec9059469823d84d8ce109', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a8a24ba85095c550505962b36d98c3be', 'a8a1e1db38cdfdbaa692e14db009c224', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a8a24ba85095c550505962b36d98c3be', 'a258e0bfcfae24e99f75be596efc173e', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a8d276ead25d55cb2786bf28a72faab0', 'aa87e149e75de05f31f28fb9309e91df', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a8d276ead25d55cb2786bf28a72faab0', 'a258e0bfcfae24e99f75be596efc173e', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a8d276ead25d55cb2786bf28a72faab0', 'a82fb95a6550c3cd5dadedd7f597c598', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a8d276ead25d55cb2786bf28a72faab0', 'a8a1e1db38cdfdbaa692e14db009c224', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a8d276ead25d55cb2786bf28a72faab0', 'aaa5103974ec9059469823d84d8ce109', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a8d276ead25d55cb2786bf28a72faab0', 'aeacaa70d21347699f5c311163d27085', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a910bacf282f3590922cd9670be1d215', 'a258e0bfcfae24e99f75be596efc173e', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a910bacf282f3590922cd9670be1d215', 'aeacaa70d21347699f5c311163d27085', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a910bacf282f3590922cd9670be1d215', 'aa87e149e75de05f31f28fb9309e91df', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a910bacf282f3590922cd9670be1d215', 'aaa5103974ec9059469823d84d8ce109', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a910bacf282f3590922cd9670be1d215', 'a8a1e1db38cdfdbaa692e14db009c224', 'editableRequired');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a910bacf282f3590922cd9670be1d215', 'a82fb95a6550c3cd5dadedd7f597c598', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a918ef651427c247593817d91ae15c7a', 'aeacaa70d21347699f5c311163d27085', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a918ef651427c247593817d91ae15c7a', 'a258e0bfcfae24e99f75be596efc173e', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a918ef651427c247593817d91ae15c7a', 'a82fb95a6550c3cd5dadedd7f597c598', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a918ef651427c247593817d91ae15c7a', 'a8a1e1db38cdfdbaa692e14db009c224', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a918ef651427c247593817d91ae15c7a', 'aaa5103974ec9059469823d84d8ce109', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a918ef651427c247593817d91ae15c7a', 'aa87e149e75de05f31f28fb9309e91df', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a929ede9bc5e1c9b55e188afa2029798', 'a258e0bfcfae24e99f75be596efc173e', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a929ede9bc5e1c9b55e188afa2029798', 'aeacaa70d21347699f5c311163d27085', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a929ede9bc5e1c9b55e188afa2029798', 'aa87e149e75de05f31f28fb9309e91df', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a929ede9bc5e1c9b55e188afa2029798', 'aaa5103974ec9059469823d84d8ce109', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a929ede9bc5e1c9b55e188afa2029798', 'a8a1e1db38cdfdbaa692e14db009c224', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a929ede9bc5e1c9b55e188afa2029798', 'a82fb95a6550c3cd5dadedd7f597c598', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a93f8a596c3ffa3ad6265bd36b923dc0', 'aeacaa70d21347699f5c311163d27085', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a93f8a596c3ffa3ad6265bd36b923dc0', 'a258e0bfcfae24e99f75be596efc173e', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a93f8a596c3ffa3ad6265bd36b923dc0', 'a82fb95a6550c3cd5dadedd7f597c598', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a93f8a596c3ffa3ad6265bd36b923dc0', 'a8a1e1db38cdfdbaa692e14db009c224', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a93f8a596c3ffa3ad6265bd36b923dc0', 'aaa5103974ec9059469823d84d8ce109', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a93f8a596c3ffa3ad6265bd36b923dc0', 'aa87e149e75de05f31f28fb9309e91df', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a96d3f6b33923e6c45471970d5d235ca', 'aeacaa70d21347699f5c311163d27085', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a96d3f6b33923e6c45471970d5d235ca', 'a258e0bfcfae24e99f75be596efc173e', 'editableRequired');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a96d3f6b33923e6c45471970d5d235ca', 'a82fb95a6550c3cd5dadedd7f597c598', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a96d3f6b33923e6c45471970d5d235ca', 'a8a1e1db38cdfdbaa692e14db009c224', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a96d3f6b33923e6c45471970d5d235ca', 'aaa5103974ec9059469823d84d8ce109', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a96d3f6b33923e6c45471970d5d235ca', 'aa87e149e75de05f31f28fb9309e91df', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a99fe87fb279b045646027a0a14b8930', 'aeacaa70d21347699f5c311163d27085', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a99fe87fb279b045646027a0a14b8930', 'a258e0bfcfae24e99f75be596efc173e', 'editableRequired');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a99fe87fb279b045646027a0a14b8930', 'a82fb95a6550c3cd5dadedd7f597c598', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a99fe87fb279b045646027a0a14b8930', 'a8a1e1db38cdfdbaa692e14db009c224', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a99fe87fb279b045646027a0a14b8930', 'aaa5103974ec9059469823d84d8ce109', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'a99fe87fb279b045646027a0a14b8930', 'aa87e149e75de05f31f28fb9309e91df', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'aa273932115bd7d96b008e12330d8570', 'a258e0bfcfae24e99f75be596efc173e', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'aa273932115bd7d96b008e12330d8570', 'aeacaa70d21347699f5c311163d27085', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'aa273932115bd7d96b008e12330d8570', 'a82fb95a6550c3cd5dadedd7f597c598', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'aa273932115bd7d96b008e12330d8570', 'a8a1e1db38cdfdbaa692e14db009c224', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'aa273932115bd7d96b008e12330d8570', 'aaa5103974ec9059469823d84d8ce109', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'aa273932115bd7d96b008e12330d8570', 'aa87e149e75de05f31f28fb9309e91df', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'ab336835f267f16eb2e9a765c9f3d562', 'a82fb95a6550c3cd5dadedd7f597c598', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'ab336835f267f16eb2e9a765c9f3d562', 'aeacaa70d21347699f5c311163d27085', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'ab336835f267f16eb2e9a765c9f3d562', 'aa87e149e75de05f31f28fb9309e91df', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'ab336835f267f16eb2e9a765c9f3d562', 'aaa5103974ec9059469823d84d8ce109', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'ab336835f267f16eb2e9a765c9f3d562', 'a8a1e1db38cdfdbaa692e14db009c224', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'ab336835f267f16eb2e9a765c9f3d562', 'a258e0bfcfae24e99f75be596efc173e', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'abcf0ce4f3b0265e4d233ec373b66cb8', 'aa87e149e75de05f31f28fb9309e91df', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'abcf0ce4f3b0265e4d233ec373b66cb8', 'aeacaa70d21347699f5c311163d27085', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'abcf0ce4f3b0265e4d233ec373b66cb8', 'aaa5103974ec9059469823d84d8ce109', 'editableRequired');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'abcf0ce4f3b0265e4d233ec373b66cb8', 'a8a1e1db38cdfdbaa692e14db009c224', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'abcf0ce4f3b0265e4d233ec373b66cb8', 'a82fb95a6550c3cd5dadedd7f597c598', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'abcf0ce4f3b0265e4d233ec373b66cb8', 'a258e0bfcfae24e99f75be596efc173e', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'abdd7d23019680a27eefafa153cd2f5c', 'aeacaa70d21347699f5c311163d27085', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'abdd7d23019680a27eefafa153cd2f5c', 'a258e0bfcfae24e99f75be596efc173e', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'abdd7d23019680a27eefafa153cd2f5c', 'a82fb95a6550c3cd5dadedd7f597c598', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'abdd7d23019680a27eefafa153cd2f5c', 'a8a1e1db38cdfdbaa692e14db009c224', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'abdd7d23019680a27eefafa153cd2f5c', 'aaa5103974ec9059469823d84d8ce109', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'abdd7d23019680a27eefafa153cd2f5c', 'aa87e149e75de05f31f28fb9309e91df', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'abf089973153fbd0a7a0fbec451bd000', 'a258e0bfcfae24e99f75be596efc173e', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'abf089973153fbd0a7a0fbec451bd000', 'a82fb95a6550c3cd5dadedd7f597c598', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'abf089973153fbd0a7a0fbec451bd000', 'a8a1e1db38cdfdbaa692e14db009c224', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'abf089973153fbd0a7a0fbec451bd000', 'aaa5103974ec9059469823d84d8ce109', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'abf089973153fbd0a7a0fbec451bd000', 'aa87e149e75de05f31f28fb9309e91df', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'abf089973153fbd0a7a0fbec451bd000', 'aeacaa70d21347699f5c311163d27085', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'ac1c201dc1018243e0aa3bcc7b6fc60a', 'a258e0bfcfae24e99f75be596efc173e', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'ac1c201dc1018243e0aa3bcc7b6fc60a', 'aa87e149e75de05f31f28fb9309e91df', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'ac1c201dc1018243e0aa3bcc7b6fc60a', 'aaa5103974ec9059469823d84d8ce109', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'ac1c201dc1018243e0aa3bcc7b6fc60a', 'a8a1e1db38cdfdbaa692e14db009c224', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'ac1c201dc1018243e0aa3bcc7b6fc60a', 'a82fb95a6550c3cd5dadedd7f597c598', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'ac1c201dc1018243e0aa3bcc7b6fc60a', 'aeacaa70d21347699f5c311163d27085', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'acc1f172a41984869823f4495e441ab0', 'aeacaa70d21347699f5c311163d27085', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'acc1f172a41984869823f4495e441ab0', 'aa87e149e75de05f31f28fb9309e91df', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'acc1f172a41984869823f4495e441ab0', 'aaa5103974ec9059469823d84d8ce109', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'acc1f172a41984869823f4495e441ab0', 'a8a1e1db38cdfdbaa692e14db009c224', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'acc1f172a41984869823f4495e441ab0', 'a82fb95a6550c3cd5dadedd7f597c598', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'acc1f172a41984869823f4495e441ab0', 'a258e0bfcfae24e99f75be596efc173e', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'acf974f8c63805a90a194b0dd7665129', 'aeacaa70d21347699f5c311163d27085', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'acf974f8c63805a90a194b0dd7665129', 'aa87e149e75de05f31f28fb9309e91df', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'acf974f8c63805a90a194b0dd7665129', 'aaa5103974ec9059469823d84d8ce109', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'acf974f8c63805a90a194b0dd7665129', 'a8a1e1db38cdfdbaa692e14db009c224', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'acf974f8c63805a90a194b0dd7665129', 'a82fb95a6550c3cd5dadedd7f597c598', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'acf974f8c63805a90a194b0dd7665129', 'a258e0bfcfae24e99f75be596efc173e', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'ad05da15bfa32cd50cdd3e778ae0f250', 'a82fb95a6550c3cd5dadedd7f597c598', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'ad05da15bfa32cd50cdd3e778ae0f250', 'a8a1e1db38cdfdbaa692e14db009c224', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'ad05da15bfa32cd50cdd3e778ae0f250', 'aaa5103974ec9059469823d84d8ce109', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'ad05da15bfa32cd50cdd3e778ae0f250', 'aa87e149e75de05f31f28fb9309e91df', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'ad05da15bfa32cd50cdd3e778ae0f250', 'aeacaa70d21347699f5c311163d27085', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'ad05da15bfa32cd50cdd3e778ae0f250', 'a258e0bfcfae24e99f75be596efc173e', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'ad40cc3d789c45285bbfe6c3c2e1bcd7', 'aeacaa70d21347699f5c311163d27085', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'ad40cc3d789c45285bbfe6c3c2e1bcd7', 'a8a1e1db38cdfdbaa692e14db009c224', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'ad40cc3d789c45285bbfe6c3c2e1bcd7', 'a82fb95a6550c3cd5dadedd7f597c598', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'ad40cc3d789c45285bbfe6c3c2e1bcd7', 'a258e0bfcfae24e99f75be596efc173e', 'editableRequired');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'ad40cc3d789c45285bbfe6c3c2e1bcd7', 'aaa5103974ec9059469823d84d8ce109', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'ad40cc3d789c45285bbfe6c3c2e1bcd7', 'aa87e149e75de05f31f28fb9309e91df', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'ad60787db1a601ea12f17b15ae6daf7b', 'aaa5103974ec9059469823d84d8ce109', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'ad60787db1a601ea12f17b15ae6daf7b', 'aeacaa70d21347699f5c311163d27085', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'ad60787db1a601ea12f17b15ae6daf7b', 'aa87e149e75de05f31f28fb9309e91df', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'ad60787db1a601ea12f17b15ae6daf7b', 'a8a1e1db38cdfdbaa692e14db009c224', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'ad60787db1a601ea12f17b15ae6daf7b', 'a82fb95a6550c3cd5dadedd7f597c598', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'ad60787db1a601ea12f17b15ae6daf7b', 'a258e0bfcfae24e99f75be596efc173e', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'ad66a326f401e3f3bdc469b363f91822', 'aeacaa70d21347699f5c311163d27085', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'ad66a326f401e3f3bdc469b363f91822', 'aa87e149e75de05f31f28fb9309e91df', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'ad66a326f401e3f3bdc469b363f91822', 'a258e0bfcfae24e99f75be596efc173e', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'ad66a326f401e3f3bdc469b363f91822', 'aaa5103974ec9059469823d84d8ce109', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'ad66a326f401e3f3bdc469b363f91822', 'a8a1e1db38cdfdbaa692e14db009c224', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'ad66a326f401e3f3bdc469b363f91822', 'a82fb95a6550c3cd5dadedd7f597c598', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'af082fb00f2e71c2a27dcc88874d3f57', 'aeacaa70d21347699f5c311163d27085', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'af082fb00f2e71c2a27dcc88874d3f57', 'a258e0bfcfae24e99f75be596efc173e', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'af082fb00f2e71c2a27dcc88874d3f57', 'a82fb95a6550c3cd5dadedd7f597c598', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'af082fb00f2e71c2a27dcc88874d3f57', 'a8a1e1db38cdfdbaa692e14db009c224', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'af082fb00f2e71c2a27dcc88874d3f57', 'aaa5103974ec9059469823d84d8ce109', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'af082fb00f2e71c2a27dcc88874d3f57', 'aa87e149e75de05f31f28fb9309e91df', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'af325a2f3a2f56413461687d7cba8f20', 'aeacaa70d21347699f5c311163d27085', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'af325a2f3a2f56413461687d7cba8f20', 'aa87e149e75de05f31f28fb9309e91df', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'af325a2f3a2f56413461687d7cba8f20', 'aaa5103974ec9059469823d84d8ce109', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'af325a2f3a2f56413461687d7cba8f20', 'a8a1e1db38cdfdbaa692e14db009c224', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'af325a2f3a2f56413461687d7cba8f20', 'a82fb95a6550c3cd5dadedd7f597c598', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'af325a2f3a2f56413461687d7cba8f20', 'a258e0bfcfae24e99f75be596efc173e', 'editableRequired');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'af3a26739df10f18ac585d2a4e6cf448', 'a258e0bfcfae24e99f75be596efc173e', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'af3a26739df10f18ac585d2a4e6cf448', 'aeacaa70d21347699f5c311163d27085', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'af3a26739df10f18ac585d2a4e6cf448', 'aa87e149e75de05f31f28fb9309e91df', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'af3a26739df10f18ac585d2a4e6cf448', 'aaa5103974ec9059469823d84d8ce109', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'af3a26739df10f18ac585d2a4e6cf448', 'a8a1e1db38cdfdbaa692e14db009c224', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'af3a26739df10f18ac585d2a4e6cf448', 'a82fb95a6550c3cd5dadedd7f597c598', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'af7313650122e67929324cf6c85a0ccd', 'a258e0bfcfae24e99f75be596efc173e', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'af7313650122e67929324cf6c85a0ccd', 'aa87e149e75de05f31f28fb9309e91df', 'editable');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'af7313650122e67929324cf6c85a0ccd', 'aaa5103974ec9059469823d84d8ce109', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'af7313650122e67929324cf6c85a0ccd', 'a8a1e1db38cdfdbaa692e14db009c224', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'af7313650122e67929324cf6c85a0ccd', 'a82fb95a6550c3cd5dadedd7f597c598', 'readonly');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'af7313650122e67929324cf6c85a0ccd', 'aeacaa70d21347699f5c311163d27085', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'af750337e7d5560447d6a47157d75b68', 'aeacaa70d21347699f5c311163d27085', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'af750337e7d5560447d6a47157d75b68', 'aa87e149e75de05f31f28fb9309e91df', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'af750337e7d5560447d6a47157d75b68', 'aaa5103974ec9059469823d84d8ce109', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'af750337e7d5560447d6a47157d75b68', 'a8a1e1db38cdfdbaa692e14db009c224', 'hidden');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'af750337e7d5560447d6a47157d75b68', 'a82fb95a6550c3cd5dadedd7f597c598', 'editableRequired');
insert into wf_document_display values ('4028b8817880d833017880f5cafc0004', 'af750337e7d5560447d6a47157d75b68', 'a258e0bfcfae24e99f75be596efc173e', 'readonly');

insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '68369e91d73748c295be7c942b2a228d', '24a2398bc10d4021b3e55f54cc500c50', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '68369e91d73748c295be7c942b2a228d', 'bc2dcad9f1c441a7ac1a425beffb7772', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '68369e91d73748c295be7c942b2a228d', '203038f6a6c54dec9824275a4c093b61', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '68369e91d73748c295be7c942b2a228d', 'd0d6c6e94f574915bd88257467b6434c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '68369e91d73748c295be7c942b2a228d', 'a7f7a90781ba158a2b53052ffd59bf15', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '68369e91d73748c295be7c942b2a228d', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '68369e91d73748c295be7c942b2a228d', 'c9d735a5aca04835ba7f09f63f768a58', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '68369e91d73748c295be7c942b2a228d', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '68369e91d73748c295be7c942b2a228d', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '0da02528c3574a5a90047944fc99b103', '24a2398bc10d4021b3e55f54cc500c50', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '0da02528c3574a5a90047944fc99b103', 'bc2dcad9f1c441a7ac1a425beffb7772', 'editable');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '0da02528c3574a5a90047944fc99b103', '203038f6a6c54dec9824275a4c093b61', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '0da02528c3574a5a90047944fc99b103', 'd0d6c6e94f574915bd88257467b6434c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '0da02528c3574a5a90047944fc99b103', 'a7f7a90781ba158a2b53052ffd59bf15', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '0da02528c3574a5a90047944fc99b103', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '0da02528c3574a5a90047944fc99b103', 'c9d735a5aca04835ba7f09f63f768a58', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '0da02528c3574a5a90047944fc99b103', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '0da02528c3574a5a90047944fc99b103', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '81a12988c7894037b6ffeae5fd024133', '24a2398bc10d4021b3e55f54cc500c50', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '81a12988c7894037b6ffeae5fd024133', 'bc2dcad9f1c441a7ac1a425beffb7772', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '81a12988c7894037b6ffeae5fd024133', '203038f6a6c54dec9824275a4c093b61', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '81a12988c7894037b6ffeae5fd024133', 'd0d6c6e94f574915bd88257467b6434c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '81a12988c7894037b6ffeae5fd024133', 'a7f7a90781ba158a2b53052ffd59bf15', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '81a12988c7894037b6ffeae5fd024133', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '81a12988c7894037b6ffeae5fd024133', 'c9d735a5aca04835ba7f09f63f768a58', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '81a12988c7894037b6ffeae5fd024133', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '81a12988c7894037b6ffeae5fd024133', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'b95569d56dba467a9ebf9ac0d8ca9e09', '24a2398bc10d4021b3e55f54cc500c50', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'b95569d56dba467a9ebf9ac0d8ca9e09', 'bc2dcad9f1c441a7ac1a425beffb7772', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'b95569d56dba467a9ebf9ac0d8ca9e09', '203038f6a6c54dec9824275a4c093b61', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'b95569d56dba467a9ebf9ac0d8ca9e09', 'd0d6c6e94f574915bd88257467b6434c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'b95569d56dba467a9ebf9ac0d8ca9e09', 'a7f7a90781ba158a2b53052ffd59bf15', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'b95569d56dba467a9ebf9ac0d8ca9e09', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'b95569d56dba467a9ebf9ac0d8ca9e09', 'c9d735a5aca04835ba7f09f63f768a58', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'b95569d56dba467a9ebf9ac0d8ca9e09', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'b95569d56dba467a9ebf9ac0d8ca9e09', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '46d4314b543c40f9845a87b3ee6db3d9', '24a2398bc10d4021b3e55f54cc500c50', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '46d4314b543c40f9845a87b3ee6db3d9', 'bc2dcad9f1c441a7ac1a425beffb7772', 'editable');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '46d4314b543c40f9845a87b3ee6db3d9', '203038f6a6c54dec9824275a4c093b61', 'editable');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '46d4314b543c40f9845a87b3ee6db3d9', 'd0d6c6e94f574915bd88257467b6434c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '46d4314b543c40f9845a87b3ee6db3d9', 'a7f7a90781ba158a2b53052ffd59bf15', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '46d4314b543c40f9845a87b3ee6db3d9', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '46d4314b543c40f9845a87b3ee6db3d9', 'c9d735a5aca04835ba7f09f63f768a58', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '46d4314b543c40f9845a87b3ee6db3d9', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '46d4314b543c40f9845a87b3ee6db3d9', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '4b499d11338341fe94f0fdb48f97533e', '24a2398bc10d4021b3e55f54cc500c50', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '4b499d11338341fe94f0fdb48f97533e', 'bc2dcad9f1c441a7ac1a425beffb7772', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '4b499d11338341fe94f0fdb48f97533e', '203038f6a6c54dec9824275a4c093b61', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '4b499d11338341fe94f0fdb48f97533e', 'd0d6c6e94f574915bd88257467b6434c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '4b499d11338341fe94f0fdb48f97533e', 'a7f7a90781ba158a2b53052ffd59bf15', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '4b499d11338341fe94f0fdb48f97533e', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '4b499d11338341fe94f0fdb48f97533e', 'c9d735a5aca04835ba7f09f63f768a58', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '4b499d11338341fe94f0fdb48f97533e', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '4b499d11338341fe94f0fdb48f97533e', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'd1cedc4c71824025b77a73aab7e6859b', '24a2398bc10d4021b3e55f54cc500c50', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'd1cedc4c71824025b77a73aab7e6859b', 'bc2dcad9f1c441a7ac1a425beffb7772', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'd1cedc4c71824025b77a73aab7e6859b', '203038f6a6c54dec9824275a4c093b61', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'd1cedc4c71824025b77a73aab7e6859b', 'd0d6c6e94f574915bd88257467b6434c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'd1cedc4c71824025b77a73aab7e6859b', 'a7f7a90781ba158a2b53052ffd59bf15', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'd1cedc4c71824025b77a73aab7e6859b', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'd1cedc4c71824025b77a73aab7e6859b', 'c9d735a5aca04835ba7f09f63f768a58', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'd1cedc4c71824025b77a73aab7e6859b', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'd1cedc4c71824025b77a73aab7e6859b', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'b7cb55ee46f047c7a11d2bfef92551de', '24a2398bc10d4021b3e55f54cc500c50', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'b7cb55ee46f047c7a11d2bfef92551de', 'bc2dcad9f1c441a7ac1a425beffb7772', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'b7cb55ee46f047c7a11d2bfef92551de', '203038f6a6c54dec9824275a4c093b61', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'b7cb55ee46f047c7a11d2bfef92551de', 'd0d6c6e94f574915bd88257467b6434c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'b7cb55ee46f047c7a11d2bfef92551de', 'a7f7a90781ba158a2b53052ffd59bf15', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'b7cb55ee46f047c7a11d2bfef92551de', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'b7cb55ee46f047c7a11d2bfef92551de', 'c9d735a5aca04835ba7f09f63f768a58', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'b7cb55ee46f047c7a11d2bfef92551de', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'b7cb55ee46f047c7a11d2bfef92551de', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '4c55f52ad993410dad36868802ca81b8', '24a2398bc10d4021b3e55f54cc500c50', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '4c55f52ad993410dad36868802ca81b8', 'bc2dcad9f1c441a7ac1a425beffb7772', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '4c55f52ad993410dad36868802ca81b8', '203038f6a6c54dec9824275a4c093b61', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '4c55f52ad993410dad36868802ca81b8', 'd0d6c6e94f574915bd88257467b6434c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '4c55f52ad993410dad36868802ca81b8', 'a7f7a90781ba158a2b53052ffd59bf15', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '4c55f52ad993410dad36868802ca81b8', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '4c55f52ad993410dad36868802ca81b8', 'c9d735a5aca04835ba7f09f63f768a58', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '4c55f52ad993410dad36868802ca81b8', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '4c55f52ad993410dad36868802ca81b8', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'f7f6fac98fed4c539b9535dabea7f03d', '24a2398bc10d4021b3e55f54cc500c50', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'f7f6fac98fed4c539b9535dabea7f03d', 'bc2dcad9f1c441a7ac1a425beffb7772', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'f7f6fac98fed4c539b9535dabea7f03d', '203038f6a6c54dec9824275a4c093b61', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'f7f6fac98fed4c539b9535dabea7f03d', 'd0d6c6e94f574915bd88257467b6434c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'f7f6fac98fed4c539b9535dabea7f03d', 'a7f7a90781ba158a2b53052ffd59bf15', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'f7f6fac98fed4c539b9535dabea7f03d', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'f7f6fac98fed4c539b9535dabea7f03d', 'c9d735a5aca04835ba7f09f63f768a58', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'f7f6fac98fed4c539b9535dabea7f03d', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'f7f6fac98fed4c539b9535dabea7f03d', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '78a67f7784984dc5b503424a4253b195', '24a2398bc10d4021b3e55f54cc500c50', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '78a67f7784984dc5b503424a4253b195', 'bc2dcad9f1c441a7ac1a425beffb7772', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '78a67f7784984dc5b503424a4253b195', '203038f6a6c54dec9824275a4c093b61', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '78a67f7784984dc5b503424a4253b195', 'd0d6c6e94f574915bd88257467b6434c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '78a67f7784984dc5b503424a4253b195', 'a7f7a90781ba158a2b53052ffd59bf15', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '78a67f7784984dc5b503424a4253b195', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '78a67f7784984dc5b503424a4253b195', 'c9d735a5aca04835ba7f09f63f768a58', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '78a67f7784984dc5b503424a4253b195', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '78a67f7784984dc5b503424a4253b195', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'd60b8b4468874dbab765fcca3bbbcb2f', '24a2398bc10d4021b3e55f54cc500c50', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'd60b8b4468874dbab765fcca3bbbcb2f', 'bc2dcad9f1c441a7ac1a425beffb7772', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'd60b8b4468874dbab765fcca3bbbcb2f', '203038f6a6c54dec9824275a4c093b61', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'd60b8b4468874dbab765fcca3bbbcb2f', 'd0d6c6e94f574915bd88257467b6434c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'd60b8b4468874dbab765fcca3bbbcb2f', 'a7f7a90781ba158a2b53052ffd59bf15', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'd60b8b4468874dbab765fcca3bbbcb2f', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'd60b8b4468874dbab765fcca3bbbcb2f', 'c9d735a5aca04835ba7f09f63f768a58', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'd60b8b4468874dbab765fcca3bbbcb2f', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'd60b8b4468874dbab765fcca3bbbcb2f', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '4e4059b95b3040079aa476af9bf7c7c6', '24a2398bc10d4021b3e55f54cc500c50', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '4e4059b95b3040079aa476af9bf7c7c6', 'bc2dcad9f1c441a7ac1a425beffb7772', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '4e4059b95b3040079aa476af9bf7c7c6', '203038f6a6c54dec9824275a4c093b61', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '4e4059b95b3040079aa476af9bf7c7c6', 'd0d6c6e94f574915bd88257467b6434c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '4e4059b95b3040079aa476af9bf7c7c6', 'a7f7a90781ba158a2b53052ffd59bf15', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '4e4059b95b3040079aa476af9bf7c7c6', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '4e4059b95b3040079aa476af9bf7c7c6', 'c9d735a5aca04835ba7f09f63f768a58', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '4e4059b95b3040079aa476af9bf7c7c6', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '4e4059b95b3040079aa476af9bf7c7c6', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'e8c4685f990c4a39ac51e2d776d8158e', '24a2398bc10d4021b3e55f54cc500c50', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'e8c4685f990c4a39ac51e2d776d8158e', 'bc2dcad9f1c441a7ac1a425beffb7772', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'e8c4685f990c4a39ac51e2d776d8158e', '203038f6a6c54dec9824275a4c093b61', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'e8c4685f990c4a39ac51e2d776d8158e', 'd0d6c6e94f574915bd88257467b6434c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'e8c4685f990c4a39ac51e2d776d8158e', 'a7f7a90781ba158a2b53052ffd59bf15', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'e8c4685f990c4a39ac51e2d776d8158e', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'e8c4685f990c4a39ac51e2d776d8158e', 'c9d735a5aca04835ba7f09f63f768a58', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'e8c4685f990c4a39ac51e2d776d8158e', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'e8c4685f990c4a39ac51e2d776d8158e', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '70b7b53f0038466aa66373e7e62c464b', '24a2398bc10d4021b3e55f54cc500c50', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '70b7b53f0038466aa66373e7e62c464b', 'bc2dcad9f1c441a7ac1a425beffb7772', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '70b7b53f0038466aa66373e7e62c464b', '203038f6a6c54dec9824275a4c093b61', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '70b7b53f0038466aa66373e7e62c464b', 'd0d6c6e94f574915bd88257467b6434c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '70b7b53f0038466aa66373e7e62c464b', 'a7f7a90781ba158a2b53052ffd59bf15', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '70b7b53f0038466aa66373e7e62c464b', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '70b7b53f0038466aa66373e7e62c464b', 'c9d735a5aca04835ba7f09f63f768a58', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '70b7b53f0038466aa66373e7e62c464b', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '70b7b53f0038466aa66373e7e62c464b', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '6afdcd6948bc40639c26a037f3844a06', '24a2398bc10d4021b3e55f54cc500c50', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '6afdcd6948bc40639c26a037f3844a06', 'bc2dcad9f1c441a7ac1a425beffb7772', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '6afdcd6948bc40639c26a037f3844a06', '203038f6a6c54dec9824275a4c093b61', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '6afdcd6948bc40639c26a037f3844a06', 'd0d6c6e94f574915bd88257467b6434c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '6afdcd6948bc40639c26a037f3844a06', 'a7f7a90781ba158a2b53052ffd59bf15', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '6afdcd6948bc40639c26a037f3844a06', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '6afdcd6948bc40639c26a037f3844a06', 'c9d735a5aca04835ba7f09f63f768a58', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '6afdcd6948bc40639c26a037f3844a06', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '6afdcd6948bc40639c26a037f3844a06', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '126d1bc226764910863a2db5198fa3a1', '24a2398bc10d4021b3e55f54cc500c50', 'editable');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '126d1bc226764910863a2db5198fa3a1', 'bc2dcad9f1c441a7ac1a425beffb7772', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '126d1bc226764910863a2db5198fa3a1', '203038f6a6c54dec9824275a4c093b61', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '126d1bc226764910863a2db5198fa3a1', 'd0d6c6e94f574915bd88257467b6434c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '126d1bc226764910863a2db5198fa3a1', 'a7f7a90781ba158a2b53052ffd59bf15', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '126d1bc226764910863a2db5198fa3a1', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '126d1bc226764910863a2db5198fa3a1', 'c9d735a5aca04835ba7f09f63f768a58', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '126d1bc226764910863a2db5198fa3a1', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '126d1bc226764910863a2db5198fa3a1', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'a65ec7e28d5548ab8948c9b45242ad92', '24a2398bc10d4021b3e55f54cc500c50', 'editable');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'a65ec7e28d5548ab8948c9b45242ad92', 'bc2dcad9f1c441a7ac1a425beffb7772', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'a65ec7e28d5548ab8948c9b45242ad92', '203038f6a6c54dec9824275a4c093b61', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'a65ec7e28d5548ab8948c9b45242ad92', 'd0d6c6e94f574915bd88257467b6434c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'a65ec7e28d5548ab8948c9b45242ad92', 'a7f7a90781ba158a2b53052ffd59bf15', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'a65ec7e28d5548ab8948c9b45242ad92', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'a65ec7e28d5548ab8948c9b45242ad92', 'c9d735a5aca04835ba7f09f63f768a58', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'a65ec7e28d5548ab8948c9b45242ad92', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'a65ec7e28d5548ab8948c9b45242ad92', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'e8a4310cfc1d4abdb50677ebd19fde93', '24a2398bc10d4021b3e55f54cc500c50', 'editableRequired');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'e8a4310cfc1d4abdb50677ebd19fde93', 'bc2dcad9f1c441a7ac1a425beffb7772', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'e8a4310cfc1d4abdb50677ebd19fde93', '203038f6a6c54dec9824275a4c093b61', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'e8a4310cfc1d4abdb50677ebd19fde93', 'd0d6c6e94f574915bd88257467b6434c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'e8a4310cfc1d4abdb50677ebd19fde93', 'a7f7a90781ba158a2b53052ffd59bf15', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'e8a4310cfc1d4abdb50677ebd19fde93', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'e8a4310cfc1d4abdb50677ebd19fde93', 'c9d735a5aca04835ba7f09f63f768a58', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'e8a4310cfc1d4abdb50677ebd19fde93', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'e8a4310cfc1d4abdb50677ebd19fde93', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '1fabe796629a4a1fbb566ee0fe13f8bd', '24a2398bc10d4021b3e55f54cc500c50', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '1fabe796629a4a1fbb566ee0fe13f8bd', 'bc2dcad9f1c441a7ac1a425beffb7772', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '1fabe796629a4a1fbb566ee0fe13f8bd', '203038f6a6c54dec9824275a4c093b61', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '1fabe796629a4a1fbb566ee0fe13f8bd', 'd0d6c6e94f574915bd88257467b6434c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '1fabe796629a4a1fbb566ee0fe13f8bd', 'a7f7a90781ba158a2b53052ffd59bf15', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '1fabe796629a4a1fbb566ee0fe13f8bd', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '1fabe796629a4a1fbb566ee0fe13f8bd', 'c9d735a5aca04835ba7f09f63f768a58', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '1fabe796629a4a1fbb566ee0fe13f8bd', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '1fabe796629a4a1fbb566ee0fe13f8bd', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '8135a66efebc4afa83790369d27fe803', '24a2398bc10d4021b3e55f54cc500c50', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '8135a66efebc4afa83790369d27fe803', 'bc2dcad9f1c441a7ac1a425beffb7772', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '8135a66efebc4afa83790369d27fe803', '203038f6a6c54dec9824275a4c093b61', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '8135a66efebc4afa83790369d27fe803', 'd0d6c6e94f574915bd88257467b6434c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '8135a66efebc4afa83790369d27fe803', 'a7f7a90781ba158a2b53052ffd59bf15', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '8135a66efebc4afa83790369d27fe803', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '8135a66efebc4afa83790369d27fe803', 'c9d735a5aca04835ba7f09f63f768a58', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '8135a66efebc4afa83790369d27fe803', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '8135a66efebc4afa83790369d27fe803', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '77bdc771389f43b8b10a0b5b8bb0466c', '24a2398bc10d4021b3e55f54cc500c50', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '77bdc771389f43b8b10a0b5b8bb0466c', 'bc2dcad9f1c441a7ac1a425beffb7772', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '77bdc771389f43b8b10a0b5b8bb0466c', '203038f6a6c54dec9824275a4c093b61', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '77bdc771389f43b8b10a0b5b8bb0466c', 'd0d6c6e94f574915bd88257467b6434c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '77bdc771389f43b8b10a0b5b8bb0466c', 'a7f7a90781ba158a2b53052ffd59bf15', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '77bdc771389f43b8b10a0b5b8bb0466c', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '77bdc771389f43b8b10a0b5b8bb0466c', 'c9d735a5aca04835ba7f09f63f768a58', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '77bdc771389f43b8b10a0b5b8bb0466c', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '77bdc771389f43b8b10a0b5b8bb0466c', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '4168f17234ca464a9e53c31b47cf2c26', '24a2398bc10d4021b3e55f54cc500c50', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '4168f17234ca464a9e53c31b47cf2c26', 'bc2dcad9f1c441a7ac1a425beffb7772', 'editableRequired');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '4168f17234ca464a9e53c31b47cf2c26', '203038f6a6c54dec9824275a4c093b61', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '4168f17234ca464a9e53c31b47cf2c26', 'd0d6c6e94f574915bd88257467b6434c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '4168f17234ca464a9e53c31b47cf2c26', 'a7f7a90781ba158a2b53052ffd59bf15', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '4168f17234ca464a9e53c31b47cf2c26', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '4168f17234ca464a9e53c31b47cf2c26', 'c9d735a5aca04835ba7f09f63f768a58', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '4168f17234ca464a9e53c31b47cf2c26', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '4168f17234ca464a9e53c31b47cf2c26', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'f9a79b39fac041f8979eb394e6d17b0d', '24a2398bc10d4021b3e55f54cc500c50', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'f9a79b39fac041f8979eb394e6d17b0d', 'bc2dcad9f1c441a7ac1a425beffb7772', 'editable');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'f9a79b39fac041f8979eb394e6d17b0d', '203038f6a6c54dec9824275a4c093b61', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'f9a79b39fac041f8979eb394e6d17b0d', 'd0d6c6e94f574915bd88257467b6434c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'f9a79b39fac041f8979eb394e6d17b0d', 'a7f7a90781ba158a2b53052ffd59bf15', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'f9a79b39fac041f8979eb394e6d17b0d', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'f9a79b39fac041f8979eb394e6d17b0d', 'c9d735a5aca04835ba7f09f63f768a58', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'f9a79b39fac041f8979eb394e6d17b0d', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'f9a79b39fac041f8979eb394e6d17b0d', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '70082734af8a49efb7f23c3be21af0d4', '24a2398bc10d4021b3e55f54cc500c50', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '70082734af8a49efb7f23c3be21af0d4', 'bc2dcad9f1c441a7ac1a425beffb7772', 'editable');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '70082734af8a49efb7f23c3be21af0d4', '203038f6a6c54dec9824275a4c093b61', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '70082734af8a49efb7f23c3be21af0d4', 'd0d6c6e94f574915bd88257467b6434c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '70082734af8a49efb7f23c3be21af0d4', 'a7f7a90781ba158a2b53052ffd59bf15', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '70082734af8a49efb7f23c3be21af0d4', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '70082734af8a49efb7f23c3be21af0d4', 'c9d735a5aca04835ba7f09f63f768a58', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '70082734af8a49efb7f23c3be21af0d4', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '70082734af8a49efb7f23c3be21af0d4', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'b845c1bbc48443bfab1f556d9b5d2f96', '24a2398bc10d4021b3e55f54cc500c50', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'b845c1bbc48443bfab1f556d9b5d2f96', 'bc2dcad9f1c441a7ac1a425beffb7772', 'editableRequired');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'b845c1bbc48443bfab1f556d9b5d2f96', '203038f6a6c54dec9824275a4c093b61', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'b845c1bbc48443bfab1f556d9b5d2f96', 'd0d6c6e94f574915bd88257467b6434c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'b845c1bbc48443bfab1f556d9b5d2f96', 'a7f7a90781ba158a2b53052ffd59bf15', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'b845c1bbc48443bfab1f556d9b5d2f96', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'b845c1bbc48443bfab1f556d9b5d2f96', 'c9d735a5aca04835ba7f09f63f768a58', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'b845c1bbc48443bfab1f556d9b5d2f96', '23bd54bbcaf642a487726b6f0dac4b4c', 'editable');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'b845c1bbc48443bfab1f556d9b5d2f96', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'fb9736fc3e364a2bbde711d7baa4c86a', '24a2398bc10d4021b3e55f54cc500c50', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'fb9736fc3e364a2bbde711d7baa4c86a', 'bc2dcad9f1c441a7ac1a425beffb7772', 'editableRequired');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'fb9736fc3e364a2bbde711d7baa4c86a', '203038f6a6c54dec9824275a4c093b61', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'fb9736fc3e364a2bbde711d7baa4c86a', 'd0d6c6e94f574915bd88257467b6434c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'fb9736fc3e364a2bbde711d7baa4c86a', 'a7f7a90781ba158a2b53052ffd59bf15', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'fb9736fc3e364a2bbde711d7baa4c86a', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'fb9736fc3e364a2bbde711d7baa4c86a', 'c9d735a5aca04835ba7f09f63f768a58', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'fb9736fc3e364a2bbde711d7baa4c86a', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'fb9736fc3e364a2bbde711d7baa4c86a', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '88adaa894c034517a6b8e5faca94aee1', '24a2398bc10d4021b3e55f54cc500c50', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '88adaa894c034517a6b8e5faca94aee1', 'bc2dcad9f1c441a7ac1a425beffb7772', 'editable');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '88adaa894c034517a6b8e5faca94aee1', '203038f6a6c54dec9824275a4c093b61', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '88adaa894c034517a6b8e5faca94aee1', 'd0d6c6e94f574915bd88257467b6434c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '88adaa894c034517a6b8e5faca94aee1', 'a7f7a90781ba158a2b53052ffd59bf15', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '88adaa894c034517a6b8e5faca94aee1', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '88adaa894c034517a6b8e5faca94aee1', 'c9d735a5aca04835ba7f09f63f768a58', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '88adaa894c034517a6b8e5faca94aee1', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '88adaa894c034517a6b8e5faca94aee1', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'ad9bbf828a0640c1870f517830298fc9', '24a2398bc10d4021b3e55f54cc500c50', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'ad9bbf828a0640c1870f517830298fc9', 'bc2dcad9f1c441a7ac1a425beffb7772', 'editable');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'ad9bbf828a0640c1870f517830298fc9', '203038f6a6c54dec9824275a4c093b61', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'ad9bbf828a0640c1870f517830298fc9', 'd0d6c6e94f574915bd88257467b6434c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'ad9bbf828a0640c1870f517830298fc9', 'a7f7a90781ba158a2b53052ffd59bf15', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'ad9bbf828a0640c1870f517830298fc9', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'ad9bbf828a0640c1870f517830298fc9', 'c9d735a5aca04835ba7f09f63f768a58', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'ad9bbf828a0640c1870f517830298fc9', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'ad9bbf828a0640c1870f517830298fc9', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '2567d59617ff4c3abb9c8df6dc021de9', '24a2398bc10d4021b3e55f54cc500c50', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '2567d59617ff4c3abb9c8df6dc021de9', 'bc2dcad9f1c441a7ac1a425beffb7772', 'editable');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '2567d59617ff4c3abb9c8df6dc021de9', '203038f6a6c54dec9824275a4c093b61', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '2567d59617ff4c3abb9c8df6dc021de9', 'd0d6c6e94f574915bd88257467b6434c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '2567d59617ff4c3abb9c8df6dc021de9', 'a7f7a90781ba158a2b53052ffd59bf15', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '2567d59617ff4c3abb9c8df6dc021de9', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '2567d59617ff4c3abb9c8df6dc021de9', 'c9d735a5aca04835ba7f09f63f768a58', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '2567d59617ff4c3abb9c8df6dc021de9', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '2567d59617ff4c3abb9c8df6dc021de9', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'f5e9c0f86f0b43d887978787f78aaffe', '24a2398bc10d4021b3e55f54cc500c50', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'f5e9c0f86f0b43d887978787f78aaffe', 'bc2dcad9f1c441a7ac1a425beffb7772', 'editable');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'f5e9c0f86f0b43d887978787f78aaffe', '203038f6a6c54dec9824275a4c093b61', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'f5e9c0f86f0b43d887978787f78aaffe', 'd0d6c6e94f574915bd88257467b6434c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'f5e9c0f86f0b43d887978787f78aaffe', 'a7f7a90781ba158a2b53052ffd59bf15', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'f5e9c0f86f0b43d887978787f78aaffe', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'f5e9c0f86f0b43d887978787f78aaffe', 'c9d735a5aca04835ba7f09f63f768a58', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'f5e9c0f86f0b43d887978787f78aaffe', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'f5e9c0f86f0b43d887978787f78aaffe', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '1d10da32376d48a4aeec5bdd5855064d', '24a2398bc10d4021b3e55f54cc500c50', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '1d10da32376d48a4aeec5bdd5855064d', 'bc2dcad9f1c441a7ac1a425beffb7772', 'editable');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '1d10da32376d48a4aeec5bdd5855064d', '203038f6a6c54dec9824275a4c093b61', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '1d10da32376d48a4aeec5bdd5855064d', 'd0d6c6e94f574915bd88257467b6434c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '1d10da32376d48a4aeec5bdd5855064d', 'a7f7a90781ba158a2b53052ffd59bf15', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '1d10da32376d48a4aeec5bdd5855064d', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '1d10da32376d48a4aeec5bdd5855064d', 'c9d735a5aca04835ba7f09f63f768a58', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '1d10da32376d48a4aeec5bdd5855064d', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '1d10da32376d48a4aeec5bdd5855064d', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '566d27971d424f6ebcac7e906af5f522', '24a2398bc10d4021b3e55f54cc500c50', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '566d27971d424f6ebcac7e906af5f522', 'bc2dcad9f1c441a7ac1a425beffb7772', 'editable');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '566d27971d424f6ebcac7e906af5f522', '203038f6a6c54dec9824275a4c093b61', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '566d27971d424f6ebcac7e906af5f522', 'd0d6c6e94f574915bd88257467b6434c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '566d27971d424f6ebcac7e906af5f522', 'a7f7a90781ba158a2b53052ffd59bf15', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '566d27971d424f6ebcac7e906af5f522', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '566d27971d424f6ebcac7e906af5f522', 'c9d735a5aca04835ba7f09f63f768a58', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '566d27971d424f6ebcac7e906af5f522', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '566d27971d424f6ebcac7e906af5f522', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '3b863fc7f81149c5a7fdbc3c3fb17864', '24a2398bc10d4021b3e55f54cc500c50', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '3b863fc7f81149c5a7fdbc3c3fb17864', 'bc2dcad9f1c441a7ac1a425beffb7772', 'editable');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '3b863fc7f81149c5a7fdbc3c3fb17864', '203038f6a6c54dec9824275a4c093b61', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '3b863fc7f81149c5a7fdbc3c3fb17864', 'd0d6c6e94f574915bd88257467b6434c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '3b863fc7f81149c5a7fdbc3c3fb17864', 'a7f7a90781ba158a2b53052ffd59bf15', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '3b863fc7f81149c5a7fdbc3c3fb17864', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '3b863fc7f81149c5a7fdbc3c3fb17864', 'c9d735a5aca04835ba7f09f63f768a58', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '3b863fc7f81149c5a7fdbc3c3fb17864', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '3b863fc7f81149c5a7fdbc3c3fb17864', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '6e14fc72a91c454585d95d7a5b1d6740', '24a2398bc10d4021b3e55f54cc500c50', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '6e14fc72a91c454585d95d7a5b1d6740', 'bc2dcad9f1c441a7ac1a425beffb7772', 'editable');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '6e14fc72a91c454585d95d7a5b1d6740', '203038f6a6c54dec9824275a4c093b61', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '6e14fc72a91c454585d95d7a5b1d6740', 'd0d6c6e94f574915bd88257467b6434c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '6e14fc72a91c454585d95d7a5b1d6740', 'a7f7a90781ba158a2b53052ffd59bf15', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '6e14fc72a91c454585d95d7a5b1d6740', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '6e14fc72a91c454585d95d7a5b1d6740', 'c9d735a5aca04835ba7f09f63f768a58', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '6e14fc72a91c454585d95d7a5b1d6740', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '6e14fc72a91c454585d95d7a5b1d6740', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '1e83c938aa794f7d9ac0b4be129fa8b8', '24a2398bc10d4021b3e55f54cc500c50', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '1e83c938aa794f7d9ac0b4be129fa8b8', 'bc2dcad9f1c441a7ac1a425beffb7772', 'editable');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '1e83c938aa794f7d9ac0b4be129fa8b8', '203038f6a6c54dec9824275a4c093b61', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '1e83c938aa794f7d9ac0b4be129fa8b8', 'd0d6c6e94f574915bd88257467b6434c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '1e83c938aa794f7d9ac0b4be129fa8b8', 'a7f7a90781ba158a2b53052ffd59bf15', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '1e83c938aa794f7d9ac0b4be129fa8b8', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '1e83c938aa794f7d9ac0b4be129fa8b8', 'c9d735a5aca04835ba7f09f63f768a58', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '1e83c938aa794f7d9ac0b4be129fa8b8', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '1e83c938aa794f7d9ac0b4be129fa8b8', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '7e8c9bc421ab4822bf51f435ee27f07c', '24a2398bc10d4021b3e55f54cc500c50', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '7e8c9bc421ab4822bf51f435ee27f07c', 'bc2dcad9f1c441a7ac1a425beffb7772', 'editable');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '7e8c9bc421ab4822bf51f435ee27f07c', '203038f6a6c54dec9824275a4c093b61', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '7e8c9bc421ab4822bf51f435ee27f07c', 'd0d6c6e94f574915bd88257467b6434c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '7e8c9bc421ab4822bf51f435ee27f07c', 'a7f7a90781ba158a2b53052ffd59bf15', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '7e8c9bc421ab4822bf51f435ee27f07c', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '7e8c9bc421ab4822bf51f435ee27f07c', 'c9d735a5aca04835ba7f09f63f768a58', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '7e8c9bc421ab4822bf51f435ee27f07c', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '7e8c9bc421ab4822bf51f435ee27f07c', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'a70d29a292f24b1d91db3f0af886808c', '24a2398bc10d4021b3e55f54cc500c50', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'a70d29a292f24b1d91db3f0af886808c', 'bc2dcad9f1c441a7ac1a425beffb7772', 'editable');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'a70d29a292f24b1d91db3f0af886808c', '203038f6a6c54dec9824275a4c093b61', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'a70d29a292f24b1d91db3f0af886808c', 'd0d6c6e94f574915bd88257467b6434c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'a70d29a292f24b1d91db3f0af886808c', 'a7f7a90781ba158a2b53052ffd59bf15', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'a70d29a292f24b1d91db3f0af886808c', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'a70d29a292f24b1d91db3f0af886808c', 'c9d735a5aca04835ba7f09f63f768a58', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'a70d29a292f24b1d91db3f0af886808c', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'a70d29a292f24b1d91db3f0af886808c', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '16efb22300754fa8912c4ae96f7d880d', '24a2398bc10d4021b3e55f54cc500c50', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '16efb22300754fa8912c4ae96f7d880d', 'bc2dcad9f1c441a7ac1a425beffb7772', 'editable');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '16efb22300754fa8912c4ae96f7d880d', '203038f6a6c54dec9824275a4c093b61', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '16efb22300754fa8912c4ae96f7d880d', 'd0d6c6e94f574915bd88257467b6434c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '16efb22300754fa8912c4ae96f7d880d', 'a7f7a90781ba158a2b53052ffd59bf15', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '16efb22300754fa8912c4ae96f7d880d', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '16efb22300754fa8912c4ae96f7d880d', 'c9d735a5aca04835ba7f09f63f768a58', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '16efb22300754fa8912c4ae96f7d880d', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '16efb22300754fa8912c4ae96f7d880d', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'a63b6c2b2db2430984cabe3f9664f65e', '24a2398bc10d4021b3e55f54cc500c50', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'a63b6c2b2db2430984cabe3f9664f65e', 'bc2dcad9f1c441a7ac1a425beffb7772', 'editable');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'a63b6c2b2db2430984cabe3f9664f65e', '203038f6a6c54dec9824275a4c093b61', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'a63b6c2b2db2430984cabe3f9664f65e', 'd0d6c6e94f574915bd88257467b6434c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'a63b6c2b2db2430984cabe3f9664f65e', 'a7f7a90781ba158a2b53052ffd59bf15', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'a63b6c2b2db2430984cabe3f9664f65e', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'a63b6c2b2db2430984cabe3f9664f65e', 'c9d735a5aca04835ba7f09f63f768a58', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'a63b6c2b2db2430984cabe3f9664f65e', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'a63b6c2b2db2430984cabe3f9664f65e', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '6bea8026826443818b96302898b59eb0', '24a2398bc10d4021b3e55f54cc500c50', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '6bea8026826443818b96302898b59eb0', 'bc2dcad9f1c441a7ac1a425beffb7772', 'editable');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '6bea8026826443818b96302898b59eb0', '203038f6a6c54dec9824275a4c093b61', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '6bea8026826443818b96302898b59eb0', 'd0d6c6e94f574915bd88257467b6434c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '6bea8026826443818b96302898b59eb0', 'a7f7a90781ba158a2b53052ffd59bf15', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '6bea8026826443818b96302898b59eb0', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '6bea8026826443818b96302898b59eb0', 'c9d735a5aca04835ba7f09f63f768a58', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '6bea8026826443818b96302898b59eb0', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '6bea8026826443818b96302898b59eb0', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '72e1568e821049e3a559bfcfa8608c5f', '24a2398bc10d4021b3e55f54cc500c50', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '72e1568e821049e3a559bfcfa8608c5f', 'bc2dcad9f1c441a7ac1a425beffb7772', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '72e1568e821049e3a559bfcfa8608c5f', '203038f6a6c54dec9824275a4c093b61', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '72e1568e821049e3a559bfcfa8608c5f', 'd0d6c6e94f574915bd88257467b6434c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '72e1568e821049e3a559bfcfa8608c5f', 'a7f7a90781ba158a2b53052ffd59bf15', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '72e1568e821049e3a559bfcfa8608c5f', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '72e1568e821049e3a559bfcfa8608c5f', 'c9d735a5aca04835ba7f09f63f768a58', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '72e1568e821049e3a559bfcfa8608c5f', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '72e1568e821049e3a559bfcfa8608c5f', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'df358ee2bb18415da12d61b7623bc00f', '24a2398bc10d4021b3e55f54cc500c50', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'df358ee2bb18415da12d61b7623bc00f', 'bc2dcad9f1c441a7ac1a425beffb7772', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'df358ee2bb18415da12d61b7623bc00f', '203038f6a6c54dec9824275a4c093b61', 'editable');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'df358ee2bb18415da12d61b7623bc00f', 'd0d6c6e94f574915bd88257467b6434c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'df358ee2bb18415da12d61b7623bc00f', 'a7f7a90781ba158a2b53052ffd59bf15', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'df358ee2bb18415da12d61b7623bc00f', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'df358ee2bb18415da12d61b7623bc00f', 'c9d735a5aca04835ba7f09f63f768a58', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'df358ee2bb18415da12d61b7623bc00f', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'df358ee2bb18415da12d61b7623bc00f', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '2f47ce3e9f4541c6b597af1f1c894114', '24a2398bc10d4021b3e55f54cc500c50', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '2f47ce3e9f4541c6b597af1f1c894114', 'bc2dcad9f1c441a7ac1a425beffb7772', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '2f47ce3e9f4541c6b597af1f1c894114', '203038f6a6c54dec9824275a4c093b61', 'editableRequired');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '2f47ce3e9f4541c6b597af1f1c894114', 'd0d6c6e94f574915bd88257467b6434c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '2f47ce3e9f4541c6b597af1f1c894114', 'a7f7a90781ba158a2b53052ffd59bf15', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '2f47ce3e9f4541c6b597af1f1c894114', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '2f47ce3e9f4541c6b597af1f1c894114', 'c9d735a5aca04835ba7f09f63f768a58', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '2f47ce3e9f4541c6b597af1f1c894114', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '2f47ce3e9f4541c6b597af1f1c894114', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '8b2ba2fdc8804624836a93800b96f1c0', '24a2398bc10d4021b3e55f54cc500c50', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '8b2ba2fdc8804624836a93800b96f1c0', 'bc2dcad9f1c441a7ac1a425beffb7772', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '8b2ba2fdc8804624836a93800b96f1c0', '203038f6a6c54dec9824275a4c093b61', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '8b2ba2fdc8804624836a93800b96f1c0', 'd0d6c6e94f574915bd88257467b6434c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '8b2ba2fdc8804624836a93800b96f1c0', 'a7f7a90781ba158a2b53052ffd59bf15', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '8b2ba2fdc8804624836a93800b96f1c0', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '8b2ba2fdc8804624836a93800b96f1c0', 'c9d735a5aca04835ba7f09f63f768a58', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '8b2ba2fdc8804624836a93800b96f1c0', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '8b2ba2fdc8804624836a93800b96f1c0', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '5f853fd6493a440bb87bad6608c3cfa0', '24a2398bc10d4021b3e55f54cc500c50', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '5f853fd6493a440bb87bad6608c3cfa0', 'bc2dcad9f1c441a7ac1a425beffb7772', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '5f853fd6493a440bb87bad6608c3cfa0', '203038f6a6c54dec9824275a4c093b61', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '5f853fd6493a440bb87bad6608c3cfa0', 'd0d6c6e94f574915bd88257467b6434c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '5f853fd6493a440bb87bad6608c3cfa0', 'a7f7a90781ba158a2b53052ffd59bf15', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '5f853fd6493a440bb87bad6608c3cfa0', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '5f853fd6493a440bb87bad6608c3cfa0', 'c9d735a5aca04835ba7f09f63f768a58', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '5f853fd6493a440bb87bad6608c3cfa0', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '5f853fd6493a440bb87bad6608c3cfa0', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'bd131acab7844bbc86daddfccd0b9424', '24a2398bc10d4021b3e55f54cc500c50', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'bd131acab7844bbc86daddfccd0b9424', 'bc2dcad9f1c441a7ac1a425beffb7772', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'bd131acab7844bbc86daddfccd0b9424', '203038f6a6c54dec9824275a4c093b61', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'bd131acab7844bbc86daddfccd0b9424', 'd0d6c6e94f574915bd88257467b6434c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'bd131acab7844bbc86daddfccd0b9424', 'a7f7a90781ba158a2b53052ffd59bf15', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'bd131acab7844bbc86daddfccd0b9424', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'bd131acab7844bbc86daddfccd0b9424', 'c9d735a5aca04835ba7f09f63f768a58', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'bd131acab7844bbc86daddfccd0b9424', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'bd131acab7844bbc86daddfccd0b9424', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'cb31c114117944bbba596e01bd4a2baf', '24a2398bc10d4021b3e55f54cc500c50', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'cb31c114117944bbba596e01bd4a2baf', 'bc2dcad9f1c441a7ac1a425beffb7772', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'cb31c114117944bbba596e01bd4a2baf', '203038f6a6c54dec9824275a4c093b61', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'cb31c114117944bbba596e01bd4a2baf', 'd0d6c6e94f574915bd88257467b6434c', 'editableRequired');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'cb31c114117944bbba596e01bd4a2baf', 'a7f7a90781ba158a2b53052ffd59bf15', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'cb31c114117944bbba596e01bd4a2baf', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'cb31c114117944bbba596e01bd4a2baf', 'c9d735a5aca04835ba7f09f63f768a58', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'cb31c114117944bbba596e01bd4a2baf', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'cb31c114117944bbba596e01bd4a2baf', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'adbd729b6ec447769e11cbdaee0a90bf', '24a2398bc10d4021b3e55f54cc500c50', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'adbd729b6ec447769e11cbdaee0a90bf', 'bc2dcad9f1c441a7ac1a425beffb7772', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'adbd729b6ec447769e11cbdaee0a90bf', '203038f6a6c54dec9824275a4c093b61', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'adbd729b6ec447769e11cbdaee0a90bf', 'd0d6c6e94f574915bd88257467b6434c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'adbd729b6ec447769e11cbdaee0a90bf', 'a7f7a90781ba158a2b53052ffd59bf15', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'adbd729b6ec447769e11cbdaee0a90bf', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'adbd729b6ec447769e11cbdaee0a90bf', 'c9d735a5aca04835ba7f09f63f768a58', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'adbd729b6ec447769e11cbdaee0a90bf', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'adbd729b6ec447769e11cbdaee0a90bf', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '493a61cf4eec43ce93799ab060a8e31a', '24a2398bc10d4021b3e55f54cc500c50', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '493a61cf4eec43ce93799ab060a8e31a', 'bc2dcad9f1c441a7ac1a425beffb7772', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '493a61cf4eec43ce93799ab060a8e31a', '203038f6a6c54dec9824275a4c093b61', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '493a61cf4eec43ce93799ab060a8e31a', 'd0d6c6e94f574915bd88257467b6434c', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '493a61cf4eec43ce93799ab060a8e31a', 'a7f7a90781ba158a2b53052ffd59bf15', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '493a61cf4eec43ce93799ab060a8e31a', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '493a61cf4eec43ce93799ab060a8e31a', 'c9d735a5aca04835ba7f09f63f768a58', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '493a61cf4eec43ce93799ab060a8e31a', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '493a61cf4eec43ce93799ab060a8e31a', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'a434c0324f3d4f96ae77fc35d3386b23', '24a2398bc10d4021b3e55f54cc500c50', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'a434c0324f3d4f96ae77fc35d3386b23', 'bc2dcad9f1c441a7ac1a425beffb7772', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'a434c0324f3d4f96ae77fc35d3386b23', '203038f6a6c54dec9824275a4c093b61', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'a434c0324f3d4f96ae77fc35d3386b23', 'd0d6c6e94f574915bd88257467b6434c', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'a434c0324f3d4f96ae77fc35d3386b23', 'a7f7a90781ba158a2b53052ffd59bf15', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'a434c0324f3d4f96ae77fc35d3386b23', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'a434c0324f3d4f96ae77fc35d3386b23', 'c9d735a5aca04835ba7f09f63f768a58', 'editableRequired');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'a434c0324f3d4f96ae77fc35d3386b23', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'a434c0324f3d4f96ae77fc35d3386b23', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'be386a19c49b4aeb8c5aedc0313a9df5', '24a2398bc10d4021b3e55f54cc500c50', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'be386a19c49b4aeb8c5aedc0313a9df5', 'bc2dcad9f1c441a7ac1a425beffb7772', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'be386a19c49b4aeb8c5aedc0313a9df5', '203038f6a6c54dec9824275a4c093b61', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'be386a19c49b4aeb8c5aedc0313a9df5', 'd0d6c6e94f574915bd88257467b6434c', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'be386a19c49b4aeb8c5aedc0313a9df5', 'a7f7a90781ba158a2b53052ffd59bf15', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'be386a19c49b4aeb8c5aedc0313a9df5', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'be386a19c49b4aeb8c5aedc0313a9df5', 'c9d735a5aca04835ba7f09f63f768a58', 'editable');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'be386a19c49b4aeb8c5aedc0313a9df5', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'be386a19c49b4aeb8c5aedc0313a9df5', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'adeb5fc671fd411d899bcd5f815ada53', '24a2398bc10d4021b3e55f54cc500c50', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'adeb5fc671fd411d899bcd5f815ada53', 'bc2dcad9f1c441a7ac1a425beffb7772', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'adeb5fc671fd411d899bcd5f815ada53', '203038f6a6c54dec9824275a4c093b61', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'adeb5fc671fd411d899bcd5f815ada53', 'd0d6c6e94f574915bd88257467b6434c', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'adeb5fc671fd411d899bcd5f815ada53', 'a7f7a90781ba158a2b53052ffd59bf15', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'adeb5fc671fd411d899bcd5f815ada53', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'adeb5fc671fd411d899bcd5f815ada53', 'c9d735a5aca04835ba7f09f63f768a58', 'editable');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'adeb5fc671fd411d899bcd5f815ada53', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'adeb5fc671fd411d899bcd5f815ada53', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '2b93c546bf814c908a92a40316ebf0fb', '24a2398bc10d4021b3e55f54cc500c50', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '2b93c546bf814c908a92a40316ebf0fb', 'bc2dcad9f1c441a7ac1a425beffb7772', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '2b93c546bf814c908a92a40316ebf0fb', '203038f6a6c54dec9824275a4c093b61', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '2b93c546bf814c908a92a40316ebf0fb', 'd0d6c6e94f574915bd88257467b6434c', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '2b93c546bf814c908a92a40316ebf0fb', 'a7f7a90781ba158a2b53052ffd59bf15', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '2b93c546bf814c908a92a40316ebf0fb', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '2b93c546bf814c908a92a40316ebf0fb', 'c9d735a5aca04835ba7f09f63f768a58', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '2b93c546bf814c908a92a40316ebf0fb', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '2b93c546bf814c908a92a40316ebf0fb', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'adcf595a433043c1bb41f0d5fd649504', '24a2398bc10d4021b3e55f54cc500c50', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'adcf595a433043c1bb41f0d5fd649504', 'bc2dcad9f1c441a7ac1a425beffb7772', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'adcf595a433043c1bb41f0d5fd649504', '203038f6a6c54dec9824275a4c093b61', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'adcf595a433043c1bb41f0d5fd649504', 'd0d6c6e94f574915bd88257467b6434c', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'adcf595a433043c1bb41f0d5fd649504', 'a7f7a90781ba158a2b53052ffd59bf15', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'adcf595a433043c1bb41f0d5fd649504', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'adcf595a433043c1bb41f0d5fd649504', 'c9d735a5aca04835ba7f09f63f768a58', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'adcf595a433043c1bb41f0d5fd649504', '23bd54bbcaf642a487726b6f0dac4b4c', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'bf83695a89684561bb21bb0f8b22a619', '23bd54bbcaf642a487726b6f0dac4b4c', 'editable');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '2e5f623b4b244f21b7230364252acc05', '23bd54bbcaf642a487726b6f0dac4b4c', 'editableRequired');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'fc4c35ef2332453ba18cc39bef378b26', '23bd54bbcaf642a487726b6f0dac4b4c', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '2d805698376849bfb4d0e28b389477df', '23bd54bbcaf642a487726b6f0dac4b4c', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '215cada954bf44e1b0f03f02e44c63db', '23bd54bbcaf642a487726b6f0dac4b4c', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '6b7e7e14001b42d9957cfd1e3aeca64a', '23bd54bbcaf642a487726b6f0dac4b4c', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'adcf595a433043c1bb41f0d5fd649504', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'bf83695a89684561bb21bb0f8b22a619', '24a2398bc10d4021b3e55f54cc500c50', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'bf83695a89684561bb21bb0f8b22a619', 'bc2dcad9f1c441a7ac1a425beffb7772', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'bf83695a89684561bb21bb0f8b22a619', '203038f6a6c54dec9824275a4c093b61', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'bf83695a89684561bb21bb0f8b22a619', 'd0d6c6e94f574915bd88257467b6434c', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'bf83695a89684561bb21bb0f8b22a619', 'a7f7a90781ba158a2b53052ffd59bf15', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '215cada954bf44e1b0f03f02e44c63db', '24a2398bc10d4021b3e55f54cc500c50', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '2d805698376849bfb4d0e28b389477df', '24a2398bc10d4021b3e55f54cc500c50', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '2e5f623b4b244f21b7230364252acc05', '24a2398bc10d4021b3e55f54cc500c50', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '52fb168ce6db474b8507e77144e256b2', '24a2398bc10d4021b3e55f54cc500c50', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '6b7e7e14001b42d9957cfd1e3aeca64a', '24a2398bc10d4021b3e55f54cc500c50', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'fc4c35ef2332453ba18cc39bef378b26', '24a2398bc10d4021b3e55f54cc500c50', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '215cada954bf44e1b0f03f02e44c63db', 'bc2dcad9f1c441a7ac1a425beffb7772', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '2d805698376849bfb4d0e28b389477df', 'bc2dcad9f1c441a7ac1a425beffb7772', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '2e5f623b4b244f21b7230364252acc05', 'bc2dcad9f1c441a7ac1a425beffb7772', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '52fb168ce6db474b8507e77144e256b2', 'bc2dcad9f1c441a7ac1a425beffb7772', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '6b7e7e14001b42d9957cfd1e3aeca64a', 'bc2dcad9f1c441a7ac1a425beffb7772', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'fc4c35ef2332453ba18cc39bef378b26', 'bc2dcad9f1c441a7ac1a425beffb7772', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '215cada954bf44e1b0f03f02e44c63db', '203038f6a6c54dec9824275a4c093b61', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '2d805698376849bfb4d0e28b389477df', '203038f6a6c54dec9824275a4c093b61', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '2e5f623b4b244f21b7230364252acc05', '203038f6a6c54dec9824275a4c093b61', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '52fb168ce6db474b8507e77144e256b2', '203038f6a6c54dec9824275a4c093b61', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '6b7e7e14001b42d9957cfd1e3aeca64a', '203038f6a6c54dec9824275a4c093b61', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'fc4c35ef2332453ba18cc39bef378b26', '203038f6a6c54dec9824275a4c093b61', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '215cada954bf44e1b0f03f02e44c63db', 'd0d6c6e94f574915bd88257467b6434c', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '2d805698376849bfb4d0e28b389477df', 'd0d6c6e94f574915bd88257467b6434c', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '2e5f623b4b244f21b7230364252acc05', 'd0d6c6e94f574915bd88257467b6434c', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '52fb168ce6db474b8507e77144e256b2', 'd0d6c6e94f574915bd88257467b6434c', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '6b7e7e14001b42d9957cfd1e3aeca64a', 'd0d6c6e94f574915bd88257467b6434c', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'fc4c35ef2332453ba18cc39bef378b26', 'd0d6c6e94f574915bd88257467b6434c', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '215cada954bf44e1b0f03f02e44c63db', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '2d805698376849bfb4d0e28b389477df', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '2e5f623b4b244f21b7230364252acc05', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '52fb168ce6db474b8507e77144e256b2', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '6b7e7e14001b42d9957cfd1e3aeca64a', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'fc4c35ef2332453ba18cc39bef378b26', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'bf83695a89684561bb21bb0f8b22a619', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '215cada954bf44e1b0f03f02e44c63db', 'c9d735a5aca04835ba7f09f63f768a58', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '2d805698376849bfb4d0e28b389477df', 'c9d735a5aca04835ba7f09f63f768a58', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '2e5f623b4b244f21b7230364252acc05', 'c9d735a5aca04835ba7f09f63f768a58', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '52fb168ce6db474b8507e77144e256b2', 'c9d735a5aca04835ba7f09f63f768a58', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '6b7e7e14001b42d9957cfd1e3aeca64a', 'c9d735a5aca04835ba7f09f63f768a58', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'fc4c35ef2332453ba18cc39bef378b26', 'c9d735a5aca04835ba7f09f63f768a58', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'bf83695a89684561bb21bb0f8b22a619', 'c9d735a5aca04835ba7f09f63f768a58', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '215cada954bf44e1b0f03f02e44c63db', '906a57e38491441fa61401a8964f8398', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '2d805698376849bfb4d0e28b389477df', '906a57e38491441fa61401a8964f8398', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '2e5f623b4b244f21b7230364252acc05', '906a57e38491441fa61401a8964f8398', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '52fb168ce6db474b8507e77144e256b2', '906a57e38491441fa61401a8964f8398', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '6b7e7e14001b42d9957cfd1e3aeca64a', '906a57e38491441fa61401a8964f8398', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'fc4c35ef2332453ba18cc39bef378b26', '906a57e38491441fa61401a8964f8398', 'hidden');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', 'fc4c35ef2332453ba18cc39bef378b26', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '2d805698376849bfb4d0e28b389477df', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '215cada954bf44e1b0f03f02e44c63db', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'editableRequired');
insert into wf_document_display values ('4028b25d78870b0901788772ffe20025', '6b7e7e14001b42d9957cfd1e3aeca64a', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '863aab52c69b48f3ad40fa378ed1f136', 'a050b0dd20e9172ff66fcd28eae58721', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '863aab52c69b48f3ad40fa378ed1f136', 'a7496301877e9d5d75fdf131d9f53bb0', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '863aab52c69b48f3ad40fa378ed1f136', 'a70b423ca1fdf0f263672fb517fd21e5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '863aab52c69b48f3ad40fa378ed1f136', 'af104e036cbb3dc685df86b88beb2657', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '863aab52c69b48f3ad40fa378ed1f136', 'ad4b094d7973b990ce47a890ce7e9732', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '863aab52c69b48f3ad40fa378ed1f136', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '863aab52c69b48f3ad40fa378ed1f136', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '863aab52c69b48f3ad40fa378ed1f136', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '863aab52c69b48f3ad40fa378ed1f136', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '863aab52c69b48f3ad40fa378ed1f136', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '863aab52c69b48f3ad40fa378ed1f136', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '863aab52c69b48f3ad40fa378ed1f136', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '863aab52c69b48f3ad40fa378ed1f136', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '863aab52c69b48f3ad40fa378ed1f136', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a8af055054d14330bc32ebde17bca561', 'a050b0dd20e9172ff66fcd28eae58721', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a8af055054d14330bc32ebde17bca561', 'a7496301877e9d5d75fdf131d9f53bb0', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a8af055054d14330bc32ebde17bca561', 'a70b423ca1fdf0f263672fb517fd21e5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a8af055054d14330bc32ebde17bca561', 'af104e036cbb3dc685df86b88beb2657', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a8af055054d14330bc32ebde17bca561', 'ad4b094d7973b990ce47a890ce7e9732', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a8af055054d14330bc32ebde17bca561', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a8af055054d14330bc32ebde17bca561', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a8af055054d14330bc32ebde17bca561', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a8af055054d14330bc32ebde17bca561', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a8af055054d14330bc32ebde17bca561', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a8af055054d14330bc32ebde17bca561', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a8af055054d14330bc32ebde17bca561', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a8af055054d14330bc32ebde17bca561', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a8af055054d14330bc32ebde17bca561', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'f945571183c5475591ee2abe96db657a', 'a050b0dd20e9172ff66fcd28eae58721', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'f945571183c5475591ee2abe96db657a', 'a7496301877e9d5d75fdf131d9f53bb0', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'f945571183c5475591ee2abe96db657a', 'a70b423ca1fdf0f263672fb517fd21e5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'f945571183c5475591ee2abe96db657a', 'af104e036cbb3dc685df86b88beb2657', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'f945571183c5475591ee2abe96db657a', 'ad4b094d7973b990ce47a890ce7e9732', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'f945571183c5475591ee2abe96db657a', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'f945571183c5475591ee2abe96db657a', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'f945571183c5475591ee2abe96db657a', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'f945571183c5475591ee2abe96db657a', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'f945571183c5475591ee2abe96db657a', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'f945571183c5475591ee2abe96db657a', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'f945571183c5475591ee2abe96db657a', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'f945571183c5475591ee2abe96db657a', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'f945571183c5475591ee2abe96db657a', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '5619598930384c76bbfd12f246a7ed2d', 'a050b0dd20e9172ff66fcd28eae58721', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '5619598930384c76bbfd12f246a7ed2d', 'a7496301877e9d5d75fdf131d9f53bb0', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '5619598930384c76bbfd12f246a7ed2d', 'a70b423ca1fdf0f263672fb517fd21e5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '5619598930384c76bbfd12f246a7ed2d', 'af104e036cbb3dc685df86b88beb2657', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '5619598930384c76bbfd12f246a7ed2d', 'ad4b094d7973b990ce47a890ce7e9732', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '5619598930384c76bbfd12f246a7ed2d', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '5619598930384c76bbfd12f246a7ed2d', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '5619598930384c76bbfd12f246a7ed2d', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '5619598930384c76bbfd12f246a7ed2d', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '5619598930384c76bbfd12f246a7ed2d', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '5619598930384c76bbfd12f246a7ed2d', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '5619598930384c76bbfd12f246a7ed2d', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '5619598930384c76bbfd12f246a7ed2d', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '5619598930384c76bbfd12f246a7ed2d', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ab56589b5c154ee9829054081516f211', 'a050b0dd20e9172ff66fcd28eae58721', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ab56589b5c154ee9829054081516f211', 'a7496301877e9d5d75fdf131d9f53bb0', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ab56589b5c154ee9829054081516f211', 'a70b423ca1fdf0f263672fb517fd21e5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ab56589b5c154ee9829054081516f211', 'af104e036cbb3dc685df86b88beb2657', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ab56589b5c154ee9829054081516f211', 'ad4b094d7973b990ce47a890ce7e9732', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ab56589b5c154ee9829054081516f211', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ab56589b5c154ee9829054081516f211', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ab56589b5c154ee9829054081516f211', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ab56589b5c154ee9829054081516f211', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ab56589b5c154ee9829054081516f211', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ab56589b5c154ee9829054081516f211', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ab56589b5c154ee9829054081516f211', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ab56589b5c154ee9829054081516f211', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ab56589b5c154ee9829054081516f211', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c89f663fad094c749810e6e268645bae', 'a050b0dd20e9172ff66fcd28eae58721', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c89f663fad094c749810e6e268645bae', 'a7496301877e9d5d75fdf131d9f53bb0', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c89f663fad094c749810e6e268645bae', 'a70b423ca1fdf0f263672fb517fd21e5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c89f663fad094c749810e6e268645bae', 'af104e036cbb3dc685df86b88beb2657', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c89f663fad094c749810e6e268645bae', 'ad4b094d7973b990ce47a890ce7e9732', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c89f663fad094c749810e6e268645bae', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c89f663fad094c749810e6e268645bae', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c89f663fad094c749810e6e268645bae', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c89f663fad094c749810e6e268645bae', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c89f663fad094c749810e6e268645bae', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c89f663fad094c749810e6e268645bae', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c89f663fad094c749810e6e268645bae', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c89f663fad094c749810e6e268645bae', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c89f663fad094c749810e6e268645bae', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd4fa12ff8a7a462992865e419bffcd93', 'a050b0dd20e9172ff66fcd28eae58721', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd4fa12ff8a7a462992865e419bffcd93', 'a7496301877e9d5d75fdf131d9f53bb0', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd4fa12ff8a7a462992865e419bffcd93', 'a70b423ca1fdf0f263672fb517fd21e5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd4fa12ff8a7a462992865e419bffcd93', 'af104e036cbb3dc685df86b88beb2657', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd4fa12ff8a7a462992865e419bffcd93', 'ad4b094d7973b990ce47a890ce7e9732', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd4fa12ff8a7a462992865e419bffcd93', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd4fa12ff8a7a462992865e419bffcd93', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd4fa12ff8a7a462992865e419bffcd93', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd4fa12ff8a7a462992865e419bffcd93', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd4fa12ff8a7a462992865e419bffcd93', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd4fa12ff8a7a462992865e419bffcd93', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd4fa12ff8a7a462992865e419bffcd93', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd4fa12ff8a7a462992865e419bffcd93', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd4fa12ff8a7a462992865e419bffcd93', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd49d626d97ae47938928bdf2162bd54a', 'a050b0dd20e9172ff66fcd28eae58721', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd49d626d97ae47938928bdf2162bd54a', 'a7496301877e9d5d75fdf131d9f53bb0', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd49d626d97ae47938928bdf2162bd54a', 'a70b423ca1fdf0f263672fb517fd21e5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd49d626d97ae47938928bdf2162bd54a', 'af104e036cbb3dc685df86b88beb2657', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd49d626d97ae47938928bdf2162bd54a', 'ad4b094d7973b990ce47a890ce7e9732', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd49d626d97ae47938928bdf2162bd54a', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd49d626d97ae47938928bdf2162bd54a', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd49d626d97ae47938928bdf2162bd54a', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd49d626d97ae47938928bdf2162bd54a', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd49d626d97ae47938928bdf2162bd54a', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd49d626d97ae47938928bdf2162bd54a', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd49d626d97ae47938928bdf2162bd54a', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd49d626d97ae47938928bdf2162bd54a', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd49d626d97ae47938928bdf2162bd54a', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '8cbae4ebe4e34e0199dc0f7e521bb8b3', 'a050b0dd20e9172ff66fcd28eae58721', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '8cbae4ebe4e34e0199dc0f7e521bb8b3', 'a7496301877e9d5d75fdf131d9f53bb0', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '8cbae4ebe4e34e0199dc0f7e521bb8b3', 'a70b423ca1fdf0f263672fb517fd21e5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '8cbae4ebe4e34e0199dc0f7e521bb8b3', 'af104e036cbb3dc685df86b88beb2657', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '8cbae4ebe4e34e0199dc0f7e521bb8b3', 'ad4b094d7973b990ce47a890ce7e9732', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '8cbae4ebe4e34e0199dc0f7e521bb8b3', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '8cbae4ebe4e34e0199dc0f7e521bb8b3', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '8cbae4ebe4e34e0199dc0f7e521bb8b3', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '8cbae4ebe4e34e0199dc0f7e521bb8b3', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '8cbae4ebe4e34e0199dc0f7e521bb8b3', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '8cbae4ebe4e34e0199dc0f7e521bb8b3', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '8cbae4ebe4e34e0199dc0f7e521bb8b3', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '8cbae4ebe4e34e0199dc0f7e521bb8b3', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '8cbae4ebe4e34e0199dc0f7e521bb8b3', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '06aa9e655148405ea88fc65d1af2f07d', 'a050b0dd20e9172ff66fcd28eae58721', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '06aa9e655148405ea88fc65d1af2f07d', 'a7496301877e9d5d75fdf131d9f53bb0', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '06aa9e655148405ea88fc65d1af2f07d', 'a70b423ca1fdf0f263672fb517fd21e5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '06aa9e655148405ea88fc65d1af2f07d', 'af104e036cbb3dc685df86b88beb2657', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '06aa9e655148405ea88fc65d1af2f07d', 'ad4b094d7973b990ce47a890ce7e9732', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '06aa9e655148405ea88fc65d1af2f07d', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '06aa9e655148405ea88fc65d1af2f07d', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '06aa9e655148405ea88fc65d1af2f07d', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '06aa9e655148405ea88fc65d1af2f07d', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '06aa9e655148405ea88fc65d1af2f07d', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '06aa9e655148405ea88fc65d1af2f07d', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '06aa9e655148405ea88fc65d1af2f07d', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '06aa9e655148405ea88fc65d1af2f07d', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '06aa9e655148405ea88fc65d1af2f07d', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '8face0b52c974369bfc2d4f3daac42c3', 'a050b0dd20e9172ff66fcd28eae58721', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '8face0b52c974369bfc2d4f3daac42c3', 'a7496301877e9d5d75fdf131d9f53bb0', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '8face0b52c974369bfc2d4f3daac42c3', 'a70b423ca1fdf0f263672fb517fd21e5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '8face0b52c974369bfc2d4f3daac42c3', 'af104e036cbb3dc685df86b88beb2657', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '8face0b52c974369bfc2d4f3daac42c3', 'ad4b094d7973b990ce47a890ce7e9732', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '8face0b52c974369bfc2d4f3daac42c3', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '8face0b52c974369bfc2d4f3daac42c3', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '8face0b52c974369bfc2d4f3daac42c3', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '8face0b52c974369bfc2d4f3daac42c3', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '8face0b52c974369bfc2d4f3daac42c3', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '8face0b52c974369bfc2d4f3daac42c3', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '8face0b52c974369bfc2d4f3daac42c3', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '8face0b52c974369bfc2d4f3daac42c3', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '8face0b52c974369bfc2d4f3daac42c3', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '5aa90376561f44c7b23474f80a3b9bb3', 'a050b0dd20e9172ff66fcd28eae58721', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '5aa90376561f44c7b23474f80a3b9bb3', 'a7496301877e9d5d75fdf131d9f53bb0', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '5aa90376561f44c7b23474f80a3b9bb3', 'a70b423ca1fdf0f263672fb517fd21e5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '5aa90376561f44c7b23474f80a3b9bb3', 'af104e036cbb3dc685df86b88beb2657', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '5aa90376561f44c7b23474f80a3b9bb3', 'ad4b094d7973b990ce47a890ce7e9732', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '5aa90376561f44c7b23474f80a3b9bb3', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '5aa90376561f44c7b23474f80a3b9bb3', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '5aa90376561f44c7b23474f80a3b9bb3', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '5aa90376561f44c7b23474f80a3b9bb3', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '5aa90376561f44c7b23474f80a3b9bb3', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '5aa90376561f44c7b23474f80a3b9bb3', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '5aa90376561f44c7b23474f80a3b9bb3', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '5aa90376561f44c7b23474f80a3b9bb3', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '5aa90376561f44c7b23474f80a3b9bb3', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'fdb3b5aec66d4d0286aa39d5306905f1', 'a050b0dd20e9172ff66fcd28eae58721', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'fdb3b5aec66d4d0286aa39d5306905f1', 'a7496301877e9d5d75fdf131d9f53bb0', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'fdb3b5aec66d4d0286aa39d5306905f1', 'a70b423ca1fdf0f263672fb517fd21e5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'fdb3b5aec66d4d0286aa39d5306905f1', 'af104e036cbb3dc685df86b88beb2657', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'fdb3b5aec66d4d0286aa39d5306905f1', 'ad4b094d7973b990ce47a890ce7e9732', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'fdb3b5aec66d4d0286aa39d5306905f1', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'fdb3b5aec66d4d0286aa39d5306905f1', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'fdb3b5aec66d4d0286aa39d5306905f1', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'fdb3b5aec66d4d0286aa39d5306905f1', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'fdb3b5aec66d4d0286aa39d5306905f1', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'fdb3b5aec66d4d0286aa39d5306905f1', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'fdb3b5aec66d4d0286aa39d5306905f1', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'fdb3b5aec66d4d0286aa39d5306905f1', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'fdb3b5aec66d4d0286aa39d5306905f1', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '132843274c11498abf2d384d0b7cd778', 'a050b0dd20e9172ff66fcd28eae58721', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '132843274c11498abf2d384d0b7cd778', 'a7496301877e9d5d75fdf131d9f53bb0', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '132843274c11498abf2d384d0b7cd778', 'a70b423ca1fdf0f263672fb517fd21e5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '132843274c11498abf2d384d0b7cd778', 'af104e036cbb3dc685df86b88beb2657', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '132843274c11498abf2d384d0b7cd778', 'ad4b094d7973b990ce47a890ce7e9732', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '132843274c11498abf2d384d0b7cd778', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '132843274c11498abf2d384d0b7cd778', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '132843274c11498abf2d384d0b7cd778', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '132843274c11498abf2d384d0b7cd778', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '132843274c11498abf2d384d0b7cd778', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '132843274c11498abf2d384d0b7cd778', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '132843274c11498abf2d384d0b7cd778', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '132843274c11498abf2d384d0b7cd778', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '132843274c11498abf2d384d0b7cd778', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c72bc134e9b94e6aadb9c043a4e51e26', 'a050b0dd20e9172ff66fcd28eae58721', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c72bc134e9b94e6aadb9c043a4e51e26', 'a7496301877e9d5d75fdf131d9f53bb0', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c72bc134e9b94e6aadb9c043a4e51e26', 'a70b423ca1fdf0f263672fb517fd21e5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c72bc134e9b94e6aadb9c043a4e51e26', 'af104e036cbb3dc685df86b88beb2657', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c72bc134e9b94e6aadb9c043a4e51e26', 'ad4b094d7973b990ce47a890ce7e9732', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c72bc134e9b94e6aadb9c043a4e51e26', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c72bc134e9b94e6aadb9c043a4e51e26', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c72bc134e9b94e6aadb9c043a4e51e26', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c72bc134e9b94e6aadb9c043a4e51e26', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c72bc134e9b94e6aadb9c043a4e51e26', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c72bc134e9b94e6aadb9c043a4e51e26', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c72bc134e9b94e6aadb9c043a4e51e26', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c72bc134e9b94e6aadb9c043a4e51e26', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c72bc134e9b94e6aadb9c043a4e51e26', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '58f8ef1520d8432783c849060943bd7d', 'a050b0dd20e9172ff66fcd28eae58721', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '58f8ef1520d8432783c849060943bd7d', 'a7496301877e9d5d75fdf131d9f53bb0', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '58f8ef1520d8432783c849060943bd7d', 'a70b423ca1fdf0f263672fb517fd21e5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '58f8ef1520d8432783c849060943bd7d', 'af104e036cbb3dc685df86b88beb2657', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '58f8ef1520d8432783c849060943bd7d', 'ad4b094d7973b990ce47a890ce7e9732', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '58f8ef1520d8432783c849060943bd7d', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '58f8ef1520d8432783c849060943bd7d', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '58f8ef1520d8432783c849060943bd7d', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '58f8ef1520d8432783c849060943bd7d', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '58f8ef1520d8432783c849060943bd7d', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '58f8ef1520d8432783c849060943bd7d', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '58f8ef1520d8432783c849060943bd7d', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '58f8ef1520d8432783c849060943bd7d', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '58f8ef1520d8432783c849060943bd7d', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd3e9b29dd46c4e0b9fb5993534de4f2a', 'a050b0dd20e9172ff66fcd28eae58721', 'editable');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd3e9b29dd46c4e0b9fb5993534de4f2a', 'a7496301877e9d5d75fdf131d9f53bb0', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd3e9b29dd46c4e0b9fb5993534de4f2a', 'a70b423ca1fdf0f263672fb517fd21e5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd3e9b29dd46c4e0b9fb5993534de4f2a', 'af104e036cbb3dc685df86b88beb2657', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd3e9b29dd46c4e0b9fb5993534de4f2a', 'ad4b094d7973b990ce47a890ce7e9732', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd3e9b29dd46c4e0b9fb5993534de4f2a', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd3e9b29dd46c4e0b9fb5993534de4f2a', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd3e9b29dd46c4e0b9fb5993534de4f2a', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd3e9b29dd46c4e0b9fb5993534de4f2a', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd3e9b29dd46c4e0b9fb5993534de4f2a', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd3e9b29dd46c4e0b9fb5993534de4f2a', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd3e9b29dd46c4e0b9fb5993534de4f2a', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd3e9b29dd46c4e0b9fb5993534de4f2a', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd3e9b29dd46c4e0b9fb5993534de4f2a', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '3defaf1ea63c4ad5a46572da0497bff7', 'a050b0dd20e9172ff66fcd28eae58721', 'editable');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '3defaf1ea63c4ad5a46572da0497bff7', 'a7496301877e9d5d75fdf131d9f53bb0', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '3defaf1ea63c4ad5a46572da0497bff7', 'a70b423ca1fdf0f263672fb517fd21e5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '3defaf1ea63c4ad5a46572da0497bff7', 'af104e036cbb3dc685df86b88beb2657', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '3defaf1ea63c4ad5a46572da0497bff7', 'ad4b094d7973b990ce47a890ce7e9732', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '3defaf1ea63c4ad5a46572da0497bff7', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '3defaf1ea63c4ad5a46572da0497bff7', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '3defaf1ea63c4ad5a46572da0497bff7', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '3defaf1ea63c4ad5a46572da0497bff7', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '3defaf1ea63c4ad5a46572da0497bff7', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '3defaf1ea63c4ad5a46572da0497bff7', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '3defaf1ea63c4ad5a46572da0497bff7', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '3defaf1ea63c4ad5a46572da0497bff7', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '3defaf1ea63c4ad5a46572da0497bff7', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'dca5ef1fac3c42bab5407f3211857f6e', 'a050b0dd20e9172ff66fcd28eae58721', 'editableRequired');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'dca5ef1fac3c42bab5407f3211857f6e', 'a7496301877e9d5d75fdf131d9f53bb0', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'dca5ef1fac3c42bab5407f3211857f6e', 'a70b423ca1fdf0f263672fb517fd21e5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'dca5ef1fac3c42bab5407f3211857f6e', 'af104e036cbb3dc685df86b88beb2657', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'dca5ef1fac3c42bab5407f3211857f6e', 'ad4b094d7973b990ce47a890ce7e9732', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'dca5ef1fac3c42bab5407f3211857f6e', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'dca5ef1fac3c42bab5407f3211857f6e', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'dca5ef1fac3c42bab5407f3211857f6e', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'dca5ef1fac3c42bab5407f3211857f6e', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'dca5ef1fac3c42bab5407f3211857f6e', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'dca5ef1fac3c42bab5407f3211857f6e', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'dca5ef1fac3c42bab5407f3211857f6e', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'dca5ef1fac3c42bab5407f3211857f6e', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'dca5ef1fac3c42bab5407f3211857f6e', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'fc5df21295ff456e81044a0c4f6e2678', 'a050b0dd20e9172ff66fcd28eae58721', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'fc5df21295ff456e81044a0c4f6e2678', 'a7496301877e9d5d75fdf131d9f53bb0', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'fc5df21295ff456e81044a0c4f6e2678', 'a70b423ca1fdf0f263672fb517fd21e5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'fc5df21295ff456e81044a0c4f6e2678', 'af104e036cbb3dc685df86b88beb2657', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'fc5df21295ff456e81044a0c4f6e2678', 'ad4b094d7973b990ce47a890ce7e9732', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'fc5df21295ff456e81044a0c4f6e2678', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'fc5df21295ff456e81044a0c4f6e2678', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'fc5df21295ff456e81044a0c4f6e2678', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'fc5df21295ff456e81044a0c4f6e2678', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'fc5df21295ff456e81044a0c4f6e2678', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'fc5df21295ff456e81044a0c4f6e2678', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'fc5df21295ff456e81044a0c4f6e2678', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'fc5df21295ff456e81044a0c4f6e2678', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'fc5df21295ff456e81044a0c4f6e2678', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '893d7bf6289e49c4b02542b33faedeec', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '893d7bf6289e49c4b02542b33faedeec', 'a7496301877e9d5d75fdf131d9f53bb0', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '893d7bf6289e49c4b02542b33faedeec', 'a70b423ca1fdf0f263672fb517fd21e5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '893d7bf6289e49c4b02542b33faedeec', 'af104e036cbb3dc685df86b88beb2657', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '893d7bf6289e49c4b02542b33faedeec', 'ad4b094d7973b990ce47a890ce7e9732', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '893d7bf6289e49c4b02542b33faedeec', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '893d7bf6289e49c4b02542b33faedeec', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '893d7bf6289e49c4b02542b33faedeec', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '893d7bf6289e49c4b02542b33faedeec', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '893d7bf6289e49c4b02542b33faedeec', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '893d7bf6289e49c4b02542b33faedeec', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '893d7bf6289e49c4b02542b33faedeec', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '893d7bf6289e49c4b02542b33faedeec', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '893d7bf6289e49c4b02542b33faedeec', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '4c50c453a14d4df6b07d2cb5b83f6353', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '4c50c453a14d4df6b07d2cb5b83f6353', 'a7496301877e9d5d75fdf131d9f53bb0', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '4c50c453a14d4df6b07d2cb5b83f6353', 'a70b423ca1fdf0f263672fb517fd21e5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '4c50c453a14d4df6b07d2cb5b83f6353', 'af104e036cbb3dc685df86b88beb2657', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '4c50c453a14d4df6b07d2cb5b83f6353', 'ad4b094d7973b990ce47a890ce7e9732', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '4c50c453a14d4df6b07d2cb5b83f6353', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '4c50c453a14d4df6b07d2cb5b83f6353', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '4c50c453a14d4df6b07d2cb5b83f6353', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '4c50c453a14d4df6b07d2cb5b83f6353', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '4c50c453a14d4df6b07d2cb5b83f6353', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '4c50c453a14d4df6b07d2cb5b83f6353', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '4c50c453a14d4df6b07d2cb5b83f6353', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '4c50c453a14d4df6b07d2cb5b83f6353', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '4c50c453a14d4df6b07d2cb5b83f6353', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '636bc689c20440489786d068719c0b8a', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '636bc689c20440489786d068719c0b8a', 'a7496301877e9d5d75fdf131d9f53bb0', 'editable');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '636bc689c20440489786d068719c0b8a', 'a70b423ca1fdf0f263672fb517fd21e5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '636bc689c20440489786d068719c0b8a', 'af104e036cbb3dc685df86b88beb2657', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '636bc689c20440489786d068719c0b8a', 'ad4b094d7973b990ce47a890ce7e9732', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '636bc689c20440489786d068719c0b8a', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '636bc689c20440489786d068719c0b8a', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '636bc689c20440489786d068719c0b8a', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '636bc689c20440489786d068719c0b8a', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '636bc689c20440489786d068719c0b8a', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '636bc689c20440489786d068719c0b8a', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '636bc689c20440489786d068719c0b8a', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '636bc689c20440489786d068719c0b8a', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '636bc689c20440489786d068719c0b8a', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '4a28390d2e4c48b9b55c13048f584910', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '4a28390d2e4c48b9b55c13048f584910', 'a7496301877e9d5d75fdf131d9f53bb0', 'editable');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '4a28390d2e4c48b9b55c13048f584910', 'a70b423ca1fdf0f263672fb517fd21e5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '4a28390d2e4c48b9b55c13048f584910', 'af104e036cbb3dc685df86b88beb2657', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '4a28390d2e4c48b9b55c13048f584910', 'ad4b094d7973b990ce47a890ce7e9732', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '4a28390d2e4c48b9b55c13048f584910', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '4a28390d2e4c48b9b55c13048f584910', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '4a28390d2e4c48b9b55c13048f584910', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '4a28390d2e4c48b9b55c13048f584910', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '4a28390d2e4c48b9b55c13048f584910', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '4a28390d2e4c48b9b55c13048f584910', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '4a28390d2e4c48b9b55c13048f584910', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '4a28390d2e4c48b9b55c13048f584910', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '4a28390d2e4c48b9b55c13048f584910', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'dbaa5d0552dd489390a5c58883f9637b', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'dbaa5d0552dd489390a5c58883f9637b', 'a7496301877e9d5d75fdf131d9f53bb0', 'editable');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'dbaa5d0552dd489390a5c58883f9637b', 'a70b423ca1fdf0f263672fb517fd21e5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'dbaa5d0552dd489390a5c58883f9637b', 'af104e036cbb3dc685df86b88beb2657', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'dbaa5d0552dd489390a5c58883f9637b', 'ad4b094d7973b990ce47a890ce7e9732', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'dbaa5d0552dd489390a5c58883f9637b', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'dbaa5d0552dd489390a5c58883f9637b', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'dbaa5d0552dd489390a5c58883f9637b', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'dbaa5d0552dd489390a5c58883f9637b', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'dbaa5d0552dd489390a5c58883f9637b', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'dbaa5d0552dd489390a5c58883f9637b', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'dbaa5d0552dd489390a5c58883f9637b', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'dbaa5d0552dd489390a5c58883f9637b', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'dbaa5d0552dd489390a5c58883f9637b', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '03ef421c8aff438fabd8802c27c3f9a0', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '03ef421c8aff438fabd8802c27c3f9a0', 'a7496301877e9d5d75fdf131d9f53bb0', 'editableRequired');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '03ef421c8aff438fabd8802c27c3f9a0', 'a70b423ca1fdf0f263672fb517fd21e5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '03ef421c8aff438fabd8802c27c3f9a0', 'af104e036cbb3dc685df86b88beb2657', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '03ef421c8aff438fabd8802c27c3f9a0', 'ad4b094d7973b990ce47a890ce7e9732', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '03ef421c8aff438fabd8802c27c3f9a0', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '03ef421c8aff438fabd8802c27c3f9a0', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '03ef421c8aff438fabd8802c27c3f9a0', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '03ef421c8aff438fabd8802c27c3f9a0', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '03ef421c8aff438fabd8802c27c3f9a0', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '03ef421c8aff438fabd8802c27c3f9a0', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '03ef421c8aff438fabd8802c27c3f9a0', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '03ef421c8aff438fabd8802c27c3f9a0', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '03ef421c8aff438fabd8802c27c3f9a0', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a1a5b7e4cb47a9bcd8c2683ab5ea2670', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a1a5b7e4cb47a9bcd8c2683ab5ea2670', 'a7496301877e9d5d75fdf131d9f53bb0', 'editableRequired');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a1a5b7e4cb47a9bcd8c2683ab5ea2670', 'a70b423ca1fdf0f263672fb517fd21e5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a1a5b7e4cb47a9bcd8c2683ab5ea2670', 'af104e036cbb3dc685df86b88beb2657', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a1a5b7e4cb47a9bcd8c2683ab5ea2670', 'ad4b094d7973b990ce47a890ce7e9732', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a1a5b7e4cb47a9bcd8c2683ab5ea2670', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a1a5b7e4cb47a9bcd8c2683ab5ea2670', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a1a5b7e4cb47a9bcd8c2683ab5ea2670', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a1a5b7e4cb47a9bcd8c2683ab5ea2670', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a1a5b7e4cb47a9bcd8c2683ab5ea2670', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a1a5b7e4cb47a9bcd8c2683ab5ea2670', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a1a5b7e4cb47a9bcd8c2683ab5ea2670', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a1a5b7e4cb47a9bcd8c2683ab5ea2670', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a1a5b7e4cb47a9bcd8c2683ab5ea2670', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a141173392185a23c919f2efed84a9ab', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a141173392185a23c919f2efed84a9ab', 'a7496301877e9d5d75fdf131d9f53bb0', 'editableRequired');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a141173392185a23c919f2efed84a9ab', 'a70b423ca1fdf0f263672fb517fd21e5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a141173392185a23c919f2efed84a9ab', 'af104e036cbb3dc685df86b88beb2657', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a141173392185a23c919f2efed84a9ab', 'ad4b094d7973b990ce47a890ce7e9732', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a141173392185a23c919f2efed84a9ab', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a141173392185a23c919f2efed84a9ab', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a141173392185a23c919f2efed84a9ab', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a141173392185a23c919f2efed84a9ab', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a141173392185a23c919f2efed84a9ab', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a141173392185a23c919f2efed84a9ab', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a141173392185a23c919f2efed84a9ab', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a141173392185a23c919f2efed84a9ab', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a141173392185a23c919f2efed84a9ab', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a3dd667185aa125018e81cdf90493e44', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a3dd667185aa125018e81cdf90493e44', 'a7496301877e9d5d75fdf131d9f53bb0', 'editableRequired');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a3dd667185aa125018e81cdf90493e44', 'a70b423ca1fdf0f263672fb517fd21e5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a3dd667185aa125018e81cdf90493e44', 'af104e036cbb3dc685df86b88beb2657', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a3dd667185aa125018e81cdf90493e44', 'ad4b094d7973b990ce47a890ce7e9732', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a3dd667185aa125018e81cdf90493e44', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a3dd667185aa125018e81cdf90493e44', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a3dd667185aa125018e81cdf90493e44', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a3dd667185aa125018e81cdf90493e44', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a3dd667185aa125018e81cdf90493e44', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a3dd667185aa125018e81cdf90493e44', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a3dd667185aa125018e81cdf90493e44', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a3dd667185aa125018e81cdf90493e44', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a3dd667185aa125018e81cdf90493e44', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ad0227ad0079b5dca85f452fe33b807d', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ad0227ad0079b5dca85f452fe33b807d', 'a7496301877e9d5d75fdf131d9f53bb0', 'editableRequired');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ad0227ad0079b5dca85f452fe33b807d', 'a70b423ca1fdf0f263672fb517fd21e5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ad0227ad0079b5dca85f452fe33b807d', 'af104e036cbb3dc685df86b88beb2657', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ad0227ad0079b5dca85f452fe33b807d', 'ad4b094d7973b990ce47a890ce7e9732', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ad0227ad0079b5dca85f452fe33b807d', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ad0227ad0079b5dca85f452fe33b807d', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ad0227ad0079b5dca85f452fe33b807d', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ad0227ad0079b5dca85f452fe33b807d', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ad0227ad0079b5dca85f452fe33b807d', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ad0227ad0079b5dca85f452fe33b807d', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ad0227ad0079b5dca85f452fe33b807d', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ad0227ad0079b5dca85f452fe33b807d', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ad0227ad0079b5dca85f452fe33b807d', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'abeee66f95e85ae97e684ee0b56da431', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'abeee66f95e85ae97e684ee0b56da431', 'a7496301877e9d5d75fdf131d9f53bb0', 'editableRequired');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'abeee66f95e85ae97e684ee0b56da431', 'a70b423ca1fdf0f263672fb517fd21e5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'abeee66f95e85ae97e684ee0b56da431', 'af104e036cbb3dc685df86b88beb2657', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'abeee66f95e85ae97e684ee0b56da431', 'ad4b094d7973b990ce47a890ce7e9732', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'abeee66f95e85ae97e684ee0b56da431', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'abeee66f95e85ae97e684ee0b56da431', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'abeee66f95e85ae97e684ee0b56da431', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'abeee66f95e85ae97e684ee0b56da431', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'abeee66f95e85ae97e684ee0b56da431', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'abeee66f95e85ae97e684ee0b56da431', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'abeee66f95e85ae97e684ee0b56da431', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'abeee66f95e85ae97e684ee0b56da431', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'abeee66f95e85ae97e684ee0b56da431', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a26001b80809c72b05238532ff989e51', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a26001b80809c72b05238532ff989e51', 'a7496301877e9d5d75fdf131d9f53bb0', 'editableRequired');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a26001b80809c72b05238532ff989e51', 'a70b423ca1fdf0f263672fb517fd21e5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a26001b80809c72b05238532ff989e51', 'af104e036cbb3dc685df86b88beb2657', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a26001b80809c72b05238532ff989e51', 'ad4b094d7973b990ce47a890ce7e9732', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a26001b80809c72b05238532ff989e51', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a26001b80809c72b05238532ff989e51', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a26001b80809c72b05238532ff989e51', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a26001b80809c72b05238532ff989e51', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a26001b80809c72b05238532ff989e51', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a26001b80809c72b05238532ff989e51', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a26001b80809c72b05238532ff989e51', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a26001b80809c72b05238532ff989e51', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a26001b80809c72b05238532ff989e51', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a95e849bce093c1cbea7a887c8a750fe', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a95e849bce093c1cbea7a887c8a750fe', 'a7496301877e9d5d75fdf131d9f53bb0', 'editableRequired');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a95e849bce093c1cbea7a887c8a750fe', 'a70b423ca1fdf0f263672fb517fd21e5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a95e849bce093c1cbea7a887c8a750fe', 'af104e036cbb3dc685df86b88beb2657', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a95e849bce093c1cbea7a887c8a750fe', 'ad4b094d7973b990ce47a890ce7e9732', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a95e849bce093c1cbea7a887c8a750fe', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a95e849bce093c1cbea7a887c8a750fe', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a95e849bce093c1cbea7a887c8a750fe', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a95e849bce093c1cbea7a887c8a750fe', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a95e849bce093c1cbea7a887c8a750fe', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a95e849bce093c1cbea7a887c8a750fe', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a95e849bce093c1cbea7a887c8a750fe', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a95e849bce093c1cbea7a887c8a750fe', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a95e849bce093c1cbea7a887c8a750fe', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '9793a1313c6046a281253143f24d64ac', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '9793a1313c6046a281253143f24d64ac', 'a7496301877e9d5d75fdf131d9f53bb0', 'editable');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '9793a1313c6046a281253143f24d64ac', 'a70b423ca1fdf0f263672fb517fd21e5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '9793a1313c6046a281253143f24d64ac', 'af104e036cbb3dc685df86b88beb2657', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '9793a1313c6046a281253143f24d64ac', 'ad4b094d7973b990ce47a890ce7e9732', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '9793a1313c6046a281253143f24d64ac', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '9793a1313c6046a281253143f24d64ac', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '9793a1313c6046a281253143f24d64ac', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '9793a1313c6046a281253143f24d64ac', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '9793a1313c6046a281253143f24d64ac', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '9793a1313c6046a281253143f24d64ac', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '9793a1313c6046a281253143f24d64ac', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '9793a1313c6046a281253143f24d64ac', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '9793a1313c6046a281253143f24d64ac', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '30d12a1db5b94969b349d7ea24d574c5', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '30d12a1db5b94969b349d7ea24d574c5', 'a7496301877e9d5d75fdf131d9f53bb0', 'editable');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '30d12a1db5b94969b349d7ea24d574c5', 'a70b423ca1fdf0f263672fb517fd21e5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '30d12a1db5b94969b349d7ea24d574c5', 'af104e036cbb3dc685df86b88beb2657', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '30d12a1db5b94969b349d7ea24d574c5', 'ad4b094d7973b990ce47a890ce7e9732', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '30d12a1db5b94969b349d7ea24d574c5', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '30d12a1db5b94969b349d7ea24d574c5', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '30d12a1db5b94969b349d7ea24d574c5', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '30d12a1db5b94969b349d7ea24d574c5', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '30d12a1db5b94969b349d7ea24d574c5', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '30d12a1db5b94969b349d7ea24d574c5', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '30d12a1db5b94969b349d7ea24d574c5', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '30d12a1db5b94969b349d7ea24d574c5', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '30d12a1db5b94969b349d7ea24d574c5', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '5e462c62e5bb44dcae86fa61e939198f', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '5e462c62e5bb44dcae86fa61e939198f', 'a7496301877e9d5d75fdf131d9f53bb0', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '5e462c62e5bb44dcae86fa61e939198f', 'a70b423ca1fdf0f263672fb517fd21e5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '5e462c62e5bb44dcae86fa61e939198f', 'af104e036cbb3dc685df86b88beb2657', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '5e462c62e5bb44dcae86fa61e939198f', 'ad4b094d7973b990ce47a890ce7e9732', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '5e462c62e5bb44dcae86fa61e939198f', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '5e462c62e5bb44dcae86fa61e939198f', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '5e462c62e5bb44dcae86fa61e939198f', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '5e462c62e5bb44dcae86fa61e939198f', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '5e462c62e5bb44dcae86fa61e939198f', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '5e462c62e5bb44dcae86fa61e939198f', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '5e462c62e5bb44dcae86fa61e939198f', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '5e462c62e5bb44dcae86fa61e939198f', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '5e462c62e5bb44dcae86fa61e939198f', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'f8ffe25354e74196a90dfef3652a3e7d', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'f8ffe25354e74196a90dfef3652a3e7d', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'f8ffe25354e74196a90dfef3652a3e7d', 'a70b423ca1fdf0f263672fb517fd21e5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'f8ffe25354e74196a90dfef3652a3e7d', 'af104e036cbb3dc685df86b88beb2657', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'f8ffe25354e74196a90dfef3652a3e7d', 'ad4b094d7973b990ce47a890ce7e9732', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'f8ffe25354e74196a90dfef3652a3e7d', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'f8ffe25354e74196a90dfef3652a3e7d', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'f8ffe25354e74196a90dfef3652a3e7d', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'f8ffe25354e74196a90dfef3652a3e7d', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'f8ffe25354e74196a90dfef3652a3e7d', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'f8ffe25354e74196a90dfef3652a3e7d', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'f8ffe25354e74196a90dfef3652a3e7d', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'f8ffe25354e74196a90dfef3652a3e7d', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'f8ffe25354e74196a90dfef3652a3e7d', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '639243d7d02a446e8d383ec3d452ffa3', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '639243d7d02a446e8d383ec3d452ffa3', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '639243d7d02a446e8d383ec3d452ffa3', 'a70b423ca1fdf0f263672fb517fd21e5', 'editable');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '639243d7d02a446e8d383ec3d452ffa3', 'af104e036cbb3dc685df86b88beb2657', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '639243d7d02a446e8d383ec3d452ffa3', 'ad4b094d7973b990ce47a890ce7e9732', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '639243d7d02a446e8d383ec3d452ffa3', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '639243d7d02a446e8d383ec3d452ffa3', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '639243d7d02a446e8d383ec3d452ffa3', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '639243d7d02a446e8d383ec3d452ffa3', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '639243d7d02a446e8d383ec3d452ffa3', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '639243d7d02a446e8d383ec3d452ffa3', 'acb90e940ac03a947df7456d4f0c7967', 'editable');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '639243d7d02a446e8d383ec3d452ffa3', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '639243d7d02a446e8d383ec3d452ffa3', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '639243d7d02a446e8d383ec3d452ffa3', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '2d872066d25f4681b31a77bf6495ab12', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '2d872066d25f4681b31a77bf6495ab12', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '2d872066d25f4681b31a77bf6495ab12', 'a70b423ca1fdf0f263672fb517fd21e5', 'editableRequired');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '2d872066d25f4681b31a77bf6495ab12', 'af104e036cbb3dc685df86b88beb2657', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '2d872066d25f4681b31a77bf6495ab12', 'ad4b094d7973b990ce47a890ce7e9732', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '2d872066d25f4681b31a77bf6495ab12', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '2d872066d25f4681b31a77bf6495ab12', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '2d872066d25f4681b31a77bf6495ab12', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '2d872066d25f4681b31a77bf6495ab12', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '2d872066d25f4681b31a77bf6495ab12', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '2d872066d25f4681b31a77bf6495ab12', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '2d872066d25f4681b31a77bf6495ab12', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '2d872066d25f4681b31a77bf6495ab12', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '2d872066d25f4681b31a77bf6495ab12', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '44eb35f7b3904d928dc502430e98f665', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '44eb35f7b3904d928dc502430e98f665', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '44eb35f7b3904d928dc502430e98f665', 'a70b423ca1fdf0f263672fb517fd21e5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '44eb35f7b3904d928dc502430e98f665', 'af104e036cbb3dc685df86b88beb2657', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '44eb35f7b3904d928dc502430e98f665', 'ad4b094d7973b990ce47a890ce7e9732', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '44eb35f7b3904d928dc502430e98f665', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '44eb35f7b3904d928dc502430e98f665', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '44eb35f7b3904d928dc502430e98f665', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '44eb35f7b3904d928dc502430e98f665', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '44eb35f7b3904d928dc502430e98f665', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '44eb35f7b3904d928dc502430e98f665', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '44eb35f7b3904d928dc502430e98f665', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '44eb35f7b3904d928dc502430e98f665', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '44eb35f7b3904d928dc502430e98f665', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a5553eb7480f43c29f971b0e93804a02', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a5553eb7480f43c29f971b0e93804a02', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a5553eb7480f43c29f971b0e93804a02', 'a70b423ca1fdf0f263672fb517fd21e5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a5553eb7480f43c29f971b0e93804a02', 'af104e036cbb3dc685df86b88beb2657', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a5553eb7480f43c29f971b0e93804a02', 'ad4b094d7973b990ce47a890ce7e9732', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a5553eb7480f43c29f971b0e93804a02', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a5553eb7480f43c29f971b0e93804a02', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a5553eb7480f43c29f971b0e93804a02', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a5553eb7480f43c29f971b0e93804a02', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a5553eb7480f43c29f971b0e93804a02', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a5553eb7480f43c29f971b0e93804a02', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a5553eb7480f43c29f971b0e93804a02', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a5553eb7480f43c29f971b0e93804a02', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a5553eb7480f43c29f971b0e93804a02', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '2cb8e92e4da8405d9e9363d651051ed0', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '2cb8e92e4da8405d9e9363d651051ed0', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '2cb8e92e4da8405d9e9363d651051ed0', 'a70b423ca1fdf0f263672fb517fd21e5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '2cb8e92e4da8405d9e9363d651051ed0', 'af104e036cbb3dc685df86b88beb2657', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '2cb8e92e4da8405d9e9363d651051ed0', 'ad4b094d7973b990ce47a890ce7e9732', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '2cb8e92e4da8405d9e9363d651051ed0', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '2cb8e92e4da8405d9e9363d651051ed0', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '2cb8e92e4da8405d9e9363d651051ed0', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '2cb8e92e4da8405d9e9363d651051ed0', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '2cb8e92e4da8405d9e9363d651051ed0', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '2cb8e92e4da8405d9e9363d651051ed0', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '2cb8e92e4da8405d9e9363d651051ed0', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '2cb8e92e4da8405d9e9363d651051ed0', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '2cb8e92e4da8405d9e9363d651051ed0', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c8dc1e3013c941dda44aee2ad8ff5bff', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c8dc1e3013c941dda44aee2ad8ff5bff', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c8dc1e3013c941dda44aee2ad8ff5bff', 'a70b423ca1fdf0f263672fb517fd21e5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c8dc1e3013c941dda44aee2ad8ff5bff', 'af104e036cbb3dc685df86b88beb2657', 'editableRequired');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c8dc1e3013c941dda44aee2ad8ff5bff', 'ad4b094d7973b990ce47a890ce7e9732', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c8dc1e3013c941dda44aee2ad8ff5bff', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c8dc1e3013c941dda44aee2ad8ff5bff', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c8dc1e3013c941dda44aee2ad8ff5bff', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c8dc1e3013c941dda44aee2ad8ff5bff', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c8dc1e3013c941dda44aee2ad8ff5bff', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c8dc1e3013c941dda44aee2ad8ff5bff', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c8dc1e3013c941dda44aee2ad8ff5bff', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c8dc1e3013c941dda44aee2ad8ff5bff', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c8dc1e3013c941dda44aee2ad8ff5bff', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c5e47b036bf54e64b7c974a7770034ad', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c5e47b036bf54e64b7c974a7770034ad', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c5e47b036bf54e64b7c974a7770034ad', 'a70b423ca1fdf0f263672fb517fd21e5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c5e47b036bf54e64b7c974a7770034ad', 'af104e036cbb3dc685df86b88beb2657', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c5e47b036bf54e64b7c974a7770034ad', 'ad4b094d7973b990ce47a890ce7e9732', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c5e47b036bf54e64b7c974a7770034ad', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c5e47b036bf54e64b7c974a7770034ad', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c5e47b036bf54e64b7c974a7770034ad', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c5e47b036bf54e64b7c974a7770034ad', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c5e47b036bf54e64b7c974a7770034ad', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c5e47b036bf54e64b7c974a7770034ad', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c5e47b036bf54e64b7c974a7770034ad', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c5e47b036bf54e64b7c974a7770034ad', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c5e47b036bf54e64b7c974a7770034ad', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aa0e47d9df139ef6a0a30c6cbf1a20f0', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aa0e47d9df139ef6a0a30c6cbf1a20f0', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aa0e47d9df139ef6a0a30c6cbf1a20f0', 'a70b423ca1fdf0f263672fb517fd21e5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aa0e47d9df139ef6a0a30c6cbf1a20f0', 'af104e036cbb3dc685df86b88beb2657', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aa0e47d9df139ef6a0a30c6cbf1a20f0', 'ad4b094d7973b990ce47a890ce7e9732', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aa0e47d9df139ef6a0a30c6cbf1a20f0', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aa0e47d9df139ef6a0a30c6cbf1a20f0', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aa0e47d9df139ef6a0a30c6cbf1a20f0', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aa0e47d9df139ef6a0a30c6cbf1a20f0', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aa0e47d9df139ef6a0a30c6cbf1a20f0', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aa0e47d9df139ef6a0a30c6cbf1a20f0', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aa0e47d9df139ef6a0a30c6cbf1a20f0', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aa0e47d9df139ef6a0a30c6cbf1a20f0', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aa0e47d9df139ef6a0a30c6cbf1a20f0', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a9a5213c5d2ff5d04a93f5d22b214c0a', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a9a5213c5d2ff5d04a93f5d22b214c0a', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a9a5213c5d2ff5d04a93f5d22b214c0a', 'a70b423ca1fdf0f263672fb517fd21e5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a9a5213c5d2ff5d04a93f5d22b214c0a', 'af104e036cbb3dc685df86b88beb2657', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a9a5213c5d2ff5d04a93f5d22b214c0a', 'ad4b094d7973b990ce47a890ce7e9732', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a9a5213c5d2ff5d04a93f5d22b214c0a', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a9a5213c5d2ff5d04a93f5d22b214c0a', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a9a5213c5d2ff5d04a93f5d22b214c0a', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a9a5213c5d2ff5d04a93f5d22b214c0a', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a9a5213c5d2ff5d04a93f5d22b214c0a', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a9a5213c5d2ff5d04a93f5d22b214c0a', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a9a5213c5d2ff5d04a93f5d22b214c0a', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a9a5213c5d2ff5d04a93f5d22b214c0a', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a9a5213c5d2ff5d04a93f5d22b214c0a', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'acacad22fad99755cd0668c7eb00040d', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'acacad22fad99755cd0668c7eb00040d', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'acacad22fad99755cd0668c7eb00040d', 'a70b423ca1fdf0f263672fb517fd21e5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'acacad22fad99755cd0668c7eb00040d', 'af104e036cbb3dc685df86b88beb2657', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'acacad22fad99755cd0668c7eb00040d', 'ad4b094d7973b990ce47a890ce7e9732', 'editableRequired');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'acacad22fad99755cd0668c7eb00040d', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'acacad22fad99755cd0668c7eb00040d', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'acacad22fad99755cd0668c7eb00040d', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'acacad22fad99755cd0668c7eb00040d', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'acacad22fad99755cd0668c7eb00040d', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'acacad22fad99755cd0668c7eb00040d', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'acacad22fad99755cd0668c7eb00040d', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'acacad22fad99755cd0668c7eb00040d', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'acacad22fad99755cd0668c7eb00040d', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aba545803a0b342007bb193abbb414e5', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aba545803a0b342007bb193abbb414e5', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aba545803a0b342007bb193abbb414e5', 'a70b423ca1fdf0f263672fb517fd21e5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aba545803a0b342007bb193abbb414e5', 'af104e036cbb3dc685df86b88beb2657', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aba545803a0b342007bb193abbb414e5', 'ad4b094d7973b990ce47a890ce7e9732', 'editable');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aba545803a0b342007bb193abbb414e5', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aba545803a0b342007bb193abbb414e5', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aba545803a0b342007bb193abbb414e5', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aba545803a0b342007bb193abbb414e5', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aba545803a0b342007bb193abbb414e5', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aba545803a0b342007bb193abbb414e5', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aba545803a0b342007bb193abbb414e5', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aba545803a0b342007bb193abbb414e5', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aba545803a0b342007bb193abbb414e5', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a7d9c0535f49834bcc5d01fd3d1ad7df', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a7d9c0535f49834bcc5d01fd3d1ad7df', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a7d9c0535f49834bcc5d01fd3d1ad7df', 'a70b423ca1fdf0f263672fb517fd21e5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a7d9c0535f49834bcc5d01fd3d1ad7df', 'af104e036cbb3dc685df86b88beb2657', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a7d9c0535f49834bcc5d01fd3d1ad7df', 'ad4b094d7973b990ce47a890ce7e9732', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a7d9c0535f49834bcc5d01fd3d1ad7df', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a7d9c0535f49834bcc5d01fd3d1ad7df', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a7d9c0535f49834bcc5d01fd3d1ad7df', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a7d9c0535f49834bcc5d01fd3d1ad7df', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a7d9c0535f49834bcc5d01fd3d1ad7df', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a7d9c0535f49834bcc5d01fd3d1ad7df', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a7d9c0535f49834bcc5d01fd3d1ad7df', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a7d9c0535f49834bcc5d01fd3d1ad7df', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a7d9c0535f49834bcc5d01fd3d1ad7df', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aba91068bc801ce3f2dbfda7c3a313a2', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aba91068bc801ce3f2dbfda7c3a313a2', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aba91068bc801ce3f2dbfda7c3a313a2', 'a70b423ca1fdf0f263672fb517fd21e5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aba91068bc801ce3f2dbfda7c3a313a2', 'af104e036cbb3dc685df86b88beb2657', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aba91068bc801ce3f2dbfda7c3a313a2', 'ad4b094d7973b990ce47a890ce7e9732', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aba91068bc801ce3f2dbfda7c3a313a2', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aba91068bc801ce3f2dbfda7c3a313a2', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aba91068bc801ce3f2dbfda7c3a313a2', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aba91068bc801ce3f2dbfda7c3a313a2', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aba91068bc801ce3f2dbfda7c3a313a2', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aba91068bc801ce3f2dbfda7c3a313a2', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aba91068bc801ce3f2dbfda7c3a313a2', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aba91068bc801ce3f2dbfda7c3a313a2', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aba91068bc801ce3f2dbfda7c3a313a2', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a125e83ece531b99d031c78847d340a8', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a125e83ece531b99d031c78847d340a8', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a125e83ece531b99d031c78847d340a8', 'a70b423ca1fdf0f263672fb517fd21e5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a125e83ece531b99d031c78847d340a8', 'af104e036cbb3dc685df86b88beb2657', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a125e83ece531b99d031c78847d340a8', 'ad4b094d7973b990ce47a890ce7e9732', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a125e83ece531b99d031c78847d340a8', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'editable');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a125e83ece531b99d031c78847d340a8', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a125e83ece531b99d031c78847d340a8', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a125e83ece531b99d031c78847d340a8', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a125e83ece531b99d031c78847d340a8', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a125e83ece531b99d031c78847d340a8', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a125e83ece531b99d031c78847d340a8', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a125e83ece531b99d031c78847d340a8', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a125e83ece531b99d031c78847d340a8', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a8c08b7ff66e94bab4ae5c9ddac77b07', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a8c08b7ff66e94bab4ae5c9ddac77b07', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a8c08b7ff66e94bab4ae5c9ddac77b07', 'a70b423ca1fdf0f263672fb517fd21e5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a8c08b7ff66e94bab4ae5c9ddac77b07', 'af104e036cbb3dc685df86b88beb2657', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a8c08b7ff66e94bab4ae5c9ddac77b07', 'ad4b094d7973b990ce47a890ce7e9732', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a8c08b7ff66e94bab4ae5c9ddac77b07', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'editable');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a8c08b7ff66e94bab4ae5c9ddac77b07', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a8c08b7ff66e94bab4ae5c9ddac77b07', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a8c08b7ff66e94bab4ae5c9ddac77b07', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a8c08b7ff66e94bab4ae5c9ddac77b07', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a8c08b7ff66e94bab4ae5c9ddac77b07', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a8c08b7ff66e94bab4ae5c9ddac77b07', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a8c08b7ff66e94bab4ae5c9ddac77b07', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a8c08b7ff66e94bab4ae5c9ddac77b07', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a82c3def831961f635c6bcb8b8e848a2', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a82c3def831961f635c6bcb8b8e848a2', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a82c3def831961f635c6bcb8b8e848a2', 'a70b423ca1fdf0f263672fb517fd21e5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a82c3def831961f635c6bcb8b8e848a2', 'af104e036cbb3dc685df86b88beb2657', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a82c3def831961f635c6bcb8b8e848a2', 'ad4b094d7973b990ce47a890ce7e9732', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a82c3def831961f635c6bcb8b8e848a2', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'editableRequired');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a82c3def831961f635c6bcb8b8e848a2', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a82c3def831961f635c6bcb8b8e848a2', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a82c3def831961f635c6bcb8b8e848a2', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a82c3def831961f635c6bcb8b8e848a2', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a82c3def831961f635c6bcb8b8e848a2', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a82c3def831961f635c6bcb8b8e848a2', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a82c3def831961f635c6bcb8b8e848a2', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a82c3def831961f635c6bcb8b8e848a2', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'af4e80182ff3fea073b396d9e8ff1c2b', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'af4e80182ff3fea073b396d9e8ff1c2b', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'af4e80182ff3fea073b396d9e8ff1c2b', 'a70b423ca1fdf0f263672fb517fd21e5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'af4e80182ff3fea073b396d9e8ff1c2b', 'af104e036cbb3dc685df86b88beb2657', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'af4e80182ff3fea073b396d9e8ff1c2b', 'ad4b094d7973b990ce47a890ce7e9732', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'af4e80182ff3fea073b396d9e8ff1c2b', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'editableRequired');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'af4e80182ff3fea073b396d9e8ff1c2b', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'af4e80182ff3fea073b396d9e8ff1c2b', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'af4e80182ff3fea073b396d9e8ff1c2b', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'af4e80182ff3fea073b396d9e8ff1c2b', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'af4e80182ff3fea073b396d9e8ff1c2b', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'af4e80182ff3fea073b396d9e8ff1c2b', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'af4e80182ff3fea073b396d9e8ff1c2b', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'af4e80182ff3fea073b396d9e8ff1c2b', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a12f03fca3786b969a923f8aad6e1d81', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a12f03fca3786b969a923f8aad6e1d81', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a12f03fca3786b969a923f8aad6e1d81', 'a70b423ca1fdf0f263672fb517fd21e5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a12f03fca3786b969a923f8aad6e1d81', 'af104e036cbb3dc685df86b88beb2657', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a12f03fca3786b969a923f8aad6e1d81', 'ad4b094d7973b990ce47a890ce7e9732', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a12f03fca3786b969a923f8aad6e1d81', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'editable');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a12f03fca3786b969a923f8aad6e1d81', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a12f03fca3786b969a923f8aad6e1d81', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a12f03fca3786b969a923f8aad6e1d81', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a12f03fca3786b969a923f8aad6e1d81', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a12f03fca3786b969a923f8aad6e1d81', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a12f03fca3786b969a923f8aad6e1d81', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a12f03fca3786b969a923f8aad6e1d81', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a12f03fca3786b969a923f8aad6e1d81', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a916992162c9f0747690fabc416098c3', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a916992162c9f0747690fabc416098c3', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a916992162c9f0747690fabc416098c3', 'a70b423ca1fdf0f263672fb517fd21e5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a916992162c9f0747690fabc416098c3', 'af104e036cbb3dc685df86b88beb2657', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a916992162c9f0747690fabc416098c3', 'ad4b094d7973b990ce47a890ce7e9732', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a916992162c9f0747690fabc416098c3', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a916992162c9f0747690fabc416098c3', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a916992162c9f0747690fabc416098c3', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a916992162c9f0747690fabc416098c3', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a916992162c9f0747690fabc416098c3', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a916992162c9f0747690fabc416098c3', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a916992162c9f0747690fabc416098c3', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a916992162c9f0747690fabc416098c3', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a916992162c9f0747690fabc416098c3', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aaa8bef0d513aa0ac8d4d44c53ef7790', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aaa8bef0d513aa0ac8d4d44c53ef7790', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aaa8bef0d513aa0ac8d4d44c53ef7790', 'a70b423ca1fdf0f263672fb517fd21e5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aaa8bef0d513aa0ac8d4d44c53ef7790', 'af104e036cbb3dc685df86b88beb2657', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aaa8bef0d513aa0ac8d4d44c53ef7790', 'ad4b094d7973b990ce47a890ce7e9732', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aaa8bef0d513aa0ac8d4d44c53ef7790', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aaa8bef0d513aa0ac8d4d44c53ef7790', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aaa8bef0d513aa0ac8d4d44c53ef7790', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aaa8bef0d513aa0ac8d4d44c53ef7790', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aaa8bef0d513aa0ac8d4d44c53ef7790', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aaa8bef0d513aa0ac8d4d44c53ef7790', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aaa8bef0d513aa0ac8d4d44c53ef7790', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aaa8bef0d513aa0ac8d4d44c53ef7790', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aaa8bef0d513aa0ac8d4d44c53ef7790', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a6bec26d5bfc73007ff79910d9aff2aa', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a6bec26d5bfc73007ff79910d9aff2aa', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a6bec26d5bfc73007ff79910d9aff2aa', 'a70b423ca1fdf0f263672fb517fd21e5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a6bec26d5bfc73007ff79910d9aff2aa', 'af104e036cbb3dc685df86b88beb2657', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a6bec26d5bfc73007ff79910d9aff2aa', 'ad4b094d7973b990ce47a890ce7e9732', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a6bec26d5bfc73007ff79910d9aff2aa', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'editable');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a6bec26d5bfc73007ff79910d9aff2aa', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a6bec26d5bfc73007ff79910d9aff2aa', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a6bec26d5bfc73007ff79910d9aff2aa', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a6bec26d5bfc73007ff79910d9aff2aa', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a6bec26d5bfc73007ff79910d9aff2aa', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a6bec26d5bfc73007ff79910d9aff2aa', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a6bec26d5bfc73007ff79910d9aff2aa', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a6bec26d5bfc73007ff79910d9aff2aa', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aa2580d3afc5bbfc9e4ad7ba8dad963c', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aa2580d3afc5bbfc9e4ad7ba8dad963c', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aa2580d3afc5bbfc9e4ad7ba8dad963c', 'a70b423ca1fdf0f263672fb517fd21e5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aa2580d3afc5bbfc9e4ad7ba8dad963c', 'af104e036cbb3dc685df86b88beb2657', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aa2580d3afc5bbfc9e4ad7ba8dad963c', 'ad4b094d7973b990ce47a890ce7e9732', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aa2580d3afc5bbfc9e4ad7ba8dad963c', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'editable');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aa2580d3afc5bbfc9e4ad7ba8dad963c', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aa2580d3afc5bbfc9e4ad7ba8dad963c', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aa2580d3afc5bbfc9e4ad7ba8dad963c', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aa2580d3afc5bbfc9e4ad7ba8dad963c', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aa2580d3afc5bbfc9e4ad7ba8dad963c', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aa2580d3afc5bbfc9e4ad7ba8dad963c', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aa2580d3afc5bbfc9e4ad7ba8dad963c', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aa2580d3afc5bbfc9e4ad7ba8dad963c', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a1a84b18bde06171fdd4edf59e1cafd9', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a1a84b18bde06171fdd4edf59e1cafd9', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a1a84b18bde06171fdd4edf59e1cafd9', 'a70b423ca1fdf0f263672fb517fd21e5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a1a84b18bde06171fdd4edf59e1cafd9', 'af104e036cbb3dc685df86b88beb2657', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a1a84b18bde06171fdd4edf59e1cafd9', 'ad4b094d7973b990ce47a890ce7e9732', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a1a84b18bde06171fdd4edf59e1cafd9', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'editableRequired');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a1a84b18bde06171fdd4edf59e1cafd9', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a1a84b18bde06171fdd4edf59e1cafd9', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a1a84b18bde06171fdd4edf59e1cafd9', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a1a84b18bde06171fdd4edf59e1cafd9', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a1a84b18bde06171fdd4edf59e1cafd9', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a1a84b18bde06171fdd4edf59e1cafd9', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a1a84b18bde06171fdd4edf59e1cafd9', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a1a84b18bde06171fdd4edf59e1cafd9', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a6aedfc56ed8c741c9f6ee5e1c3ff322', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a6aedfc56ed8c741c9f6ee5e1c3ff322', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a6aedfc56ed8c741c9f6ee5e1c3ff322', 'a70b423ca1fdf0f263672fb517fd21e5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a6aedfc56ed8c741c9f6ee5e1c3ff322', 'af104e036cbb3dc685df86b88beb2657', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a6aedfc56ed8c741c9f6ee5e1c3ff322', 'ad4b094d7973b990ce47a890ce7e9732', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a6aedfc56ed8c741c9f6ee5e1c3ff322', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'editableRequired');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a6aedfc56ed8c741c9f6ee5e1c3ff322', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a6aedfc56ed8c741c9f6ee5e1c3ff322', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a6aedfc56ed8c741c9f6ee5e1c3ff322', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a6aedfc56ed8c741c9f6ee5e1c3ff322', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a6aedfc56ed8c741c9f6ee5e1c3ff322', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a6aedfc56ed8c741c9f6ee5e1c3ff322', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a6aedfc56ed8c741c9f6ee5e1c3ff322', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a6aedfc56ed8c741c9f6ee5e1c3ff322', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ada9f8e74c0997508637cdc2d428fb94', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ada9f8e74c0997508637cdc2d428fb94', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ada9f8e74c0997508637cdc2d428fb94', 'a70b423ca1fdf0f263672fb517fd21e5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ada9f8e74c0997508637cdc2d428fb94', 'af104e036cbb3dc685df86b88beb2657', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ada9f8e74c0997508637cdc2d428fb94', 'ad4b094d7973b990ce47a890ce7e9732', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ada9f8e74c0997508637cdc2d428fb94', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ada9f8e74c0997508637cdc2d428fb94', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ada9f8e74c0997508637cdc2d428fb94', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ada9f8e74c0997508637cdc2d428fb94', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ada9f8e74c0997508637cdc2d428fb94', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ada9f8e74c0997508637cdc2d428fb94', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ada9f8e74c0997508637cdc2d428fb94', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ada9f8e74c0997508637cdc2d428fb94', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ada9f8e74c0997508637cdc2d428fb94', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'af4eef76e3a542f194ac156ac6dcc06f', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'af4eef76e3a542f194ac156ac6dcc06f', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'af4eef76e3a542f194ac156ac6dcc06f', 'a70b423ca1fdf0f263672fb517fd21e5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'af4eef76e3a542f194ac156ac6dcc06f', 'af104e036cbb3dc685df86b88beb2657', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'af4eef76e3a542f194ac156ac6dcc06f', 'ad4b094d7973b990ce47a890ce7e9732', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'af4eef76e3a542f194ac156ac6dcc06f', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'af4eef76e3a542f194ac156ac6dcc06f', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'af4eef76e3a542f194ac156ac6dcc06f', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'af4eef76e3a542f194ac156ac6dcc06f', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'af4eef76e3a542f194ac156ac6dcc06f', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'af4eef76e3a542f194ac156ac6dcc06f', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'af4eef76e3a542f194ac156ac6dcc06f', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'af4eef76e3a542f194ac156ac6dcc06f', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'af4eef76e3a542f194ac156ac6dcc06f', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aff0f64fc7d9689291e3d32c4fe8047b', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aff0f64fc7d9689291e3d32c4fe8047b', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aff0f64fc7d9689291e3d32c4fe8047b', 'a70b423ca1fdf0f263672fb517fd21e5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aff0f64fc7d9689291e3d32c4fe8047b', 'af104e036cbb3dc685df86b88beb2657', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aff0f64fc7d9689291e3d32c4fe8047b', 'ad4b094d7973b990ce47a890ce7e9732', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aff0f64fc7d9689291e3d32c4fe8047b', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aff0f64fc7d9689291e3d32c4fe8047b', 'a6f88189bc6eea031259fe9b2a323e64', 'editable');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aff0f64fc7d9689291e3d32c4fe8047b', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aff0f64fc7d9689291e3d32c4fe8047b', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aff0f64fc7d9689291e3d32c4fe8047b', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aff0f64fc7d9689291e3d32c4fe8047b', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aff0f64fc7d9689291e3d32c4fe8047b', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aff0f64fc7d9689291e3d32c4fe8047b', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'aff0f64fc7d9689291e3d32c4fe8047b', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a5dbb70aac1337c3e2206a3dbd0074b6', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a5dbb70aac1337c3e2206a3dbd0074b6', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a5dbb70aac1337c3e2206a3dbd0074b6', 'a70b423ca1fdf0f263672fb517fd21e5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a5dbb70aac1337c3e2206a3dbd0074b6', 'af104e036cbb3dc685df86b88beb2657', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a5dbb70aac1337c3e2206a3dbd0074b6', 'ad4b094d7973b990ce47a890ce7e9732', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a5dbb70aac1337c3e2206a3dbd0074b6', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a5dbb70aac1337c3e2206a3dbd0074b6', 'a6f88189bc6eea031259fe9b2a323e64', 'editable');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a5dbb70aac1337c3e2206a3dbd0074b6', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a5dbb70aac1337c3e2206a3dbd0074b6', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a5dbb70aac1337c3e2206a3dbd0074b6', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a5dbb70aac1337c3e2206a3dbd0074b6', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a5dbb70aac1337c3e2206a3dbd0074b6', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a5dbb70aac1337c3e2206a3dbd0074b6', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a5dbb70aac1337c3e2206a3dbd0074b6', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ac4213bdbdfa0f574e5575ba6e7ef8a0', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ac4213bdbdfa0f574e5575ba6e7ef8a0', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ac4213bdbdfa0f574e5575ba6e7ef8a0', 'a70b423ca1fdf0f263672fb517fd21e5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ac4213bdbdfa0f574e5575ba6e7ef8a0', 'af104e036cbb3dc685df86b88beb2657', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ac4213bdbdfa0f574e5575ba6e7ef8a0', 'ad4b094d7973b990ce47a890ce7e9732', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ac4213bdbdfa0f574e5575ba6e7ef8a0', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ac4213bdbdfa0f574e5575ba6e7ef8a0', 'a6f88189bc6eea031259fe9b2a323e64', 'editableRequired');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ac4213bdbdfa0f574e5575ba6e7ef8a0', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ac4213bdbdfa0f574e5575ba6e7ef8a0', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ac4213bdbdfa0f574e5575ba6e7ef8a0', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ac4213bdbdfa0f574e5575ba6e7ef8a0', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ac4213bdbdfa0f574e5575ba6e7ef8a0', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ac4213bdbdfa0f574e5575ba6e7ef8a0', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ac4213bdbdfa0f574e5575ba6e7ef8a0', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a980fefca465ad7fb3bdc83cfed1cd2c', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a980fefca465ad7fb3bdc83cfed1cd2c', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a980fefca465ad7fb3bdc83cfed1cd2c', 'a70b423ca1fdf0f263672fb517fd21e5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a980fefca465ad7fb3bdc83cfed1cd2c', 'af104e036cbb3dc685df86b88beb2657', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a980fefca465ad7fb3bdc83cfed1cd2c', 'ad4b094d7973b990ce47a890ce7e9732', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a980fefca465ad7fb3bdc83cfed1cd2c', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a980fefca465ad7fb3bdc83cfed1cd2c', 'a6f88189bc6eea031259fe9b2a323e64', 'editableRequired');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a980fefca465ad7fb3bdc83cfed1cd2c', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a980fefca465ad7fb3bdc83cfed1cd2c', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a980fefca465ad7fb3bdc83cfed1cd2c', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a980fefca465ad7fb3bdc83cfed1cd2c', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a980fefca465ad7fb3bdc83cfed1cd2c', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a980fefca465ad7fb3bdc83cfed1cd2c', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a980fefca465ad7fb3bdc83cfed1cd2c', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a7e8276db9ef05adcc538678a63b326b', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a7e8276db9ef05adcc538678a63b326b', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a7e8276db9ef05adcc538678a63b326b', 'a70b423ca1fdf0f263672fb517fd21e5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a7e8276db9ef05adcc538678a63b326b', 'af104e036cbb3dc685df86b88beb2657', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a7e8276db9ef05adcc538678a63b326b', 'ad4b094d7973b990ce47a890ce7e9732', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a7e8276db9ef05adcc538678a63b326b', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a7e8276db9ef05adcc538678a63b326b', 'a6f88189bc6eea031259fe9b2a323e64', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a7e8276db9ef05adcc538678a63b326b', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a7e8276db9ef05adcc538678a63b326b', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a7e8276db9ef05adcc538678a63b326b', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a7e8276db9ef05adcc538678a63b326b', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a7e8276db9ef05adcc538678a63b326b', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a7e8276db9ef05adcc538678a63b326b', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a7e8276db9ef05adcc538678a63b326b', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a0d3ea466d5aeeb00d502d8d1b615f6b', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a0d3ea466d5aeeb00d502d8d1b615f6b', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a0d3ea466d5aeeb00d502d8d1b615f6b', 'a70b423ca1fdf0f263672fb517fd21e5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a0d3ea466d5aeeb00d502d8d1b615f6b', 'af104e036cbb3dc685df86b88beb2657', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a0d3ea466d5aeeb00d502d8d1b615f6b', 'ad4b094d7973b990ce47a890ce7e9732', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a0d3ea466d5aeeb00d502d8d1b615f6b', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a0d3ea466d5aeeb00d502d8d1b615f6b', 'a6f88189bc6eea031259fe9b2a323e64', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a0d3ea466d5aeeb00d502d8d1b615f6b', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a0d3ea466d5aeeb00d502d8d1b615f6b', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a0d3ea466d5aeeb00d502d8d1b615f6b', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a0d3ea466d5aeeb00d502d8d1b615f6b', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a0d3ea466d5aeeb00d502d8d1b615f6b', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a0d3ea466d5aeeb00d502d8d1b615f6b', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a0d3ea466d5aeeb00d502d8d1b615f6b', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ac4be8fdff1752cbcbec33be1c2d99d8', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ac4be8fdff1752cbcbec33be1c2d99d8', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ac4be8fdff1752cbcbec33be1c2d99d8', 'a70b423ca1fdf0f263672fb517fd21e5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ac4be8fdff1752cbcbec33be1c2d99d8', 'af104e036cbb3dc685df86b88beb2657', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ac4be8fdff1752cbcbec33be1c2d99d8', 'ad4b094d7973b990ce47a890ce7e9732', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ac4be8fdff1752cbcbec33be1c2d99d8', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ac4be8fdff1752cbcbec33be1c2d99d8', 'a6f88189bc6eea031259fe9b2a323e64', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ac4be8fdff1752cbcbec33be1c2d99d8', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'editable');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ac4be8fdff1752cbcbec33be1c2d99d8', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ac4be8fdff1752cbcbec33be1c2d99d8', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ac4be8fdff1752cbcbec33be1c2d99d8', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ac4be8fdff1752cbcbec33be1c2d99d8', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ac4be8fdff1752cbcbec33be1c2d99d8', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ac4be8fdff1752cbcbec33be1c2d99d8', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a9038fef67bfc858282dee6faf72bde8', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a9038fef67bfc858282dee6faf72bde8', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a9038fef67bfc858282dee6faf72bde8', 'a70b423ca1fdf0f263672fb517fd21e5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a9038fef67bfc858282dee6faf72bde8', 'af104e036cbb3dc685df86b88beb2657', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a9038fef67bfc858282dee6faf72bde8', 'ad4b094d7973b990ce47a890ce7e9732', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a9038fef67bfc858282dee6faf72bde8', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a9038fef67bfc858282dee6faf72bde8', 'a6f88189bc6eea031259fe9b2a323e64', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a9038fef67bfc858282dee6faf72bde8', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'editable');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a9038fef67bfc858282dee6faf72bde8', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a9038fef67bfc858282dee6faf72bde8', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a9038fef67bfc858282dee6faf72bde8', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a9038fef67bfc858282dee6faf72bde8', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a9038fef67bfc858282dee6faf72bde8', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a9038fef67bfc858282dee6faf72bde8', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a6a05fb218d3c9c70fd30a1e8c84fca0', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a6a05fb218d3c9c70fd30a1e8c84fca0', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a6a05fb218d3c9c70fd30a1e8c84fca0', 'a70b423ca1fdf0f263672fb517fd21e5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a6a05fb218d3c9c70fd30a1e8c84fca0', 'af104e036cbb3dc685df86b88beb2657', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a6a05fb218d3c9c70fd30a1e8c84fca0', 'ad4b094d7973b990ce47a890ce7e9732', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a6a05fb218d3c9c70fd30a1e8c84fca0', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a6a05fb218d3c9c70fd30a1e8c84fca0', 'a6f88189bc6eea031259fe9b2a323e64', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a6a05fb218d3c9c70fd30a1e8c84fca0', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'editableRequired');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a6a05fb218d3c9c70fd30a1e8c84fca0', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a6a05fb218d3c9c70fd30a1e8c84fca0', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a6a05fb218d3c9c70fd30a1e8c84fca0', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a6a05fb218d3c9c70fd30a1e8c84fca0', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a6a05fb218d3c9c70fd30a1e8c84fca0', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a6a05fb218d3c9c70fd30a1e8c84fca0', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a87d67e48b0a396f09253dd92daaae2a', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a87d67e48b0a396f09253dd92daaae2a', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a87d67e48b0a396f09253dd92daaae2a', 'a70b423ca1fdf0f263672fb517fd21e5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a87d67e48b0a396f09253dd92daaae2a', 'af104e036cbb3dc685df86b88beb2657', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a87d67e48b0a396f09253dd92daaae2a', 'ad4b094d7973b990ce47a890ce7e9732', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a87d67e48b0a396f09253dd92daaae2a', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a87d67e48b0a396f09253dd92daaae2a', 'a6f88189bc6eea031259fe9b2a323e64', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a87d67e48b0a396f09253dd92daaae2a', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a87d67e48b0a396f09253dd92daaae2a', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a87d67e48b0a396f09253dd92daaae2a', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a87d67e48b0a396f09253dd92daaae2a', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a87d67e48b0a396f09253dd92daaae2a', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a87d67e48b0a396f09253dd92daaae2a', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a87d67e48b0a396f09253dd92daaae2a', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'adfc19b1f9e03fb6f5b6a58eb992f635', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'adfc19b1f9e03fb6f5b6a58eb992f635', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'adfc19b1f9e03fb6f5b6a58eb992f635', 'a70b423ca1fdf0f263672fb517fd21e5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'adfc19b1f9e03fb6f5b6a58eb992f635', 'af104e036cbb3dc685df86b88beb2657', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'adfc19b1f9e03fb6f5b6a58eb992f635', 'ad4b094d7973b990ce47a890ce7e9732', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'adfc19b1f9e03fb6f5b6a58eb992f635', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'adfc19b1f9e03fb6f5b6a58eb992f635', 'a6f88189bc6eea031259fe9b2a323e64', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'adfc19b1f9e03fb6f5b6a58eb992f635', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'adfc19b1f9e03fb6f5b6a58eb992f635', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'adfc19b1f9e03fb6f5b6a58eb992f635', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'adfc19b1f9e03fb6f5b6a58eb992f635', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'adfc19b1f9e03fb6f5b6a58eb992f635', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'adfc19b1f9e03fb6f5b6a58eb992f635', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'adfc19b1f9e03fb6f5b6a58eb992f635', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a0a8f65a8c3669bc39b3311b54ebea81', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a0a8f65a8c3669bc39b3311b54ebea81', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a0a8f65a8c3669bc39b3311b54ebea81', 'a70b423ca1fdf0f263672fb517fd21e5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a0a8f65a8c3669bc39b3311b54ebea81', 'af104e036cbb3dc685df86b88beb2657', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a0a8f65a8c3669bc39b3311b54ebea81', 'ad4b094d7973b990ce47a890ce7e9732', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a0a8f65a8c3669bc39b3311b54ebea81', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a0a8f65a8c3669bc39b3311b54ebea81', 'a6f88189bc6eea031259fe9b2a323e64', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a0a8f65a8c3669bc39b3311b54ebea81', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a0a8f65a8c3669bc39b3311b54ebea81', 'a144e8c56fbf610796ff294246fe618d', 'editable');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a0a8f65a8c3669bc39b3311b54ebea81', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a0a8f65a8c3669bc39b3311b54ebea81', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a0a8f65a8c3669bc39b3311b54ebea81', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a0a8f65a8c3669bc39b3311b54ebea81', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a0a8f65a8c3669bc39b3311b54ebea81', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ac5c39724c6d72da19d4a277f2fc0861', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ac5c39724c6d72da19d4a277f2fc0861', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ac5c39724c6d72da19d4a277f2fc0861', 'a70b423ca1fdf0f263672fb517fd21e5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ac5c39724c6d72da19d4a277f2fc0861', 'af104e036cbb3dc685df86b88beb2657', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ac5c39724c6d72da19d4a277f2fc0861', 'ad4b094d7973b990ce47a890ce7e9732', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ac5c39724c6d72da19d4a277f2fc0861', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ac5c39724c6d72da19d4a277f2fc0861', 'a6f88189bc6eea031259fe9b2a323e64', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ac5c39724c6d72da19d4a277f2fc0861', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ac5c39724c6d72da19d4a277f2fc0861', 'a144e8c56fbf610796ff294246fe618d', 'editable');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ac5c39724c6d72da19d4a277f2fc0861', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ac5c39724c6d72da19d4a277f2fc0861', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ac5c39724c6d72da19d4a277f2fc0861', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ac5c39724c6d72da19d4a277f2fc0861', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ac5c39724c6d72da19d4a277f2fc0861', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a038232a86c32bf93cc847fb86920d33', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a038232a86c32bf93cc847fb86920d33', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a038232a86c32bf93cc847fb86920d33', 'a70b423ca1fdf0f263672fb517fd21e5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a038232a86c32bf93cc847fb86920d33', 'af104e036cbb3dc685df86b88beb2657', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a038232a86c32bf93cc847fb86920d33', 'ad4b094d7973b990ce47a890ce7e9732', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a038232a86c32bf93cc847fb86920d33', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a038232a86c32bf93cc847fb86920d33', 'a6f88189bc6eea031259fe9b2a323e64', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a038232a86c32bf93cc847fb86920d33', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a038232a86c32bf93cc847fb86920d33', 'a144e8c56fbf610796ff294246fe618d', 'editableRequired');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a038232a86c32bf93cc847fb86920d33', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a038232a86c32bf93cc847fb86920d33', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a038232a86c32bf93cc847fb86920d33', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a038232a86c32bf93cc847fb86920d33', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a038232a86c32bf93cc847fb86920d33', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ad2545081cc9fa330aec2ff41dd87685', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ad2545081cc9fa330aec2ff41dd87685', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ad2545081cc9fa330aec2ff41dd87685', 'a70b423ca1fdf0f263672fb517fd21e5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ad2545081cc9fa330aec2ff41dd87685', 'af104e036cbb3dc685df86b88beb2657', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ad2545081cc9fa330aec2ff41dd87685', 'ad4b094d7973b990ce47a890ce7e9732', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ad2545081cc9fa330aec2ff41dd87685', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ad2545081cc9fa330aec2ff41dd87685', 'a6f88189bc6eea031259fe9b2a323e64', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ad2545081cc9fa330aec2ff41dd87685', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ad2545081cc9fa330aec2ff41dd87685', 'a144e8c56fbf610796ff294246fe618d', 'editableRequired');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ad2545081cc9fa330aec2ff41dd87685', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ad2545081cc9fa330aec2ff41dd87685', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ad2545081cc9fa330aec2ff41dd87685', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ad2545081cc9fa330aec2ff41dd87685', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'ad2545081cc9fa330aec2ff41dd87685', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a98f121f7440d96267a34f3c4c4b3b7b', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a98f121f7440d96267a34f3c4c4b3b7b', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a98f121f7440d96267a34f3c4c4b3b7b', 'a70b423ca1fdf0f263672fb517fd21e5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a98f121f7440d96267a34f3c4c4b3b7b', 'af104e036cbb3dc685df86b88beb2657', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a98f121f7440d96267a34f3c4c4b3b7b', 'ad4b094d7973b990ce47a890ce7e9732', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a98f121f7440d96267a34f3c4c4b3b7b', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a98f121f7440d96267a34f3c4c4b3b7b', 'a6f88189bc6eea031259fe9b2a323e64', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a98f121f7440d96267a34f3c4c4b3b7b', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a98f121f7440d96267a34f3c4c4b3b7b', 'a144e8c56fbf610796ff294246fe618d', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a98f121f7440d96267a34f3c4c4b3b7b', 'a03344086e04e8796d32ac1aca535873', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a98f121f7440d96267a34f3c4c4b3b7b', 'acb90e940ac03a947df7456d4f0c7967', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a98f121f7440d96267a34f3c4c4b3b7b', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a98f121f7440d96267a34f3c4c4b3b7b', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a98f121f7440d96267a34f3c4c4b3b7b', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '50c30230083b426b88de35afe81bae4b', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '50c30230083b426b88de35afe81bae4b', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '50c30230083b426b88de35afe81bae4b', 'a70b423ca1fdf0f263672fb517fd21e5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '50c30230083b426b88de35afe81bae4b', 'af104e036cbb3dc685df86b88beb2657', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '50c30230083b426b88de35afe81bae4b', 'ad4b094d7973b990ce47a890ce7e9732', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '50c30230083b426b88de35afe81bae4b', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '50c30230083b426b88de35afe81bae4b', 'a6f88189bc6eea031259fe9b2a323e64', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '50c30230083b426b88de35afe81bae4b', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '50c30230083b426b88de35afe81bae4b', 'a144e8c56fbf610796ff294246fe618d', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '50c30230083b426b88de35afe81bae4b', 'a03344086e04e8796d32ac1aca535873', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '50c30230083b426b88de35afe81bae4b', 'acb90e940ac03a947df7456d4f0c7967', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '50c30230083b426b88de35afe81bae4b', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '50c30230083b426b88de35afe81bae4b', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '50c30230083b426b88de35afe81bae4b', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '0481303245e348268b8618b25daa6960', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '0481303245e348268b8618b25daa6960', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '0481303245e348268b8618b25daa6960', 'a70b423ca1fdf0f263672fb517fd21e5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '0481303245e348268b8618b25daa6960', 'af104e036cbb3dc685df86b88beb2657', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '0481303245e348268b8618b25daa6960', 'ad4b094d7973b990ce47a890ce7e9732', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '0481303245e348268b8618b25daa6960', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '0481303245e348268b8618b25daa6960', 'a6f88189bc6eea031259fe9b2a323e64', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '0481303245e348268b8618b25daa6960', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '0481303245e348268b8618b25daa6960', 'a144e8c56fbf610796ff294246fe618d', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '0481303245e348268b8618b25daa6960', 'a03344086e04e8796d32ac1aca535873', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '0481303245e348268b8618b25daa6960', 'acb90e940ac03a947df7456d4f0c7967', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '0481303245e348268b8618b25daa6960', 'a2c31eed801f9034c6f83edd0098a348', 'editableRequired');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '0481303245e348268b8618b25daa6960', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '0481303245e348268b8618b25daa6960', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '69619296316a44c79f6dd78af653e16f', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '69619296316a44c79f6dd78af653e16f', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '69619296316a44c79f6dd78af653e16f', 'a70b423ca1fdf0f263672fb517fd21e5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '69619296316a44c79f6dd78af653e16f', 'af104e036cbb3dc685df86b88beb2657', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '69619296316a44c79f6dd78af653e16f', 'ad4b094d7973b990ce47a890ce7e9732', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '69619296316a44c79f6dd78af653e16f', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '69619296316a44c79f6dd78af653e16f', 'a6f88189bc6eea031259fe9b2a323e64', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '69619296316a44c79f6dd78af653e16f', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '69619296316a44c79f6dd78af653e16f', 'a144e8c56fbf610796ff294246fe618d', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '69619296316a44c79f6dd78af653e16f', 'a03344086e04e8796d32ac1aca535873', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '69619296316a44c79f6dd78af653e16f', 'acb90e940ac03a947df7456d4f0c7967', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '69619296316a44c79f6dd78af653e16f', 'a2c31eed801f9034c6f83edd0098a348', 'editableRequired');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '69619296316a44c79f6dd78af653e16f', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '69619296316a44c79f6dd78af653e16f', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd86bfcc74be147d4ae7b0433d7b8a42a', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd86bfcc74be147d4ae7b0433d7b8a42a', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd86bfcc74be147d4ae7b0433d7b8a42a', 'a70b423ca1fdf0f263672fb517fd21e5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd86bfcc74be147d4ae7b0433d7b8a42a', 'af104e036cbb3dc685df86b88beb2657', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd86bfcc74be147d4ae7b0433d7b8a42a', 'ad4b094d7973b990ce47a890ce7e9732', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd86bfcc74be147d4ae7b0433d7b8a42a', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd86bfcc74be147d4ae7b0433d7b8a42a', 'a6f88189bc6eea031259fe9b2a323e64', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd86bfcc74be147d4ae7b0433d7b8a42a', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd86bfcc74be147d4ae7b0433d7b8a42a', 'a144e8c56fbf610796ff294246fe618d', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd86bfcc74be147d4ae7b0433d7b8a42a', 'a03344086e04e8796d32ac1aca535873', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd86bfcc74be147d4ae7b0433d7b8a42a', 'acb90e940ac03a947df7456d4f0c7967', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd86bfcc74be147d4ae7b0433d7b8a42a', 'a2c31eed801f9034c6f83edd0098a348', 'editable');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd86bfcc74be147d4ae7b0433d7b8a42a', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'd86bfcc74be147d4ae7b0433d7b8a42a', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'editable');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'e2bff84339b3467e99c8b957fd14c057', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'e2bff84339b3467e99c8b957fd14c057', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'e2bff84339b3467e99c8b957fd14c057', 'a70b423ca1fdf0f263672fb517fd21e5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'e2bff84339b3467e99c8b957fd14c057', 'af104e036cbb3dc685df86b88beb2657', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'e2bff84339b3467e99c8b957fd14c057', 'ad4b094d7973b990ce47a890ce7e9732', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'e2bff84339b3467e99c8b957fd14c057', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'e2bff84339b3467e99c8b957fd14c057', 'a6f88189bc6eea031259fe9b2a323e64', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'e2bff84339b3467e99c8b957fd14c057', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'e2bff84339b3467e99c8b957fd14c057', 'a144e8c56fbf610796ff294246fe618d', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'e2bff84339b3467e99c8b957fd14c057', 'a03344086e04e8796d32ac1aca535873', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'e2bff84339b3467e99c8b957fd14c057', 'acb90e940ac03a947df7456d4f0c7967', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'e2bff84339b3467e99c8b957fd14c057', 'a2c31eed801f9034c6f83edd0098a348', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'e2bff84339b3467e99c8b957fd14c057', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'e2bff84339b3467e99c8b957fd14c057', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a8d8d72b5692446995ddd37989766156', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a8d8d72b5692446995ddd37989766156', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a8d8d72b5692446995ddd37989766156', 'a70b423ca1fdf0f263672fb517fd21e5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a8d8d72b5692446995ddd37989766156', 'af104e036cbb3dc685df86b88beb2657', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a8d8d72b5692446995ddd37989766156', 'ad4b094d7973b990ce47a890ce7e9732', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a8d8d72b5692446995ddd37989766156', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a8d8d72b5692446995ddd37989766156', 'a6f88189bc6eea031259fe9b2a323e64', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a8d8d72b5692446995ddd37989766156', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a8d8d72b5692446995ddd37989766156', 'a144e8c56fbf610796ff294246fe618d', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a8d8d72b5692446995ddd37989766156', 'a03344086e04e8796d32ac1aca535873', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a8d8d72b5692446995ddd37989766156', 'acb90e940ac03a947df7456d4f0c7967', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a8d8d72b5692446995ddd37989766156', 'a2c31eed801f9034c6f83edd0098a348', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a8d8d72b5692446995ddd37989766156', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'a8d8d72b5692446995ddd37989766156', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '4777a6b2ef2b43908c065319c2bfa516', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '4777a6b2ef2b43908c065319c2bfa516', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '4777a6b2ef2b43908c065319c2bfa516', 'a70b423ca1fdf0f263672fb517fd21e5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '4777a6b2ef2b43908c065319c2bfa516', 'af104e036cbb3dc685df86b88beb2657', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '4777a6b2ef2b43908c065319c2bfa516', 'ad4b094d7973b990ce47a890ce7e9732', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '4777a6b2ef2b43908c065319c2bfa516', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '4777a6b2ef2b43908c065319c2bfa516', 'a6f88189bc6eea031259fe9b2a323e64', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '4777a6b2ef2b43908c065319c2bfa516', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '4777a6b2ef2b43908c065319c2bfa516', 'a144e8c56fbf610796ff294246fe618d', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '4777a6b2ef2b43908c065319c2bfa516', 'a03344086e04e8796d32ac1aca535873', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '4777a6b2ef2b43908c065319c2bfa516', 'acb90e940ac03a947df7456d4f0c7967', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '4777a6b2ef2b43908c065319c2bfa516', 'a2c31eed801f9034c6f83edd0098a348', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '4777a6b2ef2b43908c065319c2bfa516', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'editable');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '4777a6b2ef2b43908c065319c2bfa516', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '12ee5318a80f4c4e97091a770f9fb59c', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '12ee5318a80f4c4e97091a770f9fb59c', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '12ee5318a80f4c4e97091a770f9fb59c', 'a70b423ca1fdf0f263672fb517fd21e5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '12ee5318a80f4c4e97091a770f9fb59c', 'af104e036cbb3dc685df86b88beb2657', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '12ee5318a80f4c4e97091a770f9fb59c', 'ad4b094d7973b990ce47a890ce7e9732', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '12ee5318a80f4c4e97091a770f9fb59c', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '12ee5318a80f4c4e97091a770f9fb59c', 'a6f88189bc6eea031259fe9b2a323e64', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '12ee5318a80f4c4e97091a770f9fb59c', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '12ee5318a80f4c4e97091a770f9fb59c', 'a144e8c56fbf610796ff294246fe618d', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '12ee5318a80f4c4e97091a770f9fb59c', 'a03344086e04e8796d32ac1aca535873', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '12ee5318a80f4c4e97091a770f9fb59c', 'acb90e940ac03a947df7456d4f0c7967', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '12ee5318a80f4c4e97091a770f9fb59c', 'a2c31eed801f9034c6f83edd0098a348', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '12ee5318a80f4c4e97091a770f9fb59c', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'editableRequired');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '12ee5318a80f4c4e97091a770f9fb59c', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '51c1befbe967406da6c774207edfb620', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '51c1befbe967406da6c774207edfb620', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '51c1befbe967406da6c774207edfb620', 'a70b423ca1fdf0f263672fb517fd21e5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '51c1befbe967406da6c774207edfb620', 'af104e036cbb3dc685df86b88beb2657', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '51c1befbe967406da6c774207edfb620', 'ad4b094d7973b990ce47a890ce7e9732', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '51c1befbe967406da6c774207edfb620', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '51c1befbe967406da6c774207edfb620', 'a6f88189bc6eea031259fe9b2a323e64', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '51c1befbe967406da6c774207edfb620', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '51c1befbe967406da6c774207edfb620', 'a144e8c56fbf610796ff294246fe618d', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '51c1befbe967406da6c774207edfb620', 'a03344086e04e8796d32ac1aca535873', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '51c1befbe967406da6c774207edfb620', 'acb90e940ac03a947df7456d4f0c7967', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '51c1befbe967406da6c774207edfb620', 'a2c31eed801f9034c6f83edd0098a348', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '51c1befbe967406da6c774207edfb620', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '51c1befbe967406da6c774207edfb620', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c865d34e6b5547eb9e519fef05ef2dcd', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c865d34e6b5547eb9e519fef05ef2dcd', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c865d34e6b5547eb9e519fef05ef2dcd', 'a70b423ca1fdf0f263672fb517fd21e5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c865d34e6b5547eb9e519fef05ef2dcd', 'af104e036cbb3dc685df86b88beb2657', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c865d34e6b5547eb9e519fef05ef2dcd', 'ad4b094d7973b990ce47a890ce7e9732', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c865d34e6b5547eb9e519fef05ef2dcd', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c865d34e6b5547eb9e519fef05ef2dcd', 'a6f88189bc6eea031259fe9b2a323e64', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c865d34e6b5547eb9e519fef05ef2dcd', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c865d34e6b5547eb9e519fef05ef2dcd', 'a144e8c56fbf610796ff294246fe618d', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c865d34e6b5547eb9e519fef05ef2dcd', 'a03344086e04e8796d32ac1aca535873', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c865d34e6b5547eb9e519fef05ef2dcd', 'acb90e940ac03a947df7456d4f0c7967', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c865d34e6b5547eb9e519fef05ef2dcd', 'a2c31eed801f9034c6f83edd0098a348', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c865d34e6b5547eb9e519fef05ef2dcd', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'c865d34e6b5547eb9e519fef05ef2dcd', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '20174e4a10174aae9f23152b2e3b1b31', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '20174e4a10174aae9f23152b2e3b1b31', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '20174e4a10174aae9f23152b2e3b1b31', 'a70b423ca1fdf0f263672fb517fd21e5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '20174e4a10174aae9f23152b2e3b1b31', 'af104e036cbb3dc685df86b88beb2657', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '20174e4a10174aae9f23152b2e3b1b31', 'ad4b094d7973b990ce47a890ce7e9732', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '20174e4a10174aae9f23152b2e3b1b31', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '20174e4a10174aae9f23152b2e3b1b31', 'a6f88189bc6eea031259fe9b2a323e64', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '20174e4a10174aae9f23152b2e3b1b31', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '20174e4a10174aae9f23152b2e3b1b31', 'a144e8c56fbf610796ff294246fe618d', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '20174e4a10174aae9f23152b2e3b1b31', 'a03344086e04e8796d32ac1aca535873', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '20174e4a10174aae9f23152b2e3b1b31', 'acb90e940ac03a947df7456d4f0c7967', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '20174e4a10174aae9f23152b2e3b1b31', 'a2c31eed801f9034c6f83edd0098a348', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '20174e4a10174aae9f23152b2e3b1b31', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', '20174e4a10174aae9f23152b2e3b1b31', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'e13a94297f794fe99403f95e716f9343', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'e13a94297f794fe99403f95e716f9343', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'e13a94297f794fe99403f95e716f9343', 'a70b423ca1fdf0f263672fb517fd21e5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'e13a94297f794fe99403f95e716f9343', 'af104e036cbb3dc685df86b88beb2657', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'e13a94297f794fe99403f95e716f9343', 'ad4b094d7973b990ce47a890ce7e9732', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'e13a94297f794fe99403f95e716f9343', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'e13a94297f794fe99403f95e716f9343', 'a6f88189bc6eea031259fe9b2a323e64', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'e13a94297f794fe99403f95e716f9343', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'e13a94297f794fe99403f95e716f9343', 'a144e8c56fbf610796ff294246fe618d', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'e13a94297f794fe99403f95e716f9343', 'a03344086e04e8796d32ac1aca535873', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'e13a94297f794fe99403f95e716f9343', 'acb90e940ac03a947df7456d4f0c7967', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'e13a94297f794fe99403f95e716f9343', 'a2c31eed801f9034c6f83edd0098a348', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'e13a94297f794fe99403f95e716f9343', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'e13a94297f794fe99403f95e716f9343', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'editableRequired');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'f0d1968e9bc94888a829cfd9d10fccb5', 'a050b0dd20e9172ff66fcd28eae58721', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'f0d1968e9bc94888a829cfd9d10fccb5', 'a7496301877e9d5d75fdf131d9f53bb0', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'f0d1968e9bc94888a829cfd9d10fccb5', 'a70b423ca1fdf0f263672fb517fd21e5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'f0d1968e9bc94888a829cfd9d10fccb5', 'af104e036cbb3dc685df86b88beb2657', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'f0d1968e9bc94888a829cfd9d10fccb5', 'ad4b094d7973b990ce47a890ce7e9732', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'f0d1968e9bc94888a829cfd9d10fccb5', 'a1e4e7ef954bf6807f50d49851bb9dfb', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'f0d1968e9bc94888a829cfd9d10fccb5', 'a6f88189bc6eea031259fe9b2a323e64', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'f0d1968e9bc94888a829cfd9d10fccb5', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'f0d1968e9bc94888a829cfd9d10fccb5', 'a144e8c56fbf610796ff294246fe618d', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'f0d1968e9bc94888a829cfd9d10fccb5', 'a03344086e04e8796d32ac1aca535873', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'f0d1968e9bc94888a829cfd9d10fccb5', 'acb90e940ac03a947df7456d4f0c7967', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'f0d1968e9bc94888a829cfd9d10fccb5', 'a2c31eed801f9034c6f83edd0098a348', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'f0d1968e9bc94888a829cfd9d10fccb5', 'a4e5a1853ac7156dd593cdcf6282d0d5', 'hidden');
insert into wf_document_display values ('4028b25d7888a7f40178893cfe7f0002', 'f0d1968e9bc94888a829cfd9d10fccb5', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788c9779b60005', '0cab5ed20996478bac575355d01d7b69', 'dff53d10bd754541993d556b15b8c019', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788c9779b60005', '9dd57b7354f546a6b3c15c1577e875d5', 'dff53d10bd754541993d556b15b8c019', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788c9779b60005', 'c275441e4c394193b0ca136b59f44d65', 'dff53d10bd754541993d556b15b8c019', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788c9779b60005', '139bf4f5584445ce8bb3deb79eb5a630', 'dff53d10bd754541993d556b15b8c019', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788c9779b60005', '7db0f91f130c45be89a6ea5e99c66dbe', 'dff53d10bd754541993d556b15b8c019', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788c9779b60005', 'c8108daef968491a9520c942984049b3', 'dff53d10bd754541993d556b15b8c019', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788c9779b60005', '5b0b6965e53049a7934a0d665f0f99da', 'dff53d10bd754541993d556b15b8c019', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788c9779b60005', 'd143565bc73043c99bf2ac72a80afe86', 'dff53d10bd754541993d556b15b8c019', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788c9779b60005', 'aac6679f5ee349d4aec894fbc3bfb279', 'dff53d10bd754541993d556b15b8c019', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788c9779b60005', 'fb9772a1d88342e9aa30ef2c4f729b76', 'dff53d10bd754541993d556b15b8c019', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788c9779b60005', '0cc84deed3424cbab82867fefcfb716e', 'dff53d10bd754541993d556b15b8c019', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788c9779b60005', 'e7ac6934423047d99e744ca42ba80a5e', 'dff53d10bd754541993d556b15b8c019', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788c9779b60005', '1997e740e5c247a6b8b2a4a435fc2bc0', 'dff53d10bd754541993d556b15b8c019', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788c9779b60005', 'd1855ada4f3a493db550efb28e80b985', 'dff53d10bd754541993d556b15b8c019', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788c9779b60005', 'aff338d65894932caaff8f9b5b137564', 'dff53d10bd754541993d556b15b8c019', 'editable');
insert into wf_document_display values ('4028b25d788c4f8601788c9779b60005', 'a841dc0bd9de9054efec9568400748f3', 'dff53d10bd754541993d556b15b8c019', 'editableRequired');
insert into wf_document_display values ('4028b25d788c4f8601788c9779b60005', 'a1daf5eb5c56523e7a975a9114954bfa', 'dff53d10bd754541993d556b15b8c019', 'editableRequired');
insert into wf_document_display values ('4028b25d788c4f8601788c9779b60005', 'a745892a840f00340bf7b267d3a63e90', 'dff53d10bd754541993d556b15b8c019', 'editable');
insert into wf_document_display values ('4028b25d788c4f8601788c9779b60005', 'aa17a892ba968a4a3c2cb6eedea8cc9d', 'dff53d10bd754541993d556b15b8c019', 'editable');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '8e94b33c57044bf6aa072c49fd30d3b1', 'ae599233b126d46d322594dc52902b29', 'editable');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '8e94b33c57044bf6aa072c49fd30d3b1', 'a8b4a594e51d22bc0412f96b2df042c8', 'editable');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '8e94b33c57044bf6aa072c49fd30d3b1', 'ad6b44dd201e5175ec8f9bddc0441415', 'editable');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '8e94b33c57044bf6aa072c49fd30d3b1', 'ac0409214a212091ee70866dd1163ca2', 'editable');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', 'f707bec669fb4d75b3f7e85c089c74ae', 'ae599233b126d46d322594dc52902b29', 'editable');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', 'f707bec669fb4d75b3f7e85c089c74ae', 'a8b4a594e51d22bc0412f96b2df042c8', 'editable');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', 'f707bec669fb4d75b3f7e85c089c74ae', 'ad6b44dd201e5175ec8f9bddc0441415', 'editable');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', 'f707bec669fb4d75b3f7e85c089c74ae', 'ac0409214a212091ee70866dd1163ca2', 'editable');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', 'dce4c9e0702744a9b75f66e2925e6671', 'ae599233b126d46d322594dc52902b29', 'editable');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', 'dce4c9e0702744a9b75f66e2925e6671', 'a8b4a594e51d22bc0412f96b2df042c8', 'editable');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', 'dce4c9e0702744a9b75f66e2925e6671', 'ad6b44dd201e5175ec8f9bddc0441415', 'editable');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', 'dce4c9e0702744a9b75f66e2925e6671', 'ac0409214a212091ee70866dd1163ca2', 'editable');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '43d5564b1f6c44f0ab8976d505fc09b3', 'ae599233b126d46d322594dc52902b29', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '43d5564b1f6c44f0ab8976d505fc09b3', 'a8b4a594e51d22bc0412f96b2df042c8', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '43d5564b1f6c44f0ab8976d505fc09b3', 'ad6b44dd201e5175ec8f9bddc0441415', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '43d5564b1f6c44f0ab8976d505fc09b3', 'ac0409214a212091ee70866dd1163ca2', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', 'a41a893e79af4e60ae84f07f9b09aaf0', 'ae599233b126d46d322594dc52902b29', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', 'a41a893e79af4e60ae84f07f9b09aaf0', 'a8b4a594e51d22bc0412f96b2df042c8', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', 'a41a893e79af4e60ae84f07f9b09aaf0', 'ad6b44dd201e5175ec8f9bddc0441415', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', 'a41a893e79af4e60ae84f07f9b09aaf0', 'ac0409214a212091ee70866dd1163ca2', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', 'c614d28cf0ff46099480ba82f2f3684e', 'ae599233b126d46d322594dc52902b29', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', 'c614d28cf0ff46099480ba82f2f3684e', 'a8b4a594e51d22bc0412f96b2df042c8', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', 'c614d28cf0ff46099480ba82f2f3684e', 'ad6b44dd201e5175ec8f9bddc0441415', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', 'c614d28cf0ff46099480ba82f2f3684e', 'ac0409214a212091ee70866dd1163ca2', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', 'c2185527b27841bf9f9aa292d11c760e', 'ae599233b126d46d322594dc52902b29', 'editable');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', 'c2185527b27841bf9f9aa292d11c760e', 'a8b4a594e51d22bc0412f96b2df042c8', 'editable');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', 'c2185527b27841bf9f9aa292d11c760e', 'ad6b44dd201e5175ec8f9bddc0441415', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', 'c2185527b27841bf9f9aa292d11c760e', 'ac0409214a212091ee70866dd1163ca2', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', 'afc02b23a7f34e578b431da9cdaf41a3', 'ae599233b126d46d322594dc52902b29', 'editableRequired');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', 'afc02b23a7f34e578b431da9cdaf41a3', 'a8b4a594e51d22bc0412f96b2df042c8', 'editableRequired');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', 'afc02b23a7f34e578b431da9cdaf41a3', 'ad6b44dd201e5175ec8f9bddc0441415', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', 'afc02b23a7f34e578b431da9cdaf41a3', 'ac0409214a212091ee70866dd1163ca2', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '1ce826a47a1545ebaba8d5527b0a98e0', 'ae599233b126d46d322594dc52902b29', 'editableRequired');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '1ce826a47a1545ebaba8d5527b0a98e0', 'a8b4a594e51d22bc0412f96b2df042c8', 'editableRequired');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '1ce826a47a1545ebaba8d5527b0a98e0', 'ad6b44dd201e5175ec8f9bddc0441415', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '1ce826a47a1545ebaba8d5527b0a98e0', 'ac0409214a212091ee70866dd1163ca2', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', 'e4c435744d944ed6a8126c08c1c0fff9', 'ae599233b126d46d322594dc52902b29', 'editableRequired');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', 'e4c435744d944ed6a8126c08c1c0fff9', 'a8b4a594e51d22bc0412f96b2df042c8', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', 'e4c435744d944ed6a8126c08c1c0fff9', 'ad6b44dd201e5175ec8f9bddc0441415', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', 'e4c435744d944ed6a8126c08c1c0fff9', 'ac0409214a212091ee70866dd1163ca2', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '244307c8dd204f9cbffbb29107c80e73', 'ae599233b126d46d322594dc52902b29', 'editable');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '244307c8dd204f9cbffbb29107c80e73', 'a8b4a594e51d22bc0412f96b2df042c8', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '244307c8dd204f9cbffbb29107c80e73', 'ad6b44dd201e5175ec8f9bddc0441415', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '244307c8dd204f9cbffbb29107c80e73', 'ac0409214a212091ee70866dd1163ca2', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '28e014bfbc8b46c784e5f4858c8e476d', 'ae599233b126d46d322594dc52902b29', 'editable');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '28e014bfbc8b46c784e5f4858c8e476d', 'a8b4a594e51d22bc0412f96b2df042c8', 'editable');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '28e014bfbc8b46c784e5f4858c8e476d', 'ad6b44dd201e5175ec8f9bddc0441415', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '28e014bfbc8b46c784e5f4858c8e476d', 'ac0409214a212091ee70866dd1163ca2', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '6069b42a0ee048489063e19915e08e46', 'ae599233b126d46d322594dc52902b29', 'editable');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '6069b42a0ee048489063e19915e08e46', 'a8b4a594e51d22bc0412f96b2df042c8', 'editable');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '6069b42a0ee048489063e19915e08e46', 'ad6b44dd201e5175ec8f9bddc0441415', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '6069b42a0ee048489063e19915e08e46', 'ac0409214a212091ee70866dd1163ca2', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '8c3a8a79321a4f70ab17b16e4da9caa6', 'ae599233b126d46d322594dc52902b29', 'editable');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '8c3a8a79321a4f70ab17b16e4da9caa6', 'a8b4a594e51d22bc0412f96b2df042c8', 'editable');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '8c3a8a79321a4f70ab17b16e4da9caa6', 'ad6b44dd201e5175ec8f9bddc0441415', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '8c3a8a79321a4f70ab17b16e4da9caa6', 'ac0409214a212091ee70866dd1163ca2', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '4dafd004cbc944b7996522ffe6b7d809', 'ae599233b126d46d322594dc52902b29', 'hidden');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '4dafd004cbc944b7996522ffe6b7d809', 'a8b4a594e51d22bc0412f96b2df042c8', 'editable');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '4dafd004cbc944b7996522ffe6b7d809', 'ad6b44dd201e5175ec8f9bddc0441415', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '4dafd004cbc944b7996522ffe6b7d809', 'ac0409214a212091ee70866dd1163ca2', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '41193073002544a29e985d2157c2867e', 'ae599233b126d46d322594dc52902b29', 'hidden');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '41193073002544a29e985d2157c2867e', 'a8b4a594e51d22bc0412f96b2df042c8', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '41193073002544a29e985d2157c2867e', 'ad6b44dd201e5175ec8f9bddc0441415', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '41193073002544a29e985d2157c2867e', 'ac0409214a212091ee70866dd1163ca2', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '5b312c6e95c7499ea0671a6afa3381d4', 'ae599233b126d46d322594dc52902b29', 'hidden');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '5b312c6e95c7499ea0671a6afa3381d4', 'a8b4a594e51d22bc0412f96b2df042c8', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '5b312c6e95c7499ea0671a6afa3381d4', 'ad6b44dd201e5175ec8f9bddc0441415', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '5b312c6e95c7499ea0671a6afa3381d4', 'ac0409214a212091ee70866dd1163ca2', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '2b2b34fdcb64411999b201fa09f3bf81', 'ae599233b126d46d322594dc52902b29', 'hidden');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '2b2b34fdcb64411999b201fa09f3bf81', 'a8b4a594e51d22bc0412f96b2df042c8', 'editableRequired');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '2b2b34fdcb64411999b201fa09f3bf81', 'ad6b44dd201e5175ec8f9bddc0441415', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '2b2b34fdcb64411999b201fa09f3bf81', 'ac0409214a212091ee70866dd1163ca2', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', 'd861305d80a045ed8d9a755d5a6c5dda', 'ae599233b126d46d322594dc52902b29', 'hidden');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', 'd861305d80a045ed8d9a755d5a6c5dda', 'a8b4a594e51d22bc0412f96b2df042c8', 'editableRequired');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', 'd861305d80a045ed8d9a755d5a6c5dda', 'ad6b44dd201e5175ec8f9bddc0441415', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', 'd861305d80a045ed8d9a755d5a6c5dda', 'ac0409214a212091ee70866dd1163ca2', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '3d9de87a9e0c4c88aecff1f2db9d14d3', 'ae599233b126d46d322594dc52902b29', 'hidden');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '3d9de87a9e0c4c88aecff1f2db9d14d3', 'a8b4a594e51d22bc0412f96b2df042c8', 'editable');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '3d9de87a9e0c4c88aecff1f2db9d14d3', 'ad6b44dd201e5175ec8f9bddc0441415', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '3d9de87a9e0c4c88aecff1f2db9d14d3', 'ac0409214a212091ee70866dd1163ca2', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '3e1423aabfd24e259a521546f8c10c97', 'ae599233b126d46d322594dc52902b29', 'hidden');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '3e1423aabfd24e259a521546f8c10c97', 'a8b4a594e51d22bc0412f96b2df042c8', 'hidden');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '3e1423aabfd24e259a521546f8c10c97', 'ad6b44dd201e5175ec8f9bddc0441415', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '3e1423aabfd24e259a521546f8c10c97', 'ac0409214a212091ee70866dd1163ca2', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', 'a1c5167618f641d4906997e4ca2e2952', 'ae599233b126d46d322594dc52902b29', 'hidden');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', 'a1c5167618f641d4906997e4ca2e2952', 'a8b4a594e51d22bc0412f96b2df042c8', 'hidden');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', 'a1c5167618f641d4906997e4ca2e2952', 'ad6b44dd201e5175ec8f9bddc0441415', 'editable');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', 'a1c5167618f641d4906997e4ca2e2952', 'ac0409214a212091ee70866dd1163ca2', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '0226c90f73c644fabe92b22e259eb7a7', 'ae599233b126d46d322594dc52902b29', 'hidden');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '0226c90f73c644fabe92b22e259eb7a7', 'a8b4a594e51d22bc0412f96b2df042c8', 'hidden');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '0226c90f73c644fabe92b22e259eb7a7', 'ad6b44dd201e5175ec8f9bddc0441415', 'editableRequired');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '0226c90f73c644fabe92b22e259eb7a7', 'ac0409214a212091ee70866dd1163ca2', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '5ee83a02fecb4fc79eca4c5e2c70d27a', 'ae599233b126d46d322594dc52902b29', 'hidden');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '5ee83a02fecb4fc79eca4c5e2c70d27a', 'a8b4a594e51d22bc0412f96b2df042c8', 'hidden');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '5ee83a02fecb4fc79eca4c5e2c70d27a', 'ad6b44dd201e5175ec8f9bddc0441415', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '5ee83a02fecb4fc79eca4c5e2c70d27a', 'ac0409214a212091ee70866dd1163ca2', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '3141b9fa06ab400bbef50a95e004e3a9', 'ae599233b126d46d322594dc52902b29', 'hidden');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '3141b9fa06ab400bbef50a95e004e3a9', 'a8b4a594e51d22bc0412f96b2df042c8', 'hidden');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '3141b9fa06ab400bbef50a95e004e3a9', 'ad6b44dd201e5175ec8f9bddc0441415', 'hidden');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '3141b9fa06ab400bbef50a95e004e3a9', 'ac0409214a212091ee70866dd1163ca2', 'editable');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', 'eedbef4d73d94377bba70b6c41df2c86', 'ae599233b126d46d322594dc52902b29', 'hidden');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', 'eedbef4d73d94377bba70b6c41df2c86', 'a8b4a594e51d22bc0412f96b2df042c8', 'hidden');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', 'eedbef4d73d94377bba70b6c41df2c86', 'ad6b44dd201e5175ec8f9bddc0441415', 'hidden');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', 'eedbef4d73d94377bba70b6c41df2c86', 'ac0409214a212091ee70866dd1163ca2', 'readonly');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '68dbfd222e8548d4b9302efa2131836f', 'ae599233b126d46d322594dc52902b29', 'hidden');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '68dbfd222e8548d4b9302efa2131836f', 'a8b4a594e51d22bc0412f96b2df042c8', 'hidden');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '68dbfd222e8548d4b9302efa2131836f', 'ad6b44dd201e5175ec8f9bddc0441415', 'hidden');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '68dbfd222e8548d4b9302efa2131836f', 'ac0409214a212091ee70866dd1163ca2', 'editableRequired');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '25f616d3be6b4723b4bbe0b9891c29a2', 'ae599233b126d46d322594dc52902b29', 'hidden');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '25f616d3be6b4723b4bbe0b9891c29a2', 'a8b4a594e51d22bc0412f96b2df042c8', 'hidden');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '25f616d3be6b4723b4bbe0b9891c29a2', 'ad6b44dd201e5175ec8f9bddc0441415', 'hidden');
insert into wf_document_display values ('4028b25d788c4f8601788ca3a7020006', '25f616d3be6b4723b4bbe0b9891c29a2', 'ac0409214a212091ee70866dd1163ca2', 'editable');
/**
 * 엘리먼트세부설정
 */
DROP TABLE IF EXISTS wf_element_data cascade;

CREATE TABLE wf_element_data
(
	element_id varchar(256) NOT NULL,
	attribute_id varchar(100) NOT NULL,
	attribute_value varchar(512) NOT NULL,
	attribute_order int,
	attribute_required boolean DEFAULT 'false' NOT NULL,
	CONSTRAINT wf_element_data_pk PRIMARY KEY (element_id, attribute_id, attribute_value),
	CONSTRAINT wf_element_data_fk FOREIGN KEY (element_id) REFERENCES wf_element (element_id)
);

COMMENT ON TABLE wf_element_data IS '엘리먼트세부설정';
COMMENT ON COLUMN wf_element_data.element_id IS '엘리먼트아이디';
COMMENT ON COLUMN wf_element_data.attribute_id IS '속성아이디';
COMMENT ON COLUMN wf_element_data.attribute_value IS '속성값';
COMMENT ON COLUMN wf_element_data.attribute_order IS '속성순서';
COMMENT ON COLUMN wf_element_data.attribute_required IS '속성필수값';

insert into wf_element_data values ('01430d9b3cfe427b89f9203185e35530', 'action-name', '접수', 0, false);
insert into wf_element_data values ('01430d9b3cfe427b89f9203185e35530', 'action-value', 'accept', 1, false);
insert into wf_element_data values ('01430d9b3cfe427b89f9203185e35530', 'is-default', 'N', 2, false);
insert into wf_element_data values ('01430d9b3cfe427b89f9203185e35530', 'condition-value', '', 3, false);
insert into wf_element_data values ('01430d9b3cfe427b89f9203185e35530', 'start-id', 'bff880e180164c4f9cedfab4ba282f28', 4, true);
insert into wf_element_data values ('01430d9b3cfe427b89f9203185e35530', 'start-name', '신청서 검토', 5, false);
insert into wf_element_data values ('01430d9b3cfe427b89f9203185e35530', 'end-id', 'b0dd93b1c9864edc8f048713c0d07e12', 6, true);
insert into wf_element_data values ('01430d9b3cfe427b89f9203185e35530', 'end-name', '접수', 7, false);
insert into wf_element_data values ('1318f304ca2a420e9c6756438daa4e3a', 'action-name', '승인요청', 0, false);
insert into wf_element_data values ('1318f304ca2a420e9c6756438daa4e3a', 'action-value', 'approve', 1, false);
insert into wf_element_data values ('1318f304ca2a420e9c6756438daa4e3a', 'is-default', 'N', 2, false);
insert into wf_element_data values ('1318f304ca2a420e9c6756438daa4e3a', 'condition-value', '', 3, false);
insert into wf_element_data values ('1318f304ca2a420e9c6756438daa4e3a', 'start-id', 'a34268ba767d48e7ab7db9e7297e7300', 4, true);
insert into wf_element_data values ('1318f304ca2a420e9c6756438daa4e3a', 'start-name', '구성변경 처리', 5, false);
insert into wf_element_data values ('1318f304ca2a420e9c6756438daa4e3a', 'end-id', 'a508191382c644b289c01cf32bed8722', 6, true);
insert into wf_element_data values ('1318f304ca2a420e9c6756438daa4e3a', 'end-name', '변경결과 승인', 7, false);
insert into wf_element_data values ('2a8341aa1dcb4ab7ab89271020c748c8', 'action-name', '', 0, false);
insert into wf_element_data values ('2a8341aa1dcb4ab7ab89271020c748c8', 'action-value', '', 1, false);
insert into wf_element_data values ('2a8341aa1dcb4ab7ab89271020c748c8', 'is-default', 'N', 2, false);
insert into wf_element_data values ('2a8341aa1dcb4ab7ab89271020c748c8', 'condition-value', '', 3, false);
insert into wf_element_data values ('2a8341aa1dcb4ab7ab89271020c748c8', 'start-id', 'd099b9ac855a4e7f9a641906549dbcb0', 4, true);
insert into wf_element_data values ('2a8341aa1dcb4ab7ab89271020c748c8', 'start-name', '', 5, false);
insert into wf_element_data values ('2a8341aa1dcb4ab7ab89271020c748c8', 'end-id', '4f296c1468f3422b8c59c97151e2c476', 6, true);
insert into wf_element_data values ('2a8341aa1dcb4ab7ab89271020c748c8', 'end-name', '신청서 작성', 7, false);
insert into wf_element_data values ('4f296c1468f3422b8c59c97151e2c476', 'assignee-type', 'assignee.type.candidate.groups', 0, true);
insert into wf_element_data values ('4f296c1468f3422b8c59c97151e2c476', 'assignee', 'users.general', 1, true);
insert into wf_element_data values ('4f296c1468f3422b8c59c97151e2c476', 'reject-id', '', 2, false);
insert into wf_element_data values ('4f296c1468f3422b8c59c97151e2c476', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('70e8f5da83584cba81bd9ff597963c4a', 'action-name', '', 0, false);
insert into wf_element_data values ('70e8f5da83584cba81bd9ff597963c4a', 'action-value', '', 1, false);
insert into wf_element_data values ('70e8f5da83584cba81bd9ff597963c4a', 'is-default', 'N', 2, false);
insert into wf_element_data values ('70e8f5da83584cba81bd9ff597963c4a', 'condition-value', '', 3, false);
insert into wf_element_data values ('70e8f5da83584cba81bd9ff597963c4a', 'start-id', 'b0dd93b1c9864edc8f048713c0d07e12', 4, true);
insert into wf_element_data values ('70e8f5da83584cba81bd9ff597963c4a', 'start-name', '접수', 5, false);
insert into wf_element_data values ('70e8f5da83584cba81bd9ff597963c4a', 'end-id', 'a34268ba767d48e7ab7db9e7297e7300', 6, true);
insert into wf_element_data values ('70e8f5da83584cba81bd9ff597963c4a', 'end-name', '구성변경 처리', 7, false);
insert into wf_element_data values ('957ac0be921c4337999fcbca2f22a92d', 'action-name', '신청등록', 0, false);
insert into wf_element_data values ('957ac0be921c4337999fcbca2f22a92d', 'action-value', 'create', 1, false);
insert into wf_element_data values ('957ac0be921c4337999fcbca2f22a92d', 'is-default', 'N', 2, false);
insert into wf_element_data values ('957ac0be921c4337999fcbca2f22a92d', 'condition-value', '', 3, false);
insert into wf_element_data values ('957ac0be921c4337999fcbca2f22a92d', 'start-id', '4f296c1468f3422b8c59c97151e2c476', 4, true);
insert into wf_element_data values ('957ac0be921c4337999fcbca2f22a92d', 'start-name', '신청서 작성', 5, false);
insert into wf_element_data values ('957ac0be921c4337999fcbca2f22a92d', 'end-id', 'bff880e180164c4f9cedfab4ba282f28', 6, true);
insert into wf_element_data values ('957ac0be921c4337999fcbca2f22a92d', 'end-name', '신청서 검토', 7, false);
insert into wf_element_data values ('a34268ba767d48e7ab7db9e7297e7300', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('a34268ba767d48e7ab7db9e7297e7300', 'assignee', 'assignee1', 1, true);
insert into wf_element_data values ('a34268ba767d48e7ab7db9e7297e7300', 'reject-id', '', 2, false);
insert into wf_element_data values ('a34268ba767d48e7ab7db9e7297e7300', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('a4aa971e1f952df93f07e932ab25fbf6', 'script-type', 'script.type.cmdb', 0, true);
insert into wf_element_data values ('a4bd7d4950b4226ea3dfc20bf15193ff', 'action-name', '승인', 0, false);
insert into wf_element_data values ('a4bd7d4950b4226ea3dfc20bf15193ff', 'action-value', 'approval', 1, false);
insert into wf_element_data values ('a4bd7d4950b4226ea3dfc20bf15193ff', 'is-default', 'N', 2, false);
insert into wf_element_data values ('a4bd7d4950b4226ea3dfc20bf15193ff', 'condition-value', '', 3, false);
insert into wf_element_data values ('a4bd7d4950b4226ea3dfc20bf15193ff', 'start-id', 'a508191382c644b289c01cf32bed8722', 4, true);
insert into wf_element_data values ('a4bd7d4950b4226ea3dfc20bf15193ff', 'start-name', '변경결과 승인', 5, false);
insert into wf_element_data values ('a4bd7d4950b4226ea3dfc20bf15193ff', 'end-id', 'a4aa971e1f952df93f07e932ab25fbf6', 6, true);
insert into wf_element_data values ('a4bd7d4950b4226ea3dfc20bf15193ff', 'end-name', 'CMDB 적용', 7, false);
insert into wf_element_data values ('a508191382c644b289c01cf32bed8722', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('a508191382c644b289c01cf32bed8722', 'assignee', 'assignee2', 1, true);
insert into wf_element_data values ('a508191382c644b289c01cf32bed8722', 'reject-id', 'a34268ba767d48e7ab7db9e7297e7300', 2, false);
insert into wf_element_data values ('a508191382c644b289c01cf32bed8722', 'withdraw', 'Y', 3, false);
insert into wf_element_data values ('ad56214bb68e7b749f7d336e14172321', 'action-name', '', 0, false);
insert into wf_element_data values ('ad56214bb68e7b749f7d336e14172321', 'action-value', '', 1, false);
insert into wf_element_data values ('ad56214bb68e7b749f7d336e14172321', 'is-default', 'N', 2, false);
insert into wf_element_data values ('ad56214bb68e7b749f7d336e14172321', 'condition-value', '', 3, false);
insert into wf_element_data values ('ad56214bb68e7b749f7d336e14172321', 'start-id', 'a4aa971e1f952df93f07e932ab25fbf6', 4, true);
insert into wf_element_data values ('ad56214bb68e7b749f7d336e14172321', 'start-name', 'CMDB 적용', 5, false);
insert into wf_element_data values ('ad56214bb68e7b749f7d336e14172321', 'end-id', 'a9a0c170a6ff5ae938f128a726fc2a60', 6, true);
insert into wf_element_data values ('ad56214bb68e7b749f7d336e14172321', 'end-name', '종료', 7, false);
insert into wf_element_data values ('b0dd93b1c9864edc8f048713c0d07e12', 'complete-action', '', 0, false);
insert into wf_element_data values ('bff880e180164c4f9cedfab4ba282f28', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('bff880e180164c4f9cedfab4ba282f28', 'assignee', 'assignee1', 1, true);
insert into wf_element_data values ('bff880e180164c4f9cedfab4ba282f28', 'reject-id', '', 2, false);
insert into wf_element_data values ('bff880e180164c4f9cedfab4ba282f28', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('32b06c8749f1466aa448c2ff6ccf0fdb', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('32b06c8749f1466aa448c2ff6ccf0fdb', 'assignee', 'user', 1, true);
insert into wf_element_data values ('32b06c8749f1466aa448c2ff6ccf0fdb', 'reject-id', '', 2, false);
insert into wf_element_data values ('32b06c8749f1466aa448c2ff6ccf0fdb', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('526362ce18804a71a38c63800ff12656', 'action-name', '만족도 평가', 0, false);
insert into wf_element_data values ('526362ce18804a71a38c63800ff12656', 'action-value', 'progress', 1, false);
insert into wf_element_data values ('526362ce18804a71a38c63800ff12656', 'is-default', 'N', 2, false);
insert into wf_element_data values ('526362ce18804a71a38c63800ff12656', 'condition-value', '', 3, false);
insert into wf_element_data values ('526362ce18804a71a38c63800ff12656', 'start-id', '32b06c8749f1466aa448c2ff6ccf0fdb', 4, true);
insert into wf_element_data values ('526362ce18804a71a38c63800ff12656', 'start-name', 'New Task', 5, false);
insert into wf_element_data values ('526362ce18804a71a38c63800ff12656', 'end-id', '2cb0c129cf054cb1b240f69d47066ff5', 6, true);
insert into wf_element_data values ('526362ce18804a71a38c63800ff12656', 'end-name', '종료', 7, false);
insert into wf_element_data values ('ad56880a70b34398ad693d0ac12b179d', 'action-name', '', 0, false);
insert into wf_element_data values ('ad56880a70b34398ad693d0ac12b179d', 'action-value', '', 1, false);
insert into wf_element_data values ('ad56880a70b34398ad693d0ac12b179d', 'is-default', 'N', 2, false);
insert into wf_element_data values ('ad56880a70b34398ad693d0ac12b179d', 'condition-value', '', 3, false);
insert into wf_element_data values ('ad56880a70b34398ad693d0ac12b179d', 'start-id', '62ee1df53a95473298186031f55f8130', 4, true);
insert into wf_element_data values ('ad56880a70b34398ad693d0ac12b179d', 'start-name', '', 5, false);
insert into wf_element_data values ('ad56880a70b34398ad693d0ac12b179d', 'end-id', '32b06c8749f1466aa448c2ff6ccf0fdb', 6, true);
insert into wf_element_data values ('ad56880a70b34398ad693d0ac12b179d', 'end-name', '만족도평가', 7, false);
insert into wf_element_data values ('079c5509ce2b4ce2973fbe5d6cebb061', 'action-name', '신청등록', 0, false);
insert into wf_element_data values ('079c5509ce2b4ce2973fbe5d6cebb061', 'action-value', 'progress', 1, false);
insert into wf_element_data values ('079c5509ce2b4ce2973fbe5d6cebb061', 'is-default', 'N', 2, false);
insert into wf_element_data values ('079c5509ce2b4ce2973fbe5d6cebb061', 'condition-value', '', 3, false);
insert into wf_element_data values ('079c5509ce2b4ce2973fbe5d6cebb061', 'start-id', 'f64ad538612043189e288b43d4bbd7ce', 4, true);
insert into wf_element_data values ('079c5509ce2b4ce2973fbe5d6cebb061', 'start-name', '신청서 작성', 5, false);
insert into wf_element_data values ('079c5509ce2b4ce2973fbe5d6cebb061', 'end-id', '5da2b19767f745c4af6cce63614954f0', 6, true);
insert into wf_element_data values ('079c5509ce2b4ce2973fbe5d6cebb061', 'end-name', '신청서 검토', 7, false);
insert into wf_element_data values ('1cc092ef9cb04b9398d5e0687e78ae8f', 'target-document-list', '4028b25d78778da6017877aff7e40001', 0, true);
insert into wf_element_data values ('1db3433a1715460e899f5188e8b80940', 'action-name', '', 0, false);
insert into wf_element_data values ('1db3433a1715460e899f5188e8b80940', 'action-value', '', 1, false);
insert into wf_element_data values ('1db3433a1715460e899f5188e8b80940', 'is-default', 'N', 2, false);
insert into wf_element_data values ('1db3433a1715460e899f5188e8b80940', 'condition-value', '', 3, false);
insert into wf_element_data values ('1db3433a1715460e899f5188e8b80940', 'start-id', '1cc092ef9cb04b9398d5e0687e78ae8f', 4, true);
insert into wf_element_data values ('1db3433a1715460e899f5188e8b80940', 'start-name', '만족도평가', 5, false);
insert into wf_element_data values ('1db3433a1715460e899f5188e8b80940', 'end-id', 'aaaf500639bd45d880d0f768bcc91507', 6, true);
insert into wf_element_data values ('1db3433a1715460e899f5188e8b80940', 'end-name', '종료', 7, false);
insert into wf_element_data values ('237ba276e53a40b5a4d62fba388d76f9', 'action-name', '접수', 0, false);
insert into wf_element_data values ('237ba276e53a40b5a4d62fba388d76f9', 'action-value', 'progress', 1, false);
insert into wf_element_data values ('237ba276e53a40b5a4d62fba388d76f9', 'is-default', 'N', 2, false);
insert into wf_element_data values ('237ba276e53a40b5a4d62fba388d76f9', 'condition-value', '', 3, false);
insert into wf_element_data values ('237ba276e53a40b5a4d62fba388d76f9', 'start-id', '5da2b19767f745c4af6cce63614954f0', 4, true);
insert into wf_element_data values ('237ba276e53a40b5a4d62fba388d76f9', 'start-name', '신청서 검토', 5, false);
insert into wf_element_data values ('237ba276e53a40b5a4d62fba388d76f9', 'end-id', 'bcdd50f874ea4ea0954ee8c51bf12fbe', 6, true);
insert into wf_element_data values ('237ba276e53a40b5a4d62fba388d76f9', 'end-name', '접수', 7, false);
insert into wf_element_data values ('5da2b19767f745c4af6cce63614954f0', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('5da2b19767f745c4af6cce63614954f0', 'assignee', 'assignee', 1, true);
insert into wf_element_data values ('5da2b19767f745c4af6cce63614954f0', 'reject-id', '', 2, false);
insert into wf_element_data values ('5da2b19767f745c4af6cce63614954f0', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('86a7a22385ab434b81fe597665865763', 'action-name', '승인', 0, false);
insert into wf_element_data values ('86a7a22385ab434b81fe597665865763', 'action-value', 'progress', 1, false);
insert into wf_element_data values ('86a7a22385ab434b81fe597665865763', 'is-default', 'N', 2, false);
insert into wf_element_data values ('86a7a22385ab434b81fe597665865763', 'condition-value', '', 3, false);
insert into wf_element_data values ('86a7a22385ab434b81fe597665865763', 'start-id', '9fa61b785b944e87b02e9fa753b4c0c5', 4, true);
insert into wf_element_data values ('86a7a22385ab434b81fe597665865763', 'start-name', '승인', 5, false);
insert into wf_element_data values ('86a7a22385ab434b81fe597665865763', 'end-id', '1cc092ef9cb04b9398d5e0687e78ae8f', 6, true);
insert into wf_element_data values ('86a7a22385ab434b81fe597665865763', 'end-name', '만족도평가', 7, false);
insert into wf_element_data values ('9cbc9c049d4741c2bbe8f44d0c7e8de7', 'action-name', '', 0, false);
insert into wf_element_data values ('9cbc9c049d4741c2bbe8f44d0c7e8de7', 'action-value', '', 1, false);
insert into wf_element_data values ('9cbc9c049d4741c2bbe8f44d0c7e8de7', 'is-default', 'N', 2, false);
insert into wf_element_data values ('9cbc9c049d4741c2bbe8f44d0c7e8de7', 'condition-value', '', 3, false);
insert into wf_element_data values ('9cbc9c049d4741c2bbe8f44d0c7e8de7', 'start-id', 'bcdd50f874ea4ea0954ee8c51bf12fbe', 4, true);
insert into wf_element_data values ('9cbc9c049d4741c2bbe8f44d0c7e8de7', 'start-name', '접수', 5, false);
insert into wf_element_data values ('9cbc9c049d4741c2bbe8f44d0c7e8de7', 'end-id', 'd3e931756f15444fa36732356e01a816', 6, true);
insert into wf_element_data values ('9cbc9c049d4741c2bbe8f44d0c7e8de7', 'end-name', '처리', 7, false);
insert into wf_element_data values ('9fa61b785b944e87b02e9fa753b4c0c5', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('9fa61b785b944e87b02e9fa753b4c0c5', 'assignee', 'assignee2', 1, true);
insert into wf_element_data values ('9fa61b785b944e87b02e9fa753b4c0c5', 'reject-id', 'd3e931756f15444fa36732356e01a816', 2, false);
insert into wf_element_data values ('9fa61b785b944e87b02e9fa753b4c0c5', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('bcdd50f874ea4ea0954ee8c51bf12fbe', 'complete-action', '', 0, false);
insert into wf_element_data values ('d01e5d4e5f33495eb239ca868f4511bc', 'action-name', '승인요청', 0, false);
insert into wf_element_data values ('d01e5d4e5f33495eb239ca868f4511bc', 'action-value', 'progress', 1, false);
insert into wf_element_data values ('d01e5d4e5f33495eb239ca868f4511bc', 'is-default', 'N', 2, false);
insert into wf_element_data values ('d01e5d4e5f33495eb239ca868f4511bc', 'condition-value', '', 3, false);
insert into wf_element_data values ('d01e5d4e5f33495eb239ca868f4511bc', 'start-id', 'd3e931756f15444fa36732356e01a816', 4, true);
insert into wf_element_data values ('d01e5d4e5f33495eb239ca868f4511bc', 'start-name', '처리', 5, false);
insert into wf_element_data values ('d01e5d4e5f33495eb239ca868f4511bc', 'end-id', '9fa61b785b944e87b02e9fa753b4c0c5', 6, true);
insert into wf_element_data values ('d01e5d4e5f33495eb239ca868f4511bc', 'end-name', '승인', 7, false);
insert into wf_element_data values ('d13b5beee66a4ac7b79fc0bc28abe0cb', 'action-name', '', 0, false);
insert into wf_element_data values ('d13b5beee66a4ac7b79fc0bc28abe0cb', 'action-value', '', 1, false);
insert into wf_element_data values ('d13b5beee66a4ac7b79fc0bc28abe0cb', 'is-default', 'N', 2, false);
insert into wf_element_data values ('d13b5beee66a4ac7b79fc0bc28abe0cb', 'condition-value', '', 3, false);
insert into wf_element_data values ('d13b5beee66a4ac7b79fc0bc28abe0cb', 'start-id', '90bfe543a47344c98188e2e09ecb50b5', 4, true);
insert into wf_element_data values ('d13b5beee66a4ac7b79fc0bc28abe0cb', 'start-name', '', 5, false);
insert into wf_element_data values ('d13b5beee66a4ac7b79fc0bc28abe0cb', 'end-id', 'f64ad538612043189e288b43d4bbd7ce', 6, true);
insert into wf_element_data values ('d13b5beee66a4ac7b79fc0bc28abe0cb', 'end-name', '신청서 작성', 7, false);
insert into wf_element_data values ('d3e931756f15444fa36732356e01a816', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('d3e931756f15444fa36732356e01a816', 'assignee', 'assignee', 1, true);
insert into wf_element_data values ('d3e931756f15444fa36732356e01a816', 'reject-id', '', 2, false);
insert into wf_element_data values ('d3e931756f15444fa36732356e01a816', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('f64ad538612043189e288b43d4bbd7ce', 'assignee-type', 'assignee.type.candidate.groups', 0, true);
insert into wf_element_data values ('f64ad538612043189e288b43d4bbd7ce', 'assignee', 'users.general', 1, true);
insert into wf_element_data values ('f64ad538612043189e288b43d4bbd7ce', 'reject-id', '', 2, false);
insert into wf_element_data values ('f64ad538612043189e288b43d4bbd7ce', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('ac4c84456b64ae08c1cfeb56234e36d5', 'action-value', 'approve', 1, false);
insert into wf_element_data values ('ac4c84456b64ae08c1cfeb56234e36d5', 'action-name', '승인요청', 0, false);
insert into wf_element_data values ('a92c43aedb89519f0c7b31278063788b', 'action-name', '작업결과서', 0, false);
insert into wf_element_data values ('a92c43aedb89519f0c7b31278063788b', 'action-value', 'work_finish_report', 1, false);
insert into wf_element_data values ('a82fb95a6550c3cd5dadedd7f597c598', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('a82fb95a6550c3cd5dadedd7f597c598', 'assignee', 'rel_manager', 1, true);
insert into wf_element_data values ('a82fb95a6550c3cd5dadedd7f597c598', 'reject-id', '', 2, false);
insert into wf_element_data values ('a82fb95a6550c3cd5dadedd7f597c598', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('a8a1e1db38cdfdbaa692e14db009c224', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('a8a1e1db38cdfdbaa692e14db009c224', 'assignee', 'rel_approver', 1, true);
insert into wf_element_data values ('a8a1e1db38cdfdbaa692e14db009c224', 'reject-id', 'aaa5103974ec9059469823d84d8ce109', 2, false);
insert into wf_element_data values ('a8a1e1db38cdfdbaa692e14db009c224', 'withdraw', 'Y', 3, false);
insert into wf_element_data values ('a905a563d43599def0638503401bcad0', 'condition-item', '#{action}', 0, true);
insert into wf_element_data values ('a92c43aedb89519f0c7b31278063788b', 'is-default', 'N', 2, false);
insert into wf_element_data values ('a92c43aedb89519f0c7b31278063788b', 'condition-value', '', 3, false);
insert into wf_element_data values ('a92c43aedb89519f0c7b31278063788b', 'start-id', 'a82fb95a6550c3cd5dadedd7f597c598', 4, true);
insert into wf_element_data values ('a92c43aedb89519f0c7b31278063788b', 'start-name', '작업결과서', 5, false);
insert into wf_element_data values ('a92c43aedb89519f0c7b31278063788b', 'end-id', 'a258e0bfcfae24e99f75be596efc173e', 6, true);
insert into wf_element_data values ('a92c43aedb89519f0c7b31278063788b', 'end-name', '완료보고서', 7, false);
insert into wf_element_data values ('a9fb5f654c0b856e1975997e9bcdb1c9', 'action-name', '', 0, false);
insert into wf_element_data values ('a9fb5f654c0b856e1975997e9bcdb1c9', 'action-value', '', 1, false);
insert into wf_element_data values ('a9fb5f654c0b856e1975997e9bcdb1c9', 'is-default', 'N', 2, false);
insert into wf_element_data values ('a9fb5f654c0b856e1975997e9bcdb1c9', 'condition-value', ' = 1', 3, false);
insert into wf_element_data values ('a9fb5f654c0b856e1975997e9bcdb1c9', 'start-id', 'a1184c7bed5efd25324bc0d70b1bfdf5', 4, true);
insert into wf_element_data values ('a9fb5f654c0b856e1975997e9bcdb1c9', 'start-name', '', 5, false);
insert into wf_element_data values ('a9fb5f654c0b856e1975997e9bcdb1c9', 'end-id', 'a905a563d43599def0638503401bcad0', 6, true);
insert into wf_element_data values ('a9fb5f654c0b856e1975997e9bcdb1c9', 'end-name', '', 7, false);
insert into wf_element_data values ('aa87e149e75de05f31f28fb9309e91df', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('aa87e149e75de05f31f28fb9309e91df', 'assignee', 'rel_manager', 1, true);
insert into wf_element_data values ('aa87e149e75de05f31f28fb9309e91df', 'reject-id', '', 2, false);
insert into wf_element_data values ('aa87e149e75de05f31f28fb9309e91df', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('aaa5103974ec9059469823d84d8ce109', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('aaa5103974ec9059469823d84d8ce109', 'assignee', 'rel_manager', 1, true);
insert into wf_element_data values ('aaa5103974ec9059469823d84d8ce109', 'reject-id', '', 2, false);
insert into wf_element_data values ('aaa5103974ec9059469823d84d8ce109', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('ac4c84456b64ae08c1cfeb56234e36d5', 'is-default', 'N', 2, false);
insert into wf_element_data values ('ac4c84456b64ae08c1cfeb56234e36d5', 'condition-value', '', 3, false);
insert into wf_element_data values ('ac4c84456b64ae08c1cfeb56234e36d5', 'start-id', 'aaa5103974ec9059469823d84d8ce109', 4, true);
insert into wf_element_data values ('ac4c84456b64ae08c1cfeb56234e36d5', 'start-name', '승인요청', 5, false);
insert into wf_element_data values ('ac4c84456b64ae08c1cfeb56234e36d5', 'end-id', 'a8a1e1db38cdfdbaa692e14db009c224', 6, true);
insert into wf_element_data values ('ac4c84456b64ae08c1cfeb56234e36d5', 'end-name', '승인', 7, false);
insert into wf_element_data values ('aeacaa70d21347699f5c311163d27085', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('aeacaa70d21347699f5c311163d27085', 'assignee', 'rel_manager', 1, true);
insert into wf_element_data values ('aeacaa70d21347699f5c311163d27085', 'reject-id', '', 2, false);
insert into wf_element_data values ('aeacaa70d21347699f5c311163d27085', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('afb41eb311d9c3e7248870e304fc9789', 'action-name', '', 0, false);
insert into wf_element_data values ('a1127cf2ba198dfdd24745876e4616cb', 'action-name', '완료보고서', 0, false);
insert into wf_element_data values ('a1127cf2ba198dfdd24745876e4616cb', 'action-value', 'finish_report', 1, false);
insert into wf_element_data values ('a06f9fbcebe8760486d8db746a025bea', 'action-name', '', 0, false);
insert into wf_element_data values ('a06f9fbcebe8760486d8db746a025bea', 'action-value', '', 1, false);
insert into wf_element_data values ('a06f9fbcebe8760486d8db746a025bea', 'is-default', 'N', 2, false);
insert into wf_element_data values ('a06f9fbcebe8760486d8db746a025bea', 'condition-value', '', 3, false);
insert into wf_element_data values ('a06f9fbcebe8760486d8db746a025bea', 'start-id', 'a905a563d43599def0638503401bcad0', 4, true);
insert into wf_element_data values ('a06f9fbcebe8760486d8db746a025bea', 'start-name', '', 5, false);
insert into wf_element_data values ('a06f9fbcebe8760486d8db746a025bea', 'end-id', 'a61866d755fa520ac6fcca0245f91001', 6, true);
insert into wf_element_data values ('a06f9fbcebe8760486d8db746a025bea', 'end-name', '이행작업', 7, false);
insert into wf_element_data values ('a1127cf2ba198dfdd24745876e4616cb', 'is-default', 'N', 2, false);
insert into wf_element_data values ('a1127cf2ba198dfdd24745876e4616cb', 'condition-value', '', 3, false);
insert into wf_element_data values ('a1127cf2ba198dfdd24745876e4616cb', 'start-id', 'a258e0bfcfae24e99f75be596efc173e', 4, true);
insert into wf_element_data values ('a1127cf2ba198dfdd24745876e4616cb', 'start-name', '완료보고서', 5, false);
insert into wf_element_data values ('a1127cf2ba198dfdd24745876e4616cb', 'end-id', 'ab0f37a1b3f2a2618fbf2be136104ed2', 6, true);
insert into wf_element_data values ('a1127cf2ba198dfdd24745876e4616cb', 'end-name', '종료', 7, false);
insert into wf_element_data values ('a1184c7bed5efd25324bc0d70b1bfdf5', 'condition-item', '${rel_emergency}', 0, true);
insert into wf_element_data values ('a24aa3c4a99fd3d8a4510286045a2f28', 'action-name', '', 0, false);
insert into wf_element_data values ('a24aa3c4a99fd3d8a4510286045a2f28', 'action-value', '', 1, false);
insert into wf_element_data values ('a24aa3c4a99fd3d8a4510286045a2f28', 'is-default', 'N', 2, false);
insert into wf_element_data values ('a24aa3c4a99fd3d8a4510286045a2f28', 'condition-value', '', 3, false);
insert into wf_element_data values ('a24aa3c4a99fd3d8a4510286045a2f28', 'start-id', 'a61866d755fa520ac6fcca0245f91001', 4, true);
insert into wf_element_data values ('a24aa3c4a99fd3d8a4510286045a2f28', 'start-name', '이행작업', 5, false);
insert into wf_element_data values ('a24aa3c4a99fd3d8a4510286045a2f28', 'end-id', 'a82fb95a6550c3cd5dadedd7f597c598', 6, true);
insert into wf_element_data values ('a24aa3c4a99fd3d8a4510286045a2f28', 'end-name', '작업결과서', 7, false);
insert into wf_element_data values ('a258e0bfcfae24e99f75be596efc173e', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('a258e0bfcfae24e99f75be596efc173e', 'assignee', 'rel_manager', 1, true);
insert into wf_element_data values ('a258e0bfcfae24e99f75be596efc173e', 'reject-id', '', 2, false);
insert into wf_element_data values ('a258e0bfcfae24e99f75be596efc173e', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('a29b7166bf3d1e6eb1f743433e1a222a', 'is-default', 'N', 2, false);
insert into wf_element_data values ('a29b7166bf3d1e6eb1f743433e1a222a', 'condition-value', '', 3, false);
insert into wf_element_data values ('a29b7166bf3d1e6eb1f743433e1a222a', 'start-id', 'aa87e149e75de05f31f28fb9309e91df', 4, true);
insert into wf_element_data values ('a29b7166bf3d1e6eb1f743433e1a222a', 'start-name', '작업 계획서', 5, false);
insert into wf_element_data values ('a29b7166bf3d1e6eb1f743433e1a222a', 'end-id', 'a1184c7bed5efd25324bc0d70b1bfdf5', 6, true);
insert into wf_element_data values ('a29b7166bf3d1e6eb1f743433e1a222a', 'end-name', '', 7, false);
insert into wf_element_data values ('a33729f914943aab418ea1e7adad0ee4', 'is-default', 'N', 2, false);
insert into wf_element_data values ('a33729f914943aab418ea1e7adad0ee4', 'condition-value', '', 3, false);
insert into wf_element_data values ('a33729f914943aab418ea1e7adad0ee4', 'start-id', 'aeacaa70d21347699f5c311163d27085', 4, true);
insert into wf_element_data values ('a33729f914943aab418ea1e7adad0ee4', 'start-name', '접수', 5, false);
insert into wf_element_data values ('a33729f914943aab418ea1e7adad0ee4', 'end-id', 'aa87e149e75de05f31f28fb9309e91df', 6, true);
insert into wf_element_data values ('a33729f914943aab418ea1e7adad0ee4', 'end-name', '작업 계획서', 7, false);
insert into wf_element_data values ('a51a85edc794cb15dbf60bf98790a8b0', 'action-name', '', 0, false);
insert into wf_element_data values ('a51a85edc794cb15dbf60bf98790a8b0', 'action-value', '', 1, false);
insert into wf_element_data values ('a51a85edc794cb15dbf60bf98790a8b0', 'is-default', 'Y', 2, false);
insert into wf_element_data values ('a51a85edc794cb15dbf60bf98790a8b0', 'condition-value', '', 3, false);
insert into wf_element_data values ('a51a85edc794cb15dbf60bf98790a8b0', 'start-id', 'a1184c7bed5efd25324bc0d70b1bfdf5', 4, true);
insert into wf_element_data values ('a51a85edc794cb15dbf60bf98790a8b0', 'start-name', '', 5, false);
insert into wf_element_data values ('a51a85edc794cb15dbf60bf98790a8b0', 'end-id', 'aaa5103974ec9059469823d84d8ce109', 6, true);
insert into wf_element_data values ('a51a85edc794cb15dbf60bf98790a8b0', 'end-name', '승인요청', 7, false);
insert into wf_element_data values ('a61866d755fa520ac6fcca0245f91001', 'complete-action', '', 0, false);
insert into wf_element_data values ('a71cf915028b1242f725d2fa4ebeb711', 'action-name', '승인', 0, false);
insert into wf_element_data values ('a71cf915028b1242f725d2fa4ebeb711', 'is-default', 'N', 2, false);
insert into wf_element_data values ('a71cf915028b1242f725d2fa4ebeb711', 'condition-value', '', 3, false);
insert into wf_element_data values ('a71cf915028b1242f725d2fa4ebeb711', 'start-id', 'a8a1e1db38cdfdbaa692e14db009c224', 4, true);
insert into wf_element_data values ('a71cf915028b1242f725d2fa4ebeb711', 'start-name', '승인', 5, false);
insert into wf_element_data values ('a71cf915028b1242f725d2fa4ebeb711', 'end-id', 'a905a563d43599def0638503401bcad0', 6, true);
insert into wf_element_data values ('a71cf915028b1242f725d2fa4ebeb711', 'end-name', '', 7, false);
insert into wf_element_data values ('a71cf915028b1242f725d2fa4ebeb711', 'action-value', 'approval', 1, false);
insert into wf_element_data values ('a33729f914943aab418ea1e7adad0ee4', 'action-name', '접수', 0, false);
insert into wf_element_data values ('a29b7166bf3d1e6eb1f743433e1a222a', 'action-name', '작업계획서', 0, false);
insert into wf_element_data values ('a29b7166bf3d1e6eb1f743433e1a222a', 'action-value', 'work_plan_report', 1, false);
insert into wf_element_data values ('a33729f914943aab418ea1e7adad0ee4', 'action-value', 'accept', 1, false);
insert into wf_element_data values ('afb41eb311d9c3e7248870e304fc9789', 'action-value', '', 1, false);
insert into wf_element_data values ('afb41eb311d9c3e7248870e304fc9789', 'is-default', 'N', 2, false);
insert into wf_element_data values ('afb41eb311d9c3e7248870e304fc9789', 'condition-value', '', 3, false);
insert into wf_element_data values ('afb41eb311d9c3e7248870e304fc9789', 'start-id', 'ac339f9a492736b1cbe9aac6db3721a0', 4, true);
insert into wf_element_data values ('afb41eb311d9c3e7248870e304fc9789', 'start-name', '', 5, false);
insert into wf_element_data values ('afb41eb311d9c3e7248870e304fc9789', 'end-id', 'aeacaa70d21347699f5c311163d27085', 6, true);
insert into wf_element_data values ('afb41eb311d9c3e7248870e304fc9789', 'end-name', '접수', 7, false);
insert into wf_element_data values ('04fd8aee89814e479ffb8058b115b466', 'action-name', '변경결과등록', 0, false);
insert into wf_element_data values ('04fd8aee89814e479ffb8058b115b466', 'action-value', 'chg_result', 1, false);
insert into wf_element_data values ('04fd8aee89814e479ffb8058b115b466', 'is-default', 'N', 2, false);
insert into wf_element_data values ('04fd8aee89814e479ffb8058b115b466', 'condition-value', '', 3, false);
insert into wf_element_data values ('04fd8aee89814e479ffb8058b115b466', 'start-id', 'c9d735a5aca04835ba7f09f63f768a58', 4, true);
insert into wf_element_data values ('04fd8aee89814e479ffb8058b115b466', 'start-name', '변경결과 등록', 5, false);
insert into wf_element_data values ('04fd8aee89814e479ffb8058b115b466', 'end-id', '23bd54bbcaf642a487726b6f0dac4b4c', 6, true);
insert into wf_element_data values ('04fd8aee89814e479ffb8058b115b466', 'end-name', '승인요청', 7, false);
insert into wf_element_data values ('120af605678049f3b2d2b267231d19cc', 'action-name', '', 0, false);
insert into wf_element_data values ('120af605678049f3b2d2b267231d19cc', 'action-value', '', 1, false);
insert into wf_element_data values ('120af605678049f3b2d2b267231d19cc', 'condition-value', '', 2, false);
insert into wf_element_data values ('120af605678049f3b2d2b267231d19cc', 'end-id', 'c9d735a5aca04835ba7f09f63f768a58', 3, true);
insert into wf_element_data values ('120af605678049f3b2d2b267231d19cc', 'end-name', '변경결과 등록', 4, false);
insert into wf_element_data values ('120af605678049f3b2d2b267231d19cc', 'is-default', 'N', 5, false);
insert into wf_element_data values ('120af605678049f3b2d2b267231d19cc', 'start-id', '2e322bca68bd49e6af7015a7381b66ed', 6, true);
insert into wf_element_data values ('120af605678049f3b2d2b267231d19cc', 'start-name', '', 7, false);
insert into wf_element_data values ('131128229e894ff9afe21a2b7cc27c28', 'action-name', '승인요청', 0, false);
insert into wf_element_data values ('131128229e894ff9afe21a2b7cc27c28', 'action-value', 'chg_job_plan', 1, false);
insert into wf_element_data values ('131128229e894ff9afe21a2b7cc27c28', 'is-default', 'N', 2, false);
insert into wf_element_data values ('131128229e894ff9afe21a2b7cc27c28', 'condition-value', '', 3, false);
insert into wf_element_data values ('131128229e894ff9afe21a2b7cc27c28', 'start-id', '203038f6a6c54dec9824275a4c093b61', 4, true);
insert into wf_element_data values ('131128229e894ff9afe21a2b7cc27c28', 'start-name', '승인요청', 5, false);
insert into wf_element_data values ('131128229e894ff9afe21a2b7cc27c28', 'end-id', 'd0d6c6e94f574915bd88257467b6434c', 6, true);
insert into wf_element_data values ('131128229e894ff9afe21a2b7cc27c28', 'end-name', '승인', 7, false);
insert into wf_element_data values ('20212f1027974110ab50990680b2a0e5', 'action-name', '승인요청', 0, false);
insert into wf_element_data values ('20212f1027974110ab50990680b2a0e5', 'action-value', 'chg_result_cmt', 1, false);
insert into wf_element_data values ('20212f1027974110ab50990680b2a0e5', 'is-default', 'N', 2, false);
insert into wf_element_data values ('20212f1027974110ab50990680b2a0e5', 'condition-value', '', 3, false);
insert into wf_element_data values ('20212f1027974110ab50990680b2a0e5', 'start-id', '23bd54bbcaf642a487726b6f0dac4b4c', 4, true);
insert into wf_element_data values ('20212f1027974110ab50990680b2a0e5', 'start-name', '승인요청', 5, false);
insert into wf_element_data values ('20212f1027974110ab50990680b2a0e5', 'end-id', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 6, true);
insert into wf_element_data values ('20212f1027974110ab50990680b2a0e5', 'end-name', '승인', 7, false);
insert into wf_element_data values ('203038f6a6c54dec9824275a4c093b61', 'assignee', 'chg_manager_user', 0, true);
insert into wf_element_data values ('203038f6a6c54dec9824275a4c093b61', 'assignee-type', 'assignee.type.assignee', 1, true);
insert into wf_element_data values ('203038f6a6c54dec9824275a4c093b61', 'reject-id', '', 2, false);
insert into wf_element_data values ('203038f6a6c54dec9824275a4c093b61', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('23bd54bbcaf642a487726b6f0dac4b4c', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('23bd54bbcaf642a487726b6f0dac4b4c', 'assignee', 'chg_manager_user', 1, true);
insert into wf_element_data values ('23bd54bbcaf642a487726b6f0dac4b4c', 'reject-id', '', 2, false);
insert into wf_element_data values ('23bd54bbcaf642a487726b6f0dac4b4c', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('24a2398bc10d4021b3e55f54cc500c50', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('24a2398bc10d4021b3e55f54cc500c50', 'assignee', 'chg_manager_user', 1, true);
insert into wf_element_data values ('24a2398bc10d4021b3e55f54cc500c50', 'reject-id', '', 2, false);
insert into wf_element_data values ('24a2398bc10d4021b3e55f54cc500c50', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('328c7d65847a4089a2d7d22b1b6bd7d1', 'action-name', '', 0, false);
insert into wf_element_data values ('328c7d65847a4089a2d7d22b1b6bd7d1', 'action-value', '', 1, false);
insert into wf_element_data values ('328c7d65847a4089a2d7d22b1b6bd7d1', 'is-default', 'N', 2, false);
insert into wf_element_data values ('328c7d65847a4089a2d7d22b1b6bd7d1', 'condition-value', '', 3, false);
insert into wf_element_data values ('328c7d65847a4089a2d7d22b1b6bd7d1', 'start-id', '906a57e38491441fa61401a8964f8398', 4, true);
insert into wf_element_data values ('328c7d65847a4089a2d7d22b1b6bd7d1', 'start-name', '릴리즈관리', 5, false);
insert into wf_element_data values ('328c7d65847a4089a2d7d22b1b6bd7d1', 'end-id', '2e322bca68bd49e6af7015a7381b66ed', 6, true);
insert into wf_element_data values ('328c7d65847a4089a2d7d22b1b6bd7d1', 'end-name', '', 7, false);
insert into wf_element_data values ('5a1417623338462eaf9533a0c23f0455', 'action-name', '승인', 0, false);
insert into wf_element_data values ('5a1417623338462eaf9533a0c23f0455', 'action-value', 'chg_result_approva', 1, false);
insert into wf_element_data values ('5a1417623338462eaf9533a0c23f0455', 'is-default', 'N', 2, false);
insert into wf_element_data values ('5a1417623338462eaf9533a0c23f0455', 'condition-value', '', 3, false);
insert into wf_element_data values ('5a1417623338462eaf9533a0c23f0455', 'start-id', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 4, true);
insert into wf_element_data values ('5a1417623338462eaf9533a0c23f0455', 'start-name', '승인', 5, false);
insert into wf_element_data values ('5a1417623338462eaf9533a0c23f0455', 'end-id', 'e7ae188b6dee468babdfed470859539c', 6, true);
insert into wf_element_data values ('5a1417623338462eaf9533a0c23f0455', 'end-name', '종료', 7, false);
insert into wf_element_data values ('6b04ce8129964511a4491a67e1aedc50', 'action-name', '처리완료', 0, false);
insert into wf_element_data values ('6b04ce8129964511a4491a67e1aedc50', 'action-value', 'complete', 1, false);
insert into wf_element_data values ('6b04ce8129964511a4491a67e1aedc50', 'is-default', 'N', 2, false);
insert into wf_element_data values ('6b04ce8129964511a4491a67e1aedc50', 'condition-value', '', 3, false);
insert into wf_element_data values ('6b04ce8129964511a4491a67e1aedc50', 'start-id', '9aa8d1454f7a4a2caebaa18586338405', 4, true);
insert into wf_element_data values ('6b04ce8129964511a4491a67e1aedc50', 'start-name', '', 5, false);
insert into wf_element_data values ('6b04ce8129964511a4491a67e1aedc50', 'end-id', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 6, true);
insert into wf_element_data values ('6b04ce8129964511a4491a67e1aedc50', 'end-name', '처리완료', 7, false);
insert into wf_element_data values ('7d9dbf9ad83b4e5681c0a6839501039f', 'action-name', '릴리즈관리', 0, false);
insert into wf_element_data values ('7d9dbf9ad83b4e5681c0a6839501039f', 'action-value', 'rel_move', 1, false);
insert into wf_element_data values ('7d9dbf9ad83b4e5681c0a6839501039f', 'is-default', 'N', 2, false);
insert into wf_element_data values ('7d9dbf9ad83b4e5681c0a6839501039f', 'condition-value', '', 3, false);
insert into wf_element_data values ('7d9dbf9ad83b4e5681c0a6839501039f', 'start-id', '9aa8d1454f7a4a2caebaa18586338405', 4, true);
insert into wf_element_data values ('7d9dbf9ad83b4e5681c0a6839501039f', 'start-name', '', 5, false);
insert into wf_element_data values ('7d9dbf9ad83b4e5681c0a6839501039f', 'end-id', '906a57e38491441fa61401a8964f8398', 6, true);
insert into wf_element_data values ('7d9dbf9ad83b4e5681c0a6839501039f', 'end-name', '릴리즈관리', 7, false);
insert into wf_element_data values ('7ebf8681283b4707b4301277fb6bf9b6', 'action-name', '', 0, false);
insert into wf_element_data values ('7ebf8681283b4707b4301277fb6bf9b6', 'action-value', '', 1, false);
insert into wf_element_data values ('7ebf8681283b4707b4301277fb6bf9b6', 'is-default', 'N', 2, false);
insert into wf_element_data values ('7ebf8681283b4707b4301277fb6bf9b6', 'condition-value', '', 3, false);
insert into wf_element_data values ('7ebf8681283b4707b4301277fb6bf9b6', 'start-id', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 4, true);
insert into wf_element_data values ('7ebf8681283b4707b4301277fb6bf9b6', 'start-name', '처리완료', 5, false);
insert into wf_element_data values ('7ebf8681283b4707b4301277fb6bf9b6', 'end-id', '2e322bca68bd49e6af7015a7381b66ed', 6, true);
insert into wf_element_data values ('7ebf8681283b4707b4301277fb6bf9b6', 'end-name', '', 7, false);
insert into wf_element_data values ('80e3a384a64446e0939f44c6e0d062a2', 'action-name', '변경계획서', 0, false);
insert into wf_element_data values ('80e3a384a64446e0939f44c6e0d062a2', 'action-value', 'chg_job_plan', 1, false);
insert into wf_element_data values ('80e3a384a64446e0939f44c6e0d062a2', 'is-default', 'N', 2, false);
insert into wf_element_data values ('80e3a384a64446e0939f44c6e0d062a2', 'condition-value', '', 3, false);
insert into wf_element_data values ('80e3a384a64446e0939f44c6e0d062a2', 'start-id', 'bc2dcad9f1c441a7ac1a425beffb7772', 4, true);
insert into wf_element_data values ('80e3a384a64446e0939f44c6e0d062a2', 'start-name', '변경계획서', 5, false);
insert into wf_element_data values ('80e3a384a64446e0939f44c6e0d062a2', 'end-id', '203038f6a6c54dec9824275a4c093b61', 6, true);
insert into wf_element_data values ('80e3a384a64446e0939f44c6e0d062a2', 'end-name', '승인요청', 7, false);
insert into wf_element_data values ('906a57e38491441fa61401a8964f8398', 'sub-document-id', '4028b8817880d833017880f5cafc0004', 0, true);
insert into wf_element_data values ('9aa8d1454f7a4a2caebaa18586338405', 'condition-item', '#{action}', 0, true);
insert into wf_element_data values ('9d50c868a38e4eb5b4620a97a4c8fa95', 'action-name', '', 0, false);
insert into wf_element_data values ('9d50c868a38e4eb5b4620a97a4c8fa95', 'action-value', '', 1, false);
insert into wf_element_data values ('9d50c868a38e4eb5b4620a97a4c8fa95', 'condition-value', '', 2, false);
insert into wf_element_data values ('9d50c868a38e4eb5b4620a97a4c8fa95', 'end-id', '24a2398bc10d4021b3e55f54cc500c50', 3, true);
insert into wf_element_data values ('9d50c868a38e4eb5b4620a97a4c8fa95', 'end-name', '접수', 4, false);
insert into wf_element_data values ('9d50c868a38e4eb5b4620a97a4c8fa95', 'is-default', 'N', 5, false);
insert into wf_element_data values ('9d50c868a38e4eb5b4620a97a4c8fa95', 'start-id', '4248c2b5ca9c4b2bb61f640e80c1b3dc', 6, true);
insert into wf_element_data values ('9d50c868a38e4eb5b4620a97a4c8fa95', 'start-name', '', 7, false);
insert into wf_element_data values ('a618f89d6cbb4611aeb5e99bf9bb9df8', 'assignee', 'chg_result_approver', 0, true);
insert into wf_element_data values ('a618f89d6cbb4611aeb5e99bf9bb9df8', 'assignee-type', 'assignee.type.assignee', 1, true);
insert into wf_element_data values ('a618f89d6cbb4611aeb5e99bf9bb9df8', 'reject-id', '23bd54bbcaf642a487726b6f0dac4b4c', 2, false);
insert into wf_element_data values ('a618f89d6cbb4611aeb5e99bf9bb9df8', 'withdraw', 'Y', 3, false);
insert into wf_element_data values ('a74d06e6a768ff1626847b9ec3a84235', 'action-name', '', 0, false);
insert into wf_element_data values ('a74d06e6a768ff1626847b9ec3a84235', 'action-value', '', 1, false);
insert into wf_element_data values ('a74d06e6a768ff1626847b9ec3a84235', 'is-default', 'N', 2, false);
insert into wf_element_data values ('a74d06e6a768ff1626847b9ec3a84235', 'condition-value', '', 3, false);
insert into wf_element_data values ('a74d06e6a768ff1626847b9ec3a84235', 'start-id', 'a7f7a90781ba158a2b53052ffd59bf15', 4, true);
insert into wf_element_data values ('a74d06e6a768ff1626847b9ec3a84235', 'start-name', '처리', 5, false);
insert into wf_element_data values ('a74d06e6a768ff1626847b9ec3a84235', 'end-id', '9aa8d1454f7a4a2caebaa18586338405', 6, true);
insert into wf_element_data values ('a74d06e6a768ff1626847b9ec3a84235', 'end-name', '', 7, false);
insert into wf_element_data values ('a7f7a90781ba158a2b53052ffd59bf15', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('a7f7a90781ba158a2b53052ffd59bf15', 'assignee', 'chg_manager_user', 1, true);
insert into wf_element_data values ('a7f7a90781ba158a2b53052ffd59bf15', 'reject-id', '', 2, false);
insert into wf_element_data values ('a7f7a90781ba158a2b53052ffd59bf15', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('ab20cca4f90b42c6a6ea4394e88594e4', 'action-name', '승인', 0, false);
insert into wf_element_data values ('ab20cca4f90b42c6a6ea4394e88594e4', 'action-value', 'chg_job_plan_approver', 1, false);
insert into wf_element_data values ('ab20cca4f90b42c6a6ea4394e88594e4', 'is-default', 'N', 2, false);
insert into wf_element_data values ('ab20cca4f90b42c6a6ea4394e88594e4', 'condition-value', '', 3, false);
insert into wf_element_data values ('ab20cca4f90b42c6a6ea4394e88594e4', 'start-id', 'd0d6c6e94f574915bd88257467b6434c', 4, true);
insert into wf_element_data values ('ab20cca4f90b42c6a6ea4394e88594e4', 'start-name', '승인', 5, false);
insert into wf_element_data values ('ab20cca4f90b42c6a6ea4394e88594e4', 'end-id', 'a7f7a90781ba158a2b53052ffd59bf15', 6, true);
insert into wf_element_data values ('ab20cca4f90b42c6a6ea4394e88594e4', 'end-name', '처리', 7, false);
insert into wf_element_data values ('ab9efe0f11dd45bab9df92b4a0d5a1e0', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('ab9efe0f11dd45bab9df92b4a0d5a1e0', 'assignee', 'chg_manager_user', 1, true);
insert into wf_element_data values ('ab9efe0f11dd45bab9df92b4a0d5a1e0', 'reject-id', '', 2, false);
insert into wf_element_data values ('ab9efe0f11dd45bab9df92b4a0d5a1e0', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('bc2dcad9f1c441a7ac1a425beffb7772', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('bc2dcad9f1c441a7ac1a425beffb7772', 'assignee', 'chg_manager_user', 1, true);
insert into wf_element_data values ('bc2dcad9f1c441a7ac1a425beffb7772', 'reject-id', '', 2, false);
insert into wf_element_data values ('bc2dcad9f1c441a7ac1a425beffb7772', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('c9d735a5aca04835ba7f09f63f768a58', 'assignee', 'chg_manager_user', 0, true);
insert into wf_element_data values ('c9d735a5aca04835ba7f09f63f768a58', 'assignee-type', 'assignee.type.assignee', 1, true);
insert into wf_element_data values ('c9d735a5aca04835ba7f09f63f768a58', 'reject-id', '', 2, false);
insert into wf_element_data values ('c9d735a5aca04835ba7f09f63f768a58', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('d0d6c6e94f574915bd88257467b6434c', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('d0d6c6e94f574915bd88257467b6434c', 'assignee', 'chg_job_plan_approver', 1, true);
insert into wf_element_data values ('d0d6c6e94f574915bd88257467b6434c', 'reject-id', '203038f6a6c54dec9824275a4c093b61', 2, false);
insert into wf_element_data values ('d0d6c6e94f574915bd88257467b6434c', 'withdraw', 'Y', 3, false);
insert into wf_element_data values ('ecf774011d9c4c0ab353194c3470dfa2', 'action-name', '접수', 0, false);
insert into wf_element_data values ('ecf774011d9c4c0ab353194c3470dfa2', 'action-value', 'chg_accept', 1, false);
insert into wf_element_data values ('ecf774011d9c4c0ab353194c3470dfa2', 'is-default', 'N', 2, false);
insert into wf_element_data values ('ecf774011d9c4c0ab353194c3470dfa2', 'condition-value', '', 3, false);
insert into wf_element_data values ('ecf774011d9c4c0ab353194c3470dfa2', 'start-id', '24a2398bc10d4021b3e55f54cc500c50', 4, true);
insert into wf_element_data values ('ecf774011d9c4c0ab353194c3470dfa2', 'start-name', '접수', 5, false);
insert into wf_element_data values ('ecf774011d9c4c0ab353194c3470dfa2', 'end-id', 'bc2dcad9f1c441a7ac1a425beffb7772', 6, true);
insert into wf_element_data values ('ecf774011d9c4c0ab353194c3470dfa2', 'end-name', '변경계획서', 7, false);
insert into wf_element_data values ('a03344086e04e8796d32ac1aca535873', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('a03344086e04e8796d32ac1aca535873', 'assignee', 'rel_req_user', 1, true);
insert into wf_element_data values ('a03344086e04e8796d32ac1aca535873', 'reject-id', '', 2, false);
insert into wf_element_data values ('a03344086e04e8796d32ac1aca535873', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('a050b0dd20e9172ff66fcd28eae58721', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('a050b0dd20e9172ff66fcd28eae58721', 'assignee', 'rel_req_user', 1, true);
insert into wf_element_data values ('a050b0dd20e9172ff66fcd28eae58721', 'reject-id', '', 2, false);
insert into wf_element_data values ('a050b0dd20e9172ff66fcd28eae58721', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('a0961206778ce5c701a67eef8d70fcc2', 'sub-document-id', '4028b8817880d833017880f5cafc0004', 0, true);
insert into wf_element_data values ('a0b7e0c8b75059e6336a6cf603088a32', 'action-name', '', 0, false);
insert into wf_element_data values ('a0b7e0c8b75059e6336a6cf603088a32', 'action-value', '', 1, false);
insert into wf_element_data values ('a0b7e0c8b75059e6336a6cf603088a32', 'is-default', 'N', 2, false);
insert into wf_element_data values ('a0b7e0c8b75059e6336a6cf603088a32', 'condition-value', '', 3, false);
insert into wf_element_data values ('a0b7e0c8b75059e6336a6cf603088a32', 'start-id', 'a03344086e04e8796d32ac1aca535873', 4, true);
insert into wf_element_data values ('a0b7e0c8b75059e6336a6cf603088a32', 'start-name', '처리', 5, false);
insert into wf_element_data values ('a0b7e0c8b75059e6336a6cf603088a32', 'end-id', 'abe525539de50a9ede8f432a991777a0', 6, true);
insert into wf_element_data values ('a0b7e0c8b75059e6336a6cf603088a32', 'end-name', '', 7, false);
insert into wf_element_data values ('a144e8c56fbf610796ff294246fe618d', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('a144e8c56fbf610796ff294246fe618d', 'assignee', 'sd_manager_user', 1, true);
insert into wf_element_data values ('a144e8c56fbf610796ff294246fe618d', 'reject-id', 'a1e4e7ef954bf6807f50d49851bb9dfb', 2, false);
insert into wf_element_data values ('a144e8c56fbf610796ff294246fe618d', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('a1767e857822c6cd01140b01193ebc52', 'action-name', '', 0, false);
insert into wf_element_data values ('a1767e857822c6cd01140b01193ebc52', 'action-value', '', 1, false);
insert into wf_element_data values ('a1767e857822c6cd01140b01193ebc52', 'is-default', 'N', 2, false);
insert into wf_element_data values ('a1767e857822c6cd01140b01193ebc52', 'condition-value', '', 3, false);
insert into wf_element_data values ('a1767e857822c6cd01140b01193ebc52', 'start-id', 'a66f901d3eee16dc209632d5e5b7d75d', 4, true);
insert into wf_element_data values ('a1767e857822c6cd01140b01193ebc52', 'start-name', '', 5, false);
insert into wf_element_data values ('a1767e857822c6cd01140b01193ebc52', 'end-id', 'a050b0dd20e9172ff66fcd28eae58721', 6, true);
insert into wf_element_data values ('a1767e857822c6cd01140b01193ebc52', 'end-name', '접수', 7, false);
insert into wf_element_data values ('a1e4e7ef954bf6807f50d49851bb9dfb', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('a1e4e7ef954bf6807f50d49851bb9dfb', 'assignee', 'cha_implement_manager', 1, true);
insert into wf_element_data values ('a1e4e7ef954bf6807f50d49851bb9dfb', 'reject-id', '', 2, false);
insert into wf_element_data values ('a1e4e7ef954bf6807f50d49851bb9dfb', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('a24b4f6637307d25e413cc4ab724b16a', 'action-name', '통합테스트', 0, false);
insert into wf_element_data values ('a24b4f6637307d25e413cc4ab724b16a', 'action-value', 'cha_tester_manager', 1, false);
insert into wf_element_data values ('a24b4f6637307d25e413cc4ab724b16a', 'is-default', 'N', 2, false);
insert into wf_element_data values ('a24b4f6637307d25e413cc4ab724b16a', 'condition-value', '', 3, false);
insert into wf_element_data values ('a24b4f6637307d25e413cc4ab724b16a', 'start-id', 'a6f88189bc6eea031259fe9b2a323e64', 4, true);
insert into wf_element_data values ('a24b4f6637307d25e413cc4ab724b16a', 'start-name', '통합테스트', 5, false);
insert into wf_element_data values ('a24b4f6637307d25e413cc4ab724b16a', 'end-id', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 6, true);
insert into wf_element_data values ('a24b4f6637307d25e413cc4ab724b16a', 'end-name', '현업테스트요청', 7, false);
insert into wf_element_data values ('a2999d5ea3e6e95c2a88a4048e94e6b0', 'action-name', '개발계획서', 0, false);
insert into wf_element_data values ('a2999d5ea3e6e95c2a88a4048e94e6b0', 'action-value', 'cha_job_plan', 1, false);
insert into wf_element_data values ('a2999d5ea3e6e95c2a88a4048e94e6b0', 'is-default', 'N', 2, false);
insert into wf_element_data values ('a2999d5ea3e6e95c2a88a4048e94e6b0', 'condition-value', '', 3, false);
insert into wf_element_data values ('a2999d5ea3e6e95c2a88a4048e94e6b0', 'start-id', 'a7496301877e9d5d75fdf131d9f53bb0', 4, true);
insert into wf_element_data values ('a2999d5ea3e6e95c2a88a4048e94e6b0', 'start-name', '개발계획서', 5, false);
insert into wf_element_data values ('a2999d5ea3e6e95c2a88a4048e94e6b0', 'end-id', 'a70b423ca1fdf0f263672fb517fd21e5', 6, true);
insert into wf_element_data values ('a2999d5ea3e6e95c2a88a4048e94e6b0', 'end-name', '승인요청', 7, false);
insert into wf_element_data values ('a2a56cf5159365210509dde33ba66fdf', 'action-name', '설계검토', 0, false);
insert into wf_element_data values ('a2a56cf5159365210509dde33ba66fdf', 'action-value', 'cha_analysis_manger', 1, false);
insert into wf_element_data values ('a2a56cf5159365210509dde33ba66fdf', 'is-default', 'N', 2, false);
insert into wf_element_data values ('a2a56cf5159365210509dde33ba66fdf', 'condition-value', '', 3, false);
insert into wf_element_data values ('a2a56cf5159365210509dde33ba66fdf', 'start-id', 'ad4b094d7973b990ce47a890ce7e9732', 4, true);
insert into wf_element_data values ('a2a56cf5159365210509dde33ba66fdf', 'start-name', '설계검토', 5, false);
insert into wf_element_data values ('a2a56cf5159365210509dde33ba66fdf', 'end-id', 'a1e4e7ef954bf6807f50d49851bb9dfb', 6, true);
insert into wf_element_data values ('a2a56cf5159365210509dde33ba66fdf', 'end-name', '구현/단위테스트', 7, false);
insert into wf_element_data values ('a2c31eed801f9034c6f83edd0098a348', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('a2c31eed801f9034c6f83edd0098a348', 'assignee', 'rel_req_user', 1, true);
insert into wf_element_data values ('a2c31eed801f9034c6f83edd0098a348', 'reject-id', '', 2, false);
insert into wf_element_data values ('a2c31eed801f9034c6f83edd0098a348', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('a2e1ceeddc40fdbe1aabc7970f2ea492', 'action-name', '변경결과등록', 0, false);
insert into wf_element_data values ('a2e1ceeddc40fdbe1aabc7970f2ea492', 'action-value', 'cha_result', 1, false);
insert into wf_element_data values ('a2e1ceeddc40fdbe1aabc7970f2ea492', 'is-default', 'N', 2, false);
insert into wf_element_data values ('a2e1ceeddc40fdbe1aabc7970f2ea492', 'condition-value', '', 3, false);
insert into wf_element_data values ('a2e1ceeddc40fdbe1aabc7970f2ea492', 'start-id', 'a2c31eed801f9034c6f83edd0098a348', 4, true);
insert into wf_element_data values ('a2e1ceeddc40fdbe1aabc7970f2ea492', 'start-name', '변경결과등록', 5, false);
insert into wf_element_data values ('a2e1ceeddc40fdbe1aabc7970f2ea492', 'end-id', 'a4e5a1853ac7156dd593cdcf6282d0d5', 6, true);
insert into wf_element_data values ('a2e1ceeddc40fdbe1aabc7970f2ea492', 'end-name', '승인요청', 7, false);
insert into wf_element_data values ('a3b9cdeb28e3dfd8cd447e999fb1fa1d', 'action-name', '처리완료', 0, false);
insert into wf_element_data values ('a3b9cdeb28e3dfd8cd447e999fb1fa1d', 'action-value', 'complete', 1, false);
insert into wf_element_data values ('a3b9cdeb28e3dfd8cd447e999fb1fa1d', 'is-default', 'N', 2, false);
insert into wf_element_data values ('a3b9cdeb28e3dfd8cd447e999fb1fa1d', 'condition-value', '', 3, false);
insert into wf_element_data values ('a3b9cdeb28e3dfd8cd447e999fb1fa1d', 'start-id', 'abe525539de50a9ede8f432a991777a0', 4, true);
insert into wf_element_data values ('a3b9cdeb28e3dfd8cd447e999fb1fa1d', 'start-name', '', 5, false);
insert into wf_element_data values ('a3b9cdeb28e3dfd8cd447e999fb1fa1d', 'end-id', 'acb90e940ac03a947df7456d4f0c7967', 6, true);
insert into wf_element_data values ('a3b9cdeb28e3dfd8cd447e999fb1fa1d', 'end-name', '처리완료', 7, false);
insert into wf_element_data values ('a3d438fc9e0d635e6e7d86e203e8c5d7', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('a3d438fc9e0d635e6e7d86e203e8c5d7', 'assignee', 'sd_manager', 1, true);
insert into wf_element_data values ('a3d438fc9e0d635e6e7d86e203e8c5d7', 'reject-id', '', 2, false);
insert into wf_element_data values ('a3d438fc9e0d635e6e7d86e203e8c5d7', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('a4e5a1853ac7156dd593cdcf6282d0d5', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('a4e5a1853ac7156dd593cdcf6282d0d5', 'assignee', 'rel_req_user', 1, true);
insert into wf_element_data values ('a4e5a1853ac7156dd593cdcf6282d0d5', 'reject-id', '', 2, false);
insert into wf_element_data values ('a4e5a1853ac7156dd593cdcf6282d0d5', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('a6f88189bc6eea031259fe9b2a323e64', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('a6f88189bc6eea031259fe9b2a323e64', 'assignee', 'cha_tester_manager', 1, true);
insert into wf_element_data values ('a6f88189bc6eea031259fe9b2a323e64', 'reject-id', 'a1e4e7ef954bf6807f50d49851bb9dfb', 2, false);
insert into wf_element_data values ('a6f88189bc6eea031259fe9b2a323e64', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('a70b423ca1fdf0f263672fb517fd21e5', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('a70b423ca1fdf0f263672fb517fd21e5', 'assignee', 'rel_req_user', 1, true);
insert into wf_element_data values ('a70b423ca1fdf0f263672fb517fd21e5', 'reject-id', '', 2, false);
insert into wf_element_data values ('a70b423ca1fdf0f263672fb517fd21e5', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('a7496301877e9d5d75fdf131d9f53bb0', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('a7496301877e9d5d75fdf131d9f53bb0', 'assignee', 'rel_req_user', 1, true);
insert into wf_element_data values ('a7496301877e9d5d75fdf131d9f53bb0', 'reject-id', '', 2, false);
insert into wf_element_data values ('a7496301877e9d5d75fdf131d9f53bb0', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('a784ad9fd8658a836e6add496073295e', 'action-name', '', 0, false);
insert into wf_element_data values ('a784ad9fd8658a836e6add496073295e', 'action-value', '', 1, false);
insert into wf_element_data values ('a784ad9fd8658a836e6add496073295e', 'is-default', 'N', 2, false);
insert into wf_element_data values ('a784ad9fd8658a836e6add496073295e', 'condition-value', '', 3, false);
insert into wf_element_data values ('a784ad9fd8658a836e6add496073295e', 'start-id', 'a0961206778ce5c701a67eef8d70fcc2', 4, true);
insert into wf_element_data values ('a784ad9fd8658a836e6add496073295e', 'start-name', '릴리즈관리', 5, false);
insert into wf_element_data values ('a784ad9fd8658a836e6add496073295e', 'end-id', 'af0efa5526bc03dd8e2972d7239d6e29', 6, true);
insert into wf_element_data values ('a784ad9fd8658a836e6add496073295e', 'end-name', '', 7, false);
insert into wf_element_data values ('a78f898a761809ab5c7cdaed87831605', 'action-name', '승인요청', 0, false);
insert into wf_element_data values ('a78f898a761809ab5c7cdaed87831605', 'action-value', 'cha_job_plan', 1, false);
insert into wf_element_data values ('a78f898a761809ab5c7cdaed87831605', 'is-default', 'N', 2, false);
insert into wf_element_data values ('a78f898a761809ab5c7cdaed87831605', 'condition-value', '', 3, false);
insert into wf_element_data values ('a78f898a761809ab5c7cdaed87831605', 'start-id', 'a70b423ca1fdf0f263672fb517fd21e5', 4, true);
insert into wf_element_data values ('a78f898a761809ab5c7cdaed87831605', 'start-name', '승인요청', 5, false);
insert into wf_element_data values ('a78f898a761809ab5c7cdaed87831605', 'end-id', 'af104e036cbb3dc685df86b88beb2657', 6, true);
insert into wf_element_data values ('a78f898a761809ab5c7cdaed87831605', 'end-name', '승인', 7, false);
insert into wf_element_data values ('a8127b76277fc9e270b38bb66c15cbd6', 'action-name', '릴리즈관리', 0, false);
insert into wf_element_data values ('a8127b76277fc9e270b38bb66c15cbd6', 'action-value', 'rel_move', 1, false);
insert into wf_element_data values ('a8127b76277fc9e270b38bb66c15cbd6', 'is-default', 'N', 2, false);
insert into wf_element_data values ('a8127b76277fc9e270b38bb66c15cbd6', 'condition-value', '', 3, false);
insert into wf_element_data values ('a8127b76277fc9e270b38bb66c15cbd6', 'start-id', 'abe525539de50a9ede8f432a991777a0', 4, true);
insert into wf_element_data values ('a8127b76277fc9e270b38bb66c15cbd6', 'start-name', '', 5, false);
insert into wf_element_data values ('a8127b76277fc9e270b38bb66c15cbd6', 'end-id', 'a0961206778ce5c701a67eef8d70fcc2', 6, true);
insert into wf_element_data values ('a8127b76277fc9e270b38bb66c15cbd6', 'end-name', '릴리즈관리', 7, false);
insert into wf_element_data values ('a81e635a2ab87151638f7a4831371cf4', 'action-name', '구현/단위테스트', 0, false);
insert into wf_element_data values ('a81e635a2ab87151638f7a4831371cf4', 'action-value', 'cha_implement_manager', 1, false);
insert into wf_element_data values ('a81e635a2ab87151638f7a4831371cf4', 'is-default', 'N', 2, false);
insert into wf_element_data values ('a81e635a2ab87151638f7a4831371cf4', 'condition-value', '', 3, false);
insert into wf_element_data values ('a81e635a2ab87151638f7a4831371cf4', 'start-id', 'a1e4e7ef954bf6807f50d49851bb9dfb', 4, true);
insert into wf_element_data values ('a81e635a2ab87151638f7a4831371cf4', 'start-name', '구현/단위테스트', 5, false);
insert into wf_element_data values ('a81e635a2ab87151638f7a4831371cf4', 'end-id', 'a6f88189bc6eea031259fe9b2a323e64', 6, true);
insert into wf_element_data values ('a81e635a2ab87151638f7a4831371cf4', 'end-name', '통합테스트', 7, false);
insert into wf_element_data values ('a81f0fc380f4a1a91aab9f8e40e8ffb2', 'action-name', '승인', 0, false);
insert into wf_element_data values ('a81f0fc380f4a1a91aab9f8e40e8ffb2', 'action-value', 'cha_job_plan_approver', 1, false);
insert into wf_element_data values ('a81f0fc380f4a1a91aab9f8e40e8ffb2', 'is-default', 'N', 2, false);
insert into wf_element_data values ('a81f0fc380f4a1a91aab9f8e40e8ffb2', 'condition-value', '', 3, false);
insert into wf_element_data values ('a81f0fc380f4a1a91aab9f8e40e8ffb2', 'start-id', 'af104e036cbb3dc685df86b88beb2657', 4, true);
insert into wf_element_data values ('a81f0fc380f4a1a91aab9f8e40e8ffb2', 'start-name', '승인', 5, false);
insert into wf_element_data values ('a81f0fc380f4a1a91aab9f8e40e8ffb2', 'end-id', 'ad4b094d7973b990ce47a890ce7e9732', 6, true);
insert into wf_element_data values ('a81f0fc380f4a1a91aab9f8e40e8ffb2', 'end-name', '설계검토', 7, false);
insert into wf_element_data values ('aa2b8fd9d2b5812b3643e7a029f2fb7e', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('aa2b8fd9d2b5812b3643e7a029f2fb7e', 'assignee', 'cha_result_approver', 1, true);
insert into wf_element_data values ('aa2b8fd9d2b5812b3643e7a029f2fb7e', 'reject-id', 'a4e5a1853ac7156dd593cdcf6282d0d5', 2, false);
insert into wf_element_data values ('aa2b8fd9d2b5812b3643e7a029f2fb7e', 'withdraw', 'Y', 3, false);
insert into wf_element_data values ('aa40acc642a50794b6ca1a40847ca573', 'action-name', '', 0, false);
insert into wf_element_data values ('aa40acc642a50794b6ca1a40847ca573', 'action-value', '', 1, false);
insert into wf_element_data values ('aa40acc642a50794b6ca1a40847ca573', 'is-default', 'N', 2, false);
insert into wf_element_data values ('aa40acc642a50794b6ca1a40847ca573', 'condition-value', '', 3, false);
insert into wf_element_data values ('aa40acc642a50794b6ca1a40847ca573', 'start-id', 'af0efa5526bc03dd8e2972d7239d6e29', 4, true);
insert into wf_element_data values ('aa40acc642a50794b6ca1a40847ca573', 'start-name', '', 5, false);
insert into wf_element_data values ('aa40acc642a50794b6ca1a40847ca573', 'end-id', 'a2c31eed801f9034c6f83edd0098a348', 6, true);
insert into wf_element_data values ('aa40acc642a50794b6ca1a40847ca573', 'end-name', '변경결과등록', 7, false);
insert into wf_element_data values ('ab154b54952a199e68d63a7bb3177e0d', 'action-name', '승인요청', 0, false);
insert into wf_element_data values ('ab154b54952a199e68d63a7bb3177e0d', 'action-value', 'chg_result_cmt', 1, false);
insert into wf_element_data values ('ab154b54952a199e68d63a7bb3177e0d', 'is-default', 'N', 2, false);
insert into wf_element_data values ('ab154b54952a199e68d63a7bb3177e0d', 'condition-value', '', 3, false);
insert into wf_element_data values ('ab154b54952a199e68d63a7bb3177e0d', 'start-id', 'a4e5a1853ac7156dd593cdcf6282d0d5', 4, true);
insert into wf_element_data values ('ab154b54952a199e68d63a7bb3177e0d', 'start-name', '승인요청', 5, false);
insert into wf_element_data values ('ab154b54952a199e68d63a7bb3177e0d', 'end-id', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 6, true);
insert into wf_element_data values ('ab154b54952a199e68d63a7bb3177e0d', 'end-name', '승인', 7, false);
insert into wf_element_data values ('ab1f576b0f30af5a67777ff1be67a90b', 'condition-value', '', 0, false);
insert into wf_element_data values ('ab1f576b0f30af5a67777ff1be67a90b', 'action-name', '', 1, false);
insert into wf_element_data values ('ab1f576b0f30af5a67777ff1be67a90b', 'action-value', '', 2, false);
insert into wf_element_data values ('ab1f576b0f30af5a67777ff1be67a90b', 'is-default', 'N', 3, false);
insert into wf_element_data values ('ab1f576b0f30af5a67777ff1be67a90b', 'start-id', 'acb90e940ac03a947df7456d4f0c7967', 4, true);
insert into wf_element_data values ('ab1f576b0f30af5a67777ff1be67a90b', 'start-name', '처리완료', 5, false);
insert into wf_element_data values ('ab1f576b0f30af5a67777ff1be67a90b', 'end-id', 'af0efa5526bc03dd8e2972d7239d6e29', 6, true);
insert into wf_element_data values ('ab1f576b0f30af5a67777ff1be67a90b', 'end-name', '', 7, false);
insert into wf_element_data values ('abe525539de50a9ede8f432a991777a0', 'condition-item', '#{action}', 0, true);
insert into wf_element_data values ('acb90e940ac03a947df7456d4f0c7967', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('acb90e940ac03a947df7456d4f0c7967', 'assignee', 'rel_req_user', 1, true);
insert into wf_element_data values ('acb90e940ac03a947df7456d4f0c7967', 'reject-id', '', 2, false);
insert into wf_element_data values ('acb90e940ac03a947df7456d4f0c7967', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('ad2e01e6ad438741b5fb7f543bd4ccc4', 'action-name', '접수', 0, false);
insert into wf_element_data values ('ad2e01e6ad438741b5fb7f543bd4ccc4', 'action-value', 'chg_accept', 1, false);
insert into wf_element_data values ('ad2e01e6ad438741b5fb7f543bd4ccc4', 'is-default', 'N', 2, false);
insert into wf_element_data values ('ad2e01e6ad438741b5fb7f543bd4ccc4', 'condition-value', '', 3, false);
insert into wf_element_data values ('ad2e01e6ad438741b5fb7f543bd4ccc4', 'start-id', 'a050b0dd20e9172ff66fcd28eae58721', 4, true);
insert into wf_element_data values ('ad2e01e6ad438741b5fb7f543bd4ccc4', 'start-name', '접수', 5, false);
insert into wf_element_data values ('ad2e01e6ad438741b5fb7f543bd4ccc4', 'end-id', 'a7496301877e9d5d75fdf131d9f53bb0', 6, true);
insert into wf_element_data values ('ad2e01e6ad438741b5fb7f543bd4ccc4', 'end-name', '개발계획서', 7, false);
insert into wf_element_data values ('ad4b094d7973b990ce47a890ce7e9732', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('ad4b094d7973b990ce47a890ce7e9732', 'assignee', 'cha_analysis_manger', 1, true);
insert into wf_element_data values ('ad4b094d7973b990ce47a890ce7e9732', 'reject-id', '', 2, false);
insert into wf_element_data values ('ad4b094d7973b990ce47a890ce7e9732', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('ae011f32321d467be8285b06002a891f', 'action-name', '현업테스트', 0, false);
insert into wf_element_data values ('ae011f32321d467be8285b06002a891f', 'action-value', 'sd_manager', 1, false);
insert into wf_element_data values ('ae011f32321d467be8285b06002a891f', 'is-default', 'N', 2, false);
insert into wf_element_data values ('ae011f32321d467be8285b06002a891f', 'condition-value', '', 3, false);
insert into wf_element_data values ('ae011f32321d467be8285b06002a891f', 'start-id', 'a144e8c56fbf610796ff294246fe618d', 4, true);
insert into wf_element_data values ('ae011f32321d467be8285b06002a891f', 'start-name', '현업테스트', 5, false);
insert into wf_element_data values ('ae011f32321d467be8285b06002a891f', 'end-id', 'a03344086e04e8796d32ac1aca535873', 6, true);
insert into wf_element_data values ('ae011f32321d467be8285b06002a891f', 'end-name', '처리', 7, false);
insert into wf_element_data values ('af0efa5526bc03dd8e2972d7239d6e29', 'condition-item', '#{action}', 0, true);
insert into wf_element_data values ('af104e036cbb3dc685df86b88beb2657', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('af104e036cbb3dc685df86b88beb2657', 'assignee', 'cha_job_plan_approver', 1, true);
insert into wf_element_data values ('af104e036cbb3dc685df86b88beb2657', 'reject-id', 'a70b423ca1fdf0f263672fb517fd21e5', 2, false);
insert into wf_element_data values ('af104e036cbb3dc685df86b88beb2657', 'withdraw', 'Y', 3, false);
insert into wf_element_data values ('af7e4f166906386d39c345206a8ff1d5', 'action-name', '승인', 0, false);
insert into wf_element_data values ('af7e4f166906386d39c345206a8ff1d5', 'action-value', 'chg_result_approva', 1, false);
insert into wf_element_data values ('af7e4f166906386d39c345206a8ff1d5', 'is-default', 'N', 2, false);
insert into wf_element_data values ('af7e4f166906386d39c345206a8ff1d5', 'condition-value', '', 3, false);
insert into wf_element_data values ('af7e4f166906386d39c345206a8ff1d5', 'start-id', 'aa2b8fd9d2b5812b3643e7a029f2fb7e', 4, true);
insert into wf_element_data values ('af7e4f166906386d39c345206a8ff1d5', 'start-name', '승인', 5, false);
insert into wf_element_data values ('af7e4f166906386d39c345206a8ff1d5', 'end-id', 'a09963d298d5f89e30ca9b1ce7ebb532', 6, true);
insert into wf_element_data values ('af7e4f166906386d39c345206a8ff1d5', 'end-name', '종료', 7, false);
insert into wf_element_data values ('afccdbd546c84e6c5a791e30e014c83d', 'action-name', '현업테스트요청', 0, false);
insert into wf_element_data values ('afccdbd546c84e6c5a791e30e014c83d', 'action-value', 'sd_test_manager', 1, false);
insert into wf_element_data values ('afccdbd546c84e6c5a791e30e014c83d', 'is-default', 'N', 2, false);
insert into wf_element_data values ('afccdbd546c84e6c5a791e30e014c83d', 'condition-value', '', 3, false);
insert into wf_element_data values ('afccdbd546c84e6c5a791e30e014c83d', 'start-id', 'a3d438fc9e0d635e6e7d86e203e8c5d7', 4, true);
insert into wf_element_data values ('afccdbd546c84e6c5a791e30e014c83d', 'start-name', '현업테스트요청', 5, false);
insert into wf_element_data values ('afccdbd546c84e6c5a791e30e014c83d', 'end-id', 'a144e8c56fbf610796ff294246fe618d', 6, true);
insert into wf_element_data values ('afccdbd546c84e6c5a791e30e014c83d', 'end-name', '현업테스트', 7, false);
insert into wf_element_data values ('a2b1a92cc7dc218e600b3d94e5b55f3f', 'sub-document-id', '4028b25d78870b0901788772ffe20025', 0, true);
insert into wf_element_data values ('a4df5578630328debe1109f956aa8e64', 'action-name', '', 0, false);
insert into wf_element_data values ('a4df5578630328debe1109f956aa8e64', 'action-value', '', 1, false);
insert into wf_element_data values ('a4df5578630328debe1109f956aa8e64', 'is-default', 'N', 2, false);
insert into wf_element_data values ('a4df5578630328debe1109f956aa8e64', 'condition-value', '', 3, false);
insert into wf_element_data values ('a4df5578630328debe1109f956aa8e64', 'start-id', 'a576bed79ec1e898945a3ad14170bf5a', 4, true);
insert into wf_element_data values ('a4df5578630328debe1109f956aa8e64', 'start-name', '', 5, false);
insert into wf_element_data values ('a4df5578630328debe1109f956aa8e64', 'end-id', 'ad6b44dd201e5175ec8f9bddc0441415', 6, true);
insert into wf_element_data values ('a4df5578630328debe1109f956aa8e64', 'end-name', '승인요청', 7, false);
insert into wf_element_data values ('a6a9b76969a592d2dc32386b6aabe804', 'action-name', '', 0, false);
insert into wf_element_data values ('a6a9b76969a592d2dc32386b6aabe804', 'action-value', '', 1, false);
insert into wf_element_data values ('a6a9b76969a592d2dc32386b6aabe804', 'is-default', 'N', 2, false);
insert into wf_element_data values ('a6a9b76969a592d2dc32386b6aabe804', 'condition-value', '', 3, false);
insert into wf_element_data values ('a6a9b76969a592d2dc32386b6aabe804', 'start-id', 'ae599233b126d46d322594dc52902b29', 4, true);
insert into wf_element_data values ('a6a9b76969a592d2dc32386b6aabe804', 'start-name', '신청서작성', 5, false);
insert into wf_element_data values ('a6a9b76969a592d2dc32386b6aabe804', 'end-id', 'a8b4a594e51d22bc0412f96b2df042c8', 6, true);
insert into wf_element_data values ('a6a9b76969a592d2dc32386b6aabe804', 'end-name', '접수', 7, false);
insert into wf_element_data values ('a872caff4f0f32efb99bd23ac353fd34', 'action-name', '승인', 0, false);
insert into wf_element_data values ('a872caff4f0f32efb99bd23ac353fd34', 'action-value', 'sd_approval', 1, false);
insert into wf_element_data values ('a872caff4f0f32efb99bd23ac353fd34', 'is-default', 'N', 2, false);
insert into wf_element_data values ('a872caff4f0f32efb99bd23ac353fd34', 'condition-value', '', 3, false);
insert into wf_element_data values ('a872caff4f0f32efb99bd23ac353fd34', 'start-id', 'ac0409214a212091ee70866dd1163ca2', 4, true);
insert into wf_element_data values ('a872caff4f0f32efb99bd23ac353fd34', 'start-name', '승인', 5, false);
insert into wf_element_data values ('a5e70d31eadc438f9f88bb065e89fb88', 'action-name', '', 0, false);
insert into wf_element_data values ('a5e70d31eadc438f9f88bb065e89fb88', 'action-value', '', 1, false);
insert into wf_element_data values ('a5e70d31eadc438f9f88bb065e89fb88', 'is-default', 'N', 2, false);
insert into wf_element_data values ('a5e70d31eadc438f9f88bb065e89fb88', 'condition-value', '', 3, false);
insert into wf_element_data values ('a5e70d31eadc438f9f88bb065e89fb88', 'start-id', '3a09ff6344194ec9af9a9488edcee914', 4, true);
insert into wf_element_data values ('a5e70d31eadc438f9f88bb065e89fb88', 'start-name', '', 5, false);
insert into wf_element_data values ('a5e70d31eadc438f9f88bb065e89fb88', 'end-id', 'dff53d10bd754541993d556b15b8c019', 6, true);
insert into wf_element_data values ('a5e70d31eadc438f9f88bb065e89fb88', 'end-name', '만족도평가', 7, false);
insert into wf_element_data values ('c8285b3efe4649038cea85496b0f4548', 'action-name', '만족도 평가', 0, false);
insert into wf_element_data values ('c8285b3efe4649038cea85496b0f4548', 'action-value', 'progress', 1, false);
insert into wf_element_data values ('c8285b3efe4649038cea85496b0f4548', 'is-default', 'N', 2, false);
insert into wf_element_data values ('c8285b3efe4649038cea85496b0f4548', 'condition-value', '', 3, false);
insert into wf_element_data values ('c8285b3efe4649038cea85496b0f4548', 'start-id', 'dff53d10bd754541993d556b15b8c019', 4, true);
insert into wf_element_data values ('c8285b3efe4649038cea85496b0f4548', 'start-name', 'New Task', 5, false);
insert into wf_element_data values ('c8285b3efe4649038cea85496b0f4548', 'end-id', '98fe51cafe4948b2a10f47159dc6e06b', 6, true);
insert into wf_element_data values ('c8285b3efe4649038cea85496b0f4548', 'end-name', '종료', 7, false);
insert into wf_element_data values ('dff53d10bd754541993d556b15b8c019', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('dff53d10bd754541993d556b15b8c019', 'assignee', 'user', 1, true);
insert into wf_element_data values ('dff53d10bd754541993d556b15b8c019', 'reject-id', '', 2, false);
insert into wf_element_data values ('dff53d10bd754541993d556b15b8c019', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('a872caff4f0f32efb99bd23ac353fd34', 'end-id', 'ace10e7a9128597f74c8f33f4efb4af6', 6, true);
insert into wf_element_data values ('a872caff4f0f32efb99bd23ac353fd34', 'end-name', '만족도', 7, false);
insert into wf_element_data values ('a8b4a594e51d22bc0412f96b2df042c8', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('a8b4a594e51d22bc0412f96b2df042c8', 'assignee', 'sd_manager_user', 1, true);
insert into wf_element_data values ('a8b4a594e51d22bc0412f96b2df042c8', 'reject-id', 'a26d17f8cee448629486d2c222c8941b', 2, false);
insert into wf_element_data values ('a8b4a594e51d22bc0412f96b2df042c8', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('a8cc714075684188d65292c614323742', 'action-name', '승인요청', 0, false);
insert into wf_element_data values ('a8cc714075684188d65292c614323742', 'action-value', 'sd_approver', 1, false);
insert into wf_element_data values ('a8cc714075684188d65292c614323742', 'is-default', 'N', 2, false);
insert into wf_element_data values ('a8cc714075684188d65292c614323742', 'condition-value', '', 3, false);
insert into wf_element_data values ('a8cc714075684188d65292c614323742', 'start-id', 'ad6b44dd201e5175ec8f9bddc0441415', 4, true);
insert into wf_element_data values ('a8cc714075684188d65292c614323742', 'start-name', '승인요청', 5, false);
insert into wf_element_data values ('a8cc714075684188d65292c614323742', 'end-id', 'ac0409214a212091ee70866dd1163ca2', 6, true);
insert into wf_element_data values ('a8cc714075684188d65292c614323742', 'end-name', '승인', 7, false);
insert into wf_element_data values ('a90c52c504704f2e9701c1ce19905ea7', 'action-name', '', 0, false);
insert into wf_element_data values ('a90c52c504704f2e9701c1ce19905ea7', 'action-value', '', 1, false);
insert into wf_element_data values ('a90c52c504704f2e9701c1ce19905ea7', 'is-default', 'N', 2, false);
insert into wf_element_data values ('a90c52c504704f2e9701c1ce19905ea7', 'condition-value', '', 3, false);
insert into wf_element_data values ('a90c52c504704f2e9701c1ce19905ea7', 'start-id', 'ace10e7a9128597f74c8f33f4efb4af6', 4, true);
insert into wf_element_data values ('a90c52c504704f2e9701c1ce19905ea7', 'start-name', '만족도', 5, false);
insert into wf_element_data values ('a90c52c504704f2e9701c1ce19905ea7', 'end-id', 'a26d17f8cee448629486d2c222c8941b', 6, true);
insert into wf_element_data values ('a90c52c504704f2e9701c1ce19905ea7', 'end-name', '종료', 7, false);
insert into wf_element_data values ('a98da74f954267149a667854a1f37c62', 'action-name', '', 0, false);
insert into wf_element_data values ('a98da74f954267149a667854a1f37c62', 'action-value', '', 1, false);
insert into wf_element_data values ('a98da74f954267149a667854a1f37c62', 'is-default', 'N', 2, false);
insert into wf_element_data values ('a98da74f954267149a667854a1f37c62', 'condition-value', '', 3, false);
insert into wf_element_data values ('a98da74f954267149a667854a1f37c62', 'start-id', 'a2b1a92cc7dc218e600b3d94e5b55f3f', 4, true);
insert into wf_element_data values ('a98da74f954267149a667854a1f37c62', 'start-name', '인프라', 5, false);
insert into wf_element_data values ('a98da74f954267149a667854a1f37c62', 'end-id', 'a576bed79ec1e898945a3ad14170bf5a', 6, true);
insert into wf_element_data values ('a98da74f954267149a667854a1f37c62', 'end-name', '', 7, false);
insert into wf_element_data values ('a9d0ec58426d02286d11fe4e5ce6ef1f', 'action-name', '', 0, false);
insert into wf_element_data values ('a9d0ec58426d02286d11fe4e5ce6ef1f', 'action-value', '', 1, false);
insert into wf_element_data values ('a9d0ec58426d02286d11fe4e5ce6ef1f', 'is-default', 'N', 2, false);
insert into wf_element_data values ('a9d0ec58426d02286d11fe4e5ce6ef1f', 'condition-value', '', 3, false);
insert into wf_element_data values ('a9d0ec58426d02286d11fe4e5ce6ef1f', 'start-id', 'a8b4a594e51d22bc0412f96b2df042c8', 4, true);
insert into wf_element_data values ('a9d0ec58426d02286d11fe4e5ce6ef1f', 'start-name', '접수', 5, false);
insert into wf_element_data values ('a9d0ec58426d02286d11fe4e5ce6ef1f', 'end-id', 'ae0977a001af0c36ec899bda5f37a299', 6, true);
insert into wf_element_data values ('a9d0ec58426d02286d11fe4e5ce6ef1f', 'end-name', '', 7, false);
insert into wf_element_data values ('a9e771c5f8ba717f4096f883c7502309', 'action-name', '', 0, false);
insert into wf_element_data values ('a9e771c5f8ba717f4096f883c7502309', 'action-value', '', 1, false);
insert into wf_element_data values ('a9e771c5f8ba717f4096f883c7502309', 'is-default', 'N', 2, false);
insert into wf_element_data values ('a9e771c5f8ba717f4096f883c7502309', 'condition-value', '', 3, false);
insert into wf_element_data values ('a9e771c5f8ba717f4096f883c7502309', 'start-id', 'aff18768b0b37a3c29ec9803aab6c1f5', 4, true);
insert into wf_element_data values ('a9e771c5f8ba717f4096f883c7502309', 'start-name', '어플리케이션', 5, false);
insert into wf_element_data values ('a9e771c5f8ba717f4096f883c7502309', 'end-id', 'a576bed79ec1e898945a3ad14170bf5a', 6, true);
insert into wf_element_data values ('a9e771c5f8ba717f4096f883c7502309', 'end-name', '', 7, false);
insert into wf_element_data values ('aa4952e410a69f6abc62d9d29c38d317', 'action-name', '어플리케이션', 0, false);
insert into wf_element_data values ('aa4952e410a69f6abc62d9d29c38d317', 'action-value', 'app', 1, false);
insert into wf_element_data values ('aa4952e410a69f6abc62d9d29c38d317', 'is-default', 'N', 2, false);
insert into wf_element_data values ('aa4952e410a69f6abc62d9d29c38d317', 'condition-value', '', 3, false);
insert into wf_element_data values ('aa4952e410a69f6abc62d9d29c38d317', 'start-id', 'ae0977a001af0c36ec899bda5f37a299', 4, true);
insert into wf_element_data values ('aa4952e410a69f6abc62d9d29c38d317', 'start-name', '', 5, false);
insert into wf_element_data values ('aa4952e410a69f6abc62d9d29c38d317', 'end-id', 'aff18768b0b37a3c29ec9803aab6c1f5', 6, true);
insert into wf_element_data values ('aa4952e410a69f6abc62d9d29c38d317', 'end-name', '어플리케이션', 7, false);
insert into wf_element_data values ('ac0409214a212091ee70866dd1163ca2', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('ac0409214a212091ee70866dd1163ca2', 'assignee', 'sd_approver', 1, true);
insert into wf_element_data values ('ac0409214a212091ee70866dd1163ca2', 'reject-id', '4028b25d788c4f8601788c7e678a0001', 2, false);
insert into wf_element_data values ('ac0409214a212091ee70866dd1163ca2', 'withdraw', 'Y', 3, false);
insert into wf_element_data values ('ace10e7a9128597f74c8f33f4efb4af6', 'target-document-list', '4028b25d788c4f8601788c9779b60005', 0, true);
insert into wf_element_data values ('ad6b44dd201e5175ec8f9bddc0441415', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('ad6b44dd201e5175ec8f9bddc0441415', 'assignee', 'sd_manager_user', 1, true);
insert into wf_element_data values ('ad6b44dd201e5175ec8f9bddc0441415', 'reject-id', '', 2, false);
insert into wf_element_data values ('ad6b44dd201e5175ec8f9bddc0441415', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('add19d953a0c003cc3e24a745493a46d', 'action-name', '인프라', 0, false);
insert into wf_element_data values ('add19d953a0c003cc3e24a745493a46d', 'action-value', 'infra', 1, false);
insert into wf_element_data values ('add19d953a0c003cc3e24a745493a46d', 'is-default', 'N', 2, false);
insert into wf_element_data values ('add19d953a0c003cc3e24a745493a46d', 'condition-value', '', 3, false);
insert into wf_element_data values ('add19d953a0c003cc3e24a745493a46d', 'start-id', 'ae0977a001af0c36ec899bda5f37a299', 4, true);
insert into wf_element_data values ('add19d953a0c003cc3e24a745493a46d', 'start-name', '', 5, false);
insert into wf_element_data values ('add19d953a0c003cc3e24a745493a46d', 'end-id', 'a2b1a92cc7dc218e600b3d94e5b55f3f', 6, true);
insert into wf_element_data values ('add19d953a0c003cc3e24a745493a46d', 'end-name', '인프라', 7, false);
insert into wf_element_data values ('ae0977a001af0c36ec899bda5f37a299', 'condition-item', '#{action}', 0, true);
insert into wf_element_data values ('ae599233b126d46d322594dc52902b29', 'assignee-type', 'assignee.type.candidate.users', 0, true);
insert into wf_element_data values ('ae599233b126d46d322594dc52902b29', 'assignee', '0509e09412534a6e98f04ca79abb6424', 1, true);
insert into wf_element_data values ('ae599233b126d46d322594dc52902b29', 'reject-id', '', 2, false);
insert into wf_element_data values ('ae599233b126d46d322594dc52902b29', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('ae5b5af4478d83bf9d2f13b8a8aa282a', 'action-name', '', 0, false);
insert into wf_element_data values ('ae5b5af4478d83bf9d2f13b8a8aa282a', 'action-value', '', 1, false);
insert into wf_element_data values ('ae5b5af4478d83bf9d2f13b8a8aa282a', 'is-default', 'N', 2, false);
insert into wf_element_data values ('ae5b5af4478d83bf9d2f13b8a8aa282a', 'condition-value', '', 3, false);
insert into wf_element_data values ('ae5b5af4478d83bf9d2f13b8a8aa282a', 'start-id', 'acfac5054c1b2da3dff103c143f3fb01', 4, true);
insert into wf_element_data values ('ae5b5af4478d83bf9d2f13b8a8aa282a', 'start-name', '', 5, false);
insert into wf_element_data values ('ae5b5af4478d83bf9d2f13b8a8aa282a', 'end-id', 'ae599233b126d46d322594dc52902b29', 6, true);
insert into wf_element_data values ('ae5b5af4478d83bf9d2f13b8a8aa282a', 'end-name', '신청서작성', 7, false);
insert into wf_element_data values ('aff18768b0b37a3c29ec9803aab6c1f5', 'sub-document-id', '4028b25d7888a7f40178893cfe7f0002', 0, true);
/**
 * 엘리먼트타입이 ScripTask인 경우 세부 설정.
 */
DROP TABLE IF EXISTS wf_element_script_data cascade;

CREATE TABLE wf_element_script_data
(
	element_id varchar(256) NOT NULL,
    script_id varchar(256) NOT NULL,
    script_value text,
	CONSTRAINT wf_element_script_data_pk PRIMARY KEY (element_id, script_id),
	CONSTRAINT wf_element_script_data_fk FOREIGN KEY (element_id) REFERENCES wf_element (element_id)
);

COMMENT ON TABLE wf_element_script_data IS '엘리먼트(ScriptTask)세부설정';
COMMENT ON COLUMN wf_element_script_data.element_id IS '엘리먼트아이디';
COMMENT ON COLUMN wf_element_script_data.script_id IS '스크립트아이디';
COMMENT ON COLUMN wf_element_script_data.script_value IS '속성값';

insert into wf_element_script_data values ('a4aa971e1f952df93f07e932ab25fbf6', '40288ab777f04ed90177f05b5f180008', '{"action":[],"target-mapping-id":"cmdb"}');
/**
 * 문서폴더정보
 */
DROP TABLE IF EXISTS wf_folder cascade;

CREATE TABLE wf_folder
(
	folder_id varchar(128) NOT NULL,
	instance_id varchar(128) NOT NULL,
	related_type varchar(100),
	create_user_key varchar(128),
	create_dt timestamp,
	update_user_key varchar(128),
	update_dt timestamp,
	CONSTRAINT wf_folder_pk PRIMARY KEY (folder_id, instance_id),
	CONSTRAINT wf_folder_fk FOREIGN KEY (instance_id) REFERENCES wf_instance (instance_id)
);

COMMENT ON TABLE wf_folder IS '문서폴더정보';
COMMENT ON COLUMN wf_folder.folder_id IS '폴더아이디';
COMMENT ON COLUMN wf_folder.instance_id IS '인스턴스아이디';
COMMENT ON COLUMN wf_folder.related_type IS '관련타입';
COMMENT ON COLUMN wf_folder.create_user_key IS '생성자';
COMMENT ON COLUMN wf_folder.create_dt IS '생성일시';
COMMENT ON COLUMN wf_folder.update_user_key IS '수정자';
COMMENT ON COLUMN wf_folder.update_dt IS '수정일시';

/**
 * 태그
 */
DROP TABLE IF EXISTS wf_tag cascade;

CREATE TABLE wf_tag
(
	tag_id varchar(128) NOT NULL,
	tag_content varchar(256) NOT NULL,
	CONSTRAINT wf_tag_pk PRIMARY KEY (tag_id)
);

COMMENT ON TABLE wf_tag IS '태그';
COMMENT ON COLUMN wf_tag.tag_id IS '태그아이디';
COMMENT ON COLUMN wf_tag.tag_content IS '태그내용';

/**
 * 태그매핑테이블
 */
DROP TABLE IF EXISTS wf_tag_map cascade;

CREATE TABLE wf_tag_map
(
	tag_id varchar(128) NOT NULL,
	instance_id varchar(128) NOT NULL,
	CONSTRAINT wf_tag_map_pk PRIMARY KEY (tag_id, instance_id),
	CONSTRAINT wf_tag_map_fk1 FOREIGN KEY (tag_id) REFERENCES wf_tag (tag_id),
	CONSTRAINT wf_tag_map_fk2 FOREIGN KEY (instance_id) REFERENCES wf_instance (instance_id)
	
);

COMMENT ON TABLE wf_tag_map IS '태그매핑테이블';
COMMENT ON COLUMN wf_tag_map.tag_id IS '태그아이디';
COMMENT ON COLUMN wf_tag_map.instance_id IS '인스턴스아이디';
/**
 * 토큰정보
 */
DROP TABLE IF EXISTS wf_token cascade;

CREATE TABLE wf_token
(
	token_id varchar(128) NOT NULL,
	instance_id varchar(128) NOT NULL,
	element_id varchar(256) NOT NULL,
	token_start_dt timestamp,
	token_end_dt timestamp,
	token_status varchar(100) NOT NULL,
	assignee_id varchar(128),
	CONSTRAINT wf_token_pk PRIMARY KEY (token_id),
	CONSTRAINT wf_token_fk1 FOREIGN KEY (instance_id) REFERENCES wf_instance (instance_id),
	CONSTRAINT wf_token_fk2 FOREIGN KEY (element_id) REFERENCES wf_element (element_id)
);

COMMENT ON TABLE wf_token IS '토큰정보';
COMMENT ON COLUMN wf_token.token_id IS '토큰아이디';
COMMENT ON COLUMN wf_token.instance_id IS '인스턴스아이디';
COMMENT ON COLUMN wf_token.element_id IS '엘리먼트아이디';
COMMENT ON COLUMN wf_token.token_start_dt IS '토큰시작일시';
COMMENT ON COLUMN wf_token.token_end_dt IS '토큰종료일시';
COMMENT ON COLUMN wf_token.token_status IS '토큰상태';
COMMENT ON COLUMN wf_token.assignee_id IS '담당자아이디';
/**
 * 토큰데이터정보
 */
DROP TABLE IF EXISTS wf_token_data cascade;

CREATE TABLE wf_token_data
(
	token_id varchar(128) NOT NULL,
	component_id varchar(128) NOT NULL,
	value text,
	CONSTRAINT wf_token_data_pk PRIMARY KEY (token_id, component_id),
	CONSTRAINT wf_token_data_fk1 FOREIGN KEY (token_id) REFERENCES wf_token (token_id),
	CONSTRAINT wf_token_data_fk2 FOREIGN KEY (component_id) REFERENCES wf_component (component_id)
);

COMMENT ON TABLE wf_token_data IS '토큰데이터정보';
COMMENT ON COLUMN wf_token_data.token_id IS '토큰아이디';
COMMENT ON COLUMN wf_token_data.component_id IS '컴포넌트아이디';
COMMENT ON COLUMN wf_token_data.value IS '데이터';
/**
 * CMDB 속성 정보
 */
DROP TABLE IF EXISTS cmdb_attribute cascade;

CREATE TABLE cmdb_attribute
(
	attribute_id character varying(128) NOT NULL UNIQUE,
	attribute_name character varying(128),
	attribute_desc character varying(512),
	attribute_type character varying(100),
	attribute_text character varying(128),
	attribute_value text,
	create_user_key character varying(128),
	create_dt timestamp,
	update_user_key character varying(128),
	update_dt timestamp,
	CONSTRAINT cmdb_attribute_pk PRIMARY KEY (attribute_id),
	CONSTRAINT cmdb_attribute_uk UNIQUE (attribute_id)
);

COMMENT ON TABLE cmdb_attribute IS 'CMDB 속성 정보';
COMMENT ON COLUMN cmdb_attribute.attribute_id IS '속성아이디';
COMMENT ON COLUMN cmdb_attribute.attribute_name IS '속성이름';
COMMENT ON COLUMN cmdb_attribute.attribute_desc IS '속성설명';
COMMENT ON COLUMN cmdb_attribute.attribute_type IS '속성타입';
COMMENT ON COLUMN cmdb_attribute.attribute_text IS '속성라벨';
COMMENT ON COLUMN cmdb_attribute.attribute_value IS '속성세부정보';
COMMENT ON COLUMN cmdb_attribute.create_user_key IS '등록자';
COMMENT ON COLUMN cmdb_attribute.create_dt IS '등록일시';
COMMENT ON COLUMN cmdb_attribute.update_user_key IS '수정자';
COMMENT ON COLUMN cmdb_attribute.update_dt IS '수정일시';

insert into cmdb_attribute values ('072fcb3be4056095a9af82dc6505b1e8', 'Availability', '', 'dropdown', '가용성', '{"option":[{"text":"상","value":"3"},{"text":"중","value":"2"},{"text":"하","value":"1"}]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('b5f16c33ca0531087ed1b46805a9c682', 'Integrity', '', 'dropdown', '무결성', '{"option":[{"text":"상","value":"3"},{"text":"중","value":"2"},{"text":"하","value":"1"}]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('77b6112b3013a6808aeb04f80dd75360', 'Confidentiality', '', 'dropdown', '기밀성', '{"option":[{"text":"상","value":"3"},{"text":"중","value":"2"},{"text":"하","value":"1"}]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('2bb03c41cd9998e77da9b737d4fcf9ab', 'Bash Version', '', 'inputbox', 'bash 버전', '{"validate":"","required":"false","maxLength":"100","minLength":"0"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('247aa7187b335f9c4d78db5e18a2704c', 'Brand', '', 'inputbox', '브랜드', '{"validate":"","required":"false","maxLength":"100","minLength":"0"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('d47973f063130acab00b2cf203a9788b', 'CPU', '', 'inputbox', 'CPU', '{"validate":"","required":"true","maxLength":"100","minLength":"0"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('ac4f3785cdbcc149a0b92dbf00af80ef', 'Classification', '', 'inputbox', '분류', '{"validate":"","required":"true","maxLength":"100","minLength":"0"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('bde6f4eac279ac3528c9cca16d12779a', 'Database', '', 'custom-code', '데이터베이스', '{"required":"true","customCode":"40288ab777dd21b50177dd52781e0000","default":{"type":"code","value":"cmdb.db.kind.altibase|altibase"},"button":"검색"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('e613591ddea0f8c1f2457104f7cf286d', 'Equipment', '', 'inputbox', '장비명', '{"validate":"","required":"true","maxLength":"100","minLength":"0"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('99a8cf26726e907a95dad34e188cbfc8', 'Grade', '', 'dropdown', '등급', '{"option":[{"text":"1등급","value":"1"},{"text":"2등급","value":"2"},{"text":"3등급","value":"3"}]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('6ea67d6c6cb28def6b289affc6c95fd1', 'MAC', '', 'inputbox', 'MAC', '{"validate":"","required":"false","maxLength":"100","minLength":"0"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('6e247bdb7b70757e1987ae25a36c3d13', 'Host', '', 'inputbox', '호스트명', '{"validate":"","required":"true","maxLength":"100","minLength":"0"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('79a99dfa69d7d0c5c369ad4840815749', 'IP_V4', '', 'inputbox', 'IP_V4', '{"validate":"","required":"false","maxLength":"100","minLength":"0"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('932831a8e53aa6f795f608794e51e7e0', 'IP_V6', '', 'inputbox', 'IP_V6', '{"validate":"","required":"false","maxLength":"100","minLength":"0"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('58e0cd57479bbb9d8a6b2bb6012206c2', 'Installation location', '', 'inputbox', '설치장소', '{"validate":"","required":"false","maxLength":"100","minLength":"0"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('489a14a0ebdca14b6eb42cf804330145', 'Licenses', '', 'inputbox', '라이선스', '{"validate":"","required":"false","maxLength":"100","minLength":"0"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('799afe719cd0bfe38797172bb77ae5d8', 'Licensing policy', '', 'dropdown', '라이선스 정책', '{"option":[{"text":"무료","value":"free"},{"text":"유료","value":"pay"}]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('a508fbfda5d65a54b9b25dc5925d79bb', 'Manager', '', 'custom-code', '관리자', '{"required":"true","customCode":"40288a19736b46fb01736b89e46c0008","default":{"type":"session","value":"userName"},"button":"검색"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('1d1338bb6316ab81f7c6adbc77199409', 'Manufacturer', '', 'inputbox', '제조사', '{"validate":"","required":"false","maxLength":"100","minLength":"0"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('addc07e374faec9f0d6d3bbeca565886', 'OS Type', '', 'dropdown', 'OS 종류', '{"option":[{"text":"common","value":"common"},{"text":"Linux","value":"linux"},{"text":"FreeBSD","value":"freebsd"},{"text":"Solaris","value":"solaris"},{"text":"AIX","value":"aix"},{"text":"HPUX","value":"hpux"},{"text":"WinNT","value":"winnt"}]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('adaeef4046bfcd78e345ad48cbbeefa5', 'Model', '', 'inputbox', '모델명', '{"validate":"","required":"true","maxLength":"100","minLength":"0"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('ef60a5a1aa010de9b7ba2dda96107c5d', 'Processor', '', 'inputbox', 'Processor', '{"validate":"","required":"false","maxLength":"100","minLength":"0"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('d0a35c07fa9bdd919a039f1f127cd54e', 'Protection level', '', 'dropdown', '보호수준', '{"option":[{"text":"가 등급","value":"3"},{"text":"나 등급","value":"2"},{"text":"다 등급","value":"1"}]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('c1f97be1aea3fdee785ca73b751f79d8', 'Quantity', '', 'inputbox', '수량', '{"validate":"number","required":"false","maxLength":"100","minLength":"0"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('f4538a0d55c456461f1d0932fd424350', 'RAM', '', 'inputbox', 'RAM', '{"validate":"","required":"false","maxLength":"100","minLength":"0"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('42b02142dd9128e47a35b737d4fc21ad', 'Service Name', '', 'inputbox', '서비스명', '{"validate":"","required":"false","maxLength":"100","minLength":"0"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('e651113f8a452f55f50ed41956cdfb34', 'Version', '', 'inputbox', '버전', '{"validate":"","required":"false","maxLength":"100","minLength":"0"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('602b2c9216825bffc96ae69eeb73bdbc', 'introduction date', '', 'inputbox', '도입일', '{"validate":"","required":"true","maxLength":"100","minLength":"0"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('189319790e6349c7248b9f50456ed47b', 'Remarks', '', 'inputbox', '비고', '{"validate":"","required":"false","maxLength":"10000","minLength":"0"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('27caaeba596663101d55a09ec873a375', 'Status', '', 'dropdown', '상태', '{"option":[{"text":"사용","value":"use"},{"text":"미사용","value":"unused"},{"text":"AS","value":"as"}]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('734ab921484883ad7760032a008baf21', 'Version_OS_Linux', '', 'dropdown', '버전', '{"option":[{"text":"Debian","value":"debian"},{"text":"Ubuntu","value":"ubuntu"},{"text":"RedHat","value":"redHat"},{"text":"CentOs","value":"centOs"},{"text":"Fedora","value":"fedora"}]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('df0e88d216ace73e0164f3dbf7ade131', 'Version_OS_Windows', '', 'dropdown', '버전', '{"option":[{"text":"Window 2012 R2","value":"2012"},{"text":"Window 10","value":"10"},{"text":"Window 10X","value":"10x"}]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);

/**
 * CMDB 클래스 정보
 */
DROP TABLE IF EXISTS cmdb_class cascade;

CREATE TABLE cmdb_class
(
    class_id character varying(128) NOT NULL,
    class_name character varying(128) NOT NULL,
    class_desc character varying(512),
    p_class_id character varying(128),
    class_level int,
    create_user_key character varying(128),
    create_dt timestamp,
    update_user_key character varying(128),
    update_dt timestamp,
    CONSTRAINT cmdb_class_pk PRIMARY KEY (class_id),
    CONSTRAINT cmdb_class_uk UNIQUE (class_id, class_name)
);

COMMENT ON TABLE cmdb_class IS 'CMDB_클래스 정보';
COMMENT ON COLUMN cmdb_class.class_id IS '클래스아이디';
COMMENT ON COLUMN cmdb_class.class_name IS '클래스이름';
COMMENT ON COLUMN cmdb_class.class_desc IS '클래스설명';
COMMENT ON COLUMN cmdb_class.p_class_id IS '부모클래스아이디';
COMMENT ON COLUMN cmdb_class.class_level IS '클래스레벨';
COMMENT ON COLUMN cmdb_class.create_user_key IS '등록자';
COMMENT ON COLUMN cmdb_class.create_dt IS '등록일시';
COMMENT ON COLUMN cmdb_class.update_user_key IS '수정자';
COMMENT ON COLUMN cmdb_class.update_dt IS '수정일시';

insert into cmdb_class values ('root', 'root', 'root', null, 0, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_class values ('df562114ab87c066adeaea79b2e4a8a2', 'Server', '서버 Class', 'root', 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_class values ('0d51e482f1a56e1074f69b5a1bce0138', 'Network', '네트워크 Class', 'root', 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_class values ('85b3c35b31059e63aaa36ce2587ea070', 'Database', '데이터베이스 Class', 'root', 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_class values ('52905fc1ae0183698f726aec3e038148', 'Software', '소프트웨어 Class', 'root', 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_class values ('0e8dd74a27bbbf86201104e91df7ee88', 'OS', 'OS Class', 'root', 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_class values ('e6663412f62bd2d3daeeadd7a36a0b0d', 'PostgreSQL', 'PostgreSQL Class', '85b3c35b31059e63aaa36ce2587ea070', 2,'0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_class values ('40e346d210cd36229d03b403153e54ab', 'Oracle', 'ORACLE Class', '85b3c35b31059e63aaa36ce2587ea070', 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_class values ('39dbe77aa58b778064a0f4a10dd06b05', 'Linux', 'Linux Class', '0e8dd74a27bbbf86201104e91df7ee88', 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_class values ('f88ee1c29fdf9d847ba6002abc5bbf1b', 'Window', 'Window Class', '0e8dd74a27bbbf86201104e91df7ee88', 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);

/**
 * CMDB 타입 정보
 */
DROP TABLE IF EXISTS cmdb_type cascade;

CREATE TABLE cmdb_type
(
	type_id character varying(128) NOT NULL,
	p_type_id character varying(128),
	type_name character varying(128),
	type_desc character varying(512),
	type_alias character varying(128),
	type_level int,
	default_class_id character varying(128) NOT NULL,
	type_icon character varying(200),
	create_user_key character varying(128),
	create_dt timestamp,
	update_user_key character varying(128),
	update_dt timestamp,
	CONSTRAINT cmdb_type_pk PRIMARY KEY (type_id),
	CONSTRAINT cmdb_type_uk UNIQUE (type_id),
	CONSTRAINT cmdb_type_fk FOREIGN KEY (default_class_id)
      REFERENCES cmdb_class (class_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

COMMENT ON TABLE cmdb_type IS 'CMDB 타입 정보';
COMMENT ON COLUMN cmdb_type.type_id IS '타입아이디';
COMMENT ON COLUMN cmdb_type.p_type_id IS '부모타입아이디';
COMMENT ON COLUMN cmdb_type.type_name IS '타입이름';
COMMENT ON COLUMN cmdb_type.type_desc IS '타입설명';
COMMENT ON COLUMN cmdb_type.type_alias IS '타입식별자';
COMMENT ON COLUMN cmdb_type.type_level IS '타입레벨';
COMMENT ON COLUMN cmdb_type.default_class_id IS '기본클래스아이디';
COMMENT ON COLUMN cmdb_type.type_icon IS '타입아이콘';
COMMENT ON COLUMN cmdb_type.create_user_key IS '등록자';
COMMENT ON COLUMN cmdb_type.create_dt IS '등록일시';
COMMENT ON COLUMN cmdb_type.update_user_key IS '수정자';
COMMENT ON COLUMN cmdb_type.update_dt IS '수정일시';

insert into cmdb_type values ('root', null, 'ROOT', null, 'CI', 0, 'root', null, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_type values ('587b4557275bcce81664db9e12485ae2', 'root', '서버', null, 'SERVER', 1, 'df562114ab87c066adeaea79b2e4a8a2', 'image_server.png', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_type values ('f18c064040304e493f4dc7385595601f', 'root', '네트워크', null, 'NETWORK', 1, '0d51e482f1a56e1074f69b5a1bce0138', 'image_snmp.png', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_type values ('b2dac0d38b39a4f8da7b98c56e831465', 'root', '데이터베이스', null, 'DATABASE', 1, '85b3c35b31059e63aaa36ce2587ea070', null, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_type values ('b1e60cb705e329ffbb5abf18e11cc32f', 'b2dac0d38b39a4f8da7b98c56e831465', '데이터베이스_PostgresSQL', null, 'POSTGRESQL', 2, 'e6663412f62bd2d3daeeadd7a36a0b0d', 'image_postresql.png', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_type values ('4788eff6532d2232d12462c85789f595', 'b2dac0d38b39a4f8da7b98c56e831465', '데이터베이스_Oracle', null, 'ORACLE', 2, '40e346d210cd36229d03b403153e54ab', 'image_oracle.png', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_type values ('3c37bbea924b4e300a4863bc1f1d41c8', 'root', '소프트웨어', null, 'SOFTWARE', 1, '52905fc1ae0183698f726aec3e038148', null, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_type values ('237053b29c09fd365d049949a14df2c5', '3c37bbea924b4e300a4863bc1f1d41c8', 'OS', null, 'OS', 2, '0e8dd74a27bbbf86201104e91df7ee88', null, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_type values ('bd746ec53dd7b64a16157d5843360391', '237053b29c09fd365d049949a14df2c5', 'OS_Linux', null, 'LINUX', 3, '39dbe77aa58b778064a0f4a10dd06b05', 'image_linux.png', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_type values ('5c6601285d44385fb6dfcf184261aa04', '237053b29c09fd365d049949a14df2c5', 'OS_Window', null, 'WINDOW', 3, 'f88ee1c29fdf9d847ba6002abc5bbf1b', 'image_winnt.png', '0509e09412534a6e98f04ca79abb6424', now(), null, null);

/**
 * CMDB CI 정보
 */
DROP TABLE IF EXISTS cmdb_ci cascade;

CREATE TABLE cmdb_ci
(
	ci_id character varying(128) NOT NULL,
	ci_no character varying(128),
	ci_name character varying(128) NOT NULL,
	ci_status character varying(100) NOT NULL,
	type_id character varying(128) NOT NULL,
	class_id character varying(128) NOT NULL,
	ci_icon character varying(200),
	ci_desc character varying(512),
	automatic boolean DEFAULT 'false',
	instance_id character varying(128),
	create_user_key character varying(128),
	create_dt timestamp,
	update_user_key character varying(128),
	update_dt timestamp,
	CONSTRAINT cmdb_ci_pk PRIMARY KEY (ci_id),
	CONSTRAINT cmdb_ci_uk UNIQUE (ci_id),
	CONSTRAINT cmdb_ci_fk1 FOREIGN KEY (type_id)
      REFERENCES cmdb_type (type_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT cmdb_ci_fk2 FOREIGN KEY (class_id)
      REFERENCES cmdb_class (class_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT cmdb_ci_fk3 FOREIGN KEY (instance_id)
        REFERENCES wf_instance (instance_id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
);

COMMENT ON TABLE cmdb_ci IS 'CMDB CI 정보';
COMMENT ON COLUMN cmdb_ci.ci_id IS 'CI아이디';
COMMENT ON COLUMN cmdb_ci.ci_no IS '시퀀스';
COMMENT ON COLUMN cmdb_ci.ci_name IS 'CI이름';
COMMENT ON COLUMN cmdb_ci.ci_status IS 'CI상태';
COMMENT ON COLUMN cmdb_ci.type_id IS '타입아이디';
COMMENT ON COLUMN cmdb_ci.class_id IS '클래스아이디';
COMMENT ON COLUMN cmdb_ci.ci_icon IS 'CI아이콘';
COMMENT ON COLUMN cmdb_ci.ci_desc IS 'CI설명';
COMMENT ON COLUMN cmdb_ci.automatic IS '자동등록여부';
COMMENT ON COLUMN cmdb_ci.instance_id IS '인스턴스ID';
COMMENT ON COLUMN cmdb_ci.create_user_key IS '등록자';
COMMENT ON COLUMN cmdb_ci.create_dt IS '등록일시';
COMMENT ON COLUMN cmdb_ci.update_user_key IS '수정자';
COMMENT ON COLUMN cmdb_ci.update_dt IS '수정일시';

/**
 * CMDB CI 데이터
 */
DROP TABLE IF EXISTS cmdb_ci_data cascade;

CREATE TABLE cmdb_ci_data
(
	ci_id character varying(128) NOT NULL,
	attribute_id character varying(128) NOT NULL,
	value text,
	CONSTRAINT cmdb_ci_data_pk PRIMARY KEY (ci_id, attribute_id),
	CONSTRAINT cmdb_ci_data_fk1 FOREIGN KEY (ci_id)
      REFERENCES cmdb_ci (ci_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT cmdb_ci_data_fk2 FOREIGN KEY (attribute_id)
      REFERENCES cmdb_attribute (attribute_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

COMMENT ON TABLE cmdb_ci_data IS 'CMDB CI 데이터';
COMMENT ON COLUMN cmdb_ci_data.ci_id IS 'CI아이디';
COMMENT ON COLUMN cmdb_ci_data.attribute_id IS '속성아이디';
COMMENT ON COLUMN cmdb_ci_data.value IS '속성값';

/**
 * CMDB CI 정보 이력
 */
DROP TABLE IF EXISTS cmdb_ci_history cascade;

CREATE TABLE cmdb_ci_history
(
	history_id character varying(128) NOT NULL,
	ci_id character varying(128) NOT NULL,
	seq int NOT NULL,
	ci_no character varying(128),
	ci_name character varying(128),
	ci_status character varying(100),
	type_id character varying(128),
	class_id character varying(128),
	ci_icon character varying(200),
	ci_desc character varying(512),
	automatic boolean DEFAULT 'false',
	instance_id character varying(128),
    apply_dt timestamp,
	CONSTRAINT cmdb_ci_history_pk PRIMARY KEY (history_id),
	CONSTRAINT cmdb_ci_history_uk UNIQUE (history_id)
);

COMMENT ON TABLE cmdb_ci_history IS 'CMDB CI 정보 이력';
COMMENT ON COLUMN cmdb_ci_history.history_id IS '히스토리아이디';
COMMENT ON COLUMN cmdb_ci_history.ci_id IS 'CI아이디';
COMMENT ON COLUMN cmdb_ci_history.seq IS '이력시퀀스';
COMMENT ON COLUMN cmdb_ci_history.ci_no IS 'CI번호';
COMMENT ON COLUMN cmdb_ci_history.ci_name IS 'CI이름';
COMMENT ON COLUMN cmdb_ci_history.type_id IS '타입아이디';
COMMENT ON COLUMN cmdb_ci_history.class_id IS '클래스아이디';
COMMENT ON COLUMN cmdb_ci_history.ci_status IS 'CI상태';
COMMENT ON COLUMN cmdb_ci_history.ci_icon IS 'CI아이콘';
COMMENT ON COLUMN cmdb_ci_history.ci_desc IS 'CI설명';
COMMENT ON COLUMN cmdb_ci_history.automatic IS '자동등록여부';
COMMENT ON COLUMN cmdb_ci_history.instance_id IS '인스턴스ID';
COMMENT ON COLUMN cmdb_ci_history.apply_dt IS '반영일시';

/**
 * CMDB CI 속성데이터 이력
 */
DROP TABLE IF EXISTS cmdb_ci_data_history cascade;

CREATE TABLE cmdb_ci_data_history
(
	data_history_id character varying(128) NOT NULL,
	ci_id character varying(128) NOT NULL,
	seq int NOT NULL,
	attribute_id character varying(128) NOT NULL,
	attribute_name character varying(128),
	attribute_desc character varying(512),
	attribute_type character varying(100),
	attribute_text character varying(128),
	attribute_value text,
	value text,
	CONSTRAINT cmdb_ci_data_history_pk PRIMARY KEY (data_history_id),
	CONSTRAINT cmdb_ci_data_history_uk UNIQUE (data_history_id)
);

COMMENT ON TABLE cmdb_ci_data_history IS 'CMDB CI 속성데이터 이력';
COMMENT ON COLUMN cmdb_ci_data_history.data_history_id IS '데이터히스토리아이디';
COMMENT ON COLUMN cmdb_ci_data_history.ci_id IS 'CI아이디';
COMMENT ON COLUMN cmdb_ci_data_history.seq IS '이력시퀀스';
COMMENT ON COLUMN cmdb_ci_data_history.attribute_id IS '속성아이디';
COMMENT ON COLUMN cmdb_ci_data_history.attribute_name IS '속성이름';
COMMENT ON COLUMN cmdb_ci_data_history.attribute_desc IS '속성설명';
COMMENT ON COLUMN cmdb_ci_data_history.attribute_type IS '속성타입';
COMMENT ON COLUMN cmdb_ci_data_history.attribute_text IS '속성라벨';
COMMENT ON COLUMN cmdb_ci_data_history.attribute_value IS '세부속성';
COMMENT ON COLUMN cmdb_ci_data_history.value IS '속성값';

/**
 * CMDB CI 연관관계
 */
DROP TABLE IF EXISTS cmdb_ci_relation cascade;

CREATE TABLE cmdb_ci_relation
(
	relation_id character varying(128) NOT NULL,
	relation_type character varying(100),
	master_ci_id character varying(128) NOT NULL,
	slave_ci_id character varying(128) NOT NULL,
	CONSTRAINT cmdb_ci_relation_pk PRIMARY KEY (relation_id),
	CONSTRAINT cmdb_ci_relation_uk UNIQUE (relation_id),
	CONSTRAINT cmdb_ci_relation_fk1 FOREIGN KEY (master_ci_id)
      REFERENCES cmdb_ci (ci_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT cmdb_ci_relation_fk2 FOREIGN KEY (slave_ci_id)
      REFERENCES cmdb_ci (ci_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

COMMENT ON TABLE cmdb_ci_relation IS 'CMDB CI 연관관계';
COMMENT ON COLUMN cmdb_ci_relation.relation_id IS '연관관계아이디';
COMMENT ON COLUMN cmdb_ci_relation.relation_type IS '연관관계타입';
COMMENT ON COLUMN cmdb_ci_relation.master_ci_id IS 'CI아이디(Master)';
COMMENT ON COLUMN cmdb_ci_relation.slave_ci_id IS 'CI아이디(Slave)';

/**
 * CMDB CI 태그정보
 */
DROP TABLE IF EXISTS cmdb_ci_tag cascade;

CREATE TABLE cmdb_ci_tag
(
	ci_id character varying(128) NOT NULL,
	tag_id character varying(128) NOT NULL,
	tag_name character varying(128),
	CONSTRAINT cmdb_ci_tag_pk PRIMARY KEY (ci_id, tag_id),
	CONSTRAINT cmdb_ci_tag_uk UNIQUE (tag_id),
	CONSTRAINT cmdb_ci_tag_fk FOREIGN KEY (ci_id)
      REFERENCES cmdb_ci (ci_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

COMMENT ON TABLE cmdb_ci_tag IS 'CMDB_CI태그정보';
COMMENT ON COLUMN cmdb_ci_tag.ci_id IS 'CI아이디';
COMMENT ON COLUMN cmdb_ci_tag.tag_id IS '태그아이디';
COMMENT ON COLUMN cmdb_ci_tag.tag_name IS '태그이름';

/**
 * CMDB 클래스 속성 정보
 */
DROP TABLE IF EXISTS cmdb_class_attribute_map cascade;

CREATE TABLE cmdb_class_attribute_map
(
	class_id character varying(128) NOT NULL,
	attribute_id character varying(128) NOT NULL,
	attribute_order int NOT NULL,
	CONSTRAINT cmdb_class_attribute_map_pk PRIMARY KEY (class_id, attribute_id),
	CONSTRAINT cmdb_class_attribute_map_fk1 FOREIGN KEY (class_id)
      REFERENCES cmdb_class (class_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT cmdb_class_attribute_map_fk2 FOREIGN KEY (attribute_id)
      REFERENCES cmdb_attribute (attribute_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

COMMENT ON TABLE cmdb_class_attribute_map IS 'CMDB 클래스 속성 정보';
COMMENT ON COLUMN cmdb_class_attribute_map.class_id IS '클래스아이디';
COMMENT ON COLUMN cmdb_class_attribute_map.attribute_id IS '속성아이디';
COMMENT ON COLUMN cmdb_class_attribute_map.attribute_order IS '속성순서';

insert into cmdb_class_attribute_map values ('df562114ab87c066adeaea79b2e4a8a2', '602b2c9216825bffc96ae69eeb73bdbc', 1);
insert into cmdb_class_attribute_map values ('df562114ab87c066adeaea79b2e4a8a2', '1d1338bb6316ab81f7c6adbc77199409', 2);
insert into cmdb_class_attribute_map values ('df562114ab87c066adeaea79b2e4a8a2', 'e613591ddea0f8c1f2457104f7cf286d', 3);
insert into cmdb_class_attribute_map values ('df562114ab87c066adeaea79b2e4a8a2', '6e247bdb7b70757e1987ae25a36c3d13', 4);
insert into cmdb_class_attribute_map values ('df562114ab87c066adeaea79b2e4a8a2', '6ea67d6c6cb28def6b289affc6c95fd1', 5);
insert into cmdb_class_attribute_map values ('df562114ab87c066adeaea79b2e4a8a2', '79a99dfa69d7d0c5c369ad4840815749', 6);
insert into cmdb_class_attribute_map values ('df562114ab87c066adeaea79b2e4a8a2', '932831a8e53aa6f795f608794e51e7e0', 7);
insert into cmdb_class_attribute_map values ('df562114ab87c066adeaea79b2e4a8a2', 'd47973f063130acab00b2cf203a9788b', 8);
insert into cmdb_class_attribute_map values ('df562114ab87c066adeaea79b2e4a8a2', 'ef60a5a1aa010de9b7ba2dda96107c5d', 9);
insert into cmdb_class_attribute_map values ('df562114ab87c066adeaea79b2e4a8a2', 'f4538a0d55c456461f1d0932fd424350', 10);
insert into cmdb_class_attribute_map values ('df562114ab87c066adeaea79b2e4a8a2', 'a508fbfda5d65a54b9b25dc5925d79bb', 11);
insert into cmdb_class_attribute_map values ('df562114ab87c066adeaea79b2e4a8a2', '189319790e6349c7248b9f50456ed47b', 12);
insert into cmdb_class_attribute_map values ('0d51e482f1a56e1074f69b5a1bce0138', 'ac4f3785cdbcc149a0b92dbf00af80ef', 1);
insert into cmdb_class_attribute_map values ('0d51e482f1a56e1074f69b5a1bce0138', '602b2c9216825bffc96ae69eeb73bdbc', 2);
insert into cmdb_class_attribute_map values ('0d51e482f1a56e1074f69b5a1bce0138', 'adaeef4046bfcd78e345ad48cbbeefa5', 3);
insert into cmdb_class_attribute_map values ('0d51e482f1a56e1074f69b5a1bce0138', 'c1f97be1aea3fdee785ca73b751f79d8', 4);
insert into cmdb_class_attribute_map values ('0d51e482f1a56e1074f69b5a1bce0138', '58e0cd57479bbb9d8a6b2bb6012206c2', 5);
insert into cmdb_class_attribute_map values ('0d51e482f1a56e1074f69b5a1bce0138', '27caaeba596663101d55a09ec873a375', 6);
insert into cmdb_class_attribute_map values ('0d51e482f1a56e1074f69b5a1bce0138', '99a8cf26726e907a95dad34e188cbfc8', 7);
insert into cmdb_class_attribute_map values ('0d51e482f1a56e1074f69b5a1bce0138', 'd0a35c07fa9bdd919a039f1f127cd54e', 8);
insert into cmdb_class_attribute_map values ('0d51e482f1a56e1074f69b5a1bce0138', '77b6112b3013a6808aeb04f80dd75360', 9);
insert into cmdb_class_attribute_map values ('0d51e482f1a56e1074f69b5a1bce0138', 'b5f16c33ca0531087ed1b46805a9c682', 10);
insert into cmdb_class_attribute_map values ('0d51e482f1a56e1074f69b5a1bce0138', '072fcb3be4056095a9af82dc6505b1e8', 11);
insert into cmdb_class_attribute_map values ('0d51e482f1a56e1074f69b5a1bce0138', 'a508fbfda5d65a54b9b25dc5925d79bb', 12);
insert into cmdb_class_attribute_map values ('0d51e482f1a56e1074f69b5a1bce0138', '189319790e6349c7248b9f50456ed47b', 13);
insert into cmdb_class_attribute_map values ('52905fc1ae0183698f726aec3e038148', 'a508fbfda5d65a54b9b25dc5925d79bb', 1);
insert into cmdb_class_attribute_map values ('85b3c35b31059e63aaa36ce2587ea070', '799afe719cd0bfe38797172bb77ae5d8', 1);
insert into cmdb_class_attribute_map values ('85b3c35b31059e63aaa36ce2587ea070', '489a14a0ebdca14b6eb42cf804330145', 2);
insert into cmdb_class_attribute_map values ('85b3c35b31059e63aaa36ce2587ea070', 'a508fbfda5d65a54b9b25dc5925d79bb', 3);
insert into cmdb_class_attribute_map values ('85b3c35b31059e63aaa36ce2587ea070', '189319790e6349c7248b9f50456ed47b', 4);
insert into cmdb_class_attribute_map values ('e6663412f62bd2d3daeeadd7a36a0b0d', 'bde6f4eac279ac3528c9cca16d12779a', 1);
insert into cmdb_class_attribute_map values ('e6663412f62bd2d3daeeadd7a36a0b0d', 'e651113f8a452f55f50ed41956cdfb34', 2);
insert into cmdb_class_attribute_map values ('40e346d210cd36229d03b403153e54ab', 'bde6f4eac279ac3528c9cca16d12779a', 1);
insert into cmdb_class_attribute_map values ('40e346d210cd36229d03b403153e54ab', 'e651113f8a452f55f50ed41956cdfb34', 2);
insert into cmdb_class_attribute_map values ('40e346d210cd36229d03b403153e54ab', '42b02142dd9128e47a35b737d4fc21ad', 3);
insert into cmdb_class_attribute_map values ('0e8dd74a27bbbf86201104e91df7ee88', '799afe719cd0bfe38797172bb77ae5d8', 1);
insert into cmdb_class_attribute_map values ('0e8dd74a27bbbf86201104e91df7ee88', '489a14a0ebdca14b6eb42cf804330145', 2);
insert into cmdb_class_attribute_map values ('0e8dd74a27bbbf86201104e91df7ee88', 'a508fbfda5d65a54b9b25dc5925d79bb', 3);
insert into cmdb_class_attribute_map values ('0e8dd74a27bbbf86201104e91df7ee88', '189319790e6349c7248b9f50456ed47b', 4);
insert into cmdb_class_attribute_map values ('39dbe77aa58b778064a0f4a10dd06b05', 'addc07e374faec9f0d6d3bbeca565886', 1);
insert into cmdb_class_attribute_map values ('39dbe77aa58b778064a0f4a10dd06b05', '734ab921484883ad7760032a008baf21', 2);
insert into cmdb_class_attribute_map values ('39dbe77aa58b778064a0f4a10dd06b05', '2bb03c41cd9998e77da9b737d4fcf9ab', 3);
insert into cmdb_class_attribute_map values ('f88ee1c29fdf9d847ba6002abc5bbf1b', 'df0e88d216ace73e0164f3dbf7ade131', 1);

/**
 * Label 마스터 클래스
 */
DROP TABLE IF EXISTS awf_label cascade;

CREATE TABLE awf_label
(
    label_target character varying(128),
    label_target_id character varying(128),
    label_key character varying(128),
    label_value character varying(512),
    create_user_key character varying(128),
    create_dt timestamp,
    update_user_key character varying(128),
    update_dt timestamp,
    CONSTRAINT awf_label_pk UNIQUE (label_target, label_target_id, label_key)
);

COMMENT ON TABLE awf_label IS '라벨마스터';
COMMENT ON COLUMN awf_label.label_target IS '라벨링 대상 타입';
COMMENT ON COLUMN awf_label.label_target_id IS '라벨링 대상';
COMMENT ON COLUMN awf_label.label_key IS '라벨링 키값';
COMMENT ON COLUMN awf_label.label_value IS '라벨링 value 값';
COMMENT ON COLUMN awf_label.create_user_key IS '등록자';
COMMENT ON COLUMN awf_label.create_dt IS '등록일시';
COMMENT ON COLUMN awf_label.update_user_key IS '수정자';
COMMENT ON COLUMN awf_label.update_dt IS '수정일시';
/**
 * CI 컴포넌트 - CI 세부 속성 임시 테이블
 */
DROP TABLE IF EXISTS wf_component_ci_data;

CREATE TABLE wf_component_ci_data
(
    component_id varchar(128) NOT NULL,
    ci_id varchar(128) NOT NULL,
    values text,
    instance_id varchar(128),
    CONSTRAINT wf_component_ci_data_pk PRIMARY KEY (component_id, ci_id)
);

COMMENT ON TABLE wf_component_ci_data IS 'CI 컴포넌트 CI 상세 데이터';
COMMENT ON COLUMN wf_component_ci_data.component_id IS '컴포넌트아이디';
COMMENT ON COLUMN wf_component_ci_data.ci_id IS 'CI아이디';
COMMENT ON COLUMN wf_component_ci_data.values IS '세부속성 데이터';
COMMENT ON COLUMN wf_component_ci_data.instance_id IS '인스턴스아이디';
/**
 * 차트설정
 */
DROP TABLE IF EXISTS awf_chart cascade;

CREATE TABLE awf_chart
(
    chart_id character varying(128) NOT NULL,
    chart_type character varying(128) NOT NULL,
    chart_name character varying(256) NOT NULL,
    chart_desc text,
    chart_config text NOT NULL,
    create_user_key character varying(128),
    create_dt timestamp,
    update_user_key character varying(128),
    update_dt timestamp,
    CONSTRAINT awf_chart_pk PRIMARY KEY (chart_id)
);

COMMENT ON TABLE awf_chart IS '차트설정';
COMMENT ON COLUMN awf_chart.chart_id IS  '차트아이디';
COMMENT ON COLUMN awf_chart.chart_type IS '차트타입';
COMMENT ON COLUMN awf_chart.chart_name IS '차트이름';
COMMENT ON COLUMN awf_chart.chart_desc IS '차트설명';
COMMENT ON COLUMN awf_chart.chart_config IS '차트설정';
COMMENT ON COLUMN awf_chart.create_user_key IS '등록자';
COMMENT ON COLUMN awf_chart.create_dt IS '등록일시';
COMMENT ON COLUMN awf_chart.update_user_key IS '수정자';
COMMENT ON COLUMN awf_chart.update_dt IS '수정일시';

/**
 * 차트설정
 */
DROP TABLE IF EXISTS awf_scheduled_history cascade;

CREATE TABLE awf_scheduled_history
(
    history_seq bigint NOT NULL,
    task_id varchar(128) NOT NULL,
    immediately_execute boolean default false,
    execute_time timestamp,
    result boolean,
    error_message text,
    CONSTRAINT awf_scheduled_history_pk PRIMARY KEY (history_seq)
);

COMMENT ON TABLE awf_scheduled_history IS '스케줄이력정보';
COMMENT ON COLUMN awf_scheduled_history.history_seq IS '이력시퀀스';
COMMENT ON COLUMN awf_scheduled_history.task_id IS '작업아이디';
COMMENT ON COLUMN awf_scheduled_history.immediately_execute IS '즉시실행여부';
COMMENT ON COLUMN awf_scheduled_history.execute_time IS '실행시각';
COMMENT ON COLUMN awf_scheduled_history.result IS '결과';
COMMENT ON COLUMN awf_scheduled_history.error_message IS '에러메시지';

/**
 * API Token 정보
 */
DROP TABLE IF EXISTS awf_api_token cascade;

CREATE TABLE awf_api_token
(
    api_id varchar(100) NOT NULL,
    access_token varchar(128) NOT NULL,
    expires_in int,
    refresh_token varchar(128) NOT NULL,
    refresh_token_expires_in int,
    create_dt timestamp,
    request_user_id varchar(128),
    CONSTRAINT awf_api_token_pk PRIMARY KEY (api_id),
    CONSTRAINT awf_api_token_uk1 UNIQUE (access_token, refresh_token)
);

COMMENT ON TABLE awf_api_token IS 'API 토큰 정보';
COMMENT ON COLUMN awf_api_token.api_id IS 'API 아이디';
COMMENT ON COLUMN awf_api_token.access_token IS '접근 토큰';
COMMENT ON COLUMN awf_api_token.expires_in IS '접근 토큰 만료 시간(초)';
COMMENT ON COLUMN awf_api_token.refresh_token IS '리프레시 토큰';
COMMENT ON COLUMN awf_api_token.refresh_token_expires_in IS '리프레시 토큰 만료 시간(초)';
COMMENT ON COLUMN awf_api_token.create_dt IS '생성일자';
COMMENT ON COLUMN awf_api_token.request_user_id IS '요청 사용자 아이디';

/**
 * API Token 정보
 */
DROP TABLE IF EXISTS cmdb_ci_doc_relation cascade;

CREATE TABLE cmdb_ci_doc_relation
(
    ci_id character varying(128) NOT NULL,
    instance_id character varying(128) NOT NULL,
    CONSTRAINT cmdb_ci_doc_relation_pk PRIMARY KEY (ci_id, instance_id),
    CONSTRAINT cmdb_ci_doc_relation_fk1 FOREIGN KEY (ci_id)
      REFERENCES cmdb_ci (ci_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT cmdb_ci_doc_relation_fk2 FOREIGN KEY (instance_id)
      REFERENCES wf_instance (instance_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

COMMENT ON TABLE cmdb_ci_doc_relation IS 'CMDB CI 관련문서 정보';
COMMENT ON COLUMN cmdb_ci_doc_relation.ci_id IS 'CI아이디';
COMMENT ON COLUMN cmdb_ci_doc_relation.instance_id IS '인스턴스ID';

