/**
 * 컴포넌트 템플릿
 */
DROP TABLE IF EXISTS wf_component_template cascade;

CREATE TABLE wf_component_template
(
    template_id varchar(128) NOT NULL,
    template_name varchar(128) NOT NULL,
    component_type varchar(100) NOT NULL,
    component_data text NOT NULL,
    CONSTRAINT wf_component_template_pk PRIMARY KEY (template_id)
);

COMMENT ON TABLE wf_component_template IS '컴포넌트 템플릿';
COMMENT ON COLUMN wf_component_template.template_id IS '컴포넌트 템플릿 아이디';
COMMENT ON COLUMN wf_component_template.template_name IS '컴포넌트 템플릿 이름';
COMMENT ON COLUMN wf_component_template.component_type IS '컴포넌트 타입';
COMMENT ON COLUMN wf_component_template.component_data IS '컴포넌트 속성값';
