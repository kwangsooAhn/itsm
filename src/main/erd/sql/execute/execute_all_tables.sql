/* Drop Sequences */

DROP SEQUENCE IF EXISTS awf_download_seq cascade;
DROP SEQUENCE IF EXISTS awf_file_loc_seq cascade;
DROP SEQUENCE IF EXISTS hibernate_sequence cascade;
DROP SEQUENCE IF EXISTS portal_board_seq cascade;


/* Create Sequences */
CREATE SEQUENCE awf_download_seq INCREMENT 1 MINVALUE 1 START 1;
CREATE SEQUENCE awf_file_loc_seq INCREMENT 1 MINVALUE 1 START 1;
CREATE SEQUENCE hibernate_sequence INCREMENT 1 MINVALUE 1 START 1;
CREATE SEQUENCE portal_board_seq INCREMENT 1 MINVALUE 1 START 1;
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
insert into awf_auth values ('code.create', '코드 등록', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('code.delete', '코드 삭제', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('code.read', '코드 조회', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('code.update', '코드 수정', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('custom.code.create', '사용자 정의 코드 등록', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('custom.code.delete', '사용자 정의 코드 삭제', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('custom.code.read', '사용자 정의 코드 조회', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('custom.code.update', '사용자 정의 코드 수정', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
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
insert into awf_auth values ('process.create', '프로세스 등록', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('process.delete', '프로세스 삭제', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('process.read', '프로세스 조회', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('process.update', '프로세스 변경', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('role.create', '역할 등록', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('role.delete', '역할 삭제', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('role.read', '역할 조회', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_auth values ('role.update', '역할 변경', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
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

CREATE TABLE awf_avatar
(
    avatar_id varchar(128) NOT NULL,
    avatar_type varchar(100),
    avatar_value varchar(512),
    uploaded boolean DEFAULT 'false' NOT NULL,
    uploaded_location varchar(512),
    create_user_key varchar(128),
    create_dt timestamp,
    update_user_key varchar(128),
    update_dt timestamp,
    CONSTRAINT awf_avatar_pk PRIMARY KEY (avatar_id)
);

COMMENT ON TABLE awf_avatar IS '아바타';
COMMENT ON COLUMN awf_avatar.avatar_id IS '아바타 아이디';
COMMENT ON COLUMN awf_avatar.avatar_type IS '아바타 종류';
COMMENT ON COLUMN awf_avatar.avatar_value IS '아바타 value';
COMMENT ON COLUMN awf_avatar.uploaded IS '업로드 여부';
COMMENT ON COLUMN awf_avatar.uploaded_location IS '업로드 경로';
COMMENT ON COLUMN awf_avatar.create_user_key IS '등록자';
COMMENT ON COLUMN awf_avatar.create_dt IS '등록일';
COMMENT ON COLUMN awf_avatar.update_user_key IS '수정자';
COMMENT ON COLUMN awf_avatar.update_dt IS '수정일';

insert into awf_avatar values('0509e09412534a6e98f04ca79abb6424', 'FILE', 'default_profile.jpg', false, '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);

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
COMMENT ON COLUMN awf_code.create_user_key IS '등록자';
COMMENT ON COLUMN awf_code.create_dt IS '등록일';
COMMENT ON COLUMN awf_code.update_user_key IS '수정자';
COMMENT ON COLUMN awf_code.update_dt IS '수정일';

insert into awf_code values ('12', 'user.time', 'hh:mm a', null, null, false, 4, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('24', 'user.time', 'HH:mm', null, null, false, 4, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('assignee', 'root', null, '담당자', null, false, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('assignee.type', 'assignee', null, '담당자 타입', null, false, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('assignee.type.assignee', 'assignee.type', null, '지정 담당자', null, false, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('assignee.type.candidate.groups', 'assignee.type', null, '담당자 후보그룹', null, false, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('assignee.type.candidate.users', 'assignee.type', null, '담당자 후보목록', null, false, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('department', 'user', null, '부서 관리', null, false, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('department.group', 'department', null, '부서 명', null, false, 4, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('department.group.design', 'department.group', 'DESIGN', 'DESIGN', null, false, 5, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('department.group.itsm', 'department.group', 'ITSM', 'ITSM', null, false, 5, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('department.group.tc', 'department.group', 'TC', 'TC', null, false, 5, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('document', 'root', null, '신청서', null, false, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('document.group', 'document', null, '신청서 목록', null, true, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('document.status', 'document', null, '신청서 상태', null, false, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('document.status.destroy', 'document.status', '폐기', '폐기', null, false, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('document.status.temporary', 'document.status', '임시', '임시', null, false, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('document.status.use', 'document.status', '사용', '사용', null, false, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('download', 'root', null, '자료실', null, true, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('download.category', 'download', null, '자료실 카테고리', null, true, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('download.category.companyPolicy', 'download.category', null, '회사규정', null, true, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('download.category.etc', 'download.category', null, '기타', null, true, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('email', 'reception_type', '이메일', '이메일', null, true, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('faq', 'root', null, 'FAQ', null, false, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('faq.category', 'faq', null, 'FAQ 카테고리', null, false, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('faq.category.etc', 'faq.category', null, '기타', null, false, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('faq.category.setting', 'faq.category', null, '설정', null, false, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('faq.category.techSupport', 'faq.category', null, '기술지원', null, false, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('form', 'root', null, '문서양식', null, false, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('form.lang', 'form', null, '문서양식 언어', null, false, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('form.lang.ko', 'form.lang', null, '한국어', null, false, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('numbering', 'root', null, '문서번호 규칙 패턴', null, false, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('numbering.pattern', 'numbering', null, '문서규칙 패턴', null, false, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('numbering.pattern.format', 'numbering.pattern', null, '문서규칙 포맷', null, false, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('numbering.pattern.format.yyyyMMdd', 'numbering.pattern.format', 'yyyyMMdd', '날짜형패턴', null, false, 4, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('post', 'reception_type', '우편', '우편', null, true, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('reception_type', 'root', '접수유형', null, null, true, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('servicedesk.incident', 'document.group', '장애문의', '장애문의', null, true, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('servicedesk.inquiry', 'document.group', '단순문의', '단순문의', null, true, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('servicedesk.request', 'document.group', '서비스요청', '서비스요청', null, true, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('telephone', 'reception_type', '전화', '전화', null, true, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('token', 'root', null, '토큰 관련 코드', null, false, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('token.status', 'token', null, '토큰 상태 코드', null, false, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('token.status.cancel', 'token.status', null, '취소', null, false, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('token.status.finish', 'token.status', null, '처리 완료', null, false, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('token.status.reject', 'token.status', null, '반려', null, false, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('token.status.running', 'token.status', null, '진행 중', null, false, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('token.status.terminate', 'token.status', null, '종료', null, false, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('token.status.waiting', 'token.status', null, '대기 중', null, false, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('token.status.withdraw', 'token.status', null, '회수', null, false, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('top', 'root', null, null, null, false, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user', 'top', null, '사용자', null, false, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.date', 'user', null, '사용자 날짜 포맷', null, false, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.date.ddmmyyyy', 'user.date', 'dd-MM-yyyy', null, null, false, 4, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.date.mmddyyyy', 'user.date', 'MM-dd-yyyy', null, null, false, 4, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.date.yyyyddmm', 'user.date', 'yyyy-dd-MM', null, null, false, 4, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.date.yyyymmdd', 'user.date', 'yyyy-MM-dd', null, null, false, 4, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.default', 'user', null, '기본 값', null, false, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.default.role', 'user.default', null, '기본 역할', null, false, 4, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.default.role.users.general', 'user.default.role', 'users.general', '역할 - 사용자 일반', null, false, 5, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.department', 'user', null, '부서', null, false, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.extensionNumber', 'user', null, '내선번호', null, false, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.id', 'user', null, '아이디', null, false, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.lang', 'user', null, '언어', null, false, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.lang.en', 'user.lang', 'en', '영어', null, false, 4, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.lang.ko', 'user.lang', 'ko', '한국어', null, false, 4, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.name', 'user', null, '이름', null, false, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.platform', 'user', null, '플랫폼', null, false, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.platform.alice', 'user.platform', null, 'Alice', null, false, 4, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.platform.google', 'user.platform', null, 'Google', null, false, 4, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.platform.kakao', 'user.platform', null, 'Kakao', null, false, 4, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.position', 'user', null, '직책', null, false, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.search', 'user', null, '검색 목록', null, false, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.search.department', 'user.search', null, '부서', null, false, 4, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.search.extensionNumber', 'user.search', null, '내선번호', null, false, 4, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.search.id', 'user.search', null, '아이디', null, false, 4, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.search.mobileNumber', 'user.search', null, '핸드폰 번호', null, false, 4, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.search.name', 'user.search', null, '이름', null, false, 4, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.search.officeNumber', 'user.search', null, '사무실 번호', null, false, 4, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.search.position', 'user.search', null, '직책', null, false, 4, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.status', 'user', null, '계정 상태', null, false, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.status.certified', 'user.status', null, '인증 완료', null, false, 4, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.status.signup', 'user.status', null, '가입', null, false, 4, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.theme', 'user', null, '테마', null, false, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.theme.dark', 'user.theme', 'dark', '어두운 테마', null, false, 4, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.theme.default', 'user.theme', 'default', '기본 테마', null, false, 4, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.time', 'user', null, '사용자 시간 포맷', null, false, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('version', 'root', null, null, null, false, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('version.workflow', 'version', '20200515', null, null, false, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('script', 'root', null, null, null, false, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('script.type', 'script', 'Script Type', null, null, false, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('script.type.document.attachFile', 'script.type', '[문서편집] 첨부파일', null, null, false, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('root', null, null, 'ROOT', null, false, 0, '0509e09412534a6e98f04ca79abb6424', now(), null, null);

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
insert into awf_menu values ('board', 'menu', '/boards/search', 7,TRUE);
insert into awf_menu values ('config', 'menu', '', 8,TRUE);
insert into awf_menu values ('config.user', 'config', '/users/search', 1,TRUE);
insert into awf_menu values ('config.auth', 'config', '/auths/edit', 2,TRUE);
insert into awf_menu values ('config.role', 'config', '/roles/edit', 3,TRUE);
insert into awf_menu values ('config.boardAdmin', 'config', '/board-admin/search', 4,TRUE);
insert into awf_menu values ('config.code', 'config', '/codes/edit', 5,TRUE);
insert into awf_menu values ('workflow', 'menu', '', 9,TRUE);
insert into awf_menu values ('workflow.process', 'workflow', '/processes-admin/search', 1,TRUE);
insert into awf_menu values ('workflow.form', 'workflow', '/forms-admin/search', 2,TRUE);
insert into awf_menu values ('workflow.workflowAdmin', 'workflow', '/documents-admin/search', 3,TRUE);
insert into awf_menu values ('workflow.customCode', 'workflow', '/custom-codes/search', 4,TRUE);
insert into awf_menu values ('workflow.image', 'workflow', '/images', 5,TRUE);


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
insert into awf_menu_auth_map values ('workflow.process', 'process.delete');
insert into awf_menu_auth_map values ('workflow.process', 'process.create');
insert into awf_menu_auth_map values ('workflow.process', 'process.update');
insert into awf_menu_auth_map values ('workflow.process', 'process.read');

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

insert into awf_numbering_rule values ('40125c91714df6c325714e053c890125', '문서 번호', '신청서 작성시 발생한 문서번호');

/**
 * 넘버링패턴정보
 */
DROP TABLE IF EXISTS awf_numbering_pattern cascade;

CREATE TABLE awf_numbering_pattern
(
	pattern_id varchar(128) NOT NULL,
	numbering_id varchar(128) NOT NULL,
	pattern_name varchar(255) NOT NULL,
	pattern_type varchar(100) NOT NULL,
	pattern_value text,
	pattern_order int NOT NULL,
	CONSTRAINT awf_numbering_pattern_pk PRIMARY KEY (pattern_id),
	CONSTRAINT awf_numbering_pattern_fk FOREIGN KEY (numbering_id) REFERENCES awf_numbering_rule (numbering_id)
);

COMMENT ON TABLE awf_numbering_pattern IS '넘버링패턴정보';
COMMENT ON COLUMN awf_numbering_pattern.pattern_id IS '패턴아이디';
COMMENT ON COLUMN awf_numbering_pattern.numbering_id IS '넘버링아이디';
COMMENT ON COLUMN awf_numbering_pattern.pattern_name IS '패턴이름';
COMMENT ON COLUMN awf_numbering_pattern.pattern_type IS '패턴타입';
COMMENT ON COLUMN awf_numbering_pattern.pattern_value IS '패턴설정값';
COMMENT ON COLUMN awf_numbering_pattern.pattern_order IS '순서';

insert into awf_numbering_pattern values ('7a112d61751fs6f325714q053c421411', '40125c91714df6c325714e053c890125', '문서 Prefix', 'numbering.pattern.text', '{"value":"CSR"}', 1);
insert into awf_numbering_pattern values ('7a112d61751fs6f325714q053c421412', '40125c91714df6c325714e053c890125', '문서 날짜', 'numbering.pattern.date', '{"code":"pattern.format.yyyyMMdd"}', 2);
insert into awf_numbering_pattern values ('7a112d61751fs6f325714q053c421413', '40125c91714df6c325714e053c890125', '문서 시퀀스', 'numbering.pattern.sequence', '{"digit":3,"start-with":1,"full-fill":"Y"}', 3);
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
insert into awf_role_auth_map values ('users.general', 'notice.read');
insert into awf_role_auth_map values ('users.general', 'faq.read');
insert into awf_role_auth_map values ('users.general', 'token.read');
insert into awf_role_auth_map values ('users.general', 'token.create');
insert into awf_role_auth_map values ('users.general', 'document.read');
insert into awf_role_auth_map values ('users.general', 'document.create');
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
 * 스케줄작업정보
 */
DROP TABLE IF EXISTS awf_scheduled_task_mst cascade;

CREATE TABLE awf_scheduled_task_mst
(
	task_id bigint NOT NULL,
	task_type varchar(100),
	execute_class varchar(512),
	execute_query varchar(1024),
	execute_cycle_type varchar(100),
	execute_cycle_period bigint,
	cron_expression varchar(128),
	create_user_key varchar(128),
	create_dt timestamp,
	update_user_key varchar(128),
	update_dt timestamp,
	CONSTRAINT awf_scheduled_task_mst_pk PRIMARY KEY (task_id)
);

COMMENT ON TABLE awf_scheduled_task_mst IS '스케줄작업정보';
COMMENT ON COLUMN awf_scheduled_task_mst.task_id IS '작업아이디';
COMMENT ON COLUMN awf_scheduled_task_mst.task_type IS '작업유형';
COMMENT ON COLUMN awf_scheduled_task_mst.execute_class IS '실행클래스';
COMMENT ON COLUMN awf_scheduled_task_mst.execute_query IS '실행쿼리';
COMMENT ON COLUMN awf_scheduled_task_mst.execute_cycle_type IS '실행주기유형';
COMMENT ON COLUMN awf_scheduled_task_mst.execute_cycle_period IS '실행주기간격';
COMMENT ON COLUMN awf_scheduled_task_mst.cron_expression IS '크론표현식';
COMMENT ON COLUMN awf_scheduled_task_mst.create_user_key IS '등록자';
COMMENT ON COLUMN awf_scheduled_task_mst.create_dt IS '등록일';
COMMENT ON COLUMN awf_scheduled_task_mst.update_user_key IS '수정자';
COMMENT ON COLUMN awf_scheduled_task_mst.update_dt IS '수정일';

insert into awf_scheduled_task_mst values ('0', '0 0 12 * * *', 'co.brainz.framework.scheduling.task.DeleteTempFile', null, 'cron', 0, 'class', now(), null, '0509e09412534a6e98f04ca79abb6424', null);

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
insert into awf_url values ('/auths/list', 'get', '권한 관리 목록', 'TRUE');
insert into awf_url values ('/board-admin/category/list', 'get', '게시판 관리 카테고리 편집', 'TRUE');
insert into awf_url values ('/board-admin/category/{id}/edit', 'get', '게시판 관리 카테고리 편집', 'TRUE');
insert into awf_url values ('/board-admin/list', 'get', '게시판 관리 리스트 호출화면', 'TRUE');
insert into awf_url values ('/board-admin/new', 'get', '게시판 관리 신규 등록', 'TRUE');
insert into awf_url values ('/board-admin/search', 'get', '게시판 관리 리스트 조회 화면', 'TRUE');
insert into awf_url values ('/board-admin/{id}/edit', 'get', '게시판 관리 상세 편집', 'TRUE');
insert into awf_url values ('/board-admin/{id}/view', 'get', '게시판 관리 상세 조회 화면', 'TRUE');
insert into awf_url values ('/boards/list', 'get', '게시판 리스트 호출 화면', 'TRUE');
insert into awf_url values ('/boards/search', 'get', '게시판 리스트 화면', 'TRUE');
insert into awf_url values ('/boards/search/param', 'get', '게시판 리스트 화면', 'TRUE');
insert into awf_url values ('/boards/{id}/comments/list', 'get', '게시판 댓글 조회', 'TRUE');
insert into awf_url values ('/boards/{id}/edit', 'get', '게시판 편집', 'TRUE');
insert into awf_url values ('/boards/{id}/new', 'get', '게시판 신규 등록', 'TRUE');
insert into awf_url values ('/boards/{id}/replay/edit', 'get', '게시판 답글 편집', 'TRUE');
insert into awf_url values ('/boards/{id}/view', 'get', '게시판 상세 조회 화면', 'TRUE');
insert into awf_url values ('/certification/certifiedMail', 'get', '메일 발송', 'FALSE');
insert into awf_url values ('/certification/regist', 'post', '회원 가입 요청(인증 메일 발송 포함)', 'FALSE');
insert into awf_url values ('/certification/sendCertifiedMail', 'get', '메일 발송', 'FALSE');
insert into awf_url values ('/certification/signup', 'get', '회원 가입 화면 호출', 'FALSE');
insert into awf_url values ('/certification/status', 'get', '메일 인증 상태/재발송 요청 화면', 'FALSE');
insert into awf_url values ('/certification/valid', 'get', '메일 인증', 'FALSE');
insert into awf_url values ('/codes/edit', 'get', '코드 편집 화면', 'TRUE');
insert into awf_url values ('/custom-codes/list', 'get', '사용자 정의 코드 리스트 화면', 'TRUE');
insert into awf_url values ('/custom-codes/new', 'get', '사용자 정의 코드 신규 등록 화면', 'TRUE');
insert into awf_url values ('/custom-codes/search', 'get', '사용자 정의 코드 리스트 호출 화면', 'TRUE');
insert into awf_url values ('/custom-codes/{id}/edit', 'get', '사용자 정의 코드 수정 화면', 'TRUE');
insert into awf_url values ('/custom-codes/{id}/search', 'get', '커스텀 코드 데이터 조회 화면', 'TRUE');
insert into awf_url values ('/custom-codes/{id}/view', 'get', '사용자 정의 코드 상세 정보 화면', 'TRUE');
insert into awf_url values ('/dashboard/list', 'get', '대시보드 목록', 'TRUE');
insert into awf_url values ('/dashboard/view', 'get', '대시보드 상세 정보 화면', 'TRUE');
insert into awf_url values ('/documents-admin/list', 'get', '업무흐름 리스트 화면', 'TRUE');
insert into awf_url values ('/documents-admin/new', 'get', '신청서 생성 화면', 'TRUE');
insert into awf_url values ('/documents-admin/search', 'get', '업무흐름 데이터 + 목록화면', 'TRUE');
insert into awf_url values ('/documents-admin/{id}/display', 'get', '신청서 디스플레이 데이터 조회', 'TRUE');
insert into awf_url values ('/documents-admin/{id}/edit', 'get', '신청서 수정 화면', 'TRUE');
insert into awf_url values ('/documents/custom-code/{id}/data', 'get', '커스텀 코드 데이터 조회 화면', 'TRUE');
insert into awf_url values ('/documents/custom-code/{id}/data', 'post', '커스텀 코드 데이터 조회 화면 ', 'TRUE');
insert into awf_url values ('/documents/list', 'get', '신청서 리스트 화면', 'FALSE');
insert into awf_url values ('/documents/search', 'get', '신청서 리스트 호출 화면', 'FALSE');
insert into awf_url values ('/documents/{id}/print', 'get', '신청서 프린트 화면', 'TRUE');
insert into awf_url values ('/downloads/list', 'get', '자료실 리스트 화면', 'TRUE');
insert into awf_url values ('/downloads/new', 'get', '자료실 신규 등록', 'TRUE');
insert into awf_url values ('/downloads/search', 'get', '자료실 리스트 호출 화면', 'TRUE');
insert into awf_url values ('/downloads/{id}/edit', 'get', '자료실 편집', 'TRUE');
insert into awf_url values ('/downloads/{id}/view', 'get', '자료실 상세 조회 화면', 'TRUE');
insert into awf_url values ('/faqs/list', 'get', 'FAQ 목록 조회', 'TRUE');
insert into awf_url values ('/faqs/new', 'get', 'FAQ 등록', 'TRUE');
insert into awf_url values ('/faqs/search', 'get', 'FAQ 검색 화면 호출', 'TRUE');
insert into awf_url values ('/faqs/{id}/edit', 'get', 'FAQ 수정', 'TRUE');
insert into awf_url values ('/faqs/{id}/view', 'get', 'FAQ 보기', 'TRUE');
insert into awf_url values ('/forms/import', 'get', '폼 IMPORT 화면', 'TRUE');
insert into awf_url values ('/forms/{id}/edit', 'get', '폼 디자이너 편집화면', 'TRUE');
insert into awf_url values ('/forms/{id}/view', 'get', '폼 디자이너 상세화면', 'TRUE');
insert into awf_url values ('/forms/{id}/preview', 'get', '폼 디자이너 미리보기 화면', 'TRUE');
insert into awf_url values ('/forms/{id}/preview', 'post', '폼 디자이너 미리보기 화면', 'TRUE');
insert into awf_url values ('/forms/imageUpload/{id}/view', 'get', '이미지 업로드 화면', 'TRUE');
insert into awf_url values ('/forms-admin/list', 'get', '폼 리스트 화면', 'TRUE');
insert into awf_url values ('/forms-admin/new', 'get', '폼 기본 정보 등록 화면', 'TRUE');
insert into awf_url values ('/forms-admin/search', 'get', '폼 리스트 검색 호출 화면', 'TRUE');
insert into awf_url values ('/forms-admin/{formId}/edit', 'get', '폼 디자이너 편집 화면', 'TRUE');
insert into awf_url values ('/forms-admin/{formId}/view', 'get', '폼 디자이너 상세화면', 'TRUE');
insert into awf_url values ('/images', 'get', '이미지 관리 화면', 'TRUE');
insert into awf_url values ('/notices/list', 'get', '공지사항 목록', 'TRUE');
insert into awf_url values ('/notices/new', 'get', '공지사항 신규 등록 화면', 'TRUE');
insert into awf_url values ('/notices/search', 'get', '공지사항 검색 화면 호출 처리', 'TRUE');
insert into awf_url values ('/notices/{id}/edit', 'get', '공지사항 편집 화면', 'TRUE');
insert into awf_url values ('/notices/{id}/view', 'get', '공지사항 상세 화면', 'TRUE');
insert into awf_url values ('/notices/{id}/view-pop', 'get', '공지사항 팝업 화면', 'TRUE');
insert into awf_url values ('/notifications/list', 'get', '알림 리스트 화면', 'FALSE');
insert into awf_url values ('/oauth/{service}/callback', 'get', 'OAuth 로그인 응답 콜백', 'TRUE');
insert into awf_url values ('/oauth/{service}/login', 'get', 'OAuth 로그인 화면 호출', 'TRUE');
insert into awf_url values ('/portal/downloads/search', 'get', '포탈 자료실 조회', 'FALSE');
insert into awf_url values ('/portal/downloads/{id}/view', 'get', '포탈 자료실 상세조회', 'FALSE');
insert into awf_url values ('/portal/faqs', 'get', '포탈 FAQ 상세조회', 'FALSE');
insert into awf_url values ('/portal/list','get','포탈 조회', 'FALSE');
insert into awf_url values ('/portal/notices/search', 'get', '포탈 공지사항 조회', 'FALSE');
insert into awf_url values ('/portal/notices/{id}/view', 'get', '포탈 공지사항 상세 조회', 'FALSE');
insert into awf_url values ('/processes-admin/list', 'get', '프로세스 목록', 'TRUE');
insert into awf_url values ('/processes-admin/new', 'get', '프로세스 기본 정보 등록 화면', 'TRUE');
insert into awf_url values ('/processes-admin/search', 'get', '프로세스 리스트 검색 호출 화면', 'TRUE');
insert into awf_url values ('/processes-admin/{id}/edit', 'get', '프로세스 상세 수정 화면', 'TRUE');
insert into awf_url values ('/processes-admin/{id}/view', 'get', '프로세스 상세 보기 화면', 'TRUE');
insert into awf_url values ('/processes/{id}/edit', 'get', '프로세스 디자이너 편집 화면' ,'TRUE');
insert into awf_url values ('/processes/{id}/view', 'get', '프로세스 디자이너 보기 화면' ,'TRUE');
insert into awf_url values ('/processes/import', 'get', '프로세스 IMPORT 화면', 'TRUE');
insert into awf_url values ('/processes/{id}/status', 'get', '프로세스 상태', 'TRUE');
insert into awf_url values ('/processes/attachFile/view', 'get', '첨부파일 선택 팝업', 'TRUE');
insert into awf_url values ('/rest/auths', 'get', '권한 전체 목록 조회', 'TRUE');
insert into awf_url values ('/rest/auths', 'post', '권한 등록', 'TRUE');
insert into awf_url values ('/rest/auths/{id}', 'get', '권한 상세 정보 조회', 'TRUE');
insert into awf_url values ('/rest/auths/{id}', 'put', '권한 수정', 'TRUE');
insert into awf_url values ('/rest/auths/{id}', 'delete', '권한 삭제', 'TRUE');
insert into awf_url values ('/rest/board-admin', 'get', '게시판 관리 조회', 'TRUE');
insert into awf_url values ('/rest/board-admin', 'post', '게시판 관리 등록', 'TRUE');
insert into awf_url values ('/rest/board-admin', 'put', '게시판 관리 변경', 'TRUE');
insert into awf_url values ('/rest/board-admin/{id}/view', 'get', '게시판 관리 상세정보', 'TRUE');
insert into awf_url values ('/rest/board-admin/category', 'post', '게시판 관리 변경', 'TRUE');
insert into awf_url values ('/rest/board-admin/category/{id}', 'delete', '카테고리 관리 삭제', 'TRUE');
insert into awf_url values ('/rest/board-admin/{id}', 'delete', '게시판 관리 삭제', 'TRUE');
insert into awf_url values ('/rest/boards', 'get', '게시판리스트 데이터조회', 'TRUE');
insert into awf_url values ('/rest/boards', 'post', '게시판 등록', 'TRUE');
insert into awf_url values ('/rest/boards', 'put', '게시판 변경', 'TRUE');
insert into awf_url values ('/rest/boards/comments', 'put', '게시판 댓글 수정', 'TRUE');
insert into awf_url values ('/rest/boards/comments', 'post', '게시판 댓글 등록', 'TRUE');
insert into awf_url values ('/rest/boards/comments/{id}', 'delete', '게시판 댓글 삭제', 'TRUE');
insert into awf_url values ('/rest/boards/reply', 'post', '게시판 답글 등록', 'TRUE');
insert into awf_url values ('/rest/boards/{id}', 'delete', '게시판 삭제', 'TRUE');
insert into awf_url values ('/rest/codes', 'post', '코드 등록', 'TRUE');
insert into awf_url values ('/rest/codes', 'get', '코드 전체 조회', 'TRUE');
insert into awf_url values ('/rest/codes/{id}', 'put', '코드 수정', 'TRUE');
insert into awf_url values ('/rest/codes/{id}', 'get', '코드 상세 조회', 'TRUE');
insert into awf_url values ('/rest/codes/{id}', 'delete', '코드 삭제', 'TRUE');
insert into awf_url values ('/rest/comments', 'post', 'Commnet 저장', 'FALSE');
insert into awf_url values ('/rest/comments/{id}', 'delete', 'Comment 삭제', 'FALSE');
insert into awf_url values ('/rest/custom-codes', 'get', '커스텀 코드 조회', 'FALSE');
insert into awf_url values ('/rest/custom-codes', 'put', '커스텀 코드 수정', 'TRUE');
insert into awf_url values ('/rest/custom-codes', 'post', '커스텀 코드 등록', 'TRUE');
insert into awf_url values ('/rest/custom-codes/{id}', 'delete', '커스텀 코드 삭제', 'TRUE');
insert into awf_url values ('/rest/custom-codes/{id}', 'get', '커스텀코드 목록 조회', 'TRUE');
insert into awf_url values ('/rest/documents', 'get', '신청서 문서 목록 조회', 'TRUE');
insert into awf_url values ('/rest/documents-admin', 'post', '신청서 작성', 'TRUE');
insert into awf_url values ('/rest/documents-admin/{id}', 'delete', '신청서 삭제', 'TRUE');
insert into awf_url values ('/rest/documents-admin/{id}', 'get', '신청서 데이터 조회', 'TRUE');
insert into awf_url values ('/rest/documents-admin/{id}', 'put', '신청서 수정', 'TRUE');
insert into awf_url values ('/rest/documents-admin/{id}/display', 'put', '신청서 디스플레이 데이터 저장', 'TRUE');
insert into awf_url values ('/rest/documents-admin/{id}/display', 'get', '신청서 디스플레이 데이터 조회', 'TRUE');
insert into awf_url values ('/rest/documents/{id}/data', 'get', '신청서의 문서 데이터 조회', 'TRUE');
insert into awf_url values ('/rest/documents-admin', 'get', '업무흐름 데이터조회', 'TRUE');
insert into awf_url values ('/rest/downloads', 'get', '자료실리스트 조회', 'TRUE');
insert into awf_url values ('/rest/downloads', 'post', '자료실 등록', 'TRUE');
insert into awf_url values ('/rest/downloads', 'put', '자료실 변경', 'TRUE');
insert into awf_url values ('/rest/downloads/{id}', 'delete', '자료실 삭제', 'TRUE');
insert into awf_url values ('/rest/faqs', 'get', 'FAQ 리스트 데이터 조회', 'TRUE');
insert into awf_url values ('/rest/faqs', 'post', 'FAQ 등록 처리', 'TRUE');
insert into awf_url values ('/rest/faqs/{id}', 'put', 'FAQ 수정 처리', 'TRUE');
insert into awf_url values ('/rest/faqs/{id}', 'get', 'FAQ 상세 조회', 'TRUE');
insert into awf_url values ('/rest/faqs/{id}', 'delete', 'FAQ 삭제 처리', 'TRUE');
insert into awf_url values ('/rest/fileNameExtensionList', 'get', '파일 확장자목록', 'FALSE');
insert into awf_url values ('/rest/folders', 'post', '폴더 등록', 'FALSE');
insert into awf_url values ('/rest/folders/{id}', 'delete', '폴더 삭제', 'FALSE');
insert into awf_url values ('/rest/forms/data', 'put', '폼 디자이너 세부 정보 저장', 'TRUE');
insert into awf_url values ('/rest/forms/data/{id}', 'get', '폼 디자이너 세부 정보 불러오기', 'TRUE');
insert into awf_url values ('/rest/forms/imageDelete', 'delete', '이미지 삭제', 'TRUE');
insert into awf_url values ('/rest/forms/imageUpload', 'post', '이미지 업로드', 'TRUE');
insert into awf_url values ('/rest/forms/{id}', 'delete', '폼 디자이너 삭제', 'TRUE');
insert into awf_url values ('/rest/forms/{id}/data', 'get', '폼 디자이너 세부 정보 불러오기', 'TRUE');
insert into awf_url values ('/rest/forms/{id}/data', 'put', '폼 디자이너 세부 정보 저장', 'TRUE');
insert into awf_url values ('/rest/forms-admin', 'post', '폼 디자이너 기본 정보 저장 / 다른 이름 저장 처리', 'TRUE');
insert into awf_url values ('/rest/forms-admin/{formId}', 'put', '폼 디자이너 기본 정보 수정', 'TRUE');
insert into awf_url values ('/rest/forms-admin', 'get', '문서양식 데이터조회', 'TRUE');
insert into awf_url values ('/rest/images', 'post', '이미지 업로드', 'TRUE');
insert into awf_url values ('/rest/images', 'put', '이미지명 수정', 'TRUE');
insert into awf_url values ('/rest/images/{id}', 'get', '이미지 조회', 'FALSE');
insert into awf_url values ('/rest/images/{id}', 'delete', '이미지 삭제', 'TRUE');
insert into awf_url values ('/rest/images/list', 'get', '이미지 전체 조회', 'FALSE');
insert into awf_url values ('/rest/notices', 'get', '공지사항 목록 조회', 'TRUE');
insert into awf_url values ('/rest/notices', 'post', '공지사항 등록', 'TRUE');
insert into awf_url values ('/rest/notices/{id}', 'get', '공지사항 상세 조회', 'TRUE');
insert into awf_url values ('/rest/notices/{id}', 'delete', '공지사항 삭제', 'TRUE');
insert into awf_url values ('/rest/notices/{id}', 'put', '공지사항 수정', 'TRUE');
insert into awf_url values ('/rest/notifications/{id}', 'delete', '알림 리스트 삭제', 'FALSE');
insert into awf_url values ('/rest/notifications/{id}/confirm', 'put', '알림 리스트 확인 여부 수정', 'FALSE');
insert into awf_url values ('/rest/notifications/{id}/display', 'put', '알림 리스트 표시 여부 수정', 'FALSE');
insert into awf_url values ('/rest/portal/filedownload', 'get', '포탈 상세 파일 리스트 조회', 'FALSE');
insert into awf_url values ('/rest/portal/filelist', 'get', '포탈 상세 파일 리스트 조회', 'FALSE');
insert into awf_url values ('/rest/portal/list', 'get', '포탈 조회 (페이징)', 'FALSE');
insert into awf_url values ('/rest/portal/top', 'get', '포탈 첫화면 Top 조회', 'FALSE');
insert into awf_url values ('/rest/processes-admin', 'post', '프로세스 디자이너 기본 정보 저장 / 다른이름 저장 처리', 'TRUE');
insert into awf_url values ('/rest/processes-admin', 'put', '프로세스 수정', 'TRUE');
insert into awf_url values ('/rest/processes-admin', 'get', '발행 상태인 프로세스 목록 조회', 'TRUE');
insert into awf_url values ('/rest/processes-admin/list', 'get', '프로세스 데이터조회', 'TRUE');
insert into awf_url values ('/rest/processes/{id}', 'delete', '프로세스 디자이너 삭제', 'TRUE');
insert into awf_url values ('/rest/processes/{id}/data', 'put', '프로세스 디자이너 수정', 'TRUE');
insert into awf_url values ('/rest/processes/{id}/data', 'get', '프로세스 디자이너 불러오기', 'TRUE');
insert into awf_url values ('/rest/processes/{id}/simulation', 'get', '프로세스 시뮬레이션', 'TRUE');
insert into awf_url values ('/rest/processes/{id}/simulation', 'put', '프로세스 시뮬레이션', 'TRUE');
insert into awf_url values ('/rest/roles', 'post', '역할 등록', 'TRUE');
insert into awf_url values ('/rest/roles', 'get', '역할 전체 목록 조회', 'TRUE');
insert into awf_url values ('/rest/roles/{id}', 'get', '역할 상제 정보 조회', 'TRUE');
insert into awf_url values ('/rest/roles/{id}', 'put', '역할 수정', 'TRUE');
insert into awf_url values ('/rest/roles/{id}', 'delete', '역할 삭제', 'TRUE');
insert into awf_url values ('/rest/tags', 'post', 'Tag 저장', 'FALSE');
insert into awf_url values ('/rest/tags/{id}', 'delete', 'Tag 삭제', 'FALSE');
insert into awf_url values ('/rest/tokens', 'get', '문서함 목록 조회', 'TRUE');
insert into awf_url values ('/rest/tokens/data', 'post', 'token 신규 등록', 'TRUE');
insert into awf_url values ('/rest/tokens/data/{id}', 'get', 'NULL', 'TRUE');
insert into awf_url values ('/rest/tokens/{id}/data', 'get', 'NULL', 'TRUE');
insert into awf_url values ('/rest/tokens/{id}/data', 'put', 'token 수정', 'TRUE');
insert into awf_url values ('/rest/users', 'post', '사용자 등록', 'TRUE');
insert into awf_url values ('/rest/users', 'get', '전체 사용자 목록 조회', 'TRUE');
insert into awf_url values ('/rest/users/list', 'get', '사용자 데이터조회', 'TRUE');
insert into awf_url values ('/rest/users/{userkey}/all', 'put', '사용자가 자신의 정보를 업데이트', 'TRUE');
insert into awf_url values ('/rest/users/{userkey}/info', 'put', '사용자가 다른 사용자의 정보를 업데이트', 'FALSE');
insert into awf_url values ('/rest/wf/instances/{id}/history', 'get', 'test', 'FALSE');
insert into awf_url values ('/rest/wf/instances/{id}/latest', 'get', '마지막 토큰 정보 조회', 'FALSE');
insert into awf_url values ('/roles/edit', 'get', '역할 설정 뷰 호출', 'TRUE');
insert into awf_url values ('/roles/list', 'get', '역할 관리 목록 뷰 호출', 'TRUE');
insert into awf_url values ('/tokens/list', 'get', '처리할 문서 리스트 조회', 'FALSE');
insert into awf_url values ('/tokens/search', 'get', '로그인시 인증여부 체크 및 처리할 문서 페이지 이동', 'FALSE');
insert into awf_url values ('/tokens/view-pop/list', 'get', '관련문서 리스트', 'TRUE');
insert into awf_url values ('/tokens/{id}/edit', 'get', 'NULL', 'TRUE');
insert into awf_url values ('/tokens/{id}/view', 'get', 'NULL', 'TRUE');
insert into awf_url values ('/tokens/{id}/print', 'get', '처리할 문서 프린트 화면', 'TRUE');
insert into awf_url values ('/tokens/{id}/view-pop', 'get', '관련문서 팝업 화면', 'TRUE');
insert into awf_url values ('/users/list', 'get', '사용자 조회 목록 화면', 'TRUE');
insert into awf_url values ('/users/new', 'get', '사용자 등록 화면', 'TRUE');
insert into awf_url values ('/users/search', 'get', '사용자 검색, 목록 등 메인이 되는 조회 화면', 'TRUE');
insert into awf_url values ('/users/{userkey}/edit', 'get', '사용자 정보 수정 화면', 'TRUE');
insert into awf_url values ('/users/{userkey}/editSelf', 'get', '사용자 자기 정보 수정 화면', 'FALSE');

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
insert into awf_url_auth_map values ('/auths/list', 'get', 'auth.create');
insert into awf_url_auth_map values ('/auths/list', 'get', 'auth.delete');
insert into awf_url_auth_map values ('/auths/list', 'get', 'auth.update');
insert into awf_url_auth_map values ('/auths/list', 'get', 'auth.read');
insert into awf_url_auth_map values ('/board-admin/category/list', 'get', 'board.admin.create');
insert into awf_url_auth_map values ('/board-admin/category/list', 'get', 'board.admin.update');
insert into awf_url_auth_map values ('/board-admin/category/{id}/edit', 'get', 'board.admin.update');
insert into awf_url_auth_map values ('/board-admin/category/{id}/edit', 'get', 'board.admin.create');
insert into awf_url_auth_map values ('/board-admin/list', 'get', 'board.admin.create');
insert into awf_url_auth_map values ('/board-admin/list', 'get', 'board.admin.delete');
insert into awf_url_auth_map values ('/board-admin/list', 'get', 'board.admin.update');
insert into awf_url_auth_map values ('/board-admin/list', 'get', 'board.admin.read');
insert into awf_url_auth_map values ('/board-admin/new', 'get', 'board.admin.create');
insert into awf_url_auth_map values ('/board-admin/search', 'get', 'board.admin.update');
insert into awf_url_auth_map values ('/board-admin/search', 'get', 'board.admin.read');
insert into awf_url_auth_map values ('/board-admin/search', 'get', 'board.admin.delete');
insert into awf_url_auth_map values ('/board-admin/search', 'get', 'board.admin.create');
insert into awf_url_auth_map values ('/board-admin/{id}/edit', 'get', 'board.admin.update');
insert into awf_url_auth_map values ('/board-admin/{id}/edit', 'get', 'board.admin.create');
insert into awf_url_auth_map values ('/board-admin/{id}/view', 'get', 'board.admin.read');
insert into awf_url_auth_map values ('/board-admin/{id}/view', 'get', 'board.admin.delete');
insert into awf_url_auth_map values ('/board-admin/{id}/view', 'get', 'board.admin.create');
insert into awf_url_auth_map values ('/board-admin/{id}/view', 'get', 'board.admin.update');
insert into awf_url_auth_map values ('/boards/list', 'get', 'board.create');
insert into awf_url_auth_map values ('/boards/list', 'get', 'board.read');
insert into awf_url_auth_map values ('/boards/list', 'get', 'board.update');
insert into awf_url_auth_map values ('/boards/list', 'get', 'board.delete');
insert into awf_url_auth_map values ('/boards/search', 'get', 'board.delete');
insert into awf_url_auth_map values ('/boards/search', 'get', 'board.create');
insert into awf_url_auth_map values ('/boards/search', 'get', 'board.read');
insert into awf_url_auth_map values ('/boards/search', 'get', 'board.update');
insert into awf_url_auth_map values ('/boards/search/param', 'get', 'board.update');
insert into awf_url_auth_map values ('/boards/search/param', 'get', 'board.create');
insert into awf_url_auth_map values ('/boards/search/param', 'get', 'board.read');
insert into awf_url_auth_map values ('/boards/search/param', 'get', 'board.delete');
insert into awf_url_auth_map values ('/boards/{id}/comments/list', 'get', 'board.read');
insert into awf_url_auth_map values ('/boards/{id}/edit', 'get', 'board.create');
insert into awf_url_auth_map values ('/boards/{id}/edit', 'get', 'board.update');
insert into awf_url_auth_map values ('/boards/{id}/new', 'get', 'board.create');
insert into awf_url_auth_map values ('/boards/{id}/replay/edit', 'get', 'board.create');
insert into awf_url_auth_map values ('/boards/{id}/view', 'get', 'board.update');
insert into awf_url_auth_map values ('/boards/{id}/view', 'get', 'board.create');
insert into awf_url_auth_map values ('/boards/{id}/view', 'get', 'board.read');
insert into awf_url_auth_map values ('/boards/{id}/view', 'get', 'board.delete');
insert into awf_url_auth_map values ('/codes/edit', 'get', 'code.delete');
insert into awf_url_auth_map values ('/codes/edit', 'get', 'code.update');
insert into awf_url_auth_map values ('/codes/edit', 'get', 'code.read');
insert into awf_url_auth_map values ('/codes/edit', 'get', 'code.create');
insert into awf_url_auth_map values ('/custom-codes/list', 'get', 'custom.code.create');
insert into awf_url_auth_map values ('/custom-codes/list', 'get', 'custom.code.read');
insert into awf_url_auth_map values ('/custom-codes/list', 'get', 'custom.code.update');
insert into awf_url_auth_map values ('/custom-codes/list', 'get', 'custom.code.delete');
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
insert into awf_url_auth_map values ('/dashboard/list', 'get', 'document.read');
insert into awf_url_auth_map values ('/dashboard/view', 'get', 'document.read');
insert into awf_url_auth_map values ('/documents-admin/list', 'get', 'document.admin.delete');
insert into awf_url_auth_map values ('/documents-admin/list', 'get', 'document.admin.read');
insert into awf_url_auth_map values ('/documents-admin/list', 'get', 'document.admin.create');
insert into awf_url_auth_map values ('/documents-admin/list', 'get', 'document.admin.update');
insert into awf_url_auth_map values ('/documents-admin/new', 'get', 'document.admin.create');
insert into awf_url_auth_map values ('/documents-admin/search', 'get', 'document.admin.update');
insert into awf_url_auth_map values ('/documents-admin/search', 'get', 'document.admin.read');
insert into awf_url_auth_map values ('/documents-admin/search', 'get', 'document.admin.create');
insert into awf_url_auth_map values ('/documents-admin/search', 'get', 'document.admin.delete');
insert into awf_url_auth_map values ('/documents-admin/{id}/display', 'get', 'document.admin.create');
insert into awf_url_auth_map values ('/documents-admin/{id}/display', 'get', 'document.admin.update');
insert into awf_url_auth_map values ('/documents-admin/{id}/edit', 'get', 'document.admin.update');
insert into awf_url_auth_map values ('/documents-admin/{id}/edit', 'get', 'document.admin.create');
insert into awf_url_auth_map values ('/documents/custom-code/{id}/data', 'get', 'document.admin.update');
insert into awf_url_auth_map values ('/documents/custom-code/{id}/data', 'get', 'document.update');
insert into awf_url_auth_map values ('/documents/custom-code/{id}/data', 'post', 'document.update');
insert into awf_url_auth_map values ('/documents/custom-code/{id}/data', 'post', 'document.admin.update');
insert into awf_url_auth_map values ('/documents/list', 'get', 'document.read');
insert into awf_url_auth_map values ('/documents/search', 'get', 'document.read');
insert into awf_url_auth_map values ('/documents/{id}/print', 'get', 'document.read');
insert into awf_url_auth_map values ('/downloads/list', 'get', 'download.update');
insert into awf_url_auth_map values ('/downloads/list', 'get', 'download.delete');
insert into awf_url_auth_map values ('/downloads/list', 'get', 'download.create');
insert into awf_url_auth_map values ('/downloads/list', 'get', 'download.read');
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
insert into awf_url_auth_map values ('/faqs/list', 'get', 'faq.read');
insert into awf_url_auth_map values ('/faqs/new', 'get', 'faq.create');
insert into awf_url_auth_map values ('/faqs/search', 'get', 'faq.read');
insert into awf_url_auth_map values ('/faqs/{id}/edit', 'get', 'faq.update');
insert into awf_url_auth_map values ('/faqs/{id}/edit', 'get', 'faq.delete');
insert into awf_url_auth_map values ('/faqs/{id}/edit', 'get', 'faq.create');
insert into awf_url_auth_map values ('/faqs/{id}/view', 'get', 'faq.read');
insert into awf_url_auth_map values ('/forms/{id}/edit', 'get', 'form.read');
insert into awf_url_auth_map values ('/forms/{id}/preview', 'post', 'form.create');
insert into awf_url_auth_map values ('/forms/{id}/preview', 'get', 'form.read');
insert into awf_url_auth_map values ('/forms/{id}/view', 'get', 'form.read');
insert into awf_url_auth_map values ('/forms-admin/list', 'get', 'form.read');
insert into awf_url_auth_map values ('/forms-admin/new', 'get', 'form.read');
insert into awf_url_auth_map values ('/forms-admin/search', 'get', 'form.read');
insert into awf_url_auth_map values ('/forms-admin/{formId}/edit', 'get', 'form.read');
insert into awf_url_auth_map values ('/forms-admin/{formId}/view', 'get', 'form.read');
insert into awf_url_auth_map values ('/forms/imageUpload/{id}/view', 'get', 'form.read');
insert into awf_url_auth_map values ('/forms/import', 'get', 'form.read');
insert into awf_url_auth_map values ('/images', 'get', 'image.read');
insert into awf_url_auth_map values ('/notices/list', 'get', 'notice.read');
insert into awf_url_auth_map values ('/notices/new', 'get', 'notice.create');
insert into awf_url_auth_map values ('/notices/search', 'get', 'notice.delete');
insert into awf_url_auth_map values ('/notices/search', 'get', 'notice.update');
insert into awf_url_auth_map values ('/notices/search', 'get', 'notice.create');
insert into awf_url_auth_map values ('/notices/search', 'get', 'notice.read');
insert into awf_url_auth_map values ('/notices/{id}/edit', 'get', 'notice.create');
insert into awf_url_auth_map values ('/notices/{id}/edit', 'get', 'notice.update');
insert into awf_url_auth_map values ('/notices/{id}/view', 'get', 'notice.read');
insert into awf_url_auth_map values ('/notices/{id}/view-pop', 'get', 'notice.read');
insert into awf_url_auth_map values ('/processes-admin/list', 'get', 'process.read');
insert into awf_url_auth_map values ('/processes-admin/list', 'get', 'process.delete');
insert into awf_url_auth_map values ('/processes-admin/list', 'get', 'process.update');
insert into awf_url_auth_map values ('/processes-admin/list', 'get', 'process.create');
insert into awf_url_auth_map values ('/processes-admin/new', 'get', 'process.create');
insert into awf_url_auth_map values ('/processes-admin/search', 'get', 'process.read');
insert into awf_url_auth_map values ('/processes-admin/search', 'get', 'process.delete');
insert into awf_url_auth_map values ('/processes-admin/search', 'get', 'process.create');
insert into awf_url_auth_map values ('/processes-admin/search', 'get', 'process.update');
insert into awf_url_auth_map values ('/processes-admin/{id}/edit', 'get', 'process.update');
insert into awf_url_auth_map values ('/processes-admin/{id}/edit', 'get', 'process.create');
insert into awf_url_auth_map values ('/processes-admin/{id}/view', 'get', 'process.create');
insert into awf_url_auth_map values ('/processes-admin/{id}/view', 'get', 'process.update');
insert into awf_url_auth_map values ('/processes-admin/{id}/view', 'get', 'process.delete');
insert into awf_url_auth_map values ('/processes-admin/{id}/view', 'get', 'process.read');
insert into awf_url_auth_map values ('/processes/{id}/view', 'get', 'process.create');
insert into awf_url_auth_map values ('/processes/{id}/view', 'get', 'process.update');
insert into awf_url_auth_map values ('/processes/{id}/edit', 'get', 'process.create');
insert into awf_url_auth_map values ('/processes/{id}/edit', 'get', 'process.update');
insert into awf_url_auth_map values ('/processes/import', 'get', 'process.create');
insert into awf_url_auth_map values ('/processes/{id}/status', 'get', 'document.read');
insert into awf_url_auth_map values ('/processes/attachFile/view', 'get', 'process.read');
insert into awf_url_auth_map values ('/processes/attachFile/view', 'get', 'process.create');
insert into awf_url_auth_map values ('/processes/attachFile/view', 'get', 'process.update');
insert into awf_url_auth_map values ('/processes/attachFile/view', 'get', 'process.delete');
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
insert into awf_url_auth_map values ('/rest/board-admin', 'get', 'board.admin.read');
insert into awf_url_auth_map values ('/rest/board-admin', 'put', 'board.admin.update');
insert into awf_url_auth_map values ('/rest/board-admin', 'post', 'board.admin.create');
insert into awf_url_auth_map values ('/rest/board-admin/{id}/view', 'get', 'board.create');
insert into awf_url_auth_map values ('/rest/board-admin/{id}/view', 'get', 'board.update');
insert into awf_url_auth_map values ('/rest/board-admin/category', 'post', 'board.admin.create');
insert into awf_url_auth_map values ('/rest/board-admin/category/{id}', 'delete', 'board.admin.delete');
insert into awf_url_auth_map values ('/rest/board-admin/{id}', 'delete', 'board.admin.delete');
insert into awf_url_auth_map values ('/rest/boards', 'get', 'board.read');
insert into awf_url_auth_map values ('/rest/boards', 'put', 'board.update');
insert into awf_url_auth_map values ('/rest/boards', 'post', 'board.create');
insert into awf_url_auth_map values ('/rest/boards/comments', 'put', 'board.update');
insert into awf_url_auth_map values ('/rest/boards/comments', 'post', 'board.create');
insert into awf_url_auth_map values ('/rest/boards/comments/{id}', 'delete', 'board.delete');
insert into awf_url_auth_map values ('/rest/boards/reply', 'post', 'board.create');
insert into awf_url_auth_map values ('/rest/boards/{id}', 'delete', 'board.delete');
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
insert into awf_url_auth_map values ('/rest/documents-admin', 'post', 'document.admin.create');
insert into awf_url_auth_map values ('/rest/documents-admin/{id}', 'delete', 'document.admin.create');
insert into awf_url_auth_map values ('/rest/documents-admin/{id}', 'get', 'document.admin.create');
insert into awf_url_auth_map values ('/rest/documents-admin/{id}', 'put', 'document.admin.create');
insert into awf_url_auth_map values ('/rest/documents-admin/{id}', 'get', 'document.admin.update');
insert into awf_url_auth_map values ('/rest/documents-admin/{id}', 'put', 'document.admin.update');
insert into awf_url_auth_map values ('/rest/documents-admin/{id}', 'delete', 'document.admin.update');
insert into awf_url_auth_map values ('/rest/documents-admin/{id}', 'delete', 'document.admin.delete');
insert into awf_url_auth_map values ('/rest/documents-admin/{id}', 'put', 'document.admin.delete');
insert into awf_url_auth_map values ('/rest/documents-admin/{id}', 'get', 'document.admin.delete');
insert into awf_url_auth_map values ('/rest/documents-admin/{id}/display', 'put', 'document.admin.create');
insert into awf_url_auth_map values ('/rest/documents-admin/{id}/display', 'get', 'document.admin.create');
insert into awf_url_auth_map values ('/rest/documents/{id}/data', 'get', 'document.create');
insert into awf_url_auth_map values ('/rest/documents-admin', 'get', 'document.admin.read');
insert into awf_url_auth_map values ('/rest/downloads', 'get', 'download.read');
insert into awf_url_auth_map values ('/rest/downloads', 'post', 'download.update');
insert into awf_url_auth_map values ('/rest/downloads', 'put', 'download.update');
insert into awf_url_auth_map values ('/rest/downloads', 'post', 'download.read');
insert into awf_url_auth_map values ('/rest/downloads', 'put', 'download.read');
insert into awf_url_auth_map values ('/rest/downloads', 'put', 'download.create');
insert into awf_url_auth_map values ('/rest/downloads', 'post', 'download.delete');
insert into awf_url_auth_map values ('/rest/downloads', 'post', 'download.create');
insert into awf_url_auth_map values ('/rest/downloads', 'put', 'download.delete');
insert into awf_url_auth_map values ('/rest/downloads/{id}', 'delete', 'download.delete');
insert into awf_url_auth_map values ('/rest/faqs', 'get', 'faq.create');
insert into awf_url_auth_map values ('/rest/faqs', 'get', 'faq.read');
insert into awf_url_auth_map values ('/rest/faqs', 'post', 'faq.create');
insert into awf_url_auth_map values ('/rest/faqs/{id}', 'put', 'faq.update');
insert into awf_url_auth_map values ('/rest/faqs/{id}', 'get', 'faq.read');
insert into awf_url_auth_map values ('/rest/faqs/{id}', 'delete', 'faq.delete');
insert into awf_url_auth_map values ('/rest/fileNameExtensionList', 'get', 'file.read');
insert into awf_url_auth_map values ('/rest/forms/{id}', 'delete', 'form.delete');
insert into awf_url_auth_map values ('/rest/forms/{id}/data', 'get', 'form.read');
insert into awf_url_auth_map values ('/rest/forms/{id}/data', 'get', 'form.create');
insert into awf_url_auth_map values ('/rest/forms/{id}/data', 'put', 'form.create');
insert into awf_url_auth_map values ('/rest/forms/{id}/data', 'get', 'form.update');
insert into awf_url_auth_map values ('/rest/forms/{id}/data', 'put', 'form.update');
insert into awf_url_auth_map values ('/rest/forms/data', 'put', 'form.update');
insert into awf_url_auth_map values ('/rest/forms/data/{id}', 'get', 'form.update');
insert into awf_url_auth_map values ('/rest/forms/data/{id}', 'get', 'form.read');
insert into awf_url_auth_map values ('/rest/forms/imageDelete', 'delete', 'form.delete');
insert into awf_url_auth_map values ('/rest/forms/imageUpload', 'post', 'form.update');
insert into awf_url_auth_map values ('/rest/forms/imageUpload', 'post', 'form.delete');
insert into awf_url_auth_map values ('/rest/forms/imageUpload', 'post', 'form.create');
insert into awf_url_auth_map values ('/rest/forms/imageUpload', 'post', 'form.read');
insert into awf_url_auth_map values ('/rest/forms-admin', 'get', 'form.read');
insert into awf_url_auth_map values ('/rest/forms-admin', 'post', 'form.create');
insert into awf_url_auth_map values ('/rest/forms-admin', 'post', 'form.delete');
insert into awf_url_auth_map values ('/rest/forms-admin/{formId}', 'put', 'form.update');
insert into awf_url_auth_map values ('/rest/images', 'put', 'image.update');
insert into awf_url_auth_map values ('/rest/images', 'post', 'image.create');
insert into awf_url_auth_map values ('/rest/images/{id}', 'delete', 'image.delete');
insert into awf_url_auth_map values ('/rest/images/list', 'get', 'form.create');
insert into awf_url_auth_map values ('/rest/images/list', 'get', 'form.read');
insert into awf_url_auth_map values ('/rest/images/list', 'get', 'form.update');
insert into awf_url_auth_map values ('/rest/images/list', 'get', 'form.delete');
insert into awf_url_auth_map values ('/rest/notices', 'get', 'notice.read');
insert into awf_url_auth_map values ('/rest/notices', 'post', 'notice.create');
insert into awf_url_auth_map values ('/rest/notices/{id}', 'delete', 'notice.delete');
insert into awf_url_auth_map values ('/rest/notices/{id}', 'get', 'notice.read');
insert into awf_url_auth_map values ('/rest/notices/{id}', 'put', 'notice.update');
insert into awf_url_auth_map values ('/rest/processes-admin', 'post', 'process.read');
insert into awf_url_auth_map values ('/rest/processes-admin', 'put', 'process.update');
insert into awf_url_auth_map values ('/rest/processes-admin', 'get', 'process.read');
insert into awf_url_auth_map values ('/rest/processes-admin/list', 'get', 'process.read');
insert into awf_url_auth_map values ('/rest/processes/{id}', 'delete', 'process.delete');
insert into awf_url_auth_map values ('/rest/processes/{id}/data', 'get', 'process.create');
insert into awf_url_auth_map values ('/rest/processes/{id}/data', 'put', 'process.update');
insert into awf_url_auth_map values ('/rest/processes/{id}/data', 'get', 'process.update');
insert into awf_url_auth_map values ('/rest/processes/{id}/simulation', 'put', 'process.create');
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
insert into awf_url_auth_map values ('/rest/tokens', 'get', 'token.read');
insert into awf_url_auth_map values ('/rest/tokens/data', 'post', 'token.create');
insert into awf_url_auth_map values ('/rest/tokens/data/{id}', 'get', 'token.create');
insert into awf_url_auth_map values ('/rest/tokens/{id}/data', 'get', 'token.create');
insert into awf_url_auth_map values ('/rest/tokens/{id}/data', 'put', 'token.create');
insert into awf_url_auth_map values ('/rest/users', 'post', 'user.create');
insert into awf_url_auth_map values ('/rest/users', 'get', 'user.read');
insert into awf_url_auth_map values ('/rest/users/list', 'get', 'user.read');
insert into awf_url_auth_map values ('/rest/users/{userkey}/all', 'put', 'user.update');
insert into awf_url_auth_map values ('/rest/users/{userkey}/all', 'put', 'user.read');
insert into awf_url_auth_map values ('/rest/users/{userkey}/info', 'put', 'user.read');
insert into awf_url_auth_map values ('/rest/users/{userkey}/info', 'put', 'user.update');
insert into awf_url_auth_map values ('/roles/edit', 'get', 'role.update');
insert into awf_url_auth_map values ('/roles/edit', 'get', 'role.read');
insert into awf_url_auth_map values ('/roles/edit', 'get', 'role.create');
insert into awf_url_auth_map values ('/roles/edit', 'get', 'role.delete');
insert into awf_url_auth_map values ('/roles/list', 'get', 'role.create');
insert into awf_url_auth_map values ('/roles/list', 'get', 'role.update');
insert into awf_url_auth_map values ('/roles/list', 'get', 'role.read');
insert into awf_url_auth_map values ('/roles/list', 'get', 'role.delete');
insert into awf_url_auth_map values ('/tokens/list', 'get', 'token.create');
insert into awf_url_auth_map values ('/tokens/list', 'get', 'token.read');
insert into awf_url_auth_map values ('/tokens/search', 'get', 'token.read');
insert into awf_url_auth_map values ('/tokens/search', 'get', 'token.create');
insert into awf_url_auth_map values ('/tokens/view-pop/list', 'get', 'token.create');
insert into awf_url_auth_map values ('/tokens/view-pop/list', 'get', 'token.read');
insert into awf_url_auth_map values ('/tokens/{id}/edit', 'get', 'token.create');
insert into awf_url_auth_map values ('/tokens/{id}/view', 'get', 'token.read');
insert into awf_url_auth_map values ('/tokens/{id}/print', 'get', 'token.read');
insert into awf_url_auth_map values ('/tokens/{id}/view-pop', 'get', 'token.read');
insert into awf_url_auth_map values ('/tokens/{id}/view-pop', 'get', 'token.create');
insert into awf_url_auth_map values ('/users/list', 'get', 'user.update');
insert into awf_url_auth_map values ('/users/list', 'get', 'user.delete');
insert into awf_url_auth_map values ('/users/list', 'get', 'user.read');
insert into awf_url_auth_map values ('/users/list', 'get', 'user.create');
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
insert into awf_url_auth_map values ('/users/{userkey}/editSelf', 'get', 'user.read');
insert into awf_url_auth_map values ('/users/{userkey}/editSelf', 'get', 'user.update');


/**
 * 사용자정보
 */
DROP TABLE IF EXISTS awf_user cascade;

CREATE TABLE awf_user
(
	user_key varchar(128) NOT NULL,
	user_id  varchar(128) UNIQUE,
	user_name  varchar(128),
	password varchar(1024),
	email  varchar(1024) NOT NULL,
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
    avatar_id varchar(128) NOT NULL,
	create_user_key varchar(128),
	create_dt timestamp,
	update_user_key varchar(128),
	update_dt timestamp,
	CONSTRAINT awf_user_pk PRIMARY KEY (user_key),
	CONSTRAINT awf_user_uk UNIQUE (user_id ),
    CONSTRAINT awf_user_fk FOREIGN KEY (avatar_id) REFERENCES awf_avatar (avatar_id)
);

COMMENT ON TABLE awf_user IS '사용자정보';
COMMENT ON COLUMN awf_user.user_key IS '사용자키';
COMMENT ON COLUMN awf_user.user_id  IS '사용자아이디';
COMMENT ON COLUMN awf_user.user_name  IS '사용자명';
COMMENT ON COLUMN awf_user.password IS '패스워드';
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
COMMENT ON COLUMN awf_user.avatar_id IS '아바타 아이디';
COMMENT ON COLUMN awf_user.create_user_key IS '등록자';
COMMENT ON COLUMN awf_user.create_dt IS '등록일시';
COMMENT ON COLUMN awf_user.update_user_key IS '수정자';
COMMENT ON COLUMN awf_user.update_dt IS '수정일시';

insert into awf_user values ('0509e09412534a6e98f04ca79abb6424', 'admin', 'ADMIN', '$2a$10$QsZ1uzooTk2yEkWIiV8tyOUc/UODpMrjdReNUQnNWm0SpjyPVOy26', 'admin@gmail.com', TRUE, 0, now() + interval '3 month', null, null, 'KEAKvaudICgcbRwNaTTNSQ2XSvIcQyTdKdlYo80qvyQjbN5fAd', 'user.status.certified', null, null, 'user.platform.alice', 'Asia/Seoul', null, 'ko', 'yyyy-MM-dd HH:mm', 'default', '0509e09412534a6e98f04ca79abb6424', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_user values ('system', 'system', 'system', '', 'system@gmail.com', TRUE, 0, now() + interval '3 month', null, null, 'KEAKvaudICgcbRwNaTTNSQ2XSvIcQyTdKdlYo80qvyQjbN5fAd', 'user.status.certified', null, null, 'user.platform.alice', 'Asia/Seoul', null, 'ko', 'yyyy-MM-dd HH:mm', 'default', '0509e09412534a6e98f04ca79abb6424', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
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
/**
 * 
 */
DROP TABLE IF EXISTS wf_component_data cascade;

CREATE TABLE wf_component_data
(
	component_id varchar(128) NOT NULL,
	attribute_id varchar(100) NOT NULL,
	attribute_value varchar(512) NOT NULL,
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
DROP TABLE IF EXISTS wf_tag cascade;

CREATE TABLE wf_tag
(
	tag_id varchar(128) NOT NULL,
	tag_content varchar(256) NOT NULL,
	CONSTRAINT wf_tag_pk PRIMARY KEY (tag_id),
	CONSTRAINT wf_tag_uk UNIQUE (tag_content)
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
