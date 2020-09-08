/**
 * 토큰데이터정보
 */
DROP TABLE IF EXISTS wf_token_data cascade;

CREATE TABLE wf_token_data
(
	token_id varchar(128) NOT NULL,
	component_id varchar(128) NOT NULL,
	value text,
	CONSTRAINT wf_token_data_pk PRIMARY KEY (token_id, component_id),
	CONSTRAINT wf_token_data_fk1 FOREIGN KEY (token_id) REFERENCES wf_token (token_id),
	CONSTRAINT wf_token_data_fk2 FOREIGN KEY (component_id) REFERENCES wf_component (component_id)
);

COMMENT ON TABLE wf_token_data IS '토큰데이터정보';
COMMENT ON COLUMN wf_token_data.token_id IS '토큰아이디';
COMMENT ON COLUMN wf_token_data.component_id IS '컴포넌트아이디';
COMMENT ON COLUMN wf_token_data.value IS '데이터';