CREATE TABLE wf_component_property (
    component_id varchar(128) NULL,
    property_type varchar(100) NULL,
    property_options text NULL,
    CONSTRAINT wf_component_property_pk PRIMARY KEY (component_id,property_type),
    CONSTRAINT wf_component_property_fk FOREIGN KEY (component_id) REFERENCES wf_component(component_id)
);

COMMENT ON TABLE wf_component_property IS '컴포넌트 세부속성';
COMMENT ON COLUMN wf_component_property.component_id IS '컴포넌트 아이디';
COMMENT ON COLUMN wf_component_property.property_type IS '속성 타입';
COMMENT ON COLUMN wf_component_property.property_options IS '속성 값';