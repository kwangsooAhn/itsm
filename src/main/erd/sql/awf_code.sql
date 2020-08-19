/**
 * 코드정보
 */
DROP TABLE IF EXISTS awf_code cascade;

CREATE TABLE awf_code
(
	code varchar(100) NOT NULL,
	p_code varchar(100),
	code_value varchar(256),
	code_name varchar(128),
	code_desc varchar(512),
	editable boolean,
	create_user_key varchar(128),
	create_dt timestamp,
	update_user_key varchar(128),
	update_dt timestamp,
	CONSTRAINT awf_code_pk PRIMARY KEY (code)
);

COMMENT ON TABLE awf_code IS '코드정보';
COMMENT ON COLUMN awf_code.code IS '코드';
COMMENT ON COLUMN awf_code.p_code IS '부모코드';
COMMENT ON COLUMN awf_code.code_value IS '코드 값';
COMMENT ON COLUMN awf_code.code_name IS '코드 명';
COMMENT ON COLUMN awf_code.code_desc IS '코드 설명';
COMMENT ON COLUMN awf_code.editable IS '수정가능여부';
COMMENT ON COLUMN awf_code.create_user_key IS '등록자';
COMMENT ON COLUMN awf_code.create_dt IS '등록일';
COMMENT ON COLUMN awf_code.update_user_key IS '수정자';
COMMENT ON COLUMN awf_code.update_dt IS '수정일';

insert into awf_code values ('12', 'user.time', 'hh:mm a', null, null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('24', 'user.time', 'HH:mm', null, null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('assignee', null, null, '담당자', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('assignee.type', 'assignee', null, '담당자 타입', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('assignee.type.assignee', 'assignee.type', null, '지정 담당자', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('assignee.type.candidate.groups', 'assignee.type', null, '담당자 후보그룹', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('assignee.type.candidate.users', 'assignee.type', null, '담당자 후보목록', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('department', 'user', null, '부서 관리', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('department.group', 'department', null, '부서 명', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('department.group.design', 'department.group', 'DESIGN', 'DESIGN', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('department.group.itsm', 'department.group', 'ITSM', 'ITSM', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('department.group.tc', 'department.group', 'TC', 'TC', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('document', null, null, '신청서', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('document.group', 'document', null, '신청서 목록', null, true, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('document.status', 'document', null, '신청서 상태', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('document.status.destroy', 'document.status', '폐기', '폐기', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('document.status.temporary', 'document.status', '임시', '임시', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('document.status.use', 'document.status', '사용', '사용', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('download', null, null, '자료실', null, true, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('download.category', 'download', null, '자료실 카테고리', null, true, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('download.category.companyPolicy', 'download.category', null, '회사규정', null, true, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('download.category.etc', 'download.category', null, '기타', null, true, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('email', 'reception_type', '이메일', '이메일', null, true, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('faq', null, null, 'FAQ', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('faq.category', 'faq', null, 'FAQ 카테고리', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('faq.category.etc', 'faq.category', null, '기타', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('faq.category.setting', 'faq.category', null, '설정', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('faq.category.techSupport', 'faq.category', null, '기술지원', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('form', null, null, '문서양식', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('form.lang', 'form', null, '문서양식 언어', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('form.lang.ko', 'form.lang', null, '한국어', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('numbering', null, null, '문서번호 규칙 패턴', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('numbering.pattern', 'numbering', null, '문서규칙 패턴', null, false,'0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('numbering.pattern.format', 'numbering.pattern', null, '문서규칙 포맷', null, false,'0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('numbering.pattern.format.yyyyMMdd', 'numbering.pattern.format', 'yyyyMMdd', '날짜형패턴', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('post', 'reception_type', '우편', '우편', null, true, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('reception_type', null, '접수유형', null, null, true, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('servicedesk.incident', 'document.group', '장애문의', '장애문의', null, true '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('servicedesk.inquiry', 'document.group', '단순문의', '단순문의', null, true '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('servicedesk.request', 'document.group', '서비스요청', '서비스요청', null, true '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('telephone', 'reception_type', '전화', '전화', null, true, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('token', null, null, '토큰 관련 코드', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('token.status', 'token', null, '토큰 상태 코드', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('token.status.cancel', 'token.status', null, '취소', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('token.status.finish', 'token.status', null, '처리 완료', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('token.status.reject', 'token.status', null, '반려', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('token.status.running', 'token.status', null, '진행 중', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('token.status.terminate', 'token.status', null, '종료', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('token.status.waiting', 'token.status', null, '대기 중', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('token.status.withdraw', 'token.status', null, '회수', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('top', null, null, null, null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user', 'top', null, '사용자', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.date', 'user', null, '사용자 날짜 포맷', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.date.ddmmyyyy', 'user.date', 'dd-MM-yyyy', null, null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.date.mmddyyyy', 'user.date', 'MM-dd-yyyy', null, null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.date.yyyyddmm', 'user.date', 'yyyy-dd-MM', null, null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.date.yyyymmdd', 'user.date', 'yyyy-MM-dd', null, null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.default', 'user', null, '기본 값', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.default.role', 'user.default', null, '기본 역할', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.default.role.faq', 'user.default.role', 'faq.all', '역할 - FAQ 전체 권한', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.default.role.user', 'user.default.role', 'users.manager', '역할 - 관리자', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.department', 'user', null, '부서', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.extensionNumber', 'user', null, '내선번호', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.id', 'user', null, '아이디', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.lang', 'user', null, '언어', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.lang.en', 'user.lang', 'en', '영어', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.lang.ko', 'user.lang', 'ko', '한국어', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.name', 'user', null, '이름', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.platform', 'user', null, '플랫폼', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.platform.alice', 'user.platform', null, 'Alice', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.platform.google', 'user.platform', null, 'Google', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.platform.kakao', 'user.platform', null, 'Kakao', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.position', 'user', null, '직책', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.search', 'user', null, '검색 목록', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.search.department', 'user.search', null, '부서', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.search.extensionNumber', 'user.search', null, '내선번호', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.search.id', 'user.search', null, '아이디', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.search.mobileNumber', 'user.search', null, '핸드폰 번호', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.search.name', 'user.search', null, '이름', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.search.officeNumber', 'user.search', null, '사무실 번호', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.search.position', 'user.search', null, '직책', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.status', 'user', null, '계정 상태', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.status.certified', 'user.status', null, '인증 완료', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.status.signup', 'user.status', null, '가입', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.theme', 'user', null, '테마', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.theme.dark', 'user.theme', 'dark', '어두운 테마', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.theme.default', 'user.theme', 'default', '기본 테마', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user.time', 'user', null, '사용자 시간 포맷', null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('version', null, null, null, null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('version.workflow', 'version', '20200515', null, null, false, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
