/**
 * 보고서 설정 테이블 (템플릿)
 */
DROP TABLE IF EXISTS awf_report_template cascade;

create table awf_report_template (
    template_id varchar(128) NOT NULL,
    template_name varchar(128) NOT NULL,
    template_desc varchar(512),
    report_name varchar(128),
    automatic boolean DEFAULT false,
    create_user_key varchar(128),
    create_dt timestamp,
    update_user_key varchar(128),
    update_dt timestamp,
    CONSTRAINT awf_report_template_pk PRIMARY KEY (template_id)
);

COMMENT ON TABLE awf_report_template IS '보고서 템플릿 정보';
COMMENT ON COLUMN awf_report_template.template_id IS '템플릿아이디';
COMMENT ON COLUMN awf_report_template.template_name IS '템플릿명';
COMMENT ON COLUMN awf_report_template.template_desc IS '템플릿설명';
COMMENT ON COLUMN awf_report_template.report_name IS '보고서명';
COMMENT ON COLUMN awf_report_template.automatic IS '자동생성여부';
COMMENT ON COLUMN awf_report_template.create_user_key IS '생성자';
COMMENT ON COLUMN awf_report_template.create_dt IS '생성일시';
COMMENT ON COLUMN awf_report_template.update_user_key IS '수정자';
COMMENT ON COLUMN awf_report_template.update_dt IS '수정일시';

INSERT INTO awf_report_template VALUES ('4028b8817cd4629c017cd4c47c49001d', '2021년 월간 서비스데스크 현황', '유형별 등록 건수/ 유형별 완료 건수/ 유형별 평균 처리 시간/ 납기준수율/ 년간 만족도 평가/
단순문의 만족도 평가/ 장애관리 만족도 평가/ 변경관리 만족도 평가/ 구성관리 만족도 평가로
월간 서비스 데스크 현황을 확인 할 수 있다.', '2021년 월간 서비스데스크 현황', true, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_report_template VALUES ('4028b8817cd4629c017cd4eced9e001e', '2021년 월간 장애관리 현황', '월간 장애등급별 등록건수, 월간 장애등급별 완료건수, 월간 장애관리 납기 준수율을 확인 할 수 있다.', '2021년 월간 장애관리 현황', true, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_report_template VALUES ('4028b8817cd4629c017cd4f0c3150020', '2021년 월간 문제관리 현황', '월간 문제관리 등록건수, 월간 문제관리 완료건수, 월간 문제관리 납기 준수율 확인 할수 있다.', '2021년 월간 문제관리 현황', true, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_report_template VALUES ('4028b8817cd4629c017cd4f1df090021', '2021년 월간 인프라변경관리 현황', '월간 인프라변경관리 등록건수, 월간 인프라변경관리 완료건수, 월간 인프라변경관리 작업결과 건수
월간 인프라변경관리 납기 준수율을 확인 할 수 있다.
', '2021년 월간 인프라변경관리 현황', true, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_report_template VALUES ('4028b8817cd4629c017cd4f485300022', '2021년 월간 구성관리 현황', '월간 구성관리 등록건수, 월간 구성관리 완료건수, 월간 구성관리 납기 준수율 확인 할수 있다.', '2021년 월간 구성관리 현황', true, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_report_template VALUES ('4028b8817cd4629c017cd50244af00a4', '2021년 월간 어플리케이션변경관리 현황', '월간 어플리케이션변경관리 등록건수, 월간 어플리케이션변경관리 완료건수, 월간 어플리케이션변경관리 작업결과 건수
월간 어플리케이션변경관리 납기 준수율을 확인 할 수 있다.', '2021년 월간 어플리케이션변경관리 현황', true, '0509e09412534a6e98f04ca79abb6424', now(), null, null);