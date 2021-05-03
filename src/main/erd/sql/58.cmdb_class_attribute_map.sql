/**
 * CMDB 클래스 속성 정보
 */
DROP TABLE IF EXISTS cmdb_class_attribute_map cascade;

CREATE TABLE cmdb_class_attribute_map
(
	class_id character varying(128) NOT NULL,
	attribute_id character varying(128) NOT NULL,
	attribute_order int NOT NULL,
	CONSTRAINT cmdb_class_attribute_map_pk PRIMARY KEY (class_id, attribute_id),
	CONSTRAINT cmdb_class_attribute_map_fk1 FOREIGN KEY (class_id)
      REFERENCES cmdb_class (class_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT cmdb_class_attribute_map_fk2 FOREIGN KEY (attribute_id)
      REFERENCES cmdb_attribute (attribute_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

COMMENT ON TABLE cmdb_class_attribute_map IS 'CMDB 클래스 속성 정보';
COMMENT ON COLUMN cmdb_class_attribute_map.class_id IS '클래스아이디';
COMMENT ON COLUMN cmdb_class_attribute_map.attribute_id IS '속성아이디';
COMMENT ON COLUMN cmdb_class_attribute_map.attribute_order IS '속성순서';

insert into cmdb_class_attribute_map values ('4028b881792074460179210677bb0016', 'adaeef4046bfcd78e345ad48cbbeefa5', 1);
insert into cmdb_class_attribute_map values ('4028b881792074460179210677bb0016', '4028b25d791b75ac01791b78b0550001', 2);
insert into cmdb_class_attribute_map values ('4028b881792074460179210677bb0016', '27caaeba596663101d55a09ec873a375', 3);
insert into cmdb_class_attribute_map values ('4028b881792074460179210677bb0016', '4028b881792074460179209cef74000c', 4);
insert into cmdb_class_attribute_map values ('4028b881792074460179210677bb0016', '4028b25d791b75ac01791bb14a4d0003', 5);
insert into cmdb_class_attribute_map values ('4028b881792074460179210677bb0016', '4028b25d791b75ac01791b777a240000', 6);
insert into cmdb_class_attribute_map values ('4028b881792074460179210677bb0016', '189319790e6349c7248b9f50456ed47b', 7);
insert into cmdb_class_attribute_map values ('4028b8817920744601792109b4800017', '4028b25d7916ef5f017917129c66000c', 1);
insert into cmdb_class_attribute_map values ('4028b8817920744601792109b4800017', '6ea67d6c6cb28def6b289affc6c95fd1', 2);
insert into cmdb_class_attribute_map values ('4028b8817920744601792109b4800017', '4028b25d791b52a501791b62eb220002', 3);
insert into cmdb_class_attribute_map values ('4028b8817920744601792109b4800017', '4028b25d791b52a501791b71ff790003', 4);
insert into cmdb_class_attribute_map values ('4028b8817920744601792109b4800017', '4028b25d791b52a501791b61f7170001', 5);
insert into cmdb_class_attribute_map values ('4028b8817920744601792109b4800017', '4028b25d791b52a501791b72ccbc0004', 6);
insert into cmdb_class_attribute_map values ('4028b8817920744601792109b4800017', '4028b25d791b52a501791b73fff90005', 7);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179211490200003', '4028b25d7916ef5f017916f397230000', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179211490200003', '4028b25d7916ef5f017916f594a70001', 2);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179211490200003', '4028b88179207446017920d8df1c000d', 3);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179211490200003', '4028b25d7916ef5f017916f71b030002', 4);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179211490200003', '4028b25d7916ef5f017916f7bd590003', 5);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179211490200003', '4028b25d7916ef5f017916fc8f1d0004', 6);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179211490200003', '4028b25d7916ef5f017916fd4e5b0005', 7);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179211490200003', '4028b25d7916ef5f017916fff6c60006', 8);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179211490200003', '4028b25d7916ef5f01791707c3b70007', 9);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179211490200003', '4028b25d7916ef5f0179170937d70008', 10);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179211490200003', '4028b25d7916ef5f0179170df7c80009', 11);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179211490200003', '4028b25d7916ef5f0179170ee2c7000a', 12);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179211490200003', '4028b25d7916ef5f01791711dc08000b', 13);
insert into cmdb_class_attribute_map values ('4028b88179210e1b01792117aab10004', '072fcb3be4056095a9af82dc6505b1e8', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b01792117aab10004', 'b5f16c33ca0531087ed1b46805a9c682', 2);
insert into cmdb_class_attribute_map values ('4028b88179210e1b01792117aab10004', '77b6112b3013a6808aeb04f80dd75360', 3);
insert into cmdb_class_attribute_map values ('4028b88179210e1b01792117aab10004', '4028b25d791b75ac01791bb4b48c0004', 4);
insert into cmdb_class_attribute_map values ('4028b88179210e1b01792117aab10004', '4028b25d791b75ac01791bb574a70005', 5);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179211d13760005', 'd47973f063130acab00b2cf203a9788b', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179211d13760005', '4028b25d7916ef5f01791713d145000d', 2);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179211d13760005', '4028b25d791b52a501791b5e35ff0000', 3);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179211d13760005', '4028b25d791c335201791c36103b0000', 4);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179211eb65d0006', '734ab921484883ad7760032a008baf21', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b017921201e8f0007', 'df0e88d216ace73e0164f3dbf7ade131', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b01792121eea80008', '4028b25d791c335201791c78e61e000e', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b01792123742e0009', '4028b25d791c335201791c7f51d8000f', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b017921242450000a', '4028b25d791c335201791c815b090010', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b01792125a23a000b', '4028b25d791c335201791c82c9ca0011', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b017921261f87000c', '4028b25d791c335201791c839afd0012', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b01792126a13a000d', '4028b25d791c335201791c84a9500013', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179212f17f90011', '4028b25d791b75ac01791bbb1ed10006', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179212f17f90011', '4028b25d791b75ac01791bbd4e5b0007', 2);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179212f17f90011', '4028b25d791b75ac01791bbe677f0008', 3);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179212f17f90011', '4028b25d791b75ac01791bc0759f0009', 4);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179212f17f90011', '4028b25d791b75ac01791bc0ffe3000a', 5);
insert into cmdb_class_attribute_map values ('4028b88179210e1b017921336fc60012', '489a14a0ebdca14b6eb42cf804330145', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b017921362c960013', '4028b25d791c335201791c5c52970002', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b01792136918f0014', '4028b25d791c335201791c5dd9240003', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b017921370e000015', '4028b25d791c335201791c5fa3850004', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b01792137989f0016', '4028b25d791c335201791c60844a0005', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179213972140017', '4028b25d791c335201791c617c790006', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179213a11f80018', '4028b25d791c335201791c62891d0007', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179213b50f50019', '4028b25d791c335201791c685fef0008', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179213baac3001a', '4028b25d791c335201791c6e00bc0009', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179213c2099001b', '4028b25d791c335201791c6f7892000a', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179213ca263001c', '4028b25d791c335201791c7057ef000b', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179213cfac9001d', '4028b25d791c335201791c711790000c', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179213d8436001e', '4028b25d791c335201791c71ff45000d', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179215a20da0028', '4028b88179210e1b017921555a73001f', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179215a98930029', '4028b88179210e1b01792155cae80020', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179215b19bb002a', '4028b88179210e1b017921563ea00021', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179215b8bff002b', '4028b88179210e1b01792156af0c0022', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179215c07b9002c', '4028b88179210e1b0179215719fb0023', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179215c8495002d', '4028b88179210e1b017921581a750024', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179215ce42d002e', '4028b88179210e1b0179215883dd0025', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179215d4b68002f', '4028b88179210e1b01792159017a0026', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179215dbbf80030', '4028b88179210e1b01792159687f0027', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b01792160f3010031', '4028b88179207446017920f72ed30015', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179216226d30032', '4028b25d791c335201791c88e4b30014', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b01792162e60f0033', '4028b25d791c335201791c91baa00015', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b017921643b830034', '4028b25d791c335201791c98e06e0016', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b017921651e3d0035', '4028b8817920744601792079683b0000', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b01792165deef0036', '4028b881792074460179207b35a20001', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b01792166dba20037', '4028b881792074460179207d3f7c0002', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b01792167e5f30038', '4028b8817920744601792080a98f0003', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179216a760a0039', '4028b8817920744601792082b3f20004', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179216af1c9003a', '4028b88179207446017920843f560005', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179216b6d55003b', '4028b881792074460179208588280006', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179216bf1a6003c', '4028b88179207446017920873e820007', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179216cf921003d', '4028b8817920744601792089f9390008', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179216e6e35003e', '4028b881792074460179208efdca000a', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179216f07b2003f', '4028b8817920744601792090af45000b', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179216f773b0040', '4028b881792074460179208c29bf0009', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179217130030041', 'adaeef4046bfcd78e345ad48cbbeefa5', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179217130030041', '4028b25d791b75ac01791b78b0550001', 2);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179217130030041', '27caaeba596663101d55a09ec873a375', 3);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179217130030041', '4028b881792074460179209cef74000c', 4);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179217130030041', '4028b25d791b75ac01791bb0f9140002', 5);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179217130030041', '189319790e6349c7248b9f50456ed47b', 6);
insert into cmdb_class_attribute_map values ('4028b88179210e1b01792172d1b80042', '4028b88179207446017920f09cc90014', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b01792172d1b80042', '4028b88179207446017920eb4f63000f', 2);
insert into cmdb_class_attribute_map values ('4028b88179210e1b01792172d1b80042', '4028b88179207446017920ec770e0010', 3);
insert into cmdb_class_attribute_map values ('4028b88179210e1b01792172d1b80042', '4028b88179207446017920ed30de0011', 4);
insert into cmdb_class_attribute_map values ('4028b88179210e1b01792172d1b80042', '4028b88179207446017920ee4ac50012', 5);
insert into cmdb_class_attribute_map values ('4028b88179210e1b01792172d1b80042', '4028b88179207446017920ef02e10013', 6);
insert into cmdb_class_attribute_map values ('4028b88179210e1b017921740c250043', '4028b25d791b75ac01791b78b0550001', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b017921740c250043', '27caaeba596663101d55a09ec873a375', 2);
insert into cmdb_class_attribute_map values ('4028b88179210e1b017921740c250043', '4028b881792074460179209cef74000c', 3);
insert into cmdb_class_attribute_map values ('4028b88179210e1b017921740c250043', '4028b25d791b75ac01791bb0f9140002', 4);
insert into cmdb_class_attribute_map values ('4028b88179210e1b017921740c250043', '4028b25d791b75ac01791bb14a4d0003', 5);
insert into cmdb_class_attribute_map values ('4028b88179210e1b017921740c250043', '189319790e6349c7248b9f50456ed47b', 6);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179217798830044', '4028b25d7916ef5f017916f397230000', 1);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179217798830044', '4028b25d7916ef5f017916f594a70001', 2);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179217798830044', '4028b88179207446017920d8df1c000d', 3);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179217798830044', '4028b25d7916ef5f017916f71b030002', 4);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179217798830044', '4028b25d7916ef5f017916f7bd590003', 5);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179217798830044', '4028b25d7916ef5f017916fc8f1d0004', 6);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179217798830044', '4028b25d7916ef5f0179170937d70008', 7);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179217798830044', '4028b25d7916ef5f0179170df7c80009', 8);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179217798830044', '4028b25d7916ef5f0179170ee2c7000a', 9);
insert into cmdb_class_attribute_map values ('4028b88179210e1b0179217798830044', '4028b25d7916ef5f01791711dc08000b', 10);
