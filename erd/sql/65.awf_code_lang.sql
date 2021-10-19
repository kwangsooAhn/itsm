/**
 * 코드 다국어 정보
 */
DROP TABLE IF EXISTS awf_code_lang cascade;

CREATE TABLE awf_code_lang
(
    code varchar(100) NOT NULL,
    code_name varchar(128),
    lang varchar(100) NOT NULL,
    CONSTRAINT awf_code_lang_pk PRIMARY KEY (code, lang),
    CONSTRAINT awf_code_lang_fk FOREIGN KEY (code) REFERENCES awf_code (code)
);

COMMENT ON TABLE awf_code_lang IS '다국어 코드 정보';
COMMENT ON COLUMN awf_code_lang.code IS '코드';
COMMENT ON COLUMN awf_code_lang.code_name IS '코드 이름';
COMMENT ON COLUMN awf_code_lang.lang IS '언어';

insert into awf_code_lang values ('document.status.temporary', 'temporary', 'en');
insert into awf_code_lang values ('document.status.destroy', 'destroy', 'en');
insert into awf_code_lang values ('document.status.use', 'use', 'en');
insert into awf_code_lang values ('document.displayType.editable', 'Editable', 'en');
insert into awf_code_lang values ('document.displayType.readonly', 'Readonly', 'en');
insert into awf_code_lang values ('document.displayType.hidden', 'Hidden', 'en');
insert into awf_code_lang values ('document.group.incident', 'Disability Inquiry', 'en');
insert into awf_code_lang values ('document.group.inquiry', 'Simple Inquiry', 'en');
insert into awf_code_lang values ('document.group.request', 'Service Request', 'en');
insert into awf_code_lang values ('form.template', 'Template', 'en');
/* 단순문의 */
insert into awf_code_lang values ('form.template.serviceDesk.inquiry', 'ServiceDesk - Simple Inquiry', 'en');
/* 단순문의 - 서비스 항목*/
insert into awf_code_lang values ('form.template.serviceDesk.inquiry.category', 'Service Category', 'en');
insert into awf_code_lang values ('form.template.serviceDesk.inquiry.category.none', '- Choose Category -', 'en');
insert into awf_code_lang values ('form.template.serviceDesk.inquiry.category.server', 'Server', 'en');
insert into awf_code_lang values ('form.template.serviceDesk.inquiry.category.network', 'Network', 'en');
insert into awf_code_lang values ('form.template.serviceDesk.inquiry.category.security', 'Security', 'en');
insert into awf_code_lang values ('form.template.serviceDesk.inquiry.category.dbms', 'Database', 'en');
insert into awf_code_lang values ('form.template.serviceDesk.inquiry.category.contract', 'Contract', 'en');
insert into awf_code_lang values ('form.template.serviceDesk.inquiry.category.etc', 'ETC', 'en');
/* 차트 */
insert into awf_code_lang values ('chart.operation.percent', 'percent', 'en');
insert into awf_code_lang values ('chart.operation.count', 'count', 'en');
insert into awf_code_lang values ('chart.unit.year', 'year', 'en');
insert into awf_code_lang values ('chart.unit.month', 'month', 'en');
insert into awf_code_lang values ('chart.unit.hour', 'hour', 'en');
insert into awf_code_lang values ('chart.unit.day', 'day', 'en');
insert into awf_code_lang values ('chart.type.basicLine', 'Basic Line Chart', 'en');
insert into awf_code_lang values ('chart.type.pie', 'Pie Chart', 'en');
insert into awf_code_lang values ('chart.type.stackedColumn', 'Stacked Column Chart', 'en');
insert into awf_code_lang values ('chart.type.stackedBar', 'Stacked Bar Chart', 'en');
insert into awf_code_lang values ('chart.type.lineAndColumn', 'Line and Column Chart', 'en');
insert into awf_code_lang values ('chart.type.activityGauge', 'Activity Gauge Chart', 'en');
