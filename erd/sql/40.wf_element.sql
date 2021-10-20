/**
 * 엘리먼트정보
 */
DROP TABLE IF EXISTS wf_element cascade;

CREATE TABLE wf_element
(
	element_id varchar(256) NOT NULL,
	process_id varchar(128) NOT NULL,
	element_type varchar(100) NOT NULL,
	element_name varchar(256),
	element_desc varchar(1024),
	notification boolean DEFAULT 'N',
	element_config text,
	display_info text,
	CONSTRAINT wf_element_pk PRIMARY KEY (element_id),
	CONSTRAINT wf_element_fk FOREIGN KEY (process_id) REFERENCES wf_process (process_id)
);

COMMENT ON TABLE wf_element IS '엘리먼트정보';
COMMENT ON COLUMN wf_element.element_id IS '엘리먼트아이디';
COMMENT ON COLUMN wf_element.process_id IS '프로세스아이디';
COMMENT ON COLUMN wf_element.element_type IS '엘리먼트종류';
COMMENT ON COLUMN wf_element.element_name IS '엘리먼트이름';
COMMENT ON COLUMN wf_element.element_desc IS '엘리먼트설명';
COMMENT ON COLUMN wf_element.notification IS '알람여부';
COMMENT ON COLUMN wf_element.element_config IS '엘리먼트설정데이터';
COMMENT ON COLUMN wf_element.display_info IS '출력정보';

