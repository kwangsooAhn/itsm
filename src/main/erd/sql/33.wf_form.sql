/**
 * 문서양식정보
 */
DROP TABLE IF EXISTS wf_form cascade;

CREATE TABLE wf_form
(
	form_id varchar(128) NOT NULL,
	form_name varchar(256) NOT NULL,
	form_desc varchar(256),
	form_status varchar(100) DEFAULT 'form.status.edit' NOT NULL,
	create_user_key varchar(128),
	create_dt timestamp,
	update_user_key varchar(128),
	update_dt timestamp,
	CONSTRAINT wf_form_pk PRIMARY KEY (form_id)
);

COMMENT ON TABLE wf_form IS '문서양식정보';
COMMENT ON COLUMN wf_form.form_id IS '문서양식아이디';
COMMENT ON COLUMN wf_form.form_name IS '문서양식이름';
COMMENT ON COLUMN wf_form.form_desc IS '문서양식설명';
COMMENT ON COLUMN wf_form.form_status IS '문서양식상태';
COMMENT ON COLUMN wf_form.create_user_key IS '생성자';
COMMENT ON COLUMN wf_form.create_dt IS '생성일시';
COMMENT ON COLUMN wf_form.update_user_key IS '수정자';
COMMENT ON COLUMN wf_form.update_dt IS '수정일시';

insert into wf_form values('4028b25d787736640178773f645b0003', '만족도 - 단순문의', '만족도 단순문의 문서양식입니다.', 'form.status.use', '0509e09412534a6e98f04ca79abb6424', now());
insert into wf_form values('4028b25d787736640178773857920000', '서비스데스크 - 단순문의', '단순문의 문서양식입니다.', 'form.status.use', '0509e09412534a6e98f04ca79abb6424', now());
insert into wf_form values('4028b25d77ed7e6f0177ed8daba10001','구성관리','구성관리 문서 양식 입니다.','form.status.use','0509e09412534a6e98f04ca79abb6424', now());
insert into wf_form values('4028b25d78870b0901788770dc400024', '인프라 변경관리', '인프라 변경관리 문서양식입니다.', 'form.status.use', '0509e09412534a6e98f04ca79abb6424', now());