/**
 * CMDB CI 태그정보
 */
DROP TABLE IF EXISTS cmdb_ci_tag cascade;

CREATE TABLE cmdb_ci_tag
(
	ci_id character varying(128) NOT NULL,
	tag_id character varying(128) NOT NULL,
	tag_name character varying(100),
	CONSTRAINT cmdb_ci_tag_pk PRIMARY KEY (ci_id, tag_id),
	CONSTRAINT cmdb_ci_tag_uk UNIQUE (tag_id),
	CONSTRAINT cmdb_ci_tag_fk FOREIGN KEY (ci_id)
      REFERENCES cmdb_ci (ci_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

COMMENT ON TABLE cmdb_ci_tag IS 'CMDB_CI태그정보';
COMMENT ON COLUMN cmdb_ci_tag.ci_id IS 'CI아이디';
COMMENT ON COLUMN cmdb_ci_tag.tag_id IS '태그아이디';
COMMENT ON COLUMN cmdb_ci_tag.tag_name IS '태그이름';
