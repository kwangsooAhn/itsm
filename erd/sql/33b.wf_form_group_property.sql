/**
 * 문서양식 그룹 세부정보
 */
DROP TABLE IF EXISTS wf_form_group_property cascade;

CREATE TABLE wf_form_group_property (
    form_group_id varchar(128) NOT NULL,
    property_type varchar(100) NOT NULL,
    property_options text NULL,
    CONSTRAINT wf_form_group_property_pk PRIMARY KEY (form_group_id, property_type),
    CONSTRAINT wf_form_group_property_fk FOREIGN KEY (form_group_id) REFERENCES wf_form_group(form_group_id)
);

COMMENT ON TABLE wf_form_group_property IS '문서양식 그룹 세부정보';
COMMENT ON COLUMN wf_form_group_property.form_group_id IS '문서양식 그룹아이디';
COMMENT ON COLUMN wf_form_group_property.property_type IS '속성 타입';
COMMENT ON COLUMN wf_form_group_property.property_options IS '속성 값';

/* 서비스데스크 - 단순문의 */
INSERT INTO wf_form_group_property VALUES ('g028b21f7c780ba6017c7832447201e1','display','{"displayOrder":0,"isAccordionUsed":false,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('g028b21f7c780ba6017c7832447201e1','label','{"visibility":false,"fontColor":"#222529","fontSize":"14","bold":false,"italic":false,"underline":false,"align":"center","text":""}');
INSERT INTO wf_form_group_property VALUES ('g028b21f7c780ba6017c7832480101e2','display','{"displayOrder":1,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('g028b21f7c780ba6017c7832480101e2','label','{"visibility":true,"fontColor":"#8B9094","fontSize":"20","bold":true,"italic":false,"underline":false,"align":"left","text":"신청내역"}');
INSERT INTO wf_form_group_property VALUES ('g028b21f7c780ba6017c7832527e0203','display','{"displayOrder":2,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('g028b21f7c780ba6017c7832527e0203','label','{"visibility":true,"fontColor":"#8B9094","fontSize":"20","bold":true,"italic":false,"underline":false,"align":"left","text":"접수내역"}');
INSERT INTO wf_form_group_property VALUES ('g028b21f7c780ba6017c783258080214','display','{"displayOrder":3,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('g028b21f7c780ba6017c783258080214','label','{"visibility":true,"fontColor":"#8B9094","fontSize":"20","bold":true,"italic":false,"underline":false,"align":"left","text":"처리내역"}');
INSERT INTO wf_form_group_property VALUES ('g028b21f7c780ba6017c78325ca80225','display','{"displayOrder":4,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('g028b21f7c780ba6017c78325ca80225','label','{"visibility":true,"fontColor":"#8B9094","fontSize":"20","bold":true,"italic":false,"underline":false,"align":"left","text":"승인 / 반려 내역"}');
/* 서비스데스크 - 단순문의 - 만족도 */
INSERT INTO wf_form_group_property VALUES ('g028b21f7c780ba6017c78334c07023c','display','{"displayOrder":0,"isAccordionUsed":false,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('g028b21f7c780ba6017c78334c07023c','label','{"visibility":false,"fontColor":"#222529","fontSize":"14","bold":false,"italic":false,"underline":false,"align":"center","text":""}');
INSERT INTO wf_form_group_property VALUES ('g028b21f7c780ba6017c78334f3a0244','display','{"displayOrder":1,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('g028b21f7c780ba6017c78334f3a0244','label','{"visibility":true,"fontColor":"#8B9094","fontSize":"20","bold":true,"italic":false,"underline":false,"align":"left","text":"만족도 평가"}');
