/**
 * 
 */
DROP TABLE IF EXISTS wf_component_data cascade;

CREATE TABLE wf_component_data
(
	component_id varchar(128) NOT NULL,
	attribute_id varchar(100) NOT NULL,
	attribute_value text NOT NULL,
	attribute_order int,
	CONSTRAINT wf_component_data_pk PRIMARY KEY (component_id, attribute_id),
	CONSTRAINT wf_component_data_fk FOREIGN KEY (component_id) REFERENCES wf_component (component_id)
);

COMMENT ON TABLE wf_component_data IS '컴포넌트세부설정';
COMMENT ON COLUMN wf_component_data.component_id IS '컴포넌트아이디';
COMMENT ON COLUMN wf_component_data.attribute_id IS '속성아이디';
COMMENT ON COLUMN wf_component_data.attribute_value IS '속성값';
COMMENT ON COLUMN wf_component_data.attribute_order IS '속성순서';

insert into wf_component_data values ('009d794d7c4e4f9f9aec2be0b4b8c47c','display','{"column":"10","customCode":"40288a19736b46fb01736b89e46c0008","default":"none","buttonText":"검색","order":11}');
insert into wf_component_data values ('009d794d7c4e4f9f9aec2be0b4b8c47c','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"담당자"}');
insert into wf_component_data values ('012b0f6d123b4282a85366e27817d895','label','{"size":"20","color":"rgba(63,75,86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"만족도 내역"}');
insert into wf_component_data values ('012b0f6d123b4282a85366e27817d895','display','{"startId":"81806c50ff734d5ea5ddd8ffeebe41e0","thickness":"1","color":"rgba(235, 235, 235, 1)","order":25}');
insert into wf_component_data values ('025ab809164c4504a24070406228f5f0','display','{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":23}');
insert into wf_component_data values ('025ab809164c4504a24070406228f5f0','validate','{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('025ab809164c4504a24070406228f5f0','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"평가의견"}');
insert into wf_component_data values ('02775b1802734c47908fc3da0c7014a8','label','{"size":"20","color":"rgba(40,50,56,1)","bold":"Y","italic":"N","underline":"N","align":"left","text":"요청 내역"}');
insert into wf_component_data values ('02775b1802734c47908fc3da0c7014a8','display','{"startId":"7ee30588d4a8430ab249fd95f9fe0890","thickness":"1","color":"rgba(235, 235, 235, 1)","order":15}');
insert into wf_component_data values ('0c262cfa44fe440bb8284978ea8554d1','display','{"thickness":"5","color":"rgba(88,104,114,1)","type":"solid","order":2}');
insert into wf_component_data values ('1c577578d51d448299eee1c98340a223','validate','{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('1c577578d51d448299eee1c98340a223','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"완료희망일시"}');
insert into wf_component_data values ('1c577578d51d448299eee1c98340a223','display','{"column":"10","default":"now","order":10}');
insert into wf_component_data values ('22fa2c689b9d43a986dfd34e197c7f3a','validate','{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"}');
insert into wf_component_data values ('22fa2c689b9d43a986dfd34e197c7f3a','display','{"placeholder":"","column":"10","default":"select|userName|이름","order":6}');
insert into wf_component_data values ('22fa2c689b9d43a986dfd34e197c7f3a','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"신청자"}');
insert into wf_component_data values ('269d0173006946b0923ce1d09c6a6393','display','{"thickness":"5","color":"rgba(88,104,114,1)","type":"solid","order":2}');
insert into wf_component_data values ('28c9a8ad4b144cfbbdc27349d855773f','validate','{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('28c9a8ad4b144cfbbdc27349d855773f','display','{"column":"10","default":"now","order":21}');
insert into wf_component_data values ('28c9a8ad4b144cfbbdc27349d855773f','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"처리일시"}');
insert into wf_component_data values ('30f4ab97a0334e7b99e6018af7a8c35f','display','{"column":"4","order":9}');
insert into wf_component_data values ('30f4ab97a0334e7b99e6018af7a8c35f','option','[{"seq":"1","name":"사무기기 사용문의","value":"oe"},{"seq":"2","name":"서비스요청 처리현황 문의","value":"sr"},{"seq":"3","name":"업무시스템 사용문의","value":"bs"},{"seq":"4","name":"불만접수","value":"di"},{"seq":"5","name":"기타","value":"etc"}]');
insert into wf_component_data values ('30f4ab97a0334e7b99e6018af7a8c35f','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"문의유형"}');
insert into wf_component_data values ('3132307e44ff4286b92650638afececd','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"첨부파일"}');
insert into wf_component_data values ('3132307e44ff4286b92650638afececd','display','{"column":"10","order":14}');
insert into wf_component_data values ('3b6712c115aa4e648565a4befea8ccd3','label','{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"접수 내역"}');
insert into wf_component_data values ('3b6712c115aa4e648565a4befea8ccd3','display','{"endId":"d9a167efd5c2406a92adf82e6363bff9","thickness":"1","color":"rgba(235, 235, 235, 1)","order":16}');
insert into wf_component_data values ('3d4a319e7c4c4e64b3bb2c0efe9f6034','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"완료희망일시"}');
insert into wf_component_data values ('3d4a319e7c4c4e64b3bb2c0efe9f6034','display','{"column":"10","default":"now","order":10}');
insert into wf_component_data values ('3d4a319e7c4c4e64b3bb2c0efe9f6034','validate','{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('4472820c4c4949258e8af5374ca15ff3','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"신청자아이디"}');
insert into wf_component_data values ('4472820c4c4949258e8af5374ca15ff3','validate','{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"}');
insert into wf_component_data values ('4472820c4c4949258e8af5374ca15ff3','display','{"placeholder":"","column":"10","default":"select|userId|아이디","order":5}');
insert into wf_component_data values ('47dc200d5a564d46bd1ac5cfc1d76187','display','{"column":"10","default":"now","order":17}');
insert into wf_component_data values ('47dc200d5a564d46bd1ac5cfc1d76187','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"접수일시"}');
insert into wf_component_data values ('47dc200d5a564d46bd1ac5cfc1d76187','validate','{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('4c82676d6321443a8ea6f38161ceff2e','display','{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":13}');
insert into wf_component_data values ('4c82676d6321443a8ea6f38161ceff2e','validate','{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('4c82676d6321443a8ea6f38161ceff2e','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"문의내용"}');
insert into wf_component_data values ('541687276b2443f0b93e3d11dc5b5737','option','[{"seq":"1","name":"홈페이지","value":"homepage"},{"seq":"2","name":"전자결재시스템","value":"eps"},{"seq":"3","name":"출입관리시스템","value":"acs"},{"seq":"4","name":"ERP","value":"erp"},{"seq":"5","name":"ITSM","value":"itsm"},{"seq":"6","name":"기타","value":"etd"}]');
insert into wf_component_data values ('541687276b2443f0b93e3d11dc5b5737','display','{"column":"4","order":8}');
insert into wf_component_data values ('541687276b2443f0b93e3d11dc5b5737','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"관련서비스"}');
insert into wf_component_data values ('560cb3baf8f64146b8d6a5f8c3124d63','display','{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":18}');
insert into wf_component_data values ('560cb3baf8f64146b8d6a5f8c3124d63','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"접수의견"}');
insert into wf_component_data values ('560cb3baf8f64146b8d6a5f8c3124d63','validate','{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('5d80057a921040669b4a5be53ea57304','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"관련서비스"}');
insert into wf_component_data values ('5d80057a921040669b4a5be53ea57304','option','[{"seq":"1","name":"홈페이지","value":"homepage"},{"seq":"2","name":"전자결재시스템","value":"eps"},{"seq":"3","name":"출입관리시스템","value":"acs"},{"seq":"4","name":"ERP","value":"erp"},{"seq":"5","name":"ITSM","value":"itsm"},{"seq":"6","name":"기타","value":"etd"}]');
insert into wf_component_data values ('5d80057a921040669b4a5be53ea57304','display','{"column":"4","order":8}');
insert into wf_component_data values ('61b3d4683993430a9cb03ab9cb088fef','display','{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":22}');
insert into wf_component_data values ('61b3d4683993430a9cb03ab9cb088fef','validate','{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('61b3d4683993430a9cb03ab9cb088fef','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"문의답변"}');
insert into wf_component_data values ('6db6c1b3e25b494aafb25d2099aa15a1','display','{"placeholder":"","column":"10","default":"none|","order":12}');
insert into wf_component_data values ('6db6c1b3e25b494aafb25d2099aa15a1','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"제목"}');
insert into wf_component_data values ('6db6c1b3e25b494aafb25d2099aa15a1','validate','{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"}');
insert into wf_component_data values ('786337268d7745c9ab06e54a0f01a23a','display','{"placeholder":"","column":"10","default":"select|department|부서","order":7}');
insert into wf_component_data values ('786337268d7745c9ab06e54a0f01a23a','validate','{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"}');
insert into wf_component_data values ('786337268d7745c9ab06e54a0f01a23a','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"신청부서"}');
insert into wf_component_data values ('7b8c641a13624211830b9c466f217985','option','[{"seq":"1","name":"사무기기 사용문의","value":"oe"},{"seq":"2","name":"서비스요청 처리현황 문의","value":"sr"},{"seq":"3","name":"업무시스템 사용문의","value":"bs"},{"seq":"4","name":"불만접수","value":"di"},{"seq":"5","name":"기타","value":"etc"}]');
insert into wf_component_data values ('7b8c641a13624211830b9c466f217985','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"문의유형"}');
insert into wf_component_data values ('7b8c641a13624211830b9c466f217985','display','{"column":"4","order":9}');
insert into wf_component_data values ('7d59614e37564e50af3a9acd91ca8706','display','{"placeholder":"","column":"10","default":"select|department|부서","order":7}');
insert into wf_component_data values ('7d59614e37564e50af3a9acd91ca8706','validate','{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"}');
insert into wf_component_data values ('7d59614e37564e50af3a9acd91ca8706','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"신청부서"}');
insert into wf_component_data values ('7ee30588d4a8430ab249fd95f9fe0890','display','{"endId":"02775b1802734c47908fc3da0c7014a8","thickness":"1","color":"rgba(235, 235, 235, 1)","order":3}');
insert into wf_component_data values ('7ee30588d4a8430ab249fd95f9fe0890','label','{"size":"20","color":"rgba(40,50,56,1)","bold":"Y","italic":"N","underline":"N","align":"left","text":"요청 내역"}');
insert into wf_component_data values ('7f55f7817cc94171a5d68e6aab533403','display','{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":18}');
insert into wf_component_data values ('7f55f7817cc94171a5d68e6aab533403','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"답변내용"}');
insert into wf_component_data values ('7f55f7817cc94171a5d68e6aab533403','validate','{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('815ef64cab6b4750a20055f89c644f00','label','{"size":"20","color":"rgba(40,50,56,1)","bold":"Y","italic":"N","underline":"N","align":"left","text":"요청 내역"}');
insert into wf_component_data values ('815ef64cab6b4750a20055f89c644f00','display','{"startId":"d88c58bdb16f47feac9c286100f67faa","thickness":"1","color":"rgba(235, 235, 235, 1)","order":15}');
insert into wf_component_data values ('81806c50ff734d5ea5ddd8ffeebe41e0','label','{"size":"20","color":"rgba(63,75,86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"만족도 내역"}');
insert into wf_component_data values ('81806c50ff734d5ea5ddd8ffeebe41e0','display','{"endId":"012b0f6d123b4282a85366e27817d895","thickness":"1","color":"rgba(235, 235, 235, 1)","order":21}');
insert into wf_component_data values ('887a8ced40b74c459c9b8c3b5fb15f07','display','{"column":"10","direction":"horizontal","position":"right","order":22}');
insert into wf_component_data values ('887a8ced40b74c459c9b8c3b5fb15f07','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"만족도"}');
insert into wf_component_data values ('887a8ced40b74c459c9b8c3b5fb15f07','option','[{"seq":"1","name":"매우만족","value":"5"},{"seq":"2","name":"만족","value":"4"},{"seq":"3","name":"보통","value":"3"},{"seq":"4","name":"불만족","value":"2"},{"seq":"5","name":"매우불만족","value":"1"}]');
insert into wf_component_data values ('895c1e72156e4b9f9d0bc011318f899e','validate','{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"}');
insert into wf_component_data values ('895c1e72156e4b9f9d0bc011318f899e','display','{"placeholder":"","column":"10","default":"select|userName|이름","order":6}');
insert into wf_component_data values ('895c1e72156e4b9f9d0bc011318f899e','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"신청자"}');
insert into wf_component_data values ('8cab0f7cd82a4873b4060d243cf1de48','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"승인일시"}');
insert into wf_component_data values ('8cab0f7cd82a4873b4060d243cf1de48','validate','{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('8cab0f7cd82a4873b4060d243cf1de48','display','{"column":"10","default":"now","order":27}');
insert into wf_component_data values ('96a8288e0cf349b6bc4609ccd45a069c','display','{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":28}');
insert into wf_component_data values ('96a8288e0cf349b6bc4609ccd45a069c','label','{"position":"left","column":"2","size":"16","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"승인의견"}');
insert into wf_component_data values ('96a8288e0cf349b6bc4609ccd45a069c','validate','{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('9b986ab955324ab481a464a0820d9f44','display','{"size":"40","color":"rgba(52,152,219,1)","bold":"Y","italic":"N","underline":"N","align":"left","text":"단순문의","order":1}');
insert into wf_component_data values ('9bde985e28cc41b098437e8eade18265','validate','{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('9bde985e28cc41b098437e8eade18265','display','{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":13}');
insert into wf_component_data values ('9bde985e28cc41b098437e8eade18265','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"문의내용"}');
insert into wf_component_data values ('9cfae02dcf344b049da289b62add4080','display','{"startId":"bffef2192206490c8ae34d1162a7013f","thickness":"1","color":"rgba(235, 235, 235, 1)","order":25}');
insert into wf_component_data values ('9cfae02dcf344b049da289b62add4080','label','{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"처리 내역"}');
insert into wf_component_data values ('a6ce1b0dac7f41e7b0c97ac84a6e086b','display','{"size":"40","color":"rgba(52,152,219,1)","bold":"Y","italic":"N","underline":"N","align":"left","text":"단순문의 만족도 평가","order":1}');
insert into wf_component_data values ('a924fa7f6fd946298f92e2589a3a81f9','validate','{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('a924fa7f6fd946298f92e2589a3a81f9','display','{"column":"10","default":"now","order":4}');
insert into wf_component_data values ('a924fa7f6fd946298f92e2589a3a81f9','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"신청일시"}');
insert into wf_component_data values ('ac12c395142045678a491164d41aff78','display','{"column":"10","order":14}');
insert into wf_component_data values ('ac12c395142045678a491164d41aff78','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"첨부파일"}');
insert into wf_component_data values ('b04f7051d7674a53804f63326e07781e','display','{"column":"10","default":"now","order":17}');
insert into wf_component_data values ('b04f7051d7674a53804f63326e07781e','validate','{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('b04f7051d7674a53804f63326e07781e','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"답변일시"}');
insert into wf_component_data values ('b6b12cfda11c4af8a76494e135ca2f8d','display','{"column":"10","order":19}');
insert into wf_component_data values ('b6b12cfda11c4af8a76494e135ca2f8d','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"첨부파일"}');
insert into wf_component_data values ('b6cf4e793abc40cc9093b0ea13157d12','display','{"endId":"e0720e3eeecd433baf321a836a8e9eb5","thickness":"1","color":"rgba(235, 235, 235, 1)","order":16}');
insert into wf_component_data values ('b6cf4e793abc40cc9093b0ea13157d12','label','{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"답변 내역"}');
insert into wf_component_data values ('bc775676f79d4a2b8c1b3a239b101ac5','label','{"size":"20","color":"rgba(63,75,86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"승인 내역"}');
insert into wf_component_data values ('bc775676f79d4a2b8c1b3a239b101ac5','display','{"startId":"df9e44a1f03941c7b69135382e99b52d","thickness":"1","color":"rgba(235, 235, 235, 1)","order":29}');
insert into wf_component_data values ('bd38d6e3e9014767a012e2aeaa779539','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"신청일시"}');
insert into wf_component_data values ('bd38d6e3e9014767a012e2aeaa779539','display','{"column":"10","default":"now","order":4}');
insert into wf_component_data values ('bd38d6e3e9014767a012e2aeaa779539','validate','{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('bffef2192206490c8ae34d1162a7013f','display','{"endId":"9cfae02dcf344b049da289b62add4080","thickness":"1","color":"rgba(235, 235, 235, 1)","order":20}');
insert into wf_component_data values ('bffef2192206490c8ae34d1162a7013f','label','{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"처리 내역"}');
insert into wf_component_data values ('d88c58bdb16f47feac9c286100f67faa','display','{"endId":"815ef64cab6b4750a20055f89c644f00","thickness":"1","color":"rgba(235, 235, 235, 1)","order":3}');
insert into wf_component_data values ('d88c58bdb16f47feac9c286100f67faa','label','{"size":"20","color":"rgba(40,50,56,1)","bold":"Y","italic":"N","underline":"N","align":"left","text":"요청 내역"}');
insert into wf_component_data values ('d89d0854c1a84472af43fdc50b28c68e','display','{"placeholder":"","column":"10","default":"none|","order":12}');
insert into wf_component_data values ('d89d0854c1a84472af43fdc50b28c68e','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"제목"}');
insert into wf_component_data values ('d89d0854c1a84472af43fdc50b28c68e','validate','{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"}');
insert into wf_component_data values ('d9a167efd5c2406a92adf82e6363bff9','display','{"startId":"3b6712c115aa4e648565a4befea8ccd3","thickness":"1","color":"rgba(235, 235, 235, 1)","order":19}');
insert into wf_component_data values ('d9a167efd5c2406a92adf82e6363bff9','label','{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"접수 내역"}');
insert into wf_component_data values ('dba870b223974a3b8287f5f2537940cc','display','{"column":"10","customCode":"40288a19736b46fb01736b89e46c0008","default":"none","buttonText":"검색","order":11}');
insert into wf_component_data values ('dba870b223974a3b8287f5f2537940cc','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"담당자"}');
insert into wf_component_data values ('df9e44a1f03941c7b69135382e99b52d','display','{"endId":"bc775676f79d4a2b8c1b3a239b101ac5","thickness":"1","color":"rgba(235, 235, 235, 1)","order":26}');
insert into wf_component_data values ('df9e44a1f03941c7b69135382e99b52d','label','{"size":"20","color":"rgba(63,75,86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"승인 내역"}');
insert into wf_component_data values ('e0720e3eeecd433baf321a836a8e9eb5','display','{"startId":"b6cf4e793abc40cc9093b0ea13157d12","thickness":"1","color":"rgba(235, 235, 235, 1)","order":20}');
insert into wf_component_data values ('e0720e3eeecd433baf321a836a8e9eb5','label','{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"답변 내역"}');
insert into wf_component_data values ('e83b4c632b264606a56012be262ec2e9','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"첨부파일"}');
insert into wf_component_data values ('e83b4c632b264606a56012be262ec2e9','display','{"column":"10","order":23}');
insert into wf_component_data values ('e8c829ae11944852ae70d37a77648edf','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"신청자아이디"}');
insert into wf_component_data values ('e8c829ae11944852ae70d37a77648edf','validate','{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"}');
insert into wf_component_data values ('e8c829ae11944852ae70d37a77648edf','display','{"placeholder":"","column":"10","default":"none|","order":5}');
insert into wf_component_data values ('f6a6b980bc134161a09e2e25eeeee4c8','display','{"column":"10","order":24}');
insert into wf_component_data values ('f6a6b980bc134161a09e2e25eeeee4c8','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"첨부파일"}');
insert into wf_component_data values ('feada594a34f4b268aab37eb9211e740','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"right","text":"승인자"}');
insert into wf_component_data values ('feada594a34f4b268aab37eb9211e740','display','{"column":"10","customCode":"40288a19736b46fb01736b89e46c0008","default":"none","buttonText":"검색","order":24}');
insert into wf_component_data values ('a0438d6ebeb3e76391de4ca2591b891d','option','[{"seq":"1","name":"전화","value":"phone"},{"seq":"2","name":"메일","value":"mail"},{"seq":"3","name":"전자결재문서","value":"electronic "},{"seq":"4","name":"자체","value":"self"}]');
insert into wf_component_data values ('a0438d6ebeb3e76391de4ca2591b891d','display','{"column":"4","order":6}');
insert into wf_component_data values ('a0438d6ebeb3e76391de4ca2591b891d','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"접수경로"}');
insert into wf_component_data values ('a083bcc9a0679f6bc9cde83eb4205161','label','{"size":"20","color":"rgba(40,50,56,1)","bold":"Y","italic":"N","underline":"N","align":"left","text":"요청 내역"}');
insert into wf_component_data values ('a083bcc9a0679f6bc9cde83eb4205161','display','{"startId":"ae615f2ac0d145ef25d5e7253c12940d","thickness":"1","color":"rgba(235, 235, 235, 1)","order":14}');
insert into wf_component_data values ('a15a240317ee972e96532bf01836fd5c','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"신청자"}');
insert into wf_component_data values ('a15a240317ee972e96532bf01836fd5c','display','{"column":"10","customCode":"40288a19736b46fb01736b89e46c0008","default":"session|userName|이름","buttonText":"검색","order":5}');
insert into wf_component_data values ('a1709377086e44e4e319ba3c2aec93c4','display','{"column":"12","isEditable":true,"border":"rgba(235, 235, 235, 1)","order":12}');
insert into wf_component_data values ('a1709377086e44e4e319ba3c2aec93c4','header','{"size":"16","color":"rgb(63, 75, 86)","bold":"N","italic":"N","underline":"N","align":"left"}');
insert into wf_component_data values ('a1709377086e44e4e319ba3c2aec93c4','label','{"position":"top","column":"12","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"CI 변경 내역"}');
insert into wf_component_data values ('a1bbab51ba6b903eea7834b64ff2018e','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"승인의견"}');
insert into wf_component_data values ('a1bbab51ba6b903eea7834b64ff2018e','display','{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":27}');
insert into wf_component_data values ('a1bbab51ba6b903eea7834b64ff2018e','validate','{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('a20306ee9477e23c3b460bdb459f5f89','display','{"column":"4","order":7}');
insert into wf_component_data values ('a20306ee9477e23c3b460bdb459f5f89','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"변경구분"}');
insert into wf_component_data values ('a20306ee9477e23c3b460bdb459f5f89','option','[{"seq":"1","name":"인프라 업데이트","value":"infra"},{"seq":"2","name":"릴리즈 업데이트","value":"rel"},{"seq":"3","name":"외부연동 업데이트","value":"external"},{"seq":"4","name":"신규장비 입고","value":"new"}]');
insert into wf_component_data values ('a293e254d00208aeb1afb8d78c0d0737','validate','{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('a293e254d00208aeb1afb8d78c0d0737','display','{"column":"10","default":"now","order":26}');
insert into wf_component_data values ('a293e254d00208aeb1afb8d78c0d0737','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"승인일시"}');
insert into wf_component_data values ('a2ef0bbeda3884c8111f82abeb242d6f','display','{"endId":"abc34c5409954d3c792f2209f3347cdf","thickness":"1","color":"rgba(235, 235, 235, 1)","order":15}');
insert into wf_component_data values ('a2ef0bbeda3884c8111f82abeb242d6f','label','{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"접수 내역"}');
insert into wf_component_data values ('a40b7f5aaa39c16cf44d8db83f3f2a5b','display','{"column":"10","order":22}');
insert into wf_component_data values ('a40b7f5aaa39c16cf44d8db83f3f2a5b','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"첨부파일"}');
insert into wf_component_data values ('a4c6a3dd66d0cc36d108ff8cc6904fd7','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"처리내용"}');
insert into wf_component_data values ('a4c6a3dd66d0cc36d108ff8cc6904fd7','validate','{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('a4c6a3dd66d0cc36d108ff8cc6904fd7','display','{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":21}');
insert into wf_component_data values ('a563dbfc8286f3ed75f47b8e778390fb','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"첨부파일"}');
insert into wf_component_data values ('a563dbfc8286f3ed75f47b8e778390fb','display','{"column":"10","order":13}');
insert into wf_component_data values ('a640301d7e67dc92c279f83bec7358be','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"접수일시"}');
insert into wf_component_data values ('a640301d7e67dc92c279f83bec7358be','validate','{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('a640301d7e67dc92c279f83bec7358be','display','{"column":"10","default":"now","order":16}');
insert into wf_component_data values ('a661cc2ef029e637a72124a4b7c5f32f','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"처리일시"}');
insert into wf_component_data values ('a661cc2ef029e637a72124a4b7c5f32f','display','{"column":"10","default":"now","order":20}');
insert into wf_component_data values ('a661cc2ef029e637a72124a4b7c5f32f','validate','{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('a7bc03e8abd70100a86e43338e308054','display','{"column":"10","customCode":"40288a19736b46fb01736b89e46c0008","default":"session|userName|이름","buttonText":"검색","order":9}');
insert into wf_component_data values ('a7bc03e8abd70100a86e43338e308054','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"담당자"}');
insert into wf_component_data values ('a7fb73ce3c61d3d600ab135d6a6a8684','validate','{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('a7fb73ce3c61d3d600ab135d6a6a8684','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"요청내역"}');
insert into wf_component_data values ('a7fb73ce3c61d3d600ab135d6a6a8684','display','{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":11}');
insert into wf_component_data values ('a82c5acd1b0ec0bba083d76135d39e5a','label','{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"구성 변경 처리 내역"}');
insert into wf_component_data values ('a82c5acd1b0ec0bba083d76135d39e5a','display','{"endId":"a87535522638ab7240e972d44d66ef49","thickness":"1","color":"rgba(235, 235, 235, 1)","order":19}');
insert into wf_component_data values ('a87535522638ab7240e972d44d66ef49','label','{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"구성 변경 처리 내역"}');
insert into wf_component_data values ('a87535522638ab7240e972d44d66ef49','display','{"startId":"a82c5acd1b0ec0bba083d76135d39e5a","thickness":"1","color":"rgba(235, 235, 235, 1)","order":24}');
insert into wf_component_data values ('a90620f766dedb2beebbe776eff556ec','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"신청일시"}');
insert into wf_component_data values ('a90620f766dedb2beebbe776eff556ec','validate','{"datetimeMin":"","datetimeMax":""}');
insert into wf_component_data values ('a90620f766dedb2beebbe776eff556ec','display','{"column":"10","default":"now","order":4}');
insert into wf_component_data values ('a9611c745a6716665c5e7690b872310b','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"승인자"}');
insert into wf_component_data values ('a9611c745a6716665c5e7690b872310b','display','{"column":"10","customCode":"40288a19736b46fb01736b89e46c0008","default":"session|userName|이름","buttonText":"검색","order":23}');
insert into wf_component_data values ('a9b580dd25da55ce3ce347bcef0169d5','display','{"thickness":"5","color":"rgba(88,104,114,1)","type":"solid","order":2}');
insert into wf_component_data values ('a9ef096c157fab2ec319efcf467b568a','validate','{"regexp":"none","regexpMsg":"","lengthMin":"0","lengthMax":"30"}');
insert into wf_component_data values ('a9ef096c157fab2ec319efcf467b568a','display','{"placeholder":"","column":"10","default":"none|","order":10}');
insert into wf_component_data values ('a9ef096c157fab2ec319efcf467b568a','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"제목"}');
insert into wf_component_data values ('aa54cd88560b84b902653fd90336ee1e','label','{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"승인 내역"}');
insert into wf_component_data values ('aa54cd88560b84b902653fd90336ee1e','display','{"endId":"ad5e4623a663879158834cbdddd032e5","thickness":"1","color":"rgba(235, 235, 235, 1)","order":25}');
insert into wf_component_data values ('aa5a75f1766bc691fa0c066202c776ae','validate','{"lengthMin":"0","lengthMax":"500"}');
insert into wf_component_data values ('aa5a75f1766bc691fa0c066202c776ae','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"접수의견"}');
insert into wf_component_data values ('aa5a75f1766bc691fa0c066202c776ae','display','{"editorUseYn":false,"rows":"3","placeholder":"","column":"10","order":17}');
insert into wf_component_data values ('abc34c5409954d3c792f2209f3347cdf','label','{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"접수 내역"}');
insert into wf_component_data values ('abc34c5409954d3c792f2209f3347cdf','display','{"startId":"a2ef0bbeda3884c8111f82abeb242d6f","thickness":"1","color":"rgba(235, 235, 235, 1)","order":18}');
insert into wf_component_data values ('ad5e4623a663879158834cbdddd032e5','display','{"startId":"aa54cd88560b84b902653fd90336ee1e","thickness":"1","color":"rgba(235, 235, 235, 1)","order":28}');
insert into wf_component_data values ('ad5e4623a663879158834cbdddd032e5','label','{"size":"20","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"left","text":"승인 내역"}');
insert into wf_component_data values ('ad6afd860d2cda7a78bf93f4e23c8efd','display','{"size":"40","color":"rgba(52,152,219,1)","bold":"Y","italic":"N","underline":"N","align":"left","text":"CI 신청서","order":1}');
insert into wf_component_data values ('ae615f2ac0d145ef25d5e7253c12940d','label','{"size":"20","color":"rgba(40,50,56,1)","bold":"Y","italic":"N","underline":"N","align":"left","text":"요청 내역"}');
insert into wf_component_data values ('ae615f2ac0d145ef25d5e7253c12940d','display','{"endId":"a083bcc9a0679f6bc9cde83eb4205161","thickness":"1","color":"rgba(235, 235, 235, 1)","order":3}');
insert into wf_component_data values ('aff3e77073f75d2385e2347ed996f412','display','{"column":"10","default":"now","order":8}');
insert into wf_component_data values ('aff3e77073f75d2385e2347ed996f412','label','{"position":"left","column":"2","size":"18","color":"rgb(63, 75, 86)","bold":"Y","italic":"N","underline":"N","align":"right","text":"완료희망일시"}');
insert into wf_component_data values ('aff3e77073f75d2385e2347ed996f412','validate','{"datetimeMin":"","datetimeMax":""}');