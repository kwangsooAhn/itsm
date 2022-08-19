/**
 * 스케줄작업정보
 */
DROP TABLE IF EXISTS awf_scheduled_task_mst cascade;

CREATE TABLE awf_scheduled_task_mst
(
	task_id varchar(128) NOT NULL,
	task_name varchar(128),
	task_type varchar(100),
	task_desc varchar(512),
	use_yn boolean default true,
	editable boolean default true,
	execute_class varchar(512),
	execute_query text,
	execute_command varchar(1024),
	execute_cycle_type varchar(100),
	execute_cycle_period bigint,
	cron_expression varchar(128),
	args varchar(128),
	src varchar(512),
	create_user_key varchar(128),
	create_dt timestamp,
	update_user_key varchar(128),
	update_dt timestamp,
	CONSTRAINT awf_scheduled_task_mst_pk PRIMARY KEY (task_id)
);

COMMENT ON TABLE awf_scheduled_task_mst IS '스케줄작업정보';
COMMENT ON COLUMN awf_scheduled_task_mst.task_id IS '작업아이디';
COMMENT ON COLUMN awf_scheduled_task_mst.task_name IS '작업명';
COMMENT ON COLUMN awf_scheduled_task_mst.task_type IS '작업유형';
COMMENT ON COLUMN awf_scheduled_task_mst.task_desc IS '작업설명';
COMMENT ON COLUMN awf_scheduled_task_mst.use_yn IS '사용여부';
COMMENT ON COLUMN awf_scheduled_task_mst.editable IS '수정가능여부';
COMMENT ON COLUMN awf_scheduled_task_mst.execute_class IS '실행클래스';
COMMENT ON COLUMN awf_scheduled_task_mst.execute_query IS '실행쿼리';
COMMENT ON COLUMN awf_scheduled_task_mst.execute_command IS '실행명령어';
COMMENT ON COLUMN awf_scheduled_task_mst.execute_cycle_type IS '실행주기유형';
COMMENT ON COLUMN awf_scheduled_task_mst.execute_cycle_period IS '실행주기간격';
COMMENT ON COLUMN awf_scheduled_task_mst.cron_expression IS '크론표현식';
COMMENT ON COLUMN awf_scheduled_task_mst.args IS 'arguments';
COMMENT ON COLUMN awf_scheduled_task_mst.src IS '경로';
COMMENT ON COLUMN awf_scheduled_task_mst.create_user_key IS '등록자';
COMMENT ON COLUMN awf_scheduled_task_mst.create_dt IS '등록일';
COMMENT ON COLUMN awf_scheduled_task_mst.update_user_key IS '수정자';
COMMENT ON COLUMN awf_scheduled_task_mst.update_dt IS '수정일';

insert into awf_scheduled_task_mst values ('4028b2647aada23c017aadd37b0c0001', '임시 첨부 파일 삭제', 'jar', '첨부된 파일 중 임시 저장된 파일을 삭제합니다.', 'TRUE', 'FALSE', null, null, 'java -jar deleteTempFile.jar', 'cron', null, '0 0 18 * * ?', null, '/deleteTempFile', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_scheduled_task_mst values ('4028b2647aada23c017aadcceabf0000', 'CMDB CI 임시데이터 삭제', 'jar', 'CMDB CI 등록시 저장된 임시 데이터 중 사용되지 않은 데이터를 삭제한다.', 'TRUE', 'FALSE', null, null, 'java -jar deleteTempCIData.jar', 'cron', null, '0 0 18 * * ?', null, '/deleteTempCIData', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_scheduled_task_mst values ('4028b2647a9890d5017a98a94efb0000', 'Zenius EMS 자산 연동', 'jar', 'Zenius EMS 7 과 연동하여 자산 정보를 수집한다.', 'TRUE', 'FALSE', null, null, 'java -jar alice-ems.jar', 'cron', null, '0 0 18 * * ?', null, '/zeniusEms', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_scheduled_task_mst values ('4028b2647aadd869017aadf4cf830000', 'Access Token 삭제', 'query', '기간이 초과된 access token 을 삭제한다.', 'TRUE', 'FALSE', null, 'delete from awf_api_token
where create_dt < now() - interval ''10day''', null, 'cron', null, '0 0 18 * * ?', null, null, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_scheduled_task_mst values ('4028b21e7c286680017c2868d9600000', '보고서 자동 생성', 'jar', '보고서 템플릿의 옵션 중, "자동 생성" 옵션이 설정된 템플릿을 수집하여, 보고서에 대한 자동 생성을 진행한다.', 'TRUE', 'FALSE', null, null, 'java -jar createReport.jar', 'cron', null, '0 0 18 * * ?', null, '/createReport', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_scheduled_task_mst values ('4028b2647fbed869039fdce4cf870321', 'IF 테이블 모니터링 - 외부시그널', 'jar', 'IF 모니터링 플러그인 연동을 통해 rest API 통신을 진행한다.(IF 테이블 설정 필요)', 'TRUE', 'FALSE', null, null, 'java -jar ifMonitoring.jar', 'cron', null, '0 0/5 * * * ?', null, '/ifMonitoring', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_scheduled_task_mst values ('40288a9d823d7ee301823d81f3b40000', 'Zenius EMS 용량 연동', 'jar', 'Zenius EMS 7 과 연동하여 용량 정보를 수집한다.', 'TRUE', 'FALSE', null, null, 'java -jar ems-capacity.jar', 'fixedDelay', 3600000, null, null, '/capacityEms', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_scheduled_task_mst values ('40288a8c828a3a5801828b7564e80018', 'Zenius EMS 이벤트 연동', 'jar', 'Zenius EMS 7 과 연동하여 이벤트 정보를 수집한다.', 'TRUE', 'FALSE', null, null, 'java -jar ems-event.jar', 'cron', null, '0 0/5 * * * ?', null, '/eventEms', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
