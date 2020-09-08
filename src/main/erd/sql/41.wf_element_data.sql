/**
 * 엘리먼트세부설정
 */
DROP TABLE IF EXISTS wf_element_data cascade;

CREATE TABLE wf_element_data
(
	element_id varchar(256) NOT NULL,
	attribute_id varchar(100) NOT NULL,
	attribute_value varchar(512) NOT NULL,
	attribute_order int,
	attribute_required boolean DEFAULT 'false' NOT NULL,
	CONSTRAINT wf_element_data_pk PRIMARY KEY (element_id, attribute_id, attribute_value),
	CONSTRAINT wf_element_data_fk FOREIGN KEY (element_id) REFERENCES wf_element (element_id)
);

COMMENT ON TABLE wf_element_data IS '엘리먼트세부설정';
COMMENT ON COLUMN wf_element_data.element_id IS '엘리먼트아이디';
COMMENT ON COLUMN wf_element_data.attribute_id IS '속성아이디';
COMMENT ON COLUMN wf_element_data.attribute_value IS '속성값';
COMMENT ON COLUMN wf_element_data.attribute_order IS '속성순서';
COMMENT ON COLUMN wf_element_data.attribute_required IS '속성필수값';