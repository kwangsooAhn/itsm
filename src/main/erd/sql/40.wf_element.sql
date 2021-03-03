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

insert into wf_element values ('0f2df8d4e3454a4c91448307b1c6a432','40288ab7772ea2e301772ea7ce1b0002','commonStart','','',FALSE,'','{"width":40,"height":40,"position-x":120,"position-y":200}');
insert into wf_element values ('44d0ed9d60d544959c853d38d5cabec9','40288ab7772ea2e301772ea7ce1b0002','arrowConnector','만족도평가','',FALSE,'','{}');
insert into wf_element values ('602d151b76584626ab765421f067da30','40288ab7772ea2e301772ea7ce1b0002','commonEnd','종료','',FALSE,'','{"width":40,"height":40,"position-x":520,"position-y":200}');
insert into wf_element values ('68725cdd45f94bf9a9012d39cc08dc6d','40288ab7772ea2e301772ea7ce1b0002','arrowConnector','','',FALSE,'','{}');
insert into wf_element values ('6e6050b6474a40d79aef93d9ef85098a','40288ab7772ea2e301772ea7ce1b0002','userTask','만족도평가','',FALSE,'','{"width":160,"height":40,"position-x":320,"position-y":200}');
insert into wf_element values ('044f83b8d41341839c64bb52d40bfc9b','40288ab7772ea2e301772eabb9280004','userTask','신청서 작성','',FALSE,'','{"width":160,"height":40,"position-x":210,"position-y":200}');
insert into wf_element values ('0c6c40925b924ac88808caabfb9dce76','40288ab7772ea2e301772eabb9280004','commonEnd','종료','',FALSE,'','{"width":40,"height":40,"position-x":1110,"position-y":280}');
insert into wf_element values ('1e0b12f83d2d4d3a85950b9a0f06d067','40288ab7772ea2e301772eabb9280004','manualTask','접수','',FALSE,'','{"width":160,"height":40,"position-x":630,"position-y":200}');
insert into wf_element values ('297df2c4770748e480376e0fcc903ef4','40288ab7772ea2e301772eabb9280004','arrowConnector','','',FALSE,'','{}');
insert into wf_element values ('317cf6711cae4263b549f937e30db546','40288ab7772ea2e301772eabb9280004','userTask','처리','',FALSE,'','{"width":160,"height":40,"position-x":850,"position-y":200}');
insert into wf_element values ('3bbb1af5cbe147718e512da2c1b2b458','40288ab7772ea2e301772eabb9280004','userTask','승인','',TRUE,'','{"width":160,"height":40,"position-x":850,"position-y":280}');
insert into wf_element values ('3f6b15d6d81c451ebcb26eae5e31ae17','40288ab7772ea2e301772eabb9280004','signalSend','만족도평가','',FALSE,'','{"width":40,"height":40,"position-x":1010,"position-y":280}');
insert into wf_element values ('682e1e295c3b4acc80f5f4942a38da8e','40288ab7772ea2e301772eabb9280004','arrowConnector','','',FALSE,'','{}');
insert into wf_element values ('8fbd692e22b4428fa9d3192a861a06ea','40288ab7772ea2e301772eabb9280004','userTask','신청서검토','',TRUE,'','{"width":160,"height":40,"position-x":420,"position-y":200}');
insert into wf_element values ('a5e1040405dd435f9bf5b745780a6158','40288ab7772ea2e301772eabb9280004','arrowConnector','','',FALSE,'','{}');
insert into wf_element values ('ab814067c8f243499c559676505ef790','40288ab7772ea2e301772eabb9280004','arrowConnector','','',FALSE,'','{}');
insert into wf_element values ('ae863e288a164f48aaa56b124a8d5113','40288ab7772ea2e301772eabb9280004','arrowConnector','','',FALSE,'','{}');
insert into wf_element values ('e38faeee0db140cebd714d290a3b3a7b','40288ab7772ea2e301772eabb9280004','arrowConnector','','',FALSE,'','{}');
insert into wf_element values ('e84bc306a19241cf98691491676df0ec','40288ab7772ea2e301772eabb9280004','commonStart','','',FALSE,'','{"width":40,"height":40,"position-x":70,"position-y":200}');
insert into wf_element values ('f0abc7d89fd049cbb9d31c7499118741','40288ab7772ea2e301772eabb9280004','arrowConnector','','',FALSE,'','{}');
insert into wf_element values ('01430d9b3cfe427b89f9203185e35530','40288ab777f04ed90177f057ca410000','arrowConnector','','',FALSE,'','{}');
insert into wf_element values ('1318f304ca2a420e9c6756438daa4e3a','40288ab777f04ed90177f057ca410000','arrowConnector','','',FALSE,'','{}');
insert into wf_element values ('2a8341aa1dcb4ab7ab89271020c748c8','40288ab777f04ed90177f057ca410000','arrowConnector','','',FALSE,'','{}');
insert into wf_element values ('4f296c1468f3422b8c59c97151e2c476','40288ab777f04ed90177f057ca410000','userTask','신청서 작성','',FALSE,'','{"width":160,"height":40,"position-x":210,"position-y":200}');
insert into wf_element values ('70e8f5da83584cba81bd9ff597963c4a','40288ab777f04ed90177f057ca410000','arrowConnector','','',FALSE,'','{}');
insert into wf_element values ('957ac0be921c4337999fcbca2f22a92d','40288ab777f04ed90177f057ca410000','arrowConnector','','',FALSE,'','{}');
insert into wf_element values ('a34268ba767d48e7ab7db9e7297e7300','40288ab777f04ed90177f057ca410000','userTask','구성변경 처리','',FALSE,'','{"width":160,"height":40,"position-x":850,"position-y":200}');
insert into wf_element values ('a4aa971e1f952df93f07e932ab25fbf6','40288ab777f04ed90177f057ca410000','scriptTask','CMDB 적용','',FALSE,'','{"width":160,"height":40,"position-x":1060,"position-y":280}');
insert into wf_element values ('a4bd7d4950b4226ea3dfc20bf15193ff','40288ab777f04ed90177f057ca410000','arrowConnector','','',FALSE,'','{}');
insert into wf_element values ('a508191382c644b289c01cf32bed8722','40288ab777f04ed90177f057ca410000','userTask','변경결과 승인','',TRUE,'','{"width":160,"height":40,"position-x":850,"position-y":280}');
insert into wf_element values ('a9a0c170a6ff5ae938f128a726fc2a60','40288ab777f04ed90177f057ca410000','commonEnd','종료','',FALSE,'','{"width":40,"height":40,"position-x":1210,"position-y":280}');
insert into wf_element values ('ad56214bb68e7b749f7d336e14172321','40288ab777f04ed90177f057ca410000','arrowConnector','','',FALSE,'','{}');
insert into wf_element values ('b0dd93b1c9864edc8f048713c0d07e12','40288ab777f04ed90177f057ca410000','manualTask','접수','',FALSE,'','{"width":160,"height":40,"position-x":630,"position-y":200}');
insert into wf_element values ('bff880e180164c4f9cedfab4ba282f28','40288ab777f04ed90177f057ca410000','userTask','신청서검토','',TRUE,'','{"width":160,"height":40,"position-x":420,"position-y":200}');
insert into wf_element values ('d099b9ac855a4e7f9a641906549dbcb0','40288ab777f04ed90177f057ca410000','commonStart','','',FALSE,'','{"width":40,"height":40,"position-x":70,"position-y":200}');