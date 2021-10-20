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
INSERT INTO wf_document_display VALUES ('4028b21f7c90d996017c91ae7987004f','g028b21f7c780ba6017c7832447201e1','9c7c235aa4eb43d8a912b2e524264c79','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c90d996017c91ae7987004f','g028b21f7c780ba6017c7832447201e1','4be94f828adb4b5a938b82a25feca589','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c90d996017c91ae7987004f','g028b21f7c780ba6017c7832447201e1','53be2caebd5e40e0b6e9ebecce3f16bd','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c90d996017c91ae7987004f','g028b21f7c780ba6017c7832447201e1','337ab138ae9e4250b41be736e0a09c5b','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c90d996017c91ae7987004f','g028b21f7c780ba6017c7832480101e2','9c7c235aa4eb43d8a912b2e524264c79','document.displayType.editable');
INSERT INTO wf_document_display VALUES ('4028b21f7c90d996017c91ae7987004f','g028b21f7c780ba6017c7832480101e2','4be94f828adb4b5a938b82a25feca589','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c90d996017c91ae7987004f','g028b21f7c780ba6017c7832480101e2','53be2caebd5e40e0b6e9ebecce3f16bd','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c90d996017c91ae7987004f','g028b21f7c780ba6017c7832480101e2','337ab138ae9e4250b41be736e0a09c5b','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c90d996017c91ae7987004f','g028b21f7c780ba6017c7832527e0203','9c7c235aa4eb43d8a912b2e524264c79','document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b21f7c90d996017c91ae7987004f','g028b21f7c780ba6017c7832527e0203','4be94f828adb4b5a938b82a25feca589','document.displayType.editable');
INSERT INTO wf_document_display VALUES ('4028b21f7c90d996017c91ae7987004f','g028b21f7c780ba6017c7832527e0203','53be2caebd5e40e0b6e9ebecce3f16bd','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c90d996017c91ae7987004f','g028b21f7c780ba6017c7832527e0203','337ab138ae9e4250b41be736e0a09c5b','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c90d996017c91ae7987004f','g028b21f7c780ba6017c783258080214','9c7c235aa4eb43d8a912b2e524264c79','document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b21f7c90d996017c91ae7987004f','g028b21f7c780ba6017c783258080214','4be94f828adb4b5a938b82a25feca589','document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b21f7c90d996017c91ae7987004f','g028b21f7c780ba6017c783258080214','53be2caebd5e40e0b6e9ebecce3f16bd','document.displayType.editable');
INSERT INTO wf_document_display VALUES ('4028b21f7c90d996017c91ae7987004f','g028b21f7c780ba6017c783258080214','337ab138ae9e4250b41be736e0a09c5b','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c90d996017c91ae7987004f','g028b21f7c780ba6017c78325ca80225','9c7c235aa4eb43d8a912b2e524264c79','document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b21f7c90d996017c91ae7987004f','g028b21f7c780ba6017c78325ca80225','4be94f828adb4b5a938b82a25feca589','document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b21f7c90d996017c91ae7987004f','g028b21f7c780ba6017c78325ca80225','53be2caebd5e40e0b6e9ebecce3f16bd','document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b21f7c90d996017c91ae7987004f','g028b21f7c780ba6017c78325ca80225','337ab138ae9e4250b41be736e0a09c5b','document.displayType.editable');
/* 서비스데스크 - 단순문의 - 만족도 */
INSERT INTO wf_document_display VALUES ('4028b21f7c90d996017c91aeff1b0050','g028b21f7c780ba6017c78334c07023c','00d3cbc448594f90a91448a7eef93dcb','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c90d996017c91aeff1b0050','g028b21f7c780ba6017c78334f3a0244','00d3cbc448594f90a91448a7eef93dcb','document.displayType.editable');
/* 서비스데스크 - 장애신고 */
INSERT INTO wf_document_display VALUES ('4028b21f7c9adb6a017c9b18489900c9','4028b21f7c9adb6a017c9b0613610065','a1c6c91a6dd476ae8ffb5e0c7e9c0d53','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9adb6a017c9b18489900c9','4028b21f7c9adb6a017c9b0613610065','a117f0729b2cd106fa00b36f12fe1cfb','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9adb6a017c9b18489900c9','4028b21f7c9adb6a017c9b0613610065','aea0ee0f94c686307c87a5c86699ef86','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9adb6a017c9b18489900c9','4028b21f7c9adb6a017c9b0613610065','aa900688d722cd451b47e72dda93fca8','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9adb6a017c9b18489900c9','4028b21f7c9adb6a017c9b0613610065','a84a4bafdc3b3f1928a0677434d34feb','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9adb6a017c9b18489900c9','4028b21f7c9adb6a017c9b061411006d','a1c6c91a6dd476ae8ffb5e0c7e9c0d53','document.displayType.editable');
INSERT INTO wf_document_display VALUES ('4028b21f7c9adb6a017c9b18489900c9','4028b21f7c9adb6a017c9b061411006d','a117f0729b2cd106fa00b36f12fe1cfb','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9adb6a017c9b18489900c9','4028b21f7c9adb6a017c9b061411006d','aea0ee0f94c686307c87a5c86699ef86','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9adb6a017c9b18489900c9','4028b21f7c9adb6a017c9b061411006d','aa900688d722cd451b47e72dda93fca8','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9adb6a017c9b18489900c9','4028b21f7c9adb6a017c9b061411006d','a84a4bafdc3b3f1928a0677434d34feb','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9adb6a017c9b18489900c9','4028b21f7c9adb6a017c9b061691008c','a1c6c91a6dd476ae8ffb5e0c7e9c0d53','document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b21f7c9adb6a017c9b18489900c9','4028b21f7c9adb6a017c9b061691008c','a117f0729b2cd106fa00b36f12fe1cfb','document.displayType.editable');
INSERT INTO wf_document_display VALUES ('4028b21f7c9adb6a017c9b18489900c9','4028b21f7c9adb6a017c9b061691008c','aea0ee0f94c686307c87a5c86699ef86','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9adb6a017c9b18489900c9','4028b21f7c9adb6a017c9b061691008c','aa900688d722cd451b47e72dda93fca8','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9adb6a017c9b18489900c9','4028b21f7c9adb6a017c9b061691008c','a84a4bafdc3b3f1928a0677434d34feb','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9adb6a017c9b18489900c9','4028b21f7c9adb6a017c9b0617fe009e','a1c6c91a6dd476ae8ffb5e0c7e9c0d53','document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b21f7c9adb6a017c9b18489900c9','4028b21f7c9adb6a017c9b0617fe009e','a117f0729b2cd106fa00b36f12fe1cfb','document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b21f7c9adb6a017c9b18489900c9','4028b21f7c9adb6a017c9b0617fe009e','aea0ee0f94c686307c87a5c86699ef86','document.displayType.editable');
INSERT INTO wf_document_display VALUES ('4028b21f7c9adb6a017c9b18489900c9','4028b21f7c9adb6a017c9b0617fe009e','aa900688d722cd451b47e72dda93fca8','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9adb6a017c9b18489900c9','4028b21f7c9adb6a017c9b0617fe009e','a84a4bafdc3b3f1928a0677434d34feb','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9adb6a017c9b18489900c9','4028b21f7c9adb6a017c9b061a8b00bf','a1c6c91a6dd476ae8ffb5e0c7e9c0d53','document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b21f7c9adb6a017c9b18489900c9','4028b21f7c9adb6a017c9b061a8b00bf','a117f0729b2cd106fa00b36f12fe1cfb','document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b21f7c9adb6a017c9b18489900c9','4028b21f7c9adb6a017c9b061a8b00bf','aea0ee0f94c686307c87a5c86699ef86','document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b21f7c9adb6a017c9b18489900c9','4028b21f7c9adb6a017c9b061a8b00bf','aa900688d722cd451b47e72dda93fca8','document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b21f7c9adb6a017c9b18489900c9','4028b21f7c9adb6a017c9b061a8b00bf','a84a4bafdc3b3f1928a0677434d34feb','document.displayType.editable');
/* 서비스데스크 - 장애신고 - 만족도 */
INSERT INTO wf_document_display VALUES ('4028b21f7c9b6b1e017c9bedbe8a0012','4028b21f7c90d996017c914da7aa0021','36dad342725848f7ab1453e309ef0aa1','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9b6b1e017c9bedbe8a0012','4028b21f7c90d996017c914da8270029','36dad342725848f7ab1453e309ef0aa1','document.displayType.editable');
