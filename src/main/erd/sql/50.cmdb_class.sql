/**
 * CMDB 클래스 정보
 */
DROP TABLE IF EXISTS cmdb_class cascade;

CREATE TABLE cmdb_class
(
	class_id character varying(128) NOT NULL,
	class_name character varying(100) NOT NULL,
	class_desc character varying(500),
	p_class_id character varying(128),
	create_user_key character varying(128),
	create_dt timestamp without time zone,
	update_user_key character varying(128),
	update_dt timestamp without time zone,
	CONSTRAINT cmdb_class_pk PRIMARY KEY (class_id),
	CONSTRAINT cmdb_class_uk UNIQUE (class_id, class_name)
);

COMMENT ON TABLE cmdb_class IS 'CMDB_클래스 정보';
COMMENT ON COLUMN cmdb_class.class_id IS '클래스아이디';
COMMENT ON COLUMN cmdb_class.class_name IS '클래스이름';
COMMENT ON COLUMN cmdb_class.class_desc IS '클래스설명';
COMMENT ON COLUMN cmdb_class.p_class_id IS '부모클래스아이디';
COMMENT ON COLUMN cmdb_class.create_user_key IS '등록자';
COMMENT ON COLUMN cmdb_class.create_dt IS '등록일시';
COMMENT ON COLUMN cmdb_class.update_user_key IS '수정자';
COMMENT ON COLUMN cmdb_class.update_dt IS '수정일시';

insert into cmdb_class values ('', 'ROOT', '', null, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_class values ('df562114ab87c066adeaea79b2e4a8a2', 'Server', '서버 Class', null, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_class values ('0d51e482f1a56e1074f69b5a1bce0138', 'Network', '네트워크 Class', null, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_class values ('85b3c35b31059e63aaa36ce2587ea070', 'Database', '데이터베이스 Class', null, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_class values ('52905fc1ae0183698f726aec3e038148', 'Software', '소프트웨어 Class', null, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_class values ('0e8dd74a27bbbf86201104e91df7ee88', 'OS', 'OS Class', null, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_class values ('e6663412f62bd2d3daeeadd7a36a0b0d', 'PostgreSQL', 'PostgreSQL Class', '85b3c35b31059e63aaa36ce2587ea070', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_class values ('40e346d210cd36229d03b403153e54ab', 'Oracle', 'ORACLE Class', '85b3c35b31059e63aaa36ce2587ea070', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_class values ('39dbe77aa58b778064a0f4a10dd06b05', 'Linux', 'Linux Class', '0e8dd74a27bbbf86201104e91df7ee88', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_class values ('f88ee1c29fdf9d847ba6002abc5bbf1b', 'Window', 'Window Class', '0e8dd74a27bbbf86201104e91df7ee88', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
