/**
 * 서비스 카테고리 정보
 */
DROP TABLE IF EXISTS service_category cascade;

CREATE TABLE service_category
(
    service_code varchar(100) NOT NULL,
    p_service_code varchar(100),
    service_name varchar(100) NOT NULL,
    service_desc text,
    ava_goal text,
    start_date timestamp,
    end_date timestamp,
    editable boolean default true,
    use_yn boolean default true,
    level integer,
    seq_num integer,
    create_user_key varchar(128),
    create_dt timestamp,
    update_user_key varchar(128),
    update_dt timestamp,
    CONSTRAINT service_category_pk PRIMARY KEY (service_code),
    CONSTRAINT service_category_uk UNIQUE (service_name)
);

COMMENT ON TABLE service_category IS '서비스카테고리정보';
COMMENT ON COLUMN service_category.service_code IS '서비스코드';
COMMENT ON COLUMN service_category.p_service_code IS '부모서비스코드';
COMMENT ON COLUMN service_category.service_name IS '서비스이름';
COMMENT ON COLUMN service_category.service_desc IS '서비스설명';
COMMENT ON COLUMN service_category.ava_goal IS '가용목표';
COMMENT ON COLUMN service_category.start_date IS '서비스시작일자';
COMMENT ON COLUMN service_category.end_date IS '서비스종료일자';
COMMENT ON COLUMN service_category.editable IS '수정가능여부';
COMMENT ON COLUMN service_category.use_yn IS '사용여부';
COMMENT ON COLUMN service_category.level IS '레벨';
COMMENT ON COLUMN service_category.seq_num IS '출력순서';
COMMENT ON COLUMN service_category.create_user_key IS '등록자';
COMMENT ON COLUMN service_category.create_dt IS '등록일';
COMMENT ON COLUMN service_category.update_user_key IS '수정자';
COMMENT ON COLUMN service_category.update_dt IS '수정일';

insert into service_category values ('service', null, 'IT서비스', null, null, null, null, false, true, 0, 0, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
