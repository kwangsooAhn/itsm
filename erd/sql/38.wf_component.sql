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
INSERT INTO wf_component VALUES ('ae3e2a000d67b4e2e8d83bf36c81260a','image','z-logo',false,'r028b21f7c780ba6017c783244ca01e5','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_component VALUES ('aa83af616d59cc86c565a0282153c236','label','z-document-title',false,'r028b21f7c780ba6017c783245d801e8','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_component VALUES ('aeaba514c85489bea2ae4c588df41f03','divider','',false,'r028b21f7c780ba6017c7832470e01eb','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_component VALUES ('ad15aab4783f55533c6c1f183a4b60cc','inputBox','z-requester',false,'r028b21f7c780ba6017c7832485c01ed','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_component VALUES ('ae4f48236ecf493eb0212d77ac14c360','customCode','z-requester-department',false,'r028b21f7c780ba6017c7832485c01ed','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_component VALUES ('a71d2d6094b2920f87ba8eaf025af1c3','inputBox','z-requester-phone',false,'r028b21f7c780ba6017c78324a3801f2','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_component VALUES ('a4a100bbcd9907aae4e260c8bfa3b45f','inputBox','z-requester-email',false,'r028b21f7c780ba6017c78324a3801f2','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_component VALUES ('a115df4520fea935e4482784064c7a51','dateTime','z-request-date',false,'r028b21f7c780ba6017c78324c4301f7','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_component VALUES ('a0c3e8d18663483284b7e9fe83d0b824','dateTime','z-request-deadline',false,'r028b21f7c780ba6017c78324c4301f7','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_component VALUES ('a75c85bba7aa722f9dcbcbcfad0b6ac7','inputBox','z-request-title',false,'r028b21f7c780ba6017c78324e2701fc','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_component VALUES ('ade3320b3bc9dca17651e4c0cdef89e4','customCode','z-request-category',false,'r028b21f7c780ba6017c78324f5001ff','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_component VALUES ('a3b140da99dbfb49beb7fa3db09042b4','textArea','z-request-content',false,'r028b21f7c780ba6017c783250730203','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_component VALUES ('a9404877fabbd09dc2603f78283fa155','fileUpload','z-request-file',false,'r028b21f7c780ba6017c783251820206','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_component VALUES ('ab6d29d8ef257f93de98c655fbaab22a','inputBox','z-acceptor',false,'r028b21f7c780ba6017c783252db020a','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_component VALUES ('ab266326aa59ed5bb4411da4142aa24a','customCode','z-acceptor-department',false,'r028b21f7c780ba6017c783252db020a','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_component VALUES ('a8e347190f51b1b05200f34d9e8b8ab0','dateTime','z-accept-date',false,'r028b21f7c780ba6017c783254ac020f','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_component VALUES ('a71977859617f137f589108c24fa7845','textArea','z-accept-content',false,'r028b21f7c780ba6017c783255fa0212','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_component VALUES ('acc02e1f2f51aa01649b6e06f7b35ec4','customCode','z-processor',false,'r028b21f7c780ba6017c783256e80215','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_component VALUES ('a83a54dfec6c65dc5983272ec9a956ce','dateTime','z-process-date',false,'r028b21f7c780ba6017c783258800219','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_component VALUES ('ae07bae262c57399a4ee8b3dea2fcbab','textArea','z-process-content',false,'r028b21f7c780ba6017c7832599c021c','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_component VALUES ('a27c22d07de4231ab41863dab45450f1','fileUpload','z-process-file',false,'r028b21f7c780ba6017c78325ab4021f','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_component VALUES ('a4c410628aa58b1863935b4056395b80','customCode','z-approver',false,'r028b21f7c780ba6017c78325bc30222','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_component VALUES ('a10c935dbdf1c30b34dfabe5ebc0ba73','dateTime','z-approve-date',false,'r028b21f7c780ba6017c78325d040226','4028b21f7c9698f4017c973010230003');
INSERT INTO wf_component VALUES ('ab97abfd66b5654e3f525e617df9afaf','textArea','z-approve-content',false,'r028b21f7c780ba6017c78325e11022a','4028b21f7c9698f4017c973010230003');
/* 서비스데스크 - 단순문의 - 만족도 */
INSERT INTO wf_component VALUES ('a605df0d26f09a3d20caaea3977a5c64','image','z-logo',false,'r028b21f7c780ba6017c78334c66023d','4028b21f7c9698f4017c9731ebae004e');
INSERT INTO wf_component VALUES ('ae54c20fd574a57845fffaa7bb936eb4','label','z-document-title',false,'r028b21f7c780ba6017c78334d6a0240','4028b21f7c9698f4017c9731ebae004e');
INSERT INTO wf_component VALUES ('a7cc83ce97c50faa754c702bf018c505','divider','',false,'r028b21f7c780ba6017c78334e7b0243','4028b21f7c9698f4017c9731ebae004e');
INSERT INTO wf_component VALUES ('ac69d66f3ec394b36215cd2ee3983292','radio','z-satisfaction',false,'r028b21f7c780ba6017c78334fa80245','4028b21f7c9698f4017c9731ebae004e');
INSERT INTO wf_component VALUES ('ad2996c39febdc13b32d08354169d6ac','textArea','z-satisfaction-content',false,'r028b21f7c780ba6017c783350b00248','4028b21f7c9698f4017c9731ebae004e');
/* 서비스데스크 - 장애신고 */
INSERT INTO wf_component VALUES ('a371d3cfecb547e4aff813ce0fca711c','image','z-logo',false,'4028b21f7c9adb6a017c9b0613760066','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('e90e4131007f470490a2ffaff402ba2f','label','z-document-title',false,'4028b21f7c9adb6a017c9b0613b00069','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('9a0a95161bbf4039a3bdfcba2a59a5e6','divider','',false,'4028b21f7c9adb6a017c9b0613e5006c','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('77e97f770393455a97c645f7562b3b53','inputBox','z-requester',false,'4028b21f7c9adb6a017c9b061427006e','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('fee68572b7bb4e04b1f27d0cdfe9ad7a','customCode','z-requester-department',false,'4028b21f7c9adb6a017c9b061427006e','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('857ac1b2c7f3424ab297108583b3c5c1','inputBox','z-requester-phone',false,'4028b21f7c9adb6a017c9b0614970073','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('b960f194ab8e4ecda04c319398bf83ea','inputBox','z-requester-email',false,'4028b21f7c9adb6a017c9b0614970073','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('4b8b310945214d0bbd262352802f93c6','dateTime','z-request-date',false,'4028b21f7c9adb6a017c9b0615030078','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('ae79624833414ab3a3ff37582c234aa2','dateTime','z-request-deadline',false,'4028b21f7c9adb6a017c9b0615030078','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('a6eb123ed5ac913c55035f15bb30efce','dateTime','z-incident-date',false,'4028b21f7c9adb6a017c9b061578007e','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('a20925ede2aee30d0c74c4647c0c46dd','dropdown','z-perception-path',false,'4028b21f7c9adb6a017c9b061578007e','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('0138fd96d2ca4075b8d8ff4ef4b9fe10','inputBox','z-request-title',false,'4028b21f7c9adb6a017c9b0615df0083','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('250e5452b08344758d08fb59915c4e95','textArea','z-request-content',false,'4028b21f7c9adb6a017c9b06161e0086','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('fdd00ba8ff1240cdaeaf526a4df96db5','fileUpload','z-request-file',false,'4028b21f7c9adb6a017c9b0616540089','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('b2f7a7991a474b42a0928714afafaa0b','inputBox','z-acceptor',false,'4028b21f7c9adb6a017c9b0616a9008d','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('c3b8421281e2499cbd2bc98098009a07','customCode','z-acceptor-department',false,'4028b21f7c9adb6a017c9b0616a9008d','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('2c0183c3015c414a823c09d13a6e49be','dateTime','z-accept-date',false,'4028b21f7c9adb6a017c9b0617160092','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('cdefccb8cf074e2f8ee1f741e80c483f','textArea','z-accept-content',false,'4028b21f7c9adb6a017c9b06174d0095','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('a7acc6870b4c4e6e147c2feb20bda6b4','ci','z-relation-ci',false,'4028b21f7c9adb6a017c9b0617870098','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('5aa28e3b3e2446258a15b99267bd063c','customCode','z-processor',false,'4028b21f7c9adb6a017c9b0617c5009b','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('966c2137e9d747e0a4ef5953189d596a','dateTime','z-process-date',false,'4028b21f7c9adb6a017c9b061813009f','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('a75adb2ca0f4d7023972794de97e979e','customCode','z-incident-category',false,'4028b21f7c9adb6a017c9b061813009f','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('aee06d995cbcad83c98df177479242c9','dropdown','z-incident-level',false,'4028b21f7c9adb6a017c9b06187e00a4','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('a6b76adaa1172d77d2b6cd62ad98db34','dropdown','z-incident-urgency',false,'4028b21f7c9adb6a017c9b06187e00a4','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('a2052b72aef1717cc785ead95c9b583c','textArea','z-incident-cause',false,'4028b21f7c9adb6a017c9b0618e600a9','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('a81e3ddc48a12baf94aebac8f2a4f6bc','textArea','z-incident-symptom',false,'4028b21f7c9adb6a017c9b06192700ac','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('aa27a4ea8b744f4bab35b7af3c2e913d','textArea','z-process-content',false,'4028b21f7c9adb6a017c9b06196700af','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('a9b723e29458f98a4904336baff53f6a','textArea','z-incident-plan',false,'4028b21f7c9adb6a017c9b06199e00b2','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('afd603a99e8287aa40706c175c697ae7','ci','z-incident-relation-ci',false,'4028b21f7c9adb6a017c9b0619d500b5','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('09f5924aeff4483daf730badbc1379d6','fileUpload','z-process-file',false,'4028b21f7c9adb6a017c9b061a1400b9','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('5a57a673747a46b0924716a33470a723','customCode','z-approver',false,'4028b21f7c9adb6a017c9b061a5600bc','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('39e8b33c67ad420599d9f57c6e23cbee','dateTime','z-approve-date',false,'4028b21f7c9adb6a017c9b061a9f00c0','4028b21f7c90d996017c91af9dcf0051');
INSERT INTO wf_component VALUES ('5dba55dd1d57415ba28a3f1816859793','textArea','z-approve-content',false,'4028b21f7c9adb6a017c9b061ae500c5','4028b21f7c90d996017c91af9dcf0051');
/* 서비스데스크 - 장애신고 - 만족도 */
INSERT INTO wf_component VALUES ('a4257952286a4e6fae6faaeaae7279fd','image','z-logo',false,'4028b21f7c90d996017c914da7b90022','4028b21f7c90d996017c914bce270002');
INSERT INTO wf_component VALUES ('a19693dff90a45b889df568bbe177bcd','label','z-document-title',false,'4028b21f7c90d996017c914da7e10025','4028b21f7c90d996017c914bce270002');
INSERT INTO wf_component VALUES ('d2d8114f31bb47608d1facc6f03e18ac','divider','',false,'4028b21f7c90d996017c914da8090028','4028b21f7c90d996017c914bce270002');
INSERT INTO wf_component VALUES ('5904fbf514df42cbb8d859799d8e7f3f','radio','z-satisfaction',false,'4028b21f7c90d996017c914da836002a','4028b21f7c90d996017c914bce270002');
INSERT INTO wf_component VALUES ('207f974ae4654e7f8331526c504d0152','textArea','z-satisfaction-content',false,'4028b21f7c90d996017c914da85b002d','4028b21f7c90d996017c914bce270002');
/* 서비스데스크 - 서비스요청 */

/* 서비스데스크 - 서비스요청 - 만족도 */
INSERT INTO wf_component VALUES ('c3c6191d9ee748bebc5745bb5be27452','image','z-logo',false,'4028b21f7c90d996017c914eec400041','4028b21f7c90d996017c914e27340030');
INSERT INTO wf_component VALUES ('90eedeed14494ea4b17d98bd4e8d0a69','label','z-document-title',false,'4028b21f7c90d996017c914eec670044','4028b21f7c90d996017c914e27340030');
INSERT INTO wf_component VALUES ('8f33d3a207b04bb886a87e1e01859500','divider','',false,'4028b21f7c90d996017c914eec970047','4028b21f7c90d996017c914e27340030');
INSERT INTO wf_component VALUES ('0986886e23a044659c7bb280347064d2','radio','z-satisfaction',false,'4028b21f7c90d996017c914eecc30049','4028b21f7c90d996017c914e27340030');
INSERT INTO wf_component VALUES ('5e203c7bace44cb58e1c38cee9372404','textArea','z-satisfaction-content',false,'4028b21f7c90d996017c914eecf3004c','4028b21f7c90d996017c914e27340030');
