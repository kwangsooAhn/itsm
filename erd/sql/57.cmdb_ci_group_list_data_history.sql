/**
 * CMDB CI 그룹 리스트 속성데이터 이력
 */
DROP TABLE IF EXISTS cmdb_ci_group_list_data_history cascade;

CREATE TABLE cmdb_ci_group_list_data_history
(
    data_history_id character varying(128) NOT NULL,
    seq int NOT NULL,
    ci_id character varying(128) NOT NULL,
    attribute_id character varying(128) NOT NULL,
    c_attribute_id character varying(128) NOT NULL,
    c_attribute_seq int NOT NULL,
    c_attribute_name character varying(128),
    c_attribute_desc character varying(512),
    c_attribute_type character varying(100),
    c_attribute_text character varying(128),
    c_attribute_value text,
    c_value text,
    CONSTRAINT cmdb_ci_group_list_data_history_pk PRIMARY KEY (data_history_id),
    CONSTRAINT cmdb_ci_group_list_data_history_pk UNIQUE (data_history_id)
);

COMMENT ON TABLE cmdb_ci_group_list_data_history IS 'CMDB CI 그룹 리스트 속성데이터 이력';
COMMENT ON COLUMN cmdb_ci_group_list_data_history.data_history_id IS '데이터히스토리아이디';
COMMENT ON COLUMN cmdb_ci_group_list_data_history.seq IS '이력시퀀스';
COMMENT ON COLUMN cmdb_ci_group_list_data_history.ci_id IS 'CI아이디';
COMMENT ON COLUMN cmdb_ci_group_list_data_history.attribute_id IS '속성아이디';
COMMENT ON COLUMN cmdb_ci_group_list_data_history.c_attribute_id IS '자식속성아이디';
COMMENT ON COLUMN cmdb_ci_group_list_data_history.c_attribute_seq IS '자식속성순서';
COMMENT ON COLUMN cmdb_ci_group_list_data_history.c_attribute_name IS '자식속성이름';
COMMENT ON COLUMN cmdb_ci_group_list_data_history.c_attribute_desc IS '자식속성설명';
COMMENT ON COLUMN cmdb_ci_group_list_data_history.c_attribute_type IS '자식속성타입';
COMMENT ON COLUMN cmdb_ci_group_list_data_history.c_attribute_text IS '자식속성라벨';
COMMENT ON COLUMN cmdb_ci_group_list_data_history.c_attribute_value IS '자식세부속성';
COMMENT ON COLUMN cmdb_ci_group_list_data_history.c_value IS '자식속성값';
