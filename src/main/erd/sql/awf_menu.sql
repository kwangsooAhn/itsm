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

insert into awf_menu values ('config', 'menu', '', 3,TRUE);
insert into awf_menu values ('config.auth', 'config', '/auths/edit', 2,TRUE);
insert into awf_menu values ('config.boardAdmin', 'config', '/board-admin/search', 4,TRUE);
insert into awf_menu values ('config.code', 'config', '/codes/edit', 5,TRUE);
insert into awf_menu values ('config.role', 'config', '/roles/edit', 3,TRUE);
insert into awf_menu values ('config.user', 'config', '/users/search', 1,TRUE);
insert into awf_menu values ('my', 'menu', '', 1,TRUE);
insert into awf_menu values ('my.dashboard', 'my', '/dashboard/view', 1,TRUE);
insert into awf_menu values ('my.document', 'my', '/documents/search', 3,TRUE);
insert into awf_menu values ('my.token', 'my', '/tokens/search', 2,TRUE);
insert into awf_menu values ('portal', 'menu', '', 2,TRUE);
insert into awf_menu values ('portal.board', 'portal', '/boards/search', 4,TRUE);
insert into awf_menu values ('portal.download', 'portal', '/downloads/search', 3,TRUE);
insert into awf_menu values ('portal.faq', 'portal', '/faqs/search', 2,TRUE);
insert into awf_menu values ('portal.notice', 'portal', '/notices/search', 1,TRUE);
insert into awf_menu values ('workflow', 'menu', '', 4,TRUE);
insert into awf_menu values ('workflow.customCode', 'workflow', '/custom-codes/search', 4,TRUE);
insert into awf_menu values ('workflow.document', 'workflow', '/documents-admin/search', 3,TRUE);
insert into awf_menu values ('workflow.form', 'workflow', '/forms/search', 2,TRUE);
insert into awf_menu values ('workflow.image', 'workflow', '/images', 5,TRUE);
insert into awf_menu values ('workflow.process', 'workflow', '/processes/search', 1,TRUE);
