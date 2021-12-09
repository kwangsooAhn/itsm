/**
 * CMDB 타입 정보
 */
DROP TABLE IF EXISTS cmdb_type cascade;

CREATE TABLE cmdb_type
(
	type_id character varying(128) NOT NULL,
	p_type_id character varying(128),
	type_name character varying(128),
	type_desc character varying(512),
	type_alias character varying(128),
	type_level int,
	type_seq int,
	class_id character varying(128) NOT NULL,
	type_icon character varying(200),
	create_user_key character varying(128),
	create_dt timestamp,
	update_user_key character varying(128),
	update_dt timestamp,
	CONSTRAINT cmdb_type_pk PRIMARY KEY (type_id),
	CONSTRAINT cmdb_type_uk UNIQUE (type_id),
	CONSTRAINT cmdb_type_fk FOREIGN KEY (class_id)
      REFERENCES cmdb_class (class_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

COMMENT ON TABLE cmdb_type IS 'CMDB 타입 정보';
COMMENT ON COLUMN cmdb_type.type_id IS '타입아이디';
COMMENT ON COLUMN cmdb_type.p_type_id IS '부모타입아이디';
COMMENT ON COLUMN cmdb_type.type_name IS '타입이름';
COMMENT ON COLUMN cmdb_type.type_desc IS '타입설명';
COMMENT ON COLUMN cmdb_type.type_alias IS '타입식별자';
COMMENT ON COLUMN cmdb_type.type_level IS '타입레벨';
COMMENT ON COLUMN cmdb_type.type_seq IS '타입정렬순서';
COMMENT ON COLUMN cmdb_type.class_id IS '클래스아이디';
COMMENT ON COLUMN cmdb_type.type_icon IS '타입아이콘';
COMMENT ON COLUMN cmdb_type.create_user_key IS '등록자';
COMMENT ON COLUMN cmdb_type.create_dt IS '등록일시';
COMMENT ON COLUMN cmdb_type.update_user_key IS '수정자';
COMMENT ON COLUMN cmdb_type.update_dt IS '수정일시';

insert into cmdb_type values ('root', null, 'root', null, 'CI', 0, 0, 'root', null, '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b017921277022000e', 'root', '서버', '서버입니다.', 'SERVER', 1, 10, '4028b88179210e1b0179211d13760005', 'image_server.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b0179217bb335004b', 'root', '네트워크', '네트워크 타입입니다.', 'NETWORK', 1, 20, '4028b88179210e1b0179212f17f90011', 'image_icmp.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b0179218fb2070055', 'root', '데이터베이스', '데이터베이스 Type입니다.', 'DATABASE', 1, 30, '4028b88179210e1b017921336fc60012', 'image_l4switch.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b0179219878cd0062', 'root', '보안장비', '보안장비 Type입니다.', 'security', 1, 40, '4028b88179210e1b01792160f3010031', 'image_snmp.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b017921a6b6950073', 'root', '소프트웨어', '소프트웨어 Type입니다.', 'software', 1, 50, '4028b8817a8eeaa3017a8f15873f0006', 'image_winnt.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b017921a6b6951174', 'root', '사업', '사업 Type입니다.', 'business', 1, 60, '4028b8817a8f3542017a8f9c365e001c', 'image_winnt.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b017921a6b6950173', 'root', '계약', '계약 Type입니다.', 'contract', 1, 70, '4028b8817a8f3542017a8f38b60d0000', 'image_winnt.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b017921a5b57a0072', 'root', '노트북', '노트북 Type입니다.', 'laptop', 1, 80, '4028b88179210e1b01792172d1b80042', 'image_server.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b017921a113fe006c', '4028b88179210e1b0179219878cd0062', '채울', '채울 Type입니다.', 'CHAEWOOL', 2, 10, '4028b88179210e1b0179216b6d55003b', 'image_snmp.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b017921a18a1b006d', '4028b88179210e1b0179219878cd0062', '체크멀', '체크멀  Type입니다.', 'CHECKMAL', 2, 20, '4028b88179210e1b0179216bf1a6003c', 'image_snmp.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b017921a230e4006e', '4028b88179210e1b0179219878cd0062', '코닉글로리', '코닉글로리 Type입니다.', 'KORINCGLORY', 2, 30, '4028b88179210e1b0179216cf921003d', 'image_snmp.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b017921a3f3320071', '4028b88179210e1b0179219878cd0062', '펜타시규리티', '펜타시규리티 Type입니다.', 'Penta', 2, 40, '4028b88179210e1b0179216e6e35003e', 'image_snmp.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b01792199e1d80063', '4028b88179210e1b0179219878cd0062', '넷크루즈', '넷크루즈', 'Netcruz', 2, 50, '4028b88179210e1b0179216226d30032', 'image_snmp.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b0179219c3f0a0064', '4028b88179210e1b0179219878cd0062', '소만사', '소만사 Type입니다.', 'Somansa', 2, 60, '4028b88179210e1b01792162e60f0033', 'image_snmp.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b0179219cf81d0065', '4028b88179210e1b0179219878cd0062', '시큐아이', 'SECUI Type입니다.', 'SECUI', 2, 70, '4028b88179210e1b017921643b830034', 'image_snmp.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b0179219dcf9d0067', '4028b88179210e1b0179219878cd0062', '에스지엔', '에스지엔 Type입니다.', 'SGN', 2, 80, '4028b88179210e1b01792165deef0036', 'image_snmp.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b0179219e84340068', '4028b88179210e1b0179219878cd0062', '에스큐브아이', '에스큐브아이 Type입니다.', 'S3I', 2, 90, '4028b88179210e1b01792166dba20037', 'image_snmp.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b0179219f04020069', '4028b88179210e1b0179219878cd0062', '이글루시큐리티', '이글루시큐리티 Type입니다.', 'IGLOO', 2, 100, '4028b88179210e1b01792167e5f30038', 'image_snmp.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b017921a097f5006b', '4028b88179210e1b0179219878cd0062', '지티원', '지티원 Type입니다.', 'GTONE', 2, 110, '4028b88179210e1b0179216af1c9003a', 'image_snmp.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b017921a355c10070', '4028b88179210e1b0179219878cd0062', '퓨처시스템', '퓨처시스템 Type입니다.', 'FutureSystem', 2, 120, '4028b88179210e1b0179216f07b2003f', 'image_snmp.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b017921a2b43a006f', '4028b88179210e1b0179219878cd0062', '파수닷컴', '파수닷컴 Type입니다.', 'FASOO', 2, 130, '4028b88179210e1b0179216f773b0040', 'image_snmp.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b0179219f9e5c006a', '4028b88179210e1b0179219878cd0062', '지니안', '지니안 Type입니다.', 'Genians', 2, 140, '4028b88179210e1b0179216a760a0039', 'image_snmp.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b0179219d58e20066', '4028b88179210e1b0179219878cd0062', '안랩', '안랩 Type입니다.', 'Ahnlab', 2, 150, '4028b88179210e1b017921651e3d0035', 'image_snmp.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b017921279d29000f', '4028b88179210e1b017921277022000e', 'Linux ', 'Linux Type입니다.', 'Linux', 2, 10, '4028b88179210e1b0179211eb65d0006', 'image_linux.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b0179217894fd0046', '4028b88179210e1b017921277022000e', 'HPUX', 'HPUX Type입니다.', 'HPUX', 2, 20, '4028b88179210e1b01792123742e0009', 'image_linux.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b01792127ed690010', '4028b88179210e1b017921277022000e', 'WinNT', 'WinNT Type입니다.', 'WinNT', 2, 30, '4028b88179210e1b017921201e8f0007', 'image_winnt.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b0179217906d10047', '4028b88179210e1b017921277022000e', 'Solaris', 'Solaris Type입니다.', 'Solaris', 2, 40, '4028b88179210e1b017921242450000a', 'image_linux.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b0179217aa9660049', '4028b88179210e1b017921277022000e', 'TRU64', 'TRU64 Type입니다.', 'TRU64', 2, 50, '4028b88179210e1b017921261f87000c', 'image_linux.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b0179217b3cfb004a', '4028b88179210e1b017921277022000e', 'UNIXWARE', 'UNIXWARE Type입니다.', 'UNIXWARE', 2, 60, '4028b88179210e1b01792126a13a000d', 'image_linux.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b0179217837510045', '4028b88179210e1b017921277022000e', 'AIX', 'AIX Type입니다.', 'AIX', 2, 70, '4028b88179210e1b01792121eea80008', 'image_linux.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b01792179d7e30048', '4028b88179210e1b017921277022000e', 'FreeBSD', 'FreeBSD Type입니다.', 'FreeBSD', 2, 80, '4028b88179210e1b01792125a23a000b', 'image_linux.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b0179219012c40056', '4028b88179210e1b0179218fb2070055', 'Oracle', 'Oracle Type입니다.', 'Oracle', 2, 10, '4028b88179210e1b017921362c960013', 'image_oracle.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b017921934c300059', '4028b88179210e1b0179218fb2070055', 'MS-SQL', 'MS-SQL Type입니다.', 'MSSQL', 2, 20, '4028b88179210e1b01792137989f0016', 'image_mssql.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b01792190905b0057', '4028b88179210e1b0179218fb2070055', 'MySQL', 'MySQL Type입니다.', 'MySQL', 2, 30, '4028b88179210e1b01792136918f0014', 'image_mysql.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b017921925bd60058', '4028b88179210e1b0179218fb2070055', 'MariaDB', 'Maria DB Type입니다.', 'MariaDB', 2, 40, '4028b88179210e1b017921370e000015', 'image_mariadb.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b017921952e81005d', '4028b88179210e1b0179218fb2070055', 'SQLite', 'SQLite Type입니다.', 'SQLite', 2, 50, '4028b88179210e1b0179213baac3001a', 'image_l4switch.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b017921942970005b', '4028b88179210e1b0179218fb2070055', 'Infomix', 'Infomix Type입니다.', 'Infomix', 2, 60, '4028b88179210e1b0179213a11f80018', 'image_l4switch.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b017921947c2e005c', '4028b88179210e1b0179218fb2070055', 'Derby', 'Derby Type입니다.', 'Derby', 2, 70, '4028b88179210e1b0179213b50f50019', 'image_l4switch.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b017921962d8d0060', '4028b88179210e1b0179218fb2070055', 'HBase', 'HBase Type입니다.', 'HBase', 2, 80, '4028b88179210e1b0179213cfac9001d', 'image_l4switch.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b01792196eb490061', '4028b88179210e1b0179218fb2070055', 'Cassandra', 'Cassandra Type입니다.', 'Cassandra', 2, 90,'4028b88179210e1b0179213d8436001e', 'image_l4switch.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b01792193da25005a', '4028b88179210e1b0179218fb2070055', 'DB2', 'DB2 Type입니다.', 'DB2', 2, 100, '4028b88179210e1b0179213972140017', 'image_l4switch.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b017921957a67005e', '4028b88179210e1b0179218fb2070055', 'MongoDB', 'MongoDB Type입니다.', 'MongoDB', 2, 110, '4028b88179210e1b0179213c2099001b', 'image_l4switch.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b01792195e8be005f', '4028b88179210e1b0179218fb2070055', 'Redis', 'Redis Type입니다.', 'Redis', 2, 120, '4028b88179210e1b0179213ca263001c', 'image_l4switch.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b0179217cc893004c', '4028b88179210e1b0179217bb335004b', 'OID', 'OID Type입니다.', 'OID', 2, 10, '4028b88179210e1b0179215a20da0028', 'image_icmp.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b0179217d5a96004d', '4028b88179210e1b0179217bb335004b', 'ICMP', 'ICMP Type입니다.', 'ICMP', 2, 20, '4028b88179210e1b0179215a98930029', 'image_icmp.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b0179217e7e7f004e', '4028b88179210e1b0179217bb335004b', 'SNMP', 'SNMP Type입니다.', 'SNMP', 2, 30, '4028b88179210e1b0179215b19bb002a', 'image_icmp.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b0179218131610050', '4028b88179210e1b0179217bb335004b', 'L2SWITCH', 'L2SWITCH Type입니다.', 'L2SWITCH', 2, 40, '4028b88179210e1b0179215c07b9002c', 'image_icmp.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b01792181cc9a0051', '4028b88179210e1b0179217bb335004b', 'L3SWITCH', 'L3SWITCH Type입니다.', 'L3SWITCH', 2, 50, '4028b88179210e1b0179215c8495002d', 'image_icmp.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b017921825cf70052', '4028b88179210e1b0179217bb335004b', 'L4SWITCH', 'L4SWITCH Type입니다.', 'L4SWITCH', 2, 60, '4028b88179210e1b0179215ce42d002e', 'image_icmp.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b0179217f2001004f', '4028b88179210e1b0179217bb335004b', 'IPCHECK', 'IPCHECK Type입니다.', 'IPCHECK', 2, 70, '4028b88179210e1b0179215b8bff002b', 'image_icmp.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b0179218e22100053', '4028b88179210e1b0179217bb335004b', 'APETC', 'APETC Type입니다.', 'APETC', 2, 80, '4028b88179210e1b0179215d4b68002f', 'image_icmp.png', '0509e09412534a6e98f04ca79abb6424', now());
insert into cmdb_type values ('4028b88179210e1b0179218ef2cc0054', '4028b88179210e1b0179217bb335004b', 'IPS', 'IPS Type입니다.', 'IPS', 2, 90, '4028b88179210e1b0179215dbbf80030', 'image_icmp.png', '0509e09412534a6e98f04ca79abb6424', now());
