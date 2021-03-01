/**
 * CMDB 속성 정보
 */
DROP TABLE IF EXISTS cmdb_attribute cascade;

CREATE TABLE cmdb_attribute
(
	attribute_id character varying(128) NOT NULL UNIQUE,
	attribute_name character varying(128),
	attribute_desc character varying(512),
	attribute_type character varying(100),
	attribute_text character varying(128),
	attribute_value text,
	create_user_key character varying(128),
	create_dt timestamp without time zone,
	update_user_key character varying(128),
	update_dt timestamp without time zone,
	CONSTRAINT cmdb_attribute_pk PRIMARY KEY (attribute_id),
	CONSTRAINT cmdb_attribute_uk UNIQUE (attribute_id)
);

COMMENT ON TABLE cmdb_attribute IS 'CMDB 속성 정보';
COMMENT ON COLUMN cmdb_attribute.attribute_id IS '속성아이디';
COMMENT ON COLUMN cmdb_attribute.attribute_name IS '속성이름';
COMMENT ON COLUMN cmdb_attribute.attribute_desc IS '속성설명';
COMMENT ON COLUMN cmdb_attribute.attribute_type IS '속성타입';
COMMENT ON COLUMN cmdb_attribute.attribute_text IS '속성라벨';
COMMENT ON COLUMN cmdb_attribute.attribute_value IS '속성세부정보';
COMMENT ON COLUMN cmdb_attribute.create_user_key IS '등록자';
COMMENT ON COLUMN cmdb_attribute.create_dt IS '등록일시';
COMMENT ON COLUMN cmdb_attribute.update_user_key IS '수정자';
COMMENT ON COLUMN cmdb_attribute.update_dt IS '수정일시';

