/**
 * 문서양식 그룹정보
 */
DROP TABLE IF EXISTS wf_form_group cascade;

CREATE TABLE wf_form_group
(
    form_group_id varchar(128) NULL,
    form_group_name varchar(256) NULL,
    form_id varchar(128) NULL,
    CONSTRAINT wf_form_group_pk PRIMARY KEY (form_group_id),
    CONSTRAINT wf_form_group_fk FOREIGN KEY (form_id) REFERENCES wf_form (form_id)
);

COMMENT ON TABLE wf_form_group IS '문서양식 그룹정보';
COMMENT ON COLUMN wf_form_group.form_group_id IS '문서양식 그룹아이디';
COMMENT ON COLUMN wf_form_group.form_group_name IS '문서양식 그룹이름';
COMMENT ON COLUMN wf_form_group.form_id IS '문서양식아이디';

/* 서비스데스크 - 단순문의 */
INSERT INTO wf_form_group VALUES ('g028b21f7c780ba6017c7832447201e1','제목','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_form_group VALUES ('g028b21f7c780ba6017c7832480101e2','신청내역','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_form_group VALUES ('g028b21f7c780ba6017c7832527e0203','접수내역','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_form_group VALUES ('g028b21f7c780ba6017c783258080214','처리내역','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_form_group VALUES ('g028b21f7c780ba6017c78325ca80225','승인 / 반려 내역','4028b21f7c9698f4017c973010230003');
/* 서비스데스크 - 단순문의 - 만족도 */
INSERT INTO wf_form_group VALUES ('g028b21f7c780ba6017c78334c07023c','제목','4028b21f7c9698f4017c9731ebae004e');
INSERT INTO wf_form_group VALUES ('g028b21f7c780ba6017c78334f3a0244','만족도평가','4028b21f7c9698f4017c9731ebae004e');
/* 서비스데스크 - 장애신고 */
INSERT INTO wf_form_group VALUES ('4028b21f7c9adb6a017c9b0613610065','제목','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_form_group VALUES ('4028b21f7c9adb6a017c9b061411006d','신청내역','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_form_group VALUES ('4028b21f7c9adb6a017c9b061691008c','접수내역','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_form_group VALUES ('4028b21f7c9adb6a017c9b0617fe009e','처리내역','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_form_group VALUES ('4028b21f7c9adb6a017c9b061a8b00bf','승인 / 반려 내역','4028b21f7c90d996017c91af9dcf0051');
/* 서비스데스크 - 장애신고 - 만족도 */
INSERT INTO wf_form_group VALUES ('4028b21f7c90d996017c914da7aa0021','제목','4028b21f7c90d996017c914bce270002');
INSERT INTO wf_form_group VALUES ('4028b21f7c90d996017c914da8270029','만족도평가','4028b21f7c90d996017c914bce270002');
/* 서비스데스크 - 서비스요청 */
INSERT INTO wf_form_group VALUES ('4028b21f7c9ff7c8017ca054465a0001','제목','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_form_group VALUES ('4028b21f7c9ff7c8017ca054470f000a','신청내역','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_form_group VALUES ('4028b21f7c9ff7c8017ca05448d20027','접수내역','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_form_group VALUES ('4028b21f7c9ff7c8017ca05449e00039','처리내역','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_form_group VALUES ('4028b21f7c9ff7c8017ca0544af6004d','승인 / 반려 내역','4028b21f7c9cc269017c9cc8cbf60001');
/* 서비스데스크 - 서비스요청 - 만족도 */
INSERT INTO wf_form_group VALUES ('4028b21f7c90d996017c914eec300040','제목','4028b21f7c90d996017c914e27340030');
INSERT INTO wf_form_group VALUES ('4028b21f7c90d996017c914eecb30048','만족도평가','4028b21f7c90d996017c914e27340030');
/* 인프라변경관리 */
INSERT INTO wf_form_group VALUES ('4028b8817cbfc7a7017cc095a5670ae8', '제목', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_form_group VALUES ('4028b8817cbfc7a7017cc095a59a0af2', '신청내역', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_form_group VALUES ('4028b8817cbfc7a7017cc095a6360b0f', '접수내역', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_form_group VALUES ('4028b8817cbfc7a7017cc095a6a10b25', '변경  자문 회의록', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_form_group VALUES ('4028b8817cbfc7a7017cc095a6eb0b35', '변경 계획서', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_form_group VALUES ('4028b8817cbfc7a7017cc095a77f0b54', '변경 계획서 승인 내역', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_form_group VALUES ('4028b8817cbfc7a7017cc095a79f0b5b', '변경 결과서', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_form_group VALUES ('4028b8817cbfc7a7017cc095a7d90b68', 'PIR', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_form_group VALUES ('4028b8817cbfc7a7017cc095a81b0b77', '최종 검토 의견', '4028b8817cbfc7a7017cc08f7b0b0763');
/* 구성관리 */
INSERT INTO wf_form_group VALUES ('4028b8817cc50161017cc531b8e50661', '제목', '4028b8817cc50161017cc5082b460002');
INSERT INTO wf_form_group VALUES ('4028b8817cc50161017cc531b913066b', '신청내역', '4028b8817cc50161017cc5082b460002');
INSERT INTO wf_form_group VALUES ('4028b8817cc50161017cc531b9bb068a', '접수내역', '4028b8817cc50161017cc5082b460002');
INSERT INTO wf_form_group VALUES ('4028b8817cc50161017cc531ba050699', '처리내역', '4028b8817cc50161017cc5082b460002');
INSERT INTO wf_form_group VALUES ('4028b8817cc50161017cc531ba5406a9', '승인 / 반려 내역', '4028b8817cc50161017cc5082b460002');
/*어플리케이션 변경관리*/
insert into wf_form_group values('4028b22f7cc55c1a017cc5731f95019d', '공통 그룹', '4028b22f7c9c4aee017c9c4e15870000');
insert into wf_form_group values('4028b22f7cc55c1a017cc57320cc01a6', '신청 내역', '4028b22f7c9c4aee017c9c4e15870000');
insert into wf_form_group values('4028b22f7cc55c1a017cc57324d201c2', '이관문서 검토 / 접수', '4028b22f7c9c4aee017c9c4e15870000');
insert into wf_form_group values('4028b22f7cc55c1a017cc57327e801d8', '개발 계획서', '4028b22f7c9c4aee017c9c4e15870000');
insert into wf_form_group values('4028b22f7cc55c1a017cc5732de30201', '승인 내역', '4028b22f7c9c4aee017c9c4e15870000');
insert into wf_form_group values('4028b22f7cc55c1a017cc5732ece0208', '설계 검증 검토내역', '4028b22f7c9c4aee017c9c4e15870000');
insert into wf_form_group values('4028b22f7cc55c1a017cc5733093020f', '구현 내역', '4028b22f7c9c4aee017c9c4e15870000');
insert into wf_form_group values('4028b22f7cc55c1a017cc5733292021d', '단위 테스트 내역', '4028b22f7c9c4aee017c9c4e15870000');
insert into wf_form_group values('4028b22f7cc55c1a017cc573342c0228', '통합 테스트 내역', '4028b22f7c9c4aee017c9c4e15870000');
insert into wf_form_group values('4028b22f7cc55c1a017cc573361a0236', '현업 테스트 요청', '4028b22f7c9c4aee017c9c4e15870000');
insert into wf_form_group values('4028b22f7cc55c1a017cc5733701023d', '현업테스트', '4028b22f7c9c4aee017c9c4e15870000');
insert into wf_form_group values('4028b22f7cc55c1a017cc573390a024b', '릴리즈 결과 입력', '4028b22f7c9c4aee017c9c4e15870000');
insert into wf_form_group values('4028b22f7cc55c1a017cc5733ac10257', '변경 완료 보고', '4028b22f7c9c4aee017c9c4e15870000');
insert into wf_form_group values('4028b22f7cc55c1a017cc5733c560264', '최종 검토 의견', '4028b22f7c9c4aee017c9c4e15870000');
