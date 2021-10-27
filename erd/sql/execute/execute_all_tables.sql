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
insert into awf_auth values ('chart.read', '사용자 정의 차트 조회', '사용자 정의 차트 조회 권한', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('chart.create', '사용자 정의 차트 등록', '사용자 정의 차트 등록 권한', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('chart.update', '사용자 정의 차트 수정', '사용자 정의 차트 수정 권한', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('chart.delete', '사용자 정의 차트 삭제', '사용자 정의 차트 삭제 권한', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
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
insert into awf_auth values ('product.read', '제품 정보 조회', '제품 정보 조회 권한', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('report.create', '보고서 등록', null, now(), null, '0509e09412534a6e98f04ca79abb6424', null);
insert into awf_auth values ('report.delete', '보고서 삭제', null, now(), null, '0509e09412534a6e98f04ca79abb6424', null);
insert into awf_auth values ('report.read', '보고서 조회', null, now(), null, '0509e09412534a6e98f04ca79abb6424', null);
insert into awf_auth values ('report.update', '보고서 변경', null, now(), null, '0509e09412534a6e98f04ca79abb6424', null);
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
	editable boolean default true,
	use_yn boolean default true,
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
insert into awf_code values ('cmdb', 'root', 'cmdb', 'CMDB 설정', 'CMDB 설정', true, true, 1, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('cmdb.relation.type', 'cmdb', 'cmdb.relation.type', 'CI 연관 관계 타입', 'CI 연관 관계 타입', true, true, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('cmdb.relation.type.default', 'cmdb.relation.type', 'default', 'default', '기본 연관', true, true, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('cmdb.db.kind', '', null, '데이터베이스', 'cmdb 데이터데이스 종류', true, TRUE, 1, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('cmdb.db.kind.oracle', 'cmdb.db.kind', 'oracle', 'Oracle', 'cmdb 데이터데이스 종류', true, TRUE, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('cmdb.db.kind.postgresql', 'cmdb.db.kind', 'postgresql', 'Postresql', '', true, TRUE, 2, 2,  '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('cmdb.db.kind.mssql', 'cmdb.db.kind', 'mssql','MSSQL', '', true, TRUE, 2, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('cmdb.db.kind.mysql', 'cmdb.db.kind', 'mysql','MYSQL', '', true, TRUE, 2, 4, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('cmdb.db.kind.tibero', 'cmdb.db.kind', 'tibero','TIBERO', '', true, TRUE, 2, 5, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('cmdb.db.kind.redis', 'cmdb.db.kind', 'redis','Redis', '', true, TRUE, 2, 6, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('cmdb.db.kind.rac', 'cmdb.db.kind', 'rac','RAC', '', true, TRUE, 2, 7, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('cmdb.db.kind.db2', 'cmdb.db.kind', 'db2','DB2', '', true, TRUE, 2, 8, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('cmdb.db.kind.altibase', 'cmdb.db.kind', 'altibase', 'Altibase', '', true, TRUE, 2, 9, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('cmdb.db.kind.sysbase', 'cmdb.db.kind', 'sysbase','SYBASE', '', true, TRUE, 2, 10, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('cmdb.db.kind.sysbaseiq', 'cmdb.db.kind', 'sysbaseiq', 'SYBASEIQ', '', true, TRUE, 2, 11, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('cmdb.db.kind.mongodb', 'cmdb.db.kind', 'mongodb', 'MONGODB', '', true, TRUE, 2, 12, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('cmdb.db.kind.ppas', 'cmdb.db.kind', 'ppas', 'PPAS', '', true, TRUE, 2, 13, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('cmdb.db.kind.cubrid', 'cmdb.db.kind', 'cubrid', 'CUBRID', '', true, TRUE, 2, 14, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('cmdb.db.kind.mariadb', 'cmdb.db.kind', 'mariadb', 'MARIADB', '', true, TRUE, 2, 15, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('cmdb.db.kind.informix', 'cmdb.db.kind', 'informix', 'INFORMIX', '', true, TRUE, 2, 16, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('root', null, null, 'ROOT', null, false, true, 0, 0, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('assignee', 'root', null, '담당자', null, false, true, 1, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('assignee.type', 'assignee', null, '담당자 타입', null, false, true, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('assignee.type.assignee', 'assignee.type', null, '지정 담당자', null, false, true, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('assignee.type.candidate.groups', 'assignee.type', null, '담당자 후보그룹', null, false, true, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('assignee.type.candidate.users', 'assignee.type', null, '담당자 후보목록', null, false, true, 3, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('document', 'root', null, '신청서', null, false, true, 1, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('document.group', 'document', null, '신청서 목록', null, true, true, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('document.group.incident', 'document.group', '장애문의', '장애문의', null, true, true, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('document.group.inquiry', 'document.group', '단순문의', '단순문의', null, true, true, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('document.group.request', 'document.group', '서비스요청', '서비스요청', null, true, true, 3, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('document.status', 'document', null, '신청서 상태', null, false, true, 2, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('document.status.temporary', 'document.status', '임시', '임시', null, false, true, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('document.status.destroy', 'document.status', '폐기', '폐기', null, false, true, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('document.status.use', 'document.status', '사용', '사용', null, false, true, 3, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('document.displayType', 'document', null, '신청서 프로세스별 그룹 출력 타입', null, false, true, 2, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('document.displayType.editable', 'document.displayType', '수정 가능', '수정 가능', null, false, true, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('document.displayType.readonly', 'document.displayType', '수정 불가', '수정 불가', null, false, true, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('document.displayType.hidden', 'document.displayType', '숨김', '숨김', null, false, true, 3, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('download', 'root', null, '자료실', null, true, true, 1, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('download.category', 'download', null, '자료실 카테고리', null, true, true, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('download.category.companyPolicy', 'download.category', null, '회사규정', null, true, true, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('download.category.etc', 'download.category', null, '기타', null, true, true, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('faq', 'root', null, 'FAQ', null, false, true, 1, 4, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('faq.category', 'faq', null, 'FAQ 카테고리', null, false, true, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('faq.category.etc', 'faq.category', null, '기타', null, false, true, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('faq.category.setting', 'faq.category', null, '설정', null, false, true, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('faq.category.techSupport', 'faq.category', null, '기술지원', null, false, true, 3, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('form', 'root', null, '문서양식', null, false, true, 1, 5, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('form.lang', 'form', null, '문서양식 언어', null, false, true, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('form.lang.ko', 'form.lang', null, '한국어', null, false, true, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('form.template', 'form', null, '템플릿', null, true, true, 2, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
/* 단순문의 */
insert into awf_code values ('form.template.serviceDesk.inquiry', 'form.template', 'form.template.serviceDesk.inquiry', '서비스데스크 - 단순문의', '', true, true, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
/* 단순문의 - 서비스 항목*/
insert into awf_code values ('form.template.serviceDesk.inquiry.category', 'form.template.serviceDesk.inquiry', 'form.template.serviceDesk.inquiry.category', '서비스 항목', '단순문의 서비스 항목', true, true, 4, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('form.template.serviceDesk.inquiry.category.none', 'form.template.serviceDesk.inquiry.category', 'none', '선택 안함', '', true, true, 5, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('form.template.serviceDesk.inquiry.category.server', 'form.template.serviceDesk.inquiry.category', 'server', '서버', '', true, true, 5, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('form.template.serviceDesk.inquiry.category.network', 'form.template.serviceDesk.inquiry.category', 'network', '네트워크', '', true, true, 5, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('form.template.serviceDesk.inquiry.category.security', 'form.template.serviceDesk.inquiry.category', 'security', '보안', '', true, true, 5, 4, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('form.template.serviceDesk.inquiry.category.dbms', 'form.template.serviceDesk.inquiry.category', 'dbms', '데이터베이스', '', true, true, 5, 5, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('form.template.serviceDesk.inquiry.category.contract', 'form.template.serviceDesk.inquiry.category', 'contract', '계약', '', true, true, 5, 6, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('form.template.serviceDesk.inquiry.category.etc', 'form.template.serviceDesk.inquiry.category', 'etc', '기타', '', true, true, 5, 7, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
/* 장애신고 */
insert into awf_code values ('form.template.serviceDesk.incident', 'form.template', 'form.template.serviceDesk.incident', '서비스데스크 - 장애신고', '', true, true, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
/* 장애신고 - 장애 유형 */
insert into awf_code values ('form.template.serviceDesk.incident.category', 'form.template.serviceDesk.incident', 'form.template.serviceDesk.incident.category', '장애유형', '장애신고서 장애유형', true, true, 4, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('form.template.serviceDesk.incident.category.none', 'form.template.serviceDesk.incident.category', 'none', '선택 안함', '', true, true, 5, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('form.template.serviceDesk.incident.category.system', 'form.template.serviceDesk.incident.category', 'system', '시스템 장애', '', true, true, 5, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('form.template.serviceDesk.incident.category.infrastructure', 'form.template.serviceDesk.incident.category', 'infrastructure', '기반구조 장애', '', true, true, 5, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('form.template.serviceDesk.incident.category.technology', 'form.template.serviceDesk.incident.category', 'technology', '기술적 장애', '', true, true, 5, 4, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('form.template.serviceDesk.incident.category.operation', 'form.template.serviceDesk.incident.category', 'operation', '운영 장애', '', true, true, 5, 5, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('form.template.serviceDesk.incident.category.human', 'form.template.serviceDesk.incident.category', 'human', '인적 장애', '', true, true, 5, 6, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
/* 서비스요청 */
insert into awf_code values ('form.template.serviceDesk.request', 'form.template', 'form.template.serviceDesk.request', '서비스데스크 - 서비스요청', '', true, true, 3, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
/* 서비스요청 - 요청 구분 */
insert into awf_code values ('form.template.serviceDesk.request.category', 'form.template.serviceDesk.request', 'form.template.serviceDesk.request.category', '요청구분', '서비스요청서 요청구분', true, true, 4, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('form.template.serviceDesk.request.category.service', 'form.template.serviceDesk.request.category', 'service', '서비스 요청', '', true, true, 5, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('form.template.serviceDesk.request.category.security', 'form.template.serviceDesk.request.category', 'security', '보안 요청', '', true, true, 5, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('form.template.serviceDesk.request.category.development', 'form.template.serviceDesk.request.category', 'development', '개발 요청', '', true, true, 5, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('form.template.serviceDesk.request.category.material', 'form.template.serviceDesk.request.category', 'material', '자료 요청', '', true, true, 5, 4, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('form.template.serviceDesk.request.category.work', 'form.template.serviceDesk.request.category', 'work', '작업 요청', '', true, true, 5, 5, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('form.template.serviceDesk.request.category.etc', 'form.template.serviceDesk.request.category', 'etc', '기타', '', true, true, 5, 6, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
/* 문서번호 */
insert into awf_code values ('numbering', 'root', null, '문서번호 규칙 패턴', null, false, true, 1, 6, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('numbering.pattern', 'numbering', null, '문서규칙 패턴', null, false, true, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('numbering.pattern.format', 'numbering.pattern', null, '문서규칙 포맷', null, false, true, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('numbering.pattern.format.yyyyMMdd', 'numbering.pattern.format', 'yyyyMMdd', '날짜형패턴', null, false, true, 4, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('numbering.pattern.format.yyyyddMM', 'numbering.pattern.format', 'yyyyddMM', '날짜형패턴', null, false, true, 4, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('numbering.pattern.format.MMddyyyy', 'numbering.pattern.format', 'MMddyyyy', '날짜형패턴', null, false, true, 4, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('numbering.pattern.format.ddMMyyyy', 'numbering.pattern.format', 'ddMMyyyy', '날짜형패턴', null, false, true, 4, 4, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('reception_type', 'root', '접수유형', null, null, true, true, 1, 7, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('email', 'reception_type', '이메일', '이메일', null, true, true, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('post', 'reception_type', '우편', '우편', null, true, true, 2, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('telephone', 'reception_type', '전화', '전화', null, true, true, 2, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('scheduler', 'root', null, '스케줄러', null, false, true, 1, 13, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('scheduler.taskType', 'scheduler', null, '작업 유형', null, false, true, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('scheduler.taskType.class', 'scheduler.taskType', 'class', 'CLASS', null, false, true, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('scheduler.taskType.query', 'scheduler.taskType', 'query', 'QUERY', null, false, true, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('scheduler.taskType.jar', 'scheduler.taskType', 'jar', 'JAR', null, false, true, 3, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('scheduler.executeCycleType', 'scheduler', null, '실행 유형', null, false, true, 2, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('scheduler.executeCycleType.fixedDelay', 'scheduler.executeCycleType', 'fixedDelay', 'FIXED_DELAY', null, false, true, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('scheduler.executeCycleType.fixedRate', 'scheduler.executeCycleType', 'fixedRate', 'FIXED_RATE', null, false, true, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('scheduler.executeCycleType.cron', 'scheduler.executeCycleType', 'cron', 'CRON', null, false, true, 3, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('script', 'root', null, null, null, false, true, 1, 8, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('script.type', 'script', 'script.type', 'Script Type', null, false, true, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('script.type.cmdb', 'script.type', 'script.type.cmdb', '[CMDB] CI 반영', null, false, true, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('script.type.document.attachFile', 'script.type', 'script.type.document.attachFile', '[문서편집] 첨부파일', null, false, true, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('token', 'root', null, '토큰 관련 코드', null, false, true, 1, 9, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('token.status', 'token', null, '토큰 상태 코드', null, false, true, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('token.status.finish', 'token.status', null, '처리 완료', null, false, true, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('token.status.running', 'token.status', null, '진행 중', null, false, true, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('token.status.waiting', 'token.status', null, '대기 중', null, false, true, 3, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user', 'root', null, '사용자', null, false, true, 1, 10, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.id', 'user', null, '아이디', null, false, true, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.name', 'user', null, '이름', null, false, true, 2, 2, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.department', 'user', null, '부서', null, false, true, 2, 3, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.position', 'user', null, '직책', null, false, true, 2, 4, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.extensionNumber', 'user', null, '내선번호', null, false, true, 2, 5, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.date', 'user', null, '사용자 날짜 포맷', null, false, true, 2, 6, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.date.yyyymmdd', 'user.date', 'yyyy-MM-dd', null, null, false, true, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.date.ddmmyyyy', 'user.date', 'dd-MM-yyyy', null, null, false, true, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.date.mmddyyyy', 'user.date', 'MM-dd-yyyy', null, null, false, true, 3, 3, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.date.yyyyddmm', 'user.date', 'yyyy-dd-MM', null, null, false, true, 3, 4, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.default', 'user', null, '기본 값', null, false, true, 2, 7, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.default.menu', 'user.default', null, '기본 메뉴', null, false, true, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.default.menu.dashboard', 'user.default.menu', 'dashboard', '개인 현황판 메뉴 아이디', null, true, true, 4, 1, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.default.role', 'user.default', null, '기본 역할', null, false, true, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.default.role.users.general', 'user.default.role', 'users.general', '역할 - 사용자 일반', null, false, true, 4, 1, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.default.url', 'user.default', null, '기본 URL', null, false, true, 3, 3, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.default.url.dashboard', 'user.default.url', '/dashboard/view', '개인 현황판 URL', null, true, true, 4, 1, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.lang', 'user', null, '언어', null, false, true, 2, 8, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.lang.ko', 'user.lang', 'ko', '한국어', null, false, true, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.lang.en', 'user.lang', 'en', '영어', null, false, true, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.platform', 'user', null, '플랫폼', null, false, true, 2, 9, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.platform.alice', 'user.platform', null, 'Alice', null, false, true, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.platform.google', 'user.platform', null, 'Google', null, false, true, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.platform.kakao', 'user.platform', null, 'Kakao', null, false, true, 3, 3, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.search', 'user', null, '검색 목록', null, false, true, 2, 10, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.search.department', 'user.search', null, '부서', null, false, true, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.search.extensionNumber', 'user.search', null, '내선번호', null, false, true, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.search.id', 'user.search', null, '아이디', null, false, true, 3, 3, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.search.mobileNumber', 'user.search', null, '핸드폰 번호', null, false, true, 3, 4, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.search.name', 'user.search', null, '이름', null, false, true, 3, 5, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.search.officeNumber', 'user.search', null, '사무실 번호', null, false, true, 3, 6, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.search.position', 'user.search', null, '직책', null, false, true, 3, 7, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.status', 'user', null, '계정 상태', null, false, true, 2, 11, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.status.certified', 'user.status', null, '인증 완료', null, false, true, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.status.signup', 'user.status', null, '가입', null, false, true, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.theme', 'user', null, '테마', null, false, true, 2, 12, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.theme.default', 'user.theme', 'default', '기본 테마', null, false, true, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.theme.dark', 'user.theme', 'dark', '어두운 테마', null, false, true, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.time', 'user', null, '사용자 시간 포맷', null, false, true, 2, 13, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('24', 'user.time', 'HH:mm', null, null, false, true, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('12', 'user.time', 'hh:mm a', null, null, false, true, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('department', 'root', null, '부서 관리', null, false, true, 1, 11, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('department.group', 'department', null, '부서 명', null, false, true, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('department.group.design', 'department.group', 'DESIGN', 'DESIGN', null, false, true, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('department.group.itsm', 'department.group', 'ITSM', 'ITSM', null, false, true, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('department.group.tc', 'department.group', 'TC', 'TC', null, false, true, 3, 3, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('version', 'root', null, null, null, false, true, 1, 12, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('version.workflow', 'version', '20200515', null, null, false, true, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('customCode', 'root', null, '커스텀코드', null, false, true, 1, 13, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('customCode.type', 'customCode', null, '신청서 목록', null, true, true, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('customCode.type.table', 'customCode.type', 'table', '테이블', null, true, true, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('customCode.type.code', 'customCode.type', 'code', '코드', null, true, true, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('customCode.operator', 'customCode', null, '연산자', null, true, true, 2, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('customCode.operator.equal', 'customCode.operator', 'equal', '=', null, true, true, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('customCode.operator.notEqual', 'customCode.operator', 'notEqual', '!=', null, true, true, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('customCode.sessionKey', 'customCode', null, '세션 사용 시 기본 값', null, true, true, 2, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('customCode.sessionKey.userKey', 'customCode.sessionKey', 'userKey', '사용자 키', null, true, true, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('customCode.sessionKey.userId', 'customCode.sessionKey', 'userId', '아이디', null, true, true, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('customCode.sessionKey.userName', 'customCode.sessionKey', 'userName', '사용자 이름', null, true, true, 3, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('customCode.sessionKey.email', 'customCode.sessionKey', 'email', '이메일', null, true, true, 3, 4, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('customCode.sessionKey.position', 'customCode.sessionKey', 'position', '직책', null, true, true, 3, 5, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('customCode.sessionKey.departmentName', 'customCode.sessionKey', 'departmentName', '부서', null, true, true, 3, 6, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('customCode.sessionKey.officeNumber', 'customCode.sessionKey', 'officeNumber', '사무실 번호', null, true, true, 3, 7, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('customCode.sessionKey.mobileNumber', 'customCode.sessionKey', 'mobileNumber', '휴대폰 번호', null, true, true, 3, 8, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('chart', 'root', null, 'CHART', null, false, true, 1, 14, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('chart.type', 'chart', null, 'CHART TYPE', null, true, true, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('chart.type.basicLine', 'chart.type', 'chart.basicLine', 'Basic Line Chart', null, true, true, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('chart.type.pie', 'chart.type', 'chart.pie', 'Pie Chart', null, true, true, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('chart.type.stackedColumn', 'chart.type', 'chart.stackedColumn', 'Stacked Column Chart', null, true, true, 3, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('chart.type.stackedBar', 'chart.type', 'chart.stackedBar', 'Stacked Bar Chart', null, true, true, 3, 4, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('chart.type.lineAndColumn', 'chart.type', 'chart.lineAndColumn', 'Line and Column Chart', null, true, true, 3, 5, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('chart.type.activityGauge', 'chart.type', 'chart.activityGauge', 'Activity Gauge Chart', null, true, true, 3, 6, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('chart.operation', 'chart', null, 'CHART OPERATION', null, true, true, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('chart.operation.count', 'chart.operation', 'count', '카운트', null, true, true, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('chart.operation.percent', 'chart.operation', 'percent', '퍼센트', null, true, true, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('chart.unit', 'chart', null, 'CHART UNIT', null, true, true, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('chart.unit.year', 'chart.unit', 'Y', '년', null, true, true, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('chart.unit.month', 'chart.unit', 'M', '월', null, true, true, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('chart.unit.day', 'chart.unit', 'D', '일', null, true, true, 3, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('chart.unit.hour', 'chart.unit', 'H', '시간', null, true, true, 3, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('chart.range', 'chart', null, 'CHART RANGE', null, false, true, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('chart.range.between', 'chart.range', 'chart.range.between', '기간 지정', null, false, true, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('chart.range.last.month', 'chart.range', 'chart.range.last.month', '지난 달', null, false, true, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('chart.range.last.day', 'chart.range', 'chart.range.last.day', '어제', null, false, true, 3, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('chart.range.all', 'chart.range', 'chart.range.all', '전체', null, false, true, 3, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('instance', 'root', null, '인스턴스', null, true, true, 1, 15, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('instance.platform', 'instance', null, '인스턴스 플랫폼', null, false, true, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('instance.platform.itsm', 'instance.platform', 'ITSM', 'ITSM', null, faLse, true, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('instance.platform.api', 'instance.platform', 'API', 'API', null, faLse, true, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);

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

insert into awf_custom_code values ('40288a19736b46fb01736b89e46c0008', '사용자 이름 검색', 'table', 'awf_user', 'user_name', 'user_key', null, '[]', 'userName', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_custom_code values ('40288a19736b46fb01736b89e46c0009', '사용자 부서 검색', 'code', null, null, null, 'department.group', null ,'departmentName', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_custom_code values ('40288ab777dd21b50177dd52781e0000', '데이터베이스', 'code', null, null, null, 'cmdb.db.kind', null ,'', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_custom_code values ('40288a19736b46fb01736b89e46c0010', '서비스데스크 - 단순문의 : 서비스 항목', 'code', null, null, null, 'form.template.serviceDesk.inquiry.category', null ,'', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_custom_code values ('40288a19736b46fb01736b89e46c0011', '서비스데스크 - 장애신고 : 장애유형', 'code', null, null, null, 'form.template.serviceDesk.incident.category', null ,'', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_custom_code values ('40288a19736b46fb01736b89e46c0012', '서비스데스크 - 서비스요청 : 요청구분', 'code', null, null, null, 'form.template.serviceDesk.request.category', null ,'', '0509e09412534a6e98f04ca79abb6424', now(), null, null);

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
insert into awf_custom_code_column values ('awf_user', 'search', 'use_yn', '사용자 사용여부');
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

insert into awf_menu values ('dashboard', 'menu', '/dashboard/view', 1, 'TRUE');
insert into awf_menu values ('token', 'menu', '/tokens/search', 2, 'TRUE');
insert into awf_menu values ('document', 'menu', '/documents/search', 3, 'TRUE');
insert into awf_menu values ('notice', 'menu', '/notices/search', 4, 'TRUE');
insert into awf_menu values ('faq', 'menu', '/faqs/search', 5, 'TRUE');
insert into awf_menu values ('download', 'menu', '/downloads/search', 6, 'TRUE');
insert into awf_menu values ('board', 'menu', '/boards/articles/search', 7, 'TRUE');
insert into awf_menu values ('report', 'menu', '', 8, 'TRUE');
insert into awf_menu values ('report.template', 'report', '/reports/template/search', 1, 'TRUE');
insert into awf_menu values ('report.report', 'report', '/reports/report/search', 2, 'TRUE');
insert into awf_menu values ('report.chart', 'report', '/charts/search', 3, 'TRUE');
insert into awf_menu values ('workflow', 'menu', '', 9, 'TRUE');
insert into awf_menu values ('workflow.process', 'workflow', '/processes/search', 1, 'TRUE');
insert into awf_menu values ('workflow.form', 'workflow', '/forms/search', 2, 'TRUE');
insert into awf_menu values ('workflow.workflowAdmin', 'workflow', '/workflows/search', 3, 'TRUE');
insert into awf_menu values ('workflow.customCode', 'workflow', '/custom-codes/search', 4, 'TRUE');
insert into awf_menu values ('workflow.image', 'workflow', '/images', 5, 'TRUE');
insert into awf_menu values ('workflow.numberingPattern', 'workflow', '/numberingPatterns/search', 6, 'TRUE');
insert into awf_menu values ('workflow.numberingRule', 'workflow', '/numberingRules/search', 7, 'TRUE');
insert into awf_menu values ('cmdb', 'menu', '', 10, 'TRUE');
insert into awf_menu values ('cmdb.attribute', 'cmdb', '/cmdb/attributes/search', 1, 'TRUE');
insert into awf_menu values ('cmdb.class', 'cmdb', '/cmdb/class/edit', 2, 'TRUE');
insert into awf_menu values ('cmdb.type', 'cmdb', '/cmdb/types/edit', 3, 'TRUE');
insert into awf_menu values ('cmdb.ci', 'cmdb', '/cmdb/cis/search', 4, 'TRUE');
insert into awf_menu values ('config', 'menu', '', 11, 'TRUE');
insert into awf_menu values ('config.user', 'config', '/users/search', 1, 'TRUE');
insert into awf_menu values ('config.auth', 'config', '/auths/search', 2, 'TRUE');
insert into awf_menu values ('config.role', 'config', '/roles/search', 3, 'TRUE');
insert into awf_menu values ('config.boardAdmin', 'config', '/boards/search', 4, 'TRUE');
insert into awf_menu values ('config.code', 'config', '/codes/edit', 5, 'TRUE');
insert into awf_menu values ('config.scheduler', 'config', '/schedulers/search', 6, 'TRUE');
insert into awf_menu values ('config.product', 'config', '', 7, 'TRUE');

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

insert into awf_menu_auth_map values ('config', 'user.read');
insert into awf_menu_auth_map values ('config', 'user.create');
insert into awf_menu_auth_map values ('config', 'user.update');
insert into awf_menu_auth_map values ('config', 'user.delete');
insert into awf_menu_auth_map values ('config.auth', 'auth.read');
insert into awf_menu_auth_map values ('config.auth', 'auth.create');
insert into awf_menu_auth_map values ('config.auth', 'auth.update');
insert into awf_menu_auth_map values ('config.auth', 'auth.delete');
insert into awf_menu_auth_map values ('config.boardAdmin', 'board.admin.read');
insert into awf_menu_auth_map values ('config.boardAdmin', 'board.admin.create');
insert into awf_menu_auth_map values ('config.boardAdmin', 'board.admin.update');
insert into awf_menu_auth_map values ('config.boardAdmin', 'board.admin.delete');
insert into awf_menu_auth_map values ('config.code', 'code.read');
insert into awf_menu_auth_map values ('config.code', 'code.create');
insert into awf_menu_auth_map values ('config.code', 'code.update');
insert into awf_menu_auth_map values ('config.code', 'code.delete');
insert into awf_menu_auth_map values ('config.role', 'role.read');
insert into awf_menu_auth_map values ('config.role', 'role.create');
insert into awf_menu_auth_map values ('config.role', 'role.update');
insert into awf_menu_auth_map values ('config.role', 'role.delete');
insert into awf_menu_auth_map values ('config.user', 'user.read');
insert into awf_menu_auth_map values ('config.user', 'user.create');
insert into awf_menu_auth_map values ('config.user', 'user.update');
insert into awf_menu_auth_map values ('config.user', 'user.delete');
insert into awf_menu_auth_map values ('config.scheduler', 'scheduler.read');
insert into awf_menu_auth_map values ('config.scheduler', 'scheduler.create');
insert into awf_menu_auth_map values ('config.scheduler', 'scheduler.update');
insert into awf_menu_auth_map values ('config.scheduler', 'scheduler.delete');
insert into awf_menu_auth_map values ('config.product', 'product.read');
insert into awf_menu_auth_map values ('dashboard', 'document.read');
insert into awf_menu_auth_map values ('dashboard', 'document.create');
insert into awf_menu_auth_map values ('dashboard', 'document.update');
insert into awf_menu_auth_map values ('dashboard', 'document.delete');
insert into awf_menu_auth_map values ('document', 'document.read');
insert into awf_menu_auth_map values ('document', 'document.create');
insert into awf_menu_auth_map values ('document', 'document.update');
insert into awf_menu_auth_map values ('document', 'document.delete');
insert into awf_menu_auth_map values ('token', 'token.read');
insert into awf_menu_auth_map values ('token', 'token.create');
insert into awf_menu_auth_map values ('board', 'board.read');
insert into awf_menu_auth_map values ('board', 'board.create');
insert into awf_menu_auth_map values ('board', 'board.update');
insert into awf_menu_auth_map values ('board', 'board.delete');
insert into awf_menu_auth_map values ('download', 'download.read');
insert into awf_menu_auth_map values ('download', 'download.create');
insert into awf_menu_auth_map values ('download', 'download.update');
insert into awf_menu_auth_map values ('download', 'download.delete');
insert into awf_menu_auth_map values ('faq', 'faq.read');
insert into awf_menu_auth_map values ('faq', 'faq.create');
insert into awf_menu_auth_map values ('faq', 'faq.update');
insert into awf_menu_auth_map values ('faq', 'faq.delete');
insert into awf_menu_auth_map values ('notice', 'notice.read');
insert into awf_menu_auth_map values ('notice', 'notice.create');
insert into awf_menu_auth_map values ('notice', 'notice.update');
insert into awf_menu_auth_map values ('notice', 'notice.delete');
insert into awf_menu_auth_map values ('workflow', 'custom.code.read');
insert into awf_menu_auth_map values ('workflow', 'custom.code.create');
insert into awf_menu_auth_map values ('workflow', 'custom.code.update');
insert into awf_menu_auth_map values ('workflow', 'custom.code.delete');
insert into awf_menu_auth_map values ('workflow', 'document.admin.read');
insert into awf_menu_auth_map values ('workflow', 'document.admin.create');
insert into awf_menu_auth_map values ('workflow', 'document.admin.update');
insert into awf_menu_auth_map values ('workflow', 'document.admin.delete');
insert into awf_menu_auth_map values ('workflow', 'form.read');
insert into awf_menu_auth_map values ('workflow', 'form.create');
insert into awf_menu_auth_map values ('workflow', 'form.update');
insert into awf_menu_auth_map values ('workflow', 'form.delete');
insert into awf_menu_auth_map values ('workflow', 'image.read');
insert into awf_menu_auth_map values ('workflow', 'image.create');
insert into awf_menu_auth_map values ('workflow', 'image.update');
insert into awf_menu_auth_map values ('workflow', 'image.delete');
insert into awf_menu_auth_map values ('workflow', 'process.read');
insert into awf_menu_auth_map values ('workflow', 'process.create');
insert into awf_menu_auth_map values ('workflow', 'process.update');
insert into awf_menu_auth_map values ('workflow', 'process.delete');
insert into awf_menu_auth_map values ('workflow.customCode', 'custom.code.read');
insert into awf_menu_auth_map values ('workflow.customCode', 'custom.code.create');
insert into awf_menu_auth_map values ('workflow.customCode', 'custom.code.update');
insert into awf_menu_auth_map values ('workflow.customCode', 'custom.code.delete');
insert into awf_menu_auth_map values ('workflow.workflowAdmin', 'document.admin.read');
insert into awf_menu_auth_map values ('workflow.workflowAdmin', 'document.admin.create');
insert into awf_menu_auth_map values ('workflow.workflowAdmin', 'document.admin.update');
insert into awf_menu_auth_map values ('workflow.workflowAdmin', 'document.admin.delete');
insert into awf_menu_auth_map values ('workflow.form', 'form.read');
insert into awf_menu_auth_map values ('workflow.form', 'form.create');
insert into awf_menu_auth_map values ('workflow.form', 'form.update');
insert into awf_menu_auth_map values ('workflow.form', 'form.delete');
insert into awf_menu_auth_map values ('workflow.image', 'image.read');
insert into awf_menu_auth_map values ('workflow.image', 'image.create');
insert into awf_menu_auth_map values ('workflow.image', 'image.update');
insert into awf_menu_auth_map values ('workflow.image', 'image.delete');
insert into awf_menu_auth_map values ('workflow.numberingPattern', 'numbering.pattern.read');
insert into awf_menu_auth_map values ('workflow.numberingPattern', 'numbering.pattern.create');
insert into awf_menu_auth_map values ('workflow.numberingPattern', 'numbering.pattern.update');
insert into awf_menu_auth_map values ('workflow.numberingPattern', 'numbering.pattern.delete');
insert into awf_menu_auth_map values ('workflow.numberingRule', 'numbering.rule.read');
insert into awf_menu_auth_map values ('workflow.numberingRule', 'numbering.rule.create');
insert into awf_menu_auth_map values ('workflow.numberingRule', 'numbering.rule.update');
insert into awf_menu_auth_map values ('workflow.numberingRule', 'numbering.rule.delete');
insert into awf_menu_auth_map values ('workflow.process', 'process.read');
insert into awf_menu_auth_map values ('workflow.process', 'process.create');
insert into awf_menu_auth_map values ('workflow.process', 'process.update');
insert into awf_menu_auth_map values ('workflow.process', 'process.delete');
insert into awf_menu_auth_map values ('cmdb', 'cmdb.attribute.read');
insert into awf_menu_auth_map values ('cmdb', 'cmdb.attribute.create');
insert into awf_menu_auth_map values ('cmdb', 'cmdb.attribute.update');
insert into awf_menu_auth_map values ('cmdb', 'cmdb.attribute.delete');
insert into awf_menu_auth_map values ('cmdb', 'cmdb.type.read');
insert into awf_menu_auth_map values ('cmdb', 'cmdb.type.create');
insert into awf_menu_auth_map values ('cmdb', 'cmdb.type.update');
insert into awf_menu_auth_map values ('cmdb', 'cmdb.type.delete');
insert into awf_menu_auth_map values ('cmdb.attribute', 'cmdb.attribute.read');
insert into awf_menu_auth_map values ('cmdb.attribute', 'cmdb.attribute.create');
insert into awf_menu_auth_map values ('cmdb.attribute', 'cmdb.attribute.update');
insert into awf_menu_auth_map values ('cmdb.attribute', 'cmdb.attribute.delete');
insert into awf_menu_auth_map values ('cmdb.ci', 'cmdb.ci.read');
insert into awf_menu_auth_map values ('cmdb.ci', 'cmdb.ci.create');
insert into awf_menu_auth_map values ('cmdb.ci', 'cmdb.ci.update');
insert into awf_menu_auth_map values ('cmdb.ci', 'cmdb.ci.delete');
insert into awf_menu_auth_map values ('cmdb.class', 'cmdb.class.read');
insert into awf_menu_auth_map values ('cmdb.class', 'cmdb.class.create');
insert into awf_menu_auth_map values ('cmdb.class', 'cmdb.class.update');
insert into awf_menu_auth_map values ('cmdb.class', 'cmdb.class.delete');
insert into awf_menu_auth_map values ('cmdb.type', 'cmdb.type.read');
insert into awf_menu_auth_map values ('cmdb.type', 'cmdb.type.create');
insert into awf_menu_auth_map values ('cmdb.type', 'cmdb.type.update');
insert into awf_menu_auth_map values ('cmdb.type', 'cmdb.type.delete');
insert into awf_menu_auth_map values ('report', 'report.read');
insert into awf_menu_auth_map values ('report', 'report.create');
insert into awf_menu_auth_map values ('report', 'report.update');
insert into awf_menu_auth_map values ('report', 'report.delete');
insert into awf_menu_auth_map values ('report.template', 'report.read');
insert into awf_menu_auth_map values ('report.template', 'report.create');
insert into awf_menu_auth_map values ('report.template', 'report.update');
insert into awf_menu_auth_map values ('report.template', 'report.delete');
insert into awf_menu_auth_map values ('report.report', 'report.read');
insert into awf_menu_auth_map values ('report.report', 'report.create');
insert into awf_menu_auth_map values ('report.report', 'report.update');
insert into awf_menu_auth_map values ('report.report', 'report.delete');
insert into awf_menu_auth_map values ('report.chart', 'chart.read');
insert into awf_menu_auth_map values ('report.chart', 'chart.create');
insert into awf_menu_auth_map values ('report.chart', 'chart.update');
insert into awf_menu_auth_map values ('report.chart', 'chart.delete');

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
insert into awf_numbering_rule values ('40125c91714df6c325714e053c890125', 'CSR-yyyyMMdd-000', '서비스데스크 문서번호');
insert into awf_numbering_rule values ('40288ab7772dae0301772dbca28a0004', 'SAT-yyyyMMdd-000', '만족도문서번호');
insert into awf_numbering_rule values ('40288ab777f04ed90177f05e5ad7000a', 'CFG-yyyyMMdd-000', '구성관리 문서번호');
insert into awf_numbering_rule values ('4028b8817880d833017880f34ae10003', 'REL_yyyyMMdd-000', '릴리즈관리 문서번호');
insert into awf_numbering_rule values ('4028b25d7886e2d801788704dd8e0002', 'RFC-yyyyMMdd-000', '인프라, 어플리케이션 변경관리에서 사용되는 문서번호');
insert into awf_numbering_rule values ('4028b88178c0fcc60178c10dbb5b0003', 'INC-yyyyMMdd-000', '장애관리 문서번호');
insert into awf_numbering_rule values ('4028b88178c01b660178c0cc91310004', 'PBM-yyyyMMdd-000', '문제관리 문서번호');

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
insert into awf_numbering_pattern values ('4028b88178c01b660178c0cbe02d0003', '문제관리 Prefix', 'numbering.pattern.text', '{"value":"PBM"}');
insert into awf_numbering_pattern values ('4028b88178c0fcc60178c10d270c0002', '장애관리 PreFix', 'numbering.pattern.text', '{"value":"INC"}');
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
insert into awf_role values ('notice.all', '공지사항 관리자', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_role values ('users.general', '사용자일반', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_role values ('role.view', '역할 사용자', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_role values ('notice.view', '공지사항 사용자', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_role values ('admin', '관리자', '전체관리자', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_role values ('users.manager', '사용자관리자', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_role values ('faq.all', 'FAQ관리자', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_role values ('auth.all', '권한 관리자', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
/* 단순문의 */
insert into awf_role values ('serviceDesk.assignee', '서비스데스크 담당자', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_role values ('serviceDesk.manager', '서비스데스크 관리자', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
/* 장애관리 */
insert into awf_role values ('incident.assignee', '장애 담당자', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_role values ('incident.manager', '장애 관리자', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
/* 변경관리 */
insert into awf_role values ('application.change.assignee', '어플리케이션 변경 담당자', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_role values ('application.change.manager', '어플리케이션 변경 관리자', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_role values ('infra.change.assignee', '인프라 변경 담당자', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_role values ('infra.change.manager', '인프라 변경 관리자', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);

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
insert into awf_role_auth_map values ('admin', 'product.read');
insert into awf_role_auth_map values ('admin', 'report.create');
insert into awf_role_auth_map values ('admin', 'report.update');
insert into awf_role_auth_map values ('admin', 'report.read');
insert into awf_role_auth_map values ('admin', 'report.delete');
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
insert into awf_role_auth_map values ('users.general', 'token.read');
insert into awf_role_auth_map values ('users.general', 'token.create');
insert into awf_role_auth_map values ('users.general', 'document.read');
insert into awf_role_auth_map values ('users.general', 'document.create');
insert into awf_role_auth_map values ('users.general', 'notice.read');
insert into awf_role_auth_map values ('users.general', 'faq.read');
insert into awf_role_auth_map values ('users.general', 'download.read');
insert into awf_role_auth_map values ('users.general', 'board.read');
insert into awf_role_auth_map values ('users.general', 'code.read');
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
/* 단순문의 - 서비스데스크 담당자, 서비스데스크 관리자 */
insert into awf_role_auth_map values ('serviceDesk.assignee', 'document.read');
insert into awf_role_auth_map values ('serviceDesk.assignee', 'document.create');
insert into awf_role_auth_map values ('serviceDesk.assignee', 'token.create');
insert into awf_role_auth_map values ('serviceDesk.assignee', 'token.read');
insert into awf_role_auth_map values ('serviceDesk.manager', 'document.create');
insert into awf_role_auth_map values ('serviceDesk.manager', 'document.read');
insert into awf_role_auth_map values ('serviceDesk.manager', 'token.create');
insert into awf_role_auth_map values ('serviceDesk.manager', 'token.read');
/* 장애관리 - 장애 담당자, 장애 관리자 */
insert into awf_role_auth_map values ('incident.assignee', 'document.read');
insert into awf_role_auth_map values ('incident.assignee', 'document.create');
insert into awf_role_auth_map values ('incident.assignee', 'token.create');
insert into awf_role_auth_map values ('incident.assignee', 'token.read');
insert into awf_role_auth_map values ('incident.manager', 'document.create');
insert into awf_role_auth_map values ('incident.manager', 'document.read');
insert into awf_role_auth_map values ('incident.manager', 'token.create');
insert into awf_role_auth_map values ('incident.manager', 'token.read');
/* 변경관리 - 담당자, 관리자 */
insert into awf_role_auth_map values ('application.change.assignee', 'document.read');
insert into awf_role_auth_map values ('application.change.assignee', 'document.create');
insert into awf_role_auth_map values ('application.change.assignee', 'token.create');
insert into awf_role_auth_map values ('application.change.assignee', 'token.read');
insert into awf_role_auth_map values ('application.change.manager', 'document.create');
insert into awf_role_auth_map values ('application.change.manager', 'document.read');
insert into awf_role_auth_map values ('application.change.manager', 'token.create');
insert into awf_role_auth_map values ('application.change.manager', 'token.read');
insert into awf_role_auth_map values ('infra.change.assignee', 'document.read');
insert into awf_role_auth_map values ('infra.change.assignee', 'document.create');
insert into awf_role_auth_map values ('infra.change.assignee', 'token.create');
insert into awf_role_auth_map values ('infra.change.assignee', 'token.read');
insert into awf_role_auth_map values ('infra.change.manager', 'document.create');
insert into awf_role_auth_map values ('infra.change.manager', 'document.read');
insert into awf_role_auth_map values ('infra.change.manager', 'token.create');
insert into awf_role_auth_map values ('infra.change.manager', 'token.read');

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
insert into awf_rule_pattern_map values ('4028b88178c01b660178c0cc91310004', '4028b88178c01b660178c0cbe02d0003', 0);
insert into awf_rule_pattern_map values ('4028b88178c01b660178c0cc91310004', '7a112d61751fs6f325714q053c421412', 1);
insert into awf_rule_pattern_map values ('4028b88178c01b660178c0cc91310004', '8a112d61751fs6f325714q053c421413', 2);
insert into awf_rule_pattern_map values ('4028b88178c0fcc60178c10dbb5b0003', '4028b88178c0fcc60178c10d270c0002', 0);
insert into awf_rule_pattern_map values ('4028b88178c0fcc60178c10dbb5b0003', '7a112d61751fs6f325714q053c421412', 1);
insert into awf_rule_pattern_map values ('4028b88178c0fcc60178c10dbb5b0003', '8a112d61751fs6f325714q053c421413', 2);
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
	execute_command varchar(1024),
	execute_cycle_type varchar(100),
	execute_cycle_period bigint,
	cron_expression varchar(128),
	args varchar(128),
	src varchar(512),
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
COMMENT ON COLUMN awf_scheduled_task_mst.execute_command IS '실행명령어';
COMMENT ON COLUMN awf_scheduled_task_mst.execute_cycle_type IS '실행주기유형';
COMMENT ON COLUMN awf_scheduled_task_mst.execute_cycle_period IS '실행주기간격';
COMMENT ON COLUMN awf_scheduled_task_mst.cron_expression IS '크론표현식';
COMMENT ON COLUMN awf_scheduled_task_mst.args IS 'arguments';
COMMENT ON COLUMN awf_scheduled_task_mst.src IS '경로';
COMMENT ON COLUMN awf_scheduled_task_mst.create_user_key IS '등록자';
COMMENT ON COLUMN awf_scheduled_task_mst.create_dt IS '등록일';
COMMENT ON COLUMN awf_scheduled_task_mst.update_user_key IS '수정자';
COMMENT ON COLUMN awf_scheduled_task_mst.update_dt IS '수정일';

insert into awf_scheduled_task_mst values ('4028b2647aada23c017aadd37b0c0001', '임시 첨부 파일 삭제', 'jar', '첨부된 파일 중 임시 저장된 파일을 삭제합니다.', 'TRUE', 'FALSE', null, null, 'java -jar deleteTempFile.jar', 'cron', null, '0 0 18 * * ?', null, '/deleteTempFile', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_scheduled_task_mst values ('4028b2647aada23c017aadcceabf0000', 'CMDB CI 임시데이터 삭제', 'jar', 'CMDB CI 등록시 저장된 임시 데이터 중 사용되지 않은 데이터를 삭제한다.', 'TRUE', 'FALSE', null, null, 'java -jar deleteTempCIData.jar', 'cron', null, '0 0 18 * * ?', null, '/deleteTempCIData', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_scheduled_task_mst values ('4028b2647a9890d5017a98a94efb0000', 'Zenius EMS 연동', 'jar', 'Zenius EMS 7 과 연동하여 자산 정보를 수집한다.', 'TRUE', 'FALSE', null, null, 'java -jar alice-ems.jar', 'cron', null, '0 0 18 * * ?', null, '/zeniusEms', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_scheduled_task_mst values ('4028b2647aadd869017aadf4cf830000', 'Access Token 삭제', 'query', '기간이 초과된 access token 을 삭제한다.', 'TRUE', 'FALSE', null, 'delete from awf_api_token
where create_dt < now() - interval ''10day''', null, 'cron', null, '0 0 18 * * ?', null, null, '0509e09412534a6e98f04ca79abb6424', now(), null, null);

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

insert into awf_url values ('/auths/search', 'get', '권한 검색화면', 'TRUE');
insert into awf_url values ('/auths/new', 'get', '권한 등록', 'TRUE');
insert into awf_url values ('/auths/{id}/edit', 'get', '권한 수정', 'TRUE');
insert into awf_url values ('/auths/{id}/view', 'get', '권한 상세 보기', 'TRUE');
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
insert into awf_url values ('/charts', 'get', '사용자 정의 차트 목록', 'TRUE');
insert into awf_url values ('/charts/search', 'get', '사용자 정의 차트 목록 조회 화면', 'TRUE');
insert into awf_url values ('/charts/new', 'get', '사용자 정의 차트 등록 화면', 'TRUE');
insert into awf_url values ('/charts/{id}/edit', 'get', '사용자 정의 차트 수정 화면', 'TRUE');
insert into awf_url values ('/charts/{id}/view', 'get', '사용자 정의 차트 조회 화면', 'TRUE');
insert into awf_url values ('/cmdb/attributes', 'get', 'CMDB Attribute 관리 목록', 'TRUE');
insert into awf_url values ('/cmdb/attributes/new', 'get', 'CMDB Attribute 등록 화면', 'TRUE');
insert into awf_url values ('/cmdb/attributes/search', 'get', 'CMDB Attribute 관리 조회 화면', 'TRUE');
insert into awf_url values ('/cmdb/attributes/{id}/edit', 'get', 'CMDB Attribute 수정 화면', 'TRUE');
insert into awf_url values ('/cmdb/attributes/{id}/view', 'get', 'CMDB Attribute 보기 화면', 'TRUE');
insert into awf_url values ('/cmdb/attributes/list-modal', 'get', 'CMDB Attribute 목록 모달 화면', 'TRUE');
insert into awf_url values ('/cmdb/class/edit', 'get', 'CMDB Class 편집 화면', 'TRUE');
insert into awf_url values ('/cmdb/class/view-pop/attributes', 'get', 'CMDB Class Attribute 모달 리스트 화면', 'TRUE');
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
insert into awf_url values ('/workflows/{id}/display', 'get', '신청서 디스플레이 데이터 조회', 'TRUE');
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
insert into awf_url values ('/forms/{id}/edit', 'get', '폼 디자이너 편집화면', 'TRUE');
insert into awf_url values ('/forms/{id}/view', 'get', '폼 디자이너 상세화면', 'TRUE');
insert into awf_url values ('/forms/{id}/preview', 'get', '폼 디자이너 미리보기 화면', 'TRUE');
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
insert into awf_url values ('/numberingPatterns/search', 'get', '패턴 검색화면', 'TRUE');
insert into awf_url values ('/numberingPatterns/new', 'get', '패턴 등록', 'TRUE');
insert into awf_url values ('/numberingPatterns/{id}/edit', 'get', '패턴 수정', 'TRUE');
insert into awf_url values ('/numberingPatterns/{id}/view', 'get', '패턴 상세 보기', 'TRUE');
insert into awf_url values ('/numberingRules', 'get', '문서번호 관리 목록 뷰', 'TRUE');
insert into awf_url values ('/numberingRules/search', 'get', '문서번호 검색화면', 'TRUE');
insert into awf_url values ('/numberingRules/new', 'get', '문서번호 등록', 'TRUE');
insert into awf_url values ('/numberingRules/{id}/edit', 'get', '문서번호 수정', 'TRUE');
insert into awf_url values ('/numberingRules/{id}/view', 'get', '문서번호 상세 보기', 'TRUE');
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
insert into awf_url values ('/reports/report', 'get', '보고서 조회', 'TRUE');
insert into awf_url values ('/reports/report/search', 'get', '보고서 조회 검색 화면 호출', 'TRUE');
insert into awf_url values ('/reports/report/{id}/view', 'get', '보고서 상세화면', 'TRUE');
insert into awf_url values ('/reports/template', 'get', '템플릿 설정 목록 조회', 'TRUE');
insert into awf_url values ('/reports/template/new', 'get', '템플릿 설정 등록', 'TRUE');
insert into awf_url values ('/reports/template/preview', 'get', '템플릿 미리보기', 'true');
insert into awf_url values ('/reports/template/search', 'get', '템플릿 설정 검색 화면 호출', 'TRUE');
insert into awf_url values ('/reports/template/{id}/edit', 'get', '템플릿 설정 수정', 'TRUE');
insert into awf_url values ('/reports/template/{id}/view', 'get', '템플릿 설정 미리보기', 'TRUE');
insert into awf_url values ('/rest/reports/template', 'post', '템플릿 설정 등록 처리', 'true');
insert into awf_url values ('/rest/reports/template/charts', 'get', '템플릿 차트 데이터 조회', 'true');
insert into awf_url values ('/rest/reports/template/{id}', 'delete', '템플릿 설정 삭제 처리', 'true');
insert into awf_url values ('/rest/reports/template/{id}', 'post', '보고서 생성 (임시)', 'true');
insert into awf_url values ('/rest/reports/template/{id}', 'put', '템플릿 설정 수정 처리', 'true');
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
insert into awf_url values ('/rest/charts', 'post', '사용자 정의 차트 등록', 'TRUE');
insert into awf_url values ('/rest/charts/{id}', 'put', '사용자 정의 차트 수정', 'TRUE');
insert into awf_url values ('/rest/charts/{id}', 'delete', '사용자 정의 차트 삭제', 'TRUE');
insert into awf_url values ('/rest/charts/{id}/preview', 'post', '사용자 정의 차트 미리보기', 'TRUE');
insert into awf_url values ('/rest/cmdb/attributes', 'get', 'CMDB Attribute 조회', 'FALSE');
insert into awf_url values ('/rest/cmdb/attributes', 'post', 'CMDB Attribute 등록', 'TRUE');
insert into awf_url values ('/rest/cmdb/attributes/{id}', 'put', 'CMDB Attribute 수정', 'TRUE');
insert into awf_url values ('/rest/cmdb/attributes/{id}', 'delete', 'CMDB Attribute 삭제', 'TRUE');
insert into awf_url values ('/rest/cmdb/cis', 'get', 'CI 전체 조회', 'TRUE');
insert into awf_url values ('/rest/cmdb/cis/{id}/data', 'post', 'CI 컴포넌트 - CI 세부 정보 등록', 'FALSE');
insert into awf_url values ('/rest/cmdb/cis/{id}/data', 'get', 'CI 컴포넌트 - CI 컴포넌트 세부 정보 조회', 'FALSE');
insert into awf_url values ('/rest/cmdb/cis/{id}/relation', 'get', 'CI 연관 관계 데이터 조회', 'FALSE');
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
insert into awf_url values ('/rest/codes/related/{id}', 'get', '연관 코드 상세 조회', 'TRUE');
insert into awf_url values ('/rest/comments', 'post', 'Comment 저장', 'FALSE');
insert into awf_url values ('/rest/comments/{id}', 'delete', 'Comment 삭제', 'FALSE');
insert into awf_url values ('/rest/custom-codes', 'get', '커스텀 코드 조회', 'FALSE');
insert into awf_url values ('/rest/custom-codes', 'put', '커스텀 코드 수정', 'TRUE');
insert into awf_url values ('/rest/custom-codes', 'post', '커스텀 코드 등록', 'TRUE');
insert into awf_url values ('/rest/custom-codes/{id}', 'delete', '커스텀 코드 삭제', 'TRUE');
insert into awf_url values ('/rest/custom-codes/{id}', 'get', '커스텀코드 목록 조회', 'TRUE');
insert into awf_url values ('/rest/dashboard/statistic', 'get', '업무 통계 조회', 'FALSE');
insert into awf_url values ('/rest/documents', 'get', '신청서 문서 목록 조회', 'TRUE');
insert into awf_url values ('/rest/workflows', 'post', '신청서 작성', 'TRUE');
insert into awf_url values ('/rest/workflows/{id}', 'delete', '신청서 삭제', 'TRUE');
insert into awf_url values ('/rest/workflows/{id}', 'get', '신청서 데이터 조회', 'TRUE');
insert into awf_url values ('/rest/workflows/{id}', 'put', '신청서 수정', 'TRUE');
insert into awf_url values ('/rest/workflows/{id}/display', 'put', '신청서 디스플레이 데이터 저장', 'TRUE');
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
insert into awf_url values ('/rest/folders/{folderId}/instances/{instanceId}', 'delete', '폴더 삭제', false);
insert into awf_url values ('/rest/folders/{folderId}', 'get', '폴더조회', false);
insert into awf_url values ('/rest/forms/{id}', 'delete', '폼 디자이너 삭제', 'TRUE');
insert into awf_url values ('/rest/forms/{id}/data', 'get', '폼 디자이너 세부 정보 불러오기', 'TRUE');
insert into awf_url values ('/rest/forms/{id}/data', 'put', '폼 디자이너 세부 정보 저장', 'TRUE');
insert into awf_url values ('/rest/forms', 'post', '폼 디자이너 기본 정보 저장 / 다른 이름 저장 처리', 'TRUE');
insert into awf_url values ('/rest/forms/{id}', 'put', '폼 디자이너 기본 정보 수정', 'TRUE');
insert into awf_url values ('/rest/images', 'post', '이미지 업로드', 'TRUE');
insert into awf_url values ('/rest/images', 'put', '이미지명 수정', 'TRUE');
insert into awf_url values ('/rest/images/{id}', 'get', '이미지 조회', 'FALSE');
insert into awf_url values ('/rest/images/{id}', 'delete', '이미지 삭제', 'TRUE');
insert into awf_url values ('/rest/images', 'get', '이미지 전체 조회', 'FALSE');
insert into awf_url values ('/rest/instances/{instanceId}/history', 'get', '문서 이력조회', false);
insert into awf_url values ('/rest/instances/{instanceId}/comments', 'get', '댓글 조회', false);
insert into awf_url values ('/rest/instances/{instanceId}/comments', 'post', '댓글 등록', false);
insert into awf_url values ('/rest/instances/{instanceId}/comments/{commentId}', 'delete', '댓글 삭제', false);
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
insert into awf_url values ('/rest/portals/filelist', 'get', '포탈 상세 파일 리스트 조회', 'FALSE');
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
insert into awf_url values ('/rest/tags/whitelist', 'get', 'Tag 추천 목록 조회', 'FALSE');
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
insert into awf_url values ('/rest/users/colors', 'get', '사용자 정의 색상 조회', 'FALSE');
insert into awf_url values ('/rest/users/colors', 'put', '사용자 정의 색상 저장', 'FALSE');
insert into awf_url values ('/rest/products/info', 'get', '제품 정보 조회', 'TRUE');
insert into awf_url values ('/roles/search', 'get', '역할 검색화면', 'TRUE');
insert into awf_url values ('/roles/new', 'get', '역할 등록', 'TRUE');
insert into awf_url values ('/roles/{id}/edit', 'get', '역할 수정', 'TRUE');
insert into awf_url values ('/roles/{id}/view', 'get', '역할 상세 보기', 'TRUE');
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
insert into awf_url values ('/tokens/{id}/edit', 'get', '', 'TRUE');
insert into awf_url values ('/tokens/{id}/view', 'get', '', 'TRUE');
insert into awf_url values ('/tokens/{id}/print', 'get', '처리할 문서 프린트 화면', 'TRUE');
insert into awf_url values ('/tokens/{id}/view-pop', 'get', '관련문서 팝업 화면', 'TRUE');
insert into awf_url values ('/tokens/{tokenId}/tokenTab','get','문서조회 탭화면', true);
insert into awf_url values ('/users', 'get', '사용자 조회 목록 화면', 'TRUE');
insert into awf_url values ('/users/new', 'get', '사용자 등록 화면', 'TRUE');
insert into awf_url values ('/users/search', 'get', '사용자 검색, 목록 등 메인이 되는 조회 화면', 'TRUE');
insert into awf_url values ('/users/{userkey}/view', 'get', '사용자 정보 조회 화면', 'TRUE');
insert into awf_url values ('/users/{userkey}/edit', 'get', '사용자 정보 수정 화면', 'TRUE');
insert into awf_url values ('/users/{userkey}/editself', 'get', '사용자 자기 정보 수정 화면', 'FALSE');
insert into awf_url values ('/rest/users/updatePassword','put', '비밀번호 변경', 'FALSE');
insert into awf_url values ('/rest/users/nextTime','put', '비밀번호 다음에 변경하기', 'FALSE');
insert into awf_url values ('/rest/tokens/todoCount', 'get', '문서함카운트', 'FALSE');

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

insert into awf_url_auth_map values ('/auths/search', 'get', 'auth.read');
insert into awf_url_auth_map values ('/auths/new', 'get', 'auth.create');
insert into awf_url_auth_map values ('/auths/{id}/edit', 'get', 'auth.update');
insert into awf_url_auth_map values ('/auths/{id}/view', 'get', 'auth.read');
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
insert into awf_url_auth_map values ('/cmdb/attributes/list-modal', 'get', 'cmdb.attribute.read');
insert into awf_url_auth_map values ('/cmdb/class/edit', 'get', 'cmdb.class.read');
insert into awf_url_auth_map values ('/cmdb/class/view-pop/attributes', 'get', 'cmdb.class.read');
insert into awf_url_auth_map values ('/cmdb/cis', 'get', 'cmdb.ci.read');
insert into awf_url_auth_map values ('/cmdb/cis/search', 'get', 'cmdb.ci.read');
insert into awf_url_auth_map values ('/cmdb/cis/{id}/view', 'get', 'form.read');
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
insert into awf_url_auth_map values ('/workflows/{id}/display', 'get', 'document.admin.create');
insert into awf_url_auth_map values ('/workflows/{id}/display', 'get', 'document.admin.update');
insert into awf_url_auth_map values ('/documents', 'get', 'document.read');
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
insert into awf_url_auth_map values ('/forms/{id}/edit', 'get', 'form.read');
insert into awf_url_auth_map values ('/forms/{id}/preview', 'get', 'form.read');
insert into awf_url_auth_map values ('/forms/{id}/view', 'get', 'form.read');
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
insert into awf_url_auth_map values ('/numberingPatterns/search', 'get', 'numbering.pattern.read');
insert into awf_url_auth_map values ('/numberingPatterns/new', 'get', 'numbering.pattern.create');
insert into awf_url_auth_map values ('/numberingPatterns/{id}/edit', 'get', 'numbering.pattern.update');
insert into awf_url_auth_map values ('/numberingPatterns/{id}/view', 'get', 'numbering.pattern.read');
insert into awf_url_auth_map values ('/numberingRules', 'get', 'numbering.rule.read');
insert into awf_url_auth_map values ('/numberingRules/search', 'get', 'numbering.rule.read');
insert into awf_url_auth_map values ('/numberingRules/new', 'get', 'numbering.rule.create');
insert into awf_url_auth_map values ('/numberingRules/{id}/edit', 'get', 'numbering.rule.update');
insert into awf_url_auth_map values ('/numberingRules/{id}/view', 'get', 'numbering.rule.read');
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
insert into awf_url_auth_map values ('/reports/template/search', 'get', 'report.read');
insert into awf_url_auth_map values ('/reports/template/search', 'get', 'report.create');
insert into awf_url_auth_map values ('/reports/template/search', 'get', 'report.update');
insert into awf_url_auth_map values ('/reports/template/search', 'get', 'report.delete');
insert into awf_url_auth_map values ('/reports/report/search', 'get', 'report.read');
insert into awf_url_auth_map values ('/reports/report/search', 'get', 'report.create');
insert into awf_url_auth_map values ('/reports/report/search', 'get', 'report.update');
insert into awf_url_auth_map values ('/reports/report/search', 'get', 'report.delete');
insert into awf_url_auth_map values ('/reports/template', 'get', 'report.read');
insert into awf_url_auth_map values ('/reports/template', 'get', 'report.create');
insert into awf_url_auth_map values ('/reports/template', 'get', 'report.update');
insert into awf_url_auth_map values ('/reports/template', 'get', 'report.delete');
insert into awf_url_auth_map values ('/reports/report', 'get', 'report.read');
insert into awf_url_auth_map values ('/reports/report', 'get', 'report.create');
insert into awf_url_auth_map values ('/reports/report', 'get', 'report.update');
insert into awf_url_auth_map values ('/reports/report', 'get', 'report.delete');
insert into awf_url_auth_map values ('/reports/template/{id}/edit', 'get', 'report.create');
insert into awf_url_auth_map values ('/reports/template/{id}/edit', 'get', 'report.update');
insert into awf_url_auth_map values ('/reports/template/{id}/view', 'get', 'report.create');
insert into awf_url_auth_map values ('/reports/template/{id}/view', 'get', 'report.update');
insert into awf_url_auth_map values ('/reports/template/{id}/view', 'get', 'report.delete');
insert into awf_url_auth_map values ('/reports/template/{id}/view', 'get', 'report.read');
insert into awf_url_auth_map values ('/reports/template/new', 'get', 'report.create');
insert into awf_url_auth_map values ('/reports/report/{id}/view', 'get', 'report.create');
insert into awf_url_auth_map values ('/reports/report/{id}/view', 'get', 'report.update');
insert into awf_url_auth_map values ('/reports/report/{id}/view', 'get', 'report.read');
insert into awf_url_auth_map values ('/reports/report/{id}/view', 'get', 'report.delete');
insert into awf_url_auth_map values ('/rest/reports/template', 'post', 'report.create');
insert into awf_url_auth_map values ('/rest/reports/template/{id}', 'put', 'report.update');
insert into awf_url_auth_map values ('/rest/reports/template/{id}', 'delete', 'report.delete');
insert into awf_url_auth_map values ('/reports/template/preview', 'get', 'report.create');
insert into awf_url_auth_map values ('/reports/template/preview', 'get', 'report.read');
insert into awf_url_auth_map values ('/reports/template/preview', 'get', 'report.update');
insert into awf_url_auth_map values ('/reports/template/preview', 'get', 'report.delete');
insert into awf_url_auth_map values ('/rest/reports/template/charts', 'get', 'report.create');
insert into awf_url_auth_map values ('/rest/reports/template/charts', 'get', 'report.delete');
insert into awf_url_auth_map values ('/rest/reports/template/charts', 'get', 'report.read');
insert into awf_url_auth_map values ('/rest/reports/template/charts', 'get', 'report.update');
insert into awf_url_auth_map values ('/rest/reports/template/{id}', 'post', 'report.create');
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
insert into awf_url_auth_map values ('/rest/cmdb/attributes', 'post', 'cmdb.attribute.create');
insert into awf_url_auth_map values ('/rest/cmdb/attributes/{id}', 'put', 'cmdb.attribute.update');
insert into awf_url_auth_map values ('/rest/cmdb/attributes/{id}', 'delete', 'cmdb.attribute.update');
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
insert into awf_url_auth_map values ('/rest/cmdb/cis', 'get', 'cmdb.ci.read');
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
insert into awf_url_auth_map values ('/rest/codes/related/{id}', 'get', 'code.create');
insert into awf_url_auth_map values ('/rest/codes/related/{id}', 'get', 'code.update');
insert into awf_url_auth_map values ('/rest/codes/related/{id}', 'get', 'code.delete');
insert into awf_url_auth_map values ('/rest/codes/related/{id}', 'get', 'code.read');
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
insert into awf_url_auth_map values ('/rest/custom-codes/{id}', 'get', 'token.read');
insert into awf_url_auth_map values ('/rest/custom-codes/{id}', 'get', 'token.create');
insert into awf_url_auth_map values ('/rest/custom-codes/{id}', 'delete', 'custom.code.delete');
insert into awf_url_auth_map values ('/rest/documents', 'get', 'document.create');
insert into awf_url_auth_map values ('/rest/documents', 'get', 'document.update');
insert into awf_url_auth_map values ('/rest/documents', 'get', 'document.read');
insert into awf_url_auth_map values ('/rest/documents', 'get', 'document.delete');
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
insert into awf_url_auth_map values ('/rest/forms/{id}', 'delete', 'form.delete');
insert into awf_url_auth_map values ('/rest/forms', 'post', 'form.create');
insert into awf_url_auth_map values ('/rest/forms', 'post', 'form.delete');
insert into awf_url_auth_map values ('/rest/forms/{id}', 'put', 'form.update');
insert into awf_url_auth_map values ('/rest/forms/{id}/data', 'get', 'form.create');
insert into awf_url_auth_map values ('/rest/forms/{id}/data', 'get', 'form.update');
insert into awf_url_auth_map values ('/rest/forms/{id}/data', 'put', 'form.create');
insert into awf_url_auth_map values ('/rest/forms/{id}/data', 'put', 'form.update');
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
insert into awf_url_auth_map values ('/rest/products/info', 'get', 'product.read');
insert into awf_url_auth_map values ('/roles/search', 'get', 'role.read');
insert into awf_url_auth_map values ('/roles/new', 'get', 'role.create');
insert into awf_url_auth_map values ('/roles/{id}/edit', 'get', 'role.update');
insert into awf_url_auth_map values ('/roles/{id}/view', 'get', 'role.read');
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
insert into awf_url_auth_map values ('/tokens/{id}/view', 'get', 'token.read');
insert into awf_url_auth_map values ('/tokens/{id}/print', 'get', 'token.read');
insert into awf_url_auth_map values ('/tokens/{tokenId}/tokenTab', 'get', 'token.read');
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
insert into awf_url_auth_map values ('/users/{userkey}/view', 'get', 'user.read');
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
	form_display_option text,
	form_category varchar(128),
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
COMMENT ON COLUMN wf_form.form_display_option IS '문서양식세부속성';
COMMENT ON COLUMN wf_form.form_category IS '문서양식카테고리';
COMMENT ON COLUMN wf_form.create_user_key IS '생성자';
COMMENT ON COLUMN wf_form.create_dt IS '생성일시';
COMMENT ON COLUMN wf_form.update_user_key IS '수정자';
COMMENT ON COLUMN wf_form.update_dt IS '수정일시';

INSERT INTO wf_form VALUES ('4028b21f7c9698f4017c973010230003', '서비스데스크 - 단순문의', '', 'form.status.use', '{"width":"960","margin":"0 0 0 0","padding":"15 15 15 15"}','process', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO wf_form VALUES ('4028b21f7c9698f4017c9731ebae004e', '서비스데스크 - 단순문의 - 만족도', '', 'form.status.use', '{"width":"960","margin":"0 0 0 0","padding":"15 15 15 15"}','process', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO wf_form VALUES ('4028b21f7c90d996017c91af9dcf0051', '서비스데스크 - 장애신고', '', 'form.status.use', '{"width":"960","margin":"0 0 0 0","padding":"15 15 15 15"}','process', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO wf_form VALUES ('4028b21f7c90d996017c914bce270002', '서비스데스크 - 장애신고 - 만족도', '', 'form.status.use', '{"width":"960","margin":"0 0 0 0","padding":"15 15 15 15"}','process', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO wf_form VALUES ('4028b21f7c9cc269017c9cc8cbf60001', '서비스 데스크 - 서비스요청', '', 'form.status.use', '{"width":"960","margin":"0 0 0 0","padding":"15 15 15 15"}','process', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO wf_form VALUES ('4028b21f7c90d996017c914e27340030', '서비스데스크 - 서비스요청 - 만족도', '', 'form.status.use', '{"width":"960","margin":"0 0 0 0","padding":"15 15 15 15"}','process', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO wf_form VALUES ('4028b8817cbfc7a7017cc08f7b0b0763', '인프라변경관리', '', 'form.status.use', '{"width":"960","margin":"0 0 0 0","padding":"15 15 15 15"}', 'process', '0509e09412534a6e98f04ca79abb6424', now(), null, null);

/**
 * 문서양식 그룹정보
 */
DROP TABLE IF EXISTS wf_form_group cascade;

CREATE TABLE wf_form_group
(
    form_group_id varchar(128) NULL,
    form_group_name varchar(256) NULL,
    form_id varchar(128) NULL,
    CONSTRAINT wf_form_group_pk PRIMARY KEY (form_group_id),
    CONSTRAINT wf_form_group_fk FOREIGN KEY (form_id) REFERENCES wf_form (form_id)
);

COMMENT ON TABLE wf_form_group IS '문서양식 그룹정보';
COMMENT ON COLUMN wf_form_group.form_group_id IS '문서양식 그룹아이디';
COMMENT ON COLUMN wf_form_group.form_group_name IS '문서양식 그룹이름';
COMMENT ON COLUMN wf_form_group.form_id IS '문서양식아이디';

/* 서비스데스크 - 단순문의 */
INSERT INTO wf_form_group VALUES ('g028b21f7c780ba6017c7832447201e1','제목','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_form_group VALUES ('g028b21f7c780ba6017c7832480101e2','신청내역','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_form_group VALUES ('g028b21f7c780ba6017c7832527e0203','접수내역','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_form_group VALUES ('g028b21f7c780ba6017c783258080214','처리내역','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_form_group VALUES ('g028b21f7c780ba6017c78325ca80225','승인 / 반려 내역','4028b21f7c9698f4017c973010230003');
/* 서비스데스크 - 단순문의 - 만족도 */
INSERT INTO wf_form_group VALUES ('g028b21f7c780ba6017c78334c07023c','제목','4028b21f7c9698f4017c9731ebae004e');
INSERT INTO wf_form_group VALUES ('g028b21f7c780ba6017c78334f3a0244','만족도평가','4028b21f7c9698f4017c9731ebae004e');
/* 서비스데스크 - 장애신고 */
INSERT INTO wf_form_group VALUES ('4028b21f7c9adb6a017c9b0613610065','제목','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_form_group VALUES ('4028b21f7c9adb6a017c9b061411006d','신청내역','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_form_group VALUES ('4028b21f7c9adb6a017c9b061691008c','접수내역','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_form_group VALUES ('4028b21f7c9adb6a017c9b0617fe009e','처리내역','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_form_group VALUES ('4028b21f7c9adb6a017c9b061a8b00bf','승인 / 반려 내역','4028b21f7c90d996017c91af9dcf0051');
/* 서비스데스크 - 장애신고 - 만족도 */
INSERT INTO wf_form_group VALUES ('4028b21f7c90d996017c914da7aa0021','제목','4028b21f7c90d996017c914bce270002');
INSERT INTO wf_form_group VALUES ('4028b21f7c90d996017c914da8270029','만족도평가','4028b21f7c90d996017c914bce270002');
/* 서비스데스크 - 서비스요청 */
INSERT INTO wf_form_group VALUES ('4028b21f7c9ff7c8017ca054465a0001','제목','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_form_group VALUES ('4028b21f7c9ff7c8017ca054470f000a','신청내역','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_form_group VALUES ('4028b21f7c9ff7c8017ca05448d20027','접수내역','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_form_group VALUES ('4028b21f7c9ff7c8017ca05449e00039','처리내역','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_form_group VALUES ('4028b21f7c9ff7c8017ca0544af6004d','승인 / 반려 내역','4028b21f7c9cc269017c9cc8cbf60001');
/* 서비스데스크 - 서비스요청 - 만족도 */
INSERT INTO wf_form_group VALUES ('4028b21f7c90d996017c914eec300040','제목','4028b21f7c90d996017c914e27340030');
INSERT INTO wf_form_group VALUES ('4028b21f7c90d996017c914eecb30048','만족도평가','4028b21f7c90d996017c914e27340030');
/* 인프라변경관리 */
INSERT INTO wf_form_group VALUES ('4028b8817cbfc7a7017cc095a5670ae8', '제목', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_form_group VALUES ('4028b8817cbfc7a7017cc095a59a0af2', '신청내역', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_form_group VALUES ('4028b8817cbfc7a7017cc095a6360b0f', '접수내역', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_form_group VALUES ('4028b8817cbfc7a7017cc095a6a10b25', '변경  자문 회의록', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_form_group VALUES ('4028b8817cbfc7a7017cc095a6eb0b35', '변경 계획서', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_form_group VALUES ('4028b8817cbfc7a7017cc095a77f0b54', '변경 계획서 승인 내역', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_form_group VALUES ('4028b8817cbfc7a7017cc095a79f0b5b', '변경 결과서', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_form_group VALUES ('4028b8817cbfc7a7017cc095a7d90b68', 'PIR', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_form_group VALUES ('4028b8817cbfc7a7017cc095a81b0b77', '최종 검토 의견', '4028b8817cbfc7a7017cc08f7b0b0763');

/**
 * 문서양식 그룹 세부정보
 */
DROP TABLE IF EXISTS wf_form_group_property cascade;

CREATE TABLE wf_form_group_property (
    form_group_id varchar(128) NOT NULL,
    property_type varchar(100) NOT NULL,
    property_options text NULL,
    CONSTRAINT wf_form_group_property_pk PRIMARY KEY (form_group_id, property_type),
    CONSTRAINT wf_form_group_property_fk FOREIGN KEY (form_group_id) REFERENCES wf_form_group(form_group_id)
);

COMMENT ON TABLE wf_form_group_property IS '문서양식 그룹 세부정보';
COMMENT ON COLUMN wf_form_group_property.form_group_id IS '문서양식 그룹아이디';
COMMENT ON COLUMN wf_form_group_property.property_type IS '속성 타입';
COMMENT ON COLUMN wf_form_group_property.property_options IS '속성 값';

/* 서비스데스크 - 단순문의 */
INSERT INTO wf_form_group_property VALUES ('g028b21f7c780ba6017c7832447201e1','display','{"displayOrder":0,"isAccordionUsed":false,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('g028b21f7c780ba6017c7832447201e1','label','{"visibility":false,"fontColor":"#222529","fontSize":"14","bold":false,"italic":false,"underline":false,"align":"center","text":""}');
INSERT INTO wf_form_group_property VALUES ('g028b21f7c780ba6017c7832480101e2','display','{"displayOrder":1,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('g028b21f7c780ba6017c7832480101e2','label','{"visibility":true,"fontColor":"#8B9094","fontSize":"20","bold":true,"italic":false,"underline":false,"align":"left","text":"신청내역"}');
INSERT INTO wf_form_group_property VALUES ('g028b21f7c780ba6017c7832527e0203','display','{"displayOrder":2,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('g028b21f7c780ba6017c7832527e0203','label','{"visibility":true,"fontColor":"#8B9094","fontSize":"20","bold":true,"italic":false,"underline":false,"align":"left","text":"접수내역"}');
INSERT INTO wf_form_group_property VALUES ('g028b21f7c780ba6017c783258080214','display','{"displayOrder":3,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('g028b21f7c780ba6017c783258080214','label','{"visibility":true,"fontColor":"#8B9094","fontSize":"20","bold":true,"italic":false,"underline":false,"align":"left","text":"처리내역"}');
INSERT INTO wf_form_group_property VALUES ('g028b21f7c780ba6017c78325ca80225','display','{"displayOrder":4,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('g028b21f7c780ba6017c78325ca80225','label','{"visibility":true,"fontColor":"#8B9094","fontSize":"20","bold":true,"italic":false,"underline":false,"align":"left","text":"승인 / 반려 내역"}');
/* 서비스데스크 - 단순문의 - 만족도 */
INSERT INTO wf_form_group_property VALUES ('g028b21f7c780ba6017c78334c07023c','display','{"displayOrder":0,"isAccordionUsed":false,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('g028b21f7c780ba6017c78334c07023c','label','{"visibility":false,"fontColor":"#222529","fontSize":"14","bold":false,"italic":false,"underline":false,"align":"center","text":""}');
INSERT INTO wf_form_group_property VALUES ('g028b21f7c780ba6017c78334f3a0244','display','{"displayOrder":1,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('g028b21f7c780ba6017c78334f3a0244','label','{"visibility":true,"fontColor":"#8B9094","fontSize":"20","bold":true,"italic":false,"underline":false,"align":"left","text":"만족도 평가"}');
/* 서비스데스크 - 장애신고 */
INSERT INTO wf_form_group_property VALUES ('4028b21f7c9adb6a017c9b0613610065','display','{"displayOrder":0,"isAccordionUsed":false,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('4028b21f7c9adb6a017c9b0613610065','label','{"visibility":false,"fontColor":"#222529","fontSize":"14","bold":false,"italic":false,"underline":false,"align":"center","text":""}');
INSERT INTO wf_form_group_property VALUES ('4028b21f7c9adb6a017c9b061411006d','display','{"displayOrder":1,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('4028b21f7c9adb6a017c9b061411006d','label','{"visibility":true,"fontColor":"#8B9094","fontSize":"20","bold":true,"italic":false,"underline":false,"align":"left","text":"신청내역"}');
INSERT INTO wf_form_group_property VALUES ('4028b21f7c9adb6a017c9b061691008c','display','{"displayOrder":2,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('4028b21f7c9adb6a017c9b061691008c','label','{"visibility":true,"fontColor":"#8B9094","fontSize":"20","bold":true,"italic":false,"underline":false,"align":"left","text":"접수내역"}');
INSERT INTO wf_form_group_property VALUES ('4028b21f7c9adb6a017c9b0617fe009e','display','{"displayOrder":3,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('4028b21f7c9adb6a017c9b0617fe009e','label','{"visibility":true,"fontColor":"#8B9094","fontSize":"20","bold":true,"italic":false,"underline":false,"align":"left","text":"처리내역"}');
INSERT INTO wf_form_group_property VALUES ('4028b21f7c9adb6a017c9b061a8b00bf','display','{"displayOrder":4,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('4028b21f7c9adb6a017c9b061a8b00bf','label','{"visibility":true,"fontColor":"#8B9094","fontSize":"20","bold":true,"italic":false,"underline":false,"align":"left","text":"승인 / 반려 내역"}');
/* 서비스데스크 - 장애신고 - 만족도 */
INSERT INTO wf_form_group_property VALUES ('4028b21f7c90d996017c914da7aa0021','display','{"displayOrder":0,"isAccordionUsed":false,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('4028b21f7c90d996017c914da7aa0021','label','{"visibility":false,"fontColor":"#222529","fontSize":"14","bold":false,"italic":false,"underline":false,"align":"center","text":""}');
INSERT INTO wf_form_group_property VALUES ('4028b21f7c90d996017c914da8270029','display','{"displayOrder":1,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('4028b21f7c90d996017c914da8270029','label','{"visibility":true,"fontColor":"#8B9094","fontSize":"20","bold":true,"italic":false,"underline":false,"align":"left","text":"만족도 평가"}');
/* 서비스데스크 - 서비스요청 */
INSERT INTO wf_form_group_property VALUES ('4028b21f7c9ff7c8017ca054465a0001','display','{"displayOrder":0,"isAccordionUsed":false,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('4028b21f7c9ff7c8017ca054465a0001','label','{"visibility":false,"fontColor":"#222529","fontSize":"14","bold":false,"italic":false,"underline":false,"align":"center","text":""}');
INSERT INTO wf_form_group_property VALUES ('4028b21f7c9ff7c8017ca054470f000a','display','{"displayOrder":1,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('4028b21f7c9ff7c8017ca054470f000a','label','{"visibility":true,"fontColor":"#8B9094","fontSize":"20","bold":true,"italic":false,"underline":false,"align":"left","text":"신청내역"}');
INSERT INTO wf_form_group_property VALUES ('4028b21f7c9ff7c8017ca05448d20027','display','{"displayOrder":2,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('4028b21f7c9ff7c8017ca05448d20027','label','{"visibility":true,"fontColor":"#8B9094","fontSize":"20","bold":true,"italic":false,"underline":false,"align":"left","text":"접수내역"}');
INSERT INTO wf_form_group_property VALUES ('4028b21f7c9ff7c8017ca05449e00039','display','{"displayOrder":3,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('4028b21f7c9ff7c8017ca05449e00039','label','{"visibility":true,"fontColor":"#8B9094","fontSize":"20","bold":true,"italic":false,"underline":false,"align":"left","text":"처리내역"}');
INSERT INTO wf_form_group_property VALUES ('4028b21f7c9ff7c8017ca0544af6004d','display','{"displayOrder":4,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('4028b21f7c9ff7c8017ca0544af6004d','label','{"visibility":true,"fontColor":"#8B9094","fontSize":"20","bold":true,"italic":false,"underline":false,"align":"left","text":"승인 / 반려 내역"}');
/* 서비스데스크 - 서비스요청 - 만족도 */
INSERT INTO wf_form_group_property VALUES ('4028b21f7c90d996017c914eec300040','display','{"displayOrder":0,"isAccordionUsed":false,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('4028b21f7c90d996017c914eec300040','label','{"visibility":false,"fontColor":"#222529","fontSize":"14","bold":false,"italic":false,"underline":false,"align":"center","text":""}');
INSERT INTO wf_form_group_property VALUES ('4028b21f7c90d996017c914eecb30048','display','{"displayOrder":1,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('4028b21f7c90d996017c914eecb30048','label','{"visibility":true,"fontColor":"#8B9094","fontSize":"20","bold":true,"italic":false,"underline":false,"align":"left","text":"만족도 평가"}');
/* 인프라변경관리 */
INSERT INTO wf_form_group_property VALUES ('4028b8817cbfc7a7017cc095a5670ae8', 'display', '{"displayOrder":0,"isAccordionUsed":false,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('4028b8817cbfc7a7017cc095a5670ae8', 'label', '{"visibility":false,"fontColor":"#222529","fontSize":"14","bold":false,"italic":false,"underline":false,"align":"center","text":""}');
INSERT INTO wf_form_group_property VALUES ('4028b8817cbfc7a7017cc095a59a0af2', 'display', '{"displayOrder":1,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('4028b8817cbfc7a7017cc095a59a0af2', 'label', '{"visibility":true,"fontColor":"#8B9094","fontSize":"20","bold":true,"italic":false,"underline":false,"align":"left","text":"신청내역"}');
INSERT INTO wf_form_group_property VALUES ('4028b8817cbfc7a7017cc095a6360b0f', 'display', '{"displayOrder":2,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('4028b8817cbfc7a7017cc095a6360b0f', 'label', '{"visibility":true,"fontColor":"#8B9094","fontSize":"20","bold":true,"italic":false,"underline":false,"align":"left","text":"접수내역"}');
INSERT INTO wf_form_group_property VALUES ('4028b8817cbfc7a7017cc095a6a10b25', 'display', '{"displayOrder":3,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('4028b8817cbfc7a7017cc095a6a10b25', 'label', '{"visibility":true,"fontColor":"#8B9094","fontSize":"20","bold":true,"italic":false,"underline":false,"align":"left","text":"변경  자문 회의록"}');
INSERT INTO wf_form_group_property VALUES ('4028b8817cbfc7a7017cc095a6eb0b35', 'display', '{"displayOrder":4,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('4028b8817cbfc7a7017cc095a6eb0b35', 'label', '{"visibility":true,"fontColor":"rgba(141, 146, 153, 1)","fontSize":"20","bold":true,"italic":false,"underline":false,"align":"left","text":"변경 계획서"}');
INSERT INTO wf_form_group_property VALUES ('4028b8817cbfc7a7017cc095a77f0b54', 'display', '{"displayOrder":5,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('4028b8817cbfc7a7017cc095a77f0b54', 'label', '{"visibility":true,"fontColor":"#8B9094","fontSize":"20","bold":true,"italic":false,"underline":false,"align":"left","text":"변경 계획서 승인 내역"}');
INSERT INTO wf_form_group_property VALUES ('4028b8817cbfc7a7017cc095a79f0b5b', 'display', '{"displayOrder":6,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('4028b8817cbfc7a7017cc095a79f0b5b', 'label', '{"visibility":true,"fontColor":"rgba(141, 146, 153, 1)","fontSize":"20","bold":true,"italic":false,"underline":false,"align":"left","text":"변경 결과서"}');
INSERT INTO wf_form_group_property VALUES ('4028b8817cbfc7a7017cc095a7d90b68', 'display', '{"displayOrder":7,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('4028b8817cbfc7a7017cc095a7d90b68', 'label', '{"visibility":true,"fontColor":"rgba(141, 146, 153, 1)","fontSize":"20","bold":false,"italic":false,"underline":false,"align":"left","text":"PIR"}');
INSERT INTO wf_form_group_property VALUES ('4028b8817cbfc7a7017cc095a81b0b77', 'display', '{"displayOrder":8,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('4028b8817cbfc7a7017cc095a81b0b77', 'label', '{"visibility":true,"fontColor":"rgba(141, 146, 153, 1)","fontSize":"20","bold":true,"italic":false,"underline":false,"align":"left","text":"최종 검토 의견"}');

/**
 * 문서양식 ROW 정보
 */
DROP TABLE IF EXISTS wf_form_row cascade;

CREATE TABLE wf_form_row (
     form_row_id varchar(128) NULL,
     form_group_id varchar(128) NULL,
     row_display_option text NULL,
     CONSTRAINT wf_form_row_pk PRIMARY KEY (form_row_id),
     CONSTRAINT wf_form_row_fk FOREIGN KEY (form_group_id) REFERENCES wf_form_group(form_group_id)
);

COMMENT ON TABLE wf_form_row IS '문서양식 ROW 정보';
COMMENT ON COLUMN wf_form_row.form_row_id IS '문서양식 ROW 아이디';
COMMENT ON COLUMN wf_form_row.form_group_id IS '문서양식 그룹아이디';
COMMENT ON COLUMN wf_form_row.row_display_option IS 'ROW 출력용 속성';

/* 서비스데스크 - 단순문의 */
INSERT INTO wf_form_row VALUES ('r028b21f7c780ba6017c783244ca01e5','g028b21f7c780ba6017c7832447201e1','{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('r028b21f7c780ba6017c783245d801e8','g028b21f7c780ba6017c7832447201e1','{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('r028b21f7c780ba6017c7832470e01eb','g028b21f7c780ba6017c7832447201e1','{"displayOrder":2,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('r028b21f7c780ba6017c7832485c01ed','g028b21f7c780ba6017c7832480101e2','{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('r028b21f7c780ba6017c78324a3801f2','g028b21f7c780ba6017c7832480101e2','{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('r028b21f7c780ba6017c78324c4301f7','g028b21f7c780ba6017c7832480101e2','{"displayOrder":2,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('r028b21f7c780ba6017c78324e2701fc','g028b21f7c780ba6017c7832480101e2','{"displayOrder":3,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('r028b21f7c780ba6017c78324f5001ff','g028b21f7c780ba6017c7832480101e2','{"displayOrder":4,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('r028b21f7c780ba6017c783250730203','g028b21f7c780ba6017c7832480101e2','{"displayOrder":5,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('r028b21f7c780ba6017c783251820206','g028b21f7c780ba6017c7832480101e2','{"displayOrder":6,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('r028b21f7c780ba6017c783252db020a','g028b21f7c780ba6017c7832527e0203','{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('r028b21f7c780ba6017c783254ac020f','g028b21f7c780ba6017c7832527e0203','{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('r028b21f7c780ba6017c783255fa0212','g028b21f7c780ba6017c7832527e0203','{"displayOrder":2,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('r028b21f7c780ba6017c783256e80215','g028b21f7c780ba6017c7832527e0203','{"displayOrder":3,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('r028b21f7c780ba6017c783258800219','g028b21f7c780ba6017c783258080214','{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('r028b21f7c780ba6017c7832599c021c','g028b21f7c780ba6017c783258080214','{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('r028b21f7c780ba6017c78325ab4021f','g028b21f7c780ba6017c783258080214','{"displayOrder":2,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('r028b21f7c780ba6017c78325bc30222','g028b21f7c780ba6017c783258080214','{"displayOrder":3,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('r028b21f7c780ba6017c78325d040226','g028b21f7c780ba6017c78325ca80225','{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('r028b21f7c780ba6017c78325e11022a','g028b21f7c780ba6017c78325ca80225','{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
/* 서비스데스크 - 단순문의 - 만족도 */
INSERT INTO wf_form_row VALUES ('r028b21f7c780ba6017c78334c66023d','g028b21f7c780ba6017c78334c07023c','{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('r028b21f7c780ba6017c78334d6a0240','g028b21f7c780ba6017c78334c07023c','{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('r028b21f7c780ba6017c78334e7b0243','g028b21f7c780ba6017c78334c07023c','{"displayOrder":2,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('r028b21f7c780ba6017c78334fa80245','g028b21f7c780ba6017c78334f3a0244','{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('r028b21f7c780ba6017c783350b00248','g028b21f7c780ba6017c78334f3a0244','{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
/* 서비스데스크 - 장애신고 */
INSERT INTO wf_form_row VALUES ('4028b21f7c9adb6a017c9b0613760066','4028b21f7c9adb6a017c9b0613610065','{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9adb6a017c9b0613b00069','4028b21f7c9adb6a017c9b0613610065','{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9adb6a017c9b0613e5006c','4028b21f7c9adb6a017c9b0613610065','{"displayOrder":2,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9adb6a017c9b061427006e','4028b21f7c9adb6a017c9b061411006d','{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9adb6a017c9b0614970073','4028b21f7c9adb6a017c9b061411006d','{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9adb6a017c9b0615030078','4028b21f7c9adb6a017c9b061411006d','{"displayOrder":2,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9adb6a017c9b061578007e','4028b21f7c9adb6a017c9b061411006d','{"displayOrder":3,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9adb6a017c9b0615df0083','4028b21f7c9adb6a017c9b061411006d','{"displayOrder":4,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9adb6a017c9b06161e0086','4028b21f7c9adb6a017c9b061411006d','{"displayOrder":5,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9adb6a017c9b0616540089','4028b21f7c9adb6a017c9b061411006d','{"displayOrder":6,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9adb6a017c9b0616a9008d','4028b21f7c9adb6a017c9b061691008c','{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9adb6a017c9b0617160092','4028b21f7c9adb6a017c9b061691008c','{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9adb6a017c9b06174d0095','4028b21f7c9adb6a017c9b061691008c','{"displayOrder":2,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9adb6a017c9b0617870098','4028b21f7c9adb6a017c9b061691008c','{"displayOrder":3,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9adb6a017c9b0617c5009b','4028b21f7c9adb6a017c9b061691008c','{"displayOrder":4,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9adb6a017c9b061813009f','4028b21f7c9adb6a017c9b0617fe009e','{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9adb6a017c9b06187e00a4','4028b21f7c9adb6a017c9b0617fe009e','{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9adb6a017c9b0618e600a9','4028b21f7c9adb6a017c9b0617fe009e','{"displayOrder":2,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9adb6a017c9b06192700ac','4028b21f7c9adb6a017c9b0617fe009e','{"displayOrder":3,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9adb6a017c9b06196700af','4028b21f7c9adb6a017c9b0617fe009e','{"displayOrder":4,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9adb6a017c9b06199e00b2','4028b21f7c9adb6a017c9b0617fe009e','{"displayOrder":5,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9adb6a017c9b0619d500b5','4028b21f7c9adb6a017c9b0617fe009e','{"displayOrder":6,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9adb6a017c9b061a1400b9','4028b21f7c9adb6a017c9b0617fe009e','{"displayOrder":7,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9adb6a017c9b061a5600bc','4028b21f7c9adb6a017c9b0617fe009e','{"displayOrder":8,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9adb6a017c9b061a9f00c0','4028b21f7c9adb6a017c9b061a8b00bf','{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9adb6a017c9b061ae500c5','4028b21f7c9adb6a017c9b061a8b00bf','{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
/* 서비스데스크 - 장애신고 - 만족도 */
INSERT INTO wf_form_row VALUES ('4028b21f7c90d996017c914da7b90022','4028b21f7c90d996017c914da7aa0021','{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c90d996017c914da7e10025','4028b21f7c90d996017c914da7aa0021','{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c90d996017c914da8090028','4028b21f7c90d996017c914da7aa0021','{"displayOrder":2,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c90d996017c914da836002a','4028b21f7c90d996017c914da8270029','{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c90d996017c914da85b002d','4028b21f7c90d996017c914da8270029','{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
/* 서비스데스크 - 서비스요청 */
INSERT INTO wf_form_row VALUES ('4028b21f7c9ff7c8017ca05446750002','4028b21f7c9ff7c8017ca054465a0001','{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9ff7c8017ca05446b40005','4028b21f7c9ff7c8017ca054465a0001','{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9ff7c8017ca05446e30008','4028b21f7c9ff7c8017ca054465a0001','{"displayOrder":2,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9ff7c8017ca0544721000b','4028b21f7c9ff7c8017ca054470f000a','{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9ff7c8017ca054477d0010','4028b21f7c9ff7c8017ca054470f000a','{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9ff7c8017ca05447cf0015','4028b21f7c9ff7c8017ca054470f000a','{"displayOrder":2,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9ff7c8017ca0544826001b','4028b21f7c9ff7c8017ca054470f000a','{"displayOrder":3,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9ff7c8017ca0544851001e','4028b21f7c9ff7c8017ca054470f000a','{"displayOrder":4,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9ff7c8017ca054487b0021','4028b21f7c9ff7c8017ca054470f000a','{"displayOrder":5,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9ff7c8017ca05448a50024','4028b21f7c9ff7c8017ca054470f000a','{"displayOrder":6,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9ff7c8017ca05448e20028','4028b21f7c9ff7c8017ca05448d20027','{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9ff7c8017ca0544932002d','4028b21f7c9ff7c8017ca05448d20027','{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9ff7c8017ca054495f0030','4028b21f7c9ff7c8017ca05448d20027','{"displayOrder":2,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9ff7c8017ca054498b0033','4028b21f7c9ff7c8017ca05448d20027','{"displayOrder":3,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9ff7c8017ca05449b50036','4028b21f7c9ff7c8017ca05448d20027','{"displayOrder":4,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9ff7c8017ca05449ef003a','4028b21f7c9ff7c8017ca05449e00039','{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9ff7c8017ca0544a1e003d','4028b21f7c9ff7c8017ca05449e00039','{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9ff7c8017ca0544a490040','4028b21f7c9ff7c8017ca05449e00039','{"displayOrder":2,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9ff7c8017ca0544a760043','4028b21f7c9ff7c8017ca05449e00039','{"displayOrder":3,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9ff7c8017ca0544aa30047','4028b21f7c9ff7c8017ca05449e00039','{"displayOrder":4,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9ff7c8017ca0544ace004a','4028b21f7c9ff7c8017ca05449e00039','{"displayOrder":5,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9ff7c8017ca0544b09004e','4028b21f7c9ff7c8017ca0544af6004d','{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9ff7c8017ca0544b390053','4028b21f7c9ff7c8017ca0544af6004d','{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
/* 서비스데스크 - 서비스요청 - 만족도 */
INSERT INTO wf_form_row VALUES ('4028b21f7c90d996017c914eec400041','4028b21f7c90d996017c914eec300040','{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c90d996017c914eec670044','4028b21f7c90d996017c914eec300040','{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c90d996017c914eec970047','4028b21f7c90d996017c914eec300040','{"displayOrder":2,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c90d996017c914eecc30049','4028b21f7c90d996017c914eecb30048','{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c90d996017c914eecf3004c','4028b21f7c90d996017c914eecb30048','{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
/* 인프라변경관리 */
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a56d0ae9', '4028b8817cbfc7a7017cc095a5670ae8', '{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a5800aec', '4028b8817cbfc7a7017cc095a5670ae8', '{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a58d0af0', '4028b8817cbfc7a7017cc095a5670ae8', '{"displayOrder":2,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a5a20af3', '4028b8817cbfc7a7017cc095a59a0af2', '{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a5c30af8', '4028b8817cbfc7a7017cc095a59a0af2', '{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a5e00afd', '4028b8817cbfc7a7017cc095a59a0af2', '{"displayOrder":2,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a5fd0b02', '4028b8817cbfc7a7017cc095a59a0af2', '{"displayOrder":3,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a60a0b05', '4028b8817cbfc7a7017cc095a59a0af2', '{"displayOrder":4,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a61b0b09', '4028b8817cbfc7a7017cc095a59a0af2', '{"displayOrder":5,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a6290b0c', '4028b8817cbfc7a7017cc095a59a0af2', '{"displayOrder":6,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a63b0b10', '4028b8817cbfc7a7017cc095a6360b0f', '{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a64c0b13', '4028b8817cbfc7a7017cc095a6360b0f', '{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a6650b18', '4028b8817cbfc7a7017cc095a6360b0f', '{"displayOrder":2,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a67e0b1d', '4028b8817cbfc7a7017cc095a6360b0f', '{"displayOrder":3,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a6950b22', '4028b8817cbfc7a7017cc095a6360b0f', '{"displayOrder":4,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a6a60b26', '4028b8817cbfc7a7017cc095a6a10b25', '{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a6b40b29', '4028b8817cbfc7a7017cc095a6a10b25', '{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a6c20b2c', '4028b8817cbfc7a7017cc095a6a10b25', '{"displayOrder":2,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a6cf0b2f', '4028b8817cbfc7a7017cc095a6a10b25', '{"displayOrder":3,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a6db0b32', '4028b8817cbfc7a7017cc095a6a10b25', '{"displayOrder":4,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a6f10b36', '4028b8817cbfc7a7017cc095a6eb0b35', '{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a6fd0b37', '4028b8817cbfc7a7017cc095a6eb0b35', '{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a71b0b3c', '4028b8817cbfc7a7017cc095a6eb0b35', '{"displayOrder":2,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a7280b3f', '4028b8817cbfc7a7017cc095a6eb0b35', '{"displayOrder":3,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a7330b42', '4028b8817cbfc7a7017cc095a6eb0b35', '{"displayOrder":4,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a73f0b45', '4028b8817cbfc7a7017cc095a6eb0b35', '{"displayOrder":5,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a74b0b48', '4028b8817cbfc7a7017cc095a6eb0b35', '{"displayOrder":6,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a7570b4b', '4028b8817cbfc7a7017cc095a6eb0b35', '{"displayOrder":7,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a7630b4e', '4028b8817cbfc7a7017cc095a6eb0b35', '{"displayOrder":8,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a7720b51', '4028b8817cbfc7a7017cc095a6eb0b35', '{"displayOrder":9,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a7840b55', '4028b8817cbfc7a7017cc095a77f0b54', '{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a7910b58', '4028b8817cbfc7a7017cc095a77f0b54', '{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a7a30b5c', '4028b8817cbfc7a7017cc095a79f0b5b', '{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a7b00b5f', '4028b8817cbfc7a7017cc095a79f0b5b', '{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a7bd0b62', '4028b8817cbfc7a7017cc095a79f0b5b', '{"displayOrder":2,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a7ca0b65', '4028b8817cbfc7a7017cc095a79f0b5b', '{"displayOrder":3,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a7e00b69', '4028b8817cbfc7a7017cc095a7d90b68', '{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a7f90b6e', '4028b8817cbfc7a7017cc095a7d90b68', '{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a8060b71', '4028b8817cbfc7a7017cc095a7d90b68', '{"displayOrder":2,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a8120b74', '4028b8817cbfc7a7017cc095a7d90b68', '{"displayOrder":3,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a8200b78', '4028b8817cbfc7a7017cc095a81b0b77', '{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a82a0b7b', '4028b8817cbfc7a7017cc095a81b0b77', '{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');

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

INSERT INTO wf_process VALUES ('4028b21f7c9698f4017c96a70ded0000','서비스데스크 - 단순문의','process.status.use','','0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO wf_process VALUES ('4028b21f7c9698f4017c96c5630c0002','서비스데스크 - 단순문의 - 만족도','process.status.use','','0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO wf_process VALUES ('4028b21f7c81a928017c81aa9dc60000','서비스데스크 - 장애신고','process.status.use','','0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO wf_process VALUES ('4028b21f7c9b6b1e017c9bdf04cb0011','서비스데스크 - 장애신고 - 만족도','process.status.use','','0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO wf_process VALUES ('4028b21f7c9ff7c8017ca0549ef00057','서비스데스크 - 서비스요청','process.status.use','','0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO wf_process VALUES ('4028b21f7c9cc269017c9cc76a5e0000','서비스데스크 - 서비스요청 - 만족도','process.status.use','','0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO wf_process VALUES ('4028b8817cbfc7a7017cc0db1a8c0bc3', '인프라변경관리', 'process.status.use', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);

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
	api_enable boolean DEFAULT false,
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
COMMENT ON COLUMN wf_document.api_enable IS 'API 활성화';
COMMENT ON COLUMN wf_document.create_user_key IS '생성자';
COMMENT ON COLUMN wf_document.create_dt IS '생성일시';
COMMENT ON COLUMN wf_document.update_user_key IS '수정자';
COMMENT ON COLUMN wf_document.update_dt IS '수정일시';

/* 서비스데스크 - 단순문의 */
INSERT INTO wf_document VALUES ('4028b21f7c90d996017c91ae7987004f','서비스데스크 - 단순문의','','4028b21f7c9698f4017c96a70ded0000','4028b21f7c9698f4017c973010230003','document.status.use','40125c91714df6c325714e053c890125','#64BBF6','application-form','document.group.inquiry','img_document_09.png',false,'0509e09412534a6e98f04ca79abb6424', now(),NULL,NULL);
/* 서비스데스크 - 단순문의 - 만족도 */
INSERT INTO wf_document VALUES ('4028b21f7c90d996017c91aeff1b0050','서비스데스크 - 단순문의 - 만족도','','4028b21f7c9698f4017c96c5630c0002','4028b21f7c9698f4017c9731ebae004e','document.status.use','40288ab7772dae0301772dbca28a0004','#BDBDBD','workflow','',NULL,false,'0509e09412534a6e98f04ca79abb6424', now(),NULL,NULL);
/* 서비스데스크 - 장애신고 */
INSERT INTO wf_document VALUES ('4028b21f7c9adb6a017c9b18489900c9','서비스데스크 - 장애신고','','4028b21f7c81a928017c81aa9dc60000','4028b21f7c90d996017c91af9dcf0051','document.status.use','40125c91714df6c325714e053c890125','#64BBF6','application-form','document.group.incident','img_document_02.png',false,'0509e09412534a6e98f04ca79abb6424', now(),NULL,NULL);
/* 서비스데스크 - 장애신고 - 만족도 */
INSERT INTO wf_document VALUES ('4028b21f7c9b6b1e017c9bedbe8a0012','서비스데스크 - 장애신고 - 만족도','','4028b21f7c9b6b1e017c9bdf04cb0011','4028b21f7c90d996017c914bce270002','document.status.use','40288ab7772dae0301772dbca28a0004','#BDBDBD','workflow','',NULL,false,'0509e09412534a6e98f04ca79abb6424', now(),NULL,NULL);
/* 서비스데스크 - 서비스요청 */
INSERT INTO wf_document VALUES ('4028b21f7c9ff7c8017ca06bde520058','서비스데스크 - 서비스요청','','4028b21f7c9ff7c8017ca0549ef00057','4028b21f7c9cc269017c9cc8cbf60001','document.status.use','40125c91714df6c325714e053c890125','#64BBF6','application-form','document.group.request','img_document_01.png',false,'0509e09412534a6e98f04ca79abb6424', now(),NULL,NULL);
/* 서비스데스크 - 서비스요청 - 만족도 */
INSERT INTO wf_document VALUES ('4028b21f7c9ff7c8017ca04d16830000','서비스데스크 - 서비스요청 - 만족도','','4028b21f7c9cc269017c9cc76a5e0000','4028b21f7c90d996017c914e27340030','document.status.use','40288ab7772dae0301772dbca28a0004','#BDBDBD','workflow','',NULL,false,'0509e09412534a6e98f04ca79abb6424', now(),NULL,NULL);
/* 인프라변경관리 */
INSERT INTO wf_document VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '인프라변경관리', '', '4028b8817cbfc7a7017cc0db1a8c0bc3', '4028b8817cbfc7a7017cc08f7b0b0763', 'document.status.use', '4028b25d7886e2d801788704dd8e0002', '#76BD26', 'workflow', '', null, false, '0509e09412534a6e98f04ca79abb6424', '2021-10-27 08:38:14.548600', null, null);

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
	instance_platform varchar(128) NOT NULL DEFAULT 'itsm',
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
COMMENT ON COLUMN wf_instance.instance_platform IS '인스턴스플랫폼';

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
	component_type varchar(100) NOT NULL,
	mapping_id varchar(128),
	is_topic boolean DEFAULT 'false',
	form_row_id varchar(128),
	form_id varchar(128),
	CONSTRAINT wf_component_pk PRIMARY KEY (component_id),
	CONSTRAINT wf_component_fk FOREIGN KEY (form_row_id) REFERENCES wf_form_row (form_row_id),
	CONSTRAINT wf_component_form_fk FOREIGN KEY (form_id) REFERENCES wf_form (form_id)
);

COMMENT ON TABLE wf_component IS '컴포넌트정보';
COMMENT ON COLUMN wf_component.component_id IS '컴포넌트아이디';
COMMENT ON COLUMN wf_component.form_row_id IS '문서양식 ROW 아이디';
COMMENT ON COLUMN wf_component.component_type IS '컴포넌트종류';
COMMENT ON COLUMN wf_component.mapping_id IS '매핑아이디';
COMMENT ON COLUMN wf_component.is_topic IS '토픽여부';
COMMENT ON COLUMN wf_component.form_id IS '폼 아이디';

/* 서비스데스크 - 단순문의 */
INSERT INTO wf_component VALUES ('ae3e2a000d67b4e2e8d83bf36c81260a','image','z-logo',false,'r028b21f7c780ba6017c783244ca01e5','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_component VALUES ('aa83af616d59cc86c565a0282153c236','label','z-document-title',false,'r028b21f7c780ba6017c783245d801e8','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_component VALUES ('aeaba514c85489bea2ae4c588df41f03','divider','',false,'r028b21f7c780ba6017c7832470e01eb','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_component VALUES ('ad15aab4783f55533c6c1f183a4b60cc','inputBox','z-requester',false,'r028b21f7c780ba6017c7832485c01ed','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_component VALUES ('ae4f48236ecf493eb0212d77ac14c360','customCode','z-requester-department',false,'r028b21f7c780ba6017c7832485c01ed','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_component VALUES ('a71d2d6094b2920f87ba8eaf025af1c3','inputBox','z-requester-phone',false,'r028b21f7c780ba6017c78324a3801f2','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_component VALUES ('a4a100bbcd9907aae4e260c8bfa3b45f','inputBox','z-requester-email',false,'r028b21f7c780ba6017c78324a3801f2','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_component VALUES ('a115df4520fea935e4482784064c7a51','dateTime','z-request-date',false,'r028b21f7c780ba6017c78324c4301f7','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_component VALUES ('a0c3e8d18663483284b7e9fe83d0b824','dateTime','z-request-deadline',false,'r028b21f7c780ba6017c78324c4301f7','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_component VALUES ('a75c85bba7aa722f9dcbcbcfad0b6ac7','inputBox','z-request-title',false,'r028b21f7c780ba6017c78324e2701fc','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_component VALUES ('ade3320b3bc9dca17651e4c0cdef89e4','customCode','z-request-category',false,'r028b21f7c780ba6017c78324f5001ff','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_component VALUES ('a3b140da99dbfb49beb7fa3db09042b4','textArea','z-request-content',false,'r028b21f7c780ba6017c783250730203','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_component VALUES ('a9404877fabbd09dc2603f78283fa155','fileUpload','z-request-file',false,'r028b21f7c780ba6017c783251820206','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_component VALUES ('ab6d29d8ef257f93de98c655fbaab22a','inputBox','z-acceptor',false,'r028b21f7c780ba6017c783252db020a','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_component VALUES ('ab266326aa59ed5bb4411da4142aa24a','customCode','z-acceptor-department',false,'r028b21f7c780ba6017c783252db020a','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_component VALUES ('a8e347190f51b1b05200f34d9e8b8ab0','dateTime','z-accept-date',false,'r028b21f7c780ba6017c783254ac020f','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_component VALUES ('a71977859617f137f589108c24fa7845','textArea','z-accept-content',false,'r028b21f7c780ba6017c783255fa0212','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_component VALUES ('acc02e1f2f51aa01649b6e06f7b35ec4','customCode','z-processor',false,'r028b21f7c780ba6017c783256e80215','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_component VALUES ('a83a54dfec6c65dc5983272ec9a956ce','dateTime','z-process-date',false,'r028b21f7c780ba6017c783258800219','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_component VALUES ('ae07bae262c57399a4ee8b3dea2fcbab','textArea','z-process-content',false,'r028b21f7c780ba6017c7832599c021c','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_component VALUES ('a27c22d07de4231ab41863dab45450f1','fileUpload','z-process-file',false,'r028b21f7c780ba6017c78325ab4021f','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_component VALUES ('a4c410628aa58b1863935b4056395b80','customCode','z-approver',false,'r028b21f7c780ba6017c78325bc30222','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_component VALUES ('a10c935dbdf1c30b34dfabe5ebc0ba73','dateTime','z-approve-date',false,'r028b21f7c780ba6017c78325d040226','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_component VALUES ('ab97abfd66b5654e3f525e617df9afaf','textArea','z-approve-content',false,'r028b21f7c780ba6017c78325e11022a','4028b21f7c9698f4017c973010230003');
/* 서비스데스크 - 단순문의 - 만족도 */
INSERT INTO wf_component VALUES ('a605df0d26f09a3d20caaea3977a5c64','image','z-logo',false,'r028b21f7c780ba6017c78334c66023d','4028b21f7c9698f4017c9731ebae004e');
INSERT INTO wf_component VALUES ('ae54c20fd574a57845fffaa7bb936eb4','label','z-document-title',false,'r028b21f7c780ba6017c78334d6a0240','4028b21f7c9698f4017c9731ebae004e');
INSERT INTO wf_component VALUES ('a7cc83ce97c50faa754c702bf018c505','divider','',false,'r028b21f7c780ba6017c78334e7b0243','4028b21f7c9698f4017c9731ebae004e');
INSERT INTO wf_component VALUES ('ac69d66f3ec394b36215cd2ee3983292','radio','z-satisfaction',false,'r028b21f7c780ba6017c78334fa80245','4028b21f7c9698f4017c9731ebae004e');
INSERT INTO wf_component VALUES ('ad2996c39febdc13b32d08354169d6ac','textArea','z-satisfaction-content',false,'r028b21f7c780ba6017c783350b00248','4028b21f7c9698f4017c9731ebae004e');
/* 서비스데스크 - 장애신고 */
INSERT INTO wf_component VALUES ('a371d3cfecb547e4aff813ce0fca711c','image','z-logo',false,'4028b21f7c9adb6a017c9b0613760066','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('e90e4131007f470490a2ffaff402ba2f','label','z-document-title',false,'4028b21f7c9adb6a017c9b0613b00069','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('9a0a95161bbf4039a3bdfcba2a59a5e6','divider','',false,'4028b21f7c9adb6a017c9b0613e5006c','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('77e97f770393455a97c645f7562b3b53','inputBox','z-requester',false,'4028b21f7c9adb6a017c9b061427006e','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('fee68572b7bb4e04b1f27d0cdfe9ad7a','customCode','z-requester-department',false,'4028b21f7c9adb6a017c9b061427006e','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('857ac1b2c7f3424ab297108583b3c5c1','inputBox','z-requester-phone',false,'4028b21f7c9adb6a017c9b0614970073','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('b960f194ab8e4ecda04c319398bf83ea','inputBox','z-requester-email',false,'4028b21f7c9adb6a017c9b0614970073','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('4b8b310945214d0bbd262352802f93c6','dateTime','z-request-date',false,'4028b21f7c9adb6a017c9b0615030078','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('ae79624833414ab3a3ff37582c234aa2','dateTime','z-request-deadline',false,'4028b21f7c9adb6a017c9b0615030078','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('a6eb123ed5ac913c55035f15bb30efce','dateTime','z-incident-date',false,'4028b21f7c9adb6a017c9b061578007e','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('a20925ede2aee30d0c74c4647c0c46dd','dropdown','z-perception-path',false,'4028b21f7c9adb6a017c9b061578007e','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('0138fd96d2ca4075b8d8ff4ef4b9fe10','inputBox','z-request-title',false,'4028b21f7c9adb6a017c9b0615df0083','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('250e5452b08344758d08fb59915c4e95','textArea','z-request-content',false,'4028b21f7c9adb6a017c9b06161e0086','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('fdd00ba8ff1240cdaeaf526a4df96db5','fileUpload','z-request-file',false,'4028b21f7c9adb6a017c9b0616540089','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('b2f7a7991a474b42a0928714afafaa0b','inputBox','z-acceptor',false,'4028b21f7c9adb6a017c9b0616a9008d','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('c3b8421281e2499cbd2bc98098009a07','customCode','z-acceptor-department',false,'4028b21f7c9adb6a017c9b0616a9008d','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('2c0183c3015c414a823c09d13a6e49be','dateTime','z-accept-date',false,'4028b21f7c9adb6a017c9b0617160092','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('cdefccb8cf074e2f8ee1f741e80c483f','textArea','z-accept-content',false,'4028b21f7c9adb6a017c9b06174d0095','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('a7acc6870b4c4e6e147c2feb20bda6b4','ci','z-relation-ci',false,'4028b21f7c9adb6a017c9b0617870098','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('5aa28e3b3e2446258a15b99267bd063c','customCode','z-processor',false,'4028b21f7c9adb6a017c9b0617c5009b','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('966c2137e9d747e0a4ef5953189d596a','dateTime','z-process-date',false,'4028b21f7c9adb6a017c9b061813009f','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('a75adb2ca0f4d7023972794de97e979e','customCode','z-incident-category',false,'4028b21f7c9adb6a017c9b061813009f','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('aee06d995cbcad83c98df177479242c9','dropdown','z-incident-level',false,'4028b21f7c9adb6a017c9b06187e00a4','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('a6b76adaa1172d77d2b6cd62ad98db34','dropdown','z-incident-urgency',false,'4028b21f7c9adb6a017c9b06187e00a4','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('a2052b72aef1717cc785ead95c9b583c','textArea','z-incident-cause',false,'4028b21f7c9adb6a017c9b0618e600a9','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('a81e3ddc48a12baf94aebac8f2a4f6bc','textArea','z-incident-symptom',false,'4028b21f7c9adb6a017c9b06192700ac','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('aa27a4ea8b744f4bab35b7af3c2e913d','textArea','z-process-content',false,'4028b21f7c9adb6a017c9b06196700af','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('a9b723e29458f98a4904336baff53f6a','textArea','z-incident-plan',false,'4028b21f7c9adb6a017c9b06199e00b2','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('afd603a99e8287aa40706c175c697ae7','ci','z-incident-relation-ci',false,'4028b21f7c9adb6a017c9b0619d500b5','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('09f5924aeff4483daf730badbc1379d6','fileUpload','z-process-file',false,'4028b21f7c9adb6a017c9b061a1400b9','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('5a57a673747a46b0924716a33470a723','customCode','z-approver',false,'4028b21f7c9adb6a017c9b061a5600bc','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('39e8b33c67ad420599d9f57c6e23cbee','dateTime','z-approve-date',false,'4028b21f7c9adb6a017c9b061a9f00c0','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('5dba55dd1d57415ba28a3f1816859793','textArea','z-approve-content',false,'4028b21f7c9adb6a017c9b061ae500c5','4028b21f7c90d996017c91af9dcf0051');
/* 서비스데스크 - 장애신고 - 만족도 */
INSERT INTO wf_component VALUES ('a4257952286a4e6fae6faaeaae7279fd','image','z-logo',false,'4028b21f7c90d996017c914da7b90022','4028b21f7c90d996017c914bce270002');
INSERT INTO wf_component VALUES ('a19693dff90a45b889df568bbe177bcd','label','z-document-title',false,'4028b21f7c90d996017c914da7e10025','4028b21f7c90d996017c914bce270002');
INSERT INTO wf_component VALUES ('d2d8114f31bb47608d1facc6f03e18ac','divider','',false,'4028b21f7c90d996017c914da8090028','4028b21f7c90d996017c914bce270002');
INSERT INTO wf_component VALUES ('5904fbf514df42cbb8d859799d8e7f3f','radio','z-satisfaction',false,'4028b21f7c90d996017c914da836002a','4028b21f7c90d996017c914bce270002');
INSERT INTO wf_component VALUES ('207f974ae4654e7f8331526c504d0152','textArea','z-satisfaction-content',false,'4028b21f7c90d996017c914da85b002d','4028b21f7c90d996017c914bce270002');
/* 서비스데스크 - 서비스요청 */
INSERT INTO wf_component VALUES ('5d0b0faef24e429ba271e1bb2175d2ff','image','z-logo',false,'4028b21f7c9ff7c8017ca05446750002','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_component VALUES ('7811e5a93319403698481a00386f8b5e','label','z-document-title',false,'4028b21f7c9ff7c8017ca05446b40005','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_component VALUES ('c2972b99571a448ebdb8b2f981412060','divider','',false,'4028b21f7c9ff7c8017ca05446e30008','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_component VALUES ('93d2c6c791ac4824bf94add769417fe4','inputBox','z-requester',false,'4028b21f7c9ff7c8017ca0544721000b','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_component VALUES ('8929fddece384122a219fb7cfbb07312','customCode','z-requester-department',false,'4028b21f7c9ff7c8017ca0544721000b','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_component VALUES ('413ef0d9ef51415c9772baf19911b940','inputBox','z-requester-phone',false,'4028b21f7c9ff7c8017ca054477d0010','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_component VALUES ('3cdd30935a7e4da1b93e82e1ca65747f','inputBox','z-requester-email',false,'4028b21f7c9ff7c8017ca054477d0010','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_component VALUES ('76f13c0db2894fb5b8dcabc6e3e7a1fc','dateTime','z-request-date',false,'4028b21f7c9ff7c8017ca05447cf0015','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_component VALUES ('3304af52ee0a4009aaadb3917d5de7f8','dateTime','z-request-deadline',false,'4028b21f7c9ff7c8017ca05447cf0015','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_component VALUES ('8fe57acaefef4533b3897867cedb6579','inputBox','z-request-title',false,'4028b21f7c9ff7c8017ca0544826001b','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_component VALUES ('c4591d870dab40e3a18ca6f41250b200','customCode','z-request-category',false,'4028b21f7c9ff7c8017ca0544851001e','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_component VALUES ('4370df5c2ba44544a3444ce38240e61d','textArea','z-request-content',false,'4028b21f7c9ff7c8017ca054487b0021','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_component VALUES ('ea1841546d6648f49bafc2ebbe11c32a','fileUpload','z-request-file',false,'4028b21f7c9ff7c8017ca05448a50024','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_component VALUES ('f1867a396519411c848f65bfbda28d29','inputBox','z-acceptor',false,'4028b21f7c9ff7c8017ca05448e20028','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_component VALUES ('ee30c91397e242b5a1ddae91437b50ea','customCode','z-acceptor-department',false,'4028b21f7c9ff7c8017ca05448e20028','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_component VALUES ('a6d20858d6e84b85b1e75fc28ec36696','dateTime','z-accept-date',false,'4028b21f7c9ff7c8017ca0544932002d','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_component VALUES ('7afa8f35f45f412eb98db57dfd07b6b6','textArea','z-accept-content',false,'4028b21f7c9ff7c8017ca054495f0030','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_component VALUES ('ebcb50c94a9f494c8afd28d95c4aa79a','ci','z-relation-ci',false,'4028b21f7c9ff7c8017ca054498b0033','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_component VALUES ('afa6d40a2b304474ab7f424670587959','customCode','z-processor',false,'4028b21f7c9ff7c8017ca05449b50036','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_component VALUES ('1f3a106094dd4e02b170f20396d526de','dateTime','z-process-date',false,'4028b21f7c9ff7c8017ca05449ef003a','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_component VALUES ('d80c69ade3fc48f1bd78e1b4b02397d7','textArea','z-process-content',false,'4028b21f7c9ff7c8017ca0544a1e003d','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_component VALUES ('68098b156d354c069bfaf660b5b03f9d','textArea','z-process-etc',false,'4028b21f7c9ff7c8017ca0544a490040','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_component VALUES ('7f1e91223181424b89172127908058f5','ci','z-process-relation-ci',false,'4028b21f7c9ff7c8017ca0544a760043','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_component VALUES ('7c77ea3ec86c41ecb6c0b4dc1ddcd9f9','fileUpload','z-process-file',false,'4028b21f7c9ff7c8017ca0544aa30047','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_component VALUES ('8a4339add3c54b3aba9e4864c16c558c','customCode','z-approver',false,'4028b21f7c9ff7c8017ca0544ace004a','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_component VALUES ('ba5482f4fcfc45d7b20a04328670f11b','dateTime','z-approve-date',false,'4028b21f7c9ff7c8017ca0544b09004e','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_component VALUES ('23e3399e82b644d09ab20b281860a4cb','textArea','z-approve-content',false,'4028b21f7c9ff7c8017ca0544b390053','4028b21f7c9cc269017c9cc8cbf60001');
/* 서비스데스크 - 서비스요청 - 만족도 */
INSERT INTO wf_component VALUES ('c3c6191d9ee748bebc5745bb5be27452','image','z-logo',false,'4028b21f7c90d996017c914eec400041','4028b21f7c90d996017c914e27340030');
INSERT INTO wf_component VALUES ('90eedeed14494ea4b17d98bd4e8d0a69','label','z-document-title',false,'4028b21f7c90d996017c914eec670044','4028b21f7c90d996017c914e27340030');
INSERT INTO wf_component VALUES ('8f33d3a207b04bb886a87e1e01859500','divider','',false,'4028b21f7c90d996017c914eec970047','4028b21f7c90d996017c914e27340030');
INSERT INTO wf_component VALUES ('0986886e23a044659c7bb280347064d2','radio','z-satisfaction',false,'4028b21f7c90d996017c914eecc30049','4028b21f7c90d996017c914e27340030');
INSERT INTO wf_component VALUES ('5e203c7bace44cb58e1c38cee9372404','textArea','z-satisfaction-content',false,'4028b21f7c90d996017c914eecf3004c','4028b21f7c90d996017c914e27340030');
/* 인프라변경관리 */
INSERT INTO wf_component VALUES ('b5e06a4da8914e08b63a589fe16578db', 'image', 'z-logo', false, '4028b8817cbfc7a7017cc095a56d0ae9', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('44026fa563f649779f65f52dc0bfd0d6', 'label', 'z-change-infra-document-title', false, '4028b8817cbfc7a7017cc095a5800aec', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('a70e12f4514b4377bc9a94eb0f80deeb', 'divider', '', false, '4028b8817cbfc7a7017cc095a58d0af0', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('6df0771bb8a94deda97d0d6ba42ef01e', 'inputBox', 'z-requester', false, '4028b8817cbfc7a7017cc095a5a20af3', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('1a4024f7913e426383187d6bcc7962af', 'customCode', 'z-requester-department', false, '4028b8817cbfc7a7017cc095a5a20af3', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('11ed46ce208341fd842be00ced0e0381', 'inputBox', 'z-requester-phone', false, '4028b8817cbfc7a7017cc095a5c30af8', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('a50504ee81204910a1e13782987b4f89', 'inputBox', 'z-requester-email', false, '4028b8817cbfc7a7017cc095a5c30af8', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('610a36dd4d28448f9ca09623962d64e5', 'dateTime', 'z-request-date', false, '4028b8817cbfc7a7017cc095a5e00afd', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('9bbb34c9d2fe40ba802ee9f674210fe5', 'dateTime', 'z-request-deadline', false, '4028b8817cbfc7a7017cc095a5e00afd', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('c520f41591ca4c18bc89b08e5b58a78b', 'inputBox', 'z-request-title', true, '4028b8817cbfc7a7017cc095a5fd0b02', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('5aa56cbcd2ab48bbbcffd0ba53af499c', 'customCode', 'z-request-category', false, '4028b8817cbfc7a7017cc095a60a0b05', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('654a2e1cbdf14f39a883ff4ab168046c', 'textArea', 'z-request-content', false, '4028b8817cbfc7a7017cc095a61b0b09', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('41ff46f9f19d4e92853118fa1e914877', 'fileUpload', 'z-request-file', false, '4028b8817cbfc7a7017cc095a6290b0c', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('2c260851462b40ea82949561ed0f2b68', 'dateTime', 'z-accept-date', false, '4028b8817cbfc7a7017cc095a63b0b10', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('9e79bf1880094800b3b3d68044f40538', 'inputBox', 'z-change-infra-acceptor', false, '4028b8817cbfc7a7017cc095a64c0b13', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('59ffe940a3464a9ca3683224f7038c41', 'customCode', 'z-change-infra-acceptor-department', false, '4028b8817cbfc7a7017cc095a64c0b13', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('88660c980418450baf87a6d7ef510626', 'customCode', 'z-change-infra-processor', false, '4028b8817cbfc7a7017cc095a6650b18', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('f12492917ac64f418c37cfa98c4ab02d', 'customCode', 'z-change-infra-processor-department', false, '4028b8817cbfc7a7017cc095a6650b18', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('24f2a648a5ba458d9224a61c0cb1cdb0', 'dropdown', 'z-change-infra-impact', false, '4028b8817cbfc7a7017cc095a67e0b1d', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('15cd0e737e9c41c5a27132cfa18f370a', 'dropdown', 'z-change-infra-urgency', false, '4028b8817cbfc7a7017cc095a67e0b1d', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('bb38d2e37255417b8f61e0947e224974', 'textArea', 'z-change-infra-accept-content', false, '4028b8817cbfc7a7017cc095a6950b22', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('c7e485828f2b4c85a7cba5d6b3646b16', 'dateTime', 'z-change-infra-changeControlBoard-date', false, '4028b8817cbfc7a7017cc095a6a60b26', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('6deb6fcad6124c9a907fc4850285e342', 'inputBox', 'z-change-infra-changeControlBoard-meetingRoom', false, '4028b8817cbfc7a7017cc095a6b40b29', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('1d2ea0820d514512a1e3837e04fbc84b', 'inputBox', 'z-change-infra-changeControlBoard-subject', false, '4028b8817cbfc7a7017cc095a6c20b2c', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('2cb51de14f824f62bf9d5f634d276fdc', 'textArea', 'z-change-infra-changeControlBoard-consultation', false, '4028b8817cbfc7a7017cc095a6cf0b2f', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('c27d14120e1044a5b70fe4372ed78c2e', 'textArea', 'z-change-infra-changeControlBoard-result', false, '4028b8817cbfc7a7017cc095a6db0b32', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('3d5bb59a5b264430b2a8d1b19f6741ae', 'dateTime', 'z-change-infra-changePlan-date', false, '4028b8817cbfc7a7017cc095a6f10b36', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('e6d73057dd5a4b2a8cf01e15d3793eca', 'dateTime', 'z-change-infra-changePlan-expected-start-date', false, '4028b8817cbfc7a7017cc095a6fd0b37', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('afe2bec3307442f0be22af203e233f2f', 'dateTime', 'z-change-infra-changePlan-expected-end-date', false, '4028b8817cbfc7a7017cc095a6fd0b37', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('f5595e73f0654239aab9e66f71c528cc', 'radio', 'z-change-infra-changePlan-service-interruption', false, '4028b8817cbfc7a7017cc095a71b0b3c', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('b16f8424688f4e608d1f7b71f1aaf4b5', 'textArea', 'z-change-infra-changePlan-changeContents', false, '4028b8817cbfc7a7017cc095a7280b3f', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('2692ffba732d496498bd2f53f35016f9', 'textArea', 'z-change-infra-changePlan-recovery-failure-plan', false, '4028b8817cbfc7a7017cc095a7330b42', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('961037311d5c4201b869c642abdbf394', 'textArea', 'z-change-infra-changePlan-planContents', false, '4028b8817cbfc7a7017cc095a73f0b45', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('3c8cd6bf769041a68c628c1eb1fa2bd2', 'textArea', 'z-change-infra-changePlan-etc', false, '4028b8817cbfc7a7017cc095a74b0b48', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('f5081a7fd40d46da9b65daf5645906a0', 'ci', 'z-process-relation-ci', false, '4028b8817cbfc7a7017cc095a7570b4b', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('663450c3389c465584685365602dd961', 'fileUpload', 'z-change-infra-changePlan-fileupload', false, '4028b8817cbfc7a7017cc095a7630b4e', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('2437c9c99fc14cb895f2290519515a7b', 'customCode', 'z-change-infra-changePlan-approver', false, '4028b8817cbfc7a7017cc095a7720b51', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('1991ff59e63246ccb13a7946379b14e7', 'dateTime', 'z-change-infra-changePlan-approve-date', false, '4028b8817cbfc7a7017cc095a7840b55', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('51ac11434d1a4c0ca87b4930ffce047c', 'textArea', 'z-change-infra-changePlan-approve-content', false, '4028b8817cbfc7a7017cc095a7910b58', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('5e32d98e8bcd4e8e9cc8cdaea9ee45ff', 'dateTime', 'z-change-infra-changeResult-process-date', false, '4028b8817cbfc7a7017cc095a7a30b5c', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('1c9cb6326b104af88ff79743ce63bcce', 'radio', 'z-change-infra-result', false, '4028b8817cbfc7a7017cc095a7b00b5f', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('45fdeda567e449849075599eaa4a8c06', 'textArea', 'z-change-infra-changeResult-content', false, '4028b8817cbfc7a7017cc095a7bd0b62', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('3f2ea69c92bd42a5a903b0d8884b19ab', 'fileUpload', 'z-change-infra-changeResult-fileupload', false, '4028b8817cbfc7a7017cc095a7ca0b65', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('061ec27ed5e3446db15b256afb694f7b', 'dateTime', 'z-change-infra-postImplementationReview-start-date', false, '4028b8817cbfc7a7017cc095a7e00b69', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('a09757a1c1bb40e381e491474bd8ce75', 'dateTime', 'z-change-infra-postImplementationReview-end-date', false, '4028b8817cbfc7a7017cc095a7e00b69', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('6a78f0ee60434ebab14d1b2854057e4c', 'radio', 'z-change-infra-postImplementationReview-result', false, '4028b8817cbfc7a7017cc095a7f90b6e', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('7ce9995e0c2c40c18b8404f73684bd5d', 'textArea', 'z-change-infra-postImplementationReview-content', false, '4028b8817cbfc7a7017cc095a8060b71', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('9312d808fce84d5fb061a99e6d779c4d', 'customCode', 'z-change-infra-complete-reviewer', false, '4028b8817cbfc7a7017cc095a8120b74', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('d189734cda47408ca2a33a4d6b8d04b2', 'dateTime', 'z-change-infra-complete-review-date', false, '4028b8817cbfc7a7017cc095a8200b78', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('60b0b5ffba4047bbb2f2779f40df84ac', 'textArea', 'z-change-infra-complete-review', false, '4028b8817cbfc7a7017cc095a82a0b7b', '4028b8817cbfc7a7017cc08f7b0b0763');
/**
 * 컴포넌트 세부속성
 */
DROP TABLE IF EXISTS wf_component_property cascade;

CREATE TABLE wf_component_property (
    component_id varchar(128) NULL,
    property_type varchar(100) NULL,
    property_options text NULL,
    CONSTRAINT wf_component_property_pk PRIMARY KEY (component_id,property_type),
    CONSTRAINT wf_component_property_fk FOREIGN KEY (component_id) REFERENCES wf_component(component_id)
);

COMMENT ON TABLE wf_component_property IS '컴포넌트 세부속성';
COMMENT ON COLUMN wf_component_property.component_id IS '컴포넌트 아이디';
COMMENT ON COLUMN wf_component_property.property_type IS '속성 타입';
COMMENT ON COLUMN wf_component_property.property_options IS '속성 값';

/* 서비스데스크 - 단순문의 */
INSERT INTO wf_component_property VALUES ('ae3e2a000d67b4e2e8d83bf36c81260a','validation','{"required":false}');
INSERT INTO wf_component_property VALUES ('ae3e2a000d67b4e2e8d83bf36c81260a','label','{"position":"hidden","fontSize":"14","fontColor":"rgba(141, 146, 153, 1)","bold":false,"italic":false,"underline":false,"align":"left","text":""}');
INSERT INTO wf_component_property VALUES ('ae3e2a000d67b4e2e8d83bf36c81260a','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('ae3e2a000d67b4e2e8d83bf36c81260a','element','{"columnWidth":"12","path":"file:///logo.png","width":"155","height":"26","align":"left"}');
INSERT INTO wf_component_property VALUES ('aa83af616d59cc86c565a0282153c236','label','{"position":"left","fontSize":"40","fontColor":"#222529","bold":false,"italic":false,"underline":false,"align":"center","text":"단순문의"}');
INSERT INTO wf_component_property VALUES ('aa83af616d59cc86c565a0282153c236','element','{"columnWidth":"0","labelWidth":"10","text":"","fontSize":"12","align":"left","fontOption":"","fontOptionBold":"N","fontOptionItalic":"N","fontOptionUnderline":"N","fontColor":"#8B9094"}');
INSERT INTO wf_component_property VALUES ('aa83af616d59cc86c565a0282153c236','validation','{"required":false}');
INSERT INTO wf_component_property VALUES ('aa83af616d59cc86c565a0282153c236','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('aeaba514c85489bea2ae4c588df41f03','element','{"columnWidth":"12","thickness":"3","color":"#CFD5D9","type":"solid"}');
INSERT INTO wf_component_property VALUES ('aeaba514c85489bea2ae4c588df41f03','label','{"position":"hidden","fontSize":"14","fontColor":"#CFD5D9","bold":false,"italic":false,"underline":false,"align":"left","text":""}');
INSERT INTO wf_component_property VALUES ('aeaba514c85489bea2ae4c588df41f03','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('aeaba514c85489bea2ae4c588df41f03','validation','{"required":false}');
INSERT INTO wf_component_property VALUES ('ad15aab4783f55533c6c1f183a4b60cc','element','{"placeholder":"","columnWidth":"9","defaultValueSelect":"select|userName"}');
INSERT INTO wf_component_property VALUES ('ad15aab4783f55533c6c1f183a4b60cc','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"신청자"}');
INSERT INTO wf_component_property VALUES ('ad15aab4783f55533c6c1f183a4b60cc','validation','{"validationType":"none","required":true,"minLength":"0","maxLength":"100"}');
INSERT INTO wf_component_property VALUES ('ad15aab4783f55533c6c1f183a4b60cc','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('ae4f48236ecf493eb0212d77ac14c360','validation','{"required":true}');
INSERT INTO wf_component_property VALUES ('ae4f48236ecf493eb0212d77ac14c360','display','{"displayOrder":1,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('ae4f48236ecf493eb0212d77ac14c360','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"신청부서"}');
INSERT INTO wf_component_property VALUES ('ae4f48236ecf493eb0212d77ac14c360','element','{"columnWidth":"9","defaultValueCustomCode":"40288a19736b46fb01736b89e46c0009|session|department"}');
INSERT INTO wf_component_property VALUES ('a71d2d6094b2920f87ba8eaf025af1c3','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"전화번호"}');
INSERT INTO wf_component_property VALUES ('a71d2d6094b2920f87ba8eaf025af1c3','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('a71d2d6094b2920f87ba8eaf025af1c3','validation','{"validationType":"phone","required":false,"minLength":"0","maxLength":"100"}');
INSERT INTO wf_component_property VALUES ('a71d2d6094b2920f87ba8eaf025af1c3','element','{"placeholder":"","columnWidth":"9","defaultValueSelect":"select|officeNumber"}');
INSERT INTO wf_component_property VALUES ('a4a100bbcd9907aae4e260c8bfa3b45f','display','{"displayOrder":1,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('a4a100bbcd9907aae4e260c8bfa3b45f','validation','{"validationType":"email","required":false,"minLength":"0","maxLength":"100"}');
INSERT INTO wf_component_property VALUES ('a4a100bbcd9907aae4e260c8bfa3b45f','element','{"placeholder":"","columnWidth":"9","defaultValueSelect":"select|email"}');
INSERT INTO wf_component_property VALUES ('a4a100bbcd9907aae4e260c8bfa3b45f','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"메일주소"}');
INSERT INTO wf_component_property VALUES ('a115df4520fea935e4482784064c7a51','validation','{"required":true,"minDateTime":"","maxDateTime":""}');
INSERT INTO wf_component_property VALUES ('a115df4520fea935e4482784064c7a51','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"신청일시"}');
INSERT INTO wf_component_property VALUES ('a115df4520fea935e4482784064c7a51','element','{"columnWidth":"9","defaultValueRadio":"now"}');
INSERT INTO wf_component_property VALUES ('a115df4520fea935e4482784064c7a51','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('a0c3e8d18663483284b7e9fe83d0b824','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"완료희망일시"}');
INSERT INTO wf_component_property VALUES ('a0c3e8d18663483284b7e9fe83d0b824','display','{"displayOrder":1,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('a0c3e8d18663483284b7e9fe83d0b824','validation','{"required":true,"minDateTime":"","maxDateTime":""}');
INSERT INTO wf_component_property VALUES ('a0c3e8d18663483284b7e9fe83d0b824','element','{"columnWidth":"9","defaultValueRadio":"now"}');
INSERT INTO wf_component_property VALUES ('a75c85bba7aa722f9dcbcbcfad0b6ac7','validation','{"validationType":"none","required":true,"minLength":"0","maxLength":"100"}');
INSERT INTO wf_component_property VALUES ('a75c85bba7aa722f9dcbcbcfad0b6ac7','element','{"placeholder":"제목을 입력하세요.","columnWidth":"10","defaultValueSelect":"input|"}');
INSERT INTO wf_component_property VALUES ('a75c85bba7aa722f9dcbcbcfad0b6ac7','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('a75c85bba7aa722f9dcbcbcfad0b6ac7','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"제목"}');
INSERT INTO wf_component_property VALUES ('ade3320b3bc9dca17651e4c0cdef89e4','validation','{"required":true}');
INSERT INTO wf_component_property VALUES ('ade3320b3bc9dca17651e4c0cdef89e4','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"서비스 항목"}');
INSERT INTO wf_component_property VALUES ('ade3320b3bc9dca17651e4c0cdef89e4','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('ade3320b3bc9dca17651e4c0cdef89e4','element','{"columnWidth":"10","defaultValueCustomCode":"40288a19736b46fb01736b89e46c0010|code|form.template.serviceDesk.inquiry.category.etc|etc"}');
INSERT INTO wf_component_property VALUES ('a3b140da99dbfb49beb7fa3db09042b4','element','{"columnWidth":"10","rows":"3","placeholder":"내용을 입력하세요."}');
INSERT INTO wf_component_property VALUES ('a3b140da99dbfb49beb7fa3db09042b4','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('a3b140da99dbfb49beb7fa3db09042b4','validation','{"required":true,"minLength":"0","maxLength":"512"}');
INSERT INTO wf_component_property VALUES ('a3b140da99dbfb49beb7fa3db09042b4','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"내용"}');
INSERT INTO wf_component_property VALUES ('a9404877fabbd09dc2603f78283fa155','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('a9404877fabbd09dc2603f78283fa155','validation','{"required":false}');
INSERT INTO wf_component_property VALUES ('a9404877fabbd09dc2603f78283fa155','element','{"columnWidth":"10","align":"left"}');
INSERT INTO wf_component_property VALUES ('a9404877fabbd09dc2603f78283fa155','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"첨부파일"}');
INSERT INTO wf_component_property VALUES ('ab6d29d8ef257f93de98c655fbaab22a','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"접수자"}');
INSERT INTO wf_component_property VALUES ('ab6d29d8ef257f93de98c655fbaab22a','element','{"placeholder":"","columnWidth":"9","defaultValueSelect":"select|userName"}');
INSERT INTO wf_component_property VALUES ('ab6d29d8ef257f93de98c655fbaab22a','validation','{"validationType":"none","required":true,"minLength":"0","maxLength":"100"}');
INSERT INTO wf_component_property VALUES ('ab6d29d8ef257f93de98c655fbaab22a','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('ab266326aa59ed5bb4411da4142aa24a','display','{"displayOrder":1,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('ab266326aa59ed5bb4411da4142aa24a','validation','{"required":true}');
INSERT INTO wf_component_property VALUES ('ab266326aa59ed5bb4411da4142aa24a','element','{"columnWidth":"9","defaultValueCustomCode":"40288a19736b46fb01736b89e46c0009|session|department"}');
INSERT INTO wf_component_property VALUES ('ab266326aa59ed5bb4411da4142aa24a','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"접수부서"}');
INSERT INTO wf_component_property VALUES ('a8e347190f51b1b05200f34d9e8b8ab0','element','{"columnWidth":"10","defaultValueRadio":"now"}');
INSERT INTO wf_component_property VALUES ('a8e347190f51b1b05200f34d9e8b8ab0','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"접수일시"}');
INSERT INTO wf_component_property VALUES ('a8e347190f51b1b05200f34d9e8b8ab0','validation','{"required":true,"minDateTime":"","maxDateTime":""}');
INSERT INTO wf_component_property VALUES ('a8e347190f51b1b05200f34d9e8b8ab0','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('a71977859617f137f589108c24fa7845','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('a71977859617f137f589108c24fa7845','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"접수의견"}');
INSERT INTO wf_component_property VALUES ('a71977859617f137f589108c24fa7845','validation','{"required":false,"minLength":"0","maxLength":"512"}');
INSERT INTO wf_component_property VALUES ('a71977859617f137f589108c24fa7845','element','{"columnWidth":"10","rows":"3","placeholder":"내용을 입력하세요."}');
INSERT INTO wf_component_property VALUES ('acc02e1f2f51aa01649b6e06f7b35ec4','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('acc02e1f2f51aa01649b6e06f7b35ec4','validation','{"required":true}');
INSERT INTO wf_component_property VALUES ('acc02e1f2f51aa01649b6e06f7b35ec4','element','{"columnWidth":"10","defaultValueCustomCode":"40288a19736b46fb01736b89e46c0008|session|userName"}');
INSERT INTO wf_component_property VALUES ('acc02e1f2f51aa01649b6e06f7b35ec4','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"처리자"}');
INSERT INTO wf_component_property VALUES ('a83a54dfec6c65dc5983272ec9a956ce','element','{"columnWidth":"10","defaultValueRadio":"now"}');
INSERT INTO wf_component_property VALUES ('a83a54dfec6c65dc5983272ec9a956ce','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('a83a54dfec6c65dc5983272ec9a956ce','validation','{"required":true,"minDateTime":"","maxDateTime":""}');
INSERT INTO wf_component_property VALUES ('a83a54dfec6c65dc5983272ec9a956ce','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"처리일시"}');
INSERT INTO wf_component_property VALUES ('ae07bae262c57399a4ee8b3dea2fcbab','validation','{"required":true,"minLength":"0","maxLength":"512"}');
INSERT INTO wf_component_property VALUES ('ae07bae262c57399a4ee8b3dea2fcbab','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('ae07bae262c57399a4ee8b3dea2fcbab','element','{"columnWidth":"10","rows":"3","placeholder":"내용을 입력하세요."}');
INSERT INTO wf_component_property VALUES ('ae07bae262c57399a4ee8b3dea2fcbab','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"처리내용"}');
INSERT INTO wf_component_property VALUES ('a27c22d07de4231ab41863dab45450f1','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('a27c22d07de4231ab41863dab45450f1','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"첨부파일"}');
INSERT INTO wf_component_property VALUES ('a27c22d07de4231ab41863dab45450f1','element','{"columnWidth":"10","align":"left"}');
INSERT INTO wf_component_property VALUES ('a27c22d07de4231ab41863dab45450f1','validation','{"required":false}');
INSERT INTO wf_component_property VALUES ('a4c410628aa58b1863935b4056395b80','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('a4c410628aa58b1863935b4056395b80','validation','{"required":true}');
INSERT INTO wf_component_property VALUES ('a4c410628aa58b1863935b4056395b80','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"승인자"}');
INSERT INTO wf_component_property VALUES ('a4c410628aa58b1863935b4056395b80','element','{"columnWidth":"10","defaultValueCustomCode":"40288a19736b46fb01736b89e46c0008|session|userName"}');
INSERT INTO wf_component_property VALUES ('a10c935dbdf1c30b34dfabe5ebc0ba73','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('a10c935dbdf1c30b34dfabe5ebc0ba73','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"승인 / 반려 일시"}');
INSERT INTO wf_component_property VALUES ('a10c935dbdf1c30b34dfabe5ebc0ba73','element','{"columnWidth":"10","defaultValueRadio":"now"}');
INSERT INTO wf_component_property VALUES ('a10c935dbdf1c30b34dfabe5ebc0ba73','validation','{"required":true,"minDateTime":"","maxDateTime":""}');
INSERT INTO wf_component_property VALUES ('ab97abfd66b5654e3f525e617df9afaf','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"승인 / 반려 의견"}');
INSERT INTO wf_component_property VALUES ('ab97abfd66b5654e3f525e617df9afaf','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('ab97abfd66b5654e3f525e617df9afaf','validation','{"required":false,"minLength":"0","maxLength":"512"}');
INSERT INTO wf_component_property VALUES ('ab97abfd66b5654e3f525e617df9afaf','element','{"columnWidth":"10","rows":"3","placeholder":""}');
/* 서비스데스크 - 단순문의 - 만족도 */
INSERT INTO wf_component_property VALUES ('a605df0d26f09a3d20caaea3977a5c64','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('a605df0d26f09a3d20caaea3977a5c64','validation','{"required":false}');
INSERT INTO wf_component_property VALUES ('a605df0d26f09a3d20caaea3977a5c64','element','{"columnWidth":"12","path":"file:///logo.png","width":"155","height":"26","align":"left"}');
INSERT INTO wf_component_property VALUES ('a605df0d26f09a3d20caaea3977a5c64','label','{"position":"hidden","fontSize":"14","fontColor":"rgba(141, 146, 153, 1)","bold":false,"italic":false,"underline":false,"align":"left","text":""}');
INSERT INTO wf_component_property VALUES ('ae54c20fd574a57845fffaa7bb936eb4','label','{"position":"left","fontSize":"40","fontColor":"#222529","bold":false,"italic":false,"underline":false,"align":"center","text":"만족도 평가"}');
INSERT INTO wf_component_property VALUES ('ae54c20fd574a57845fffaa7bb936eb4','validation','{"required":false}');
INSERT INTO wf_component_property VALUES ('ae54c20fd574a57845fffaa7bb936eb4','element','{"columnWidth":"0","labelWidth":"10","text":"","fontSize":"12","align":"left","fontOption":"","fontOptionBold":"N","fontOptionItalic":"N","fontOptionUnderline":"N","fontColor":"#8B9094"}');
INSERT INTO wf_component_property VALUES ('ae54c20fd574a57845fffaa7bb936eb4','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('a7cc83ce97c50faa754c702bf018c505','element','{"columnWidth":"12","thickness":"3","color":"#CFD5D9","type":"solid"}');
INSERT INTO wf_component_property VALUES ('a7cc83ce97c50faa754c702bf018c505','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('a7cc83ce97c50faa754c702bf018c505','validation','{"required":false}');
INSERT INTO wf_component_property VALUES ('a7cc83ce97c50faa754c702bf018c505','label','{"position":"hidden","fontSize":"14","fontColor":"rgba(141, 146, 153, 1)","bold":false,"italic":false,"underline":false,"align":"left","text":""}');
INSERT INTO wf_component_property VALUES ('ac69d66f3ec394b36215cd2ee3983292','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('ac69d66f3ec394b36215cd2ee3983292','validation','{"required":true}');
INSERT INTO wf_component_property VALUES ('ac69d66f3ec394b36215cd2ee3983292','element','{"position":"left","columnWidth":"10","align":"horizontal","options":[{"name":"매우불만족","value":"1", "checked": false},{"name":"불만족","value":"2", "checked": false},{"name":"보통","value":"3", "checked": false},{"name":"만족","value":"4", "checked": false},{"name":"매우만족","value":"5", "checked": true}]}');
INSERT INTO wf_component_property VALUES ('ac69d66f3ec394b36215cd2ee3983292','label','{"position":"left","fontSize":"14","fontColor":"rgba(141, 146, 153, 1)","bold":false,"italic":false,"underline":false,"align":"left","text":"만족도"}');
INSERT INTO wf_component_property VALUES ('ad2996c39febdc13b32d08354169d6ac','validation','{"required":false,"minLength":"0","maxLength":"512"}');
INSERT INTO wf_component_property VALUES ('ad2996c39febdc13b32d08354169d6ac','element','{"columnWidth":"10","rows":"3","placeholder":"내용을 입력하세요."}');
INSERT INTO wf_component_property VALUES ('ad2996c39febdc13b32d08354169d6ac','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"평가의견"}');
INSERT INTO wf_component_property VALUES ('ad2996c39febdc13b32d08354169d6ac','display','{"displayOrder":0,"columnWidth":"12"}');
/* 서비스데스크 - 장애신고 */
INSERT INTO wf_component_property VALUES ('0138fd96d2ca4075b8d8ff4ef4b9fe10','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('0138fd96d2ca4075b8d8ff4ef4b9fe10','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"제목"}');
INSERT INTO wf_component_property VALUES ('0138fd96d2ca4075b8d8ff4ef4b9fe10','element','{"placeholder":"제목을 입력하세요.","columnWidth":"10","defaultValueSelect":"input|"}');
INSERT INTO wf_component_property VALUES ('0138fd96d2ca4075b8d8ff4ef4b9fe10','validation','{"validationType":"none","required":true,"minLength":"0","maxLength":"100"}');
INSERT INTO wf_component_property VALUES ('09f5924aeff4483daf730badbc1379d6','element','{"columnWidth":"10","align":"left"}');
INSERT INTO wf_component_property VALUES ('09f5924aeff4483daf730badbc1379d6','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('09f5924aeff4483daf730badbc1379d6','validation','{"required":false}');
INSERT INTO wf_component_property VALUES ('09f5924aeff4483daf730badbc1379d6','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"첨부파일"}');
INSERT INTO wf_component_property VALUES ('250e5452b08344758d08fb59915c4e95','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"내용"}');
INSERT INTO wf_component_property VALUES ('250e5452b08344758d08fb59915c4e95','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('250e5452b08344758d08fb59915c4e95','validation','{"required":true,"minLength":"0","maxLength":"512"}');
INSERT INTO wf_component_property VALUES ('250e5452b08344758d08fb59915c4e95','element','{"columnWidth":"10","rows":"3","placeholder":"내용을 입력하세요."}');
INSERT INTO wf_component_property VALUES ('2c0183c3015c414a823c09d13a6e49be','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('2c0183c3015c414a823c09d13a6e49be','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"접수일시"}');
INSERT INTO wf_component_property VALUES ('2c0183c3015c414a823c09d13a6e49be','element','{"columnWidth":"10","defaultValueRadio":"now"}');
INSERT INTO wf_component_property VALUES ('2c0183c3015c414a823c09d13a6e49be','validation','{"required":true,"minDateTime":"","maxDateTime":""}');
INSERT INTO wf_component_property VALUES ('39e8b33c67ad420599d9f57c6e23cbee','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('39e8b33c67ad420599d9f57c6e23cbee','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"승인 / 반려 일시"}');
INSERT INTO wf_component_property VALUES ('39e8b33c67ad420599d9f57c6e23cbee','validation','{"required":true,"minDateTime":"","maxDateTime":""}');
INSERT INTO wf_component_property VALUES ('39e8b33c67ad420599d9f57c6e23cbee','element','{"columnWidth":"10","defaultValueRadio":"now"}');
INSERT INTO wf_component_property VALUES ('4b8b310945214d0bbd262352802f93c6','element','{"columnWidth":"9","defaultValueRadio":"now"}');
INSERT INTO wf_component_property VALUES ('4b8b310945214d0bbd262352802f93c6','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('4b8b310945214d0bbd262352802f93c6','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"신청일시"}');
INSERT INTO wf_component_property VALUES ('4b8b310945214d0bbd262352802f93c6','validation','{"required":true,"minDateTime":"","maxDateTime":""}');
INSERT INTO wf_component_property VALUES ('5a57a673747a46b0924716a33470a723','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('5a57a673747a46b0924716a33470a723','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"승인자"}');
INSERT INTO wf_component_property VALUES ('5a57a673747a46b0924716a33470a723','element','{"columnWidth":"10","defaultValueCustomCode":"40288a19736b46fb01736b89e46c0008|session|userName"}');
INSERT INTO wf_component_property VALUES ('5a57a673747a46b0924716a33470a723','validation','{"required":true}');
INSERT INTO wf_component_property VALUES ('5aa28e3b3e2446258a15b99267bd063c','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('5aa28e3b3e2446258a15b99267bd063c','validation','{"required":true}');
INSERT INTO wf_component_property VALUES ('5aa28e3b3e2446258a15b99267bd063c','element','{"columnWidth":"10","defaultValueCustomCode":"40288a19736b46fb01736b89e46c0008|session|userName"}');
INSERT INTO wf_component_property VALUES ('5aa28e3b3e2446258a15b99267bd063c','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"처리자"}');
INSERT INTO wf_component_property VALUES ('5dba55dd1d57415ba28a3f1816859793','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"승인 / 반려 의견"}');
INSERT INTO wf_component_property VALUES ('5dba55dd1d57415ba28a3f1816859793','validation','{"required":false,"minLength":"0","maxLength":"512"}');
INSERT INTO wf_component_property VALUES ('5dba55dd1d57415ba28a3f1816859793','element','{"columnWidth":"10","rows":"3","placeholder":""}');
INSERT INTO wf_component_property VALUES ('5dba55dd1d57415ba28a3f1816859793','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('77e97f770393455a97c645f7562b3b53','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('77e97f770393455a97c645f7562b3b53','validation','{"validationType":"none","required":true,"minLength":"0","maxLength":"100"}');
INSERT INTO wf_component_property VALUES ('77e97f770393455a97c645f7562b3b53','element','{"placeholder":"","columnWidth":"9","defaultValueSelect":"select|userName"}');
INSERT INTO wf_component_property VALUES ('77e97f770393455a97c645f7562b3b53','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"신청자"}');
INSERT INTO wf_component_property VALUES ('857ac1b2c7f3424ab297108583b3c5c1','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('857ac1b2c7f3424ab297108583b3c5c1','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"전화번호"}');
INSERT INTO wf_component_property VALUES ('857ac1b2c7f3424ab297108583b3c5c1','element','{"placeholder":"","columnWidth":"9","defaultValueSelect":"select|officeNumber"}');
INSERT INTO wf_component_property VALUES ('857ac1b2c7f3424ab297108583b3c5c1','validation','{"validationType":"phone","required":false,"minLength":"0","maxLength":"100"}');
INSERT INTO wf_component_property VALUES ('966c2137e9d747e0a4ef5953189d596a','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('966c2137e9d747e0a4ef5953189d596a','validation','{"required":true,"minDateTime":"","maxDateTime":""}');
INSERT INTO wf_component_property VALUES ('966c2137e9d747e0a4ef5953189d596a','element','{"columnWidth":"9","defaultValueRadio":"now"}');
INSERT INTO wf_component_property VALUES ('966c2137e9d747e0a4ef5953189d596a','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"처리일시"}');
INSERT INTO wf_component_property VALUES ('9a0a95161bbf4039a3bdfcba2a59a5e6','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('9a0a95161bbf4039a3bdfcba2a59a5e6','validation','{"required":false}');
INSERT INTO wf_component_property VALUES ('9a0a95161bbf4039a3bdfcba2a59a5e6','element','{"columnWidth":"12","thickness":"3","color":"#CFD5D9","type":"solid"}');
INSERT INTO wf_component_property VALUES ('9a0a95161bbf4039a3bdfcba2a59a5e6','label','{"position":"hidden","fontSize":"14","fontColor":"#CFD5D9","bold":false,"italic":false,"underline":false,"align":"left","text":""}');
INSERT INTO wf_component_property VALUES ('a2052b72aef1717cc785ead95c9b583c','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"장애원인"}');
INSERT INTO wf_component_property VALUES ('a2052b72aef1717cc785ead95c9b583c','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('a2052b72aef1717cc785ead95c9b583c','validation','{"required":true,"minLength":"0","maxLength":"512"}');
INSERT INTO wf_component_property VALUES ('a2052b72aef1717cc785ead95c9b583c','element','{"columnWidth":"10","rows":"3","placeholder":"내용을 입력하세요."}');
INSERT INTO wf_component_property VALUES ('a20925ede2aee30d0c74c4647c0c46dd','display','{"displayOrder":1,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('a20925ede2aee30d0c74c4647c0c46dd','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"장애인지경로"}');
INSERT INTO wf_component_property VALUES ('a20925ede2aee30d0c74c4647c0c46dd','element','{"columnWidth":"9","options":[{"name":"선택 안함","value":"none","checked": true},{"name":"전화 통화","value":"phone","checked": false},{"name":"서비스 포탈 및 모바일 애플리케이션","value":"portal","checked": false},{"name":"라이브 채팅 및 챗봇","value":"chatting","checked": false},{"name":"이메일","value":"email","checked": false},{"name":"대면","value":"interview","checked": false},{"name":"소셜 미디어","value":"sns","checked": false},{"name":"모니터링툴 자동 등록","value":"automatic","checked": false}]}');
INSERT INTO wf_component_property VALUES ('a20925ede2aee30d0c74c4647c0c46dd','validation','{"required":true}');
INSERT INTO wf_component_property VALUES ('a371d3cfecb547e4aff813ce0fca711c','label','{"position":"hidden","fontSize":"14","fontColor":"rgba(141, 146, 153, 1)","bold":false,"italic":false,"underline":false,"align":"left","text":""}');
INSERT INTO wf_component_property VALUES ('a371d3cfecb547e4aff813ce0fca711c','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('a371d3cfecb547e4aff813ce0fca711c','validation','{"required":false}');
INSERT INTO wf_component_property VALUES ('a371d3cfecb547e4aff813ce0fca711c','element','{"columnWidth":"12","path":"file:///logo.png","width":"155","height":"26","align":"left"}');
INSERT INTO wf_component_property VALUES ('a6b76adaa1172d77d2b6cd62ad98db34','display','{"displayOrder":1,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('a6b76adaa1172d77d2b6cd62ad98db34','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"긴급도"}');
INSERT INTO wf_component_property VALUES ('a6b76adaa1172d77d2b6cd62ad98db34','element','{"columnWidth":"9","options":[{"name":"매우 낮음","value":"0","checked": false},{"name":"낮음","value":"1","checked": false},{"name":"중간","value":"2","checked": false},{"name":"높음","value":"3","checked": false},{"name":"매우 높음","value":"4","checked": true}]}');
INSERT INTO wf_component_property VALUES ('a6b76adaa1172d77d2b6cd62ad98db34','validation','{"required":true}');
INSERT INTO wf_component_property VALUES ('a6eb123ed5ac913c55035f15bb30efce','validation','{"required":true,"minDateTime":"","maxDateTime":""}');
INSERT INTO wf_component_property VALUES ('a6eb123ed5ac913c55035f15bb30efce','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('a6eb123ed5ac913c55035f15bb30efce','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"장애발생일시"}');
INSERT INTO wf_component_property VALUES ('a6eb123ed5ac913c55035f15bb30efce','element','{"columnWidth":"9","defaultValueRadio":"now"}');
INSERT INTO wf_component_property VALUES ('a75adb2ca0f4d7023972794de97e979e','element','{"columnWidth":"9","defaultValueCustomCode":"40288a19736b46fb01736b89e46c0011|code|form.template.serviceDesk.incident.category.system|system"}');
INSERT INTO wf_component_property VALUES ('a75adb2ca0f4d7023972794de97e979e','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"장애유형"}');
INSERT INTO wf_component_property VALUES ('a75adb2ca0f4d7023972794de97e979e','validation','{"required":true}');
INSERT INTO wf_component_property VALUES ('a75adb2ca0f4d7023972794de97e979e','display','{"displayOrder":1,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('a7acc6870b4c4e6e147c2feb20bda6b4','element','{"columnWidth":"12","isEditable":false}');
INSERT INTO wf_component_property VALUES ('a7acc6870b4c4e6e147c2feb20bda6b4','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('a7acc6870b4c4e6e147c2feb20bda6b4','validation','{"required":false}');
INSERT INTO wf_component_property VALUES ('a7acc6870b4c4e6e147c2feb20bda6b4','label','{"position":"top","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"관련 CI"}');
INSERT INTO wf_component_property VALUES ('a81e3ddc48a12baf94aebac8f2a4f6bc','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('a81e3ddc48a12baf94aebac8f2a4f6bc','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"장애증상"}');
INSERT INTO wf_component_property VALUES ('a81e3ddc48a12baf94aebac8f2a4f6bc','element','{"columnWidth":"10","rows":"3","placeholder":"내용을 입력하세요"}');
INSERT INTO wf_component_property VALUES ('a81e3ddc48a12baf94aebac8f2a4f6bc','validation','{"required":true,"minLength":"0","maxLength":"512"}');
INSERT INTO wf_component_property VALUES ('a9b723e29458f98a4904336baff53f6a','element','{"columnWidth":"10","rows":"3","placeholder":"내용을 입력하세요."}');
INSERT INTO wf_component_property VALUES ('a9b723e29458f98a4904336baff53f6a','validation','{"required":false,"minLength":"0","maxLength":"512"}');
INSERT INTO wf_component_property VALUES ('a9b723e29458f98a4904336baff53f6a','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('a9b723e29458f98a4904336baff53f6a','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"향후대책"}');
INSERT INTO wf_component_property VALUES ('aa27a4ea8b744f4bab35b7af3c2e913d','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('aa27a4ea8b744f4bab35b7af3c2e913d','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"처리내용 및 결과"}');
INSERT INTO wf_component_property VALUES ('aa27a4ea8b744f4bab35b7af3c2e913d','validation','{"required":true,"minLength":"0","maxLength":"512"}');
INSERT INTO wf_component_property VALUES ('aa27a4ea8b744f4bab35b7af3c2e913d','element','{"columnWidth":"10","rows":"3","placeholder":"내용을 입력하세요."}');
INSERT INTO wf_component_property VALUES ('ae79624833414ab3a3ff37582c234aa2','validation','{"required":true,"minDateTime":"","maxDateTime":""}');
INSERT INTO wf_component_property VALUES ('ae79624833414ab3a3ff37582c234aa2','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"완료희망일시"}');
INSERT INTO wf_component_property VALUES ('ae79624833414ab3a3ff37582c234aa2','element','{"columnWidth":"9","defaultValueRadio":"now"}');
INSERT INTO wf_component_property VALUES ('ae79624833414ab3a3ff37582c234aa2','display','{"displayOrder":1,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('aee06d995cbcad83c98df177479242c9','validation','{"required":true}');
INSERT INTO wf_component_property VALUES ('aee06d995cbcad83c98df177479242c9','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('aee06d995cbcad83c98df177479242c9','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"장애등급"}');
INSERT INTO wf_component_property VALUES ('aee06d995cbcad83c98df177479242c9','element','{"columnWidth":"9","options":[{"name":"1등급","value":"1","checked": true},{"name":"2등급","value":"2","checked": false},{"name":"3등급","value":"3","checked": false},{"name":"4등급","value":"4","checked": false}]}');
INSERT INTO wf_component_property VALUES ('afd603a99e8287aa40706c175c697ae7','label','{"position":"top","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"관련 CI"}');
INSERT INTO wf_component_property VALUES ('afd603a99e8287aa40706c175c697ae7','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('afd603a99e8287aa40706c175c697ae7','validation','{"required":false}');
INSERT INTO wf_component_property VALUES ('afd603a99e8287aa40706c175c697ae7','element','{"columnWidth":"12","isEditable":false}');
INSERT INTO wf_component_property VALUES ('b2f7a7991a474b42a0928714afafaa0b','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"접수자"}');
INSERT INTO wf_component_property VALUES ('b2f7a7991a474b42a0928714afafaa0b','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('b2f7a7991a474b42a0928714afafaa0b','element','{"placeholder":"","columnWidth":"9","defaultValueSelect":"select|userName"}');
INSERT INTO wf_component_property VALUES ('b2f7a7991a474b42a0928714afafaa0b','validation','{"validationType":"none","required":true,"minLength":"0","maxLength":"100"}');
INSERT INTO wf_component_property VALUES ('b960f194ab8e4ecda04c319398bf83ea','validation','{"validationType":"email","required":false,"minLength":"0","maxLength":"100"}');
INSERT INTO wf_component_property VALUES ('b960f194ab8e4ecda04c319398bf83ea','display','{"displayOrder":1,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('b960f194ab8e4ecda04c319398bf83ea','element','{"placeholder":"","columnWidth":"9","defaultValueSelect":"select|email"}');
INSERT INTO wf_component_property VALUES ('b960f194ab8e4ecda04c319398bf83ea','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"메일주소"}');
INSERT INTO wf_component_property VALUES ('c3b8421281e2499cbd2bc98098009a07','validation','{"required":true}');
INSERT INTO wf_component_property VALUES ('c3b8421281e2499cbd2bc98098009a07','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"접수부서"}');
INSERT INTO wf_component_property VALUES ('c3b8421281e2499cbd2bc98098009a07','display','{"displayOrder":1,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('c3b8421281e2499cbd2bc98098009a07','element','{"columnWidth":"9","defaultValueCustomCode":"40288a19736b46fb01736b89e46c0009|session|department"}');
INSERT INTO wf_component_property VALUES ('cdefccb8cf074e2f8ee1f741e80c483f','validation','{"required":false,"minLength":"0","maxLength":"512"}');
INSERT INTO wf_component_property VALUES ('cdefccb8cf074e2f8ee1f741e80c483f','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"접수의견"}');
INSERT INTO wf_component_property VALUES ('cdefccb8cf074e2f8ee1f741e80c483f','element','{"columnWidth":"10","rows":"3","placeholder":"내용을 입력하세요."}');
INSERT INTO wf_component_property VALUES ('cdefccb8cf074e2f8ee1f741e80c483f','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('e90e4131007f470490a2ffaff402ba2f','label','{"position":"left","fontSize":"40","fontColor":"#222529","bold":false,"italic":false,"underline":false,"align":"center","text":"장애신고서"}');
INSERT INTO wf_component_property VALUES ('e90e4131007f470490a2ffaff402ba2f','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('e90e4131007f470490a2ffaff402ba2f','validation','{"required":false}');
INSERT INTO wf_component_property VALUES ('e90e4131007f470490a2ffaff402ba2f','element','{"columnWidth":"0","labelWidth":"10","text":"","fontSize":"12","align":"left","fontOption":"","fontOptionBold":"N","fontOptionItalic":"N","fontOptionUnderline":"N","fontColor":"#8B9094"}');
INSERT INTO wf_component_property VALUES ('fdd00ba8ff1240cdaeaf526a4df96db5','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"첨부파일"}');
INSERT INTO wf_component_property VALUES ('fdd00ba8ff1240cdaeaf526a4df96db5','validation','{"required":false}');
INSERT INTO wf_component_property VALUES ('fdd00ba8ff1240cdaeaf526a4df96db5','element','{"columnWidth":"10","align":"left"}');
INSERT INTO wf_component_property VALUES ('fdd00ba8ff1240cdaeaf526a4df96db5','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('fee68572b7bb4e04b1f27d0cdfe9ad7a','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"신청부서"}');
INSERT INTO wf_component_property VALUES ('fee68572b7bb4e04b1f27d0cdfe9ad7a','display','{"displayOrder":1,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('fee68572b7bb4e04b1f27d0cdfe9ad7a','validation','{"required":true}');
INSERT INTO wf_component_property VALUES ('fee68572b7bb4e04b1f27d0cdfe9ad7a','element','{"columnWidth":"9","defaultValueCustomCode":"40288a19736b46fb01736b89e46c0009|session|department"}');
/* 서비스데스크 - 장애신고 - 만족도 */
INSERT INTO wf_component_property VALUES ('a4257952286a4e6fae6faaeaae7279fd','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('a4257952286a4e6fae6faaeaae7279fd','validation','{"required":false}');
INSERT INTO wf_component_property VALUES ('a4257952286a4e6fae6faaeaae7279fd','element','{"columnWidth":"12","path":"file:///logo.png","width":"155","height":"26","align":"left"}');
INSERT INTO wf_component_property VALUES ('a4257952286a4e6fae6faaeaae7279fd','label','{"position":"hidden","fontSize":"14","fontColor":"rgba(141, 146, 153, 1)","bold":false,"italic":false,"underline":false,"align":"left","text":""}');
INSERT INTO wf_component_property VALUES ('a19693dff90a45b889df568bbe177bcd','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('a19693dff90a45b889df568bbe177bcd','validation','{"required":false}');
INSERT INTO wf_component_property VALUES ('a19693dff90a45b889df568bbe177bcd','element','{"columnWidth":"0","labelWidth":"10","text":"","fontSize":"12","align":"left","fontOption":"","fontOptionBold":"N","fontOptionItalic":"N","fontOptionUnderline":"N","fontColor":"#8B9094"}');
INSERT INTO wf_component_property VALUES ('a19693dff90a45b889df568bbe177bcd','label','{"position":"left","fontSize":"40","fontColor":"#222529","bold":false,"italic":false,"underline":false,"align":"center","text":"만족도 평가"}');
INSERT INTO wf_component_property VALUES ('d2d8114f31bb47608d1facc6f03e18ac','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('d2d8114f31bb47608d1facc6f03e18ac','validation','{"required":false}');
INSERT INTO wf_component_property VALUES ('d2d8114f31bb47608d1facc6f03e18ac','element','{"columnWidth":"12","thickness":"3","color":"#CFD5D9","type":"solid"}');
INSERT INTO wf_component_property VALUES ('d2d8114f31bb47608d1facc6f03e18ac','label','{"position":"hidden","fontSize":"14","fontColor":"rgba(141, 146, 153, 1)","bold":false,"italic":false,"underline":false,"align":"left","text":""}');
INSERT INTO wf_component_property VALUES ('5904fbf514df42cbb8d859799d8e7f3f','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('5904fbf514df42cbb8d859799d8e7f3f','validation','{"required":true}');
INSERT INTO wf_component_property VALUES ('5904fbf514df42cbb8d859799d8e7f3f','element','{"position":"left","columnWidth":"10","align":"horizontal","options":[{"name":"매우불만족","value":"1", "checked": false},{"name":"불만족","value":"2", "checked": false},{"name":"보통","value":"3", "checked": false},{"name":"만족","value":"4", "checked": false},{"name":"매우만족","value":"5", "checked": true}]}');
INSERT INTO wf_component_property VALUES ('5904fbf514df42cbb8d859799d8e7f3f','label','{"position":"left","fontSize":"14","fontColor":"rgba(141, 146, 153, 1)","bold":false,"italic":false,"underline":false,"align":"left","text":"만족도"}');
INSERT INTO wf_component_property VALUES ('207f974ae4654e7f8331526c504d0152','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('207f974ae4654e7f8331526c504d0152','validation','{"required":false,"minLength":"0","maxLength":"512"}');
INSERT INTO wf_component_property VALUES ('207f974ae4654e7f8331526c504d0152','element','{"columnWidth":"10","rows":"3","placeholder":"내용을 입력하세요."}');
INSERT INTO wf_component_property VALUES ('207f974ae4654e7f8331526c504d0152','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"평가의견"}');
/* 서비스데스크 - 서비스요청 */
INSERT INTO wf_component_property VALUES ('1f3a106094dd4e02b170f20396d526de','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"처리일시"}');
INSERT INTO wf_component_property VALUES ('1f3a106094dd4e02b170f20396d526de','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('1f3a106094dd4e02b170f20396d526de','validation','{"required":true,"minDateTime":"","maxDateTime":""}');
INSERT INTO wf_component_property VALUES ('1f3a106094dd4e02b170f20396d526de','element','{"columnWidth":"9","defaultValueRadio":"now"}');
INSERT INTO wf_component_property VALUES ('23e3399e82b644d09ab20b281860a4cb','validation','{"required":false,"minLength":"0","maxLength":"512"}');
INSERT INTO wf_component_property VALUES ('23e3399e82b644d09ab20b281860a4cb','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"승인 / 반려 의견"}');
INSERT INTO wf_component_property VALUES ('23e3399e82b644d09ab20b281860a4cb','element','{"columnWidth":"10","rows":"3","placeholder":""}');
INSERT INTO wf_component_property VALUES ('23e3399e82b644d09ab20b281860a4cb','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('3304af52ee0a4009aaadb3917d5de7f8','element','{"columnWidth":"9","defaultValueRadio":"now"}');
INSERT INTO wf_component_property VALUES ('3304af52ee0a4009aaadb3917d5de7f8','validation','{"required":true,"minDateTime":"","maxDateTime":""}');
INSERT INTO wf_component_property VALUES ('3304af52ee0a4009aaadb3917d5de7f8','display','{"displayOrder":1,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('3304af52ee0a4009aaadb3917d5de7f8','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"완료희망일시"}');
INSERT INTO wf_component_property VALUES ('3cdd30935a7e4da1b93e82e1ca65747f','element','{"placeholder":"","columnWidth":"9","defaultValueSelect":"select|email"}');
INSERT INTO wf_component_property VALUES ('3cdd30935a7e4da1b93e82e1ca65747f','validation','{"validationType":"email","required":false,"minLength":"0","maxLength":"100"}');
INSERT INTO wf_component_property VALUES ('3cdd30935a7e4da1b93e82e1ca65747f','display','{"displayOrder":1,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('3cdd30935a7e4da1b93e82e1ca65747f','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"메일주소"}');
INSERT INTO wf_component_property VALUES ('413ef0d9ef51415c9772baf19911b940','validation','{"validationType":"phone","required":false,"minLength":"0","maxLength":"100"}');
INSERT INTO wf_component_property VALUES ('413ef0d9ef51415c9772baf19911b940','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"전화번호"}');
INSERT INTO wf_component_property VALUES ('413ef0d9ef51415c9772baf19911b940','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('413ef0d9ef51415c9772baf19911b940','element','{"placeholder":"","columnWidth":"9","defaultValueSelect":"select|officeNumber"}');
INSERT INTO wf_component_property VALUES ('4370df5c2ba44544a3444ce38240e61d','validation','{"required":true,"minLength":"0","maxLength":"512"}');
INSERT INTO wf_component_property VALUES ('4370df5c2ba44544a3444ce38240e61d','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"내용"}');
INSERT INTO wf_component_property VALUES ('4370df5c2ba44544a3444ce38240e61d','element','{"columnWidth":"10","rows":"3","placeholder":"내용을 입력하세요."}');
INSERT INTO wf_component_property VALUES ('4370df5c2ba44544a3444ce38240e61d','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('5d0b0faef24e429ba271e1bb2175d2ff','label','{"position":"hidden","fontSize":"14","fontColor":"rgba(141, 146, 153, 1)","bold":false,"italic":false,"underline":false,"align":"left","text":""}');
INSERT INTO wf_component_property VALUES ('5d0b0faef24e429ba271e1bb2175d2ff','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('5d0b0faef24e429ba271e1bb2175d2ff','validation','{"required":false}');
INSERT INTO wf_component_property VALUES ('5d0b0faef24e429ba271e1bb2175d2ff','element','{"columnWidth":"12","path":"file:///logo.png","width":"155","height":"26","align":"left"}');
INSERT INTO wf_component_property VALUES ('68098b156d354c069bfaf660b5b03f9d','element','{"columnWidth":"10","rows":"3","placeholder":"내용을 입력하세요"}');
INSERT INTO wf_component_property VALUES ('68098b156d354c069bfaf660b5b03f9d','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('68098b156d354c069bfaf660b5b03f9d','validation','{"required":false,"minLength":"0","maxLength":"512"}');
INSERT INTO wf_component_property VALUES ('68098b156d354c069bfaf660b5b03f9d','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"비고"}');
INSERT INTO wf_component_property VALUES ('76f13c0db2894fb5b8dcabc6e3e7a1fc','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('76f13c0db2894fb5b8dcabc6e3e7a1fc','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"신청일시"}');
INSERT INTO wf_component_property VALUES ('76f13c0db2894fb5b8dcabc6e3e7a1fc','element','{"columnWidth":"9","defaultValueRadio":"now"}');
INSERT INTO wf_component_property VALUES ('76f13c0db2894fb5b8dcabc6e3e7a1fc','validation','{"required":true,"minDateTime":"","maxDateTime":""}');
INSERT INTO wf_component_property VALUES ('7811e5a93319403698481a00386f8b5e','validation','{"required":false}');
INSERT INTO wf_component_property VALUES ('7811e5a93319403698481a00386f8b5e','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('7811e5a93319403698481a00386f8b5e','element','{"columnWidth":"0","labelWidth":"10","text":"","fontSize":"12","align":"left","fontOption":"","fontOptionBold":"N","fontOptionItalic":"N","fontOptionUnderline":"N","fontColor":"#8B9094"}');
INSERT INTO wf_component_property VALUES ('7811e5a93319403698481a00386f8b5e','label','{"position":"left","fontSize":"40","fontColor":"#222529","bold":false,"italic":false,"underline":false,"align":"center","text":"서비스요청서"}');
INSERT INTO wf_component_property VALUES ('7afa8f35f45f412eb98db57dfd07b6b6','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('7afa8f35f45f412eb98db57dfd07b6b6','validation','{"required":false,"minLength":"0","maxLength":"512"}');
INSERT INTO wf_component_property VALUES ('7afa8f35f45f412eb98db57dfd07b6b6','element','{"columnWidth":"10","rows":"3","placeholder":"내용을 입력하세요."}');
INSERT INTO wf_component_property VALUES ('7afa8f35f45f412eb98db57dfd07b6b6','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"접수의견"}');
INSERT INTO wf_component_property VALUES ('7c77ea3ec86c41ecb6c0b4dc1ddcd9f9','element','{"columnWidth":"10","align":"left"}');
INSERT INTO wf_component_property VALUES ('7c77ea3ec86c41ecb6c0b4dc1ddcd9f9','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('7c77ea3ec86c41ecb6c0b4dc1ddcd9f9','validation','{"required":false}');
INSERT INTO wf_component_property VALUES ('7c77ea3ec86c41ecb6c0b4dc1ddcd9f9','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"첨부파일"}');
INSERT INTO wf_component_property VALUES ('7f1e91223181424b89172127908058f5','label','{"position":"top","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"관련 CI"}');
INSERT INTO wf_component_property VALUES ('7f1e91223181424b89172127908058f5','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('7f1e91223181424b89172127908058f5','validation','{"required":false}');
INSERT INTO wf_component_property VALUES ('7f1e91223181424b89172127908058f5','element','{"columnWidth":"12","isEditable":false}');
INSERT INTO wf_component_property VALUES ('8929fddece384122a219fb7cfbb07312','validation','{"required":true}');
INSERT INTO wf_component_property VALUES ('8929fddece384122a219fb7cfbb07312','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"신청부서"}');
INSERT INTO wf_component_property VALUES ('8929fddece384122a219fb7cfbb07312','element','{"columnWidth":"9","defaultValueCustomCode":"40288a19736b46fb01736b89e46c0009|session|department"}');
INSERT INTO wf_component_property VALUES ('8929fddece384122a219fb7cfbb07312','display','{"displayOrder":1,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('8a4339add3c54b3aba9e4864c16c558c','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('8a4339add3c54b3aba9e4864c16c558c','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"승인자"}');
INSERT INTO wf_component_property VALUES ('8a4339add3c54b3aba9e4864c16c558c','element','{"columnWidth":"10","defaultValueCustomCode":"40288a19736b46fb01736b89e46c0008|session|userName"}');
INSERT INTO wf_component_property VALUES ('8a4339add3c54b3aba9e4864c16c558c','validation','{"required":true}');
INSERT INTO wf_component_property VALUES ('8fe57acaefef4533b3897867cedb6579','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('8fe57acaefef4533b3897867cedb6579','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"제목"}');
INSERT INTO wf_component_property VALUES ('8fe57acaefef4533b3897867cedb6579','element','{"placeholder":"제목을 입력하세요.","columnWidth":"10","defaultValueSelect":"input|"}');
INSERT INTO wf_component_property VALUES ('8fe57acaefef4533b3897867cedb6579','validation','{"validationType":"none","required":true,"minLength":"0","maxLength":"100"}');
INSERT INTO wf_component_property VALUES ('93d2c6c791ac4824bf94add769417fe4','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('93d2c6c791ac4824bf94add769417fe4','validation','{"validationType":"none","required":true,"minLength":"0","maxLength":"100"}');
INSERT INTO wf_component_property VALUES ('93d2c6c791ac4824bf94add769417fe4','element','{"placeholder":"","columnWidth":"9","defaultValueSelect":"select|userName"}');
INSERT INTO wf_component_property VALUES ('93d2c6c791ac4824bf94add769417fe4','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"신청자"}');
INSERT INTO wf_component_property VALUES ('a6d20858d6e84b85b1e75fc28ec36696','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('a6d20858d6e84b85b1e75fc28ec36696','validation','{"required":true,"minDateTime":"","maxDateTime":""}');
INSERT INTO wf_component_property VALUES ('a6d20858d6e84b85b1e75fc28ec36696','element','{"columnWidth":"10","defaultValueRadio":"now"}');
INSERT INTO wf_component_property VALUES ('a6d20858d6e84b85b1e75fc28ec36696','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"접수일시"}');
INSERT INTO wf_component_property VALUES ('afa6d40a2b304474ab7f424670587959','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('afa6d40a2b304474ab7f424670587959','validation','{"required":true}');
INSERT INTO wf_component_property VALUES ('afa6d40a2b304474ab7f424670587959','element','{"columnWidth":"10","defaultValueCustomCode":"40288a19736b46fb01736b89e46c0008|session|userName"}');
INSERT INTO wf_component_property VALUES ('afa6d40a2b304474ab7f424670587959','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"처리자"}');
INSERT INTO wf_component_property VALUES ('ba5482f4fcfc45d7b20a04328670f11b','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"승인 / 반려 일시"}');
INSERT INTO wf_component_property VALUES ('ba5482f4fcfc45d7b20a04328670f11b','element','{"columnWidth":"10","defaultValueRadio":"now"}');
INSERT INTO wf_component_property VALUES ('ba5482f4fcfc45d7b20a04328670f11b','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('ba5482f4fcfc45d7b20a04328670f11b','validation','{"required":true,"minDateTime":"","maxDateTime":""}');
INSERT INTO wf_component_property VALUES ('c2972b99571a448ebdb8b2f981412060','validation','{"required":false}');
INSERT INTO wf_component_property VALUES ('c2972b99571a448ebdb8b2f981412060','element','{"columnWidth":"12","thickness":"3","color":"#CFD5D9","type":"solid"}');
INSERT INTO wf_component_property VALUES ('c2972b99571a448ebdb8b2f981412060','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('c2972b99571a448ebdb8b2f981412060','label','{"position":"hidden","fontSize":"14","fontColor":"#CFD5D9","bold":false,"italic":false,"underline":false,"align":"left","text":""}');
INSERT INTO wf_component_property VALUES ('c4591d870dab40e3a18ca6f41250b200','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('c4591d870dab40e3a18ca6f41250b200','validation','{"required":true}');
INSERT INTO wf_component_property VALUES ('c4591d870dab40e3a18ca6f41250b200','element','{"columnWidth":"10","defaultValueCustomCode":"40288a19736b46fb01736b89e46c0012|code|form.template.serviceDesk.request.category.service|service"}');
INSERT INTO wf_component_property VALUES ('c4591d870dab40e3a18ca6f41250b200','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"요청구분"}');
INSERT INTO wf_component_property VALUES ('d80c69ade3fc48f1bd78e1b4b02397d7','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('d80c69ade3fc48f1bd78e1b4b02397d7','validation','{"required":true,"minLength":"0","maxLength":"512"}');
INSERT INTO wf_component_property VALUES ('d80c69ade3fc48f1bd78e1b4b02397d7','element','{"columnWidth":"10","rows":"3","placeholder":"내용을 입력하세요."}');
INSERT INTO wf_component_property VALUES ('d80c69ade3fc48f1bd78e1b4b02397d7','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"처리결과"}');
INSERT INTO wf_component_property VALUES ('ea1841546d6648f49bafc2ebbe11c32a','validation','{"required":false}');
INSERT INTO wf_component_property VALUES ('ea1841546d6648f49bafc2ebbe11c32a','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"첨부파일"}');
INSERT INTO wf_component_property VALUES ('ea1841546d6648f49bafc2ebbe11c32a','element','{"columnWidth":"10","align":"left"}');
INSERT INTO wf_component_property VALUES ('ea1841546d6648f49bafc2ebbe11c32a','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('ebcb50c94a9f494c8afd28d95c4aa79a','label','{"position":"top","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"관련 CI"}');
INSERT INTO wf_component_property VALUES ('ebcb50c94a9f494c8afd28d95c4aa79a','validation','{"required":false}');
INSERT INTO wf_component_property VALUES ('ebcb50c94a9f494c8afd28d95c4aa79a','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('ebcb50c94a9f494c8afd28d95c4aa79a','element','{"columnWidth":"12","isEditable":false}');
INSERT INTO wf_component_property VALUES ('ee30c91397e242b5a1ddae91437b50ea','element','{"columnWidth":"9","defaultValueCustomCode":"40288a19736b46fb01736b89e46c0009|session|department"}');
INSERT INTO wf_component_property VALUES ('ee30c91397e242b5a1ddae91437b50ea','validation','{"required":true}');
INSERT INTO wf_component_property VALUES ('ee30c91397e242b5a1ddae91437b50ea','display','{"displayOrder":1,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('ee30c91397e242b5a1ddae91437b50ea','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"접수부서"}');
INSERT INTO wf_component_property VALUES ('f1867a396519411c848f65bfbda28d29','validation','{"validationType":"none","required":true,"minLength":"0","maxLength":"100"}');
INSERT INTO wf_component_property VALUES ('f1867a396519411c848f65bfbda28d29','element','{"placeholder":"","columnWidth":"9","defaultValueSelect":"select|userName"}');
INSERT INTO wf_component_property VALUES ('f1867a396519411c848f65bfbda28d29','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"접수자"}');
INSERT INTO wf_component_property VALUES ('f1867a396519411c848f65bfbda28d29','display','{"displayOrder":0,"columnWidth":"12"}');
/* 서비스데스크 - 서비스요청 - 만족도 */
INSERT INTO wf_component_property VALUES ('c3c6191d9ee748bebc5745bb5be27452','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('c3c6191d9ee748bebc5745bb5be27452','validation','{"required":false}');
INSERT INTO wf_component_property VALUES ('c3c6191d9ee748bebc5745bb5be27452','element','{"columnWidth":"12","path":"file:///logo.png","width":"155","height":"26","align":"left"}');
INSERT INTO wf_component_property VALUES ('c3c6191d9ee748bebc5745bb5be27452','label','{"position":"hidden","fontSize":"14","fontColor":"rgba(141, 146, 153, 1)","bold":false,"italic":false,"underline":false,"align":"left","text":""}');
INSERT INTO wf_component_property VALUES ('90eedeed14494ea4b17d98bd4e8d0a69','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('90eedeed14494ea4b17d98bd4e8d0a69','validation','{"required":false}');
INSERT INTO wf_component_property VALUES ('90eedeed14494ea4b17d98bd4e8d0a69','element','{"columnWidth":"0","labelWidth":"10","text":"","fontSize":"12","align":"left","fontOption":"","fontOptionBold":"N","fontOptionItalic":"N","fontOptionUnderline":"N","fontColor":"#8B9094"}');
INSERT INTO wf_component_property VALUES ('90eedeed14494ea4b17d98bd4e8d0a69','label','{"position":"left","fontSize":"40","fontColor":"#222529","bold":false,"italic":false,"underline":false,"align":"center","text":"만족도 평가"}');
INSERT INTO wf_component_property VALUES ('8f33d3a207b04bb886a87e1e01859500','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('8f33d3a207b04bb886a87e1e01859500','validation','{"required":false}');
INSERT INTO wf_component_property VALUES ('8f33d3a207b04bb886a87e1e01859500','element','{"columnWidth":"12","thickness":"3","color":"#CFD5D9","type":"solid"}');
INSERT INTO wf_component_property VALUES ('8f33d3a207b04bb886a87e1e01859500','label','{"position":"hidden","fontSize":"14","fontColor":"rgba(141, 146, 153, 1)","bold":false,"italic":false,"underline":false,"align":"left","text":""}');
INSERT INTO wf_component_property VALUES ('0986886e23a044659c7bb280347064d2','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('0986886e23a044659c7bb280347064d2','validation','{"required":true}');
INSERT INTO wf_component_property VALUES ('0986886e23a044659c7bb280347064d2','element','{"position":"left","columnWidth":"10","align":"horizontal","options":[{"name":"매우불만족","value":"1", "checked": false},{"name":"불만족","value":"2", "checked": false},{"name":"보통","value":"3", "checked": false},{"name":"만족","value":"4", "checked": false},{"name":"매우만족","value":"5", "checked": true}]}');
INSERT INTO wf_component_property VALUES ('0986886e23a044659c7bb280347064d2','label','{"position":"left","fontSize":"14","fontColor":"rgba(141, 146, 153, 1)","bold":false,"italic":false,"underline":false,"align":"left","text":"만족도"}');
INSERT INTO wf_component_property VALUES ('5e203c7bace44cb58e1c38cee9372404','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('5e203c7bace44cb58e1c38cee9372404','validation','{"required":false,"minLength":"0","maxLength":"512"}');
INSERT INTO wf_component_property VALUES ('5e203c7bace44cb58e1c38cee9372404','element','{"columnWidth":"10","rows":"3","placeholder":"내용을 입력하세요."}');
INSERT INTO wf_component_property VALUES ('5e203c7bace44cb58e1c38cee9372404','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"평가의견"}');
/* 인프라변경관리 */
INSERT INTO wf_component_property VALUES ('b5e06a4da8914e08b63a589fe16578db', 'display', '{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('b5e06a4da8914e08b63a589fe16578db', 'validation', '{"required":false}');
INSERT INTO wf_component_property VALUES ('b5e06a4da8914e08b63a589fe16578db', 'element', '{"columnWidth":"12","path":"file:///logo.png","width":"155","height":"26","align":"left"}');
INSERT INTO wf_component_property VALUES ('b5e06a4da8914e08b63a589fe16578db', 'label', '{"position":"hidden","fontSize":"14","fontColor":"rgba(141, 146, 153, 1)","bold":false,"italic":false,"underline":false,"align":"left","text":""}');
INSERT INTO wf_component_property VALUES ('44026fa563f649779f65f52dc0bfd0d6', 'display', '{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('44026fa563f649779f65f52dc0bfd0d6', 'validation', '{"required":false}');
INSERT INTO wf_component_property VALUES ('44026fa563f649779f65f52dc0bfd0d6', 'element', '{"columnWidth":"0","labelWidth":"10","text":"","fontSize":"12","align":"left","fontOption":"","fontOptionBold":"N","fontOptionItalic":"N","fontOptionUnderline":"N","fontColor":"#8B9094"}');
INSERT INTO wf_component_property VALUES ('44026fa563f649779f65f52dc0bfd0d6', 'label', '{"position":"left","fontSize":"40","fontColor":"#222529","bold":false,"italic":false,"underline":false,"align":"center","text":"인프라변경관리서"}');
INSERT INTO wf_component_property VALUES ('a70e12f4514b4377bc9a94eb0f80deeb', 'display', '{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('a70e12f4514b4377bc9a94eb0f80deeb', 'validation', '{"required":false}');
INSERT INTO wf_component_property VALUES ('a70e12f4514b4377bc9a94eb0f80deeb', 'element', '{"columnWidth":"12","thickness":"3","color":"#CFD5D9","type":"solid"}');
INSERT INTO wf_component_property VALUES ('a70e12f4514b4377bc9a94eb0f80deeb', 'label', '{"position":"hidden","fontSize":"14","fontColor":"#CFD5D9","bold":false,"italic":false,"underline":false,"align":"left","text":""}');
INSERT INTO wf_component_property VALUES ('6df0771bb8a94deda97d0d6ba42ef01e', 'display', '{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('6df0771bb8a94deda97d0d6ba42ef01e', 'validation', '{"validationType":"none","required":true,"minLength":"0","maxLength":"100"}');
INSERT INTO wf_component_property VALUES ('6df0771bb8a94deda97d0d6ba42ef01e', 'element', '{"placeholder":"","columnWidth":"9","defaultValueSelect":"select|userName"}');
INSERT INTO wf_component_property VALUES ('6df0771bb8a94deda97d0d6ba42ef01e', 'label', '{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"신청자"}');
INSERT INTO wf_component_property VALUES ('1a4024f7913e426383187d6bcc7962af', 'display', '{"displayOrder":1,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('1a4024f7913e426383187d6bcc7962af', 'validation', '{"required":true}');
INSERT INTO wf_component_property VALUES ('1a4024f7913e426383187d6bcc7962af', 'element', '{"columnWidth":"9","defaultValueCustomCode":"40288a19736b46fb01736b89e46c0009|session|department"}');
INSERT INTO wf_component_property VALUES ('1a4024f7913e426383187d6bcc7962af', 'label', '{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"신청부서"}');
INSERT INTO wf_component_property VALUES ('11ed46ce208341fd842be00ced0e0381', 'display', '{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('11ed46ce208341fd842be00ced0e0381', 'validation', '{"validationType":"phone","required":false,"minLength":"0","maxLength":"100"}');
INSERT INTO wf_component_property VALUES ('11ed46ce208341fd842be00ced0e0381', 'element', '{"placeholder":"","columnWidth":"9","defaultValueSelect":"select|officeNumber"}');
INSERT INTO wf_component_property VALUES ('11ed46ce208341fd842be00ced0e0381', 'label', '{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"전화번호"}');
INSERT INTO wf_component_property VALUES ('a50504ee81204910a1e13782987b4f89', 'display', '{"displayOrder":1,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('a50504ee81204910a1e13782987b4f89', 'validation', '{"validationType":"email","required":false,"minLength":"0","maxLength":"100"}');
INSERT INTO wf_component_property VALUES ('a50504ee81204910a1e13782987b4f89', 'element', '{"placeholder":"","columnWidth":"9","defaultValueSelect":"select|email"}');
INSERT INTO wf_component_property VALUES ('a50504ee81204910a1e13782987b4f89', 'label', '{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"메일주소"}');
INSERT INTO wf_component_property VALUES ('610a36dd4d28448f9ca09623962d64e5', 'display', '{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('610a36dd4d28448f9ca09623962d64e5', 'validation', '{"required":true,"minDateTime":"","maxDateTime":""}');
INSERT INTO wf_component_property VALUES ('610a36dd4d28448f9ca09623962d64e5', 'element', '{"columnWidth":"9","defaultValueRadio":"now"}');
INSERT INTO wf_component_property VALUES ('610a36dd4d28448f9ca09623962d64e5', 'label', '{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"신청일시"}');
INSERT INTO wf_component_property VALUES ('9bbb34c9d2fe40ba802ee9f674210fe5', 'display', '{"displayOrder":1,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('9bbb34c9d2fe40ba802ee9f674210fe5', 'validation', '{"required":true,"minDateTime":"","maxDateTime":""}');
INSERT INTO wf_component_property VALUES ('9bbb34c9d2fe40ba802ee9f674210fe5', 'element', '{"columnWidth":"9","defaultValueRadio":"now"}');
INSERT INTO wf_component_property VALUES ('9bbb34c9d2fe40ba802ee9f674210fe5', 'label', '{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"완료희망일시"}');
INSERT INTO wf_component_property VALUES ('c520f41591ca4c18bc89b08e5b58a78b', 'display', '{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('c520f41591ca4c18bc89b08e5b58a78b', 'validation', '{"validationType":"none","required":true,"minLength":"0","maxLength":"100"}');
INSERT INTO wf_component_property VALUES ('c520f41591ca4c18bc89b08e5b58a78b', 'element', '{"placeholder":"제목을 입력하세요.","columnWidth":"10","defaultValueSelect":"input|"}');
INSERT INTO wf_component_property VALUES ('c520f41591ca4c18bc89b08e5b58a78b', 'label', '{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"제목"}');
INSERT INTO wf_component_property VALUES ('5aa56cbcd2ab48bbbcffd0ba53af499c', 'display', '{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('5aa56cbcd2ab48bbbcffd0ba53af499c', 'validation', '{"required":true}');
INSERT INTO wf_component_property VALUES ('5aa56cbcd2ab48bbbcffd0ba53af499c', 'element', '{"columnWidth":"10","defaultValueCustomCode":"40288a19736b46fb01736b89e46c0012|code|form.template.serviceDesk.request.category.service|service"}');
INSERT INTO wf_component_property VALUES ('5aa56cbcd2ab48bbbcffd0ba53af499c', 'label', '{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"요청구분"}');
INSERT INTO wf_component_property VALUES ('654a2e1cbdf14f39a883ff4ab168046c', 'display', '{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('654a2e1cbdf14f39a883ff4ab168046c', 'validation', '{"required":true,"minLength":"0","maxLength":"512"}');
INSERT INTO wf_component_property VALUES ('654a2e1cbdf14f39a883ff4ab168046c', 'element', '{"columnWidth":"10","rows":"3","placeholder":"내용을 입력하세요."}');
INSERT INTO wf_component_property VALUES ('654a2e1cbdf14f39a883ff4ab168046c', 'label', '{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"내용"}');
INSERT INTO wf_component_property VALUES ('41ff46f9f19d4e92853118fa1e914877', 'display', '{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('41ff46f9f19d4e92853118fa1e914877', 'validation', '{"required":false}');
INSERT INTO wf_component_property VALUES ('41ff46f9f19d4e92853118fa1e914877', 'element', '{"columnWidth":"10","align":"left"}');
INSERT INTO wf_component_property VALUES ('41ff46f9f19d4e92853118fa1e914877', 'label', '{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"첨부파일"}');
INSERT INTO wf_component_property VALUES ('2c260851462b40ea82949561ed0f2b68', 'display', '{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('2c260851462b40ea82949561ed0f2b68', 'validation', '{"required":true,"minDateTime":"","maxDateTime":""}');
INSERT INTO wf_component_property VALUES ('2c260851462b40ea82949561ed0f2b68', 'element', '{"columnWidth":"10","defaultValueRadio":"now"}');
INSERT INTO wf_component_property VALUES ('2c260851462b40ea82949561ed0f2b68', 'label', '{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"접수일시"}');
INSERT INTO wf_component_property VALUES ('9e79bf1880094800b3b3d68044f40538', 'display', '{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('9e79bf1880094800b3b3d68044f40538', 'validation', '{"validationType":"none","required":true,"minLength":"0","maxLength":"100"}');
INSERT INTO wf_component_property VALUES ('9e79bf1880094800b3b3d68044f40538', 'element', '{"placeholder":"","columnWidth":"9","defaultValueSelect":"select|userName"}');
INSERT INTO wf_component_property VALUES ('9e79bf1880094800b3b3d68044f40538', 'label', '{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"접수자"}');
INSERT INTO wf_component_property VALUES ('59ffe940a3464a9ca3683224f7038c41', 'display', '{"displayOrder":1,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('59ffe940a3464a9ca3683224f7038c41', 'validation', '{"required":true}');
INSERT INTO wf_component_property VALUES ('59ffe940a3464a9ca3683224f7038c41', 'element', '{"columnWidth":"9","defaultValueCustomCode":"40288a19736b46fb01736b89e46c0009|session|department"}');
INSERT INTO wf_component_property VALUES ('59ffe940a3464a9ca3683224f7038c41', 'label', '{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"접수부서"}');
INSERT INTO wf_component_property VALUES ('88660c980418450baf87a6d7ef510626', 'display', '{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('88660c980418450baf87a6d7ef510626', 'validation', '{"required":true}');
INSERT INTO wf_component_property VALUES ('88660c980418450baf87a6d7ef510626', 'element', '{"columnWidth":"9","defaultValueCustomCode":"40288a19736b46fb01736b89e46c0008|none|"}');
INSERT INTO wf_component_property VALUES ('88660c980418450baf87a6d7ef510626', 'label', '{"position":"left","fontSize":"14","fontColor":"rgba(141, 146, 153, 1)","bold":false,"italic":false,"underline":false,"align":"left","text":"처리자"}');
INSERT INTO wf_component_property VALUES ('f12492917ac64f418c37cfa98c4ab02d', 'display', '{"displayOrder":1,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('f12492917ac64f418c37cfa98c4ab02d', 'validation', '{"required":true}');
INSERT INTO wf_component_property VALUES ('f12492917ac64f418c37cfa98c4ab02d', 'element', '{"columnWidth":"9","defaultValueCustomCode":"40288a19736b46fb01736b89e46c0009|none|"}');
INSERT INTO wf_component_property VALUES ('f12492917ac64f418c37cfa98c4ab02d', 'label', '{"position":"left","fontSize":"14","fontColor":"rgba(141, 146, 153, 1)","bold":false,"italic":false,"underline":false,"align":"left","text":"처리부서"}');
INSERT INTO wf_component_property VALUES ('24f2a648a5ba458d9224a61c0cb1cdb0', 'display', '{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('24f2a648a5ba458d9224a61c0cb1cdb0', 'validation', '{"required":false}');
INSERT INTO wf_component_property VALUES ('24f2a648a5ba458d9224a61c0cb1cdb0', 'element', '{"columnWidth":"9","options":[{"name":"낮음","value":"1","checked":false},{"name":"중간","value":"2","checked":false},{"name":"높음","value":"3","checked":false},{"name":"아주높음","value":"4","checked":false}]}');
INSERT INTO wf_component_property VALUES ('24f2a648a5ba458d9224a61c0cb1cdb0', 'label', '{"position":"left","fontSize":"14","fontColor":"rgba(141, 146, 153, 1)","bold":false,"italic":false,"underline":false,"align":"left","text":"영향도"}');
INSERT INTO wf_component_property VALUES ('15cd0e737e9c41c5a27132cfa18f370a', 'display', '{"displayOrder":1,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('15cd0e737e9c41c5a27132cfa18f370a', 'validation', '{"required":false}');
INSERT INTO wf_component_property VALUES ('15cd0e737e9c41c5a27132cfa18f370a', 'element', '{"columnWidth":"9","options":[{"name":"1일 이내 처리","value":"1","checked":false},{"name":"3일 이내 처리","value":"3","checked":false},{"name":"7일 이내 처리","value":"7","checked":false},{"name":"7일 이상","value":"8","checked":false}]}');
INSERT INTO wf_component_property VALUES ('15cd0e737e9c41c5a27132cfa18f370a', 'label', '{"position":"left","fontSize":"14","fontColor":"rgba(141, 146, 153, 1)","bold":false,"italic":false,"underline":false,"align":"left","text":"긴급도"}');
INSERT INTO wf_component_property VALUES ('bb38d2e37255417b8f61e0947e224974', 'display', '{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('bb38d2e37255417b8f61e0947e224974', 'validation', '{"required":false,"minLength":"0","maxLength":"512"}');
INSERT INTO wf_component_property VALUES ('bb38d2e37255417b8f61e0947e224974', 'element', '{"columnWidth":"10","rows":"3","placeholder":"내용을 입력하세요."}');
INSERT INTO wf_component_property VALUES ('bb38d2e37255417b8f61e0947e224974', 'label', '{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"접수의견"}');
INSERT INTO wf_component_property VALUES ('c7e485828f2b4c85a7cba5d6b3646b16', 'display', '{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('c7e485828f2b4c85a7cba5d6b3646b16', 'validation', '{"required":false,"minDateTime":"","maxDateTime":""}');
INSERT INTO wf_component_property VALUES ('c7e485828f2b4c85a7cba5d6b3646b16', 'element', '{"columnWidth":"10","defaultValueRadio":"none"}');
INSERT INTO wf_component_property VALUES ('c7e485828f2b4c85a7cba5d6b3646b16', 'label', '{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"회의일시"}');
INSERT INTO wf_component_property VALUES ('6deb6fcad6124c9a907fc4850285e342', 'display', '{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('6deb6fcad6124c9a907fc4850285e342', 'validation', '{"validationType":"none","required":false,"minLength":"0","maxLength":"100"}');
INSERT INTO wf_component_property VALUES ('6deb6fcad6124c9a907fc4850285e342', 'element', '{"placeholder":"","columnWidth":"10","defaultValueSelect":"input|"}');
INSERT INTO wf_component_property VALUES ('6deb6fcad6124c9a907fc4850285e342', 'label', '{"position":"left","fontSize":"14","fontColor":"rgba(141, 146, 153, 1)","bold":false,"italic":false,"underline":false,"align":"left","text":"회의장소"}');
INSERT INTO wf_component_property VALUES ('1d2ea0820d514512a1e3837e04fbc84b', 'display', '{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('1d2ea0820d514512a1e3837e04fbc84b', 'validation', '{"validationType":"none","required":false,"minLength":"0","maxLength":"100"}');
INSERT INTO wf_component_property VALUES ('1d2ea0820d514512a1e3837e04fbc84b', 'element', '{"placeholder":"","columnWidth":"10","defaultValueSelect":"input|"}');
INSERT INTO wf_component_property VALUES ('1d2ea0820d514512a1e3837e04fbc84b', 'label', '{"position":"left","fontSize":"14","fontColor":"rgba(141, 146, 153, 1)","bold":false,"italic":false,"underline":false,"align":"left","text":"주제"}');
INSERT INTO wf_component_property VALUES ('2cb51de14f824f62bf9d5f634d276fdc', 'display', '{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('2cb51de14f824f62bf9d5f634d276fdc', 'validation', '{"required":false,"minLength":"0","maxLength":"512"}');
INSERT INTO wf_component_property VALUES ('2cb51de14f824f62bf9d5f634d276fdc', 'element', '{"columnWidth":"10","rows":"3","placeholder":""}');
INSERT INTO wf_component_property VALUES ('2cb51de14f824f62bf9d5f634d276fdc', 'label', '{"position":"left","fontSize":"14","fontColor":"rgba(141, 146, 153, 1)","bold":false,"italic":false,"underline":false,"align":"left","text":"협의사항"}');
INSERT INTO wf_component_property VALUES ('c27d14120e1044a5b70fe4372ed78c2e', 'display', '{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('c27d14120e1044a5b70fe4372ed78c2e', 'validation', '{"required":false,"minLength":"0","maxLength":"512"}');
INSERT INTO wf_component_property VALUES ('c27d14120e1044a5b70fe4372ed78c2e', 'element', '{"columnWidth":"10","rows":"3","placeholder":""}');
INSERT INTO wf_component_property VALUES ('c27d14120e1044a5b70fe4372ed78c2e', 'label', '{"position":"left","fontSize":"14","fontColor":"rgba(141, 146, 153, 1)","bold":false,"italic":false,"underline":false,"align":"left","text":"회의결과"}');
INSERT INTO wf_component_property VALUES ('3d5bb59a5b264430b2a8d1b19f6741ae', 'display', '{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('3d5bb59a5b264430b2a8d1b19f6741ae', 'validation', '{"required":true,"minDateTime":"","maxDateTime":""}');
INSERT INTO wf_component_property VALUES ('3d5bb59a5b264430b2a8d1b19f6741ae', 'element', '{"columnWidth":"10","defaultValueRadio":"now"}');
INSERT INTO wf_component_property VALUES ('3d5bb59a5b264430b2a8d1b19f6741ae', 'label', '{"position":"left","fontSize":"14","fontColor":"rgba(141, 146, 153, 1)","bold":false,"italic":false,"underline":false,"align":"left","text":"작성일시"}');
INSERT INTO wf_component_property VALUES ('e6d73057dd5a4b2a8cf01e15d3793eca', 'display', '{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('e6d73057dd5a4b2a8cf01e15d3793eca', 'validation', '{"required":true,"minDateTime":"","maxDateTime":""}');
INSERT INTO wf_component_property VALUES ('e6d73057dd5a4b2a8cf01e15d3793eca', 'element', '{"columnWidth":"9","defaultValueRadio":"now"}');
INSERT INTO wf_component_property VALUES ('e6d73057dd5a4b2a8cf01e15d3793eca', 'label', '{"position":"left","fontSize":"14","fontColor":"rgba(141, 146, 153, 1)","bold":false,"italic":false,"underline":false,"align":"left","text":"변경 예정 시작일시"}');
INSERT INTO wf_component_property VALUES ('afe2bec3307442f0be22af203e233f2f', 'display', '{"displayOrder":1,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('afe2bec3307442f0be22af203e233f2f', 'validation', '{"required":true,"minDateTime":"","maxDateTime":""}');
INSERT INTO wf_component_property VALUES ('afe2bec3307442f0be22af203e233f2f', 'element', '{"columnWidth":"9","defaultValueRadio":"dateTime|1|"}');
INSERT INTO wf_component_property VALUES ('afe2bec3307442f0be22af203e233f2f', 'label', '{"position":"left","fontSize":"14","fontColor":"rgba(141, 146, 153, 1)","bold":false,"italic":false,"underline":false,"align":"left","text":"변경 예정 종료일시"}');
INSERT INTO wf_component_property VALUES ('f5595e73f0654239aab9e66f71c528cc', 'display', '{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('f5595e73f0654239aab9e66f71c528cc', 'validation', '{"required":true}');
INSERT INTO wf_component_property VALUES ('f5595e73f0654239aab9e66f71c528cc', 'element', '{"position":"left","columnWidth":10,"align":"horizontal","options":[{"name":"중단","value":"stop"},{"name":"미중단","value":"online"}]}');
INSERT INTO wf_component_property VALUES ('f5595e73f0654239aab9e66f71c528cc', 'label', '{"position":"left","fontSize":"14","fontColor":"rgba(141, 146, 153, 1)","bold":false,"italic":false,"underline":false,"align":"left","text":"서비스 중단"}');
INSERT INTO wf_component_property VALUES ('b16f8424688f4e608d1f7b71f1aaf4b5', 'display', '{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('b16f8424688f4e608d1f7b71f1aaf4b5', 'validation', '{"required":false,"minLength":"0","maxLength":"512"}');
INSERT INTO wf_component_property VALUES ('b16f8424688f4e608d1f7b71f1aaf4b5', 'element', '{"columnWidth":"10","rows":"3","placeholder":""}');
INSERT INTO wf_component_property VALUES ('b16f8424688f4e608d1f7b71f1aaf4b5', 'label', '{"position":"left","fontSize":"14","fontColor":"rgba(141, 146, 153, 1)","bold":false,"italic":false,"underline":false,"align":"left","text":"변경작업계획"}');
INSERT INTO wf_component_property VALUES ('2692ffba732d496498bd2f53f35016f9', 'display', '{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('2692ffba732d496498bd2f53f35016f9', 'validation', '{"required":false,"minLength":"0","maxLength":"512"}');
INSERT INTO wf_component_property VALUES ('2692ffba732d496498bd2f53f35016f9', 'element', '{"columnWidth":"10","rows":"3","placeholder":""}');
INSERT INTO wf_component_property VALUES ('2692ffba732d496498bd2f53f35016f9', 'label', '{"position":"left","fontSize":"14","fontColor":"rgba(141, 146, 153, 1)","bold":false,"italic":false,"underline":false,"align":"left","text":"실패시복구방안"}');
INSERT INTO wf_component_property VALUES ('961037311d5c4201b869c642abdbf394', 'display', '{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('961037311d5c4201b869c642abdbf394', 'validation', '{"required":false,"minLength":"0","maxLength":"512"}');
INSERT INTO wf_component_property VALUES ('961037311d5c4201b869c642abdbf394', 'element', '{"columnWidth":"10","rows":"3","placeholder":""}');
INSERT INTO wf_component_property VALUES ('961037311d5c4201b869c642abdbf394', 'label', '{"position":"left","fontSize":"14","fontColor":"rgba(141, 146, 153, 1)","bold":false,"italic":false,"underline":false,"align":"left","text":"시험계획"}');
INSERT INTO wf_component_property VALUES ('3c8cd6bf769041a68c628c1eb1fa2bd2', 'display', '{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('3c8cd6bf769041a68c628c1eb1fa2bd2', 'validation', '{"required":false,"minLength":"0","maxLength":"512"}');
INSERT INTO wf_component_property VALUES ('3c8cd6bf769041a68c628c1eb1fa2bd2', 'element', '{"columnWidth":"10","rows":"3","placeholder":""}');
INSERT INTO wf_component_property VALUES ('3c8cd6bf769041a68c628c1eb1fa2bd2', 'label', '{"position":"left","fontSize":"14","fontColor":"rgba(141, 146, 153, 1)","bold":false,"italic":false,"underline":false,"align":"left","text":"기타의견"}');
INSERT INTO wf_component_property VALUES ('f5081a7fd40d46da9b65daf5645906a0', 'display', '{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('f5081a7fd40d46da9b65daf5645906a0', 'validation', '{"required":false}');
INSERT INTO wf_component_property VALUES ('f5081a7fd40d46da9b65daf5645906a0', 'element', '{"columnWidth":"10","isEditable":false}');
INSERT INTO wf_component_property VALUES ('f5081a7fd40d46da9b65daf5645906a0', 'label', '{"position":"left","fontSize":"14","fontColor":"rgba(141, 146, 153, 1)","bold":false,"italic":false,"underline":false,"align":"left","text":"관련 CI"}');
INSERT INTO wf_component_property VALUES ('663450c3389c465584685365602dd961', 'display', '{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('663450c3389c465584685365602dd961', 'validation', '{"required":false}');
INSERT INTO wf_component_property VALUES ('663450c3389c465584685365602dd961', 'element', '{"columnWidth":"10","align":"left"}');
INSERT INTO wf_component_property VALUES ('663450c3389c465584685365602dd961', 'label', '{"position":"left","fontSize":"14","fontColor":"rgba(141, 146, 153, 1)","bold":false,"italic":false,"underline":false,"align":"left","text":"첨부파일"}');
INSERT INTO wf_component_property VALUES ('2437c9c99fc14cb895f2290519515a7b', 'display', '{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('2437c9c99fc14cb895f2290519515a7b', 'validation', '{"required":true}');
INSERT INTO wf_component_property VALUES ('2437c9c99fc14cb895f2290519515a7b', 'element', '{"columnWidth":"10","defaultValueCustomCode":"40288a19736b46fb01736b89e46c0008|none|"}');
INSERT INTO wf_component_property VALUES ('2437c9c99fc14cb895f2290519515a7b', 'label', '{"position":"left","fontSize":"14","fontColor":"rgba(141, 146, 153, 1)","bold":false,"italic":false,"underline":false,"align":"left","text":"승인자"}');
INSERT INTO wf_component_property VALUES ('1991ff59e63246ccb13a7946379b14e7', 'display', '{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('1991ff59e63246ccb13a7946379b14e7', 'validation', '{"required":true,"minDateTime":"","maxDateTime":""}');
INSERT INTO wf_component_property VALUES ('1991ff59e63246ccb13a7946379b14e7', 'element', '{"columnWidth":"10","defaultValueRadio":"now"}');
INSERT INTO wf_component_property VALUES ('1991ff59e63246ccb13a7946379b14e7', 'label', '{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"승인일시"}');
INSERT INTO wf_component_property VALUES ('51ac11434d1a4c0ca87b4930ffce047c', 'display', '{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('51ac11434d1a4c0ca87b4930ffce047c', 'validation', '{"required":false,"minLength":"0","maxLength":"512"}');
INSERT INTO wf_component_property VALUES ('51ac11434d1a4c0ca87b4930ffce047c', 'element', '{"columnWidth":"10","rows":"3","placeholder":""}');
INSERT INTO wf_component_property VALUES ('51ac11434d1a4c0ca87b4930ffce047c', 'label', '{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"승인 의견"}');
INSERT INTO wf_component_property VALUES ('5e32d98e8bcd4e8e9cc8cdaea9ee45ff', 'display', '{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('5e32d98e8bcd4e8e9cc8cdaea9ee45ff', 'validation', '{"required":true,"minDateTime":"","maxDateTime":""}');
INSERT INTO wf_component_property VALUES ('5e32d98e8bcd4e8e9cc8cdaea9ee45ff', 'element', '{"columnWidth":"10","defaultValueRadio":"now"}');
INSERT INTO wf_component_property VALUES ('5e32d98e8bcd4e8e9cc8cdaea9ee45ff', 'label', '{"position":"left","fontSize":"14","fontColor":"rgba(141, 146, 153, 1)","bold":false,"italic":false,"underline":false,"align":"left","text":"변경 결과서 작성일시"}');
INSERT INTO wf_component_property VALUES ('1c9cb6326b104af88ff79743ce63bcce', 'display', '{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('1c9cb6326b104af88ff79743ce63bcce', 'validation', '{"required":true}');
INSERT INTO wf_component_property VALUES ('1c9cb6326b104af88ff79743ce63bcce', 'element', '{"position":"left","columnWidth":10,"align":"horizontal","options":[{"name":"성공 종결","value":"sucess"},{"name":"실패 종결","value":""}]}');
INSERT INTO wf_component_property VALUES ('1c9cb6326b104af88ff79743ce63bcce', 'label', '{"position":"left","fontSize":"14","fontColor":"rgba(141, 146, 153, 1)","bold":false,"italic":false,"underline":false,"align":"left","text":"변경작업결과"}');
INSERT INTO wf_component_property VALUES ('45fdeda567e449849075599eaa4a8c06', 'display', '{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('45fdeda567e449849075599eaa4a8c06', 'validation', '{"required":true,"minLength":"0","maxLength":"512"}');
INSERT INTO wf_component_property VALUES ('45fdeda567e449849075599eaa4a8c06', 'element', '{"columnWidth":"10","rows":"3","placeholder":""}');
INSERT INTO wf_component_property VALUES ('45fdeda567e449849075599eaa4a8c06', 'label', '{"position":"left","fontSize":"14","fontColor":"rgba(141, 146, 153, 1)","bold":false,"italic":false,"underline":false,"align":"left","text":"내용"}');
INSERT INTO wf_component_property VALUES ('3f2ea69c92bd42a5a903b0d8884b19ab', 'display', '{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('3f2ea69c92bd42a5a903b0d8884b19ab', 'validation', '{"required":false}');
INSERT INTO wf_component_property VALUES ('3f2ea69c92bd42a5a903b0d8884b19ab', 'element', '{"columnWidth":"10","align":"left"}');
INSERT INTO wf_component_property VALUES ('3f2ea69c92bd42a5a903b0d8884b19ab', 'label', '{"position":"left","fontSize":"14","fontColor":"rgba(141, 146, 153, 1)","bold":false,"italic":false,"underline":false,"align":"left","text":"첨부파일"}');
INSERT INTO wf_component_property VALUES ('061ec27ed5e3446db15b256afb694f7b', 'display', '{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('061ec27ed5e3446db15b256afb694f7b', 'validation', '{"required":false,"minDateTime":"","maxDateTime":""}');
INSERT INTO wf_component_property VALUES ('061ec27ed5e3446db15b256afb694f7b', 'element', '{"columnWidth":"9","defaultValueRadio":"now"}');
INSERT INTO wf_component_property VALUES ('061ec27ed5e3446db15b256afb694f7b', 'label', '{"position":"left","fontSize":"14","fontColor":"rgba(141, 146, 153, 1)","bold":false,"italic":false,"underline":false,"align":"left","text":"모니터링 시작일시"}');
INSERT INTO wf_component_property VALUES ('a09757a1c1bb40e381e491474bd8ce75', 'display', '{"displayOrder":1,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('a09757a1c1bb40e381e491474bd8ce75', 'validation', '{"required":false,"minDateTime":"","maxDateTime":""}');
INSERT INTO wf_component_property VALUES ('a09757a1c1bb40e381e491474bd8ce75', 'element', '{"columnWidth":"9","defaultValueRadio":"dateTime|1|"}');
INSERT INTO wf_component_property VALUES ('a09757a1c1bb40e381e491474bd8ce75', 'label', '{"position":"left","fontSize":"14","fontColor":"rgba(141, 146, 153, 1)","bold":false,"italic":false,"underline":false,"align":"left","text":"모니터링종료일시"}');
INSERT INTO wf_component_property VALUES ('6a78f0ee60434ebab14d1b2854057e4c', 'display', '{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('6a78f0ee60434ebab14d1b2854057e4c', 'validation', '{"required":true}');
INSERT INTO wf_component_property VALUES ('6a78f0ee60434ebab14d1b2854057e4c', 'element', '{"position":"left","columnWidth":10,"align":"horizontal","options":[{"name":"성공","value":"sucess"},{"name":"실패","value":""}]}');
INSERT INTO wf_component_property VALUES ('6a78f0ee60434ebab14d1b2854057e4c', 'label', '{"position":"left","fontSize":"14","fontColor":"rgba(141, 146, 153, 1)","bold":false,"italic":false,"underline":false,"align":"left","text":"결과"}');
INSERT INTO wf_component_property VALUES ('7ce9995e0c2c40c18b8404f73684bd5d', 'display', '{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('7ce9995e0c2c40c18b8404f73684bd5d', 'validation', '{"required":false,"minLength":"0","maxLength":"512"}');
INSERT INTO wf_component_property VALUES ('7ce9995e0c2c40c18b8404f73684bd5d', 'element', '{"columnWidth":"10","rows":"3","placeholder":""}');
INSERT INTO wf_component_property VALUES ('7ce9995e0c2c40c18b8404f73684bd5d', 'label', '{"position":"left","fontSize":"14","fontColor":"rgba(141, 146, 153, 1)","bold":false,"italic":false,"underline":false,"align":"left","text":"내용"}');
INSERT INTO wf_component_property VALUES ('9312d808fce84d5fb061a99e6d779c4d', 'display', '{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('9312d808fce84d5fb061a99e6d779c4d', 'validation', '{"required":true}');
INSERT INTO wf_component_property VALUES ('9312d808fce84d5fb061a99e6d779c4d', 'element', '{"columnWidth":"10","defaultValueCustomCode":"40288a19736b46fb01736b89e46c0008|none|"}');
INSERT INTO wf_component_property VALUES ('9312d808fce84d5fb061a99e6d779c4d', 'label', '{"position":"left","fontSize":"14","fontColor":"rgba(141, 146, 153, 1)","bold":false,"italic":false,"underline":false,"align":"left","text":"검토자"}');
INSERT INTO wf_component_property VALUES ('d189734cda47408ca2a33a4d6b8d04b2', 'display', '{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('d189734cda47408ca2a33a4d6b8d04b2', 'validation', '{"required":true,"minDateTime":"","maxDateTime":""}');
INSERT INTO wf_component_property VALUES ('d189734cda47408ca2a33a4d6b8d04b2', 'element', '{"columnWidth":"10","defaultValueRadio":"now"}');
INSERT INTO wf_component_property VALUES ('d189734cda47408ca2a33a4d6b8d04b2', 'label', '{"position":"left","fontSize":"14","fontColor":"rgba(141, 146, 153, 1)","bold":false,"italic":false,"underline":false,"align":"left","text":"최종 검토일시"}');
INSERT INTO wf_component_property VALUES ('60b0b5ffba4047bbb2f2779f40df84ac', 'display', '{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('60b0b5ffba4047bbb2f2779f40df84ac', 'validation', '{"required":false,"minLength":"0","maxLength":"512"}');
INSERT INTO wf_component_property VALUES ('60b0b5ffba4047bbb2f2779f40df84ac', 'element', '{"columnWidth":"10","rows":"3","placeholder":""}');
INSERT INTO wf_component_property VALUES ('60b0b5ffba4047bbb2f2779f40df84ac', 'label', '{"position":"left","fontSize":"14","fontColor":"rgba(141, 146, 153, 1)","bold":false,"italic":false,"underline":false,"align":"left","text":"최종 검토의견"}');
/**
 * 컴포넌트세부설정
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

/* 서비스데스크 - 단순문의 */
INSERT INTO wf_element VALUES ('337ab138ae9e4250b41be736e0a09c5b','4028b21f7c9698f4017c96a70ded0000','userTask','승인','',true,'','{"width":160,"height":40,"position-x":744,"position-y":670}');
INSERT INTO wf_element VALUES ('3cc34436df104c9eaf6852f52d0ad8a2','4028b21f7c9698f4017c96a70ded0000','signalSend','만족도 평가','',false,'','{"width":40,"height":40,"position-x":1044,"position-y":210}');
INSERT INTO wf_element VALUES ('4be94f828adb4b5a938b82a25feca589','4028b21f7c9698f4017c96a70ded0000','userTask','신청서 검토','',true,'','{"width":160,"height":40,"position-x":454,"position-y":390}');
INSERT INTO wf_element VALUES ('536974f7f4484443acd76b5bd80fc159','4028b21f7c9698f4017c96a70ded0000','arrowConnector','승인','',false,'','{"mid-point":[934,670],"target-point":[934,210],"text-point":[22,5]}');
INSERT INTO wf_element VALUES ('53be2caebd5e40e0b6e9ebecce3f16bd','4028b21f7c9698f4017c96a70ded0000','userTask','처리','',false,'','{"width":160,"height":40,"position-x":744,"position-y":520}');
INSERT INTO wf_element VALUES ('64c845635dfe43be8fba233999327cee','4028b21f7c9698f4017c96a70ded0000','arrowConnector','접수','',false,'','{"text-point":[25,-39]}');
INSERT INTO wf_element VALUES ('670f8e3514c54a429b0d04897907fa45','4028b21f7c9698f4017c96a70ded0000','commonStart','시작','',false,'','{"width":40,"height":40,"position-x":244,"position-y":210}');
INSERT INTO wf_element VALUES ('6c42da85993f4ae9b551ef67b15c5d49','4028b21f7c9698f4017c96a70ded0000','arrowConnector','승인요청','',false,'','{"text-point":[14,19]}');
INSERT INTO wf_element VALUES ('7a1ec9adb4594335bfbd973c0e35489c','4028b21f7c9698f4017c96a70ded0000','commonEnd','종료','',false,'','{"width":40,"height":40,"position-x":1214,"position-y":210}');
INSERT INTO wf_element VALUES ('9c7c235aa4eb43d8a912b2e524264c79','4028b21f7c9698f4017c96a70ded0000','userTask','신청서 작성','',false,'','{"width":160,"height":40,"position-x":454,"position-y":210}');
INSERT INTO wf_element VALUES ('a727237e2c6f9dbdbbff693ed151c85d','4028b21f7c9698f4017c96a70ded0000','arrowConnector','신청서 등록','',false,'','{"text-point":[14,28]}');
INSERT INTO wf_element VALUES ('b579885ee8094ab7989916905212ca03','4028b21f7c9698f4017c96a70ded0000','arrowConnector','','',false,'','{}');
INSERT INTO wf_element VALUES ('c731ce831af64b798fcbd074d5d932be','4028b21f7c9698f4017c96a70ded0000','arrowConnector','','',false,'','{}');
INSERT INTO wf_element VALUES ('e577dd3058394669acb40d471d83635e','4028b21f7c9698f4017c96a70ded0000','arrowConnector','','',false,'','{}');
INSERT INTO wf_element VALUES ('eba7310bd0dd41cfb88622e8f8e00767','4028b21f7c9698f4017c96a70ded0000','manualTask','접수','',false,'','{"width":160,"height":40,"position-x":744,"position-y":390}');
/* 서비스데스크 - 단순문의 - 만족도 */
INSERT INTO wf_element VALUES ('00d3cbc448594f90a91448a7eef93dcb','4028b21f7c9698f4017c96c5630c0002','userTask','만족도 평가','',true,'','{"width":160,"height":40,"position-x":680,"position-y":370}');
INSERT INTO wf_element VALUES ('2a85de200cc44f17a540d9f2ccea6c97','4028b21f7c9698f4017c96c5630c0002','commonStart','시작','',false,'','{"width":40,"height":40,"position-x":460,"position-y":370}');
INSERT INTO wf_element VALUES ('2b4667523f6445ab835e2b5135ddfecd','4028b21f7c9698f4017c96c5630c0002','commonEnd','종료','',false,'','{"width":40,"height":40,"position-x":900,"position-y":370}');
INSERT INTO wf_element VALUES ('71ac5aa1ed1142deb364bbc9bea88cca','4028b21f7c9698f4017c96c5630c0002','arrowConnector','만족도 평가','',false,'','{"text-point":[1,-38]}');
INSERT INTO wf_element VALUES ('fca1181323704662acc1c234886be58e','4028b21f7c9698f4017c96c5630c0002','arrowConnector','','',false,'','{}');
/* 서비스데스크 - 장애신고 */
INSERT INTO wf_element VALUES ('a03d8919f63a4e99e0ace283a7119717','4028b21f7c81a928017c81aa9dc60000','arrowConnector','','',false,'','{}');
INSERT INTO wf_element VALUES ('a109f703f8258c01c71fd7a2c6917225','4028b21f7c81a928017c81aa9dc60000','commonStart','시작','',false,'','{"width":40,"height":40,"position-x":75,"position-y":257}');
INSERT INTO wf_element VALUES ('a117f0729b2cd106fa00b36f12fe1cfb','4028b21f7c81a928017c81aa9dc60000','userTask','신청서 검토','',true,'','{"width":160,"height":40,"position-x":225,"position-y":357}');
INSERT INTO wf_element VALUES ('a13cd52a4338bde9db939249fcb722f7','4028b21f7c81a928017c81aa9dc60000','arrowConnector','','',false,'','{}');
INSERT INTO wf_element VALUES ('a1c6c91a6dd476ae8ffb5e0c7e9c0d53','4028b21f7c81a928017c81aa9dc60000','userTask','신청서 작성','',false,'','{"width":160,"height":40,"position-x":225,"position-y":257}');
INSERT INTO wf_element VALUES ('a271b9f3a790280811cd6a6ff94c02e1','4028b21f7c81a928017c81aa9dc60000','arrowConnector','','',false,'','{"mid-point":[885,397]}');
INSERT INTO wf_element VALUES ('a296d2e534c0c2fe97071cdc9acfeb80','4028b21f7c81a928017c81aa9dc60000','subprocess','장애 이관','',false,'','{"width":152,"height":40,"position-x":745,"position-y":517}');
INSERT INTO wf_element VALUES ('a2a91cf26c14dc98cbc1dfd9ac433477','4028b21f7c81a928017c81aa9dc60000','arrowConnector','','',false,'','{}');
INSERT INTO wf_element VALUES ('a2c92c99371e885e53bc0b3ae65b8ac8','4028b21f7c81a928017c81aa9dc60000','arrowConnector','','',false,'','{}');
INSERT INTO wf_element VALUES ('a45089565ee86a40cfcc16070d6055a4','4028b21f7c81a928017c81aa9dc60000','arrowConnector','','',false,'','{}');
INSERT INTO wf_element VALUES ('a4bf5389f404c756d957b40cd7a1bf10','4028b21f7c81a928017c81aa9dc60000','manualTask','작업 결과 보고','',false,'','{"width":160,"height":40,"position-x":745,"position-y":397}');
INSERT INTO wf_element VALUES ('a7acf388af93de92a17b7068f846c073','4028b21f7c81a928017c81aa9dc60000','commonEnd','종료','',false,'','{"width":40,"height":40,"position-x":1337,"position-y":260}');
INSERT INTO wf_element VALUES ('a7f0ad953c834f779aac354f97b84eb2','4028b21f7c81a928017c81aa9dc60000','signalSend','만족도 평가','',false,'','{"width":40,"height":40,"position-x":1220,"position-y":260}');
INSERT INTO wf_element VALUES ('a7f615029e8be83d8008d2012454e1f4','4028b21f7c81a928017c81aa9dc60000','arrowConnector','자체 처리','',false,'','{"mid-point":[615,397],"text-point":[-36,-48]}');
INSERT INTO wf_element VALUES ('a84a4bafdc3b3f1928a0677434d34feb','4028b21f7c81a928017c81aa9dc60000','userTask','승인','',true,'','{"width":160,"height":40,"position-x":1027,"position-y":620}');
INSERT INTO wf_element VALUES ('aa104e687c0f220966d493707a87def1','4028b21f7c81a928017c81aa9dc60000','exclusiveGateway','','',false,'','{"width":34,"height":34,"position-x":615,"position-y":457}');
INSERT INTO wf_element VALUES ('aa57399fa79e38fd8e59a3a1a11e56d2','4028b21f7c81a928017c81aa9dc60000','exclusiveGateway','','',false,'','{"width":34,"height":34,"position-x":885,"position-y":457}');
INSERT INTO wf_element VALUES ('aa900688d722cd451b47e72dda93fca8','4028b21f7c81a928017c81aa9dc60000','userTask','승인요청','',false,'','{"width":160,"height":40,"position-x":1025,"position-y":457}');
INSERT INTO wf_element VALUES ('ab3d8234ca2ab3b44934df41563f067d','4028b21f7c81a928017c81aa9dc60000','arrowConnector','접수','',false,'','{"text-point":[4,-38]}');
INSERT INTO wf_element VALUES ('ac6b5d8653e21191f1f0ac59136a5297','4028b21f7c81a928017c81aa9dc60000','arrowConnector','','',false,'','{"mid-point":[885,517]}');
INSERT INTO wf_element VALUES ('ae39db34c9022d8fa2e101b32a5cb860','4028b21f7c81a928017c81aa9dc60000','arrowConnector','승인요청','',false,'','{"text-point":[15,25]}');
INSERT INTO wf_element VALUES ('ae5f5ef48e6ad78e8351b93017c565c9','4028b21f7c81a928017c81aa9dc60000','arrowConnector','승인','',false,'','{"mid-point":[1147,260],"source-point":[1147,620],"text-point":[31,-83]}');
INSERT INTO wf_element VALUES ('aea0ee0f94c686307c87a5c86699ef86','4028b21f7c81a928017c81aa9dc60000','userTask','처리','',false,'','{"width":160,"height":40,"position-x":465,"position-y":457}');
INSERT INTO wf_element VALUES ('aeefd9f38872ec0907d42c401dc8b71c','4028b21f7c81a928017c81aa9dc60000','manualTask','접수','',false,'','{"width":160,"height":40,"position-x":465,"position-y":357}');
INSERT INTO wf_element VALUES ('af4ce9dee1335cb46a3c127d27267bb1','4028b21f7c81a928017c81aa9dc60000','arrowConnector','신청서 등록','',false,'','{"text-point":[10,-4]}');
INSERT INTO wf_element VALUES ('af59396a9ffd08eee3efb2d0f0a4fd21','4028b21f7c81a928017c81aa9dc60000','arrowConnector','장애 이관','',false,'','{"mid-point":[615,517],"text-point":[-40,21]}');
/* 서비스데스크 - 장애신고 - 만족도 */
INSERT INTO wf_element VALUES ('2f3712ca2ea34ae1800179f2dd7f6424','4028b21f7c9b6b1e017c9bdf04cb0011','commonEnd','종료','',false,'','{"width":40,"height":40,"position-x":900,"position-y":370}');
INSERT INTO wf_element VALUES ('36dad342725848f7ab1453e309ef0aa1','4028b21f7c9b6b1e017c9bdf04cb0011','userTask','만족도 평가','',true,'','{"width":160,"height":40,"position-x":680,"position-y":370}');
INSERT INTO wf_element VALUES ('8d12225883ea4eb3bf2d137c67278eb8','4028b21f7c9b6b1e017c9bdf04cb0011','arrowConnector','','',false,'','{}');
INSERT INTO wf_element VALUES ('997d5b3c3246420181a774069b489909','4028b21f7c9b6b1e017c9bdf04cb0011','commonStart','시작','',false,'','{"width":40,"height":40,"position-x":460,"position-y":370}');
INSERT INTO wf_element VALUES ('a9eac2d7b40b4ea880926a38379f3ddd','4028b21f7c9b6b1e017c9bdf04cb0011','arrowConnector','만족도 평가','',false,'','{"text-point":[1,-38]}');
/* 서비스데스크 - 서비스요청 */
INSERT INTO wf_element VALUES ('04195a896b4a4c5f8c2e8d221c536bc8','4028b21f7c9ff7c8017ca0549ef00057','userTask','신청서 검토','',true,'','{"width":160,"height":40,"position-x":230,"position-y":350}');
INSERT INTO wf_element VALUES ('095e98b2596a4019ae25c01de033aed3','4028b21f7c9ff7c8017ca0549ef00057','manualTask','작업 결과 보고','',false,'','{"width":160,"height":40,"position-x":750,"position-y":350}');
INSERT INTO wf_element VALUES ('0c98f0917c7347418933aab46b8b7823','4028b21f7c9ff7c8017ca0549ef00057','userTask','처리','',false,'','{"width":160,"height":40,"position-x":450,"position-y":450}');
INSERT INTO wf_element VALUES ('24372ebb7e174fca99ae500d05aed4b5','4028b21f7c9ff7c8017ca0549ef00057','subprocess','APP 변경 이관','',false,'','{"width":152,"height":40,"position-x":750,"position-y":450}');
INSERT INTO wf_element VALUES ('447aceb93c124909954082bfed2cc74f','4028b21f7c9ff7c8017ca0549ef00057','arrowConnector','','',false,'','{}');
INSERT INTO wf_element VALUES ('5c2b8645d1994bf683c4c51e08e2932a','4028b21f7c9ff7c8017ca0549ef00057','arrowConnector','신청서 등록','',false,'','{"text-point":[9,-8]}');
INSERT INTO wf_element VALUES ('5cca217b673542e1ad086a67777ef28e','4028b21f7c9ff7c8017ca0549ef00057','arrowConnector','','',false,'','{}');
INSERT INTO wf_element VALUES ('5e98b781b4e74d85ac90e6703cd052a6','4028b21f7c9ff7c8017ca0549ef00057','arrowConnector','','',false,'','{}');
INSERT INTO wf_element VALUES ('5fe9d50a699f4d389ee702135c67d7b7','4028b21f7c9ff7c8017ca0549ef00057','exclusiveGateway','','',false,'','{"width":34,"height":34,"position-x":580,"position-y":450}');
INSERT INTO wf_element VALUES ('6774c1370d614d47bb3b9b0ec33b9c10','4028b21f7c9ff7c8017ca0549ef00057','userTask','승인','',true,'','{"width":160,"height":40,"position-x":1040,"position-y":650}');
INSERT INTO wf_element VALUES ('6eb7ec88b9cf4d3384d0f873b016a76b','4028b21f7c9ff7c8017ca0549ef00057','arrowConnector','인프라 변경 이관','',false,'','{"mid-point":[580,550],"text-point":[6,7]}');
INSERT INTO wf_element VALUES ('705f3c898c3e4b2f8ea838e1b7a1b72f','4028b21f7c9ff7c8017ca0549ef00057','arrowConnector','접수','',false,'','{"text-point":[-4,-37]}');
INSERT INTO wf_element VALUES ('73d8d9a35d7947c68d1abaece193585f','4028b21f7c9ff7c8017ca0549ef00057','arrowConnector','','',false,'','{"mid-point":[900,550]}');
INSERT INTO wf_element VALUES ('74a586945aca4bd598297f1f72e2bbd2','4028b21f7c9ff7c8017ca0549ef00057','signalSend','만족도 평가','',false,'','{"width":40,"height":40,"position-x":1230,"position-y":250}');
INSERT INTO wf_element VALUES ('8187b4a4e8e54c3c86c9acdf3726b445','4028b21f7c9ff7c8017ca0549ef00057','arrowConnector','','',false,'','{"mid-point":[900,350]}');
INSERT INTO wf_element VALUES ('898c51972e9c45e49646c3797165411d','4028b21f7c9ff7c8017ca0549ef00057','userTask','승인요청','',false,'','{"width":160,"height":40,"position-x":1040,"position-y":450}');
INSERT INTO wf_element VALUES ('8a2ff6fdb21d4602a2c4c365798469c4','4028b21f7c9ff7c8017ca0549ef00057','arrowConnector','승인요청','',false,'','{"text-point":[13,34]}');
INSERT INTO wf_element VALUES ('8b962df9ad894d3283ac188d8759267b','4028b21f7c9ff7c8017ca0549ef00057','commonStart','시작','',false,'','{"width":40,"height":40,"position-x":80,"position-y":250}');
INSERT INTO wf_element VALUES ('9ccf78fb415c4b09b32b65e946f0ef93','4028b21f7c9ff7c8017ca0549ef00057','commonEnd','종료','',false,'','{"width":40,"height":40,"position-x":1320,"position-y":250}');
INSERT INTO wf_element VALUES ('9cde4a46cd8c42d8af2d45ba77721091','4028b21f7c9ff7c8017ca0549ef00057','subprocess','인프라 변경 이관','',false,'','{"width":152,"height":40,"position-x":750,"position-y":550}');
INSERT INTO wf_element VALUES ('a71a9701104d441c8b8f73d4afd88f6e','4028b21f7c9ff7c8017ca0549ef00057','arrowConnector','자체 처리','',false,'','{"mid-point":[580,350],"text-point":[6,-93]}');
INSERT INTO wf_element VALUES ('ade030c7ef0f1914e5b06e2e31f8147a','4028b21f7c9ff7c8017ca0549ef00057','arrowConnector','','',false,'','{}');
INSERT INTO wf_element VALUES ('af8674894f8bc525c6f41a482bcf4300','4028b21f7c9ff7c8017ca0549ef00057','arrowConnector','승인','',false,'','{"mid-point":[1160,650],"target-point":[1160,250],"text-point":[29,-39]}');
INSERT INTO wf_element VALUES ('cc4048cd712147b8af4016e647e69b14','4028b21f7c9ff7c8017ca0549ef00057','userTask','신청서 작성','',false,'','{"width":160,"height":40,"position-x":230,"position-y":250}');
INSERT INTO wf_element VALUES ('d0d3a5405e974ec8ad00a72a9eaf6f25','4028b21f7c9ff7c8017ca0549ef00057','arrowConnector','APP 변경 이관','',false,'','{"text-point":[-36,-50]}');
INSERT INTO wf_element VALUES ('d4b6e2e20c884104ac36916f803c65f1','4028b21f7c9ff7c8017ca0549ef00057','exclusiveGateway','','',false,'','{"width":34,"height":34,"position-x":900,"position-y":450}');
INSERT INTO wf_element VALUES ('dd46580c457f49afb05b4d719293d8f5','4028b21f7c9ff7c8017ca0549ef00057','arrowConnector','','',false,'','{}');
INSERT INTO wf_element VALUES ('f4f0bfcfdb3e406e9eec224d79c10ee6','4028b21f7c9ff7c8017ca0549ef00057','manualTask','접수','',false,'','{"width":160,"height":40,"position-x":450,"position-y":350}');
INSERT INTO wf_element VALUES ('f6c8b24bb46041a889a26a84d53ad401','4028b21f7c9ff7c8017ca0549ef00057','arrowConnector','','',false,'','{}');
/* 서비스데스크 - 서비스요청 - 만족도 */
INSERT INTO wf_element VALUES ('0f9c64e3371448358e6f39f292c93a46','4028b21f7c9cc269017c9cc76a5e0000','arrowConnector','만족도 평가','',false,'','{"text-point":[1,-38]}');
INSERT INTO wf_element VALUES ('3914bb0a2d32423885ab84339bf391d5','4028b21f7c9cc269017c9cc76a5e0000','userTask','만족도 평가','',true,'','{"width":160,"height":40,"position-x":680,"position-y":370}');
INSERT INTO wf_element VALUES ('cc5734c370864f15be79c750c9ffffdb','4028b21f7c9cc269017c9cc76a5e0000','commonStart','시작','',false,'','{"width":40,"height":40,"position-x":460,"position-y":370}');
INSERT INTO wf_element VALUES ('d855698cb2c7443194b03eb17fe69522','4028b21f7c9cc269017c9cc76a5e0000','arrowConnector','','',false,'','{}');
INSERT INTO wf_element VALUES ('f9a7329b5cd24001b532265ee88ad3ee','4028b21f7c9cc269017c9cc76a5e0000','commonEnd','종료','',false,'','{"width":40,"height":40,"position-x":900,"position-y":370}');
/* 인프라변경관리 */
INSERT INTO wf_element VALUES ('0844d2fc0cfc45238f24f0a09a328f0a', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'arrowConnector', '', '', false, '', '{"mid-point":[-458,20]}');
INSERT INTO wf_element VALUES ('0c5465de1a8a480a9ac919b79b29569f', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'arrowConnector', '', '', false, '', '{}');
INSERT INTO wf_element VALUES ('0e1c6a483d1f415eb1dd91c0348f13dd', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'manualTask', '접수', '', false, '', '{"width":160,"height":40,"position-x":-203,"position-y":220}');
INSERT INTO wf_element VALUES ('0f188cf2587b488293d6c22b74258167', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'arrowConnector', '승인', '', false, '', '{"mid-point":[327,390],"text-point":[73,-105]}');
INSERT INTO wf_element VALUES ('139f55357c1242cc89a0a8162734bae6', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'userTask', '최종검토', '', true, '', '{"width":160,"height":40,"position-x":1384,"position-y":393}');
INSERT INTO wf_element VALUES ('32f0d926786441ee84b6d56bf2f0fd08', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'commonStart', '시작', '', false, '', '{"width":40,"height":40,"position-x":-578,"position-y":20}');
INSERT INTO wf_element VALUES ('3d56aaee23934e179d2c0ba8e0c860a2', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'arrowConnector', '자체처리', '', false, '', '{"text-point":[-65,-4]}');
INSERT INTO wf_element VALUES ('432255a7eae9415085f38348d6445468', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'arrowConnector', '구성관리', '', false, '', '{"text-point":[-59,-44]}');
INSERT INTO wf_element VALUES ('474cccfff5344196ade87e3479910a6f', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'arrowConnector', '', '', false, '', '{}');
INSERT INTO wf_element VALUES ('4b7e284e81014cfda64de39550895647', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'userTask', '처리', '', true, '', '{"width":160,"height":40,"position-x":327,"position-y":220}');
INSERT INTO wf_element VALUES ('4f2a50e6b49a4e1fab1a9866277a6996', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'exclusiveGateway', '', '', false, '', '{"width":34,"height":34,"position-x":776,"position-y":218}');
INSERT INTO wf_element VALUES ('515b32b1032c48d48b0cb7f304701aec', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'arrowConnector', 'PIR 작성', '', false, '', '{"text-point":[0,22]}');
INSERT INTO wf_element VALUES ('58d6f5e75a5543ab96bc0fa1fe2637c7', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'userTask', '변경계획서 검토', '', true, '', '{"width":160,"height":40,"position-x":177,"position-y":390.0000305175781}');
INSERT INTO wf_element VALUES ('6366778fd60e4a2d913e0d84f80bd11c', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'arrowConnector', '', '', false, '', '{}');
INSERT INTO wf_element VALUES ('697ba85cf98c4d9083f83bd5dd66639f', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'userTask', '변경계획서 작성', '', true, '', '{"width":160,"height":40,"position-x":27,"position-y":220}');
INSERT INTO wf_element VALUES ('7de6a641ac754f48be50f40c63bb1ef5', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'subprocess', '구성관리', '', false, '', '{"width":152,"height":40,"position-x":637,"position-y":140}');
INSERT INTO wf_element VALUES ('89d3605de19d491ea6590640b94461d4', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'arrowConnector', '검토완료', '', false, '', '{"text-point":[14,-66]}');
INSERT INTO wf_element VALUES ('8b8564b9f8164971bee112f0a73e66f3', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'userTask', '변경결과서 작성', '', true, '', '{"width":160,"height":40,"position-x":952,"position-y":220}');
INSERT INTO wf_element VALUES ('996c1c14b95643b1b0a22436f2dee6ea', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'exclusiveGateway', '', '', false, '', '{"width":34,"height":34,"position-x":517,"position-y":220}');
INSERT INTO wf_element VALUES ('9d30b4b0fa3e4b9190c5042e379e59f6', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'commonEnd', '종료', '', false, '', '{"width":40,"height":40,"position-x":1384,"position-y":43}');
INSERT INTO wf_element VALUES ('9deadc6cdd6947eebcac777abaa8338b', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'arrowConnector', '', '', false, '', '{}');
INSERT INTO wf_element VALUES ('a0bfeb6307bf9d5c6c71db3c5e3232df', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'arrowConnector', '접수', '', false, '', '{"text-point":[10,8]}');
INSERT INTO wf_element VALUES ('a94aaf66757795501eacedd06a5b3295', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'arrowConnector', '검토요청', '', false, '', '{"mid-point":[1194,393],"text-point":[7,26]}');
INSERT INTO wf_element VALUES ('baae90a53e39457480d0b2139622e1d2', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'manualTask', '자체처리', '', false, '', '{"width":160,"height":40,"position-x":637,"position-y":290}');
INSERT INTO wf_element VALUES ('be6990d4bd1f4c3586df37fe147b2dea', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'userTask', 'PIR', '', false, '', '{"width":160,"height":40,"position-x":1192,"position-y":220}');
INSERT INTO wf_element VALUES ('c9c7ed6d193b4fd58b726668cbddccae', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'userTask', '이관문서검토', '', false, '', '{"width":160,"height":40,"position-x":-458,"position-y":220}');
INSERT INTO wf_element VALUES ('e15d71bd48bc485f860d2abb70d8d16e', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'arrowConnector', '승인요청', '', false, '', '{"mid-point":[177,220],"text-point":[60,66]}');
INSERT INTO wf_element VALUES ('e2c199ab8f4a4c2ab7cdfc67073b3340', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'arrowConnector', '', '', false, '', '{}');

/**
 * 문서출력정보
 */
DROP TABLE IF EXISTS wf_document_display cascade;

CREATE TABLE wf_document_display
(
	document_id varchar(128) NOT NULL,
	form_group_id varchar(128) NOT NULL,
	element_id varchar(256) NOT NULL,
	display varchar(100) DEFAULT 'document.displayType.editable' NOT NULL,
	CONSTRAINT wf_document_display_pk PRIMARY KEY (document_id, form_group_id, element_id),
	CONSTRAINT wf_document_display_fk1 FOREIGN KEY (document_id) REFERENCES wf_document (document_id),
	CONSTRAINT wf_document_display_fk2 FOREIGN KEY (form_group_id) REFERENCES wf_form_group (form_group_id),
	CONSTRAINT wf_document_display_fk3 FOREIGN KEY (element_id) REFERENCES wf_element (element_id)
);

COMMENT ON TABLE wf_document_display IS '문서출력정보';
COMMENT ON COLUMN wf_document_display.document_id IS '신청서아이디';
COMMENT ON COLUMN wf_document_display.form_group_id IS '문서그룹아이디';
COMMENT ON COLUMN wf_document_display.element_id IS '엘리먼트아이디';
COMMENT ON COLUMN wf_document_display.display IS '엘리먼트별컴포넌트출력정보';

/* 서비스데스크 - 단순문의 */
INSERT INTO wf_document_display VALUES ('4028b21f7c90d996017c91ae7987004f','g028b21f7c780ba6017c7832447201e1','9c7c235aa4eb43d8a912b2e524264c79','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c90d996017c91ae7987004f','g028b21f7c780ba6017c7832447201e1','4be94f828adb4b5a938b82a25feca589','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c90d996017c91ae7987004f','g028b21f7c780ba6017c7832447201e1','53be2caebd5e40e0b6e9ebecce3f16bd','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c90d996017c91ae7987004f','g028b21f7c780ba6017c7832447201e1','337ab138ae9e4250b41be736e0a09c5b','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c90d996017c91ae7987004f','g028b21f7c780ba6017c7832480101e2','9c7c235aa4eb43d8a912b2e524264c79','document.displayType.editable');
INSERT INTO wf_document_display VALUES ('4028b21f7c90d996017c91ae7987004f','g028b21f7c780ba6017c7832480101e2','4be94f828adb4b5a938b82a25feca589','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c90d996017c91ae7987004f','g028b21f7c780ba6017c7832480101e2','53be2caebd5e40e0b6e9ebecce3f16bd','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c90d996017c91ae7987004f','g028b21f7c780ba6017c7832480101e2','337ab138ae9e4250b41be736e0a09c5b','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c90d996017c91ae7987004f','g028b21f7c780ba6017c7832527e0203','9c7c235aa4eb43d8a912b2e524264c79','document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b21f7c90d996017c91ae7987004f','g028b21f7c780ba6017c7832527e0203','4be94f828adb4b5a938b82a25feca589','document.displayType.editable');
INSERT INTO wf_document_display VALUES ('4028b21f7c90d996017c91ae7987004f','g028b21f7c780ba6017c7832527e0203','53be2caebd5e40e0b6e9ebecce3f16bd','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c90d996017c91ae7987004f','g028b21f7c780ba6017c7832527e0203','337ab138ae9e4250b41be736e0a09c5b','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c90d996017c91ae7987004f','g028b21f7c780ba6017c783258080214','9c7c235aa4eb43d8a912b2e524264c79','document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b21f7c90d996017c91ae7987004f','g028b21f7c780ba6017c783258080214','4be94f828adb4b5a938b82a25feca589','document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b21f7c90d996017c91ae7987004f','g028b21f7c780ba6017c783258080214','53be2caebd5e40e0b6e9ebecce3f16bd','document.displayType.editable');
INSERT INTO wf_document_display VALUES ('4028b21f7c90d996017c91ae7987004f','g028b21f7c780ba6017c783258080214','337ab138ae9e4250b41be736e0a09c5b','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c90d996017c91ae7987004f','g028b21f7c780ba6017c78325ca80225','9c7c235aa4eb43d8a912b2e524264c79','document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b21f7c90d996017c91ae7987004f','g028b21f7c780ba6017c78325ca80225','4be94f828adb4b5a938b82a25feca589','document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b21f7c90d996017c91ae7987004f','g028b21f7c780ba6017c78325ca80225','53be2caebd5e40e0b6e9ebecce3f16bd','document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b21f7c90d996017c91ae7987004f','g028b21f7c780ba6017c78325ca80225','337ab138ae9e4250b41be736e0a09c5b','document.displayType.editable');
/* 서비스데스크 - 단순문의 - 만족도 */
INSERT INTO wf_document_display VALUES ('4028b21f7c90d996017c91aeff1b0050','g028b21f7c780ba6017c78334c07023c','00d3cbc448594f90a91448a7eef93dcb','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c90d996017c91aeff1b0050','g028b21f7c780ba6017c78334f3a0244','00d3cbc448594f90a91448a7eef93dcb','document.displayType.editable');
/* 서비스데스크 - 장애신고 */
INSERT INTO wf_document_display VALUES ('4028b21f7c9adb6a017c9b18489900c9','4028b21f7c9adb6a017c9b0613610065','a1c6c91a6dd476ae8ffb5e0c7e9c0d53','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9adb6a017c9b18489900c9','4028b21f7c9adb6a017c9b0613610065','a117f0729b2cd106fa00b36f12fe1cfb','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9adb6a017c9b18489900c9','4028b21f7c9adb6a017c9b0613610065','aea0ee0f94c686307c87a5c86699ef86','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9adb6a017c9b18489900c9','4028b21f7c9adb6a017c9b0613610065','aa900688d722cd451b47e72dda93fca8','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9adb6a017c9b18489900c9','4028b21f7c9adb6a017c9b0613610065','a84a4bafdc3b3f1928a0677434d34feb','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9adb6a017c9b18489900c9','4028b21f7c9adb6a017c9b061411006d','a1c6c91a6dd476ae8ffb5e0c7e9c0d53','document.displayType.editable');
INSERT INTO wf_document_display VALUES ('4028b21f7c9adb6a017c9b18489900c9','4028b21f7c9adb6a017c9b061411006d','a117f0729b2cd106fa00b36f12fe1cfb','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9adb6a017c9b18489900c9','4028b21f7c9adb6a017c9b061411006d','aea0ee0f94c686307c87a5c86699ef86','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9adb6a017c9b18489900c9','4028b21f7c9adb6a017c9b061411006d','aa900688d722cd451b47e72dda93fca8','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9adb6a017c9b18489900c9','4028b21f7c9adb6a017c9b061411006d','a84a4bafdc3b3f1928a0677434d34feb','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9adb6a017c9b18489900c9','4028b21f7c9adb6a017c9b061691008c','a1c6c91a6dd476ae8ffb5e0c7e9c0d53','document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b21f7c9adb6a017c9b18489900c9','4028b21f7c9adb6a017c9b061691008c','a117f0729b2cd106fa00b36f12fe1cfb','document.displayType.editable');
INSERT INTO wf_document_display VALUES ('4028b21f7c9adb6a017c9b18489900c9','4028b21f7c9adb6a017c9b061691008c','aea0ee0f94c686307c87a5c86699ef86','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9adb6a017c9b18489900c9','4028b21f7c9adb6a017c9b061691008c','aa900688d722cd451b47e72dda93fca8','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9adb6a017c9b18489900c9','4028b21f7c9adb6a017c9b061691008c','a84a4bafdc3b3f1928a0677434d34feb','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9adb6a017c9b18489900c9','4028b21f7c9adb6a017c9b0617fe009e','a1c6c91a6dd476ae8ffb5e0c7e9c0d53','document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b21f7c9adb6a017c9b18489900c9','4028b21f7c9adb6a017c9b0617fe009e','a117f0729b2cd106fa00b36f12fe1cfb','document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b21f7c9adb6a017c9b18489900c9','4028b21f7c9adb6a017c9b0617fe009e','aea0ee0f94c686307c87a5c86699ef86','document.displayType.editable');
INSERT INTO wf_document_display VALUES ('4028b21f7c9adb6a017c9b18489900c9','4028b21f7c9adb6a017c9b0617fe009e','aa900688d722cd451b47e72dda93fca8','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9adb6a017c9b18489900c9','4028b21f7c9adb6a017c9b0617fe009e','a84a4bafdc3b3f1928a0677434d34feb','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9adb6a017c9b18489900c9','4028b21f7c9adb6a017c9b061a8b00bf','a1c6c91a6dd476ae8ffb5e0c7e9c0d53','document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b21f7c9adb6a017c9b18489900c9','4028b21f7c9adb6a017c9b061a8b00bf','a117f0729b2cd106fa00b36f12fe1cfb','document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b21f7c9adb6a017c9b18489900c9','4028b21f7c9adb6a017c9b061a8b00bf','aea0ee0f94c686307c87a5c86699ef86','document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b21f7c9adb6a017c9b18489900c9','4028b21f7c9adb6a017c9b061a8b00bf','aa900688d722cd451b47e72dda93fca8','document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b21f7c9adb6a017c9b18489900c9','4028b21f7c9adb6a017c9b061a8b00bf','a84a4bafdc3b3f1928a0677434d34feb','document.displayType.editable');
/* 서비스데스크 - 장애신고 - 만족도 */
INSERT INTO wf_document_display VALUES ('4028b21f7c9b6b1e017c9bedbe8a0012','4028b21f7c90d996017c914da7aa0021','36dad342725848f7ab1453e309ef0aa1','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9b6b1e017c9bedbe8a0012','4028b21f7c90d996017c914da8270029','36dad342725848f7ab1453e309ef0aa1','document.displayType.editable');
/* 서비스데스크 - 서비스요청 */
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca06bde520058','4028b21f7c9ff7c8017ca054465a0001','cc4048cd712147b8af4016e647e69b14','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca06bde520058','4028b21f7c9ff7c8017ca054465a0001','04195a896b4a4c5f8c2e8d221c536bc8','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca06bde520058','4028b21f7c9ff7c8017ca054465a0001','0c98f0917c7347418933aab46b8b7823','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca06bde520058','4028b21f7c9ff7c8017ca054465a0001','898c51972e9c45e49646c3797165411d','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca06bde520058','4028b21f7c9ff7c8017ca054465a0001','6774c1370d614d47bb3b9b0ec33b9c10','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca06bde520058','4028b21f7c9ff7c8017ca054470f000a','cc4048cd712147b8af4016e647e69b14','document.displayType.editable');
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca06bde520058','4028b21f7c9ff7c8017ca054470f000a','04195a896b4a4c5f8c2e8d221c536bc8','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca06bde520058','4028b21f7c9ff7c8017ca054470f000a','0c98f0917c7347418933aab46b8b7823','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca06bde520058','4028b21f7c9ff7c8017ca054470f000a','898c51972e9c45e49646c3797165411d','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca06bde520058','4028b21f7c9ff7c8017ca054470f000a','6774c1370d614d47bb3b9b0ec33b9c10','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca06bde520058','4028b21f7c9ff7c8017ca05448d20027','cc4048cd712147b8af4016e647e69b14','document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca06bde520058','4028b21f7c9ff7c8017ca05448d20027','04195a896b4a4c5f8c2e8d221c536bc8','document.displayType.editable');
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca06bde520058','4028b21f7c9ff7c8017ca05448d20027','0c98f0917c7347418933aab46b8b7823','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca06bde520058','4028b21f7c9ff7c8017ca05448d20027','898c51972e9c45e49646c3797165411d','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca06bde520058','4028b21f7c9ff7c8017ca05448d20027','6774c1370d614d47bb3b9b0ec33b9c10','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca06bde520058','4028b21f7c9ff7c8017ca05449e00039','cc4048cd712147b8af4016e647e69b14','document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca06bde520058','4028b21f7c9ff7c8017ca05449e00039','04195a896b4a4c5f8c2e8d221c536bc8','document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca06bde520058','4028b21f7c9ff7c8017ca05449e00039','0c98f0917c7347418933aab46b8b7823','document.displayType.editable');
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca06bde520058','4028b21f7c9ff7c8017ca05449e00039','898c51972e9c45e49646c3797165411d','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca06bde520058','4028b21f7c9ff7c8017ca05449e00039','6774c1370d614d47bb3b9b0ec33b9c10','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca06bde520058','4028b21f7c9ff7c8017ca0544af6004d','cc4048cd712147b8af4016e647e69b14','document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca06bde520058','4028b21f7c9ff7c8017ca0544af6004d','04195a896b4a4c5f8c2e8d221c536bc8','document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca06bde520058','4028b21f7c9ff7c8017ca0544af6004d','0c98f0917c7347418933aab46b8b7823','document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca06bde520058','4028b21f7c9ff7c8017ca0544af6004d','898c51972e9c45e49646c3797165411d','document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca06bde520058','4028b21f7c9ff7c8017ca0544af6004d','6774c1370d614d47bb3b9b0ec33b9c10','document.displayType.editable');
/* 서비스데스크 - 서비스요청 - 만족도 */
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca04d16830000','4028b21f7c90d996017c914eec300040','3914bb0a2d32423885ab84339bf391d5','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca04d16830000','4028b21f7c90d996017c914eecb30048','3914bb0a2d32423885ab84339bf391d5','document.displayType.editable');
/* 인프라변경관리 */
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a5670ae8', 'c9c7ed6d193b4fd58b726668cbddccae', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a5670ae8', '697ba85cf98c4d9083f83bd5dd66639f', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a5670ae8', '58d6f5e75a5543ab96bc0fa1fe2637c7', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a5670ae8', '4b7e284e81014cfda64de39550895647', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a5670ae8', '8b8564b9f8164971bee112f0a73e66f3', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a5670ae8', 'be6990d4bd1f4c3586df37fe147b2dea', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a5670ae8', '139f55357c1242cc89a0a8162734bae6', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a59a0af2', 'c9c7ed6d193b4fd58b726668cbddccae', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a59a0af2', '697ba85cf98c4d9083f83bd5dd66639f', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a59a0af2', '58d6f5e75a5543ab96bc0fa1fe2637c7', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a59a0af2', '4b7e284e81014cfda64de39550895647', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a59a0af2', '8b8564b9f8164971bee112f0a73e66f3', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a59a0af2', 'be6990d4bd1f4c3586df37fe147b2dea', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a59a0af2', '139f55357c1242cc89a0a8162734bae6', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a6360b0f', 'c9c7ed6d193b4fd58b726668cbddccae', 'document.displayType.editable');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a6360b0f', '697ba85cf98c4d9083f83bd5dd66639f', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a6360b0f', '58d6f5e75a5543ab96bc0fa1fe2637c7', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a6360b0f', '4b7e284e81014cfda64de39550895647', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a6360b0f', '8b8564b9f8164971bee112f0a73e66f3', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a6360b0f', 'be6990d4bd1f4c3586df37fe147b2dea', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a6360b0f', '139f55357c1242cc89a0a8162734bae6', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a6a10b25', 'c9c7ed6d193b4fd58b726668cbddccae', 'document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a6a10b25', '697ba85cf98c4d9083f83bd5dd66639f', 'document.displayType.editable');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a6a10b25', '58d6f5e75a5543ab96bc0fa1fe2637c7', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a6a10b25', '4b7e284e81014cfda64de39550895647', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a6a10b25', '8b8564b9f8164971bee112f0a73e66f3', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a6a10b25', 'be6990d4bd1f4c3586df37fe147b2dea', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a6a10b25', '139f55357c1242cc89a0a8162734bae6', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a6eb0b35', 'c9c7ed6d193b4fd58b726668cbddccae', 'document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a6eb0b35', '697ba85cf98c4d9083f83bd5dd66639f', 'document.displayType.editable');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a6eb0b35', '58d6f5e75a5543ab96bc0fa1fe2637c7', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a6eb0b35', '4b7e284e81014cfda64de39550895647', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a6eb0b35', '8b8564b9f8164971bee112f0a73e66f3', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a6eb0b35', 'be6990d4bd1f4c3586df37fe147b2dea', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a6eb0b35', '139f55357c1242cc89a0a8162734bae6', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a77f0b54', 'c9c7ed6d193b4fd58b726668cbddccae', 'document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a77f0b54', '697ba85cf98c4d9083f83bd5dd66639f', 'document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a77f0b54', '58d6f5e75a5543ab96bc0fa1fe2637c7', 'document.displayType.editable');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a77f0b54', '4b7e284e81014cfda64de39550895647', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a77f0b54', '8b8564b9f8164971bee112f0a73e66f3', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a77f0b54', 'be6990d4bd1f4c3586df37fe147b2dea', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a77f0b54', '139f55357c1242cc89a0a8162734bae6', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a79f0b5b', 'c9c7ed6d193b4fd58b726668cbddccae', 'document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a79f0b5b', '697ba85cf98c4d9083f83bd5dd66639f', 'document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a79f0b5b', '58d6f5e75a5543ab96bc0fa1fe2637c7', 'document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a79f0b5b', '4b7e284e81014cfda64de39550895647', 'document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a79f0b5b', '8b8564b9f8164971bee112f0a73e66f3', 'document.displayType.editable');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a79f0b5b', 'be6990d4bd1f4c3586df37fe147b2dea', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a79f0b5b', '139f55357c1242cc89a0a8162734bae6', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a7d90b68', 'c9c7ed6d193b4fd58b726668cbddccae', 'document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a7d90b68', '697ba85cf98c4d9083f83bd5dd66639f', 'document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a7d90b68', '58d6f5e75a5543ab96bc0fa1fe2637c7', 'document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a7d90b68', '4b7e284e81014cfda64de39550895647', 'document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a7d90b68', '8b8564b9f8164971bee112f0a73e66f3', 'document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a7d90b68', 'be6990d4bd1f4c3586df37fe147b2dea', 'document.displayType.editable');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a7d90b68', '139f55357c1242cc89a0a8162734bae6', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a81b0b77', 'c9c7ed6d193b4fd58b726668cbddccae', 'document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a81b0b77', '697ba85cf98c4d9083f83bd5dd66639f', 'document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a81b0b77', '58d6f5e75a5543ab96bc0fa1fe2637c7', 'document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a81b0b77', '4b7e284e81014cfda64de39550895647', 'document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a81b0b77', '8b8564b9f8164971bee112f0a73e66f3', 'document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a81b0b77', 'be6990d4bd1f4c3586df37fe147b2dea', 'document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a81b0b77', '139f55357c1242cc89a0a8162734bae6', 'document.displayType.editable');

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

/* 서비스데스크 - 단순문의 */
INSERT INTO wf_element_data VALUES ('337ab138ae9e4250b41be736e0a09c5b','assignee-type','assignee.type.assignee',0,true);
INSERT INTO wf_element_data VALUES ('337ab138ae9e4250b41be736e0a09c5b','assignee','z-approver',1,true);
INSERT INTO wf_element_data VALUES ('337ab138ae9e4250b41be736e0a09c5b','reject-id','53be2caebd5e40e0b6e9ebecce3f16bd',2,false);
INSERT INTO wf_element_data VALUES ('337ab138ae9e4250b41be736e0a09c5b','withdraw','N',3,false);
INSERT INTO wf_element_data VALUES ('3cc34436df104c9eaf6852f52d0ad8a2','target-document-list','4028b21f7c90d996017c91aeff1b0050',0,true);
INSERT INTO wf_element_data VALUES ('4be94f828adb4b5a938b82a25feca589','assignee-type','assignee.type.candidate.groups',0,true);
INSERT INTO wf_element_data VALUES ('4be94f828adb4b5a938b82a25feca589','assignee','serviceDesk.assignee',1,true);
INSERT INTO wf_element_data VALUES ('4be94f828adb4b5a938b82a25feca589','reject-id','',2,false);
INSERT INTO wf_element_data VALUES ('4be94f828adb4b5a938b82a25feca589','withdraw','N',3,false);
INSERT INTO wf_element_data VALUES ('536974f7f4484443acd76b5bd80fc159','action-name','승인',0,false);
INSERT INTO wf_element_data VALUES ('536974f7f4484443acd76b5bd80fc159','action-value','progress',1,false);
INSERT INTO wf_element_data VALUES ('536974f7f4484443acd76b5bd80fc159','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('536974f7f4484443acd76b5bd80fc159','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('536974f7f4484443acd76b5bd80fc159','start-id','337ab138ae9e4250b41be736e0a09c5b',4,true);
INSERT INTO wf_element_data VALUES ('536974f7f4484443acd76b5bd80fc159','start-name','승인',5,false);
INSERT INTO wf_element_data VALUES ('536974f7f4484443acd76b5bd80fc159','end-id','3cc34436df104c9eaf6852f52d0ad8a2',6,true);
INSERT INTO wf_element_data VALUES ('536974f7f4484443acd76b5bd80fc159','end-name','만족도 평가',7,false);
INSERT INTO wf_element_data VALUES ('53be2caebd5e40e0b6e9ebecce3f16bd','assignee','z-processor',0,true);
INSERT INTO wf_element_data VALUES ('53be2caebd5e40e0b6e9ebecce3f16bd','assignee-type','assignee.type.assignee',1,true);
INSERT INTO wf_element_data VALUES ('53be2caebd5e40e0b6e9ebecce3f16bd','reject-id','',2,false);
INSERT INTO wf_element_data VALUES ('53be2caebd5e40e0b6e9ebecce3f16bd','withdraw','N',3,false);
INSERT INTO wf_element_data VALUES ('64c845635dfe43be8fba233999327cee','action-name','접수',0,false);
INSERT INTO wf_element_data VALUES ('64c845635dfe43be8fba233999327cee','action-value','progress',1,false);
INSERT INTO wf_element_data VALUES ('64c845635dfe43be8fba233999327cee','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('64c845635dfe43be8fba233999327cee','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('64c845635dfe43be8fba233999327cee','start-id','4be94f828adb4b5a938b82a25feca589',4,true);
INSERT INTO wf_element_data VALUES ('64c845635dfe43be8fba233999327cee','start-name','신청서 검토',5,false);
INSERT INTO wf_element_data VALUES ('64c845635dfe43be8fba233999327cee','end-id','eba7310bd0dd41cfb88622e8f8e00767',6,true);
INSERT INTO wf_element_data VALUES ('64c845635dfe43be8fba233999327cee','end-name','접수',7,false);
INSERT INTO wf_element_data VALUES ('6c42da85993f4ae9b551ef67b15c5d49','action-name','승인요청',0,false);
INSERT INTO wf_element_data VALUES ('6c42da85993f4ae9b551ef67b15c5d49','action-value','progress',1,false);
INSERT INTO wf_element_data VALUES ('6c42da85993f4ae9b551ef67b15c5d49','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('6c42da85993f4ae9b551ef67b15c5d49','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('6c42da85993f4ae9b551ef67b15c5d49','start-id','53be2caebd5e40e0b6e9ebecce3f16bd',4,true);
INSERT INTO wf_element_data VALUES ('6c42da85993f4ae9b551ef67b15c5d49','start-name','처리',5,false);
INSERT INTO wf_element_data VALUES ('6c42da85993f4ae9b551ef67b15c5d49','end-id','337ab138ae9e4250b41be736e0a09c5b',6,true);
INSERT INTO wf_element_data VALUES ('6c42da85993f4ae9b551ef67b15c5d49','end-name','승인',7,false);
INSERT INTO wf_element_data VALUES ('9c7c235aa4eb43d8a912b2e524264c79','assignee-type','assignee.type.candidate.groups',0,true);
INSERT INTO wf_element_data VALUES ('9c7c235aa4eb43d8a912b2e524264c79','assignee','users.general',1,true);
INSERT INTO wf_element_data VALUES ('9c7c235aa4eb43d8a912b2e524264c79','reject-id','',2,false);
INSERT INTO wf_element_data VALUES ('9c7c235aa4eb43d8a912b2e524264c79','withdraw','N',3,false);
INSERT INTO wf_element_data VALUES ('a727237e2c6f9dbdbbff693ed151c85d','action-name','신청서 등록',0,false);
INSERT INTO wf_element_data VALUES ('a727237e2c6f9dbdbbff693ed151c85d','action-value','progress',1,false);
INSERT INTO wf_element_data VALUES ('a727237e2c6f9dbdbbff693ed151c85d','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('a727237e2c6f9dbdbbff693ed151c85d','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('a727237e2c6f9dbdbbff693ed151c85d','start-id','9c7c235aa4eb43d8a912b2e524264c79',4,true);
INSERT INTO wf_element_data VALUES ('a727237e2c6f9dbdbbff693ed151c85d','start-name','신청서 작성',5,false);
INSERT INTO wf_element_data VALUES ('a727237e2c6f9dbdbbff693ed151c85d','end-id','4be94f828adb4b5a938b82a25feca589',6,true);
INSERT INTO wf_element_data VALUES ('a727237e2c6f9dbdbbff693ed151c85d','end-name','신청서 검토',7,false);
INSERT INTO wf_element_data VALUES ('b579885ee8094ab7989916905212ca03','action-name','',0,false);
INSERT INTO wf_element_data VALUES ('b579885ee8094ab7989916905212ca03','action-value','',1,false);
INSERT INTO wf_element_data VALUES ('b579885ee8094ab7989916905212ca03','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('b579885ee8094ab7989916905212ca03','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('b579885ee8094ab7989916905212ca03','start-id','670f8e3514c54a429b0d04897907fa45',4,true);
INSERT INTO wf_element_data VALUES ('b579885ee8094ab7989916905212ca03','start-name','시작',5,false);
INSERT INTO wf_element_data VALUES ('b579885ee8094ab7989916905212ca03','end-id','9c7c235aa4eb43d8a912b2e524264c79',6,true);
INSERT INTO wf_element_data VALUES ('b579885ee8094ab7989916905212ca03','end-name','신청서 작성',7,false);
INSERT INTO wf_element_data VALUES ('c731ce831af64b798fcbd074d5d932be','action-name','',0,false);
INSERT INTO wf_element_data VALUES ('c731ce831af64b798fcbd074d5d932be','action-value','',1,false);
INSERT INTO wf_element_data VALUES ('c731ce831af64b798fcbd074d5d932be','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('c731ce831af64b798fcbd074d5d932be','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('c731ce831af64b798fcbd074d5d932be','start-id','eba7310bd0dd41cfb88622e8f8e00767',4,true);
INSERT INTO wf_element_data VALUES ('c731ce831af64b798fcbd074d5d932be','start-name','접수',5,false);
INSERT INTO wf_element_data VALUES ('c731ce831af64b798fcbd074d5d932be','end-id','53be2caebd5e40e0b6e9ebecce3f16bd',6,true);
INSERT INTO wf_element_data VALUES ('c731ce831af64b798fcbd074d5d932be','end-name','처리',7,false);
INSERT INTO wf_element_data VALUES ('e577dd3058394669acb40d471d83635e','action-name','',0,false);
INSERT INTO wf_element_data VALUES ('e577dd3058394669acb40d471d83635e','action-value','',1,false);
INSERT INTO wf_element_data VALUES ('e577dd3058394669acb40d471d83635e','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('e577dd3058394669acb40d471d83635e','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('e577dd3058394669acb40d471d83635e','start-id','3cc34436df104c9eaf6852f52d0ad8a2',4,true);
INSERT INTO wf_element_data VALUES ('e577dd3058394669acb40d471d83635e','start-name','만족도 평가',5,false);
INSERT INTO wf_element_data VALUES ('e577dd3058394669acb40d471d83635e','end-id','7a1ec9adb4594335bfbd973c0e35489c',6,true);
INSERT INTO wf_element_data VALUES ('e577dd3058394669acb40d471d83635e','end-name','종료',7,false);
INSERT INTO wf_element_data VALUES ('eba7310bd0dd41cfb88622e8f8e00767','complete-action','',0,false);
/* 서비스데스크 - 단순문의 - 만족도 */
INSERT INTO wf_element_data VALUES ('00d3cbc448594f90a91448a7eef93dcb','assignee-type','assignee.type.assignee',0,true);
INSERT INTO wf_element_data VALUES ('00d3cbc448594f90a91448a7eef93dcb','assignee','z-requester',1,true);
INSERT INTO wf_element_data VALUES ('00d3cbc448594f90a91448a7eef93dcb','reject-id','',2,false);
INSERT INTO wf_element_data VALUES ('00d3cbc448594f90a91448a7eef93dcb','withdraw','N',3,false);
INSERT INTO wf_element_data VALUES ('71ac5aa1ed1142deb364bbc9bea88cca','action-name','만족도 평가',0,false);
INSERT INTO wf_element_data VALUES ('71ac5aa1ed1142deb364bbc9bea88cca','action-value','progress',1,false);
INSERT INTO wf_element_data VALUES ('71ac5aa1ed1142deb364bbc9bea88cca','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('71ac5aa1ed1142deb364bbc9bea88cca','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('71ac5aa1ed1142deb364bbc9bea88cca','start-id','00d3cbc448594f90a91448a7eef93dcb',4,true);
INSERT INTO wf_element_data VALUES ('71ac5aa1ed1142deb364bbc9bea88cca','start-name','만족도 평가',5,false);
INSERT INTO wf_element_data VALUES ('71ac5aa1ed1142deb364bbc9bea88cca','end-id','2b4667523f6445ab835e2b5135ddfecd',6,true);
INSERT INTO wf_element_data VALUES ('71ac5aa1ed1142deb364bbc9bea88cca','end-name','종료',7,false);
INSERT INTO wf_element_data VALUES ('fca1181323704662acc1c234886be58e','action-name','',0,false);
INSERT INTO wf_element_data VALUES ('fca1181323704662acc1c234886be58e','action-value','',1,false);
INSERT INTO wf_element_data VALUES ('fca1181323704662acc1c234886be58e','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('fca1181323704662acc1c234886be58e','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('fca1181323704662acc1c234886be58e','start-id','2a85de200cc44f17a540d9f2ccea6c97',4,true);
INSERT INTO wf_element_data VALUES ('fca1181323704662acc1c234886be58e','start-name','시작',5,false);
INSERT INTO wf_element_data VALUES ('fca1181323704662acc1c234886be58e','end-id','00d3cbc448594f90a91448a7eef93dcb',6,true);
INSERT INTO wf_element_data VALUES ('fca1181323704662acc1c234886be58e','end-name','만족도 평가',7,false);
/* 서비스데스크 - 장애신고 */
INSERT INTO wf_element_data VALUES ('a03d8919f63a4e99e0ace283a7119717','action-name','',0,false);
INSERT INTO wf_element_data VALUES ('a03d8919f63a4e99e0ace283a7119717','action-value','',1,false);
INSERT INTO wf_element_data VALUES ('a03d8919f63a4e99e0ace283a7119717','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('a03d8919f63a4e99e0ace283a7119717','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('a03d8919f63a4e99e0ace283a7119717','start-id','aa57399fa79e38fd8e59a3a1a11e56d2',4,true);
INSERT INTO wf_element_data VALUES ('a03d8919f63a4e99e0ace283a7119717','start-name','',5,false);
INSERT INTO wf_element_data VALUES ('a03d8919f63a4e99e0ace283a7119717','end-id','aa900688d722cd451b47e72dda93fca8',6,true);
INSERT INTO wf_element_data VALUES ('a03d8919f63a4e99e0ace283a7119717','end-name','승인요청',7,false);
INSERT INTO wf_element_data VALUES ('a117f0729b2cd106fa00b36f12fe1cfb','assignee-type','assignee.type.candidate.groups',0,true);
INSERT INTO wf_element_data VALUES ('a117f0729b2cd106fa00b36f12fe1cfb','assignee','serviceDesk.assignee',1,true);
INSERT INTO wf_element_data VALUES ('a117f0729b2cd106fa00b36f12fe1cfb','reject-id','',2,false);
INSERT INTO wf_element_data VALUES ('a117f0729b2cd106fa00b36f12fe1cfb','withdraw','N',3,false);
INSERT INTO wf_element_data VALUES ('a13cd52a4338bde9db939249fcb722f7','action-name','',0,false);
INSERT INTO wf_element_data VALUES ('a13cd52a4338bde9db939249fcb722f7','action-value','',1,false);
INSERT INTO wf_element_data VALUES ('a13cd52a4338bde9db939249fcb722f7','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('a13cd52a4338bde9db939249fcb722f7','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('a13cd52a4338bde9db939249fcb722f7','start-id','a7f0ad953c834f779aac354f97b84eb2',4,true);
INSERT INTO wf_element_data VALUES ('a13cd52a4338bde9db939249fcb722f7','start-name','만족도 평가',5,false);
INSERT INTO wf_element_data VALUES ('a13cd52a4338bde9db939249fcb722f7','end-id','a7acf388af93de92a17b7068f846c073',6,true);
INSERT INTO wf_element_data VALUES ('a13cd52a4338bde9db939249fcb722f7','end-name','종료',7,false);
INSERT INTO wf_element_data VALUES ('a1c6c91a6dd476ae8ffb5e0c7e9c0d53','assignee-type','assignee.type.candidate.groups',0,true);
INSERT INTO wf_element_data VALUES ('a1c6c91a6dd476ae8ffb5e0c7e9c0d53','assignee','users.general',1,true);
INSERT INTO wf_element_data VALUES ('a1c6c91a6dd476ae8ffb5e0c7e9c0d53','reject-id','',2,false);
INSERT INTO wf_element_data VALUES ('a1c6c91a6dd476ae8ffb5e0c7e9c0d53','withdraw','N',3,false);
INSERT INTO wf_element_data VALUES ('a271b9f3a790280811cd6a6ff94c02e1','action-name','',0,false);
INSERT INTO wf_element_data VALUES ('a271b9f3a790280811cd6a6ff94c02e1','action-value','',1,false);
INSERT INTO wf_element_data VALUES ('a271b9f3a790280811cd6a6ff94c02e1','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('a271b9f3a790280811cd6a6ff94c02e1','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('a271b9f3a790280811cd6a6ff94c02e1','start-id','a4bf5389f404c756d957b40cd7a1bf10',4,true);
INSERT INTO wf_element_data VALUES ('a271b9f3a790280811cd6a6ff94c02e1','start-name','작업 결과 보고',5,false);
INSERT INTO wf_element_data VALUES ('a271b9f3a790280811cd6a6ff94c02e1','end-id','aa57399fa79e38fd8e59a3a1a11e56d2',6,true);
INSERT INTO wf_element_data VALUES ('a271b9f3a790280811cd6a6ff94c02e1','end-name','',7,false);
INSERT INTO wf_element_data VALUES ('a296d2e534c0c2fe97071cdc9acfeb80','sub-document-id','',0,true);
INSERT INTO wf_element_data VALUES ('a2a91cf26c14dc98cbc1dfd9ac433477','action-name','',0,false);
INSERT INTO wf_element_data VALUES ('a2a91cf26c14dc98cbc1dfd9ac433477','action-value','',1,false);
INSERT INTO wf_element_data VALUES ('a2a91cf26c14dc98cbc1dfd9ac433477','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('a2a91cf26c14dc98cbc1dfd9ac433477','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('a2a91cf26c14dc98cbc1dfd9ac433477','start-id','a109f703f8258c01c71fd7a2c6917225',4,true);
INSERT INTO wf_element_data VALUES ('a2a91cf26c14dc98cbc1dfd9ac433477','start-name','시작',5,false);
INSERT INTO wf_element_data VALUES ('a2a91cf26c14dc98cbc1dfd9ac433477','end-id','a1c6c91a6dd476ae8ffb5e0c7e9c0d53',6,true);
INSERT INTO wf_element_data VALUES ('a2a91cf26c14dc98cbc1dfd9ac433477','end-name','신청서 작성',7,false);
INSERT INTO wf_element_data VALUES ('a2c92c99371e885e53bc0b3ae65b8ac8','action-name','',0,false);
INSERT INTO wf_element_data VALUES ('a2c92c99371e885e53bc0b3ae65b8ac8','action-value','',1,false);
INSERT INTO wf_element_data VALUES ('a2c92c99371e885e53bc0b3ae65b8ac8','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('a2c92c99371e885e53bc0b3ae65b8ac8','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('a2c92c99371e885e53bc0b3ae65b8ac8','start-id','aea0ee0f94c686307c87a5c86699ef86',4,true);
INSERT INTO wf_element_data VALUES ('a2c92c99371e885e53bc0b3ae65b8ac8','start-name','처리',5,false);
INSERT INTO wf_element_data VALUES ('a2c92c99371e885e53bc0b3ae65b8ac8','end-id','aa104e687c0f220966d493707a87def1',6,true);
INSERT INTO wf_element_data VALUES ('a2c92c99371e885e53bc0b3ae65b8ac8','end-name','',7,false);
INSERT INTO wf_element_data VALUES ('a45089565ee86a40cfcc16070d6055a4','action-name','',0,false);
INSERT INTO wf_element_data VALUES ('a45089565ee86a40cfcc16070d6055a4','action-value','',1,false);
INSERT INTO wf_element_data VALUES ('a45089565ee86a40cfcc16070d6055a4','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('a45089565ee86a40cfcc16070d6055a4','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('a45089565ee86a40cfcc16070d6055a4','start-id','aeefd9f38872ec0907d42c401dc8b71c',4,true);
INSERT INTO wf_element_data VALUES ('a45089565ee86a40cfcc16070d6055a4','start-name','접수',5,false);
INSERT INTO wf_element_data VALUES ('a45089565ee86a40cfcc16070d6055a4','end-id','aea0ee0f94c686307c87a5c86699ef86',6,true);
INSERT INTO wf_element_data VALUES ('a45089565ee86a40cfcc16070d6055a4','end-name','처리',7,false);
INSERT INTO wf_element_data VALUES ('a4bf5389f404c756d957b40cd7a1bf10','complete-action','',0,false);
INSERT INTO wf_element_data VALUES ('a7f0ad953c834f779aac354f97b84eb2','target-document-list','4028b21f7c9b6b1e017c9bedbe8a0012',0,true);
INSERT INTO wf_element_data VALUES ('a7f615029e8be83d8008d2012454e1f4','action-name','자체 처리',0,false);
INSERT INTO wf_element_data VALUES ('a7f615029e8be83d8008d2012454e1f4','action-value','self_complete',1,false);
INSERT INTO wf_element_data VALUES ('a7f615029e8be83d8008d2012454e1f4','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('a7f615029e8be83d8008d2012454e1f4','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('a7f615029e8be83d8008d2012454e1f4','start-id','aa104e687c0f220966d493707a87def1',4,true);
INSERT INTO wf_element_data VALUES ('a7f615029e8be83d8008d2012454e1f4','start-name','',5,false);
INSERT INTO wf_element_data VALUES ('a7f615029e8be83d8008d2012454e1f4','end-id','a4bf5389f404c756d957b40cd7a1bf10',6,true);
INSERT INTO wf_element_data VALUES ('a7f615029e8be83d8008d2012454e1f4','end-name','작업 결과 보고',7,false);
INSERT INTO wf_element_data VALUES ('a84a4bafdc3b3f1928a0677434d34feb','assignee-type','assignee.type.assignee',0,true);
INSERT INTO wf_element_data VALUES ('a84a4bafdc3b3f1928a0677434d34feb','assignee','z-approver',1,true);
INSERT INTO wf_element_data VALUES ('a84a4bafdc3b3f1928a0677434d34feb','reject-id','aea0ee0f94c686307c87a5c86699ef86',2,false);
INSERT INTO wf_element_data VALUES ('a84a4bafdc3b3f1928a0677434d34feb','withdraw','N',3,false);
INSERT INTO wf_element_data VALUES ('aa104e687c0f220966d493707a87def1','condition-item','#{action}',0,true);
INSERT INTO wf_element_data VALUES ('aa57399fa79e38fd8e59a3a1a11e56d2','condition-item','#{action}',0,true);
INSERT INTO wf_element_data VALUES ('aa900688d722cd451b47e72dda93fca8','assignee-type','assignee.type.assignee',0,true);
INSERT INTO wf_element_data VALUES ('aa900688d722cd451b47e72dda93fca8','assignee','z-processor',1,true);
INSERT INTO wf_element_data VALUES ('aa900688d722cd451b47e72dda93fca8','reject-id','',2,false);
INSERT INTO wf_element_data VALUES ('aa900688d722cd451b47e72dda93fca8','withdraw','N',3,false);
INSERT INTO wf_element_data VALUES ('ab3d8234ca2ab3b44934df41563f067d','action-name','접수',0,false);
INSERT INTO wf_element_data VALUES ('ab3d8234ca2ab3b44934df41563f067d','action-value','progress',1,false);
INSERT INTO wf_element_data VALUES ('ab3d8234ca2ab3b44934df41563f067d','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('ab3d8234ca2ab3b44934df41563f067d','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('ab3d8234ca2ab3b44934df41563f067d','start-id','a117f0729b2cd106fa00b36f12fe1cfb',4,true);
INSERT INTO wf_element_data VALUES ('ab3d8234ca2ab3b44934df41563f067d','start-name','신청서 검토',5,false);
INSERT INTO wf_element_data VALUES ('ab3d8234ca2ab3b44934df41563f067d','end-id','aeefd9f38872ec0907d42c401dc8b71c',6,true);
INSERT INTO wf_element_data VALUES ('ab3d8234ca2ab3b44934df41563f067d','end-name','접수',7,false);
INSERT INTO wf_element_data VALUES ('ac6b5d8653e21191f1f0ac59136a5297','action-name','',0,false);
INSERT INTO wf_element_data VALUES ('ac6b5d8653e21191f1f0ac59136a5297','action-value','',1,false);
INSERT INTO wf_element_data VALUES ('ac6b5d8653e21191f1f0ac59136a5297','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('ac6b5d8653e21191f1f0ac59136a5297','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('ac6b5d8653e21191f1f0ac59136a5297','start-id','a296d2e534c0c2fe97071cdc9acfeb80',4,true);
INSERT INTO wf_element_data VALUES ('ac6b5d8653e21191f1f0ac59136a5297','start-name','장애 이관',5,false);
INSERT INTO wf_element_data VALUES ('ac6b5d8653e21191f1f0ac59136a5297','end-id','aa57399fa79e38fd8e59a3a1a11e56d2',6,true);
INSERT INTO wf_element_data VALUES ('ac6b5d8653e21191f1f0ac59136a5297','end-name','',7,false);
INSERT INTO wf_element_data VALUES ('ae39db34c9022d8fa2e101b32a5cb860','action-name','승인요청',0,false);
INSERT INTO wf_element_data VALUES ('ae39db34c9022d8fa2e101b32a5cb860','action-value','progress',1,false);
INSERT INTO wf_element_data VALUES ('ae39db34c9022d8fa2e101b32a5cb860','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('ae39db34c9022d8fa2e101b32a5cb860','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('ae39db34c9022d8fa2e101b32a5cb860','start-id','aa900688d722cd451b47e72dda93fca8',4,true);
INSERT INTO wf_element_data VALUES ('ae39db34c9022d8fa2e101b32a5cb860','start-name','승인요청',5,false);
INSERT INTO wf_element_data VALUES ('ae39db34c9022d8fa2e101b32a5cb860','end-id','a84a4bafdc3b3f1928a0677434d34feb',6,true);
INSERT INTO wf_element_data VALUES ('ae39db34c9022d8fa2e101b32a5cb860','end-name','승인',7,false);
INSERT INTO wf_element_data VALUES ('ae5f5ef48e6ad78e8351b93017c565c9','action-name','승인',0,false);
INSERT INTO wf_element_data VALUES ('ae5f5ef48e6ad78e8351b93017c565c9','action-value','progress',1,false);
INSERT INTO wf_element_data VALUES ('ae5f5ef48e6ad78e8351b93017c565c9','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('ae5f5ef48e6ad78e8351b93017c565c9','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('ae5f5ef48e6ad78e8351b93017c565c9','start-id','a84a4bafdc3b3f1928a0677434d34feb',4,true);
INSERT INTO wf_element_data VALUES ('ae5f5ef48e6ad78e8351b93017c565c9','start-name','승인',5,false);
INSERT INTO wf_element_data VALUES ('ae5f5ef48e6ad78e8351b93017c565c9','end-id','a7f0ad953c834f779aac354f97b84eb2',6,true);
INSERT INTO wf_element_data VALUES ('ae5f5ef48e6ad78e8351b93017c565c9','end-name','만족도 평가',7,false);
INSERT INTO wf_element_data VALUES ('aea0ee0f94c686307c87a5c86699ef86','assignee-type','assignee.type.assignee',0,true);
INSERT INTO wf_element_data VALUES ('aea0ee0f94c686307c87a5c86699ef86','assignee','z-processor',1,true);
INSERT INTO wf_element_data VALUES ('aea0ee0f94c686307c87a5c86699ef86','reject-id','',2,false);
INSERT INTO wf_element_data VALUES ('aea0ee0f94c686307c87a5c86699ef86','withdraw','N',3,false);
INSERT INTO wf_element_data VALUES ('aeefd9f38872ec0907d42c401dc8b71c','complete-action','',0,false);
INSERT INTO wf_element_data VALUES ('af4ce9dee1335cb46a3c127d27267bb1','action-name','신청서 등록',0,false);
INSERT INTO wf_element_data VALUES ('af4ce9dee1335cb46a3c127d27267bb1','action-value','progress',1,false);
INSERT INTO wf_element_data VALUES ('af4ce9dee1335cb46a3c127d27267bb1','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('af4ce9dee1335cb46a3c127d27267bb1','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('af4ce9dee1335cb46a3c127d27267bb1','start-id','a1c6c91a6dd476ae8ffb5e0c7e9c0d53',4,true);
INSERT INTO wf_element_data VALUES ('af4ce9dee1335cb46a3c127d27267bb1','start-name','신청서 작성',5,false);
INSERT INTO wf_element_data VALUES ('af4ce9dee1335cb46a3c127d27267bb1','end-id','a117f0729b2cd106fa00b36f12fe1cfb',6,true);
INSERT INTO wf_element_data VALUES ('af4ce9dee1335cb46a3c127d27267bb1','end-name','신청서 검토',7,false);
INSERT INTO wf_element_data VALUES ('af59396a9ffd08eee3efb2d0f0a4fd21','action-name','장애 이관',0,false);
INSERT INTO wf_element_data VALUES ('af59396a9ffd08eee3efb2d0f0a4fd21','action-value','incident',1,false);
INSERT INTO wf_element_data VALUES ('af59396a9ffd08eee3efb2d0f0a4fd21','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('af59396a9ffd08eee3efb2d0f0a4fd21','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('af59396a9ffd08eee3efb2d0f0a4fd21','start-id','aa104e687c0f220966d493707a87def1',4,true);
INSERT INTO wf_element_data VALUES ('af59396a9ffd08eee3efb2d0f0a4fd21','start-name','',5,false);
INSERT INTO wf_element_data VALUES ('af59396a9ffd08eee3efb2d0f0a4fd21','end-id','a296d2e534c0c2fe97071cdc9acfeb80',6,true);
INSERT INTO wf_element_data VALUES ('af59396a9ffd08eee3efb2d0f0a4fd21','end-name','장애 이관',7,false);
/* 서비스데스크 - 장애신고 - 만족도 */
INSERT INTO wf_element_data VALUES ('36dad342725848f7ab1453e309ef0aa1','assignee-type','assignee.type.assignee',0,true);
INSERT INTO wf_element_data VALUES ('36dad342725848f7ab1453e309ef0aa1','assignee','z-requester',1,true);
INSERT INTO wf_element_data VALUES ('36dad342725848f7ab1453e309ef0aa1','reject-id','',2,false);
INSERT INTO wf_element_data VALUES ('36dad342725848f7ab1453e309ef0aa1','withdraw','N',3,false);
INSERT INTO wf_element_data VALUES ('8d12225883ea4eb3bf2d137c67278eb8','action-name','',0,false);
INSERT INTO wf_element_data VALUES ('8d12225883ea4eb3bf2d137c67278eb8','action-value','',1,false);
INSERT INTO wf_element_data VALUES ('8d12225883ea4eb3bf2d137c67278eb8','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('8d12225883ea4eb3bf2d137c67278eb8','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('8d12225883ea4eb3bf2d137c67278eb8','start-id','997d5b3c3246420181a774069b489909',4,true);
INSERT INTO wf_element_data VALUES ('8d12225883ea4eb3bf2d137c67278eb8','start-name','시작',5,false);
INSERT INTO wf_element_data VALUES ('8d12225883ea4eb3bf2d137c67278eb8','end-id','36dad342725848f7ab1453e309ef0aa1',6,true);
INSERT INTO wf_element_data VALUES ('8d12225883ea4eb3bf2d137c67278eb8','end-name','만족도 평가',7,false);
INSERT INTO wf_element_data VALUES ('a9eac2d7b40b4ea880926a38379f3ddd','action-name','만족도 평가',0,false);
INSERT INTO wf_element_data VALUES ('a9eac2d7b40b4ea880926a38379f3ddd','action-value','progress',1,false);
INSERT INTO wf_element_data VALUES ('a9eac2d7b40b4ea880926a38379f3ddd','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('a9eac2d7b40b4ea880926a38379f3ddd','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('a9eac2d7b40b4ea880926a38379f3ddd','start-id','36dad342725848f7ab1453e309ef0aa1',4,true);
INSERT INTO wf_element_data VALUES ('a9eac2d7b40b4ea880926a38379f3ddd','start-name','만족도 평가',5,false);
INSERT INTO wf_element_data VALUES ('a9eac2d7b40b4ea880926a38379f3ddd','end-id','2f3712ca2ea34ae1800179f2dd7f6424',6,true);
INSERT INTO wf_element_data VALUES ('a9eac2d7b40b4ea880926a38379f3ddd','end-name','종료',7,false);
/* 서비스데스크 - 서비스요청 */
INSERT INTO wf_element_data VALUES ('5cca217b673542e1ad086a67777ef28e','start-id','f4f0bfcfdb3e406e9eec224d79c10ee6',4,true);
INSERT INTO wf_element_data VALUES ('5cca217b673542e1ad086a67777ef28e','start-name','접수',5,false);
INSERT INTO wf_element_data VALUES ('5cca217b673542e1ad086a67777ef28e','end-id','0c98f0917c7347418933aab46b8b7823',6,true);
INSERT INTO wf_element_data VALUES ('5cca217b673542e1ad086a67777ef28e','end-name','처리',7,false);
INSERT INTO wf_element_data VALUES ('5e98b781b4e74d85ac90e6703cd052a6','action-name','',0,false);
INSERT INTO wf_element_data VALUES ('5e98b781b4e74d85ac90e6703cd052a6','action-value','',1,false);
INSERT INTO wf_element_data VALUES ('5e98b781b4e74d85ac90e6703cd052a6','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('5e98b781b4e74d85ac90e6703cd052a6','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('5e98b781b4e74d85ac90e6703cd052a6','start-id','8b962df9ad894d3283ac188d8759267b',4,true);
INSERT INTO wf_element_data VALUES ('04195a896b4a4c5f8c2e8d221c536bc8','assignee-type','assignee.type.candidate.groups',0,true);
INSERT INTO wf_element_data VALUES ('04195a896b4a4c5f8c2e8d221c536bc8','assignee','serviceDesk.assignee',1,true);
INSERT INTO wf_element_data VALUES ('04195a896b4a4c5f8c2e8d221c536bc8','reject-id','',2,false);
INSERT INTO wf_element_data VALUES ('04195a896b4a4c5f8c2e8d221c536bc8','withdraw','N',3,false);
INSERT INTO wf_element_data VALUES ('095e98b2596a4019ae25c01de033aed3','complete-action','',0,false);
INSERT INTO wf_element_data VALUES ('0c98f0917c7347418933aab46b8b7823','assignee-type','assignee.type.assignee',0,true);
INSERT INTO wf_element_data VALUES ('0c98f0917c7347418933aab46b8b7823','assignee','z-processor',1,true);
INSERT INTO wf_element_data VALUES ('0c98f0917c7347418933aab46b8b7823','reject-id','',2,false);
INSERT INTO wf_element_data VALUES ('0c98f0917c7347418933aab46b8b7823','withdraw','N',3,false);
INSERT INTO wf_element_data VALUES ('24372ebb7e174fca99ae500d05aed4b5','sub-document-id','',0,true);
INSERT INTO wf_element_data VALUES ('447aceb93c124909954082bfed2cc74f','action-name','',0,false);
INSERT INTO wf_element_data VALUES ('447aceb93c124909954082bfed2cc74f','action-value','',1,false);
INSERT INTO wf_element_data VALUES ('447aceb93c124909954082bfed2cc74f','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('447aceb93c124909954082bfed2cc74f','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('447aceb93c124909954082bfed2cc74f','start-id','0c98f0917c7347418933aab46b8b7823',4,true);
INSERT INTO wf_element_data VALUES ('447aceb93c124909954082bfed2cc74f','start-name','처리',5,false);
INSERT INTO wf_element_data VALUES ('447aceb93c124909954082bfed2cc74f','end-id','5fe9d50a699f4d389ee702135c67d7b7',6,true);
INSERT INTO wf_element_data VALUES ('447aceb93c124909954082bfed2cc74f','end-name','',7,false);
INSERT INTO wf_element_data VALUES ('5c2b8645d1994bf683c4c51e08e2932a','action-name','신청서 등록',0,false);
INSERT INTO wf_element_data VALUES ('5c2b8645d1994bf683c4c51e08e2932a','action-value','progress',1,false);
INSERT INTO wf_element_data VALUES ('5c2b8645d1994bf683c4c51e08e2932a','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('5c2b8645d1994bf683c4c51e08e2932a','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('5c2b8645d1994bf683c4c51e08e2932a','start-id','cc4048cd712147b8af4016e647e69b14',4,true);
INSERT INTO wf_element_data VALUES ('5c2b8645d1994bf683c4c51e08e2932a','start-name','신청서 작성',5,false);
INSERT INTO wf_element_data VALUES ('5c2b8645d1994bf683c4c51e08e2932a','end-id','04195a896b4a4c5f8c2e8d221c536bc8',6,true);
INSERT INTO wf_element_data VALUES ('5c2b8645d1994bf683c4c51e08e2932a','end-name','신청서 검토',7,false);
INSERT INTO wf_element_data VALUES ('5cca217b673542e1ad086a67777ef28e','action-name','',0,false);
INSERT INTO wf_element_data VALUES ('5cca217b673542e1ad086a67777ef28e','action-value','',1,false);
INSERT INTO wf_element_data VALUES ('5cca217b673542e1ad086a67777ef28e','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('5cca217b673542e1ad086a67777ef28e','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('5e98b781b4e74d85ac90e6703cd052a6','start-name','시작',5,false);
INSERT INTO wf_element_data VALUES ('5e98b781b4e74d85ac90e6703cd052a6','end-id','cc4048cd712147b8af4016e647e69b14',6,true);
INSERT INTO wf_element_data VALUES ('5e98b781b4e74d85ac90e6703cd052a6','end-name','신청서 작성',7,false);
INSERT INTO wf_element_data VALUES ('5fe9d50a699f4d389ee702135c67d7b7','condition-item','#{action}',0,true);
INSERT INTO wf_element_data VALUES ('6774c1370d614d47bb3b9b0ec33b9c10','assignee-type','assignee.type.assignee',0,true);
INSERT INTO wf_element_data VALUES ('6774c1370d614d47bb3b9b0ec33b9c10','assignee','z-approver',1,true);
INSERT INTO wf_element_data VALUES ('6774c1370d614d47bb3b9b0ec33b9c10','reject-id','0c98f0917c7347418933aab46b8b7823',2,false);
INSERT INTO wf_element_data VALUES ('6774c1370d614d47bb3b9b0ec33b9c10','withdraw','N',3,false);
INSERT INTO wf_element_data VALUES ('6eb7ec88b9cf4d3384d0f873b016a76b','action-name','인프라 변경 이관',0,false);
INSERT INTO wf_element_data VALUES ('6eb7ec88b9cf4d3384d0f873b016a76b','action-value','infra',1,false);
INSERT INTO wf_element_data VALUES ('6eb7ec88b9cf4d3384d0f873b016a76b','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('6eb7ec88b9cf4d3384d0f873b016a76b','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('6eb7ec88b9cf4d3384d0f873b016a76b','start-id','5fe9d50a699f4d389ee702135c67d7b7',4,true);
INSERT INTO wf_element_data VALUES ('6eb7ec88b9cf4d3384d0f873b016a76b','start-name','',5,false);
INSERT INTO wf_element_data VALUES ('6eb7ec88b9cf4d3384d0f873b016a76b','end-id','9cde4a46cd8c42d8af2d45ba77721091',6,true);
INSERT INTO wf_element_data VALUES ('6eb7ec88b9cf4d3384d0f873b016a76b','end-name','인프라 변경 이관',7,false);
INSERT INTO wf_element_data VALUES ('705f3c898c3e4b2f8ea838e1b7a1b72f','action-name','접수',0,false);
INSERT INTO wf_element_data VALUES ('705f3c898c3e4b2f8ea838e1b7a1b72f','action-value','progress',1,false);
INSERT INTO wf_element_data VALUES ('705f3c898c3e4b2f8ea838e1b7a1b72f','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('705f3c898c3e4b2f8ea838e1b7a1b72f','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('705f3c898c3e4b2f8ea838e1b7a1b72f','start-id','04195a896b4a4c5f8c2e8d221c536bc8',4,true);
INSERT INTO wf_element_data VALUES ('705f3c898c3e4b2f8ea838e1b7a1b72f','start-name','신청서 검토',5,false);
INSERT INTO wf_element_data VALUES ('705f3c898c3e4b2f8ea838e1b7a1b72f','end-id','f4f0bfcfdb3e406e9eec224d79c10ee6',6,true);
INSERT INTO wf_element_data VALUES ('705f3c898c3e4b2f8ea838e1b7a1b72f','end-name','접수',7,false);
INSERT INTO wf_element_data VALUES ('73d8d9a35d7947c68d1abaece193585f','action-name','',0,false);
INSERT INTO wf_element_data VALUES ('73d8d9a35d7947c68d1abaece193585f','action-value','',1,false);
INSERT INTO wf_element_data VALUES ('73d8d9a35d7947c68d1abaece193585f','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('73d8d9a35d7947c68d1abaece193585f','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('73d8d9a35d7947c68d1abaece193585f','start-id','9cde4a46cd8c42d8af2d45ba77721091',4,true);
INSERT INTO wf_element_data VALUES ('73d8d9a35d7947c68d1abaece193585f','start-name','인프라 변경 이관',5,false);
INSERT INTO wf_element_data VALUES ('73d8d9a35d7947c68d1abaece193585f','end-id','d4b6e2e20c884104ac36916f803c65f1',6,true);
INSERT INTO wf_element_data VALUES ('73d8d9a35d7947c68d1abaece193585f','end-name','',7,false);
INSERT INTO wf_element_data VALUES ('74a586945aca4bd598297f1f72e2bbd2','target-document-list','4028b21f7c9ff7c8017ca04d16830000',0,true);
INSERT INTO wf_element_data VALUES ('8187b4a4e8e54c3c86c9acdf3726b445','action-name','',0,false);
INSERT INTO wf_element_data VALUES ('8187b4a4e8e54c3c86c9acdf3726b445','action-value','',1,false);
INSERT INTO wf_element_data VALUES ('8187b4a4e8e54c3c86c9acdf3726b445','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('8187b4a4e8e54c3c86c9acdf3726b445','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('8187b4a4e8e54c3c86c9acdf3726b445','start-id','095e98b2596a4019ae25c01de033aed3',4,true);
INSERT INTO wf_element_data VALUES ('8187b4a4e8e54c3c86c9acdf3726b445','start-name','작업 결과 보고',5,false);
INSERT INTO wf_element_data VALUES ('8187b4a4e8e54c3c86c9acdf3726b445','end-id','d4b6e2e20c884104ac36916f803c65f1',6,true);
INSERT INTO wf_element_data VALUES ('8187b4a4e8e54c3c86c9acdf3726b445','end-name','',7,false);
INSERT INTO wf_element_data VALUES ('898c51972e9c45e49646c3797165411d','assignee-type','assignee.type.assignee',0,true);
INSERT INTO wf_element_data VALUES ('898c51972e9c45e49646c3797165411d','assignee','z-processor',1,true);
INSERT INTO wf_element_data VALUES ('898c51972e9c45e49646c3797165411d','reject-id','',2,false);
INSERT INTO wf_element_data VALUES ('898c51972e9c45e49646c3797165411d','withdraw','N',3,false);
INSERT INTO wf_element_data VALUES ('8a2ff6fdb21d4602a2c4c365798469c4','action-name','승인요청',0,false);
INSERT INTO wf_element_data VALUES ('8a2ff6fdb21d4602a2c4c365798469c4','action-value','progress',1,false);
INSERT INTO wf_element_data VALUES ('8a2ff6fdb21d4602a2c4c365798469c4','condition-value','',2,false);
INSERT INTO wf_element_data VALUES ('8a2ff6fdb21d4602a2c4c365798469c4','end-id','6774c1370d614d47bb3b9b0ec33b9c10',3,true);
INSERT INTO wf_element_data VALUES ('8a2ff6fdb21d4602a2c4c365798469c4','end-name','승인',4,false);
INSERT INTO wf_element_data VALUES ('8a2ff6fdb21d4602a2c4c365798469c4','is-default','N',5,false);
INSERT INTO wf_element_data VALUES ('8a2ff6fdb21d4602a2c4c365798469c4','start-id','898c51972e9c45e49646c3797165411d',6,true);
INSERT INTO wf_element_data VALUES ('8a2ff6fdb21d4602a2c4c365798469c4','start-name','승인요청',7,false);
INSERT INTO wf_element_data VALUES ('9cde4a46cd8c42d8af2d45ba77721091','sub-document-id','4028b8817cbfc7a7017cc0e65b260bdf',0,true);
INSERT INTO wf_element_data VALUES ('a71a9701104d441c8b8f73d4afd88f6e','action-name','자체 처리',0,false);
INSERT INTO wf_element_data VALUES ('a71a9701104d441c8b8f73d4afd88f6e','action-value','self_complete',1,false);
INSERT INTO wf_element_data VALUES ('a71a9701104d441c8b8f73d4afd88f6e','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('a71a9701104d441c8b8f73d4afd88f6e','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('a71a9701104d441c8b8f73d4afd88f6e','start-id','5fe9d50a699f4d389ee702135c67d7b7',4,true);
INSERT INTO wf_element_data VALUES ('a71a9701104d441c8b8f73d4afd88f6e','start-name','',5,false);
INSERT INTO wf_element_data VALUES ('a71a9701104d441c8b8f73d4afd88f6e','end-id','095e98b2596a4019ae25c01de033aed3',6,true);
INSERT INTO wf_element_data VALUES ('a71a9701104d441c8b8f73d4afd88f6e','end-name','작업 결과 보고',7,false);
INSERT INTO wf_element_data VALUES ('ade030c7ef0f1914e5b06e2e31f8147a','action-name','',0,false);
INSERT INTO wf_element_data VALUES ('ade030c7ef0f1914e5b06e2e31f8147a','action-value','',1,false);
INSERT INTO wf_element_data VALUES ('ade030c7ef0f1914e5b06e2e31f8147a','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('ade030c7ef0f1914e5b06e2e31f8147a','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('ade030c7ef0f1914e5b06e2e31f8147a','start-id','74a586945aca4bd598297f1f72e2bbd2',4,true);
INSERT INTO wf_element_data VALUES ('ade030c7ef0f1914e5b06e2e31f8147a','start-name','만족도 평가',5,false);
INSERT INTO wf_element_data VALUES ('ade030c7ef0f1914e5b06e2e31f8147a','end-id','9ccf78fb415c4b09b32b65e946f0ef93',6,true);
INSERT INTO wf_element_data VALUES ('ade030c7ef0f1914e5b06e2e31f8147a','end-name','종료',7,false);
INSERT INTO wf_element_data VALUES ('af8674894f8bc525c6f41a482bcf4300','action-name','승인',0,false);
INSERT INTO wf_element_data VALUES ('af8674894f8bc525c6f41a482bcf4300','action-value','progress',1,false);
INSERT INTO wf_element_data VALUES ('af8674894f8bc525c6f41a482bcf4300','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('af8674894f8bc525c6f41a482bcf4300','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('af8674894f8bc525c6f41a482bcf4300','start-id','6774c1370d614d47bb3b9b0ec33b9c10',4,true);
INSERT INTO wf_element_data VALUES ('af8674894f8bc525c6f41a482bcf4300','start-name','승인',5,false);
INSERT INTO wf_element_data VALUES ('af8674894f8bc525c6f41a482bcf4300','end-id','74a586945aca4bd598297f1f72e2bbd2',6,true);
INSERT INTO wf_element_data VALUES ('af8674894f8bc525c6f41a482bcf4300','end-name','만족도 평가',7,false);
INSERT INTO wf_element_data VALUES ('cc4048cd712147b8af4016e647e69b14','assignee-type','assignee.type.candidate.groups',0,true);
INSERT INTO wf_element_data VALUES ('cc4048cd712147b8af4016e647e69b14','assignee','users.general',1,true);
INSERT INTO wf_element_data VALUES ('cc4048cd712147b8af4016e647e69b14','reject-id','',2,false);
INSERT INTO wf_element_data VALUES ('cc4048cd712147b8af4016e647e69b14','withdraw','N',3,false);
INSERT INTO wf_element_data VALUES ('d0d3a5405e974ec8ad00a72a9eaf6f25','action-name','APP 변경 이관',0,false);
INSERT INTO wf_element_data VALUES ('d0d3a5405e974ec8ad00a72a9eaf6f25','action-value','application',1,false);
INSERT INTO wf_element_data VALUES ('d0d3a5405e974ec8ad00a72a9eaf6f25','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('d0d3a5405e974ec8ad00a72a9eaf6f25','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('d0d3a5405e974ec8ad00a72a9eaf6f25','start-id','5fe9d50a699f4d389ee702135c67d7b7',4,true);
INSERT INTO wf_element_data VALUES ('d0d3a5405e974ec8ad00a72a9eaf6f25','start-name','',5,false);
INSERT INTO wf_element_data VALUES ('d0d3a5405e974ec8ad00a72a9eaf6f25','end-id','24372ebb7e174fca99ae500d05aed4b5',6,true);
INSERT INTO wf_element_data VALUES ('d0d3a5405e974ec8ad00a72a9eaf6f25','end-name','APP 변경 이관',7,false);
INSERT INTO wf_element_data VALUES ('d4b6e2e20c884104ac36916f803c65f1','condition-item','#{action}',0,true);
INSERT INTO wf_element_data VALUES ('dd46580c457f49afb05b4d719293d8f5','action-value','',0,false);
INSERT INTO wf_element_data VALUES ('dd46580c457f49afb05b4d719293d8f5','is-default','N',1,false);
INSERT INTO wf_element_data VALUES ('dd46580c457f49afb05b4d719293d8f5','condition-value','',2,false);
INSERT INTO wf_element_data VALUES ('dd46580c457f49afb05b4d719293d8f5','start-id','d4b6e2e20c884104ac36916f803c65f1',3,true);
INSERT INTO wf_element_data VALUES ('dd46580c457f49afb05b4d719293d8f5','start-name','',4,false);
INSERT INTO wf_element_data VALUES ('dd46580c457f49afb05b4d719293d8f5','end-id','898c51972e9c45e49646c3797165411d',5,true);
INSERT INTO wf_element_data VALUES ('dd46580c457f49afb05b4d719293d8f5','end-name','승인요청',6,false);
INSERT INTO wf_element_data VALUES ('dd46580c457f49afb05b4d719293d8f5','action-name','',7,false);
INSERT INTO wf_element_data VALUES ('f4f0bfcfdb3e406e9eec224d79c10ee6','complete-action','',0,false);
INSERT INTO wf_element_data VALUES ('f6c8b24bb46041a889a26a84d53ad401','action-name','',0,false);
INSERT INTO wf_element_data VALUES ('f6c8b24bb46041a889a26a84d53ad401','action-value','',1,false);
INSERT INTO wf_element_data VALUES ('f6c8b24bb46041a889a26a84d53ad401','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('f6c8b24bb46041a889a26a84d53ad401','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('f6c8b24bb46041a889a26a84d53ad401','start-id','24372ebb7e174fca99ae500d05aed4b5',4,true);
INSERT INTO wf_element_data VALUES ('f6c8b24bb46041a889a26a84d53ad401','start-name','APP 변경 이관',5,false);
INSERT INTO wf_element_data VALUES ('f6c8b24bb46041a889a26a84d53ad401','end-id','d4b6e2e20c884104ac36916f803c65f1',6,true);
INSERT INTO wf_element_data VALUES ('f6c8b24bb46041a889a26a84d53ad401','end-name','',7,false);
/* 서비스데스크 - 서비스요청 - 만족도 */
INSERT INTO wf_element_data VALUES ('0f9c64e3371448358e6f39f292c93a46','action-name','만족도 평가',0,false);
INSERT INTO wf_element_data VALUES ('0f9c64e3371448358e6f39f292c93a46','action-value','progress',1,false);
INSERT INTO wf_element_data VALUES ('0f9c64e3371448358e6f39f292c93a46','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('0f9c64e3371448358e6f39f292c93a46','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('0f9c64e3371448358e6f39f292c93a46','start-id','3914bb0a2d32423885ab84339bf391d5',4,true);
INSERT INTO wf_element_data VALUES ('0f9c64e3371448358e6f39f292c93a46','start-name','만족도 평가',5,false);
INSERT INTO wf_element_data VALUES ('0f9c64e3371448358e6f39f292c93a46','end-id','f9a7329b5cd24001b532265ee88ad3ee',6,true);
INSERT INTO wf_element_data VALUES ('0f9c64e3371448358e6f39f292c93a46','end-name','종료',7,false);
INSERT INTO wf_element_data VALUES ('3914bb0a2d32423885ab84339bf391d5','assignee-type','assignee.type.assignee',0,true);
INSERT INTO wf_element_data VALUES ('3914bb0a2d32423885ab84339bf391d5','assignee','z-requester',1,true);
INSERT INTO wf_element_data VALUES ('3914bb0a2d32423885ab84339bf391d5','reject-id','',2,false);
INSERT INTO wf_element_data VALUES ('3914bb0a2d32423885ab84339bf391d5','withdraw','N',3,false);
INSERT INTO wf_element_data VALUES ('d855698cb2c7443194b03eb17fe69522','action-name','',0,false);
INSERT INTO wf_element_data VALUES ('d855698cb2c7443194b03eb17fe69522','action-value','',1,false);
INSERT INTO wf_element_data VALUES ('d855698cb2c7443194b03eb17fe69522','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('d855698cb2c7443194b03eb17fe69522','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('d855698cb2c7443194b03eb17fe69522','start-id','cc5734c370864f15be79c750c9ffffdb',4,true);
INSERT INTO wf_element_data VALUES ('d855698cb2c7443194b03eb17fe69522','start-name','시작',5,false);
INSERT INTO wf_element_data VALUES ('d855698cb2c7443194b03eb17fe69522','end-id','3914bb0a2d32423885ab84339bf391d5',6,true);
INSERT INTO wf_element_data VALUES ('d855698cb2c7443194b03eb17fe69522','end-name','만족도 평가',7,false);
/* 인프라변경관리 */
INSERT INTO wf_element_data VALUES ('0844d2fc0cfc45238f24f0a09a328f0a', 'action-name', '', 0, false);
INSERT INTO wf_element_data VALUES ('0844d2fc0cfc45238f24f0a09a328f0a', 'action-value', '', 1, false);
INSERT INTO wf_element_data VALUES ('0844d2fc0cfc45238f24f0a09a328f0a', 'is-default', 'N', 2, false);
INSERT INTO wf_element_data VALUES ('0844d2fc0cfc45238f24f0a09a328f0a', 'condition-value', '', 3, false);
INSERT INTO wf_element_data VALUES ('0844d2fc0cfc45238f24f0a09a328f0a', 'start-id', '32f0d926786441ee84b6d56bf2f0fd08', 4, true);
INSERT INTO wf_element_data VALUES ('0844d2fc0cfc45238f24f0a09a328f0a', 'start-name', '시작', 5, false);
INSERT INTO wf_element_data VALUES ('0844d2fc0cfc45238f24f0a09a328f0a', 'end-id', 'c9c7ed6d193b4fd58b726668cbddccae', 6, true);
INSERT INTO wf_element_data VALUES ('0844d2fc0cfc45238f24f0a09a328f0a', 'end-name', '이관문서검토', 7, false);
INSERT INTO wf_element_data VALUES ('0c5465de1a8a480a9ac919b79b29569f', 'action-name', '', 0, false);
INSERT INTO wf_element_data VALUES ('0c5465de1a8a480a9ac919b79b29569f', 'action-value', '', 1, false);
INSERT INTO wf_element_data VALUES ('0c5465de1a8a480a9ac919b79b29569f', 'is-default', 'N', 2, false);
INSERT INTO wf_element_data VALUES ('0c5465de1a8a480a9ac919b79b29569f', 'condition-value', '', 3, false);
INSERT INTO wf_element_data VALUES ('0c5465de1a8a480a9ac919b79b29569f', 'start-id', '4f2a50e6b49a4e1fab1a9866277a6996', 4, true);
INSERT INTO wf_element_data VALUES ('0c5465de1a8a480a9ac919b79b29569f', 'start-name', '', 5, false);
INSERT INTO wf_element_data VALUES ('0c5465de1a8a480a9ac919b79b29569f', 'end-id', '8b8564b9f8164971bee112f0a73e66f3', 6, true);
INSERT INTO wf_element_data VALUES ('0c5465de1a8a480a9ac919b79b29569f', 'end-name', '변경결과서 작성', 7, false);
INSERT INTO wf_element_data VALUES ('0e1c6a483d1f415eb1dd91c0348f13dd', 'complete-action', '', 0, false);
INSERT INTO wf_element_data VALUES ('0f188cf2587b488293d6c22b74258167', 'action-name', '승인', 0, false);
INSERT INTO wf_element_data VALUES ('0f188cf2587b488293d6c22b74258167', 'action-value', 'plan-approval', 1, false);
INSERT INTO wf_element_data VALUES ('0f188cf2587b488293d6c22b74258167', 'is-default', 'N', 2, false);
INSERT INTO wf_element_data VALUES ('0f188cf2587b488293d6c22b74258167', 'condition-value', '', 3, false);
INSERT INTO wf_element_data VALUES ('0f188cf2587b488293d6c22b74258167', 'start-id', '58d6f5e75a5543ab96bc0fa1fe2637c7', 4, true);
INSERT INTO wf_element_data VALUES ('0f188cf2587b488293d6c22b74258167', 'start-name', '변경계획서 검토', 5, false);
INSERT INTO wf_element_data VALUES ('0f188cf2587b488293d6c22b74258167', 'end-id', '4b7e284e81014cfda64de39550895647', 6, true);
INSERT INTO wf_element_data VALUES ('0f188cf2587b488293d6c22b74258167', 'end-name', '처리', 7, false);
INSERT INTO wf_element_data VALUES ('139f55357c1242cc89a0a8162734bae6', 'assignee-type', 'assignee.type.assignee', 0, true);
INSERT INTO wf_element_data VALUES ('139f55357c1242cc89a0a8162734bae6', 'assignee', 'z-change-infra-complete-reviewer', 1, true);
INSERT INTO wf_element_data VALUES ('139f55357c1242cc89a0a8162734bae6', 'reject-id', '', 2, false);
INSERT INTO wf_element_data VALUES ('139f55357c1242cc89a0a8162734bae6', 'withdraw', 'Y', 3, false);
INSERT INTO wf_element_data VALUES ('3d56aaee23934e179d2c0ba8e0c860a2', 'action-name', '자체처리', 0, false);
INSERT INTO wf_element_data VALUES ('3d56aaee23934e179d2c0ba8e0c860a2', 'action-value', 'infra-self', 1, false);
INSERT INTO wf_element_data VALUES ('3d56aaee23934e179d2c0ba8e0c860a2', 'is-default', 'N', 2, false);
INSERT INTO wf_element_data VALUES ('3d56aaee23934e179d2c0ba8e0c860a2', 'condition-value', '', 3, false);
INSERT INTO wf_element_data VALUES ('3d56aaee23934e179d2c0ba8e0c860a2', 'start-id', '996c1c14b95643b1b0a22436f2dee6ea', 4, true);
INSERT INTO wf_element_data VALUES ('3d56aaee23934e179d2c0ba8e0c860a2', 'start-name', '', 5, false);
INSERT INTO wf_element_data VALUES ('3d56aaee23934e179d2c0ba8e0c860a2', 'end-id', 'baae90a53e39457480d0b2139622e1d2', 6, true);
INSERT INTO wf_element_data VALUES ('3d56aaee23934e179d2c0ba8e0c860a2', 'end-name', '자체처리', 7, false);
INSERT INTO wf_element_data VALUES ('432255a7eae9415085f38348d6445468', 'action-name', '구성관리', 0, false);
INSERT INTO wf_element_data VALUES ('432255a7eae9415085f38348d6445468', 'action-value', 'cfg', 1, false);
INSERT INTO wf_element_data VALUES ('432255a7eae9415085f38348d6445468', 'is-default', 'N', 2, false);
INSERT INTO wf_element_data VALUES ('432255a7eae9415085f38348d6445468', 'condition-value', '', 3, false);
INSERT INTO wf_element_data VALUES ('432255a7eae9415085f38348d6445468', 'start-id', '996c1c14b95643b1b0a22436f2dee6ea', 4, true);
INSERT INTO wf_element_data VALUES ('432255a7eae9415085f38348d6445468', 'start-name', '', 5, false);
INSERT INTO wf_element_data VALUES ('432255a7eae9415085f38348d6445468', 'end-id', '7de6a641ac754f48be50f40c63bb1ef5', 6, true);
INSERT INTO wf_element_data VALUES ('432255a7eae9415085f38348d6445468', 'end-name', '구성관리', 7, false);
INSERT INTO wf_element_data VALUES ('474cccfff5344196ade87e3479910a6f', 'action-name', '', 0, false);
INSERT INTO wf_element_data VALUES ('474cccfff5344196ade87e3479910a6f', 'action-value', '', 1, false);
INSERT INTO wf_element_data VALUES ('474cccfff5344196ade87e3479910a6f', 'is-default', 'N', 2, false);
INSERT INTO wf_element_data VALUES ('474cccfff5344196ade87e3479910a6f', 'condition-value', '', 3, false);
INSERT INTO wf_element_data VALUES ('474cccfff5344196ade87e3479910a6f', 'start-id', '4b7e284e81014cfda64de39550895647', 4, true);
INSERT INTO wf_element_data VALUES ('474cccfff5344196ade87e3479910a6f', 'start-name', '처리', 5, false);
INSERT INTO wf_element_data VALUES ('474cccfff5344196ade87e3479910a6f', 'end-id', '996c1c14b95643b1b0a22436f2dee6ea', 6, true);
INSERT INTO wf_element_data VALUES ('474cccfff5344196ade87e3479910a6f', 'end-name', '', 7, false);
INSERT INTO wf_element_data VALUES ('4b7e284e81014cfda64de39550895647', 'assignee-type', 'assignee.type.assignee', 0, true);
INSERT INTO wf_element_data VALUES ('4b7e284e81014cfda64de39550895647', 'assignee', 'z-change-infra-processor', 1, true);
INSERT INTO wf_element_data VALUES ('4b7e284e81014cfda64de39550895647', 'reject-id', '', 2, false);
INSERT INTO wf_element_data VALUES ('4b7e284e81014cfda64de39550895647', 'withdraw', 'N', 3, false);
INSERT INTO wf_element_data VALUES ('4f2a50e6b49a4e1fab1a9866277a6996', 'condition-item', '#{action}', 0, true);
INSERT INTO wf_element_data VALUES ('515b32b1032c48d48b0cb7f304701aec', 'action-name', 'PIR 작성', 0, false);
INSERT INTO wf_element_data VALUES ('515b32b1032c48d48b0cb7f304701aec', 'action-value', 'pir', 1, false);
INSERT INTO wf_element_data VALUES ('515b32b1032c48d48b0cb7f304701aec', 'is-default', 'N', 2, false);
INSERT INTO wf_element_data VALUES ('515b32b1032c48d48b0cb7f304701aec', 'condition-value', '', 3, false);
INSERT INTO wf_element_data VALUES ('515b32b1032c48d48b0cb7f304701aec', 'start-id', '8b8564b9f8164971bee112f0a73e66f3', 4, true);
INSERT INTO wf_element_data VALUES ('515b32b1032c48d48b0cb7f304701aec', 'start-name', '변경결과서 작성', 5, false);
INSERT INTO wf_element_data VALUES ('515b32b1032c48d48b0cb7f304701aec', 'end-id', 'be6990d4bd1f4c3586df37fe147b2dea', 6, true);
INSERT INTO wf_element_data VALUES ('515b32b1032c48d48b0cb7f304701aec', 'end-name', 'PIR', 7, false);
INSERT INTO wf_element_data VALUES ('58d6f5e75a5543ab96bc0fa1fe2637c7', 'assignee-type', 'assignee.type.assignee', 0, true);
INSERT INTO wf_element_data VALUES ('58d6f5e75a5543ab96bc0fa1fe2637c7', 'assignee', 'z-change-infra-changePlan-approver', 1, true);
INSERT INTO wf_element_data VALUES ('58d6f5e75a5543ab96bc0fa1fe2637c7', 'reject-id', '697ba85cf98c4d9083f83bd5dd66639f', 2, false);
INSERT INTO wf_element_data VALUES ('58d6f5e75a5543ab96bc0fa1fe2637c7', 'withdraw', 'Y', 3, false);
INSERT INTO wf_element_data VALUES ('6366778fd60e4a2d913e0d84f80bd11c', 'action-name', '', 0, false);
INSERT INTO wf_element_data VALUES ('6366778fd60e4a2d913e0d84f80bd11c', 'action-value', '', 1, false);
INSERT INTO wf_element_data VALUES ('6366778fd60e4a2d913e0d84f80bd11c', 'is-default', 'N', 2, false);
INSERT INTO wf_element_data VALUES ('6366778fd60e4a2d913e0d84f80bd11c', 'condition-value', '', 3, false);
INSERT INTO wf_element_data VALUES ('6366778fd60e4a2d913e0d84f80bd11c', 'start-id', 'baae90a53e39457480d0b2139622e1d2', 4, true);
INSERT INTO wf_element_data VALUES ('6366778fd60e4a2d913e0d84f80bd11c', 'start-name', '자체처리', 5, false);
INSERT INTO wf_element_data VALUES ('6366778fd60e4a2d913e0d84f80bd11c', 'end-id', '4f2a50e6b49a4e1fab1a9866277a6996', 6, true);
INSERT INTO wf_element_data VALUES ('6366778fd60e4a2d913e0d84f80bd11c', 'end-name', '', 7, false);
INSERT INTO wf_element_data VALUES ('697ba85cf98c4d9083f83bd5dd66639f', 'assignee-type', 'assignee.type.assignee', 0, true);
INSERT INTO wf_element_data VALUES ('697ba85cf98c4d9083f83bd5dd66639f', 'assignee', 'z-change-infra-processor', 1, true);
INSERT INTO wf_element_data VALUES ('697ba85cf98c4d9083f83bd5dd66639f', 'reject-id', '', 2, false);
INSERT INTO wf_element_data VALUES ('697ba85cf98c4d9083f83bd5dd66639f', 'withdraw', 'N', 3, false);
INSERT INTO wf_element_data VALUES ('7de6a641ac754f48be50f40c63bb1ef5', 'sub-document-id', '', 0, true);
INSERT INTO wf_element_data VALUES ('89d3605de19d491ea6590640b94461d4', 'action-name', '검토완료', 0, false);
INSERT INTO wf_element_data VALUES ('89d3605de19d491ea6590640b94461d4', 'action-value', 'infra-review-complete', 1, false);
INSERT INTO wf_element_data VALUES ('89d3605de19d491ea6590640b94461d4', 'is-default', 'N', 2, false);
INSERT INTO wf_element_data VALUES ('89d3605de19d491ea6590640b94461d4', 'condition-value', '', 3, false);
INSERT INTO wf_element_data VALUES ('89d3605de19d491ea6590640b94461d4', 'start-id', '139f55357c1242cc89a0a8162734bae6', 4, true);
INSERT INTO wf_element_data VALUES ('89d3605de19d491ea6590640b94461d4', 'start-name', '최종검토', 5, false);
INSERT INTO wf_element_data VALUES ('89d3605de19d491ea6590640b94461d4', 'end-id', '9d30b4b0fa3e4b9190c5042e379e59f6', 6, true);
INSERT INTO wf_element_data VALUES ('89d3605de19d491ea6590640b94461d4', 'end-name', '종료', 7, false);
INSERT INTO wf_element_data VALUES ('8b8564b9f8164971bee112f0a73e66f3', 'assignee-type', 'assignee.type.assignee', 0, true);
INSERT INTO wf_element_data VALUES ('8b8564b9f8164971bee112f0a73e66f3', 'assignee', 'z-change-infra-processor', 1, true);
INSERT INTO wf_element_data VALUES ('8b8564b9f8164971bee112f0a73e66f3', 'reject-id', '', 2, false);
INSERT INTO wf_element_data VALUES ('8b8564b9f8164971bee112f0a73e66f3', 'withdraw', 'N', 3, false);
INSERT INTO wf_element_data VALUES ('996c1c14b95643b1b0a22436f2dee6ea', 'condition-item', '#{action}', 0, true);
INSERT INTO wf_element_data VALUES ('9deadc6cdd6947eebcac777abaa8338b', 'action-name', '', 0, false);
INSERT INTO wf_element_data VALUES ('9deadc6cdd6947eebcac777abaa8338b', 'action-value', '', 1, false);
INSERT INTO wf_element_data VALUES ('9deadc6cdd6947eebcac777abaa8338b', 'is-default', 'N', 2, false);
INSERT INTO wf_element_data VALUES ('9deadc6cdd6947eebcac777abaa8338b', 'condition-value', '', 3, false);
INSERT INTO wf_element_data VALUES ('9deadc6cdd6947eebcac777abaa8338b', 'start-id', '0e1c6a483d1f415eb1dd91c0348f13dd', 4, true);
INSERT INTO wf_element_data VALUES ('9deadc6cdd6947eebcac777abaa8338b', 'start-name', '접수', 5, false);
INSERT INTO wf_element_data VALUES ('9deadc6cdd6947eebcac777abaa8338b', 'end-id', '697ba85cf98c4d9083f83bd5dd66639f', 6, true);
INSERT INTO wf_element_data VALUES ('9deadc6cdd6947eebcac777abaa8338b', 'end-name', '변경계획서 작성', 7, false);
INSERT INTO wf_element_data VALUES ('a0bfeb6307bf9d5c6c71db3c5e3232df', 'action-name', '접수', 0, false);
INSERT INTO wf_element_data VALUES ('a0bfeb6307bf9d5c6c71db3c5e3232df', 'action-value', 'infra-accept', 1, false);
INSERT INTO wf_element_data VALUES ('a0bfeb6307bf9d5c6c71db3c5e3232df', 'is-default', 'N', 2, false);
INSERT INTO wf_element_data VALUES ('a0bfeb6307bf9d5c6c71db3c5e3232df', 'condition-value', '', 3, false);
INSERT INTO wf_element_data VALUES ('a0bfeb6307bf9d5c6c71db3c5e3232df', 'start-id', 'c9c7ed6d193b4fd58b726668cbddccae', 4, true);
INSERT INTO wf_element_data VALUES ('a0bfeb6307bf9d5c6c71db3c5e3232df', 'start-name', '이관문서검토', 5, false);
INSERT INTO wf_element_data VALUES ('a0bfeb6307bf9d5c6c71db3c5e3232df', 'end-id', '0e1c6a483d1f415eb1dd91c0348f13dd', 6, true);
INSERT INTO wf_element_data VALUES ('a0bfeb6307bf9d5c6c71db3c5e3232df', 'end-name', '접수', 7, false);
INSERT INTO wf_element_data VALUES ('a94aaf66757795501eacedd06a5b3295', 'action-name', '검토요청', 0, false);
INSERT INTO wf_element_data VALUES ('a94aaf66757795501eacedd06a5b3295', 'action-value', 'infra-review-request', 1, false);
INSERT INTO wf_element_data VALUES ('a94aaf66757795501eacedd06a5b3295', 'is-default', 'N', 2, false);
INSERT INTO wf_element_data VALUES ('a94aaf66757795501eacedd06a5b3295', 'condition-value', '', 3, false);
INSERT INTO wf_element_data VALUES ('a94aaf66757795501eacedd06a5b3295', 'start-id', 'be6990d4bd1f4c3586df37fe147b2dea', 4, true);
INSERT INTO wf_element_data VALUES ('a94aaf66757795501eacedd06a5b3295', 'start-name', 'PIR', 5, false);
INSERT INTO wf_element_data VALUES ('a94aaf66757795501eacedd06a5b3295', 'end-id', '139f55357c1242cc89a0a8162734bae6', 6, true);
INSERT INTO wf_element_data VALUES ('a94aaf66757795501eacedd06a5b3295', 'end-name', '최종검토', 7, false);
INSERT INTO wf_element_data VALUES ('baae90a53e39457480d0b2139622e1d2', 'complete-action', '', 0, false);
INSERT INTO wf_element_data VALUES ('be6990d4bd1f4c3586df37fe147b2dea', 'assignee-type', 'assignee.type.assignee', 0, true);
INSERT INTO wf_element_data VALUES ('be6990d4bd1f4c3586df37fe147b2dea', 'assignee', 'z-change-infra-processor', 1, true);
INSERT INTO wf_element_data VALUES ('be6990d4bd1f4c3586df37fe147b2dea', 'reject-id', '8b8564b9f8164971bee112f0a73e66f3', 2, false);
INSERT INTO wf_element_data VALUES ('be6990d4bd1f4c3586df37fe147b2dea', 'withdraw', 'Y', 3, false);
INSERT INTO wf_element_data VALUES ('c9c7ed6d193b4fd58b726668cbddccae', 'assignee-type', 'assignee.type.candidate.groups', 0, true);
INSERT INTO wf_element_data VALUES ('c9c7ed6d193b4fd58b726668cbddccae', 'assignee', 'infra.change.assignee', 1, true);
INSERT INTO wf_element_data VALUES ('c9c7ed6d193b4fd58b726668cbddccae', 'reject-id', '', 2, false);
INSERT INTO wf_element_data VALUES ('c9c7ed6d193b4fd58b726668cbddccae', 'withdraw', 'N', 3, false);
INSERT INTO wf_element_data VALUES ('e15d71bd48bc485f860d2abb70d8d16e', 'action-name', '승인요청', 0, false);
INSERT INTO wf_element_data VALUES ('e15d71bd48bc485f860d2abb70d8d16e', 'action-value', 'plan-request', 1, false);
INSERT INTO wf_element_data VALUES ('e15d71bd48bc485f860d2abb70d8d16e', 'is-default', 'N', 2, false);
INSERT INTO wf_element_data VALUES ('e15d71bd48bc485f860d2abb70d8d16e', 'condition-value', '', 3, false);
INSERT INTO wf_element_data VALUES ('e15d71bd48bc485f860d2abb70d8d16e', 'start-id', '697ba85cf98c4d9083f83bd5dd66639f', 4, true);
INSERT INTO wf_element_data VALUES ('e15d71bd48bc485f860d2abb70d8d16e', 'start-name', '변경계획서 작성', 5, false);
INSERT INTO wf_element_data VALUES ('e15d71bd48bc485f860d2abb70d8d16e', 'end-id', '58d6f5e75a5543ab96bc0fa1fe2637c7', 6, true);
INSERT INTO wf_element_data VALUES ('e15d71bd48bc485f860d2abb70d8d16e', 'end-name', '변경계획서 검토', 7, false);
INSERT INTO wf_element_data VALUES ('e2c199ab8f4a4c2ab7cdfc67073b3340', 'action-value', '', 0, false);
INSERT INTO wf_element_data VALUES ('e2c199ab8f4a4c2ab7cdfc67073b3340', 'is-default', 'N', 1, false);
INSERT INTO wf_element_data VALUES ('e2c199ab8f4a4c2ab7cdfc67073b3340', 'condition-value', '', 2, false);
INSERT INTO wf_element_data VALUES ('e2c199ab8f4a4c2ab7cdfc67073b3340', 'start-id', '7de6a641ac754f48be50f40c63bb1ef5', 3, true);
INSERT INTO wf_element_data VALUES ('e2c199ab8f4a4c2ab7cdfc67073b3340', 'start-name', '구성관리', 4, false);
INSERT INTO wf_element_data VALUES ('e2c199ab8f4a4c2ab7cdfc67073b3340', 'end-id', '4f2a50e6b49a4e1fab1a9866277a6996', 5, true);
INSERT INTO wf_element_data VALUES ('e2c199ab8f4a4c2ab7cdfc67073b3340', 'end-name', '', 6, false);
INSERT INTO wf_element_data VALUES ('e2c199ab8f4a4c2ab7cdfc67073b3340', 'action-name', '', 7, false);

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
DROP TABLE IF EXISTS awf_tag cascade;

CREATE TABLE awf_tag
(
	tag_id varchar(128) NOT NULL,
	tag_type varchar(128) NOT NULL,
	tag_value varchar(256) NOT NULL,
	target_id varchar(128) NOT NULL,
    CONSTRAINT awf_tag_pk PRIMARY KEY (tag_id),
    CONSTRAINT awf_tag_un UNIQUE (tag_type, tag_value, target_id)
);

COMMENT ON TABLE awf_tag IS '태그';
COMMENT ON COLUMN awf_tag.tag_id IS '태그아이디';
COMMENT ON COLUMN awf_tag.tag_type IS '태그타입';
COMMENT ON COLUMN awf_tag.tag_value IS '태그내용';
COMMENT ON COLUMN awf_tag.target_id IS '태그대상아이디';

/* 단순문의 */
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c157913004c','component','로고','ae3e2a000d67b4e2e8d83bf36c81260a');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c157914004d','component','z-logo','ae3e2a000d67b4e2e8d83bf36c81260a');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c15791a004f','component','제목','aa83af616d59cc86c565a0282153c236');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c15791b0050','component','z-document-title','aa83af616d59cc86c565a0282153c236');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c15791b0051','component','z-serviceDesk-inquiry','aa83af616d59cc86c565a0282153c236');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c1579290054','component','신청자','ad15aab4783f55533c6c1f183a4b60cc');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c15792a0055','component','z-requester','ad15aab4783f55533c6c1f183a4b60cc');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c15792c0056','component','신청부서','ae4f48236ecf493eb0212d77ac14c360');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c15792d0057','component','z-requester-department','ae4f48236ecf493eb0212d77ac14c360');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c1579360059','component','전화번호','a71d2d6094b2920f87ba8eaf025af1c3');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c157937005a','component','z-requester-phone','a71d2d6094b2920f87ba8eaf025af1c3');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c157939005b','component','이메일','a4a100bbcd9907aae4e260c8bfa3b45f');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c15793a005c','component','z-requester-email','a4a100bbcd9907aae4e260c8bfa3b45f');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c157946005e','component','신청일시','a115df4520fea935e4482784064c7a51');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c157947005f','component','z-request-date','a115df4520fea935e4482784064c7a51');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c1579490060','component','완료희망일시','a0c3e8d18663483284b7e9fe83d0b824');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c1579490061','component','z-request-deadline','a0c3e8d18663483284b7e9fe83d0b824');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c1579490062','component','z-serviceDesk-inquiry-request-deadline','a0c3e8d18663483284b7e9fe83d0b824');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c1579520063','component','z-request-title','a75c85bba7aa722f9dcbcbcfad0b6ac7');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c1579530064','component','제목','a75c85bba7aa722f9dcbcbcfad0b6ac7');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c1579590066','component','z-request-category','ade3320b3bc9dca17651e4c0cdef89e4');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c1579590067','component','서비스항목','ade3320b3bc9dca17651e4c0cdef89e4');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c15795a0068','component','category','ade3320b3bc9dca17651e4c0cdef89e4');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c15795f006a','component','내용','a3b140da99dbfb49beb7fa3db09042b4');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c157960006b','component','z-request-content','a3b140da99dbfb49beb7fa3db09042b4');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c157965006d','component','첨부파일','a9404877fabbd09dc2603f78283fa155');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c157966006e','component','z-request-file','a9404877fabbd09dc2603f78283fa155');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c15796e0071','component','접수자','ab6d29d8ef257f93de98c655fbaab22a');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c15796f0072','component','z-acceptor','ab6d29d8ef257f93de98c655fbaab22a');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c1579700073','component','접수부서','ab266326aa59ed5bb4411da4142aa24a');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c1579710074','component','z-acceptor-department','ab266326aa59ed5bb4411da4142aa24a');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c1579790076','component','접수일시','a8e347190f51b1b05200f34d9e8b8ab0');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c15797a0077','component','z-accept-date','a8e347190f51b1b05200f34d9e8b8ab0');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c15797f0079','component','접수의견','a71977859617f137f589108c24fa7845');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c15797f007a','component','z-accept-content','a71977859617f137f589108c24fa7845');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c157984007c','component','처리자','acc02e1f2f51aa01649b6e06f7b35ec4');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c157985007d','component','z-processor','acc02e1f2f51aa01649b6e06f7b35ec4');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c15798d0080','component','처리일시','a83a54dfec6c65dc5983272ec9a956ce');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c15798e0081','component','z-process-date','a83a54dfec6c65dc5983272ec9a956ce');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c1579930083','component','처리내용','ae07bae262c57399a4ee8b3dea2fcbab');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c1579940084','component','z-process-content','ae07bae262c57399a4ee8b3dea2fcbab');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c1579990086','component','첨부파일','a27c22d07de4231ab41863dab45450f1');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c15799a0087','component','z-process-file','a27c22d07de4231ab41863dab45450f1');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c1579a00089','component','승인자','a4c410628aa58b1863935b4056395b80');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c1579a1008a','component','z-approver','a4c410628aa58b1863935b4056395b80');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c1579a9008d','component','승인일시','a10c935dbdf1c30b34dfabe5ebc0ba73');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c1579aa008e','component','반려일시','a10c935dbdf1c30b34dfabe5ebc0ba73');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c1579ab008f','component','z-approve-date','a10c935dbdf1c30b34dfabe5ebc0ba73');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c1579ab008g','component','z-serviceDesk-inquiry-approve-date','a10c935dbdf1c30b34dfabe5ebc0ba73');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c1579b20091','component','승인의견','ab97abfd66b5654e3f525e617df9afaf');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c1579b30092','component','반려의견','ab97abfd66b5654e3f525e617df9afaf');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c1579b40093','component','z-approve-content','ab97abfd66b5654e3f525e617df9afaf');
/* 서비스데스크 - 단순문의 - 만족도 */
INSERT INTO awf_tag VALUES ('4028b21f7c780ba6017c78334c7e023e','component','로고','a605df0d26f09a3d20caaea3977a5c64');
INSERT INTO awf_tag VALUES ('4028b21f7c780ba6017c78334c9c023f','component','z-logo','a605df0d26f09a3d20caaea3977a5c64');
INSERT INTO awf_tag VALUES ('4028b21f7c780ba6017c78334d900241','component','제목','ae54c20fd574a57845fffaa7bb936eb4');
INSERT INTO awf_tag VALUES ('4028b21f7c780ba6017c78334db70242','component','z-document-title','ae54c20fd574a57845fffaa7bb936eb4');
INSERT INTO awf_tag VALUES ('4028b21f7c780ba6017c78334fd20246','component','만족도','ac69d66f3ec394b36215cd2ee3983292');
INSERT INTO awf_tag VALUES ('4028b21f7c780ba6017c78334ff20247','component','z-satisfaction','ac69d66f3ec394b36215cd2ee3983292');
INSERT INTO awf_tag VALUES ('4028b21f7c780ba6017c78334ff20248','component','z-serviceDesk-inquiry-satisfaction','ac69d66f3ec394b36215cd2ee3983292');
INSERT INTO awf_tag VALUES ('4028b21f7c780ba6017c783350cb0249','component','만족도평가의견','ad2996c39febdc13b32d08354169d6ac');
INSERT INTO awf_tag VALUES ('4028b21f7c780ba6017c783350f1024a','component','z-satisfaction-content','ad2996c39febdc13b32d08354169d6ac');
/* 서비스데스크 - 장애신고 */
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b06137c0067','component','로고','a371d3cfecb547e4aff813ce0fca711c');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0613820068','component','z-logo','a371d3cfecb547e4aff813ce0fca711c');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0613b6006a','component','제목','e90e4131007f470490a2ffaff402ba2f');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0613bd006b','component','z-document-title','e90e4131007f470490a2ffaff402ba2f');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b061436006f','component','신청자','77e97f770393455a97c645f7562b3b53');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b06143d0070','component','z-requester','77e97f770393455a97c645f7562b3b53');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b06144a0071','component','신청부서','fee68572b7bb4e04b1f27d0cdfe9ad7a');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0614500072','component','z-requester-department','fee68572b7bb4e04b1f27d0cdfe9ad7a');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b06149e0074','component','전화번호','857ac1b2c7f3424ab297108583b3c5c1');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0614a50075','component','z-requester-phone','857ac1b2c7f3424ab297108583b3c5c1');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0614b50076','component','이메일','b960f194ab8e4ecda04c319398bf83ea');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0614bb0077','component','z-requester-email','b960f194ab8e4ecda04c319398bf83ea');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b06150a0079','component','신청일시','4b8b310945214d0bbd262352802f93c6');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b061511007a','component','z-request-date','4b8b310945214d0bbd262352802f93c6');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b06151f007b','component','완료희망일시','ae79624833414ab3a3ff37582c234aa2');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b061527007c','component','z-request-deadline','ae79624833414ab3a3ff37582c234aa2');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b061530007d','component','z-serviceDesk-incident-request-deadline','ae79624833414ab3a3ff37582c234aa2');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b06157f007f','component','장애발생일시','a6eb123ed5ac913c55035f15bb30efce');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0615850080','component','z-incident-date','a6eb123ed5ac913c55035f15bb30efce');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0615920081','component','장애인지경로','a20925ede2aee30d0c74c4647c0c46dd');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0615970082','component','z-perception-path','a20925ede2aee30d0c74c4647c0c46dd');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0615e60084','component','z-request-title','0138fd96d2ca4075b8d8ff4ef4b9fe10');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0615ee0085','component','제목','0138fd96d2ca4075b8d8ff4ef4b9fe10');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0616260087','component','내용','250e5452b08344758d08fb59915c4e95');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b06162d0088','component','z-request-content','250e5452b08344758d08fb59915c4e95');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b06165c008a','component','첨부파일','fdd00ba8ff1240cdaeaf526a4df96db5');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b061665008b','component','z-request-file','fdd00ba8ff1240cdaeaf526a4df96db5');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0616b2008e','component','접수자','b2f7a7991a474b42a0928714afafaa0b');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0616b8008f','component','z-acceptor','b2f7a7991a474b42a0928714afafaa0b');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0616c80090','component','접수부서','c3b8421281e2499cbd2bc98098009a07');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0616cf0091','component','z-acceptor-department','c3b8421281e2499cbd2bc98098009a07');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b06171c0093','component','접수일시','2c0183c3015c414a823c09d13a6e49be');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0617240094','component','z-accept-date','2c0183c3015c414a823c09d13a6e49be');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0617540096','component','접수의견','cdefccb8cf074e2f8ee1f741e80c483f');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b06175b0097','component','z-accept-content','cdefccb8cf074e2f8ee1f741e80c483f');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b06178e0099','component','관련CI','a7acc6870b4c4e6e147c2feb20bda6b4');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b061796009a','component','z-relation-ci','a7acc6870b4c4e6e147c2feb20bda6b4');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0617cb009c','component','처리자','5aa28e3b3e2446258a15b99267bd063c');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0617d2009d','component','z-processor','5aa28e3b3e2446258a15b99267bd063c');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b06181900a0','component','처리일시','966c2137e9d747e0a4ef5953189d596a');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b06182000a1','component','z-process-date','966c2137e9d747e0a4ef5953189d596a');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b06182e00a2','component','장애유형','a75adb2ca0f4d7023972794de97e979e');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b06183500a3','component','z-incident-category','a75adb2ca0f4d7023972794de97e979e');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b06188400a5','component','장애등급','aee06d995cbcad83c98df177479242c9');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b06188a00a6','component','z-incident-level','aee06d995cbcad83c98df177479242c9');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b06189700a7','component','긴급도','a6b76adaa1172d77d2b6cd62ad98db34');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b06189e00a8','component','z-incident-urgency','a6b76adaa1172d77d2b6cd62ad98db34');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0618ec00aa','component','장애원인','a2052b72aef1717cc785ead95c9b583c');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0618f400ab','component','z-incident-cause','a2052b72aef1717cc785ead95c9b583c');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b06193000ad','component','z-incident-symptom','a81e3ddc48a12baf94aebac8f2a4f6bc');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b06193700ae','component','장애증상','a81e3ddc48a12baf94aebac8f2a4f6bc');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b06196d00b0','component','처리내용','aa27a4ea8b744f4bab35b7af3c2e913d');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b06197400b1','component','z-process-content','aa27a4ea8b744f4bab35b7af3c2e913d');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0619a500b3','component','향후대책','a9b723e29458f98a4904336baff53f6a');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0619ac00b4','component','z-incident-plan','a9b723e29458f98a4904336baff53f6a');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0619dd00b6','component','CI','afd603a99e8287aa40706c175c697ae7');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0619e300b7','component','관련CI','afd603a99e8287aa40706c175c697ae7');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0619ea00b8','component','z-incident-relation-ci','afd603a99e8287aa40706c175c697ae7');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b061a1c00ba','component','첨부파일','09f5924aeff4483daf730badbc1379d6');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b061a2300bb','component','z-process-file','09f5924aeff4483daf730badbc1379d6');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b061a5c00bd','component','승인자','5a57a673747a46b0924716a33470a723');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b061a6300be','component','z-approver','5a57a673747a46b0924716a33470a723');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b061aa600c1','component','승인일시','39e8b33c67ad420599d9f57c6e23cbee');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b061aad00c2','component','반려일시','39e8b33c67ad420599d9f57c6e23cbee');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b061ab500c3','component','z-approve-date','39e8b33c67ad420599d9f57c6e23cbee');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b061abb00c4','component','z-incident-approve-date','39e8b33c67ad420599d9f57c6e23cbee');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b061aed00c6','component','승인의견','5dba55dd1d57415ba28a3f1816859793');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b061af300c7','component','반려의견','5dba55dd1d57415ba28a3f1816859793');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b061af900c8','component','z-approve-content','5dba55dd1d57415ba28a3f1816859793');
/* 서비스데스크 - 장애신고 - 만족도 */
INSERT INTO awf_tag VALUES ('4028b21f7c90d996017c914da7bf0023','component','로고','a4257952286a4e6fae6faaeaae7279fd');
INSERT INTO awf_tag VALUES ('4028b21f7c90d996017c914da7c50024','component','z-logo','a4257952286a4e6fae6faaeaae7279fd');
INSERT INTO awf_tag VALUES ('4028b21f7c90d996017c914da7e70026','component','제목','a19693dff90a45b889df568bbe177bcd');
INSERT INTO awf_tag VALUES ('4028b21f7c90d996017c914da7eb0027','component','z-document-title','a19693dff90a45b889df568bbe177bcd');
INSERT INTO awf_tag VALUES ('4028b21f7c90d996017c914da83a002b','component','만족도','5904fbf514df42cbb8d859799d8e7f3f');
INSERT INTO awf_tag VALUES ('4028b21f7c90d996017c914da841002c','component','z-satisfaction','5904fbf514df42cbb8d859799d8e7f3f');
INSERT INTO awf_tag VALUES ('4028b21f7c90d996017c914da841002g','component','z-serviceDesk-incident-satisfaction','5904fbf514df42cbb8d859799d8e7f3f');
INSERT INTO awf_tag VALUES ('4028b21f7c90d996017c914da860002e','component','만족도평가의견','207f974ae4654e7f8331526c504d0152');
INSERT INTO awf_tag VALUES ('4028b21f7c90d996017c914da865002f','component','z-satisfaction-content','207f974ae4654e7f8331526c504d0152');
/* 서비스데스크 - 서비스요청 */
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca054467d0003','component','로고','5d0b0faef24e429ba271e1bb2175d2ff');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca05446840004','component','z-logo','5d0b0faef24e429ba271e1bb2175d2ff');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca05446bb0006','component','제목','7811e5a93319403698481a00386f8b5e');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca05446c00007','component','z-document-title','7811e5a93319403698481a00386f8b5e');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca05446e90009','component','','c2972b99571a448ebdb8b2f981412060');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca0544728000c','component','신청자','93d2c6c791ac4824bf94add769417fe4');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca054472e000d','component','z-requester','93d2c6c791ac4824bf94add769417fe4');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca0544739000e','component','신청부서','8929fddece384122a219fb7cfbb07312');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca0544740000f','component','z-requester-department','8929fddece384122a219fb7cfbb07312');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca05447840011','component','전화번호','413ef0d9ef51415c9772baf19911b940');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca05447880012','component','z-requester-phone','413ef0d9ef51415c9772baf19911b940');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca05447940013','component','이메일','3cdd30935a7e4da1b93e82e1ca65747f');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca05447990014','component','z-requester-email','3cdd30935a7e4da1b93e82e1ca65747f');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca05447d40016','component','신청일시','76f13c0db2894fb5b8dcabc6e3e7a1fc');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca05447db0017','component','z-request-date','76f13c0db2894fb5b8dcabc6e3e7a1fc');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca05447e50018','component','완료희망일시','3304af52ee0a4009aaadb3917d5de7f8');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca05447e90019','component','z-request-deadline','3304af52ee0a4009aaadb3917d5de7f8');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca05447ef001a','component','z-serviceDesk-change-request-deadline','3304af52ee0a4009aaadb3917d5de7f8');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca054482b001c','component','z-request-title','8fe57acaefef4533b3897867cedb6579');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca0544831001d','component','제목','8fe57acaefef4533b3897867cedb6579');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca0544856001f','component','z-request-category','c4591d870dab40e3a18ca6f41250b200');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca054485b0020','component','요청구분','c4591d870dab40e3a18ca6f41250b200');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca05448800022','component','내용','4370df5c2ba44544a3444ce38240e61d');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca05448860023','component','z-request-content','4370df5c2ba44544a3444ce38240e61d');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca05448aa0025','component','첨부파일','ea1841546d6648f49bafc2ebbe11c32a');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca05448b10026','component','z-request-file','ea1841546d6648f49bafc2ebbe11c32a');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca05448e70029','component','접수자','f1867a396519411c848f65bfbda28d29');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca05448ed002a','component','z-acceptor','f1867a396519411c848f65bfbda28d29');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca05448f7002b','component','접수부서','ee30c91397e242b5a1ddae91437b50ea');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca05448fe002c','component','z-acceptor-department','ee30c91397e242b5a1ddae91437b50ea');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca0544937002e','component','접수일시','a6d20858d6e84b85b1e75fc28ec36696');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca054493c002f','component','z-accept-date','a6d20858d6e84b85b1e75fc28ec36696');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca05449660031','component','접수의견','7afa8f35f45f412eb98db57dfd07b6b6');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca054496a0032','component','z-accept-content','7afa8f35f45f412eb98db57dfd07b6b6');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca05449900034','component','관련CI','ebcb50c94a9f494c8afd28d95c4aa79a');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca05449970035','component','z-relation-ci','ebcb50c94a9f494c8afd28d95c4aa79a');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca05449b90037','component','처리자','afa6d40a2b304474ab7f424670587959');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca05449bf0038','component','z-processor','afa6d40a2b304474ab7f424670587959');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca05449f5003b','component','처리일시','1f3a106094dd4e02b170f20396d526de');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca05449fa003c','component','z-process-date','1f3a106094dd4e02b170f20396d526de');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca0544a22003e','component','처리결과','d80c69ade3fc48f1bd78e1b4b02397d7');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca0544a28003f','component','z-process-content','d80c69ade3fc48f1bd78e1b4b02397d7');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca0544a500041','component','비고','68098b156d354c069bfaf660b5b03f9d');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca0544a550042','component','z-process-etc','68098b156d354c069bfaf660b5b03f9d');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca0544a7d0044','component','CI','7f1e91223181424b89172127908058f5');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca0544a820045','component','관련CI','7f1e91223181424b89172127908058f5');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca0544a870046','component','z-process-relation-ci','7f1e91223181424b89172127908058f5');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca0544aa90048','component','첨부파일','7c77ea3ec86c41ecb6c0b4dc1ddcd9f9');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca0544aae0049','component','z-process-file','7c77ea3ec86c41ecb6c0b4dc1ddcd9f9');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca0544ad4004b','component','승인자','8a4339add3c54b3aba9e4864c16c558c');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca0544ada004c','component','z-approver','8a4339add3c54b3aba9e4864c16c558c');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca0544b10004f','component','승인일시','ba5482f4fcfc45d7b20a04328670f11b');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca0544b150050','component','반려일시','ba5482f4fcfc45d7b20a04328670f11b');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca0544b1b0051','component','z-approve-date','ba5482f4fcfc45d7b20a04328670f11b');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca0544b200052','component','z-serviceDesk-change-approve-date','ba5482f4fcfc45d7b20a04328670f11b');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca0544b3f0054','component','승인의견','23e3399e82b644d09ab20b281860a4cb');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca0544b440055','component','반려의견','23e3399e82b644d09ab20b281860a4cb');
INSERT INTO awf_tag VALUES ('4028b21f7c9ff7c8017ca0544b490056','component','z-approve-content','23e3399e82b644d09ab20b281860a4cb');
/* 서비스데스크 - 서비스요청 - 만족도 */
INSERT INTO awf_tag VALUES ('4028b21f7c90d996017c914eec450042','component','로고','c3c6191d9ee748bebc5745bb5be27452');
INSERT INTO awf_tag VALUES ('4028b21f7c90d996017c914eec4a0043','component','z-logo','c3c6191d9ee748bebc5745bb5be27452');
INSERT INTO awf_tag VALUES ('4028b21f7c90d996017c914eec6c0045','component','제목','90eedeed14494ea4b17d98bd4e8d0a69');
INSERT INTO awf_tag VALUES ('4028b21f7c90d996017c914eec700046','component','z-document-title','90eedeed14494ea4b17d98bd4e8d0a69');
INSERT INTO awf_tag VALUES ('4028b21f7c90d996017c914eecca004a','component','만족도','0986886e23a044659c7bb280347064d2');
INSERT INTO awf_tag VALUES ('4028b21f7c90d996017c914eecd3004b','component','z-satisfaction','0986886e23a044659c7bb280347064d2');
INSERT INTO awf_tag VALUES ('4028b21f7c90d996017c914eecd3000f','component','z-serviceDesk-change-satisfaction','0986886e23a044659c7bb280347064d2');
INSERT INTO awf_tag VALUES ('4028b21f7c90d996017c914eecf9004d','component','만족도평가의견','5e203c7bace44cb58e1c38cee9372404');
INSERT INTO awf_tag VALUES ('4028b21f7c90d996017c914eecfe004e','component','z-satisfaction-content','5e203c7bace44cb58e1c38cee9372404');
/* 인프라변경관리 */
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a5700aea', 'component', '로고', 'b5e06a4da8914e08b63a589fe16578db');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a5720aeb', 'component', 'z-logo', 'b5e06a4da8914e08b63a589fe16578db');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a5810aed', 'component', '제목', '44026fa563f649779f65f52dc0bfd0d6');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a5830aee', 'component', 'z-change-infra-document-title', '44026fa563f649779f65f52dc0bfd0d6');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a5850aef', 'component', 'z-change-infra', '44026fa563f649779f65f52dc0bfd0d6');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a58f0af1', 'component', '', 'a70e12f4514b4377bc9a94eb0f80deeb');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a5a40af4', 'component', '신청자', '6df0771bb8a94deda97d0d6ba42ef01e');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a5a60af5', 'component', 'z-change-infra-requester', '6df0771bb8a94deda97d0d6ba42ef01e');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a5aa0af6', 'component', '신청부서', '1a4024f7913e426383187d6bcc7962af');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a5ac0af7', 'component', 'z-change-infra-requester-department', '1a4024f7913e426383187d6bcc7962af');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a5c50af9', 'component', '전화번호', '11ed46ce208341fd842be00ced0e0381');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a5c70afa', 'component', 'z-change-infra-requester-phone', '11ed46ce208341fd842be00ced0e0381');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a5cb0afb', 'component', '이메일', 'a50504ee81204910a1e13782987b4f89');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a5cd0afc', 'component', 'z-change-infra-requester-email', 'a50504ee81204910a1e13782987b4f89');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a5e20afe', 'component', '신청일시', '610a36dd4d28448f9ca09623962d64e5');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a5e40aff', 'component', 'z-change-infra-request-date', '610a36dd4d28448f9ca09623962d64e5');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a5e80b00', 'component', '완료희망일시', '9bbb34c9d2fe40ba802ee9f674210fe5');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a5ea0b01', 'component', 'z-change-infra-request-deadline', '9bbb34c9d2fe40ba802ee9f674210fe5');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a5fe0b03', 'component', '제목', 'c520f41591ca4c18bc89b08e5b58a78b');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a5ff0b04', 'component', 'z-change-infra-request-title', 'c520f41591ca4c18bc89b08e5b58a78b');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a60c0b06', 'component', '요청 구분', '5aa56cbcd2ab48bbbcffd0ba53af499c');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a60d0b07', 'component', 'z-change-infra-category', '5aa56cbcd2ab48bbbcffd0ba53af499c');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a60e0b08', 'component', 'z-change-infra-request-category', '5aa56cbcd2ab48bbbcffd0ba53af499c');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a61d0b0a', 'component', '내용', '654a2e1cbdf14f39a883ff4ab168046c');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a61e0b0b', 'component', 'z-change-infra-request-content', '654a2e1cbdf14f39a883ff4ab168046c');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a62a0b0d', 'component', '첨부파일', '41ff46f9f19d4e92853118fa1e914877');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a62d0b0e', 'component', 'z-change-infra-request-file', '41ff46f9f19d4e92853118fa1e914877');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a63d0b11', 'component', '접수일시', '2c260851462b40ea82949561ed0f2b68');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a63f0b12', 'component', 'z-change-infra-accept-date', '2c260851462b40ea82949561ed0f2b68');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a64e0b14', 'component', '접수자', '9e79bf1880094800b3b3d68044f40538');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a64f0b15', 'component', 'z-change-infra-acceptor', '9e79bf1880094800b3b3d68044f40538');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a6530b16', 'component', '접수부서', '59ffe940a3464a9ca3683224f7038c41');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a6540b17', 'component', 'z-change-infra-acceptor-department', '59ffe940a3464a9ca3683224f7038c41');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a6670b19', 'component', '처리자', '88660c980418450baf87a6d7ef510626');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a6680b1a', 'component', 'z-change-infra-processor', '88660c980418450baf87a6d7ef510626');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a66c0b1b', 'component', '처리부서', 'f12492917ac64f418c37cfa98c4ab02d');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a66e0b1c', 'component', 'z-change-infra-processor-department', 'f12492917ac64f418c37cfa98c4ab02d');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a6800b1e', 'component', '영향도', '24f2a648a5ba458d9224a61c0cb1cdb0');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a6810b1f', 'component', 'z-change-infra-impact', '24f2a648a5ba458d9224a61c0cb1cdb0');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a6840b20', 'component', '긴급도', '15cd0e737e9c41c5a27132cfa18f370a');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a6850b21', 'component', 'z-change-infra-urgency', '15cd0e737e9c41c5a27132cfa18f370a');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a6960b23', 'component', '접수의견', 'bb38d2e37255417b8f61e0947e224974');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a6980b24', 'component', 'z-change-infra-accept-content', 'bb38d2e37255417b8f61e0947e224974');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a6a70b27', 'component', 'ccb회의일시', 'c7e485828f2b4c85a7cba5d6b3646b16');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a6a90b28', 'component', 'z-change-infra-changeControlBoard-date', 'c7e485828f2b4c85a7cba5d6b3646b16');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a6b60b2a', 'component', 'z-change-infra-changeControlBoard-meetingRoom', '6deb6fcad6124c9a907fc4850285e342');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a6b70b2b', 'component', 'ccb회의장소', '6deb6fcad6124c9a907fc4850285e342');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a6c30b2d', 'component', 'ccb주제', '1d2ea0820d514512a1e3837e04fbc84b');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a6c50b2e', 'component', 'z-change-infra-changeControlBoard-subject', '1d2ea0820d514512a1e3837e04fbc84b');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a6d00b30', 'component', 'ccb협의사항', '2cb51de14f824f62bf9d5f634d276fdc');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a6d20b31', 'component', 'z-change-infra-changeControlBoard-consultation', '2cb51de14f824f62bf9d5f634d276fdc');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a6dd0b33', 'component', 'ccb회의결과', 'c27d14120e1044a5b70fe4372ed78c2e');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a6df0b34', 'component', 'z-change-infra-changeControlBoard-result', 'c27d14120e1044a5b70fe4372ed78c2e');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a6fe0b38', 'component', '변경예정시작일시', 'e6d73057dd5a4b2a8cf01e15d3793eca');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a7000b39', 'component', 'z-change-infra-changePlan-expected-start-date', 'e6d73057dd5a4b2a8cf01e15d3793eca');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a7040b3a', 'component', '변경예정종료일시', 'afe2bec3307442f0be22af203e233f2f');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a7060b3b', 'component', 'z-change-infra-changePlan-expected-end-date', 'afe2bec3307442f0be22af203e233f2f');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a71c0b3d', 'component', '서비스중단', 'f5595e73f0654239aab9e66f71c528cc');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a71d0b3e', 'component', 'z-change-infra-changePlan-service-interruption', 'f5595e73f0654239aab9e66f71c528cc');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a7290b40', 'component', '변경작업계획', 'b16f8424688f4e608d1f7b71f1aaf4b5');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a72b0b41', 'component', 'z-change-infra-changePlan-changeContents', 'b16f8424688f4e608d1f7b71f1aaf4b5');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a7340b43', 'component', '실패시복구방안', '2692ffba732d496498bd2f53f35016f9');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a7360b44', 'component', 'z-change-infra-changePlan-recovery-failure-plan', '2692ffba732d496498bd2f53f35016f9');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a7400b46', 'component', '시험계획', '961037311d5c4201b869c642abdbf394');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a7410b47', 'component', 'z-change-infra-changePlan-planContents', '961037311d5c4201b869c642abdbf394');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a74c0b49', 'component', '기타의견', '3c8cd6bf769041a68c628c1eb1fa2bd2');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a74e0b4a', 'component', 'z-change-infra-changePlan-etc', '3c8cd6bf769041a68c628c1eb1fa2bd2');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a7590b4c', 'component', '변경계획서인프라변경CI', 'f5081a7fd40d46da9b65daf5645906a0');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a75a0b4d', 'component', 'z-change-infra-changePlan-process-relation-ci', 'f5081a7fd40d46da9b65daf5645906a0');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a7640b4f', 'component', '변경계획서첨부파일', '663450c3389c465584685365602dd961');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a7660b50', 'component', 'z-change-infra-changePlan-fileupload', '663450c3389c465584685365602dd961');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a7730b52', 'component', '변경계획서승인자', '2437c9c99fc14cb895f2290519515a7b');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a7750b53', 'component', 'z-change-infra-changePlan-approver', '2437c9c99fc14cb895f2290519515a7b');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a7850b56', 'component', '변경계획서승인일시', '1991ff59e63246ccb13a7946379b14e7');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a7870b57', 'component', 'z-change-infra-changePlan-approve-date', '1991ff59e63246ccb13a7946379b14e7');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a7930b59', 'component', '변경계획서승인의견', '51ac11434d1a4c0ca87b4930ffce047c');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a7940b5a', 'component', 'z-change-infra-changePlan-approve-content', '51ac11434d1a4c0ca87b4930ffce047c');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a7a50b5d', 'component', '변경결과서작성일시', '5e32d98e8bcd4e8e9cc8cdaea9ee45ff');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a7a60b5e', 'component', 'z-change-infra-changeResult-process-date', '5e32d98e8bcd4e8e9cc8cdaea9ee45ff');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a7b10b60', 'component', '변경작업결과', '1c9cb6326b104af88ff79743ce63bcce');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a7b30b61', 'component', 'z-change-infra-result', '1c9cb6326b104af88ff79743ce63bcce');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a7be0b63', 'component', '변경작업결과내용', '45fdeda567e449849075599eaa4a8c06');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a7c10b64', 'component', 'z-change-infra-changeResult-content', '45fdeda567e449849075599eaa4a8c06');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a7cc0b66', 'component', '변경작업결과첨부파일', '3f2ea69c92bd42a5a903b0d8884b19ab');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a7cd0b67', 'component', 'z-change-infra-changeResult-fileupload', '3f2ea69c92bd42a5a903b0d8884b19ab');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a7e10b6a', 'component', 'PIR모니터링시작일시', '061ec27ed5e3446db15b256afb694f7b');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a7e30b6b', 'component', 'z-change-infra-postImplementationReview-start-date', '061ec27ed5e3446db15b256afb694f7b');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a7e70b6c', 'component', 'PIR모니터링종료일시', 'a09757a1c1bb40e381e491474bd8ce75');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a7e90b6d', 'component', 'z-change-infra-postImplementationReview-end-date', 'a09757a1c1bb40e381e491474bd8ce75');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a7fb0b6f', 'component', 'PIR모니터링결과', '6a78f0ee60434ebab14d1b2854057e4c');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a7fc0b70', 'component', 'z-change-infra-postImplementationReview-result', '6a78f0ee60434ebab14d1b2854057e4c');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a8070b72', 'component', 'PIR모니터링내용', '7ce9995e0c2c40c18b8404f73684bd5d');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a8080b73', 'component', 'z-change-infra-postImplementationReview-content', '7ce9995e0c2c40c18b8404f73684bd5d');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a8130b75', 'component', '변경결과서검토자', '9312d808fce84d5fb061a99e6d779c4d');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a8140b76', 'component', 'z-change-infra-complete-reviewer', '9312d808fce84d5fb061a99e6d779c4d');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a8210b79', 'component', '최종검토일시', 'd189734cda47408ca2a33a4d6b8d04b2');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a8220b7a', 'component', 'z-change-infra-complete-review-date', 'd189734cda47408ca2a33a4d6b8d04b2');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a82b0b7c', 'component', '최종검토의견', '60b0b5ffba4047bbb2f2779f40df84ac');
INSERT INTO awf_tag VALUES ('4028b8817cbfc7a7017cc095a82c0b7d', 'component', 'z-change-infra-complete-review', '60b0b5ffba4047bbb2f2779f40df84ac');

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
	token_action varchar(100),
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
COMMENT ON COLUMN wf_token.token_action IS '토큰액션';
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
    attribute_id    character varying(128) NOT NULL UNIQUE,
    attribute_name  character varying(128),
    attribute_desc  character varying(512),
    attribute_type  character varying(100),
    attribute_text  character varying(128),
    attribute_value text,
    mapping_id      character varying(128),
    create_user_key character varying(128),
    create_dt       timestamp,
    update_user_key character varying(128),
    update_dt       timestamp,
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
COMMENT ON COLUMN cmdb_attribute.mapping_id IS '매핑아이디';
COMMENT ON COLUMN cmdb_attribute.create_user_key IS '등록자';
COMMENT ON COLUMN cmdb_attribute.create_dt IS '등록일시';
COMMENT ON COLUMN cmdb_attribute.update_user_key IS '수정자';
COMMENT ON COLUMN cmdb_attribute.update_dt IS '수정일시';

INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('ac4f3785cdbcc149a0b92dbf00af80ef','분류','','inputbox','분류','{"validate":"","required":"true","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d791b75ac01791bb574a70005','자산중요도','자산보안등급정보','inputbox','자산중요도','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('77b6112b3013a6808aeb04f80dd75360','기밀성','자산보안등급정보','dropdown','기밀성','{"option":[{"text":"선택하세요","value":""},{"text":"상","value":"3"},{"text":"중","value":"2"},{"text":"하","value":"1"}]}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('b5f16c33ca0531087ed1b46805a9c682','무결성','자산보안등급정보','dropdown','무결성','{"option":[{"text":"선택하세요","value":""},{"text":"상","value":"3"},{"text":"중","value":"2"},{"text":"하","value":"1"}]}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('072fcb3be4056095a9af82dc6505b1e8','가용성','자산보안등급정보','dropdown','가용성','{"option":[{"text":"선택하세요","value":""},{"text":"상","value":"3"},{"text":"중","value":"2"},{"text":"하","value":"1"}]}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d791b75ac01791bb4b48c0004','평가결과','자산보안등급정보','inputbox','평가결과','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d791b75ac01791b777a240000','외부연동ID','인프라정보','inputbox','외부연동ID','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','zid','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d791b75ac01791b78b0550001','시리얼번호','인프라정보','inputbox','시리얼번호','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','serial','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d791b75ac01791bb0f9140002','담당자(정)','일반정보','inputbox','담당자(정)','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d791b75ac01791bb14a4d0003','담당자(부)','일반정보','inputbox','담당자(부)','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('adaeef4046bfcd78e345ad48cbbeefa5','모델명','인프라정보','inputbox','모델명','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','modal','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('189319790e6349c7248b9f50456ed47b','비고','일반정보','inputbox','비고','{"validate":"","required":"false","maxLength":"10000","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('27caaeba596663101d55a09ec873a375','상태','일반정보 - 인프라','radio','상태','{"option":[{"text":"사용","value":"use"},{"text":"미사용","value":"unused"},{"text":"폐기","value":"disposal"},{"text":"할당","value":"assignment"},{"text":"반납","value":"return"},{"text":"AS","value":"as"},{"text":"예비","value":"spare"}]}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b881792074460179209cef74000c','관리부서','일반정보','inputbox','관리부서','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d7916ef5f017916fc8f1d0004','기간(연평균)','유지보수정보','inputbox','기간(연평균)','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d7916ef5f017916f397230000','취득 일','유지보수정보','inputbox','취득 일','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d7916ef5f017916f7bd590003','유지보수 종료일','유지보수정보','inputbox','유지보수 종료일','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d7916ef5f017916fff6c60006','업무영향 범위','유지보수정보','inputbox','업무영향 범위','{"validate":"","required":"false","maxLength":"300","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d7916ef5f017916f71b030002','유지보수 시작일','유지보수정보','inputbox','유지보수 시작일','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d7916ef5f01791711dc08000b','유지 보수 비상 연락처','유지보수정보','inputbox','유지 보수 비상 연락처','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d7916ef5f0179170ee2c7000a','유지보수 담당자명','유지보수정보','inputbox','유지보수 담당자명','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d7916ef5f0179170df7c80009','유지보수 업체명','유지보수정보','inputbox','유지보수 업체명','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b88179207446017920d8df1c000d','유지보수여부','유지보수정보','dropdown','유지보수여부','{"option":[{"text":"선택하세요","value":""},{"text":"사용","value":"used"},{"text":"미사용","value":"unused"}]}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d7916ef5f017916f594a70001','도입금액','유지보수정보','inputbox','도입금액','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d7916ef5f0179170937d70008','근거자료','유지보수정보','inputbox','근거자료','{"validate":"","required":"false","maxLength":"300","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d7916ef5f01791707c3b70007','이용자수/처리건수','유지보수정보','inputbox','이용자수/처리건수','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d7916ef5f017916fd4e5b0005','예상금액','유지보수정보','inputbox','예상금액','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d791c335201791c7f51d8000f','버전','인프라정보 - 서버 - 운영체제','dropdown','버전','{"option":[{"text":"선택하세요","value":""},{"text":"11i v1","value":"v1"},{"text":"11i v2","value":"v2"},{"text":"11i v3","value":"v3"}]}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d791c335201791c839afd0012','버전','인프라정보 - 서버 - 운영체제','dropdown','버전','{"option":[{"text":"선택하세요","value":""},{"text":"UNIX 3.2","value":"UNIX 3.2"},{"text":"UNIX 4.0","value":"UNIX 4.0"},{"text":"UNIX 5.1","value":"UNIX 5.1"}]}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('734ab921484883ad7760032a008baf21','버전','인프라정보 - 서버 - 운영체제','dropdown','버전','{"option":[{"text":"선택하세요","value":""},{"text":"Debian","value":"debian"},{"text":"Ubuntu","value":"ubuntu"},{"text":"RedHat","value":"redHat"},{"text":"CentOs","value":"centOs"},{"text":"Fedora","value":"fedora"},{"text":"Mint","value":"Mint"}]}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b88179207446017920ed30de0011','HDD','인프라정보 - 노트북','inputbox','HDD','{"validate":"","required":"false","maxLength":"300","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d791c335201791c84a9500013','버전','인프라정보 - 서버 - 운영체제','dropdown','버전','{"option":[{"text":"선택하세요","value":""},{"text":"7.1.0","value":"7.1.0"},{"text":"7.1.1","value":"7.1.1"},{"text":"7.1.2","value":"7.1.2"},{"text":"7.1.3","value":"7.1.3"},{"text":"7.1.4","value":"7.1.4"}]}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d791c335201791c78e61e000e','버전','인프라정보 - 서버 - 운영체제','dropdown','버전','{"option":[{"text":"선택하세요","value":""},{"text":"5.2","value":"5.2"},{"text":"5.3","value":"5.3"},{"text":"6.1","value":"6.1"},{"text":"7.1","value":"7.1"},{"text":"7.2","value":"7.2"}]}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d791c335201791c815b090010','버전','인프라정보 - 서버 - 운영체제','dropdown','버전','{"option":[{"text":"선택하세요","value":""},{"text":"10","value":"10"},{"text":"11.1","value":"11.1"},{"text":"11.2","value":"11.2"},{"text":"11.3","value":"11.3"},{"text":"11.4","value":"11.4"}]}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('df0e88d216ace73e0164f3dbf7ade131','버전','인프라정보 - 서버 - 운영체제','dropdown','버전','{"option":[{"text":"선택하세요","value":""},{"text":"Windows Server 2012","value":"2012"},{"text":"Windows Server 2016","value":"2016"},{"text":"Windows Server 2019","value":"2019"},{"text":"Windows Server 20H2","value":"20H2"}]}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d791c335201791c82c9ca0011','버전','인프라정보 - 서버 - 운영체제','dropdown','버전','{"option":[{"text":"선택하세요","value":""},{"text":"9","value":"9"},{"text":"10","value":"10"},{"text":"11","value":"11"},{"text":"12","value":"12"},{"text":"13","value":"13"}]}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d791c335201791c36103b0000','NIC','인프라정보 - 서버','inputbox','NIC','{"validate":"","required":"false","maxLength":"300","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d7916ef5f01791713d145000d','메모리','인프라정보 - 서버','inputbox','메모리','{"validate":"","required":"false","maxLength":"300","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('d47973f063130acab00b2cf203a9788b','CPU','인프라정보 - 서버','inputbox','CPU','{"validate":"","required":"false","maxLength":"300","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d791b52a501791b5e35ff0000','디스크','인프라정보 - 서버','inputbox','디스크','{"validate":"","required":"false","maxLength":"300","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b8817920744601792089f9390008','솔류션','인프라정보 - 보안솔류션','dropdown','솔류션','{"option":[{"text":"선택하세요","value":""},{"text":"APpCheckEndPointProtection","value":"APpCheckEndPointProtection"},{"text":"AppcheckAnalyzerCloud","value":"AppcheckAnalyzerCloud"},{"text":"CMSCloud","value":"CMSCloud"}]}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b8817920744601792090af45000b','솔류션','인프라정보 - 보안솔류션','dropdown','솔류션','{"option":[{"text":"선택하세요","value":""},{"text":"SSLPLUS","value":"SSLPLUS"},{"text":"XTM","value":"XTM"},{"text":"IPS","value":"IPS"},{"text":"WIPS","value":"WIPS"}]}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b881792074460179208c29bf0009','솔류션','인프라정보 - 보안솔류션','dropdown','솔류션','{"option":[{"text":"선택하세요","value":""},{"text":"Fasoo Enterprise DRM","value":"FasooEnterpriseDRM"},{"text":"Fasoo Data Radar","value":"FasooDataRadar"},{"text":"Fasoo RiskView","value":"FasooRiskView"},{"text":"FED-N","value":"FED-N"},{"text":"FED-R","value":"FED-R"},{"text":"FED-E","value":"FED-E"},{"text":"FED-M","value":"FED-M"},{"text":"FSP","value":"FSP"},{"text":"FSS","value":"FSS"},{"text":"FSW","value":"FSW"},{"text":"FSM","value":"FSM"},{"text":"FXM","value":"FXM"},{"text":"FILM","value":"FILM"}]}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b88179207446017920873e820007','솔류션','인프라정보 - 보안솔류션','dropdown','솔류션','{"option":[{"text":"선택하세요","value":""},{"text":"AppCheck","value":"AppCheck"}]}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b881792074460179207d3f7c0002','솔류션','인프라정보 - 보안솔류션','dropdown','솔류션','{"option":[{"text":"선택하세요","value":""},{"text":"Net-Protect","value":"Net-Protect"},{"text":"OneWay Protect","value":"OneWayProtect"},{"text":"ShellCop","value":"ShellCop"},{"text":"FireMon","value":"FireMon"},{"text":"ZyroidSE","value":"ZyroidSE"},{"text":"Xnexpose","value":"Xnexpose"},{"text":"Metasploit","value":"Metasploit"}]}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b881792074460179207b35a20001','솔류션','인프라정보 - 보안솔류션','dropdown','솔류션','{"option":[{"text":"선택하세요","value":""},{"text":"Cloudera","value":"Cloudera"},{"text":"SecureGuard IM","value":"SecureGuardIM"},{"text":"SecureGuard AM","value":"SecureGuardAM"},{"text":"SecureGuard PM","value":"SecureGuardPM"},{"text":"SecureGurard CCTV PM","value":"SecureGurardCCTVPM"},{"text":"SecureSuard VPN","value":"SecureSuardVPN"},{"text":"SecureGruard OTP","value":"SecureGruardOTP"}]}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b88179207446017920843f560005','솔류션','인프라정보 - 보안솔류션','dropdown','솔류션','{"option":[{"text":"선택하세요","value":""},{"text":"ChangeMiner","value":"ChangeMiner"},{"text":"MetaMiner","value":"MetaMiner"},{"text":"DQMiner","value":"DQMiner"},{"text":"DQXpress","value":"DQXpress"},{"text":"DQ loT","value":"DQloT"},{"text":"SQLMiner","value":"SQLMiner"},{"text":"DataHawk","value":"DataHawk"}]}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b881792074460179208588280006','솔류션','인프라정보 - 보안솔류션','dropdown','솔류션','{"option":[{"text":"선택하세요","value":""},{"text":"uToken","value":"uToken"},{"text":"uTokenHSM","value":"uTokenHSM"},{"text":"Genian NAC","value":"GenianNAC"}]}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d791c335201791c98e06e0016','솔류션','인프라정보 - 보안솔류션','dropdown','솔류션','{"option":[{"text":"선택하세요","value":""},{"text":"BLUEMAX-NGF","value":"BLUEMAX-NGF"},{"text":"BLUEMAX-NFG VE","value":"BLUEMAX-NFG-VE"},{"text":"SECUI MF2","value":"SECUI-MF2"},{"text":"BLUEMAX-WIPS","value":"BLUEMAX-WIPS"},{"text":"BLUEMAX-TAMS","value":"BLUEMAX-TAMS"},{"text":"SECUI TMS","value":"SECUI-TMS"},{"text":"BLUEMAX-LMS","value":"BLUEMAX-LMS"},{"text":"SECUI MFI","value":"SECUI-MFI"},{"text":"SECUI MFD","value":"SECUI-MFD"},{"text":"SECUI MA","value":"SECUI-MA"},{"text":"SECUI SCAN","value":"SECUI-SCAN"}]}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b88179207446017920ee4ac50012','AC 어댑터','인프라정보 - 노트북','inputbox','AC 어댑터','{"validate":"","required":"false","maxLength":"300","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b88179207446017920ec770e0010','RAM','인프라정보 - 노트북','inputbox','RAM','{"validate":"","required":"false","maxLength":"300","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b88179207446017920ef02e10013','그래픽 카드','인프라정보 - 노트북','inputbox','그래픽 카드','{"validate":"","required":"false","maxLength":"300","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d791c335201791c91baa00015','솔류션','인프라정보 - 보안솔류션','dropdown','솔류션','{"option":[{"text":"선택하세요","value":""},{"text":"DB-I","value":"DB-I"},{"text":"WAS-I","value":"WAS-I"},{"text":"App-I","value":"App-I"},{"text":"Mail-I","value":"Mail-I"},{"text":"Privacy-I","value":"Privacy-I"},{"text":"Server-I","value":"Server-I"},{"text":"WebKeeper","value":"WebKeeper"},{"text":"EDR","value":"EDR"},{"text":"Privacy-I Cloud","value":"Privacy-I-Cloud"},{"text":"WebKeeper Cloud","value":"WebKeeper-Cloud"},{"text":"Mail-I Cloud","value":"Mail-I-Cloud"}]}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d791c335201791c88e4b30014','솔류션','인프라정보 - 보안솔류션','dropdown','솔류션','{"option":[{"text":"선택하세요","value":""},{"text":"BigEye","value":"BigEye"},{"text":"nSIEM","value":"nSIEM"},{"text":"nPIS","value":"nPIS"}]}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b8817920744601792079683b0000','솔류션','인프라정보 - 보안솔류션','dropdown','솔류션','{"option":[{"text":"선택하세요","value":""},{"text":"TIP","value":"TIP"},{"text":"MDS","value":"MDS"},{"text":"EPP Patch Management","value":"EPPPatchManagement"},{"text":"EPP Security Assessment","value":"EPPSecurityAssessment"},{"text":"V3 Internet Secuiry 9.0","value":"V3InternetSecuiry9.0"},{"text":"TrusGuard","value":"TrusGuard"},{"text":"TrusGuard DPX","value":"TrusGuardDPX"},{"text":"AIIPS","value":"AIIPS"},{"text":"TMS","value":"TMS"},{"text":"vTrusGruard WAS","value":"vTrusGruardWAS"}]}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b8817920744601792080a98f0003','솔류션','인프라정보 - 보안솔류션','dropdown','솔류션','{"option":[{"text":"선택하세요","value":""},{"text":"SPiDER TM","value":"SPiDERTM"},{"text":"SPiDER AI","value":"SPiDERAI"},{"text":"Smart Guard","value":"SmartGuard"},{"text":"WEBMON","value":"WEBMON"},{"text":"SPiDER LogBox","value":"SPiDERLogBox"},{"text":"SPiDER SOAR","value":"SPiDERSOAR"}]}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b8817920744601792082b3f20004','솔류션','인프라정보 - 보안솔류션','dropdown','솔류션','{"option":[{"text":"선택하세요","value":""},{"text":"Genian NAC","value":"GenianNAC"},{"text":"Genian Cloud NAC","value":"GenianCloudNAC"},{"text":"Genian Insights E","value":"GenianInsightsE"},{"text":"Genian GPI","value":"GenianGPI"}]}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b881792074460179208efdca000a','솔류션','인프라정보 - 보안솔류션','dropdown','솔류션','{"option":[{"text":"선택하세요","value":""},{"text":"WAPPLES","value":"WAPPLES"},{"text":"DAmo","value":"DAmo"},{"text":"CIS-CC","value":"CIS-CC"},{"text":"iSIGN+","value":"iSIGN+"}]}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d791c335201791c6e00bc0009','라이선스','인프라정보 - 데이터베이스','dropdown','라이선스','{"validate":"","required":"false","maxLength":"300","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d791c335201791c5fa3850004','라이선스','인프라정보 - 데이터베이스','dropdown','라이선스','{"validate":"","required":"false","maxLength":"300","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d791c335201791c5dd9240003','라이선스','인프라정보 - 데이터베이스','dropdown','라이선스','{"validate":"","required":"false","maxLength":"300","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d791c335201791c6f7892000a','라이선스','인프라정보 - 데이터베이스','dropdown','라이선스','{"validate":"","required":"false","maxLength":"300","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('489a14a0ebdca14b6eb42cf804330145','버전','인프라정보 - 데이터베이스','inputbox','버전','{"validate":"","required":"false","maxLength":"1000","minLength":"0"}','version','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d791c335201791c5c52970002','라이선스','인프라정보 - 데이터베이스','dropdown','라이선스','{"validate":"","required":"false","maxLength":"300","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d791c335201791c7057ef000b','라이선스','인프라정보 - 데이터베이스','dropdown','라이선스','{"validate":"","required":"false","maxLength":"300","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d791c335201791c71ff45000d','라이선스','인프라정보 - 데이터베이스','dropdown','라이선스','{"validate":"","required":"false","maxLength":"300","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d791c335201791c60844a0005','라이선스','인프라정보 - 데이터베이스','dropdown','라이선스','{"validate":"","required":"false","maxLength":"300","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d791c335201791c711790000c','라이선스','인프라정보 - 데이터베이스','dropdown','라이선스','{"validate":"","required":"false","maxLength":"300","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d791c335201791c617c790006','라이선스','인프라정보 - 데이터베이스','dropdown','라이선스','{"validate":"","required":"false","maxLength":"300","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d791c335201791c62891d0007','라이선스','인프라정보 - 데이터베이스','dropdown','라이선스','{"validate":"","required":"false","maxLength":"300","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d791c335201791c685fef0008','라이선스','인프라정보 - 데이터베이스','dropdown','라이선스','{"validate":"","required":"false","maxLength":"300","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d7a8a5993017a8a5e5aac0000','인스턴스 명','인프라정보 - 데이터베이스','inputbox','인스턴스 명','{"validate":"","required":"false","maxLength":"500","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d7a8a5993017a8a6008e30003','백업 주기','인프라정보 - 데이터베이스','inputbox','백업 주기','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d7a8a5993017a8a5f95d70002','백업 방식','인프라정보 - 데이터베이스','inputbox','백업 방식','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d7a8a5993017a8a60910e0004','보관 주기','인프라정보 - 데이터베이스','inputbox','보관 주기','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d7a8a5993017a8a61b37f0005','보관 위치','인프라정보 - 데이터베이스','inputbox','보관 위치','{"validate":"","required":"false","maxLength":"500","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d7a8a5993017a8a5efdb00001','인스턴스 역할','인프라정보 - 데이터베이스','inputbox','인스턴스 역할','{"validate":"","required":"false","maxLength":"500","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b88179207446017920eb4f63000f','OS','인프라정보 - 노트북','inputbox','OS','{"validate":"","required":"true","maxLength":"300","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b88179207446017920f09cc90014','CPU','인프라정보 - 노트북','inputbox','CPU','{"validate":"","required":"true","maxLength":"300","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d791b75ac01791bc0ffe3000a','IOS Version','인프라정보 - 네트워크','inputbox','IOS Version','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','iosver','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d791b75ac01791bbb1ed10006','인터페이스정보','인프라정보 - 네트워크','inputbox','인터페이스정보','{"validate":"","required":"false","maxLength":"300","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d791b75ac01791bbd4e5b0007','SNMP버전','인프라정보 - 네트워크','inputbox','SNMP버전','{"validate":"","required":"false","maxLength":"300","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d791b75ac01791bbe677f0008','전체IF개수','인프라정보 - 네트워크','inputbox','전체IF개수','{"validate":"number","required":"false","maxLength":"1000","minLength":"0"}','totifnum','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d791b75ac01791bc0759f0009','사용IF개수','인프라정보 - 네트워크','inputbox','사용IF개수','{"validate":"number","required":"false","maxLength":"1000","minLength":"0"}','actifnum','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d791b52a501791b61f7170001','지역','인프라정보 - 공통','inputbox','지역','{"validate":"","required":"false","maxLength":"300","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d7916ef5f017917129c66000c','IP','인프라정보 - 공통','inputbox','IP','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','ip','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d791b52a501791b71ff790003','건물명','인프라정보 - 공통','inputbox','건물명','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d791b52a501791b72ccbc0004','층','인프라정보 - 공통','inputbox','층','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d791b52a501791b73fff90005','랙위치','인프라정보 - 공통','inputbox','랙위치','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('6ea67d6c6cb28def6b289affc6c95fd1','MAC','인프라정보 - 공통','inputbox','MAC','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','mac','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b88179210e1b01792155cae80020','ICMP 정보','인프라상세 - 네트워크','inputbox','ICMP 정보','{"validate":"","required":"false","maxLength":"300","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b88179210e1b01792156af0c0022','IPCHECK 정보','인프라상세 - 네트워크','inputbox','IPCHECK 정보','{"validate":"","required":"false","maxLength":"300","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b88179210e1b0179215883dd0025','L4SWITCH 정보','인프라상세 - 네트워크','inputbox','L4SWITCH 정보','{"validate":"","required":"false","maxLength":"300","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b88179210e1b017921555a73001f','OID 정보','인프라상세 - 네트워크','inputbox','OID 정보','{"validate":"","required":"false","maxLength":"300","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b88179210e1b017921563ea00021','SNMP 정보','인프라상세 - 네트워크','inputbox','SNMP 정보','{"validate":"","required":"false","maxLength":"300","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b88179210e1b0179215719fb0023','L2SWITCH 정보','인프라상세 - 네트워크','inputbox','L2SWITCH 정보','{"validate":"","required":"false","maxLength":"300","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b88179210e1b017921581a750024','L3SWITCH 정보','인프라상세 - 네트워크','inputbox','L3SWITCH 정보','{"validate":"","required":"false","maxLength":"300","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b88179210e1b01792159017a0026','APETC 정보','인프라상세 - 네트워크','inputbox','APETC 정보','{"validate":"","required":"false","maxLength":"300","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b88179210e1b01792159687f0027','IPS 정보','인프라상세 - 네트워크','inputbox','IPS 정보','{"validate":"","required":"false","maxLength":"300","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b88179207446017920f72ed30015','분류','보안장비 - 설비정보','radio','분류','{"option":[{"text":"서버","value":"sms"},{"text":"네트워크","value":"nms"},{"text":"기타","value":"etc"}]}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b8817a8eeaa3017a8f0b263e0000','구매(건)','소프트웨어정보','inputbox','구매(건)','{"validate":"number","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b8817a8eeaa3017a8f0b97760001','설치(건)','소프트웨어정보','inputbox','설치(건)','{"validate":"number","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b8817a8eeaa3017a8f0c231a0002','공급사','소프트웨어정보','inputbox','공급사','{"validate":"","required":"false","maxLength":"1000","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b8817a8eeaa3017a8f0c78660003','라이선스 시작일','소프트웨어정보','inputbox','라이선스 시작일','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b8817a8eeaa3017a8f0d0aec0004','라이선스 종료일','소프트웨어정보','inputbox','라이선스 종료일','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b8817a8eeaa3017a8f0d5e0d0005','금액','소프트웨어정보','inputbox','금액','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b8817a8eeaa3017a8f282cc10007','업체명','계약정보관리','inputbox','업체명','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b8817a8eeaa3017a8f288f020008','계약명','계약정보관리','inputbox','계약명','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b8817a8eeaa3017a8f297d640009','계약일','계약정보관리','inputbox','계약일','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b8817a8eeaa3017a8f29d2ac000a','계약시작일','계약정보관리','inputbox','계약시작일','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b8817a8eeaa3017a8f2a6796000b','계약종료일','계약정보관리','inputbox','계약종료일','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b8817a8eeaa3017a8f2ad6ea000c','계약방법','계약정보관리','inputbox','계약방법','{"validate":"","required":"false","maxLength":"500","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b8817a8eeaa3017a8f2b611f000d','계약금액','계약정보관리','inputbox','계약금액','{"validate":"number","required":"false","maxLength":"1000","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b8817a8eeaa3017a8f2cab18000e','계약상세내역','계약정보관리','inputbox','계약상세내역','{"validate":"char","required":"false","maxLength":"1000","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b8817a8eeaa3017a8f2d01d6000f','예산코드','계약정보관리','inputbox','예산코드','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b8817a8eeaa3017a8f2d60300010','계약 담당부서','계약정보관리','inputbox','계약 담당부서','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b8817a8eeaa3017a8f2e573f0011','계약 담당자','계약정보관리','custom-code','계약 담당자','{"required":"false","customCode":"40288a19736b46fb01736b89e46c0008","default":{"type":"session","value":"userName"},"button":"검색"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b8817a8f3542017a8f5b51700001','년도','사업정보관리','inputbox','년도','{"validate":"","required":"true","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b8817a8f3542017a8f6123360002','처리상태','사업정보관리','dropdown','처리상태','{"option":[{"text":"선택하세요.","value":""},{"text":"정상","value":"normal"},{"text":"취소","value":"cancel"},{"text":"대기","value":"waiting"},{"text":"전산실무협의회","value":"computing working group"},{"text":"전산운영위원회","value":"computing steering committee"},{"text":"RFI발송","value":"rfisent"},{"text":"RFI접수","value":"rfireceipt"},{"text":"RFP발송","value":"rfpsent"},{"text":"RFP접수","value":"rfpreceipt"},{"text":"제안평가","value":"proposalevaluation"},{"text":"입찰발송","value":"sendbid"},{"text":"입찰등록","value":"bidregistration"},{"text":"입찰실시","value":"bidding"},{"text":"계약완료","value":"contractcompletion"},{"text":"사업추진중","value":"businessinprogress"},{"text":"최종검수완료","value":"finalinspectioncompleted"},{"text":"최종대금지금완료","value":"finalpaymentcompleted"}]}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b8817a8f3542017a8f62f3d10003','구분','사업정보관리','dropdown','구분','{"option":[{"text":"선택하세요.","value":""},{"text":"전년도 계속사업","value":"beforeyear"},{"text":"본부부서 추진사업","value":"headquarters"},{"text":"전산정보부 추진사업","value":"computerinformation"}]}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b8817a8f3542017a8f63cdf60004','우선순위','사업정보관리','inputbox','우선순위','{"validate":"number","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b8817a8f3542017a8f6451ec0005','주관부서','사업정보관리','inputbox','주관부서','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b8817a8f3542017a8f64b62c0006','사업추진일정 시작월','사업정보관리','inputbox','사업추진일정 시작월','{"validate":"number","required":"false","maxLength":"10","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b8817a8f3542017a8f6587d60007','사업추진일정 종료월','사업정보관리','inputbox','사업추진일정 종료월','{"validate":"number","required":"false","maxLength":"10","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b8817a8f3542017a8f6697260008','소요예산 합계','사업정보관리','inputbox','소요예산 합계','{"validate":"","required":"false","maxLength":"10000","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b8817a8f3542017a8f6b9b180009','정보보호 사업구분','사업정보관리','radio','정보보호 사업구분','{"option":[{"text":"해당없음","value":"none"},{"text":"전체","value":"all"},{"text":"부분","value":"part"}]}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b8817a8f3542017a8f6c041c000a','정보보호 사업기준','사업정보관리','inputbox','정보보호 사업기준','{"validate":"","required":"false","maxLength":"1000","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b8817a8f3542017a8f6c8877000b','정보보호예산 합계','사업정보관리','inputbox','정보보호예산 합계','{"validate":"","required":"false","maxLength":"1000","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b8817a8f3542017a8f6d490a000c','전산실무협의회 심의대상 여부','사업정보관리','radio','전산실무협의회 심의대상 여부','{"option":[{"text":"예","value":"yes"},{"text":"아니오","value":"no"}]}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b8817a8f3542017a8f6db3b8000d','전산실무협의회 심의완료일','사업정보관리','inputbox','전산실무협의회 심의완료일','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b8817a8f3542017a8f6e6999000e','전산운영위원회 심의완료일','사업정보관리','inputbox','전산운영위원회 심의완료일','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b8817a8f3542017a8f6f4ff6000f','RFI 발송여부','사업정보관리','radio','RFI 발송여부','{"option":[{"text":"예","value":"yes"},{"text":"아니오","value":"no"}]}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b8817a8f3542017a8f7524b40010','전산운영위원회 심의대상여부','사업정보관리','radio','전산운영위원회 심의대상여부','{"option":[{"text":"예","value":"yes"},{"text":"아니오","value":"no"}]}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b8817a8f3542017a8f7610e60011','RFI 발송일자','사업정보관리','inputbox','RFI 발송일자','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b8817a8f3542017a8f812a9c0012','RFI 접수일자','사업정보관리','inputbox','RFI 접수일자','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b8817a8f3542017a8f81bf150013','RFI 발송대상업체','사업정보관리','inputbox','RFI 발송대상업체','{"validate":"","required":"false","maxLength":"1000","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b8817a8f3542017a8f821bfc0014','RFP 발송일자','사업정보관리','inputbox','RFP 발송일자','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b8817a8f3542017a8f827a9a0015','RFP 접수일자','사업정보관리','inputbox','RFP 접수일자','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b8817a8f3542017a8f82e2990016','RFP 발송업체','사업정보관리','inputbox','RFP 발송업체','{"validate":"","required":"false","maxLength":"1000","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b8817a8f3542017a8f83df0c0017','제안설명회 일자','사업정보관리','inputbox','제안설명회 일자','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b8817a8f3542017a8f84329c0018','제안평가 일자','사업정보관리','inputbox','제안평가 일자','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b8817a8f3542017a8f8551c80019','예산배정방법','사업정보관리','radio','예산배정방법','{"option":[{"text":"정상배정","value":"normal"},{"text":"타사업에예산전용","value":"other"},{"text":"정상배정+타사업예산전용","value":"normalother"}]}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b8817a8f3542017a8f860fb7001a','추진사유','사업정보관리','inputbox','추진사유','{"validate":"","required":"false","maxLength":"1000","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b8817a8f3542017a8f8669aa001b','미추진사유','사업정보관리','inputbox','미추진사유','{"validate":"","required":"false","maxLength":"1000","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('e613591ddea0f8c1f2457104f7cf286d','장비명','','inputbox','장비명','{"validate":"","required":"true","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('bde6f4eac279ac3528c9cca16d12779a','데이터베이스','','custom-code','데이터베이스','{"required":"true","customCode":"40288ab777dd21b50177dd52781e0000","default":{"type":"code","value":"cmdb.db.kind.altibase|altibase"},"button":"검색"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('ef60a5a1aa010de9b7ba2dda96107c5d','Processor','','inputbox','Processor','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('c1f97be1aea3fdee785ca73b751f79d8','수량','','inputbox','수량','{"validate":"number","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('247aa7187b335f9c4d78db5e18a2704c','브랜드','','inputbox','브랜드','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('2bb03c41cd9998e77da9b737d4fcf9ab','bash 버전','','inputbox','bash 버전','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('e651113f8a452f55f50ed41956cdfb34','버전','','inputbox','버전','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('58e0cd57479bbb9d8a6b2bb6012206c2','설치장소','','inputbox','설치장소','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('602b2c9216825bffc96ae69eeb73bdbc','도입일','','inputbox','도입일','{"validate":"","required":"true","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('d0a35c07fa9bdd919a039f1f127cd54e','보호수준','','dropdown','보호수준','{{"text":"선택하세요","value":""},"option":[{"text":"가 등급","value":"3"},{"text":"나 등급","value":"2"},{"text":"다 등급","value":"1"}]}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('f4538a0d55c456461f1d0932fd424350','RAM','','inputbox','RAM','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('42b02142dd9128e47a35b737d4fc21ad','서비스명','','inputbox','서비스명','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('799afe719cd0bfe38797172bb77ae5d8','라이선스 정책','','dropdown','라이선스 정책','{"option":[{"text":"선택하세요","value":""},{"text":"무료","value":"free"},{"text":"유료","value":"pay"}]}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('932831a8e53aa6f795f608794e51e7e0','IP_V6','','inputbox','IP_V6','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('a508fbfda5d65a54b9b25dc5925d79bb','관리자','','custom-code','관리자','{"required":"true","customCode":"40288a19736b46fb01736b89e46c0008","default":{"type":"session","value":"userName"},"button":"검색"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('1d1338bb6316ab81f7c6adbc77199409','제조사','','inputbox','제조사','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('addc07e374faec9f0d6d3bbeca565886','OS 종류','','dropdown','OS 종류','{"option":[{"text":"선택하세요","value":""},{"text":"common","value":"common"},{"text":"Linux","value":"linux"},{"text":"FreeBSD","value":"freebsd"},{"text":"Solaris","value":"solaris"},{"text":"AIX","value":"aix"},{"text":"HPUX","value":"hpux"},{"text":"WinNT","value":"winnt"}]}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('79a99dfa69d7d0c5c369ad4840815749','IP_V4','','inputbox','IP_V4','{"validate":"","required":"false","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('99a8cf26726e907a95dad34e188cbfc8','등급','','dropdown','등급','{"option":[{"text":"선택하세요","value":""},{"text":"1등급","value":"1"},{"text":"2등급","value":"2"},{"text":"3등급","value":"3"}]}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('6e247bdb7b70757e1987ae25a36c3d13','호스트명','','inputbox','호스트명','{"validate":"","required":"true","maxLength":"100","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
INSERT INTO cmdb_attribute (attribute_id,attribute_name,attribute_desc,attribute_type,attribute_text,attribute_value,mapping_id,create_user_key,create_dt,update_user_key,update_dt) VALUES ('4028b25d791b52a501791b62eb220002','용도','','inputbox','용도','{"validate":"","required":"false","maxLength":"300","minLength":"0"}','','0509e09412534a6e98f04ca79abb6424','2021-09-08 17:43:14.049445',NULL,NULL);
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
	class_seq int,
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
COMMENT ON COLUMN cmdb_class.class_seq IS '클래스정렬순서';
COMMENT ON COLUMN cmdb_class.create_user_key IS '등록자';
COMMENT ON COLUMN cmdb_class.create_dt IS '등록일시';
COMMENT ON COLUMN cmdb_class.update_user_key IS '수정자';
COMMENT ON COLUMN cmdb_class.update_dt IS '수정일시';

insert into cmdb_class values ('root', 'root', 'root', null, 0, 0, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b881792074460179210677bb0016', '일반정보 - 인프라', '일반정보 - 인프라 Class입니다.', 'root', 1, 10, '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_class values ('4028b88179210e1b017921740c250043', '일반정보 - 소프트웨어', '일반정보 - 소프트웨어 Class입니다.', 'root', 1, 20, '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_class values ('4028b8817a8f3542017a8f38b60d0000', '계약정보관리', '계약정보관리 Class입니다.', 'root', 1, 30, '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_class values ('4028b8817a8f3542017a8f9c365e001c', '사업정보관리', '사업정보관리 Class입니다.', 'root', 1, 40, '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_class values ('4028b88179210e1b0179217130030041', '일반정보 - 노트북', '일반정보 - 노트북 Class입니다.', 'root', 1, 50, '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_class values ('4028b8817920744601792109b4800017', '위치정보 - 인프라', '인프라정보 위치정보 Class입니다.', '4028b881792074460179210677bb0016', 2, 10, '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_class values ('4028b88179210e1b0179217798830044', '유지보수정보 - 소프트웨어', '유지보수정보 - 소프트웨어 Class입니다.', '4028b88179210e1b017921740c250043', 2, 10, '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_class values ('4028b88179210e1b01792172d1b80042', '자산정보 - 노트북', '자산정보 - 노트북 Class입니다.', '4028b88179210e1b0179217130030041', 2, 10, '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_class values ('4028b88179210e1b0179211490200003', '유지보수정보 - 인프라', '인프라정보 유지보수 Class입니다.', '4028b8817920744601792109b4800017', 3, 10, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b8817a8eeaa3017a8f15873f0006', '소프트웨어', '소프트웨어 Class입니다.', '4028b88179210e1b0179217798830044', 3, 10,  '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_class values ('4028b88179210e1b01792117aab10004', '자산보안등급정보 - 인프라', '인프라 자산보안등급 Class입니다.', '4028b88179210e1b0179211490200003', 4, 10, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179211d13760005', '인프라정보 - 서버', '인프라 - 서버 Class입니다.', '4028b88179210e1b01792117aab10004', 5, 10, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179212f17f90011', '인프라정보 - 네트워크', '인프라 - 네트워크 Class입니다.', '4028b88179210e1b01792117aab10004', 5, 20, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b017921336fc60012', '인프라정보 - 데이터베이스', '인프라정보 - 데이터베이스 Class입니다.', '4028b88179210e1b01792117aab10004', 5, 30, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b01792160f3010031', '인프라정보 - 보안장비', '인프라정보 - 보안장비 Class입니다.', '4028b88179210e1b01792117aab10004', 5, 40, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179216b6d55003b', '채울', '채울 Class입니다.', '4028b88179210e1b01792160f3010031', 6, 10, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179216bf1a6003c', '체크멀', '체크멀 Class입니다.', '4028b88179210e1b01792160f3010031', 6, 20, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179216cf921003d', '코닉글로리', '코닉글로리 Class입니다.', '4028b88179210e1b01792160f3010031', 6, 30, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179216e6e35003e', '펜타시규리티', '펜타시규리티 Class입니다.', '4028b88179210e1b01792160f3010031', 6, 40, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179216226d30032', '넷크루즈', '넷크루즈 Class입니다.', '4028b88179210e1b01792160f3010031', 6, 50, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b01792162e60f0033', '소만사', '소만사 Class입니다.', '4028b88179210e1b01792160f3010031', 6, 60, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b017921643b830034', '시큐아이', '시큐아이 Class입니다.', '4028b88179210e1b01792160f3010031', 6, 70, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b01792165deef0036', '에스지엔', '에스지엔 Class입니다.', '4028b88179210e1b01792160f3010031', 6, 80, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b01792166dba20037', '에스큐브아이', '에스큐브아이 Class입니다.', '4028b88179210e1b01792160f3010031', 6, 90, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b01792167e5f30038', '이글루시큐리티', '이글루시큐리티 Class입니다.', '4028b88179210e1b01792160f3010031', 6, 100, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179216af1c9003a', '지티원 ', '지티원 Class입니다.', '4028b88179210e1b01792160f3010031', 6, 110, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179216f07b2003f', '퓨처시스템', '퓨처시스템 Class입니다.', '4028b88179210e1b01792160f3010031', 6, 120, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179216f773b0040', '파수닷컴', '파수닷컴 Class입니다.', '4028b88179210e1b01792160f3010031', 6, 130, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179216a760a0039', '지니안', '지이안 Class입니다.', '4028b88179210e1b01792160f3010031', 6, 140, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b017921651e3d0035', '안랩', '안랩 Class입니다.', '4028b88179210e1b01792160f3010031', 6, 150, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179211eb65d0006', 'Linux', 'Linux Class입니다.', '4028b88179210e1b0179211d13760005', 6, 10, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b01792123742e0009', 'HPUX', 'HPUX Class입니다.', '4028b88179210e1b0179211d13760005', 6, 20, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b017921201e8f0007', 'WinNT ', 'WinNT Class입니다.', '4028b88179210e1b0179211d13760005', 6, 30, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b017921242450000a', 'Solaris', 'Solaris Class입니다.', '4028b88179210e1b0179211d13760005', 6, 40, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b017921261f87000c', 'TRU64', 'TRU64 Class입니다.', '4028b88179210e1b0179211d13760005', 6, 50, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b01792126a13a000d', 'UNIXWARE', 'UNIXWARE Class입니다.', '4028b88179210e1b0179211d13760005', 6, 60, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b01792121eea80008', 'AIX', 'AIX Class입니다.', '4028b88179210e1b0179211d13760005', 6, 70, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b01792125a23a000b', 'FreeBSD', 'FreeBSD Class입니다.', '4028b88179210e1b0179211d13760005', 6, 80, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b017921362c960013', 'Oracle', 'Oracle Class입니다.', '4028b88179210e1b017921336fc60012', 6, 10, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b01792137989f0016', 'MS-SQL', 'MS-SQL Class입니다.', '4028b88179210e1b017921336fc60012', 6, 20, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b01792136918f0014', 'MySQL', 'MySQL Class입니다.', '4028b88179210e1b017921336fc60012', 6, 30, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b017921370e000015', 'Maria DB', 'Maria DB Class입니다.', '4028b88179210e1b017921336fc60012', 6, 40, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179213baac3001a', 'SQLite', 'SQLite Class입니다.', '4028b88179210e1b017921336fc60012', 6, 50, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179213a11f80018', 'Infomix', 'Infomix Class입니다.', '4028b88179210e1b017921336fc60012', 6, 60, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179213b50f50019', 'Derby', 'Derby Class입니다.', '4028b88179210e1b017921336fc60012', 6, 70, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179213cfac9001d', 'HBase', 'HBase Class입니다.', '4028b88179210e1b017921336fc60012', 6, 80, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179213d8436001e', 'Cassandra', 'Cassandra Class입니다.', '4028b88179210e1b017921336fc60012', 6, 90,'0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179213972140017', 'DB2', 'DB2 Class입니다.', '4028b88179210e1b017921336fc60012', 6, 100, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179213c2099001b', 'MongoDB', 'MongoDB Class입니다.', '4028b88179210e1b017921336fc60012', 6, 110, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179213ca263001c', 'Redis', 'Redis Class입니다.', '4028b88179210e1b017921336fc60012', 6, 120, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179215a20da0028', 'OID', 'OID Class입니다.', '4028b88179210e1b0179212f17f90011', 6, 10, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179215a98930029', 'ICMP', 'ICMP Class입니다.', '4028b88179210e1b0179212f17f90011', 6, 20, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179215b19bb002a', 'SNMP', 'SNMP Class입니다.', '4028b88179210e1b0179212f17f90011', 6, 30, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179215c07b9002c', 'L2SWITCH', 'L2SWITCH Class입니다.', '4028b88179210e1b0179212f17f90011', 6, 40, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179215c8495002d', 'L3SWITCH', 'L3SWITCH Class입니다.', '4028b88179210e1b0179212f17f90011', 6, 50, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179215ce42d002e', 'L4SWITCH', 'L4SWITCH Class입니다.', '4028b88179210e1b0179212f17f90011', 6, 60, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179215b8bff002b', 'IPCHECK', 'IPCHECK Class입니다.', '4028b88179210e1b0179212f17f90011', 6, 70, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179215d4b68002f', 'APETC', 'APETC Class입니다.', '4028b88179210e1b0179212f17f90011', 6, 80, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179215dbbf80030', 'IPS', 'IPS Class입니다.', '4028b88179210e1b0179212f17f90011', 6, 90, '0509e09412534a6e98f04ca79abb6424',now());
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
	type_seq int,
	class_id character varying(128) NOT NULL,
	type_icon character varying(200),
	create_user_key character varying(128),
	create_dt timestamp,
	update_user_key character varying(128),
	update_dt timestamp,
	CONSTRAINT cmdb_type_pk PRIMARY KEY (type_id),
	CONSTRAINT cmdb_type_uk UNIQUE (type_id),
	CONSTRAINT cmdb_type_fk FOREIGN KEY (class_id)
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
COMMENT ON COLUMN cmdb_type.type_seq IS '타입정렬순서';
COMMENT ON COLUMN cmdb_type.class_id IS '클래스아이디';
COMMENT ON COLUMN cmdb_type.type_icon IS '타입아이콘';
COMMENT ON COLUMN cmdb_type.create_user_key IS '등록자';
COMMENT ON COLUMN cmdb_type.create_dt IS '등록일시';
COMMENT ON COLUMN cmdb_type.update_user_key IS '수정자';
COMMENT ON COLUMN cmdb_type.update_dt IS '수정일시';

insert into cmdb_type values ('root', null, 'ROOT', null, 'CI', 0, 0, 'root', null, '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b017921277022000e', 'root', '서버', '서버입니다.', 'SERVER', 1, 10, '4028b88179210e1b0179211d13760005', 'image_server.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b0179217bb335004b', 'root', '네트워크', '네트워크 타입입니다.', 'NETWORK', 1, 20, '4028b88179210e1b0179212f17f90011', 'image_icmp.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b0179218fb2070055', 'root', '데이터베이스', '데이터베이스 Type입니다.', 'DATABASE', 1, 30, '4028b88179210e1b017921336fc60012', 'image_l4switch.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b0179219878cd0062', 'root', '보안장비', '보안장비 Type입니다.', 'security', 1, 40, '4028b88179210e1b01792160f3010031', 'image_snmp.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b017921a6b6950073', 'root', '소프트웨어', '소프트웨어 Type입니다.', 'software', 1, 50, '4028b8817a8eeaa3017a8f15873f0006', 'image_winnt.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b017921a6b6951174', 'root', '사업', '사업 Type입니다.', 'business', 1, 60, '4028b8817a8f3542017a8f9c365e001c', 'image_winnt.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b017921a6b6950173', 'root', '계약', '계약 Type입니다.', 'contract', 1, 70, '4028b8817a8f3542017a8f38b60d0000', 'image_winnt.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b017921a5b57a0072', 'root', '노트북', '노트북 Type입니다.', 'laptop', 1, 80, '4028b88179210e1b01792172d1b80042', 'image_server.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b017921a113fe006c', '4028b88179210e1b0179219878cd0062', '채울', '채울 Type입니다.', 'CHAEWOOL', 2, 10, '4028b88179210e1b0179216b6d55003b', 'image_snmp.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b017921a18a1b006d', '4028b88179210e1b0179219878cd0062', '체크멀', '체크멀  Type입니다.', 'CHECKMAL', 2, 20, '4028b88179210e1b0179216bf1a6003c', 'image_snmp.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b017921a230e4006e', '4028b88179210e1b0179219878cd0062', '코닉글로리', '코닉글로리 Type입니다.', 'KORINCGLORY', 2, 30, '4028b88179210e1b0179216cf921003d', 'image_snmp.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b017921a3f3320071', '4028b88179210e1b0179219878cd0062', '펜타시규리티', '펜타시규리티 Type입니다.', 'Penta', 2, 40, '4028b88179210e1b0179216e6e35003e', 'image_snmp.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b01792199e1d80063', '4028b88179210e1b0179219878cd0062', '넷크루즈', '넷크루즈', 'Netcruz', 2, 50, '4028b88179210e1b0179216226d30032', 'image_snmp.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b0179219c3f0a0064', '4028b88179210e1b0179219878cd0062', '소만사', '소만사 Type입니다.', 'Somansa', 2, 60, '4028b88179210e1b01792162e60f0033', 'image_snmp.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b0179219cf81d0065', '4028b88179210e1b0179219878cd0062', '시큐아이', 'SECUI Type입니다.', 'SECUI', 2, 70, '4028b88179210e1b017921643b830034', 'image_snmp.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b0179219dcf9d0067', '4028b88179210e1b0179219878cd0062', '에스지엔', '에스지엔 Type입니다.', 'SGN', 2, 80, '4028b88179210e1b01792165deef0036', 'image_snmp.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b0179219e84340068', '4028b88179210e1b0179219878cd0062', '에스큐브아이', '에스큐브아이 Type입니다.', 'S3I', 2, 90, '4028b88179210e1b01792166dba20037', 'image_snmp.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b0179219f04020069', '4028b88179210e1b0179219878cd0062', '이글루시큐리티', '이글루시큐리티 Type입니다.', 'IGLOO', 2, 100, '4028b88179210e1b01792167e5f30038', 'image_snmp.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b017921a097f5006b', '4028b88179210e1b0179219878cd0062', '지티원', '지티원 Type입니다.', 'GTONE', 2, 110, '4028b88179210e1b0179216af1c9003a', 'image_snmp.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b017921a355c10070', '4028b88179210e1b0179219878cd0062', '퓨처시스템', '퓨처시스템 Type입니다.', 'FutureSystem', 2, 120, '4028b88179210e1b0179216f07b2003f', 'image_snmp.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b017921a2b43a006f', '4028b88179210e1b0179219878cd0062', '파수닷컴', '파수닷컴 Type입니다.', 'FASOO', 2, 130, '4028b88179210e1b0179216f773b0040', 'image_snmp.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b0179219f9e5c006a', '4028b88179210e1b0179219878cd0062', '지니안', '지니안 Type입니다.', 'Genians', 2, 140, '4028b88179210e1b0179216a760a0039', 'image_snmp.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b0179219d58e20066', '4028b88179210e1b0179219878cd0062', '안랩', '안랩 Type입니다.', 'Ahnlab', 2, 150, '4028b88179210e1b017921651e3d0035', 'image_snmp.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b017921279d29000f', '4028b88179210e1b017921277022000e', 'Linux ', 'Linux Type입니다.', 'Linux', 2, 10, '4028b88179210e1b0179211eb65d0006', 'image_linux.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b0179217894fd0046', '4028b88179210e1b017921277022000e', 'HPUX', 'HPUX Type입니다.', 'HPUX', 2, 20, '4028b88179210e1b01792123742e0009', 'image_linux.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b01792127ed690010', '4028b88179210e1b017921277022000e', 'WinNT', 'WinNT Type입니다.', 'WinNT', 2, 30, '4028b88179210e1b017921201e8f0007', 'image_winnt.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b0179217906d10047', '4028b88179210e1b017921277022000e', 'Solaris', 'Solaris Type입니다.', 'Solaris', 2, 40, '4028b88179210e1b017921242450000a', 'image_linux.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b0179217aa9660049', '4028b88179210e1b017921277022000e', 'TRU64', 'TRU64 Type입니다.', 'TRU64', 2, 50, '4028b88179210e1b017921261f87000c', 'image_linux.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b0179217b3cfb004a', '4028b88179210e1b017921277022000e', 'UNIXWARE', 'UNIXWARE Type입니다.', 'UNIXWARE', 2, 60, '4028b88179210e1b01792126a13a000d', 'image_linux.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b0179217837510045', '4028b88179210e1b017921277022000e', 'AIX', 'AIX Type입니다.', 'AIX', 2, 70, '4028b88179210e1b01792121eea80008', 'image_linux.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b01792179d7e30048', '4028b88179210e1b017921277022000e', 'FreeBSD', 'FreeBSD Type입니다.', 'FreeBSD', 2, 80, '4028b88179210e1b01792125a23a000b', 'image_linux.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b0179219012c40056', '4028b88179210e1b0179218fb2070055', 'Oracle', 'Oracle Type입니다.', 'Oracle', 2, 10, '4028b88179210e1b017921362c960013', 'image_oracle.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b017921934c300059', '4028b88179210e1b0179218fb2070055', 'MS-SQL', 'MS-SQL Type입니다.', 'MSSQL', 2, 20, '4028b88179210e1b01792137989f0016', 'image_mssql.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b01792190905b0057', '4028b88179210e1b0179218fb2070055', 'MySQL', 'MySQL Type입니다.', 'MySQL', 2, 30, '4028b88179210e1b01792136918f0014', 'image_mysql.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b017921925bd60058', '4028b88179210e1b0179218fb2070055', 'MariaDB', 'Maria DB Type입니다.', 'MariaDB', 2, 40, '4028b88179210e1b017921370e000015', 'image_mariadb.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b017921952e81005d', '4028b88179210e1b0179218fb2070055', 'SQLite', 'SQLite Type입니다.', 'SQLite', 2, 50, '4028b88179210e1b0179213baac3001a', 'image_l4switch.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b017921942970005b', '4028b88179210e1b0179218fb2070055', 'Infomix', 'Infomix Type입니다.', 'Infomix', 2, 60, '4028b88179210e1b0179213a11f80018', 'image_l4switch.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b017921947c2e005c', '4028b88179210e1b0179218fb2070055', 'Derby', 'Derby Type입니다.', 'Derby', 2, 70, '4028b88179210e1b0179213b50f50019', 'image_l4switch.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b017921962d8d0060', '4028b88179210e1b0179218fb2070055', 'HBase', 'HBase Type입니다.', 'HBase', 2, 80, '4028b88179210e1b0179213cfac9001d', 'image_l4switch.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b01792196eb490061', '4028b88179210e1b0179218fb2070055', 'Cassandra', 'Cassandra Type입니다.', 'Cassandra', 2, 90,'4028b88179210e1b0179213d8436001e', 'image_l4switch.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b01792193da25005a', '4028b88179210e1b0179218fb2070055', 'DB2', 'DB2 Type입니다.', 'DB2', 2, 100, '4028b88179210e1b0179213972140017', 'image_l4switch.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b017921957a67005e', '4028b88179210e1b0179218fb2070055', 'MongoDB', 'MongoDB Type입니다.', 'MongoDB', 2, 110, '4028b88179210e1b0179213c2099001b', 'image_l4switch.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b01792195e8be005f', '4028b88179210e1b0179218fb2070055', 'Redis', 'Redis Type입니다.', 'Redis', 2, 120, '4028b88179210e1b0179213ca263001c', 'image_l4switch.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b0179217cc893004c', '4028b88179210e1b0179217bb335004b', 'OID', 'OID Type입니다.', 'OID', 2, 10, '4028b88179210e1b0179215a20da0028', 'image_icmp.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b0179217d5a96004d', '4028b88179210e1b0179217bb335004b', 'ICMP', 'ICMP Type입니다.', 'ICMP', 2, 20, '4028b88179210e1b0179215a98930029', 'image_icmp.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b0179217e7e7f004e', '4028b88179210e1b0179217bb335004b', 'SNMP', 'SNMP Type입니다.', 'SNMP', 2, 30, '4028b88179210e1b0179215b19bb002a', 'image_icmp.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b0179218131610050', '4028b88179210e1b0179217bb335004b', 'L2SWITCH', 'L2SWITCH Type입니다.', 'L2SWITCH', 2, 40, '4028b88179210e1b0179215c07b9002c', 'image_icmp.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b01792181cc9a0051', '4028b88179210e1b0179217bb335004b', 'L3SWITCH', 'L3SWITCH Type입니다.', 'L3SWITCH', 2, 50, '4028b88179210e1b0179215c8495002d', 'image_icmp.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b017921825cf70052', '4028b88179210e1b0179217bb335004b', 'L4SWITCH', 'L4SWITCH Type입니다.', 'L4SWITCH', 2, 60, '4028b88179210e1b0179215ce42d002e', 'image_icmp.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b0179217f2001004f', '4028b88179210e1b0179217bb335004b', 'IPCHECK', 'IPCHECK Type입니다.', 'IPCHECK', 2, 70, '4028b88179210e1b0179215b8bff002b', 'image_icmp.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b0179218e22100053', '4028b88179210e1b0179217bb335004b', 'APETC', 'APETC Type입니다.', 'APETC', 2, 80, '4028b88179210e1b0179215d4b68002f', 'image_icmp.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b0179218ef2cc0054', '4028b88179210e1b0179217bb335004b', 'IPS', 'IPS Type입니다.', 'IPS', 2, 90, '4028b88179210e1b0179215dbbf80030', 'image_icmp.png', '0509e09412534a6e98f04ca79abb6424', now());
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
	ci_desc character varying(512),
    interlink boolean DEFAULT 'false',
	instance_id character varying(128),
	create_user_key character varying(128),
	create_dt timestamp,
	update_user_key character varying(128),
	update_dt timestamp,
	mapping_id character varying(128),
	CONSTRAINT cmdb_ci_pk PRIMARY KEY (ci_id),
	CONSTRAINT cmdb_ci_uk UNIQUE (ci_id),
	CONSTRAINT cmdb_ci_fk1 FOREIGN KEY (type_id)
      REFERENCES cmdb_type (type_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT cmdb_ci_fk2 FOREIGN KEY (instance_id)
        REFERENCES wf_instance (instance_id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
);

COMMENT ON TABLE cmdb_ci IS 'CMDB CI 정보';
COMMENT ON COLUMN cmdb_ci.ci_id IS 'CI아이디';
COMMENT ON COLUMN cmdb_ci.ci_no IS '시퀀스';
COMMENT ON COLUMN cmdb_ci.ci_name IS 'CI이름';
COMMENT ON COLUMN cmdb_ci.ci_status IS 'CI상태';
COMMENT ON COLUMN cmdb_ci.type_id IS '타입아이디';
COMMENT ON COLUMN cmdb_ci.ci_desc IS 'CI설명';
COMMENT ON COLUMN cmdb_ci.interlink IS '연동 여부';
COMMENT ON COLUMN cmdb_ci.instance_id IS '인스턴스ID';
COMMENT ON COLUMN cmdb_ci.create_user_key IS '등록자';
COMMENT ON COLUMN cmdb_ci.create_dt IS '등록일시';
COMMENT ON COLUMN cmdb_ci.update_user_key IS '수정자';
COMMENT ON COLUMN cmdb_ci.update_dt IS '수정일시';
COMMENT ON COLUMN cmdb_ci.mapping_id IS '매핑아이디';

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
    interlink boolean DEFAULT 'false',
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
COMMENT ON COLUMN cmdb_ci_history.interlink IS '연동 여부';
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
    ci_id character varying(128) NOT NULL,
	target_ci_id character varying(128) NOT NULL,
	CONSTRAINT cmdb_ci_relation_pk PRIMARY KEY (relation_id),
	CONSTRAINT cmdb_ci_relation_uk UNIQUE (relation_id),
    CONSTRAINT cmdb_ci_relation_fk FOREIGN KEY (ci_id)
      REFERENCES cmdb_ci (ci_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

COMMENT ON TABLE cmdb_ci_relation IS 'CMDB CI 연관관계';
COMMENT ON COLUMN cmdb_ci_relation.relation_id IS '연관관계아이디';
COMMENT ON COLUMN cmdb_ci_relation.relation_type IS '연관관계타입';
COMMENT ON COLUMN cmdb_ci_relation.ci_id IS 'CI 아이디';
COMMENT ON COLUMN cmdb_ci_relation.target_ci_id IS 'CI아이디(Slave)';

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

insert into cmdb_class_attribute_map values ('4028b881792074460179210677bb0016', '27caaeba596663101d55a09ec873a375', 1);
insert into cmdb_class_attribute_map values ('4028b881792074460179210677bb0016', '4028b881792074460179209cef74000c', 2);
insert into cmdb_class_attribute_map values ('4028b881792074460179210677bb0016', '4028b25d791b75ac01791bb0f9140002', 3);
insert into cmdb_class_attribute_map values ('4028b881792074460179210677bb0016', '189319790e6349c7248b9f50456ed47b', 4);
insert into cmdb_class_attribute_map values ('4028b8817920744601792109b4800017', '4028b25d791b52a501791b71ff790003', 1);
insert into cmdb_class_attribute_map values ('4028b8817920744601792109b4800017', '4028b25d791b52a501791b61f7170001', 2);
insert into cmdb_class_attribute_map values ('4028b8817920744601792109b4800017', '4028b25d791b52a501791b72ccbc0004', 3);
insert into cmdb_class_attribute_map values ('4028b8817920744601792109b4800017', '4028b25d791b52a501791b73fff90005', 4);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179211490200003', '4028b25d7916ef5f017916f397230000', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179211490200003', '4028b25d7916ef5f017916f594a70001', 2);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179211490200003', '4028b88179207446017920d8df1c000d', 3);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179211490200003', '4028b25d7916ef5f017916f71b030002', 4);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179211490200003', '4028b25d7916ef5f017916f7bd590003', 5);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179211490200003', '4028b25d7916ef5f017916fc8f1d0004', 6);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179211490200003', '4028b25d7916ef5f017916fd4e5b0005', 7);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179211490200003', '4028b25d7916ef5f017916fff6c60006', 8);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179211490200003', '4028b25d7916ef5f01791707c3b70007', 9);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179211490200003', '4028b25d7916ef5f0179170937d70008', 10);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179211490200003', '4028b25d7916ef5f0179170df7c80009', 11);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179211490200003', '4028b25d7916ef5f0179170ee2c7000a', 12);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179211490200003', '4028b25d7916ef5f01791711dc08000b', 13);
insert into cmdb_class_attribute_map values ('4028b88179210e1b01792117aab10004', '072fcb3be4056095a9af82dc6505b1e8', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b01792117aab10004', 'b5f16c33ca0531087ed1b46805a9c682', 2);
insert into cmdb_class_attribute_map values ('4028b88179210e1b01792117aab10004', '77b6112b3013a6808aeb04f80dd75360', 3);
insert into cmdb_class_attribute_map values ('4028b88179210e1b01792117aab10004', '4028b25d791b75ac01791bb4b48c0004', 4);
insert into cmdb_class_attribute_map values ('4028b88179210e1b01792117aab10004', '4028b25d791b75ac01791bb574a70005', 5);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179211d13760005', '4028b25d7916ef5f017917129c66000c', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179211d13760005', '6ea67d6c6cb28def6b289affc6c95fd1', 2);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179211d13760005', 'adaeef4046bfcd78e345ad48cbbeefa5', 3);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179211d13760005', '4028b25d791b75ac01791b78b0550001', 4);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179211d13760005', '4028b25d791b75ac01791b777a240000', 5);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179211d13760005', 'd47973f063130acab00b2cf203a9788b', 6);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179211d13760005', '4028b25d7916ef5f01791713d145000d', 7);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179211d13760005', '4028b25d791b52a501791b5e35ff0000', 8);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179211d13760005', '4028b25d791c335201791c36103b0000', 9);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179211eb65d0006', '734ab921484883ad7760032a008baf21', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b017921201e8f0007', 'df0e88d216ace73e0164f3dbf7ade131', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b01792121eea80008', '4028b25d791c335201791c78e61e000e', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b01792123742e0009', '4028b25d791c335201791c7f51d8000f', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b017921242450000a', '4028b25d791c335201791c815b090010', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b01792125a23a000b', '4028b25d791c335201791c82c9ca0011', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b017921261f87000c', '4028b25d791c335201791c839afd0012', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b01792126a13a000d', '4028b25d791c335201791c84a9500013', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179212f17f90011', '4028b25d7916ef5f017917129c66000c', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179212f17f90011', '6ea67d6c6cb28def6b289affc6c95fd1', 2);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179212f17f90011', 'adaeef4046bfcd78e345ad48cbbeefa5', 3);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179212f17f90011', '4028b25d791b75ac01791b78b0550001', 4);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179212f17f90011', '4028b25d791b75ac01791b777a240000', 5);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179212f17f90011', '4028b25d791b75ac01791bbb1ed10006', 6);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179212f17f90011', '4028b25d791b75ac01791bbd4e5b0007', 7);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179212f17f90011', '4028b25d791b75ac01791bbe677f0008', 8);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179212f17f90011', '4028b25d791b75ac01791bc0759f0009', 9);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179212f17f90011', '4028b25d791b75ac01791bc0ffe3000a', 10);
insert into cmdb_class_attribute_map values ('4028b88179210e1b017921336fc60012', '4028b25d7916ef5f017917129c66000c', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b017921336fc60012', '6ea67d6c6cb28def6b289affc6c95fd1', 2);
insert into cmdb_class_attribute_map values ('4028b88179210e1b017921336fc60012', '4028b25d791b75ac01791b777a240000', 3);
insert into cmdb_class_attribute_map values ('4028b88179210e1b017921336fc60012', '489a14a0ebdca14b6eb42cf804330145', 4);
insert into cmdb_class_attribute_map values ('4028b88179210e1b017921336fc60012', '4028b25d7a8a5993017a8a5e5aac0000', 5);
insert into cmdb_class_attribute_map values ('4028b88179210e1b017921336fc60012', '4028b25d7a8a5993017a8a5efdb00001', 6);
insert into cmdb_class_attribute_map values ('4028b88179210e1b017921336fc60012', '4028b25d7a8a5993017a8a60910e0004', 7);
insert into cmdb_class_attribute_map values ('4028b88179210e1b017921336fc60012', '4028b25d7a8a5993017a8a61b37f0005', 8);
insert into cmdb_class_attribute_map values ('4028b88179210e1b017921336fc60012', '4028b25d7a8a5993017a8a5f95d70002', 9);
insert into cmdb_class_attribute_map values ('4028b88179210e1b017921336fc60012', '4028b25d7a8a5993017a8a6008e30003', 10);
insert into cmdb_class_attribute_map values ('4028b88179210e1b017921362c960013', '4028b25d791c335201791c5c52970002', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b01792136918f0014', '4028b25d791c335201791c5dd9240003', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b017921370e000015', '4028b25d791c335201791c5fa3850004', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b01792137989f0016', '4028b25d791c335201791c60844a0005', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179213972140017', '4028b25d791c335201791c617c790006', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179213a11f80018', '4028b25d791c335201791c62891d0007', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179213b50f50019', '4028b25d791c335201791c685fef0008', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179213baac3001a', '4028b25d791c335201791c6e00bc0009', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179213c2099001b', '4028b25d791c335201791c6f7892000a', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179213ca263001c', '4028b25d791c335201791c7057ef000b', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179213cfac9001d', '4028b25d791c335201791c711790000c', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179213d8436001e', '4028b25d791c335201791c71ff45000d', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179215a20da0028', '4028b88179210e1b017921555a73001f', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179215a98930029', '4028b88179210e1b01792155cae80020', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179215b19bb002a', '4028b88179210e1b017921563ea00021', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179215b8bff002b', '4028b88179210e1b01792156af0c0022', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179215c07b9002c', '4028b88179210e1b0179215719fb0023', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179215c8495002d', '4028b88179210e1b017921581a750024', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179215ce42d002e', '4028b88179210e1b0179215883dd0025', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179215d4b68002f', '4028b88179210e1b01792159017a0026', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179215dbbf80030', '4028b88179210e1b01792159687f0027', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b01792160f3010031', '4028b88179207446017920f72ed30015', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b01792160f3010031', '4028b25d7916ef5f017917129c66000c', 2);
insert into cmdb_class_attribute_map values ('4028b88179210e1b01792160f3010031', '6ea67d6c6cb28def6b289affc6c95fd1', 3);
insert into cmdb_class_attribute_map values ('4028b88179210e1b01792160f3010031', '4028b25d791b75ac01791b777a240000', 4);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179216226d30032', '4028b25d791c335201791c88e4b30014', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b01792162e60f0033', '4028b25d791c335201791c91baa00015', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b017921643b830034', '4028b25d791c335201791c98e06e0016', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b017921651e3d0035', '4028b8817920744601792079683b0000', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b01792165deef0036', '4028b881792074460179207b35a20001', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b01792166dba20037', '4028b881792074460179207d3f7c0002', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b01792167e5f30038', '4028b8817920744601792080a98f0003', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179216a760a0039', '4028b8817920744601792082b3f20004', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179216af1c9003a', '4028b88179207446017920843f560005', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179216b6d55003b', '4028b881792074460179208588280006', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179216bf1a6003c', '4028b88179207446017920873e820007', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179216cf921003d', '4028b8817920744601792089f9390008', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179216e6e35003e', '4028b881792074460179208efdca000a', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179216f07b2003f', '4028b8817920744601792090af45000b', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179216f773b0040', '4028b881792074460179208c29bf0009', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179217130030041', 'adaeef4046bfcd78e345ad48cbbeefa5', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179217130030041', '4028b25d791b75ac01791b78b0550001', 2);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179217130030041', '27caaeba596663101d55a09ec873a375', 3);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179217130030041', '4028b881792074460179209cef74000c', 4);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179217130030041', '4028b25d791b75ac01791bb0f9140002', 5);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179217130030041', '189319790e6349c7248b9f50456ed47b', 6);
insert into cmdb_class_attribute_map values ('4028b88179210e1b01792172d1b80042', '4028b88179207446017920f09cc90014', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b01792172d1b80042', '4028b88179207446017920eb4f63000f', 2);
insert into cmdb_class_attribute_map values ('4028b88179210e1b01792172d1b80042', '4028b88179207446017920ec770e0010', 3);
insert into cmdb_class_attribute_map values ('4028b88179210e1b01792172d1b80042', '4028b88179207446017920ed30de0011', 4);
insert into cmdb_class_attribute_map values ('4028b88179210e1b01792172d1b80042', '4028b88179207446017920ee4ac50012', 5);
insert into cmdb_class_attribute_map values ('4028b88179210e1b01792172d1b80042', '4028b88179207446017920ef02e10013', 6);
insert into cmdb_class_attribute_map values ('4028b88179210e1b017921740c250043', '4028b25d791b75ac01791b78b0550001', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b017921740c250043', '27caaeba596663101d55a09ec873a375', 2);
insert into cmdb_class_attribute_map values ('4028b88179210e1b017921740c250043', '4028b881792074460179209cef74000c', 3);
insert into cmdb_class_attribute_map values ('4028b88179210e1b017921740c250043', '4028b25d791b75ac01791bb0f9140002', 4);
insert into cmdb_class_attribute_map values ('4028b88179210e1b017921740c250043', '189319790e6349c7248b9f50456ed47b', 5);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179217798830044', '4028b25d7916ef5f017916f397230000', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179217798830044', '4028b25d7916ef5f017916f594a70001', 2);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179217798830044', '4028b88179207446017920d8df1c000d', 3);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179217798830044', '4028b25d7916ef5f017916f71b030002', 4);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179217798830044', '4028b25d7916ef5f017916f7bd590003', 5);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179217798830044', '4028b25d7916ef5f017916fc8f1d0004', 6);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179217798830044', '4028b25d7916ef5f0179170937d70008', 7);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179217798830044', '4028b25d7916ef5f0179170df7c80009', 8);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179217798830044', '4028b25d7916ef5f0179170ee2c7000a', 9);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179217798830044', '4028b25d7916ef5f01791711dc08000b', 10);
insert into cmdb_class_attribute_map values ('4028b8817a8eeaa3017a8f15873f0006', '4028b8817a8eeaa3017a8f0b263e0000', 1);
insert into cmdb_class_attribute_map values ('4028b8817a8eeaa3017a8f15873f0006', '4028b8817a8eeaa3017a8f0b97760001', 2);
insert into cmdb_class_attribute_map values ('4028b8817a8eeaa3017a8f15873f0006', '4028b8817a8eeaa3017a8f0c231a0002', 3);
insert into cmdb_class_attribute_map values ('4028b8817a8eeaa3017a8f15873f0006', '4028b8817a8eeaa3017a8f0c78660003', 4);
insert into cmdb_class_attribute_map values ('4028b8817a8eeaa3017a8f15873f0006', '4028b8817a8eeaa3017a8f0d0aec0004', 5);
insert into cmdb_class_attribute_map values ('4028b8817a8eeaa3017a8f15873f0006', '4028b8817a8eeaa3017a8f0d5e0d0005', 6);
insert into cmdb_class_attribute_map values ('4028b8817a8f3542017a8f38b60d0000', '4028b8817a8eeaa3017a8f282cc10007', 1);
insert into cmdb_class_attribute_map values ('4028b8817a8f3542017a8f38b60d0000', '4028b8817a8eeaa3017a8f288f020008', 2);
insert into cmdb_class_attribute_map values ('4028b8817a8f3542017a8f38b60d0000', '4028b8817a8eeaa3017a8f297d640009', 3);
insert into cmdb_class_attribute_map values ('4028b8817a8f3542017a8f38b60d0000', '4028b8817a8eeaa3017a8f29d2ac000a', 4);
insert into cmdb_class_attribute_map values ('4028b8817a8f3542017a8f38b60d0000', '4028b8817a8eeaa3017a8f2a6796000b', 5);
insert into cmdb_class_attribute_map values ('4028b8817a8f3542017a8f38b60d0000', '4028b8817a8eeaa3017a8f2ad6ea000c', 6);
insert into cmdb_class_attribute_map values ('4028b8817a8f3542017a8f38b60d0000', '4028b8817a8eeaa3017a8f2b611f000d', 7);
insert into cmdb_class_attribute_map values ('4028b8817a8f3542017a8f38b60d0000', '4028b8817a8eeaa3017a8f2cab18000e', 8);
insert into cmdb_class_attribute_map values ('4028b8817a8f3542017a8f38b60d0000', '4028b8817a8eeaa3017a8f2d01d6000f', 9);
insert into cmdb_class_attribute_map values ('4028b8817a8f3542017a8f38b60d0000', '4028b8817a8eeaa3017a8f2d60300010', 10);
insert into cmdb_class_attribute_map values ('4028b8817a8f3542017a8f38b60d0000', '4028b8817a8eeaa3017a8f2e573f0011', 11);
insert into cmdb_class_attribute_map values ('4028b8817a8f3542017a8f38b60d0000', '189319790e6349c7248b9f50456ed47b', 12);
insert into cmdb_class_attribute_map values ('4028b8817a8f3542017a8f9c365e001c' ,'4028b8817a8f3542017a8f5b51700001' ,1);
insert into cmdb_class_attribute_map values ('4028b8817a8f3542017a8f9c365e001c' ,'4028b8817a8f3542017a8f6123360002' ,2);
insert into cmdb_class_attribute_map values ('4028b8817a8f3542017a8f9c365e001c' ,'4028b8817a8f3542017a8f62f3d10003' ,3);
insert into cmdb_class_attribute_map values ('4028b8817a8f3542017a8f9c365e001c' ,'4028b8817a8f3542017a8f63cdf60004' ,4);
insert into cmdb_class_attribute_map values ('4028b8817a8f3542017a8f9c365e001c' ,'4028b8817a8f3542017a8f6451ec0005' ,5);
insert into cmdb_class_attribute_map values ('4028b8817a8f3542017a8f9c365e001c' ,'4028b8817a8f3542017a8f64b62c0006' ,6);
insert into cmdb_class_attribute_map values ('4028b8817a8f3542017a8f9c365e001c' ,'4028b8817a8f3542017a8f6587d60007' ,7);
insert into cmdb_class_attribute_map values ('4028b8817a8f3542017a8f9c365e001c' ,'4028b8817a8f3542017a8f6697260008' ,8);
insert into cmdb_class_attribute_map values ('4028b8817a8f3542017a8f9c365e001c' ,'4028b8817a8f3542017a8f6b9b180009' ,9);
insert into cmdb_class_attribute_map values ('4028b8817a8f3542017a8f9c365e001c' ,'4028b8817a8f3542017a8f6c041c000a' ,10);
insert into cmdb_class_attribute_map values ('4028b8817a8f3542017a8f9c365e001c' ,'4028b8817a8f3542017a8f6c8877000b' ,11);
insert into cmdb_class_attribute_map values ('4028b8817a8f3542017a8f9c365e001c' ,'4028b8817a8f3542017a8f6d490a000c' ,12);
insert into cmdb_class_attribute_map values ('4028b8817a8f3542017a8f9c365e001c' ,'4028b8817a8f3542017a8f6db3b8000d' ,13);
insert into cmdb_class_attribute_map values ('4028b8817a8f3542017a8f9c365e001c' ,'4028b8817a8f3542017a8f7524b40010' ,14);
insert into cmdb_class_attribute_map values ('4028b8817a8f3542017a8f9c365e001c' ,'4028b8817a8f3542017a8f6e6999000e' ,15);
insert into cmdb_class_attribute_map values ('4028b8817a8f3542017a8f9c365e001c' ,'4028b8817a8f3542017a8f6f4ff6000f' ,16);
insert into cmdb_class_attribute_map values ('4028b8817a8f3542017a8f9c365e001c' ,'4028b8817a8f3542017a8f7610e60011' ,17);
insert into cmdb_class_attribute_map values ('4028b8817a8f3542017a8f9c365e001c' ,'4028b8817a8f3542017a8f812a9c0012' ,18);
insert into cmdb_class_attribute_map values ('4028b8817a8f3542017a8f9c365e001c' ,'4028b8817a8f3542017a8f81bf150013' ,19);
insert into cmdb_class_attribute_map values ('4028b8817a8f3542017a8f9c365e001c' ,'4028b8817a8f3542017a8f821bfc0014' ,20);
insert into cmdb_class_attribute_map values ('4028b8817a8f3542017a8f9c365e001c' ,'4028b8817a8f3542017a8f827a9a0015' ,21);
insert into cmdb_class_attribute_map values ('4028b8817a8f3542017a8f9c365e001c' ,'4028b8817a8f3542017a8f82e2990016' ,22);
insert into cmdb_class_attribute_map values ('4028b8817a8f3542017a8f9c365e001c' ,'4028b8817a8f3542017a8f83df0c0017' ,23);
insert into cmdb_class_attribute_map values ('4028b8817a8f3542017a8f9c365e001c' ,'4028b8817a8f3542017a8f84329c0018' ,24);
insert into cmdb_class_attribute_map values ('4028b8817a8f3542017a8f9c365e001c' ,'4028b8817a8f3542017a8f8551c80019' ,25);
insert into cmdb_class_attribute_map values ('4028b8817a8f3542017a8f9c365e001c' ,'4028b8817a8f3542017a8f860fb7001a' ,26);
insert into cmdb_class_attribute_map values ('4028b8817a8f3542017a8f9c365e001c' ,'4028b8817a8f3542017a8f8669aa001b' ,27);


/**
 * CMDB CI 그룹 리스트 데이터
 */
DROP TABLE IF EXISTS cmdb_ci_group_list_data cascade;

CREATE TABLE cmdb_ci_group_list_data
(
    ci_id character varying(128) NOT NULL,
    attribute_id character varying(128) NOT NULL,
    c_attribute_id character varying(128) NOT NULL,
    c_attribute_seq int NOT NULL,
    c_value text,
    CONSTRAINT cmdb_ci_group_list_data_pk PRIMARY KEY (ci_id, attribute_id, c_attribute_id, c_attribute_seq),
    CONSTRAINT cmdb_ci_group_list_data_fk1 FOREIGN KEY (ci_id)
        REFERENCES cmdb_ci (ci_id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT cmdb_ci_group_list_data_fk2 FOREIGN KEY (attribute_id)
        REFERENCES cmdb_attribute (attribute_id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
);

COMMENT ON TABLE cmdb_ci_group_list_data IS 'CMDB CI 그룹 리스트 데이터';
COMMENT ON COLUMN cmdb_ci_group_list_data.ci_id IS 'CI아이디';
COMMENT ON COLUMN cmdb_ci_group_list_data.attribute_id IS '속성아이디';
COMMENT ON COLUMN cmdb_ci_group_list_data.c_attribute_id IS '자식속성아이디';
COMMENT ON COLUMN cmdb_ci_group_list_data.c_attribute_seq IS '자식속성순서';
COMMENT ON COLUMN cmdb_ci_group_list_data.c_value IS '자식속성값';

/**
 * CI 컴포넌트 - CI 세부 속성 임시 테이블
 */
DROP TABLE IF EXISTS wf_component_ci_data cascade;

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
    request_user_key varchar(128) NOT NULL,
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
COMMENT ON COLUMN awf_api_token.request_user_key IS '요청 사용자 키';

/**
 * API Token 정보
 */
DROP TABLE IF EXISTS cmdb_ci_instance_relation cascade;

CREATE TABLE cmdb_ci_instance_relation
(
    ci_id character varying(128) NOT NULL,
    instance_id character varying(128) NOT NULL,
    CONSTRAINT cmdb_ci_instance_relation_pk PRIMARY KEY (ci_id, instance_id),
    CONSTRAINT cmdb_ci_instance_relation_fk1 FOREIGN KEY (ci_id)
      REFERENCES cmdb_ci (ci_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT cmdb_ci_instance_relation_fk2 FOREIGN KEY (instance_id)
      REFERENCES wf_instance (instance_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

COMMENT ON TABLE cmdb_ci_instance_relation IS 'CMDB CI 관련문서 정보';
COMMENT ON COLUMN cmdb_ci_instance_relation.ci_id IS 'CI아이디';
COMMENT ON COLUMN cmdb_ci_instance_relation.instance_id IS '인스턴스ID';

/**
 * 코드 다국어 정보
 */
DROP TABLE IF EXISTS awf_code_lang cascade;

CREATE TABLE awf_code_lang
(
    code varchar(100) NOT NULL,
    code_name varchar(128),
    lang varchar(100) NOT NULL,
    CONSTRAINT awf_code_lang_pk PRIMARY KEY (code, lang),
    CONSTRAINT awf_code_lang_fk FOREIGN KEY (code) REFERENCES awf_code (code)
);

COMMENT ON TABLE awf_code_lang IS '다국어 코드 정보';
COMMENT ON COLUMN awf_code_lang.code IS '코드';
COMMENT ON COLUMN awf_code_lang.code_name IS '코드 이름';
COMMENT ON COLUMN awf_code_lang.lang IS '언어';

insert into awf_code_lang values ('document.status.temporary', 'temporary', 'en');
insert into awf_code_lang values ('document.status.destroy', 'destroy', 'en');
insert into awf_code_lang values ('document.status.use', 'use', 'en');
insert into awf_code_lang values ('document.displayType.editable', 'Editable', 'en');
insert into awf_code_lang values ('document.displayType.readonly', 'Readonly', 'en');
insert into awf_code_lang values ('document.displayType.hidden', 'Hidden', 'en');
insert into awf_code_lang values ('document.group.incident', 'Disability Inquiry', 'en');
insert into awf_code_lang values ('document.group.inquiry', 'Simple Inquiry', 'en');
insert into awf_code_lang values ('document.group.request', 'Service Request', 'en');
insert into awf_code_lang values ('form.template', 'Template', 'en');
/* 단순문의 */
insert into awf_code_lang values ('form.template.serviceDesk.inquiry', 'ServiceDesk - Simple Inquiry', 'en');
/* 단순문의 - 서비스 항목*/
insert into awf_code_lang values ('form.template.serviceDesk.inquiry.category', 'Service Category', 'en');
insert into awf_code_lang values ('form.template.serviceDesk.inquiry.category.none', '- Choose Category -', 'en');
insert into awf_code_lang values ('form.template.serviceDesk.inquiry.category.server', 'Server', 'en');
insert into awf_code_lang values ('form.template.serviceDesk.inquiry.category.network', 'Network', 'en');
insert into awf_code_lang values ('form.template.serviceDesk.inquiry.category.security', 'Security', 'en');
insert into awf_code_lang values ('form.template.serviceDesk.inquiry.category.dbms', 'Database', 'en');
insert into awf_code_lang values ('form.template.serviceDesk.inquiry.category.contract', 'Contract', 'en');
insert into awf_code_lang values ('form.template.serviceDesk.inquiry.category.etc', 'ETC', 'en');
/* 장애신고 */
insert into awf_code_lang values ('form.template.serviceDesk.incident', 'ServiceDesk - Incident', 'en');
/* 장애신고 - 장애 유형 */
insert into awf_code_lang values ('form.template.serviceDesk.incident.category', 'Incident Category', 'en');
insert into awf_code_lang values ('form.template.serviceDesk.incident.category.none', '- Choose Category -', 'en');
insert into awf_code_lang values ('form.template.serviceDesk.incident.category.system', 'System Error', 'en');
insert into awf_code_lang values ('form.template.serviceDesk.incident.category.infrastructure', 'Infrastructure error', 'en');
insert into awf_code_lang values ('form.template.serviceDesk.incident.category.technology', 'Technology error', 'en');
insert into awf_code_lang values ('form.template.serviceDesk.incident.category.operation', 'Operation error', 'en');
insert into awf_code_lang values ('form.template.serviceDesk.incident.category.human', 'Human error', 'en');
/* 서비스요청 */
insert into awf_code_lang values ('form.template.serviceDesk.request', 'ServiceDesk - Service Request', 'en');
/* 서비스요청 - 요청 구분 */
insert into awf_code_lang values ('form.template.serviceDesk.request.category', 'Service Request Category', 'en');
insert into awf_code_lang values ('form.template.serviceDesk.request.category.service', 'Service Request', 'en');
insert into awf_code_lang values ('form.template.serviceDesk.request.category.security', 'Security Request', 'en');
insert into awf_code_lang values ('form.template.serviceDesk.request.category.development', 'Development Request', 'en');
insert into awf_code_lang values ('form.template.serviceDesk.request.category.material', 'Material Request', 'en');
insert into awf_code_lang values ('form.template.serviceDesk.request.category.work', 'Work Request', 'en');
insert into awf_code_lang values ('form.template.serviceDesk.request.category.etc', 'ETC', 'en');
/* 차트 */
insert into awf_code_lang values ('chart.operation.percent', 'percent', 'en');
insert into awf_code_lang values ('chart.operation.count', 'count', 'en');
insert into awf_code_lang values ('chart.unit.year', 'year', 'en');
insert into awf_code_lang values ('chart.unit.month', 'month', 'en');
insert into awf_code_lang values ('chart.unit.hour', 'hour', 'en');
insert into awf_code_lang values ('chart.unit.day', 'day', 'en');
insert into awf_code_lang values ('chart.type.basicLine', 'Basic Line Chart', 'en');
insert into awf_code_lang values ('chart.type.pie', 'Pie Chart', 'en');
insert into awf_code_lang values ('chart.type.stackedColumn', 'Stacked Column Chart', 'en');
insert into awf_code_lang values ('chart.type.stackedBar', 'Stacked Bar Chart', 'en');
insert into awf_code_lang values ('chart.type.lineAndColumn', 'Line and Column Chart', 'en');
insert into awf_code_lang values ('chart.type.activityGauge', 'Activity Gauge Chart', 'en');
/* 자료실 */
insert into awf_code_lang values ('download.category.companyPolicy', 'Company Policy', 'en');
insert into awf_code_lang values ('download.category.etc', 'Etc', 'en');
/* FAQ */
insert into awf_code_lang values ('faq.category.etc', 'Etc', 'en');
insert into awf_code_lang values ('faq.category.setting', 'Setting', 'en');
insert into awf_code_lang values ('faq.category.techSupport', 'Tech support', 'en');
/* 커스텀 코드 */
insert into awf_code_lang values ('customCode.sessionKey.userKey', 'User Key', 'en');
insert into awf_code_lang values ('customCode.sessionKey.userId', 'User ID', 'en');
insert into awf_code_lang values ('customCode.sessionKey.userName', 'Username', 'en');
insert into awf_code_lang values ('customCode.sessionKey.email', 'Email', 'en');
insert into awf_code_lang values ('customCode.sessionKey.position', 'Position', 'en');
insert into awf_code_lang values ('customCode.sessionKey.departmentName', 'Department', 'en');
insert into awf_code_lang values ('customCode.sessionKey.officeNumber', 'Office Number', 'en');
insert into awf_code_lang values ('customCode.sessionKey.mobileNumber', 'Mobile', 'en');

/**
 * 사용자 지정 테이블
 */
DROP TABLE IF EXISTS awf_user_custom cascade;

CREATE TABLE awf_user_custom
(
    user_key varchar(128) NOT NULL,
    custom_type varchar(128) NOT NULL,
    custom_value varchar(512),
    CONSTRAINT awf_user_custom_pk PRIMARY KEY (user_key, custom_type),
    CONSTRAINT awf_user_custom_fk FOREIGN KEY (user_key) REFERENCES awf_user (user_key)
);

COMMENT ON TABLE awf_user_custom IS '사용자 지정';
COMMENT ON COLUMN awf_user_custom.user_key IS '사용자키';
COMMENT ON COLUMN awf_user_custom.custom_type IS '타입';
COMMENT ON COLUMN awf_user_custom.custom_value IS '값';
/**
 * 보고서 설정 테이블 (템플릿)
 */
DROP TABLE IF EXISTS awf_report_template cascade;

create table awf_report_template (
    template_id varchar(128) NOT NULL,
    template_name varchar(128) NOT NULL,
    template_desc varchar(512),
    report_name varchar(128),
    automatic boolean DEFAULT false,
    create_user_key varchar(128),
    create_dt timestamp,
    update_user_key varchar(128),
    update_dt timestamp,
    CONSTRAINT awf_report_template_pk PRIMARY KEY (template_id)
);

COMMENT ON TABLE awf_report_template IS '보고서 템플릿 정보';
COMMENT ON COLUMN awf_report_template.template_id IS '템플릿아이디';
COMMENT ON COLUMN awf_report_template.template_name IS '템플릿명';
COMMENT ON COLUMN awf_report_template.template_desc IS '템플릿설명';
COMMENT ON COLUMN awf_report_template.report_name IS '보고서명';
COMMENT ON COLUMN awf_report_template.automatic IS '자동생성여부';
COMMENT ON COLUMN awf_report_template.create_user_key IS '생성자';
COMMENT ON COLUMN awf_report_template.create_dt IS '생성일시';
COMMENT ON COLUMN awf_report_template.update_user_key IS '수정자';
COMMENT ON COLUMN awf_report_template.update_dt IS '수정일시';

/**
 * 보고서 설정 차트 매핑 테이블
 */
DROP TABLE IF EXISTS awf_report_template_map cascade;

create table awf_report_template_map (
    template_id varchar(128) NOT NULL,
    chart_id varchar(128) NOT NULL,
    display_order int,
    CONSTRAINT awf_report_template_map_pk PRIMARY KEY (template_id, chart_id)
);

COMMENT ON TABLE awf_report_template_map IS '보고서 템플릿 차트 정보';
COMMENT ON COLUMN awf_report_template_map.template_id IS '템플릿아이디';
COMMENT ON COLUMN awf_report_template_map.chart_id IS '차트아이디';

/**
 * 보고서 조회 테이블
 */
DROP TABLE IF EXISTS awf_report cascade;

create table awf_report (
    report_id varchar(128) NOT NULL,
    template_id varchar(128) NOT NULL,
    report_name varchar(128),
    report_desc varchar(512),
    publish_dt timestamp,
    CONSTRAINT awf_report_pk PRIMARY KEY (report_id)
);

COMMENT ON TABLE awf_report IS '보고서 정보';
COMMENT ON COLUMN awf_report.report_id IS '보고서아이디';
COMMENT ON COLUMN awf_report.template_id IS '템플릿아이디';
COMMENT ON COLUMN awf_report.report_name IS '보고서명';
COMMENT ON COLUMN awf_report.report_desc IS '보고서설명';
COMMENT ON COLUMN awf_report.publish_dt IS '발행일시';

/**
 * 보고서 조회 데이터 테이블
 */
DROP TABLE IF EXISTS awf_report_data cascade;

create table awf_report_data (
    data_id varchar(128) NOT NULL,
    report_id varchar(128) NOT NULL,
    chart_id varchar(128) NOT NULL,
    display_order int,
    values text,
    CONSTRAINT awf_report_data_pk PRIMARY KEY (data_id)
);

COMMENT ON TABLE awf_report_data IS '보고서 데이터 정보';
COMMENT ON COLUMN awf_report_data.data_id IS '데이터아이디';
COMMENT ON COLUMN awf_report_data.report_id IS '보고서아이디';
COMMENT ON COLUMN awf_report_data.chart_id IS '차트아이디';
COMMENT ON COLUMN awf_report_data.display_order IS '차트순서';
COMMENT ON COLUMN awf_report_data.values IS '데이터';

