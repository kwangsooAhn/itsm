/**
 * 컴포넌트 세부속성
 */
DROP TABLE IF EXISTS wf_component_property cascade;

CREATE TABLE wf_component_property (
    component_id varchar(128) NULL,
    property_type varchar(100) NULL,
    property_options text NULL,
    CONSTRAINT wf_component_property_pk PRIMARY KEY (component_id,property_type),
    CONSTRAINT wf_component_property_fk FOREIGN KEY (component_id) REFERENCES wf_component(component_id)
);

COMMENT ON TABLE wf_component_property IS '컴포넌트 세부속성';
COMMENT ON COLUMN wf_component_property.component_id IS '컴포넌트 아이디';
COMMENT ON COLUMN wf_component_property.property_type IS '속성 타입';
COMMENT ON COLUMN wf_component_property.property_options IS '속성 값';

/* 서비스데스크 - 단순문의 */
INSERT INTO wf_component_property VALUES ('ae3e2a000d67b4e2e8d83bf36c81260a','validation','{"required":false}');
INSERT INTO wf_component_property VALUES ('ae3e2a000d67b4e2e8d83bf36c81260a','label','{"position":"hidden","fontSize":"14","fontColor":"rgba(141, 146, 153, 1)","bold":false,"italic":false,"underline":false,"align":"left","text":""}');
INSERT INTO wf_component_property VALUES ('ae3e2a000d67b4e2e8d83bf36c81260a','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('ae3e2a000d67b4e2e8d83bf36c81260a','element','{"columnWidth":"12","path":"file:///logo.png","width":"155","height":"26","align":"left"}');
INSERT INTO wf_component_property VALUES ('aa83af616d59cc86c565a0282153c236','label','{"position":"left","fontSize":"40","fontColor":"#222529","bold":false,"italic":false,"underline":false,"align":"center","text":"단순문의"}');
INSERT INTO wf_component_property VALUES ('aa83af616d59cc86c565a0282153c236','element','{"columnWidth":"0","labelWidth":"10","text":"","fontSize":"12","align":"left","fontOption":"","fontOptionBold":"N","fontOptionItalic":"N","fontOptionUnderline":"N","fontColor":"#8B9094"}');
INSERT INTO wf_component_property VALUES ('aa83af616d59cc86c565a0282153c236','validation','{"required":false}');
INSERT INTO wf_component_property VALUES ('aa83af616d59cc86c565a0282153c236','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('aeaba514c85489bea2ae4c588df41f03','element','{"columnWidth":"12","thickness":"3","color":"#CFD5D9","type":"solid"}');
INSERT INTO wf_component_property VALUES ('aeaba514c85489bea2ae4c588df41f03','label','{"position":"hidden","fontSize":"14","fontColor":"#CFD5D9","bold":false,"italic":false,"underline":false,"align":"left","text":""}');
INSERT INTO wf_component_property VALUES ('aeaba514c85489bea2ae4c588df41f03','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('aeaba514c85489bea2ae4c588df41f03','validation','{"required":false}');
INSERT INTO wf_component_property VALUES ('ad15aab4783f55533c6c1f183a4b60cc','element','{"placeholder":"","columnWidth":"9","defaultValueSelect":"select|userName"}');
INSERT INTO wf_component_property VALUES ('ad15aab4783f55533c6c1f183a4b60cc','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"신청자"}');
INSERT INTO wf_component_property VALUES ('ad15aab4783f55533c6c1f183a4b60cc','validation','{"validationType":"none","required":true,"minLength":"0","maxLength":"100"}');
INSERT INTO wf_component_property VALUES ('ad15aab4783f55533c6c1f183a4b60cc','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('ae4f48236ecf493eb0212d77ac14c360','validation','{"required":true}');
INSERT INTO wf_component_property VALUES ('ae4f48236ecf493eb0212d77ac14c360','display','{"displayOrder":1,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('ae4f48236ecf493eb0212d77ac14c360','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"신청부서"}');
INSERT INTO wf_component_property VALUES ('ae4f48236ecf493eb0212d77ac14c360','element','{"columnWidth":"9","defaultValueCustomCode":"40288a19736b46fb01736b89e46c0009|session|department"}');
INSERT INTO wf_component_property VALUES ('a71d2d6094b2920f87ba8eaf025af1c3','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"전화번호"}');
INSERT INTO wf_component_property VALUES ('a71d2d6094b2920f87ba8eaf025af1c3','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('a71d2d6094b2920f87ba8eaf025af1c3','validation','{"validationType":"phone","required":false,"minLength":"0","maxLength":"100"}');
INSERT INTO wf_component_property VALUES ('a71d2d6094b2920f87ba8eaf025af1c3','element','{"placeholder":"","columnWidth":"9","defaultValueSelect":"select|officeNumber"}');
INSERT INTO wf_component_property VALUES ('a4a100bbcd9907aae4e260c8bfa3b45f','display','{"displayOrder":1,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('a4a100bbcd9907aae4e260c8bfa3b45f','validation','{"validationType":"email","required":false,"minLength":"0","maxLength":"100"}');
INSERT INTO wf_component_property VALUES ('a4a100bbcd9907aae4e260c8bfa3b45f','element','{"placeholder":"","columnWidth":"9","defaultValueSelect":"select|email"}');
INSERT INTO wf_component_property VALUES ('a4a100bbcd9907aae4e260c8bfa3b45f','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"메일주소"}');
INSERT INTO wf_component_property VALUES ('a115df4520fea935e4482784064c7a51','validation','{"required":true,"minDateTime":"","maxDateTime":""}');
INSERT INTO wf_component_property VALUES ('a115df4520fea935e4482784064c7a51','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"신청일시"}');
INSERT INTO wf_component_property VALUES ('a115df4520fea935e4482784064c7a51','element','{"columnWidth":"9","defaultValueRadio":"now"}');
INSERT INTO wf_component_property VALUES ('a115df4520fea935e4482784064c7a51','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('a0c3e8d18663483284b7e9fe83d0b824','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"완료희망일시"}');
INSERT INTO wf_component_property VALUES ('a0c3e8d18663483284b7e9fe83d0b824','display','{"displayOrder":1,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('a0c3e8d18663483284b7e9fe83d0b824','validation','{"required":true,"minDateTime":"","maxDateTime":""}');
INSERT INTO wf_component_property VALUES ('a0c3e8d18663483284b7e9fe83d0b824','element','{"columnWidth":"9","defaultValueRadio":"now"}');
INSERT INTO wf_component_property VALUES ('a75c85bba7aa722f9dcbcbcfad0b6ac7','validation','{"validationType":"none","required":true,"minLength":"0","maxLength":"100"}');
INSERT INTO wf_component_property VALUES ('a75c85bba7aa722f9dcbcbcfad0b6ac7','element','{"placeholder":"제목을 입력하세요.","columnWidth":"10","defaultValueSelect":"input|"}');
INSERT INTO wf_component_property VALUES ('a75c85bba7aa722f9dcbcbcfad0b6ac7','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('a75c85bba7aa722f9dcbcbcfad0b6ac7','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"제목"}');
INSERT INTO wf_component_property VALUES ('ade3320b3bc9dca17651e4c0cdef89e4','validation','{"required":true}');
INSERT INTO wf_component_property VALUES ('ade3320b3bc9dca17651e4c0cdef89e4','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"서비스 항목"}');
INSERT INTO wf_component_property VALUES ('ade3320b3bc9dca17651e4c0cdef89e4','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('ade3320b3bc9dca17651e4c0cdef89e4','element','{"columnWidth":"10","defaultValueCustomCode":"40288a19736b46fb01736b89e46c0010|code|form.template.serviceDesk.inquiry.category.etc|etc"}');
INSERT INTO wf_component_property VALUES ('a3b140da99dbfb49beb7fa3db09042b4','element','{"columnWidth":"10","rows":"3","placeholder":"내용을 입력하세요."}');
INSERT INTO wf_component_property VALUES ('a3b140da99dbfb49beb7fa3db09042b4','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('a3b140da99dbfb49beb7fa3db09042b4','validation','{"required":true,"minLength":"0","maxLength":"512"}');
INSERT INTO wf_component_property VALUES ('a3b140da99dbfb49beb7fa3db09042b4','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"내용"}');
INSERT INTO wf_component_property VALUES ('a9404877fabbd09dc2603f78283fa155','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('a9404877fabbd09dc2603f78283fa155','validation','{"required":false}');
INSERT INTO wf_component_property VALUES ('a9404877fabbd09dc2603f78283fa155','element','{"columnWidth":"10","align":"left"}');
INSERT INTO wf_component_property VALUES ('a9404877fabbd09dc2603f78283fa155','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"첨부파일"}');
INSERT INTO wf_component_property VALUES ('ab6d29d8ef257f93de98c655fbaab22a','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"접수자"}');
INSERT INTO wf_component_property VALUES ('ab6d29d8ef257f93de98c655fbaab22a','element','{"placeholder":"","columnWidth":"9","defaultValueSelect":"select|userName"}');
INSERT INTO wf_component_property VALUES ('ab6d29d8ef257f93de98c655fbaab22a','validation','{"validationType":"none","required":true,"minLength":"0","maxLength":"100"}');
INSERT INTO wf_component_property VALUES ('ab6d29d8ef257f93de98c655fbaab22a','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('ab266326aa59ed5bb4411da4142aa24a','display','{"displayOrder":1,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('ab266326aa59ed5bb4411da4142aa24a','validation','{"required":true}');
INSERT INTO wf_component_property VALUES ('ab266326aa59ed5bb4411da4142aa24a','element','{"columnWidth":"9","defaultValueCustomCode":"40288a19736b46fb01736b89e46c0009|session|department"}');
INSERT INTO wf_component_property VALUES ('ab266326aa59ed5bb4411da4142aa24a','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"접수부서"}');
INSERT INTO wf_component_property VALUES ('a8e347190f51b1b05200f34d9e8b8ab0','element','{"columnWidth":"10","defaultValueRadio":"now"}');
INSERT INTO wf_component_property VALUES ('a8e347190f51b1b05200f34d9e8b8ab0','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"접수일시"}');
INSERT INTO wf_component_property VALUES ('a8e347190f51b1b05200f34d9e8b8ab0','validation','{"required":true,"minDateTime":"","maxDateTime":""}');
INSERT INTO wf_component_property VALUES ('a8e347190f51b1b05200f34d9e8b8ab0','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('a71977859617f137f589108c24fa7845','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('a71977859617f137f589108c24fa7845','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"접수의견"}');
INSERT INTO wf_component_property VALUES ('a71977859617f137f589108c24fa7845','validation','{"required":false,"minLength":"0","maxLength":"512"}');
INSERT INTO wf_component_property VALUES ('a71977859617f137f589108c24fa7845','element','{"columnWidth":"10","rows":"3","placeholder":"내용을 입력하세요."}');
INSERT INTO wf_component_property VALUES ('acc02e1f2f51aa01649b6e06f7b35ec4','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('acc02e1f2f51aa01649b6e06f7b35ec4','validation','{"required":true}');
INSERT INTO wf_component_property VALUES ('acc02e1f2f51aa01649b6e06f7b35ec4','element','{"columnWidth":"10","defaultValueCustomCode":"40288a19736b46fb01736b89e46c0008|session|userName"}');
INSERT INTO wf_component_property VALUES ('acc02e1f2f51aa01649b6e06f7b35ec4','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"처리자"}');
INSERT INTO wf_component_property VALUES ('a83a54dfec6c65dc5983272ec9a956ce','element','{"columnWidth":"10","defaultValueRadio":"now"}');
INSERT INTO wf_component_property VALUES ('a83a54dfec6c65dc5983272ec9a956ce','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('a83a54dfec6c65dc5983272ec9a956ce','validation','{"required":true,"minDateTime":"","maxDateTime":""}');
INSERT INTO wf_component_property VALUES ('a83a54dfec6c65dc5983272ec9a956ce','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"처리일시"}');
INSERT INTO wf_component_property VALUES ('ae07bae262c57399a4ee8b3dea2fcbab','validation','{"required":true,"minLength":"0","maxLength":"512"}');
INSERT INTO wf_component_property VALUES ('ae07bae262c57399a4ee8b3dea2fcbab','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('ae07bae262c57399a4ee8b3dea2fcbab','element','{"columnWidth":"10","rows":"3","placeholder":"내용을 입력하세요."}');
INSERT INTO wf_component_property VALUES ('ae07bae262c57399a4ee8b3dea2fcbab','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"처리내용"}');
INSERT INTO wf_component_property VALUES ('a27c22d07de4231ab41863dab45450f1','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('a27c22d07de4231ab41863dab45450f1','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"첨부파일"}');
INSERT INTO wf_component_property VALUES ('a27c22d07de4231ab41863dab45450f1','element','{"columnWidth":"10","align":"left"}');
INSERT INTO wf_component_property VALUES ('a27c22d07de4231ab41863dab45450f1','validation','{"required":false}');
INSERT INTO wf_component_property VALUES ('a4c410628aa58b1863935b4056395b80','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('a4c410628aa58b1863935b4056395b80','validation','{"required":true}');
INSERT INTO wf_component_property VALUES ('a4c410628aa58b1863935b4056395b80','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"승인자"}');
INSERT INTO wf_component_property VALUES ('a4c410628aa58b1863935b4056395b80','element','{"columnWidth":"10","defaultValueCustomCode":"40288a19736b46fb01736b89e46c0008|session|userName"}');
INSERT INTO wf_component_property VALUES ('a10c935dbdf1c30b34dfabe5ebc0ba73','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('a10c935dbdf1c30b34dfabe5ebc0ba73','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"승인 / 반려 일시"}');
INSERT INTO wf_component_property VALUES ('a10c935dbdf1c30b34dfabe5ebc0ba73','element','{"columnWidth":"10","defaultValueRadio":"now"}');
INSERT INTO wf_component_property VALUES ('a10c935dbdf1c30b34dfabe5ebc0ba73','validation','{"required":true,"minDateTime":"","maxDateTime":""}');
INSERT INTO wf_component_property VALUES ('ab97abfd66b5654e3f525e617df9afaf','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"승인 / 반려 의견"}');
INSERT INTO wf_component_property VALUES ('ab97abfd66b5654e3f525e617df9afaf','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('ab97abfd66b5654e3f525e617df9afaf','validation','{"required":false,"minLength":"0","maxLength":"512"}');
INSERT INTO wf_component_property VALUES ('ab97abfd66b5654e3f525e617df9afaf','element','{"columnWidth":"10","rows":"3","placeholder":""}');
/* 서비스데스크 - 단순문의 - 만족도 */
INSERT INTO wf_component_property VALUES ('a605df0d26f09a3d20caaea3977a5c64','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('a605df0d26f09a3d20caaea3977a5c64','validation','{"required":false}');
INSERT INTO wf_component_property VALUES ('a605df0d26f09a3d20caaea3977a5c64','element','{"columnWidth":"12","path":"file:///logo.png","width":"155","height":"26","align":"left"}');
INSERT INTO wf_component_property VALUES ('a605df0d26f09a3d20caaea3977a5c64','label','{"position":"hidden","fontSize":"14","fontColor":"rgba(141, 146, 153, 1)","bold":false,"italic":false,"underline":false,"align":"left","text":""}');
INSERT INTO wf_component_property VALUES ('ae54c20fd574a57845fffaa7bb936eb4','label','{"position":"left","fontSize":"40","fontColor":"#222529","bold":false,"italic":false,"underline":false,"align":"center","text":"만족도 평가"}');
INSERT INTO wf_component_property VALUES ('ae54c20fd574a57845fffaa7bb936eb4','validation','{"required":false}');
INSERT INTO wf_component_property VALUES ('ae54c20fd574a57845fffaa7bb936eb4','element','{"columnWidth":"0","labelWidth":"10","text":"","fontSize":"12","align":"left","fontOption":"","fontOptionBold":"N","fontOptionItalic":"N","fontOptionUnderline":"N","fontColor":"#8B9094"}');
INSERT INTO wf_component_property VALUES ('ae54c20fd574a57845fffaa7bb936eb4','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('a7cc83ce97c50faa754c702bf018c505','element','{"columnWidth":"12","thickness":"3","color":"#CFD5D9","type":"solid"}');
INSERT INTO wf_component_property VALUES ('a7cc83ce97c50faa754c702bf018c505','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('a7cc83ce97c50faa754c702bf018c505','validation','{"required":false}');
INSERT INTO wf_component_property VALUES ('a7cc83ce97c50faa754c702bf018c505','label','{"position":"hidden","fontSize":"14","fontColor":"rgba(141, 146, 153, 1)","bold":false,"italic":false,"underline":false,"align":"left","text":""}');
INSERT INTO wf_component_property VALUES ('ac69d66f3ec394b36215cd2ee3983292','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('ac69d66f3ec394b36215cd2ee3983292','validation','{"required":true}');
INSERT INTO wf_component_property VALUES ('ac69d66f3ec394b36215cd2ee3983292','element','{"position":"left","columnWidth":"10","align":"horizontal","options":[{"name":"매우불만족","value":"1"},{"name":"불만족","value":"2"},{"name":"보통","value":"3"},{"name":"만족","value":"4"},{"name":"매우만족","value":"5"}]}');
INSERT INTO wf_component_property VALUES ('ac69d66f3ec394b36215cd2ee3983292','label','{"position":"left","fontSize":"14","fontColor":"rgba(141, 146, 153, 1)","bold":false,"italic":false,"underline":false,"align":"left","text":"만족도"}');
INSERT INTO wf_component_property VALUES ('ad2996c39febdc13b32d08354169d6ac','validation','{"required":false,"minLength":"0","maxLength":"512"}');
INSERT INTO wf_component_property VALUES ('ad2996c39febdc13b32d08354169d6ac','element','{"columnWidth":"10","rows":"3","placeholder":"내용을 입력하세요."}');
INSERT INTO wf_component_property VALUES ('ad2996c39febdc13b32d08354169d6ac','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"평가의견"}');
INSERT INTO wf_component_property VALUES ('ad2996c39febdc13b32d08354169d6ac','display','{"displayOrder":0,"columnWidth":"12"}');
/* 서비스데스크 - 장애신고 */

/* 서비스데스크 - 장애신고 - 만족도 */
INSERT INTO wf_component_property VALUES ('a4257952286a4e6fae6faaeaae7279fd','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('a4257952286a4e6fae6faaeaae7279fd','validation','{"required":false}');
INSERT INTO wf_component_property VALUES ('a4257952286a4e6fae6faaeaae7279fd','element','{"columnWidth":"12","path":"file:///logo.png","width":"155","height":"26","align":"left"}');
INSERT INTO wf_component_property VALUES ('a4257952286a4e6fae6faaeaae7279fd','label','{"position":"hidden","fontSize":"14","fontColor":"rgba(141, 146, 153, 1)","bold":false,"italic":false,"underline":false,"align":"left","text":""}');
INSERT INTO wf_component_property VALUES ('a19693dff90a45b889df568bbe177bcd','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('a19693dff90a45b889df568bbe177bcd','validation','{"required":false}');
INSERT INTO wf_component_property VALUES ('a19693dff90a45b889df568bbe177bcd','element','{"columnWidth":"0","labelWidth":"10","text":"","fontSize":"12","align":"left","fontOption":"","fontOptionBold":"N","fontOptionItalic":"N","fontOptionUnderline":"N","fontColor":"#8B9094"}');
INSERT INTO wf_component_property VALUES ('a19693dff90a45b889df568bbe177bcd','label','{"position":"left","fontSize":"40","fontColor":"#222529","bold":false,"italic":false,"underline":false,"align":"center","text":"만족도 평가"}');
INSERT INTO wf_component_property VALUES ('d2d8114f31bb47608d1facc6f03e18ac','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('d2d8114f31bb47608d1facc6f03e18ac','validation','{"required":false}');
INSERT INTO wf_component_property VALUES ('d2d8114f31bb47608d1facc6f03e18ac','element','{"columnWidth":"12","thickness":"3","color":"#CFD5D9","type":"solid"}');
INSERT INTO wf_component_property VALUES ('d2d8114f31bb47608d1facc6f03e18ac','label','{"position":"hidden","fontSize":"14","fontColor":"rgba(141, 146, 153, 1)","bold":false,"italic":false,"underline":false,"align":"left","text":""}');
INSERT INTO wf_component_property VALUES ('5904fbf514df42cbb8d859799d8e7f3f','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('5904fbf514df42cbb8d859799d8e7f3f','validation','{"required":true}');
INSERT INTO wf_component_property VALUES ('5904fbf514df42cbb8d859799d8e7f3f','element','{"position":"left","columnWidth":"10","align":"horizontal","options":[{"name":"매우불만족","value":"1"},{"name":"불만족","value":"2"},{"name":"보통","value":"3"},{"name":"만족","value":"4"},{"name":"매우만족","value":"5"}]}');
INSERT INTO wf_component_property VALUES ('5904fbf514df42cbb8d859799d8e7f3f','label','{"position":"left","fontSize":"14","fontColor":"rgba(141, 146, 153, 1)","bold":false,"italic":false,"underline":false,"align":"left","text":"만족도"}');
INSERT INTO wf_component_property VALUES ('207f974ae4654e7f8331526c504d0152','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('207f974ae4654e7f8331526c504d0152','validation','{"required":false,"minLength":"0","maxLength":"512"}');
INSERT INTO wf_component_property VALUES ('207f974ae4654e7f8331526c504d0152','element','{"columnWidth":"10","rows":"3","placeholder":"내용을 입력하세요."}');
INSERT INTO wf_component_property VALUES ('207f974ae4654e7f8331526c504d0152','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"평가의견"}');
/* 서비스데스크 - 서비스요청 */

/* 서비스데스크 - 서비스요청 - 만족도 */
INSERT INTO wf_component_property VALUES ('c3c6191d9ee748bebc5745bb5be27452','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('c3c6191d9ee748bebc5745bb5be27452','validation','{"required":false}');
INSERT INTO wf_component_property VALUES ('c3c6191d9ee748bebc5745bb5be27452','element','{"columnWidth":"12","path":"file:///logo.png","width":"155","height":"26","align":"left"}');
INSERT INTO wf_component_property VALUES ('c3c6191d9ee748bebc5745bb5be27452','label','{"position":"hidden","fontSize":"14","fontColor":"rgba(141, 146, 153, 1)","bold":false,"italic":false,"underline":false,"align":"left","text":""}');
INSERT INTO wf_component_property VALUES ('90eedeed14494ea4b17d98bd4e8d0a69','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('90eedeed14494ea4b17d98bd4e8d0a69','validation','{"required":false}');
INSERT INTO wf_component_property VALUES ('90eedeed14494ea4b17d98bd4e8d0a69','element','{"columnWidth":"0","labelWidth":"10","text":"","fontSize":"12","align":"left","fontOption":"","fontOptionBold":"N","fontOptionItalic":"N","fontOptionUnderline":"N","fontColor":"#8B9094"}');
INSERT INTO wf_component_property VALUES ('90eedeed14494ea4b17d98bd4e8d0a69','label','{"position":"left","fontSize":"40","fontColor":"#222529","bold":false,"italic":false,"underline":false,"align":"center","text":"만족도 평가"}');
INSERT INTO wf_component_property VALUES ('8f33d3a207b04bb886a87e1e01859500','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('8f33d3a207b04bb886a87e1e01859500','validation','{"required":false}');
INSERT INTO wf_component_property VALUES ('8f33d3a207b04bb886a87e1e01859500','element','{"columnWidth":"12","thickness":"3","color":"#CFD5D9","type":"solid"}');
INSERT INTO wf_component_property VALUES ('8f33d3a207b04bb886a87e1e01859500','label','{"position":"hidden","fontSize":"14","fontColor":"rgba(141, 146, 153, 1)","bold":false,"italic":false,"underline":false,"align":"left","text":""}');
INSERT INTO wf_component_property VALUES ('0986886e23a044659c7bb280347064d2','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('0986886e23a044659c7bb280347064d2','validation','{"required":true}');
INSERT INTO wf_component_property VALUES ('0986886e23a044659c7bb280347064d2','element','{"position":"left","columnWidth":"10","align":"horizontal","options":[{"name":"매우불만족","value":"1"},{"name":"불만족","value":"2"},{"name":"보통","value":"3"},{"name":"만족","value":"4"},{"name":"매우만족","value":"5"}]}');
INSERT INTO wf_component_property VALUES ('0986886e23a044659c7bb280347064d2','label','{"position":"left","fontSize":"14","fontColor":"rgba(141, 146, 153, 1)","bold":false,"italic":false,"underline":false,"align":"left","text":"만족도"}');
INSERT INTO wf_component_property VALUES ('5e203c7bace44cb58e1c38cee9372404','display','{"displayOrder":0,"columnWidth":"12"}');
INSERT INTO wf_component_property VALUES ('5e203c7bace44cb58e1c38cee9372404','validation','{"required":false,"minLength":"0","maxLength":"512"}');
INSERT INTO wf_component_property VALUES ('5e203c7bace44cb58e1c38cee9372404','element','{"columnWidth":"10","rows":"3","placeholder":"내용을 입력하세요."}');
INSERT INTO wf_component_property VALUES ('5e203c7bace44cb58e1c38cee9372404','label','{"position":"left","fontSize":"14","fontColor":"#8B9094","bold":false,"italic":false,"underline":false,"align":"left","text":"평가의견"}');
