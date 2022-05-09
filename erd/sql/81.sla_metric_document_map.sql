/**
  SLA 지표 - 문서 매핑
 */
DROP TABLE IF EXISTS sla_metric_document_map cascade;

CREATE TABLE sla_metric_document_map
(
    metric_id varchar(128) NOT NULL,
    document_id varchar(128) NOT NULL,
    CONSTRAINT sla_metric_document_map_pk PRIMARY KEY (metric_id, document_id),
    CONSTRAINT sla_metric_document_map_fk1 FOREIGN KEY (metric_id) REFERENCES sla_metric (metric_id),
    CONSTRAINT sla_metric_document_map_fk2 FOREIGN KEY (document_id) REFERENCES wf_document (document_id)
);

COMMENT ON TABLE sla_metric_document_map IS 'SLA 지표 문서 매핑';
COMMENT ON COLUMN sla_metric_document_map.class_id IS '지표아이디';
COMMENT ON COLUMN sla_metric_document_map.attribute_id IS '신청서아이디';
