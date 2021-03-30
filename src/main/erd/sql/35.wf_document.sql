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
COMMENT ON COLUMN wf_document.create_user_key IS '생성자';
COMMENT ON COLUMN wf_document.create_dt IS '생성일시';
COMMENT ON COLUMN wf_document.update_user_key IS '수정자';
COMMENT ON COLUMN wf_document.update_dt IS '수정일시';

insert into wf_document values ('4028b25d78778da6017877aff7e40001','만족도 - 단순문의','만족도  단순문의 입니다.','4028b25d787736640178773e71480002','4028b25d787736640178773f645b0003','document.status.use','40288ab7772dae0301772dbca28a0004','#586872','workflow','',NULL,'0509e09412534a6e98f04ca79abb6424',now());
insert into wf_document values ('4028b25d78778da6017877bb3d3c0010','단순문의','서비스데스크 단순문의 신청서 입니다.','4028b25d78778da6017877b9df60000f','4028b25d787736640178773857920000','document.status.use','40125c91714df6c325714e053c890125','#F1C40F','application-form','servicedesk.inquiry','img_document_11.png','0509e09412534a6e98f04ca79abb6424',now());
insert into wf_document values ('40288ab777f04ed90177f05f01d1000b','CI 신청서','CI를 등록/수정/삭제를 할 수 있는 구성관리 신청서 입니다.','40288ab777f04ed90177f057ca410000','4028b25d77ed7e6f0177ed8daba10001','document.status.use','40288ab777f04ed90177f05e5ad7000a','#825A2C','application-form','','img_document_06.png','0509e09412534a6e98f04ca79abb6424',now());
insert into wf_document values ('4028b8817880d833017880f5cafc0004', '릴리즈관리', '릴리즈관리 업무흐름입니다.', '40288ab77878ea67017878eb3dc30000', '4028b881787e3e8c01787e5018880000', 'document.status.use', '4028b8817880d833017880f34ae10003', '#9B59B6', 'workflow', '', NULL, '0509e09412534a6e98f04ca79abb6424', now());
