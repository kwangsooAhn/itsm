/**
 * 사용자 캘린더 반복일정
 */
DROP TABLE IF EXISTS awf_calendar_user_repeat cascade;

CREATE TABLE awf_calendar_user_repeat
(
    repeat_id   varchar(128) NOT NULL,
    calendar_id varchar(128) NOT NULL,
    CONSTRAINT awf_calendar_user_repeat_pk PRIMARY KEY (repeat_id),
    CONSTRAINT awf_calendar_user_repeat_fk FOREIGN KEY (calendar_id) REFERENCES awf_calendar (calendar_id)
);

COMMENT ON TABLE awf_calendar_user_repeat IS '사용자 캘린더 반복일정';
COMMENT ON COLUMN awf_calendar_user_repeat.repeat_id IS '반복일정아이디';
COMMENT ON COLUMN awf_calendar_user_repeat.calendar_id IS '캘린더아이디';
