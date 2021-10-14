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
INSERT INTO awf_tag VALUES ('4028b21f7c780ba6017c783350cb0249','component','만족도평가의견','ad2996c39febdc13b32d08354169d6ac');
INSERT INTO awf_tag VALUES ('4028b21f7c780ba6017c783350f1024a','component','z-satisfaction-content','ad2996c39febdc13b32d08354169d6ac');
