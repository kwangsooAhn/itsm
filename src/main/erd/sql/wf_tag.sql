/**
 * 태그
 */
DROP TABLE IF EXISTS wf_tag cascade;

CREATE TABLE wf_tag
(
	tag_id varchar(128) NOT NULL,
	tag_content varchar(256) NOT NULL,
	CONSTRAINT wf_tag_pk PRIMARY KEY (tag_id),
	CONSTRAINT wf_tag_uk UNIQUE (tag_content)
);

COMMENT ON TABLE wf_tag IS '태그';
COMMENT ON COLUMN wf_tag.tag_id IS '태그아이디';
COMMENT ON COLUMN wf_tag.tag_content IS '태그내용';