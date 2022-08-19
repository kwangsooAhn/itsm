/**
 * 알람 발송 정보
 */

DROP TABLE IF EXISTS notification_data cascade;

CREATE TABLE notification_data
(
    notification_id   varchar(128) NOT NULL,
    receiver_user_key varchar(128) NOT NULL,
    title             varchar(512) NOT NULL,
    message           text NOT NULL,
    send_dt           timestamp NOT NULL,
    channel           varchar(128) NOT NULL,
    display_dt        timestamp,
    confirm_dt        timestamp,
    list_yn           boolean,
    url               varchar(512),
    CONSTRAINT notification_data_pk PRIMARY KEY (notification_id)
);

COMMENT ON TABLE notification_data IS '알람 발송 정보';
COMMENT ON COLUMN notification_data.notification_id IS '알람 발송 아이디';
COMMENT ON COLUMN notification_data.receiver_user_key IS '수신자';
COMMENT ON COLUMN notification_data.title IS '제목';
COMMENT ON COLUMN notification_data.message IS '메세지';
COMMENT ON COLUMN notification_data.send_dt IS '발송일시';
COMMENT ON COLUMN notification_data.channel IS '발송채널';
COMMENT ON COLUMN notification_data.display_dt IS '디스플레이 일시';
COMMENT ON COLUMN notification_data.confirm_dt IS '확인 일시';
COMMENT ON COLUMN notification_data.list_yn IS '리스트 대상 유무';
COMMENT ON COLUMN notification_data.url IS '링크 URL';

