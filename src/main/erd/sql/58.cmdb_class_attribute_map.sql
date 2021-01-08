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

insert into cmdb_class_attribute_map values ('df562114ab87c066adeaea79b2e4a8a2', '602b2c9216825bffc96ae69eeb73bdbc', 1);
insert into cmdb_class_attribute_map values ('df562114ab87c066adeaea79b2e4a8a2', '1d1338bb6316ab81f7c6adbc77199409', 2);
insert into cmdb_class_attribute_map values ('df562114ab87c066adeaea79b2e4a8a2', 'e613591ddea0f8c1f2457104f7cf286d', 3);
insert into cmdb_class_attribute_map values ('df562114ab87c066adeaea79b2e4a8a2', '6e247bdb7b70757e1987ae25a36c3d13', 4);
insert into cmdb_class_attribute_map values ('df562114ab87c066adeaea79b2e4a8a2', '6ea67d6c6cb28def6b289affc6c95fd1', 5);
insert into cmdb_class_attribute_map values ('df562114ab87c066adeaea79b2e4a8a2', '79a99dfa69d7d0c5c369ad4840815749', 6);
insert into cmdb_class_attribute_map values ('df562114ab87c066adeaea79b2e4a8a2', '932831a8e53aa6f795f608794e51e7e0', 7);
insert into cmdb_class_attribute_map values ('df562114ab87c066adeaea79b2e4a8a2', 'd47973f063130acab00b2cf203a9788b', 8);
insert into cmdb_class_attribute_map values ('df562114ab87c066adeaea79b2e4a8a2', 'ef60a5a1aa010de9b7ba2dda96107c5d', 9);
insert into cmdb_class_attribute_map values ('df562114ab87c066adeaea79b2e4a8a2', 'f4538a0d55c456461f1d0932fd424350', 10);
insert into cmdb_class_attribute_map values ('df562114ab87c066adeaea79b2e4a8a2', 'a508fbfda5d65a54b9b25dc5925d79bb', 11);
insert into cmdb_class_attribute_map values ('df562114ab87c066adeaea79b2e4a8a2', '189319790e6349c7248b9f50456ed47b', 12);
insert into cmdb_class_attribute_map values ('0d51e482f1a56e1074f69b5a1bce0138', 'ac4f3785cdbcc149a0b92dbf00af80ef', 1);
insert into cmdb_class_attribute_map values ('0d51e482f1a56e1074f69b5a1bce0138', '602b2c9216825bffc96ae69eeb73bdbc', 2);
insert into cmdb_class_attribute_map values ('0d51e482f1a56e1074f69b5a1bce0138', 'adaeef4046bfcd78e345ad48cbbeefa5', 3);
insert into cmdb_class_attribute_map values ('0d51e482f1a56e1074f69b5a1bce0138', 'c1f97be1aea3fdee785ca73b751f79d8', 4);
insert into cmdb_class_attribute_map values ('0d51e482f1a56e1074f69b5a1bce0138', '58e0cd57479bbb9d8a6b2bb6012206c2', 5);
insert into cmdb_class_attribute_map values ('0d51e482f1a56e1074f69b5a1bce0138', '27caaeba596663101d55a09ec873a375', 6);
insert into cmdb_class_attribute_map values ('0d51e482f1a56e1074f69b5a1bce0138', '99a8cf26726e907a95dad34e188cbfc8', 7);
insert into cmdb_class_attribute_map values ('0d51e482f1a56e1074f69b5a1bce0138', 'd0a35c07fa9bdd919a039f1f127cd54e', 8);
insert into cmdb_class_attribute_map values ('0d51e482f1a56e1074f69b5a1bce0138', '77b6112b3013a6808aeb04f80dd75360', 9);
insert into cmdb_class_attribute_map values ('0d51e482f1a56e1074f69b5a1bce0138', 'b5f16c33ca0531087ed1b46805a9c682', 10);
insert into cmdb_class_attribute_map values ('0d51e482f1a56e1074f69b5a1bce0138', '072fcb3be4056095a9af82dc6505b1e8', 11);
insert into cmdb_class_attribute_map values ('0d51e482f1a56e1074f69b5a1bce0138', 'a508fbfda5d65a54b9b25dc5925d79bb', 12);
insert into cmdb_class_attribute_map values ('0d51e482f1a56e1074f69b5a1bce0138', '189319790e6349c7248b9f50456ed47b', 13);
insert into cmdb_class_attribute_map values ('52905fc1ae0183698f726aec3e038148', 'a508fbfda5d65a54b9b25dc5925d79bb', 1);
insert into cmdb_class_attribute_map values ('85b3c35b31059e63aaa36ce2587ea070', '799afe719cd0bfe38797172bb77ae5d8', 1);
insert into cmdb_class_attribute_map values ('85b3c35b31059e63aaa36ce2587ea070', '489a14a0ebdca14b6eb42cf804330145', 2);
insert into cmdb_class_attribute_map values ('85b3c35b31059e63aaa36ce2587ea070', 'a508fbfda5d65a54b9b25dc5925d79bb', 3);
insert into cmdb_class_attribute_map values ('85b3c35b31059e63aaa36ce2587ea070', '189319790e6349c7248b9f50456ed47b', 4);
insert into cmdb_class_attribute_map values ('e6663412f62bd2d3daeeadd7a36a0b0d', 'bde6f4eac279ac3528c9cca16d12779a', 1);
insert into cmdb_class_attribute_map values ('e6663412f62bd2d3daeeadd7a36a0b0d', 'e651113f8a452f55f50ed41956cdfb34', 2);
insert into cmdb_class_attribute_map values ('40e346d210cd36229d03b403153e54ab', 'bde6f4eac279ac3528c9cca16d12779a', 1);
insert into cmdb_class_attribute_map values ('40e346d210cd36229d03b403153e54ab', 'e651113f8a452f55f50ed41956cdfb34', 2);
insert into cmdb_class_attribute_map values ('40e346d210cd36229d03b403153e54ab', '42b02142dd9128e47a35b737d4fc21ad', 3);
insert into cmdb_class_attribute_map values ('0e8dd74a27bbbf86201104e91df7ee88', '799afe719cd0bfe38797172bb77ae5d8', 1);
insert into cmdb_class_attribute_map values ('0e8dd74a27bbbf86201104e91df7ee88', '489a14a0ebdca14b6eb42cf804330145', 2);
insert into cmdb_class_attribute_map values ('0e8dd74a27bbbf86201104e91df7ee88', 'a508fbfda5d65a54b9b25dc5925d79bb', 3);
insert into cmdb_class_attribute_map values ('0e8dd74a27bbbf86201104e91df7ee88', '189319790e6349c7248b9f50456ed47b', 4);
insert into cmdb_class_attribute_map values ('39dbe77aa58b778064a0f4a10dd06b05', 'addc07e374faec9f0d6d3bbeca565886', 1);
insert into cmdb_class_attribute_map values ('39dbe77aa58b778064a0f4a10dd06b05', '734ab921484883ad7760032a008baf21', 2);
insert into cmdb_class_attribute_map values ('39dbe77aa58b778064a0f4a10dd06b05', '2bb03c41cd9998e77da9b737d4fcf9ab', 3);
insert into cmdb_class_attribute_map values ('f88ee1c29fdf9d847ba6002abc5bbf1b', 'df0e88d216ace73e0164f3dbf7ade131', 1);
