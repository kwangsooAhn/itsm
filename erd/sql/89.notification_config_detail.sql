/**
 * 알람 발송관리 설정
 */

DROP TABLE IF EXISTS notification_config_detail cascade;

CREATE TABLE notification_config_detail
(
    notification_code   varchar(128) NOT NULL,
    channel             varchar(128) NOT NULL,
    use_yn              boolean NOT NULL,
    title_format        varchar(512) NOT NULL,
    message_format      text NOT NULL,
    template            varchar(128),
    url                 varchar(256),
    create_user_key     varchar(128),
    create_dt           timestamp,
    update_user_key     varchar(128),
    update_dt           timestamp,

    CONSTRAINT notification_config_detail_pk PRIMARY KEY (notification_code, channel),
    CONSTRAINT notification_config_detail_fk FOREIGN KEY (notification_code) REFERENCES notification_config (notification_code)
);

COMMENT ON TABLE notification_config_detail IS 'notification_config_detail';
COMMENT ON COLUMN notification_config_detail.notification_code IS '알람 발송관리 코드';
COMMENT ON COLUMN notification_config_detail.channel IS '발송 채널';
COMMENT ON COLUMN notification_config_detail.use_yn IS '사용 여부';
COMMENT ON COLUMN notification_config_detail.title_format IS '제목 양식';
COMMENT ON COLUMN notification_config_detail.message_format IS '메세지 양식';
COMMENT ON COLUMN notification_config_detail.template IS '템플릿 파일명';
COMMENT ON COLUMN notification_config_detail.url IS '링크 URL';
COMMENT ON COLUMN notification_config_detail.create_user_key IS '생성자';
COMMENT ON COLUMN notification_config_detail.create_dt IS '생성 일시';
COMMENT ON COLUMN notification_config_detail.update_user_key IS '수정자';
COMMENT ON COLUMN notification_config_detail.update_dt IS '수정 일시';

INSERT INTO notification_config_detail VALUES('document', 'toast', true, '$[doc_type] $[doc_no]', '$[doc_step]', null, null, '0509e09412534a6e98f04ca79abb6424',now(),null,null);
INSERT INTO notification_config_detail VALUES('document', 'sms', true, '[ITSM] $[doc_type] $[doc_no] 처리안내', '$[doc_no] 처리바랍니다', null, null, '0509e09412534a6e98f04ca79abb6424',now(),null,null);
INSERT INTO notification_config_detail VALUES('document', 'mail', true, '[ITSM] $[doc_type] $[doc_no] 처리안내', '$[doc_no] 처리바랍니다', 'document_mail_template.html', null, '0509e09412534a6e98f04ca79abb6424',now(),null,null);
INSERT INTO notification_config_detail VALUES('cmdbLicense', 'toast', true, '$[ci_name] $[doc_no]', '$[ci_name] $[monitoring_field]가 $[due_date]', null, null, '0509e09412534a6e98f04ca79abb6424',now(),null,null);
INSERT INTO notification_config_detail VALUES('cmdbLicense', 'sms', true, '[ITSM]  $[ci_no] $[ci_name] 만료 안내', '$[ci_no] $[ci_name]가 $[due_date]', null, null, '0509e09412534a6e98f04ca79abb6424',now(),null,null);
INSERT INTO notification_config_detail VALUES('cmdbLicense', 'mail', true, '[ITSM]  $[ci_no] $[ci_name] 만료 안내', '$[ci_no] $[ci_name]가 $[due_date]', 'cmdb_mail_template.html', null, '0509e09412534a6e98f04ca79abb6424',now(),null,null);
