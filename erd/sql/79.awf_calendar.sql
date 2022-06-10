/**
 * 캘린더
 */
DROP TABLE IF EXISTS awf_calendar cascade;

CREATE TABLE awf_calendar
(
    calendar_id   varchar(128) NOT NULL,
    calendar_type varchar(100) NOT NULL,
    CONSTRAINT awf_calendar_pk PRIMARY KEY (calendar_id)
);

COMMENT ON TABLE awf_calendar IS '캘린더';
COMMENT ON COLUMN awf_calendar.calendar_id IS '캘린더아이디';
COMMENT ON COLUMN awf_calendar.calendar_type IS '캘린더구분';

--기본 데이터
insert into awf_calendar values ('2b2380667b0c3133026d0de8df480001', 'user');
insert into awf_calendar values ('2c1120637b0d4123026d0de8df480005', 'user');
insert into awf_calendar values ('1a2380167a0c3161026d0de7df780203', 'user');
insert into awf_calendar values ('2c2183663b0c3133228d3ce8cf580015', 'user');
insert into awf_calendar values ('4a2388567c7b2113121d0de8bf110002', 'user');
insert into awf_calendar values ('3b2380627b1c3133625d1de9af233001', 'user');
insert into awf_calendar values ('6d2381637b0d1233322d0fe8df471009', 'user');
insert into awf_calendar values ('9c1320817c0d3112616d1df8df480002', 'document');
