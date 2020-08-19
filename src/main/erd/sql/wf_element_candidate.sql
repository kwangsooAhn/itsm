/**
 * 엘리먼트담당후보자
 */
DROP TABLE IF EXISTS wf_element_candidate cascade;

CREATE TABLE wf_element_candidate
(
    element_id varchar(128) NOT NULL,
    candidate_type varchar(100) NOT NULL,
    candidate_value varchar(512) NOT NULL,
    CONSTRAINT wf_element_candidate_pk PRIMARY KEY (element_id, candidate_type, candidate_value),
    CONSTRAINT wf_element_candidate_fk FOREIGN KEY (element_id) REFERENCES wf_element (element_id)
);

COMMENT ON TABLE wf_element_candidate IS '엘리먼트담당후보자';
COMMENT ON COLUMN wf_element_candidate.element_id IS '엘리먼트아이디';
COMMENT ON COLUMN wf_element_candidate.candidate_type IS '엘리먼트담당후보자타입';
COMMENT ON COLUMN wf_element_candidate.candidate_value IS '엘리먼트후보자';
