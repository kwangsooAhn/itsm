/**
 * 토큰담당후보자
 */
DROP TABLE IF EXISTS wf_candidate cascade;

CREATE TABLE wf_candidate
(
	token_id varchar(128) NOT NULL,
	candidate_type varchar(100) NOT NULL,
	candidate_value varchar(512) NOT NULL,
	CONSTRAINT wf_candidate_pk PRIMARY KEY (token_id, candidate_type, candidate_value),
	CONSTRAINT wf_candidate_fk FOREIGN KEY (token_id) REFERENCES wf_token (token_id)
);

COMMENT ON TABLE wf_candidate IS '토큰담당후보자';
COMMENT ON COLUMN wf_candidate.token_id IS '토큰아이디';
COMMENT ON COLUMN wf_candidate.candidate_type IS '토큰담당후보자타입';
COMMENT ON COLUMN wf_candidate.candidate_value IS '토큰후보자';