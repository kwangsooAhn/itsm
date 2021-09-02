/**
 * 문서양식 ROW 정보
 */
DROP TABLE IF EXISTS wf_form_row cascade;

CREATE TABLE wf_form_row (
     form_row_id varchar(128) NULL,
     form_group_id varchar(128) NULL,
     row_display_option text NULL,
     CONSTRAINT wf_form_row_pk PRIMARY KEY (form_row_id),
     CONSTRAINT wf_form_row_fk FOREIGN KEY (form_group_id) REFERENCES wf_form_group(form_group_id)
);

COMMENT ON TABLE wf_form_row IS '문서양식 ROW 정보';
COMMENT ON COLUMN wf_form_row.form_row_id IS '문서양식 ROW 아이디';
COMMENT ON COLUMN wf_form_row.form_group_id IS '문서양식 그룹아이디';
COMMENT ON COLUMN wf_form_row.row_display_option IS 'ROW 출력용 속성';