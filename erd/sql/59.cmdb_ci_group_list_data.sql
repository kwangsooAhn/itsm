/**
 * CMDB CI 그룹 리스트 데이터
 */
DROP TABLE IF EXISTS cmdb_ci_group_list_data cascade;

CREATE TABLE cmdb_ci_group_list_data
(
    ci_id character varying(128) NOT NULL,
    attribute_id character varying(128) NOT NULL,
    c_attribute_id character varying(128) NOT NULL,
    c_attribute_seq int NOT NULL,
    c_value text,
    CONSTRAINT cmdb_ci_group_list_data_pk PRIMARY KEY (ci_id, attribute_id, c_attribute_id, c_attribute_seq),
    CONSTRAINT cmdb_ci_group_list_data_fk1 FOREIGN KEY (ci_id)
        REFERENCES cmdb_ci (ci_id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT cmdb_ci_group_list_data_fk2 FOREIGN KEY (attribute_id)
        REFERENCES cmdb_attribute (attribute_id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
);

COMMENT ON TABLE cmdb_ci_group_list_data IS 'CMDB CI 그룹 리스트 데이터';
COMMENT ON COLUMN cmdb_ci_group_list_data.ci_id IS 'CI아이디';
COMMENT ON COLUMN cmdb_ci_group_list_data.attribute_id IS '속성아이디';
COMMENT ON COLUMN cmdb_ci_group_list_data.c_attribute_id IS '자식속성아이디';
COMMENT ON COLUMN cmdb_ci_group_list_data.c_attribute_seq IS '자식속성순서';
COMMENT ON COLUMN cmdb_ci_group_list_data.c_value IS '자식속성값';
