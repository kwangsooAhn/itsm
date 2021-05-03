/**
 * CMDB 클래스 정보
 */
DROP TABLE IF EXISTS cmdb_class cascade;

CREATE TABLE cmdb_class
(
    class_id character varying(128) NOT NULL,
    class_name character varying(128) NOT NULL,
    class_desc character varying(512),
    p_class_id character varying(128),
    class_level int,
    create_user_key character varying(128),
    create_dt timestamp,
    update_user_key character varying(128),
    update_dt timestamp,
    CONSTRAINT cmdb_class_pk PRIMARY KEY (class_id),
    CONSTRAINT cmdb_class_uk UNIQUE (class_id, class_name)
);

COMMENT ON TABLE cmdb_class IS 'CMDB_클래스 정보';
COMMENT ON COLUMN cmdb_class.class_id IS '클래스아이디';
COMMENT ON COLUMN cmdb_class.class_name IS '클래스이름';
COMMENT ON COLUMN cmdb_class.class_desc IS '클래스설명';
COMMENT ON COLUMN cmdb_class.p_class_id IS '부모클래스아이디';
COMMENT ON COLUMN cmdb_class.class_level IS '클래스레벨';
COMMENT ON COLUMN cmdb_class.create_user_key IS '등록자';
COMMENT ON COLUMN cmdb_class.create_dt IS '등록일시';
COMMENT ON COLUMN cmdb_class.update_user_key IS '수정자';
COMMENT ON COLUMN cmdb_class.update_dt IS '수정일시';

