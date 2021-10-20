/**
 * 태그
 */
DROP TABLE IF EXISTS awf_tag cascade;

CREATE TABLE awf_tag
(
	tag_id varchar(128) NOT NULL,
	tag_type varchar(128) NOT NULL,
	tag_value varchar(256) NOT NULL,
	target_id varchar(128) NOT NULL,
    CONSTRAINT awf_tag_pk PRIMARY KEY (tag_id),
    CONSTRAINT awf_tag_un UNIQUE (tag_type, tag_value, target_id)
);

COMMENT ON TABLE awf_tag IS '태그';
COMMENT ON COLUMN awf_tag.tag_id IS '태그아이디';
COMMENT ON COLUMN awf_tag.tag_type IS '태그타입';
COMMENT ON COLUMN awf_tag.tag_value IS '태그내용';
COMMENT ON COLUMN awf_tag.target_id IS '태그대상아이디';

/* 단순문의 */
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c157913004c','component','로고','ae3e2a000d67b4e2e8d83bf36c81260a');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c157914004d','component','z-logo','ae3e2a000d67b4e2e8d83bf36c81260a');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c15791a004f','component','제목','aa83af616d59cc86c565a0282153c236');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c15791b0050','component','z-document-title','aa83af616d59cc86c565a0282153c236');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c15791b0051','component','z-serviceDesk-inquiry','aa83af616d59cc86c565a0282153c236');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c1579290054','component','신청자','ad15aab4783f55533c6c1f183a4b60cc');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c15792a0055','component','z-requester','ad15aab4783f55533c6c1f183a4b60cc');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c15792c0056','component','신청부서','ae4f48236ecf493eb0212d77ac14c360');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c15792d0057','component','z-requester-department','ae4f48236ecf493eb0212d77ac14c360');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c1579360059','component','전화번호','a71d2d6094b2920f87ba8eaf025af1c3');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c157937005a','component','z-requester-phone','a71d2d6094b2920f87ba8eaf025af1c3');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c157939005b','component','이메일','a4a100bbcd9907aae4e260c8bfa3b45f');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c15793a005c','component','z-requester-email','a4a100bbcd9907aae4e260c8bfa3b45f');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c157946005e','component','신청일시','a115df4520fea935e4482784064c7a51');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c157947005f','component','z-request-date','a115df4520fea935e4482784064c7a51');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c1579490060','component','완료희망일시','a0c3e8d18663483284b7e9fe83d0b824');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c1579490061','component','z-request-deadline','a0c3e8d18663483284b7e9fe83d0b824');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c1579490062','component','z-serviceDesk-inquiry-request-deadline','a0c3e8d18663483284b7e9fe83d0b824');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c1579520063','component','z-request-title','a75c85bba7aa722f9dcbcbcfad0b6ac7');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c1579530064','component','제목','a75c85bba7aa722f9dcbcbcfad0b6ac7');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c1579590066','component','z-request-category','ade3320b3bc9dca17651e4c0cdef89e4');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c1579590067','component','서비스항목','ade3320b3bc9dca17651e4c0cdef89e4');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c15795a0068','component','category','ade3320b3bc9dca17651e4c0cdef89e4');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c15795f006a','component','내용','a3b140da99dbfb49beb7fa3db09042b4');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c157960006b','component','z-request-content','a3b140da99dbfb49beb7fa3db09042b4');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c157965006d','component','첨부파일','a9404877fabbd09dc2603f78283fa155');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c157966006e','component','z-request-file','a9404877fabbd09dc2603f78283fa155');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c15796e0071','component','접수자','ab6d29d8ef257f93de98c655fbaab22a');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c15796f0072','component','z-acceptor','ab6d29d8ef257f93de98c655fbaab22a');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c1579700073','component','접수부서','ab266326aa59ed5bb4411da4142aa24a');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c1579710074','component','z-acceptor-department','ab266326aa59ed5bb4411da4142aa24a');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c1579790076','component','접수일시','a8e347190f51b1b05200f34d9e8b8ab0');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c15797a0077','component','z-accept-date','a8e347190f51b1b05200f34d9e8b8ab0');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c15797f0079','component','접수의견','a71977859617f137f589108c24fa7845');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c15797f007a','component','z-accept-content','a71977859617f137f589108c24fa7845');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c157984007c','component','처리자','acc02e1f2f51aa01649b6e06f7b35ec4');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c157985007d','component','z-processor','acc02e1f2f51aa01649b6e06f7b35ec4');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c15798d0080','component','처리일시','a83a54dfec6c65dc5983272ec9a956ce');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c15798e0081','component','z-process-date','a83a54dfec6c65dc5983272ec9a956ce');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c1579930083','component','처리내용','ae07bae262c57399a4ee8b3dea2fcbab');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c1579940084','component','z-process-content','ae07bae262c57399a4ee8b3dea2fcbab');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c1579990086','component','첨부파일','a27c22d07de4231ab41863dab45450f1');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c15799a0087','component','z-process-file','a27c22d07de4231ab41863dab45450f1');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c1579a00089','component','승인자','a4c410628aa58b1863935b4056395b80');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c1579a1008a','component','z-approver','a4c410628aa58b1863935b4056395b80');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c1579a9008d','component','승인일시','a10c935dbdf1c30b34dfabe5ebc0ba73');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c1579aa008e','component','반려일시','a10c935dbdf1c30b34dfabe5ebc0ba73');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c1579ab008f','component','z-approve-date','a10c935dbdf1c30b34dfabe5ebc0ba73');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c1579ab008g','component','z-serviceDesk-inquiry-approve-date','a10c935dbdf1c30b34dfabe5ebc0ba73');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c1579b20091','component','승인의견','ab97abfd66b5654e3f525e617df9afaf');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c1579b30092','component','반려의견','ab97abfd66b5654e3f525e617df9afaf');
INSERT INTO awf_tag VALUES ('2c91808e7c7b0130017c7c1579b40093','component','z-approve-content','ab97abfd66b5654e3f525e617df9afaf');
/* 서비스데스크 - 단순문의 - 만족도 */
INSERT INTO awf_tag VALUES ('4028b21f7c780ba6017c78334c7e023e','component','로고','a605df0d26f09a3d20caaea3977a5c64');
INSERT INTO awf_tag VALUES ('4028b21f7c780ba6017c78334c9c023f','component','z-logo','a605df0d26f09a3d20caaea3977a5c64');
INSERT INTO awf_tag VALUES ('4028b21f7c780ba6017c78334d900241','component','제목','ae54c20fd574a57845fffaa7bb936eb4');
INSERT INTO awf_tag VALUES ('4028b21f7c780ba6017c78334db70242','component','z-document-title','ae54c20fd574a57845fffaa7bb936eb4');
INSERT INTO awf_tag VALUES ('4028b21f7c780ba6017c78334fd20246','component','만족도','ac69d66f3ec394b36215cd2ee3983292');
INSERT INTO awf_tag VALUES ('4028b21f7c780ba6017c78334ff20247','component','z-satisfaction','ac69d66f3ec394b36215cd2ee3983292');
INSERT INTO awf_tag VALUES ('4028b21f7c780ba6017c78334ff20248','component','z-serviceDesk-inquiry-satisfaction','ac69d66f3ec394b36215cd2ee3983292');
INSERT INTO awf_tag VALUES ('4028b21f7c780ba6017c783350cb0249','component','만족도평가의견','ad2996c39febdc13b32d08354169d6ac');
INSERT INTO awf_tag VALUES ('4028b21f7c780ba6017c783350f1024a','component','z-satisfaction-content','ad2996c39febdc13b32d08354169d6ac');
/* 서비스데스크 - 장애신고 */
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b06137c0067','component','로고','a371d3cfecb547e4aff813ce0fca711c');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0613820068','component','z-logo','a371d3cfecb547e4aff813ce0fca711c');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0613b6006a','component','제목','e90e4131007f470490a2ffaff402ba2f');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0613bd006b','component','z-document-title','e90e4131007f470490a2ffaff402ba2f');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b061436006f','component','신청자','77e97f770393455a97c645f7562b3b53');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b06143d0070','component','z-requester','77e97f770393455a97c645f7562b3b53');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b06144a0071','component','신청부서','fee68572b7bb4e04b1f27d0cdfe9ad7a');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0614500072','component','z-requester-department','fee68572b7bb4e04b1f27d0cdfe9ad7a');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b06149e0074','component','전화번호','857ac1b2c7f3424ab297108583b3c5c1');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0614a50075','component','z-requester-phone','857ac1b2c7f3424ab297108583b3c5c1');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0614b50076','component','이메일','b960f194ab8e4ecda04c319398bf83ea');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0614bb0077','component','z-requester-email','b960f194ab8e4ecda04c319398bf83ea');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b06150a0079','component','신청일시','4b8b310945214d0bbd262352802f93c6');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b061511007a','component','z-request-date','4b8b310945214d0bbd262352802f93c6');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b06151f007b','component','완료희망일시','ae79624833414ab3a3ff37582c234aa2');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b061527007c','component','z-request-deadline','ae79624833414ab3a3ff37582c234aa2');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b061530007d','component','z-serviceDesk-incident-request-deadline','ae79624833414ab3a3ff37582c234aa2');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b06157f007f','component','장애발생일시','a6eb123ed5ac913c55035f15bb30efce');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0615850080','component','z-incident-date','a6eb123ed5ac913c55035f15bb30efce');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0615920081','component','장애인지경로','a20925ede2aee30d0c74c4647c0c46dd');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0615970082','component','z-perception-path','a20925ede2aee30d0c74c4647c0c46dd');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0615e60084','component','z-request-title','0138fd96d2ca4075b8d8ff4ef4b9fe10');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0615ee0085','component','제목','0138fd96d2ca4075b8d8ff4ef4b9fe10');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0616260087','component','내용','250e5452b08344758d08fb59915c4e95');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b06162d0088','component','z-request-content','250e5452b08344758d08fb59915c4e95');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b06165c008a','component','첨부파일','fdd00ba8ff1240cdaeaf526a4df96db5');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b061665008b','component','z-request-file','fdd00ba8ff1240cdaeaf526a4df96db5');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0616b2008e','component','접수자','b2f7a7991a474b42a0928714afafaa0b');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0616b8008f','component','z-acceptor','b2f7a7991a474b42a0928714afafaa0b');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0616c80090','component','접수부서','c3b8421281e2499cbd2bc98098009a07');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0616cf0091','component','z-acceptor-department','c3b8421281e2499cbd2bc98098009a07');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b06171c0093','component','접수일시','2c0183c3015c414a823c09d13a6e49be');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0617240094','component','z-accept-date','2c0183c3015c414a823c09d13a6e49be');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0617540096','component','접수의견','cdefccb8cf074e2f8ee1f741e80c483f');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b06175b0097','component','z-accept-content','cdefccb8cf074e2f8ee1f741e80c483f');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b06178e0099','component','관련CI','a7acc6870b4c4e6e147c2feb20bda6b4');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b061796009a','component','z-relation-ci','a7acc6870b4c4e6e147c2feb20bda6b4');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0617cb009c','component','처리자','5aa28e3b3e2446258a15b99267bd063c');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0617d2009d','component','z-processor','5aa28e3b3e2446258a15b99267bd063c');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b06181900a0','component','처리일시','966c2137e9d747e0a4ef5953189d596a');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b06182000a1','component','z-process-date','966c2137e9d747e0a4ef5953189d596a');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b06182e00a2','component','장애유형','a75adb2ca0f4d7023972794de97e979e');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b06183500a3','component','z-incident-category','a75adb2ca0f4d7023972794de97e979e');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b06188400a5','component','장애등급','aee06d995cbcad83c98df177479242c9');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b06188a00a6','component','z-incident-level','aee06d995cbcad83c98df177479242c9');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b06189700a7','component','긴급도','a6b76adaa1172d77d2b6cd62ad98db34');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b06189e00a8','component','z-incident-urgency','a6b76adaa1172d77d2b6cd62ad98db34');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0618ec00aa','component','장애원인','a2052b72aef1717cc785ead95c9b583c');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0618f400ab','component','z-incident-cause','a2052b72aef1717cc785ead95c9b583c');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b06193000ad','component','z-incident-symptom','a81e3ddc48a12baf94aebac8f2a4f6bc');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b06193700ae','component','장애증상','a81e3ddc48a12baf94aebac8f2a4f6bc');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b06196d00b0','component','처리내용','aa27a4ea8b744f4bab35b7af3c2e913d');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b06197400b1','component','z-process-content','aa27a4ea8b744f4bab35b7af3c2e913d');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0619a500b3','component','향후대책','a9b723e29458f98a4904336baff53f6a');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0619ac00b4','component','z-incident-plan','a9b723e29458f98a4904336baff53f6a');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0619dd00b6','component','CI','afd603a99e8287aa40706c175c697ae7');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0619e300b7','component','관련CI','afd603a99e8287aa40706c175c697ae7');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b0619ea00b8','component','z-incident-relation-ci','afd603a99e8287aa40706c175c697ae7');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b061a1c00ba','component','첨부파일','09f5924aeff4483daf730badbc1379d6');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b061a2300bb','component','z-process-file','09f5924aeff4483daf730badbc1379d6');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b061a5c00bd','component','승인자','5a57a673747a46b0924716a33470a723');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b061a6300be','component','z-approver','5a57a673747a46b0924716a33470a723');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b061aa600c1','component','승인일시','39e8b33c67ad420599d9f57c6e23cbee');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b061aad00c2','component','반려일시','39e8b33c67ad420599d9f57c6e23cbee');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b061ab500c3','component','z-approve-date','39e8b33c67ad420599d9f57c6e23cbee');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b061abb00c4','component','z-incident-approve-date','39e8b33c67ad420599d9f57c6e23cbee');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b061aed00c6','component','승인의견','5dba55dd1d57415ba28a3f1816859793');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b061af300c7','component','반려의견','5dba55dd1d57415ba28a3f1816859793');
INSERT INTO awf_tag VALUES ('4028b21f7c9adb6a017c9b061af900c8','component','z-approve-content','5dba55dd1d57415ba28a3f1816859793');
/* 서비스데스크 - 장애신고 - 만족도 */
INSERT INTO awf_tag VALUES ('4028b21f7c90d996017c914da7bf0023','component','로고','a4257952286a4e6fae6faaeaae7279fd');
INSERT INTO awf_tag VALUES ('4028b21f7c90d996017c914da7c50024','component','z-logo','a4257952286a4e6fae6faaeaae7279fd');
INSERT INTO awf_tag VALUES ('4028b21f7c90d996017c914da7e70026','component','제목','a19693dff90a45b889df568bbe177bcd');
INSERT INTO awf_tag VALUES ('4028b21f7c90d996017c914da7eb0027','component','z-document-title','a19693dff90a45b889df568bbe177bcd');
INSERT INTO awf_tag VALUES ('4028b21f7c90d996017c914da83a002b','component','만족도','5904fbf514df42cbb8d859799d8e7f3f');
INSERT INTO awf_tag VALUES ('4028b21f7c90d996017c914da841002c','component','z-satisfaction','5904fbf514df42cbb8d859799d8e7f3f');
INSERT INTO awf_tag VALUES ('4028b21f7c90d996017c914da841002g','component','z-serviceDesk-incident-satisfaction','5904fbf514df42cbb8d859799d8e7f3f');
INSERT INTO awf_tag VALUES ('4028b21f7c90d996017c914da860002e','component','만족도평가의견','207f974ae4654e7f8331526c504d0152');
INSERT INTO awf_tag VALUES ('4028b21f7c90d996017c914da865002f','component','z-satisfaction-content','207f974ae4654e7f8331526c504d0152');
/* 서비스데스크 - 서비스요청 */

