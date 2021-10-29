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
	form_display_option text,
	form_category varchar(128),
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
COMMENT ON COLUMN wf_form.form_display_option IS '문서양식세부속성';
COMMENT ON COLUMN wf_form.form_category IS '문서양식카테고리';
COMMENT ON COLUMN wf_form.create_user_key IS '생성자';
COMMENT ON COLUMN wf_form.create_dt IS '생성일시';
COMMENT ON COLUMN wf_form.update_user_key IS '수정자';
COMMENT ON COLUMN wf_form.update_dt IS '수정일시';

INSERT INTO wf_form VALUES ('4028b21f7c9698f4017c973010230003', '서비스데스크 - 단순문의', '', 'form.status.use', '{"width":"960","margin":"0 0 0 0","padding":"15 15 15 15"}','process', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO wf_form VALUES ('4028b21f7c9698f4017c9731ebae004e', '서비스데스크 - 단순문의 - 만족도', '', 'form.status.use', '{"width":"960","margin":"0 0 0 0","padding":"15 15 15 15"}','process', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO wf_form VALUES ('4028b21f7c90d996017c91af9dcf0051', '서비스데스크 - 장애신고', '', 'form.status.use', '{"width":"960","margin":"0 0 0 0","padding":"15 15 15 15"}','process', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO wf_form VALUES ('4028b21f7c90d996017c914bce270002', '서비스데스크 - 장애신고 - 만족도', '', 'form.status.use', '{"width":"960","margin":"0 0 0 0","padding":"15 15 15 15"}','process', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO wf_form VALUES ('4028b21f7c9cc269017c9cc8cbf60001', '서비스 데스크 - 서비스요청', '', 'form.status.use', '{"width":"960","margin":"0 0 0 0","padding":"15 15 15 15"}','process', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO wf_form VALUES ('4028b21f7c90d996017c914e27340030', '서비스데스크 - 서비스요청 - 만족도', '', 'form.status.use', '{"width":"960","margin":"0 0 0 0","padding":"15 15 15 15"}','process', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO wf_form VALUES ('2c9180867cc31a25017cc7a069e301a5', '서비스데스크 - 구성관리', '', 'form.status.use', '{"width":"960","margin":"0 0 0 0","padding":"15 15 15 15"}', 'process', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO wf_form VALUES ('2c9180867cc31a25017cc5c7268f0122', '서비스데스크 - 구성관리 - 만족도', '', 'form.status.use', '{"width":"960","margin":"0 0 0 0","padding":"15 15 15 15"}', 'process', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO wf_form VALUES ('4028b8817cbfc7a7017cc08f7b0b0763', '인프라변경관리', '', 'form.status.use', '{"width":"960","margin":"0 0 0 0","padding":"15 15 15 15"}', 'process', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO wf_form VALUES ('4028b8817cc50161017cc5082b460002', '구성관리', '', 'form.status.use', '{"width":"960","margin":"0 0 0 0","padding":"15 15 15 15"}', 'process', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO wf_form VALUES ('4028b22f7c9c4aee017c9c4e15870000', '어플리케이션 변경관리', '', 'form.status.use', '{"width":"960","margin":"0 0 0 0","padding":"15 15 15 15"}', 'process', '0509e09412534a6e98f04ca79abb6424',  now(), null, null);

