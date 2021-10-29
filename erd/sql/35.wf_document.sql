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
INSERT INTO wf_document VALUES ('4028b21f7c90d996017c91ae7987004f','서비스데스크 - 단순문의','','4028b21f7c9698f4017c96a70ded0000','4028b21f7c9698f4017c973010230003','document.status.use','40125c91714df6c325714e053c890125','#64BBF6','application-form','document.group.inquiry','img_document_09.png',false,'0509e09412534a6e98f04ca79abb6424', now(),NULL,NULL);
/* 서비스데스크 - 단순문의 - 만족도 */
INSERT INTO wf_document VALUES ('4028b21f7c90d996017c91aeff1b0050','서비스데스크 - 단순문의 - 만족도','','4028b21f7c9698f4017c96c5630c0002','4028b21f7c9698f4017c9731ebae004e','document.status.use','40288ab7772dae0301772dbca28a0004','#BDBDBD','workflow','',NULL,false,'0509e09412534a6e98f04ca79abb6424', now(),NULL,NULL);
/* 서비스데스크 - 장애신고 */
INSERT INTO wf_document VALUES ('4028b21f7c9adb6a017c9b18489900c9','서비스데스크 - 장애신고','','4028b21f7c81a928017c81aa9dc60000','4028b21f7c90d996017c91af9dcf0051','document.status.use','40125c91714df6c325714e053c890125','#64BBF6','application-form','document.group.incident','img_document_02.png',false,'0509e09412534a6e98f04ca79abb6424', now(),NULL,NULL);
/* 서비스데스크 - 장애신고 - 만족도 */
INSERT INTO wf_document VALUES ('4028b21f7c9b6b1e017c9bedbe8a0012','서비스데스크 - 장애신고 - 만족도','','4028b21f7c9b6b1e017c9bdf04cb0011','4028b21f7c90d996017c914bce270002','document.status.use','40288ab7772dae0301772dbca28a0004','#BDBDBD','workflow','',NULL,false,'0509e09412534a6e98f04ca79abb6424', now(),NULL,NULL);
/* 서비스데스크 - 서비스요청 */
INSERT INTO wf_document VALUES ('4028b21f7c9ff7c8017ca06bde520058','서비스데스크 - 서비스요청','','4028b21f7c9ff7c8017ca0549ef00057','4028b21f7c9cc269017c9cc8cbf60001','document.status.use','40125c91714df6c325714e053c890125','#64BBF6','application-form','document.group.request','img_document_01.png',false,'0509e09412534a6e98f04ca79abb6424', now(),NULL,NULL);
/* 서비스데스크 - 서비스요청 - 만족도 */
INSERT INTO wf_document VALUES ('4028b21f7c9ff7c8017ca04d16830000','서비스데스크 - 서비스요청 - 만족도','','4028b21f7c9cc269017c9cc76a5e0000','4028b21f7c90d996017c914e27340030','document.status.use','40288ab7772dae0301772dbca28a0004','#BDBDBD','workflow','',NULL,false,'0509e09412534a6e98f04ca79abb6424', now(),NULL,NULL);
/* 인프라변경관리 */
INSERT INTO wf_document VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '인프라변경관리', '', '4028b8817cbfc7a7017cc0db1a8c0bc3', '4028b8817cbfc7a7017cc08f7b0b0763', 'document.status.use', '4028b25d7886e2d801788704dd8e0002', '#76BD26', 'workflow', '', null, false, '0509e09412534a6e98f04ca79abb6424', '2021-10-27 08:38:14.548600',NULL,NULL);
/* 구성관리 */
INSERT INTO wf_document VALUES ('4028b8817cc50161017cc53233c206b0', '구성관리', '', '4028b8817cc50161017cc5079e850000', '4028b8817cc50161017cc5082b460002', 'document.status.use', '40288ab777f04ed90177f05e5ad7000a', '#A95EEB', 'workflow', '', null, false, '0509e09412534a6e98f04ca79abb6424', now(),NULL,NULL);
/*어플리케이션 변경관리*/
INSERT INTO wf_document VALUES('4028b22f7cc55c1a017cc5775d10026b', '어플리케이션 변경관리', '', '4028b22f7cba6866017cbb63a53c08f8', '4028b22f7c9c4aee017c9c4e15870000', 'document.status.use', '4028b25d7886e2d801788704dd8e0002', '#2578CE', 'workflow', 'servicedesk.request', NULL, false, '0509e09412534a6e98f04ca79abb6424', now(), NULL, NULL);
