/**
  조직역할매핑
 */
DROP TABLE IF EXISTS awf_organization_role_map cascade;

create table awf_organization_role_map
(
    organization_id varchar(100) NOT NULL,
    role_id varchar(100) NOT NULL,
    CONSTRAINT awf_organization_role_map_pk PRIMARY KEY (organization_id, role_id),
    CONSTRAINT awf_organization_role_map_fk1 FOREIGN KEY (organization_id) REFERENCES awf_organization (organization_id),
    CONSTRAINT awf_organization_role_map_fk2 FOREIGN KEY (role_id) REFERENCES awf_role (role_id)
);

COMMENT ON TABLE awf_organization_role_map IS '조직역할매핑';
COMMENT ON COLUMN awf_organization_role_map.organization_id IS '그룹아이디';
COMMENT ON COLUMN awf_organization_role_map.role_id IS '역할아이디';
