/**
 * 문서양식 ROW 정보
 */
DROP TABLE IF EXISTS wf_form_row cascade;

CREATE TABLE wf_form_row (
     form_row_id varchar(128) NULL,
     form_group_id varchar(128) NULL,
     row_display_option text NULL,
     CONSTRAINT wf_form_row_pk PRIMARY KEY (form_row_id),
     CONSTRAINT wf_form_row_fk FOREIGN KEY (form_group_id) REFERENCES wf_form_group(form_group_id)
);

COMMENT ON TABLE wf_form_row IS '문서양식 ROW 정보';
COMMENT ON COLUMN wf_form_row.form_row_id IS '문서양식 ROW 아이디';
COMMENT ON COLUMN wf_form_row.form_group_id IS '문서양식 그룹아이디';
COMMENT ON COLUMN wf_form_row.row_display_option IS 'ROW 출력용 속성';

/* 서비스데스크 - 단순문의 */
INSERT INTO wf_form_row VALUES ('r028b21f7c780ba6017c783244ca01e5','g028b21f7c780ba6017c7832447201e1','{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('r028b21f7c780ba6017c783245d801e8','g028b21f7c780ba6017c7832447201e1','{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('r028b21f7c780ba6017c7832470e01eb','g028b21f7c780ba6017c7832447201e1','{"displayOrder":2,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('r028b21f7c780ba6017c7832485c01ed','g028b21f7c780ba6017c7832480101e2','{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('r028b21f7c780ba6017c78324a3801f2','g028b21f7c780ba6017c7832480101e2','{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('r028b21f7c780ba6017c78324c4301f7','g028b21f7c780ba6017c7832480101e2','{"displayOrder":2,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('r028b21f7c780ba6017c78324e2701fc','g028b21f7c780ba6017c7832480101e2','{"displayOrder":3,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('r028b21f7c780ba6017c78324f5001ff','g028b21f7c780ba6017c7832480101e2','{"displayOrder":4,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('r028b21f7c780ba6017c783250730203','g028b21f7c780ba6017c7832480101e2','{"displayOrder":5,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('r028b21f7c780ba6017c783251820206','g028b21f7c780ba6017c7832480101e2','{"displayOrder":6,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('r028b21f7c780ba6017c783252db020a','g028b21f7c780ba6017c7832527e0203','{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('r028b21f7c780ba6017c783254ac020f','g028b21f7c780ba6017c7832527e0203','{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('r028b21f7c780ba6017c783255fa0212','g028b21f7c780ba6017c7832527e0203','{"displayOrder":2,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('r028b21f7c780ba6017c783256e80215','g028b21f7c780ba6017c7832527e0203','{"displayOrder":3,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('r028b21f7c780ba6017c783258800219','g028b21f7c780ba6017c783258080214','{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('r028b21f7c780ba6017c7832599c021c','g028b21f7c780ba6017c783258080214','{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('r028b21f7c780ba6017c78325ab4021f','g028b21f7c780ba6017c783258080214','{"displayOrder":2,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('r028b21f7c780ba6017c78325bc30222','g028b21f7c780ba6017c783258080214','{"displayOrder":3,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('r028b21f7c780ba6017c78325d040226','g028b21f7c780ba6017c78325ca80225','{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('r028b21f7c780ba6017c78325e11022a','g028b21f7c780ba6017c78325ca80225','{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
/* 서비스데스크 - 단순문의 - 만족도 */
INSERT INTO wf_form_row VALUES ('r028b21f7c780ba6017c78334c66023d','g028b21f7c780ba6017c78334c07023c','{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('r028b21f7c780ba6017c78334d6a0240','g028b21f7c780ba6017c78334c07023c','{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('r028b21f7c780ba6017c78334e7b0243','g028b21f7c780ba6017c78334c07023c','{"displayOrder":2,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('r028b21f7c780ba6017c78334fa80245','g028b21f7c780ba6017c78334f3a0244','{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('r028b21f7c780ba6017c783350b00248','g028b21f7c780ba6017c78334f3a0244','{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
/* 서비스데스크 - 장애신고 */
INSERT INTO wf_form_row VALUES ('4028b21f7c9adb6a017c9b0613760066','4028b21f7c9adb6a017c9b0613610065','{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9adb6a017c9b0613b00069','4028b21f7c9adb6a017c9b0613610065','{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9adb6a017c9b0613e5006c','4028b21f7c9adb6a017c9b0613610065','{"displayOrder":2,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9adb6a017c9b061427006e','4028b21f7c9adb6a017c9b061411006d','{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9adb6a017c9b0614970073','4028b21f7c9adb6a017c9b061411006d','{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9adb6a017c9b0615030078','4028b21f7c9adb6a017c9b061411006d','{"displayOrder":2,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9adb6a017c9b061578007e','4028b21f7c9adb6a017c9b061411006d','{"displayOrder":3,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9adb6a017c9b0615df0083','4028b21f7c9adb6a017c9b061411006d','{"displayOrder":4,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9adb6a017c9b06161e0086','4028b21f7c9adb6a017c9b061411006d','{"displayOrder":5,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9adb6a017c9b0616540089','4028b21f7c9adb6a017c9b061411006d','{"displayOrder":6,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9adb6a017c9b0616a9008d','4028b21f7c9adb6a017c9b061691008c','{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9adb6a017c9b0617160092','4028b21f7c9adb6a017c9b061691008c','{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9adb6a017c9b06174d0095','4028b21f7c9adb6a017c9b061691008c','{"displayOrder":2,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9adb6a017c9b0617870098','4028b21f7c9adb6a017c9b061691008c','{"displayOrder":3,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9adb6a017c9b0617c5009b','4028b21f7c9adb6a017c9b061691008c','{"displayOrder":4,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9adb6a017c9b061813009f','4028b21f7c9adb6a017c9b0617fe009e','{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9adb6a017c9b06187e00a4','4028b21f7c9adb6a017c9b0617fe009e','{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9adb6a017c9b0618e600a9','4028b21f7c9adb6a017c9b0617fe009e','{"displayOrder":2,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9adb6a017c9b06192700ac','4028b21f7c9adb6a017c9b0617fe009e','{"displayOrder":3,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9adb6a017c9b06196700af','4028b21f7c9adb6a017c9b0617fe009e','{"displayOrder":4,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9adb6a017c9b06199e00b2','4028b21f7c9adb6a017c9b0617fe009e','{"displayOrder":5,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9adb6a017c9b0619d500b5','4028b21f7c9adb6a017c9b0617fe009e','{"displayOrder":6,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9adb6a017c9b061a1400b9','4028b21f7c9adb6a017c9b0617fe009e','{"displayOrder":7,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9adb6a017c9b061a5600bc','4028b21f7c9adb6a017c9b0617fe009e','{"displayOrder":8,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9adb6a017c9b061a9f00c0','4028b21f7c9adb6a017c9b061a8b00bf','{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9adb6a017c9b061ae500c5','4028b21f7c9adb6a017c9b061a8b00bf','{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
/* 서비스데스크 - 장애신고 - 만족도 */
INSERT INTO wf_form_row VALUES ('4028b21f7c90d996017c914da7b90022','4028b21f7c90d996017c914da7aa0021','{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c90d996017c914da7e10025','4028b21f7c90d996017c914da7aa0021','{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c90d996017c914da8090028','4028b21f7c90d996017c914da7aa0021','{"displayOrder":2,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c90d996017c914da836002a','4028b21f7c90d996017c914da8270029','{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c90d996017c914da85b002d','4028b21f7c90d996017c914da8270029','{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
/* 서비스데스크 - 서비스요청 */

/* 서비스데스크 - 서비스요청 - 만족도 */
INSERT INTO wf_form_row VALUES ('4028b21f7c90d996017c914eec400041','4028b21f7c90d996017c914eec300040','{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c90d996017c914eec670044','4028b21f7c90d996017c914eec300040','{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c90d996017c914eec970047','4028b21f7c90d996017c914eec300040','{"displayOrder":2,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c90d996017c914eecc30049','4028b21f7c90d996017c914eecb30048','{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c90d996017c914eecf3004c','4028b21f7c90d996017c914eecb30048','{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
