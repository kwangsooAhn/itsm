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

insert into wf_process values('4028b25d787736640178773e71480002', '만족도 - 단순문의', 'process.status.use', '만족도 단순문의 프로세스입니다.', '0509e09412534a6e98f04ca79abb6424', now());
insert into wf_process values('4028b25d78778da6017877b9df60000f', '서비스데스크 - 단순문의', 'process.status.use', '서비스데스크  단순문의 프로세스 입니다.', '0509e09412534a6e98f04ca79abb6424', now());
insert into wf_process values('40288ab777f04ed90177f057ca410000','구성관리','process.status.use','구성관리 프로세스입니다.','0509e09412534a6e98f04ca79abb6424', now());
insert into wf_process values('40288ab77878ea67017878eb3dc30000', '릴리즈관리', 'process.status.use', '릴리즈관리 프로세스입니다.', '0509e09412534a6e98f04ca79abb6424', now());

