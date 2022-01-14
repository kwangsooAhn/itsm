/**
 * 사용자역할매핑
 */
DROP TABLE IF EXISTS awf_user_role_map cascade;

CREATE TABLE awf_user_role_map
(
	user_key varchar(128) NOT NULL,
	role_id varchar(100) NOT NULL,
	CONSTRAINT awf_user_role_map_pk PRIMARY KEY (user_key, role_id),
	CONSTRAINT awf_user_role_map_fk1 FOREIGN KEY (user_key) REFERENCES awf_user (user_key),
	CONSTRAINT awf_user_role_map_fk2 FOREIGN KEY (role_id) REFERENCES awf_role (role_id)
);

COMMENT ON TABLE awf_user_role_map IS '사용자역할매핑';
COMMENT ON COLUMN awf_user_role_map.user_key IS '사용자키';
COMMENT ON COLUMN awf_user_role_map.role_id IS '역할아이디';

insert into awf_user_role_map values ('0509e09412534a6e98f04ca79abb6424', 'system.admin');
insert into awf_user_role_map values ('4028b21c7c4df297017c4e595fd90000', 'service.admin');
insert into awf_user_role_map values ('40288ad27c729b34017c729c2e370000', 'service.manager');
insert into awf_user_role_map values ('40288ada7cfd3301017cfd3a78580000', 'general.user');
insert into awf_user_role_map values ('2c9180867d0b3336017d0de8bf480001', 'workflow.admin');
insert into awf_user_role_map values ('2c91808e7c75dad2017c781635e20000', 'cmdb.admin');
insert into awf_user_role_map values ('2c91808e7c75dad2017c781635e22000', 'portal.admin');
