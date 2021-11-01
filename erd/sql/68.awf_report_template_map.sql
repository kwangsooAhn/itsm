/**
 * 보고서 설정 차트 매핑 테이블
 */
DROP TABLE IF EXISTS awf_report_template_map cascade;

create table awf_report_template_map (
    template_id varchar(128) NOT NULL,
    chart_id varchar(128) NOT NULL,
    display_order int,
    CONSTRAINT awf_report_template_map_pk PRIMARY KEY (template_id, chart_id)
);

COMMENT ON TABLE awf_report_template_map IS '보고서 템플릿 차트 정보';
COMMENT ON COLUMN awf_report_template_map.template_id IS '템플릿아이디';
COMMENT ON COLUMN awf_report_template_map.chart_id IS '차트아이디';

INSERT INTO awf_report_template_map VALUES ('4028b8817cd4629c017cd4c47c49001d', '4028b8817cd4629c017cd4659dde0000', 1);
INSERT INTO awf_report_template_map VALUES ('4028b8817cd4629c017cd4c47c49001d', '4028b8817cd4629c017cd46affee0001', 2);
INSERT INTO awf_report_template_map VALUES ('4028b8817cd4629c017cd4c47c49001d', '4028b8817cd4629c017cd47752aa0002', 3);
INSERT INTO awf_report_template_map VALUES ('4028b8817cd4629c017cd4c47c49001d', '4028b8817cd4629c017cd4902183000a', 4);
INSERT INTO awf_report_template_map VALUES ('4028b8817cd4629c017cd4c47c49001d', '4028b8817cd4629c017cd47827cb0003', 5);
INSERT INTO awf_report_template_map VALUES ('4028b8817cd4629c017cd4c47c49001d', '4028b8817cd4629c017cd478d0a10004', 6);
INSERT INTO awf_report_template_map VALUES ('4028b8817cd4629c017cd4c47c49001d', '4028b8817cd4629c017cd479a9890005', 7);
INSERT INTO awf_report_template_map VALUES ('4028b8817cd4629c017cd4c47c49001d', '4028b8817cd4629c017cd4838b2e0007', 8);
INSERT INTO awf_report_template_map VALUES ('4028b8817cd4629c017cd4c47c49001d', '4028b8817cd4629c017cd48588b50008', 9);
INSERT INTO awf_report_template_map VALUES ('4028b8817cd4629c017cd4c47c49001d', '4028b8817cd4629c017cd4863bfc0009', 10);
INSERT INTO awf_report_template_map VALUES ('4028b8817cd4629c017cd4c47c49001d', '4028b8817cd4629c017cd47a4c0d0006', 11);
INSERT INTO awf_report_template_map VALUES ('4028b8817cd4629c017cd4eced9e001e', '4028b8817cd4629c017cd49b24ea000b', 1);
INSERT INTO awf_report_template_map VALUES ('4028b8817cd4629c017cd4eced9e001e', '4028b8817cd4629c017cd49c25e2000c', 2);
INSERT INTO awf_report_template_map VALUES ('4028b8817cd4629c017cd4eced9e001e', '4028b8817cd4629c017cd49d1ef3000d', 3);
INSERT INTO awf_report_template_map VALUES ('4028b8817cd4629c017cd4eced9e001e', '4028b8817cd4629c017cd49e0d34000e', 4);
INSERT INTO awf_report_template_map VALUES ('4028b8817cd4629c017cd4f0c3150020', '4028b8817cd4629c017cd49f3a34000f', 1);
INSERT INTO awf_report_template_map VALUES ('4028b8817cd4629c017cd4f0c3150020', '4028b8817cd4629c017cd49fec730010', 2);
INSERT INTO awf_report_template_map VALUES ('4028b8817cd4629c017cd4f0c3150020', '4028b8817cd4629c017cd4a092020011', 3);
INSERT INTO awf_report_template_map VALUES ('4028b8817cd4629c017cd4f1df090021', '4028b8817cd4629c017cd4a2b5c90012', 1);
INSERT INTO awf_report_template_map VALUES ('4028b8817cd4629c017cd4f1df090021', '4028b8817cd4629c017cd4a3758c0013', 2);
INSERT INTO awf_report_template_map VALUES ('4028b8817cd4629c017cd4f1df090021', '4028b8817cd4629c017cd4a41aee0014', 3);
INSERT INTO awf_report_template_map VALUES ('4028b8817cd4629c017cd4f1df090021', '4028b8817cd4629c017cd4a4c4570015', 4);
INSERT INTO awf_report_template_map VALUES ('4028b8817cd4629c017cd4f485300022', '4028b8817cd4629c017cd4a99f18001a', 1);
INSERT INTO awf_report_template_map VALUES ('4028b8817cd4629c017cd4f485300022', '4028b8817cd4629c017cd4aa4abf001b', 2);
INSERT INTO awf_report_template_map VALUES ('4028b8817cd4629c017cd4f485300022', '4028b8817cd4629c017cd4ab4915001c', 3);
INSERT INTO awf_report_template_map VALUES ('4028b8817cd4629c017cd50244af00a4', '4028b8817cd4629c017cd4a70cef0016', 1);
INSERT INTO awf_report_template_map VALUES ('4028b8817cd4629c017cd50244af00a4', '4028b8817cd4629c017cd4a7b2f10017', 2);
INSERT INTO awf_report_template_map VALUES ('4028b8817cd4629c017cd50244af00a4', '4028b8817cd4629c017cd4a831ea0018', 3);
INSERT INTO awf_report_template_map VALUES ('4028b8817cd4629c017cd50244af00a4', '4028b8817cd4629c017cd4a8cc9b0019', 4);