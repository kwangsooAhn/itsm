/**
 * 넘버링정보
 */
DROP TABLE IF EXISTS awf_numbering_rule cascade;

CREATE TABLE awf_numbering_rule
(
	numbering_id varchar(128) NOT NULL,
	numbering_name varchar(255) NOT NULL,
	numbering_desc text,
	latest_value text,
	latest_date timestamp,
	CONSTRAINT awf_numbering_rule_pk PRIMARY KEY (numbering_id)
);

COMMENT ON TABLE awf_numbering_rule IS '넘버링정보';
COMMENT ON COLUMN awf_numbering_rule.numbering_id IS '넘버링아이디';
COMMENT ON COLUMN awf_numbering_rule.numbering_name IS '넘버링명';
COMMENT ON COLUMN awf_numbering_rule.numbering_desc IS '넘버링설명';
COMMENT ON COLUMN awf_numbering_rule.latest_value IS '최근값';
COMMENT ON COLUMN awf_numbering_rule.latest_date IS '최근날짜';

insert into awf_numbering_rule values ('40125c91714df6c325714e053c890125', '문서 번호', '신청서 작성시 발생한 문서번호');
