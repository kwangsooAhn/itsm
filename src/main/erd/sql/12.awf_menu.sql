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

