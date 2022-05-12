/**
  SLA ZQL
 */
DROP TABLE IF EXISTS sla_zql cascade;

CREATE TABLE sla_zql
(
    metric_id varchar(128) NOT NULL,
    zql_string text,
    CONSTRAINT sla_zql_pk PRIMARY KEY (metric_id),
    CONSTRAINT sla_zql_fk FOREIGN KEY (metric_id) REFERENCES sla_metric (metric_id)
);

COMMENT ON TABLE sla_zql IS 'SLA ZQL';
COMMENT ON COLUMN sla_zql.metric_id IS '지표아이디';
COMMENT ON COLUMN sla_zql.zql_string IS 'zql';
