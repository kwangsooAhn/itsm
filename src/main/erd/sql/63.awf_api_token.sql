/**
 * API Token 정보
 */
DROP TABLE IF EXISTS awf_api_token cascade;

CREATE TABLE awf_api_token
(
    api_id varchar(100) NOT NULL,
    access_token varchar(128) NOT NULL,
    expires_in int,
    refresh_token varchar(128) NOT NULL,
    refresh_token_expires_in int,
    create_dt timestamp,
    request_user_id varchar(128),
    CONSTRAINT awf_api_token_pk PRIMARY KEY (api_id),
    CONSTRAINT awf_api_token_uk1 UNIQUE (access_token, refresh_token)
);

COMMENT ON TABLE awf_api_token IS 'API 토큰 정보';
COMMENT ON COLUMN awf_api_token.api_id IS 'API 아이디';
COMMENT ON COLUMN awf_api_token.access_token IS '접근 토큰';
COMMENT ON COLUMN awf_api_token.expires_in IS '접근 토큰 만료 시간(초)';
COMMENT ON COLUMN awf_api_token.refresh_token IS '리프레시 토큰';
COMMENT ON COLUMN awf_api_token.refresh_token_expires_in IS '리프레시 토큰 만료 시간(초)';
COMMENT ON COLUMN awf_api_token.create_dt IS '생성일자';
COMMENT ON COLUMN awf_api_token.request_user_id IS '요청 사용자 아이디';
