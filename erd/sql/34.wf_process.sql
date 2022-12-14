/**
 * 프로세스정보
 */
DROP TABLE IF EXISTS wf_process cascade;

CREATE TABLE wf_process
(
	process_id varchar(128) NOT NULL,
	process_name varchar(256) NOT NULL,
	process_status varchar(100) NOT NULL,
	process_desc varchar(256),
	create_user_key varchar(128),
	create_dt timestamp,
	update_user_key varchar(128),
	update_dt timestamp,
	CONSTRAINT wf_process_pk PRIMARY KEY (process_id)
);

COMMENT ON TABLE wf_process IS '프로세스정보';
COMMENT ON COLUMN wf_process.process_id IS '프로세스아이디';
COMMENT ON COLUMN wf_process.process_name IS '프로세스이름';
COMMENT ON COLUMN wf_process.process_status IS '프로세스상태';
COMMENT ON COLUMN wf_process.process_desc IS '프로세스설명';
COMMENT ON COLUMN wf_process.create_user_key IS '생성자';
COMMENT ON COLUMN wf_process.create_dt IS '생성일시';
COMMENT ON COLUMN wf_process.update_user_key IS '수정자';
COMMENT ON COLUMN wf_process.update_dt IS '수정일시';

INSERT INTO wf_process VALUES ('4028b21f7c9698f4017c96a70ded0000','서비스데스크 - 단순문의','process.status.use','','0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO wf_process VALUES ('4028b21f7c9698f4017c96c5630c0002','서비스데스크 - 단순문의 - 만족도','process.status.use','','0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO wf_process VALUES ('4028b21f7c81a928017c81aa9dc60000','서비스데스크 - 장애신고','process.status.use','','0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO wf_process VALUES ('4028b21f7c9b6b1e017c9bdf04cb0011','서비스데스크 - 장애신고 - 만족도','process.status.use','','0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO wf_process VALUES ('4028b21f7c9ff7c8017ca0549ef00057','서비스데스크 - 서비스요청','process.status.use','','0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO wf_process VALUES ('4028b21f7c9cc269017c9cc76a5e0000','서비스데스크 - 서비스요청 - 만족도','process.status.use','','0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO wf_process VALUES ('2c9180837c94c0f3017c977775530001','서비스데스크 - 구성관리', 'process.status.use', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO wf_process VALUES ('2c9180867cc31a25017cc5c08e2f0120','서비스데스크 - 구성관리 - 만족도', 'process.status.use', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO wf_process VALUES ('4028b8817cbfc7a7017cc0db1a8c0bc3','인프라 변경관리', 'process.status.use', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO wf_process VALUES ('4028b8817cc50161017cc5079e850000','구성관리', 'process.status.use', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO wf_process VALUES ('4028b22f7cba6866017cbb63a53c08f8','어플리케이션 변경관리', 'process.status.use', '', '0509e09412534a6e98f04ca79abb6424',  now(), null, null);
INSERT INTO wf_process VALUES ('4028b21c7cdffb67017ce0b33f5e07b6','문제관리', 'process.status.use', '', '0509e09412534a6e98f04ca79abb6424',  now(), null, null);
INSERT INTO wf_process VALUES ('4028b21c7cdffb67017ce0b1d3af07b5','장애관리', 'process.status.use', '', '0509e09412534a6e98f04ca79abb6424',  now(), null, null);
INSERT INTO wf_process VALUES ('4028b21c821952af01821a0baf1c0175','연속성 관리','process.status.use', '', '0509e09412534a6e98f04ca79abb6424',  now(), null, null);
INSERT INTO wf_process VALUES ('40288a9d826b8fbf01826cd178780053', '용량 관리', 'process.status.use', '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
