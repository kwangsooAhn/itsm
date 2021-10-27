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
/* 서비스데스크 - 서비스요청 */
INSERT INTO wf_element VALUES ('04195a896b4a4c5f8c2e8d221c536bc8','4028b21f7c9ff7c8017ca0549ef00057','userTask','신청서 검토','',true,'','{"width":160,"height":40,"position-x":230,"position-y":350}');
INSERT INTO wf_element VALUES ('095e98b2596a4019ae25c01de033aed3','4028b21f7c9ff7c8017ca0549ef00057','manualTask','작업 결과 보고','',false,'','{"width":160,"height":40,"position-x":750,"position-y":350}');
INSERT INTO wf_element VALUES ('0c98f0917c7347418933aab46b8b7823','4028b21f7c9ff7c8017ca0549ef00057','userTask','처리','',false,'','{"width":160,"height":40,"position-x":450,"position-y":450}');
INSERT INTO wf_element VALUES ('24372ebb7e174fca99ae500d05aed4b5','4028b21f7c9ff7c8017ca0549ef00057','subprocess','APP 변경 이관','',false,'','{"width":152,"height":40,"position-x":750,"position-y":450}');
INSERT INTO wf_element VALUES ('447aceb93c124909954082bfed2cc74f','4028b21f7c9ff7c8017ca0549ef00057','arrowConnector','','',false,'','{}');
INSERT INTO wf_element VALUES ('5c2b8645d1994bf683c4c51e08e2932a','4028b21f7c9ff7c8017ca0549ef00057','arrowConnector','신청서 등록','',false,'','{"text-point":[9,-8]}');
INSERT INTO wf_element VALUES ('5cca217b673542e1ad086a67777ef28e','4028b21f7c9ff7c8017ca0549ef00057','arrowConnector','','',false,'','{}');
INSERT INTO wf_element VALUES ('5e98b781b4e74d85ac90e6703cd052a6','4028b21f7c9ff7c8017ca0549ef00057','arrowConnector','','',false,'','{}');
INSERT INTO wf_element VALUES ('5fe9d50a699f4d389ee702135c67d7b7','4028b21f7c9ff7c8017ca0549ef00057','exclusiveGateway','','',false,'','{"width":34,"height":34,"position-x":580,"position-y":450}');
INSERT INTO wf_element VALUES ('6774c1370d614d47bb3b9b0ec33b9c10','4028b21f7c9ff7c8017ca0549ef00057','userTask','승인','',true,'','{"width":160,"height":40,"position-x":1040,"position-y":650}');
INSERT INTO wf_element VALUES ('6eb7ec88b9cf4d3384d0f873b016a76b','4028b21f7c9ff7c8017ca0549ef00057','arrowConnector','인프라 변경 이관','',false,'','{"mid-point":[580,550],"text-point":[6,7]}');
INSERT INTO wf_element VALUES ('705f3c898c3e4b2f8ea838e1b7a1b72f','4028b21f7c9ff7c8017ca0549ef00057','arrowConnector','접수','',false,'','{"text-point":[-4,-37]}');
INSERT INTO wf_element VALUES ('73d8d9a35d7947c68d1abaece193585f','4028b21f7c9ff7c8017ca0549ef00057','arrowConnector','','',false,'','{"mid-point":[900,550]}');
INSERT INTO wf_element VALUES ('74a586945aca4bd598297f1f72e2bbd2','4028b21f7c9ff7c8017ca0549ef00057','signalSend','만족도 평가','',false,'','{"width":40,"height":40,"position-x":1230,"position-y":250}');
INSERT INTO wf_element VALUES ('8187b4a4e8e54c3c86c9acdf3726b445','4028b21f7c9ff7c8017ca0549ef00057','arrowConnector','','',false,'','{"mid-point":[900,350]}');
INSERT INTO wf_element VALUES ('898c51972e9c45e49646c3797165411d','4028b21f7c9ff7c8017ca0549ef00057','userTask','승인요청','',false,'','{"width":160,"height":40,"position-x":1040,"position-y":450}');
INSERT INTO wf_element VALUES ('8a2ff6fdb21d4602a2c4c365798469c4','4028b21f7c9ff7c8017ca0549ef00057','arrowConnector','승인요청','',false,'','{"text-point":[13,34]}');
INSERT INTO wf_element VALUES ('8b962df9ad894d3283ac188d8759267b','4028b21f7c9ff7c8017ca0549ef00057','commonStart','시작','',false,'','{"width":40,"height":40,"position-x":80,"position-y":250}');
INSERT INTO wf_element VALUES ('9ccf78fb415c4b09b32b65e946f0ef93','4028b21f7c9ff7c8017ca0549ef00057','commonEnd','종료','',false,'','{"width":40,"height":40,"position-x":1320,"position-y":250}');
INSERT INTO wf_element VALUES ('9cde4a46cd8c42d8af2d45ba77721091','4028b21f7c9ff7c8017ca0549ef00057','subprocess','인프라 변경 이관','',false,'','{"width":152,"height":40,"position-x":750,"position-y":550}');
INSERT INTO wf_element VALUES ('a71a9701104d441c8b8f73d4afd88f6e','4028b21f7c9ff7c8017ca0549ef00057','arrowConnector','자체 처리','',false,'','{"mid-point":[580,350],"text-point":[6,-93]}');
INSERT INTO wf_element VALUES ('ade030c7ef0f1914e5b06e2e31f8147a','4028b21f7c9ff7c8017ca0549ef00057','arrowConnector','','',false,'','{}');
INSERT INTO wf_element VALUES ('af8674894f8bc525c6f41a482bcf4300','4028b21f7c9ff7c8017ca0549ef00057','arrowConnector','승인','',false,'','{"mid-point":[1160,650],"target-point":[1160,250],"text-point":[29,-39]}');
INSERT INTO wf_element VALUES ('cc4048cd712147b8af4016e647e69b14','4028b21f7c9ff7c8017ca0549ef00057','userTask','신청서 작성','',false,'','{"width":160,"height":40,"position-x":230,"position-y":250}');
INSERT INTO wf_element VALUES ('d0d3a5405e974ec8ad00a72a9eaf6f25','4028b21f7c9ff7c8017ca0549ef00057','arrowConnector','APP 변경 이관','',false,'','{"text-point":[-36,-50]}');
INSERT INTO wf_element VALUES ('d4b6e2e20c884104ac36916f803c65f1','4028b21f7c9ff7c8017ca0549ef00057','exclusiveGateway','','',false,'','{"width":34,"height":34,"position-x":900,"position-y":450}');
INSERT INTO wf_element VALUES ('dd46580c457f49afb05b4d719293d8f5','4028b21f7c9ff7c8017ca0549ef00057','arrowConnector','','',false,'','{}');
INSERT INTO wf_element VALUES ('f4f0bfcfdb3e406e9eec224d79c10ee6','4028b21f7c9ff7c8017ca0549ef00057','manualTask','접수','',false,'','{"width":160,"height":40,"position-x":450,"position-y":350}');
INSERT INTO wf_element VALUES ('f6c8b24bb46041a889a26a84d53ad401','4028b21f7c9ff7c8017ca0549ef00057','arrowConnector','','',false,'','{}');
/* 서비스데스크 - 서비스요청 - 만족도 */
INSERT INTO wf_element VALUES ('0f9c64e3371448358e6f39f292c93a46','4028b21f7c9cc269017c9cc76a5e0000','arrowConnector','만족도 평가','',false,'','{"text-point":[1,-38]}');
INSERT INTO wf_element VALUES ('3914bb0a2d32423885ab84339bf391d5','4028b21f7c9cc269017c9cc76a5e0000','userTask','만족도 평가','',true,'','{"width":160,"height":40,"position-x":680,"position-y":370}');
INSERT INTO wf_element VALUES ('cc5734c370864f15be79c750c9ffffdb','4028b21f7c9cc269017c9cc76a5e0000','commonStart','시작','',false,'','{"width":40,"height":40,"position-x":460,"position-y":370}');
INSERT INTO wf_element VALUES ('d855698cb2c7443194b03eb17fe69522','4028b21f7c9cc269017c9cc76a5e0000','arrowConnector','','',false,'','{}');
INSERT INTO wf_element VALUES ('f9a7329b5cd24001b532265ee88ad3ee','4028b21f7c9cc269017c9cc76a5e0000','commonEnd','종료','',false,'','{"width":40,"height":40,"position-x":900,"position-y":370}');
/* 인프라변경관리 */
INSERT INTO wf_element VALUES ('0844d2fc0cfc45238f24f0a09a328f0a', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'arrowConnector', '', '', false, '', '{"mid-point":[-458,20]}');
INSERT INTO wf_element VALUES ('0c5465de1a8a480a9ac919b79b29569f', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'arrowConnector', '', '', false, '', '{}');
INSERT INTO wf_element VALUES ('0e1c6a483d1f415eb1dd91c0348f13dd', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'manualTask', '접수', '', false, '', '{"width":160,"height":40,"position-x":-203,"position-y":220}');
INSERT INTO wf_element VALUES ('0f188cf2587b488293d6c22b74258167', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'arrowConnector', '승인', '', false, '', '{"mid-point":[327,390],"text-point":[73,-105]}');
INSERT INTO wf_element VALUES ('139f55357c1242cc89a0a8162734bae6', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'userTask', '최종검토', '', true, '', '{"width":160,"height":40,"position-x":1384,"position-y":393}');
INSERT INTO wf_element VALUES ('32f0d926786441ee84b6d56bf2f0fd08', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'commonStart', '시작', '', false, '', '{"width":40,"height":40,"position-x":-578,"position-y":20}');
INSERT INTO wf_element VALUES ('3d56aaee23934e179d2c0ba8e0c860a2', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'arrowConnector', '자체처리', '', false, '', '{"text-point":[-65,-4]}');
INSERT INTO wf_element VALUES ('432255a7eae9415085f38348d6445468', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'arrowConnector', '구성관리', '', false, '', '{"text-point":[-59,-44]}');
INSERT INTO wf_element VALUES ('474cccfff5344196ade87e3479910a6f', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'arrowConnector', '', '', false, '', '{}');
INSERT INTO wf_element VALUES ('4b7e284e81014cfda64de39550895647', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'userTask', '처리', '', true, '', '{"width":160,"height":40,"position-x":327,"position-y":220}');
INSERT INTO wf_element VALUES ('4f2a50e6b49a4e1fab1a9866277a6996', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'exclusiveGateway', '', '', false, '', '{"width":34,"height":34,"position-x":776,"position-y":218}');
INSERT INTO wf_element VALUES ('515b32b1032c48d48b0cb7f304701aec', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'arrowConnector', 'PIR 작성', '', false, '', '{"text-point":[0,22]}');
INSERT INTO wf_element VALUES ('58d6f5e75a5543ab96bc0fa1fe2637c7', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'userTask', '변경계획서 검토', '', true, '', '{"width":160,"height":40,"position-x":177,"position-y":390.0000305175781}');
INSERT INTO wf_element VALUES ('6366778fd60e4a2d913e0d84f80bd11c', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'arrowConnector', '', '', false, '', '{}');
INSERT INTO wf_element VALUES ('697ba85cf98c4d9083f83bd5dd66639f', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'userTask', '변경계획서 작성', '', true, '', '{"width":160,"height":40,"position-x":27,"position-y":220}');
INSERT INTO wf_element VALUES ('7de6a641ac754f48be50f40c63bb1ef5', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'subprocess', '구성관리', '', false, '', '{"width":152,"height":40,"position-x":637,"position-y":140}');
INSERT INTO wf_element VALUES ('89d3605de19d491ea6590640b94461d4', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'arrowConnector', '검토완료', '', false, '', '{"text-point":[14,-66]}');
INSERT INTO wf_element VALUES ('8b8564b9f8164971bee112f0a73e66f3', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'userTask', '변경결과서 작성', '', true, '', '{"width":160,"height":40,"position-x":952,"position-y":220}');
INSERT INTO wf_element VALUES ('996c1c14b95643b1b0a22436f2dee6ea', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'exclusiveGateway', '', '', false, '', '{"width":34,"height":34,"position-x":517,"position-y":220}');
INSERT INTO wf_element VALUES ('9d30b4b0fa3e4b9190c5042e379e59f6', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'commonEnd', '종료', '', false, '', '{"width":40,"height":40,"position-x":1384,"position-y":43}');
INSERT INTO wf_element VALUES ('9deadc6cdd6947eebcac777abaa8338b', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'arrowConnector', '', '', false, '', '{}');
INSERT INTO wf_element VALUES ('a0bfeb6307bf9d5c6c71db3c5e3232df', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'arrowConnector', '접수', '', false, '', '{"text-point":[10,8]}');
INSERT INTO wf_element VALUES ('a94aaf66757795501eacedd06a5b3295', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'arrowConnector', '검토요청', '', false, '', '{"mid-point":[1194,393],"text-point":[7,26]}');
INSERT INTO wf_element VALUES ('baae90a53e39457480d0b2139622e1d2', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'manualTask', '자체처리', '', false, '', '{"width":160,"height":40,"position-x":637,"position-y":290}');
INSERT INTO wf_element VALUES ('be6990d4bd1f4c3586df37fe147b2dea', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'userTask', 'PIR', '', false, '', '{"width":160,"height":40,"position-x":1192,"position-y":220}');
INSERT INTO wf_element VALUES ('c9c7ed6d193b4fd58b726668cbddccae', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'userTask', '이관문서검토', '', false, '', '{"width":160,"height":40,"position-x":-458,"position-y":220}');
INSERT INTO wf_element VALUES ('e15d71bd48bc485f860d2abb70d8d16e', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'arrowConnector', '승인요청', '', false, '', '{"mid-point":[177,220],"text-point":[60,66]}');
INSERT INTO wf_element VALUES ('e2c199ab8f4a4c2ab7cdfc67073b3340', '4028b8817cbfc7a7017cc0db1a8c0bc3', 'arrowConnector', '', '', false, '', '{}');
