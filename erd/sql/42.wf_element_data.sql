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
INSERT INTO wf_element_data VALUES ('adab84bb124390e77008596eab4c9e6c','action-name','',0,false);
INSERT INTO wf_element_data VALUES ('adab84bb124390e77008596eab4c9e6c','action-value','',1,false);
INSERT INTO wf_element_data VALUES ('adab84bb124390e77008596eab4c9e6c','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('adab84bb124390e77008596eab4c9e6c','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('adab84bb124390e77008596eab4c9e6c','start-id','ae077d4b051dd0ba41c0faed6e8ad930',4,true);
INSERT INTO wf_element_data VALUES ('adab84bb124390e77008596eab4c9e6c','start-name','시작',5,false);
INSERT INTO wf_element_data VALUES ('adab84bb124390e77008596eab4c9e6c','end-id','a55083f4bc4491059cfa4d0fc7dd6b4b',6,true);
INSERT INTO wf_element_data VALUES ('adab84bb124390e77008596eab4c9e6c','end-name','신청서 작성',7,false);
INSERT INTO wf_element_data VALUES ('a55083f4bc4491059cfa4d0fc7dd6b4b','assignee-type','assignee.type.candidate.groups',0,true);
INSERT INTO wf_element_data VALUES ('a55083f4bc4491059cfa4d0fc7dd6b4b','assignee','users.general',1,true);
INSERT INTO wf_element_data VALUES ('a55083f4bc4491059cfa4d0fc7dd6b4b','reject-id','',2,false);
INSERT INTO wf_element_data VALUES ('a55083f4bc4491059cfa4d0fc7dd6b4b','withdraw','N',3,false);
INSERT INTO wf_element_data VALUES ('aa83352b102d7b1d4ae48e7e59f5c912','action-name','신청서 등록',0,false);
INSERT INTO wf_element_data VALUES ('aa83352b102d7b1d4ae48e7e59f5c912','action-value','progress',1,false);
INSERT INTO wf_element_data VALUES ('aa83352b102d7b1d4ae48e7e59f5c912','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('aa83352b102d7b1d4ae48e7e59f5c912','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('aa83352b102d7b1d4ae48e7e59f5c912','start-id','a55083f4bc4491059cfa4d0fc7dd6b4b',4,true);
INSERT INTO wf_element_data VALUES ('aa83352b102d7b1d4ae48e7e59f5c912','start-name','신청서 작성',5,false);
INSERT INTO wf_element_data VALUES ('aa83352b102d7b1d4ae48e7e59f5c912','end-id','ad120e2076402481c51d6fdc476ab7f1',6,true);
INSERT INTO wf_element_data VALUES ('aa83352b102d7b1d4ae48e7e59f5c912','end-name','신청서 검토',7,false);
INSERT INTO wf_element_data VALUES ('ad120e2076402481c51d6fdc476ab7f1','assignee-type','assignee.type.candidate.groups',0,true);
INSERT INTO wf_element_data VALUES ('ad120e2076402481c51d6fdc476ab7f1','assignee','serviceDesk.assignee',1,true);
INSERT INTO wf_element_data VALUES ('ad120e2076402481c51d6fdc476ab7f1','reject-id','',2,false);
INSERT INTO wf_element_data VALUES ('ad120e2076402481c51d6fdc476ab7f1','withdraw','N',3,false);
INSERT INTO wf_element_data VALUES ('ae78ad81861f188dcaac632ee907b1e3','action-name','접수',0,false);
INSERT INTO wf_element_data VALUES ('ae78ad81861f188dcaac632ee907b1e3','action-value','progress',1,false);
INSERT INTO wf_element_data VALUES ('ae78ad81861f188dcaac632ee907b1e3','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('ae78ad81861f188dcaac632ee907b1e3','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('ae78ad81861f188dcaac632ee907b1e3','start-id','ad120e2076402481c51d6fdc476ab7f1',4,true);
INSERT INTO wf_element_data VALUES ('ae78ad81861f188dcaac632ee907b1e3','start-name','신청서 검토',5,false);
INSERT INTO wf_element_data VALUES ('ae78ad81861f188dcaac632ee907b1e3','end-id','a7d36f300cb8f98b4d6f2ff4d18d40c3',6,true);
INSERT INTO wf_element_data VALUES ('ae78ad81861f188dcaac632ee907b1e3','end-name','접수',7,false);
INSERT INTO wf_element_data VALUES ('a7d36f300cb8f98b4d6f2ff4d18d40c3','complete-action','',0,false);
INSERT INTO wf_element_data VALUES ('ae1377ec5a5f3cbefcbfcfe2e972d8ef','action-name','',0,false);
INSERT INTO wf_element_data VALUES ('ae1377ec5a5f3cbefcbfcfe2e972d8ef','action-value','',1,false);
INSERT INTO wf_element_data VALUES ('ae1377ec5a5f3cbefcbfcfe2e972d8ef','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('ae1377ec5a5f3cbefcbfcfe2e972d8ef','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('ae1377ec5a5f3cbefcbfcfe2e972d8ef','start-id','a7d36f300cb8f98b4d6f2ff4d18d40c3',4,true);
INSERT INTO wf_element_data VALUES ('ae1377ec5a5f3cbefcbfcfe2e972d8ef','start-name','접수',5,false);
INSERT INTO wf_element_data VALUES ('ae1377ec5a5f3cbefcbfcfe2e972d8ef','end-id','a2c82c19d4a250782639f98c3861c5b3',6,true);
INSERT INTO wf_element_data VALUES ('ae1377ec5a5f3cbefcbfcfe2e972d8ef','end-name','처리',7,false);
INSERT INTO wf_element_data VALUES ('a2c82c19d4a250782639f98c3861c5b3','assignee-type','assignee.type.assignee',0,true);
INSERT INTO wf_element_data VALUES ('a2c82c19d4a250782639f98c3861c5b3','assignee','z-processor',1,true);
INSERT INTO wf_element_data VALUES ('a2c82c19d4a250782639f98c3861c5b3','reject-id','',2,false);
INSERT INTO wf_element_data VALUES ('a2c82c19d4a250782639f98c3861c5b3','withdraw','N',3,false);
INSERT INTO wf_element_data VALUES ('a4a3d299994a7d7fb2704622df38bd92','action-name','승인요청',0,false);
INSERT INTO wf_element_data VALUES ('a4a3d299994a7d7fb2704622df38bd92','action-value','progress',1,false);
INSERT INTO wf_element_data VALUES ('a4a3d299994a7d7fb2704622df38bd92','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('a4a3d299994a7d7fb2704622df38bd92','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('a4a3d299994a7d7fb2704622df38bd92','start-id','a2c82c19d4a250782639f98c3861c5b3',4,true);
INSERT INTO wf_element_data VALUES ('a4a3d299994a7d7fb2704622df38bd92','start-name','처리',5,false);
INSERT INTO wf_element_data VALUES ('a4a3d299994a7d7fb2704622df38bd92','end-id','ab9d23c048f2a125da0dad37ade2d3c7',6,true);
INSERT INTO wf_element_data VALUES ('a4a3d299994a7d7fb2704622df38bd92','end-name','승인',7,false);
INSERT INTO wf_element_data VALUES ('ab9d23c048f2a125da0dad37ade2d3c7','assignee-type','assignee.type.assignee',0,true);
INSERT INTO wf_element_data VALUES ('ab9d23c048f2a125da0dad37ade2d3c7','assignee','z-approver',1,true);
INSERT INTO wf_element_data VALUES ('ab9d23c048f2a125da0dad37ade2d3c7','reject-id','a2c82c19d4a250782639f98c3861c5b3',2,false);
INSERT INTO wf_element_data VALUES ('ab9d23c048f2a125da0dad37ade2d3c7','withdraw','N',3,false);
INSERT INTO wf_element_data VALUES ('aa8ecd60b3dcb9f6a21b4a0353a59587','action-name','승인',0,false);
INSERT INTO wf_element_data VALUES ('aa8ecd60b3dcb9f6a21b4a0353a59587','action-value','progress',1,false);
INSERT INTO wf_element_data VALUES ('aa8ecd60b3dcb9f6a21b4a0353a59587','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('aa8ecd60b3dcb9f6a21b4a0353a59587','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('aa8ecd60b3dcb9f6a21b4a0353a59587','start-id','ab9d23c048f2a125da0dad37ade2d3c7',4,true);
INSERT INTO wf_element_data VALUES ('aa8ecd60b3dcb9f6a21b4a0353a59587','start-name','승인',5,false);
INSERT INTO wf_element_data VALUES ('aa8ecd60b3dcb9f6a21b4a0353a59587','end-id','a7f89253a73e08d2b9a1d9eb5d932cda',6,true);
INSERT INTO wf_element_data VALUES ('aa8ecd60b3dcb9f6a21b4a0353a59587','end-name','만족도 평가',7,false);
INSERT INTO wf_element_data VALUES ('a7f89253a73e08d2b9a1d9eb5d932cda','target-document-list','csr0000000000000000000000000002d',0,true);
INSERT INTO wf_element_data VALUES ('a674b7ea4f80e97a20986505cbba8f9f','action-name','',0,false);
INSERT INTO wf_element_data VALUES ('a674b7ea4f80e97a20986505cbba8f9f','action-value','',1,false);
INSERT INTO wf_element_data VALUES ('a674b7ea4f80e97a20986505cbba8f9f','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('a674b7ea4f80e97a20986505cbba8f9f','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('a674b7ea4f80e97a20986505cbba8f9f','start-id','a7f89253a73e08d2b9a1d9eb5d932cda',4,true);
INSERT INTO wf_element_data VALUES ('a674b7ea4f80e97a20986505cbba8f9f','start-name','만족도 평가',5,false);
INSERT INTO wf_element_data VALUES ('a674b7ea4f80e97a20986505cbba8f9f','end-id','a0a15f45c89c31277d2833346c329a15',6,true);
INSERT INTO wf_element_data VALUES ('a674b7ea4f80e97a20986505cbba8f9f','end-name','종료',7,false);
/* 서비스데스크 - 단순문의 - 만족도 */
INSERT INTO wf_element_data VALUES ('aa8ecdf36b3f398e2047d267b00e78b0','action-name','',0,false);
INSERT INTO wf_element_data VALUES ('aa8ecdf36b3f398e2047d267b00e78b0','action-value','',1,false);
INSERT INTO wf_element_data VALUES ('aa8ecdf36b3f398e2047d267b00e78b0','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('aa8ecdf36b3f398e2047d267b00e78b0','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('aa8ecdf36b3f398e2047d267b00e78b0','start-id','ade2bd4055242cc12cdb86666f413698',4,true);
INSERT INTO wf_element_data VALUES ('aa8ecdf36b3f398e2047d267b00e78b0','start-name','시작',5,false);
INSERT INTO wf_element_data VALUES ('aa8ecdf36b3f398e2047d267b00e78b0','end-id','a69041dce1c4f1a0d5bee6a380553ae8',6,true);
INSERT INTO wf_element_data VALUES ('aa8ecdf36b3f398e2047d267b00e78b0','end-name','만족도 평가',7,false);
INSERT INTO wf_element_data VALUES ('a69041dce1c4f1a0d5bee6a380553ae8','assignee-type','assignee.type.assignee',0,true);
INSERT INTO wf_element_data VALUES ('a69041dce1c4f1a0d5bee6a380553ae8','assignee','z-requester',1,true);
INSERT INTO wf_element_data VALUES ('a69041dce1c4f1a0d5bee6a380553ae8','reject-id','',2,false);
INSERT INTO wf_element_data VALUES ('a69041dce1c4f1a0d5bee6a380553ae8','withdraw','N',3,false);
INSERT INTO wf_element_data VALUES ('a25435ee42c720b51c63b7105886f698','action-name','만족도 평가',0,false);
INSERT INTO wf_element_data VALUES ('a25435ee42c720b51c63b7105886f698','action-value','progress',1,false);
INSERT INTO wf_element_data VALUES ('a25435ee42c720b51c63b7105886f698','is-default','N',2,false);
INSERT INTO wf_element_data VALUES ('a25435ee42c720b51c63b7105886f698','condition-value','',3,false);
INSERT INTO wf_element_data VALUES ('a25435ee42c720b51c63b7105886f698','start-id','a69041dce1c4f1a0d5bee6a380553ae8',4,true);
INSERT INTO wf_element_data VALUES ('a25435ee42c720b51c63b7105886f698','start-name','만족도 평가',5,false);
INSERT INTO wf_element_data VALUES ('a25435ee42c720b51c63b7105886f698','end-id','a00a18b2e30774df2a305dbaba690bbe',6,true);
INSERT INTO wf_element_data VALUES ('a25435ee42c720b51c63b7105886f698','end-name','종료',7,false);
