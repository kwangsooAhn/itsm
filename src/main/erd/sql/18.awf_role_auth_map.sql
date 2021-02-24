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

