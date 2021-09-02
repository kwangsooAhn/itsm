/**
 * API Token 정보
 */
DROP TABLE IF EXISTS cmdb_ci_instance_relation cascade;

CREATE TABLE cmdb_ci_instance_relation
(
    ci_id character varying(128) NOT NULL,
    instance_id character varying(128) NOT NULL,
    CONSTRAINT cmdb_ci_instance_relation_pk PRIMARY KEY (ci_id, instance_id),
    CONSTRAINT cmdb_ci_instance_relation_fk1 FOREIGN KEY (ci_id)
      REFERENCES cmdb_ci (ci_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT cmdb_ci_instance_relation_fk2 FOREIGN KEY (instance_id)
      REFERENCES wf_instance (instance_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

COMMENT ON TABLE cmdb_ci_instance_relation IS 'CMDB CI 관련문서 정보';
COMMENT ON COLUMN cmdb_ci_instance_relation.ci_id IS 'CI아이디';
COMMENT ON COLUMN cmdb_ci_instance_relation.instance_id IS '인스턴스ID';
