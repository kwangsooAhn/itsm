/**
 * 문서양식 그룹정보
 */
DROP TABLE IF EXISTS wf_form_group cascade;

CREATE TABLE wf_form_group
(
    form_group_id varchar(128) NULL,
    form_group_name varchar(256) NULL,
    form_id varchar(128) NULL,
    CONSTRAINT wf_form_group_pk PRIMARY KEY (form_group_id),
    CONSTRAINT wf_form_group_fk FOREIGN KEY (form_id) REFERENCES wf_form (form_id)
);

COMMENT ON TABLE wf_form_group IS '문서양식 그룹정보';
COMMENT ON COLUMN wf_form_group.form_group_id IS '문서양식 그룹아이디';
COMMENT ON COLUMN wf_form_group.form_group_name IS '문서양식 그룹이름';
COMMENT ON COLUMN wf_form_group.form_id IS '문서양식아이디';