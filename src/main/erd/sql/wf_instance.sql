/**
 * 인스턴스정보
 */
DROP TABLE IF EXISTS wf_instance cascade;

CREATE TABLE wf_instance
(
	instance_id varchar(128) NOT NULL,
	document_id varchar(128) NOT NULL,
	instance_status varchar(100) NOT NULL,
	instance_start_dt timestamp NOT NULL,
	instance_end_dt timestamp,
	instance_create_user_key varchar(128) NOT NULL,
	p_token_id varchar(128),
	document_no varchar(128) NOT NULL,
	CONSTRAINT wf_instance_pk PRIMARY KEY (instance_id),
	CONSTRAINT wf_instance_fk FOREIGN KEY (document_id) REFERENCES wf_document (document_id)
);

COMMENT ON TABLE wf_instance IS '인스턴스정보';
COMMENT ON COLUMN wf_instance.instance_id IS '인스턴스아이디';
COMMENT ON COLUMN wf_instance.document_id IS '신청서아이디';
COMMENT ON COLUMN wf_instance.instance_status IS '인스턴스상태';
COMMENT ON COLUMN wf_instance.instance_start_dt IS '인스턴스시작일시';
COMMENT ON COLUMN wf_instance.instance_end_dt IS '인스턴스종료일시';
COMMENT ON COLUMN wf_instance.instance_create_user_key IS '인스턴스생성자';
COMMENT ON COLUMN wf_instance.p_token_id IS '호출토큰아이디';
COMMENT ON COLUMN wf_instance.document_no IS '문서번호';