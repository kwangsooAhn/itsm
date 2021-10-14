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
INSERT INTO wf_element VALUES ('ae077d4b051dd0ba41c0faed6e8ad930','csr0000000000000000000000000001p','commonStart','시작','',false,'','{"width":40,"height":40,"position-x":190,"position-y":300}');
INSERT INTO wf_element VALUES ('adab84bb124390e77008596eab4c9e6c','csr0000000000000000000000000001p','arrowConnector','','',false,'','{}');
INSERT INTO wf_element VALUES ('a55083f4bc4491059cfa4d0fc7dd6b4b','csr0000000000000000000000000001p','userTask','신청서 작성','',false,'','{"width":160,"height":40,"position-x":330,"position-y":300}');
INSERT INTO wf_element VALUES ('aa83352b102d7b1d4ae48e7e59f5c912','csr0000000000000000000000000001p','arrowConnector','','',false,'','{}');
INSERT INTO wf_element VALUES ('ad120e2076402481c51d6fdc476ab7f1','csr0000000000000000000000000001p','userTask','신청서 검토','',true,'','{"width":160,"height":40,"position-x":530,"position-y":300}');
INSERT INTO wf_element VALUES ('ae78ad81861f188dcaac632ee907b1e3','csr0000000000000000000000000001p','arrowConnector','','',false,'','{}');
INSERT INTO wf_element VALUES ('a7d36f300cb8f98b4d6f2ff4d18d40c3','csr0000000000000000000000000001p','manualTask','접수','',false,'','{"width":160,"height":40,"position-x":730,"position-y":300}');
INSERT INTO wf_element VALUES ('ae1377ec5a5f3cbefcbfcfe2e972d8ef','csr0000000000000000000000000001p','arrowConnector','','',false,'','{}');
INSERT INTO wf_element VALUES ('a2c82c19d4a250782639f98c3861c5b3','csr0000000000000000000000000001p','userTask','처리','',false,'','{"width":160,"height":40,"position-x":930,"position-y":300}');
INSERT INTO wf_element VALUES ('a4a3d299994a7d7fb2704622df38bd92','csr0000000000000000000000000001p','arrowConnector','','',false,'','{}');
INSERT INTO wf_element VALUES ('ab9d23c048f2a125da0dad37ade2d3c7','csr0000000000000000000000000001p','userTask','승인','',true,'','{"width":160,"height":40,"position-x":930,"position-y":380}');
INSERT INTO wf_element VALUES ('aa8ecd60b3dcb9f6a21b4a0353a59587','csr0000000000000000000000000001p','arrowConnector','','',false,'','{}');
INSERT INTO wf_element VALUES ('a7f89253a73e08d2b9a1d9eb5d932cda','csr0000000000000000000000000001p','signalSend','만족도 평가','',false,'','{"width":40,"height":40,"position-x":1080,"position-y":380}');
INSERT INTO wf_element VALUES ('a674b7ea4f80e97a20986505cbba8f9f','csr0000000000000000000000000001p','arrowConnector','','',false,'','{}');
INSERT INTO wf_element VALUES ('a0a15f45c89c31277d2833346c329a15','csr0000000000000000000000000001p','commonEnd','종료','',false,'','{"width":40,"height":40,"position-x":1170,"position-y":380}');
/* 서비스데스크 - 단순문의 - 만족도 */
INSERT INTO wf_element VALUES ('ade2bd4055242cc12cdb86666f413698','csr0000000000000000000000000002p','commonStart','시작','',false,'','{"width":40,"height":40,"position-x":480,"position-y":370}');
INSERT INTO wf_element VALUES ('aa8ecdf36b3f398e2047d267b00e78b0','csr0000000000000000000000000002p','arrowConnector','','',false,'','{}');
INSERT INTO wf_element VALUES ('a69041dce1c4f1a0d5bee6a380553ae8','csr0000000000000000000000000002p','userTask','만족도 평가','',true,'','{"width":160,"height":40,"position-x":680,"position-y":370}');
INSERT INTO wf_element VALUES ('a25435ee42c720b51c63b7105886f698','csr0000000000000000000000000002p','arrowConnector','','',false,'','{}');
INSERT INTO wf_element VALUES ('a00a18b2e30774df2a305dbaba690bbe','csr0000000000000000000000000002p','commonEnd','종료','',false,'','{"width":40,"height":40,"position-x":880,"position-y":370}');
