/**
 * CMDB 속성 정보
 */
DROP TABLE IF EXISTS cmdb_attribute cascade;

CREATE TABLE cmdb_attribute
(
	attribute_id character varying(128) NOT NULL UNIQUE,
	attribute_name character varying(100),
	attribute_desc character varying(500),
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

insert into cmdb_attribute values ('ac4f3785cdbcc149a0b92dbf00af80ef', 'Classification', null, 'inputbox', '분류', null, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('c1f97be1aea3fdee785ca73b751f79d8', 'Quantity', null, 'inputbox', '수량', null, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('602b2c9216825bffc96ae69eeb73bdbc', 'introduction date', null, 'date', '도입일', null, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('adaeef4046bfcd78e345ad48cbbeefa5', 'Model', null, 'inputbox', '모델명', null, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('247aa7187b335f9c4d78db5e18a2704c', 'Brand', null, 'inputbox', '브랜드', null, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('58e0cd57479bbb9d8a6b2bb6012206c2', 'Installation location', null, 'inputbox', '설치장소', null, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('99a8cf26726e907a95dad34e188cbfc8', 'Grade', null, 'dropdown', '등급', null, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('d0a35c07fa9bdd919a039f1f127cd54e', 'Protection level', null, 'dropdown', '보호수준', null, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('b5f16c33ca0531087ed1b46805a9c682', 'Integrity', null, 'dropdown', '무결성', null, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('072fcb3be4056095a9af82dc6505b1e8', 'Availability', null, 'dropdown', '가용성', null, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('77b6112b3013a6808aeb04f80dd75360', 'Confidentiality', null, 'dropdown', '기밀성', null, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('a508fbfda5d65a54b9b25dc5925d79bb', 'Manager', null, 'inputbox', '관리자', null, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('189319790e6349c7248b9f50456ed47b', 'Remarks', null, 'textbox', '비고', null, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('1d1338bb6316ab81f7c6adbc77199409', 'Manufacturer', null, 'inputbox', '제조사', null, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('e613591ddea0f8c1f2457104f7cf286d', 'Equipment', null, 'inputbox', '장비명', null, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('6e247bdb7b70757e1987ae25a36c3d13', 'Host', null, 'inputbox', '호스트명', null, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('6ea67d6c6cb28def6b289affc6c95fd1', 'MAC', null, 'inputbox', 'MAC', null, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('79a99dfa69d7d0c5c369ad4840815749', 'IP_V4', null, 'inputbox', 'IP_V4', null, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('932831a8e53aa6f795f608794e51e7e0', 'IP_V6', null, 'inputbox', 'IP_V6', null, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('d47973f063130acab00b2cf203a9788b', 'CPU', null, 'inputbox', 'CPU', null, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('ef60a5a1aa010de9b7ba2dda96107c5d', 'Processor', null, 'inputbox', 'Processor', null, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('f4538a0d55c456461f1d0932fd424350', 'RAM', null, 'inputbox', 'RAM', null, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('27caaeba596663101d55a09ec873a375', 'Status', null, 'dropdown', '상태', null, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('e651113f8a452f55f50ed41956cdfb34', 'Version', null, 'inputbox', '버전', null, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('734ab921484883ad7760032a008baf21', 'Version_OS_Linux', null, 'dropdown', '버전', null, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('df0e88d216ace73e0164f3dbf7ade131', 'Version_OS_Windows', null, 'dropdown', '버전', null, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('bde6f4eac279ac3528c9cca16d12779a', 'Database', null, 'table', '데이터베이스', null, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('799afe719cd0bfe38797172bb77ae5d8', 'Licensing policy', null, 'dropdown', '라이선스 정책', null, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('489a14a0ebdca14b6eb42cf804330145', 'Licenses', null, 'inputbox', '라이선스', null, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('addc07e374faec9f0d6d3bbeca565886', 'OS Type', null, 'dropdown', 'OS 종류', null, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('2bb03c41cd9998e77da9b737d4fcf9ab', 'Bash Version', null, 'inputbox', 'bash 버전', null, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into cmdb_attribute values ('42b02142dd9128e47a35b737d4fc21ad', 'Service Name', null, 'table', '서비스명', null, '0509e09412534a6e98f04ca79abb6424', now(), null, null);

