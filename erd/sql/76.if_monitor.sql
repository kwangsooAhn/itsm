/**
  IF 모니터
 */
DROP TABLE IF EXISTS if_monitor cascade;

CREATE TABLE if_monitor
(
    document_no varchar(128) NOT NULL
);

COMMENT ON TABLE if_monitor IS 'IF 모니터';
COMMENT ON COLUMN if_monitor.document_no IS '문서번호';
