/**
 * CMDB CI 연관관계
 */
DROP TABLE IF EXISTS cmdb_ci_relation cascade;

CREATE TABLE cmdb_ci_relation
(
	relation_id character varying(128) NOT NULL,
	relation_type character varying(100),
	source_ci_id character varying(128) NOT NULL,
	target_ci_id character varying(128) NOT NULL,
	CONSTRAINT cmdb_ci_relation_pk PRIMARY KEY (relation_id),
	CONSTRAINT cmdb_ci_relation_uk UNIQUE (relation_id),
	CONSTRAINT cmdb_ci_relation_fk1 FOREIGN KEY (source_ci_id)
      REFERENCES cmdb_ci (ci_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT cmdb_ci_relation_fk2 FOREIGN KEY (target_ci_id)
      REFERENCES cmdb_ci (ci_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

COMMENT ON TABLE cmdb_ci_relation IS 'CMDB CI 연관관계';
COMMENT ON COLUMN cmdb_ci_relation.relation_id IS '연관관계아이디';
COMMENT ON COLUMN cmdb_ci_relation.relation_type IS '연관관계타입';
COMMENT ON COLUMN cmdb_ci_relation.source_ci_id IS 'CI아이디(Master)';
COMMENT ON COLUMN cmdb_ci_relation.target_ci_id IS 'CI아이디(Slave)';
