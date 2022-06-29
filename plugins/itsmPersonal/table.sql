/**
 * 인사 DB 사용자 이력 테이블
 */
DROP TABLE IF EXISTS if_awf_user_history cascade;
DROP SEQUENCE IF EXISTS if_awf_user_history_seq cascade;

CREATE SEQUENCE if_awf_user_history_seq INCREMENT 1 MINVALUE 1 START 1;
CREATE TABLE if_awf_user_history
(
    seq bigint NOT NULL PRIMARY KEY,
    user_key varchar(128) NOT NULL,
    user_id varchar(128) NOT NULL,
    user_name varchar(128) NOT NULL,
    create_dt timestamp
);

COMMENT ON TABLE if_awf_user_history IS '인사 DB 이력 테이블';
COMMENT ON COLUMN if_awf_user_history.seq IS '이력 번호';
COMMENT ON COLUMN if_awf_user_history.user_key IS '사용자 키';
COMMENT ON COLUMN if_awf_user_history.user_id  IS '사용자 아이디';
COMMENT ON COLUMN if_awf_user_history.user_name  IS '사용자 명';
COMMENT ON COLUMN if_awf_user_history.create_dt IS '등록일';

/**
 * 테스트용 CLIENT 사용자 테이블
 */
DROP TABLE IF EXISTS awf_user_client cascade;
CREATE TABLE awf_user_client
(
    user_key_client varchar(128) NOT NULL PRIMARY KEY,
    user_id_client varchar(128) NOT NULL,
    user_name_client varchar(128) NOT NULL
);
insert into awf_user_client values ('0509e09412534a6e98f04ca79abb6424', 'test1', '테스터1');
insert into awf_user_client values ('0509e09412534a6e98f04ca79abb6425', 'test2', '테스터2');
insert into awf_user_client values ('0509e09412534a6e98f04ca79abb6426', 'test3', '테스터3');

/**
 * 테스트용 ITSM IF 사용자 테이블
 */
DROP TABLE IF EXISTS if_awf_user_test cascade;
CREATE TABLE if_awf_user_test
(
    user_key varchar(128) NOT NULL PRIMARY KEY,
    user_id varchar(128) NOT NULL,
    user_name varchar(128) NOT NULL
);
insert into if_awf_user_test values ('0509e09412534a6e98f04ca79abb6424', 'test1', '테스터1');
insert into if_awf_user_test values ('0509e09412534a6e98f04ca79abb6425', 'test2', '테스터2');
insert into if_awf_user_test values ('0509e09412534a6e98f04ca79abb6426', 'test3', '테스터3');

/**
 * 테스트용 ITSM 사용자 테이블
 */
DROP TABLE IF EXISTS awf_user_test cascade;
CREATE TABLE awf_user_test
(
    user_key varchar(128) NOT NULL PRIMARY KEY,
    user_id varchar(128) NOT NULL,
    user_name varchar(128) NOT NULL
);

insert into awf_user_test values ('0509e09412534a6e98f04ca79abb6424', 'test1', '테스터1');
insert into awf_user_test values ('0509e09412534a6e98f04ca79abb6425', 'test2', '테스터2');
insert into awf_user_test values ('0509e09412534a6e98f04ca79abb6426', 'test3', '테스터3');

/**
 * 테스트용 ITSM 사용자 역할 테이블
 */
DROP TABLE IF EXISTS awf_user_role_test cascade;
CREATE TABLE awf_user_role_test
(
    user_key varchar(128) NOT NULL PRIMARY KEY,
    role_id varchar(128) NOT NULL
);
