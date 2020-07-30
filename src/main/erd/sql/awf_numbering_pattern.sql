/**
 * 넘버링패턴정보
 */
DROP TABLE IF EXISTS awf_numbering_pattern cascade;

CREATE TABLE awf_numbering_pattern
(
	pattern_id varchar(128) NOT NULL,
	numbering_id varchar(128) NOT NULL,
	pattern_name varchar(255) NOT NULL,
	pattern_type varchar(100) NOT NULL,
	pattern_value text,
	pattern_order int NOT NULL,
	CONSTRAINT awf_numbering_pattern_pk PRIMARY KEY (pattern_id),
	CONSTRAINT awf_numbering_pattern_fk FOREIGN KEY (numbering_id) REFERENCES awf_numbering_rule (numbering_id)
);

COMMENT ON TABLE awf_numbering_pattern IS '넘버링패턴정보';
COMMENT ON COLUMN awf_numbering_pattern.pattern_id IS '패턴아이디';
COMMENT ON COLUMN awf_numbering_pattern.numbering_id IS '넘버링아이디';
COMMENT ON COLUMN awf_numbering_pattern.pattern_name IS '패턴이름';
COMMENT ON COLUMN awf_numbering_pattern.pattern_type IS '패턴타입';
COMMENT ON COLUMN awf_numbering_pattern.pattern_value IS '패턴설정값';
COMMENT ON COLUMN awf_numbering_pattern.pattern_order IS '순서';

insert into awf_numbering_pattern values ('7a112d61751fs6f325714q053c421411', '40125c91714df6c325714e053c890125', '문서 Prefix', 'numbering.pattern.text', '{"value":"CSR"}', 1);
insert into awf_numbering_pattern values ('7a112d61751fs6f325714q053c421412', '40125c91714df6c325714e053c890125', '문서 날짜', 'numbering.pattern.date', '{"code":"pattern.format.yyyyMMdd"}', 2);
insert into awf_numbering_pattern values ('7a112d61751fs6f325714q053c421413', '40125c91714df6c325714e053c890125', '문서 시퀀스', 'numbering.pattern.sequence', '{"digit":3,"start-with":1,"full-fill":"Y"}', 3);