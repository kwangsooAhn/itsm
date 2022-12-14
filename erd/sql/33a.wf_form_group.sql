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
INSERT INTO wf_form_group VALUES ('g028b21f7ce3c1c2017ce435961500b4','숨김영역','4028b21f7c9698f4017c9731ebae004e');
INSERT INTO wf_form_group VALUES ('g028b21f7c780ba6017c78334f3a0244','만족도평가','4028b21f7c9698f4017c9731ebae004e');
/* 서비스데스크 - 장애신고 */
INSERT INTO wf_form_group VALUES ('4028b21f7c9adb6a017c9b0613610065','제목','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_form_group VALUES ('4028b21f7c9adb6a017c9b061411006d','신청내역','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_form_group VALUES ('4028b21f7c9adb6a017c9b061691008c','접수내역','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_form_group VALUES ('4028b21f7c9adb6a017c9b0617fe009e','처리내역','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_form_group VALUES ('4028b21f7c9adb6a017c9b061a8b00bf','승인 / 반려 내역','4028b21f7c90d996017c91af9dcf0051');
/* 서비스데스크 - 장애신고 - 만족도 */
INSERT INTO wf_form_group VALUES ('4028b21f7c90d996017c914da7aa0021','제목','4028b21f7c90d996017c914bce270002');
INSERT INTO wf_form_group VALUES ('4028b21f7ce3c1c2017ce435961500b5','숨김영역','4028b21f7c90d996017c914bce270002');
INSERT INTO wf_form_group VALUES ('4028b21f7c90d996017c914da8270029','만족도평가','4028b21f7c90d996017c914bce270002');
/* 서비스데스크 - 서비스요청 */
INSERT INTO wf_form_group VALUES('4028b21f7c9ff7c8017ca054465a0001', '제목', '4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_form_group VALUES('4028b21f7c9ff7c8017ca054470f000a', '신청내역', '4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_form_group VALUES('4028b21f7c9ff7c8017ca05448d20027', '접수내역', '4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_form_group VALUES('4028b21f7c9ff7c8017ca05449e00039', '처리내역', '4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_form_group VALUES('4028b21f7c9ff7c8017ca0544af6004d', '승인 / 반려 내역', '4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_form_group VALUES('40288ada7d2b171e017d2b211b3a00f7', '관련CI', '4028b21f7c9cc269017c9cc8cbf60001');
/* 서비스데스크 - 서비스요청 - 만족도 */
INSERT INTO wf_form_group VALUES ('4028b21f7c90d996017c914eec300040','제목','4028b21f7c90d996017c914e27340030');
INSERT INTO wf_form_group VALUES ('4028b21f7ce3c1c2017ce435961500b6','숨김영역','4028b21f7c90d996017c914e27340030');
INSERT INTO wf_form_group VALUES ('4028b21f7c90d996017c914eecb30048','만족도평가','4028b21f7c90d996017c914e27340030');
/* 서비스데스크 - 구성관리 */
INSERT INTO wf_form_group VALUES('2c9180867cc31a25017cc7a68e9204d3', '제목', '2c9180867cc31a25017cc7a069e301a5');
INSERT INTO wf_form_group VALUES('2c9180867cc31a25017cc7a68ea004dc', '신청내역', '2c9180867cc31a25017cc7a069e301a5');
INSERT INTO wf_form_group VALUES('2c9180867cc31a25017cc7a68ece0500', '접수내역', '2c9180867cc31a25017cc7a069e301a5');
INSERT INTO wf_form_group VALUES('2c9180867cc31a25017cc7a68edf050c', '처리내역', '2c9180867cc31a25017cc7a069e301a5');
INSERT INTO wf_form_group VALUES('2c9180867cc31a25017cc7a68eef0519', '승인내역', '2c9180867cc31a25017cc7a069e301a5');
INSERT INTO wf_form_group VALUES('40288ada7d08d6b1017d091dcf4b0261', '대상 CI', '2c9180867cc31a25017cc7a069e301a5');
/* 서비스데스크 - 구성관리 - 만족도 */
INSERT INTO wf_form_group VALUES ('2c9180867cc31a25017cc5c8ad340134', '제목', '2c9180867cc31a25017cc5c7268f0122');
INSERT INTO wf_form_group VALUES ('4028b21f7ce3c1c2017ce435961500b7', '숨김영역','2c9180867cc31a25017cc5c7268f0122');
INSERT INTO wf_form_group VALUES ('2c9180867cc31a25017cc5c8ad4b013d', '만족도평가', '2c9180867cc31a25017cc5c7268f0122');
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
INSERT INTO wf_form_group VALUES('4028b8817cc50161017cc531b8e50661', '제목', '4028b8817cc50161017cc5082b460002');
INSERT INTO wf_form_group VALUES('4028b8817cc50161017cc531b913066b', '신청내역', '4028b8817cc50161017cc5082b460002');
INSERT INTO wf_form_group VALUES('4028b8817cc50161017cc531b9bb068a', '접수내역', '4028b8817cc50161017cc5082b460002');
INSERT INTO wf_form_group VALUES('4028b8817cc50161017cc531ba050699', '처리내역', '4028b8817cc50161017cc5082b460002');
INSERT INTO wf_form_group VALUES('4028b8817cc50161017cc531ba5406a9', '승인 / 반려 내역', '4028b8817cc50161017cc5082b460002');
INSERT INTO wf_form_group VALUES('40288ada7d0d3c49017d0d57be030026', '대상 CI', '4028b8817cc50161017cc5082b460002');
/*어플리케이션 변경관리*/
insert into wf_form_group values('4028b22f7cc55c1a017cc5731f95019d', '공통 그룹', '4028b22f7c9c4aee017c9c4e15870000');
insert into wf_form_group values('4028b22f7cc55c1a017cc57320cc01a6', '신청 내역', '4028b22f7c9c4aee017c9c4e15870000');
insert into wf_form_group values('4028b22f7cc55c1a017cc57324d201c2', '접수내역', '4028b22f7c9c4aee017c9c4e15870000');
insert into wf_form_group values('4028b22f7cc55c1a017cc57327e801d8', '개발 계획서', '4028b22f7c9c4aee017c9c4e15870000');
insert into wf_form_group values('4028b22f7cc55c1a017cc5732de30201', '승인 내역', '4028b22f7c9c4aee017c9c4e15870000');
insert into wf_form_group values('4028b22f7cc55c1a017cc5732ece0208', '설계 검증 검토내역', '4028b22f7c9c4aee017c9c4e15870000');
insert into wf_form_group values('4028b22f7cc55c1a017cc5733093020f', '구현 내역', '4028b22f7c9c4aee017c9c4e15870000');
insert into wf_form_group values('4028b22f7cc55c1a017cc5733292021d', '단위 테스트 내역', '4028b22f7c9c4aee017c9c4e15870000');
insert into wf_form_group values('4028b22f7cc55c1a017cc573342c0228', '통합 테스트 내역', '4028b22f7c9c4aee017c9c4e15870000');
insert into wf_form_group values('4028b22f7cc55c1a017cc573361a0236', '현업 테스트 요청', '4028b22f7c9c4aee017c9c4e15870000');
insert into wf_form_group values('4028b22f7cc55c1a017cc5733701023d', '현업테스트', '4028b22f7c9c4aee017c9c4e15870000');
insert into wf_form_group values('4028b22f7cc55c1a017cc573390a024b', '릴리즈 결과 내역', '4028b22f7c9c4aee017c9c4e15870000');
insert into wf_form_group values('4028b22f7cc55c1a017cc5733ac10257', '변경 완료 보고', '4028b22f7c9c4aee017c9c4e15870000');
insert into wf_form_group values('4028b22f7cc55c1a017cc5733c560264', '최종 검토 의견', '4028b22f7c9c4aee017c9c4e15870000');
/* 문제관리 */
INSERT INTO wf_form_group VALUES ('4028b21c7cdffb67017ce0b45bbb0815', '제목', '4028b21c7cdffb67017ce0b3fce307b7');
INSERT INTO wf_form_group VALUES ('4028b21c7cdffb67017ce0b45c93081e', '신청내역', '4028b21c7cdffb67017ce0b3fce307b7');
INSERT INTO wf_form_group VALUES ('4028b21c7cdffb67017ce0b45ff30843', '접수내역', '4028b21c7cdffb67017ce0b3fce307b7');
INSERT INTO wf_form_group VALUES ('4028b21c7cdffb67017ce0b461580853', '해결내역', '4028b21c7cdffb67017ce0b3fce307b7');
INSERT INTO wf_form_group VALUES ('4028b21c7cdffb67017ce0b463150868', '승인 / 반려 내역', '4028b21c7cdffb67017ce0b3fce307b7');
INSERT INTO wf_form_group VALUES ('a327b427dbe31e0fcea86fef3bd68174', '관련 CI', '4028b21c7cdffb67017ce0b3fce307b7');
/* 장애관리 */
INSERT INTO wf_form_group VALUES ('4028b21c7cdffb67017ce0b6490a08d9', '제목', '4028b21c7cdffb67017ce0b5f9920873');
INSERT INTO wf_form_group VALUES ('4028b21c7cdffb67017ce0b64a0a08e3', '신청내역', '4028b21c7cdffb67017ce0b5f9920873');
INSERT INTO wf_form_group VALUES ('4028b21c7cdffb67017ce0b64d9f090b', '접수내역', '4028b21c7cdffb67017ce0b5f9920873');
INSERT INTO wf_form_group VALUES ('4028b21c7cdffb67017ce0b64ea60917', '처리내역', '4028b21c7cdffb67017ce0b5f9920873');
INSERT INTO wf_form_group VALUES ('4028b21c7cdffb67017ce0b6510d0933', '승인 / 반려 내역', '4028b21c7cdffb67017ce0b5f9920873');
/* 서비스 연속성 관리 계획서 */
INSERT INTO wf_form_group VALUES ('9c8cceb77a20423984bfa4ef7e73d4d1', '제목', '4028b21c8233ff430182340690a20000');
INSERT INTO wf_form_group VALUES ('99ddaf58c42d4d259213b83120714e8e', '신청내역', '4028b21c8233ff430182340690a20000');
INSERT INTO wf_form_group VALUES ('ca60cc72983c4b62beebb4fa22f03402', '승인 / 반려 내역', '4028b21c8233ff430182340690a20000');
/* 복구 훈련 계획서 */
INSERT INTO wf_form_group VALUES ('b9b860237b0e44758d6c28a37d028c4a', '제목', '4028b21c8233ff43018234224021004d');
INSERT INTO wf_form_group VALUES ('22ebaf5b292242219be3999d3fefa23f', '신청내역', '4028b21c8233ff43018234224021004d');
INSERT INTO wf_form_group VALUES ('def5b9a0a7a34b238cbc45d2fe6578b1', '승인 / 반려 내역', '4028b21c8233ff43018234224021004d');
/* 복구 훈련 결과서 */
INSERT INTO wf_form_group VALUES ('bdc95719b39949809fbf2aaa5052224e', '제목', '4028b21c82343bfb018234420f630000');
INSERT INTO wf_form_group VALUES ('03c0b58eeda64bcdb7598b853e0e27f9', '신청내역', '4028b21c82343bfb018234420f630000');
INSERT INTO wf_form_group VALUES ('3cd16ba4f27547a4afaed9e28faeac10', '승인 / 반려 내역', '4028b21c82343bfb018234420f630000');
/* 용량 검토 보고서*/
INSERT INTO wf_form_group VALUES ('12be8c4ab2ea42049c3c9d49c5eadb37', '제목', '40288a9d826b8fbf01826cd3ff160054');
INSERT INTO wf_form_group VALUES ('71f00c59f9cc4a06ac370f7d9470ad95', '작성내역', '40288a9d826b8fbf01826cd3ff160054');
INSERT INTO wf_form_group VALUES ('a25f03ce1c814208a4991667d1d53636', '승인 / 반려 내역', '40288a9d826b8fbf01826cd3ff160054');
/* 용량 조치 보고서 */
INSERT INTO wf_form_group VALUES ('a33304346f2c45ddb202de50cce4efb3', '제목', '40288a9d827b4bf001827b8ede710013');
INSERT INTO wf_form_group VALUES ('a534780c74fc465fbe18383216b0e66b', '조치 내역', '40288a9d827b4bf001827b8ede710013');
INSERT INTO wf_form_group VALUES ('6a763bba33e64614a83a37afdc54d711', '승인 / 반려 내역', '40288a9d827b4bf001827b8ede710013');
