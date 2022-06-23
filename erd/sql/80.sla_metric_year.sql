/**
  SLA 연도별 지표
 */
DROP TABLE IF EXISTS sla_metric_year cascade;

CREATE TABLE sla_metric_year
(
    metric_id varchar(128) NOT NULL,
    metric_year varchar(128) NOT NULL,
    min_value decimal,
    max_value decimal,
    weight_value decimal,
    owner varchar(100),
    comment text,
    zql_string text,
    create_user_key varchar(128),
    create_dt timestamp,
    update_user_key varchar(128),
    update_dt timestamp,
    CONSTRAINT sla_metric_year_pk PRIMARY KEY (metric_id, metric_year),
    CONSTRAINT sla_metric_year_fk FOREIGN KEY (metric_id) REFERENCES sla_metric (metric_id)
);

COMMENT ON TABLE sla_metric_year IS 'SLA 연도별 지표';
COMMENT ON COLUMN sla_metric_year.metric_id IS '지표아이디';
COMMENT ON COLUMN sla_metric_year.metric_year IS '지표관리년도';
COMMENT ON COLUMN sla_metric_year.min_value IS '최소치';
COMMENT ON COLUMN sla_metric_year.max_value IS '목표치';
COMMENT ON COLUMN sla_metric_year.weight_value IS '가중치';
COMMENT ON COLUMN sla_metric_year.owner IS '담당자';
COMMENT ON COLUMN sla_metric_year.comment IS '비고';
COMMENT ON COLUMN sla_metric_year.zql_string IS 'zql';
COMMENT ON COLUMN sla_metric_year.create_user_key IS '등록자';
COMMENT ON COLUMN sla_metric_year.create_dt IS '등록일';
COMMENT ON COLUMN sla_metric_year.update_user_key IS '수정자';
COMMENT ON COLUMN sla_metric_year.update_dt IS '수정일';
