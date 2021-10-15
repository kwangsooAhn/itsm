/**
 * 컴포넌트정보
 */
DROP TABLE IF EXISTS wf_component cascade;

CREATE TABLE wf_component
(
	component_id varchar(128) NOT NULL,
	component_type varchar(100) NOT NULL,
	mapping_id varchar(128),
	is_topic boolean DEFAULT 'false',
	form_row_id varchar(128),
	form_id varchar(128),
	CONSTRAINT wf_component_pk PRIMARY KEY (component_id),
	CONSTRAINT wf_component_fk FOREIGN KEY (form_row_id) REFERENCES wf_form_row (form_row_id),
	CONSTRAINT wf_component_form_fk FOREIGN KEY (form_id) REFERENCES wf_form (form_id)
);

COMMENT ON TABLE wf_component IS '컴포넌트정보';
COMMENT ON COLUMN wf_component.component_id IS '컴포넌트아이디';
COMMENT ON COLUMN wf_component.form_row_id IS '문서양식 ROW 아이디';
COMMENT ON COLUMN wf_component.component_type IS '컴포넌트종류';
COMMENT ON COLUMN wf_component.mapping_id IS '매핑아이디';
COMMENT ON COLUMN wf_component.is_topic IS '토픽여부';
COMMENT ON COLUMN wf_component.form_id IS '폼 아이디';

