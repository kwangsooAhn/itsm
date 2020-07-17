/**
 * 코드정보
 */
DROP TABLE IF EXISTS awf_code cascade;

CREATE TABLE awf_code
(
	code varchar(100) NOT NULL,
	p_code varchar(100),
	code_value varchar(256),
	editable boolean,
	create_user_key varchar(128) NOT NULL,
	create_dt timestamp NOT NULL,
	update_user_key varchar(128),
	update_dt timestamp,
	CONSTRAINT awf_code_pk PRIMARY KEY (code)
);

COMMENT ON TABLE awf_code IS '코드정보';
COMMENT ON COLUMN awf_code.code IS '코드';
COMMENT ON COLUMN awf_code.p_code IS '부모코드';
COMMENT ON COLUMN awf_code.code_value IS '코드 값';
COMMENT ON COLUMN awf_code.editable IS '수정가능여부';
COMMENT ON COLUMN awf_code.create_user_key IS '등록자';
COMMENT ON COLUMN awf_code.create_dt IS '등록일';
COMMENT ON COLUMN awf_code.update_user_key IS '수정자';
COMMENT ON COLUMN awf_code.update_dt IS '수정일';

insert into awf_code values ('12', 'user.time', 'hh:mm a', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('24', 'user.time', 'HH:mm', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('assignee.type', 'assignee', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('assignee.type.assignee', 'assignee.type', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('assignee.type.candidate.groups', 'assignee.type', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('assignee.type.candidate.users', 'assignee.type', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('document', '', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('document.group', 'document', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('document.status', 'document', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('document.status.destroy', 'document.status', '폐기', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('document.status.temporary', 'document.status', '임시', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('document.status.use', 'document.status', '사용', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('download.category', 'download', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('download.category.companyPolicy', 'download.category', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('download.category.etc', 'download.category', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('faq.category', 'faq', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('faq.category.etc', 'faq.category', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('faq.category.setting', 'faq.category', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('faq.category.techSupport', 'faq.category', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('form.lang', 'form', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('form.lang.ko', 'form.lang', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('numbering.pattern', 'numbering', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('numbering.pattern.format', 'numbering.pattern', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('numbering.pattern.format.yyyyMMdd', 'numbering.pattern.format', 'yyyyMMdd', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('token', 'NULL', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('token.status', 'token', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('token.status.cancel', 'token.status', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('token.status.finish', 'token.status', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('token.status.reject', 'token.status', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('token.status.running', 'token.status', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('token.status.terminate', 'token.status', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('token.status.waiting', 'token.status', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('token.status.withdraw', 'token.status', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user', '', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.date', 'user', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.date.ddmmyyyy', 'user.date', 'dd-MM-yyyy', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.date.mmddyyyy', 'user.date', 'MM-dd-yyyy', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.date.yyyyddmm', 'user.date', 'yyyy-dd-MM', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.date.yyyymmdd', 'user.date', 'yyyy-MM-dd', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.default', 'user', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.default.role', 'user.default', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.default.role.faq', 'user.default.role', 'faq.all', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.default.role.user', 'user.default.role', 'users.manager', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.department', 'user', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.extensionNumber', 'user', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.id', 'user', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.lang', 'user', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.lang.en', 'user.lang', 'en', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.lang.ko', 'user.lang', 'ko', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.name', 'user', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.platform', 'user', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.platform.alice', 'user.platform', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.platform.google', 'user.platform', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.platform.kakao', 'user.platform', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.position', 'user', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.search', 'user', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.search.department', 'user.search', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.search.extensionNumber', 'user.search', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.search.id', 'user.search', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.search.mobileNumber', 'user.search', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.search.name', 'user.search', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.search.officeNumber', 'user.search', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.search.position', 'user.search', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.status', 'user', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.status.certified', 'user.status', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.status.signup', 'user.status', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.theme', 'user', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.theme.dark', 'user.theme', 'dark', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.theme.default', 'user.theme', 'default', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.time', 'user', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('version', '', '', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('version.workflow', 'version', '20200515', false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);

