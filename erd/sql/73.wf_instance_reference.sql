/**
  참조인 관리
 */
DROP TABLE IF EXISTS wf_instance_reference cascade;

CREATE TABLE wf_instance_reference
(
    instance_id varchar(128) NOT NULL,
    reference_user_key varchar(128) NOT NULL,
    review_yn boolean default false,
    display_yn boolean default false,
    create_user_key varchar(128),
    create_dt timestamp,
    update_user_key varchar(128),
    update_dt timestamp,
    CONSTRAINT wf_instance_reference_pk PRIMARY KEY (instance_id, reference_user_key),
    CONSTRAINT wf_instance_reference_fk1 FOREIGN KEY (instance_id) REFERENCES wf_instance (instance_id),
    CONSTRAINT wf_instance_reference_fk2 FOREIGN KEY (reference_user_key) REFERENCES awf_user (user_key)
);

COMMENT ON TABLE wf_instance_reference IS '참조인관리';
COMMENT ON COLUMN wf_instance_reference.instance_id IS '인스턴스아이디';
COMMENT ON COLUMN wf_instance_reference.reference_user_key IS '참조인아이디';
COMMENT ON COLUMN wf_instance_reference.review_yn IS '읽음여부';
COMMENT ON COLUMN wf_instance_reference.display_yn IS '표시여부';
COMMENT ON COLUMN wf_instance_reference.create_user_key IS '생성자';
COMMENT ON COLUMN wf_instance_reference.create_dt IS '생성일시';
COMMENT ON COLUMN wf_instance_reference.create_user_key IS '수정자';
COMMENT ON COLUMN wf_instance_reference.create_dt IS '수정일시';
