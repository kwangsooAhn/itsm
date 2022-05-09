/**
  SLA 지표 그룹
 */
DROP TABLE IF EXISTS sla_metric_group cascade;

CREATE TABLE sla_metric_group
(
    metric_group_id varchar(128) NOT NULL,
    metric_group_name varchar(100) NOT NULL,
    create_user_key varchar(128),
    create_dt timestamp,
    CONSTRAINT sla_metric_group_pk PRIMARY KEY (metric_group_id),
    CONSTRAINT sla_metric_group_uk UNIQUE (metric_group_name)
);

COMMENT ON TABLE sla_metric_group IS 'SLA 지표 그룹';
COMMENT ON COLUMN sla_metric_group.metric_group_id IS '지표그룹아이디';
COMMENT ON COLUMN sla_metric_group.metric_group_name IS '지표그룹이름';
COMMENT ON COLUMN sla_metric_group.create_user_key IS '등록자';
COMMENT ON COLUMN sla_metric_group.create_dt IS '등록일';
