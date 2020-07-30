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
insert into awf_menu_auth_map values ('my', 'notice.read');
insert into awf_menu_auth_map values ('my', 'faq.delete');
insert into awf_menu_auth_map values ('my', 'faq.update');
insert into awf_menu_auth_map values ('my', 'faq.read');
insert into awf_menu_auth_map values ('my', 'notice.delete');
insert into awf_menu_auth_map values ('my', 'notice.update');
insert into awf_menu_auth_map values ('my', 'notice.create');
insert into awf_menu_auth_map values ('my', 'document.create');
insert into awf_menu_auth_map values ('my', 'document.read');
insert into awf_menu_auth_map values ('my', 'faq.create');
insert into awf_menu_auth_map values ('my.dashboard', 'document.read');
insert into awf_menu_auth_map values ('my.document', 'document.read');
insert into awf_menu_auth_map values ('my.document', 'document.create');
insert into awf_menu_auth_map values ('my.token', 'token.create');
insert into awf_menu_auth_map values ('my.token', 'token.read');
insert into awf_menu_auth_map values ('portal', 'notice.create');
insert into awf_menu_auth_map values ('portal', 'download.read');
insert into awf_menu_auth_map values ('portal', 'notice.delete');
insert into awf_menu_auth_map values ('portal', 'board.read');
insert into awf_menu_auth_map values ('portal', 'faq.read');
insert into awf_menu_auth_map values ('portal', 'download.delete');
insert into awf_menu_auth_map values ('portal', 'download.update');
insert into awf_menu_auth_map values ('portal', 'notice.update');
insert into awf_menu_auth_map values ('portal', 'board.create');
insert into awf_menu_auth_map values ('portal', 'download.create');
insert into awf_menu_auth_map values ('portal', 'faq.update');
insert into awf_menu_auth_map values ('portal', 'faq.delete');
insert into awf_menu_auth_map values ('portal', 'faq.create');
insert into awf_menu_auth_map values ('portal', 'notice.read');
insert into awf_menu_auth_map values ('portal', 'board.update');
insert into awf_menu_auth_map values ('portal', 'board.delete');
insert into awf_menu_auth_map values ('portal.board', 'board.create');
insert into awf_menu_auth_map values ('portal.board', 'board.read');
insert into awf_menu_auth_map values ('portal.board', 'board.update');
insert into awf_menu_auth_map values ('portal.board', 'board.delete');
insert into awf_menu_auth_map values ('portal.download', 'download.delete');
insert into awf_menu_auth_map values ('portal.download', 'download.update');
insert into awf_menu_auth_map values ('portal.download', 'download.read');
insert into awf_menu_auth_map values ('portal.download', 'download.create');
insert into awf_menu_auth_map values ('portal.faq', 'faq.update');
insert into awf_menu_auth_map values ('portal.faq', 'faq.read');
insert into awf_menu_auth_map values ('portal.faq', 'faq.delete');
insert into awf_menu_auth_map values ('portal.faq', 'faq.create');
insert into awf_menu_auth_map values ('portal.notice', 'notice.update');
insert into awf_menu_auth_map values ('portal.notice', 'notice.create');
insert into awf_menu_auth_map values ('portal.notice', 'notice.read');
insert into awf_menu_auth_map values ('portal.notice', 'notice.delete');
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
insert into awf_menu_auth_map values ('workflow.document', 'document.admin.create');
insert into awf_menu_auth_map values ('workflow.document', 'document.admin.update');
insert into awf_menu_auth_map values ('workflow.document', 'document.admin.delete');
insert into awf_menu_auth_map values ('workflow.document', 'document.admin.read');
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
