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
insert into awf_code_lang values ('form.template.serviceDesk.inquiry.category.etc', 'Etc', 'en');
/* 장애신고 */
insert into awf_code_lang values ('form.template.serviceDesk.incident', 'ServiceDesk - Incident', 'en');
/* 장애신고 - 장애 유형 */
insert into awf_code_lang values ('form.template.serviceDesk.incident.category', 'Incident Category', 'en');
insert into awf_code_lang values ('form.template.serviceDesk.incident.category.none', '- Choose Category -', 'en');
insert into awf_code_lang values ('form.template.serviceDesk.incident.category.system', 'System Error', 'en');
insert into awf_code_lang values ('form.template.serviceDesk.incident.category.infrastructure', 'Infrastructure error', 'en');
insert into awf_code_lang values ('form.template.serviceDesk.incident.category.technology', 'Technology error', 'en');
insert into awf_code_lang values ('form.template.serviceDesk.incident.category.operation', 'Operation error', 'en');
insert into awf_code_lang values ('form.template.serviceDesk.incident.category.human', 'Human error', 'en');
/* 서비스요청 */
insert into awf_code_lang values ('form.template.serviceDesk.request', 'ServiceDesk - Service Request', 'en');
/* 서비스요청 - 요청 구분 */
insert into awf_code_lang values ('form.template.serviceDesk.request.category', 'Service Request Category', 'en');
insert into awf_code_lang values ('form.template.serviceDesk.request.category.service', 'Service Request', 'en');
insert into awf_code_lang values ('form.template.serviceDesk.request.category.security', 'Security Request', 'en');
insert into awf_code_lang values ('form.template.serviceDesk.request.category.development', 'Development Request', 'en');
insert into awf_code_lang values ('form.template.serviceDesk.request.category.material', 'Material Request', 'en');
insert into awf_code_lang values ('form.template.serviceDesk.request.category.work', 'Work Request', 'en');
insert into awf_code_lang values ('form.template.serviceDesk.request.category.etc', 'Etc', 'en');
/* 차트 */
insert into awf_code_lang values ('chart.operation.percent', 'percent', 'en');
insert into awf_code_lang values ('chart.operation.count', 'count', 'en');
insert into awf_code_lang values ('chart.operation.average', 'average', 'en');
insert into awf_code_lang values ('chart.unit.year', 'year', 'en');
insert into awf_code_lang values ('chart.unit.month', 'month', 'en');
insert into awf_code_lang values ('chart.unit.hour', 'hour', 'en');
insert into awf_code_lang values ('chart.unit.day', 'day', 'en');
insert into awf_code_lang values ('chart.type.basicLine', 'Basic Line Chart', 'en');
insert into awf_code_lang values ('chart.type.pie', 'Pie Chart', 'en');
insert into awf_code_lang values ('chart.type.stackedColumn', 'Stacked Column Chart', 'en');
insert into awf_code_lang values ('chart.type.stackedBar', 'Stacked Bar Chart', 'en');
insert into awf_code_lang values ('chart.type.gauge', 'Gauge Chart', 'en');
/* 자료실 */
insert into awf_code_lang values ('download.category.companyPolicy', 'Company Policy', 'en');
insert into awf_code_lang values ('download.category.etc', 'Etc', 'en');
/* FAQ */
insert into awf_code_lang values ('faq.category.etc', 'Etc', 'en');
insert into awf_code_lang values ('faq.category.setting', 'Setting', 'en');
insert into awf_code_lang values ('faq.category.techSupport', 'Tech support', 'en');
/* 커스텀 코드 */
insert into awf_code_lang values ('customCode.sessionKey.userKey', 'User Key', 'en');
insert into awf_code_lang values ('customCode.sessionKey.userId', 'User ID', 'en');
insert into awf_code_lang values ('customCode.sessionKey.userName', 'Username', 'en');
insert into awf_code_lang values ('customCode.sessionKey.email', 'Email', 'en');
insert into awf_code_lang values ('customCode.sessionKey.position', 'Position', 'en');
insert into awf_code_lang values ('customCode.sessionKey.departmentName', 'Department', 'en');
insert into awf_code_lang values ('customCode.sessionKey.officeNumber', 'Office Number', 'en');
insert into awf_code_lang values ('customCode.sessionKey.mobileNumber', 'Mobile', 'en');
