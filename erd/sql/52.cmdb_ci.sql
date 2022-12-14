/**
 * CMDB CI 정보
 */
DROP TABLE IF EXISTS cmdb_ci cascade;

CREATE TABLE cmdb_ci
(
	ci_id character varying(128) NOT NULL,
	ci_no character varying(128),
	ci_name character varying(128) NOT NULL,
	ci_status character varying(100) NOT NULL,
	type_id character varying(128) NOT NULL,
	ci_desc character varying(512),
    interlink boolean DEFAULT 'false',
	instance_id character varying(128),
	create_user_key character varying(128),
	create_dt timestamp,
	update_user_key character varying(128),
	update_dt timestamp,
	mapping_id character varying(128),
	CONSTRAINT cmdb_ci_pk PRIMARY KEY (ci_id),
	CONSTRAINT cmdb_ci_uk UNIQUE (ci_id),
	CONSTRAINT cmdb_ci_fk1 FOREIGN KEY (type_id)
      REFERENCES cmdb_type (type_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT cmdb_ci_fk2 FOREIGN KEY (instance_id)
        REFERENCES wf_instance (instance_id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
);

COMMENT ON TABLE cmdb_ci IS 'CMDB CI 정보';
COMMENT ON COLUMN cmdb_ci.ci_id IS 'CI아이디';
COMMENT ON COLUMN cmdb_ci.ci_no IS '시퀀스';
COMMENT ON COLUMN cmdb_ci.ci_name IS 'CI이름';
COMMENT ON COLUMN cmdb_ci.ci_status IS 'CI상태';
COMMENT ON COLUMN cmdb_ci.type_id IS '타입아이디';
COMMENT ON COLUMN cmdb_ci.ci_desc IS 'CI설명';
COMMENT ON COLUMN cmdb_ci.interlink IS '연동 여부';
COMMENT ON COLUMN cmdb_ci.instance_id IS '인스턴스ID';
COMMENT ON COLUMN cmdb_ci.create_user_key IS '등록자';
COMMENT ON COLUMN cmdb_ci.create_dt IS '등록일시';
COMMENT ON COLUMN cmdb_ci.update_user_key IS '수정자';
COMMENT ON COLUMN cmdb_ci.update_dt IS '수정일시';
COMMENT ON COLUMN cmdb_ci.mapping_id IS '매핑아이디';
