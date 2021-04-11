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
insert into wf_process values('40288ab777f04ed90177f057ca410000', '구성관리','process.status.use', '구성관리 프로세스입니다.', '0509e09412534a6e98f04ca79abb6424', now());
insert into wf_process values('40288ab77878ea67017878eb3dc30000', '릴리즈관리', 'process.status.use', '릴리즈관리 프로세스입니다.', '0509e09412534a6e98f04ca79abb6424', now());
insert into wf_process values('4028b25d78870b0901788766663a0023', '인프라변경관리', 'process.status.use', '인프라변경관리 프로세스입니다.', '0509e09412534a6e98f04ca79abb6424', now());
insert into wf_process values('4028b25d7888a7f4017888b1cde90000', '어플리케이션변경관리', 'process.status.use', '어플리케이션변경관리', '0509e09412534a6e98f04ca79abb6424', now());
insert into wf_process values('4028b25d788c4f8601788c905a790004', '만족도 - 서비스요청', 'process.status.use', '만족도 서비스 요청 프로세스입니다.', '0509e09412534a6e98f04ca79abb6424', now());
insert into wf_process values('4028b25d788c4f8601788c7e678a0001', '서비스데스크 - 서비스요청', 'process.status.use', '서비스데스크 - 서비스요청 프로세스입니다.', '0509e09412534a6e98f04ca79abb6424', now());
insert into wf_process values('4028b25d789037e50178906287a00003', '문제관리', 'process.status.use', '문제관리 프로세스입니다.', '0509e09412534a6e98f04ca79abb6424', now());
insert into wf_process values('4028b25d789037e50178904619930000', '서비스데스크 - 장애신고', 'process.status.use', '서비스데스크 - 장애신청 프로세스입니다.', '0509e09412534a6e98f04ca79abb6424', now());
insert into wf_process values('4028b25d789037e501789050f3020002', '장애관리', 'process.status.use', '장애관리 프로세스입니다.','0509e09412534a6e98f04ca79abb6424', now());
insert into wf_process values('4028b88178c0fcc60178c1301f040005', '만족도 - 장애관리', 'process.status.use', '만족도 장애관리 프로세스입니다.', '0509e09412534a6e98f04ca79abb6424', now());