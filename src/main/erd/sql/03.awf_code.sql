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
	editable boolean default false,
	level integer,
	seq_num integer,
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
COMMENT ON COLUMN awf_code.level IS '코드 레벨';
COMMENT ON COLUMN awf_code.seq_num IS '정렬 순서';
COMMENT ON COLUMN awf_code.create_user_key IS '등록자';
COMMENT ON COLUMN awf_code.create_dt IS '등록일';
COMMENT ON COLUMN awf_code.update_user_key IS '수정자';
COMMENT ON COLUMN awf_code.update_dt IS '수정일';

insert into awf_code values ('cmdb.db.kind', '', null, '데이터베이스', 'cmdb 데이터데이스 종류', TRUE, 1, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('cmdb.db.kind.oracle', 'cmdb.db.kind', 'oracle', 'Oracle', 'cmdb 데이터데이스 종류', TRUE, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('cmdb.db.kind.postgresql', 'cmdb.db.kind', 'postgresql', 'Postresql', '', TRUE, 2, 2,  '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('cmdb.db.kind.mssql', 'cmdb.db.kind', 'mssql','MSSQL', '', TRUE, 2, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('cmdb.db.kind.mysql', 'cmdb.db.kind', 'mysql','MYSQL', '', TRUE, 2, 4, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('cmdb.db.kind.tibero', 'cmdb.db.kind', 'tibero','TIBERO', '', TRUE, 2, 5, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('cmdb.db.kind.redis', 'cmdb.db.kind', 'redis','Redis', '', TRUE, 2, 6, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('cmdb.db.kind.rac', 'cmdb.db.kind', 'rac','RAC', '', TRUE, 2, 7, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('cmdb.db.kind.db2', 'cmdb.db.kind', 'db2','DB2', '', TRUE, 2, 8, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('cmdb.db.kind.altibase', 'cmdb.db.kind', 'altibase', 'Altibase', '', TRUE, 2, 9, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('cmdb.db.kind.sysbase', 'cmdb.db.kind', 'sysbase','SYBASE', '', TRUE, 2, 10, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('cmdb.db.kind.sysbaseiq', 'cmdb.db.kind', 'sysbaseiq', 'SYBASEIQ', '', TRUE, 2, 11, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('cmdb.db.kind.mongodb', 'cmdb.db.kind', 'mongodb', 'MONGODB', '', TRUE, 2, 12, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('cmdb.db.kind.ppas', 'cmdb.db.kind', 'ppas', 'PPAS', '', TRUE, 2, 13, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('cmdb.db.kind.cubrid', 'cmdb.db.kind', 'cubrid', 'CUBRID', '', TRUE, 2, 14, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('cmdb.db.kind.mariadb', 'cmdb.db.kind', 'mariadb', 'MARIADB', '', TRUE, 2, 15, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('cmdb.db.kind.informix', 'cmdb.db.kind', 'informix', 'INFORMIX', '', TRUE, 2, 16, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('root', null, null, 'ROOT', null, false, 0, 0, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('assignee', 'root', null, '담당자', null, false, 1, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('assignee.type', 'assignee', null, '담당자 타입', null, false, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('assignee.type.assignee', 'assignee.type', null, '지정 담당자', null, false, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('assignee.type.candidate.groups', 'assignee.type', null, '담당자 후보그룹', null, false, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('assignee.type.candidate.users', 'assignee.type', null, '담당자 후보목록', null, false, 3, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('document', 'root', null, '신청서', null, false, 1, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('document.group', 'document', null, '신청서 목록', null, true, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('servicedesk.incident', 'document.group', '장애문의', '장애문의', null, true, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('servicedesk.inquiry', 'document.group', '단순문의', '단순문의', null, true, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('servicedesk.request', 'document.group', '서비스요청', '서비스요청', null, true, 3, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('document.status', 'document', null, '신청서 상태', null, false, 2, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('document.status.temporary', 'document.status', '임시', '임시', null, false, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('document.status.destroy', 'document.status', '폐기', '폐기', null, false, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('document.status.use', 'document.status', '사용', '사용', null, false, 3, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('document.displayType', 'document', null, '신청서 프로세스별 그룹 출력 타입', null, false, 2, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('document.displayType.editable', 'document.displayType', '수정 가능', '수정 가능', null, false, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('document.displayType.readonly', 'document.displayType', '수정 불가', '수정 불가', null, false, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('document.displayType.hidden', 'document.displayType', '숨김', '숨김', null, false, 3, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('download', 'root', null, '자료실', null, true, 1, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('download.category', 'download', null, '자료실 카테고리', null, true, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('download.category.companyPolicy', 'download.category', null, '회사규정', null, true, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('download.category.etc', 'download.category', null, '기타', null, true, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('faq', 'root', null, 'FAQ', null, false, 1, 4, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('faq.category', 'faq', null, 'FAQ 카테고리', null, false, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('faq.category.etc', 'faq.category', null, '기타', null, false, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('faq.category.setting', 'faq.category', null, '설정', null, false, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('faq.category.techSupport', 'faq.category', null, '기술지원', null, false, 3, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('form', 'root', null, '문서양식', null, false, 1, 5, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('form.lang', 'form', null, '문서양식 언어', null, false, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('form.lang.ko', 'form.lang', null, '한국어', null, false, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('numbering', 'root', null, '문서번호 규칙 패턴', null, false, 1, 6, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('numbering.pattern', 'numbering', null, '문서규칙 패턴', null, false, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('numbering.pattern.format', 'numbering.pattern', null, '문서규칙 포맷', null, false, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('numbering.pattern.format.yyyyMMdd', 'numbering.pattern.format', 'yyyyMMdd', '날짜형패턴', null, false, 4, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('numbering.pattern.format.yyyyddMM', 'numbering.pattern.format', 'yyyyddMM', '날짜형패턴', null, false, 4, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('numbering.pattern.format.MMddyyyy', 'numbering.pattern.format', 'MMddyyyy', '날짜형패턴', null, false, 4, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('numbering.pattern.format.ddMMyyyy', 'numbering.pattern.format', 'ddMMyyyy', '날짜형패턴', null, false, 4, 4, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('reception_type', 'root', '접수유형', null, null, true, 1, 7, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('email', 'reception_type', '이메일', '이메일', null, true, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('post', 'reception_type', '우편', '우편', null, true, 2, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('telephone', 'reception_type', '전화', '전화', null, true, 2, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('scheduler', 'root', null, '스케줄러', null, false, 1, 13, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('scheduler.taskType', 'scheduler', null, '작업 유형', null, false, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('scheduler.taskType.class', 'scheduler.taskType', 'class', 'CLASS', null, false, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('scheduler.taskType.query', 'scheduler.taskType', 'query', 'QUERY', null, false, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('scheduler.taskType.jar', 'scheduler.taskType', 'jar', 'JAR', null, false, 3, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('scheduler.executeCycleType', 'scheduler', null, '실행 유형', null, false, 2, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('scheduler.executeCycleType.fixedDelay', 'scheduler.executeCycleType', 'fixedDelay', 'FIXED_DELAY', null, false, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('scheduler.executeCycleType.fixedRate', 'scheduler.executeCycleType', 'fixedRate', 'FIXED_RATE', null, false, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('scheduler.executeCycleType.cron', 'scheduler.executeCycleType', 'cron', 'CRON', null, false, 3, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('script', 'root', null, null, null, false, 1, 8, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('script.type', 'script', 'script.type', 'Script Type', null, false, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('script.type.cmdb', 'script.type', 'script.type.cmdb', '[CMDB] CI 반영', null, false, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('script.type.document.attachFile', 'script.type', 'script.type.document.attachFile', '[문서편집] 첨부파일', null, false, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('token', 'root', null, '토큰 관련 코드', null, false, 1, 9, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('token.status', 'token', null, '토큰 상태 코드', null, false, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('token.status.finish', 'token.status', null, '처리 완료', null, false, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('token.status.running', 'token.status', null, '진행 중', null, false, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('token.status.waiting', 'token.status', null, '대기 중', null, false, 3, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('user', 'root', null, '사용자', null, false,1,10, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.id', 'user', null, '아이디', null, false,2,1, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.name', 'user', null, '이름', null, false,2,2, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.department', 'user', null, '부서', null, false,2,3, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.position', 'user', null, '직책', null, false,2,4, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.extensionNumber', 'user', null, '내선번호', null, false,2,5, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.date', 'user', null, '사용자 날짜 포맷', null, false,2,6, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.date.yyyymmdd', 'user.date', 'yyyy-MM-dd', null, null, false,3,1, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.date.ddmmyyyy', 'user.date', 'dd-MM-yyyy', null, null, false,3,2, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.date.mmddyyyy', 'user.date', 'MM-dd-yyyy', null, null, false,3,3, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.date.yyyyddmm', 'user.date', 'yyyy-dd-MM', null, null, false,3,4, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.default', 'user', null, '기본 값', null, false,2,7, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.default.menu', 'user.default', null, '기본 메뉴', null, false,3,1, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.default.menu.dashboard', 'user.default.menu', 'dashboard', '개인 현황판 메뉴 아이디', null, true,4,1, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.default.role', 'user.default', null, '기본 역할', null, false,3,2, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.default.role.users.general', 'user.default.role', 'users.general', '역할 - 사용자 일반', null, false,4,1, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.default.url', 'user.default', null, '기본 URL', null, false,3,3, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.default.url.dashboard', 'user.default.url', '/dashboard/view', '개인 현황판 URL', null, true,4,1, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.lang', 'user', null, '언어', null, false,2,8, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.lang.ko', 'user.lang', 'ko', '한국어', null, false,3,1, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.lang.en', 'user.lang', 'en', '영어', null, false,3,2, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.platform', 'user', null, '플랫폼', null, false,2,9, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.platform.alice', 'user.platform', null, 'Alice', null, false,3,1, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.platform.google', 'user.platform', null, 'Google', null, false,3,2, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.platform.kakao', 'user.platform', null, 'Kakao', null, false,3,3, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.search', 'user', null, '검색 목록', null, false,2,10, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.search.department', 'user.search', null, '부서', null, false,3,1, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.search.extensionNumber', 'user.search', null, '내선번호', null, false,3,2, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.search.id', 'user.search', null, '아이디', null, false,3,3, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.search.mobileNumber', 'user.search', null, '핸드폰 번호', null, false,3,4, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.search.name', 'user.search', null, '이름', null, false,3,5, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.search.officeNumber', 'user.search', null, '사무실 번호', null, false,3,6, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.search.position', 'user.search', null, '직책', null, false,3,7, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.status', 'user', null, '계정 상태', null, false,2,11, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.status.certified', 'user.status', null, '인증 완료', null, false,3,1, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.status.signup', 'user.status', null, '가입', null, false,3,2, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.theme', 'user', null, '테마', null, false,2,12, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.theme.default', 'user.theme', 'default', '기본 테마', null, false,3,1, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.theme.dark', 'user.theme', 'dark', '어두운 테마', null, false,3,2, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('user.time', 'user', null, '사용자 시간 포맷', null, false,2,13, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('24', 'user.time', 'HH:mm', null, null, false,3,1, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('12', 'user.time', 'hh:mm a', null, null, false,3,2, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('department', 'root', null, '부서 관리', null, false,1,11, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('department.group', 'department', null, '부서 명', null, false,2,1, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('department.group.design', 'department.group', 'DESIGN', 'DESIGN', null, false,3,1, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('department.group.itsm', 'department.group', 'ITSM', 'ITSM', null, false,3,2, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('department.group.tc', 'department.group', 'TC', 'TC', null, false,3,3, '0509e09412534a6e98f04ca79abb6424', now(), null);
insert into awf_code values ('version', 'root', null, null, null, false, 1, 12, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('version.workflow', 'version', '20200515', null, null, false, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('customCode', 'root', null, '커스텀코드', null, false, 1, 13, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('customCode.type', 'customCode', null, '신청서 목록', null, true, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('customCode.type.table', 'customCode.type', 'table', '테이블', null, true, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('customCode.type.code', 'customCode.type', 'code', '코드', null, true, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('customCode.operator', 'customCode', null, '연산자', null, true, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('customCode.operator.equal', 'customCode.operator', 'equal', '=', null, true, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('customCode.operator.notEqual', 'customCode.operator', 'notEqual', '!=', null, true, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('chart', 'root', null, 'CHART', null, false, 1, 14, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('chart.type', 'chart', null, 'CHART TYPE', null, true, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('chart.type.basicLine', 'chart.type', 'chart.basicLine', 'Basic Line Chart', null, true, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('chart.type.pie', 'chart.type', 'chart.pie', 'Pie Chart', null, true, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('chart.type.stackedColumn', 'chart.type', 'chart.stackedColumn', 'Stacked Column Chart', null, true, 3, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('chart.operation', 'chart', null, 'CHART OPERATION', null, true, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('chart.operation.count', 'chart.operation', 'count', '카운트', null, true, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('chart.operation.percent', 'chart.operation', 'percent', '퍼센트', null, true, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('chart.unit', 'chart', null, 'CHART UNIT', null, true, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('chart.unit.year', 'chart.unit', 'Y', '년', null, true, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('chart.unit.month', 'chart.unit', 'M', '월', null, true, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('chart.unit.day', 'chart.unit', 'D', '일', null, true, 3, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('chart.unit.hour', 'chart.unit', 'H', '시간', null, true, 3, 3, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('instance', 'root', null, '인스턴스', null, true, 1, 15, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('instance.platform', 'instance', null, '인스턴스 플랫폼', null, false, 2, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('instance.platform.itsm', 'instance.platform', 'ITSM', 'ITSM', null, faLse, 3, 1, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_code values ('instance.platform.api', 'instance.platform', 'API', 'API', null, faLse, 3, 2, '0509e09412534a6e98f04ca79abb6424', now(), null, null);
