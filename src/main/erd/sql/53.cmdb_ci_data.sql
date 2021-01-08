/**
 * CMDB CI 데이터
 */
DROP TABLE IF EXISTS cmdb_ci_data cascade;

CREATE TABLE cmdb_ci_data
(
	ci_id character varying(128) NOT NULL,
	attribute_id character varying(128) NOT NULL,
	value text,
	CONSTRAINT cmdb_ci_data_pk PRIMARY KEY (ci_id, attribute_id),
	CONSTRAINT cmdb_ci_data_fk1 FOREIGN KEY (ci_id)
      REFERENCES cmdb_ci (ci_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT cmdb_ci_data_fk2 FOREIGN KEY (attribute_id)
      REFERENCES cmdb_attribute (attribute_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

COMMENT ON TABLE cmdb_ci_data IS 'CMDB CI 데이터';
COMMENT ON COLUMN cmdb_ci_data.ci_id IS 'CI아이디';
COMMENT ON COLUMN cmdb_ci_data.attribute_id IS '속성아이디';
COMMENT ON COLUMN cmdb_ci_data.value IS '속성값';