/* 서비스데스크 - 단순문의 */
INSERT INTO wf_element VALUES ('337ab138ae9e4250b41be736e0a09c5b','4028b21f7c9698f4017c96a70ded0000','userTask','승인','',true,'','{"width":160,"height":40,"position-x":744,"position-y":670}');
INSERT INTO wf_element VALUES ('3cc34436df104c9eaf6852f52d0ad8a2','4028b21f7c9698f4017c96a70ded0000','signalSend','만족도 평가','',false,'','{"width":40,"height":40,"position-x":1044,"position-y":210}');
INSERT INTO wf_element VALUES ('4be94f828adb4b5a938b82a25feca589','4028b21f7c9698f4017c96a70ded0000','userTask','신청서 검토','',true,'','{"width":160,"height":40,"position-x":454,"position-y":390}');
INSERT INTO wf_element VALUES ('536974f7f4484443acd76b5bd80fc159','4028b21f7c9698f4017c96a70ded0000','arrowConnector','승인','',false,'','{"mid-point":[934,670],"target-point":[934,210],"text-point":[22,5]}');
INSERT INTO wf_element VALUES ('53be2caebd5e40e0b6e9ebecce3f16bd','4028b21f7c9698f4017c96a70ded0000','userTask','처리','',false,'','{"width":160,"height":40,"position-x":744,"position-y":520}');
INSERT INTO wf_element VALUES ('64c845635dfe43be8fba233999327cee','4028b21f7c9698f4017c96a70ded0000','arrowConnector','접수','',false,'','{"text-point":[25,-39]}');
INSERT INTO wf_element VALUES ('670f8e3514c54a429b0d04897907fa45','4028b21f7c9698f4017c96a70ded0000','commonStart','시작','',false,'','{"width":40,"height":40,"position-x":244,"position-y":210}');
INSERT INTO wf_element VALUES ('6c42da85993f4ae9b551ef67b15c5d49','4028b21f7c9698f4017c96a70ded0000','arrowConnector','승인요청','',false,'','{"text-point":[14,19]}');
INSERT INTO wf_element VALUES ('7a1ec9adb4594335bfbd973c0e35489c','4028b21f7c9698f4017c96a70ded0000','commonEnd','종료','',false,'','{"width":40,"height":40,"position-x":1214,"position-y":210}');
INSERT INTO wf_element VALUES ('9c7c235aa4eb43d8a912b2e524264c79','4028b21f7c9698f4017c96a70ded0000','userTask','신청서 작성','',false,'','{"width":160,"height":40,"position-x":454,"position-y":210}');
INSERT INTO wf_element VALUES ('a727237e2c6f9dbdbbff693ed151c85d','4028b21f7c9698f4017c96a70ded0000','arrowConnector','신청서 등록','',false,'','{"text-point":[14,28]}');
INSERT INTO wf_element VALUES ('b579885ee8094ab7989916905212ca03','4028b21f7c9698f4017c96a70ded0000','arrowConnector','','',false,'','{}');
INSERT INTO wf_element VALUES ('c731ce831af64b798fcbd074d5d932be','4028b21f7c9698f4017c96a70ded0000','arrowConnector','','',false,'','{}');
INSERT INTO wf_element VALUES ('e577dd3058394669acb40d471d83635e','4028b21f7c9698f4017c96a70ded0000','arrowConnector','','',false,'','{}');
INSERT INTO wf_element VALUES ('eba7310bd0dd41cfb88622e8f8e00767','4028b21f7c9698f4017c96a70ded0000','manualTask','접수','',false,'','{"width":160,"height":40,"position-x":744,"position-y":390}');
/* 서비스데스크 - 단순문의 - 만족도 */
INSERT INTO wf_element VALUES ('00d3cbc448594f90a91448a7eef93dcb','4028b21f7c9698f4017c96c5630c0002','userTask','만족도 평가','',true,'','{"width":160,"height":40,"position-x":680,"position-y":370}');
INSERT INTO wf_element VALUES ('2a85de200cc44f17a540d9f2ccea6c97','4028b21f7c9698f4017c96c5630c0002','commonStart','시작','',false,'','{"width":40,"height":40,"position-x":460,"position-y":370}');
INSERT INTO wf_element VALUES ('2b4667523f6445ab835e2b5135ddfecd','4028b21f7c9698f4017c96c5630c0002','commonEnd','종료','',false,'','{"width":40,"height":40,"position-x":900,"position-y":370}');
INSERT INTO wf_element VALUES ('71ac5aa1ed1142deb364bbc9bea88cca','4028b21f7c9698f4017c96c5630c0002','arrowConnector','만족도 평가','',false,'','{"text-point":[1,-38]}');
INSERT INTO wf_element VALUES ('fca1181323704662acc1c234886be58e','4028b21f7c9698f4017c96c5630c0002','arrowConnector','','',false,'','{}');
/* 서비스데스크 - 장애신고 */
INSERT INTO wf_element VALUES ('a03d8919f63a4e99e0ace283a7119717','4028b21f7c81a928017c81aa9dc60000','arrowConnector','','',false,'','{}');
INSERT INTO wf_element VALUES ('a109f703f8258c01c71fd7a2c6917225','4028b21f7c81a928017c81aa9dc60000','commonStart','시작','',false,'','{"width":40,"height":40,"position-x":75,"position-y":257}');
INSERT INTO wf_element VALUES ('a117f0729b2cd106fa00b36f12fe1cfb','4028b21f7c81a928017c81aa9dc60000','userTask','신청서 검토','',true,'','{"width":160,"height":40,"position-x":225,"position-y":357}');
INSERT INTO wf_element VALUES ('a13cd52a4338bde9db939249fcb722f7','4028b21f7c81a928017c81aa9dc60000','arrowConnector','','',false,'','{}');
INSERT INTO wf_element VALUES ('a1c6c91a6dd476ae8ffb5e0c7e9c0d53','4028b21f7c81a928017c81aa9dc60000','userTask','신청서 작성','',false,'','{"width":160,"height":40,"position-x":225,"position-y":257}');
INSERT INTO wf_element VALUES ('a271b9f3a790280811cd6a6ff94c02e1','4028b21f7c81a928017c81aa9dc60000','arrowConnector','','',false,'','{"mid-point":[885,397]}');
INSERT INTO wf_element VALUES ('a296d2e534c0c2fe97071cdc9acfeb80','4028b21f7c81a928017c81aa9dc60000','subprocess','장애 이관','',false,'','{"width":152,"height":40,"position-x":745,"position-y":517}');
INSERT INTO wf_element VALUES ('a2a91cf26c14dc98cbc1dfd9ac433477','4028b21f7c81a928017c81aa9dc60000','arrowConnector','','',false,'','{}');
INSERT INTO wf_element VALUES ('a2c92c99371e885e53bc0b3ae65b8ac8','4028b21f7c81a928017c81aa9dc60000','arrowConnector','','',false,'','{}');
INSERT INTO wf_element VALUES ('a45089565ee86a40cfcc16070d6055a4','4028b21f7c81a928017c81aa9dc60000','arrowConnector','','',false,'','{}');
INSERT INTO wf_element VALUES ('a4bf5389f404c756d957b40cd7a1bf10','4028b21f7c81a928017c81aa9dc60000','manualTask','작업 결과 보고','',false,'','{"width":160,"height":40,"position-x":745,"position-y":397}');
INSERT INTO wf_element VALUES ('a7acf388af93de92a17b7068f846c073','4028b21f7c81a928017c81aa9dc60000','commonEnd','종료','',false,'','{"width":40,"height":40,"position-x":1337,"position-y":260}');
INSERT INTO wf_element VALUES ('a7f0ad953c834f779aac354f97b84eb2','4028b21f7c81a928017c81aa9dc60000','signalSend','만족도 평가','',false,'','{"width":40,"height":40,"position-x":1220,"position-y":260}');
INSERT INTO wf_element VALUES ('a7f615029e8be83d8008d2012454e1f4','4028b21f7c81a928017c81aa9dc60000','arrowConnector','자체 처리','',false,'','{"mid-point":[615,397],"text-point":[-36,-48]}');
INSERT INTO wf_element VALUES ('a84a4bafdc3b3f1928a0677434d34feb','4028b21f7c81a928017c81aa9dc60000','userTask','승인','',true,'','{"width":160,"height":40,"position-x":1027,"position-y":620}');
INSERT INTO wf_element VALUES ('aa104e687c0f220966d493707a87def1','4028b21f7c81a928017c81aa9dc60000','exclusiveGateway','','',false,'','{"width":34,"height":34,"position-x":615,"position-y":457}');
INSERT INTO wf_element VALUES ('aa57399fa79e38fd8e59a3a1a11e56d2','4028b21f7c81a928017c81aa9dc60000','exclusiveGateway','','',false,'','{"width":34,"height":34,"position-x":885,"position-y":457}');
INSERT INTO wf_element VALUES ('aa900688d722cd451b47e72dda93fca8','4028b21f7c81a928017c81aa9dc60000','userTask','승인요청','',false,'','{"width":160,"height":40,"position-x":1025,"position-y":457}');
INSERT INTO wf_element VALUES ('ab3d8234ca2ab3b44934df41563f067d','4028b21f7c81a928017c81aa9dc60000','arrowConnector','접수','',false,'','{"text-point":[4,-38]}');
INSERT INTO wf_element VALUES ('ac6b5d8653e21191f1f0ac59136a5297','4028b21f7c81a928017c81aa9dc60000','arrowConnector','','',false,'','{"mid-point":[885,517]}');
INSERT INTO wf_element VALUES ('ae39db34c9022d8fa2e101b32a5cb860','4028b21f7c81a928017c81aa9dc60000','arrowConnector','승인요청','',false,'','{"text-point":[15,25]}');
INSERT INTO wf_element VALUES ('ae5f5ef48e6ad78e8351b93017c565c9','4028b21f7c81a928017c81aa9dc60000','arrowConnector','승인','',false,'','{"mid-point":[1147,260],"source-point":[1147,620],"text-point":[31,-83]}');
INSERT INTO wf_element VALUES ('aea0ee0f94c686307c87a5c86699ef86','4028b21f7c81a928017c81aa9dc60000','userTask','처리','',false,'','{"width":160,"height":40,"position-x":465,"position-y":457}');
INSERT INTO wf_element VALUES ('aeefd9f38872ec0907d42c401dc8b71c','4028b21f7c81a928017c81aa9dc60000','manualTask','접수','',false,'','{"width":160,"height":40,"position-x":465,"position-y":357}');
INSERT INTO wf_element VALUES ('af4ce9dee1335cb46a3c127d27267bb1','4028b21f7c81a928017c81aa9dc60000','arrowConnector','신청서 등록','',false,'','{"text-point":[10,-4]}');
INSERT INTO wf_element VALUES ('af59396a9ffd08eee3efb2d0f0a4fd21','4028b21f7c81a928017c81aa9dc60000','arrowConnector','장애 이관','',false,'','{"mid-point":[615,517],"text-point":[-40,21]}');
/* 서비스데스크 - 장애신고 - 만족도 */
INSERT INTO wf_element VALUES ('2f3712ca2ea34ae1800179f2dd7f6424','4028b21f7c9b6b1e017c9bdf04cb0011','commonEnd','종료','',false,'','{"width":40,"height":40,"position-x":900,"position-y":370}');
INSERT INTO wf_element VALUES ('36dad342725848f7ab1453e309ef0aa1','4028b21f7c9b6b1e017c9bdf04cb0011','userTask','만족도 평가','',true,'','{"width":160,"height":40,"position-x":680,"position-y":370}');
INSERT INTO wf_element VALUES ('8d12225883ea4eb3bf2d137c67278eb8','4028b21f7c9b6b1e017c9bdf04cb0011','arrowConnector','','',false,'','{}');
INSERT INTO wf_element VALUES ('997d5b3c3246420181a774069b489909','4028b21f7c9b6b1e017c9bdf04cb0011','commonStart','시작','',false,'','{"width":40,"height":40,"position-x":460,"position-y":370}');
INSERT INTO wf_element VALUES ('a9eac2d7b40b4ea880926a38379f3ddd','4028b21f7c9b6b1e017c9bdf04cb0011','arrowConnector','만족도 평가','',false,'','{"text-point":[1,-38]}');