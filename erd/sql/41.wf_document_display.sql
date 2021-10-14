/**
 * 문서출력정보
 */
DROP TABLE IF EXISTS wf_document_display cascade;

CREATE TABLE wf_document_display
(
	document_id varchar(128) NOT NULL,
	form_group_id varchar(128) NOT NULL,
	element_id varchar(256) NOT NULL,
	display varchar(100) DEFAULT 'document.displayType.editable' NOT NULL,
	CONSTRAINT wf_document_display_pk PRIMARY KEY (document_id, form_group_id, element_id),
	CONSTRAINT wf_document_display_fk1 FOREIGN KEY (document_id) REFERENCES wf_document (document_id),
	CONSTRAINT wf_document_display_fk2 FOREIGN KEY (form_group_id) REFERENCES wf_form_group (form_group_id),
	CONSTRAINT wf_document_display_fk3 FOREIGN KEY (element_id) REFERENCES wf_element (element_id)
);

COMMENT ON TABLE wf_document_display IS '문서출력정보';
COMMENT ON COLUMN wf_document_display.document_id IS '신청서아이디';
COMMENT ON COLUMN wf_document_display.form_group_id IS '문서그룹아이디';
COMMENT ON COLUMN wf_document_display.element_id IS '엘리먼트아이디';
COMMENT ON COLUMN wf_document_display.display IS '엘리먼트별컴포넌트출력정보';

/* 서비스데스크 - 단순문의 */
INSERT INTO wf_document_display VALUES ('csr0000000000000000000000000001d','g028b21f7c780ba6017c7832447201e1','a55083f4bc4491059cfa4d0fc7dd6b4b','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('csr0000000000000000000000000001d','g028b21f7c780ba6017c7832447201e1','ad120e2076402481c51d6fdc476ab7f1','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('csr0000000000000000000000000001d','g028b21f7c780ba6017c7832447201e1','a2c82c19d4a250782639f98c3861c5b3','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('csr0000000000000000000000000001d','g028b21f7c780ba6017c7832447201e1','ab9d23c048f2a125da0dad37ade2d3c7','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('csr0000000000000000000000000001d','g028b21f7c780ba6017c7832480101e2','a55083f4bc4491059cfa4d0fc7dd6b4b','document.displayType.editable');
INSERT INTO wf_document_display VALUES ('csr0000000000000000000000000001d','g028b21f7c780ba6017c7832480101e2','ad120e2076402481c51d6fdc476ab7f1','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('csr0000000000000000000000000001d','g028b21f7c780ba6017c7832480101e2','a2c82c19d4a250782639f98c3861c5b3','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('csr0000000000000000000000000001d','g028b21f7c780ba6017c7832480101e2','ab9d23c048f2a125da0dad37ade2d3c7','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('csr0000000000000000000000000001d','g028b21f7c780ba6017c7832527e0203','a55083f4bc4491059cfa4d0fc7dd6b4b','document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('csr0000000000000000000000000001d','g028b21f7c780ba6017c7832527e0203','ad120e2076402481c51d6fdc476ab7f1','document.displayType.editable');
INSERT INTO wf_document_display VALUES ('csr0000000000000000000000000001d','g028b21f7c780ba6017c7832527e0203','a2c82c19d4a250782639f98c3861c5b3','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('csr0000000000000000000000000001d','g028b21f7c780ba6017c7832527e0203','ab9d23c048f2a125da0dad37ade2d3c7','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('csr0000000000000000000000000001d','g028b21f7c780ba6017c783258080214','a55083f4bc4491059cfa4d0fc7dd6b4b','document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('csr0000000000000000000000000001d','g028b21f7c780ba6017c783258080214','ad120e2076402481c51d6fdc476ab7f1','document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('csr0000000000000000000000000001d','g028b21f7c780ba6017c783258080214','a2c82c19d4a250782639f98c3861c5b3','document.displayType.editable');
INSERT INTO wf_document_display VALUES ('csr0000000000000000000000000001d','g028b21f7c780ba6017c783258080214','ab9d23c048f2a125da0dad37ade2d3c7','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('csr0000000000000000000000000001d','g028b21f7c780ba6017c78325ca80225','a55083f4bc4491059cfa4d0fc7dd6b4b','document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('csr0000000000000000000000000001d','g028b21f7c780ba6017c78325ca80225','ad120e2076402481c51d6fdc476ab7f1','document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('csr0000000000000000000000000001d','g028b21f7c780ba6017c78325ca80225','a2c82c19d4a250782639f98c3861c5b3','document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('csr0000000000000000000000000001d','g028b21f7c780ba6017c78325ca80225','ab9d23c048f2a125da0dad37ade2d3c7','document.displayType.editable');
/* 서비스데스크 - 단순문의 - 만족도 */
INSERT INTO wf_document_display VALUES ('csr0000000000000000000000000002d','g028b21f7c780ba6017c78334c07023c','a69041dce1c4f1a0d5bee6a380553ae8','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('csr0000000000000000000000000002d','g028b21f7c780ba6017c78334f3a0244','a69041dce1c4f1a0d5bee6a380553ae8','document.displayType.editable');