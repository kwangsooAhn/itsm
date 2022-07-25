/**
 * CMDB 아이콘 관리
 */
DROP TABLE IF EXISTS cmdb_ci_icon cascade;

CREATE TABLE cmdb_ci_icon
(
    file_seq bigint NOT null,
    file_name varchar(512) NOT NULL,
    file_name_extension varchar(128) NOT NULL,
    editable boolean default true,
    create_user_key varchar(128),
    create_dt timestamp,
    update_user_key varchar(128),
    update_dt timestamp,
    CONSTRAINT cmdb_ci_icon_pk PRIMARY KEY (file_seq)
);

COMMENT ON TABLE cmdb_ci_icon IS 'CMDB 아이콘 관리';
COMMENT ON COLUMN cmdb_ci_icon.file_seq IS '아이콘파일관리번호';
COMMENT ON COLUMN cmdb_ci_icon.file_name IS '아이콘파일명';
COMMENT ON COLUMN cmdb_ci_icon.file_name_extension IS '아이콘파일확장자';
COMMENT ON COLUMN cmdb_ci_icon.editable IS '수정가능여부';
COMMENT ON COLUMN cmdb_ci_icon.create_user_key IS '등록자';
COMMENT ON COLUMN cmdb_ci_icon.create_dt IS '등록일';
COMMENT ON COLUMN cmdb_ci_icon.update_user_key IS '수정자';
COMMENT ON COLUMN cmdb_ci_icon.update_dt IS '수정일';

-- 기본 아이콘
INSERT INTO cmdb_ci_icon VALUES (1, 'image_assets', 'png', false, '2c91808e7c75dad2017c781635e20000', now(), null, null);
INSERT INTO cmdb_ci_icon VALUES (2, 'image_icmp', 'png', false, '2c91808e7c75dad2017c781635e20000', now(), null, null);
INSERT INTO cmdb_ci_icon VALUES (3, 'image_l4switch', 'png', false, '2c91808e7c75dad2017c781635e20000', now(), null, null);
INSERT INTO cmdb_ci_icon VALUES (4, 'image_linux', 'png', false, '2c91808e7c75dad2017c781635e20000', now(), null, null);
INSERT INTO cmdb_ci_icon VALUES (5, 'image_maintenance', 'png', false, '2c91808e7c75dad2017c781635e20000', now(), null, null);
INSERT INTO cmdb_ci_icon VALUES (6, 'image_mariadb', 'png', false, '2c91808e7c75dad2017c781635e20000', now(), null, null);
INSERT INTO cmdb_ci_icon VALUES (7, 'image_mssql', 'png', false, '2c91808e7c75dad2017c781635e20000', now(), null, null);
INSERT INTO cmdb_ci_icon VALUES (8, 'image_mysql', 'png', false, '2c91808e7c75dad2017c781635e20000', now(), null, null);
INSERT INTO cmdb_ci_icon VALUES (9, 'image_oracle', 'png', false, '2c91808e7c75dad2017c781635e20000', now(), null, null);
INSERT INTO cmdb_ci_icon VALUES (10, 'image_postgresql', 'png', false, '2c91808e7c75dad2017c781635e20000', now(), null, null);
INSERT INTO cmdb_ci_icon VALUES (11, 'image_server', 'png', false, '2c91808e7c75dad2017c781635e20000', now(), null, null);
INSERT INTO cmdb_ci_icon VALUES (12, 'image_snmp', 'png', false, '2c91808e7c75dad2017c781635e20000', now(), null, null);
INSERT INTO cmdb_ci_icon VALUES (13, 'image_software', 'png', false, '2c91808e7c75dad2017c781635e20000', now(), null, null);
INSERT INTO cmdb_ci_icon VALUES (14, 'image_storage', 'png', false, '2c91808e7c75dad2017c781635e20000', now(), null, null);
INSERT INTO cmdb_ci_icon VALUES (15, 'image_winnt', 'png', false, '2c91808e7c75dad2017c781635e20000', now(), null, null);