/**
 * 문서번호패턴맵핑
 */
DROP TABLE IF EXISTS awf_rule_pattern_map cascade;

CREATE TABLE awf_rule_pattern_map
(
	numbering_id varchar(128) NOT NULL,
	pattern_id varchar(128) NOT NULL,
	pattern_order int NOT NULL,
	CONSTRAINT awf_rule_pattern_map_pk PRIMARY KEY (numbering_id, pattern_id, pattern_order),
	CONSTRAINT awf_rule_pattern_map_fk1 FOREIGN KEY (numbering_id) REFERENCES awf_numbering_rule (numbering_id),
	CONSTRAINT awf_rule_pattern_map_fk2 FOREIGN KEY (pattern_id) REFERENCES awf_numbering_pattern (pattern_id)
);

COMMENT ON TABLE awf_rule_pattern_map IS '문서번호 패턴 맵핑 테이블';
COMMENT ON COLUMN awf_rule_pattern_map.numbering_id IS '문서번호 아이디';
COMMENT ON COLUMN awf_rule_pattern_map.pattern_id IS '패턴 아이디';
COMMENT ON COLUMN awf_rule_pattern_map.pattern_order IS '패턴 순서';

insert into awf_rule_pattern_map values ('60211d93621zd1f126241s053c890122', '8a112d61751fs6f325714q053c421411', 0);
insert into awf_rule_pattern_map values ('60211d93621zd1f126241s053c890122', '8a112d61751fs6f325714q053c421412', 1);
insert into awf_rule_pattern_map values ('60211d93621zd1f126241s053c890122', '8a112d61751fs6f325714q053c421413', 2);
insert into awf_rule_pattern_map values ('40125c91714df6c325714e053c890125', '7a112d61751fs6f325714q053c421411', 0);
insert into awf_rule_pattern_map values ('40125c91714df6c325714e053c890125', '7a112d61751fs6f325714q053c421412', 1);
insert into awf_rule_pattern_map values ('40125c91714df6c325714e053c890125', '7a112d61751fs6f325714q053c421413', 2);
insert into awf_rule_pattern_map values ('40288ab7772dae0301772dbca28a0004', '40288ab7772dae0301772dba75b10003', 0);
insert into awf_rule_pattern_map values ('40288ab7772dae0301772dbca28a0004', '8a112d61751fs6f325714q053c421412', 1);
insert into awf_rule_pattern_map values ('40288ab7772dae0301772dbca28a0004', '8a112d61751fs6f325714q053c421413', 2);
insert into awf_rule_pattern_map values ('40288ab777f04ed90177f05e5ad7000a', '40288ab777f04ed90177f05dc2110009', 0);
insert into awf_rule_pattern_map values ('40288ab777f04ed90177f05e5ad7000a', '7a112d61751fs6f325714q053c421412', 1);
insert into awf_rule_pattern_map values ('40288ab777f04ed90177f05e5ad7000a', '8a112d61751fs6f325714q053c421413', 2);
insert into awf_rule_pattern_map values ('4028b8817880d833017880f34ae10003', '4028b8817880d833017880f26a920002', 0);
insert into awf_rule_pattern_map values ('4028b8817880d833017880f34ae10003', '7a112d61751fs6f325714q053c421412', 1);
insert into awf_rule_pattern_map values ('4028b8817880d833017880f34ae10003', '7a112d61751fs6f325714q053c421413', 2);
insert into awf_rule_pattern_map values ('4028b25d7886e2d801788704dd8e0002', '4028b25d7886e2d801788703c8a00001', 0);
insert into awf_rule_pattern_map values ('4028b25d7886e2d801788704dd8e0002', '7a112d61751fs6f325714q053c421412', 1);
insert into awf_rule_pattern_map values ('4028b25d7886e2d801788704dd8e0002', '8a112d61751fs6f325714q053c421413', 2);
insert into awf_rule_pattern_map values ('4028b88178c01b660178c0cc91310004', '4028b88178c01b660178c0cbe02d0003', 0);
insert into awf_rule_pattern_map values ('4028b88178c01b660178c0cc91310004', '7a112d61751fs6f325714q053c421412', 1);
insert into awf_rule_pattern_map values ('4028b88178c01b660178c0cc91310004', '8a112d61751fs6f325714q053c421413', 2);
insert into awf_rule_pattern_map values ('4028b88178c0fcc60178c10dbb5b0003', '4028b88178c0fcc60178c10d270c0002', 0);
insert into awf_rule_pattern_map values ('4028b88178c0fcc60178c10dbb5b0003', '7a112d61751fs6f325714q053c421412', 1);
insert into awf_rule_pattern_map values ('4028b88178c0fcc60178c10dbb5b0003', '8a112d61751fs6f325714q053c421413', 2);
insert into awf_rule_pattern_map values ('40288ab2808768300180881537d5001d', '40288ab280876830018088101fab001b', 0);
insert into awf_rule_pattern_map values ('40288ab2808768300180881537d5001d', '8a112d61751fs6f325714q053c421412', 1);
insert into awf_rule_pattern_map values ('40288ab2808768300180881537d5001d', '8a112d61751fs6f325714q053c421413', 2);
insert into awf_rule_pattern_map values ('40288ab2808768300180881cb9f6001e', '40288ab280876830018088105a0b001c', 0);
insert into awf_rule_pattern_map values ('40288ab2808768300180881cb9f6001e', '8a112d61751fs6f325714q053c421412', 1);
insert into awf_rule_pattern_map values ('40288ab2808768300180881cb9f6001e', '8a112d61751fs6f325714q053c421413', 2);
insert into awf_rule_pattern_map values ('4028b21c821e445f01821e4cc6d40001', '4028b21c821e445f01821e4986c20000', 0);
insert into awf_rule_pattern_map values ('4028b21c821e445f01821e4cc6d40001', '8a112d61751fs6f325714q053c421412', 1);
insert into awf_rule_pattern_map values ('4028b21c821e445f01821e4cc6d40001', '8a112d61751fs6f325714q053c421413', 2);
insert into awf_rule_pattern_map values ('40288a9d827ace2901827b1ba1220020', '40288a9d827ace2901827b1afd07001f', 0);
insert into awf_rule_pattern_map values ('40288a9d827ace2901827b1ba1220020', '8a112d61751fs6f325714q053c421412', 1);
insert into awf_rule_pattern_map values ('40288a9d827ace2901827b1ba1220020', '8a112d61751fs6f325714q053c421413', 2);