/* 서비스데스크 - 단순문의 */
INSERT INTO wf_component VALUES ('ae3e2a000d67b4e2e8d83bf36c81260a','image','z-logo',false,'r028b21f7c780ba6017c783244ca01e5','csr0000000000000000000000000001f');
INSERT INTO wf_component VALUES ('aa83af616d59cc86c565a0282153c236','label','z-document-title',false,'r028b21f7c780ba6017c783245d801e8','csr0000000000000000000000000001f');
INSERT INTO wf_component VALUES ('aeaba514c85489bea2ae4c588df41f03','divider','',false,'r028b21f7c780ba6017c7832470e01eb','csr0000000000000000000000000001f');
INSERT INTO wf_component VALUES ('ad15aab4783f55533c6c1f183a4b60cc','inputBox','z-requester',false,'r028b21f7c780ba6017c7832485c01ed','csr0000000000000000000000000001f');
INSERT INTO wf_component VALUES ('ae4f48236ecf493eb0212d77ac14c360','customCode','z-requester-department',false,'r028b21f7c780ba6017c7832485c01ed','csr0000000000000000000000000001f');
INSERT INTO wf_component VALUES ('a71d2d6094b2920f87ba8eaf025af1c3','inputBox','z-requester-phone',false,'r028b21f7c780ba6017c78324a3801f2','csr0000000000000000000000000001f');
INSERT INTO wf_component VALUES ('a4a100bbcd9907aae4e260c8bfa3b45f','inputBox','z-requester-email',false,'r028b21f7c780ba6017c78324a3801f2','csr0000000000000000000000000001f');
INSERT INTO wf_component VALUES ('a115df4520fea935e4482784064c7a51','dateTime','z-request-date',false,'r028b21f7c780ba6017c78324c4301f7','csr0000000000000000000000000001f');
INSERT INTO wf_component VALUES ('a0c3e8d18663483284b7e9fe83d0b824','dateTime','z-request-deadline',false,'r028b21f7c780ba6017c78324c4301f7','csr0000000000000000000000000001f');
INSERT INTO wf_component VALUES ('a75c85bba7aa722f9dcbcbcfad0b6ac7','inputBox','z-request-title',false,'r028b21f7c780ba6017c78324e2701fc','csr0000000000000000000000000001f');
INSERT INTO wf_component VALUES ('ade3320b3bc9dca17651e4c0cdef89e4','customCode','z-request-category',false,'r028b21f7c780ba6017c78324f5001ff','csr0000000000000000000000000001f');
INSERT INTO wf_component VALUES ('a3b140da99dbfb49beb7fa3db09042b4','textArea','z-request-content',false,'r028b21f7c780ba6017c783250730203','csr0000000000000000000000000001f');
INSERT INTO wf_component VALUES ('a9404877fabbd09dc2603f78283fa155','fileUpload','z-request-file',false,'r028b21f7c780ba6017c783251820206','csr0000000000000000000000000001f');
INSERT INTO wf_component VALUES ('ab6d29d8ef257f93de98c655fbaab22a','inputBox','z-acceptor',false,'r028b21f7c780ba6017c783252db020a','csr0000000000000000000000000001f');
INSERT INTO wf_component VALUES ('ab266326aa59ed5bb4411da4142aa24a','customCode','z-acceptor-department',false,'r028b21f7c780ba6017c783252db020a','csr0000000000000000000000000001f');
INSERT INTO wf_component VALUES ('a8e347190f51b1b05200f34d9e8b8ab0','dateTime','z-accept-date',false,'r028b21f7c780ba6017c783254ac020f','csr0000000000000000000000000001f');
INSERT INTO wf_component VALUES ('a71977859617f137f589108c24fa7845','textArea','z-accept-content',false,'r028b21f7c780ba6017c783255fa0212','csr0000000000000000000000000001f');
INSERT INTO wf_component VALUES ('acc02e1f2f51aa01649b6e06f7b35ec4','customCode','z-processor',false,'r028b21f7c780ba6017c783256e80215','csr0000000000000000000000000001f');
INSERT INTO wf_component VALUES ('a83a54dfec6c65dc5983272ec9a956ce','dateTime','z-process-date',false,'r028b21f7c780ba6017c783258800219','csr0000000000000000000000000001f');
INSERT INTO wf_component VALUES ('ae07bae262c57399a4ee8b3dea2fcbab','textArea','z-process-content',false,'r028b21f7c780ba6017c7832599c021c','csr0000000000000000000000000001f');
INSERT INTO wf_component VALUES ('a27c22d07de4231ab41863dab45450f1','fileUpload','z-process-file',false,'r028b21f7c780ba6017c78325ab4021f','csr0000000000000000000000000001f');
INSERT INTO wf_component VALUES ('a4c410628aa58b1863935b4056395b80','customCode','z-approver',false,'r028b21f7c780ba6017c78325bc30222','csr0000000000000000000000000001f');
INSERT INTO wf_component VALUES ('a10c935dbdf1c30b34dfabe5ebc0ba73','dateTime','z-approve-date',false,'r028b21f7c780ba6017c78325d040226','csr0000000000000000000000000001f');
INSERT INTO wf_component VALUES ('ab97abfd66b5654e3f525e617df9afaf','textArea','z-approve-content',false,'r028b21f7c780ba6017c78325e11022a','csr0000000000000000000000000001f');
/* 서비스데스크 - 단순문의 - 만족도 */
INSERT INTO wf_component VALUES ('a605df0d26f09a3d20caaea3977a5c64','image','z-logo',false,'r028b21f7c780ba6017c78334c66023d','csr0000000000000000000000000002f');
INSERT INTO wf_component VALUES ('ae54c20fd574a57845fffaa7bb936eb4','label','z-document-title',false,'r028b21f7c780ba6017c78334d6a0240','csr0000000000000000000000000002f');
INSERT INTO wf_component VALUES ('a7cc83ce97c50faa754c702bf018c505','divider','',false,'r028b21f7c780ba6017c78334e7b0243','csr0000000000000000000000000002f');
INSERT INTO wf_component VALUES ('ac69d66f3ec394b36215cd2ee3983292','radio','z-satisfaction',false,'r028b21f7c780ba6017c78334fa80245','csr0000000000000000000000000002f');
INSERT INTO wf_component VALUES ('ad2996c39febdc13b32d08354169d6ac','textArea','z-satisfaction-content',false,'r028b21f7c780ba6017c783350b00248','csr0000000000000000000000000002f');
