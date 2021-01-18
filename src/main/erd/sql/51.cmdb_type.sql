/**
 * CMDB 타입 정보
 */
DROP TABLE IF EXISTS cmdb_type cascade;

CREATE TABLE cmdb_type
(
	type_id character varying(128) NOT NULL,
	p_type_id character varying(128),
	type_name character varying(100),
	type_desc character varying(500),
	type_level int,
	default_class_id character varying(128) NOT NULL,
	type_icon character varying(200),
	create_user_key character varying(128),
	create_dt timestamp without time zone,
	update_user_key character varying(128),
	update_dt timestamp without time zone,
	CONSTRAINT cmdb_type_pk PRIMARY KEY (type_id),
	CONSTRAINT cmdb_type_uk UNIQUE (type_id),
	CONSTRAINT cmdb_type_fk FOREIGN KEY (default_class_id)
      REFERENCES cmdb_class (class_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

COMMENT ON TABLE cmdb_type IS 'CMDB 타입 정보';
COMMENT ON COLUMN cmdb_type.type_id IS '타입아이디';
COMMENT ON COLUMN cmdb_type.p_type_id IS '부모타입아이디';
COMMENT ON COLUMN cmdb_type.type_name IS '타입이름';
COMMENT ON COLUMN cmdb_type.type_desc IS '타입설명';
COMMENT ON COLUMN cmdb_type.type_level IS '타입레벨';
COMMENT ON COLUMN cmdb_type.default_class_id IS '기본클래스아이디';
COMMENT ON COLUMN cmdb_type.type_icon IS '타입아이콘';
COMMENT ON COLUMN cmdb_type.create_user_key IS '등록자';
COMMENT ON COLUMN cmdb_type.create_dt IS '등록일시';
COMMENT ON COLUMN cmdb_type.update_user_key IS '수정자';
COMMENT ON COLUMN cmdb_type.update_dt IS '수정일시';

insert into cmdb_type values ('root', 'root', '서버', 'ROOT', 0, 'root', 'server.svg', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_type values ('587b4557275bcce81664db9e12485ae2', 'root', '서버', null, 1, 'df562114ab87c066adeaea79b2e4a8a2', 'server.svg', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_type values ('f18c064040304e493f4dc7385595601f', 'root', '네트워크', null, 1, '0d51e482f1a56e1074f69b5a1bce0138', 'network.svg', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_type values ('b2dac0d38b39a4f8da7b98c56e831465', 'root', '데이터베이스', null, 1, '85b3c35b31059e63aaa36ce2587ea070', 'database.svg', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_type values ('b1e60cb705e329ffbb5abf18e11cc32f', 'b2dac0d38b39a4f8da7b98c56e831465', '데이터베이스_PostgresSQL', null, 2, 'e6663412f62bd2d3daeeadd7a36a0b0d', 'database_postgresql.svg', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_type values ('4788eff6532d2232d12462c85789f595', 'b2dac0d38b39a4f8da7b98c56e831465', '데이터베이스_Oracle', null, 2, '40e346d210cd36229d03b403153e54ab', 'database_oracle.svg', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_type values ('3c37bbea924b4e300a4863bc1f1d41c8', 'root', '소프트웨어', null, 1, '52905fc1ae0183698f726aec3e038148', 'software.svg', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_type values ('237053b29c09fd365d049949a14df2c5', '3c37bbea924b4e300a4863bc1f1d41c8', 'OS', null, 2, '0e8dd74a27bbbf86201104e91df7ee88', 'os.svg', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_type values ('bd746ec53dd7b64a16157d5843360391', '237053b29c09fd365d049949a14df2c5', 'OS_Linux', null, 3, '39dbe77aa58b778064a0f4a10dd06b05', 'os_linux.svg', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_type values ('5c6601285d44385fb6dfcf184261aa04', '237053b29c09fd365d049949a14df2c5', 'OS_Window', null, 3, 'f88ee1c29fdf9d847ba6002abc5bbf1b', 'os_window.svg', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
