/**
 * 조직관리 테이블
 */
DROP TABLE IF EXISTS awf_group cascade;

CREATE TABLE awf_group
(
    group_id varchar(100) NOT NULL,
    p_group_id  varchar(100),
    group_name varchar(128),
    group_desc text,
    use_yn boolean default true,
    create_user_key character varying(128),
    create_dt timestamp,
    update_user_key varchar(128),
    update_dt timestamp,
    CONSTRAINT awf_group_pk PRIMARY KEY (group_id),
    CONSTRAINT awf_group_uk UNIQUE (group_name)
);

COMMENT ON TABLE awf_group IS '조직관리';
COMMENT ON COLUMN awf_group.group_id IS '조직아이디';
COMMENT ON COLUMN awf_group.group_id IS '부모조직아이디';
COMMENT ON COLUMN awf_group.group_name IS '조직명';
COMMENT ON COLUMN awf_group.group_desc IS '조직설명';
COMMENT ON COLUMN awf_group.use_yn IS '사용여부';
COMMENT ON COLUMN awf_group.create_user_key IS '등록자';
COMMENT ON COLUMN awf_group.create_dt IS '등록일';
COMMENT ON COLUMN awf_group.update_user_key IS '수정자';
COMMENT ON COLUMN awf_group.update_dt IS '수정일';

insert into awf_group values ('4028b2d57d37168e017d3716cgf00000', null, '조직구성', null, true, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_group values ('4028b2d57d37168e017d3715fae00002', '4028b2d57d37168e017d3716cgf00000', '본부 1', null, true, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_group values ('4028b2d57d37168e017d3713bb430003', '4028b2d57d37168e017d3716cgf00000', '본부 2', null, true, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_group values ('4028b2d57d37168e017d37197bb40001', '4028b2d57d37168e017d3715fae00002', '그룹 1-1', null, true, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_group values ('4028b2d57d37168e017d371a4c1f0002', '4028b2d57d37168e017d3715fae00002', '그룹 1-2', null, true, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_group values ('4028b2d57d37168e017d371a5f7f0004', '4028b2d57d37168e017d37197bb40001', '팀 1-1-1', null, true, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_group values ('4028b2d57d37168e017d371a5f3d0006', '4028b2d57d37168e017d37197bb40001', '팀 1-1-2', null, true, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
