/**
  신청서 링크
 */
DROP TABLE IF EXISTS wf_document_link cascade;

create table wf_document_link
(
    document_link_id varchar(128) NOT NULL,
    document_name varchar(256) NOT NULL,
    document_group varchar(100),
    document_desc varchar(256),
    document_link_url varchar(256),
    document_status varchar(100) DEFAULT 'document.status.use',
    document_color  varchar(128),
    document_icon varchar(100),
    create_user_key varchar(128),
    create_dt timestamp,
    update_user_key varchar(128),
    update_dt timestamp,
    CONSTRAINT wf_document_link_pk PRIMARY KEY (document_link_id)
);

COMMENT ON TABLE wf_document_link IS '신청서 링크 정보';
COMMENT ON COLUMN wf_document_link.document_link_id IS '신청서 링크 아이디';
COMMENT ON COLUMN wf_document_link.document_name IS '신청서 이름';
COMMENT ON COLUMN wf_document_link.document_group IS '신청서 그룹';
COMMENT ON COLUMN wf_document_link.document_desc IS '신청서 설명';
COMMENT ON COLUMN wf_document_link.document_link_url IS '신청서 링크 URL';
COMMENT ON COLUMN wf_document_link.document_status IS '신청서 상태';
COMMENT ON COLUMN wf_document_link.create_user_key IS '생성자';
COMMENT ON COLUMN wf_document_link.create_dt IS '생성일시';
COMMENT ON COLUMN wf_document_link.update_user_key IS '수정자';
COMMENT ON COLUMN wf_document_link.update_dt IS '수정일시';
