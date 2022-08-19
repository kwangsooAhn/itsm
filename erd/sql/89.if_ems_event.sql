/**
 * IF EMS 연동 - 이벤트
 */
DROP TABLE IF EXISTS if_ems_event cascade;

CREATE TABLE if_ems_event
(
    z_myid bigint NOT NULL,
    z_evttime timestamp NULL,
    z_infraid int NOT NULL,
    z_infraName varchar(32) NULL,
    z_itname varchar(64) NULL,
    z_myhost varchar(255) NULL,
    z_myname varchar(255) NULL,
    z_mymsg varchar(512) NULL,
    z_alert int NOT NULL,
    z_status int NULL DEFAULT 0,
    CONSTRAINT if_ems_event_pk PRIMARY KEY (z_myid)
);

COMMENT ON TABLE if_ems_event IS 'EMS 이벤트 정보';
COMMENT ON COLUMN if_ems_event.z_myid IS '이벤트아이디';
COMMENT ON COLUMN if_ems_event.z_evttime IS '이벤트발생일시';
COMMENT ON COLUMN if_ems_event.z_infraid IS '인프라아이디';
COMMENT ON COLUMN if_ems_event.z_infraName IS '인프라명';
COMMENT ON COLUMN if_ems_event.z_itname IS '이벤트항목명';
COMMENT ON COLUMN if_ems_event.z_myhost IS '이벤트호스트명';
COMMENT ON COLUMN if_ems_event.z_myname IS '이벤트명';
COMMENT ON COLUMN if_ems_event.z_mymsg IS '이벤트메시지';
COMMENT ON COLUMN if_ems_event.z_alert IS '심각도';
COMMENT ON COLUMN if_ems_event.z_status IS '상태';
