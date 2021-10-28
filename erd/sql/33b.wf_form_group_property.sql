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
/* 서비스데스크 - 장애신고 */
INSERT INTO wf_form_group_property VALUES ('4028b21f7c9adb6a017c9b0613610065','display','{"displayOrder":0,"isAccordionUsed":false,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('4028b21f7c9adb6a017c9b0613610065','label','{"visibility":false,"fontColor":"#222529","fontSize":"14","bold":false,"italic":false,"underline":false,"align":"center","text":""}');
INSERT INTO wf_form_group_property VALUES ('4028b21f7c9adb6a017c9b061411006d','display','{"displayOrder":1,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('4028b21f7c9adb6a017c9b061411006d','label','{"visibility":true,"fontColor":"#8B9094","fontSize":"20","bold":true,"italic":false,"underline":false,"align":"left","text":"신청내역"}');
INSERT INTO wf_form_group_property VALUES ('4028b21f7c9adb6a017c9b061691008c','display','{"displayOrder":2,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('4028b21f7c9adb6a017c9b061691008c','label','{"visibility":true,"fontColor":"#8B9094","fontSize":"20","bold":true,"italic":false,"underline":false,"align":"left","text":"접수내역"}');
INSERT INTO wf_form_group_property VALUES ('4028b21f7c9adb6a017c9b0617fe009e','display','{"displayOrder":3,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('4028b21f7c9adb6a017c9b0617fe009e','label','{"visibility":true,"fontColor":"#8B9094","fontSize":"20","bold":true,"italic":false,"underline":false,"align":"left","text":"처리내역"}');
INSERT INTO wf_form_group_property VALUES ('4028b21f7c9adb6a017c9b061a8b00bf','display','{"displayOrder":4,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('4028b21f7c9adb6a017c9b061a8b00bf','label','{"visibility":true,"fontColor":"#8B9094","fontSize":"20","bold":true,"italic":false,"underline":false,"align":"left","text":"승인 / 반려 내역"}');
/* 서비스데스크 - 장애신고 - 만족도 */
INSERT INTO wf_form_group_property VALUES ('4028b21f7c90d996017c914da7aa0021','display','{"displayOrder":0,"isAccordionUsed":false,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('4028b21f7c90d996017c914da7aa0021','label','{"visibility":false,"fontColor":"#222529","fontSize":"14","bold":false,"italic":false,"underline":false,"align":"center","text":""}');
INSERT INTO wf_form_group_property VALUES ('4028b21f7c90d996017c914da8270029','display','{"displayOrder":1,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('4028b21f7c90d996017c914da8270029','label','{"visibility":true,"fontColor":"#8B9094","fontSize":"20","bold":true,"italic":false,"underline":false,"align":"left","text":"만족도 평가"}');
/* 서비스데스크 - 서비스요청 */
INSERT INTO wf_form_group_property VALUES ('4028b21f7c9ff7c8017ca054465a0001','display','{"displayOrder":0,"isAccordionUsed":false,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('4028b21f7c9ff7c8017ca054465a0001','label','{"visibility":false,"fontColor":"#222529","fontSize":"14","bold":false,"italic":false,"underline":false,"align":"center","text":""}');
INSERT INTO wf_form_group_property VALUES ('4028b21f7c9ff7c8017ca054470f000a','display','{"displayOrder":1,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('4028b21f7c9ff7c8017ca054470f000a','label','{"visibility":true,"fontColor":"#8B9094","fontSize":"20","bold":true,"italic":false,"underline":false,"align":"left","text":"신청내역"}');
INSERT INTO wf_form_group_property VALUES ('4028b21f7c9ff7c8017ca05448d20027','display','{"displayOrder":2,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('4028b21f7c9ff7c8017ca05448d20027','label','{"visibility":true,"fontColor":"#8B9094","fontSize":"20","bold":true,"italic":false,"underline":false,"align":"left","text":"접수내역"}');
INSERT INTO wf_form_group_property VALUES ('4028b21f7c9ff7c8017ca05449e00039','display','{"displayOrder":3,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('4028b21f7c9ff7c8017ca05449e00039','label','{"visibility":true,"fontColor":"#8B9094","fontSize":"20","bold":true,"italic":false,"underline":false,"align":"left","text":"처리내역"}');
INSERT INTO wf_form_group_property VALUES ('4028b21f7c9ff7c8017ca0544af6004d','display','{"displayOrder":4,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('4028b21f7c9ff7c8017ca0544af6004d','label','{"visibility":true,"fontColor":"#8B9094","fontSize":"20","bold":true,"italic":false,"underline":false,"align":"left","text":"승인 / 반려 내역"}');
/* 서비스데스크 - 서비스요청 - 만족도 */
INSERT INTO wf_form_group_property VALUES ('4028b21f7c90d996017c914eec300040','display','{"displayOrder":0,"isAccordionUsed":false,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('4028b21f7c90d996017c914eec300040','label','{"visibility":false,"fontColor":"#222529","fontSize":"14","bold":false,"italic":false,"underline":false,"align":"center","text":""}');
INSERT INTO wf_form_group_property VALUES ('4028b21f7c90d996017c914eecb30048','display','{"displayOrder":1,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('4028b21f7c90d996017c914eecb30048','label','{"visibility":true,"fontColor":"#8B9094","fontSize":"20","bold":true,"italic":false,"underline":false,"align":"left","text":"만족도 평가"}');
/* 인프라변경관리 */
INSERT INTO wf_form_group_property VALUES ('4028b8817cbfc7a7017cc095a5670ae8', 'display', '{"displayOrder":0,"isAccordionUsed":false,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('4028b8817cbfc7a7017cc095a5670ae8', 'label', '{"visibility":false,"fontColor":"#222529","fontSize":"14","bold":false,"italic":false,"underline":false,"align":"center","text":""}');
INSERT INTO wf_form_group_property VALUES ('4028b8817cbfc7a7017cc095a59a0af2', 'display', '{"displayOrder":1,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('4028b8817cbfc7a7017cc095a59a0af2', 'label', '{"visibility":true,"fontColor":"#8B9094","fontSize":"20","bold":true,"italic":false,"underline":false,"align":"left","text":"신청내역"}');
INSERT INTO wf_form_group_property VALUES ('4028b8817cbfc7a7017cc095a6360b0f', 'display', '{"displayOrder":2,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('4028b8817cbfc7a7017cc095a6360b0f', 'label', '{"visibility":true,"fontColor":"#8B9094","fontSize":"20","bold":true,"italic":false,"underline":false,"align":"left","text":"접수내역"}');
INSERT INTO wf_form_group_property VALUES ('4028b8817cbfc7a7017cc095a6a10b25', 'display', '{"displayOrder":3,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('4028b8817cbfc7a7017cc095a6a10b25', 'label', '{"visibility":true,"fontColor":"#8B9094","fontSize":"20","bold":true,"italic":false,"underline":false,"align":"left","text":"변경  자문 회의록"}');
INSERT INTO wf_form_group_property VALUES ('4028b8817cbfc7a7017cc095a6eb0b35', 'display', '{"displayOrder":4,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('4028b8817cbfc7a7017cc095a6eb0b35', 'label', '{"visibility":true,"fontColor":"rgba(141, 146, 153, 1)","fontSize":"20","bold":true,"italic":false,"underline":false,"align":"left","text":"변경 계획서"}');
INSERT INTO wf_form_group_property VALUES ('4028b8817cbfc7a7017cc095a77f0b54', 'display', '{"displayOrder":5,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('4028b8817cbfc7a7017cc095a77f0b54', 'label', '{"visibility":true,"fontColor":"#8B9094","fontSize":"20","bold":true,"italic":false,"underline":false,"align":"left","text":"변경 계획서 승인 내역"}');
INSERT INTO wf_form_group_property VALUES ('4028b8817cbfc7a7017cc095a79f0b5b', 'display', '{"displayOrder":6,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('4028b8817cbfc7a7017cc095a79f0b5b', 'label', '{"visibility":true,"fontColor":"rgba(141, 146, 153, 1)","fontSize":"20","bold":true,"italic":false,"underline":false,"align":"left","text":"변경 결과서"}');
INSERT INTO wf_form_group_property VALUES ('4028b8817cbfc7a7017cc095a7d90b68', 'display', '{"displayOrder":7,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('4028b8817cbfc7a7017cc095a7d90b68', 'label', '{"visibility":true,"fontColor":"rgba(141, 146, 153, 1)","fontSize":"20","bold":false,"italic":false,"underline":false,"align":"left","text":"PIR"}');
INSERT INTO wf_form_group_property VALUES ('4028b8817cbfc7a7017cc095a81b0b77', 'display', '{"displayOrder":8,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('4028b8817cbfc7a7017cc095a81b0b77', 'label', '{"visibility":true,"fontColor":"rgba(141, 146, 153, 1)","fontSize":"20","bold":true,"italic":false,"underline":false,"align":"left","text":"최종 검토 의견"}');
/* 구성관리 */
INSERT INTO wf_form_group_property VALUES ('4028b8817cc50161017cc531b8e50661', 'display', '{"displayOrder":0,"isAccordionUsed":false,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('4028b8817cc50161017cc531b8e50661', 'label', '{"visibility":false,"fontColor":"#222529","fontSize":"14","bold":false,"italic":false,"underline":false,"align":"center","text":""}');
INSERT INTO wf_form_group_property VALUES ('4028b8817cc50161017cc531b913066b', 'display', '{"displayOrder":1,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('4028b8817cc50161017cc531b913066b', 'label', '{"visibility":true,"fontColor":"#8B9094","fontSize":"20","bold":true,"italic":false,"underline":false,"align":"left","text":"신청내역"}');
INSERT INTO wf_form_group_property VALUES ('4028b8817cc50161017cc531b9bb068a', 'display', '{"displayOrder":2,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('4028b8817cc50161017cc531b9bb068a', 'label', '{"visibility":true,"fontColor":"#8B9094","fontSize":"20","bold":true,"italic":false,"underline":false,"align":"left","text":"접수내역"}');
INSERT INTO wf_form_group_property VALUES ('4028b8817cc50161017cc531ba050699', 'display', '{"displayOrder":3,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('4028b8817cc50161017cc531ba050699', 'label', '{"visibility":true,"fontColor":"#8B9094","fontSize":"20","bold":true,"italic":false,"underline":false,"align":"left","text":"처리내역"}');
INSERT INTO wf_form_group_property VALUES ('4028b8817cc50161017cc531ba5406a9', 'display', '{"displayOrder":4,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES ('4028b8817cc50161017cc531ba5406a9', 'label', '{"visibility":true,"fontColor":"#8B9094","fontSize":"20","bold":true,"italic":false,"underline":false,"align":"left","text":"승인내역"}');
