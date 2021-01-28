/**
 * 컴포넌트정보
 */
DROP TABLE IF EXISTS wf_component cascade;

CREATE TABLE wf_component
(
	component_id varchar(128) NOT NULL,
	form_id varchar(128) NOT NULL,
	component_type varchar(100) NOT NULL,
	mapping_id varchar(128),
	is_topic boolean DEFAULT 'false',
	CONSTRAINT wf_component_pk PRIMARY KEY (component_id),
	CONSTRAINT wf_component_fk FOREIGN KEY (form_id) REFERENCES wf_form (form_id)
);

COMMENT ON TABLE wf_component IS '컴포넌트정보';
COMMENT ON COLUMN wf_component.component_id IS '컴포넌트아이디';
COMMENT ON COLUMN wf_component.form_id IS '문서양식아이디';
COMMENT ON COLUMN wf_component.component_type IS '컴포넌트종류';
COMMENT ON COLUMN wf_component.mapping_id IS '매핑아이디';
COMMENT ON COLUMN wf_component.is_topic IS '토픽여부';

insert into wf_component values ('02775b1802734c47908fc3da0c7014a8','40288ab7772ea2e301772ea450bb0000','accordion-end','',FALSE);
insert into wf_component values ('0c262cfa44fe440bb8284978ea8554d1','40288ab7772ea2e301772ea450bb0000','divider','',FALSE);
insert into wf_component values ('1c577578d51d448299eee1c98340a223','40288ab7772ea2e301772ea450bb0000','datetime','id6',FALSE);
insert into wf_component values ('22fa2c689b9d43a986dfd34e197c7f3a','40288ab7772ea2e301772ea450bb0000','inputbox','id2',FALSE);
insert into wf_component values ('28c9a8ad4b144cfbbdc27349d855773f','40288ab7772ea2e301772ea450bb0000','datetime','id12',FALSE);
insert into wf_component values ('3132307e44ff4286b92650638afececd','40288ab7772ea2e301772ea450bb0000','fileupload','id9',FALSE);
insert into wf_component values ('3b6712c115aa4e648565a4befea8ccd3','40288ab7772ea2e301772ea450bb0000','accordion-start','',FALSE);
insert into wf_component values ('4472820c4c4949258e8af5374ca15ff3','40288ab7772ea2e301772ea450bb0000','inputbox','user',FALSE);
insert into wf_component values ('47dc200d5a564d46bd1ac5cfc1d76187','40288ab7772ea2e301772ea450bb0000','datetime','id10',FALSE);
insert into wf_component values ('4c82676d6321443a8ea6f38161ceff2e','40288ab7772ea2e301772ea450bb0000','textbox','id8',FALSE);
insert into wf_component values ('560cb3baf8f64146b8d6a5f8c3124d63','40288ab7772ea2e301772ea450bb0000','textbox','id11',FALSE);
insert into wf_component values ('5d80057a921040669b4a5be53ea57304','40288ab7772ea2e301772ea450bb0000','dropdown','id4',FALSE);
insert into wf_component values ('61b3d4683993430a9cb03ab9cb088fef','40288ab7772ea2e301772ea450bb0000','textbox','id13',FALSE);
insert into wf_component values ('786337268d7745c9ab06e54a0f01a23a','40288ab7772ea2e301772ea450bb0000','inputbox','id3',FALSE);
insert into wf_component values ('7b8c641a13624211830b9c466f217985','40288ab7772ea2e301772ea450bb0000','dropdown','id5',FALSE);
insert into wf_component values ('7ee30588d4a8430ab249fd95f9fe0890','40288ab7772ea2e301772ea450bb0000','accordion-start','',FALSE);
insert into wf_component values ('8cab0f7cd82a4873b4060d243cf1de48','40288ab7772ea2e301772ea450bb0000','datetime','id15',FALSE);
insert into wf_component values ('96a8288e0cf349b6bc4609ccd45a069c','40288ab7772ea2e301772ea450bb0000','textbox','id16',FALSE);
insert into wf_component values ('9b986ab955324ab481a464a0820d9f44','40288ab7772ea2e301772ea450bb0000','label','',FALSE);
insert into wf_component values ('9cfae02dcf344b049da289b62add4080','40288ab7772ea2e301772ea450bb0000','accordion-end','',FALSE);
insert into wf_component values ('a924fa7f6fd946298f92e2589a3a81f9','40288ab7772ea2e301772ea450bb0000','datetime','id1',FALSE);
insert into wf_component values ('bc775676f79d4a2b8c1b3a239b101ac5','40288ab7772ea2e301772ea450bb0000','accordion-end','',FALSE);
insert into wf_component values ('bffef2192206490c8ae34d1162a7013f','40288ab7772ea2e301772ea450bb0000','accordion-start','',FALSE);
insert into wf_component values ('d89d0854c1a84472af43fdc50b28c68e','40288ab7772ea2e301772ea450bb0000','inputbox','id7',TRUE);
insert into wf_component values ('d9a167efd5c2406a92adf82e6363bff9','40288ab7772ea2e301772ea450bb0000','accordion-end','',FALSE);
insert into wf_component values ('dba870b223974a3b8287f5f2537940cc','40288ab7772ea2e301772ea450bb0000','custom-code','assignee',FALSE);
insert into wf_component values ('df9e44a1f03941c7b69135382e99b52d','40288ab7772ea2e301772ea450bb0000','accordion-start','',FALSE);
insert into wf_component values ('e83b4c632b264606a56012be262ec2e9','40288ab7772ea2e301772ea450bb0000','fileupload','id14',FALSE);
insert into wf_component values ('feada594a34f4b268aab37eb9211e740','40288ab7772ea2e301772ea450bb0000','custom-code','assignee2',FALSE);
insert into wf_component values ('009d794d7c4e4f9f9aec2be0b4b8c47c','40288ab7772ea2e301772ea633090001','custom-code','assignee',FALSE);
insert into wf_component values ('012b0f6d123b4282a85366e27817d895','40288ab7772ea2e301772ea633090001','accordion-end','',FALSE);
insert into wf_component values ('025ab809164c4504a24070406228f5f0','40288ab7772ea2e301772ea633090001','textbox','',FALSE);
insert into wf_component values ('269d0173006946b0923ce1d09c6a6393','40288ab7772ea2e301772ea633090001','divider','',FALSE);
insert into wf_component values ('30f4ab97a0334e7b99e6018af7a8c35f','40288ab7772ea2e301772ea633090001','dropdown','id5',FALSE);
insert into wf_component values ('3d4a319e7c4c4e64b3bb2c0efe9f6034','40288ab7772ea2e301772ea633090001','datetime','id6',FALSE);
insert into wf_component values ('541687276b2443f0b93e3d11dc5b5737','40288ab7772ea2e301772ea633090001','dropdown','id4',FALSE);
insert into wf_component values ('6db6c1b3e25b494aafb25d2099aa15a1','40288ab7772ea2e301772ea633090001','inputbox','id7',TRUE);
insert into wf_component values ('7d59614e37564e50af3a9acd91ca8706','40288ab7772ea2e301772ea633090001','inputbox','id3',FALSE);
insert into wf_component values ('7f55f7817cc94171a5d68e6aab533403','40288ab7772ea2e301772ea633090001','textbox','id13',FALSE);
insert into wf_component values ('815ef64cab6b4750a20055f89c644f00','40288ab7772ea2e301772ea633090001','accordion-end','',FALSE);
insert into wf_component values ('81806c50ff734d5ea5ddd8ffeebe41e0','40288ab7772ea2e301772ea633090001','accordion-start','',FALSE);
insert into wf_component values ('887a8ced40b74c459c9b8c3b5fb15f07','40288ab7772ea2e301772ea633090001','radio','id15',FALSE);
insert into wf_component values ('895c1e72156e4b9f9d0bc011318f899e','40288ab7772ea2e301772ea633090001','inputbox','id2',FALSE);
insert into wf_component values ('9bde985e28cc41b098437e8eade18265','40288ab7772ea2e301772ea633090001','textbox','id8',FALSE);
insert into wf_component values ('a6ce1b0dac7f41e7b0c97ac84a6e086b','40288ab7772ea2e301772ea633090001','label','',FALSE);
insert into wf_component values ('ac12c395142045678a491164d41aff78','40288ab7772ea2e301772ea633090001','fileupload','id9',FALSE);
insert into wf_component values ('b04f7051d7674a53804f63326e07781e','40288ab7772ea2e301772ea633090001','datetime','id12',FALSE);
insert into wf_component values ('b6b12cfda11c4af8a76494e135ca2f8d','40288ab7772ea2e301772ea633090001','fileupload','id14',FALSE);
insert into wf_component values ('b6cf4e793abc40cc9093b0ea13157d12','40288ab7772ea2e301772ea633090001','accordion-start','',FALSE);
insert into wf_component values ('bd38d6e3e9014767a012e2aeaa779539','40288ab7772ea2e301772ea633090001','datetime','id1',FALSE);
insert into wf_component values ('d88c58bdb16f47feac9c286100f67faa','40288ab7772ea2e301772ea633090001','accordion-start','',FALSE);
insert into wf_component values ('e0720e3eeecd433baf321a836a8e9eb5','40288ab7772ea2e301772ea633090001','accordion-end','',FALSE);
insert into wf_component values ('e8c829ae11944852ae70d37a77648edf','40288ab7772ea2e301772ea633090001','inputbox','user',FALSE);
insert into wf_component values ('f6a6b980bc134161a09e2e25eeeee4c8','40288ab7772ea2e301772ea633090001','fileupload','id17',FALSE);