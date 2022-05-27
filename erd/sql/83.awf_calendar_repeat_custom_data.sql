/**
 * 캘린더 반복일정 커스텀 상세정보
 */
DROP TABLE IF EXISTS awf_calendar_repeat_custom_data cascade;

CREATE TABLE awf_calendar_repeat_custom_data
(
    data_id           varchar(128) NOT NULL,
    repeat_id         varchar(128) NOT NULL,
    repeat_start_dt   timestamp,
    repeat_end_dt     timestamp,
    repeat_type       varchar(64),
    repeat_value      varchar(64),
    schedule_title    varchar(200),
    schedule_contents text,
    all_day_yn        boolean,
    start_dt          timestamp,
    end_dt            timestamp,
    CONSTRAINT awf_calendar_repeat_custom_data_pk PRIMARY KEY (data_id),
    CONSTRAINT awf_calendar_repeat_custom_data_fk FOREIGN KEY (repeat_id) REFERENCES awf_calendar_repeat (repeat_id)
);

COMMENT ON TABLE awf_calendar_repeat_custom_data IS '캘린더 반복일정';
COMMENT ON COLUMN awf_calendar_repeat_custom_data.data_id IS '데이터아이디';
COMMENT ON COLUMN awf_calendar_repeat_custom_data.repeat_id IS '반복일정아이디';
COMMENT ON COLUMN awf_calendar_repeat_custom_data.repeat_start_dt IS '반복일정시작일';
COMMENT ON COLUMN awf_calendar_repeat_custom_data.repeat_end_dt IS '반복일정종료일';
COMMENT ON COLUMN awf_calendar_repeat_custom_data.repeat_type IS '반복일정 타입';
COMMENT ON COLUMN awf_calendar_repeat_custom_data.repeat_value IS '반복일정 설정 값';
COMMENT ON COLUMN awf_calendar_repeat_custom_data.schedule_title IS '제목';
COMMENT ON COLUMN awf_calendar_repeat_custom_data.schedule_contents IS '내용';
COMMENT ON COLUMN awf_calendar_repeat_custom_data.all_day_yn IS '종일여부';
COMMENT ON COLUMN awf_calendar_repeat_custom_data.start_dt IS '시작일';
COMMENT ON COLUMN awf_calendar_repeat_custom_data.end_dt IS '종료일';
