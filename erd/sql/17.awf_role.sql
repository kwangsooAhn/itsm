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
/* 구성관리 */
INSERT INTO awf_role VALUES ('configuration.change.manager', '구성관리 관리자', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_role VALUES ('configuration.change.assignee', '구성관리 담당자', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
/* 문제관리 */
INSERT INTO awf_role VALUES ('problem.manager', '문제관리 관리자', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_role VALUES ('problem.assignee', '문제관리 담당자', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
