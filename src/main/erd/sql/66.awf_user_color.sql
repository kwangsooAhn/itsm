/**
 * 사용자 지정 테이블
 */
DROP TABLE IF EXISTS awf_user_custom cascade;

CREATE TABLE awf_user_custom
(
    user_key varchar(128) NOT NULL,
    custom_type varchar(128) NOT NULL,
    custom_value varchar(512),
    CONSTRAINT awf_user_custom_pk PRIMARY KEY (user_key, custom_type)
);

COMMENT ON TABLE awf_user_custom IS '사용자 지정';
COMMENT ON COLUMN awf_user_custom.user_key IS '사용자키';
COMMENT ON COLUMN awf_user_custom.custom_type IS '타입';
COMMENT ON COLUMN awf_user_custom.custom_value IS '값';