INSERT INTO cmdb_attribute VALUES ('072fcb3be4056095a9af82dc6505b1e8', 'Availability', '', 'dropdown', '가용성', '{"option":[{"text":"상","value":"3"},{"text":"중","value":"2"},{"text":"하","value":"1"}]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO cmdb_attribute VALUES ('b5f16c33ca0531087ed1b46805a9c682', 'Integrity', '', 'dropdown', '무결성', '{"option":[{"text":"상","value":"3"},{"text":"중","value":"2"},{"text":"하","value":"1"}]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO cmdb_attribute VALUES ('77b6112b3013a6808aeb04f80dd75360', 'Confidentiality', '', 'dropdown', '기밀성', '{"option":[{"text":"상","value":"3"},{"text":"중","value":"2"},{"text":"하","value":"1"}]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO cmdb_attribute VALUES ('2bb03c41cd9998e77da9b737d4fcf9ab', 'Bash Version', '', 'inputbox', 'bash 버전', '{"validate":"","required":"false","maxLength":"100","minLength":"0"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO cmdb_attribute VALUES ('247aa7187b335f9c4d78db5e18a2704c', 'Brand', '', 'inputbox', '브랜드', '{"validate":"","required":"false","maxLength":"100","minLength":"0"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO cmdb_attribute VALUES ('d47973f063130acab00b2cf203a9788b', 'CPU', '', 'inputbox', 'CPU', '{"validate":"","required":"true","maxLength":"100","minLength":"0"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO cmdb_attribute VALUES ('ac4f3785cdbcc149a0b92dbf00af80ef', 'Classification', '', 'inputbox', '분류', '{"validate":"","required":"true","maxLength":"100","minLength":"0"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO cmdb_attribute VALUES ('bde6f4eac279ac3528c9cca16d12779a', 'Database', '', 'custom-code', '데이터베이스', '{"required":"true","customCode":"40288ab777dd21b50177dd52781e0000","default":{"type":"code","value":"cmdb.db.kind.altibase|altibase"},"button":"검색"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO cmdb_attribute VALUES ('e613591ddea0f8c1f2457104f7cf286d', 'Equipment', '', 'inputbox', '장비명', '{"validate":"","required":"true","maxLength":"100","minLength":"0"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO cmdb_attribute VALUES ('99a8cf26726e907a95dad34e188cbfc8', 'Grade', '', 'dropdown', '등급', '{"option":[{"text":"1등급","value":"1"},{"text":"2등급","value":"2"},{"text":"3등급","value":"3"}]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO cmdb_attribute VALUES ('6ea67d6c6cb28def6b289affc6c95fd1', 'MAC', '', 'inputbox', 'MAC', '{"validate":"","required":"false","maxLength":"100","minLength":"0"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO cmdb_attribute VALUES ('6e247bdb7b70757e1987ae25a36c3d13', 'Host', '', 'inputbox', '호스트명', '{"validate":"","required":"true","maxLength":"100","minLength":"0"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO cmdb_attribute VALUES ('79a99dfa69d7d0c5c369ad4840815749', 'IP_V4', '', 'inputbox', 'IP_V4', '{"validate":"","required":"false","maxLength":"100","minLength":"0"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO cmdb_attribute VALUES ('932831a8e53aa6f795f608794e51e7e0', 'IP_V6', '', 'inputbox', 'IP_V6', '{"validate":"","required":"false","maxLength":"100","minLength":"0"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO cmdb_attribute VALUES ('58e0cd57479bbb9d8a6b2bb6012206c2', 'Installation location', '', 'inputbox', '설치장소', '{"validate":"","required":"false","maxLength":"100","minLength":"0"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO cmdb_attribute VALUES ('489a14a0ebdca14b6eb42cf804330145', 'Licenses', '', 'inputbox', '라이선스', '{"validate":"","required":"false","maxLength":"100","minLength":"0"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO cmdb_attribute VALUES ('799afe719cd0bfe38797172bb77ae5d8', 'Licensing policy', '', 'dropdown', '라이선스 정책', '{"option":[{"text":"무료","value":"free"},{"text":"유료","value":"pay"}]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO cmdb_attribute VALUES ('a508fbfda5d65a54b9b25dc5925d79bb', 'Manager', '', 'custom-code', '관리자', '{"required":"true","customCode":"40288a19736b46fb01736b89e46c0008","default":{"type":"session","value":"userName"},"button":"검색"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO cmdb_attribute VALUES ('1d1338bb6316ab81f7c6adbc77199409', 'Manufacturer', '', 'inputbox', '제조사', '{"validate":"","required":"false","maxLength":"100","minLength":"0"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO cmdb_attribute VALUES ('addc07e374faec9f0d6d3bbeca565886', 'OS Type', '', 'dropdown', 'OS 종류', '{"option":[{"text":"common","value":"common"},{"text":"Linux","value":"linux"},{"text":"FreeBSD","value":"freebsd"},{"text":"Solaris","value":"solaris"},{"text":"AIX","value":"aix"},{"text":"HPUX","value":"hpux"},{"text":"WinNT","value":"winnt"}]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO cmdb_attribute VALUES ('adaeef4046bfcd78e345ad48cbbeefa5', 'Model', '', 'inputbox', '모델명', '{"validate":"","required":"true","maxLength":"100","minLength":"0"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO cmdb_attribute VALUES ('ef60a5a1aa010de9b7ba2dda96107c5d', 'Processor', '', 'inputbox', 'Processor', '{"validate":"","required":"false","maxLength":"100","minLength":"0"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO cmdb_attribute VALUES ('d0a35c07fa9bdd919a039f1f127cd54e', 'Protection level', '', 'dropdown', '보호수준', '{"option":[{"text":"가 등급","value":"3"},{"text":"나 등급","value":"2"},{"text":"다 등급","value":"1"}]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO cmdb_attribute VALUES ('c1f97be1aea3fdee785ca73b751f79d8', 'Quantity', '', 'inputbox', '수량', '{"validate":"number","required":"false","maxLength":"100","minLength":"0"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO cmdb_attribute VALUES ('f4538a0d55c456461f1d0932fd424350', 'RAM', '', 'inputbox', 'RAM', '{"validate":"","required":"false","maxLength":"100","minLength":"0"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO cmdb_attribute VALUES ('42b02142dd9128e47a35b737d4fc21ad', 'Service Name', '', 'inputbox', '서비스명', '{"validate":"","required":"false","maxLength":"100","minLength":"0"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO cmdb_attribute VALUES ('e651113f8a452f55f50ed41956cdfb34', 'Version', '', 'inputbox', '버전', '{"validate":"","required":"false","maxLength":"100","minLength":"0"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO cmdb_attribute VALUES ('602b2c9216825bffc96ae69eeb73bdbc', 'introduction date', '', 'inputbox', '도입일', '{"validate":"","required":"true","maxLength":"100","minLength":"0"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO cmdb_attribute VALUES ('189319790e6349c7248b9f50456ed47b', 'Remarks', '', 'inputbox', '비고', '{"validate":"","required":"false","maxLength":"10000","minLength":"0"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO cmdb_attribute VALUES ('27caaeba596663101d55a09ec873a375', 'Status', '', 'dropdown', '상태', '{"option":[{"text":"사용","value":"use"},{"text":"미사용","value":"unused"},{"text":"AS","value":"as"}]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO cmdb_attribute VALUES ('734ab921484883ad7760032a008baf21', 'Version_OS_Linux', '', 'dropdown', '버전', '{"option":[{"text":"Debian","value":"debian"},{"text":"Ubuntu","value":"ubuntu"},{"text":"RedHat","value":"redHat"},{"text":"CentOs","value":"centOs"},{"text":"Fedora","value":"fedora"}]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO cmdb_attribute VALUES ('df0e88d216ace73e0164f3dbf7ade131', 'Version_OS_Windows', '', 'dropdown', '버전', '{"option":[{"text":"Window 2012 R2","value":"2012"},{"text":"Window 10","value":"10"},{"text":"Window 10X","value":"10x"}]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
