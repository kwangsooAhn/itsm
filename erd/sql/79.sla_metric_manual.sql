/**
  SLA 수동 지표
 */
DROP TABLE IF EXISTS sla_metric_manual cascade;

CREATE TABLE sla_metric_manual
(
    metric_id varchar(128) NOT NULL,
    reference_dt timestamp NOT NULL,
    metric_value integer NOT NULL,
    create_user_key varchar(128),
    create_dt timestamp,
    CONSTRAINT sla_metric_manual_pk PRIMARY KEY (metric_id, reference_dt, metric_value),
    CONSTRAINT sla_metric_manual_fk FOREIGN KEY (metric_id) REFERENCES sla_metric (metric_id)
);

COMMENT ON TABLE sla_metric_manual IS 'SLA 수동 지표';
COMMENT ON COLUMN sla_metric_manual.metric_id IS '지표아이디';
COMMENT ON COLUMN sla_metric_manual.reference_dt IS '기준일자';
COMMENT ON COLUMN sla_metric_manual.metric_value IS '지표값';
COMMENT ON COLUMN sla_metric_manual.create_user_key IS '등록자';
COMMENT ON COLUMN sla_metric_manual.create_dt IS '등록일';
