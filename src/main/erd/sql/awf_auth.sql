/**
 * 권한
 */
DROP TABLE IF EXISTS awf_auth cascade;

CREATE TABLE awf_auth
(
	auth_id varchar(100) NOT NULL,
	auth_name varchar(128) NOT NULL,
	auth_desc text,
	create_user_key varchar(128) NOT NULL,
	create_dt timestamp NOT NULL,
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

