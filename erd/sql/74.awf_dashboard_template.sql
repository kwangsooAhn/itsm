/**
  대시보드 템플릿
 */
DROP TABLE IF EXISTS awf_dashboard_template cascade;

CREATE TABLE awf_dashboard_template
(
    template_id varchar(128) not null,
    template_name varchar(128),
    template_config text,
    template_desc text,
    CONSTRAINT awf_dashboard_template_pk PRIMARY KEY (template_id),
    CONSTRAINT awf_dashboard_template_uq UNIQUE (template_name)
);

COMMENT ON TABLE awf_dashboard_template is '개인현황판 템플릿';
COMMENT ON COLUMN awf_dashboard_template.template_id is '템플릿아이디';
COMMENT ON COLUMN awf_dashboard_template.template_name is '템플릿 이름';
COMMENT ON COLUMN awf_dashboard_template.template_config is '템플릿 설정';
COMMENT ON COLUMN awf_dashboard_template.template_desc is '템플릿 설명';

-- 초기 데이터 샘플
INSERT INTO awf_dashboard_template VALUES ('template-001', '부서별 요청현황', '{
  "components": [
    {
      "key": "requestStatusByOrganization.chart",
      "title": "부서별 요청현황",
      "target": {
        "organizations": ["4028b2d57d37168e017d3715fae00002", "4028b2d57d37168e017d3713bb430003", "4028b2d57d37168e017d3715fae00004", "4028b2d57d37168e017d3715fae00005"],
        "documents": ["4028b21f7c90d996017c91ae7987004f", "4028b21f7c9adb6a017c9b18489900c9", "4028b21f7c9ff7c8017ca06bde520058", "2c9180867cc31a25017cc7a779d70523"]
      }
    }, {
      "key": "requestStatusByUser.list",
      "title": "개인 요청 현황",
      "target": {
        "documents": ["4028b21f7c90d996017c91ae7987004f", "4028b21f7c9adb6a017c9b18489900c9", "4028b21f7c9ff7c8017ca06bde520058", "2c9180867cc31a25017cc7a779d70523"]
      }
    }, {
      "key": "requestListByOrganization.list",
      "title": "요청현황",
      "target": {
        "documents": ["4028b21f7c90d996017c91ae7987004f", "4028b21f7c9adb6a017c9b18489900c9", "4028b21f7c9ff7c8017ca06bde520058", "2c9180867cc31a25017cc7a779d70523"],
        "items": [
         {
            "title": "순번",
            "width": "60px",
            "type": "index",
            "name": "",
            "dataType": "number"
          }, {
            "title": "신청부서",
            "width": "200px",
            "type": "mapping",
            "name": "z-sd-requester-department",
            "dataType": "string"
          }, {
            "title": "문서 종류",
            "width": "200px",
            "type": "field",
            "name": "document_name",
            "dataType": "string"
          }, {
            "title": "제목",
            "width": "300px",
            "type": "mapping",
            "name": "z-sd-request-title",
            "dataType": "string"
          }, {
            "title": "신청일시",
            "width": "150px",
            "type": "mapping",
            "name": "z-sd-request-date",
            "dataType": "dateTime"
          }, {
            "title": "완료 희망일시",
            "width": "150px",
            "type": "mapping",
            "name": "z-sd-request-deadline",
            "dataType": "dateTime"
          }, {
            "title": "상태",
            "width": "150px",
            "type": "field",
            "name": "instance_status",
            "dataType": "string"
          }, {
            "title": "PL",
            "width": "150px",
            "type": "mapping",
            "name": "",
            "dataType": "string"
          }, {
            "title": "신청자",
            "width": "150px",
            "type": "mapping",
            "name": "z-sd-requester",
            "dataType": "string"
          }, {
            "title": "난이도",
            "width": "80px",
            "type": "mapping",
            "name": "",
            "dataType": "string"
          }, {
            "title": "문서번호",
            "width": "300px",
            "type": "field",
            "name": "document_no",
            "dataType": "string"
          }
        ]
      }
    }
  ]
}', 'KB 저축은행에서 만든 첫 번째 템플릿');
