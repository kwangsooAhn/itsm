/**
 * URL별메소드명
 */
DROP TABLE IF EXISTS awf_url cascade;

CREATE TABLE awf_url
(
	url varchar(512) NOT NULL,
	method varchar(16) NOT NULL,
	url_desc varchar(256),
	is_required_auth boolean DEFAULT 'true',
	CONSTRAINT awf_url_pk PRIMARY KEY (url, method)
);

COMMENT ON TABLE awf_url IS 'URL별메소드명';
COMMENT ON COLUMN awf_url.url IS '요청url';
COMMENT ON COLUMN awf_url.method IS 'method';
COMMENT ON COLUMN awf_url.url_desc IS '설명';
COMMENT ON COLUMN awf_url.is_required_auth IS '권한 필수여부';

insert into awf_url values ('/auths/edit', 'get', '역할  설정 뷰를 호출', 'TRUE');
insert into awf_url values ('/auths', 'get', '권한 관리 목록', 'TRUE');
insert into awf_url values ('/board-admin/category/list', 'get', '게시판 관리 카테고리 편집', 'TRUE');
insert into awf_url values ('/board-admin/category/{id}/edit', 'get', '게시판 관리 카테고리 편집', 'TRUE');
insert into awf_url values ('/board-admin/list', 'get', '게시판 관리 리스트 호출화면', 'TRUE');
insert into awf_url values ('/board-admin/new', 'get', '게시판 관리 신규 등록', 'TRUE');
insert into awf_url values ('/board-admin/search', 'get', '게시판 관리 리스트 조회 화면', 'TRUE');
insert into awf_url values ('/board-admin/{id}/edit', 'get', '게시판 관리 상세 편집', 'TRUE');
insert into awf_url values ('/board-admin/{id}/view', 'get', '게시판 관리 상세 조회 화면', 'TRUE');
insert into awf_url values ('/boards/list', 'get', '게시판 리스트 호출 화면', 'TRUE');
insert into awf_url values ('/boards/search', 'get', '게시판 리스트 화면', 'TRUE');
insert into awf_url values ('/boards/search/param', 'get', '게시판 리스트 화면', 'TRUE');
insert into awf_url values ('/boards/{id}/comments/list', 'get', '게시판 댓글 조회', 'TRUE');
insert into awf_url values ('/boards/{id}/edit', 'get', '게시판 편집', 'TRUE');
insert into awf_url values ('/boards/{id}/new', 'get', '게시판 신규 등록', 'TRUE');
insert into awf_url values ('/boards/{id}/replay/edit', 'get', '게시판 답글 편집', 'TRUE');
insert into awf_url values ('/boards/{id}/view', 'get', '게시판 상세 조회 화면', 'TRUE');
insert into awf_url values ('/certification/certifiedmail', 'get', '메일 발송', 'FALSE');
insert into awf_url values ('/certification', 'post', '회원 가입 요청(인증 메일 발송 포함)', 'FALSE');
insert into awf_url values ('/certification/sendCertifiedMail', 'get', '메일 발송', 'FALSE');
insert into awf_url values ('/certification/signup', 'get', '회원 가입 화면 호출', 'FALSE');
insert into awf_url values ('/certification/status', 'get', '메일 인증 상태/재발송 요청 화면', 'FALSE');
insert into awf_url values ('/certification/valid', 'get', '메일 인증', 'FALSE');
insert into awf_url values ('/codes/edit', 'get', '코드 편집 화면', 'TRUE');
insert into awf_url values ('/custom-codes', 'get', '사용자 정의 코드 리스트 화면', 'TRUE');
insert into awf_url values ('/custom-codes/new', 'get', '사용자 정의 코드 신규 등록 화면', 'TRUE');
insert into awf_url values ('/custom-codes/search', 'get', '사용자 정의 코드 리스트 호출 화면', 'TRUE');
insert into awf_url values ('/custom-codes/{id}/edit', 'get', '사용자 정의 코드 수정 화면', 'TRUE');
insert into awf_url values ('/custom-codes/{id}/search', 'get', '커스텀 코드 데이터 조회 화면', 'TRUE');
insert into awf_url values ('/custom-codes/{id}/view', 'get', '사용자 정의 코드 상세 정보 화면', 'TRUE');
insert into awf_url values ('/dashboard/list', 'get', '대시보드 목록', 'TRUE');
insert into awf_url values ('/dashboard/view', 'get', '대시보드 상세 정보 화면', 'TRUE');
insert into awf_url values ('/documents-admin/list', 'get', '업무흐름 리스트 화면', 'TRUE');
insert into awf_url values ('/documents-admin/new', 'get', '신청서 생성 화면', 'TRUE');
insert into awf_url values ('/documents-admin/search', 'get', '업무흐름 데이터 + 목록화면', 'TRUE');
insert into awf_url values ('/documents-admin/{id}/edit', 'get', '신청서 수정 화면', 'TRUE');
insert into awf_url values ('/documents/custom-code/{id}/data', 'get', '커스텀 코드 데이터 조회 화면', 'TRUE');
insert into awf_url values ('/documents/custom-code/{id}/data', 'post', '커스텀 코드 데이터 조회 화면 ', 'TRUE');
insert into awf_url values ('/documents/list', 'get', '신청서 리스트 화면', 'FALSE');
insert into awf_url values ('/documents/search', 'get', '신청서 리스트 호출 화면', 'FALSE');
insert into awf_url values ('/documents/{id}/print', 'get', '신청서 프린트 화면', 'TRUE');
insert into awf_url values ('/downloads', 'get', '자료실 리스트 화면', 'TRUE');
insert into awf_url values ('/downloads/new', 'get', '자료실 신규 등록', 'TRUE');
insert into awf_url values ('/downloads/search', 'get', '자료실 리스트 호출 화면', 'TRUE');
insert into awf_url values ('/downloads/{id}/edit', 'get', '자료실 편집', 'TRUE');
insert into awf_url values ('/downloads/{id}/view', 'get', '자료실 상세 조회 화면', 'TRUE');
insert into awf_url values ('/faqs', 'get', 'FAQ 목록 조회', 'TRUE');
insert into awf_url values ('/faqs/new', 'get', 'FAQ 등록', 'TRUE');
insert into awf_url values ('/faqs/search', 'get', 'FAQ 검색 화면 호출', 'TRUE');
insert into awf_url values ('/faqs/{id}/edit', 'get', 'FAQ 수정', 'TRUE');
insert into awf_url values ('/faqs/{id}/view', 'get', 'FAQ 보기', 'TRUE');
insert into awf_url values ('/form/{id}/edit', 'get', '폼 디자이너 편집화면', 'TRUE');
insert into awf_url values ('/form/{id}/view', 'get', '폼 디자이너 상세화면', 'TRUE');
insert into awf_url values ('/form/{id}/preview', 'get', '폼 디자이너 미리보기 화면', 'TRUE');
insert into awf_url values ('/forms', 'get', '폼 리스트 화면', 'TRUE');
insert into awf_url values ('/forms/search', 'get', '폼 리스트 검색 호출 화면', 'TRUE');
insert into awf_url values ('/images', 'get', '이미지 관리 화면', 'TRUE');
insert into awf_url values ('/notices', 'get', '공지사항 목록', 'TRUE');
insert into awf_url values ('/notices/new', 'get', '공지사항 신규 등록 화면', 'TRUE');
insert into awf_url values ('/notices/search', 'get', '공지사항 검색 화면 호출 처리', 'TRUE');
insert into awf_url values ('/notices/{id}/edit', 'get', '공지사항 편집 화면', 'TRUE');
insert into awf_url values ('/notices/{id}/view', 'get', '공지사항 상세 화면', 'TRUE');
insert into awf_url values ('/notices/{id}/view-pop', 'get', '공지사항 팝업 화면', 'TRUE');
insert into awf_url values ('/notifications', 'get', '알림 리스트 화면', 'FALSE');
insert into awf_url values ('/oauth/{service}/callback', 'get', 'OAuth 로그인 응답 콜백', 'TRUE');
insert into awf_url values ('/oauth/{service}/login', 'get', 'OAuth 로그인 화면 호출', 'TRUE');
insert into awf_url values ('/portals', 'get', '포탈 조회', 'FALSE');
insert into awf_url values ('/portals/browserguide', 'get', '포탈 브라우저 안내', 'FALSE');
insert into awf_url values ('/portals/downloads', 'get', '포달 자료실 리스트', 'FALSE');
insert into awf_url values ('/portals/downloads/{downloadId}/view', 'get', '포탈 자료실 상세조회', 'FALSE');
insert into awf_url values ('/portals/downloads/search', 'get', '포탈 자료실 조회', 'FALSE');
insert into awf_url values ('/portals/faqs', 'get', '포탈 FAQ 상세조회', 'FALSE');
insert into awf_url values ('/portals/faqs/{faqId}/view', 'get', '포탈 FAQ 리스트', 'FALSE');
insert into awf_url values ('/portals/main', 'get', '포탈', 'FALSE');
insert into awf_url values ('/portals/notices', 'get', '포탈 공지사항 리스트', 'FALSE');
insert into awf_url values ('/portals/notices/{noticeId}/view', 'get', '포탈 공지사항 상세 조회', 'FALSE');
insert into awf_url values ('/portals/notices/search', 'get', '포탈 공지사항 조회', 'FALSE');
insert into awf_url values ('/processes', 'get', '프로세스 목록', 'TRUE');
insert into awf_url values ('/processes/search', 'get', '프로세스 리스트 검색 호출 화면', 'TRUE');
insert into awf_url values ('/processes/{id}/edit', 'get', '프로세스 상세 수정 화면', 'TRUE');
insert into awf_url values ('/processes/{id}/view', 'get', '프로세스 상세 보기 화면', 'TRUE');
insert into awf_url values ('/process/{id}/edit', 'get', '프로세스 디자이너 편집 화면' ,'TRUE');
insert into awf_url values ('/process/{id}/view', 'get', '프로세스 디자이너 보기 화면' ,'TRUE');
insert into awf_url values ('/process/{id}/status', 'get', '프로세스 상태', 'TRUE');
insert into awf_url values ('/rest/auths', 'get', '권한 전체 목록 조회', 'TRUE');
insert into awf_url values ('/rest/auths', 'post', '권한 등록', 'TRUE');
insert into awf_url values ('/rest/auths/{id}', 'get', '권한 상세 정보 조회', 'TRUE');
insert into awf_url values ('/rest/auths/{id}', 'put', '권한 수정', 'TRUE');
insert into awf_url values ('/rest/auths/{id}', 'delete', '권한 삭제', 'TRUE');
insert into awf_url values ('/rest/board-admin', 'get', '게시판 관리 조회', 'TRUE');
insert into awf_url values ('/rest/board-admin', 'post', '게시판 관리 등록', 'TRUE');
insert into awf_url values ('/rest/board-admin', 'put', '게시판 관리 변경', 'TRUE');
insert into awf_url values ('/rest/board-admin/{id}/view', 'get', '게시판 관리 상세정보', 'TRUE');
insert into awf_url values ('/rest/board-admin/category', 'post', '게시판 관리 변경', 'TRUE');
insert into awf_url values ('/rest/board-admin/category/{id}', 'delete', '카테고리 관리 삭제', 'TRUE');
insert into awf_url values ('/rest/board-admin/{id}', 'delete', '게시판 관리 삭제', 'TRUE');
insert into awf_url values ('/rest/boards', 'get', '게시판리스트 데이터조회', 'TRUE');
insert into awf_url values ('/rest/boards', 'post', '게시판 등록', 'TRUE');
insert into awf_url values ('/rest/boards', 'put', '게시판 변경', 'TRUE');
insert into awf_url values ('/rest/boards/comments', 'put', '게시판 댓글 수정', 'TRUE');
insert into awf_url values ('/rest/boards/comments', 'post', '게시판 댓글 등록', 'TRUE');
insert into awf_url values ('/rest/boards/comments/{id}', 'delete', '게시판 댓글 삭제', 'TRUE');
insert into awf_url values ('/rest/boards/reply', 'post', '게시판 답글 등록', 'TRUE');
insert into awf_url values ('/rest/boards/{id}', 'delete', '게시판 삭제', 'TRUE');
insert into awf_url values ('/rest/codes', 'post', '코드 등록', 'TRUE');
insert into awf_url values ('/rest/codes', 'get', '코드 전체 조회', 'TRUE');
insert into awf_url values ('/rest/codes/{id}', 'put', '코드 수정', 'TRUE');
insert into awf_url values ('/rest/codes/{id}', 'get', '코드 상세 조회', 'TRUE');
insert into awf_url values ('/rest/codes/{id}', 'delete', '코드 삭제', 'TRUE');
insert into awf_url values ('/rest/comments', 'post', 'Commnet 저장', 'FALSE');
insert into awf_url values ('/rest/comments/{id}', 'delete', 'Comment 삭제', 'FALSE');
insert into awf_url values ('/rest/custom-codes', 'get', '커스텀 코드 조회', 'FALSE');
insert into awf_url values ('/rest/custom-codes', 'put', '커스텀 코드 수정', 'TRUE');
insert into awf_url values ('/rest/custom-codes', 'post', '커스텀 코드 등록', 'TRUE');
insert into awf_url values ('/rest/custom-codes/{id}', 'delete', '커스텀 코드 삭제', 'TRUE');
insert into awf_url values ('/rest/custom-codes/{id}', 'get', '커스텀코드 목록 조회', 'TRUE');
insert into awf_url values ('/rest/documents', 'get', '신청서 문서 목록 조회', 'TRUE');
insert into awf_url values ('/rest/documents-admin', 'post', '신청서 작성', 'TRUE');
insert into awf_url values ('/rest/documents-admin/{id}', 'delete', '신청서 삭제', 'TRUE');
insert into awf_url values ('/rest/documents-admin/{id}', 'get', '신청서 데이터 조회', 'TRUE');
insert into awf_url values ('/rest/documents-admin/{id}', 'put', '신청서 수정', 'TRUE');
insert into awf_url values ('/rest/documents-admin/{id}/display', 'put', '신청서 디스플레이 데이터 저장', 'TRUE');
insert into awf_url values ('/rest/documents-admin/{id}/display', 'get', '신청서 디스플레이 데이터 조회', 'TRUE');
insert into awf_url values ('/rest/documents/{id}/data', 'get', '신청서의 문서 데이터 조회', 'TRUE');
insert into awf_url values ('/rest/documents-admin', 'get', '업무흐름 데이터조회', 'TRUE');
insert into awf_url values ('/rest/downloads', 'get', '자료실리스트 조회', 'TRUE');
insert into awf_url values ('/rest/downloads', 'post', '자료실 등록', 'TRUE');
insert into awf_url values ('/rest/downloads', 'put', '자료실 변경', 'TRUE');
insert into awf_url values ('/rest/downloads/{id}', 'delete', '자료실 삭제', 'TRUE');
insert into awf_url values ('/rest/faqs', 'get', 'FAQ 리스트 데이터 조회', 'TRUE');
insert into awf_url values ('/rest/faqs', 'post', 'FAQ 등록 처리', 'TRUE');
insert into awf_url values ('/rest/faqs/{id}', 'put', 'FAQ 수정 처리', 'TRUE');
insert into awf_url values ('/rest/faqs/{id}', 'get', 'FAQ 상세 조회', 'TRUE');
insert into awf_url values ('/rest/faqs/{id}', 'delete', 'FAQ 삭제 처리', 'TRUE');
insert into awf_url values ('/rest/filenameextensions', 'get', '파일 확장자목록', 'FALSE');
insert into awf_url values ('/rest/folders', 'post', '폴더 등록', 'FALSE');
insert into awf_url values ('/rest/folders/{id}', 'delete', '폴더 삭제', 'FALSE');
insert into awf_url values ('/rest/form/{id}', 'delete', '폼 디자이너 삭제', 'TRUE');
insert into awf_url values ('/rest/form/{id}/data', 'get', '폼 디자이너 세부 정보 불러오기', 'TRUE');
insert into awf_url values ('/rest/form/{id}/data', 'put', '폼 디자이너 세부 정보 저장', 'TRUE');
insert into awf_url values ('/rest/forms', 'post', '폼 디자이너 기본 정보 저장 / 다른 이름 저장 처리', 'TRUE');
insert into awf_url values ('/rest/forms/{id}', 'put', '폼 디자이너 기본 정보 수정', 'TRUE');
insert into awf_url values ('/rest/forms', 'get', '문서양식 데이터조회', 'TRUE');
insert into awf_url values ('/rest/forms/{id}/data', 'get', '문서양식 데이터조회', 'TRUE');
insert into awf_url values ('/rest/images', 'post', '이미지 업로드', 'TRUE');
insert into awf_url values ('/rest/images', 'put', '이미지명 수정', 'TRUE');
insert into awf_url values ('/rest/images/{id}', 'get', '이미지 조회', 'FALSE');
insert into awf_url values ('/rest/images/{id}', 'delete', '이미지 삭제', 'TRUE');
insert into awf_url values ('/rest/images', 'get', '이미지 전체 조회', 'FALSE');
insert into awf_url values ('/rest/notices', 'get', '공지사항 목록 조회', 'TRUE');
insert into awf_url values ('/rest/notices', 'post', '공지사항 등록', 'TRUE');
insert into awf_url values ('/rest/notices/{id}', 'get', '공지사항 상세 조회', 'TRUE');
insert into awf_url values ('/rest/notices/{id}', 'delete', '공지사항 삭제', 'TRUE');
insert into awf_url values ('/rest/notices/{id}', 'put', '공지사항 수정', 'TRUE');
insert into awf_url values ('/rest/notifications/{id}', 'delete', '알림 리스트 삭제', 'FALSE');
insert into awf_url values ('/rest/notifications/{id}/confirm', 'put', '알림 리스트 확인 여부 수정', 'FALSE');
insert into awf_url values ('/rest/notifications/{id}/display', 'put', '알림 리스트 표시 여부 수정', 'FALSE');
insert into awf_url values ('/rest/portals', 'get', '포탈 조회 (페이징)', 'FALSE');
insert into awf_url values ('/rest/portals/downloads', 'get', '포탈 자료실 조회', 'FALSE');
insert into awf_url values ('/rest/portals/filedownload', 'get', '포탈 상세 파일 리스트 조회', 'FALSE');
insert into awf_url values ('/rest/portals/filenameextensions', 'get', '포탈 첨부파일 확장자 조회', 'FALSE');
insert into awf_url values ('/rest/portals/files', 'get', '포탈 상세 파일 리스트 조회', 'FALSE');
insert into awf_url values ('/rest/portals/notices', 'get', '포탈 공지사항 리스트', 'FALSE');
insert into awf_url values ('/rest/portals/top', 'get', '포탈 첫화면 Top 조회', 'FALSE');
insert into awf_url values ('/rest/processes', 'post', '프로세스 디자이너 기본 정보 저장 / 다른이름 저장 처리', 'TRUE');
insert into awf_url values ('/rest/processes/{id}', 'put', '프로세스 수정', 'TRUE');
insert into awf_url values ('/rest/processes', 'get', '프로세스 데이터조회', 'TRUE');
insert into awf_url values ('/rest/processes/all', 'get', '발행 상태인 프로세스 목록 조회', 'TRUE');
insert into awf_url values ('/rest/processes/{processId}/data', 'get', '프로세스 데이터조회', 'TRUE');
insert into awf_url values ('/rest/process/{id}', 'delete', '프로세스 디자이너 삭제', 'TRUE');
insert into awf_url values ('/rest/process/{id}/data', 'put', '프로세스 디자이너 수정', 'TRUE');
insert into awf_url values ('/rest/process/{id}/data', 'get', '프로세스 디자이너 불러오기', 'TRUE');
insert into awf_url values ('/rest/process/{id}/simulation', 'put', '프로세스 시뮬레이션', 'TRUE');
insert into awf_url values ('/rest/processes/{id}/data', 'get', '프로세스 기본데이터 조회', 'TRUE');
insert into awf_url values ('/rest/processes/{id}/data', 'put', '프로세스 데이터조회', 'TRUE');
insert into awf_url values ('/rest/roles', 'post', '역할 등록', 'TRUE');
insert into awf_url values ('/rest/roles', 'get', '역할 전체 목록 조회', 'TRUE');
insert into awf_url values ('/rest/roles/{id}', 'get', '역할 상제 정보 조회', 'TRUE');
insert into awf_url values ('/rest/roles/{id}', 'put', '역할 수정', 'TRUE');
insert into awf_url values ('/rest/roles/{id}', 'delete', '역할 삭제', 'TRUE');
insert into awf_url values ('/rest/tags', 'post', 'Tag 저장', 'FALSE');
insert into awf_url values ('/rest/tags/{id}', 'delete', 'Tag 삭제', 'FALSE');
insert into awf_url values ('/rest/tokens', 'get', '문서함 목록 조회', 'TRUE');
insert into awf_url values ('/rest/tokens/data', 'post', 'token 신규 등록', 'TRUE');
insert into awf_url values ('/rest/tokens/data/{id}', 'get', 'NULL', 'TRUE');
insert into awf_url values ('/rest/tokens/{id}/data', 'get', 'NULL', 'TRUE');
insert into awf_url values ('/rest/tokens/{id}/data', 'put', 'token 수정', 'TRUE');
insert into awf_url values ('/rest/users', 'post', '사용자 등록', 'TRUE');
insert into awf_url values ('/rest/users', 'get', '사용자 리스트 데이터 조회', 'TRUE');
insert into awf_url values ('/rest/users/all', 'get', '전체 사용자 목록 조회', 'TRUE');
insert into awf_url values ('/rest/users/{userkey}/all', 'put', '사용자가 자신의 정보를 업데이트', 'TRUE');
insert into awf_url values ('/rest/users/{userkey}/info', 'put', '사용자가 다른 사용자의 정보를 업데이트', 'FALSE');
insert into awf_url values ('/rest/users/{userkey}/resetpassword', 'put', '사용자 비밀번호 초기화', 'TRUE');
insert into awf_url values ('/rest/wf/instances/{id}/history', 'get', 'test', 'FALSE');
insert into awf_url values ('/rest/wf/instances/{id}/latest', 'get', '마지막 토큰 정보 조회', 'FALSE');
insert into awf_url values ('/roles/edit', 'get', '역할 설정 뷰 호출', 'TRUE');
insert into awf_url values ('/roles', 'get', '역할 관리 목록 뷰 호출', 'TRUE');
insert into awf_url values ('/tokens', 'get', '처리할 문서 리스트 조회', 'FALSE');
insert into awf_url values ('/tokens/search', 'get', '로그인시 인증여부 체크 및 처리할 문서 페이지 이동', 'FALSE');
insert into awf_url values ('/tokens/view-pop/documents', 'get', '관련문서 리스트', 'TRUE');
insert into awf_url values ('/tokens/{id}/edit', 'get', 'NULL', 'TRUE');
insert into awf_url values ('/tokens/{id}/edit-tab', 'get', '문서 오른쪽 탭 정보', 'TRUE');
insert into awf_url values ('/tokens/{id}/view', 'get', 'NULL', 'TRUE');
insert into awf_url values ('/tokens/{id}/view-tab', 'get', '문서 오른쪽 탭 정보', 'TRUE');
insert into awf_url values ('/tokens/{id}/print', 'get', '처리할 문서 프린트 화면', 'TRUE');
insert into awf_url values ('/tokens/{id}/view-pop', 'get', '관련문서 팝업 화면', 'TRUE');
insert into awf_url values ('/users', 'get', '사용자 조회 목록 화면', 'TRUE');
insert into awf_url values ('/users/new', 'get', '사용자 등록 화면', 'TRUE');
insert into awf_url values ('/users/search', 'get', '사용자 검색, 목록 등 메인이 되는 조회 화면', 'TRUE');
insert into awf_url values ('/users/{userkey}/edit', 'get', '사용자 정보 수정 화면', 'TRUE');
insert into awf_url values ('/users/{userkey}/editself', 'get', '사용자 자기 정보 수정 화면', 'FALSE');
