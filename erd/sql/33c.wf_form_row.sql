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
INSERT INTO wf_form_row VALUES ('4028b21f7c9ff7c8017ca05446750002','4028b21f7c9ff7c8017ca054465a0001','{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9ff7c8017ca05446b40005','4028b21f7c9ff7c8017ca054465a0001','{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9ff7c8017ca05446e30008','4028b21f7c9ff7c8017ca054465a0001','{"displayOrder":2,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9ff7c8017ca0544721000b','4028b21f7c9ff7c8017ca054470f000a','{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9ff7c8017ca054477d0010','4028b21f7c9ff7c8017ca054470f000a','{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9ff7c8017ca05447cf0015','4028b21f7c9ff7c8017ca054470f000a','{"displayOrder":2,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9ff7c8017ca0544826001b','4028b21f7c9ff7c8017ca054470f000a','{"displayOrder":3,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9ff7c8017ca0544851001e','4028b21f7c9ff7c8017ca054470f000a','{"displayOrder":4,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9ff7c8017ca054487b0021','4028b21f7c9ff7c8017ca054470f000a','{"displayOrder":5,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9ff7c8017ca05448a50024','4028b21f7c9ff7c8017ca054470f000a','{"displayOrder":6,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9ff7c8017ca05448e20028','4028b21f7c9ff7c8017ca05448d20027','{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9ff7c8017ca0544932002d','4028b21f7c9ff7c8017ca05448d20027','{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9ff7c8017ca054495f0030','4028b21f7c9ff7c8017ca05448d20027','{"displayOrder":2,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9ff7c8017ca054498b0033','4028b21f7c9ff7c8017ca05448d20027','{"displayOrder":3,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9ff7c8017ca05449b50036','4028b21f7c9ff7c8017ca05448d20027','{"displayOrder":4,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9ff7c8017ca05449ef003a','4028b21f7c9ff7c8017ca05449e00039','{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9ff7c8017ca0544a1e003d','4028b21f7c9ff7c8017ca05449e00039','{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9ff7c8017ca0544a490040','4028b21f7c9ff7c8017ca05449e00039','{"displayOrder":2,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9ff7c8017ca0544a760043','4028b21f7c9ff7c8017ca05449e00039','{"displayOrder":3,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9ff7c8017ca0544aa30047','4028b21f7c9ff7c8017ca05449e00039','{"displayOrder":4,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9ff7c8017ca0544ace004a','4028b21f7c9ff7c8017ca05449e00039','{"displayOrder":5,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9ff7c8017ca0544b09004e','4028b21f7c9ff7c8017ca0544af6004d','{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c9ff7c8017ca0544b390053','4028b21f7c9ff7c8017ca0544af6004d','{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
/* 서비스데스크 - 서비스요청 - 만족도 */
INSERT INTO wf_form_row VALUES ('4028b21f7c90d996017c914eec400041','4028b21f7c90d996017c914eec300040','{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c90d996017c914eec670044','4028b21f7c90d996017c914eec300040','{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c90d996017c914eec970047','4028b21f7c90d996017c914eec300040','{"displayOrder":2,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c90d996017c914eecc30049','4028b21f7c90d996017c914eecb30048','{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b21f7c90d996017c914eecf3004c','4028b21f7c90d996017c914eecb30048','{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
/* 인프라변경관리 */
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a56d0ae9', '4028b8817cbfc7a7017cc095a5670ae8', '{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a5800aec', '4028b8817cbfc7a7017cc095a5670ae8', '{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a58d0af0', '4028b8817cbfc7a7017cc095a5670ae8', '{"displayOrder":2,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a5a20af3', '4028b8817cbfc7a7017cc095a59a0af2', '{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a5c30af8', '4028b8817cbfc7a7017cc095a59a0af2', '{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a5e00afd', '4028b8817cbfc7a7017cc095a59a0af2', '{"displayOrder":2,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a5fd0b02', '4028b8817cbfc7a7017cc095a59a0af2', '{"displayOrder":3,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a60a0b05', '4028b8817cbfc7a7017cc095a59a0af2', '{"displayOrder":4,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a61b0b09', '4028b8817cbfc7a7017cc095a59a0af2', '{"displayOrder":5,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a6290b0c', '4028b8817cbfc7a7017cc095a59a0af2', '{"displayOrder":6,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a63b0b10', '4028b8817cbfc7a7017cc095a6360b0f', '{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a64c0b13', '4028b8817cbfc7a7017cc095a6360b0f', '{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a6650b18', '4028b8817cbfc7a7017cc095a6360b0f', '{"displayOrder":2,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a67e0b1d', '4028b8817cbfc7a7017cc095a6360b0f', '{"displayOrder":3,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a6950b22', '4028b8817cbfc7a7017cc095a6360b0f', '{"displayOrder":4,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a6a60b26', '4028b8817cbfc7a7017cc095a6a10b25', '{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a6b40b29', '4028b8817cbfc7a7017cc095a6a10b25', '{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a6c20b2c', '4028b8817cbfc7a7017cc095a6a10b25', '{"displayOrder":2,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a6cf0b2f', '4028b8817cbfc7a7017cc095a6a10b25', '{"displayOrder":3,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a6db0b32', '4028b8817cbfc7a7017cc095a6a10b25', '{"displayOrder":4,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a6f10b36', '4028b8817cbfc7a7017cc095a6eb0b35', '{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a6fd0b37', '4028b8817cbfc7a7017cc095a6eb0b35', '{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a71b0b3c', '4028b8817cbfc7a7017cc095a6eb0b35', '{"displayOrder":2,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a7280b3f', '4028b8817cbfc7a7017cc095a6eb0b35', '{"displayOrder":3,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a7330b42', '4028b8817cbfc7a7017cc095a6eb0b35', '{"displayOrder":4,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a73f0b45', '4028b8817cbfc7a7017cc095a6eb0b35', '{"displayOrder":5,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a74b0b48', '4028b8817cbfc7a7017cc095a6eb0b35', '{"displayOrder":6,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a7570b4b', '4028b8817cbfc7a7017cc095a6eb0b35', '{"displayOrder":7,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a7630b4e', '4028b8817cbfc7a7017cc095a6eb0b35', '{"displayOrder":8,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a7720b51', '4028b8817cbfc7a7017cc095a6eb0b35', '{"displayOrder":9,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a7840b55', '4028b8817cbfc7a7017cc095a77f0b54', '{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a7910b58', '4028b8817cbfc7a7017cc095a77f0b54', '{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a7a30b5c', '4028b8817cbfc7a7017cc095a79f0b5b', '{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a7b00b5f', '4028b8817cbfc7a7017cc095a79f0b5b', '{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a7bd0b62', '4028b8817cbfc7a7017cc095a79f0b5b', '{"displayOrder":2,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a7ca0b65', '4028b8817cbfc7a7017cc095a79f0b5b', '{"displayOrder":3,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a7e00b69', '4028b8817cbfc7a7017cc095a7d90b68', '{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a7f90b6e', '4028b8817cbfc7a7017cc095a7d90b68', '{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a8060b71', '4028b8817cbfc7a7017cc095a7d90b68', '{"displayOrder":2,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a8120b74', '4028b8817cbfc7a7017cc095a7d90b68', '{"displayOrder":3,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a8200b78', '4028b8817cbfc7a7017cc095a81b0b77', '{"displayOrder":0,"margin":"4 0 4 0","padding":"0 0 0 0"}');
INSERT INTO wf_form_row VALUES ('4028b8817cbfc7a7017cc095a82a0b7b', '4028b8817cbfc7a7017cc095a81b0b77', '{"displayOrder":1,"margin":"4 0 4 0","padding":"0 0 0 0"}');
