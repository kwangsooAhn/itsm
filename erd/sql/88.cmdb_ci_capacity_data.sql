-- 용량관리 저장 테이블 생성
DROP TABLE if EXISTS cmdb_ci_capacity_data;

CREATE TABLE cmdb_ci_capacity_data (
    ci_id           VARCHAR (128) NOT NULL,
    reference_dt    TIMESTAMP NOT NULL,
    cpu_avg         FLOAT,
    memory_avg      FLOAT,
    disk_avg        FLOAT,
    mapping_id      VARCHAR (128),
    CONSTRAINT cmdb_ci_capacity_data_pk PRIMARY KEY (ci_id, reference_dt),
    CONSTRAINT cmdb_ci_capacity_data_fk FOREIGN KEY (ci_id)
    REFERENCES cmdb_ci(ci_id)
    ON UPDATE NO ACTION ON DELETE NO ACTION
);

COMMENT ON TABLE cmdb_ci_capacity_data IS '용량관리 정보';
COMMENT ON COLUMN cmdb_ci_capacity_data.ci_id IS 'CI아이디';
COMMENT ON COLUMN cmdb_ci_capacity_data.reference_dt IS '저장일시';
COMMENT ON COLUMN cmdb_ci_capacity_data.cpu_avg IS 'CPU사용량';
COMMENT ON COLUMN cmdb_ci_capacity_data.memory_avg IS '메모리사용량';
COMMENT ON COLUMN cmdb_ci_capacity_data.disk_avg IS '디스크사용량';
COMMENT ON COLUMN cmdb_ci_capacity_data.mapping_id IS '매핑아이디';
