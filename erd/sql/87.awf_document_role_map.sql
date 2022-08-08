/**
 * 신청서-역할 매핑
 */
DROP TABLE IF EXISTS awf_document_role_map cascade;

CREATE TABLE awf_document_role_map
(
    document_id varchar(128) NOT NULL,
    document_type varchar(100) NOT NULL,
    role_id varchar(100) NOT NULL,
    CONSTRAINT awf_document_role_map_pk PRIMARY KEY (document_id, role_id)
);

COMMENT ON TABLE awf_document_role_map  IS '신청서역할매핑';
COMMENT ON COLUMN awf_document_role_map.document_id IS '신청서아이디';
COMMENT ON COLUMN awf_document_role_map.document_type IS '신청서 타입';
COMMENT ON COLUMN awf_document_role_map.role_id IS '역할아이디';

/* 서비스데스크 - 단순문의 */
INSERT INTO awf_document_role_map VALUES ('4028b21f7c90d996017c91ae7987004f', 'application-form', 'service.admin');
INSERT INTO awf_document_role_map VALUES ('4028b21f7c90d996017c91ae7987004f', 'application-form', 'system.admin');
INSERT INTO awf_document_role_map VALUES ('4028b21f7c90d996017c91ae7987004f', 'application-form', 'workflow.admin');
INSERT INTO awf_document_role_map VALUES ('4028b21f7c90d996017c91ae7987004f', 'application-form', 'service.manager');
INSERT INTO awf_document_role_map VALUES ('4028b21f7c90d996017c91ae7987004f', 'application-form', 'portal.admin');
INSERT INTO awf_document_role_map VALUES ('4028b21f7c90d996017c91ae7987004f', 'application-form', 'general.user');
/* 서비스데스크 - 단순문의 - 만족도 */
INSERT INTO awf_document_role_map VALUES ('4028b21f7c90d996017c91aeff1b0050', 'workflow', 'service.admin');
INSERT INTO awf_document_role_map VALUES ('4028b21f7c90d996017c91aeff1b0050', 'workflow', 'system.admin');
INSERT INTO awf_document_role_map VALUES ('4028b21f7c90d996017c91aeff1b0050', 'workflow', 'workflow.admin');
INSERT INTO awf_document_role_map VALUES ('4028b21f7c90d996017c91aeff1b0050', 'workflow', 'service.manager');
INSERT INTO awf_document_role_map VALUES ('4028b21f7c90d996017c91aeff1b0050', 'workflow', 'portal.admin');
INSERT INTO awf_document_role_map VALUES ('4028b21f7c90d996017c91aeff1b0050', 'workflow', 'general.user');
/* 서비스데스크 - 장애신고 */
INSERT INTO awf_document_role_map VALUES ('4028b21f7c9adb6a017c9b18489900c9', 'application-form', 'service.admin');
INSERT INTO awf_document_role_map VALUES ('4028b21f7c9adb6a017c9b18489900c9', 'application-form', 'system.admin');
INSERT INTO awf_document_role_map VALUES ('4028b21f7c9adb6a017c9b18489900c9', 'application-form', 'workflow.admin');
INSERT INTO awf_document_role_map VALUES ('4028b21f7c9adb6a017c9b18489900c9', 'application-form', 'service.manager');
INSERT INTO awf_document_role_map VALUES ('4028b21f7c9adb6a017c9b18489900c9', 'application-form', 'portal.admin');
INSERT INTO awf_document_role_map VALUES ('4028b21f7c9adb6a017c9b18489900c9', 'application-form', 'general.user');
/* 서비스데스크 - 장애신고 - 만족도 */
INSERT INTO awf_document_role_map VALUES ('4028b21f7c9b6b1e017c9bedbe8a0012', 'workflow', 'service.admin');
INSERT INTO awf_document_role_map VALUES ('4028b21f7c9b6b1e017c9bedbe8a0012', 'workflow', 'system.admin');
INSERT INTO awf_document_role_map VALUES ('4028b21f7c9b6b1e017c9bedbe8a0012', 'workflow', 'workflow.admin');
INSERT INTO awf_document_role_map VALUES ('4028b21f7c9b6b1e017c9bedbe8a0012', 'workflow', 'service.manager');
INSERT INTO awf_document_role_map VALUES ('4028b21f7c9b6b1e017c9bedbe8a0012', 'workflow', 'portal.admin');
INSERT INTO awf_document_role_map VALUES ('4028b21f7c9b6b1e017c9bedbe8a0012', 'workflow', 'general.user');
/* 서비스데스크 - 서비스요청 */
INSERT INTO awf_document_role_map VALUES ('4028b21f7c9ff7c8017ca06bde520058', 'application-form', 'service.admin');
INSERT INTO awf_document_role_map VALUES ('4028b21f7c9ff7c8017ca06bde520058', 'application-form', 'system.admin');
INSERT INTO awf_document_role_map VALUES ('4028b21f7c9ff7c8017ca06bde520058', 'application-form', 'workflow.admin');
INSERT INTO awf_document_role_map VALUES ('4028b21f7c9ff7c8017ca06bde520058', 'application-form', 'service.manager');
INSERT INTO awf_document_role_map VALUES ('4028b21f7c9ff7c8017ca06bde520058', 'application-form', 'portal.admin');
INSERT INTO awf_document_role_map VALUES ('4028b21f7c9ff7c8017ca06bde520058', 'application-form', 'general.user');
/* 서비스데스크 - 서비스요청 - 만족도 */
INSERT INTO awf_document_role_map VALUES ('4028b21f7c9ff7c8017ca04d16830000', 'workflow', 'service.admin');
INSERT INTO awf_document_role_map VALUES ('4028b21f7c9ff7c8017ca04d16830000', 'workflow', 'system.admin');
INSERT INTO awf_document_role_map VALUES ('4028b21f7c9ff7c8017ca04d16830000', 'workflow', 'workflow.admin');
INSERT INTO awf_document_role_map VALUES ('4028b21f7c9ff7c8017ca04d16830000', 'workflow', 'service.manager');
INSERT INTO awf_document_role_map VALUES ('4028b21f7c9ff7c8017ca04d16830000', 'workflow', 'portal.admin');
INSERT INTO awf_document_role_map VALUES ('4028b21f7c9ff7c8017ca04d16830000', 'workflow', 'general.user');
/* 서비스데스크 - 구성관리 */
INSERT INTO awf_document_role_map VALUES ('2c9180867cc31a25017cc7a779d70523', 'application-form-workflow', 'service.admin');
INSERT INTO awf_document_role_map VALUES ('2c9180867cc31a25017cc7a779d70523', 'application-form-workflow', 'system.admin');
INSERT INTO awf_document_role_map VALUES ('2c9180867cc31a25017cc7a779d70523', 'application-form-workflow', 'workflow.admin');
INSERT INTO awf_document_role_map VALUES ('2c9180867cc31a25017cc7a779d70523', 'application-form-workflow', 'service.manager');
INSERT INTO awf_document_role_map VALUES ('2c9180867cc31a25017cc7a779d70523', 'application-form-workflow', 'portal.admin');
INSERT INTO awf_document_role_map VALUES ('2c9180867cc31a25017cc7a779d70523', 'application-form-workflow', 'general.user');
/* 서비스데스크 - 구성관리 - 만족도 */
INSERT INTO awf_document_role_map VALUES ('2c9180867cc31a25017cc5ca1a9f0145', 'workflow', 'service.admin');
INSERT INTO awf_document_role_map VALUES ('2c9180867cc31a25017cc5ca1a9f0145', 'workflow', 'system.admin');
INSERT INTO awf_document_role_map VALUES ('2c9180867cc31a25017cc5ca1a9f0145', 'workflow', 'workflow.admin');
INSERT INTO awf_document_role_map VALUES ('2c9180867cc31a25017cc5ca1a9f0145', 'workflow', 'service.manager');
INSERT INTO awf_document_role_map VALUES ('2c9180867cc31a25017cc5ca1a9f0145', 'workflow', 'portal.admin');
INSERT INTO awf_document_role_map VALUES ('2c9180867cc31a25017cc5ca1a9f0145', 'workflow', 'general.user');
/* 인프라 변경관리 */
INSERT INTO awf_document_role_map VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', 'application-form-workflow', 'service.admin');
INSERT INTO awf_document_role_map VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', 'application-form-workflow', 'system.admin');
INSERT INTO awf_document_role_map VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', 'application-form-workflow', 'workflow.admin');
INSERT INTO awf_document_role_map VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', 'application-form-workflow', 'service.manager');
INSERT INTO awf_document_role_map VALUES ('4028b8817cbfc7a7017cc0e65b260bdf', 'application-form-workflow', 'portal.admin');
/* 구성관리 */
INSERT INTO awf_document_role_map VALUES ('4028b8817cc50161017cc53233c206b0', 'workflow', 'service.admin');
INSERT INTO awf_document_role_map VALUES ('4028b8817cc50161017cc53233c206b0', 'workflow', 'system.admin');
INSERT INTO awf_document_role_map VALUES ('4028b8817cc50161017cc53233c206b0', 'workflow', 'workflow.admin');
INSERT INTO awf_document_role_map VALUES ('4028b8817cc50161017cc53233c206b0', 'workflow', 'service.manager');
INSERT INTO awf_document_role_map VALUES ('4028b8817cc50161017cc53233c206b0', 'workflow', 'cmdb.admin');
/* 어플리케이션 변경관리 */
INSERT INTO awf_document_role_map VALUES ('4028b22f7cc55c1a017cc5775d10026b', 'application-form-workflow', 'service.admin');
INSERT INTO awf_document_role_map VALUES ('4028b22f7cc55c1a017cc5775d10026b', 'application-form-workflow', 'system.admin');
INSERT INTO awf_document_role_map VALUES ('4028b22f7cc55c1a017cc5775d10026b', 'application-form-workflow', 'workflow.admin');
INSERT INTO awf_document_role_map VALUES ('4028b22f7cc55c1a017cc5775d10026b', 'application-form-workflow', 'service.manager');
INSERT INTO awf_document_role_map VALUES ('4028b22f7cc55c1a017cc5775d10026b', 'application-form-workflow', 'portal.admin');
/* 문제관리 */
INSERT INTO awf_document_role_map VALUES ('4028b21c7cdffb67017ce0b4d3e30872', 'workflow', 'service.admin');
INSERT INTO awf_document_role_map VALUES ('4028b21c7cdffb67017ce0b4d3e30872', 'workflow', 'system.admin');
INSERT INTO awf_document_role_map VALUES ('4028b21c7cdffb67017ce0b4d3e30872', 'workflow', 'workflow.admin');
INSERT INTO awf_document_role_map VALUES ('4028b21c7cdffb67017ce0b4d3e30872', 'workflow', 'service.manager');
INSERT INTO awf_document_role_map VALUES ('4028b21c7cdffb67017ce0b4d3e30872', 'workflow', 'portal.admin');
/* 장애관리 */
INSERT INTO awf_document_role_map VALUES ('4028b21c7cdffb67017ce0b70509093e', 'workflow', 'service.admin');
INSERT INTO awf_document_role_map VALUES ('4028b21c7cdffb67017ce0b70509093e', 'workflow', 'system.admin');
INSERT INTO awf_document_role_map VALUES ('4028b21c7cdffb67017ce0b70509093e', 'workflow', 'workflow.admin');
INSERT INTO awf_document_role_map VALUES ('4028b21c7cdffb67017ce0b70509093e', 'workflow', 'service.manager');
INSERT INTO awf_document_role_map VALUES ('4028b21c7cdffb67017ce0b70509093e', 'workflow', 'portal.admin');
/* 서비스 연속성 관리 계획서 */
INSERT INTO awf_document_role_map VALUES ('4028b21c82343bfb01823453b1bf0029','application-form', 'continuity.manager');
INSERT INTO awf_document_role_map VALUES ('4028b21c82343bfb01823453b1bf0029','application-form', 'system.admin');
INSERT INTO awf_document_role_map VALUES ('4028b21c82343bfb01823453b1bf0029','application-form', 'continuity.admin');
/* 복구 훈련 계획서 */
INSERT INTO awf_document_role_map VALUES ('4028b21c82343bfb0182345ad3bf002a','application-form', 'continuity.manager');
INSERT INTO awf_document_role_map VALUES ('4028b21c82343bfb0182345ad3bf002a','application-form', 'system.admin');
INSERT INTO awf_document_role_map VALUES ('4028b21c82343bfb0182345ad3bf002a','application-form', 'continuity.admin');
/* 복구 훈련 결과서 */
INSERT INTO awf_document_role_map VALUES ('4028b21c82343bfb01823466bff5002b','application-form', 'continuity.manager');
INSERT INTO awf_document_role_map VALUES ('4028b21c82343bfb01823466bff5002b','application-form', 'system.admin');
INSERT INTO awf_document_role_map VALUES ('4028b21c82343bfb01823466bff5002b','application-form', 'continuity.admin');
/* 용량 검토 보고서 */
INSERT INTO awf_document_role_map VALUES ('40288a9d826b8fbf01826cdf77130082', 'document.type.application-form', 'system.admin');
INSERT INTO awf_document_role_map VALUES ('40288a9d826b8fbf01826cdf77130082', 'document.type.application-form', 'cmdb.admin');