insert into cmdb_class values ('root', 'root', 'root', null, 0, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b017921740c250043', '일반정보 - 소프트웨어', '일반정보 - 소프트웨어 Class입니다.', 'root', 1, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b881792074460179210677bb0016', '일반정보', '일반정보 Class입니다.', 'root', 1, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179217130030041', '일반정보 - 노트북', '일반정보 - 노트북 Class입니다.', 'root', 1, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179217798830044', '유지보수정보 - 소프트웨어', '유지보수정보 - 소프트웨어 Class입니다.', '4028b88179210e1b017921740c250043', 2, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b01792172d1b80042', '설비정보 - 노트북', '설비정보 - 노트북 Class입니다.', '4028b88179210e1b0179217130030041', 2, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179216b6d55003b', '채울', '채울 Class입니다.', '4028b88179210e1b01792160f3010031', 6, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179216bf1a6003c', '체크멀', '체크멀 Class입니다.', '4028b88179210e1b01792160f3010031', 6, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179216cf921003d', '코닉글로리', '코닉글로리 Class입니다.', '4028b88179210e1b01792160f3010031', 6, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179216e6e35003e', '펜타시규리티', '펜타시규리티 Class입니다.', '4028b88179210e1b01792160f3010031', 6, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179216226d30032', '넷크루즈', '넷크루즈 Class입니다.', '4028b88179210e1b01792160f3010031', 6, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b01792162e60f0033', '소만사', '소만사 Class입니다.', '4028b88179210e1b01792160f3010031', 6, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b017921643b830034', '시큐아이', '시큐아이 Class입니다.', '4028b88179210e1b01792160f3010031', 6, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b01792165deef0036', '에스지엔', '에스지엔 Class입니다.', '4028b88179210e1b01792160f3010031', 6, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b01792166dba20037', '에스큐브아이', '에스큐브아이 Class입니다.', '4028b88179210e1b01792160f3010031', 6, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b01792167e5f30038', '이글루시큐리티', '이글루시큐리티 Class입니다.', '4028b88179210e1b01792160f3010031', 6, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179216af1c9003a', '지티원 ', '지티원 Class입니다.', '4028b88179210e1b01792160f3010031', 6, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179216f07b2003f', '퓨처시스템', '퓨처시스템 Class입니다.', '4028b88179210e1b01792160f3010031', 6, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179216f773b0040', '파수닷컴', '파수닷컴 Class입니다.', '4028b88179210e1b01792160f3010031', 6, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179216a760a0039', '지니안', '지이안 Class입니다.', '4028b88179210e1b01792160f3010031', 6, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b017921651e3d0035', '안랩', '안랩 Class입니다.', '4028b88179210e1b01792160f3010031', 6, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179213a11f80018', 'Infomix', 'Infomix Class입니다.', '4028b88179210e1b017921336fc60012', 6, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179213b50f50019', 'Derby', 'Derby Class입니다.', '4028b88179210e1b017921336fc60012', 6, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179213cfac9001d', 'HBase', 'HBase Class입니다.', '4028b88179210e1b017921336fc60012', 6, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179213d8436001e', 'Cassandra', 'Cassandra Class입니다.', '4028b88179210e1b017921336fc60012', 6, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179213baac3001a', 'SQLite', 'SQLite Class입니다.', '4028b88179210e1b017921336fc60012', 6, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b017921362c960013', 'Oracle', 'Oracle Class입니다.', '4028b88179210e1b017921336fc60012', 6, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b01792136918f0014', 'MySQL', 'MySQL Class입니다.', '4028b88179210e1b017921336fc60012', 6, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b017921370e000015', 'Maria DB', 'Maria DB Class입니다.', '4028b88179210e1b017921336fc60012', 6, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b01792137989f0016', 'MS-SQL', 'MS-SQL Class입니다.', '4028b88179210e1b017921336fc60012', 6, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179213972140017', 'DB2', 'DB2 Class입니다.', '4028b88179210e1b017921336fc60012', 6, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179213c2099001b', 'MongoDB', 'MongoDB Class입니다.', '4028b88179210e1b017921336fc60012', 6, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179213ca263001c', 'Redis', 'Redis Class입니다.', '4028b88179210e1b017921336fc60012', 6, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179215c07b9002c', 'L2SWITCH', 'L2SWITCH Class입니다.', '4028b88179210e1b0179212f17f90011', 6, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179215b8bff002b', 'IPCHECK', 'IPCHECK Class입니다.', '4028b88179210e1b0179212f17f90011', 6, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179215ce42d002e', 'L4SWITCH', 'L4SWITCH Class입니다.', '4028b88179210e1b0179212f17f90011', 6, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179215d4b68002f', 'APETC', 'APETC Class입니다.', '4028b88179210e1b0179212f17f90011', 6, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179215dbbf80030', 'IPS', 'IPS Class입니다.', '4028b88179210e1b0179212f17f90011', 6, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179215c8495002d', 'L3SWITCH', 'L3SWITCH Class입니다.', '4028b88179210e1b0179212f17f90011', 6, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179215a20da0028', 'OID', 'OID Class입니다.', '4028b88179210e1b0179212f17f90011', 6, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179215a98930029', 'ICMP', 'ICMP Class입니다.', '4028b88179210e1b0179212f17f90011', 6, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179215b19bb002a', 'SNMP', 'SNMP Class입니다.', '4028b88179210e1b0179212f17f90011', 6, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b01792123742e0009', 'HPUX', 'HPUX Class입니다.', '4028b88179210e1b0179211d13760005', 6, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b017921242450000a', 'Solaris', 'Solaris Class입니다.', '4028b88179210e1b0179211d13760005', 6, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b017921261f87000c', 'TRU64', 'TRU64 Class입니다.', '4028b88179210e1b0179211d13760005', 6, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b01792126a13a000d', 'UNIXWARE', 'UNIXWARE Class입니다.', '4028b88179210e1b0179211d13760005', 6, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b017921201e8f0007', 'WinNT ', 'WinNT Class입니다.', '4028b88179210e1b0179211d13760005', 6, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b01792121eea80008', 'AIX', 'AIX Class입니다.', '4028b88179210e1b0179211d13760005', 6, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b01792125a23a000b', 'FreeBSD', 'FreeBSD Class입니다.', '4028b88179210e1b0179211d13760005', 6, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179211eb65d0006', 'Linux', 'Linux Class입니다.', '4028b88179210e1b0179211d13760005', 6, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b01792160f3010031', '보안장비 - 설비정보', '보안장비 - 설비정보 Class입니다.', '4028b88179210e1b01792117aab10004', 5, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b017921336fc60012', '데이터베이스 - 설비정보', '데이터베이스 - 설비정보 Class입니다.', '4028b88179210e1b01792117aab10004', 5, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179212f17f90011', '네트워크 - 설비정보', '네트워크 - 설비정보 Class입니다.', '4028b88179210e1b01792117aab10004', 5, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179211d13760005', '서버 - 설비정보', '서버 - 설비정보 Class입니다.', '4028b88179210e1b01792117aab10004', 5, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b01792117aab10004', '자산보안등급 정보', '서버의 자산보안등급 Class입니다.', '4028b88179210e1b0179211490200003', 4, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b88179210e1b0179211490200003', '유지보수 항목', '서버 유지보수 항목입니다.', '4028b8817920744601792109b4800017', 3, '0509e09412534a6e98f04ca79abb6424',now());
insert into cmdb_class values ('4028b8817920744601792109b4800017', '설비정보 - 공통', '서버의 설비정보 Class입니다.', '4028b881792074460179210677bb0016', 2, '0509e09412534a6e98f04ca79abb6424',now());
