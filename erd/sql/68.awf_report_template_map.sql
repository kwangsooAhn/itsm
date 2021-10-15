/**
 * 보고서 설정 차트 매핑 테이블
 */
DROP TABLE IF EXISTS awf_report_template_map cascade;

create table awf_report_template_map (
    template_id varchar(128) NOT NULL,
    chart_id varchar(128) NOT NULL,
    display_order int,
    CONSTRAINT awf_report_template_map_pk PRIMARY KEY (template_id, chart_id)
);

COMMENT ON TABLE awf_report_template_map IS '보고서 템플릿 차트 정보';
COMMENT ON COLUMN awf_report_template_map.template_id IS '템플릿아이디';
COMMENT ON COLUMN awf_report_template_map.chart_id IS '차트아이디';
