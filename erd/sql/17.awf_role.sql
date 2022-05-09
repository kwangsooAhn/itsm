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

INSERT INTO awf_role VALUES ('system.admin', '시스템 관리자', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_role VALUES ('service.admin', '서비스 관리자', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_role VALUES ('service.manager', '서비스 담당자', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_role VALUES ('general.user', '일반 사용자', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_role VALUES ('workflow.admin', '업무흐름 관리자', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_role VALUES ('cmdb.admin', 'CMDB 관리자', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_role VALUES ('portal.admin', '포털 관리자', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_role VALUES ('sla.admin', 'SLA 관리자', ' ', '0509e09412534a6e98f04ca79abb6424', now(), NULL, NULL);
