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
insert into awf_menu values ('config.scheduler', 'config', '/scheduler/search', 6,TRUE);
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

insert into wf_form values('40288ab7772ea2e301772ea633090001','만족도 - 단순문의','만족도 단순문의 문서양식입니다.','form.status.use','0509e09412534a6e98f04ca79abb6424',now());
insert into wf_form values('40288ab7772ea2e301772ea450bb0000','서비스데스크 - 단순문의','단순문의 문서양식입니다.','form.status.use','0509e09412534a6e98f04ca79abb6424',now());
insert into wf_form values('4028b25d77ed7e6f0177ed8daba10001','구성관리','구성관리 문서 양식 입니다.','form.status.use','0509e09412534a6e98f04ca79abb6424', now());
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

insert into wf_process values('40288ab7772ea2e301772ea7ce1b0002','만족도 - 단순문의','process.status.use','만족도 단순문의 프로세스입니다.','0509e09412534a6e98f04ca79abb6424',now());
insert into wf_process values('40288ab7772ea2e301772eabb9280004','서비스데스크 - 단순문의','process.status.use','단순문의 서비스데스크 입니다.','0509e09412534a6e98f04ca79abb6424',now());
insert into wf_process values('40288ab777f04ed90177f057ca410000','구성관리','process.status.use','구성관리 프로세스입니다.','0509e09412534a6e98f04ca79abb6424', now());

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

insert into wf_document values ('40288ab7772ea2e301772ea9aba40003','만족도 - 단순문의','만족도  단순문의 입니다.','40288ab7772ea2e301772ea7ce1b0002','40288ab7772ea2e301772ea633090001','document.status.use','40288ab7772dae0301772dbca28a0004','#586872','workflow','',NULL,'0509e09412534a6e98f04ca79abb6424',now());
insert into wf_document values ('40288ab7772ea2e301772ead51fe0005','단순문의','서비스데스크 단순문의 신청서 입니다.','40288ab7772ea2e301772eabb9280004','40288ab7772ea2e301772ea450bb0000','document.status.use','40125c91714df6c325714e053c890125','#F1C40F','application-form','servicedesk.inquiry','img_document_11.png','0509e09412534a6e98f04ca79abb6424',now());
insert into wf_document values ('40288ab777f04ed90177f05f01d1000b','CI 신청서','CI를 등록/수정/삭제를 할 수 있는 구성관리 신청서 입니다.','40288ab777f04ed90177f057ca410000','4028b25d77ed7e6f0177ed8daba10001','document.status.use','40288ab777f04ed90177f05e5ad7000a','#825A2C','application-form','','img_document_06.png','0509e09412534a6e98f04ca79abb6424',now());
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

insert into wf_component values ('02775b1802734c47908fc3da0c7014a8','40288ab7772ea2e301772ea450bb0000','accordion-end','',FALSE);
insert into wf_component values ('0c262cfa44fe440bb8284978ea8554d1','40288ab7772ea2e301772ea450bb0000','divider','',FALSE);
insert into wf_component values ('1c577578d51d448299eee1c98340a223','40288ab7772ea2e301772ea450bb0000','datetime','id6',FALSE);
insert into wf_component values ('22fa2c689b9d43a986dfd34e197c7f3a','40288ab7772ea2e301772ea450bb0000','inputbox','id2',FALSE);
insert into wf_component values ('28c9a8ad4b144cfbbdc27349d855773f','40288ab7772ea2e301772ea450bb0000','datetime','id12',FALSE);
insert into wf_component values ('3132307e44ff4286b92650638afececd','40288ab7772ea2e301772ea450bb0000','fileupload','id9',FALSE);
insert into wf_component values ('3b6712c115aa4e648565a4befea8ccd3','40288ab7772ea2e301772ea450bb0000','accordion-start','',FALSE);
insert into wf_component values ('4472820c4c4949258e8af5374ca15ff3','40288ab7772ea2e301772ea450bb0000','inputbox','user',FALSE);
insert into wf_component values ('47dc200d5a564d46bd1ac5cfc1d76187','40288ab7772ea2e301772ea450bb0000','datetime','id10',FALSE);
insert into wf_component values ('4c82676d6321443a8ea6f38161ceff2e','40288ab7772ea2e301772ea450bb0000','textbox','id8',FALSE);
insert into wf_component values ('560cb3baf8f64146b8d6a5f8c3124d63','40288ab7772ea2e301772ea450bb0000','textbox','id11',FALSE);
insert into wf_component values ('5d80057a921040669b4a5be53ea57304','40288ab7772ea2e301772ea450bb0000','dropdown','id4',FALSE);
insert into wf_component values ('61b3d4683993430a9cb03ab9cb088fef','40288ab7772ea2e301772ea450bb0000','textbox','id13',FALSE);
insert into wf_component values ('786337268d7745c9ab06e54a0f01a23a','40288ab7772ea2e301772ea450bb0000','inputbox','id3',FALSE);
insert into wf_component values ('7b8c641a13624211830b9c466f217985','40288ab7772ea2e301772ea450bb0000','dropdown','id5',FALSE);
insert into wf_component values ('7ee30588d4a8430ab249fd95f9fe0890','40288ab7772ea2e301772ea450bb0000','accordion-start','',FALSE);
insert into wf_component values ('8cab0f7cd82a4873b4060d243cf1de48','40288ab7772ea2e301772ea450bb0000','datetime','id15',FALSE);
insert into wf_component values ('96a8288e0cf349b6bc4609ccd45a069c','40288ab7772ea2e301772ea450bb0000','textbox','id16',FALSE);
insert into wf_component values ('9b986ab955324ab481a464a0820d9f44','40288ab7772ea2e301772ea450bb0000','label','',FALSE);
insert into wf_component values ('9cfae02dcf344b049da289b62add4080','40288ab7772ea2e301772ea450bb0000','accordion-end','',FALSE);
insert into wf_component values ('a924fa7f6fd946298f92e2589a3a81f9','40288ab7772ea2e301772ea450bb0000','datetime','id1',FALSE);
insert into wf_component values ('bc775676f79d4a2b8c1b3a239b101ac5','40288ab7772ea2e301772ea450bb0000','accordion-end','',FALSE);
insert into wf_component values ('bffef2192206490c8ae34d1162a7013f','40288ab7772ea2e301772ea450bb0000','accordion-start','',FALSE);
insert into wf_component values ('d89d0854c1a84472af43fdc50b28c68e','40288ab7772ea2e301772ea450bb0000','inputbox','id7',TRUE);
insert into wf_component values ('d9a167efd5c2406a92adf82e6363bff9','40288ab7772ea2e301772ea450bb0000','accordion-end','',FALSE);
insert into wf_component values ('dba870b223974a3b8287f5f2537940cc','40288ab7772ea2e301772ea450bb0000','custom-code','assignee',FALSE);
insert into wf_component values ('df9e44a1f03941c7b69135382e99b52d','40288ab7772ea2e301772ea450bb0000','accordion-start','',FALSE);
insert into wf_component values ('e83b4c632b264606a56012be262ec2e9','40288ab7772ea2e301772ea450bb0000','fileupload','id14',FALSE);
insert into wf_component values ('feada594a34f4b268aab37eb9211e740','40288ab7772ea2e301772ea450bb0000','custom-code','assignee2',FALSE);
insert into wf_component values ('009d794d7c4e4f9f9aec2be0b4b8c47c','40288ab7772ea2e301772ea633090001','custom-code','assignee',FALSE);
insert into wf_component values ('012b0f6d123b4282a85366e27817d895','40288ab7772ea2e301772ea633090001','accordion-end','',FALSE);
insert into wf_component values ('025ab809164c4504a24070406228f5f0','40288ab7772ea2e301772ea633090001','textbox','',FALSE);
insert into wf_component values ('269d0173006946b0923ce1d09c6a6393','40288ab7772ea2e301772ea633090001','divider','',FALSE);
insert into wf_component values ('30f4ab97a0334e7b99e6018af7a8c35f','40288ab7772ea2e301772ea633090001','dropdown','id5',FALSE);
insert into wf_component values ('3d4a319e7c4c4e64b3bb2c0efe9f6034','40288ab7772ea2e301772ea633090001','datetime','id6',FALSE);
insert into wf_component values ('541687276b2443f0b93e3d11dc5b5737','40288ab7772ea2e301772ea633090001','dropdown','id4',FALSE);
insert into wf_component values ('6db6c1b3e25b494aafb25d2099aa15a1','40288ab7772ea2e301772ea633090001','inputbox','id7',TRUE);
insert into wf_component values ('7d59614e37564e50af3a9acd91ca8706','40288ab7772ea2e301772ea633090001','inputbox','id3',FALSE);
insert into wf_component values ('7f55f7817cc94171a5d68e6aab533403','40288ab7772ea2e301772ea633090001','textbox','id13',FALSE);
insert into wf_component values ('815ef64cab6b4750a20055f89c644f00','40288ab7772ea2e301772ea633090001','accordion-end','',FALSE);
insert into wf_component values ('81806c50ff734d5ea5ddd8ffeebe41e0','40288ab7772ea2e301772ea633090001','accordion-start','',FALSE);
insert into wf_component values ('887a8ced40b74c459c9b8c3b5fb15f07','40288ab7772ea2e301772ea633090001','radio','id15',FALSE);
insert into wf_component values ('895c1e72156e4b9f9d0bc011318f899e','40288ab7772ea2e301772ea633090001','inputbox','id2',FALSE);
insert into wf_component values ('9bde985e28cc41b098437e8eade18265','40288ab7772ea2e301772ea633090001','textbox','id8',FALSE);
insert into wf_component values ('a6ce1b0dac7f41e7b0c97ac84a6e086b','40288ab7772ea2e301772ea633090001','label','',FALSE);
insert into wf_component values ('ac12c395142045678a491164d41aff78','40288ab7772ea2e301772ea633090001','fileupload','id9',FALSE);
insert into wf_component values ('b04f7051d7674a53804f63326e07781e','40288ab7772ea2e301772ea633090001','datetime','id12',FALSE);
insert into wf_component values ('b6b12cfda11c4af8a76494e135ca2f8d','40288ab7772ea2e301772ea633090001','fileupload','id14',FALSE);
insert into wf_component values ('b6cf4e793abc40cc9093b0ea13157d12','40288ab7772ea2e301772ea633090001','accordion-start','',FALSE);
insert into wf_component values ('bd38d6e3e9014767a012e2aeaa779539','40288ab7772ea2e301772ea633090001','datetime','id1',FALSE);
insert into wf_component values ('d88c58bdb16f47feac9c286100f67faa','40288ab7772ea2e301772ea633090001','accordion-start','',FALSE);
insert into wf_component values ('e0720e3eeecd433baf321a836a8e9eb5','40288ab7772ea2e301772ea633090001','accordion-end','',FALSE);
insert into wf_component values ('e8c829ae11944852ae70d37a77648edf','40288ab7772ea2e301772ea633090001','inputbox','user',FALSE);
insert into wf_component values ('f6a6b980bc134161a09e2e25eeeee4c8','40288ab7772ea2e301772ea633090001','fileupload','id17',FALSE);
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

