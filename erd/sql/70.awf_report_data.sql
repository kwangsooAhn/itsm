/**
 * 보고서 조회 데이터 테이블
 */
DROP TABLE IF EXISTS awf_report_data cascade;

create table awf_report_data (
    data_id varchar(128) NOT NULL,
    report_id varchar(128) NOT NULL,
    chart_id varchar(128) NOT NULL,
    display_order int,
    values text,
    CONSTRAINT awf_report_data_pk PRIMARY KEY (data_id)
);

COMMENT ON TABLE awf_report_data IS '보고서 데이터 정보';
COMMENT ON COLUMN awf_report_data.data_id IS '데이터아이디';
COMMENT ON COLUMN awf_report_data.report_id IS '보고서아이디';
COMMENT ON COLUMN awf_report_data.chart_id IS '차트아이디';
COMMENT ON COLUMN awf_report_data.display_order IS '차트순서';
COMMENT ON COLUMN awf_report_data.values IS '데이터';
