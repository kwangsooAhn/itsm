/**
 * 문서출력정보
 */
DROP TABLE IF EXISTS wf_document_display cascade;

CREATE TABLE wf_document_display
(
	document_id varchar(128) NOT NULL,
	component_id varchar(128) NOT NULL,
	element_id varchar(256) NOT NULL,
	display varchar(100) DEFAULT 'editable' NOT NULL,
	CONSTRAINT wf_document_display_pk PRIMARY KEY (document_id, component_id, element_id),
	CONSTRAINT wf_document_display_fk1 FOREIGN KEY (document_id) REFERENCES wf_document (document_id),
	CONSTRAINT wf_document_display_fk2 FOREIGN KEY (component_id) REFERENCES wf_component (component_id),
	CONSTRAINT wf_document_display_fk3 FOREIGN KEY (element_id) REFERENCES wf_element (element_id)
);

COMMENT ON TABLE wf_document_display IS '문서출력정보';
COMMENT ON COLUMN wf_document_display.document_id IS '신청서아이디';
COMMENT ON COLUMN wf_document_display.component_id IS '컴포넌트아이디';
COMMENT ON COLUMN wf_document_display.element_id IS '엘리먼트아이디';
COMMENT ON COLUMN wf_document_display.display IS '엘리먼트별컴포넌트출력정보';

