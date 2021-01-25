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

insert into wf_element_data values ('044f83b8d41341839c64bb52d40bfc9b','assignee-type','assignee.type.candidate.groups',0,TRUE);
insert into wf_element_data values ('044f83b8d41341839c64bb52d40bfc9b','assignee','users.general',1,TRUE);
insert into wf_element_data values ('044f83b8d41341839c64bb52d40bfc9b','reject-id','',2,FALSE);
insert into wf_element_data values ('044f83b8d41341839c64bb52d40bfc9b','withdraw','N',3,FALSE);
insert into wf_element_data values ('1e0b12f83d2d4d3a85950b9a0f06d067','complete-action','',0,FALSE);
insert into wf_element_data values ('297df2c4770748e480376e0fcc903ef4','action-name','',0,FALSE);
insert into wf_element_data values ('297df2c4770748e480376e0fcc903ef4','action-value','',1,FALSE);
insert into wf_element_data values ('297df2c4770748e480376e0fcc903ef4','is-default','N',2,FALSE);
insert into wf_element_data values ('297df2c4770748e480376e0fcc903ef4','condition-value','',3,FALSE);
insert into wf_element_data values ('297df2c4770748e480376e0fcc903ef4','start-id','3f6b15d6d81c451ebcb26eae5e31ae17',4,TRUE);
insert into wf_element_data values ('297df2c4770748e480376e0fcc903ef4','start-name','만족도평가',5,FALSE);
insert into wf_element_data values ('297df2c4770748e480376e0fcc903ef4','end-id','0c6c40925b924ac88808caabfb9dce76',6,TRUE);
insert into wf_element_data values ('297df2c4770748e480376e0fcc903ef4','end-name','종료',7,FALSE);
insert into wf_element_data values ('317cf6711cae4263b549f937e30db546','assignee-type','assignee.type.assignee',0,TRUE);
insert into wf_element_data values ('317cf6711cae4263b549f937e30db546','assignee','assignee',1,TRUE);
insert into wf_element_data values ('317cf6711cae4263b549f937e30db546','reject-id','',2,FALSE);
insert into wf_element_data values ('317cf6711cae4263b549f937e30db546','withdraw','N',3,FALSE);
insert into wf_element_data values ('3bbb1af5cbe147718e512da2c1b2b458','assignee-type','assignee.type.assignee',0,TRUE);
insert into wf_element_data values ('3bbb1af5cbe147718e512da2c1b2b458','assignee','assignee2',1,TRUE);
insert into wf_element_data values ('3bbb1af5cbe147718e512da2c1b2b458','reject-id','317cf6711cae4263b549f937e30db546',2,FALSE);
insert into wf_element_data values ('3bbb1af5cbe147718e512da2c1b2b458','withdraw','N',3,FALSE);
insert into wf_element_data values ('3f6b15d6d81c451ebcb26eae5e31ae17','target-document-list','40288ab7772ea2e301772ea9aba40003',0,TRUE);
insert into wf_element_data values ('44d0ed9d60d544959c853d38d5cabec9','action-name','만족도 평가',0,FALSE);
insert into wf_element_data values ('44d0ed9d60d544959c853d38d5cabec9','action-value','progress',1,FALSE);
insert into wf_element_data values ('44d0ed9d60d544959c853d38d5cabec9','is-default','N',2,FALSE);
insert into wf_element_data values ('44d0ed9d60d544959c853d38d5cabec9','condition-value','',3,FALSE);
insert into wf_element_data values ('44d0ed9d60d544959c853d38d5cabec9','start-id','6e6050b6474a40d79aef93d9ef85098a',4,TRUE);
insert into wf_element_data values ('44d0ed9d60d544959c853d38d5cabec9','start-name','New Task',5,FALSE);
insert into wf_element_data values ('44d0ed9d60d544959c853d38d5cabec9','end-id','602d151b76584626ab765421f067da30',6,TRUE);
insert into wf_element_data values ('44d0ed9d60d544959c853d38d5cabec9','end-name','종료',7,FALSE);
insert into wf_element_data values ('682e1e295c3b4acc80f5f4942a38da8e','action-name','승인',0,FALSE);
insert into wf_element_data values ('682e1e295c3b4acc80f5f4942a38da8e','action-value','progress',1,FALSE);
insert into wf_element_data values ('682e1e295c3b4acc80f5f4942a38da8e','is-default','N',2,FALSE);
insert into wf_element_data values ('682e1e295c3b4acc80f5f4942a38da8e','condition-value','',3,FALSE);
insert into wf_element_data values ('682e1e295c3b4acc80f5f4942a38da8e','start-id','3bbb1af5cbe147718e512da2c1b2b458',4,TRUE);
insert into wf_element_data values ('682e1e295c3b4acc80f5f4942a38da8e','start-name','승인',5,FALSE);
insert into wf_element_data values ('682e1e295c3b4acc80f5f4942a38da8e','end-id','3f6b15d6d81c451ebcb26eae5e31ae17',6,TRUE);
insert into wf_element_data values ('682e1e295c3b4acc80f5f4942a38da8e','end-name','만족도평가',7,FALSE);
insert into wf_element_data values ('68725cdd45f94bf9a9012d39cc08dc6d','action-name','',0,FALSE);
insert into wf_element_data values ('68725cdd45f94bf9a9012d39cc08dc6d','action-value','',1,FALSE);
insert into wf_element_data values ('68725cdd45f94bf9a9012d39cc08dc6d','is-default','N',2,FALSE);
insert into wf_element_data values ('68725cdd45f94bf9a9012d39cc08dc6d','condition-value','',3,FALSE);
insert into wf_element_data values ('68725cdd45f94bf9a9012d39cc08dc6d','start-id','0f2df8d4e3454a4c91448307b1c6a432',4,TRUE);
insert into wf_element_data values ('68725cdd45f94bf9a9012d39cc08dc6d','start-name','',5,FALSE);
insert into wf_element_data values ('68725cdd45f94bf9a9012d39cc08dc6d','end-id','6e6050b6474a40d79aef93d9ef85098a',6,TRUE);
insert into wf_element_data values ('68725cdd45f94bf9a9012d39cc08dc6d','end-name','만족도평가',7,FALSE);
insert into wf_element_data values ('6e6050b6474a40d79aef93d9ef85098a','assignee-type','assignee.type.candidate.groups',0,TRUE);
insert into wf_element_data values ('6e6050b6474a40d79aef93d9ef85098a','assignee','admin',1,TRUE);
insert into wf_element_data values ('6e6050b6474a40d79aef93d9ef85098a','reject-id','',2,FALSE);
insert into wf_element_data values ('6e6050b6474a40d79aef93d9ef85098a','withdraw','N',3,FALSE);
insert into wf_element_data values ('8fbd692e22b4428fa9d3192a861a06ea','assignee-type','assignee.type.assignee',0,TRUE);
insert into wf_element_data values ('8fbd692e22b4428fa9d3192a861a06ea','assignee','assignee',1,TRUE);
insert into wf_element_data values ('8fbd692e22b4428fa9d3192a861a06ea','reject-id','',2,FALSE);
insert into wf_element_data values ('8fbd692e22b4428fa9d3192a861a06ea','withdraw','N',3,FALSE);
insert into wf_element_data values ('a5e1040405dd435f9bf5b745780a6158','action-name','신청등록',0,FALSE);
insert into wf_element_data values ('a5e1040405dd435f9bf5b745780a6158','action-value','progress',1,FALSE);
insert into wf_element_data values ('a5e1040405dd435f9bf5b745780a6158','is-default','N',2,FALSE);
insert into wf_element_data values ('a5e1040405dd435f9bf5b745780a6158','condition-value','',3,FALSE);
insert into wf_element_data values ('a5e1040405dd435f9bf5b745780a6158','start-id','044f83b8d41341839c64bb52d40bfc9b',4,TRUE);
insert into wf_element_data values ('a5e1040405dd435f9bf5b745780a6158','start-name','신청서 작성',5,FALSE);
insert into wf_element_data values ('a5e1040405dd435f9bf5b745780a6158','end-id','8fbd692e22b4428fa9d3192a861a06ea',6,TRUE);
insert into wf_element_data values ('a5e1040405dd435f9bf5b745780a6158','end-name','신청서 검토',7,FALSE);
insert into wf_element_data values ('ab814067c8f243499c559676505ef790','action-name','접수',0,FALSE);
insert into wf_element_data values ('ab814067c8f243499c559676505ef790','action-value','progress',1,FALSE);
insert into wf_element_data values ('ab814067c8f243499c559676505ef790','is-default','N',2,FALSE);
insert into wf_element_data values ('ab814067c8f243499c559676505ef790','condition-value','',3,FALSE);
insert into wf_element_data values ('ab814067c8f243499c559676505ef790','start-id','8fbd692e22b4428fa9d3192a861a06ea',4,TRUE);
insert into wf_element_data values ('ab814067c8f243499c559676505ef790','start-name','신청서 검토',5,FALSE);
insert into wf_element_data values ('ab814067c8f243499c559676505ef790','end-id','1e0b12f83d2d4d3a85950b9a0f06d067',6,TRUE);
insert into wf_element_data values ('ab814067c8f243499c559676505ef790','end-name','접수',7,FALSE);
insert into wf_element_data values ('ae863e288a164f48aaa56b124a8d5113','action-name','승인요청',0,FALSE);
insert into wf_element_data values ('ae863e288a164f48aaa56b124a8d5113','action-value','progress',1,FALSE);
insert into wf_element_data values ('ae863e288a164f48aaa56b124a8d5113','is-default','N',2,FALSE);
insert into wf_element_data values ('ae863e288a164f48aaa56b124a8d5113','condition-value','',3,FALSE);
insert into wf_element_data values ('ae863e288a164f48aaa56b124a8d5113','start-id','317cf6711cae4263b549f937e30db546',4,TRUE);
insert into wf_element_data values ('ae863e288a164f48aaa56b124a8d5113','start-name','처리',5,FALSE);
insert into wf_element_data values ('ae863e288a164f48aaa56b124a8d5113','end-id','3bbb1af5cbe147718e512da2c1b2b458',6,TRUE);
insert into wf_element_data values ('ae863e288a164f48aaa56b124a8d5113','end-name','승인',7,FALSE);
insert into wf_element_data values ('e38faeee0db140cebd714d290a3b3a7b','action-name','',0,FALSE);
insert into wf_element_data values ('e38faeee0db140cebd714d290a3b3a7b','action-value','',1,FALSE);
insert into wf_element_data values ('e38faeee0db140cebd714d290a3b3a7b','is-default','N',2,FALSE);
insert into wf_element_data values ('e38faeee0db140cebd714d290a3b3a7b','condition-value','',3,FALSE);
insert into wf_element_data values ('e38faeee0db140cebd714d290a3b3a7b','start-id','1e0b12f83d2d4d3a85950b9a0f06d067',4,TRUE);
insert into wf_element_data values ('e38faeee0db140cebd714d290a3b3a7b','start-name','접수',5,FALSE);
insert into wf_element_data values ('e38faeee0db140cebd714d290a3b3a7b','end-id','317cf6711cae4263b549f937e30db546',6,TRUE);
insert into wf_element_data values ('e38faeee0db140cebd714d290a3b3a7b','end-name','처리',7,FALSE);
insert into wf_element_data values ('f0abc7d89fd049cbb9d31c7499118741','action-name','',0,FALSE);
insert into wf_element_data values ('f0abc7d89fd049cbb9d31c7499118741','action-value','',1,FALSE);
insert into wf_element_data values ('f0abc7d89fd049cbb9d31c7499118741','is-default','N',2,FALSE);
insert into wf_element_data values ('f0abc7d89fd049cbb9d31c7499118741','condition-value','',3,FALSE);
insert into wf_element_data values ('f0abc7d89fd049cbb9d31c7499118741','start-id','e84bc306a19241cf98691491676df0ec',4,TRUE);
insert into wf_element_data values ('f0abc7d89fd049cbb9d31c7499118741','start-name','',5,FALSE);
insert into wf_element_data values ('f0abc7d89fd049cbb9d31c7499118741','end-id','044f83b8d41341839c64bb52d40bfc9b',6,TRUE);
insert into wf_element_data values ('f0abc7d89fd049cbb9d31c7499118741','end-name','신청서 작성',7,FALSE);