insert into wf_component_data values ('009d794d7c4e4f9f9aec2be0b4b8c47c','display','{"column":"10","customCode":"40288a19736b46fb01736b89e46c0008","default":"none","buttonText":"검색","order":11}');
insert into wf_component_data values ('009d794d7c4e4f9f9aec2be0b4b8c47c','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"담당자"}');
insert into wf_component_data values ('012b0f6d123b4282a85366e27817d895','label','{"size":"20","color":"rgba(63,75,86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"만족도 내역"}');
insert into wf_component_data values ('012b0f6d123b4282a85366e27817d895','display','{"startId":"81806c50ff734d5ea5ddd8ffeebe41e0","thickness":"1","color":"rgba(235, 235, 235, 1)","order":25}');
insert into wf_component_data values ('025ab809164c4504a24070406228f5f0','display','{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":23}');
insert into wf_component_data values ('025ab809164c4504a24070406228f5f0','validate','{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('025ab809164c4504a24070406228f5f0','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"평가의견"}');
insert into wf_component_data values ('02775b1802734c47908fc3da0c7014a8','label','{"size":"20","color":"rgba(40,50,56,1)","bold":"Y","italic":"N","underline":"N","align":"left","text":"요청 내역"}');
insert into wf_component_data values ('02775b1802734c47908fc3da0c7014a8','display','{"startId":"7ee30588d4a8430ab249fd95f9fe0890","thickness":"1","color":"rgba(235, 235, 235, 1)","order":15}');
insert into wf_component_data values ('0c262cfa44fe440bb8284978ea8554d1','display','{"thickness":"5","color":"rgba(88,104,114,1)","type":"solid","order":2}');
insert into wf_component_data values ('1c577578d51d448299eee1c98340a223','validate','{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('1c577578d51d448299eee1c98340a223','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"완료희망일시"}');
insert into wf_component_data values ('1c577578d51d448299eee1c98340a223','display','{"column":"10","default":"now","order":10}');
insert into wf_component_data values ('22fa2c689b9d43a986dfd34e197c7f3a','validate','{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"}');
insert into wf_component_data values ('22fa2c689b9d43a986dfd34e197c7f3a','display','{"placeholder":"","column":"10","default":"select|userName|이름","order":6}');
insert into wf_component_data values ('22fa2c689b9d43a986dfd34e197c7f3a','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"신청자"}');
insert into wf_component_data values ('269d0173006946b0923ce1d09c6a6393','display','{"thickness":"5","color":"rgba(88,104,114,1)","type":"solid","order":2}');
insert into wf_component_data values ('28c9a8ad4b144cfbbdc27349d855773f','validate','{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('28c9a8ad4b144cfbbdc27349d855773f','display','{"column":"10","default":"now","order":21}');
insert into wf_component_data values ('28c9a8ad4b144cfbbdc27349d855773f','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"처리일시"}');
insert into wf_component_data values ('30f4ab97a0334e7b99e6018af7a8c35f','display','{"column":"4","order":9}');
insert into wf_component_data values ('30f4ab97a0334e7b99e6018af7a8c35f','option','[{"seq":"1","name":"사무기기 사용문의","value":"oe"},{"seq":"2","name":"서비스요청 처리현황 문의","value":"sr"},{"seq":"3","name":"업무시스템 사용문의","value":"bs"},{"seq":"4","name":"불만접수","value":"di"},{"seq":"5","name":"기타","value":"etc"}]');
insert into wf_component_data values ('30f4ab97a0334e7b99e6018af7a8c35f','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"문의유형"}');
insert into wf_component_data values ('3132307e44ff4286b92650638afececd','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"첨부파일"}');
insert into wf_component_data values ('3132307e44ff4286b92650638afececd','display','{"column":"10","order":14}');
insert into wf_component_data values ('3b6712c115aa4e648565a4befea8ccd3','label','{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"접수 내역"}');
insert into wf_component_data values ('3b6712c115aa4e648565a4befea8ccd3','display','{"endId":"d9a167efd5c2406a92adf82e6363bff9","thickness":"1","color":"rgba(235, 235, 235, 1)","order":16}');
insert into wf_component_data values ('3d4a319e7c4c4e64b3bb2c0efe9f6034','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"완료희망일시"}');
insert into wf_component_data values ('3d4a319e7c4c4e64b3bb2c0efe9f6034','display','{"column":"10","default":"now","order":10}');
insert into wf_component_data values ('3d4a319e7c4c4e64b3bb2c0efe9f6034','validate','{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('4472820c4c4949258e8af5374ca15ff3','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"신청자아이디"}');
insert into wf_component_data values ('4472820c4c4949258e8af5374ca15ff3','validate','{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"}');
insert into wf_component_data values ('4472820c4c4949258e8af5374ca15ff3','display','{"placeholder":"","column":"10","default":"select|userId|아이디","order":5}');
insert into wf_component_data values ('47dc200d5a564d46bd1ac5cfc1d76187','display','{"column":"10","default":"now","order":17}');
insert into wf_component_data values ('47dc200d5a564d46bd1ac5cfc1d76187','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"접수일시"}');
insert into wf_component_data values ('47dc200d5a564d46bd1ac5cfc1d76187','validate','{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('4c82676d6321443a8ea6f38161ceff2e','display','{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":13}');
insert into wf_component_data values ('4c82676d6321443a8ea6f38161ceff2e','validate','{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('4c82676d6321443a8ea6f38161ceff2e','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"문의내용"}');
insert into wf_component_data values ('541687276b2443f0b93e3d11dc5b5737','option','[{"seq":"1","name":"홈페이지","value":"homepage"},{"seq":"2","name":"전자결재시스템","value":"eps"},{"seq":"3","name":"출입관리시스템","value":"acs"},{"seq":"4","name":"ERP","value":"erp"},{"seq":"5","name":"ITSM","value":"itsm"},{"seq":"6","name":"기타","value":"etd"}]');
insert into wf_component_data values ('541687276b2443f0b93e3d11dc5b5737','display','{"column":"4","order":8}');
insert into wf_component_data values ('541687276b2443f0b93e3d11dc5b5737','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"관련서비스"}');
insert into wf_component_data values ('560cb3baf8f64146b8d6a5f8c3124d63','display','{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":18}');
insert into wf_component_data values ('560cb3baf8f64146b8d6a5f8c3124d63','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"접수의견"}');
insert into wf_component_data values ('560cb3baf8f64146b8d6a5f8c3124d63','validate','{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('5d80057a921040669b4a5be53ea57304','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"관련서비스"}');
insert into wf_component_data values ('5d80057a921040669b4a5be53ea57304','option','[{"seq":"1","name":"홈페이지","value":"homepage"},{"seq":"2","name":"전자결재시스템","value":"eps"},{"seq":"3","name":"출입관리시스템","value":"acs"},{"seq":"4","name":"ERP","value":"erp"},{"seq":"5","name":"ITSM","value":"itsm"},{"seq":"6","name":"기타","value":"etd"}]');
insert into wf_component_data values ('5d80057a921040669b4a5be53ea57304','display','{"column":"4","order":8}');
insert into wf_component_data values ('61b3d4683993430a9cb03ab9cb088fef','display','{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":22}');
insert into wf_component_data values ('61b3d4683993430a9cb03ab9cb088fef','validate','{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('61b3d4683993430a9cb03ab9cb088fef','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"문의답변"}');
insert into wf_component_data values ('6db6c1b3e25b494aafb25d2099aa15a1','display','{"placeholder":"","column":"10","default":"none|","order":12}');
insert into wf_component_data values ('6db6c1b3e25b494aafb25d2099aa15a1','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"제목"}');
insert into wf_component_data values ('6db6c1b3e25b494aafb25d2099aa15a1','validate','{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"}');
insert into wf_component_data values ('786337268d7745c9ab06e54a0f01a23a','display','{"placeholder":"","column":"10","default":"select|department|부서","order":7}');
insert into wf_component_data values ('786337268d7745c9ab06e54a0f01a23a','validate','{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"}');
insert into wf_component_data values ('786337268d7745c9ab06e54a0f01a23a','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"신청부서"}');
insert into wf_component_data values ('7b8c641a13624211830b9c466f217985','option','[{"seq":"1","name":"사무기기 사용문의","value":"oe"},{"seq":"2","name":"서비스요청 처리현황 문의","value":"sr"},{"seq":"3","name":"업무시스템 사용문의","value":"bs"},{"seq":"4","name":"불만접수","value":"di"},{"seq":"5","name":"기타","value":"etc"}]');
insert into wf_component_data values ('7b8c641a13624211830b9c466f217985','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"문의유형"}');
insert into wf_component_data values ('7b8c641a13624211830b9c466f217985','display','{"column":"4","order":9}');
insert into wf_component_data values ('7d59614e37564e50af3a9acd91ca8706','display','{"placeholder":"","column":"10","default":"select|department|부서","order":7}');
insert into wf_component_data values ('7d59614e37564e50af3a9acd91ca8706','validate','{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"}');
insert into wf_component_data values ('7d59614e37564e50af3a9acd91ca8706','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"신청부서"}');
insert into wf_component_data values ('7ee30588d4a8430ab249fd95f9fe0890','display','{"endId":"02775b1802734c47908fc3da0c7014a8","thickness":"1","color":"rgba(235, 235, 235, 1)","order":3}');
insert into wf_component_data values ('7ee30588d4a8430ab249fd95f9fe0890','label','{"size":"20","color":"rgba(40,50,56,1)","bold":"Y","italic":"N","underline":"N","align":"left","text":"요청 내역"}');
insert into wf_component_data values ('7f55f7817cc94171a5d68e6aab533403','display','{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":18}');
insert into wf_component_data values ('7f55f7817cc94171a5d68e6aab533403','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"답변내용"}');
insert into wf_component_data values ('7f55f7817cc94171a5d68e6aab533403','validate','{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('815ef64cab6b4750a20055f89c644f00','label','{"size":"20","color":"rgba(40,50,56,1)","bold":"Y","italic":"N","underline":"N","align":"left","text":"요청 내역"}');
insert into wf_component_data values ('815ef64cab6b4750a20055f89c644f00','display','{"startId":"d88c58bdb16f47feac9c286100f67faa","thickness":"1","color":"rgba(235, 235, 235, 1)","order":15}');
insert into wf_component_data values ('81806c50ff734d5ea5ddd8ffeebe41e0','label','{"size":"20","color":"rgba(63,75,86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"만족도 내역"}');
insert into wf_component_data values ('81806c50ff734d5ea5ddd8ffeebe41e0','display','{"endId":"012b0f6d123b4282a85366e27817d895","thickness":"1","color":"rgba(235, 235, 235, 1)","order":21}');
insert into wf_component_data values ('887a8ced40b74c459c9b8c3b5fb15f07','display','{"column":"10","direction":"horizontal","position":"right","order":22}');
insert into wf_component_data values ('887a8ced40b74c459c9b8c3b5fb15f07','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"만족도"}');
insert into wf_component_data values ('887a8ced40b74c459c9b8c3b5fb15f07','option','[{"seq":"1","name":"매우만족","value":"5"},{"seq":"2","name":"만족","value":"4"},{"seq":"3","name":"보통","value":"3"},{"seq":"4","name":"불만족","value":"2"},{"seq":"5","name":"매우불만족","value":"1"}]');
insert into wf_component_data values ('895c1e72156e4b9f9d0bc011318f899e','validate','{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"}');
insert into wf_component_data values ('895c1e72156e4b9f9d0bc011318f899e','display','{"placeholder":"","column":"10","default":"select|userName|이름","order":6}');
insert into wf_component_data values ('895c1e72156e4b9f9d0bc011318f899e','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"신청자"}');
insert into wf_component_data values ('8cab0f7cd82a4873b4060d243cf1de48','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"승인일시"}');
insert into wf_component_data values ('8cab0f7cd82a4873b4060d243cf1de48','validate','{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('8cab0f7cd82a4873b4060d243cf1de48','display','{"column":"10","default":"now","order":27}');
insert into wf_component_data values ('96a8288e0cf349b6bc4609ccd45a069c','display','{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":28}');
insert into wf_component_data values ('96a8288e0cf349b6bc4609ccd45a069c','label','{"position":"left","column":"2","size":"16","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"승인의견"}');
insert into wf_component_data values ('96a8288e0cf349b6bc4609ccd45a069c','validate','{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('9b986ab955324ab481a464a0820d9f44','display','{"size":"40","color":"rgba(52,152,219,1)","bold":"Y","italic":"N","underline":"N","align":"left","text":"단순문의","order":1}');
insert into wf_component_data values ('9bde985e28cc41b098437e8eade18265','validate','{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('9bde985e28cc41b098437e8eade18265','display','{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":13}');
insert into wf_component_data values ('9bde985e28cc41b098437e8eade18265','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"문의내용"}');
insert into wf_component_data values ('9cfae02dcf344b049da289b62add4080','display','{"startId":"bffef2192206490c8ae34d1162a7013f","thickness":"1","color":"rgba(235, 235, 235, 1)","order":25}');
insert into wf_component_data values ('9cfae02dcf344b049da289b62add4080','label','{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"처리 내역"}');
insert into wf_component_data values ('a6ce1b0dac7f41e7b0c97ac84a6e086b','display','{"size":"40","color":"rgba(52,152,219,1)","bold":"Y","italic":"N","underline":"N","align":"left","text":"단순문의 만족도 평가","order":1}');
insert into wf_component_data values ('a924fa7f6fd946298f92e2589a3a81f9','validate','{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('a924fa7f6fd946298f92e2589a3a81f9','display','{"column":"10","default":"now","order":4}');
insert into wf_component_data values ('a924fa7f6fd946298f92e2589a3a81f9','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"신청일시"}');
insert into wf_component_data values ('ac12c395142045678a491164d41aff78','display','{"column":"10","order":14}');
insert into wf_component_data values ('ac12c395142045678a491164d41aff78','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"첨부파일"}');
insert into wf_component_data values ('b04f7051d7674a53804f63326e07781e','display','{"column":"10","default":"now","order":17}');
insert into wf_component_data values ('b04f7051d7674a53804f63326e07781e','validate','{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('b04f7051d7674a53804f63326e07781e','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"답변일시"}');
insert into wf_component_data values ('b6b12cfda11c4af8a76494e135ca2f8d','display','{"column":"10","order":19}');
insert into wf_component_data values ('b6b12cfda11c4af8a76494e135ca2f8d','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"첨부파일"}');
insert into wf_component_data values ('b6cf4e793abc40cc9093b0ea13157d12','display','{"endId":"e0720e3eeecd433baf321a836a8e9eb5","thickness":"1","color":"rgba(235, 235, 235, 1)","order":16}');
insert into wf_component_data values ('b6cf4e793abc40cc9093b0ea13157d12','label','{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"답변 내역"}');
insert into wf_component_data values ('bc775676f79d4a2b8c1b3a239b101ac5','label','{"size":"20","color":"rgba(63,75,86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"승인 내역"}');
insert into wf_component_data values ('bc775676f79d4a2b8c1b3a239b101ac5','display','{"startId":"df9e44a1f03941c7b69135382e99b52d","thickness":"1","color":"rgba(235, 235, 235, 1)","order":29}');
insert into wf_component_data values ('bd38d6e3e9014767a012e2aeaa779539','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"신청일시"}');
insert into wf_component_data values ('bd38d6e3e9014767a012e2aeaa779539','display','{"column":"10","default":"now","order":4}');
insert into wf_component_data values ('bd38d6e3e9014767a012e2aeaa779539','validate','{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('bffef2192206490c8ae34d1162a7013f','display','{"endId":"9cfae02dcf344b049da289b62add4080","thickness":"1","color":"rgba(235, 235, 235, 1)","order":20}');
insert into wf_component_data values ('bffef2192206490c8ae34d1162a7013f','label','{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"처리 내역"}');
insert into wf_component_data values ('d88c58bdb16f47feac9c286100f67faa','display','{"endId":"815ef64cab6b4750a20055f89c644f00","thickness":"1","color":"rgba(235, 235, 235, 1)","order":3}');
insert into wf_component_data values ('d88c58bdb16f47feac9c286100f67faa','label','{"size":"20","color":"rgba(40,50,56,1)","bold":"Y","italic":"N","underline":"N","align":"left","text":"요청 내역"}');
insert into wf_component_data values ('d89d0854c1a84472af43fdc50b28c68e','display','{"placeholder":"","column":"10","default":"none|","order":12}');
insert into wf_component_data values ('d89d0854c1a84472af43fdc50b28c68e','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"제목"}');
insert into wf_component_data values ('d89d0854c1a84472af43fdc50b28c68e','validate','{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"}');
insert into wf_component_data values ('d9a167efd5c2406a92adf82e6363bff9','display','{"startId":"3b6712c115aa4e648565a4befea8ccd3","thickness":"1","color":"rgba(235, 235, 235, 1)","order":19}');
insert into wf_component_data values ('d9a167efd5c2406a92adf82e6363bff9','label','{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"접수 내역"}');
insert into wf_component_data values ('dba870b223974a3b8287f5f2537940cc','display','{"column":"10","customCode":"40288a19736b46fb01736b89e46c0008","default":"none","buttonText":"검색","order":11}');
insert into wf_component_data values ('dba870b223974a3b8287f5f2537940cc','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"담당자"}');
insert into wf_component_data values ('df9e44a1f03941c7b69135382e99b52d','display','{"endId":"bc775676f79d4a2b8c1b3a239b101ac5","thickness":"1","color":"rgba(235, 235, 235, 1)","order":26}');
insert into wf_component_data values ('df9e44a1f03941c7b69135382e99b52d','label','{"size":"20","color":"rgba(63,75,86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"승인 내역"}');
insert into wf_component_data values ('e0720e3eeecd433baf321a836a8e9eb5','display','{"startId":"b6cf4e793abc40cc9093b0ea13157d12","thickness":"1","color":"rgba(235, 235, 235, 1)","order":20}');
insert into wf_component_data values ('e0720e3eeecd433baf321a836a8e9eb5','label','{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"답변 내역"}');
insert into wf_component_data values ('e83b4c632b264606a56012be262ec2e9','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"첨부파일"}');
insert into wf_component_data values ('e83b4c632b264606a56012be262ec2e9','display','{"column":"10","order":23}');
insert into wf_component_data values ('e8c829ae11944852ae70d37a77648edf','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"신청자아이디"}');
insert into wf_component_data values ('e8c829ae11944852ae70d37a77648edf','validate','{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"}');
insert into wf_component_data values ('e8c829ae11944852ae70d37a77648edf','display','{"placeholder":"","column":"10","default":"none|","order":5}');
insert into wf_component_data values ('f6a6b980bc134161a09e2e25eeeee4c8','display','{"column":"10","order":24}');
insert into wf_component_data values ('f6a6b980bc134161a09e2e25eeeee4c8','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"첨부파일"}');
insert into wf_component_data values ('feada594a34f4b268aab37eb9211e740','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"승인자"}');
insert into wf_component_data values ('feada594a34f4b268aab37eb9211e740','display','{"column":"10","customCode":"40288a19736b46fb01736b89e46c0008","default":"none","buttonText":"검색","order":24}');
insert into wf_component_data values ('a0438d6ebeb3e76391de4ca2591b891d','option','[{"seq":"1","name":"전화","value":"phone"},{"seq":"2","name":"메일","value":"mail"},{"seq":"3","name":"전자결재문서","value":"electronic "},{"seq":"4","name":"자체","value":"self"}]');
insert into wf_component_data values ('a0438d6ebeb3e76391de4ca2591b891d','display','{"column":"4","order":6}');
insert into wf_component_data values ('a0438d6ebeb3e76391de4ca2591b891d','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"접수경로"}');
insert into wf_component_data values ('a083bcc9a0679f6bc9cde83eb4205161','label','{"size":"20","color":"rgba(40,50,56,1)","bold":"Y","italic":"N","underline":"N","align":"left","text":"요청 내역"}');
insert into wf_component_data values ('a083bcc9a0679f6bc9cde83eb4205161','display','{"startId":"ae615f2ac0d145ef25d5e7253c12940d","thickness":"1","color":"rgba(235, 235, 235, 1)","order":14}');
insert into wf_component_data values ('a15a240317ee972e96532bf01836fd5c','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"신청자"}');
insert into wf_component_data values ('a15a240317ee972e96532bf01836fd5c','display','{"column":"10","customCode":"40288a19736b46fb01736b89e46c0008","default":"session|userName|이름","buttonText":"검색","order":5}');
insert into wf_component_data values ('a1709377086e44e4e319ba3c2aec93c4','display','{"column":"12","isEditable":true,"border":"rgba(235, 235, 235, 1)","order":12}');
insert into wf_component_data values ('a1709377086e44e4e319ba3c2aec93c4','header','{"size":"16","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"left"}');
insert into wf_component_data values ('a1709377086e44e4e319ba3c2aec93c4','label','{"position":"top","column":"12","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"CI 변경 내역"}');
insert into wf_component_data values ('a1bbab51ba6b903eea7834b64ff2018e','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"승인의견"}');
insert into wf_component_data values ('a1bbab51ba6b903eea7834b64ff2018e','display','{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":27}');
insert into wf_component_data values ('a1bbab51ba6b903eea7834b64ff2018e','validate','{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('a20306ee9477e23c3b460bdb459f5f89','display','{"column":"4","order":7}');
insert into wf_component_data values ('a20306ee9477e23c3b460bdb459f5f89','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"변경구분"}');
insert into wf_component_data values ('a20306ee9477e23c3b460bdb459f5f89','option','[{"seq":"1","name":"인프라 업데이트","value":"infra"},{"seq":"2","name":"릴리즈 업데이트","value":"rel"},{"seq":"3","name":"외부연동 업데이트","value":"external"},{"seq":"4","name":"신규장비 입고","value":"new"}]');
insert into wf_component_data values ('a293e254d00208aeb1afb8d78c0d0737','validate','{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('a293e254d00208aeb1afb8d78c0d0737','display','{"column":"10","default":"now","order":26}');
insert into wf_component_data values ('a293e254d00208aeb1afb8d78c0d0737','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"승인일시"}');
insert into wf_component_data values ('a2ef0bbeda3884c8111f82abeb242d6f','display','{"endId":"abc34c5409954d3c792f2209f3347cdf","thickness":"1","color":"rgba(235, 235, 235, 1)","order":15}');
insert into wf_component_data values ('a2ef0bbeda3884c8111f82abeb242d6f','label','{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"접수 내역"}');
insert into wf_component_data values ('a40b7f5aaa39c16cf44d8db83f3f2a5b','display','{"column":"10","order":22}');
insert into wf_component_data values ('a40b7f5aaa39c16cf44d8db83f3f2a5b','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"첨부파일"}');
insert into wf_component_data values ('a4c6a3dd66d0cc36d108ff8cc6904fd7','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"처리내용"}');
insert into wf_component_data values ('a4c6a3dd66d0cc36d108ff8cc6904fd7','validate','{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('a4c6a3dd66d0cc36d108ff8cc6904fd7','display','{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":21}');
insert into wf_component_data values ('a563dbfc8286f3ed75f47b8e778390fb','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"첨부파일"}');
insert into wf_component_data values ('a563dbfc8286f3ed75f47b8e778390fb','display','{"column":"10","order":13}');
insert into wf_component_data values ('a640301d7e67dc92c279f83bec7358be','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"접수일시"}');
insert into wf_component_data values ('a640301d7e67dc92c279f83bec7358be','validate','{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('a640301d7e67dc92c279f83bec7358be','display','{"column":"10","default":"now","order":16}');
insert into wf_component_data values ('a661cc2ef029e637a72124a4b7c5f32f','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"처리일시"}');
insert into wf_component_data values ('a661cc2ef029e637a72124a4b7c5f32f','display','{"column":"10","default":"now","order":20}');
insert into wf_component_data values ('a661cc2ef029e637a72124a4b7c5f32f','validate','{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('a7bc03e8abd70100a86e43338e308054','display','{"column":"10","customCode":"40288a19736b46fb01736b89e46c0008","default":"session|userName|이름","buttonText":"검색","order":9}');
insert into wf_component_data values ('a7bc03e8abd70100a86e43338e308054','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"담당자"}');
insert into wf_component_data values ('a7fb73ce3c61d3d600ab135d6a6a8684','validate','{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('a7fb73ce3c61d3d600ab135d6a6a8684','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"요청내역"}');
insert into wf_component_data values ('a7fb73ce3c61d3d600ab135d6a6a8684','display','{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":11}');
insert into wf_component_data values ('a82c5acd1b0ec0bba083d76135d39e5a','label','{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"구성 변경 처리 내역"}');
insert into wf_component_data values ('a82c5acd1b0ec0bba083d76135d39e5a','display','{"endId":"a87535522638ab7240e972d44d66ef49","thickness":"1","color":"rgba(235, 235, 235, 1)","order":19}');
insert into wf_component_data values ('a87535522638ab7240e972d44d66ef49','label','{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"구성 변경 처리 내역"}');
insert into wf_component_data values ('a87535522638ab7240e972d44d66ef49','display','{"startId":"a82c5acd1b0ec0bba083d76135d39e5a","thickness":"1","color":"rgba(235, 235, 235, 1)","order":24}');
insert into wf_component_data values ('a90620f766dedb2beebbe776eff556ec','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"신청일시"}');
insert into wf_component_data values ('a90620f766dedb2beebbe776eff556ec','validate','{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('a90620f766dedb2beebbe776eff556ec','display','{"column":"10","default":"now","order":4}');
insert into wf_component_data values ('a9611c745a6716665c5e7690b872310b','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"승인자"}');
insert into wf_component_data values ('a9611c745a6716665c5e7690b872310b','display','{"column":"10","customCode":"40288a19736b46fb01736b89e46c0008","default":"session|userName|이름","buttonText":"검색","order":23}');
insert into wf_component_data values ('a9b580dd25da55ce3ce347bcef0169d5','display','{"thickness":"5","color":"rgba(88,104,114,1)","type":"solid","order":2}');
insert into wf_component_data values ('a9ef096c157fab2ec319efcf467b568a','validate','{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"}');
insert into wf_component_data values ('a9ef096c157fab2ec319efcf467b568a','display','{"placeholder":"","column":"10","default":"none|","order":10}');
insert into wf_component_data values ('a9ef096c157fab2ec319efcf467b568a','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"제목"}');
insert into wf_component_data values ('aa54cd88560b84b902653fd90336ee1e','label','{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"승인 내역"}');
insert into wf_component_data values ('aa54cd88560b84b902653fd90336ee1e','display','{"endId":"ad5e4623a663879158834cbdddd032e5","thickness":"1","color":"rgba(235, 235, 235, 1)","order":25}');
insert into wf_component_data values ('aa5a75f1766bc691fa0c066202c776ae','validate','{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('aa5a75f1766bc691fa0c066202c776ae','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"접수의견"}');
insert into wf_component_data values ('aa5a75f1766bc691fa0c066202c776ae','display','{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":17}');
insert into wf_component_data values ('abc34c5409954d3c792f2209f3347cdf','label','{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"접수 내역"}');
insert into wf_component_data values ('abc34c5409954d3c792f2209f3347cdf','display','{"startId":"a2ef0bbeda3884c8111f82abeb242d6f","thickness":"1","color":"rgba(235, 235, 235, 1)","order":18}');
insert into wf_component_data values ('ad5e4623a663879158834cbdddd032e5','display','{"startId":"aa54cd88560b84b902653fd90336ee1e","thickness":"1","color":"rgba(235, 235, 235, 1)","order":28}');
insert into wf_component_data values ('ad5e4623a663879158834cbdddd032e5','label','{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"승인 내역"}');
insert into wf_component_data values ('ad6afd860d2cda7a78bf93f4e23c8efd','display','{"size":"40","color":"rgba(52,152,219,1)","bold":"Y","italic":"N","underline":"N","align":"left","text":"CI 신청서","order":1}');
insert into wf_component_data values ('ae615f2ac0d145ef25d5e7253c12940d','label','{"size":"20","color":"rgba(40,50,56,1)","bold":"Y","italic":"N","underline":"N","align":"left","text":"요청 내역"}');
insert into wf_component_data values ('ae615f2ac0d145ef25d5e7253c12940d','display','{"endId":"a083bcc9a0679f6bc9cde83eb4205161","thickness":"1","color":"rgba(235, 235, 235, 1)","order":3}');
insert into wf_component_data values ('aff3e77073f75d2385e2347ed996f412','display','{"column":"10","default":"now","order":8}');
insert into wf_component_data values ('aff3e77073f75d2385e2347ed996f412','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"완료희망일시"}');
insert into wf_component_data values ('aff3e77073f75d2385e2347ed996f412','validate','{"datetimeMin":"","datetimeMax":""}');
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

insert into wf_element values ('0f2df8d4e3454a4c91448307b1c6a432','40288ab7772ea2e301772ea7ce1b0002','commonStart','','',FALSE,'','{"width":40,"height":40,"position-x":120,"position-y":200}');
insert into wf_element values ('44d0ed9d60d544959c853d38d5cabec9','40288ab7772ea2e301772ea7ce1b0002','arrowConnector','만족도평가','',FALSE,'','{}');
insert into wf_element values ('602d151b76584626ab765421f067da30','40288ab7772ea2e301772ea7ce1b0002','commonEnd','종료','',FALSE,'','{"width":40,"height":40,"position-x":520,"position-y":200}');
insert into wf_element values ('68725cdd45f94bf9a9012d39cc08dc6d','40288ab7772ea2e301772ea7ce1b0002','arrowConnector','','',FALSE,'','{}');
insert into wf_element values ('6e6050b6474a40d79aef93d9ef85098a','40288ab7772ea2e301772ea7ce1b0002','userTask','만족도평가','',FALSE,'','{"width":160,"height":40,"position-x":320,"position-y":200}');
insert into wf_element values ('044f83b8d41341839c64bb52d40bfc9b','40288ab7772ea2e301772eabb9280004','userTask','신청서 작성','',FALSE,'','{"width":160,"height":40,"position-x":210,"position-y":200}');
insert into wf_element values ('0c6c40925b924ac88808caabfb9dce76','40288ab7772ea2e301772eabb9280004','commonEnd','종료','',FALSE,'','{"width":40,"height":40,"position-x":1110,"position-y":280}');
insert into wf_element values ('1e0b12f83d2d4d3a85950b9a0f06d067','40288ab7772ea2e301772eabb9280004','manualTask','접수','',FALSE,'','{"width":160,"height":40,"position-x":630,"position-y":200}');
insert into wf_element values ('297df2c4770748e480376e0fcc903ef4','40288ab7772ea2e301772eabb9280004','arrowConnector','','',FALSE,'','{}');
insert into wf_element values ('317cf6711cae4263b549f937e30db546','40288ab7772ea2e301772eabb9280004','userTask','처리','',FALSE,'','{"width":160,"height":40,"position-x":850,"position-y":200}');
insert into wf_element values ('3bbb1af5cbe147718e512da2c1b2b458','40288ab7772ea2e301772eabb9280004','userTask','승인','',TRUE,'','{"width":160,"height":40,"position-x":850,"position-y":280}');
insert into wf_element values ('3f6b15d6d81c451ebcb26eae5e31ae17','40288ab7772ea2e301772eabb9280004','signalSend','만족도평가','',FALSE,'','{"width":40,"height":40,"position-x":1010,"position-y":280}');
insert into wf_element values ('682e1e295c3b4acc80f5f4942a38da8e','40288ab7772ea2e301772eabb9280004','arrowConnector','','',FALSE,'','{}');
insert into wf_element values ('8fbd692e22b4428fa9d3192a861a06ea','40288ab7772ea2e301772eabb9280004','userTask','신청서검토','',TRUE,'','{"width":160,"height":40,"position-x":420,"position-y":200}');
insert into wf_element values ('a5e1040405dd435f9bf5b745780a6158','40288ab7772ea2e301772eabb9280004','arrowConnector','','',FALSE,'','{}');
insert into wf_element values ('ab814067c8f243499c559676505ef790','40288ab7772ea2e301772eabb9280004','arrowConnector','','',FALSE,'','{}');
insert into wf_element values ('ae863e288a164f48aaa56b124a8d5113','40288ab7772ea2e301772eabb9280004','arrowConnector','','',FALSE,'','{}');
insert into wf_element values ('e38faeee0db140cebd714d290a3b3a7b','40288ab7772ea2e301772eabb9280004','arrowConnector','','',FALSE,'','{}');
insert into wf_element values ('e84bc306a19241cf98691491676df0ec','40288ab7772ea2e301772eabb9280004','commonStart','','',FALSE,'','{"width":40,"height":40,"position-x":70,"position-y":200}');
insert into wf_element values ('f0abc7d89fd049cbb9d31c7499118741','40288ab7772ea2e301772eabb9280004','arrowConnector','','',FALSE,'','{}');
insert into wf_element values ('01430d9b3cfe427b89f9203185e35530','40288ab777f04ed90177f057ca410000','arrowConnector','','',FALSE,'','{}');
insert into wf_element values ('1318f304ca2a420e9c6756438daa4e3a','40288ab777f04ed90177f057ca410000','arrowConnector','','',FALSE,'','{}');
insert into wf_element values ('2a8341aa1dcb4ab7ab89271020c748c8','40288ab777f04ed90177f057ca410000','arrowConnector','','',FALSE,'','{}');
insert into wf_element values ('4f296c1468f3422b8c59c97151e2c476','40288ab777f04ed90177f057ca410000','userTask','신청서 작성','',FALSE,'','{"width":160,"height":40,"position-x":210,"position-y":200}');
insert into wf_element values ('70e8f5da83584cba81bd9ff597963c4a','40288ab777f04ed90177f057ca410000','arrowConnector','','',FALSE,'','{}');
insert into wf_element values ('957ac0be921c4337999fcbca2f22a92d','40288ab777f04ed90177f057ca410000','arrowConnector','','',FALSE,'','{}');
insert into wf_element values ('a34268ba767d48e7ab7db9e7297e7300','40288ab777f04ed90177f057ca410000','userTask','구성변경 처리','',FALSE,'','{"width":160,"height":40,"position-x":850,"position-y":200}');
insert into wf_element values ('a4aa971e1f952df93f07e932ab25fbf6','40288ab777f04ed90177f057ca410000','scriptTask','CMDB 적용','',FALSE,'','{"width":160,"height":40,"position-x":1060,"position-y":280}');
insert into wf_element values ('a4bd7d4950b4226ea3dfc20bf15193ff','40288ab777f04ed90177f057ca410000','arrowConnector','','',FALSE,'','{}');
insert into wf_element values ('a508191382c644b289c01cf32bed8722','40288ab777f04ed90177f057ca410000','userTask','변경결과 승인','',TRUE,'','{"width":160,"height":40,"position-x":850,"position-y":280}');
insert into wf_element values ('a9a0c170a6ff5ae938f128a726fc2a60','40288ab777f04ed90177f057ca410000','commonEnd','종료','',FALSE,'','{"width":40,"height":40,"position-x":1210,"position-y":280}');
insert into wf_element values ('ad56214bb68e7b749f7d336e14172321','40288ab777f04ed90177f057ca410000','arrowConnector','','',FALSE,'','{}');
insert into wf_element values ('b0dd93b1c9864edc8f048713c0d07e12','40288ab777f04ed90177f057ca410000','manualTask','접수','',FALSE,'','{"width":160,"height":40,"position-x":630,"position-y":200}');
insert into wf_element values ('bff880e180164c4f9cedfab4ba282f28','40288ab777f04ed90177f057ca410000','userTask','신청서검토','',TRUE,'','{"width":160,"height":40,"position-x":420,"position-y":200}');
insert into wf_element values ('d099b9ac855a4e7f9a641906549dbcb0','40288ab777f04ed90177f057ca410000','commonStart','','',FALSE,'','{"width":40,"height":40,"position-x":70,"position-y":200}');
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

insert into wf_document_display values ('40288ab7772ea2e301772ea9aba40003','009d794d7c4e4f9f9aec2be0b4b8c47c','6e6050b6474a40d79aef93d9ef85098a','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ea9aba40003','012b0f6d123b4282a85366e27817d895','6e6050b6474a40d79aef93d9ef85098a','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ea9aba40003','025ab809164c4504a24070406228f5f0','6e6050b6474a40d79aef93d9ef85098a','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ea9aba40003','269d0173006946b0923ce1d09c6a6393','6e6050b6474a40d79aef93d9ef85098a','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ea9aba40003','30f4ab97a0334e7b99e6018af7a8c35f','6e6050b6474a40d79aef93d9ef85098a','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ea9aba40003','3d4a319e7c4c4e64b3bb2c0efe9f6034','6e6050b6474a40d79aef93d9ef85098a','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ea9aba40003','541687276b2443f0b93e3d11dc5b5737','6e6050b6474a40d79aef93d9ef85098a','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ea9aba40003','6db6c1b3e25b494aafb25d2099aa15a1','6e6050b6474a40d79aef93d9ef85098a','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ea9aba40003','7d59614e37564e50af3a9acd91ca8706','6e6050b6474a40d79aef93d9ef85098a','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ea9aba40003','7f55f7817cc94171a5d68e6aab533403','6e6050b6474a40d79aef93d9ef85098a','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ea9aba40003','815ef64cab6b4750a20055f89c644f00','6e6050b6474a40d79aef93d9ef85098a','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ea9aba40003','81806c50ff734d5ea5ddd8ffeebe41e0','6e6050b6474a40d79aef93d9ef85098a','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ea9aba40003','887a8ced40b74c459c9b8c3b5fb15f07','6e6050b6474a40d79aef93d9ef85098a','editableRequired');
insert into wf_document_display values ('40288ab7772ea2e301772ea9aba40003','895c1e72156e4b9f9d0bc011318f899e','6e6050b6474a40d79aef93d9ef85098a','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ea9aba40003','9bde985e28cc41b098437e8eade18265','6e6050b6474a40d79aef93d9ef85098a','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ea9aba40003','a6ce1b0dac7f41e7b0c97ac84a6e086b','6e6050b6474a40d79aef93d9ef85098a','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ea9aba40003','ac12c395142045678a491164d41aff78','6e6050b6474a40d79aef93d9ef85098a','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ea9aba40003','b04f7051d7674a53804f63326e07781e','6e6050b6474a40d79aef93d9ef85098a','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ea9aba40003','b6b12cfda11c4af8a76494e135ca2f8d','6e6050b6474a40d79aef93d9ef85098a','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ea9aba40003','b6cf4e793abc40cc9093b0ea13157d12','6e6050b6474a40d79aef93d9ef85098a','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ea9aba40003','bd38d6e3e9014767a012e2aeaa779539','6e6050b6474a40d79aef93d9ef85098a','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ea9aba40003','d88c58bdb16f47feac9c286100f67faa','6e6050b6474a40d79aef93d9ef85098a','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ea9aba40003','e0720e3eeecd433baf321a836a8e9eb5','6e6050b6474a40d79aef93d9ef85098a','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ea9aba40003','e8c829ae11944852ae70d37a77648edf','6e6050b6474a40d79aef93d9ef85098a','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ea9aba40003','f6a6b980bc134161a09e2e25eeeee4c8','6e6050b6474a40d79aef93d9ef85098a','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','02775b1802734c47908fc3da0c7014a8','8fbd692e22b4428fa9d3192a861a06ea','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','02775b1802734c47908fc3da0c7014a8','3bbb1af5cbe147718e512da2c1b2b458','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','02775b1802734c47908fc3da0c7014a8','317cf6711cae4263b549f937e30db546','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','02775b1802734c47908fc3da0c7014a8','044f83b8d41341839c64bb52d40bfc9b','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','0c262cfa44fe440bb8284978ea8554d1','3bbb1af5cbe147718e512da2c1b2b458','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','0c262cfa44fe440bb8284978ea8554d1','317cf6711cae4263b549f937e30db546','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','0c262cfa44fe440bb8284978ea8554d1','8fbd692e22b4428fa9d3192a861a06ea','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','0c262cfa44fe440bb8284978ea8554d1','044f83b8d41341839c64bb52d40bfc9b','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','1c577578d51d448299eee1c98340a223','8fbd692e22b4428fa9d3192a861a06ea','editableRequired');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','1c577578d51d448299eee1c98340a223','317cf6711cae4263b549f937e30db546','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','1c577578d51d448299eee1c98340a223','044f83b8d41341839c64bb52d40bfc9b','editableRequired');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','1c577578d51d448299eee1c98340a223','3bbb1af5cbe147718e512da2c1b2b458','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','22fa2c689b9d43a986dfd34e197c7f3a','8fbd692e22b4428fa9d3192a861a06ea','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','22fa2c689b9d43a986dfd34e197c7f3a','317cf6711cae4263b549f937e30db546','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','22fa2c689b9d43a986dfd34e197c7f3a','3bbb1af5cbe147718e512da2c1b2b458','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','22fa2c689b9d43a986dfd34e197c7f3a','044f83b8d41341839c64bb52d40bfc9b','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','28c9a8ad4b144cfbbdc27349d855773f','8fbd692e22b4428fa9d3192a861a06ea','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','28c9a8ad4b144cfbbdc27349d855773f','317cf6711cae4263b549f937e30db546','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','28c9a8ad4b144cfbbdc27349d855773f','3bbb1af5cbe147718e512da2c1b2b458','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','28c9a8ad4b144cfbbdc27349d855773f','044f83b8d41341839c64bb52d40bfc9b','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','3132307e44ff4286b92650638afececd','044f83b8d41341839c64bb52d40bfc9b','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','3132307e44ff4286b92650638afececd','3bbb1af5cbe147718e512da2c1b2b458','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','3132307e44ff4286b92650638afececd','317cf6711cae4263b549f937e30db546','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','3132307e44ff4286b92650638afececd','8fbd692e22b4428fa9d3192a861a06ea','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','3b6712c115aa4e648565a4befea8ccd3','044f83b8d41341839c64bb52d40bfc9b','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','3b6712c115aa4e648565a4befea8ccd3','3bbb1af5cbe147718e512da2c1b2b458','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','3b6712c115aa4e648565a4befea8ccd3','317cf6711cae4263b549f937e30db546','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','3b6712c115aa4e648565a4befea8ccd3','8fbd692e22b4428fa9d3192a861a06ea','editableRequired');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','4472820c4c4949258e8af5374ca15ff3','8fbd692e22b4428fa9d3192a861a06ea','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','4472820c4c4949258e8af5374ca15ff3','044f83b8d41341839c64bb52d40bfc9b','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','4472820c4c4949258e8af5374ca15ff3','3bbb1af5cbe147718e512da2c1b2b458','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','4472820c4c4949258e8af5374ca15ff3','317cf6711cae4263b549f937e30db546','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','47dc200d5a564d46bd1ac5cfc1d76187','3bbb1af5cbe147718e512da2c1b2b458','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','47dc200d5a564d46bd1ac5cfc1d76187','044f83b8d41341839c64bb52d40bfc9b','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','47dc200d5a564d46bd1ac5cfc1d76187','8fbd692e22b4428fa9d3192a861a06ea','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','47dc200d5a564d46bd1ac5cfc1d76187','317cf6711cae4263b549f937e30db546','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','4c82676d6321443a8ea6f38161ceff2e','3bbb1af5cbe147718e512da2c1b2b458','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','4c82676d6321443a8ea6f38161ceff2e','044f83b8d41341839c64bb52d40bfc9b','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','4c82676d6321443a8ea6f38161ceff2e','8fbd692e22b4428fa9d3192a861a06ea','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','4c82676d6321443a8ea6f38161ceff2e','317cf6711cae4263b549f937e30db546','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','560cb3baf8f64146b8d6a5f8c3124d63','317cf6711cae4263b549f937e30db546','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','560cb3baf8f64146b8d6a5f8c3124d63','044f83b8d41341839c64bb52d40bfc9b','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','560cb3baf8f64146b8d6a5f8c3124d63','8fbd692e22b4428fa9d3192a861a06ea','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','560cb3baf8f64146b8d6a5f8c3124d63','3bbb1af5cbe147718e512da2c1b2b458','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','5d80057a921040669b4a5be53ea57304','044f83b8d41341839c64bb52d40bfc9b','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','5d80057a921040669b4a5be53ea57304','8fbd692e22b4428fa9d3192a861a06ea','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','5d80057a921040669b4a5be53ea57304','317cf6711cae4263b549f937e30db546','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','5d80057a921040669b4a5be53ea57304','3bbb1af5cbe147718e512da2c1b2b458','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','61b3d4683993430a9cb03ab9cb088fef','3bbb1af5cbe147718e512da2c1b2b458','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','61b3d4683993430a9cb03ab9cb088fef','044f83b8d41341839c64bb52d40bfc9b','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','61b3d4683993430a9cb03ab9cb088fef','8fbd692e22b4428fa9d3192a861a06ea','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','61b3d4683993430a9cb03ab9cb088fef','317cf6711cae4263b549f937e30db546','editableRequired');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','786337268d7745c9ab06e54a0f01a23a','3bbb1af5cbe147718e512da2c1b2b458','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','786337268d7745c9ab06e54a0f01a23a','044f83b8d41341839c64bb52d40bfc9b','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','786337268d7745c9ab06e54a0f01a23a','8fbd692e22b4428fa9d3192a861a06ea','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','786337268d7745c9ab06e54a0f01a23a','317cf6711cae4263b549f937e30db546','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','7b8c641a13624211830b9c466f217985','3bbb1af5cbe147718e512da2c1b2b458','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','7b8c641a13624211830b9c466f217985','317cf6711cae4263b549f937e30db546','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','7b8c641a13624211830b9c466f217985','8fbd692e22b4428fa9d3192a861a06ea','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','7b8c641a13624211830b9c466f217985','044f83b8d41341839c64bb52d40bfc9b','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','7ee30588d4a8430ab249fd95f9fe0890','044f83b8d41341839c64bb52d40bfc9b','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','7ee30588d4a8430ab249fd95f9fe0890','3bbb1af5cbe147718e512da2c1b2b458','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','7ee30588d4a8430ab249fd95f9fe0890','317cf6711cae4263b549f937e30db546','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','7ee30588d4a8430ab249fd95f9fe0890','8fbd692e22b4428fa9d3192a861a06ea','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','8cab0f7cd82a4873b4060d243cf1de48','3bbb1af5cbe147718e512da2c1b2b458','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','8cab0f7cd82a4873b4060d243cf1de48','044f83b8d41341839c64bb52d40bfc9b','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','8cab0f7cd82a4873b4060d243cf1de48','8fbd692e22b4428fa9d3192a861a06ea','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','8cab0f7cd82a4873b4060d243cf1de48','317cf6711cae4263b549f937e30db546','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','96a8288e0cf349b6bc4609ccd45a069c','8fbd692e22b4428fa9d3192a861a06ea','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','96a8288e0cf349b6bc4609ccd45a069c','3bbb1af5cbe147718e512da2c1b2b458','editableRequired');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','96a8288e0cf349b6bc4609ccd45a069c','044f83b8d41341839c64bb52d40bfc9b','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','96a8288e0cf349b6bc4609ccd45a069c','317cf6711cae4263b549f937e30db546','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','9b986ab955324ab481a464a0820d9f44','044f83b8d41341839c64bb52d40bfc9b','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','9b986ab955324ab481a464a0820d9f44','8fbd692e22b4428fa9d3192a861a06ea','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','9b986ab955324ab481a464a0820d9f44','317cf6711cae4263b549f937e30db546','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','9b986ab955324ab481a464a0820d9f44','3bbb1af5cbe147718e512da2c1b2b458','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','9cfae02dcf344b049da289b62add4080','3bbb1af5cbe147718e512da2c1b2b458','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','9cfae02dcf344b049da289b62add4080','044f83b8d41341839c64bb52d40bfc9b','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','9cfae02dcf344b049da289b62add4080','8fbd692e22b4428fa9d3192a861a06ea','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','9cfae02dcf344b049da289b62add4080','317cf6711cae4263b549f937e30db546','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','a924fa7f6fd946298f92e2589a3a81f9','3bbb1af5cbe147718e512da2c1b2b458','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','a924fa7f6fd946298f92e2589a3a81f9','044f83b8d41341839c64bb52d40bfc9b','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','a924fa7f6fd946298f92e2589a3a81f9','8fbd692e22b4428fa9d3192a861a06ea','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','a924fa7f6fd946298f92e2589a3a81f9','317cf6711cae4263b549f937e30db546','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','bc775676f79d4a2b8c1b3a239b101ac5','317cf6711cae4263b549f937e30db546','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','bc775676f79d4a2b8c1b3a239b101ac5','044f83b8d41341839c64bb52d40bfc9b','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','bc775676f79d4a2b8c1b3a239b101ac5','3bbb1af5cbe147718e512da2c1b2b458','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','bc775676f79d4a2b8c1b3a239b101ac5','8fbd692e22b4428fa9d3192a861a06ea','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','bffef2192206490c8ae34d1162a7013f','3bbb1af5cbe147718e512da2c1b2b458','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','bffef2192206490c8ae34d1162a7013f','8fbd692e22b4428fa9d3192a861a06ea','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','bffef2192206490c8ae34d1162a7013f','317cf6711cae4263b549f937e30db546','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','bffef2192206490c8ae34d1162a7013f','044f83b8d41341839c64bb52d40bfc9b','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','d89d0854c1a84472af43fdc50b28c68e','044f83b8d41341839c64bb52d40bfc9b','editableRequired');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','d89d0854c1a84472af43fdc50b28c68e','317cf6711cae4263b549f937e30db546','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','d89d0854c1a84472af43fdc50b28c68e','8fbd692e22b4428fa9d3192a861a06ea','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','d89d0854c1a84472af43fdc50b28c68e','3bbb1af5cbe147718e512da2c1b2b458','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','d9a167efd5c2406a92adf82e6363bff9','3bbb1af5cbe147718e512da2c1b2b458','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','d9a167efd5c2406a92adf82e6363bff9','317cf6711cae4263b549f937e30db546','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','d9a167efd5c2406a92adf82e6363bff9','044f83b8d41341839c64bb52d40bfc9b','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','d9a167efd5c2406a92adf82e6363bff9','8fbd692e22b4428fa9d3192a861a06ea','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','dba870b223974a3b8287f5f2537940cc','3bbb1af5cbe147718e512da2c1b2b458','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','dba870b223974a3b8287f5f2537940cc','044f83b8d41341839c64bb52d40bfc9b','editableRequired');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','dba870b223974a3b8287f5f2537940cc','8fbd692e22b4428fa9d3192a861a06ea','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','dba870b223974a3b8287f5f2537940cc','317cf6711cae4263b549f937e30db546','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','df9e44a1f03941c7b69135382e99b52d','044f83b8d41341839c64bb52d40bfc9b','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','df9e44a1f03941c7b69135382e99b52d','8fbd692e22b4428fa9d3192a861a06ea','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','df9e44a1f03941c7b69135382e99b52d','317cf6711cae4263b549f937e30db546','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','df9e44a1f03941c7b69135382e99b52d','3bbb1af5cbe147718e512da2c1b2b458','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','e83b4c632b264606a56012be262ec2e9','3bbb1af5cbe147718e512da2c1b2b458','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','e83b4c632b264606a56012be262ec2e9','044f83b8d41341839c64bb52d40bfc9b','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','e83b4c632b264606a56012be262ec2e9','8fbd692e22b4428fa9d3192a861a06ea','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','e83b4c632b264606a56012be262ec2e9','317cf6711cae4263b549f937e30db546','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','feada594a34f4b268aab37eb9211e740','8fbd692e22b4428fa9d3192a861a06ea','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','feada594a34f4b268aab37eb9211e740','044f83b8d41341839c64bb52d40bfc9b','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','feada594a34f4b268aab37eb9211e740','3bbb1af5cbe147718e512da2c1b2b458','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','feada594a34f4b268aab37eb9211e740','317cf6711cae4263b549f937e30db546','editableRequired');
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

insert into wf_element_data values ('044f83b8d41341839c64bb52d40bfc9b','assignee-type','assignee.type.candidate.groups',0,TRUE);
insert into wf_element_data values ('044f83b8d41341839c64bb52d40bfc9b','assignee','users.general',1,TRUE);
insert into wf_element_data values ('044f83b8d41341839c64bb52d40bfc9b','reject-id','',2,FALSE);
insert into wf_element_data values ('044f83b8d41341839c64bb52d40bfc9b','withdraw','N',3,FALSE);
insert into wf_element_data values ('1e0b12f83d2d4d3a85950b9a0f06d067','complete-action','',0,FALSE);
insert into wf_element_data values ('297df2c4770748e480376e0fcc903ef4','action-name','',0,FALSE);
insert into wf_element_data values ('297df2c4770748e480376e0fcc903ef4','action-value','',1,FALSE);
insert into wf_element_data values ('297df2c4770748e480376e0fcc903ef4','is-default','N',2,FALSE);
insert into wf_element_data values ('297df2c4770748e480376e0fcc903ef4','condition-value','',3,FALSE);
insert into wf_element_data values ('297df2c4770748e480376e0fcc903ef4','start-id','3f6b15d6d81c451ebcb26eae5e31ae17',4,TRUE);
insert into wf_element_data values ('297df2c4770748e480376e0fcc903ef4','start-name','만족도평가',5,FALSE);
insert into wf_element_data values ('297df2c4770748e480376e0fcc903ef4','end-id','0c6c40925b924ac88808caabfb9dce76',6,TRUE);
insert into wf_element_data values ('297df2c4770748e480376e0fcc903ef4','end-name','종료',7,FALSE);
insert into wf_element_data values ('317cf6711cae4263b549f937e30db546','assignee-type','assignee.type.assignee',0,TRUE);
insert into wf_element_data values ('317cf6711cae4263b549f937e30db546','assignee','assignee',1,TRUE);
insert into wf_element_data values ('317cf6711cae4263b549f937e30db546','reject-id','',2,FALSE);
insert into wf_element_data values ('317cf6711cae4263b549f937e30db546','withdraw','N',3,FALSE);
insert into wf_element_data values ('3bbb1af5cbe147718e512da2c1b2b458','assignee-type','assignee.type.assignee',0,TRUE);
insert into wf_element_data values ('3bbb1af5cbe147718e512da2c1b2b458','assignee','assignee2',1,TRUE);
insert into wf_element_data values ('3bbb1af5cbe147718e512da2c1b2b458','reject-id','317cf6711cae4263b549f937e30db546',2,FALSE);
insert into wf_element_data values ('3bbb1af5cbe147718e512da2c1b2b458','withdraw','N',3,FALSE);
insert into wf_element_data values ('3f6b15d6d81c451ebcb26eae5e31ae17','target-document-list','40288ab7772ea2e301772ea9aba40003',0,TRUE);
insert into wf_element_data values ('44d0ed9d60d544959c853d38d5cabec9','action-name','만족도 평가',0,FALSE);
insert into wf_element_data values ('44d0ed9d60d544959c853d38d5cabec9','action-value','progress',1,FALSE);
insert into wf_element_data values ('44d0ed9d60d544959c853d38d5cabec9','is-default','N',2,FALSE);
insert into wf_element_data values ('44d0ed9d60d544959c853d38d5cabec9','condition-value','',3,FALSE);
insert into wf_element_data values ('44d0ed9d60d544959c853d38d5cabec9','start-id','6e6050b6474a40d79aef93d9ef85098a',4,TRUE);
insert into wf_element_data values ('44d0ed9d60d544959c853d38d5cabec9','start-name','New Task',5,FALSE);
insert into wf_element_data values ('44d0ed9d60d544959c853d38d5cabec9','end-id','602d151b76584626ab765421f067da30',6,TRUE);
insert into wf_element_data values ('44d0ed9d60d544959c853d38d5cabec9','end-name','종료',7,FALSE);
insert into wf_element_data values ('682e1e295c3b4acc80f5f4942a38da8e','action-name','승인',0,FALSE);
insert into wf_element_data values ('682e1e295c3b4acc80f5f4942a38da8e','action-value','progress',1,FALSE);
insert into wf_element_data values ('682e1e295c3b4acc80f5f4942a38da8e','is-default','N',2,FALSE);
insert into wf_element_data values ('682e1e295c3b4acc80f5f4942a38da8e','condition-value','',3,FALSE);
insert into wf_element_data values ('682e1e295c3b4acc80f5f4942a38da8e','start-id','3bbb1af5cbe147718e512da2c1b2b458',4,TRUE);
insert into wf_element_data values ('682e1e295c3b4acc80f5f4942a38da8e','start-name','승인',5,FALSE);
insert into wf_element_data values ('682e1e295c3b4acc80f5f4942a38da8e','end-id','3f6b15d6d81c451ebcb26eae5e31ae17',6,TRUE);
insert into wf_element_data values ('682e1e295c3b4acc80f5f4942a38da8e','end-name','만족도평가',7,FALSE);
insert into wf_element_data values ('68725cdd45f94bf9a9012d39cc08dc6d','action-name','',0,FALSE);
insert into wf_element_data values ('68725cdd45f94bf9a9012d39cc08dc6d','action-value','',1,FALSE);
insert into wf_element_data values ('68725cdd45f94bf9a9012d39cc08dc6d','is-default','N',2,FALSE);
insert into wf_element_data values ('68725cdd45f94bf9a9012d39cc08dc6d','condition-value','',3,FALSE);
insert into wf_element_data values ('68725cdd45f94bf9a9012d39cc08dc6d','start-id','0f2df8d4e3454a4c91448307b1c6a432',4,TRUE);
insert into wf_element_data values ('68725cdd45f94bf9a9012d39cc08dc6d','start-name','',5,FALSE);
insert into wf_element_data values ('68725cdd45f94bf9a9012d39cc08dc6d','end-id','6e6050b6474a40d79aef93d9ef85098a',6,TRUE);
insert into wf_element_data values ('68725cdd45f94bf9a9012d39cc08dc6d','end-name','만족도평가',7,FALSE);
insert into wf_element_data values ('6e6050b6474a40d79aef93d9ef85098a','assignee-type','assignee.type.candidate.groups',0,TRUE);
insert into wf_element_data values ('6e6050b6474a40d79aef93d9ef85098a','assignee','admin',1,TRUE);
insert into wf_element_data values ('6e6050b6474a40d79aef93d9ef85098a','reject-id','',2,FALSE);
insert into wf_element_data values ('6e6050b6474a40d79aef93d9ef85098a','withdraw','N',3,FALSE);
insert into wf_element_data values ('8fbd692e22b4428fa9d3192a861a06ea','assignee-type','assignee.type.assignee',0,TRUE);
insert into wf_element_data values ('8fbd692e22b4428fa9d3192a861a06ea','assignee','assignee',1,TRUE);
insert into wf_element_data values ('8fbd692e22b4428fa9d3192a861a06ea','reject-id','',2,FALSE);
insert into wf_element_data values ('8fbd692e22b4428fa9d3192a861a06ea','withdraw','N',3,FALSE);
insert into wf_element_data values ('a5e1040405dd435f9bf5b745780a6158','action-name','신청등록',0,FALSE);
insert into wf_element_data values ('a5e1040405dd435f9bf5b745780a6158','action-value','progress',1,FALSE);
insert into wf_element_data values ('a5e1040405dd435f9bf5b745780a6158','is-default','N',2,FALSE);
insert into wf_element_data values ('a5e1040405dd435f9bf5b745780a6158','condition-value','',3,FALSE);
insert into wf_element_data values ('a5e1040405dd435f9bf5b745780a6158','start-id','044f83b8d41341839c64bb52d40bfc9b',4,TRUE);
insert into wf_element_data values ('a5e1040405dd435f9bf5b745780a6158','start-name','신청서 작성',5,FALSE);
insert into wf_element_data values ('a5e1040405dd435f9bf5b745780a6158','end-id','8fbd692e22b4428fa9d3192a861a06ea',6,TRUE);
insert into wf_element_data values ('a5e1040405dd435f9bf5b745780a6158','end-name','신청서 검토',7,FALSE);
insert into wf_element_data values ('ab814067c8f243499c559676505ef790','action-name','접수',0,FALSE);
insert into wf_element_data values ('ab814067c8f243499c559676505ef790','action-value','progress',1,FALSE);
insert into wf_element_data values ('ab814067c8f243499c559676505ef790','is-default','N',2,FALSE);
insert into wf_element_data values ('ab814067c8f243499c559676505ef790','condition-value','',3,FALSE);
insert into wf_element_data values ('ab814067c8f243499c559676505ef790','start-id','8fbd692e22b4428fa9d3192a861a06ea',4,TRUE);
insert into wf_element_data values ('ab814067c8f243499c559676505ef790','start-name','신청서 검토',5,FALSE);
insert into wf_element_data values ('ab814067c8f243499c559676505ef790','end-id','1e0b12f83d2d4d3a85950b9a0f06d067',6,TRUE);
insert into wf_element_data values ('ab814067c8f243499c559676505ef790','end-name','접수',7,FALSE);
insert into wf_element_data values ('ae863e288a164f48aaa56b124a8d5113','action-name','승인요청',0,FALSE);
insert into wf_element_data values ('ae863e288a164f48aaa56b124a8d5113','action-value','progress',1,FALSE);
insert into wf_element_data values ('ae863e288a164f48aaa56b124a8d5113','is-default','N',2,FALSE);
insert into wf_element_data values ('ae863e288a164f48aaa56b124a8d5113','condition-value','',3,FALSE);
insert into wf_element_data values ('ae863e288a164f48aaa56b124a8d5113','start-id','317cf6711cae4263b549f937e30db546',4,TRUE);
insert into wf_element_data values ('ae863e288a164f48aaa56b124a8d5113','start-name','처리',5,FALSE);
insert into wf_element_data values ('ae863e288a164f48aaa56b124a8d5113','end-id','3bbb1af5cbe147718e512da2c1b2b458',6,TRUE);
insert into wf_element_data values ('ae863e288a164f48aaa56b124a8d5113','end-name','승인',7,FALSE);
insert into wf_element_data values ('e38faeee0db140cebd714d290a3b3a7b','action-name','',0,FALSE);
insert into wf_element_data values ('e38faeee0db140cebd714d290a3b3a7b','action-value','',1,FALSE);
insert into wf_element_data values ('e38faeee0db140cebd714d290a3b3a7b','is-default','N',2,FALSE);
insert into wf_element_data values ('e38faeee0db140cebd714d290a3b3a7b','condition-value','',3,FALSE);
insert into wf_element_data values ('e38faeee0db140cebd714d290a3b3a7b','start-id','1e0b12f83d2d4d3a85950b9a0f06d067',4,TRUE);
insert into wf_element_data values ('e38faeee0db140cebd714d290a3b3a7b','start-name','접수',5,FALSE);
insert into wf_element_data values ('e38faeee0db140cebd714d290a3b3a7b','end-id','317cf6711cae4263b549f937e30db546',6,TRUE);
insert into wf_element_data values ('e38faeee0db140cebd714d290a3b3a7b','end-name','처리',7,FALSE);
insert into wf_element_data values ('f0abc7d89fd049cbb9d31c7499118741','action-name','',0,FALSE);
insert into wf_element_data values ('f0abc7d89fd049cbb9d31c7499118741','action-value','',1,FALSE);
insert into wf_element_data values ('f0abc7d89fd049cbb9d31c7499118741','is-default','N',2,FALSE);
insert into wf_element_data values ('f0abc7d89fd049cbb9d31c7499118741','condition-value','',3,FALSE);
insert into wf_element_data values ('f0abc7d89fd049cbb9d31c7499118741','start-id','e84bc306a19241cf98691491676df0ec',4,TRUE);
insert into wf_element_data values ('f0abc7d89fd049cbb9d31c7499118741','start-name','',5,FALSE);
insert into wf_element_data values ('f0abc7d89fd049cbb9d31c7499118741','end-id','044f83b8d41341839c64bb52d40bfc9b',6,TRUE);
insert into wf_element_data values ('f0abc7d89fd049cbb9d31c7499118741','end-name','신청서 작성',7,FALSE);
insert into wf_element_data values ('01430d9b3cfe427b89f9203185e35530','action-name','접수', 0,FALSE);
insert into wf_element_data values ('01430d9b3cfe427b89f9203185e35530','action-value','accept', 1,FALSE);
insert into wf_element_data values ('01430d9b3cfe427b89f9203185e35530','is-default','N', 2,FALSE);
insert into wf_element_data values ('01430d9b3cfe427b89f9203185e35530','condition-value','', 3,FALSE);
insert into wf_element_data values ('01430d9b3cfe427b89f9203185e35530','start-id','bff880e180164c4f9cedfab4ba282f28', 4,TRUE);
insert into wf_element_data values ('01430d9b3cfe427b89f9203185e35530','start-name','신청서 검토', 5,FALSE);
insert into wf_element_data values ('01430d9b3cfe427b89f9203185e35530','end-id','b0dd93b1c9864edc8f048713c0d07e12', 6,TRUE);
insert into wf_element_data values ('01430d9b3cfe427b89f9203185e35530','end-name','접수', 7,FALSE);
insert into wf_element_data values ('1318f304ca2a420e9c6756438daa4e3a','action-name','승인요청', 0,FALSE);
insert into wf_element_data values ('1318f304ca2a420e9c6756438daa4e3a','action-value','approve', 1,FALSE);
insert into wf_element_data values ('1318f304ca2a420e9c6756438daa4e3a','is-default','N', 2,FALSE);
insert into wf_element_data values ('1318f304ca2a420e9c6756438daa4e3a','condition-value','', 3,FALSE);
insert into wf_element_data values ('1318f304ca2a420e9c6756438daa4e3a','start-id','a34268ba767d48e7ab7db9e7297e7300', 4,TRUE);
insert into wf_element_data values ('1318f304ca2a420e9c6756438daa4e3a','start-name','구성변경 처리', 5,FALSE);
insert into wf_element_data values ('1318f304ca2a420e9c6756438daa4e3a','end-id','a508191382c644b289c01cf32bed8722', 6,TRUE);
insert into wf_element_data values ('1318f304ca2a420e9c6756438daa4e3a','end-name','변경결과 승인', 7,FALSE);
insert into wf_element_data values ('2a8341aa1dcb4ab7ab89271020c748c8','action-name','', 0,FALSE);
insert into wf_element_data values ('2a8341aa1dcb4ab7ab89271020c748c8','action-value','', 1,FALSE);
insert into wf_element_data values ('2a8341aa1dcb4ab7ab89271020c748c8','is-default','N', 2,FALSE);
insert into wf_element_data values ('2a8341aa1dcb4ab7ab89271020c748c8','condition-value','', 3,FALSE);
insert into wf_element_data values ('2a8341aa1dcb4ab7ab89271020c748c8','start-id','d099b9ac855a4e7f9a641906549dbcb0', 4,TRUE);
insert into wf_element_data values ('2a8341aa1dcb4ab7ab89271020c748c8','start-name','', 5,FALSE);
insert into wf_element_data values ('2a8341aa1dcb4ab7ab89271020c748c8','end-id','4f296c1468f3422b8c59c97151e2c476', 6,TRUE);
insert into wf_element_data values ('2a8341aa1dcb4ab7ab89271020c748c8','end-name','신청서 작성', 7,FALSE);
insert into wf_element_data values ('4f296c1468f3422b8c59c97151e2c476','assignee-type','assignee.type.candidate.groups', 0,TRUE);
insert into wf_element_data values ('4f296c1468f3422b8c59c97151e2c476','assignee','users.general', 1,TRUE);
insert into wf_element_data values ('4f296c1468f3422b8c59c97151e2c476','reject-id','', 2,FALSE);
insert into wf_element_data values ('4f296c1468f3422b8c59c97151e2c476','withdraw','N', 3,FALSE);
insert into wf_element_data values ('70e8f5da83584cba81bd9ff597963c4a','action-name','', 0,FALSE);
insert into wf_element_data values ('70e8f5da83584cba81bd9ff597963c4a','action-value','', 1,FALSE);
insert into wf_element_data values ('70e8f5da83584cba81bd9ff597963c4a','is-default','N', 2,FALSE);
insert into wf_element_data values ('70e8f5da83584cba81bd9ff597963c4a','condition-value','', 3,FALSE);
insert into wf_element_data values ('70e8f5da83584cba81bd9ff597963c4a','start-id','b0dd93b1c9864edc8f048713c0d07e12', 4,TRUE);
insert into wf_element_data values ('70e8f5da83584cba81bd9ff597963c4a','start-name','접수', 5,FALSE);
insert into wf_element_data values ('70e8f5da83584cba81bd9ff597963c4a','end-id','a34268ba767d48e7ab7db9e7297e7300', 6,TRUE);
insert into wf_element_data values ('70e8f5da83584cba81bd9ff597963c4a','end-name','구성변경 처리', 7,FALSE);
insert into wf_element_data values ('957ac0be921c4337999fcbca2f22a92d','action-name','신청등록', 0,FALSE);
insert into wf_element_data values ('957ac0be921c4337999fcbca2f22a92d','action-value','create', 1,FALSE);
insert into wf_element_data values ('957ac0be921c4337999fcbca2f22a92d','is-default','N', 2,FALSE);
insert into wf_element_data values ('957ac0be921c4337999fcbca2f22a92d','condition-value','', 3,FALSE);
insert into wf_element_data values ('957ac0be921c4337999fcbca2f22a92d','start-id','4f296c1468f3422b8c59c97151e2c476', 4,TRUE);
insert into wf_element_data values ('957ac0be921c4337999fcbca2f22a92d','start-name','신청서 작성', 5,FALSE);
insert into wf_element_data values ('957ac0be921c4337999fcbca2f22a92d','end-id','bff880e180164c4f9cedfab4ba282f28', 6,TRUE);
insert into wf_element_data values ('957ac0be921c4337999fcbca2f22a92d','end-name','신청서 검토', 7,FALSE);
insert into wf_element_data values ('a34268ba767d48e7ab7db9e7297e7300','assignee-type','assignee.type.assignee', 0,TRUE);
insert into wf_element_data values ('a34268ba767d48e7ab7db9e7297e7300','assignee','assignee1', 1,TRUE);
insert into wf_element_data values ('a34268ba767d48e7ab7db9e7297e7300','reject-id','', 2,FALSE);
insert into wf_element_data values ('a34268ba767d48e7ab7db9e7297e7300','withdraw','N', 3,FALSE);
insert into wf_element_data values ('a4aa971e1f952df93f07e932ab25fbf6','script-type','script.type.cmdb', 0,TRUE);
insert into wf_element_data values ('a4bd7d4950b4226ea3dfc20bf15193ff','action-name','승인', 0,FALSE);
insert into wf_element_data values ('a4bd7d4950b4226ea3dfc20bf15193ff','action-value','approval', 1,FALSE);
insert into wf_element_data values ('a4bd7d4950b4226ea3dfc20bf15193ff','is-default','N', 2,FALSE);
insert into wf_element_data values ('a4bd7d4950b4226ea3dfc20bf15193ff','condition-value','', 3,FALSE);
insert into wf_element_data values ('a4bd7d4950b4226ea3dfc20bf15193ff','start-id','a508191382c644b289c01cf32bed8722', 4,TRUE);
insert into wf_element_data values ('a4bd7d4950b4226ea3dfc20bf15193ff','start-name','변경결과 승인', 5,FALSE);
insert into wf_element_data values ('a4bd7d4950b4226ea3dfc20bf15193ff','end-id','a4aa971e1f952df93f07e932ab25fbf6', 6,TRUE);
insert into wf_element_data values ('a4bd7d4950b4226ea3dfc20bf15193ff','end-name','CMDB 적용', 7,FALSE);
insert into wf_element_data values ('a508191382c644b289c01cf32bed8722','assignee-type','assignee.type.assignee', 0,TRUE);
insert into wf_element_data values ('a508191382c644b289c01cf32bed8722','assignee','assignee2', 1,TRUE);
insert into wf_element_data values ('a508191382c644b289c01cf32bed8722','reject-id','a34268ba767d48e7ab7db9e7297e7300', 2,FALSE);
insert into wf_element_data values ('a508191382c644b289c01cf32bed8722','withdraw','Y', 3,FALSE);
insert into wf_element_data values ('ad56214bb68e7b749f7d336e14172321','action-name','', 0,FALSE);
insert into wf_element_data values ('ad56214bb68e7b749f7d336e14172321','action-value','', 1,FALSE);
insert into wf_element_data values ('ad56214bb68e7b749f7d336e14172321','is-default','N', 2,FALSE);
insert into wf_element_data values ('ad56214bb68e7b749f7d336e14172321','condition-value','', 3,FALSE);
insert into wf_element_data values ('ad56214bb68e7b749f7d336e14172321','start-id','a4aa971e1f952df93f07e932ab25fbf6', 4,TRUE);
insert into wf_element_data values ('ad56214bb68e7b749f7d336e14172321','start-name','CMDB 적용', 5,FALSE);
insert into wf_element_data values ('ad56214bb68e7b749f7d336e14172321','end-id','a9a0c170a6ff5ae938f128a726fc2a60', 6,TRUE);
insert into wf_element_data values ('ad56214bb68e7b749f7d336e14172321','end-name','종료', 7,FALSE);
insert into wf_element_data values ('b0dd93b1c9864edc8f048713c0d07e12','complete-action','', 0,FALSE);
insert into wf_element_data values ('bff880e180164c4f9cedfab4ba282f28','assignee-type','assignee.type.assignee', 0,TRUE);
insert into wf_element_data values ('bff880e180164c4f9cedfab4ba282f28','assignee','assignee1', 1,TRUE);
insert into wf_element_data values ('bff880e180164c4f9cedfab4ba282f28','reject-id','', 2,FALSE);
insert into wf_element_data values ('bff880e180164c4f9cedfab4ba282f28','withdraw','N', 3,FALSE);
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
    update_dt timestamp
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

