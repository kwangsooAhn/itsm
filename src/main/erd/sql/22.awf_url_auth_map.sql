/**
 * URL별권한매핑
 */

DROP TABLE IF EXISTS awf_url_auth_map cascade;

CREATE TABLE awf_url_auth_map
(
	url varchar(512) NOT NULL,
	method varchar(16) NOT NULL,
	auth_id varchar(100) NOT NULL,
	CONSTRAINT awf_url_auth_map_pk PRIMARY KEY (url, method, auth_id),
	CONSTRAINT awf_url_auth_map_fk1 FOREIGN KEY (url, method) REFERENCES awf_url (url, method),
    CONSTRAINT awf_url_auth_map_fk2 FOREIGN KEY (auth_id) REFERENCES awf_auth (auth_id)
);

COMMENT ON TABLE awf_url_auth_map IS 'URL별권한매핑';
COMMENT ON COLUMN awf_url_auth_map.url IS '요청url';
COMMENT ON COLUMN awf_url_auth_map.method IS 'method';
COMMENT ON COLUMN awf_url_auth_map.auth_id IS '권한아이디';

insert into awf_url_auth_map values ('/auths/edit', 'get', 'auth.create');
insert into awf_url_auth_map values ('/auths/edit', 'get', 'auth.delete');
insert into awf_url_auth_map values ('/auths/edit', 'get', 'auth.update');
insert into awf_url_auth_map values ('/auths/edit', 'get', 'auth.read');
insert into awf_url_auth_map values ('/auths/list', 'get', 'auth.create');
insert into awf_url_auth_map values ('/auths/list', 'get', 'auth.delete');
insert into awf_url_auth_map values ('/auths/list', 'get', 'auth.update');
insert into awf_url_auth_map values ('/auths/list', 'get', 'auth.read');
insert into awf_url_auth_map values ('/board-admin/category/list', 'get', 'board.admin.create');
insert into awf_url_auth_map values ('/board-admin/category/list', 'get', 'board.admin.update');
insert into awf_url_auth_map values ('/board-admin/category/{id}/edit', 'get', 'board.admin.update');
insert into awf_url_auth_map values ('/board-admin/category/{id}/edit', 'get', 'board.admin.create');
insert into awf_url_auth_map values ('/board-admin/list', 'get', 'board.admin.create');
insert into awf_url_auth_map values ('/board-admin/list', 'get', 'board.admin.delete');
insert into awf_url_auth_map values ('/board-admin/list', 'get', 'board.admin.update');
insert into awf_url_auth_map values ('/board-admin/list', 'get', 'board.admin.read');
insert into awf_url_auth_map values ('/board-admin/new', 'get', 'board.admin.create');
insert into awf_url_auth_map values ('/board-admin/search', 'get', 'board.admin.update');
insert into awf_url_auth_map values ('/board-admin/search', 'get', 'board.admin.read');
insert into awf_url_auth_map values ('/board-admin/search', 'get', 'board.admin.delete');
insert into awf_url_auth_map values ('/board-admin/search', 'get', 'board.admin.create');
insert into awf_url_auth_map values ('/board-admin/{id}/edit', 'get', 'board.admin.update');
insert into awf_url_auth_map values ('/board-admin/{id}/edit', 'get', 'board.admin.create');
insert into awf_url_auth_map values ('/board-admin/{id}/view', 'get', 'board.admin.read');
insert into awf_url_auth_map values ('/board-admin/{id}/view', 'get', 'board.admin.delete');
insert into awf_url_auth_map values ('/board-admin/{id}/view', 'get', 'board.admin.create');
insert into awf_url_auth_map values ('/board-admin/{id}/view', 'get', 'board.admin.update');
insert into awf_url_auth_map values ('/boards/list', 'get', 'board.create');
insert into awf_url_auth_map values ('/boards/list', 'get', 'board.read');
insert into awf_url_auth_map values ('/boards/list', 'get', 'board.update');
insert into awf_url_auth_map values ('/boards/list', 'get', 'board.delete');
insert into awf_url_auth_map values ('/boards/search', 'get', 'board.delete');
insert into awf_url_auth_map values ('/boards/search', 'get', 'board.create');
insert into awf_url_auth_map values ('/boards/search', 'get', 'board.read');
insert into awf_url_auth_map values ('/boards/search', 'get', 'board.update');
insert into awf_url_auth_map values ('/boards/search/param', 'get', 'board.update');
insert into awf_url_auth_map values ('/boards/search/param', 'get', 'board.create');
insert into awf_url_auth_map values ('/boards/search/param', 'get', 'board.read');
insert into awf_url_auth_map values ('/boards/search/param', 'get', 'board.delete');
insert into awf_url_auth_map values ('/boards/{id}/comments/list', 'get', 'board.read');
insert into awf_url_auth_map values ('/boards/{id}/edit', 'get', 'board.create');
insert into awf_url_auth_map values ('/boards/{id}/edit', 'get', 'board.update');
insert into awf_url_auth_map values ('/boards/{id}/new', 'get', 'board.create');
insert into awf_url_auth_map values ('/boards/{id}/replay/edit', 'get', 'board.create');
insert into awf_url_auth_map values ('/boards/{id}/view', 'get', 'board.update');
insert into awf_url_auth_map values ('/boards/{id}/view', 'get', 'board.create');
insert into awf_url_auth_map values ('/boards/{id}/view', 'get', 'board.read');
insert into awf_url_auth_map values ('/boards/{id}/view', 'get', 'board.delete');
insert into awf_url_auth_map values ('/codes/edit', 'get', 'code.delete');
insert into awf_url_auth_map values ('/codes/edit', 'get', 'code.update');
insert into awf_url_auth_map values ('/codes/edit', 'get', 'code.read');
insert into awf_url_auth_map values ('/codes/edit', 'get', 'code.create');
insert into awf_url_auth_map values ('/custom-codes/list', 'get', 'custom.code.create');
insert into awf_url_auth_map values ('/custom-codes/list', 'get', 'custom.code.read');
insert into awf_url_auth_map values ('/custom-codes/list', 'get', 'custom.code.update');
insert into awf_url_auth_map values ('/custom-codes/list', 'get', 'custom.code.delete');
insert into awf_url_auth_map values ('/custom-codes/new', 'get', 'custom.code.create');
insert into awf_url_auth_map values ('/custom-codes/search', 'get', 'custom.code.update');
insert into awf_url_auth_map values ('/custom-codes/search', 'get', 'custom.code.create');
insert into awf_url_auth_map values ('/custom-codes/search', 'get', 'custom.code.read');
insert into awf_url_auth_map values ('/custom-codes/search', 'get', 'custom.code.delete');
insert into awf_url_auth_map values ('/custom-codes/{id}/edit', 'get', 'custom.code.create');
insert into awf_url_auth_map values ('/custom-codes/{id}/edit', 'get', 'custom.code.update');
insert into awf_url_auth_map values ('/custom-codes/{id}/search', 'get', 'document.create');
insert into awf_url_auth_map values ('/custom-codes/{id}/search', 'get', 'document.read');
insert into awf_url_auth_map values ('/custom-codes/{id}/search', 'get', 'document.delete');
insert into awf_url_auth_map values ('/custom-codes/{id}/search', 'get', 'document.update');
insert into awf_url_auth_map values ('/custom-codes/{id}/view', 'get', 'custom.code.delete');
insert into awf_url_auth_map values ('/custom-codes/{id}/view', 'get', 'custom.code.read');
insert into awf_url_auth_map values ('/custom-codes/{id}/view', 'get', 'custom.code.update');
insert into awf_url_auth_map values ('/custom-codes/{id}/view', 'get', 'custom.code.create');
insert into awf_url_auth_map values ('/dashboard/list', 'get', 'document.read');
insert into awf_url_auth_map values ('/dashboard/view', 'get', 'document.read');
insert into awf_url_auth_map values ('/documents-admin/list', 'get', 'document.admin.delete');
insert into awf_url_auth_map values ('/documents-admin/list', 'get', 'document.admin.read');
insert into awf_url_auth_map values ('/documents-admin/list', 'get', 'document.admin.create');
insert into awf_url_auth_map values ('/documents-admin/list', 'get', 'document.admin.update');
insert into awf_url_auth_map values ('/documents-admin/new', 'get', 'document.admin.create');
insert into awf_url_auth_map values ('/documents-admin/search', 'get', 'document.admin.update');
insert into awf_url_auth_map values ('/documents-admin/search', 'get', 'document.admin.read');
insert into awf_url_auth_map values ('/documents-admin/search', 'get', 'document.admin.create');
insert into awf_url_auth_map values ('/documents-admin/search', 'get', 'document.admin.delete');
insert into awf_url_auth_map values ('/documents-admin/{id}/edit', 'get', 'document.admin.update');
insert into awf_url_auth_map values ('/documents-admin/{id}/edit', 'get', 'document.admin.create');
insert into awf_url_auth_map values ('/documents/custom-code/{id}/data', 'get', 'document.admin.update');
insert into awf_url_auth_map values ('/documents/custom-code/{id}/data', 'get', 'document.update');
insert into awf_url_auth_map values ('/documents/custom-code/{id}/data', 'post', 'document.update');
insert into awf_url_auth_map values ('/documents/custom-code/{id}/data', 'post', 'document.admin.update');
insert into awf_url_auth_map values ('/documents/list', 'get', 'document.read');
insert into awf_url_auth_map values ('/documents/search', 'get', 'document.read');
insert into awf_url_auth_map values ('/documents/{id}/print', 'get', 'document.read');
insert into awf_url_auth_map values ('/downloads/list', 'get', 'download.update');
insert into awf_url_auth_map values ('/downloads/list', 'get', 'download.delete');
insert into awf_url_auth_map values ('/downloads/list', 'get', 'download.create');
insert into awf_url_auth_map values ('/downloads/list', 'get', 'download.read');
insert into awf_url_auth_map values ('/downloads/new', 'get', 'download.create');
insert into awf_url_auth_map values ('/downloads/search', 'get', 'download.update');
insert into awf_url_auth_map values ('/downloads/search', 'get', 'download.read');
insert into awf_url_auth_map values ('/downloads/search', 'get', 'download.create');
insert into awf_url_auth_map values ('/downloads/search', 'get', 'download.delete');
insert into awf_url_auth_map values ('/downloads/{id}/edit', 'get', 'download.create');
insert into awf_url_auth_map values ('/downloads/{id}/edit', 'get', 'download.update');
insert into awf_url_auth_map values ('/downloads/{id}/view', 'get', 'download.delete');
insert into awf_url_auth_map values ('/downloads/{id}/view', 'get', 'download.create');
insert into awf_url_auth_map values ('/downloads/{id}/view', 'get', 'download.read');
insert into awf_url_auth_map values ('/downloads/{id}/view', 'get', 'download.update');
insert into awf_url_auth_map values ('/faqs', 'get', 'faq.read');
insert into awf_url_auth_map values ('/faqs/new', 'get', 'faq.create');
insert into awf_url_auth_map values ('/faqs/search', 'get', 'faq.read');
insert into awf_url_auth_map values ('/faqs/{id}/edit', 'get', 'faq.update');
insert into awf_url_auth_map values ('/faqs/{id}/edit', 'get', 'faq.delete');
insert into awf_url_auth_map values ('/faqs/{id}/edit', 'get', 'faq.create');
insert into awf_url_auth_map values ('/faqs/{id}/view', 'get', 'faq.read');
insert into awf_url_auth_map values ('/forms/{id}/edit', 'get', 'form.read');
insert into awf_url_auth_map values ('/forms/{id}/preview', 'post', 'form.create');
insert into awf_url_auth_map values ('/forms/{id}/preview', 'get', 'form.read');
insert into awf_url_auth_map values ('/forms/{id}/view', 'get', 'form.read');
insert into awf_url_auth_map values ('/forms-admin/list', 'get', 'form.read');
insert into awf_url_auth_map values ('/forms-admin/search', 'get', 'form.read');
insert into awf_url_auth_map values ('/forms/imageUpload/{id}/view', 'get', 'form.read');
insert into awf_url_auth_map values ('/images', 'get', 'image.read');
insert into awf_url_auth_map values ('/notices', 'get', 'notice.read');
insert into awf_url_auth_map values ('/notices/new', 'get', 'notice.create');
insert into awf_url_auth_map values ('/notices/search', 'get', 'notice.delete');
insert into awf_url_auth_map values ('/notices/search', 'get', 'notice.update');
insert into awf_url_auth_map values ('/notices/search', 'get', 'notice.create');
insert into awf_url_auth_map values ('/notices/search', 'get', 'notice.read');
insert into awf_url_auth_map values ('/notices/{id}/edit', 'get', 'notice.create');
insert into awf_url_auth_map values ('/notices/{id}/edit', 'get', 'notice.update');
insert into awf_url_auth_map values ('/notices/{id}/view', 'get', 'notice.read');
insert into awf_url_auth_map values ('/notices/{id}/view-pop', 'get', 'notice.read');
insert into awf_url_auth_map values ('/processes-admin/list', 'get', 'process.read');
insert into awf_url_auth_map values ('/processes-admin/list', 'get', 'process.delete');
insert into awf_url_auth_map values ('/processes-admin/list', 'get', 'process.update');
insert into awf_url_auth_map values ('/processes-admin/list', 'get', 'process.create');
insert into awf_url_auth_map values ('/processes-admin/search', 'get', 'process.read');
insert into awf_url_auth_map values ('/processes-admin/search', 'get', 'process.delete');
insert into awf_url_auth_map values ('/processes-admin/search', 'get', 'process.create');
insert into awf_url_auth_map values ('/processes-admin/search', 'get', 'process.update');
insert into awf_url_auth_map values ('/processes/{id}/view', 'get', 'process.create');
insert into awf_url_auth_map values ('/processes/{id}/view', 'get', 'process.update');
insert into awf_url_auth_map values ('/processes/{id}/edit', 'get', 'process.create');
insert into awf_url_auth_map values ('/processes/{id}/edit', 'get', 'process.update');
insert into awf_url_auth_map values ('/processes/import', 'get', 'process.create');
insert into awf_url_auth_map values ('/processes/{id}/status', 'get', 'document.read');
insert into awf_url_auth_map values ('/processes/attachFile/view', 'get', 'process.read');
insert into awf_url_auth_map values ('/processes/attachFile/view', 'get', 'process.create');
insert into awf_url_auth_map values ('/processes/attachFile/view', 'get', 'process.update');
insert into awf_url_auth_map values ('/processes/attachFile/view', 'get', 'process.delete');
insert into awf_url_auth_map values ('/rest/auths', 'post', 'auth.create');
insert into awf_url_auth_map values ('/rest/auths', 'get', 'auth.delete');
insert into awf_url_auth_map values ('/rest/auths', 'get', 'auth.update');
insert into awf_url_auth_map values ('/rest/auths', 'get', 'auth.create');
insert into awf_url_auth_map values ('/rest/auths', 'get', 'auth.read');
insert into awf_url_auth_map values ('/rest/auths/{id}', 'get', 'auth.update');
insert into awf_url_auth_map values ('/rest/auths/{id}', 'get', 'auth.read');
insert into awf_url_auth_map values ('/rest/auths/{id}', 'get', 'auth.create');
insert into awf_url_auth_map values ('/rest/auths/{id}', 'get', 'auth.delete');
insert into awf_url_auth_map values ('/rest/auths/{id}', 'put', 'auth.update');
insert into awf_url_auth_map values ('/rest/auths/{id}', 'delete', 'auth.delete');
insert into awf_url_auth_map values ('/rest/board-admin', 'get', 'board.admin.read');
insert into awf_url_auth_map values ('/rest/board-admin', 'put', 'board.admin.update');
insert into awf_url_auth_map values ('/rest/board-admin', 'post', 'board.admin.create');
insert into awf_url_auth_map values ('/rest/board-admin/{id}/view', 'get', 'board.create');
insert into awf_url_auth_map values ('/rest/board-admin/{id}/view', 'get', 'board.update');
insert into awf_url_auth_map values ('/rest/board-admin/category', 'post', 'board.admin.create');
insert into awf_url_auth_map values ('/rest/board-admin/category/{id}', 'delete', 'board.admin.delete');
insert into awf_url_auth_map values ('/rest/board-admin/{id}', 'delete', 'board.admin.delete');
insert into awf_url_auth_map values ('/rest/boards', 'get', 'board.read');
insert into awf_url_auth_map values ('/rest/boards', 'put', 'board.update');
insert into awf_url_auth_map values ('/rest/boards', 'post', 'board.create');
insert into awf_url_auth_map values ('/rest/boards/comments', 'put', 'board.update');
insert into awf_url_auth_map values ('/rest/boards/comments', 'post', 'board.create');
insert into awf_url_auth_map values ('/rest/boards/comments/{id}', 'delete', 'board.delete');
insert into awf_url_auth_map values ('/rest/boards/reply', 'post', 'board.create');
insert into awf_url_auth_map values ('/rest/boards/{id}', 'delete', 'board.delete');
insert into awf_url_auth_map values ('/rest/codes', 'get', 'code.read');
insert into awf_url_auth_map values ('/rest/codes', 'post', 'code.create');
insert into awf_url_auth_map values ('/rest/codes', 'get', 'code.delete');
insert into awf_url_auth_map values ('/rest/codes', 'get', 'code.update');
insert into awf_url_auth_map values ('/rest/codes', 'get', 'code.create');
insert into awf_url_auth_map values ('/rest/codes/{id}', 'get', 'code.create');
insert into awf_url_auth_map values ('/rest/codes/{id}', 'get', 'code.update');
insert into awf_url_auth_map values ('/rest/codes/{id}', 'get', 'code.delete');
insert into awf_url_auth_map values ('/rest/codes/{id}', 'get', 'code.read');
insert into awf_url_auth_map values ('/rest/codes/{id}', 'put', 'code.update');
insert into awf_url_auth_map values ('/rest/codes/{id}', 'delete', 'code.delete');
insert into awf_url_auth_map values ('/rest/custom-codes', 'post', 'custom.code.delete');
insert into awf_url_auth_map values ('/rest/custom-codes', 'put', 'custom.code.create');
insert into awf_url_auth_map values ('/rest/custom-codes', 'post', 'custom.code.create');
insert into awf_url_auth_map values ('/rest/custom-codes', 'get', 'custom.code.delete');
insert into awf_url_auth_map values ('/rest/custom-codes', 'get', 'custom.code.create');
insert into awf_url_auth_map values ('/rest/custom-codes', 'put', 'custom.code.read');
insert into awf_url_auth_map values ('/rest/custom-codes', 'post', 'custom.code.read');
insert into awf_url_auth_map values ('/rest/custom-codes', 'get', 'custom.code.read');
insert into awf_url_auth_map values ('/rest/custom-codes', 'put', 'custom.code.update');
insert into awf_url_auth_map values ('/rest/custom-codes', 'post', 'custom.code.update');
insert into awf_url_auth_map values ('/rest/custom-codes', 'get', 'custom.code.update');
insert into awf_url_auth_map values ('/rest/custom-codes', 'put', 'custom.code.delete');
insert into awf_url_auth_map values ('/rest/custom-codes/{id}', 'get', 'form.read');
insert into awf_url_auth_map values ('/rest/custom-codes/{id}', 'get', 'form.update');
insert into awf_url_auth_map values ('/rest/custom-codes/{id}', 'delete', 'custom.code.delete');
insert into awf_url_auth_map values ('/rest/documents', 'get', 'process.update');
insert into awf_url_auth_map values ('/rest/documents', 'get', 'process.create');
insert into awf_url_auth_map values ('/rest/documents-admin', 'post', 'document.admin.create');
insert into awf_url_auth_map values ('/rest/documents-admin/{id}', 'delete', 'document.admin.create');
insert into awf_url_auth_map values ('/rest/documents-admin/{id}', 'get', 'document.admin.create');
insert into awf_url_auth_map values ('/rest/documents-admin/{id}', 'put', 'document.admin.create');
insert into awf_url_auth_map values ('/rest/documents-admin/{id}', 'get', 'document.admin.update');
insert into awf_url_auth_map values ('/rest/documents-admin/{id}', 'put', 'document.admin.update');
insert into awf_url_auth_map values ('/rest/documents-admin/{id}', 'delete', 'document.admin.update');
insert into awf_url_auth_map values ('/rest/documents-admin/{id}', 'delete', 'document.admin.delete');
insert into awf_url_auth_map values ('/rest/documents-admin/{id}', 'put', 'document.admin.delete');
insert into awf_url_auth_map values ('/rest/documents-admin/{id}', 'get', 'document.admin.delete');
insert into awf_url_auth_map values ('/rest/documents-admin/{id}/display', 'put', 'document.admin.create');
insert into awf_url_auth_map values ('/rest/documents-admin/{id}/display', 'get', 'document.admin.create');
insert into awf_url_auth_map values ('/rest/documents/{id}/data', 'get', 'document.create');
insert into awf_url_auth_map values ('/rest/documents-admin', 'get', 'document.admin.read');
insert into awf_url_auth_map values ('/rest/downloads', 'get', 'download.read');
insert into awf_url_auth_map values ('/rest/downloads', 'post', 'download.update');
insert into awf_url_auth_map values ('/rest/downloads', 'put', 'download.update');
insert into awf_url_auth_map values ('/rest/downloads', 'post', 'download.read');
insert into awf_url_auth_map values ('/rest/downloads', 'put', 'download.read');
insert into awf_url_auth_map values ('/rest/downloads', 'put', 'download.create');
insert into awf_url_auth_map values ('/rest/downloads', 'post', 'download.delete');
insert into awf_url_auth_map values ('/rest/downloads', 'post', 'download.create');
insert into awf_url_auth_map values ('/rest/downloads', 'put', 'download.delete');
insert into awf_url_auth_map values ('/rest/downloads/{id}', 'delete', 'download.delete');
insert into awf_url_auth_map values ('/rest/faqs', 'get', 'faq.create');
insert into awf_url_auth_map values ('/rest/faqs', 'get', 'faq.read');
insert into awf_url_auth_map values ('/rest/faqs', 'post', 'faq.create');
insert into awf_url_auth_map values ('/rest/faqs/{id}', 'put', 'faq.update');
insert into awf_url_auth_map values ('/rest/faqs/{id}', 'get', 'faq.read');
insert into awf_url_auth_map values ('/rest/faqs/{id}', 'delete', 'faq.delete');
insert into awf_url_auth_map values ('/rest/fileNameExtensionList', 'get', 'file.read');
insert into awf_url_auth_map values ('/rest/forms/{id}', 'delete', 'form.delete');
insert into awf_url_auth_map values ('/rest/forms/{id}/data', 'get', 'form.read');
insert into awf_url_auth_map values ('/rest/forms/{id}/data', 'get', 'form.create');
insert into awf_url_auth_map values ('/rest/forms/{id}/data', 'put', 'form.create');
insert into awf_url_auth_map values ('/rest/forms/{id}/data', 'get', 'form.update');
insert into awf_url_auth_map values ('/rest/forms/{id}/data', 'put', 'form.update');
insert into awf_url_auth_map values ('/rest/forms/data', 'put', 'form.update');
insert into awf_url_auth_map values ('/rest/forms/data/{id}', 'get', 'form.update');
insert into awf_url_auth_map values ('/rest/forms/data/{id}', 'get', 'form.read');
insert into awf_url_auth_map values ('/rest/forms/imageDelete', 'delete', 'form.delete');
insert into awf_url_auth_map values ('/rest/forms/imageUpload', 'post', 'form.update');
insert into awf_url_auth_map values ('/rest/forms/imageUpload', 'post', 'form.delete');
insert into awf_url_auth_map values ('/rest/forms/imageUpload', 'post', 'form.create');
insert into awf_url_auth_map values ('/rest/forms/imageUpload', 'post', 'form.read');
insert into awf_url_auth_map values ('/rest/forms-admin', 'get', 'form.read');
insert into awf_url_auth_map values ('/rest/forms-admin', 'post', 'form.create');
insert into awf_url_auth_map values ('/rest/forms-admin', 'post', 'form.delete');
insert into awf_url_auth_map values ('/rest/forms-admin/{formId}', 'put', 'form.update');
insert into awf_url_auth_map values ('/rest/forms-admin/{id}/data', 'get', 'form.create');
insert into awf_url_auth_map values ('/rest/forms-admin/{id}/data', 'get', 'form.update');
insert into awf_url_auth_map values ('/rest/images', 'put', 'image.update');
insert into awf_url_auth_map values ('/rest/images', 'post', 'image.create');
insert into awf_url_auth_map values ('/rest/images/{id}', 'delete', 'image.delete');
insert into awf_url_auth_map values ('/rest/images/list', 'get', 'form.create');
insert into awf_url_auth_map values ('/rest/images/list', 'get', 'form.read');
insert into awf_url_auth_map values ('/rest/images/list', 'get', 'form.update');
insert into awf_url_auth_map values ('/rest/images/list', 'get', 'form.delete');
insert into awf_url_auth_map values ('/rest/notices', 'get', 'notice.read');
insert into awf_url_auth_map values ('/rest/notices', 'post', 'notice.create');
insert into awf_url_auth_map values ('/rest/notices/{id}', 'delete', 'notice.delete');
insert into awf_url_auth_map values ('/rest/notices/{id}', 'put', 'notice.update');
insert into awf_url_auth_map values ('/rest/processes-admin', 'post', 'process.read');
insert into awf_url_auth_map values ('/rest/processes-admin', 'put', 'process.update');
insert into awf_url_auth_map values ('/rest/processes-admin', 'get', 'process.read');
insert into awf_url_auth_map values ('/rest/processes-admin/all', 'get', 'process.read');
insert into awf_url_auth_map values ('/rest/processes-admin/{processId}/data', 'get', 'process.create');
insert into awf_url_auth_map values ('/rest/processes-admin/{processId}/data', 'get', 'process.update');
insert into awf_url_auth_map values ('/rest/processes/{id}', 'delete', 'process.delete');
insert into awf_url_auth_map values ('/rest/processes/{id}/data', 'get', 'process.create');
insert into awf_url_auth_map values ('/rest/processes/{id}/data', 'put', 'process.update');
insert into awf_url_auth_map values ('/rest/processes/{id}/data', 'get', 'process.update');
insert into awf_url_auth_map values ('/rest/processes/{id}/simulation', 'put', 'process.create');
insert into awf_url_auth_map values ('/rest/roles', 'get', 'role.create');
insert into awf_url_auth_map values ('/rest/roles', 'get', 'role.read');
insert into awf_url_auth_map values ('/rest/roles', 'post', 'role.create');
insert into awf_url_auth_map values ('/rest/roles', 'get', 'role.delete');
insert into awf_url_auth_map values ('/rest/roles', 'get', 'role.update');
insert into awf_url_auth_map values ('/rest/roles/{id}', 'delete', 'role.delete');
insert into awf_url_auth_map values ('/rest/roles/{id}', 'get', 'role.read');
insert into awf_url_auth_map values ('/rest/roles/{id}', 'get', 'role.create');
insert into awf_url_auth_map values ('/rest/roles/{id}', 'get', 'role.update');
insert into awf_url_auth_map values ('/rest/roles/{id}', 'get', 'role.delete');
insert into awf_url_auth_map values ('/rest/roles/{id}', 'put', 'role.update');
insert into awf_url_auth_map values ('/rest/tokens', 'get', 'token.read');
insert into awf_url_auth_map values ('/rest/tokens/data', 'post', 'token.create');
insert into awf_url_auth_map values ('/rest/tokens/data/{id}', 'get', 'token.create');
insert into awf_url_auth_map values ('/rest/tokens/{id}/data', 'get', 'token.create');
insert into awf_url_auth_map values ('/rest/tokens/{id}/data', 'put', 'token.create');
insert into awf_url_auth_map values ('/rest/users', 'post', 'user.create');
insert into awf_url_auth_map values ('/rest/users', 'get', 'user.read');
insert into awf_url_auth_map values ('/rest/users/all', 'get', 'user.read');
insert into awf_url_auth_map values ('/rest/users/{userkey}/all', 'put', 'user.update');
insert into awf_url_auth_map values ('/rest/users/{userkey}/all', 'put', 'user.read');
insert into awf_url_auth_map values ('/rest/users/{userkey}/info', 'put', 'user.read');
insert into awf_url_auth_map values ('/rest/users/{userkey}/info', 'put', 'user.update');
insert into awf_url_auth_map values ('/rest/users/{userkey}/resetPassword', 'put', 'user.read');
insert into awf_url_auth_map values ('/rest/users/{userkey}/resetPassword', 'put', 'user.update');
insert into awf_url_auth_map values ('/roles/edit', 'get', 'role.update');
insert into awf_url_auth_map values ('/roles/edit', 'get', 'role.read');
insert into awf_url_auth_map values ('/roles/edit', 'get', 'role.create');
insert into awf_url_auth_map values ('/roles/edit', 'get', 'role.delete');
insert into awf_url_auth_map values ('/roles', 'get', 'role.read');
insert into awf_url_auth_map values ('/tokens/list', 'get', 'token.create');
insert into awf_url_auth_map values ('/tokens/list', 'get', 'token.read');
insert into awf_url_auth_map values ('/tokens/search', 'get', 'token.read');
insert into awf_url_auth_map values ('/tokens/search', 'get', 'token.create');
insert into awf_url_auth_map values ('/tokens/view-pop/list', 'get', 'token.create');
insert into awf_url_auth_map values ('/tokens/view-pop/list', 'get', 'token.read');
insert into awf_url_auth_map values ('/tokens/{id}/edit', 'get', 'token.create');
insert into awf_url_auth_map values ('/tokens/{id}/edit-tab', 'get', 'token.create');
insert into awf_url_auth_map values ('/tokens/{id}/view', 'get', 'token.read');
insert into awf_url_auth_map values ('/tokens/{id}/view-tab', 'get', 'token.read');
insert into awf_url_auth_map values ('/tokens/{id}/print', 'get', 'token.read');
insert into awf_url_auth_map values ('/tokens/{id}/view-pop', 'get', 'token.read');
insert into awf_url_auth_map values ('/tokens/{id}/view-pop', 'get', 'token.create');
insert into awf_url_auth_map values ('/users/list', 'get', 'user.update');
insert into awf_url_auth_map values ('/users/list', 'get', 'user.delete');
insert into awf_url_auth_map values ('/users/list', 'get', 'user.read');
insert into awf_url_auth_map values ('/users/list', 'get', 'user.create');
insert into awf_url_auth_map values ('/users/new', 'get', 'user.read');
insert into awf_url_auth_map values ('/users/new', 'get', 'user.create');
insert into awf_url_auth_map values ('/users/new', 'get', 'user.update');
insert into awf_url_auth_map values ('/users/new', 'get', 'user.delete');
insert into awf_url_auth_map values ('/users/search', 'get', 'user.read');
insert into awf_url_auth_map values ('/users/search', 'get', 'user.create');
insert into awf_url_auth_map values ('/users/search', 'get', 'user.update');
insert into awf_url_auth_map values ('/users/search', 'get', 'user.delete');
insert into awf_url_auth_map values ('/users/{userkey}/edit', 'get', 'user.read');
insert into awf_url_auth_map values ('/users/{userkey}/edit', 'get', 'user.update');
insert into awf_url_auth_map values ('/users/{userkey}/editSelf', 'get', 'user.read');
insert into awf_url_auth_map values ('/users/{userkey}/editSelf', 'get', 'user.update');

