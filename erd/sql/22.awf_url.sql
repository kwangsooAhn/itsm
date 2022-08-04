/**
 * URL별메소드명
 */
DROP TABLE IF EXISTS awf_url cascade;

CREATE TABLE awf_url
(
	url varchar(512) NOT NULL,
	method varchar(16) NOT NULL,
	url_desc varchar(256),
	is_required_auth boolean DEFAULT 'TRUE',
	CONSTRAINT awf_url_pk PRIMARY KEY (url, method)
);

COMMENT ON TABLE awf_url IS 'URL별메소드명';
COMMENT ON COLUMN awf_url.url IS '요청url';
COMMENT ON COLUMN awf_url.method IS 'method';
COMMENT ON COLUMN awf_url.url_desc IS '설명';
COMMENT ON COLUMN awf_url.is_required_auth IS '권한 필수여부';

insert into awf_url values ('/boards', 'get', '게시판 관리 리스트 호출화면', 'TRUE');
insert into awf_url values ('/boards/new', 'get', '게시판 관리 신규 등록', 'TRUE');
insert into awf_url values ('/boards/search', 'get', '게시판 관리 리스트 조회 화면', 'TRUE');
insert into awf_url values ('/boards/{id}/edit', 'get', '게시판 관리 상세 편집', 'TRUE');
insert into awf_url values ('/boards/{id}/view', 'get', '게시판 관리 상세 조회 화면', 'TRUE');
insert into awf_url values ('/boards/articles', 'get', '게시판 리스트 호출 화면', 'TRUE');
insert into awf_url values ('/boards/articles/search', 'get', '게시판 리스트 화면', 'TRUE');
insert into awf_url values ('/boards/articles/search/param', 'get', '게시판 리스트 화면', 'TRUE');
insert into awf_url values ('/boards/articles/{id}/comments', 'get', '게시판 댓글 조회', 'TRUE');
insert into awf_url values ('/boards/articles/{id}/edit', 'get', '게시판 편집', 'TRUE');
insert into awf_url values ('/boards/articles/{id}/new', 'get', '게시판 신규 등록', 'TRUE');
insert into awf_url values ('/boards/articles/{id}/reply/edit', 'get', '게시판 답글 편집', 'TRUE');
insert into awf_url values ('/boards/articles/{id}/view', 'get', '게시판 상세 조회 화면', 'TRUE');
insert into awf_url values ('/certification/certifiedmail', 'get', '메일 발송', 'FALSE');
insert into awf_url values ('/certification/fileupload', 'post', '회원가입 아바타 이미지 업로드', 'FALSE');
insert into awf_url values ('/certification', 'post', '회원 가입 요청(인증 메일 발송 포함)', 'FALSE');
insert into awf_url values ('/certification/signup', 'get', '회원 가입 화면 호출', 'FALSE');
insert into awf_url values ('/certification/status', 'get', '메일 인증 상태/재발송 요청 화면', 'FALSE');
insert into awf_url values ('/certification/valid', 'get', '메일 인증', 'FALSE');
insert into awf_url values ('/cmdb/attributes', 'get', 'CMDB Attribute 관리 목록', 'TRUE');
insert into awf_url values ('/cmdb/attributes/new', 'get', 'CMDB Attribute 등록 화면', 'TRUE');
insert into awf_url values ('/cmdb/attributes/search', 'get', 'CMDB Attribute 관리 조회 화면', 'TRUE');
insert into awf_url values ('/cmdb/attributes/{id}/edit', 'get', 'CMDB Attribute 수정 화면', 'TRUE');
insert into awf_url values ('/cmdb/attributes/{id}/view', 'get', 'CMDB Attribute 보기 화면', 'TRUE');
insert into awf_url values ('/cmdb/attributes/list-modal', 'get', 'CMDB Attribute 목록 모달 화면', 'TRUE');
insert into awf_url values ('/cmdb/class/edit', 'get', 'CMDB Class 편집 화면', 'TRUE');
insert into awf_url values ('/cmdb/class/view-pop/attributes', 'get', 'CMDB Class Attribute 모달 리스트 화면', 'TRUE');
insert into awf_url values ('/cmdb/types/edit', 'get', 'CMDB Type 관리 화면', 'TRUE');
insert into awf_url values ('/cmdb/cis', 'post', 'CMDB CI 조회 목록', 'TRUE');
insert into awf_url values ('/cmdb/cis/search', 'get', 'CMDB CI 조회 목록 화면', 'TRUE');
insert into awf_url values ('/cmdb/cis/component/new', 'get', 'CMDB CI 등록 화면', 'FALSE');
insert into awf_url values ('/cmdb/cis/component/edit', 'post', 'CMDB CI 수정 화면', 'FALSE');
insert into awf_url values ('/cmdb/cis/component/view', 'get', 'CMDB CI 보기 화면', 'FALSE');
insert into awf_url values ('/cmdb/cis/{id}/view', 'get', 'CMDB CI 보기 화면', 'TRUE');
insert into awf_url values ('/cmdb/cis/component/list', 'get', 'CMDB CI 리스트 조회 팝업 화면', 'FALSE');
insert into awf_url values ('/codes/edit', 'get', '코드 편집 화면', 'TRUE');
insert into awf_url values ('/custom-codes', 'get', '사용자 정의 코드 리스트 화면', 'TRUE');
insert into awf_url values ('/custom-codes/new', 'get', '사용자 정의 코드 신규 등록 화면', 'TRUE');
insert into awf_url values ('/custom-codes/search', 'get', '사용자 정의 코드 리스트 호출 화면', 'TRUE');
insert into awf_url values ('/custom-codes/{id}/edit', 'get', '사용자 정의 코드 수정 화면', 'TRUE');
insert into awf_url values ('/custom-codes/{id}/view', 'get', '사용자 정의 코드 상세 정보 화면', 'TRUE');
insert into awf_url values ('/dashboard/view', 'get', '대시보드 상세 정보 화면', 'FALSE');
insert into awf_url values ('/workflows', 'get', '업무흐름 리스트 화면', 'TRUE');
insert into awf_url values ('/workflows/new', 'get', '신청서 생성 화면', 'TRUE');
insert into awf_url values ('/workflows/search', 'get', '업무흐름 데이터 + 목록화면', 'TRUE');
insert into awf_url values ('/workflows/{id}/edit', 'get', '신청서 수정 화면', 'TRUE');
insert into awf_url values ('/workflows/{id}/display', 'get', '신청서 디스플레이 데이터 조회', 'TRUE');
insert into awf_url values ('/workflows/import', 'get', '업무흐름 import 화면', 'TRUE');
insert into awf_url values ('/documents', 'get', '신청서 리스트 화면', 'FALSE');
insert into awf_url values ('/documents/search', 'get', '신청서 리스트 호출 화면', 'FALSE');
insert into awf_url values ('/documents/{id}/edit', 'get', '신청서 조회', 'TRUE');
insert into awf_url values ('/documents/{id}/print', 'get', '신청서 프린트 화면', 'TRUE');
insert into awf_url values ('/archives', 'get', '자료실 리스트 화면', 'TRUE');
insert into awf_url values ('/archives/new', 'get', '자료실 신규 등록', 'TRUE');
insert into awf_url values ('/archives/search', 'get', '자료실 리스트 호출 화면', 'TRUE');
insert into awf_url values ('/archives/{id}/edit', 'get', '자료실 편집', 'TRUE');
insert into awf_url values ('/archives/{id}/view', 'get', '자료실 상세 조회 화면', 'TRUE');
insert into awf_url values ('/faqs', 'get', 'FAQ 목록 조회', 'TRUE');
insert into awf_url values ('/faqs/new', 'get', 'FAQ 등록', 'TRUE');
insert into awf_url values ('/faqs/search', 'get', 'FAQ 검색 화면 호출', 'TRUE');
insert into awf_url values ('/faqs/{id}/edit', 'get', 'FAQ 수정', 'TRUE');
insert into awf_url values ('/faqs/{id}/view', 'get', 'FAQ 보기', 'TRUE');
insert into awf_url values ('/forms/{id}/edit', 'get', '폼 디자이너 편집화면', 'TRUE');
insert into awf_url values ('/forms/{id}/view', 'get', '폼 디자이너 상세화면', 'TRUE');
insert into awf_url values ('/forms/{id}/preview', 'get', '폼 디자이너 미리보기 화면', 'TRUE');
insert into awf_url values ('/forms', 'get', '폼 리스트 화면', 'TRUE');
insert into awf_url values ('/forms/search', 'get', '폼 리스트 검색 호출 화면', 'TRUE');
insert into awf_url values ('/files', 'get', '파일 관리 화면', 'TRUE');
insert into awf_url values ('/notices', 'get', '공지사항 목록', 'TRUE');
insert into awf_url values ('/notices/new', 'get', '공지사항 신규 등록 화면', 'TRUE');
insert into awf_url values ('/notices/search', 'get', '공지사항 검색 화면 호출 처리', 'TRUE');
insert into awf_url values ('/notices/{id}/edit', 'get', '공지사항 편집 화면', 'TRUE');
insert into awf_url values ('/notices/{id}/view', 'get', '공지사항 상세 화면', 'TRUE');
insert into awf_url values ('/notices/{id}/view-pop', 'get', '공지사항 팝업 화면', 'FALSE');
insert into awf_url values ('/notifications', 'get', '알림 리스트 화면', 'FALSE');
insert into awf_url values ('/numberingPatterns', 'get', '패턴 관리 목록 뷰', 'TRUE');
insert into awf_url values ('/numberingPatterns/search', 'get', '패턴 검색화면', 'TRUE');
insert into awf_url values ('/numberingPatterns/new', 'get', '패턴 등록', 'TRUE');
insert into awf_url values ('/numberingPatterns/{id}/edit', 'get', '패턴 수정', 'TRUE');
insert into awf_url values ('/numberingPatterns/{id}/view', 'get', '패턴 상세 보기', 'TRUE');
insert into awf_url values ('/numberingRules', 'get', '문서번호 관리 목록 뷰', 'TRUE');
insert into awf_url values ('/numberingRules/search', 'get', '문서번호 검색화면', 'TRUE');
insert into awf_url values ('/numberingRules/new', 'get', '문서번호 등록', 'TRUE');
insert into awf_url values ('/numberingRules/{id}/edit', 'get', '문서번호 수정', 'TRUE');
insert into awf_url values ('/numberingRules/{id}/view', 'get', '문서번호 상세 보기', 'TRUE');
insert into awf_url values ('/oauth/{service}/callback', 'get', 'OAuth 로그인 응답 콜백', 'TRUE');
insert into awf_url values ('/oauth/{service}/login', 'get', 'OAuth 로그인 화면 호출', 'TRUE');
insert into awf_url values ('/organizations/edit', 'get', '조직 관리 편집 화면', 'TRUE');
insert into awf_url values ('/portals', 'get', '포탈 조회', 'FALSE');
insert into awf_url values ('/portals/browserguide', 'get', '포탈 브라우저 안내', 'FALSE');
insert into awf_url values ('/portals/archives', 'get', '포달 자료실 리스트', 'FALSE');
insert into awf_url values ('/portals/archives/{archiveId}/view', 'get', '포탈 자료실 상세조회', 'FALSE');
insert into awf_url values ('/portals/archives/search', 'get', '포탈 자료실 조회', 'FALSE');
insert into awf_url values ('/portals/faqs', 'get', '포탈 FAQ 상세조회', 'FALSE');
insert into awf_url values ('/portals/faqs/{faqId}/view', 'get', '포탈 FAQ 리스트', 'FALSE');
insert into awf_url values ('/portals/main', 'get', '포탈', 'FALSE');
insert into awf_url values ('/portals/notices', 'get', '포탈 공지사항 리스트', 'FALSE');
insert into awf_url values ('/portals/notices/{noticeId}/view', 'get', '포탈 공지사항 상세 조회', 'FALSE');
insert into awf_url values ('/portals/notices/search', 'get', '포탈 공지사항 조회', 'FALSE');
insert into awf_url values ('/processes', 'get', '프로세스 목록', 'TRUE');
insert into awf_url values ('/processes/search', 'get', '프로세스 리스트 검색 호출 화면', 'TRUE');
insert into awf_url values ('/process/{id}/edit', 'get', '프로세스 디자이너 편집 화면' ,'TRUE');
insert into awf_url values ('/process/{id}/view', 'get', '프로세스 디자이너 보기 화면' ,'TRUE');
insert into awf_url values ('/rest/auths', 'get', '권한 전체 목록 조회', 'TRUE');
insert into awf_url values ('/rest/auths', 'post', '권한 등록', 'TRUE');
insert into awf_url values ('/rest/auths/{id}', 'get', '권한 상세 정보 조회', 'TRUE');
insert into awf_url values ('/rest/auths/{id}', 'put', '권한 수정', 'TRUE');
insert into awf_url values ('/rest/auths/{id}', 'delete', '권한 삭제', 'TRUE');
insert into awf_url values ('/rest/boards', 'post', '게시판 관리 등록', 'TRUE');
insert into awf_url values ('/rest/boards', 'put', '게시판 관리 변경', 'TRUE');
insert into awf_url values ('/rest/boards/{id}/view', 'get', '게시판 관리 상세정보', 'TRUE');
insert into awf_url values ('/rest/boards/{id}', 'delete', '게시판 관리 삭제', 'TRUE');
insert into awf_url values ('/rest/boards/articles', 'post', '게시판 등록', 'TRUE');
insert into awf_url values ('/rest/boards/articles', 'put', '게시판 변경', 'TRUE');
insert into awf_url values ('/rest/boards/articles/comments', 'put', '게시판 댓글 수정', 'TRUE');
insert into awf_url values ('/rest/boards/articles/comments', 'post', '게시판 댓글 등록', 'TRUE');
insert into awf_url values ('/rest/boards/articles/comments/{id}', 'delete', '게시판 댓글 삭제', 'TRUE');
insert into awf_url values ('/rest/boards/articles/reply', 'post', '게시판 답글 등록', 'TRUE');
insert into awf_url values ('/rest/boards/articles/{id}', 'delete', '게시판 삭제', 'TRUE');
insert into awf_url values ('/rest/calendars', 'post', '캘린더별 전체 데이터 조회', 'TRUE');
insert into awf_url values ('/rest/calendars/excel', 'post', '일정 엑셀 다운로드', 'TRUE');
insert into awf_url values ('/rest/calendars/template', 'get', '일괄 등록 템플릿 다운로드', 'TRUE');
insert into awf_url values ('/rest/calendars/{id}/templateUpload', 'post', '일괄 등록', 'TRUE');
insert into awf_url values ('/rest/calendars/{id}/repeat', 'post', '반복 일정 등록', 'TRUE');
insert into awf_url values ('/rest/calendars/{id}/repeat', 'put', '반복 일정 수정', 'TRUE');
insert into awf_url values ('/rest/calendars/{id}/repeat', 'delete', '반복 일정 삭제', 'TRUE');
insert into awf_url values ('/rest/calendars/{id}/schedule', 'post', '일반 일정 등록', 'TRUE');
insert into awf_url values ('/rest/calendars/{id}/schedule', 'put', '일반 일정 수정', 'TRUE');
insert into awf_url values ('/rest/calendars/{id}/schedule', 'delete', '일반 일정 삭제', 'TRUE');
insert into awf_url values ('/rest/cmdb/attributes', 'get', 'CMDB Attribute 조회', 'FALSE');
insert into awf_url values ('/rest/cmdb/attributes', 'post', 'CMDB Attribute 등록', 'TRUE');
insert into awf_url values ('/rest/cmdb/attributes/{id}', 'put', 'CMDB Attribute 수정', 'TRUE');
insert into awf_url values ('/rest/cmdb/attributes/{id}', 'delete', 'CMDB Attribute 삭제', 'TRUE');
insert into awf_url values ('/rest/cmdb/cis/component/list', 'get', 'CI 모달 스크롤 조회', 'FALSE');
insert into awf_url values ('/rest/cmdb/cis/{id}/data', 'post', 'CI 컴포넌트 - CI 세부 정보 등록', 'FALSE');
insert into awf_url values ('/rest/cmdb/cis/{id}/data', 'get', 'CI 컴포넌트 - CI 컴포넌트 세부 정보 조회', 'FALSE');
insert into awf_url values ('/rest/cmdb/cis/{id}/relation', 'get', 'CI 연관 관계 데이터 조회', 'FALSE');
insert into awf_url values ('/rest/cmdb/cis/data', 'delete', 'CI 컴포넌트 - CI 세부 정보 삭제', 'FALSE');
insert into awf_url values ('/rest/cmdb/cis/excel', 'post', 'CI 조회 엑셀 다운로드', 'TRUE');
insert into awf_url values ('/rest/cmdb/cis/template', 'get', 'CI 일괄 등록 템플릿 다운로드', 'TRUE');
insert into awf_url values ('/rest/cmdb/cis/templateUpload', 'post', 'CI 일괄 등록 템플릿 업로드', 'TRUE');
insert into awf_url values ('/rest/cmdb/classes', 'get', 'CMDB Class 리스트', 'TRUE');
insert into awf_url values ('/rest/cmdb/classes', 'post', 'CMDB Class 등록', 'TRUE');
insert into awf_url values ('/rest/cmdb/classes/{id}', 'get', 'CMDB Class 단일 조회', 'TRUE');
insert into awf_url values ('/rest/cmdb/classes/{id}', 'put', 'CMDB Class 수정', 'TRUE');
insert into awf_url values ('/rest/cmdb/classes/{id}', 'delete', 'CMDB Class 삭제', 'TRUE');
insert into awf_url values ('/rest/cmdb/classes/{id}/attributes', 'get', 'CI 컴포넌트 - CI CLASS에 따른 세부 속성 조회', 'FALSE');
insert into awf_url values ('/rest/cmdb/types', 'get', 'CMDB Type 조회', 'TRUE');
insert into awf_url values ('/rest/cmdb/types', 'post', 'CMDB Type 등록', 'TRUE');
insert into awf_url values ('/rest/cmdb/types/{id}', 'get', 'CMDB Type 단일 조회', 'TRUE');
insert into awf_url values ('/rest/cmdb/types/{id}', 'put', 'CMDB Type 수정', 'TRUE');
insert into awf_url values ('/rest/cmdb/types/{id}', 'delete', 'CMDB Type 삭제', 'TRUE');
insert into awf_url values ('/rest/codes', 'post', '코드 등록', 'TRUE');
insert into awf_url values ('/rest/codes', 'get', '코드 전체 조회', 'TRUE');
insert into awf_url values ('/rest/codes/excel', 'get', '코드 조회 엑셀 다운로드', 'TRUE');
insert into awf_url values ('/rest/codes/{id}', 'put', '코드 수정', 'TRUE');
insert into awf_url values ('/rest/codes/{id}', 'get', '코드 상세 조회', 'TRUE');
insert into awf_url values ('/rest/codes/{id}', 'delete', '코드 삭제', 'TRUE');
insert into awf_url values ('/rest/codes/related/{id}', 'get', '연관 코드 상세 조회', 'FALSE');
insert into awf_url values ('/rest/comments', 'post', 'Comment 저장', 'FALSE');
insert into awf_url values ('/rest/comments/{id}', 'delete', 'Comment 삭제', 'FALSE');
insert into awf_url values ('/rest/custom-codes', 'get', '커스텀 코드 조회', 'FALSE');
insert into awf_url values ('/rest/custom-codes', 'put', '커스텀 코드 수정', 'TRUE');
insert into awf_url values ('/rest/custom-codes', 'post', '커스텀 코드 등록', 'TRUE');
insert into awf_url values ('/rest/custom-codes/{id}', 'delete', '커스텀 코드 삭제', 'TRUE');
insert into awf_url values ('/rest/custom-codes/{id}', 'get', '커스텀코드 목록 조회', 'FALSE');
insert into awf_url values ('/rest/dashboard/template/{id}/component/{id}', 'post', '템플릿 컴포넌트별 조회', 'FALSE');
insert into awf_url values ('/rest/documents', 'get', '신청서 문서 목록 조회', 'TRUE');
insert into awf_url values ('/rest/workflows', 'post', '신청서 작성', 'TRUE');
insert into awf_url values ('/rest/workflows/{id}', 'delete', '신청서 삭제', 'TRUE');
insert into awf_url values ('/rest/workflows/{id}', 'get', '신청서 데이터 조회', 'TRUE');
insert into awf_url values ('/rest/workflows/{id}', 'put', '신청서 수정', 'TRUE');
insert into awf_url values ('/rest/workflows/{id}/display', 'put', '신청서 디스플레이 데이터 저장', 'TRUE');
insert into awf_url values ('/rest/workflows/{id}/export', 'get', '신청서 export 데이터 조회', 'TRUE');
insert into awf_url values ('/rest/workflows/import', 'post', '업무흐름 import', 'TRUE');
insert into awf_url values ('/rest/documents/{id}/data', 'get', '신청서의 문서 데이터 조회', 'TRUE');
insert into awf_url values ('/rest/documents/components/{id}/value', 'get', '이력조회 컴포넌트 조회', 'TRUE');
insert into awf_url values ('/rest/archives', 'post', '자료실 등록', 'TRUE');
insert into awf_url values ('/rest/archives', 'put', '자료실 변경', 'TRUE');
insert into awf_url values ('/rest/archives/{id}', 'delete', '자료실 삭제', 'TRUE');
insert into awf_url values ('/rest/faqs', 'post', 'FAQ 등록 처리', 'TRUE');
insert into awf_url values ('/rest/faqs/{id}', 'put', 'FAQ 수정 처리', 'TRUE');
insert into awf_url values ('/rest/faqs/{id}', 'get', 'FAQ 상세 조회', 'TRUE');
insert into awf_url values ('/rest/faqs/{id}', 'delete', 'FAQ 삭제 처리', 'TRUE');
insert into awf_url values ('/rest/filenameextensions', 'get', '파일 확장자목록', 'FALSE');
insert into awf_url values ('/rest/files/download', 'get', '파일관리 다운로드', 'FALSE');
insert into awf_url values ('/rest/folders', 'post', '폴더 등록', 'FALSE');
insert into awf_url values ('/rest/folders', 'delete', '폴더 삭제', 'FALSE');
insert into awf_url values ('/rest/folders/{folderId}', 'get', '폴더조회', 'FALSE');
insert into awf_url values ('/rest/forms/{id}', 'delete', '폼 디자이너 삭제', 'TRUE');
insert into awf_url values ('/rest/forms/{id}/data', 'get', '폼 디자이너 세부 정보 불러오기', 'TRUE');
insert into awf_url values ('/rest/forms/{id}/data', 'put', '폼 디자이너 세부 정보 저장', 'TRUE');
insert into awf_url values ('/rest/forms', 'post', '폼 디자이너 기본 정보 저장 / 다른 이름 저장 처리', 'TRUE');
insert into awf_url values ('/rest/forms/{id}', 'put', '폼 디자이너 기본 정보 수정', 'TRUE');
insert into awf_url values ('/rest/forms/{id}', 'get', '폼 디자이너 기본 정보 조회', 'TRUE');
insert into awf_url values ('/rest/files', 'post', '파일 업로드', 'TRUE');
insert into awf_url values ('/rest/files', 'put', '파일명 수정', 'TRUE');
insert into awf_url values ('/rest/files/{id}', 'get', '파일 조회', 'FALSE');
insert into awf_url values ('/rest/files/{id}', 'delete', '파일 삭제', 'TRUE');
insert into awf_url values ('/rest/files', 'get', '파일 전체 조회', 'FALSE');
insert into awf_url values ('/rest/instances/{id}/schedule', 'get', '문서 일정 조회', 'TRUE');
insert into awf_url values ('/rest/instances/{id}/schedule', 'post', '문서 일정 등록', 'TRUE');
insert into awf_url values ('/rest/instances/{id}/schedule/{id}', 'delete', '문서 일정 삭제', 'TRUE');
insert into awf_url values ('/rest/instances/{id}/viewer/', 'get', '참조인 목록 조회', 'TRUE');
insert into awf_url values ('/rest/instances/{id}/viewer/', 'post', '참조인 등록(수정)', 'TRUE');
insert into awf_url values ('/rest/instances/{id}/viewer/{userkey}', 'delete', '참조인 삭제', 'TRUE');
insert into awf_url values ('/rest/instances/{id}/viewer/{userkey}/read', 'post', '참조인 읽음', 'TRUE');
insert into awf_url values ('/rest/instances/{instanceId}/history', 'get', '문서 이력조회', 'FALSE');
insert into awf_url values ('/rest/instances/{instanceId}/comments', 'get', '댓글 조회', 'FALSE');
insert into awf_url values ('/rest/instances/{instanceId}/comments', 'post', '댓글 등록', 'FALSE');
insert into awf_url values ('/rest/instances/{instanceId}/comments/{commentId}', 'delete', '댓글 삭제', 'FALSE');
insert into awf_url values ('/rest/instances/{instanceId}/tags', 'get', '태그 조회', 'FALSE');
insert into awf_url values ('/rest/notices', 'post', '공지사항 등록', 'TRUE');
insert into awf_url values ('/rest/notices/{id}', 'delete', '공지사항 삭제', 'TRUE');
insert into awf_url values ('/rest/notices/{id}', 'put', '공지사항 수정', 'TRUE');
insert into awf_url values ('/rest/notifications/{id}', 'delete', '알림 리스트 삭제', 'FALSE');
insert into awf_url values ('/rest/notifications/{id}/confirm', 'put', '알림 리스트 확인 여부 수정', 'FALSE');
insert into awf_url values ('/rest/notifications/{id}/display', 'put', '알림 리스트 표시 여부 수정', 'FALSE');
insert into awf_url values ('/rest/numberingPatterns', 'get', '패턴 리스트', 'TRUE');
insert into awf_url values ('/rest/numberingPatterns', 'post', '패턴 등록', 'TRUE');
insert into awf_url values ('/rest/numberingPatterns/{id}', 'get', '패턴 세부 조회', 'TRUE');
insert into awf_url values ('/rest/numberingPatterns/{id}', 'put', '패턴 정보 변경', 'TRUE');
insert into awf_url values ('/rest/numberingPatterns/{id}', 'delete', '패턴 삭제', 'TRUE');
insert into awf_url values ('/rest/numberingRules', 'get', '문서번호 리스트', 'TRUE');
insert into awf_url values ('/rest/numberingRules', 'post', '문서번호 등록', 'TRUE');
insert into awf_url values ('/rest/numberingRules/{id}', 'get', '문서번호 세부 조회', 'TRUE');
insert into awf_url values ('/rest/numberingRules/{id}', 'put', '문서번호 정보 변경', 'TRUE');
insert into awf_url values ('/rest/numberingRules/{id}', 'delete', '문서번호 삭제', 'TRUE');
insert into awf_url values ('/rest/organizations', 'get', '조직 조회', 'FALSE');
insert into awf_url values ('/rest/organizations/{id}', 'get', '조직 상세 조회', 'FALSE');
insert into awf_url values ('/rest/organizations/{id}', 'put', '조직 수정', 'TRUE');
insert into awf_url values ('/rest/organizations/{id}', 'delete', '조직 삭제', 'TRUE');
insert into awf_url values ('/rest/organizations', 'post', '조직 등록', 'TRUE');
insert into awf_url values ('/rest/plugins', 'get', '플러그인 목록 조회', 'TRUE');
insert into awf_url values ('/rest/plugins/{id}', 'post', '플러그인 조회', 'TRUE');
insert into awf_url values ('/rest/portals', 'get', '포탈 조회 (페이징)', 'FALSE');
insert into awf_url values ('/rest/portals/filedownload', 'get', '포탈 상세 파일 리스트 조회', 'FALSE');
insert into awf_url values ('/rest/portals/filenameextensions', 'get', '포탈 첨부파일 확장자 조회', 'FALSE');
insert into awf_url values ('/rest/portals/filelist', 'get', '포탈 상세 파일 리스트 조회', 'FALSE');
insert into awf_url values ('/rest/portals/top', 'get', '포탈 첫화면 Top 조회', 'FALSE');
insert into awf_url values ('/rest/processes', 'post', '프로세스 디자이너 기본 정보 저장 / 다른이름 저장 처리', 'TRUE');
insert into awf_url values ('/rest/processes/{id}', 'put', '프로세스 수정', 'TRUE');
insert into awf_url values ('/rest/processes/all', 'get', '발행 상태인 프로세스 목록 조회', 'TRUE');
insert into awf_url values ('/rest/process/{id}', 'delete', '프로세스 디자이너 삭제', 'TRUE');
insert into awf_url values ('/rest/process/{id}/data', 'put', '프로세스 디자이너 수정', 'TRUE');
insert into awf_url values ('/rest/process/{id}/data', 'get', '프로세스 디자이너 불러오기', 'TRUE');
insert into awf_url values ('/rest/process/{id}/simulation', 'put', '프로세스 시뮬레이션', 'TRUE');
insert into awf_url values ('/rest/processes/{id}/data', 'get', '프로세스 기본데이터 조회', 'TRUE');
insert into awf_url values ('/rest/roles', 'post', '역할 등록', 'TRUE');
insert into awf_url values ('/rest/roles', 'get', '역할 전체 목록 조회', 'TRUE');
insert into awf_url values ('/rest/roles/excel', 'get', '역할 조회 엑셀 다운로드', 'TRUE');
insert into awf_url values ('/rest/roles/{id}', 'get', '역할 상제 정보 조회', 'TRUE');
insert into awf_url values ('/rest/roles/{id}', 'put', '역할 수정', 'TRUE');
insert into awf_url values ('/rest/roles/{id}', 'delete', '역할 삭제', 'TRUE');
insert into awf_url values ('/rest/schedulers', 'post', '스케줄러 등록', 'TRUE');
insert into awf_url values ('/rest/schedulers/{id}', 'delete', '스케줄러 삭제', 'TRUE');
insert into awf_url values ('/rest/schedulers/{id}', 'put', '스케줄러 수정', 'TRUE');
insert into awf_url values ('/rest/schedulers/{id}/execute', 'post', '스케줄러 실행', 'TRUE');
insert into awf_url values ('/rest/sla/metrics', 'get', '해당 년도에 저장된 지표 목록', 'TRUE');
insert into awf_url values ('/rest/sla/metrics', 'post', '년도별 지표 등록', 'TRUE');
insert into awf_url values ('/rest/sla/metrics', 'put', '년도별 지표 변경', 'TRUE');
insert into awf_url values ('/rest/sla/metrics/{id}/{year}', 'delete', '년도별 지표 삭제', 'TRUE');
insert into awf_url values ('/rest/sla/metrics/copy', 'post', '지표 데이터 복사', 'TRUE');
insert into awf_url values ('/rest/sla/metrics/exist', 'get', '연도별 지표 존재 여부 확인', 'FALSE');
insert into awf_url values ('/rest/sla/metric-manuals', 'post', '수동 지표 등록', 'TRUE');
insert into awf_url values ('/rest/sla/metric-manuals/{id}', 'delete', '수동 지표 삭제', 'TRUE');
insert into awf_url values ('/rest/sla/metrics/annual/excel', 'get', '년도별 현황 엑셀 다운로드', 'TRUE');
insert into awf_url values ('/rest/sla/metrics/{id}/preview', 'get', '년도별 현황 미리보기', 'TRUE');
insert into awf_url values ('/rest/sla/metric-status', 'get', '지표별 현황 목록 화면', 'TRUE');
insert into awf_url VALUES ('/rest/sla/metric-status/list', 'get', '년도별 지표 목록 불러오기 ', 'TRUE');
insert into awf_url values ('/rest/sla/metric-pools', 'post', 'SLA 지표 등록', 'TRUE');
insert into awf_url values ('/rest/sla/metric-pools/{id}', 'put', 'SLA 지표 변경', 'TRUE');
insert into awf_url values ('/rest/sla/metric-pools/{id}', 'delete', 'SLA 지표 삭제', 'TRUE');
insert into awf_url values ('/rest/statistics/customChart', 'post', '사용자 정의 차트 등록', 'TRUE');
insert into awf_url values ('/rest/statistics/customChart/{id}', 'get', '사용자 정의 차트 미리보기');
insert into awf_url values ('/rest/statistics/customChart/{id}', 'put', '사용자 정의 차트 수정', 'TRUE');
insert into awf_url values ('/rest/statistics/customChart/{id}', 'delete', '사용자 정의 차트 삭제', 'TRUE');
insert into awf_url values ('/rest/statistics/customChart/{id}/preview', 'post', '사용자 정의 차트 미리보기', 'TRUE');
insert into awf_url values ('/rest/statistics/customReportTemplate', 'post', '템플릿 설정 등록 처리', 'TRUE');
insert into awf_url values ('/rest/statistics/customReportTemplate/charts', 'get', '템플릿 차트 데이터 조회', 'TRUE');
insert into awf_url values ('/rest/statistics/customReportTemplate/{id}', 'delete', '템플릿 설정 삭제 처리', 'TRUE');
insert into awf_url values ('/rest/statistics/customReportTemplate/{id}', 'post', '보고서 생성 (임시)', 'TRUE');
insert into awf_url values ('/rest/statistics/customReportTemplate/{id}', 'put', '템플릿 설정 수정 처리', 'TRUE');
insert into awf_url values ('/rest/tags/whitelist', 'get', 'Tag 추천 목록 조회', 'FALSE');
insert into awf_url values ('/rest/tags', 'post', 'Tag 저장', 'FALSE');
insert into awf_url values ('/rest/tags/{id}', 'delete', 'Tag 삭제', 'FALSE');
insert into awf_url values ('/rest/tokens/data', 'post', 'token 신규 등록', 'TRUE');
insert into awf_url values ('/rest/tokens/{id}/data', 'get', '처리할 문서 상세 데이터', 'TRUE');
insert into awf_url values ('/rest/tokens/{id}/data', 'put', 'token 수정', 'TRUE');
insert into awf_url values ('/rest/tokens/{id}/status', 'get', '프로세스맵 팝업 화면', 'FALSE');
insert into awf_url values ('/rest/users', 'post', '사용자 등록', 'TRUE');
insert into awf_url values ('/rest/users/all', 'get', '전체 사용자 목록 조회', 'TRUE');
insert into awf_url values ('/rest/users/{userkey}/all', 'put', '사용자가 자신의 정보를 업데이트', 'TRUE');
insert into awf_url values ('/rest/users/{userkey}/info', 'put', '사용자가 다른 사용자의 정보를 업데이트', 'FALSE');
insert into awf_url values ('/rest/users/{userkey}/resetpassword', 'put', '사용자 비밀번호 초기화', 'TRUE');
insert into awf_url values ('/rest/users/colors', 'get', '사용자 정의 색상 조회', 'FALSE');
insert into awf_url values ('/rest/users/colors', 'put', '사용자 정의 색상 저장', 'FALSE');
insert into awf_url values ('/rest/users/excel', 'get', '사용자 목록 엑셀 다운로드', 'TRUE');
insert into awf_url values ('/rest/products/info', 'get', '제품 정보 조회', 'TRUE');
insert into awf_url values ('/roles/search', 'get', '역할 검색화면', 'TRUE');
insert into awf_url values ('/roles/new', 'get', '역할 등록', 'TRUE');
insert into awf_url values ('/roles/{id}/edit', 'get', '역할 수정', 'TRUE');
insert into awf_url values ('/roles/{id}/view', 'get', '역할 상세 보기', 'TRUE');
insert into awf_url values ('/roles', 'get', '역할 관리 목록 뷰 호출', 'TRUE');
insert into awf_url values ('/schedulers', 'get', '스케줄러 리스트 화면', 'TRUE');
insert into awf_url values ('/schedulers/new', 'get', '스케줄러 신규 등록 화면', 'TRUE');
insert into awf_url values ('/schedulers/search', 'get', '스케줄러 리스트 화면 호출', 'TRUE');
insert into awf_url values ('/schedulers/{id}/edit', 'get', '스케줄러 상세 수정 화면', 'TRUE');
insert into awf_url values ('/schedulers/{id}/history', 'get', '스케줄러 이력 리스트 모달 화면', 'TRUE');
insert into awf_url values ('/schedulers/{id}/view', 'get', '스케줄러 상세 조회 화면', 'TRUE');
insert into awf_url values ('/sla/metrics/search', 'get', '년도별 지표관리 검색 화면', 'TRUE');
insert into awf_url values ('/sla/metrics', 'get', '년도별 지표관리 목록 화면', 'TRUE');
insert into awf_url values ('/sla/metrics/new', 'get', '년도별 지표관리 등록 화면', 'TRUE');
insert into awf_url values ('/sla/metrics/{id}/{year}/edit', 'get', '년도별 지표관리 편집 화면', 'TRUE');
insert into awf_url values ('/sla/metrics/{id}/{year}/view', 'get', '년도별 지표관리 조회 화면', 'TRUE');
insert into awf_url values ('/sla/metrics/copy', 'get', '연도별 지표 복사해오기 모달 화면', 'TRUE');
insert into awf_url values ('/sla/metric-manuals/search', 'get', '수동 지표관리 검색 화면', 'TRUE');
insert into awf_url values ('/sla/metric-manuals', 'get', '수동 지표관리 목록 화면', 'TRUE');
insert into awf_url values ('/sla/metric-manuals/new', 'get', '수동 지표 등록 모달 화면', 'TRUE');
insert into awf_url values ('/sla/metric-status', 'get', '지표별 현황 차트 화면', 'TRUE');
insert into awf_url values ('/sla/metric-status/search', 'get', '지표별 현황 검색 화면', 'TRUE');
insert into awf_url values ('/sla/metrics/annual/search', 'get', '년도별 현황 검색 화면', 'TRUE');
insert into awf_url values ('/sla/metrics/annual', 'get', '년도별 현황 목록 화면', 'TRUE');
insert into awf_url values ('/sla/metric-pools/search', 'get', 'SLA 지표관리 검색 화면', 'TRUE');
insert into awf_url values ('/sla/metric-pools', 'get', 'SLA 지표관리 목록 화면', 'TRUE');
insert into awf_url values ('/sla/metric-pools/new', 'get', 'SLA 지표관리 등록 화면', 'TRUE');
insert into awf_url values ('/sla/metric-pools/{id}/edit', 'get', 'SLA 지표관리 편집 화면', 'TRUE');
insert into awf_url values ('/sla/metric-pools/{id}/view', 'get', 'SLA 지표관리 조회 화면', 'TRUE');
insert into awf_url values ('/statistics/basicChart/search', 'get', '기본 차트 목록 조회 화면', 'TRUE');
insert into awf_url values ('/statistics/basicReport/search', 'get', '기본 보고서 목록 조회 화면', 'TRUE');
insert into awf_url values ('/statistics/customChart', 'get', '사용자 정의 차트 목록', 'TRUE');
insert into awf_url values ('/statistics/customChart/search', 'get', '사용자 정의 차트 목록 조회 화면', 'TRUE');
insert into awf_url values ('/statistics/customChart/new', 'get', '사용자 정의 차트 등록 화면', 'TRUE');
insert into awf_url values ('/statistics/customChart/{id}/edit', 'get', '사용자 정의 차트 수정 화면', 'TRUE');
insert into awf_url values ('/statistics/customChart/{id}/view', 'get', '사용자 정의 차트 조회 화면', 'TRUE');
insert into awf_url values ('/statistics/customChart/{id}/preview', 'get', '사용자 정의차트 미리보기', 'TRUE');
insert into awf_url values ('/statistics/customDashboardTemplate/edit', 'get', '개인 현황판 관리 화면', 'TRUE');
insert into awf_url values ('/statistics/customReport', 'get', '보고서 조회', 'TRUE');
insert into awf_url values ('/statistics/customReport/search', 'get', '보고서 조회 검색 화면 호출', 'TRUE');
insert into awf_url values ('/statistics/customReport/{id}/view', 'get', '보고서 상세화면', 'TRUE');
insert into awf_url values ('/statistics/customReportTemplate', 'get', '템플릿 설정 목록 조회', 'TRUE');
insert into awf_url values ('/statistics/customReportTemplate/new', 'get', '템플릿 설정 등록', 'TRUE');
insert into awf_url values ('/statistics/customReportTemplate/preview', 'get', '템플릿 미리보기', 'TRUE');
insert into awf_url values ('/statistics/customReportTemplate/search', 'get', '템플릿 설정 검색 화면 호출', 'TRUE');
insert into awf_url values ('/statistics/customReportTemplate/{id}/edit', 'get', '템플릿 설정 수정', 'TRUE');
insert into awf_url values ('/statistics/customReportTemplate/{id}/view', 'get', '템플릿 설정 미리보기', 'TRUE');
insert into awf_url values ('/statistics/dashboardTemplate/search', 'get', '현황판 템플릿 목록 조회 화면', 'TRUE');
insert into awf_url values ('/tokens', 'get', '처리할 문서 리스트 조회', 'FALSE');
insert into awf_url values ('/tokens/search', 'get', '로그인시 인증여부 체크 및 처리할 문서 페이지 이동', 'FALSE');
insert into awf_url values ('/tokens/view-pop/documents', 'get', '관련문서 리스트', 'TRUE');
insert into awf_url values ('/tokens/{id}/edit', 'get', '', 'TRUE');
insert into awf_url values ('/tokens/{id}/view', 'get', '', 'TRUE');
insert into awf_url values ('/tokens/{id}/print', 'get', '처리할 문서 프린트 화면', 'TRUE');
insert into awf_url values ('/tokens/{id}/view-pop', 'get', '관련문서 팝업 화면', 'TRUE');
insert into awf_url values ('/tokens/tokenTab','get','문서조회 탭화면', 'TRUE');
insert into awf_url values ('/users', 'get', '사용자 조회 목록 화면', 'TRUE');
insert into awf_url values ('/users/new', 'get', '사용자 등록 화면', 'TRUE');
insert into awf_url values ('/users/search', 'get', '사용자 검색, 목록 등 메인이 되는 조회 화면', 'TRUE');
insert into awf_url values ('/users/{userkey}/view', 'get', '사용자 정보 조회 화면', 'TRUE');
insert into awf_url values ('/users/{userkey}/edit', 'get', '사용자 정보 수정 화면', 'TRUE');
insert into awf_url values ('/users/{userkey}/editself', 'get', '사용자 자기 정보 수정 화면', 'FALSE');
insert into awf_url values ('/users/substituteUsers', 'get', '업무 대리인 모달 리스트 화면', 'FALSE');
insert into awf_url values ('/users/searchUsers', 'get', '사용자 검색 모달 리스트 화면', 'FALSE');
insert into awf_url values ('/rest/users/updatePassword','put', '비밀번호 변경', 'FALSE');
insert into awf_url values ('/rest/users/nextTime','put', '비밀번호 다음에 변경하기', 'FALSE');
insert into awf_url values ('/rest/users/rsa','get', 'RSA Key 받기', 'FALSE');
insert into awf_url values ('/rest/users/passwordConfirm','post', '사용자 비밀번호 확인', 'FALSE');
insert into awf_url values ('/rest/tokens/todoCount', 'get', '문서함카운트', 'FALSE');
insert into awf_url values ('/rest/tokens/excel', 'get', '문서함 엑셀 다운로드', 'TRUE');
insert into	awf_url values ('/rest/workflows/workflowLink', 'post', '업무흐름 링크 등록', 'TRUE');
insert into awf_url values ('/workflows/workflowLink/{id}/edit', 'get', '업무흐름 링크 편집', 'TRUE');
insert into awf_url values ('/rest/workflows/workflowLink/{id}', 'delete', '업무흐름 링크 삭제', 'TRUE');
insert into awf_url values ('/rest/workflows/workflowLink/{id}', 'put', '업무흐름 링크 변경', 'TRUE');
insert into awf_url values ('/itsm','get','SSO 사용 여부', 'FALSE');
insert into awf_url values ('/itsm/sso','get','SSO 토큰 확인 화면', 'FALSE');
insert into awf_url values ('/itsm/ssoLogin','post','SSO 로그인 처리', 'FALSE');
insert into awf_url values ('/rest/documentStorage', 'post', '보관 문서 데이터 추가', 'FALSE');
insert into awf_url values ('/rest/documentStorage/{instanceId}', 'delete', '보관 문서 데이터 삭제', 'FALSE');
insert into awf_url values ('/rest/documentStorage/{instanceId}/exist', 'get', '보관 문서 데이터 존재 여부 확인', 'FALSE');
insert into awf_url values ('/rest/forms/component/template', 'get', '컴포넌트 템플릿 조회', 'FALSE');
insert into awf_url values ('/rest/forms/component/template', 'post', '컴포넌트 템플릿 저장', 'FALSE');
insert into awf_url values ('/rest/forms/component/template/{templateId}', 'delete', '컴포넌트 템플릿 삭제', 'FALSE');
insert into awf_url values ('/calendars', 'get', '일정 관리', 'TRUE');
