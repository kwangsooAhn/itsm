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
insert into awf_code values ('form.template.serviceDesk.inquiry.category.etc', 'form.template.serviceDesk.inquiry.category', 'none', '기타', '', true, true, 5, 7, '0509e09412534a6e98f04ca79abb6424', now(), null, null);

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
insert into awf_code values ('customCode.operator', 'customCode', null, '연산자', null, true, true, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('customCode.operator.equal', 'customCode.operator', 'equal', '=', null, true, true, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('customCode.operator.notEqual', 'customCode.operator', 'notEqual', '!=', null, true, true, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
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

insert into awf_custom_code values ('40288a19736b46fb01736b89e46c0008', '사용자 이름 검색', 'table', 'awf_user', 'user_name', 'user_key', null, '[]', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_custom_code values ('40288a19736b46fb01736b89e46c0009', '사용자 부서 검색', 'code', null, null, null, 'department.group', '[]', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_custom_code values ('40288ab777dd21b50177dd52781e0000', '데이터베이스', 'code', null, null, null, 'cmdb.db.kind', null, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_custom_code values ('40288a19736b46fb01736b89e46c0010', '서비스데스크 - 단순문의 : 서비스 항목', 'code', null, null, null, 'form.template.serviceDesk.inquiry.category', '[]', '0509e09412534a6e98f04ca79abb6424', now(), null, null);

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
/* 단순 문의 */
insert into awf_role values ('serviceDesk.assignee', '서비스데스크 담당자', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_role values ('serviceDesk.manager', '서비스데스크 관리자', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);

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
/*단순문의 - 서비스데스크 담당자, 서비스데스크 관리자*/
insert into awf_role_auth_map values ('serviceDesk.assignee', 'board.read');
insert into awf_role_auth_map values ('serviceDesk.assignee', 'code.read');
insert into awf_role_auth_map values ('serviceDesk.assignee', 'notice.read');
insert into awf_role_auth_map values ('serviceDesk.assignee', 'faq.read');
insert into awf_role_auth_map values ('serviceDesk.assignee', 'token.read');
insert into awf_role_auth_map values ('serviceDesk.assignee', 'token.create');
insert into awf_role_auth_map values ('serviceDesk.assignee', 'document.read');
insert into awf_role_auth_map values ('serviceDesk.assignee', 'document.create');
insert into awf_role_auth_map values ('serviceDesk.assignee', 'download.read');
insert into awf_role_auth_map values ('serviceDesk.manager', 'notice.read');
insert into awf_role_auth_map values ('serviceDesk.manager', 'faq.create');
insert into awf_role_auth_map values ('serviceDesk.manager', 'faq.update');
insert into awf_role_auth_map values ('serviceDesk.manager', 'faq.delete');
insert into awf_role_auth_map values ('serviceDesk.manager', 'form.read');
insert into awf_role_auth_map values ('serviceDesk.manager', 'user.read');
insert into awf_role_auth_map values ('serviceDesk.manager', 'user.create');
insert into awf_role_auth_map values ('serviceDesk.manager', 'role.create');
insert into awf_role_auth_map values ('serviceDesk.manager', 'role.update');
insert into awf_role_auth_map values ('serviceDesk.manager', 'process.delete');
insert into awf_role_auth_map values ('serviceDesk.manager', 'process.update');
insert into awf_role_auth_map values ('serviceDesk.manager', 'process.create');
insert into awf_role_auth_map values ('serviceDesk.manager', 'process.read');
insert into awf_role_auth_map values ('serviceDesk.manager', 'form.delete');
insert into awf_role_auth_map values ('serviceDesk.manager', 'form.update');
insert into awf_role_auth_map values ('serviceDesk.manager', 'form.create');
insert into awf_role_auth_map values ('serviceDesk.manager', 'user.update');
insert into awf_role_auth_map values ('serviceDesk.manager', 'user.delete');
insert into awf_role_auth_map values ('serviceDesk.manager', 'faq.read');
insert into awf_role_auth_map values ('serviceDesk.manager', 'notice.update');
insert into awf_role_auth_map values ('serviceDesk.manager', 'notice.delete');
insert into awf_role_auth_map values ('serviceDesk.manager', 'notice.create');
insert into awf_role_auth_map values ('serviceDesk.manager', 'role.read');
insert into awf_role_auth_map values ('serviceDesk.manager', 'role.delete');

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
insert into awf_url values ('/rest/folders/{id}', 'delete', '폴더 삭제', 'FALSE');
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
insert into awf_url values ('/tokens/{id}/edit-tab', 'get', '문서 오른쪽 탭 정보', 'TRUE');
insert into awf_url values ('/tokens/{id}/view', 'get', '', 'TRUE');
insert into awf_url values ('/tokens/{id}/view-tab', 'get', '문서 오른쪽 탭 정보', 'TRUE');
insert into awf_url values ('/tokens/{id}/print', 'get', '처리할 문서 프린트 화면', 'TRUE');
insert into awf_url values ('/tokens/{id}/view-pop', 'get', '관련문서 팝업 화면', 'TRUE');
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
COMMENT ON COLUMN wf_form.create_user_key IS '생성자';
COMMENT ON COLUMN wf_form.create_dt IS '생성일시';
COMMENT ON COLUMN wf_form.update_user_key IS '수정자';
COMMENT ON COLUMN wf_form.update_dt IS '수정일시';
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
    code_value varchar(256),
    lang varchar(100) NOT NULL,
    CONSTRAINT awf_code_lang_pk PRIMARY KEY (code, lang),
    CONSTRAINT awf_code_lang_fk FOREIGN KEY (code) REFERENCES awf_code (code)
);

COMMENT ON TABLE awf_code_lang IS '다국어 코드 정보';
COMMENT ON COLUMN awf_code_lang.code IS '코드';
COMMENT ON COLUMN awf_code_lang.code_value IS '코드 값';
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

