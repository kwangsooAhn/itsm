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

insert into wf_element values ('01430d9b3cfe427b89f9203185e35530', '40288ab777f04ed90177f057ca410000', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('1318f304ca2a420e9c6756438daa4e3a', '40288ab777f04ed90177f057ca410000', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('2a8341aa1dcb4ab7ab89271020c748c8', '40288ab777f04ed90177f057ca410000', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('4f296c1468f3422b8c59c97151e2c476', '40288ab777f04ed90177f057ca410000', 'userTask', '신청서 작성', '', false, '', '{"width":160,"height":40,"position-x":210,"position-y":200}');
insert into wf_element values ('70e8f5da83584cba81bd9ff597963c4a', '40288ab777f04ed90177f057ca410000', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('957ac0be921c4337999fcbca2f22a92d', '40288ab777f04ed90177f057ca410000', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('a34268ba767d48e7ab7db9e7297e7300', '40288ab777f04ed90177f057ca410000', 'userTask', '구성변경 처리', '', false, '', '{"width":160,"height":40,"position-x":850,"position-y":200}');
insert into wf_element values ('a4aa971e1f952df93f07e932ab25fbf6', '40288ab777f04ed90177f057ca410000', 'scriptTask', 'CMDB 적용', '', false, '', '{"width":160,"height":40,"position-x":1060,"position-y":280}');
insert into wf_element values ('a4bd7d4950b4226ea3dfc20bf15193ff', '40288ab777f04ed90177f057ca410000', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('a508191382c644b289c01cf32bed8722', '40288ab777f04ed90177f057ca410000', 'userTask', '변경결과 승인', '', true, '', '{"width":160,"height":40,"position-x":850,"position-y":280}');
insert into wf_element values ('a9a0c170a6ff5ae938f128a726fc2a60', '40288ab777f04ed90177f057ca410000', 'commonEnd', '종료', '', false, '', '{"width":40,"height":40,"position-x":1210,"position-y":280}');
insert into wf_element values ('ad56214bb68e7b749f7d336e14172321', '40288ab777f04ed90177f057ca410000', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('b0dd93b1c9864edc8f048713c0d07e12', '40288ab777f04ed90177f057ca410000', 'manualTask', '접수', '', false, '', '{"width":160,"height":40,"position-x":630,"position-y":200}');
insert into wf_element values ('bff880e180164c4f9cedfab4ba282f28', '40288ab777f04ed90177f057ca410000', 'userTask', '신청서검토', '', true, '', '{"width":160,"height":40,"position-x":420,"position-y":200}');
insert into wf_element values ('d099b9ac855a4e7f9a641906549dbcb0', '40288ab777f04ed90177f057ca410000', 'commonStart', '', '', false, '', '{"width":40,"height":40,"position-x":70,"position-y":200}');
insert into wf_element values ('2cb0c129cf054cb1b240f69d47066ff5', '4028b25d787736640178773e71480002', 'commonEnd', '종료', '', false, '', '{"width":40,"height":40,"position-x":520,"position-y":200}');
insert into wf_element values ('32b06c8749f1466aa448c2ff6ccf0fdb', '4028b25d787736640178773e71480002', 'userTask', '만족도평가', '', true, '', '{"width":160,"height":40,"position-x":320,"position-y":200}');
insert into wf_element values ('526362ce18804a71a38c63800ff12656', '4028b25d787736640178773e71480002', 'arrowConnector', '만족도평가', '', false, '', '{}');
insert into wf_element values ('62ee1df53a95473298186031f55f8130', '4028b25d787736640178773e71480002', 'commonStart', '', '', false, '', '{"width":40,"height":40,"position-x":120,"position-y":200}');
insert into wf_element values ('ad56880a70b34398ad693d0ac12b179d', '4028b25d787736640178773e71480002', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('079c5509ce2b4ce2973fbe5d6cebb061', '4028b25d78778da6017877b9df60000f', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('1cc092ef9cb04b9398d5e0687e78ae8f', '4028b25d78778da6017877b9df60000f', 'signalSend', '만족도평가', '', false, '', '{"width":40,"height":40,"position-x":1010,"position-y":280}');
insert into wf_element values ('1db3433a1715460e899f5188e8b80940', '4028b25d78778da6017877b9df60000f', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('237ba276e53a40b5a4d62fba388d76f9', '4028b25d78778da6017877b9df60000f', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('5da2b19767f745c4af6cce63614954f0', '4028b25d78778da6017877b9df60000f', 'userTask', '신청서검토', '', true, '', '{"width":160,"height":40,"position-x":420,"position-y":200}');
insert into wf_element values ('86a7a22385ab434b81fe597665865763', '4028b25d78778da6017877b9df60000f', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('90bfe543a47344c98188e2e09ecb50b5', '4028b25d78778da6017877b9df60000f', 'commonStart', '', '', false, '', '{"width":40,"height":40,"position-x":70,"position-y":200}');
insert into wf_element values ('9cbc9c049d4741c2bbe8f44d0c7e8de7', '4028b25d78778da6017877b9df60000f', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('9fa61b785b944e87b02e9fa753b4c0c5', '4028b25d78778da6017877b9df60000f', 'userTask', '승인', '', true, '', '{"width":160,"height":40,"position-x":850,"position-y":280}');
insert into wf_element values ('aaaf500639bd45d880d0f768bcc91507', '4028b25d78778da6017877b9df60000f', 'commonEnd', '종료', '', false, '', '{"width":40,"height":40,"position-x":1110,"position-y":280}');
insert into wf_element values ('bcdd50f874ea4ea0954ee8c51bf12fbe', '4028b25d78778da6017877b9df60000f', 'manualTask', '접수', '', false, '', '{"width":160,"height":40,"position-x":630,"position-y":200}');
insert into wf_element values ('d01e5d4e5f33495eb239ca868f4511bc', '4028b25d78778da6017877b9df60000f', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('d13b5beee66a4ac7b79fc0bc28abe0cb', '4028b25d78778da6017877b9df60000f', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('d3e931756f15444fa36732356e01a816', '4028b25d78778da6017877b9df60000f', 'userTask', '처리', '', false, '', '{"width":160,"height":40,"position-x":850,"position-y":200}');
insert into wf_element values ('f64ad538612043189e288b43d4bbd7ce', '4028b25d78778da6017877b9df60000f', 'userTask', '신청서 작성', '', false, '', '{"width":160,"height":40,"position-x":210,"position-y":200}');
insert into wf_element values ('04fd8aee89814e479ffb8058b115b466', '4028b25d78870b0901788766663a0023', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('120af605678049f3b2d2b267231d19cc', '4028b25d78870b0901788766663a0023', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('131128229e894ff9afe21a2b7cc27c28', '4028b25d78870b0901788766663a0023', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('20212f1027974110ab50990680b2a0e5', '4028b25d78870b0901788766663a0023', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('203038f6a6c54dec9824275a4c093b61', '4028b25d78870b0901788766663a0023', 'userTask', '승인요청', '', false, '', '{"width":160,"height":40,"position-x":609,"position-y":59}');
insert into wf_element values ('23bd54bbcaf642a487726b6f0dac4b4c', '4028b25d78870b0901788766663a0023', 'userTask', '승인요청', '', false, '', '{"width":160,"height":40,"position-x":1399,"position-y":239}');
insert into wf_element values ('24a2398bc10d4021b3e55f54cc500c50', '4028b25d78870b0901788766663a0023', 'userTask', '접수', '', false, '', '{"width":160,"height":40,"position-x":189,"position-y":59}');
insert into wf_element values ('2e322bca68bd49e6af7015a7381b66ed', '4028b25d78870b0901788766663a0023', 'exclusiveGateway', '', '', false, '', '{"width":34,"height":34,"position-x":1029,"position-y":239}');
insert into wf_element values ('328c7d65847a4089a2d7d22b1b6bd7d1', '4028b25d78870b0901788766663a0023', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('4248c2b5ca9c4b2bb61f640e80c1b3dc', '4028b25d78870b0901788766663a0023', 'commonStart', '', '', false, '', '{"width":40,"height":40,"position-x":49,"position-y":59}');
insert into wf_element values ('5a1417623338462eaf9533a0c23f0455', '4028b25d78870b0901788766663a0023', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('6b04ce8129964511a4491a67e1aedc50', '4028b25d78870b0901788766663a0023', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('7d9dbf9ad83b4e5681c0a6839501039f', '4028b25d78870b0901788766663a0023', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('7ebf8681283b4707b4301277fb6bf9b6', '4028b25d78870b0901788766663a0023', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('80e3a384a64446e0939f44c6e0d062a2', '4028b25d78870b0901788766663a0023', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('906a57e38491441fa61401a8964f8398', '4028b25d78870b0901788766663a0023', 'subprocess', '릴리즈관리', '', false, '', '{"width":152,"height":40,"position-x":889,"position-y":199}');
insert into wf_element values ('9aa8d1454f7a4a2caebaa18586338405', '4028b25d78870b0901788766663a0023', 'exclusiveGateway', '', '', false, '', '{"width":34,"height":34,"position-x":749,"position-y":239}');
insert into wf_element values ('9d50c868a38e4eb5b4620a97a4c8fa95', '4028b25d78870b0901788766663a0023', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('a618f89d6cbb4611aeb5e99bf9bb9df8', '4028b25d78870b0901788766663a0023', 'userTask', '승인', '', true, '', '{"width":160,"height":40,"position-x":1399,"position-y":319}');
insert into wf_element values ('a74d06e6a768ff1626847b9ec3a84235', '4028b25d78870b0901788766663a0023', 'arrowConnector', '', '', false, '', '{}');
insert into wf_element values ('a7f7a90781ba158a2b53052ffd59bf15', '4028b25d78870b0901788766663a0023', 'userTask', '처리', '', false, '', '{"width":160,"height":40,"position-x":609,"position-y":239}');
insert into wf_element values ('ab20cca4f90b42c6a6ea4394e88594e4', '4028b25d78870b0901788766663a0023', 'arrowConnector', '', '', false, '', '{"mid-point":[609,199]}');
insert into wf_element values ('ab9efe0f11dd45bab9df92b4a0d5a1e0', '4028b25d78870b0901788766663a0023', 'userTask', '처리완료', '', false, '', '{"width":160,"height":40,"position-x":889,"position-y":279}');
insert into wf_element values ('bc2dcad9f1c441a7ac1a425beffb7772', '4028b25d78870b0901788766663a0023', 'userTask', '변경계획서', '', false, '', '{"width":160,"height":40,"position-x":399,"position-y":59}');
insert into wf_element values ('c9d735a5aca04835ba7f09f63f768a58', '4028b25d78870b0901788766663a0023', 'userTask', '변경결과 등록', '', false, '', '{"width":160,"height":40,"position-x":1189,"position-y":239}');
insert into wf_element values ('d0d6c6e94f574915bd88257467b6434c', '4028b25d78870b0901788766663a0023', 'userTask', '승인', '', true, '', '{"width":160,"height":40,"position-x":609,"position-y":149}');
insert into wf_element values ('e7ae188b6dee468babdfed470859539c', '4028b25d78870b0901788766663a0023', 'commonEnd', '종료', '', false, '', '{"width":40,"height":40,"position-x":1399,"position-y":399}');
insert into wf_element values ('ecf774011d9c4c0ab353194c3470dfa2', '4028b25d78870b0901788766663a0023', 'arrowConnector', '', '', false, '', '{}');