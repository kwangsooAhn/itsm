/**
 * 조직관리 테이블
 */
DROP TABLE IF EXISTS awf_organization cascade;

CREATE TABLE awf_organization
(
    organization_id varchar(100) NOT NULL,
    p_organization_id  varchar(100),
    organization_name varchar(128),
    organization_desc text,
    use_yn boolean default true,
    level integer,
    seq_num integer,
    editable boolean default true,
    create_user_key varchar(128),
    create_dt timestamp,
    update_user_key varchar(128),
    update_dt timestamp,
    CONSTRAINT awf_organization_pk PRIMARY KEY (organization_id)
);

COMMENT ON TABLE awf_organization IS '조직관리';
COMMENT ON COLUMN awf_organization.organization_id IS '조직아이디';
COMMENT ON COLUMN awf_organization.p_organization_id IS '부모조직아이디';
COMMENT ON COLUMN awf_organization.organization_name IS '조직명';
COMMENT ON COLUMN awf_organization.organization_desc IS '조직설명';
COMMENT ON COLUMN awf_organization.use_yn IS '사용여부';
COMMENT ON COLUMN awf_organization.level IS '그룹 레벨';
COMMENT ON COLUMN awf_organization.seq_num IS '정렬 순서';
COMMENT ON COLUMN awf_organization.editable IS '수정여부';
COMMENT ON COLUMN awf_organization.create_user_key IS '등록자';
COMMENT ON COLUMN awf_organization.create_dt IS '등록일';
COMMENT ON COLUMN awf_organization.update_user_key IS '수정자';
COMMENT ON COLUMN awf_organization.update_dt IS '수정일';

insert into awf_organization values ('4028b2d57d37168e017d3716cgf00000', null, '전체', null, true, 0, 0, true, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_organization values ('4028b2d57d37168e017d3715fae00002', '4028b2d57d37168e017d3716cgf00000', '본부 1', null, true, 1, 1, true, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_organization values ('4028b2d57d37168e017d3713bb430003', '4028b2d57d37168e017d3716cgf00000', '본부 2', null, true, 1, 2, true, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_organization values ('4028b2d57d37168e017d3715fae00004', '4028b2d57d37168e017d3716cgf00000', '본부 3', null, true, 1, 3, true, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_organization values ('4028b2d57d37168e017d3713bb430005', '4028b2d57d37168e017d3716cgf00000', '본부 4', null, true, 1, 4, true, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_organization values ('4028b2d57d37168e017d37197bb40001', '4028b2d57d37168e017d3715fae00002', '그룹 1-1', null, true, 2, 1, true, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_organization values ('4028b2d57d37168e017d371a4c1f0002', '4028b2d57d37168e017d3715fae00002', '그룹 1-2', null, true, 2, 2, true, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_organization values ('4028b2d57d37168e017d371a5f7f0004', '4028b2d57d37168e017d37197bb40001', '팀 1-1-1', null, true, 3, 1, true, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_organization values ('4028b2d57d37168e017d371a5f3d0006', '4028b2d57d37168e017d37197bb40001', '팀 1-1-2', null, true, 3, 2, true, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
