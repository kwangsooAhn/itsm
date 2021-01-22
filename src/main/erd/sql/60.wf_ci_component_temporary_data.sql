/**
 * CI 컴포넌트 - CI 세부 속성 임시 테이블
 */
DROP TABLE IF EXISTS wf_ci_component_temporary_data;

CREATE TABLE wf_ci_component_temporary_data
(
    component_id varchar(128) NOT NULL,
    ci_id character varying(128) NOT NULL,
    values text,
    CONSTRAINT wf_ci_component_temporary_data_pk PRIMARY KEY (component_id, ci_id)
);

COMMENT ON TABLE wf_ci_component_temporary_data IS 'ci 컴포넌트 임시 데이터';
COMMENT ON COLUMN wf_ci_component_temporary_data.component_id IS '컴포넌트아이디';
COMMENT ON COLUMN wf_ci_component_temporary_data.ci_id IS 'CI아이디';
COMMENT ON COLUMN wf_ci_component_temporary_data.values IS '세부속성 데이터';