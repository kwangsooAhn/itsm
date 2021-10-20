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

/* 서비스데스크 - 단순문의 */
INSERT INTO wf_element_data VALUES ('337ab138ae9e4250b41be736e0a09c5b','assignee-type','assignee.type.assignee',0,true);
INSERT INTO wf_element_data VALUES ('337ab138ae9e4250b41be736e0a09c5b','assignee','z-approver',1,true);
INSERT INTO wf_element_data VALUES ('337ab138ae9e4250b41be736e0a09c5b','reject-id','53be2caebd5e40e0b6e9ebecce3f16bd',2,false);
INSERT INTO wf_element_data VALUES ('337ab138ae9e4250b41be736e0a09c5b','withdraw','N',3,false);
INSERT INTO wf_element_data VALUES ('3cc34436df104c9eaf6852f52d0ad8a2','target-document-list','4028b21f7c90d996017c91aeff1b0050',0,true);
INSERT INTO wf_element_data VALUES ('4be94f828adb4b5a938b82a25feca589','assignee-type','assignee.type.candidate.groups',0,true);
INSERT INTO wf_element_data VALUES ('4be94f828adb4b5a938b82a25feca589','assignee','serviceDesk.assignee',1,true);
INSERT INTO wf_element_data VALUES ('4be94f828adb4b5a938b82a25feca589','reject-id','',2,false);
INSERT INTO wf_element_data VALUES ('4be94f828adb4b5a938b82a25feca589','withdraw','N',3,false);
INSERT INTO wf_element_data VALUES ('536974f7f4484443acd76b5bd80fc159','action-name','승인',0,false);
INSERT INTO wf_element_data VALUES ('536974f7f4484443acd76b5bd80fc159','action-value','progress',1,false);
INSERT INTO wf_element_data VALUES ('536974f7f4484443acd76b5bd80fc159','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('536974f7f4484443acd76b5bd80fc159','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('536974f7f4484443acd76b5bd80fc159','start-id','337ab138ae9e4250b41be736e0a09c5b',4,true);
INSERT INTO wf_element_data VALUES ('536974f7f4484443acd76b5bd80fc159','start-name','승인',5,false);
INSERT INTO wf_element_data VALUES ('536974f7f4484443acd76b5bd80fc159','end-id','3cc34436df104c9eaf6852f52d0ad8a2',6,true);
INSERT INTO wf_element_data VALUES ('536974f7f4484443acd76b5bd80fc159','end-name','만족도 평가',7,false);
INSERT INTO wf_element_data VALUES ('53be2caebd5e40e0b6e9ebecce3f16bd','assignee','z-processor',0,true);
INSERT INTO wf_element_data VALUES ('53be2caebd5e40e0b6e9ebecce3f16bd','assignee-type','assignee.type.assignee',1,true);
INSERT INTO wf_element_data VALUES ('53be2caebd5e40e0b6e9ebecce3f16bd','reject-id','',2,false);
INSERT INTO wf_element_data VALUES ('53be2caebd5e40e0b6e9ebecce3f16bd','withdraw','N',3,false);
INSERT INTO wf_element_data VALUES ('64c845635dfe43be8fba233999327cee','action-name','접수',0,false);
INSERT INTO wf_element_data VALUES ('64c845635dfe43be8fba233999327cee','action-value','progress',1,false);
INSERT INTO wf_element_data VALUES ('64c845635dfe43be8fba233999327cee','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('64c845635dfe43be8fba233999327cee','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('64c845635dfe43be8fba233999327cee','start-id','4be94f828adb4b5a938b82a25feca589',4,true);
INSERT INTO wf_element_data VALUES ('64c845635dfe43be8fba233999327cee','start-name','신청서 검토',5,false);
INSERT INTO wf_element_data VALUES ('64c845635dfe43be8fba233999327cee','end-id','eba7310bd0dd41cfb88622e8f8e00767',6,true);
INSERT INTO wf_element_data VALUES ('64c845635dfe43be8fba233999327cee','end-name','접수',7,false);
INSERT INTO wf_element_data VALUES ('6c42da85993f4ae9b551ef67b15c5d49','action-name','승인요청',0,false);
INSERT INTO wf_element_data VALUES ('6c42da85993f4ae9b551ef67b15c5d49','action-value','progress',1,false);
INSERT INTO wf_element_data VALUES ('6c42da85993f4ae9b551ef67b15c5d49','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('6c42da85993f4ae9b551ef67b15c5d49','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('6c42da85993f4ae9b551ef67b15c5d49','start-id','53be2caebd5e40e0b6e9ebecce3f16bd',4,true);
INSERT INTO wf_element_data VALUES ('6c42da85993f4ae9b551ef67b15c5d49','start-name','처리',5,false);
INSERT INTO wf_element_data VALUES ('6c42da85993f4ae9b551ef67b15c5d49','end-id','337ab138ae9e4250b41be736e0a09c5b',6,true);
INSERT INTO wf_element_data VALUES ('6c42da85993f4ae9b551ef67b15c5d49','end-name','승인',7,false);
INSERT INTO wf_element_data VALUES ('9c7c235aa4eb43d8a912b2e524264c79','assignee-type','assignee.type.candidate.groups',0,true);
INSERT INTO wf_element_data VALUES ('9c7c235aa4eb43d8a912b2e524264c79','assignee','users.general',1,true);
INSERT INTO wf_element_data VALUES ('9c7c235aa4eb43d8a912b2e524264c79','reject-id','',2,false);
INSERT INTO wf_element_data VALUES ('9c7c235aa4eb43d8a912b2e524264c79','withdraw','N',3,false);
INSERT INTO wf_element_data VALUES ('a727237e2c6f9dbdbbff693ed151c85d','action-name','신청서 등록',0,false);
INSERT INTO wf_element_data VALUES ('a727237e2c6f9dbdbbff693ed151c85d','action-value','progress',1,false);
INSERT INTO wf_element_data VALUES ('a727237e2c6f9dbdbbff693ed151c85d','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('a727237e2c6f9dbdbbff693ed151c85d','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('a727237e2c6f9dbdbbff693ed151c85d','start-id','9c7c235aa4eb43d8a912b2e524264c79',4,true);
INSERT INTO wf_element_data VALUES ('a727237e2c6f9dbdbbff693ed151c85d','start-name','신청서 작성',5,false);
INSERT INTO wf_element_data VALUES ('a727237e2c6f9dbdbbff693ed151c85d','end-id','4be94f828adb4b5a938b82a25feca589',6,true);
INSERT INTO wf_element_data VALUES ('a727237e2c6f9dbdbbff693ed151c85d','end-name','신청서 검토',7,false);
INSERT INTO wf_element_data VALUES ('b579885ee8094ab7989916905212ca03','action-name','',0,false);
INSERT INTO wf_element_data VALUES ('b579885ee8094ab7989916905212ca03','action-value','',1,false);
INSERT INTO wf_element_data VALUES ('b579885ee8094ab7989916905212ca03','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('b579885ee8094ab7989916905212ca03','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('b579885ee8094ab7989916905212ca03','start-id','670f8e3514c54a429b0d04897907fa45',4,true);
INSERT INTO wf_element_data VALUES ('b579885ee8094ab7989916905212ca03','start-name','시작',5,false);
INSERT INTO wf_element_data VALUES ('b579885ee8094ab7989916905212ca03','end-id','9c7c235aa4eb43d8a912b2e524264c79',6,true);
INSERT INTO wf_element_data VALUES ('b579885ee8094ab7989916905212ca03','end-name','신청서 작성',7,false);
INSERT INTO wf_element_data VALUES ('c731ce831af64b798fcbd074d5d932be','action-name','',0,false);
INSERT INTO wf_element_data VALUES ('c731ce831af64b798fcbd074d5d932be','action-value','',1,false);
INSERT INTO wf_element_data VALUES ('c731ce831af64b798fcbd074d5d932be','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('c731ce831af64b798fcbd074d5d932be','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('c731ce831af64b798fcbd074d5d932be','start-id','eba7310bd0dd41cfb88622e8f8e00767',4,true);
INSERT INTO wf_element_data VALUES ('c731ce831af64b798fcbd074d5d932be','start-name','접수',5,false);
INSERT INTO wf_element_data VALUES ('c731ce831af64b798fcbd074d5d932be','end-id','53be2caebd5e40e0b6e9ebecce3f16bd',6,true);
INSERT INTO wf_element_data VALUES ('c731ce831af64b798fcbd074d5d932be','end-name','처리',7,false);
INSERT INTO wf_element_data VALUES ('e577dd3058394669acb40d471d83635e','action-name','',0,false);
INSERT INTO wf_element_data VALUES ('e577dd3058394669acb40d471d83635e','action-value','',1,false);
INSERT INTO wf_element_data VALUES ('e577dd3058394669acb40d471d83635e','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('e577dd3058394669acb40d471d83635e','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('e577dd3058394669acb40d471d83635e','start-id','3cc34436df104c9eaf6852f52d0ad8a2',4,true);
INSERT INTO wf_element_data VALUES ('e577dd3058394669acb40d471d83635e','start-name','만족도 평가',5,false);
INSERT INTO wf_element_data VALUES ('e577dd3058394669acb40d471d83635e','end-id','7a1ec9adb4594335bfbd973c0e35489c',6,true);
INSERT INTO wf_element_data VALUES ('e577dd3058394669acb40d471d83635e','end-name','종료',7,false);
INSERT INTO wf_element_data VALUES ('eba7310bd0dd41cfb88622e8f8e00767','complete-action','',0,false);
/* 서비스데스크 - 단순문의 - 만족도 */
INSERT INTO wf_element_data VALUES ('00d3cbc448594f90a91448a7eef93dcb','assignee-type','assignee.type.assignee',0,true);
INSERT INTO wf_element_data VALUES ('00d3cbc448594f90a91448a7eef93dcb','assignee','z-requester',1,true);
INSERT INTO wf_element_data VALUES ('00d3cbc448594f90a91448a7eef93dcb','reject-id','',2,false);
INSERT INTO wf_element_data VALUES ('00d3cbc448594f90a91448a7eef93dcb','withdraw','N',3,false);
INSERT INTO wf_element_data VALUES ('71ac5aa1ed1142deb364bbc9bea88cca','action-name','만족도 평가',0,false);
INSERT INTO wf_element_data VALUES ('71ac5aa1ed1142deb364bbc9bea88cca','action-value','progress',1,false);
INSERT INTO wf_element_data VALUES ('71ac5aa1ed1142deb364bbc9bea88cca','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('71ac5aa1ed1142deb364bbc9bea88cca','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('71ac5aa1ed1142deb364bbc9bea88cca','start-id','00d3cbc448594f90a91448a7eef93dcb',4,true);
INSERT INTO wf_element_data VALUES ('71ac5aa1ed1142deb364bbc9bea88cca','start-name','만족도 평가',5,false);
INSERT INTO wf_element_data VALUES ('71ac5aa1ed1142deb364bbc9bea88cca','end-id','2b4667523f6445ab835e2b5135ddfecd',6,true);
INSERT INTO wf_element_data VALUES ('71ac5aa1ed1142deb364bbc9bea88cca','end-name','종료',7,false);
INSERT INTO wf_element_data VALUES ('fca1181323704662acc1c234886be58e','action-name','',0,false);
INSERT INTO wf_element_data VALUES ('fca1181323704662acc1c234886be58e','action-value','',1,false);
INSERT INTO wf_element_data VALUES ('fca1181323704662acc1c234886be58e','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('fca1181323704662acc1c234886be58e','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('fca1181323704662acc1c234886be58e','start-id','2a85de200cc44f17a540d9f2ccea6c97',4,true);
INSERT INTO wf_element_data VALUES ('fca1181323704662acc1c234886be58e','start-name','시작',5,false);
INSERT INTO wf_element_data VALUES ('fca1181323704662acc1c234886be58e','end-id','00d3cbc448594f90a91448a7eef93dcb',6,true);
INSERT INTO wf_element_data VALUES ('fca1181323704662acc1c234886be58e','end-name','만족도 평가',7,false);
