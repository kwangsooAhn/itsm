/**
 * 캘린더 반복일정 커스텀 상세정보
 */
DROP TABLE IF EXISTS awf_calendar_repeat_custom_data cascade;

CREATE TABLE awf_calendar_repeat_custom_data
(
    custom_id         varchar(128) NOT NULL,
    custom_type       varchar(64),
    data_id           varchar(128) NOT NULL,
    data_index        int,
    schedule_title    varchar(200),
    schedule_contents text,
    all_day_yn        boolean,
    start_dt          timestamp,
    end_dt            timestamp,
    CONSTRAINT awf_calendar_repeat_custom_data_pk PRIMARY KEY (custom_id),
    CONSTRAINT awf_calendar_repeat_custom_data_fk FOREIGN KEY (data_id) REFERENCES awf_calendar_repeat_data (data_id)
);

COMMENT ON TABLE awf_calendar_repeat_custom_data IS '캘린더 반복일정 커스텀';
COMMENT ON COLUMN awf_calendar_repeat_custom_data.custom_id IS '커스텀아이디';
COMMENT ON COLUMN awf_calendar_repeat_custom_data.custom_type IS '커스텀타입';
COMMENT ON COLUMN awf_calendar_repeat_custom_data.data_id IS '데이터아이디';
COMMENT ON COLUMN awf_calendar_repeat_custom_data.data_index IS '데이터인덱스';
COMMENT ON COLUMN awf_calendar_repeat_custom_data.schedule_title IS '제목';
COMMENT ON COLUMN awf_calendar_repeat_custom_data.schedule_contents IS '내용';
COMMENT ON COLUMN awf_calendar_repeat_custom_data.all_day_yn IS '종일여부';
COMMENT ON COLUMN awf_calendar_repeat_custom_data.start_dt IS '시작일';
COMMENT ON COLUMN awf_calendar_repeat_custom_data.end_dt IS '종료일';
