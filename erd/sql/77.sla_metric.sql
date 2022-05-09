/**
  SLA 지표
 */
DROP TABLE IF EXISTS sla_metric cascade;

CREATE TABLE sla_metric
(
    metric_id varchar(128) NOT NULL,
    metric_name varchar(100),
    metric_desc text,
    metric_group_id varchar(128) NOT NULL,
    metric_type varchar(128),
    metric_unit varchar(128),
    calculation_type varchar(128),
    create_user_key varchar(128),
    create_dt timestamp ,
    update_user_key varchar(128),
    update_dt timestamp,
    CONSTRAINT sla_metric_pk PRIMARY KEY (metric_id),
    CONSTRAINT sla_metric_fk FOREIGN KEY (metric_group_id) REFERENCES sla_metric_group (metric_group_id)
);

COMMENT ON TABLE sla_metric IS 'SLA 지표';
COMMENT ON COLUMN sla_metric.metric_id IS '지표아이디';
COMMENT ON COLUMN sla_metric.metric_name IS '지표이름';
COMMENT ON COLUMN sla_metric.metric_desc IS '지표설명';
COMMENT ON COLUMN sla_metric.metric_group_id IS '지표그룹아이디';
COMMENT ON COLUMN sla_metric.metric_type IS '지표관리타입';
COMMENT ON COLUMN sla_metric.metric_unit IS '지표단위';
COMMENT ON COLUMN sla_metric.calculation_type IS '계산방식';
COMMENT ON COLUMN sla_metric.create_user_key IS '등록자';
COMMENT ON COLUMN sla_metric.create_dt IS '등록일';
COMMENT ON COLUMN sla_metric.update_user_key IS '수정자';
COMMENT ON COLUMN sla_metric.update_dt IS '수정일';
