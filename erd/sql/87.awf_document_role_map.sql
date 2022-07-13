/**
 * 신청서-역할 매핑
 */
DROP TABLE IF EXISTS awf_document_role_map cascade;

CREATE TABLE awf_document_role_map
(
    document_id varchar(128) NOT NULL,
    document_type varchar(100) NOT NULL,
    role_id varchar(100) NOT NULL,
    CONSTRAINT awf_document_role_map_pk PRIMARY KEY (document_id, role_id)
);

COMMENT ON TABLE awf_document_role_map  IS '신청서역할매핑';
COMMENT ON COLUMN awf_document_role_map.document_id IS '신청서아이디';
COMMENT ON COLUMN awf_document_role_map.document_type IS '신청서 타입';
COMMENT ON COLUMN awf_document_role_map.role_id IS '역할아이디';