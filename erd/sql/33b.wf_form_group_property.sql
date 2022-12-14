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
INSERT INTO wf_form_group_property VALUES('g028b21f7c780ba6017c7832447201e1', 'display', '{"displayOrder":0,"isAccordionUsed":false,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('g028b21f7c780ba6017c7832447201e1', 'label', '{"visibility":false,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"center","text":""}');
INSERT INTO wf_form_group_property VALUES('g028b21f7c780ba6017c7832480101e2', 'display', '{"displayOrder":1,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('g028b21f7c780ba6017c7832480101e2', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"신청내역"}');
INSERT INTO wf_form_group_property VALUES('g028b21f7c780ba6017c7832527e0203', 'display', '{"displayOrder":2,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('g028b21f7c780ba6017c7832527e0203', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"접수내역"}');
INSERT INTO wf_form_group_property VALUES('g028b21f7c780ba6017c783258080214', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"처리내역"}');
INSERT INTO wf_form_group_property VALUES('g028b21f7c780ba6017c783258080214', 'display', '{"displayOrder":3,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('g028b21f7c780ba6017c78325ca80225', 'display', '{"displayOrder":4,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('g028b21f7c780ba6017c78325ca80225', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"승인 / 반려 내역"}');
/* 서비스데스크 - 단순문의 - 만족도 */
INSERT INTO wf_form_group_property VALUES('g028b21f7c780ba6017c78334c07023c', 'display', '{"displayOrder":0,"isAccordionUsed":false,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('g028b21f7c780ba6017c78334c07023c', 'label', '{"visibility":false,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"center","text":""}');
INSERT INTO wf_form_group_property VALUES('g028b21f7c780ba6017c78334f3a0244', 'display', '{"displayOrder":2,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('g028b21f7c780ba6017c78334f3a0244', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"만족도 평가"}');
INSERT INTO wf_form_group_property VALUES('g028b21f7ce3c1c2017ce435961500b4', 'display', '{"displayOrder":1,"isAccordionUsed":false,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('g028b21f7ce3c1c2017ce435961500b4', 'label', '{"visibility":false,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"center","text":""}');
/* 서비스데스크 - 장애신고 */
INSERT INTO wf_form_group_property VALUES('4028b21f7c9adb6a017c9b0613610065', 'display', '{"displayOrder":0,"isAccordionUsed":false,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b21f7c9adb6a017c9b0613610065', 'label', '{"visibility":false,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"center","text":""}');
INSERT INTO wf_form_group_property VALUES('4028b21f7c9adb6a017c9b061411006d', 'display', '{"displayOrder":1,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b21f7c9adb6a017c9b061411006d', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"신청내역"}');
INSERT INTO wf_form_group_property VALUES('4028b21f7c9adb6a017c9b061691008c', 'display', '{"displayOrder":2,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b21f7c9adb6a017c9b061691008c', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"접수내역"}');
INSERT INTO wf_form_group_property VALUES('4028b21f7c9adb6a017c9b0617fe009e', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"처리내역"}');
INSERT INTO wf_form_group_property VALUES('4028b21f7c9adb6a017c9b0617fe009e', 'display', '{"displayOrder":3,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b21f7c9adb6a017c9b061a8b00bf', 'display', '{"displayOrder":4,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b21f7c9adb6a017c9b061a8b00bf', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"승인 / 반려 내역"}');
/* 서비스데스크 - 장애신고 - 만족도 */
INSERT INTO wf_form_group_property VALUES('4028b21f7c90d996017c914da7aa0021', 'display', '{"displayOrder":0,"isAccordionUsed":false,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b21f7c90d996017c914da7aa0021', 'label', '{"visibility":false,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"center","text":""}');
INSERT INTO wf_form_group_property VALUES('4028b21f7c90d996017c914da8270029', 'display', '{"displayOrder":2,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b21f7c90d996017c914da8270029', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"만족도 평가"}');
INSERT INTO wf_form_group_property VALUES('4028b21f7ce3c1c2017ce435961500b5', 'display', '{"displayOrder":1,"isAccordionUsed":false,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b21f7ce3c1c2017ce435961500b5', 'label', '{"visibility":false,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"center","text":""}');
/* 서비스데스크 - 서비스요청 */
INSERT INTO wf_form_group_property VALUES('40288ada7d2b171e017d2b211b3a00f7', 'display', '{"displayOrder":5,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('40288ada7d2b171e017d2b211b3a00f7', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"관련 CI"}');
INSERT INTO wf_form_group_property VALUES('4028b21f7c9ff7c8017ca054465a0001', 'display', '{"displayOrder":0,"isAccordionUsed":false,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b21f7c9ff7c8017ca054465a0001', 'label', '{"visibility":false,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"center","text":""}');
INSERT INTO wf_form_group_property VALUES('4028b21f7c9ff7c8017ca054470f000a', 'display', '{"displayOrder":1,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b21f7c9ff7c8017ca054470f000a', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"신청내역"}');
INSERT INTO wf_form_group_property VALUES('4028b21f7c9ff7c8017ca05448d20027', 'display', '{"displayOrder":2,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b21f7c9ff7c8017ca05448d20027', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"접수내역"}');
INSERT INTO wf_form_group_property VALUES('4028b21f7c9ff7c8017ca05449e00039', 'display', '{"displayOrder":3,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b21f7c9ff7c8017ca05449e00039', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"처리내역"}');
INSERT INTO wf_form_group_property VALUES('4028b21f7c9ff7c8017ca0544af6004d', 'display', '{"displayOrder":4,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b21f7c9ff7c8017ca0544af6004d', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"승인 / 반려 내역"}');
/* 서비스데스크 - 서비스요청 - 만족도 */
INSERT INTO wf_form_group_property VALUES('4028b21f7c90d996017c914eec300040', 'display', '{"displayOrder":0,"isAccordionUsed":false,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b21f7c90d996017c914eec300040', 'label', '{"visibility":false,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"center","text":""}');
INSERT INTO wf_form_group_property VALUES('4028b21f7c90d996017c914eecb30048', 'display', '{"displayOrder":2,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b21f7c90d996017c914eecb30048', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"만족도 평가"}');
INSERT INTO wf_form_group_property VALUES('4028b21f7ce3c1c2017ce435961500b6', 'display', '{"displayOrder":1,"isAccordionUsed":false,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b21f7ce3c1c2017ce435961500b6', 'label', '{"visibility":false,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"center","text":""}');
/* 서비스데스크 - 구성관리 */
INSERT INTO wf_form_group_property VALUES('2c9180867cc31a25017cc7a68e9204d3', 'display', '{"displayOrder":0,"isAccordionUsed":false,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('2c9180867cc31a25017cc7a68e9204d3', 'label', '{"visibility":false,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":""}');
INSERT INTO wf_form_group_property VALUES('2c9180867cc31a25017cc7a68ea004dc', 'display', '{"displayOrder":1,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('2c9180867cc31a25017cc7a68ea004dc', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"신청내역"}');
INSERT INTO wf_form_group_property VALUES('40288ada7d08d6b1017d091dcf4b0261', 'display', '{"displayOrder":2,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('40288ada7d08d6b1017d091dcf4b0261', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"대상 CI"}');
INSERT INTO wf_form_group_property VALUES('2c9180867cc31a25017cc7a68ece0500', 'display', '{"displayOrder":3,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('2c9180867cc31a25017cc7a68ece0500', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"접수내역"}');
INSERT INTO wf_form_group_property VALUES('2c9180867cc31a25017cc7a68edf050c', 'display', '{"displayOrder":4,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('2c9180867cc31a25017cc7a68edf050c', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"처리내역"}');
INSERT INTO wf_form_group_property VALUES('2c9180867cc31a25017cc7a68eef0519', 'display', '{"displayOrder":5,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('2c9180867cc31a25017cc7a68eef0519', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"승인내역"}');
/* 서비스데스크 - 구성관리 - 만족도 */
INSERT INTO wf_form_group_property VALUES('2c9180867cc31a25017cc5c8ad340134', 'display', '{"displayOrder":0,"isAccordionUsed":false,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('2c9180867cc31a25017cc5c8ad340134', 'label', '{"visibility":false,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"center","text":""}');
INSERT INTO wf_form_group_property VALUES('2c9180867cc31a25017cc5c8ad4b013d', 'display', '{"displayOrder":2,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('2c9180867cc31a25017cc5c8ad4b013d', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"만족도 평가"}');
INSERT INTO wf_form_group_property VALUES('4028b21f7ce3c1c2017ce435961500b7', 'display', '{"displayOrder":1,"isAccordionUsed":false,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b21f7ce3c1c2017ce435961500b7', 'label', '{"visibility":false,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"center","text":""}');
/* 인프라변경관리 */
INSERT INTO wf_form_group_property VALUES('4028b8817cbfc7a7017cc095a5670ae8', 'display', '{"displayOrder":0,"isAccordionUsed":false,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b8817cbfc7a7017cc095a5670ae8', 'label', '{"visibility":false,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"center","text":""}');
INSERT INTO wf_form_group_property VALUES('4028b8817cbfc7a7017cc095a59a0af2', 'display', '{"displayOrder":1,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b8817cbfc7a7017cc095a59a0af2', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"신청내역"}');
INSERT INTO wf_form_group_property VALUES('4028b8817cbfc7a7017cc095a6360b0f', 'display', '{"displayOrder":2,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b8817cbfc7a7017cc095a6360b0f', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"접수내역"}');
INSERT INTO wf_form_group_property VALUES('4028b8817cbfc7a7017cc095a6a10b25', 'display', '{"displayOrder":3,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b8817cbfc7a7017cc095a6a10b25', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"변경  자문 회의록"}');
INSERT INTO wf_form_group_property VALUES('4028b8817cbfc7a7017cc095a6eb0b35', 'display', '{"displayOrder":4,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b8817cbfc7a7017cc095a6eb0b35', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"변경 계획서"}');
INSERT INTO wf_form_group_property VALUES('4028b8817cbfc7a7017cc095a77f0b54', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"변경 계획서 승인 내역"}');
INSERT INTO wf_form_group_property VALUES('4028b8817cbfc7a7017cc095a77f0b54', 'display', '{"displayOrder":5,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b8817cbfc7a7017cc095a79f0b5b', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"변경 결과서"}');
INSERT INTO wf_form_group_property VALUES('4028b8817cbfc7a7017cc095a79f0b5b', 'display', '{"displayOrder":6,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b8817cbfc7a7017cc095a7d90b68', 'display', '{"displayOrder":7,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b8817cbfc7a7017cc095a7d90b68', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"PIR"}');
INSERT INTO wf_form_group_property VALUES('4028b8817cbfc7a7017cc095a81b0b77', 'display', '{"displayOrder":8,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b8817cbfc7a7017cc095a81b0b77', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"최종 검토 의견"}');
/* 구성관리 */
INSERT INTO wf_form_group_property VALUES('4028b8817cc50161017cc531b8e50661', 'display', '{"displayOrder":0,"isAccordionUsed":false,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b8817cc50161017cc531b8e50661', 'label', '{"visibility":false,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"center","text":""}');
INSERT INTO wf_form_group_property VALUES('4028b8817cc50161017cc531b913066b', 'display', '{"displayOrder":1,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b8817cc50161017cc531b913066b', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"신청내역"}');
INSERT INTO wf_form_group_property VALUES('4028b8817cc50161017cc531b9bb068a', 'display', '{"displayOrder":2,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b8817cc50161017cc531b9bb068a', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"접수내역"}');
INSERT INTO wf_form_group_property VALUES('4028b8817cc50161017cc531ba050699', 'display', '{"displayOrder":3,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b8817cc50161017cc531ba050699', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"처리내역"}');
INSERT INTO wf_form_group_property VALUES('4028b8817cc50161017cc531ba5406a9', 'display', '{"displayOrder":4,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b8817cc50161017cc531ba5406a9', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"승인내역"}');
INSERT INTO wf_form_group_property VALUES('40288ada7d0d3c49017d0d57be030026', 'display', '{"displayOrder":5,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('40288ada7d0d3c49017d0d57be030026', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"대상 CI"}');
/*어플리케이션 변경관리*/
INSERT INTO wf_form_group_property VALUES('4028b22f7cc55c1a017cc5731f95019d', 'label', '{"visibility":false,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":""}');
INSERT INTO wf_form_group_property VALUES('4028b22f7cc55c1a017cc5731f95019d', 'display', '{"displayOrder":0,"isAccordionUsed":false,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b22f7cc55c1a017cc57320cc01a6', 'display', '{"displayOrder":1,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b22f7cc55c1a017cc57320cc01a6', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"신청 내역"}');
INSERT INTO wf_form_group_property VALUES('4028b22f7cc55c1a017cc57324d201c2', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"접수내역"}');
INSERT INTO wf_form_group_property VALUES('4028b22f7cc55c1a017cc57324d201c2', 'display', '{"displayOrder":2,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b22f7cc55c1a017cc57327e801d8', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"개발 계획서"}');
INSERT INTO wf_form_group_property VALUES('4028b22f7cc55c1a017cc57327e801d8', 'display', '{"displayOrder":3,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b22f7cc55c1a017cc5732de30201', 'display', '{"displayOrder":4,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b22f7cc55c1a017cc5732de30201', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"승인 내역"}');
INSERT INTO wf_form_group_property VALUES('4028b22f7cc55c1a017cc5732ece0208', 'display', '{"displayOrder":5,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b22f7cc55c1a017cc5732ece0208', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"설계 검증 검토"}');
INSERT INTO wf_form_group_property VALUES('4028b22f7cc55c1a017cc5733093020f', 'display', '{"displayOrder":6,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b22f7cc55c1a017cc5733093020f', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"구현 내역"}');
INSERT INTO wf_form_group_property VALUES('4028b22f7cc55c1a017cc5733292021d', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"단위 테스트 내역"}');
INSERT INTO wf_form_group_property VALUES('4028b22f7cc55c1a017cc5733292021d', 'display', '{"displayOrder":7,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b22f7cc55c1a017cc573342c0228', 'display', '{"displayOrder":8,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b22f7cc55c1a017cc573342c0228', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"통합 테스트 내역"}');
INSERT INTO wf_form_group_property VALUES('4028b22f7cc55c1a017cc573361a0236', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"현업 테스트 요청"}');
INSERT INTO wf_form_group_property VALUES('4028b22f7cc55c1a017cc573361a0236', 'display', '{"displayOrder":9,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b22f7cc55c1a017cc5733701023d', 'display', '{"displayOrder":10,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b22f7cc55c1a017cc5733701023d', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"현업 테스트 내역"}');
INSERT INTO wf_form_group_property VALUES('4028b22f7cc55c1a017cc573390a024b', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"릴리즈 결과 내역"}');
INSERT INTO wf_form_group_property VALUES('4028b22f7cc55c1a017cc573390a024b', 'display', '{"displayOrder":11,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b22f7cc55c1a017cc5733ac10257', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"변경 완료 보고"}');
INSERT INTO wf_form_group_property VALUES('4028b22f7cc55c1a017cc5733ac10257', 'display', '{"displayOrder":12,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b22f7cc55c1a017cc5733c560264', 'display', '{"displayOrder":13,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b22f7cc55c1a017cc5733c560264', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"최종 검토 의견"}');
/* 문제관리 */
INSERT INTO wf_form_group_property VALUES('4028b21c7cdffb67017ce0b45bbb0815', 'display', '{"displayOrder":0,"isAccordionUsed":false,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b21c7cdffb67017ce0b45bbb0815', 'label', '{"visibility":false,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"center","text":""}');
INSERT INTO wf_form_group_property VALUES('4028b21c7cdffb67017ce0b45c93081e', 'display', '{"displayOrder":1,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b21c7cdffb67017ce0b45c93081e', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"신청내역"}');
INSERT INTO wf_form_group_property VALUES('4028b21c7cdffb67017ce0b45ff30843', 'display', '{"displayOrder":2,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b21c7cdffb67017ce0b45ff30843', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"접수내역"}');
INSERT INTO wf_form_group_property VALUES('4028b21c7cdffb67017ce0b461580853', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"해결내역서"}');
INSERT INTO wf_form_group_property VALUES('4028b21c7cdffb67017ce0b461580853', 'display', '{"displayOrder":3,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b21c7cdffb67017ce0b463150868', 'display', '{"displayOrder":4,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b21c7cdffb67017ce0b463150868', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"승인 / 반려 내역"}');
INSERT INTO wf_form_group_property VALUES('a327b427dbe31e0fcea86fef3bd68174', 'display', '{"displayOrder":5,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('a327b427dbe31e0fcea86fef3bd68174', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":false,"italic":false,"underline":false,"align":"left","text":"관련 CI"}');
/* 장애관리 */
INSERT INTO wf_form_group_property VALUES('4028b21c7cdffb67017ce0b6490a08d9', 'display', '{"displayOrder":0,"isAccordionUsed":false,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b21c7cdffb67017ce0b6490a08d9', 'label', '{"visibility":false,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"center","text":""}');
INSERT INTO wf_form_group_property VALUES('4028b21c7cdffb67017ce0b64a0a08e3', 'display', '{"displayOrder":1,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b21c7cdffb67017ce0b64a0a08e3', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"신청내역"}');
INSERT INTO wf_form_group_property VALUES('4028b21c7cdffb67017ce0b64d9f090b', 'display', '{"displayOrder":2,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b21c7cdffb67017ce0b64d9f090b', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"접수내역"}');
INSERT INTO wf_form_group_property VALUES('4028b21c7cdffb67017ce0b64ea60917', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"처리내역"}');
INSERT INTO wf_form_group_property VALUES('4028b21c7cdffb67017ce0b64ea60917', 'display', '{"displayOrder":3,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b21c7cdffb67017ce0b6510d0933', 'display', '{"displayOrder":4,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('4028b21c7cdffb67017ce0b6510d0933', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"승인 / 반려 내역"}');
/* 서비스 연속성 관리 계획서 */
INSERT INTO wf_form_group_property VALUES('9c8cceb77a20423984bfa4ef7e73d4d1', 'display', '{"displayOrder":0,"isAccordionUsed":false,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('9c8cceb77a20423984bfa4ef7e73d4d1', 'label', '{"visibility":false,"fontColor":"#8d9299","fontSize":"14","bold":true,"italic":false,"underline":false,"align":"center","text":""}');
INSERT INTO wf_form_group_property VALUES('99ddaf58c42d4d259213b83120714e8e', 'display', '{"displayOrder":1,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('99ddaf58c42d4d259213b83120714e8e', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"신청내역"}');
INSERT INTO wf_form_group_property VALUES('ca60cc72983c4b62beebb4fa22f03402', 'display', '{"displayOrder":2,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('ca60cc72983c4b62beebb4fa22f03402', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"승인 / 반려 내역"}');
/* 복구 훈련 계획서 */
INSERT INTO wf_form_group_property VALUES('b9b860237b0e44758d6c28a37d028c4a', 'display', '{"displayOrder":"0","isAccordionUsed":false,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('b9b860237b0e44758d6c28a37d028c4a', 'label', '{"visibility":false,"fontColor":"#8d9299","fontSize":"14","bold":true,"italic":false,"underline":false,"align":"center","text":""}');
INSERT INTO wf_form_group_property VALUES('22ebaf5b292242219be3999d3fefa23f', 'display', '{"displayOrder":"1","isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('22ebaf5b292242219be3999d3fefa23f', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"신청내역"}');
INSERT INTO wf_form_group_property VALUES('def5b9a0a7a34b238cbc45d2fe6578b1', 'display', '{"displayOrder":"2","isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('def5b9a0a7a34b238cbc45d2fe6578b1', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"승인 / 반려 내역"}');
/* 복구 훈련 결과서 */
INSERT INTO wf_form_group_property VALUES('bdc95719b39949809fbf2aaa5052224e', 'display', '{"displayOrder":"0","isAccordionUsed":false,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('bdc95719b39949809fbf2aaa5052224e', 'label', '{"visibility":false,"fontColor":"#8d9299","fontSize":"14","bold":true,"italic":false,"underline":false,"align":"center","text":""}');
INSERT INTO wf_form_group_property VALUES('03c0b58eeda64bcdb7598b853e0e27f9', 'display', '{"displayOrder":"1","isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('03c0b58eeda64bcdb7598b853e0e27f9', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"신청내역"}');
INSERT INTO wf_form_group_property VALUES('3cd16ba4f27547a4afaed9e28faeac10', 'display', '{"displayOrder":"2","isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('3cd16ba4f27547a4afaed9e28faeac10', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"승인 / 반려 내역"}');
/* 용량 검토 보고서*/
INSERT INTO wf_form_group_property VALUES('12be8c4ab2ea42049c3c9d49c5eadb37', 'display', '{"displayOrder":0,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('12be8c4ab2ea42049c3c9d49c5eadb37', 'label', '{"visibility":false,"fontColor":"#8d9299","fontSize":"14","bold":false,"italic":false,"underline":false,"align":"left","text":"GROUP LABEL"}');
INSERT INTO wf_form_group_property VALUES('71f00c59f9cc4a06ac370f7d9470ad95', 'display', '{"displayOrder":1,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('71f00c59f9cc4a06ac370f7d9470ad95', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"작성 내역"}');
INSERT INTO wf_form_group_property VALUES('a25f03ce1c814208a4991667d1d53636', 'display', '{"displayOrder":2,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('a25f03ce1c814208a4991667d1d53636', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"승인 / 반려 내역"}');
/* 용량 조치 보고서 */
INSERT INTO wf_form_group_property VALUES('a33304346f2c45ddb202de50cce4efb3', 'display', '{"displayOrder":0,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('a33304346f2c45ddb202de50cce4efb3', 'label', '{"visibility":false,"fontColor":"#8d9299","fontSize":"14","bold":false,"italic":false,"underline":false,"align":"left","text":"GROUP LABEL"}');
INSERT INTO wf_form_group_property VALUES('a534780c74fc465fbe18383216b0e66b', 'display', '{"displayOrder":1,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('a534780c74fc465fbe18383216b0e66b', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"조치 내역"}');
INSERT INTO wf_form_group_property VALUES('6a763bba33e64614a83a37afdc54d711', 'display', '{"displayOrder":2,"isAccordionUsed":true,"margin":"10 0 10 0"}');
INSERT INTO wf_form_group_property VALUES('6a763bba33e64614a83a37afdc54d711', 'label', '{"visibility":true,"fontColor":"#6D6D6D","fontSize":"22","bold":true,"italic":false,"underline":false,"align":"left","text":"승인 / 반려 내역"}');
