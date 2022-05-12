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

insert into awf_numbering_rule values ('60211d93621zd1f126241s053c890122', 'TEST-yyyyMMdd-000', '테스트 문서');
insert into awf_numbering_rule values ('40125c91714df6c325714e053c890125', 'CSR-yyyyMMdd-000', '서비스데스크 문서번호');
insert into awf_numbering_rule values ('40288ab7772dae0301772dbca28a0004', 'SAT-yyyyMMdd-000', '만족도문서번호');
insert into awf_numbering_rule values ('40288ab777f04ed90177f05e5ad7000a', 'CFG-yyyyMMdd-000', '구성관리 문서번호');
insert into awf_numbering_rule values ('4028b8817880d833017880f34ae10003', 'REL_yyyyMMdd-000', '릴리즈관리 문서번호');
insert into awf_numbering_rule values ('4028b25d7886e2d801788704dd8e0002', 'RFC-yyyyMMdd-000', '인프라, 어플리케이션 변경관리에서 사용되는 문서번호');
insert into awf_numbering_rule values ('4028b88178c0fcc60178c10dbb5b0003', 'INC-yyyyMMdd-000', '장애관리 문서번호');
insert into awf_numbering_rule values ('4028b88178c01b660178c0cc91310004', 'PBM-yyyyMMdd-000', '문제관리 문서번호');
insert into awf_numbering_rule values ('40288ab2808768300180881537d5001d', 'SMS', 'SMS 번호');
insert into awf_numbering_rule values ('40288ab2808768300180881cb9f6001e', 'NMS', 'NMS 번호');