insert into wf_document_display values ('40288ab7772ea2e301772ea9aba40003','009d794d7c4e4f9f9aec2be0b4b8c47c','6e6050b6474a40d79aef93d9ef85098a','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ea9aba40003','012b0f6d123b4282a85366e27817d895','6e6050b6474a40d79aef93d9ef85098a','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ea9aba40003','025ab809164c4504a24070406228f5f0','6e6050b6474a40d79aef93d9ef85098a','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ea9aba40003','269d0173006946b0923ce1d09c6a6393','6e6050b6474a40d79aef93d9ef85098a','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ea9aba40003','30f4ab97a0334e7b99e6018af7a8c35f','6e6050b6474a40d79aef93d9ef85098a','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ea9aba40003','3d4a319e7c4c4e64b3bb2c0efe9f6034','6e6050b6474a40d79aef93d9ef85098a','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ea9aba40003','541687276b2443f0b93e3d11dc5b5737','6e6050b6474a40d79aef93d9ef85098a','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ea9aba40003','6db6c1b3e25b494aafb25d2099aa15a1','6e6050b6474a40d79aef93d9ef85098a','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ea9aba40003','7d59614e37564e50af3a9acd91ca8706','6e6050b6474a40d79aef93d9ef85098a','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ea9aba40003','7f55f7817cc94171a5d68e6aab533403','6e6050b6474a40d79aef93d9ef85098a','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ea9aba40003','815ef64cab6b4750a20055f89c644f00','6e6050b6474a40d79aef93d9ef85098a','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ea9aba40003','81806c50ff734d5ea5ddd8ffeebe41e0','6e6050b6474a40d79aef93d9ef85098a','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ea9aba40003','887a8ced40b74c459c9b8c3b5fb15f07','6e6050b6474a40d79aef93d9ef85098a','editableRequired');
insert into wf_document_display values ('40288ab7772ea2e301772ea9aba40003','895c1e72156e4b9f9d0bc011318f899e','6e6050b6474a40d79aef93d9ef85098a','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ea9aba40003','9bde985e28cc41b098437e8eade18265','6e6050b6474a40d79aef93d9ef85098a','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ea9aba40003','a6ce1b0dac7f41e7b0c97ac84a6e086b','6e6050b6474a40d79aef93d9ef85098a','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ea9aba40003','ac12c395142045678a491164d41aff78','6e6050b6474a40d79aef93d9ef85098a','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ea9aba40003','b04f7051d7674a53804f63326e07781e','6e6050b6474a40d79aef93d9ef85098a','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ea9aba40003','b6b12cfda11c4af8a76494e135ca2f8d','6e6050b6474a40d79aef93d9ef85098a','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ea9aba40003','b6cf4e793abc40cc9093b0ea13157d12','6e6050b6474a40d79aef93d9ef85098a','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ea9aba40003','bd38d6e3e9014767a012e2aeaa779539','6e6050b6474a40d79aef93d9ef85098a','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ea9aba40003','d88c58bdb16f47feac9c286100f67faa','6e6050b6474a40d79aef93d9ef85098a','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ea9aba40003','e0720e3eeecd433baf321a836a8e9eb5','6e6050b6474a40d79aef93d9ef85098a','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ea9aba40003','e8c829ae11944852ae70d37a77648edf','6e6050b6474a40d79aef93d9ef85098a','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ea9aba40003','f6a6b980bc134161a09e2e25eeeee4c8','6e6050b6474a40d79aef93d9ef85098a','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','02775b1802734c47908fc3da0c7014a8','8fbd692e22b4428fa9d3192a861a06ea','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','02775b1802734c47908fc3da0c7014a8','3bbb1af5cbe147718e512da2c1b2b458','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','02775b1802734c47908fc3da0c7014a8','317cf6711cae4263b549f937e30db546','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','02775b1802734c47908fc3da0c7014a8','044f83b8d41341839c64bb52d40bfc9b','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','0c262cfa44fe440bb8284978ea8554d1','3bbb1af5cbe147718e512da2c1b2b458','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','0c262cfa44fe440bb8284978ea8554d1','317cf6711cae4263b549f937e30db546','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','0c262cfa44fe440bb8284978ea8554d1','8fbd692e22b4428fa9d3192a861a06ea','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','0c262cfa44fe440bb8284978ea8554d1','044f83b8d41341839c64bb52d40bfc9b','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','1c577578d51d448299eee1c98340a223','8fbd692e22b4428fa9d3192a861a06ea','editableRequired');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','1c577578d51d448299eee1c98340a223','317cf6711cae4263b549f937e30db546','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','1c577578d51d448299eee1c98340a223','044f83b8d41341839c64bb52d40bfc9b','editableRequired');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','1c577578d51d448299eee1c98340a223','3bbb1af5cbe147718e512da2c1b2b458','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','22fa2c689b9d43a986dfd34e197c7f3a','8fbd692e22b4428fa9d3192a861a06ea','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','22fa2c689b9d43a986dfd34e197c7f3a','317cf6711cae4263b549f937e30db546','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','22fa2c689b9d43a986dfd34e197c7f3a','3bbb1af5cbe147718e512da2c1b2b458','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','22fa2c689b9d43a986dfd34e197c7f3a','044f83b8d41341839c64bb52d40bfc9b','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','28c9a8ad4b144cfbbdc27349d855773f','8fbd692e22b4428fa9d3192a861a06ea','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','28c9a8ad4b144cfbbdc27349d855773f','317cf6711cae4263b549f937e30db546','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','28c9a8ad4b144cfbbdc27349d855773f','3bbb1af5cbe147718e512da2c1b2b458','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','28c9a8ad4b144cfbbdc27349d855773f','044f83b8d41341839c64bb52d40bfc9b','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','3132307e44ff4286b92650638afececd','044f83b8d41341839c64bb52d40bfc9b','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','3132307e44ff4286b92650638afececd','3bbb1af5cbe147718e512da2c1b2b458','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','3132307e44ff4286b92650638afececd','317cf6711cae4263b549f937e30db546','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','3132307e44ff4286b92650638afececd','8fbd692e22b4428fa9d3192a861a06ea','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','3b6712c115aa4e648565a4befea8ccd3','044f83b8d41341839c64bb52d40bfc9b','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','3b6712c115aa4e648565a4befea8ccd3','3bbb1af5cbe147718e512da2c1b2b458','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','3b6712c115aa4e648565a4befea8ccd3','317cf6711cae4263b549f937e30db546','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','3b6712c115aa4e648565a4befea8ccd3','8fbd692e22b4428fa9d3192a861a06ea','editableRequired');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','4472820c4c4949258e8af5374ca15ff3','8fbd692e22b4428fa9d3192a861a06ea','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','4472820c4c4949258e8af5374ca15ff3','044f83b8d41341839c64bb52d40bfc9b','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','4472820c4c4949258e8af5374ca15ff3','3bbb1af5cbe147718e512da2c1b2b458','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','4472820c4c4949258e8af5374ca15ff3','317cf6711cae4263b549f937e30db546','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','47dc200d5a564d46bd1ac5cfc1d76187','3bbb1af5cbe147718e512da2c1b2b458','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','47dc200d5a564d46bd1ac5cfc1d76187','044f83b8d41341839c64bb52d40bfc9b','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','47dc200d5a564d46bd1ac5cfc1d76187','8fbd692e22b4428fa9d3192a861a06ea','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','47dc200d5a564d46bd1ac5cfc1d76187','317cf6711cae4263b549f937e30db546','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','4c82676d6321443a8ea6f38161ceff2e','3bbb1af5cbe147718e512da2c1b2b458','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','4c82676d6321443a8ea6f38161ceff2e','044f83b8d41341839c64bb52d40bfc9b','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','4c82676d6321443a8ea6f38161ceff2e','8fbd692e22b4428fa9d3192a861a06ea','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','4c82676d6321443a8ea6f38161ceff2e','317cf6711cae4263b549f937e30db546','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','560cb3baf8f64146b8d6a5f8c3124d63','317cf6711cae4263b549f937e30db546','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','560cb3baf8f64146b8d6a5f8c3124d63','044f83b8d41341839c64bb52d40bfc9b','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','560cb3baf8f64146b8d6a5f8c3124d63','8fbd692e22b4428fa9d3192a861a06ea','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','560cb3baf8f64146b8d6a5f8c3124d63','3bbb1af5cbe147718e512da2c1b2b458','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','5d80057a921040669b4a5be53ea57304','044f83b8d41341839c64bb52d40bfc9b','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','5d80057a921040669b4a5be53ea57304','8fbd692e22b4428fa9d3192a861a06ea','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','5d80057a921040669b4a5be53ea57304','317cf6711cae4263b549f937e30db546','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','5d80057a921040669b4a5be53ea57304','3bbb1af5cbe147718e512da2c1b2b458','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','61b3d4683993430a9cb03ab9cb088fef','3bbb1af5cbe147718e512da2c1b2b458','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','61b3d4683993430a9cb03ab9cb088fef','044f83b8d41341839c64bb52d40bfc9b','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','61b3d4683993430a9cb03ab9cb088fef','8fbd692e22b4428fa9d3192a861a06ea','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','61b3d4683993430a9cb03ab9cb088fef','317cf6711cae4263b549f937e30db546','editableRequired');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','786337268d7745c9ab06e54a0f01a23a','3bbb1af5cbe147718e512da2c1b2b458','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','786337268d7745c9ab06e54a0f01a23a','044f83b8d41341839c64bb52d40bfc9b','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','786337268d7745c9ab06e54a0f01a23a','8fbd692e22b4428fa9d3192a861a06ea','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','786337268d7745c9ab06e54a0f01a23a','317cf6711cae4263b549f937e30db546','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','7b8c641a13624211830b9c466f217985','3bbb1af5cbe147718e512da2c1b2b458','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','7b8c641a13624211830b9c466f217985','317cf6711cae4263b549f937e30db546','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','7b8c641a13624211830b9c466f217985','8fbd692e22b4428fa9d3192a861a06ea','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','7b8c641a13624211830b9c466f217985','044f83b8d41341839c64bb52d40bfc9b','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','7ee30588d4a8430ab249fd95f9fe0890','044f83b8d41341839c64bb52d40bfc9b','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','7ee30588d4a8430ab249fd95f9fe0890','3bbb1af5cbe147718e512da2c1b2b458','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','7ee30588d4a8430ab249fd95f9fe0890','317cf6711cae4263b549f937e30db546','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','7ee30588d4a8430ab249fd95f9fe0890','8fbd692e22b4428fa9d3192a861a06ea','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','8cab0f7cd82a4873b4060d243cf1de48','3bbb1af5cbe147718e512da2c1b2b458','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','8cab0f7cd82a4873b4060d243cf1de48','044f83b8d41341839c64bb52d40bfc9b','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','8cab0f7cd82a4873b4060d243cf1de48','8fbd692e22b4428fa9d3192a861a06ea','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','8cab0f7cd82a4873b4060d243cf1de48','317cf6711cae4263b549f937e30db546','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','96a8288e0cf349b6bc4609ccd45a069c','8fbd692e22b4428fa9d3192a861a06ea','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','96a8288e0cf349b6bc4609ccd45a069c','3bbb1af5cbe147718e512da2c1b2b458','editableRequired');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','96a8288e0cf349b6bc4609ccd45a069c','044f83b8d41341839c64bb52d40bfc9b','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','96a8288e0cf349b6bc4609ccd45a069c','317cf6711cae4263b549f937e30db546','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','9b986ab955324ab481a464a0820d9f44','044f83b8d41341839c64bb52d40bfc9b','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','9b986ab955324ab481a464a0820d9f44','8fbd692e22b4428fa9d3192a861a06ea','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','9b986ab955324ab481a464a0820d9f44','317cf6711cae4263b549f937e30db546','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','9b986ab955324ab481a464a0820d9f44','3bbb1af5cbe147718e512da2c1b2b458','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','9cfae02dcf344b049da289b62add4080','3bbb1af5cbe147718e512da2c1b2b458','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','9cfae02dcf344b049da289b62add4080','044f83b8d41341839c64bb52d40bfc9b','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','9cfae02dcf344b049da289b62add4080','8fbd692e22b4428fa9d3192a861a06ea','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','9cfae02dcf344b049da289b62add4080','317cf6711cae4263b549f937e30db546','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','a924fa7f6fd946298f92e2589a3a81f9','3bbb1af5cbe147718e512da2c1b2b458','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','a924fa7f6fd946298f92e2589a3a81f9','044f83b8d41341839c64bb52d40bfc9b','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','a924fa7f6fd946298f92e2589a3a81f9','8fbd692e22b4428fa9d3192a861a06ea','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','a924fa7f6fd946298f92e2589a3a81f9','317cf6711cae4263b549f937e30db546','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','bc775676f79d4a2b8c1b3a239b101ac5','317cf6711cae4263b549f937e30db546','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','bc775676f79d4a2b8c1b3a239b101ac5','044f83b8d41341839c64bb52d40bfc9b','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','bc775676f79d4a2b8c1b3a239b101ac5','3bbb1af5cbe147718e512da2c1b2b458','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','bc775676f79d4a2b8c1b3a239b101ac5','8fbd692e22b4428fa9d3192a861a06ea','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','bffef2192206490c8ae34d1162a7013f','3bbb1af5cbe147718e512da2c1b2b458','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','bffef2192206490c8ae34d1162a7013f','8fbd692e22b4428fa9d3192a861a06ea','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','bffef2192206490c8ae34d1162a7013f','317cf6711cae4263b549f937e30db546','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','bffef2192206490c8ae34d1162a7013f','044f83b8d41341839c64bb52d40bfc9b','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','d89d0854c1a84472af43fdc50b28c68e','044f83b8d41341839c64bb52d40bfc9b','editableRequired');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','d89d0854c1a84472af43fdc50b28c68e','317cf6711cae4263b549f937e30db546','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','d89d0854c1a84472af43fdc50b28c68e','8fbd692e22b4428fa9d3192a861a06ea','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','d89d0854c1a84472af43fdc50b28c68e','3bbb1af5cbe147718e512da2c1b2b458','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','d9a167efd5c2406a92adf82e6363bff9','3bbb1af5cbe147718e512da2c1b2b458','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','d9a167efd5c2406a92adf82e6363bff9','317cf6711cae4263b549f937e30db546','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','d9a167efd5c2406a92adf82e6363bff9','044f83b8d41341839c64bb52d40bfc9b','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','d9a167efd5c2406a92adf82e6363bff9','8fbd692e22b4428fa9d3192a861a06ea','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','dba870b223974a3b8287f5f2537940cc','3bbb1af5cbe147718e512da2c1b2b458','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','dba870b223974a3b8287f5f2537940cc','044f83b8d41341839c64bb52d40bfc9b','editableRequired');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','dba870b223974a3b8287f5f2537940cc','8fbd692e22b4428fa9d3192a861a06ea','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','dba870b223974a3b8287f5f2537940cc','317cf6711cae4263b549f937e30db546','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','df9e44a1f03941c7b69135382e99b52d','044f83b8d41341839c64bb52d40bfc9b','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','df9e44a1f03941c7b69135382e99b52d','8fbd692e22b4428fa9d3192a861a06ea','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','df9e44a1f03941c7b69135382e99b52d','317cf6711cae4263b549f937e30db546','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','df9e44a1f03941c7b69135382e99b52d','3bbb1af5cbe147718e512da2c1b2b458','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','e83b4c632b264606a56012be262ec2e9','3bbb1af5cbe147718e512da2c1b2b458','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','e83b4c632b264606a56012be262ec2e9','044f83b8d41341839c64bb52d40bfc9b','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','e83b4c632b264606a56012be262ec2e9','8fbd692e22b4428fa9d3192a861a06ea','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','e83b4c632b264606a56012be262ec2e9','317cf6711cae4263b549f937e30db546','editable');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','feada594a34f4b268aab37eb9211e740','8fbd692e22b4428fa9d3192a861a06ea','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','feada594a34f4b268aab37eb9211e740','044f83b8d41341839c64bb52d40bfc9b','hidden');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','feada594a34f4b268aab37eb9211e740','3bbb1af5cbe147718e512da2c1b2b458','readonly');
insert into wf_document_display values ('40288ab7772ea2e301772ead51fe0005','feada594a34f4b268aab37eb9211e740','317cf6711cae4263b549f937e30db546','editableRequired');