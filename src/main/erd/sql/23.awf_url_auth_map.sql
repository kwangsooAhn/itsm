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
insert into awf_url_auth_map values ('/auths', 'get', 'auth.create');
insert into awf_url_auth_map values ('/auths', 'get', 'auth.delete');
insert into awf_url_auth_map values ('/auths', 'get', 'auth.update');
insert into awf_url_auth_map values ('/auths', 'get', 'auth.read');
insert into awf_url_auth_map values ('/boards', 'get', 'board.admin.create');
insert into awf_url_auth_map values ('/boards', 'get', 'board.admin.delete');
insert into awf_url_auth_map values ('/boards', 'get', 'board.admin.update');
insert into awf_url_auth_map values ('/boards', 'get', 'board.admin.read');
insert into awf_url_auth_map values ('/boards/new', 'get', 'board.admin.create');
insert into awf_url_auth_map values ('/boards/search', 'get', 'board.admin.update');
insert into awf_url_auth_map values ('/boards/search', 'get', 'board.admin.read');
insert into awf_url_auth_map values ('/boards/search', 'get', 'board.admin.delete');
insert into awf_url_auth_map values ('/boards/search', 'get', 'board.admin.create');
insert into awf_url_auth_map values ('/boards/{id}/edit', 'get', 'board.admin.update');
insert into awf_url_auth_map values ('/boards/{id}/edit', 'get', 'board.admin.create');
insert into awf_url_auth_map values ('/boards/{id}/view', 'get', 'board.admin.read');
insert into awf_url_auth_map values ('/boards/{id}/view', 'get', 'board.admin.delete');
insert into awf_url_auth_map values ('/boards/{id}/view', 'get', 'board.admin.create');
insert into awf_url_auth_map values ('/boards/{id}/view', 'get', 'board.admin.update');
insert into awf_url_auth_map values ('/boards/articles', 'get', 'board.create');
insert into awf_url_auth_map values ('/boards/articles', 'get', 'board.read');
insert into awf_url_auth_map values ('/boards/articles', 'get', 'board.update');
insert into awf_url_auth_map values ('/boards/articles', 'get', 'board.delete');
insert into awf_url_auth_map values ('/boards/articles/search', 'get', 'board.delete');
insert into awf_url_auth_map values ('/boards/articles/search', 'get', 'board.create');
insert into awf_url_auth_map values ('/boards/articles/search', 'get', 'board.read');
insert into awf_url_auth_map values ('/boards/articles/search', 'get', 'board.update');
insert into awf_url_auth_map values ('/boards/articles/search/param', 'get', 'board.update');
insert into awf_url_auth_map values ('/boards/articles/search/param', 'get', 'board.create');
insert into awf_url_auth_map values ('/boards/articles/search/param', 'get', 'board.read');
insert into awf_url_auth_map values ('/boards/articles/search/param', 'get', 'board.delete');
insert into awf_url_auth_map values ('/boards/articles/{id}/comments', 'get', 'board.read');
insert into awf_url_auth_map values ('/boards/articles/{id}/edit', 'get', 'board.create');
insert into awf_url_auth_map values ('/boards/articles/{id}/edit', 'get', 'board.update');
insert into awf_url_auth_map values ('/boards/articles/{id}/new', 'get', 'board.create');
insert into awf_url_auth_map values ('/boards/articles/{id}/reply/edit', 'get', 'board.create');
insert into awf_url_auth_map values ('/boards/articles/{id}/view', 'get', 'board.update');
insert into awf_url_auth_map values ('/boards/articles/{id}/view', 'get', 'board.create');
insert into awf_url_auth_map values ('/boards/articles/{id}/view', 'get', 'board.read');
insert into awf_url_auth_map values ('/boards/articles/{id}/view', 'get', 'board.delete');
insert into awf_url_auth_map values ('/charts', 'get', 'chart.read');
insert into awf_url_auth_map values ('/charts/search', 'get', 'chart.read');
insert into awf_url_auth_map values ('/charts/new', 'get', 'chart.create');
insert into awf_url_auth_map values ('/charts/new', 'get', 'chart.update');
insert into awf_url_auth_map values ('/charts/{id}/edit', 'get', 'chart.create');
insert into awf_url_auth_map values ('/charts/{id}/edit', 'get', 'chart.update');
insert into awf_url_auth_map values ('/charts/{id}/view', 'get', 'chart.read');
insert into awf_url_auth_map values ('/cmdb/attributes', 'get', 'cmdb.attribute.read');
insert into awf_url_auth_map values ('/cmdb/attributes/new', 'get', 'cmdb.attribute.create');
insert into awf_url_auth_map values ('/cmdb/attributes/search', 'get', 'cmdb.attribute.read');
insert into awf_url_auth_map values ('/cmdb/attributes/{id}/edit', 'get', 'cmdb.attribute.create');
insert into awf_url_auth_map values ('/cmdb/attributes/{id}/edit', 'get', 'cmdb.attribute.update');
insert into awf_url_auth_map values ('/cmdb/attributes/{id}/view', 'get', 'cmdb.attribute.read');
insert into awf_url_auth_map values ('/cmdb/class/edit', 'get', 'cmdb.class.read');
insert into awf_url_auth_map values ('/cmdb/class/view-pop/attributes', 'get', 'cmdb.class.read');
insert into awf_url_auth_map values ('/cmdb/cis', 'get', 'cmdb.ci.read');
insert into awf_url_auth_map values ('/cmdb/cis/search', 'get', 'cmdb.ci.read');
insert into awf_url_auth_map values ('/cmdb/cis/{id}/view', 'get', 'form.read');
insert into awf_url_auth_map values ('/cmdb/types/edit', 'get', 'cmdb.type.read');
insert into awf_url_auth_map values ('/codes/edit', 'get', 'code.delete');
insert into awf_url_auth_map values ('/codes/edit', 'get', 'code.update');
insert into awf_url_auth_map values ('/codes/edit', 'get', 'code.read');
insert into awf_url_auth_map values ('/codes/edit', 'get', 'code.create');
insert into awf_url_auth_map values ('/custom-codes', 'get', 'custom.code.create');
insert into awf_url_auth_map values ('/custom-codes', 'get', 'custom.code.read');
insert into awf_url_auth_map values ('/custom-codes', 'get', 'custom.code.update');
insert into awf_url_auth_map values ('/custom-codes', 'get', 'custom.code.delete');
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
insert into awf_url_auth_map values ('/dashboard/view', 'get', 'document.read');
insert into awf_url_auth_map values ('/workflows', 'get', 'document.admin.delete');
insert into awf_url_auth_map values ('/workflows', 'get', 'document.admin.read');
insert into awf_url_auth_map values ('/workflows', 'get', 'document.admin.create');
insert into awf_url_auth_map values ('/workflows', 'get', 'document.admin.update');
insert into awf_url_auth_map values ('/workflows/new', 'get', 'document.admin.create');
insert into awf_url_auth_map values ('/workflows/search', 'get', 'document.admin.update');
insert into awf_url_auth_map values ('/workflows/search', 'get', 'document.admin.read');
insert into awf_url_auth_map values ('/workflows/search', 'get', 'document.admin.create');
insert into awf_url_auth_map values ('/workflows/search', 'get', 'document.admin.delete');
insert into awf_url_auth_map values ('/workflows/{id}/edit', 'get', 'document.admin.update');
insert into awf_url_auth_map values ('/workflows/{id}/edit', 'get', 'document.admin.create');
insert into awf_url_auth_map values ('/workflows/{id}/display', 'get', 'document.admin.create');
insert into awf_url_auth_map values ('/workflows/{id}/display', 'get', 'document.admin.update');
insert into awf_url_auth_map values ('/documents', 'get', 'document.read');
insert into awf_url_auth_map values ('/documents/search', 'get', 'document.read');
insert into awf_url_auth_map values ('/documents/{id}/print', 'get', 'document.read');
insert into awf_url_auth_map values ('/downloads', 'get', 'download.update');
insert into awf_url_auth_map values ('/downloads', 'get', 'download.delete');
insert into awf_url_auth_map values ('/downloads', 'get', 'download.create');
insert into awf_url_auth_map values ('/downloads', 'get', 'download.read');
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
insert into awf_url_auth_map values ('/form/{id}/edit', 'get', 'form.read');
insert into awf_url_auth_map values ('/form/{id}/preview', 'get', 'form.read');
insert into awf_url_auth_map values ('/form/{id}/view', 'get', 'form.read');
insert into awf_url_auth_map values ('/forms', 'get', 'form.read');
insert into awf_url_auth_map values ('/forms/search', 'get', 'form.read');
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
insert into awf_url_auth_map values ('/numberingPatterns', 'get', 'numbering.pattern.read');
insert into awf_url_auth_map values ('/numberingPatterns/edit', 'get', 'numbering.pattern.update');
insert into awf_url_auth_map values ('/numberingPatterns/edit', 'get', 'numbering.pattern.read');
insert into awf_url_auth_map values ('/numberingPatterns/edit', 'get', 'numbering.pattern.create');
insert into awf_url_auth_map values ('/numberingPatterns/edit', 'get', 'numbering.pattern.delete');
insert into awf_url_auth_map values ('/numberingRules', 'get', 'numbering.rule.read');
insert into awf_url_auth_map values ('/numberingRules/edit', 'get', 'numbering.rule.update');
insert into awf_url_auth_map values ('/numberingRules/edit', 'get', 'numbering.rule.read');
insert into awf_url_auth_map values ('/numberingRules/edit', 'get', 'numbering.rule.create');
insert into awf_url_auth_map values ('/numberingRules/edit', 'get', 'numbering.rule.delete');
insert into awf_url_auth_map values ('/processes', 'get', 'process.read');
insert into awf_url_auth_map values ('/processes', 'get', 'process.delete');
insert into awf_url_auth_map values ('/processes', 'get', 'process.update');
insert into awf_url_auth_map values ('/processes', 'get', 'process.create');
insert into awf_url_auth_map values ('/processes/search', 'get', 'process.read');
insert into awf_url_auth_map values ('/processes/search', 'get', 'process.delete');
insert into awf_url_auth_map values ('/processes/search', 'get', 'process.create');
insert into awf_url_auth_map values ('/processes/search', 'get', 'process.update');
insert into awf_url_auth_map values ('/process/{id}/view', 'get', 'process.create');
insert into awf_url_auth_map values ('/process/{id}/view', 'get', 'process.update');
insert into awf_url_auth_map values ('/process/{id}/edit', 'get', 'process.create');
insert into awf_url_auth_map values ('/process/{id}/edit', 'get', 'process.update');
insert into awf_url_auth_map values ('/process/{id}/status', 'get', 'document.read');
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
insert into awf_url_auth_map values ('/rest/boards', 'put', 'board.admin.update');
insert into awf_url_auth_map values ('/rest/boards', 'post', 'board.admin.create');
insert into awf_url_auth_map values ('/rest/boards/{id}/view', 'get', 'board.create');
insert into awf_url_auth_map values ('/rest/boards/{id}/view', 'get', 'board.update');
insert into awf_url_auth_map values ('/rest/boards/{id}', 'delete', 'board.admin.delete');
insert into awf_url_auth_map values ('/rest/boards/articles', 'put', 'board.update');
insert into awf_url_auth_map values ('/rest/boards/articles', 'post', 'board.create');
insert into awf_url_auth_map values ('/rest/boards/articles/comments', 'put', 'board.update');
insert into awf_url_auth_map values ('/rest/boards/articles/comments', 'post', 'board.create');
insert into awf_url_auth_map values ('/rest/boards/articles/comments/{id}', 'delete', 'board.delete');
insert into awf_url_auth_map values ('/rest/boards/articles/reply', 'post', 'board.create');
insert into awf_url_auth_map values ('/rest/boards/articles/{id}', 'delete', 'board.delete');
insert into awf_url_auth_map values ('/rest/charts', 'post', 'chart.create');
insert into awf_url_auth_map values ('/rest/charts/{id}', 'put', 'chart.update');
insert into awf_url_auth_map values ('/rest/charts/{id}', 'delete', 'chart.delete');
insert into awf_url_auth_map values ('/rest/charts/{id}/preview', 'post', 'chart.create');
insert into awf_url_auth_map values ('/rest/charts/{id}/preview', 'post', 'chart.update');
insert into awf_url_auth_map values ('/rest/cmdb/attributes', 'post', 'cmdb.attribute.create');
insert into awf_url_auth_map values ('/rest/cmdb/attributes/{id}', 'put', 'cmdb.attribute.update');
insert into awf_url_auth_map values ('/rest/cmdb/attributes/{id}', 'delete', 'cmdb.attribute.update');
insert into awf_url_auth_map values ('/rest/cmdb/attributes/{id}', 'delete', 'cmdb.attribute.delete');
insert into awf_url_auth_map values ('/rest/cmdb/classes', 'get', 'cmdb.class.read');
insert into awf_url_auth_map values ('/rest/cmdb/classes', 'get', 'cmdb.class.create');
insert into awf_url_auth_map values ('/rest/cmdb/classes', 'get', 'cmdb.class.update');
insert into awf_url_auth_map values ('/rest/cmdb/classes', 'get', 'cmdb.class.delete');
insert into awf_url_auth_map values ('/rest/cmdb/classes', 'post', 'cmdb.class.create');
insert into awf_url_auth_map values ('/rest/cmdb/classes/{id}', 'get', 'cmdb.class.read');
insert into awf_url_auth_map values ('/rest/cmdb/classes/{id}', 'get', 'cmdb.class.create');
insert into awf_url_auth_map values ('/rest/cmdb/classes/{id}', 'get', 'cmdb.class.update');
insert into awf_url_auth_map values ('/rest/cmdb/classes/{id}', 'get', 'cmdb.class.delete');
insert into awf_url_auth_map values ('/rest/cmdb/classes/{id}', 'put', 'cmdb.class.update');
insert into awf_url_auth_map values ('/rest/cmdb/classes/{id}', 'delete', 'cmdb.class.delete');
insert into awf_url_auth_map values ('/rest/cmdb/classes/{id}/attributes', 'get', 'form.read');
insert into awf_url_auth_map values ('/rest/cmdb/classes/{id}/attributes', 'get', 'form.update');
insert into awf_url_auth_map values ('/rest/cmdb/types', 'get', 'cmdb.type.read');
insert into awf_url_auth_map values ('/rest/cmdb/types', 'get', 'cmdb.type.create');
insert into awf_url_auth_map values ('/rest/cmdb/types', 'get', 'cmdb.type.update');
insert into awf_url_auth_map values ('/rest/cmdb/types', 'get', 'cmdb.type.delete');
insert into awf_url_auth_map values ('/rest/cmdb/types', 'post', 'cmdb.type.create');
insert into awf_url_auth_map values ('/rest/cmdb/types/{id}', 'get', 'cmdb.type.read');
insert into awf_url_auth_map values ('/rest/cmdb/types/{id}', 'get', 'cmdb.type.create');
insert into awf_url_auth_map values ('/rest/cmdb/types/{id}', 'get', 'cmdb.type.update');
insert into awf_url_auth_map values ('/rest/cmdb/types/{id}', 'get', 'cmdb.type.delete');
insert into awf_url_auth_map values ('/rest/cmdb/types/{id}', 'put', 'cmdb.type.update');
insert into awf_url_auth_map values ('/rest/cmdb/types/{id}', 'delete', 'cmdb.type.delete');
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
insert into awf_url_auth_map values ('/rest/documents', 'get', 'document.create');
insert into awf_url_auth_map values ('/rest/documents', 'get', 'document.update');
insert into awf_url_auth_map values ('/rest/documents', 'get', 'document.read');
insert into awf_url_auth_map values ('/rest/documents', 'get', 'document.delete');
insert into awf_url_auth_map values ('/rest/workflows', 'post', 'document.admin.create');
insert into awf_url_auth_map values ('/rest/workflows/{id}', 'delete', 'document.admin.create');
insert into awf_url_auth_map values ('/rest/workflows/{id}', 'get', 'document.admin.create');
insert into awf_url_auth_map values ('/rest/workflows/{id}', 'put', 'document.admin.create');
insert into awf_url_auth_map values ('/rest/workflows/{id}', 'get', 'document.admin.update');
insert into awf_url_auth_map values ('/rest/workflows/{id}', 'put', 'document.admin.update');
insert into awf_url_auth_map values ('/rest/workflows/{id}', 'delete', 'document.admin.update');
insert into awf_url_auth_map values ('/rest/workflows/{id}', 'delete', 'document.admin.delete');
insert into awf_url_auth_map values ('/rest/workflows/{id}', 'put', 'document.admin.delete');
insert into awf_url_auth_map values ('/rest/workflows/{id}', 'get', 'document.admin.delete');
insert into awf_url_auth_map values ('/rest/workflows/{id}/display', 'put', 'document.admin.create');
insert into awf_url_auth_map values ('/rest/documents/{id}/data', 'get', 'document.create');
insert into awf_url_auth_map values ('/rest/downloads', 'post', 'download.update');
insert into awf_url_auth_map values ('/rest/downloads', 'put', 'download.update');
insert into awf_url_auth_map values ('/rest/downloads', 'post', 'download.read');
insert into awf_url_auth_map values ('/rest/downloads', 'put', 'download.read');
insert into awf_url_auth_map values ('/rest/downloads', 'put', 'download.create');
insert into awf_url_auth_map values ('/rest/downloads', 'post', 'download.delete');
insert into awf_url_auth_map values ('/rest/downloads', 'post', 'download.create');
insert into awf_url_auth_map values ('/rest/downloads', 'put', 'download.delete');
insert into awf_url_auth_map values ('/rest/downloads/{id}', 'delete', 'download.delete');
insert into awf_url_auth_map values ('/rest/faqs', 'post', 'faq.create');
insert into awf_url_auth_map values ('/rest/faqs/{id}', 'put', 'faq.update');
insert into awf_url_auth_map values ('/rest/faqs/{id}', 'get', 'faq.read');
insert into awf_url_auth_map values ('/rest/faqs/{id}', 'delete', 'faq.delete');
insert into awf_url_auth_map values ('/rest/filenameextensions', 'get', 'file.read');
insert into awf_url_auth_map values ('/rest/form/{id}', 'delete', 'form.delete');
insert into awf_url_auth_map values ('/rest/form/{id}/data', 'get', 'form.read');
insert into awf_url_auth_map values ('/rest/form/{id}/data', 'get', 'form.create');
insert into awf_url_auth_map values ('/rest/form/{id}/data', 'get', 'form.update');
insert into awf_url_auth_map values ('/rest/form/{id}/data', 'put', 'form.create');
insert into awf_url_auth_map values ('/rest/form/{id}/data', 'put', 'form.update');
insert into awf_url_auth_map values ('/rest/forms', 'post', 'form.create');
insert into awf_url_auth_map values ('/rest/forms', 'post', 'form.delete');
insert into awf_url_auth_map values ('/rest/forms/{id}', 'put', 'form.update');
insert into awf_url_auth_map values ('/rest/forms/{id}/data', 'get', 'form.create');
insert into awf_url_auth_map values ('/rest/forms/{id}/data', 'get', 'form.update');
insert into awf_url_auth_map values ('/rest/images', 'put', 'image.update');
insert into awf_url_auth_map values ('/rest/images', 'post', 'image.create');
insert into awf_url_auth_map values ('/rest/images/{id}', 'delete', 'image.delete');
insert into awf_url_auth_map values ('/rest/images', 'get', 'form.create');
insert into awf_url_auth_map values ('/rest/images', 'get', 'form.read');
insert into awf_url_auth_map values ('/rest/images', 'get', 'form.update');
insert into awf_url_auth_map values ('/rest/images', 'get', 'form.delete');
insert into awf_url_auth_map values ('/rest/notices', 'post', 'notice.create');
insert into awf_url_auth_map values ('/rest/notices/{id}', 'delete', 'notice.delete');
insert into awf_url_auth_map values ('/rest/notices/{id}', 'put', 'notice.update');
insert into awf_url_auth_map values ('/rest/numberingPatterns', 'get', 'numbering.pattern.read');
insert into awf_url_auth_map values ('/rest/numberingPatterns', 'get', 'numbering.pattern.create');
insert into awf_url_auth_map values ('/rest/numberingPatterns', 'get', 'numbering.pattern.update');
insert into awf_url_auth_map values ('/rest/numberingPatterns', 'get', 'numbering.pattern.delete');
insert into awf_url_auth_map values ('/rest/numberingPatterns', 'post', 'numbering.pattern.create');
insert into awf_url_auth_map values ('/rest/numberingPatterns/{id}', 'get', 'numbering.pattern.read');
insert into awf_url_auth_map values ('/rest/numberingPatterns/{id}', 'get', 'numbering.pattern.create');
insert into awf_url_auth_map values ('/rest/numberingPatterns/{id}', 'get', 'numbering.pattern.update');
insert into awf_url_auth_map values ('/rest/numberingPatterns/{id}', 'get', 'numbering.pattern.delete');
insert into awf_url_auth_map values ('/rest/numberingPatterns/{id}', 'put', 'numbering.pattern.update');
insert into awf_url_auth_map values ('/rest/numberingPatterns/{id}', 'delete', 'numbering.pattern.delete');
insert into awf_url_auth_map values ('/rest/numberingRules', 'get', 'numbering.rule.read');
insert into awf_url_auth_map values ('/rest/numberingRules', 'get', 'numbering.rule.create');
insert into awf_url_auth_map values ('/rest/numberingRules', 'get', 'numbering.rule.update');
insert into awf_url_auth_map values ('/rest/numberingRules', 'get', 'numbering.rule.delete');
insert into awf_url_auth_map values ('/rest/numberingRules', 'post', 'numbering.rule.create');
insert into awf_url_auth_map values ('/rest/numberingRules/{id}', 'get', 'numbering.rule.read');
insert into awf_url_auth_map values ('/rest/numberingRules/{id}', 'get', 'numbering.rule.create');
insert into awf_url_auth_map values ('/rest/numberingRules/{id}', 'get', 'numbering.rule.update');
insert into awf_url_auth_map values ('/rest/numberingRules/{id}', 'get', 'numbering.rule.delete');
insert into awf_url_auth_map values ('/rest/numberingRules/{id}', 'put', 'numbering.rule.update');
insert into awf_url_auth_map values ('/rest/numberingRules/{id}', 'delete', 'numbering.rule.delete');
insert into awf_url_auth_map values ('/rest/processes', 'post', 'process.read');
insert into awf_url_auth_map values ('/rest/processes/{id}', 'put', 'process.update');
insert into awf_url_auth_map values ('/rest/process/{id}/data', 'get', 'process.create');
insert into awf_url_auth_map values ('/rest/process/{id}/data', 'get', 'process.update');
insert into awf_url_auth_map values ('/rest/process/{id}/data', 'put', 'process.update');
insert into awf_url_auth_map values ('/rest/process/{id}', 'delete', 'process.delete');
insert into awf_url_auth_map values ('/rest/process/{id}/simulation', 'put', 'process.create');
insert into awf_url_auth_map values ('/rest/processes/all', 'get', 'process.read');
insert into awf_url_auth_map values ('/rest/processes/{id}/data', 'get', 'process.read');
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
insert into awf_url_auth_map values ('/rest/schedulers', 'post', 'scheduler.create');
insert into awf_url_auth_map values ('/rest/schedulers/{id}', 'delete', 'scheduler.delete');
insert into awf_url_auth_map values ('/rest/schedulers/{id}', 'put', 'scheduler.update');
insert into awf_url_auth_map values ('/rest/schedulers/{id}/execute', 'post', 'scheduler.execute');
insert into awf_url_auth_map values ('/rest/tokens/data', 'post', 'token.create');
insert into awf_url_auth_map values ('/rest/tokens/{id}/data', 'get', 'token.create');
insert into awf_url_auth_map values ('/rest/tokens/{id}/data', 'put', 'token.create');
insert into awf_url_auth_map values ('/rest/users', 'post', 'user.create');
insert into awf_url_auth_map values ('/rest/users/all', 'get', 'user.read');
insert into awf_url_auth_map values ('/rest/users/{userkey}/all', 'put', 'user.update');
insert into awf_url_auth_map values ('/rest/users/{userkey}/all', 'put', 'user.read');
insert into awf_url_auth_map values ('/rest/users/{userkey}/info', 'put', 'user.read');
insert into awf_url_auth_map values ('/rest/users/{userkey}/info', 'put', 'user.update');
insert into awf_url_auth_map values ('/rest/users/{userkey}/resetpassword', 'put', 'user.read');
insert into awf_url_auth_map values ('/rest/users/{userkey}/resetpassword', 'put', 'user.update');
insert into awf_url_auth_map values ('/roles/edit', 'get', 'role.update');
insert into awf_url_auth_map values ('/roles/edit', 'get', 'role.read');
insert into awf_url_auth_map values ('/roles/edit', 'get', 'role.create');
insert into awf_url_auth_map values ('/roles/edit', 'get', 'role.delete');
insert into awf_url_auth_map values ('/roles', 'get', 'role.read');
insert into awf_url_auth_map values ('/schedulers', 'get', 'scheduler.create');
insert into awf_url_auth_map values ('/schedulers', 'get', 'scheduler.delete');
insert into awf_url_auth_map values ('/schedulers', 'get', 'scheduler.read');
insert into awf_url_auth_map values ('/schedulers', 'get', 'scheduler.update');
insert into awf_url_auth_map values ('/schedulers/new', 'get', 'scheduler.create');
insert into awf_url_auth_map values ('/schedulers/search', 'get', 'scheduler.create');
insert into awf_url_auth_map values ('/schedulers/search', 'get', 'scheduler.delete');
insert into awf_url_auth_map values ('/schedulers/search', 'get', 'scheduler.read');
insert into awf_url_auth_map values ('/schedulers/search', 'get', 'scheduler.update');
insert into awf_url_auth_map values ('/schedulers/{id}/edit', 'get', 'scheduler.delete');
insert into awf_url_auth_map values ('/schedulers/{id}/edit', 'get', 'scheduler.update');
insert into awf_url_auth_map values ('/schedulers/{id}/history', 'get', 'scheduler.read');
insert into awf_url_auth_map values ('/schedulers/{id}/view', 'get', 'scheduler.read');
insert into awf_url_auth_map values ('/tokens', 'get', 'token.create');
insert into awf_url_auth_map values ('/tokens', 'get', 'token.read');
insert into awf_url_auth_map values ('/tokens/search', 'get', 'token.read');
insert into awf_url_auth_map values ('/tokens/search', 'get', 'token.create');
insert into awf_url_auth_map values ('/tokens/view-pop/documents', 'get', 'token.create');
insert into awf_url_auth_map values ('/tokens/view-pop/documents', 'get', 'token.read');
insert into awf_url_auth_map values ('/tokens/{id}/edit', 'get', 'token.create');
insert into awf_url_auth_map values ('/tokens/{id}/edit-tab', 'get', 'token.create');
insert into awf_url_auth_map values ('/tokens/{id}/view', 'get', 'token.read');
insert into awf_url_auth_map values ('/tokens/{id}/view-tab', 'get', 'token.read');
insert into awf_url_auth_map values ('/tokens/{id}/print', 'get', 'token.read');
insert into awf_url_auth_map values ('/tokens/{id}/view-pop', 'get', 'token.read');
insert into awf_url_auth_map values ('/tokens/{id}/view-pop', 'get', 'token.create');
insert into awf_url_auth_map values ('/users', 'get', 'user.update');
insert into awf_url_auth_map values ('/users', 'get', 'user.delete');
insert into awf_url_auth_map values ('/users', 'get', 'user.read');
insert into awf_url_auth_map values ('/users', 'get', 'user.create');
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
insert into awf_url_auth_map values ('/users/{userkey}/editself', 'get', 'user.read');
insert into awf_url_auth_map values ('/users/{userkey}/editself', 'get', 'user.update');
