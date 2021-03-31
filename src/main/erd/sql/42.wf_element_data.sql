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
insert into wf_element_data values ('04fd8aee89814e479ffb8058b115b466', 'action-name', '변경결과등록', 0, false);
insert into wf_element_data values ('04fd8aee89814e479ffb8058b115b466', 'action-value', 'chg_result', 1, false);
insert into wf_element_data values ('04fd8aee89814e479ffb8058b115b466', 'is-default', 'N', 2, false);
insert into wf_element_data values ('04fd8aee89814e479ffb8058b115b466', 'condition-value', '', 3, false);
insert into wf_element_data values ('04fd8aee89814e479ffb8058b115b466', 'start-id', 'c9d735a5aca04835ba7f09f63f768a58', 4, true);
insert into wf_element_data values ('04fd8aee89814e479ffb8058b115b466', 'start-name', '변경결과 등록', 5, false);
insert into wf_element_data values ('04fd8aee89814e479ffb8058b115b466', 'end-id', '23bd54bbcaf642a487726b6f0dac4b4c', 6, true);
insert into wf_element_data values ('04fd8aee89814e479ffb8058b115b466', 'end-name', '승인요청', 7, false);
insert into wf_element_data values ('120af605678049f3b2d2b267231d19cc', 'action-name', '', 0, false);
insert into wf_element_data values ('120af605678049f3b2d2b267231d19cc', 'action-value', '', 1, false);
insert into wf_element_data values ('120af605678049f3b2d2b267231d19cc', 'condition-value', '', 2, false);
insert into wf_element_data values ('120af605678049f3b2d2b267231d19cc', 'end-id', 'c9d735a5aca04835ba7f09f63f768a58', 3, true);
insert into wf_element_data values ('120af605678049f3b2d2b267231d19cc', 'end-name', '변경결과 등록', 4, false);
insert into wf_element_data values ('120af605678049f3b2d2b267231d19cc', 'is-default', 'N', 5, false);
insert into wf_element_data values ('120af605678049f3b2d2b267231d19cc', 'start-id', '2e322bca68bd49e6af7015a7381b66ed', 6, true);
insert into wf_element_data values ('120af605678049f3b2d2b267231d19cc', 'start-name', '', 7, false);
insert into wf_element_data values ('131128229e894ff9afe21a2b7cc27c28', 'action-name', '승인요청', 0, false);
insert into wf_element_data values ('131128229e894ff9afe21a2b7cc27c28', 'action-value', 'chg_job_plan', 1, false);
insert into wf_element_data values ('131128229e894ff9afe21a2b7cc27c28', 'is-default', 'N', 2, false);
insert into wf_element_data values ('131128229e894ff9afe21a2b7cc27c28', 'condition-value', '', 3, false);
insert into wf_element_data values ('131128229e894ff9afe21a2b7cc27c28', 'start-id', '203038f6a6c54dec9824275a4c093b61', 4, true);
insert into wf_element_data values ('131128229e894ff9afe21a2b7cc27c28', 'start-name', '승인요청', 5, false);
insert into wf_element_data values ('131128229e894ff9afe21a2b7cc27c28', 'end-id', 'd0d6c6e94f574915bd88257467b6434c', 6, true);
insert into wf_element_data values ('131128229e894ff9afe21a2b7cc27c28', 'end-name', '승인', 7, false);
insert into wf_element_data values ('20212f1027974110ab50990680b2a0e5', 'action-name', '승인요청', 0, false);
insert into wf_element_data values ('20212f1027974110ab50990680b2a0e5', 'action-value', 'chg_result_cmt', 1, false);
insert into wf_element_data values ('20212f1027974110ab50990680b2a0e5', 'is-default', 'N', 2, false);
insert into wf_element_data values ('20212f1027974110ab50990680b2a0e5', 'condition-value', '', 3, false);
insert into wf_element_data values ('20212f1027974110ab50990680b2a0e5', 'start-id', '23bd54bbcaf642a487726b6f0dac4b4c', 4, true);
insert into wf_element_data values ('20212f1027974110ab50990680b2a0e5', 'start-name', '승인요청', 5, false);
insert into wf_element_data values ('20212f1027974110ab50990680b2a0e5', 'end-id', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 6, true);
insert into wf_element_data values ('20212f1027974110ab50990680b2a0e5', 'end-name', '승인', 7, false);
insert into wf_element_data values ('203038f6a6c54dec9824275a4c093b61', 'assignee', 'chg_manager_user', 0, true);
insert into wf_element_data values ('203038f6a6c54dec9824275a4c093b61', 'assignee-type', 'assignee.type.assignee', 1, true);
insert into wf_element_data values ('203038f6a6c54dec9824275a4c093b61', 'reject-id', '', 2, false);
insert into wf_element_data values ('203038f6a6c54dec9824275a4c093b61', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('23bd54bbcaf642a487726b6f0dac4b4c', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('23bd54bbcaf642a487726b6f0dac4b4c', 'assignee', 'chg_manager_user', 1, true);
insert into wf_element_data values ('23bd54bbcaf642a487726b6f0dac4b4c', 'reject-id', '', 2, false);
insert into wf_element_data values ('23bd54bbcaf642a487726b6f0dac4b4c', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('24a2398bc10d4021b3e55f54cc500c50', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('24a2398bc10d4021b3e55f54cc500c50', 'assignee', 'chg_manager_user', 1, true);
insert into wf_element_data values ('24a2398bc10d4021b3e55f54cc500c50', 'reject-id', '', 2, false);
insert into wf_element_data values ('24a2398bc10d4021b3e55f54cc500c50', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('328c7d65847a4089a2d7d22b1b6bd7d1', 'action-name', '', 0, false);
insert into wf_element_data values ('328c7d65847a4089a2d7d22b1b6bd7d1', 'action-value', '', 1, false);
insert into wf_element_data values ('328c7d65847a4089a2d7d22b1b6bd7d1', 'is-default', 'N', 2, false);
insert into wf_element_data values ('328c7d65847a4089a2d7d22b1b6bd7d1', 'condition-value', '', 3, false);
insert into wf_element_data values ('328c7d65847a4089a2d7d22b1b6bd7d1', 'start-id', '906a57e38491441fa61401a8964f8398', 4, true);
insert into wf_element_data values ('328c7d65847a4089a2d7d22b1b6bd7d1', 'start-name', '릴리즈관리', 5, false);
insert into wf_element_data values ('328c7d65847a4089a2d7d22b1b6bd7d1', 'end-id', '2e322bca68bd49e6af7015a7381b66ed', 6, true);
insert into wf_element_data values ('328c7d65847a4089a2d7d22b1b6bd7d1', 'end-name', '', 7, false);
insert into wf_element_data values ('5a1417623338462eaf9533a0c23f0455', 'action-name', '승인', 0, false);
insert into wf_element_data values ('5a1417623338462eaf9533a0c23f0455', 'action-value', 'chg_result_approva', 1, false);
insert into wf_element_data values ('5a1417623338462eaf9533a0c23f0455', 'is-default', 'N', 2, false);
insert into wf_element_data values ('5a1417623338462eaf9533a0c23f0455', 'condition-value', '', 3, false);
insert into wf_element_data values ('5a1417623338462eaf9533a0c23f0455', 'start-id', 'a618f89d6cbb4611aeb5e99bf9bb9df8', 4, true);
insert into wf_element_data values ('5a1417623338462eaf9533a0c23f0455', 'start-name', '승인', 5, false);
insert into wf_element_data values ('5a1417623338462eaf9533a0c23f0455', 'end-id', 'e7ae188b6dee468babdfed470859539c', 6, true);
insert into wf_element_data values ('5a1417623338462eaf9533a0c23f0455', 'end-name', '종료', 7, false);
insert into wf_element_data values ('6b04ce8129964511a4491a67e1aedc50', 'action-name', '처리완료', 0, false);
insert into wf_element_data values ('6b04ce8129964511a4491a67e1aedc50', 'action-value', 'complete', 1, false);
insert into wf_element_data values ('6b04ce8129964511a4491a67e1aedc50', 'is-default', 'N', 2, false);
insert into wf_element_data values ('6b04ce8129964511a4491a67e1aedc50', 'condition-value', '', 3, false);
insert into wf_element_data values ('6b04ce8129964511a4491a67e1aedc50', 'start-id', '9aa8d1454f7a4a2caebaa18586338405', 4, true);
insert into wf_element_data values ('6b04ce8129964511a4491a67e1aedc50', 'start-name', '', 5, false);
insert into wf_element_data values ('6b04ce8129964511a4491a67e1aedc50', 'end-id', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 6, true);
insert into wf_element_data values ('6b04ce8129964511a4491a67e1aedc50', 'end-name', '처리완료', 7, false);
insert into wf_element_data values ('7d9dbf9ad83b4e5681c0a6839501039f', 'action-name', '릴리즈관리', 0, false);
insert into wf_element_data values ('7d9dbf9ad83b4e5681c0a6839501039f', 'action-value', 'rel_move', 1, false);
insert into wf_element_data values ('7d9dbf9ad83b4e5681c0a6839501039f', 'is-default', 'N', 2, false);
insert into wf_element_data values ('7d9dbf9ad83b4e5681c0a6839501039f', 'condition-value', '', 3, false);
insert into wf_element_data values ('7d9dbf9ad83b4e5681c0a6839501039f', 'start-id', '9aa8d1454f7a4a2caebaa18586338405', 4, true);
insert into wf_element_data values ('7d9dbf9ad83b4e5681c0a6839501039f', 'start-name', '', 5, false);
insert into wf_element_data values ('7d9dbf9ad83b4e5681c0a6839501039f', 'end-id', '906a57e38491441fa61401a8964f8398', 6, true);
insert into wf_element_data values ('7d9dbf9ad83b4e5681c0a6839501039f', 'end-name', '릴리즈관리', 7, false);
insert into wf_element_data values ('7ebf8681283b4707b4301277fb6bf9b6', 'action-name', '', 0, false);
insert into wf_element_data values ('7ebf8681283b4707b4301277fb6bf9b6', 'action-value', '', 1, false);
insert into wf_element_data values ('7ebf8681283b4707b4301277fb6bf9b6', 'is-default', 'N', 2, false);
insert into wf_element_data values ('7ebf8681283b4707b4301277fb6bf9b6', 'condition-value', '', 3, false);
insert into wf_element_data values ('7ebf8681283b4707b4301277fb6bf9b6', 'start-id', 'ab9efe0f11dd45bab9df92b4a0d5a1e0', 4, true);
insert into wf_element_data values ('7ebf8681283b4707b4301277fb6bf9b6', 'start-name', '처리완료', 5, false);
insert into wf_element_data values ('7ebf8681283b4707b4301277fb6bf9b6', 'end-id', '2e322bca68bd49e6af7015a7381b66ed', 6, true);
insert into wf_element_data values ('7ebf8681283b4707b4301277fb6bf9b6', 'end-name', '', 7, false);
insert into wf_element_data values ('80e3a384a64446e0939f44c6e0d062a2', 'action-name', '변경계획서', 0, false);
insert into wf_element_data values ('80e3a384a64446e0939f44c6e0d062a2', 'action-value', 'chg_job_plan', 1, false);
insert into wf_element_data values ('80e3a384a64446e0939f44c6e0d062a2', 'is-default', 'N', 2, false);
insert into wf_element_data values ('80e3a384a64446e0939f44c6e0d062a2', 'condition-value', '', 3, false);
insert into wf_element_data values ('80e3a384a64446e0939f44c6e0d062a2', 'start-id', 'bc2dcad9f1c441a7ac1a425beffb7772', 4, true);
insert into wf_element_data values ('80e3a384a64446e0939f44c6e0d062a2', 'start-name', '변경계획서', 5, false);
insert into wf_element_data values ('80e3a384a64446e0939f44c6e0d062a2', 'end-id', '203038f6a6c54dec9824275a4c093b61', 6, true);
insert into wf_element_data values ('80e3a384a64446e0939f44c6e0d062a2', 'end-name', '승인요청', 7, false);
insert into wf_element_data values ('906a57e38491441fa61401a8964f8398', 'sub-document-id', '4028b8817880d833017880f5cafc0004', 0, true);
insert into wf_element_data values ('9aa8d1454f7a4a2caebaa18586338405', 'condition-item', '#{action}', 0, true);
insert into wf_element_data values ('9d50c868a38e4eb5b4620a97a4c8fa95', 'action-name', '', 0, false);
insert into wf_element_data values ('9d50c868a38e4eb5b4620a97a4c8fa95', 'action-value', '', 1, false);
insert into wf_element_data values ('9d50c868a38e4eb5b4620a97a4c8fa95', 'condition-value', '', 2, false);
insert into wf_element_data values ('9d50c868a38e4eb5b4620a97a4c8fa95', 'end-id', '24a2398bc10d4021b3e55f54cc500c50', 3, true);
insert into wf_element_data values ('9d50c868a38e4eb5b4620a97a4c8fa95', 'end-name', '접수', 4, false);
insert into wf_element_data values ('9d50c868a38e4eb5b4620a97a4c8fa95', 'is-default', 'N', 5, false);
insert into wf_element_data values ('9d50c868a38e4eb5b4620a97a4c8fa95', 'start-id', '4248c2b5ca9c4b2bb61f640e80c1b3dc', 6, true);
insert into wf_element_data values ('9d50c868a38e4eb5b4620a97a4c8fa95', 'start-name', '', 7, false);
insert into wf_element_data values ('a618f89d6cbb4611aeb5e99bf9bb9df8', 'assignee', 'chg_result_approver', 0, true);
insert into wf_element_data values ('a618f89d6cbb4611aeb5e99bf9bb9df8', 'assignee-type', 'assignee.type.assignee', 1, true);
insert into wf_element_data values ('a618f89d6cbb4611aeb5e99bf9bb9df8', 'reject-id', '23bd54bbcaf642a487726b6f0dac4b4c', 2, false);
insert into wf_element_data values ('a618f89d6cbb4611aeb5e99bf9bb9df8', 'withdraw', 'Y', 3, false);
insert into wf_element_data values ('a74d06e6a768ff1626847b9ec3a84235', 'action-name', '', 0, false);
insert into wf_element_data values ('a74d06e6a768ff1626847b9ec3a84235', 'action-value', '', 1, false);
insert into wf_element_data values ('a74d06e6a768ff1626847b9ec3a84235', 'is-default', 'N', 2, false);
insert into wf_element_data values ('a74d06e6a768ff1626847b9ec3a84235', 'condition-value', '', 3, false);
insert into wf_element_data values ('a74d06e6a768ff1626847b9ec3a84235', 'start-id', 'a7f7a90781ba158a2b53052ffd59bf15', 4, true);
insert into wf_element_data values ('a74d06e6a768ff1626847b9ec3a84235', 'start-name', '처리', 5, false);
insert into wf_element_data values ('a74d06e6a768ff1626847b9ec3a84235', 'end-id', '9aa8d1454f7a4a2caebaa18586338405', 6, true);
insert into wf_element_data values ('a74d06e6a768ff1626847b9ec3a84235', 'end-name', '', 7, false);
insert into wf_element_data values ('a7f7a90781ba158a2b53052ffd59bf15', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('a7f7a90781ba158a2b53052ffd59bf15', 'assignee', 'chg_manager_user', 1, true);
insert into wf_element_data values ('a7f7a90781ba158a2b53052ffd59bf15', 'reject-id', '', 2, false);
insert into wf_element_data values ('a7f7a90781ba158a2b53052ffd59bf15', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('ab20cca4f90b42c6a6ea4394e88594e4', 'action-name', '승인', 0, false);
insert into wf_element_data values ('ab20cca4f90b42c6a6ea4394e88594e4', 'action-value', 'chg_job_plan_approver', 1, false);
insert into wf_element_data values ('ab20cca4f90b42c6a6ea4394e88594e4', 'is-default', 'N', 2, false);
insert into wf_element_data values ('ab20cca4f90b42c6a6ea4394e88594e4', 'condition-value', '', 3, false);
insert into wf_element_data values ('ab20cca4f90b42c6a6ea4394e88594e4', 'start-id', 'd0d6c6e94f574915bd88257467b6434c', 4, true);
insert into wf_element_data values ('ab20cca4f90b42c6a6ea4394e88594e4', 'start-name', '승인', 5, false);
insert into wf_element_data values ('ab20cca4f90b42c6a6ea4394e88594e4', 'end-id', 'a7f7a90781ba158a2b53052ffd59bf15', 6, true);
insert into wf_element_data values ('ab20cca4f90b42c6a6ea4394e88594e4', 'end-name', '처리', 7, false);
insert into wf_element_data values ('ab9efe0f11dd45bab9df92b4a0d5a1e0', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('ab9efe0f11dd45bab9df92b4a0d5a1e0', 'assignee', 'chg_manager_user', 1, true);
insert into wf_element_data values ('ab9efe0f11dd45bab9df92b4a0d5a1e0', 'reject-id', '', 2, false);
insert into wf_element_data values ('ab9efe0f11dd45bab9df92b4a0d5a1e0', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('bc2dcad9f1c441a7ac1a425beffb7772', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('bc2dcad9f1c441a7ac1a425beffb7772', 'assignee', 'chg_manager_user', 1, true);
insert into wf_element_data values ('bc2dcad9f1c441a7ac1a425beffb7772', 'reject-id', '', 2, false);
insert into wf_element_data values ('bc2dcad9f1c441a7ac1a425beffb7772', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('c9d735a5aca04835ba7f09f63f768a58', 'assignee', 'chg_manager_user', 0, true);
insert into wf_element_data values ('c9d735a5aca04835ba7f09f63f768a58', 'assignee-type', 'assignee.type.assignee', 1, true);
insert into wf_element_data values ('c9d735a5aca04835ba7f09f63f768a58', 'reject-id', '', 2, false);
insert into wf_element_data values ('c9d735a5aca04835ba7f09f63f768a58', 'withdraw', 'N', 3, false);
insert into wf_element_data values ('d0d6c6e94f574915bd88257467b6434c', 'assignee-type', 'assignee.type.assignee', 0, true);
insert into wf_element_data values ('d0d6c6e94f574915bd88257467b6434c', 'assignee', 'chg_job_plan_approver', 1, true);
insert into wf_element_data values ('d0d6c6e94f574915bd88257467b6434c', 'reject-id', '203038f6a6c54dec9824275a4c093b61', 2, false);
insert into wf_element_data values ('d0d6c6e94f574915bd88257467b6434c', 'withdraw', 'Y', 3, false);
insert into wf_element_data values ('ecf774011d9c4c0ab353194c3470dfa2', 'action-name', '접수', 0, false);
insert into wf_element_data values ('ecf774011d9c4c0ab353194c3470dfa2', 'action-value', 'chg_accept', 1, false);
insert into wf_element_data values ('ecf774011d9c4c0ab353194c3470dfa2', 'is-default', 'N', 2, false);
insert into wf_element_data values ('ecf774011d9c4c0ab353194c3470dfa2', 'condition-value', '', 3, false);
insert into wf_element_data values ('ecf774011d9c4c0ab353194c3470dfa2', 'start-id', '24a2398bc10d4021b3e55f54cc500c50', 4, true);
insert into wf_element_data values ('ecf774011d9c4c0ab353194c3470dfa2', 'start-name', '접수', 5, false);
insert into wf_element_data values ('ecf774011d9c4c0ab353194c3470dfa2', 'end-id', 'bc2dcad9f1c441a7ac1a425beffb7772', 6, true);
insert into wf_element_data values ('ecf774011d9c4c0ab353194c3470dfa2', 'end-name', '변경계획서', 7, false);