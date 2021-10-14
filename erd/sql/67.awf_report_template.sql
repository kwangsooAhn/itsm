/**
 * 보고서 설정 테이블 (템플릿)
 */
DROP TABLE IF EXISTS awf_report_template cascade;

create table awf_report_template (
    template_id varchar(128) NOT NULL,
    template_name varchar(128) NOT NULL,
    template_desc varchar(512),
    report_name varchar(128),
    automatic boolean DEFAULT false,
    create_user_key varchar(128),
    create_dt timestamp,
    update_user_key varchar(128),
    update_dt timestamp,
    CONSTRAINT awf_report_template_pk PRIMARY KEY (template_id)
);

COMMENT ON TABLE awf_report_template IS '보고서 템플릿 정보';
COMMENT ON COLUMN awf_report_template.template_id IS '템플릿아이디';
COMMENT ON COLUMN awf_report_template.template_name IS '템플릿명';
COMMENT ON COLUMN awf_report_template.template_desc IS '템플릿설명';
COMMENT ON COLUMN awf_report_template.report_name IS '보고서명';
COMMENT ON COLUMN awf_report_template.automatic IS '자동생성여부';
COMMENT ON COLUMN awf_report_template.create_user_key IS '생성자';
COMMENT ON COLUMN awf_report_template.create_dt IS '생성일시';
COMMENT ON COLUMN awf_report_template.update_user_key IS '수정자';
COMMENT ON COLUMN awf_report_template.update_dt IS '수정일시';
