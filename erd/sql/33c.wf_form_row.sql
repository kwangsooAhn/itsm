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
