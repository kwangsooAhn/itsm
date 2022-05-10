/**
 * 보관 문서 데이터
 */
DROP TABLE IF EXISTS awf_document_storage cascade;

CREATE TABLE awf_document_storage
(
    instance_id varchar(128) NOT NULL,
    user_key varchar(128) NOT NULL,
    CONSTRAINT awf_document_storage_pk PRIMARY KEY (instance_id, user_key)
);

COMMENT ON TABLE awf_document_storage IS '보관 문서 데이터';
COMMENT ON COLUMN awf_document_storage.instance_id IS '인스턴스아이디';
COMMENT ON COLUMN awf_document_storage.user_key IS '사용자 키';