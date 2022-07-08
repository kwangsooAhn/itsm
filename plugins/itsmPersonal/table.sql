/**
 * 인사 DB 사용자 이력 테이블
 */
DROP TABLE IF EXISTS if_awf_user_history cascade;
DROP SEQUENCE IF EXISTS if_awf_user_history_seq cascade;

CREATE SEQUENCE if_awf_user_history_seq INCREMENT 1 MINVALUE 1 START 1;
CREATE TABLE if_awf_user_history
(
    seq           bigint       NOT NULL PRIMARY KEY,
    user_key      varchar(128) NOT NULL,
    user_id       varchar(128) NOT NULL,
    user_name     varchar(128) NOT NULL,
    mobile_number varchar(128),
    organization  varchar(100),
    use_yn        boolean,
    create_dt     timestamp
);

/**
 * 테스트용 CLIENT 사용자 테이블
 */
DROP TABLE IF EXISTS awf_user_client cascade;
CREATE TABLE awf_user_client
(
    user_key_client      varchar(128) NOT NULL PRIMARY KEY,
    user_id_client       varchar(128) NOT NULL,
    user_name_client     varchar(128) NOT NULL,
    mobile_number_client varchar(128),
    organization_client  varchar(100),
    use_yn_client        boolean
);
insert into awf_user_client
values ('0509e09412534a6e98f04ca79abb6424', 'test1', '테스터1', '010-1111-1111', '001', true);
insert into awf_user_client
values ('0509e09412534a6e98f04ca79abb6425', 'test2', '테스터2', '010-2222-2222', '003', true);
insert into awf_user_client
values ('0509e09412534a6e98f04ca79abb6426', 'test3', '테스터3', '010-3333-3333', '004', true);
insert into awf_user_client
values ('0509e09412534a6e98f04ca79abb6427', 'test4', '테스터4', '010-4444-5555', '006', true);
insert into awf_user_client
values ('0509e09412534a6e98f04ca79abb6428', 'test5', '테스터5', '010-6666-6666', '009', true);
insert into awf_user_client
values ('0509e09412534a6e98f04ca79abb6429', 'test6', '테스터6', '010-7777-7777', '017', true);

/**
 * 테스트용 ITSM IF 사용자 테이블
 */
DROP TABLE IF EXISTS if_awf_user_test cascade;
CREATE TABLE if_awf_user_test
(
    user_key      varchar(128) NOT NULL PRIMARY KEY,
    user_id       varchar(128) NOT NULL,
    user_name     varchar(128) NOT NULL,
    mobile_number varchar(128),
    organization  varchar(100),
    use_yn        boolean
);

/**
 * 테스트용 ITSM 사용자 테이블
 */
DROP TABLE IF EXISTS awf_user_test cascade;
CREATE TABLE awf_user_test
(
    user_key      varchar(128) NOT NULL PRIMARY KEY,
    user_id       varchar(128) NOT NULL,
    user_name     varchar(128) NOT NULL,
    mobile_number varchar(128),
    organization  varchar(100),
    use_yn        boolean
);

/**
 * 테스트용 ITSM 사용자 역할 테이블
 */
DROP TABLE IF EXISTS awf_user_role_test cascade;
CREATE TABLE awf_user_role_test
(
    user_key varchar(128) NOT NULL PRIMARY KEY,
    role_id  varchar(128) NOT NULL
);