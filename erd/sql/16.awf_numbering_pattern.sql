/**
 * 넘버링패턴정보
 */
DROP TABLE IF EXISTS awf_numbering_pattern cascade;

CREATE TABLE awf_numbering_pattern
(
	pattern_id varchar(128) NOT NULL,
	pattern_name varchar(255) NOT NULL,
	pattern_type varchar(100) NOT NULL,
	pattern_value text,
	CONSTRAINT awf_numbering_pattern_pk PRIMARY KEY (pattern_id)
);

COMMENT ON TABLE awf_numbering_pattern IS '넘버링패턴정보';
COMMENT ON COLUMN awf_numbering_pattern.pattern_id IS '패턴아이디';
COMMENT ON COLUMN awf_numbering_pattern.pattern_name IS '패턴이름';
COMMENT ON COLUMN awf_numbering_pattern.pattern_type IS '패턴타입';
COMMENT ON COLUMN awf_numbering_pattern.pattern_value IS '패턴설정값';

insert into awf_numbering_pattern values ('8a112d61751fs6f325714q053c421411', 'Prefix', 'numbering.pattern.text', '{"value":"Test"}');
insert into awf_numbering_pattern values ('8a112d61751fs6f325714q053c421412', 'Date', 'numbering.pattern.date', '{"code":"pattern.format.yyyyMMdd"}');
insert into awf_numbering_pattern values ('8a112d61751fs6f325714q053c421413', 'Sequence', 'numbering.pattern.sequence', '{"digit":3,"start-with":1,"full-fill":"Y"}');
insert into awf_numbering_pattern values ('7a112d61751fs6f325714q053c421411', '문서 Prefix', 'numbering.pattern.text', '{"value":"CSR"}');
insert into awf_numbering_pattern values ('7a112d61751fs6f325714q053c421412', '문서 날짜', 'numbering.pattern.date', '{"code":"pattern.format.yyyyMMdd"}');
insert into awf_numbering_pattern values ('7a112d61751fs6f325714q053c421413', '문서 시퀀스', 'numbering.pattern.sequence', '{"digit":3,"start-with":1,"full-fill":"Y"}');
insert into awf_numbering_pattern values ('40288ab7772dae0301772dba75b10003', '만족도 Prefix', 'numbering.pattern.text', '{"value":"SAT"}' );
insert into awf_numbering_pattern values ('40288ab777f04ed90177f05dc2110009', '구성관리 Prefix', 'numbering.pattern.text', '{"value":"CFG"}');
insert into awf_numbering_pattern values ('4028b8817880d833017880f26a920002', '릴리즈관리 Prefix', 'numbering.pattern.text', '{"value":"REL"}');
insert into awf_numbering_pattern values ('4028b25d7886e2d801788703c8a00001', '변경관리 PreFix', 'numbering.pattern.text', '{"value":"RFC"}');
insert into awf_numbering_pattern values ('4028b88178c01b660178c0cbe02d0003', '문제관리 Prefix', 'numbering.pattern.text', '{"value":"PBM"}');
insert into awf_numbering_pattern values ('4028b88178c0fcc60178c10d270c0002', '장애관리 PreFix', 'numbering.pattern.text', '{"value":"INC"}');
insert into awf_numbering_pattern values ('4028b21c821e445f01821e4986c20000', '연속성관리  Prefix', 'numbering.pattern.text', '{"value":"SCM"}');
insert into awf_numbering_pattern values ('40288a9d827ace2901827b1afd07001f', '용량관리 Prefix', 'numbering.pattern.text', '{"value":"CAP"}');
insert into awf_numbering_pattern values ('40288ab280876830018088101fab001b', 'SMS', 'numbering.pattern.text', '{"value":"SMS"}');
insert into awf_numbering_pattern values ('40288ab280876830018088105a0b001c', 'NMS', 'numbering.pattern.text', '{"value":"NMS"}');
