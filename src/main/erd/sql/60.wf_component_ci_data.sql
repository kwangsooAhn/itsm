/**
 * CI 컴포넌트 - CI 세부 속성 임시 테이블
 */
DROP TABLE IF EXISTS wf_component_ci_data cascade;

CREATE TABLE wf_component_ci_data
(
    component_id varchar(128) NOT NULL,
    ci_id varchar(128) NOT NULL,
    values text,
    instance_id varchar(128),
    CONSTRAINT wf_component_ci_data_pk PRIMARY KEY (component_id, ci_id)
);

COMMENT ON TABLE wf_component_ci_data IS 'CI 컴포넌트 CI 상세 데이터';
COMMENT ON COLUMN wf_component_ci_data.component_id IS '컴포넌트아이디';
COMMENT ON COLUMN wf_component_ci_data.ci_id IS 'CI아이디';
COMMENT ON COLUMN wf_component_ci_data.values IS '세부속성 데이터';
COMMENT ON COLUMN wf_component_ci_data.instance_id IS '인스턴스아이디';