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
	execute_query varchar(1024),
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

insert into awf_scheduled_task_mst values ('4021c26175cd53df0175bb545fb30000', '임시 첨부 파일 삭제', 'class', null, 'TRUE', 'FALSE', 'co.brainz.framework.scheduling.task.DeleteTempFile', null, 'cron', 0, '0 0 12 * * *', null, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_scheduled_task_mst values ('4142d16273df21de0175be515fc20000', 'CMDB CI 임시데이터 삭제', 'class', null, 'TRUE', 'FALSE', 'co.brainz.framework.scheduling.task.DeleteTempCIData', null, 'cron', 0, '0 0 12 * * *', 'postgresql, 10.0.10.175, 32784, itsm, itsm, itsm123', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
