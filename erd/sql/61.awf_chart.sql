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

INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd479a9890005', 'chart.pie', '년간 만족도 평가건수', '월별로 단순문의, 장애관리, 변경관리, 구성관리의 만족도 평가 값의 건수를 확인 한다.', '{"range":{"type":"chart.range.between","from":"2020-12-31T15:00:00.000Z","to":"2021-12-31T14:59:00.000Z"},"operation":"count","periodUnit":"Y"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd46affee0001', 'chart.stackedColumn', '월간 유형별 완료건수', '월별로 서비스데스크 유형별 완료 건수를 확인 한다.', '{"range":{"type":"chart.range.between","from":"2020-12-31T15:00:00.000Z","to":"2021-12-31T14:59:00.000Z"},"operation":"count","periodUnit":"M"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd4838b2e0007', 'chart.stackedColumn', '월간 단순문의 만족도 평가', '월별로 단순문의 만족도 평가 값의 점수를 확인 한다.', '{"range":{"type":"chart.range.between","from":"2020-12-31T15:00:00.000Z","to":"2021-12-31T14:59:00.000Z"},"operation":"average","periodUnit":"M"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd48588b50008', 'chart.stackedColumn', '월간 장애관리 만족도 평가', '월별로 장애관리 만족도 평가 값의 점수를 확인 한다.', '{"range":{"type":"chart.range.between","from":"2020-12-31T15:00:00.000Z","to":"2021-12-31T14:59:00.000Z"},"operation":"average","periodUnit":"M"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd4863bfc0009', 'chart.stackedColumn', '월간 변경관리 만족도 평가', '월별로 변경관리 만족도 평가 값의 점수를 확인 한다.', '{"range":{"type":"chart.range.between","from":"2020-12-31T15:00:00.000Z","to":"2021-12-31T14:59:00.000Z"},"operation":"average","periodUnit":"M"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd47a4c0d0006', 'chart.stackedColumn', '월간 구성관리 만족도 평가', '월별로 구성관리 만족도 평가 값의 점수를 확인 한다.', '{"range":{"type":"chart.range.between","from":"2020-12-31T15:00:00.000Z","to":"2021-12-31T14:59:00.000Z"},"operation":"average","periodUnit":"M"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd49c25e2000c', 'chart.basicLine', '월간 장애 완료건수', '월별로 장애관리 완료 건수를 확인한다.', '{"range":{"type":"chart.range.between","from":"2020-12-31T15:00:00.000Z","to":"2021-12-31T14:59:00.000Z"},"operation":"count","periodUnit":"M"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd49fec730010', 'chart.basicLine', '월간 문제관리 완료건수', '월별로 문제관리 완료 건수를 확인한다.', '{"range":{"type":"chart.range.between","from":"2020-12-31T15:00:00.000Z","to":"2021-12-31T14:59:00.000Z"},"operation":"count","periodUnit":"M"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd4a3758c0013', 'chart.basicLine', '월간 인프라변경관리 완료건수', '월별로 인프라 변경관리 완료 건수를 확인한다.', '{"range":{"type":"chart.range.between","from":"2020-12-31T15:00:00.000Z","to":"2021-12-31T14:59:00.000Z"},"operation":"count","periodUnit":"M"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd4a7b2f10017', 'chart.basicLine', '월간 어플리케이션변경관리 완료건수', '월별로 어플리케이션 변경관리 완료 건수를 확인한다.', '{"range":{"type":"chart.range.between","from":"2020-12-31T15:00:00.000Z","to":"2021-12-31T14:59:00.000Z"},"operation":"count","periodUnit":"M"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd4aa4abf001b', 'chart.basicLine', '월간 구성관리 완료건수', '월별로 구성관리 완료 건수를 확인한다.', '{"range":{"type":"chart.range.between","from":"2020-12-31T15:00:00.000Z","to":"2021-12-31T14:59:00.000Z"},"operation":"count","periodUnit":"M"}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);

/* TODO: 값 연산 불가능 - 납기 준수율 = 어플리케이션 변경관리 완료희망일시 >= 어플리케이션 변경관리 완료일시 인 경우만 카운트 > 일감 등록 #11860 [사용자 정의 차트] 값을 기준으로 비교 연산 기능 추가
INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd47752aa0002', 'chart.stackedColumn', '월간 납기준수율 - 단순문의', '월별로 서비스데스크- 단순문의 납기준수율 건수(성공/실패)를 확인 한다.', '{"range":{"type":"chart.range.between","from":"2020-12-31T15:00:00.000Z","to":"2021-12-31T14:59:00.000Z"},"operation":"percent","periodUnit":"M","tags":[{"value":"단순문의 완료희망일시"},{"value":"단순문의 완료일시"}]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd4902183000a', 'chart.stackedColumn', '월간 납기준수율 - 장애관리', '월별로 서비스데스크- 장애관리 납기준수율 건수(성공/실패)를 확인 한다.', '{"range":{"type":"chart.range.between","from":"2020-12-31T15:00:00.000Z","to":"2021-12-31T14:59:00.000Z"},"operation":"percent","periodUnit":"M","tags":[{"value":"장애관리 완료희망일시"},{"value":"장애관리 완료일시"}]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd47827cb0003', 'chart.stackedColumn', '월간 납기준수율 - 변경관리', '월별로 서비스데스크- 변경관리 납기준수율 건수(성공/실패)를 확인 한다.', '{"range":{"type":"chart.range.between","from":"2020-12-31T15:00:00.000Z","to":"2021-12-31T14:59:00.000Z"},"operation":"percent","periodUnit":"M","tags":[{"value":"변경관리 완료희망일시"},{"value":"변경관리 완료일시"}]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd478d0a10004', 'chart.stackedColumn', '월간 납기준수율 - 구성관리', '월별로 서비스데스크- 구성관리 납기준수율 건수(성공/실패)를 확인 한다.', '{"range":{"type":"chart.range.between","from":"2020-12-31T15:00:00.000Z","to":"2021-12-31T14:59:00.000Z"},"operation":"percent","periodUnit":"M","tags":[{"value":"구성관리 완료희망일시"},{"value":"구성관리 완료일시"}]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd4ab4915001c', 'chart.stackedColumn', '월간 구성관리 납기 준수율', '월별로 구성관리 납기준수율 건수(성공/실패)를 확인한다.', '{"range":{"type":"chart.range.between","from":"2020-12-31T15:00:00.000Z","to":"2021-12-31T14:59:00.000Z"},"operation":"count","periodUnit":"M","tags":[{"value":"구성관리 완료희망일시"},{"value":"구성관리 완료일시"}]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd4a092020011', 'chart.stackedColumn', '월간 문제관리 납기 준수율', '월별로 문제관리 납기준수율 건수(성공/실패)를 확인한다.', '{"range":{"type":"chart.range.between","from":"2020-12-31T15:00:00.000Z","to":"2021-12-31T14:59:00.000Z"},"operation":"count","periodUnit":"M","tags":[{"value":"문제관리 완료희망일시"},{"value":"문제관리 완료일시"}]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd49e0d34000e', 'chart.stackedColumn', '월간 장애관리 납기 준수율', '월별로 장애관리 납기준수율 건수(성공/실패)를 확인한다.', '{"range":{"type":"chart.range.between","from":"2020-12-31T15:00:00.000Z","to":"2021-12-31T14:59:00.000Z"},"operation":"count","periodUnit":"M","tags":[{"value":"장애관리 완료희망일시"},{"value":"장애관리 완료일시"}]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd4a4c4570015', 'chart.stackedColumn', '월간 인프라변경관리 납기 준수율', '월별로 인프라변경관리 납기준수율 건수(성공/실패)를 확인한다.', '{"range":{"type":"chart.range.between","from":"2020-12-31T15:00:00.000Z","to":"2021-12-31T14:59:00.000Z"},"operation":"count","periodUnit":"M","tags":[{"value":"인프라 변경관리 완료희망일시"},{"value":"인프라 변경관리 완료일시"}]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd4a8cc9b0019', 'chart.stackedColumn', '월간 어플리케이션변경관리 납기 준수율', '월별로 어플리케이션변경관리 납기준수율 건수(성공/실패)를 확인한다.', '{"range":{"type":"chart.range.between","from":"2020-12-31T15:00:00.000Z","to":"2021-12-31T14:59:00.000Z"},"operation":"count","periodUnit":"M","tags":[{"value":"어플리케이션 변경관리 완료희망일시"},{"value":"어플리케이션 변경관리 완료일시"}]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd4a41aee0014', 'chart.stackedColumn', '월간 인프라변경관리 작업결과 건수', '월별로 인프라변경관리 작업결과(성공/실패)를 확인한다.', '{"range":{"type":"chart.range.between","from":"2020-12-31T15:00:00.000Z","to":"2021-12-31T14:59:00.000Z"},"operation":"count","periodUnit":"M","tags":[{"value":"인프라 변경관리 작업결과"}]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd4a831ea0018', 'chart.stackedColumn', '월간 어플리케이션변경관리 작업결과 건수', '월별로 어플리케이션변경관리 작업결과(성공/실패)를 확인한다.', '{"range":{"type":"chart.range.between","from":"2020-12-31T15:00:00.000Z","to":"2021-12-31T14:59:00.000Z"},"operation":"count","periodUnit":"Y","tags":[{"value":"어플리케이션 변경관리 작업결과"}]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd49d1ef3000d', 'chart.stackedColumn', '월간 장애등급별 건수', '월별로 장애등급(1,2,3,4 등급)별로  등록건수를 확인한다.', '{"range":{"type":"chart.range.between","from":"2020-12-31T15:00:00.000Z","to":"2021-12-31T14:59:00.000Z"},"operation":"count","periodUnit":"M","tags":[{"value":"장애등급"}]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
*/
/* TODO: 등록 건수 불가능 = 차트에서 조회되는 기준은 '완료된' 문서임 > 일감 등록 : #11859 [사용자 정의차트] 태그 조회 대상 설정
INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd4659dde0000', 'chart.stackedColumn', '월간 유형별 등록 건수', '월별로 서비스데스크 유형별 등록 건수를 확인 한다.', '{"range":{"type":"chart.range.between","from":"2020-12-31T15:00:00.000Z","to":"2021-12-31T14:59:00.000Z"},"operation":"count","periodUnit":"M","tags":[{"value":"단순문의"},{"value":"장애신고"},{"value":"서비스요청"},{"value":"CMDB 변경요청"}]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd4a99f18001a', 'chart.basicLine', '월간 구성관리 등록건수', '월별로 구성관리 등록 건수를 확인한다.', '{"range":{"type":"chart.range.between","from":"2020-12-31T15:00:00.000Z","to":"2021-12-31T14:59:00.000Z"},"operation":"count","periodUnit":"M","tags":[{"value":"구성관리"}]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd49f3a34000f', 'chart.basicLine', '월간 문제관리 등록건수', '월별로 문제관리 등록 건수를 확인한다.', '{"range":{"type":"chart.range.between","from":"2020-12-31T15:00:00.000Z","to":"2021-12-31T14:59:00.000Z"},"operation":"count","periodUnit":"M","tags":[{"value":"문제관리"}]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd49b24ea000b', 'chart.basicLine', '월간 장애 등록건수', '월별로 장애관리 등록 건수를 확인한다.', '{"range":{"type":"chart.range.between","from":"2020-12-31T15:00:00.000Z","to":"2021-12-31T14:59:00.000Z"},"operation":"count","periodUnit":"M","tags":[{"value":"장애관리"}]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd4a2b5c90012', 'chart.basicLine', '월간 인프라변경관리 등록건수', '월별로 인프라변경관리 등록 건수를 확인한다.', '{"range":{"type":"chart.range.between","from":"2020-12-31T15:00:00.000Z","to":"2021-12-31T14:59:00.000Z"},"operation":"count","periodUnit":"M","tags":[{"value":"인프라 변경관리"}]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
INSERT INTO awf_chart VALUES ('4028b8817cd4629c017cd4a70cef0016', 'chart.basicLine', '월간 어플리케이션변경관리 등록건수', '월별로 어플리케이션변경관리 등록 건수를 확인한다.', '{"range":{"type":"chart.range.between","from":"2020-12-31T15:00:00.000Z","to":"2021-12-31T14:59:00.000Z"},"operation":"count","periodUnit":"M","tags":[{"value":"어플리케이션 변경관리"}]}', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
*/