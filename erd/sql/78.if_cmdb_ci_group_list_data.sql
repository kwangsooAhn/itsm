/**
  IF CMDB 그룹 데이터 테이블
 */
DROP TABLE IF EXISTS if_cmdb_ci_group_list_data cascade;

CREATE TABLE if_cmdb_ci_group_list_data
(
    ci_id character varying(128) NOT NULL,
    attribute_id character varying(128) NOT NULL,
    c_attribute_id character varying(128) NOT NULL,
    c_attribute_seq int NOT NULL,
    c_value text,
    CONSTRAINT if_cmdb_ci_group_list_data_pk PRIMARY KEY (ci_id, attribute_id, c_attribute_id, c_attribute_seq),
    CONSTRAINT if_cmdb_ci_group_list_data_fk1 FOREIGN KEY (ci_id)
        REFERENCES if_cmdb_ci (ci_id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT if_cmdb_ci_group_list_data_fk2 FOREIGN KEY (attribute_id)
        REFERENCES cmdb_attribute (attribute_id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
);

COMMENT ON TABLE if_cmdb_ci_group_list_data IS 'CMDB CI IF 그룹 리스트 데이터';
COMMENT ON COLUMN if_cmdb_ci_group_list_data.ci_id IS 'CI아이디';
COMMENT ON COLUMN if_cmdb_ci_group_list_data.attribute_id IS '속성아이디';
COMMENT ON COLUMN if_cmdb_ci_group_list_data.c_attribute_id IS '자식속성아이디';
COMMENT ON COLUMN if_cmdb_ci_group_list_data.c_attribute_seq IS '자식속성순서';
COMMENT ON COLUMN if_cmdb_ci_group_list_data.c_value IS '자식속성값';
