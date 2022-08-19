/**
 * 알람 발송관리 설정
 */

DROP TABLE IF EXISTS notification_config_detail cascade;

CREATE TABLE notification_config_detail
(
    notification_code   varchar(128) NOT NULL,
    channel             varchar(128) NOT NULL,
    use_yn              boolean NOT NULL,
    config_detail       text,
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
COMMENT ON COLUMN notification_config_detail.config_detail IS '설정 정보';
COMMENT ON COLUMN notification_config_detail.create_user_key IS '생성자';
COMMENT ON COLUMN notification_config_detail.create_dt IS '생성 일시';
COMMENT ON COLUMN notification_config_detail.update_user_key IS '수정자';
COMMENT ON COLUMN notification_config_detail.update_dt IS '수정 일시';

insert into notification_config_detail values('document', 'Toast', true, '{"title": "$[doc_type] $[doc_no]","message": "$[doc_step]"}','0509e09412534a6e98f04ca79abb6424',now(),null,null);
insert into notification_config_detail values('document', 'SMS', true, '{"title": "$[doc_type] $[doc_no] 처리안내","message": "$[doc_no] 처리바랍니다"}','0509e09412534a6e98f04ca79abb6424',now(),null,null);
insert into notification_config_detail values('document', 'E-mail', true, '{"title": "$[doc_type] $[doc_no] 처리안내","message": "$[doc_no] 처리바랍니다", "template": "document_mail_template.html", "url":["https://127.0.0.1/portals/main"] }','0509e09412534a6e98f04ca79abb6424',now(),null,null);
insert into notification_config_detail values('cmdbLicense', 'Toast', true, '{"title": "$[ci_no] $[ci_name]","message": "$[monitoring_field] $[duedate]"}','0509e09412534a6e98f04ca79abb6424',now(),null,null);
insert into notification_config_detail values('cmdbLicense', 'SMS', true, '{"title": "$[ci_no] $[ci_name] 안내","message": "$[ci_no] $[ci_name]  $[monitoring_field] $[duedate]"}','0509e09412534a6e98f04ca79abb6424',now(),null,null);
insert into notification_config_detail values('cmdbLicense', 'E-mail', true, '{"title": "$[ci_no] $[ci_name] 안내","message": "$[ci_no] $[ci_name]  $[monitoring_field] $[duedate]", "template": "cmdb_mail_template.html", "url":["https://127.0.0.1/portals/main"]}','0509e09412534a6e98f04ca79abb6424',now(),null,null);
