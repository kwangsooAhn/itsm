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