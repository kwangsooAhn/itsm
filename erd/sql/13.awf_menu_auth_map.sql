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

insert into awf_menu_auth_map values ('dashboard', 'general');
insert into awf_menu_auth_map values ('document', 'general');
insert into awf_menu_auth_map values ('faq', 'general');
insert into awf_menu_auth_map values ('notice', 'general');
insert into awf_menu_auth_map values ('board', 'general');
insert into awf_menu_auth_map values ('download', 'general');
insert into awf_menu_auth_map values ('token', 'general');
insert into awf_menu_auth_map values ('cmdb', 'cmdb.manage');
insert into awf_menu_auth_map values ('cmdb.attribute', 'cmdb.manage');
insert into awf_menu_auth_map values ('cmdb.ci', 'cmdb.manage');
insert into awf_menu_auth_map values ('cmdb.class', 'cmdb.manage');
insert into awf_menu_auth_map values ('cmdb.type', 'cmdb.manage');
insert into awf_menu_auth_map values ('cmdb', 'cmdb.view');
insert into awf_menu_auth_map values ('cmdb.ci', 'cmdb.view');
insert into awf_menu_auth_map values ('config', 'system.manage');
insert into awf_menu_auth_map values ('config.auth', 'system.manage');
insert into awf_menu_auth_map values ('config.boardAdmin', 'system.manage');
insert into awf_menu_auth_map values ('config.boardAdmin', 'portal.manage');
insert into awf_menu_auth_map values ('config.code', 'system.manage');
insert into awf_menu_auth_map values ('config.product', 'system.manage');
insert into awf_menu_auth_map values ('config.role', 'system.manage');
insert into awf_menu_auth_map values ('config.scheduler', 'system.manage');
insert into awf_menu_auth_map values ('config.user', 'system.manage');
insert into awf_menu_auth_map values ('report', 'report.manage');
insert into awf_menu_auth_map values ('report.chart', 'report.manage');
insert into awf_menu_auth_map values ('report.report', 'report.manage');
insert into awf_menu_auth_map values ('report.template', 'report.manage');
insert into awf_menu_auth_map values ('report', 'report.view');
insert into awf_menu_auth_map values ('report.chart', 'report.view');
insert into awf_menu_auth_map values ('report.report', 'report.view');
insert into awf_menu_auth_map values ('report.template', 'report.view');
insert into awf_menu_auth_map values ('workflow', 'workflow.manage');
insert into awf_menu_auth_map values ('workflow.customCode', 'workflow.manage');
insert into awf_menu_auth_map values ('workflow.form', 'workflow.manage');
insert into awf_menu_auth_map values ('workflow.image', 'workflow.manage');
insert into awf_menu_auth_map values ('workflow.numberingPattern', 'workflow.manage');
insert into awf_menu_auth_map values ('workflow.numberingRule', 'workflow.manage');
insert into awf_menu_auth_map values ('workflow.process', 'workflow.manage');
insert into awf_menu_auth_map values ('workflow.workflowAdmin', 'workflow.manage');