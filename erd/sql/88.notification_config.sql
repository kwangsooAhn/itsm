/**
 * 알람 발송관리 설정
 */

DROP TABLE IF EXISTS notification_config cascade;

CREATE TABLE notification_config
(
    notification_code varchar(128) NOT NULL,
    notification_name varchar(128) NOT NULL,
    CONSTRAINT notification_config_pk PRIMARY KEY (notification_code)
);

COMMENT ON TABLE notification_config IS '알람 발송관리 설정';
COMMENT ON COLUMN notification_config.notification_code IS '알람 발송관리 코드';
COMMENT ON COLUMN notification_config.notification_name IS '알람 발송관리 명';

INSERT INTO notification_config VALUES('document', '신청서');
INSERT INTO notification_config VALUES('cmdbLicense', 'CMDB');
