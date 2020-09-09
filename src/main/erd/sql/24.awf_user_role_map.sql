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

insert into awf_user_role_map values ('0509e09412534a6e98f04ca79abb6424', 'admin');