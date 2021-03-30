/**
 * 엘리먼트세부설정
 */
DROP TABLE IF EXISTS wf_element_data cascade;

CREATE TABLE wf_element_data
(
	element_id varchar(256) NOT NULL,
	attribute_id varchar(100) NOT NULL,
	attribute_value varchar(512) NOT NULL,
	attribute_order int,
	attribute_required boolean DEFAULT 'false' NOT NULL,
	CONSTRAINT wf_element_data_pk PRIMARY KEY (element_id, attribute_id, attribute_value),
	CONSTRAINT wf_element_data_fk FOREIGN KEY (element_id) REFERENCES wf_element (element_id)
);

COMMENT ON TABLE wf_element_data IS '엘리먼트세부설정';
COMMENT ON COLUMN wf_element_data.element_id IS '엘리먼트아이디';
COMMENT ON COLUMN wf_element_data.attribute_id IS '속성아이디';
COMMENT ON COLUMN wf_element_data.attribute_value IS '속성값';
COMMENT ON COLUMN wf_element_data.attribute_order IS '속성순서';
COMMENT ON COLUMN wf_element_data.attribute_required IS '속성필수값';

insert into wf_element_data values ('01430d9b3cfe427b89f9203185e35530', 'action-name', '접수', 0, false);
insert into wf_element_data values ('01430d9b3cfe427b89f9203185e35530', 'action-value', 'accept', 1, false);
insert into wf_element_data values ('01430d9b3cfe427b89f9203185e35530', 'is-default', 'N', 2, false);
insert into wf_element_data values ('01430d9b3cfe427b89f9203185e35530', 'condition-value', '', 3, false);
insert into wf_element_data values ('01430d9b3cfe427b89f9203185e35530', 'start-id', 'bff880e180164c4f9cedfab4ba282f28', 4, true);
insert into wf_element_data values ('01430d9b3cfe427b89f9203185e35530', 'start-name', '신청서 검토', 5, false);
insert into wf_element_data values ('01430d9b3cfe427b89f9203185e35530', 'end-id', 'b0dd93b1c9864edc8f048713c0d07e12', 6, true);
insert into wf_element_data values ('01430d9b3cfe427b89f9203185e35530', 'end-name', '접수', 7, false);
insert into wf_element_data values ('1318f304ca2a420e9c6756438daa4e3a', 'action-name', '승인요청', 0, false);
insert into wf_element_data values ('1318f304ca2a420e9c6756438daa4e3a', 'action-value', 'approve', 1, false);
insert into wf_element_data values ('1318f304ca2a420e9c6756438daa4e3a', 'is-default', 'N', 2, false);
insert into wf_element_data values ('1318f304ca2a420e9c6756438daa4e3a', 'condition-value', '', 3, false);
insert into wf_element_data values ('1318f304ca2a420e9c6756438daa4e3a', 'start-id', 'a34268ba767d48e7ab7db9e7297e7300', 4, true);
insert into wf_element_data values ('1318f304ca2a420e9c6756438daa4e3a', 'start-name', '구성변경 처리', 5, false);
insert into wf_element_data values ('1318f304ca2a420e9c6756438daa4e3a', 'end-id', 'a508191382c644b289c01cf32bed8722', 6, true);
insert into wf_element_data values ('1318f304ca2a420e9c6756438daa4e3a', 'end-name', '변경결과 승인', 7, false);
insert into wf_element_data values ('2a8341aa1dcb4ab7ab89271020c748c8', 'action-name', '', 0, false);
insert into wf_element_data values ('2a8341aa1dcb4ab7ab89271020c748c8', 'action-value', '', 1, false);
insert into wf_element_data values ('2a8341aa1dcb4ab7ab89271020c748c8', 'is-default', 'N', 2, false);
insert into wf_element_data values ('2a8341aa1dcb4ab7ab89271020c748c8', 'condition-value', '', 3, false);
insert into wf_element_data values ('2a8341aa1dcb4ab7ab89271020c748c8', 'start-id', 'd099b9ac855a4e7f9a641906549dbcb0', 4, true);
insert into wf_element_data values ('2a8341aa1dcb4ab7ab89271020c748c8', 'start-name', '', 5, false);
insert into wf_element_data values ('2a8341aa1dcb4ab7ab89271020c748c8', 'end-id', '4f296c1468f3422b8c59c97151e2c476', 6, true);
insert into wf_element_data values ('2a8341aa1dcb4ab7ab89271020c748c8', 'end-name', '신청서 작성', 7, false);
insert into wf_element_data values ('4f296c1468f3422b8c59c97151e2c476', 'assignee-type', 'assignee.type.candidate.groups', 0, true);
insert into wf_element_data values ('4f296c1468f3422b8c59c97151e2c476', 'assignee', 'users.general', 1, true);
insert into wf_element_data values ('4f296c1468f3422b8c59c97151e2c476', 'reject-id', '', 2, false);
insert into wf_element_data values ('4f296c1468f3422b8c59c97151e2c476', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('70e8f5da83584cba81bd9ff597963c4a', 'action-name', '', 0, false);
insert into wf_element_data values ('70e8f5da83584cba81bd9ff597963c4a', 'action-value', '', 1, false);
insert into wf_element_data values ('70e8f5da83584cba81bd9ff597963c4a', 'is-default', 'N', 2, false);
insert into wf_element_data values ('70e8f5da83584cba81bd9ff597963c4a', 'condition-value', '', 3, false);
insert into wf_element_data values ('70e8f5da83584cba81bd9ff597963c4a', 'start-id', 'b0dd93b1c9864edc8f048713c0d07e12', 4, true);
insert into wf_element_data values ('70e8f5da83584cba81bd9ff597963c4a', 'start-name', '접수', 5, false);
insert into wf_element_data values ('70e8f5da83584cba81bd9ff597963c4a', 'end-id', 'a34268ba767d48e7ab7db9e7297e7300', 6, true);
insert into wf_element_data values ('70e8f5da83584cba81bd9ff597963c4a', 'end-name', '구성변경 처리', 7, false);
insert into wf_element_data values ('957ac0be921c4337999fcbca2f22a92d', 'action-name', '신청등록', 0, false);
insert into wf_element_data values ('957ac0be921c4337999fcbca2f22a92d', 'action-value', 'create', 1, false);
insert into wf_element_data values ('957ac0be921c4337999fcbca2f22a92d', 'is-default', 'N', 2, false);
insert into wf_element_data values ('957ac0be921c4337999fcbca2f22a92d', 'condition-value', '', 3, false);
insert into wf_element_data values ('957ac0be921c4337999fcbca2f22a92d', 'start-id', '4f296c1468f3422b8c59c97151e2c476', 4, true);
insert into wf_element_data values ('957ac0be921c4337999fcbca2f22a92d', 'start-name', '신청서 작성', 5, false);
insert into wf_element_data values ('957ac0be921c4337999fcbca2f22a92d', 'end-id', 'bff880e180164c4f9cedfab4ba282f28', 6, true);
insert into wf_element_data values ('957ac0be921c4337999fcbca2f22a92d', 'end-name', '신청서 검토', 7, false);
insert into wf_element_data values ('a34268ba767d48e7ab7db9e7297e7300', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('a34268ba767d48e7ab7db9e7297e7300', 'assignee', 'assignee1', 1, true);
insert into wf_element_data values ('a34268ba767d48e7ab7db9e7297e7300', 'reject-id', '', 2, false);
insert into wf_element_data values ('a34268ba767d48e7ab7db9e7297e7300', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('a4aa971e1f952df93f07e932ab25fbf6', 'script-type', 'script.type.cmdb', 0, true);
insert into wf_element_data values ('a4bd7d4950b4226ea3dfc20bf15193ff', 'action-name', '승인', 0, false);
insert into wf_element_data values ('a4bd7d4950b4226ea3dfc20bf15193ff', 'action-value', 'approval', 1, false);
insert into wf_element_data values ('a4bd7d4950b4226ea3dfc20bf15193ff', 'is-default', 'N', 2, false);
insert into wf_element_data values ('a4bd7d4950b4226ea3dfc20bf15193ff', 'condition-value', '', 3, false);
insert into wf_element_data values ('a4bd7d4950b4226ea3dfc20bf15193ff', 'start-id', 'a508191382c644b289c01cf32bed8722', 4, true);
insert into wf_element_data values ('a4bd7d4950b4226ea3dfc20bf15193ff', 'start-name', '변경결과 승인', 5, false);
insert into wf_element_data values ('a4bd7d4950b4226ea3dfc20bf15193ff', 'end-id', 'a4aa971e1f952df93f07e932ab25fbf6', 6, true);
insert into wf_element_data values ('a4bd7d4950b4226ea3dfc20bf15193ff', 'end-name', 'CMDB 적용', 7, false);
insert into wf_element_data values ('a508191382c644b289c01cf32bed8722', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('a508191382c644b289c01cf32bed8722', 'assignee', 'assignee2', 1, true);
insert into wf_element_data values ('a508191382c644b289c01cf32bed8722', 'reject-id', 'a34268ba767d48e7ab7db9e7297e7300', 2, false);
insert into wf_element_data values ('a508191382c644b289c01cf32bed8722', 'withdraw', 'Y', 3, false);
insert into wf_element_data values ('ad56214bb68e7b749f7d336e14172321', 'action-name', '', 0, false);
insert into wf_element_data values ('ad56214bb68e7b749f7d336e14172321', 'action-value', '', 1, false);
insert into wf_element_data values ('ad56214bb68e7b749f7d336e14172321', 'is-default', 'N', 2, false);
insert into wf_element_data values ('ad56214bb68e7b749f7d336e14172321', 'condition-value', '', 3, false);
insert into wf_element_data values ('ad56214bb68e7b749f7d336e14172321', 'start-id', 'a4aa971e1f952df93f07e932ab25fbf6', 4, true);
insert into wf_element_data values ('ad56214bb68e7b749f7d336e14172321', 'start-name', 'CMDB 적용', 5, false);
insert into wf_element_data values ('ad56214bb68e7b749f7d336e14172321', 'end-id', 'a9a0c170a6ff5ae938f128a726fc2a60', 6, true);
insert into wf_element_data values ('ad56214bb68e7b749f7d336e14172321', 'end-name', '종료', 7, false);
insert into wf_element_data values ('b0dd93b1c9864edc8f048713c0d07e12', 'complete-action', '', 0, false);
insert into wf_element_data values ('bff880e180164c4f9cedfab4ba282f28', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('bff880e180164c4f9cedfab4ba282f28', 'assignee', 'assignee1', 1, true);
insert into wf_element_data values ('bff880e180164c4f9cedfab4ba282f28', 'reject-id', '', 2, false);
insert into wf_element_data values ('bff880e180164c4f9cedfab4ba282f28', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('32b06c8749f1466aa448c2ff6ccf0fdb', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('32b06c8749f1466aa448c2ff6ccf0fdb', 'assignee', 'user', 1, true);
insert into wf_element_data values ('32b06c8749f1466aa448c2ff6ccf0fdb', 'reject-id', '', 2, false);
insert into wf_element_data values ('32b06c8749f1466aa448c2ff6ccf0fdb', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('526362ce18804a71a38c63800ff12656', 'action-name', '만족도 평가', 0, false);
insert into wf_element_data values ('526362ce18804a71a38c63800ff12656', 'action-value', 'progress', 1, false);
insert into wf_element_data values ('526362ce18804a71a38c63800ff12656', 'is-default', 'N', 2, false);
insert into wf_element_data values ('526362ce18804a71a38c63800ff12656', 'condition-value', '', 3, false);
insert into wf_element_data values ('526362ce18804a71a38c63800ff12656', 'start-id', '32b06c8749f1466aa448c2ff6ccf0fdb', 4, true);
insert into wf_element_data values ('526362ce18804a71a38c63800ff12656', 'start-name', 'New Task', 5, false);
insert into wf_element_data values ('526362ce18804a71a38c63800ff12656', 'end-id', '2cb0c129cf054cb1b240f69d47066ff5', 6, true);
insert into wf_element_data values ('526362ce18804a71a38c63800ff12656', 'end-name', '종료', 7, false);
insert into wf_element_data values ('ad56880a70b34398ad693d0ac12b179d', 'action-name', '', 0, false);
insert into wf_element_data values ('ad56880a70b34398ad693d0ac12b179d', 'action-value', '', 1, false);
insert into wf_element_data values ('ad56880a70b34398ad693d0ac12b179d', 'is-default', 'N', 2, false);
insert into wf_element_data values ('ad56880a70b34398ad693d0ac12b179d', 'condition-value', '', 3, false);
insert into wf_element_data values ('ad56880a70b34398ad693d0ac12b179d', 'start-id', '62ee1df53a95473298186031f55f8130', 4, true);
insert into wf_element_data values ('ad56880a70b34398ad693d0ac12b179d', 'start-name', '', 5, false);
insert into wf_element_data values ('ad56880a70b34398ad693d0ac12b179d', 'end-id', '32b06c8749f1466aa448c2ff6ccf0fdb', 6, true);
insert into wf_element_data values ('ad56880a70b34398ad693d0ac12b179d', 'end-name', '만족도평가', 7, false);
insert into wf_element_data values ('079c5509ce2b4ce2973fbe5d6cebb061', 'action-name', '신청등록', 0, false);
insert into wf_element_data values ('079c5509ce2b4ce2973fbe5d6cebb061', 'action-value', 'progress', 1, false);
insert into wf_element_data values ('079c5509ce2b4ce2973fbe5d6cebb061', 'is-default', 'N', 2, false);
insert into wf_element_data values ('079c5509ce2b4ce2973fbe5d6cebb061', 'condition-value', '', 3, false);
insert into wf_element_data values ('079c5509ce2b4ce2973fbe5d6cebb061', 'start-id', 'f64ad538612043189e288b43d4bbd7ce', 4, true);
insert into wf_element_data values ('079c5509ce2b4ce2973fbe5d6cebb061', 'start-name', '신청서 작성', 5, false);
insert into wf_element_data values ('079c5509ce2b4ce2973fbe5d6cebb061', 'end-id', '5da2b19767f745c4af6cce63614954f0', 6, true);
insert into wf_element_data values ('079c5509ce2b4ce2973fbe5d6cebb061', 'end-name', '신청서 검토', 7, false);
insert into wf_element_data values ('1cc092ef9cb04b9398d5e0687e78ae8f', 'target-document-list', '4028b25d78778da6017877aff7e40001', 0, true);
insert into wf_element_data values ('1db3433a1715460e899f5188e8b80940', 'action-name', '', 0, false);
insert into wf_element_data values ('1db3433a1715460e899f5188e8b80940', 'action-value', '', 1, false);
insert into wf_element_data values ('1db3433a1715460e899f5188e8b80940', 'is-default', 'N', 2, false);
insert into wf_element_data values ('1db3433a1715460e899f5188e8b80940', 'condition-value', '', 3, false);
insert into wf_element_data values ('1db3433a1715460e899f5188e8b80940', 'start-id', '1cc092ef9cb04b9398d5e0687e78ae8f', 4, true);
insert into wf_element_data values ('1db3433a1715460e899f5188e8b80940', 'start-name', '만족도평가', 5, false);
insert into wf_element_data values ('1db3433a1715460e899f5188e8b80940', 'end-id', 'aaaf500639bd45d880d0f768bcc91507', 6, true);
insert into wf_element_data values ('1db3433a1715460e899f5188e8b80940', 'end-name', '종료', 7, false);
insert into wf_element_data values ('237ba276e53a40b5a4d62fba388d76f9', 'action-name', '접수', 0, false);
insert into wf_element_data values ('237ba276e53a40b5a4d62fba388d76f9', 'action-value', 'progress', 1, false);
insert into wf_element_data values ('237ba276e53a40b5a4d62fba388d76f9', 'is-default', 'N', 2, false);
insert into wf_element_data values ('237ba276e53a40b5a4d62fba388d76f9', 'condition-value', '', 3, false);
insert into wf_element_data values ('237ba276e53a40b5a4d62fba388d76f9', 'start-id', '5da2b19767f745c4af6cce63614954f0', 4, true);
insert into wf_element_data values ('237ba276e53a40b5a4d62fba388d76f9', 'start-name', '신청서 검토', 5, false);
insert into wf_element_data values ('237ba276e53a40b5a4d62fba388d76f9', 'end-id', 'bcdd50f874ea4ea0954ee8c51bf12fbe', 6, true);
insert into wf_element_data values ('237ba276e53a40b5a4d62fba388d76f9', 'end-name', '접수', 7, false);
insert into wf_element_data values ('5da2b19767f745c4af6cce63614954f0', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('5da2b19767f745c4af6cce63614954f0', 'assignee', 'assignee', 1, true);
insert into wf_element_data values ('5da2b19767f745c4af6cce63614954f0', 'reject-id', '', 2, false);
insert into wf_element_data values ('5da2b19767f745c4af6cce63614954f0', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('86a7a22385ab434b81fe597665865763', 'action-name', '승인', 0, false);
insert into wf_element_data values ('86a7a22385ab434b81fe597665865763', 'action-value', 'progress', 1, false);
insert into wf_element_data values ('86a7a22385ab434b81fe597665865763', 'is-default', 'N', 2, false);
insert into wf_element_data values ('86a7a22385ab434b81fe597665865763', 'condition-value', '', 3, false);
insert into wf_element_data values ('86a7a22385ab434b81fe597665865763', 'start-id', '9fa61b785b944e87b02e9fa753b4c0c5', 4, true);
insert into wf_element_data values ('86a7a22385ab434b81fe597665865763', 'start-name', '승인', 5, false);
insert into wf_element_data values ('86a7a22385ab434b81fe597665865763', 'end-id', '1cc092ef9cb04b9398d5e0687e78ae8f', 6, true);
insert into wf_element_data values ('86a7a22385ab434b81fe597665865763', 'end-name', '만족도평가', 7, false);
insert into wf_element_data values ('9cbc9c049d4741c2bbe8f44d0c7e8de7', 'action-name', '', 0, false);
insert into wf_element_data values ('9cbc9c049d4741c2bbe8f44d0c7e8de7', 'action-value', '', 1, false);
insert into wf_element_data values ('9cbc9c049d4741c2bbe8f44d0c7e8de7', 'is-default', 'N', 2, false);
insert into wf_element_data values ('9cbc9c049d4741c2bbe8f44d0c7e8de7', 'condition-value', '', 3, false);
insert into wf_element_data values ('9cbc9c049d4741c2bbe8f44d0c7e8de7', 'start-id', 'bcdd50f874ea4ea0954ee8c51bf12fbe', 4, true);
insert into wf_element_data values ('9cbc9c049d4741c2bbe8f44d0c7e8de7', 'start-name', '접수', 5, false);
insert into wf_element_data values ('9cbc9c049d4741c2bbe8f44d0c7e8de7', 'end-id', 'd3e931756f15444fa36732356e01a816', 6, true);
insert into wf_element_data values ('9cbc9c049d4741c2bbe8f44d0c7e8de7', 'end-name', '처리', 7, false);
insert into wf_element_data values ('9fa61b785b944e87b02e9fa753b4c0c5', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('9fa61b785b944e87b02e9fa753b4c0c5', 'assignee', 'assignee2', 1, true);
insert into wf_element_data values ('9fa61b785b944e87b02e9fa753b4c0c5', 'reject-id', 'd3e931756f15444fa36732356e01a816', 2, false);
insert into wf_element_data values ('9fa61b785b944e87b02e9fa753b4c0c5', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('bcdd50f874ea4ea0954ee8c51bf12fbe', 'complete-action', '', 0, false);
insert into wf_element_data values ('d01e5d4e5f33495eb239ca868f4511bc', 'action-name', '승인요청', 0, false);
insert into wf_element_data values ('d01e5d4e5f33495eb239ca868f4511bc', 'action-value', 'progress', 1, false);
insert into wf_element_data values ('d01e5d4e5f33495eb239ca868f4511bc', 'is-default', 'N', 2, false);
insert into wf_element_data values ('d01e5d4e5f33495eb239ca868f4511bc', 'condition-value', '', 3, false);
insert into wf_element_data values ('d01e5d4e5f33495eb239ca868f4511bc', 'start-id', 'd3e931756f15444fa36732356e01a816', 4, true);
insert into wf_element_data values ('d01e5d4e5f33495eb239ca868f4511bc', 'start-name', '처리', 5, false);
insert into wf_element_data values ('d01e5d4e5f33495eb239ca868f4511bc', 'end-id', '9fa61b785b944e87b02e9fa753b4c0c5', 6, true);
insert into wf_element_data values ('d01e5d4e5f33495eb239ca868f4511bc', 'end-name', '승인', 7, false);
insert into wf_element_data values ('d13b5beee66a4ac7b79fc0bc28abe0cb', 'action-name', '', 0, false);
insert into wf_element_data values ('d13b5beee66a4ac7b79fc0bc28abe0cb', 'action-value', '', 1, false);
insert into wf_element_data values ('d13b5beee66a4ac7b79fc0bc28abe0cb', 'is-default', 'N', 2, false);
insert into wf_element_data values ('d13b5beee66a4ac7b79fc0bc28abe0cb', 'condition-value', '', 3, false);
insert into wf_element_data values ('d13b5beee66a4ac7b79fc0bc28abe0cb', 'start-id', '90bfe543a47344c98188e2e09ecb50b5', 4, true);
insert into wf_element_data values ('d13b5beee66a4ac7b79fc0bc28abe0cb', 'start-name', '', 5, false);
insert into wf_element_data values ('d13b5beee66a4ac7b79fc0bc28abe0cb', 'end-id', 'f64ad538612043189e288b43d4bbd7ce', 6, true);
insert into wf_element_data values ('d13b5beee66a4ac7b79fc0bc28abe0cb', 'end-name', '신청서 작성', 7, false);
insert into wf_element_data values ('d3e931756f15444fa36732356e01a816', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('d3e931756f15444fa36732356e01a816', 'assignee', 'assignee', 1, true);
insert into wf_element_data values ('d3e931756f15444fa36732356e01a816', 'reject-id', '', 2, false);
insert into wf_element_data values ('d3e931756f15444fa36732356e01a816', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('f64ad538612043189e288b43d4bbd7ce', 'assignee-type', 'assignee.type.candidate.groups', 0, true);
insert into wf_element_data values ('f64ad538612043189e288b43d4bbd7ce', 'assignee', 'users.general', 1, true);
insert into wf_element_data values ('f64ad538612043189e288b43d4bbd7ce', 'reject-id', '', 2, false);
insert into wf_element_data values ('f64ad538612043189e288b43d4bbd7ce', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('ac4c84456b64ae08c1cfeb56234e36d5', 'action-value', 'approve', 1, false);
insert into wf_element_data values ('ac4c84456b64ae08c1cfeb56234e36d5', 'action-name', '승인요청', 0, false);
insert into wf_element_data values ('a92c43aedb89519f0c7b31278063788b', 'action-name', '작업결과서', 0, false);
insert into wf_element_data values ('a92c43aedb89519f0c7b31278063788b', 'action-value', 'work_finish_report', 1, false);
insert into wf_element_data values ('a82fb95a6550c3cd5dadedd7f597c598', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('a82fb95a6550c3cd5dadedd7f597c598', 'assignee', 'rel_manager', 1, true);
insert into wf_element_data values ('a82fb95a6550c3cd5dadedd7f597c598', 'reject-id', '', 2, false);
insert into wf_element_data values ('a82fb95a6550c3cd5dadedd7f597c598', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('a8a1e1db38cdfdbaa692e14db009c224', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('a8a1e1db38cdfdbaa692e14db009c224', 'assignee', 'rel_approver', 1, true);
insert into wf_element_data values ('a8a1e1db38cdfdbaa692e14db009c224', 'reject-id', 'aaa5103974ec9059469823d84d8ce109', 2, false);
insert into wf_element_data values ('a8a1e1db38cdfdbaa692e14db009c224', 'withdraw', 'Y', 3, false);
insert into wf_element_data values ('a905a563d43599def0638503401bcad0', 'condition-item', '#{action}', 0, true);
insert into wf_element_data values ('a92c43aedb89519f0c7b31278063788b', 'is-default', 'N', 2, false);
insert into wf_element_data values ('a92c43aedb89519f0c7b31278063788b', 'condition-value', '', 3, false);
insert into wf_element_data values ('a92c43aedb89519f0c7b31278063788b', 'start-id', 'a82fb95a6550c3cd5dadedd7f597c598', 4, true);
insert into wf_element_data values ('a92c43aedb89519f0c7b31278063788b', 'start-name', '작업결과서', 5, false);
insert into wf_element_data values ('a92c43aedb89519f0c7b31278063788b', 'end-id', 'a258e0bfcfae24e99f75be596efc173e', 6, true);
insert into wf_element_data values ('a92c43aedb89519f0c7b31278063788b', 'end-name', '완료보고서', 7, false);
insert into wf_element_data values ('a9fb5f654c0b856e1975997e9bcdb1c9', 'action-name', '', 0, false);
insert into wf_element_data values ('a9fb5f654c0b856e1975997e9bcdb1c9', 'action-value', '', 1, false);
insert into wf_element_data values ('a9fb5f654c0b856e1975997e9bcdb1c9', 'is-default', 'N', 2, false);
insert into wf_element_data values ('a9fb5f654c0b856e1975997e9bcdb1c9', 'condition-value', ' = 1', 3, false);
insert into wf_element_data values ('a9fb5f654c0b856e1975997e9bcdb1c9', 'start-id', 'a1184c7bed5efd25324bc0d70b1bfdf5', 4, true);
insert into wf_element_data values ('a9fb5f654c0b856e1975997e9bcdb1c9', 'start-name', '', 5, false);
insert into wf_element_data values ('a9fb5f654c0b856e1975997e9bcdb1c9', 'end-id', 'a905a563d43599def0638503401bcad0', 6, true);
insert into wf_element_data values ('a9fb5f654c0b856e1975997e9bcdb1c9', 'end-name', '', 7, false);
insert into wf_element_data values ('aa87e149e75de05f31f28fb9309e91df', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('aa87e149e75de05f31f28fb9309e91df', 'assignee', 'rel_manager', 1, true);
insert into wf_element_data values ('aa87e149e75de05f31f28fb9309e91df', 'reject-id', '', 2, false);
insert into wf_element_data values ('aa87e149e75de05f31f28fb9309e91df', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('aaa5103974ec9059469823d84d8ce109', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('aaa5103974ec9059469823d84d8ce109', 'assignee', 'rel_manager', 1, true);
insert into wf_element_data values ('aaa5103974ec9059469823d84d8ce109', 'reject-id', '', 2, false);
insert into wf_element_data values ('aaa5103974ec9059469823d84d8ce109', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('ac4c84456b64ae08c1cfeb56234e36d5', 'is-default', 'N', 2, false);
insert into wf_element_data values ('ac4c84456b64ae08c1cfeb56234e36d5', 'condition-value', '', 3, false);
insert into wf_element_data values ('ac4c84456b64ae08c1cfeb56234e36d5', 'start-id', 'aaa5103974ec9059469823d84d8ce109', 4, true);
insert into wf_element_data values ('ac4c84456b64ae08c1cfeb56234e36d5', 'start-name', '승인요청', 5, false);
insert into wf_element_data values ('ac4c84456b64ae08c1cfeb56234e36d5', 'end-id', 'a8a1e1db38cdfdbaa692e14db009c224', 6, true);
insert into wf_element_data values ('ac4c84456b64ae08c1cfeb56234e36d5', 'end-name', '승인', 7, false);
insert into wf_element_data values ('aeacaa70d21347699f5c311163d27085', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('aeacaa70d21347699f5c311163d27085', 'assignee', 'rel_manager', 1, true);
insert into wf_element_data values ('aeacaa70d21347699f5c311163d27085', 'reject-id', '', 2, false);
insert into wf_element_data values ('aeacaa70d21347699f5c311163d27085', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('afb41eb311d9c3e7248870e304fc9789', 'action-name', '', 0, false);
insert into wf_element_data values ('a1127cf2ba198dfdd24745876e4616cb', 'action-name', '완료보고서', 0, false);
insert into wf_element_data values ('a1127cf2ba198dfdd24745876e4616cb', 'action-value', 'finish_report', 1, false);
insert into wf_element_data values ('a06f9fbcebe8760486d8db746a025bea', 'action-name', '', 0, false);
insert into wf_element_data values ('a06f9fbcebe8760486d8db746a025bea', 'action-value', '', 1, false);
insert into wf_element_data values ('a06f9fbcebe8760486d8db746a025bea', 'is-default', 'N', 2, false);
insert into wf_element_data values ('a06f9fbcebe8760486d8db746a025bea', 'condition-value', '', 3, false);
insert into wf_element_data values ('a06f9fbcebe8760486d8db746a025bea', 'start-id', 'a905a563d43599def0638503401bcad0', 4, true);
insert into wf_element_data values ('a06f9fbcebe8760486d8db746a025bea', 'start-name', '', 5, false);
insert into wf_element_data values ('a06f9fbcebe8760486d8db746a025bea', 'end-id', 'a61866d755fa520ac6fcca0245f91001', 6, true);
insert into wf_element_data values ('a06f9fbcebe8760486d8db746a025bea', 'end-name', '이행작업', 7, false);
insert into wf_element_data values ('a1127cf2ba198dfdd24745876e4616cb', 'is-default', 'N', 2, false);
insert into wf_element_data values ('a1127cf2ba198dfdd24745876e4616cb', 'condition-value', '', 3, false);
insert into wf_element_data values ('a1127cf2ba198dfdd24745876e4616cb', 'start-id', 'a258e0bfcfae24e99f75be596efc173e', 4, true);
insert into wf_element_data values ('a1127cf2ba198dfdd24745876e4616cb', 'start-name', '완료보고서', 5, false);
insert into wf_element_data values ('a1127cf2ba198dfdd24745876e4616cb', 'end-id', 'ab0f37a1b3f2a2618fbf2be136104ed2', 6, true);
insert into wf_element_data values ('a1127cf2ba198dfdd24745876e4616cb', 'end-name', '종료', 7, false);
insert into wf_element_data values ('a1184c7bed5efd25324bc0d70b1bfdf5', 'condition-item', '${rel_emergency}', 0, true);
insert into wf_element_data values ('a24aa3c4a99fd3d8a4510286045a2f28', 'action-name', '', 0, false);
insert into wf_element_data values ('a24aa3c4a99fd3d8a4510286045a2f28', 'action-value', '', 1, false);
insert into wf_element_data values ('a24aa3c4a99fd3d8a4510286045a2f28', 'is-default', 'N', 2, false);
insert into wf_element_data values ('a24aa3c4a99fd3d8a4510286045a2f28', 'condition-value', '', 3, false);
insert into wf_element_data values ('a24aa3c4a99fd3d8a4510286045a2f28', 'start-id', 'a61866d755fa520ac6fcca0245f91001', 4, true);
insert into wf_element_data values ('a24aa3c4a99fd3d8a4510286045a2f28', 'start-name', '이행작업', 5, false);
insert into wf_element_data values ('a24aa3c4a99fd3d8a4510286045a2f28', 'end-id', 'a82fb95a6550c3cd5dadedd7f597c598', 6, true);
insert into wf_element_data values ('a24aa3c4a99fd3d8a4510286045a2f28', 'end-name', '작업결과서', 7, false);
insert into wf_element_data values ('a258e0bfcfae24e99f75be596efc173e', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('a258e0bfcfae24e99f75be596efc173e', 'assignee', 'rel_manager', 1, true);
insert into wf_element_data values ('a258e0bfcfae24e99f75be596efc173e', 'reject-id', '', 2, false);
insert into wf_element_data values ('a258e0bfcfae24e99f75be596efc173e', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('a29b7166bf3d1e6eb1f743433e1a222a', 'is-default', 'N', 2, false);
insert into wf_element_data values ('a29b7166bf3d1e6eb1f743433e1a222a', 'condition-value', '', 3, false);
insert into wf_element_data values ('a29b7166bf3d1e6eb1f743433e1a222a', 'start-id', 'aa87e149e75de05f31f28fb9309e91df', 4, true);
insert into wf_element_data values ('a29b7166bf3d1e6eb1f743433e1a222a', 'start-name', '작업 계획서', 5, false);
insert into wf_element_data values ('a29b7166bf3d1e6eb1f743433e1a222a', 'end-id', 'a1184c7bed5efd25324bc0d70b1bfdf5', 6, true);
insert into wf_element_data values ('a29b7166bf3d1e6eb1f743433e1a222a', 'end-name', '', 7, false);
insert into wf_element_data values ('a33729f914943aab418ea1e7adad0ee4', 'is-default', 'N', 2, false);
insert into wf_element_data values ('a33729f914943aab418ea1e7adad0ee4', 'condition-value', '', 3, false);
insert into wf_element_data values ('a33729f914943aab418ea1e7adad0ee4', 'start-id', 'aeacaa70d21347699f5c311163d27085', 4, true);
insert into wf_element_data values ('a33729f914943aab418ea1e7adad0ee4', 'start-name', '접수', 5, false);
insert into wf_element_data values ('a33729f914943aab418ea1e7adad0ee4', 'end-id', 'aa87e149e75de05f31f28fb9309e91df', 6, true);
insert into wf_element_data values ('a33729f914943aab418ea1e7adad0ee4', 'end-name', '작업 계획서', 7, false);
insert into wf_element_data values ('a51a85edc794cb15dbf60bf98790a8b0', 'action-name', '', 0, false);
insert into wf_element_data values ('a51a85edc794cb15dbf60bf98790a8b0', 'action-value', '', 1, false);
insert into wf_element_data values ('a51a85edc794cb15dbf60bf98790a8b0', 'is-default', 'Y', 2, false);
insert into wf_element_data values ('a51a85edc794cb15dbf60bf98790a8b0', 'condition-value', '', 3, false);
insert into wf_element_data values ('a51a85edc794cb15dbf60bf98790a8b0', 'start-id', 'a1184c7bed5efd25324bc0d70b1bfdf5', 4, true);
insert into wf_element_data values ('a51a85edc794cb15dbf60bf98790a8b0', 'start-name', '', 5, false);
insert into wf_element_data values ('a51a85edc794cb15dbf60bf98790a8b0', 'end-id', 'aaa5103974ec9059469823d84d8ce109', 6, true);
insert into wf_element_data values ('a51a85edc794cb15dbf60bf98790a8b0', 'end-name', '승인요청', 7, false);
insert into wf_element_data values ('a61866d755fa520ac6fcca0245f91001', 'complete-action', '', 0, false);
insert into wf_element_data values ('a71cf915028b1242f725d2fa4ebeb711', 'action-name', '승인', 0, false);
insert into wf_element_data values ('a71cf915028b1242f725d2fa4ebeb711', 'is-default', 'N', 2, false);
insert into wf_element_data values ('a71cf915028b1242f725d2fa4ebeb711', 'condition-value', '', 3, false);
insert into wf_element_data values ('a71cf915028b1242f725d2fa4ebeb711', 'start-id', 'a8a1e1db38cdfdbaa692e14db009c224', 4, true);
insert into wf_element_data values ('a71cf915028b1242f725d2fa4ebeb711', 'start-name', '승인', 5, false);
insert into wf_element_data values ('a71cf915028b1242f725d2fa4ebeb711', 'end-id', 'a905a563d43599def0638503401bcad0', 6, true);
insert into wf_element_data values ('a71cf915028b1242f725d2fa4ebeb711', 'end-name', '', 7, false);
insert into wf_element_data values ('a71cf915028b1242f725d2fa4ebeb711', 'action-value', 'approval', 1, false);
insert into wf_element_data values ('a33729f914943aab418ea1e7adad0ee4', 'action-name', '접수', 0, false);
insert into wf_element_data values ('a29b7166bf3d1e6eb1f743433e1a222a', 'action-name', '작업계획서', 0, false);
insert into wf_element_data values ('a29b7166bf3d1e6eb1f743433e1a222a', 'action-value', 'work_plan_report', 1, false);
insert into wf_element_data values ('a33729f914943aab418ea1e7adad0ee4', 'action-value', 'accept', 1, false);
insert into wf_element_data values ('afb41eb311d9c3e7248870e304fc9789', 'action-value', '', 1, false);
insert into wf_element_data values ('afb41eb311d9c3e7248870e304fc9789', 'is-default', 'N', 2, false);
insert into wf_element_data values ('afb41eb311d9c3e7248870e304fc9789', 'condition-value', '', 3, false);
insert into wf_element_data values ('afb41eb311d9c3e7248870e304fc9789', 'start-id', 'ac339f9a492736b1cbe9aac6db3721a0', 4, true);
insert into wf_element_data values ('afb41eb311d9c3e7248870e304fc9789', 'start-name', '', 5, false);
insert into wf_element_data values ('afb41eb311d9c3e7248870e304fc9789', 'end-id', 'aeacaa70d21347699f5c311163d27085', 6, true);
insert into wf_element_data values ('afb41eb311d9c3e7248870e304fc9789', 'end-name', '접수', 7, false);