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

INSERT INTO awf_role_auth_map VALUES ('system.admin', 'general');
INSERT INTO awf_role_auth_map VALUES ('system.admin', 'cmdb.manage');
INSERT INTO awf_role_auth_map VALUES ('system.admin', 'cmdb.view');
INSERT INTO awf_role_auth_map VALUES ('system.admin', 'workflow.manage');
INSERT INTO awf_role_auth_map VALUES ('system.admin', 'report.manage');
INSERT INTO awf_role_auth_map VALUES ('system.admin', 'report.view');
INSERT INTO awf_role_auth_map VALUES ('system.admin', 'system.manage');
INSERT INTO awf_role_auth_map VALUES ('service.admin', 'general');
INSERT INTO awf_role_auth_map VALUES ('service.admin', 'cmdb.view');
INSERT INTO awf_role_auth_map VALUES ('service.admin', 'workflow.expire');
INSERT INTO awf_role_auth_map VALUES ('service.admin', 'report.view');
INSERT INTO awf_role_auth_map VALUES ('service.manager', 'general');
INSERT INTO awf_role_auth_map VALUES ('service.manager', 'cmdb.view');
INSERT INTO awf_role_auth_map VALUES ('service.manager', 'report.view');
INSERT INTO awf_role_auth_map VALUES ('general.user', 'general');
INSERT INTO awf_role_auth_map VALUES ('general.user', 'cmdb.view');
INSERT INTO awf_role_auth_map VALUES ('workflow.admin', 'general');
INSERT INTO awf_role_auth_map VALUES ('workflow.admin', 'cmdb.view');
INSERT INTO awf_role_auth_map VALUES ('workflow.admin', 'workflow.manage');
INSERT INTO awf_role_auth_map VALUES ('workflow.admin', 'report.manage');
INSERT INTO awf_role_auth_map VALUES ('workflow.admin', 'report.view');
INSERT INTO awf_role_auth_map VALUES ('cmdb.admin', 'general');
INSERT INTO awf_role_auth_map VALUES ('cmdb.admin', 'cmdb.manage');
INSERT INTO awf_role_auth_map VALUES ('cmdb.admin', 'cmdb.view');
INSERT INTO awf_role_auth_map VALUES ('cmdb.admin', 'report.view');
