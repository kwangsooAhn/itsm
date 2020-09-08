/**
 * 태그매핑테이블
 */
DROP TABLE IF EXISTS wf_tag_map cascade;

CREATE TABLE wf_tag_map
(
	tag_id varchar(128) NOT NULL,
	instance_id varchar(128) NOT NULL,
	CONSTRAINT wf_tag_map_pk PRIMARY KEY (tag_id, instance_id),
	CONSTRAINT wf_tag_map_fk1 FOREIGN KEY (tag_id) REFERENCES wf_tag (tag_id),
	CONSTRAINT wf_tag_map_fk2 FOREIGN KEY (instance_id) REFERENCES wf_instance (instance_id)
	
);

COMMENT ON TABLE wf_tag_map IS '태그매핑테이블';
COMMENT ON COLUMN wf_tag_map.tag_id IS '태그아이디';
COMMENT ON COLUMN wf_tag_map.instance_id IS '인스턴스아이디';