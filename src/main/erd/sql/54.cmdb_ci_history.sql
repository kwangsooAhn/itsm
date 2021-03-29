/**
 * CMDB CI 정보 이력
 */
DROP TABLE IF EXISTS cmdb_ci_history cascade;

CREATE TABLE cmdb_ci_history
(
	history_id character varying(128) NOT NULL,
	ci_id character varying(128) NOT NULL,
	seq int NOT NULL,
	ci_no character varying(128),
	ci_name character varying(128),
	ci_status character varying(100),
	type_id character varying(128),
	class_id character varying(128),
	ci_icon character varying(200),
	ci_desc character varying(512),
	automatic boolean DEFAULT 'false',
	document_no character varying(128),
    create_user_key character varying(128),
    apply_dt timestamp,
	CONSTRAINT cmdb_ci_history_pk PRIMARY KEY (history_id),
	CONSTRAINT cmdb_ci_history_uk UNIQUE (history_id)
);

COMMENT ON TABLE cmdb_ci_history IS 'CMDB CI 정보 이력';
COMMENT ON COLUMN cmdb_ci_history.history_id IS '히스토리아이디';
COMMENT ON COLUMN cmdb_ci_history.ci_id IS 'CI아이디';
COMMENT ON COLUMN cmdb_ci_history.seq IS '이력시퀀스';
COMMENT ON COLUMN cmdb_ci_history.ci_no IS 'CI번호';
COMMENT ON COLUMN cmdb_ci_history.ci_name IS 'CI이름';
COMMENT ON COLUMN cmdb_ci_history.type_id IS '타입아이디';
COMMENT ON COLUMN cmdb_ci_history.class_id IS '클래스아이디';
COMMENT ON COLUMN cmdb_ci_history.ci_status IS 'CI상태';
COMMENT ON COLUMN cmdb_ci_history.ci_icon IS 'CI아이콘';
COMMENT ON COLUMN cmdb_ci_history.ci_desc IS 'CI설명';
COMMENT ON COLUMN cmdb_ci_history.automatic IS '자동등록여부';
COMMENT ON COLUMN cmdb_ci_history.document_no IS '관련문서번호';
COMMENT ON COLUMN cmdb_ci_history.create_user_key IS '신청자';
COMMENT ON COLUMN cmdb_ci_history.apply_dt IS '반영일시';
