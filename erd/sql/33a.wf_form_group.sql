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

/* 서비스데스크 - 서비스요청 - 만족도 */
INSERT INTO wf_form_group VALUES ('4028b21f7c90d996017c914eec300040','제목','4028b21f7c90d996017c914e27340030');
INSERT INTO wf_form_group VALUES ('4028b21f7c90d996017c914eecb30048','만족도평가','4028b21f7c90d996017c914e27340030');
