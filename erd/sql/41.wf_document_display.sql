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
/* 서비스데스크 - 서비스요청 */
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca06bde520058','4028b21f7c9ff7c8017ca054465a0001','cc4048cd712147b8af4016e647e69b14','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca06bde520058','4028b21f7c9ff7c8017ca054465a0001','04195a896b4a4c5f8c2e8d221c536bc8','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca06bde520058','4028b21f7c9ff7c8017ca054465a0001','0c98f0917c7347418933aab46b8b7823','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca06bde520058','4028b21f7c9ff7c8017ca054465a0001','898c51972e9c45e49646c3797165411d','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca06bde520058','4028b21f7c9ff7c8017ca054465a0001','6774c1370d614d47bb3b9b0ec33b9c10','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca06bde520058','4028b21f7c9ff7c8017ca054470f000a','cc4048cd712147b8af4016e647e69b14','document.displayType.editable');
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca06bde520058','4028b21f7c9ff7c8017ca054470f000a','04195a896b4a4c5f8c2e8d221c536bc8','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca06bde520058','4028b21f7c9ff7c8017ca054470f000a','0c98f0917c7347418933aab46b8b7823','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca06bde520058','4028b21f7c9ff7c8017ca054470f000a','898c51972e9c45e49646c3797165411d','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca06bde520058','4028b21f7c9ff7c8017ca054470f000a','6774c1370d614d47bb3b9b0ec33b9c10','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca06bde520058','4028b21f7c9ff7c8017ca05448d20027','cc4048cd712147b8af4016e647e69b14','document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca06bde520058','4028b21f7c9ff7c8017ca05448d20027','04195a896b4a4c5f8c2e8d221c536bc8','document.displayType.editable');
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca06bde520058','4028b21f7c9ff7c8017ca05448d20027','0c98f0917c7347418933aab46b8b7823','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca06bde520058','4028b21f7c9ff7c8017ca05448d20027','898c51972e9c45e49646c3797165411d','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca06bde520058','4028b21f7c9ff7c8017ca05448d20027','6774c1370d614d47bb3b9b0ec33b9c10','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca06bde520058','4028b21f7c9ff7c8017ca05449e00039','cc4048cd712147b8af4016e647e69b14','document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca06bde520058','4028b21f7c9ff7c8017ca05449e00039','04195a896b4a4c5f8c2e8d221c536bc8','document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca06bde520058','4028b21f7c9ff7c8017ca05449e00039','0c98f0917c7347418933aab46b8b7823','document.displayType.editable');
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca06bde520058','4028b21f7c9ff7c8017ca05449e00039','898c51972e9c45e49646c3797165411d','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca06bde520058','4028b21f7c9ff7c8017ca05449e00039','6774c1370d614d47bb3b9b0ec33b9c10','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca06bde520058','4028b21f7c9ff7c8017ca0544af6004d','cc4048cd712147b8af4016e647e69b14','document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca06bde520058','4028b21f7c9ff7c8017ca0544af6004d','04195a896b4a4c5f8c2e8d221c536bc8','document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca06bde520058','4028b21f7c9ff7c8017ca0544af6004d','0c98f0917c7347418933aab46b8b7823','document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca06bde520058','4028b21f7c9ff7c8017ca0544af6004d','898c51972e9c45e49646c3797165411d','document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca06bde520058','4028b21f7c9ff7c8017ca0544af6004d','6774c1370d614d47bb3b9b0ec33b9c10','document.displayType.editable');
/* 서비스데스크 - 서비스요청 - 만족도 */
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca04d16830000','4028b21f7c90d996017c914eec300040','3914bb0a2d32423885ab84339bf391d5','document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b21f7c9ff7c8017ca04d16830000','4028b21f7c90d996017c914eecb30048','3914bb0a2d32423885ab84339bf391d5','document.displayType.editable');
/* 인프라변경관리 */
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a5670ae8', 'c9c7ed6d193b4fd58b726668cbddccae', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a5670ae8', '697ba85cf98c4d9083f83bd5dd66639f', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a5670ae8', '58d6f5e75a5543ab96bc0fa1fe2637c7', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a5670ae8', '4b7e284e81014cfda64de39550895647', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a5670ae8', '8b8564b9f8164971bee112f0a73e66f3', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a5670ae8', 'be6990d4bd1f4c3586df37fe147b2dea', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a5670ae8', '139f55357c1242cc89a0a8162734bae6', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a59a0af2', 'c9c7ed6d193b4fd58b726668cbddccae', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a59a0af2', '697ba85cf98c4d9083f83bd5dd66639f', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a59a0af2', '58d6f5e75a5543ab96bc0fa1fe2637c7', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a59a0af2', '4b7e284e81014cfda64de39550895647', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a59a0af2', '8b8564b9f8164971bee112f0a73e66f3', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a59a0af2', 'be6990d4bd1f4c3586df37fe147b2dea', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a59a0af2', '139f55357c1242cc89a0a8162734bae6', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a6360b0f', 'c9c7ed6d193b4fd58b726668cbddccae', 'document.displayType.editable');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a6360b0f', '697ba85cf98c4d9083f83bd5dd66639f', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a6360b0f', '58d6f5e75a5543ab96bc0fa1fe2637c7', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a6360b0f', '4b7e284e81014cfda64de39550895647', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a6360b0f', '8b8564b9f8164971bee112f0a73e66f3', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a6360b0f', 'be6990d4bd1f4c3586df37fe147b2dea', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a6360b0f', '139f55357c1242cc89a0a8162734bae6', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a6a10b25', 'c9c7ed6d193b4fd58b726668cbddccae', 'document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a6a10b25', '697ba85cf98c4d9083f83bd5dd66639f', 'document.displayType.editable');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a6a10b25', '58d6f5e75a5543ab96bc0fa1fe2637c7', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a6a10b25', '4b7e284e81014cfda64de39550895647', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a6a10b25', '8b8564b9f8164971bee112f0a73e66f3', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a6a10b25', 'be6990d4bd1f4c3586df37fe147b2dea', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a6a10b25', '139f55357c1242cc89a0a8162734bae6', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a6eb0b35', 'c9c7ed6d193b4fd58b726668cbddccae', 'document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a6eb0b35', '697ba85cf98c4d9083f83bd5dd66639f', 'document.displayType.editable');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a6eb0b35', '58d6f5e75a5543ab96bc0fa1fe2637c7', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a6eb0b35', '4b7e284e81014cfda64de39550895647', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a6eb0b35', '8b8564b9f8164971bee112f0a73e66f3', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a6eb0b35', 'be6990d4bd1f4c3586df37fe147b2dea', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a6eb0b35', '139f55357c1242cc89a0a8162734bae6', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a77f0b54', 'c9c7ed6d193b4fd58b726668cbddccae', 'document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a77f0b54', '697ba85cf98c4d9083f83bd5dd66639f', 'document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a77f0b54', '58d6f5e75a5543ab96bc0fa1fe2637c7', 'document.displayType.editable');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a77f0b54', '4b7e284e81014cfda64de39550895647', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a77f0b54', '8b8564b9f8164971bee112f0a73e66f3', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a77f0b54', 'be6990d4bd1f4c3586df37fe147b2dea', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a77f0b54', '139f55357c1242cc89a0a8162734bae6', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a79f0b5b', 'c9c7ed6d193b4fd58b726668cbddccae', 'document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a79f0b5b', '697ba85cf98c4d9083f83bd5dd66639f', 'document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a79f0b5b', '58d6f5e75a5543ab96bc0fa1fe2637c7', 'document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a79f0b5b', '4b7e284e81014cfda64de39550895647', 'document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a79f0b5b', '8b8564b9f8164971bee112f0a73e66f3', 'document.displayType.editable');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a79f0b5b', 'be6990d4bd1f4c3586df37fe147b2dea', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a79f0b5b', '139f55357c1242cc89a0a8162734bae6', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a7d90b68', 'c9c7ed6d193b4fd58b726668cbddccae', 'document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a7d90b68', '697ba85cf98c4d9083f83bd5dd66639f', 'document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a7d90b68', '58d6f5e75a5543ab96bc0fa1fe2637c7', 'document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a7d90b68', '4b7e284e81014cfda64de39550895647', 'document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a7d90b68', '8b8564b9f8164971bee112f0a73e66f3', 'document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a7d90b68', 'be6990d4bd1f4c3586df37fe147b2dea', 'document.displayType.editable');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a7d90b68', '139f55357c1242cc89a0a8162734bae6', 'document.displayType.readonly');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a81b0b77', 'c9c7ed6d193b4fd58b726668cbddccae', 'document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a81b0b77', '697ba85cf98c4d9083f83bd5dd66639f', 'document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a81b0b77', '58d6f5e75a5543ab96bc0fa1fe2637c7', 'document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a81b0b77', '4b7e284e81014cfda64de39550895647', 'document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a81b0b77', '8b8564b9f8164971bee112f0a73e66f3', 'document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a81b0b77', 'be6990d4bd1f4c3586df37fe147b2dea', 'document.displayType.hidden');
INSERT INTO wf_document_display VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', '4028b8817cbfc7a7017cc095a81b0b77', '139f55357c1242cc89a0a8162734bae6', 'document.displayType.editable');
