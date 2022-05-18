/**
 * 캘린더 스케줄
 */
DROP TABLE IF EXISTS awf_calendar_schedule cascade;

CREATE TABLE awf_calendar_schedule
(
    schedule_id       varchar(128) NOT NULL,
    calendar_id       varchar(128) NOT NULL,
    schedule_title    varchar(200),
    schedule_contents text,
    all_day_un        boolean,
    start_dt          timestamp,
    end_dt            timestamp,
    CONSTRAINT awf_calendar_schedule_pk PRIMARY KEY (schedule_id),
    CONSTRAINT awf_calendar_schedule_fk FOREIGN KEY (calendar_id) REFERENCES awf_calendar (calendar_id)
);

COMMENT ON TABLE awf_calendar_schedule IS '사용자정의코드';
COMMENT ON COLUMN awf_calendar_schedule.schedule_id IS '스케줄아이디';
COMMENT ON COLUMN awf_calendar_schedule.calendar_id IS '캘린더아이디';
COMMENT ON COLUMN awf_calendar_schedule.schedule_title IS '제목';
COMMENT ON COLUMN awf_calendar_schedule.schedule_contents IS '내용';
COMMENT ON COLUMN awf_calendar_schedule.all_day_un IS '종일여부';
COMMENT ON COLUMN awf_calendar_schedule.start_dt IS '시작일';
COMMENT ON COLUMN awf_calendar_schedule.end_dt IS '종료일';
