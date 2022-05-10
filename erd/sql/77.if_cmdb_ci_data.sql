/**
  IF CMDB 데이터 테이블
 */
DROP TABLE IF EXISTS if_cmdb_ci_data cascade;

CREATE TABLE if_cmdb_ci_data
(
    ci_id character varying(128) NOT NULL,
    attribute_id character varying(128) NOT NULL,
    value text,
    CONSTRAINT if_cmdb_ci_data_pk PRIMARY KEY (ci_id, attribute_id),
    CONSTRAINT if_cmdb_ci_data_fk1 FOREIGN KEY (ci_id)
        REFERENCES if_cmdb_ci (ci_id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT if_cmdb_ci_data_fk2 FOREIGN KEY (attribute_id)
        REFERENCES cmdb_attribute (attribute_id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
);

COMMENT ON TABLE if_cmdb_ci_data IS 'CMDB CI IF 데이터';
COMMENT ON COLUMN if_cmdb_ci_data.ci_id IS 'CI아이디';
COMMENT ON COLUMN if_cmdb_ci_data.attribute_id IS '속성아이디';
COMMENT ON COLUMN if_cmdb_ci_data.value IS '속성값';
