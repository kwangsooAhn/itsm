/**
 * 신청서정보
 */
DROP TABLE IF EXISTS wf_document cascade;

CREATE TABLE wf_document
(
	document_id varchar(128) NOT NULL,
	document_name varchar(256) NOT NULL,
	document_desc varchar(256),
	process_id varchar(128) NOT NULL,
	form_id varchar(128) NOT NULL,
	document_status varchar(100) DEFAULT 'document.status.use',
	numbering_id varchar(128),
	document_color varchar(128),
	document_type varchar(100) NOT NULL,
	document_group varchar(100),
	document_icon varchar(100),
	api_enable boolean DEFAULT false,
	create_user_key varchar(128),
	create_dt timestamp,
	update_user_key varchar(128),
	update_dt timestamp,
	CONSTRAINT wf_document_pk PRIMARY KEY (document_id),
	CONSTRAINT wf_document_fk1 FOREIGN KEY (process_id) REFERENCES wf_process (process_id),
	CONSTRAINT wf_document_fk2 FOREIGN KEY (form_id) REFERENCES wf_form (form_id),
	CONSTRAINT wf_document_fk3 FOREIGN KEY (numbering_id) REFERENCES awf_numbering_rule (numbering_id)
);

COMMENT ON TABLE wf_document IS '신청서정보';
COMMENT ON COLUMN wf_document.document_id IS '신청서아이디';
COMMENT ON COLUMN wf_document.document_name IS '신청서이름';
COMMENT ON COLUMN wf_document.document_desc IS '신청서설명';
COMMENT ON COLUMN wf_document.process_id IS '프로세스아이디';
COMMENT ON COLUMN wf_document.form_id IS '문서양식아이디';
COMMENT ON COLUMN wf_document.document_status IS '문서상태';
COMMENT ON COLUMN wf_document.numbering_id IS '넘버링아이디';
COMMENT ON COLUMN wf_document.document_color IS '문서색상';
COMMENT ON COLUMN wf_document.document_type IS '문서종류';
COMMENT ON COLUMN wf_document.document_group IS '신청서그룹';
COMMENT ON COLUMN wf_document.document_icon IS '신청서아이콘';
COMMENT ON COLUMN wf_document.api_enable IS 'API 활성화';
COMMENT ON COLUMN wf_document.create_user_key IS '생성자';
COMMENT ON COLUMN wf_document.create_dt IS '생성일시';
COMMENT ON COLUMN wf_document.update_user_key IS '수정자';
COMMENT ON COLUMN wf_document.update_dt IS '수정일시';

/* 서비스데스크 - 단순문의 */
INSERT INTO wf_document VALUES ('4028b21f7c90d996017c91ae7987004f','서비스 데스크 - 단순문의','','4028b21f7c9698f4017c96a70ded0000','4028b21f7c9698f4017c973010230003','document.status.use','40125c91714df6c325714e053c890125','#64BBF6','application-form','document.group.inquiry','img_document_11.png',false,'0509e09412534a6e98f04ca79abb6424', now(),NULL,NULL);
/* 서비스데스크 - 단순문의 - 만족도 */
INSERT INTO wf_document VALUES ('4028b21f7c90d996017c91aeff1b0050','서비스 데스크 - 단순문의 - 만족도','','4028b21f7c9698f4017c96c5630c0002','4028b21f7c9698f4017c9731ebae004e','document.status.use','40288ab7772dae0301772dbca28a0004','#BDBDBD','workflow','',NULL,false,'0509e09412534a6e98f04ca79abb6424', now(),NULL,NULL);
