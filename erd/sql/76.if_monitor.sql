/**
  IF 모니터
 */
DROP TABLE IF EXISTS if_monitor cascade;

CREATE TABLE if_monitor
(
    document_no varchar(128) NOT NULL,
    check_flag boolean DEFAULT 'false'
);

COMMENT ON TABLE if_monitor IS 'IF 모니터';
COMMENT ON COLUMN if_monitor.document_no IS '문서번호';
COMMNET ON COLUMN if_monitor.check_flag IS '처리여부';
