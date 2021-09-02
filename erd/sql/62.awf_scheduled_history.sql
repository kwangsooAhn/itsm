/**
 * 차트설정
 */
DROP TABLE IF EXISTS awf_scheduled_history cascade;

CREATE TABLE awf_scheduled_history
(
    history_seq bigint NOT NULL,
    task_id varchar(128) NOT NULL,
    immediately_execute boolean default false,
    execute_time timestamp,
    result boolean,
    error_message text,
    CONSTRAINT awf_scheduled_history_pk PRIMARY KEY (history_seq)
);

COMMENT ON TABLE awf_scheduled_history IS '스케줄이력정보';
COMMENT ON COLUMN awf_scheduled_history.history_seq IS '이력시퀀스';
COMMENT ON COLUMN awf_scheduled_history.task_id IS '작업아이디';
COMMENT ON COLUMN awf_scheduled_history.immediately_execute IS '즉시실행여부';
COMMENT ON COLUMN awf_scheduled_history.execute_time IS '실행시각';
COMMENT ON COLUMN awf_scheduled_history.result IS '결과';
COMMENT ON COLUMN awf_scheduled_history.error_message IS '에러메시지';