/* 서비스데스크 - 서비스요청 - 만족도 */
INSERT INTO awf_tag VALUES ('4028b21f7c90d996017c914eec450042','component','로고','c3c6191d9ee748bebc5745bb5be27452');
INSERT INTO awf_tag VALUES ('4028b21f7c90d996017c914eec4a0043','component','z-logo','c3c6191d9ee748bebc5745bb5be27452');
INSERT INTO awf_tag VALUES ('4028b21f7c90d996017c914eec6c0045','component','제목','90eedeed14494ea4b17d98bd4e8d0a69');
INSERT INTO awf_tag VALUES ('4028b21f7c90d996017c914eec700046','component','z-document-title','90eedeed14494ea4b17d98bd4e8d0a69');
INSERT INTO awf_tag VALUES ('4028b21f7c90d996017c914eecca004a','component','만족도','0986886e23a044659c7bb280347064d2');
INSERT INTO awf_tag VALUES ('4028b21f7c90d996017c914eecd3004b','component','z-satisfaction','0986886e23a044659c7bb280347064d2');
INSERT INTO awf_tag VALUES ('4028b21f7c90d996017c914eecd3000f','component','z-serviceDesk-change-satisfaction','0986886e23a044659c7bb280347064d2');
INSERT INTO awf_tag VALUES ('4028b21f7c90d996017c914eecf9004d','component','만족도평가의견','5e203c7bace44cb58e1c38cee9372404');
INSERT INTO awf_tag VALUES ('4028b21f7c90d996017c914eecfe004e','component','z-satisfaction-content','5e203c7bace44cb58e1c38cee9372404');
