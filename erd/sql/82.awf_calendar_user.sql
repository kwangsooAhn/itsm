/**
 * 사용자 캘린더
 */
DROP TABLE IF EXISTS awf_calendar_user cascade;

CREATE TABLE awf_calendar_user
(
    calendar_id   varchar(128) NOT NULL,
    calendar_name varchar(100),
    owner         varchar(128),
    create_dt     timestamp,
    CONSTRAINT awf_calendar_user_pk PRIMARY KEY (calendar_id),
    CONSTRAINT awf_calendar_user_fk FOREIGN KEY (calendar_id) REFERENCES awf_calendar (calendar_id),
    CONSTRAINT awf_calendar_user_uk UNIQUE (owner, calendar_name)
);

COMMENT ON TABLE awf_calendar_user IS '사용자 캘린더';
COMMENT ON COLUMN awf_calendar_user.calendar_id IS '캘린더아이디';
COMMENT ON COLUMN awf_calendar_user.calendar_name IS '캘린더이름';
COMMENT ON COLUMN awf_calendar_user.owner IS '사용자';
COMMENT ON COLUMN awf_calendar_user.create_dt IS '등록일';

--기본 데이터
insert into awf_calendar_user values ('2b2380667b0c3133026d0de8df480001', '기본', '0509e09412534a6e98f04ca79abb6424', now());
insert into awf_calendar_user values ('2c1120637b0d4123026d0de8df480005', '기본', '4028b21c7c4df297017c4e595fd90000', now());
insert into awf_calendar_user values ('1a2380167a0c3161026d0de7df780203', '기본', '40288ad27c729b34017c729c2e370000', now());
insert into awf_calendar_user values ('2c2183663b0c3133228d3ce8cf580015', '기본', '40288ada7cfd3301017cfd3a78580000', now());
insert into awf_calendar_user values ('4a2388567c7b2113121d0de8bf110002', '기본', '2c9180867d0b3336017d0de8bf480001', now());
insert into awf_calendar_user values ('3b2380627b1c3133625d1de9af233001', '기본', '2c91808e7c75dad2017c781635e22000', now());
insert into awf_calendar_user values ('6d2381637b0d1233322d0fe8df471009', '기본', '2c91808e7c75dad2017c781635e20000', now());
