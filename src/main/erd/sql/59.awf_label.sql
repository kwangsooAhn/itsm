/**
 * Label 마스터 클래스
 */
DROP TABLE IF EXISTS awf_label cascade;

CREATE TABLE awf_label
(
    label_target character varying(128),
    label_target_id character varying(128),
    label_key character varying(128),
    label_value character varying(512),
    create_user_key character varying(128),
    create_dt timestamp,
    update_user_key character varying(128),
    update_dt timestamp,
    CONSTRAINT awf_label_pk UNIQUE (label_target, label_target_id, label_key)
);

COMMENT ON TABLE awf_label IS '라벨마스터';
COMMENT ON COLUMN awf_label.label_target IS '라벨링 대상 타입';
COMMENT ON COLUMN awf_label.label_target_id IS '라벨링 대상';
COMMENT ON COLUMN awf_label.label_key IS '라벨링 키값';
COMMENT ON COLUMN awf_label.label_value IS '라벨링 value 값';
COMMENT ON COLUMN awf_label.create_user_key IS '등록자';
COMMENT ON COLUMN awf_label.create_dt IS '등록일시';
COMMENT ON COLUMN awf_label.update_user_key IS '수정자';
COMMENT ON COLUMN awf_label.update_dt IS '수정일시';