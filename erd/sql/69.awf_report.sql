/**
 * 보고서 조회 테이블
 */
DROP TABLE IF EXISTS awf_report cascade;

create table awf_report (
    report_id varchar(128) NOT NULL,
    template_id varchar(128) NOT NULL,
    report_name varchar(128),
    report_desc varchar(512),
    publish_dt timestamp,
    CONSTRAINT awf_report_pk PRIMARY KEY (report_id)
);

COMMENT ON TABLE awf_report IS '보고서 정보';
COMMENT ON COLUMN awf_report.report_id IS '보고서아이디';
COMMENT ON COLUMN awf_report.template_id IS '템플릿아이디';
COMMENT ON COLUMN awf_report.report_name IS '보고서명';
COMMENT ON COLUMN awf_report.report_desc IS '보고서설명';
COMMENT ON COLUMN awf_report.publish_dt IS '발행일시';
