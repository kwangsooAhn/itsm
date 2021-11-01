/**
 * 차트설정
 */
DROP TABLE IF EXISTS awf_chart cascade;

CREATE TABLE awf_chart
(
    chart_id character varying(128) NOT NULL,
    chart_type character varying(128) NOT NULL,
    chart_name character varying(256) NOT NULL,
    chart_desc text,
    chart_config text NOT NULL,
    create_user_key character varying(128),
    create_dt timestamp,
    update_user_key character varying(128),
    update_dt timestamp,
    CONSTRAINT awf_chart_pk PRIMARY KEY (chart_id)
);

COMMENT ON TABLE awf_chart IS '차트설정';
COMMENT ON COLUMN awf_chart.chart_id IS  '차트아이디';
COMMENT ON COLUMN awf_chart.chart_type IS '차트타입';
COMMENT ON COLUMN awf_chart.chart_name IS '차트이름';
COMMENT ON COLUMN awf_chart.chart_desc IS '차트설명';
COMMENT ON COLUMN awf_chart.chart_config IS '차트설정';
COMMENT ON COLUMN awf_chart.create_user_key IS '등록자';
COMMENT ON COLUMN awf_chart.create_dt IS '등록일시';
COMMENT ON COLUMN awf_chart.update_user_key IS '수정자';
COMMENT ON COLUMN awf_chart.update_dt IS '수정일시';

INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd4659dde0000', 'chart.stackedColumn', '월간 유형별 등록 건수', '월별로 서비스데스크 유형별 등록 건수를 확인 한다.', '{"range":{"type":"chart.range.between","from":"2021-01-01","to":"2021-12-31"},"operation":"count","periodUnit":"M","group":"","tags":["z-serviceDesk-inquiry","z-serviceDesk-incident","z-serviceDesk-change","z-serviceDesk-configuration"]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd46affee0001', 'chart.stackedColumn', '월간 유형별 완료 건수', '월별로 서비스데스크 유형별 완료 건수를 확인 한다.', '{"range":{"type":"chart.range.between","from":"2021-01-01","to":"2021-12-31"},"operation":"count","periodUnit":"M","group":"","tags":["z-serviceDesk-inquiry-approve-date","z-serviceDesk-incident-approve-date","z-serviceDesk-change-approve-date","z-serviceDesk-configuration-approve-date"]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd47752aa0002', 'chart.stackedColumn', '월간 납기준수율 - 단순문의', '월별로 서비스데스크- 단순문의 납기준수율 건수(성공/실패)를 확인 한다.', '{"range":{"type":"chart.range.between","from":"2021-01-01","to":"2021-12-31"},"operation":"percent","periodUnit":"M","group":"","tags":["z-serviceDesk-inquiry-request-deadline","z-serviceDesk-inquiry-approve-date"]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd4902183000a', 'chart.stackedColumn', '월간 납기준수율 - 장애관리', '월별로 서비스데스크- 장애관리 납기준수율 건수(성공/실패)를 확인 한다.', '{"range":{"type":"chart.range.between","from":"2021-01-01","to":"2021-12-31"},"operation":"percent","periodUnit":"M","group":"","tags":["z-serviceDesk-incident-request-deadline","z-serviceDesk-incident-approve-date"]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd47827cb0003', 'chart.stackedColumn', '월간 납기준수율 - 변경관리', '월별로 서비스데스크- 변경관리 납기준수율 건수(성공/실패)를 확인 한다.', '{"range":{"type":"chart.range.between","from":"2021-01-01","to":"2021-12-31"},"operation":"percent","periodUnit":"M","group":"","tags":["z-serviceDesk-change-request-deadline","z-serviceDesk-change-approve-date"]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd478d0a10004', 'chart.stackedColumn', '월간 납기준수율 - 구성관리', '월별로 서비스데스크- 구성관리 납기준수율 건수(성공/실패)를 확인 한다.', '{"range":{"type":"chart.range.between","from":"2021-01-01","to":"2021-12-31"},"operation":"percent","periodUnit":"M","group":"","tags":["z-serviceDesk-configuration-request-deadline","z-serviceDesk-configuration-approve-date"]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd479a9890005', 'chart.pie', '년간 만족도 평가', '월별로 단순문의, 장애관리, 변경관리, 구성관리의 만족도 평가 값의 건수를 확인 한다.', '{"range":{"type":"chart.range.between","from":"2021-01-01","to":"2021-12-31"},"operation":"count","periodUnit":"Y","group":"","tags":["z-serviceDesk-inquiry-satisfaction","z-serviceDesk-incident-satisfaction","z-serviceDesk-change-satisfaction","z-serviceDesk-configuration-satisfaction"]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd4838b2e0007', 'chart.stackedColumn', '월간 단순문의 만족도 평가', '월별로 단순문의 만족도 평가 값의 점수를 확인 한다.', '{"range":{"type":"chart.range.between","from":"2021-01-01","to":"2021-12-31"},"operation":"count","periodUnit":"M","group":"","tags":["z-serviceDesk-inquiry-satisfaction"]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd48588b50008', 'chart.stackedColumn', '월간 장애관리 만족도 평가', '월별로 장애관리 만족도 평가 값의 점수를 확인 한다.', '{"range":{"type":"chart.range.between","from":"2021-01-01","to":"2021-12-31"},"operation":"count","periodUnit":"M","group":"","tags":["z-serviceDesk-incident-satisfaction"]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd4863bfc0009', 'chart.stackedColumn', '월간 변경관리 만족도 평가', '월별로 변경관리 만족도 평가 값의 점수를 확인 한다.', '{"range":{"type":"chart.range.between","from":"2021-01-01","to":"2021-12-31"},"operation":"count","periodUnit":"M","group":"","tags":["z-serviceDesk-change-satisfaction"]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd47a4c0d0006', 'chart.stackedColumn', '월간 구성관리 만족도 평가', '월별로 구성관리 만족도 평가 값의 점수를 확인 한다.', '{"range":{"type":"chart.range.between","from":"2021-01-01","to":"2021-12-31"},"operation":"count","periodUnit":"M","group":"","tags":["z-serviceDesk-configuration-satisfaction"]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);

INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd49b24ea000b', 'chart.basicLine', '월간 장애 등록건수', '월별로 장애관리 등록 건수를 확인한다.', '{"range":{"type":"chart.range.between","from":"2021-01-01","to":"2021-12-31"},"operation":"count","periodUnit":"M","group":"장애등록건수","tags":["z-incident"]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd49c25e2000c', 'chart.basicLine', '월간 장애 완료건수', '월별로 장애관리 완료 건수를 확인한다.', '{"range":{"type":"chart.range.between","from":"2021-01-01","to":"2021-12-31"},"operation":"count","periodUnit":"M","group":"장애완료건수","tags":["z-incident-approve-date"]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd49d1ef3000d', 'chart.stackedColumn', '월간 장애등급별 건수', '월별로 장애등급(1,2,3,4 등급)별로  등록건수를 확인한다.', '{"range":{"type":"chart.range.between","from":"2021-01-01","to":"2021-12-31"},"operation":"count","periodUnit":"M","group":"","tags":["z-incident-level"]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd49e0d34000e', 'chart.stackedColumn', '월간 장애관리 납기 준수율', '월별로 장애관리 납기준수율 건수(성공/실패)를 확인한다.', '{"range":{"type":"chart.range.between","from":"2021-01-01","to":"2021-12-31"},"operation":"count","periodUnit":"M","group":"","tags":["z-incident-request-deadline","z-incident-approve-date"]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);

INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd49f3a34000f', 'chart.basicLine', '월간 문제관리 등록건수', '월별로 문제관리 등록 건수를 확인한다.', '{"range":{"type":"chart.range.between","from":"2021-01-01","to":"2021-12-31"},"operation":"count","periodUnit":"M","group":"문제관리등록건수","tags":["z-problem"]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd49fec730010', 'chart.basicLine', '월간 문제관리 완료건수', '월별로 문제관리 완료 건수를 확인한다.', '{"range":{"type":"chart.range.between","from":"2021-01-01","to":"2021-12-31"},"operation":"count","periodUnit":"M","group":"문제관리완료건수","tags":["z-problem-approve-date"]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd4a092020011', 'chart.stackedColumn', '월간 문제관리 납기 준수율', '월별로 문제관리 납기준수율 건수(성공/실패)를 확인한다.', '{"range":{"type":"chart.range.between","from":"2021-01-01","to":"2021-12-31"},"operation":"count","periodUnit":"M","group":"","tags":["z-problem-request-deadline","z-problem-approve-date"]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);

INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd4a2b5c90012', 'chart.basicLine', '월간 인프라변경관리 등록건수', '월별로 인프라변경관리 등록 건수를 확인한다.', '{"range":{"type":"chart.range.between","from":"2021-01-01","to":"2021-12-31"},"operation":"count","periodUnit":"M","group":"인프라변경관리등록","tags":["z-change-infra"]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd4a3758c0013', 'chart.basicLine', '월간 인프라변경관리 완료건수', '월별로 인프라변경관리 완료 건수를 확인한다.', '{"range":{"type":"chart.range.between","from":"2021-01-01","to":"2021-12-31"},"operation":"count","periodUnit":"M","group":"인프라변경관리완료","tags":["z-change-infra-complete-review-date"]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd4a41aee0014', 'chart.stackedColumn', '월간 인프라변경관리 작업결과 건수', '월별로 인프라변경관리 작업결과(성공/실패)를 확인한다.', '{"range":{"type":"chart.range.between","from":"2021-01-01","to":"2021-12-31"},"operation":"count","periodUnit":"M","group":"","tags":["z-change-infra-result"]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd4a4c4570015', 'chart.stackedColumn', '월간 인프라변경관리 납기 준수율', '월별로 인프라변경관리 납기준수율 건수(성공/실패)를 확인한다.', '{"range":{"type":"chart.range.between","from":"2021-01-01","to":"2021-12-31"},"operation":"count","periodUnit":"M","group":"","tags":["z-change-infra-request-deadline","z-change-infra-complete-review-date"]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd4a70cef0016', 'chart.basicLine', '월간 어플리케이션변경관리 등록건수', '월별로 어플리케이션변경관리 등록 건수를 확인한다.', '{"range":{"type":"chart.range.between","from":"2021-01-01","to":"2021-12-31"},"operation":"count","periodUnit":"M","group":"어플리케이션변경관리등록건수","tags":["z-change-application"]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd4a7b2f10017', 'chart.basicLine', '월간 어플리케이션변경관리 완료건수', '월별로 어플리케이션변경관리 완료 건수를 확인한다.', '{"range":{"type":"chart.range.between","from":"2021-01-01","to":"2021-12-31"},"operation":"count","periodUnit":"M","group":"어플리케이션변경관리완료","tags":["z-change-application-complete-review-date"]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd4a8cc9b0019', 'chart.stackedColumn', '월간 어플리케이션변경관리 납기 준수율', '월별로 어플리케이션변경관리 납기준수율 건수(성공/실패)를 확인한다.', '{"range":{"type":"chart.range.between","from":"2021-01-01","to":"2021-12-31"},"operation":"count","periodUnit":"M","group":"","tags":["z-change-application-request-deadline","z-change-application-complete-review-date"]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd4a831ea0018', 'chart.stackedColumn', '월간 어플리케이션변경관리 작업결과 건수', '월별로 어플리케이션변경관리 작업결과(성공/실패)를 확인한다.', '{"range":{"type":"chart.range.between","from":"2021-01-01","to":"2021-12-31"},"operation":"count","periodUnit":"Y","group":"","tags":["z-change-application-result"]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);

INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd4a99f18001a', 'chart.basicLine', '월간 구성관리 등록건수', '월별로 구성관리 등록 건수를 확인한다.', '{"range":{"type":"chart.range.between","from":"2021-01-01","to":"2021-12-31"},"operation":"count","periodUnit":"M","group":"구성관리등록건수","tags":["z-change-configuration"]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd4aa4abf001b', 'chart.basicLine', '월간 구성관리 완료건수', '월별로 구성관리 완료 건수를 확인한다.', '{"range":{"type":"chart.range.between","from":"2021-01-01","to":"2021-12-31"},"operation":"count","periodUnit":"M","group":"구성관리완료","tags":["z-change-configuration-approve-date"]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd4ab4915001c', 'chart.stackedColumn', '월간 구성관리 납기 준수율', '월별로 구성관리 납기준수율 건수(성공/실패)를 확인한다.', '{"range":{"type":"chart.range.between","from":"2021-01-01","to":"2021-12-31"},"operation":"count","periodUnit":"M","group":"","tags":["z-change-configuration-request-deadline","z-change-configuration-approve-date"]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);