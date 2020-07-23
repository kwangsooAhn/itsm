/**
 * 
 */
DROP TABLE IF EXISTS wf_component_data cascade;

CREATE TABLE wf_component_data
(
	component_id varchar(128) NOT NULL,
	attribute_id varchar(100) NOT NULL,
	attribute_value varchar(512) NOT NULL,
	attribute_order int,
	CONSTRAINT wf_component_data_pk PRIMARY KEY (component_id, attribute_id),
	CONSTRAINT wf_component_data_fk FOREIGN KEY (component_id) REFERENCES wf_component (component_id)
);

COMMENT ON TABLE wf_component_data IS '컴포넌트세부설정';
COMMENT ON COLUMN wf_component_data.component_id IS '컴포넌트아이디';
COMMENT ON COLUMN wf_component_data.attribute_id IS '속성아이디';
COMMENT ON COLUMN wf_component_data.attribute_value IS '속성값';
COMMENT ON COLUMN wf_component_data.attribute_order IS '속성순서';