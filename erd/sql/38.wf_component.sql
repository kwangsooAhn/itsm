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
INSERT INTO wf_component VALUES ('a75c85bba7aa722f9dcbcbcfad0b6ac7','inputBox','z-request-title',true,'r028b21f7c780ba6017c78324e2701fc','4028b21f7c9698f4017c973010230003');
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
INSERT INTO wf_component VALUES ('0138fd96d2ca4075b8d8ff4ef4b9fe10','inputBox','z-request-title',true,'4028b21f7c9adb6a017c9b0615df0083','4028b21f7c90d996017c91af9dcf0051');
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
INSERT INTO wf_component VALUES ('5d0b0faef24e429ba271e1bb2175d2ff','image','z-logo',false,'4028b21f7c9ff7c8017ca05446750002','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_component VALUES ('7811e5a93319403698481a00386f8b5e','label','z-document-title',false,'4028b21f7c9ff7c8017ca05446b40005','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_component VALUES ('c2972b99571a448ebdb8b2f981412060','divider','',false,'4028b21f7c9ff7c8017ca05446e30008','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_component VALUES ('93d2c6c791ac4824bf94add769417fe4','inputBox','z-requester',false,'4028b21f7c9ff7c8017ca0544721000b','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_component VALUES ('8929fddece384122a219fb7cfbb07312','customCode','z-requester-department',false,'4028b21f7c9ff7c8017ca0544721000b','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_component VALUES ('413ef0d9ef51415c9772baf19911b940','inputBox','z-requester-phone',false,'4028b21f7c9ff7c8017ca054477d0010','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_component VALUES ('3cdd30935a7e4da1b93e82e1ca65747f','inputBox','z-requester-email',false,'4028b21f7c9ff7c8017ca054477d0010','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_component VALUES ('76f13c0db2894fb5b8dcabc6e3e7a1fc','dateTime','z-request-date',false,'4028b21f7c9ff7c8017ca05447cf0015','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_component VALUES ('3304af52ee0a4009aaadb3917d5de7f8','dateTime','z-request-deadline',false,'4028b21f7c9ff7c8017ca05447cf0015','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_component VALUES ('8fe57acaefef4533b3897867cedb6579','inputBox','z-request-title',true,'4028b21f7c9ff7c8017ca0544826001b','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_component VALUES ('c4591d870dab40e3a18ca6f41250b200','customCode','z-request-category',false,'4028b21f7c9ff7c8017ca0544851001e','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_component VALUES ('4370df5c2ba44544a3444ce38240e61d','textArea','z-request-content',false,'4028b21f7c9ff7c8017ca054487b0021','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_component VALUES ('ea1841546d6648f49bafc2ebbe11c32a','fileUpload','z-request-file',false,'4028b21f7c9ff7c8017ca05448a50024','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_component VALUES ('f1867a396519411c848f65bfbda28d29','inputBox','z-acceptor',false,'4028b21f7c9ff7c8017ca05448e20028','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_component VALUES ('ee30c91397e242b5a1ddae91437b50ea','customCode','z-acceptor-department',false,'4028b21f7c9ff7c8017ca05448e20028','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_component VALUES ('a6d20858d6e84b85b1e75fc28ec36696','dateTime','z-accept-date',false,'4028b21f7c9ff7c8017ca0544932002d','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_component VALUES ('7afa8f35f45f412eb98db57dfd07b6b6','textArea','z-accept-content',false,'4028b21f7c9ff7c8017ca054495f0030','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_component VALUES ('ebcb50c94a9f494c8afd28d95c4aa79a','ci','z-relation-ci',false,'4028b21f7c9ff7c8017ca054498b0033','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_component VALUES ('afa6d40a2b304474ab7f424670587959','customCode','z-processor',false,'4028b21f7c9ff7c8017ca05449b50036','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_component VALUES ('1f3a106094dd4e02b170f20396d526de','dateTime','z-process-date',false,'4028b21f7c9ff7c8017ca05449ef003a','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_component VALUES ('d80c69ade3fc48f1bd78e1b4b02397d7','textArea','z-process-content',false,'4028b21f7c9ff7c8017ca0544a1e003d','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_component VALUES ('68098b156d354c069bfaf660b5b03f9d','textArea','z-process-etc',false,'4028b21f7c9ff7c8017ca0544a490040','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_component VALUES ('7f1e91223181424b89172127908058f5','ci','z-process-relation-ci',false,'4028b21f7c9ff7c8017ca0544a760043','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_component VALUES ('7c77ea3ec86c41ecb6c0b4dc1ddcd9f9','fileUpload','z-process-file',false,'4028b21f7c9ff7c8017ca0544aa30047','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_component VALUES ('8a4339add3c54b3aba9e4864c16c558c','customCode','z-approver',false,'4028b21f7c9ff7c8017ca0544ace004a','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_component VALUES ('ba5482f4fcfc45d7b20a04328670f11b','dateTime','z-approve-date',false,'4028b21f7c9ff7c8017ca0544b09004e','4028b21f7c9cc269017c9cc8cbf60001');
INSERT INTO wf_component VALUES ('23e3399e82b644d09ab20b281860a4cb','textArea','z-approve-content',false,'4028b21f7c9ff7c8017ca0544b390053','4028b21f7c9cc269017c9cc8cbf60001');
/* 서비스데스크 - 서비스요청 - 만족도 */
INSERT INTO wf_component VALUES ('c3c6191d9ee748bebc5745bb5be27452','image','z-logo',false,'4028b21f7c90d996017c914eec400041','4028b21f7c90d996017c914e27340030');
INSERT INTO wf_component VALUES ('90eedeed14494ea4b17d98bd4e8d0a69','label','z-document-title',false,'4028b21f7c90d996017c914eec670044','4028b21f7c90d996017c914e27340030');
INSERT INTO wf_component VALUES ('8f33d3a207b04bb886a87e1e01859500','divider','',false,'4028b21f7c90d996017c914eec970047','4028b21f7c90d996017c914e27340030');
INSERT INTO wf_component VALUES ('0986886e23a044659c7bb280347064d2','radio','z-satisfaction',false,'4028b21f7c90d996017c914eecc30049','4028b21f7c90d996017c914e27340030');
INSERT INTO wf_component VALUES ('5e203c7bace44cb58e1c38cee9372404','textArea','z-satisfaction-content',false,'4028b21f7c90d996017c914eecf3004c','4028b21f7c90d996017c914e27340030');
/* 인프라변경관리 */
INSERT INTO wf_component VALUES ('b5e06a4da8914e08b63a589fe16578db', 'image', 'z-logo', false, '4028b8817cbfc7a7017cc095a56d0ae9', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('44026fa563f649779f65f52dc0bfd0d6', 'label', 'z-change-infra-document-title', false, '4028b8817cbfc7a7017cc095a5800aec', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('a70e12f4514b4377bc9a94eb0f80deeb', 'divider', '', false, '4028b8817cbfc7a7017cc095a58d0af0', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('6df0771bb8a94deda97d0d6ba42ef01e', 'inputBox', 'z-requester', false, '4028b8817cbfc7a7017cc095a5a20af3', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('1a4024f7913e426383187d6bcc7962af', 'customCode', 'z-requester-department', false, '4028b8817cbfc7a7017cc095a5a20af3', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('11ed46ce208341fd842be00ced0e0381', 'inputBox', 'z-requester-phone', false, '4028b8817cbfc7a7017cc095a5c30af8', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('a50504ee81204910a1e13782987b4f89', 'inputBox', 'z-requester-email', false, '4028b8817cbfc7a7017cc095a5c30af8', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('610a36dd4d28448f9ca09623962d64e5', 'dateTime', 'z-request-date', false, '4028b8817cbfc7a7017cc095a5e00afd', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('9bbb34c9d2fe40ba802ee9f674210fe5', 'dateTime', 'z-request-deadline', false, '4028b8817cbfc7a7017cc095a5e00afd', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('c520f41591ca4c18bc89b08e5b58a78b', 'inputBox', 'z-request-title', true, '4028b8817cbfc7a7017cc095a5fd0b02', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('5aa56cbcd2ab48bbbcffd0ba53af499c', 'customCode', 'z-request-category', false, '4028b8817cbfc7a7017cc095a60a0b05', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('654a2e1cbdf14f39a883ff4ab168046c', 'textArea', 'z-request-content', false, '4028b8817cbfc7a7017cc095a61b0b09', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('41ff46f9f19d4e92853118fa1e914877', 'fileUpload', 'z-request-file', false, '4028b8817cbfc7a7017cc095a6290b0c', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('2c260851462b40ea82949561ed0f2b68', 'dateTime', 'z-accept-date', false, '4028b8817cbfc7a7017cc095a63b0b10', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('9e79bf1880094800b3b3d68044f40538', 'inputBox', 'z-change-infra-acceptor', false, '4028b8817cbfc7a7017cc095a64c0b13', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('59ffe940a3464a9ca3683224f7038c41', 'customCode', 'z-change-infra-acceptor-department', false, '4028b8817cbfc7a7017cc095a64c0b13', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('88660c980418450baf87a6d7ef510626', 'customCode', 'z-change-infra-processor', false, '4028b8817cbfc7a7017cc095a6650b18', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('f12492917ac64f418c37cfa98c4ab02d', 'customCode', 'z-change-infra-processor-department', false, '4028b8817cbfc7a7017cc095a6650b18', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('24f2a648a5ba458d9224a61c0cb1cdb0', 'dropdown', 'z-change-infra-impact', false, '4028b8817cbfc7a7017cc095a67e0b1d', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('15cd0e737e9c41c5a27132cfa18f370a', 'dropdown', 'z-change-infra-urgency', false, '4028b8817cbfc7a7017cc095a67e0b1d', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('bb38d2e37255417b8f61e0947e224974', 'textArea', 'z-change-infra-accept-content', false, '4028b8817cbfc7a7017cc095a6950b22', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('c7e485828f2b4c85a7cba5d6b3646b16', 'dateTime', 'z-change-infra-changeControlBoard-date', false, '4028b8817cbfc7a7017cc095a6a60b26', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('6deb6fcad6124c9a907fc4850285e342', 'inputBox', 'z-change-infra-changeControlBoard-meetingRoom', false, '4028b8817cbfc7a7017cc095a6b40b29', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('1d2ea0820d514512a1e3837e04fbc84b', 'inputBox', 'z-change-infra-changeControlBoard-subject', false, '4028b8817cbfc7a7017cc095a6c20b2c', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('2cb51de14f824f62bf9d5f634d276fdc', 'textArea', 'z-change-infra-changeControlBoard-consultation', false, '4028b8817cbfc7a7017cc095a6cf0b2f', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('c27d14120e1044a5b70fe4372ed78c2e', 'textArea', 'z-change-infra-changeControlBoard-result', false, '4028b8817cbfc7a7017cc095a6db0b32', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('3d5bb59a5b264430b2a8d1b19f6741ae', 'dateTime', 'z-change-infra-changePlan-date', false, '4028b8817cbfc7a7017cc095a6f10b36', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('e6d73057dd5a4b2a8cf01e15d3793eca', 'dateTime', 'z-change-infra-changePlan-expected-start-date', false, '4028b8817cbfc7a7017cc095a6fd0b37', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('afe2bec3307442f0be22af203e233f2f', 'dateTime', 'z-change-infra-changePlan-expected-end-date', false, '4028b8817cbfc7a7017cc095a6fd0b37', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('f5595e73f0654239aab9e66f71c528cc', 'radio', 'z-change-infra-changePlan-service-interruption', false, '4028b8817cbfc7a7017cc095a71b0b3c', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('b16f8424688f4e608d1f7b71f1aaf4b5', 'textArea', 'z-change-infra-changePlan-changeContents', false, '4028b8817cbfc7a7017cc095a7280b3f', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('2692ffba732d496498bd2f53f35016f9', 'textArea', 'z-change-infra-changePlan-recovery-failure-plan', false, '4028b8817cbfc7a7017cc095a7330b42', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('961037311d5c4201b869c642abdbf394', 'textArea', 'z-change-infra-changePlan-planContents', false, '4028b8817cbfc7a7017cc095a73f0b45', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('3c8cd6bf769041a68c628c1eb1fa2bd2', 'textArea', 'z-change-infra-changePlan-etc', false, '4028b8817cbfc7a7017cc095a74b0b48', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('f5081a7fd40d46da9b65daf5645906a0', 'ci', 'z-process-relation-ci', false, '4028b8817cbfc7a7017cc095a7570b4b', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('663450c3389c465584685365602dd961', 'fileUpload', 'z-change-infra-changePlan-fileupload', false, '4028b8817cbfc7a7017cc095a7630b4e', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('2437c9c99fc14cb895f2290519515a7b', 'customCode', 'z-change-infra-changePlan-approver', false, '4028b8817cbfc7a7017cc095a7720b51', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('1991ff59e63246ccb13a7946379b14e7', 'dateTime', 'z-change-infra-changePlan-approve-date', false, '4028b8817cbfc7a7017cc095a7840b55', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('51ac11434d1a4c0ca87b4930ffce047c', 'textArea', 'z-change-infra-changePlan-approve-content', false, '4028b8817cbfc7a7017cc095a7910b58', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('5e32d98e8bcd4e8e9cc8cdaea9ee45ff', 'dateTime', 'z-change-infra-changeResult-process-date', false, '4028b8817cbfc7a7017cc095a7a30b5c', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('1c9cb6326b104af88ff79743ce63bcce', 'radio', 'z-change-infra-result', false, '4028b8817cbfc7a7017cc095a7b00b5f', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('45fdeda567e449849075599eaa4a8c06', 'textArea', 'z-change-infra-changeResult-content', false, '4028b8817cbfc7a7017cc095a7bd0b62', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('3f2ea69c92bd42a5a903b0d8884b19ab', 'fileUpload', 'z-change-infra-changeResult-fileupload', false, '4028b8817cbfc7a7017cc095a7ca0b65', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('061ec27ed5e3446db15b256afb694f7b', 'dateTime', 'z-change-infra-postImplementationReview-start-date', false, '4028b8817cbfc7a7017cc095a7e00b69', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('a09757a1c1bb40e381e491474bd8ce75', 'dateTime', 'z-change-infra-postImplementationReview-end-date', false, '4028b8817cbfc7a7017cc095a7e00b69', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('6a78f0ee60434ebab14d1b2854057e4c', 'radio', 'z-change-infra-postImplementationReview-result', false, '4028b8817cbfc7a7017cc095a7f90b6e', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('7ce9995e0c2c40c18b8404f73684bd5d', 'textArea', 'z-change-infra-postImplementationReview-content', false, '4028b8817cbfc7a7017cc095a8060b71', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('9312d808fce84d5fb061a99e6d779c4d', 'customCode', 'z-change-infra-complete-reviewer', false, '4028b8817cbfc7a7017cc095a8120b74', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('d189734cda47408ca2a33a4d6b8d04b2', 'dateTime', 'z-change-infra-complete-review-date', false, '4028b8817cbfc7a7017cc095a8200b78', '4028b8817cbfc7a7017cc08f7b0b0763');
INSERT INTO wf_component VALUES ('60b0b5ffba4047bbb2f2779f40df84ac', 'textArea', 'z-change-infra-complete-review', false, '4028b8817cbfc7a7017cc095a82a0b7b', '4028b8817cbfc7a7017cc08f7b0b0763');