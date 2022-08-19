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
insert into awf_menu values ('archive', 'menu', '/archives/search', 6, 'TRUE');
insert into awf_menu values ('board', 'menu', '/boards/articles/search', 7, 'TRUE');
insert into awf_menu values ('calendar', 'menu', '/calendars', 8, 'TRUE');
insert into awf_menu values ('statistic', 'menu', '', 9, 'TRUE');
insert into awf_menu values ('statistic.customReportTemplate', 'statistic', '/statistics/customReportTemplate/search', 1, 'TRUE');
insert into awf_menu values ('statistic.customReport', 'statistic', '/statistics/customReport/search', 2, 'TRUE');
insert into awf_menu values ('statistic.basicReport', 'statistic', '/statistics/basicReport/search', 3, 'FALSE');
insert into awf_menu values ('statistic.customChart', 'statistic', '/statistics/customChart/search', 4, 'TRUE');
insert into awf_menu values ('statistic.basicChart', 'statistic', '/statistics/basicChart/search', 5, 'FALSE');
insert into awf_menu values ('statistic.dashboardTemplate', 'statistic', '/statistics/dashboardTemplate/search', 6, 'FALSE');
insert into awf_menu values ('statistic.customDashboardTemplate', 'statistic', '/statistics/customDashboardTemplate/edit', 7, 'FALSE');
insert into awf_menu values ('workflow', 'menu', '', 10, 'TRUE');
insert into awf_menu values ('workflow.process', 'workflow', '/processes/search', 1, 'TRUE');
insert into awf_menu values ('workflow.form', 'workflow', '/forms/search', 2, 'TRUE');
insert into awf_menu values ('workflow.workflowAdmin', 'workflow', '/workflows/search', 3, 'TRUE');
insert into awf_menu values ('workflow.customCode', 'workflow', '/custom-codes/search', 4, 'TRUE');
insert into awf_menu values ('workflow.resource', 'workflow', '/resources', 5, 'TRUE');
insert into awf_menu values ('workflow.numberingPattern', 'workflow', '/numberingPatterns/search', 6, 'TRUE');
insert into awf_menu values ('workflow.numberingRule', 'workflow', '/numberingRules/search', 7, 'TRUE');
insert into awf_menu values ('cmdb', 'menu', '', 11, 'TRUE');
insert into awf_menu values ('cmdb.attribute', 'cmdb', '/cmdb/attributes/search', 1, 'TRUE');
insert into awf_menu values ('cmdb.class', 'cmdb', '/cmdb/class/edit', 2, 'TRUE');
insert into awf_menu values ('cmdb.icon', 'cmdb', '/cmdb/icons', 3, 'TRUE');
insert into awf_menu values ('cmdb.type', 'cmdb', '/cmdb/types/edit', 4, 'TRUE');
insert into awf_menu values ('cmdb.ci', 'cmdb', '/cmdb/cis/search', 5, 'TRUE');
insert into awf_menu values ('sla', 'menu', '', 12, 'TRUE');
insert into awf_menu values ('sla.metricStatus', 'sla', '/sla/metric-status/search', 1, 'TRUE');
insert into awf_menu values ('sla.yearStatus', 'sla', '/sla/metrics/annual/search', 2, 'TRUE');
insert into awf_menu values ('sla.manualMetric', 'sla', '/sla/metric-manuals/search', 3, 'TRUE');
insert into awf_menu values ('sla.year', 'sla', '/sla/metrics/search', 4, 'TRUE');
insert into awf_menu values ('sla.pool', 'sla', '/sla/metric-pools/search', 5, 'TRUE');
insert into awf_menu values ('config', 'menu', '', 13, 'TRUE');
insert into awf_menu values ('config.organization', 'config', '/organizations/edit', 1, 'TRUE');
insert into awf_menu values ('config.user', 'config', '/users/search', 2, 'TRUE');
insert into awf_menu values ('config.role', 'config', '/roles/search', 3, 'TRUE');
insert into awf_menu values ('config.boardAdmin', 'config', '/boards/search', 4, 'TRUE');
insert into awf_menu values ('config.code', 'config', '/codes/edit', 5, 'TRUE');
insert into awf_menu values ('config.scheduler', 'config', '/schedulers/search', 6, 'TRUE');
insert into awf_menu values ('config.service', 'config', '/service-category/edit', 7, 'TRUE');
insert into awf_menu values ('config.product', 'config', '', 8, 'TRUE');
insert into awf_menu values('notification', 'menu', '', 15);
insert into awf_menu values('notification.config', 'notification', '/notifications/edit', 1);
insert into awf_menu values('notification.record', 'notification', '/notifications/record', 2);
