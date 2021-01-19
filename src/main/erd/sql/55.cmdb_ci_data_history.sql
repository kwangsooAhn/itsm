/**
 * CMDB CI 속성데이터 이력
 */
DROP TABLE IF EXISTS cmdb_ci_data_history cascade;

CREATE TABLE cmdb_ci_data_history
(
	data_history_id character varying(128) NOT NULL,
	ci_id character varying(128) NOT NULL,
	seq int NOT NULL,
	attribute_id character varying(128) NOT NULL,
	attribute_name character varying(100),
	attribute_desc character varying(500),
	attribute_type character varying(100),
	attribute_text character varying(128),
	value text,
	CONSTRAINT cmdb_ci_data_history_pk PRIMARY KEY (attribute_id),
	CONSTRAINT cmdb_ci_data_history_uk UNIQUE (data_history_id)
);

COMMENT ON TABLE cmdb_ci_data_history IS 'CMDB CI 속성데이터 이력';
COMMENT ON COLUMN cmdb_ci_data_history.data_history_id IS '데이터히스토리아이디';
COMMENT ON COLUMN cmdb_ci_data_history.ci_id IS 'CI아이디';
COMMENT ON COLUMN cmdb_ci_data_history.seq IS '이력시퀀스';
COMMENT ON COLUMN cmdb_ci_data_history.attribute_id IS '속성아이디';
COMMENT ON COLUMN cmdb_ci_data_history.attribute_name IS '속성이름';
COMMENT ON COLUMN cmdb_ci_data_history.attribute_desc IS '속성설명';
COMMENT ON COLUMN cmdb_ci_data_history.attribute_type IS '속성타입';
COMMENT ON COLUMN cmdb_ci_data_history.attribute_text IS '속성라벨';
COMMENT ON COLUMN cmdb_ci_data_history.value IS '속성값';
