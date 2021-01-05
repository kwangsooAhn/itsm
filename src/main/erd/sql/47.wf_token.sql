/**
 * 토큰정보
 */
DROP TABLE IF EXISTS wf_token cascade;

CREATE TABLE wf_token
(
	token_id varchar(128) NOT NULL,
	instance_id varchar(128) NOT NULL,
	element_id varchar(256) NOT NULL,
	token_start_dt timestamp,
	token_end_dt timestamp,
	token_status varchar(100) NOT NULL,
	assignee_id varchar(128),
	CONSTRAINT wf_token_pk PRIMARY KEY (token_id),
	CONSTRAINT wf_token_fk1 FOREIGN KEY (instance_id) REFERENCES wf_instance (instance_id),
	CONSTRAINT wf_token_fk2 FOREIGN KEY (element_id) REFERENCES wf_element (element_id)
);

COMMENT ON TABLE wf_token IS '토큰정보';
COMMENT ON COLUMN wf_token.token_id IS '토큰아이디';
COMMENT ON COLUMN wf_token.instance_id IS '인스턴스아이디';
COMMENT ON COLUMN wf_token.element_id IS '엘리먼트아이디';
COMMENT ON COLUMN wf_token.token_start_dt IS '토큰시작일시';
COMMENT ON COLUMN wf_token.token_end_dt IS '토큰종료일시';
COMMENT ON COLUMN wf_token.token_status IS '토큰상태';
COMMENT ON COLUMN wf_token.assignee_id IS '담당자아이디';