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

insert into awf_url_auth_map values ('/boards', 'get', 'system.manage');
insert into awf_url_auth_map values ('/boards', 'get', 'portal.manage');
insert into awf_url_auth_map values ('/boards/new', 'get', 'system.manage');
insert into awf_url_auth_map values ('/boards/new', 'get', 'portal.manage');
insert into awf_url_auth_map values ('/boards/search', 'get', 'system.manage');
insert into awf_url_auth_map values ('/boards/search', 'get', 'portal.manage');
insert into awf_url_auth_map values ('/boards/{id}/edit', 'get', 'system.manage');
insert into awf_url_auth_map values ('/boards/{id}/edit', 'get', 'portal.manage');
insert into awf_url_auth_map values ('/boards/{id}/view', 'get', 'system.manage');
insert into awf_url_auth_map values ('/boards/{id}/view', 'get', 'portal.manage');
insert into awf_url_auth_map values ('/boards/articles', 'get', 'general');
insert into awf_url_auth_map values ('/boards/articles', 'get', 'portal.manage');
insert into awf_url_auth_map values ('/boards/articles/search', 'get', 'general');
insert into awf_url_auth_map values ('/boards/articles/search', 'get', 'portal.manage');
insert into awf_url_auth_map values ('/boards/articles/search/param', 'get', 'general');
insert into awf_url_auth_map values ('/boards/articles/search/param', 'get', 'portal.manage');
insert into awf_url_auth_map values ('/boards/articles/{id}/comments', 'get', 'general');
insert into awf_url_auth_map values ('/boards/articles/{id}/comments', 'get', 'portal.manage');
insert into awf_url_auth_map values ('/boards/articles/{id}/edit', 'get', 'general');
insert into awf_url_auth_map values ('/boards/articles/{id}/edit', 'get', 'portal.manage');
insert into awf_url_auth_map values ('/boards/articles/{id}/new', 'get', 'general');
insert into awf_url_auth_map values ('/boards/articles/{id}/new', 'get', 'portal.manage');
insert into awf_url_auth_map values ('/boards/articles/{id}/reply/edit', 'get', 'general');
insert into awf_url_auth_map values ('/boards/articles/{id}/reply/edit', 'get', 'portal.manage');
insert into awf_url_auth_map values ('/boards/articles/{id}/view', 'get', 'general');
insert into awf_url_auth_map values ('/boards/articles/{id}/view', 'get', 'portal.manage');
insert into awf_url_auth_map values ('/cmdb/attributes', 'get', 'cmdb.manage');
insert into awf_url_auth_map values ('/cmdb/attributes', 'get', 'cmdb.view');
insert into awf_url_auth_map values ('/cmdb/attributes/list-modal', 'get', 'cmdb.manage');
insert into awf_url_auth_map values ('/cmdb/attributes/list-modal', 'get', 'cmdb.view');
insert into awf_url_auth_map values ('/cmdb/attributes/new', 'get', 'cmdb.manage');
insert into awf_url_auth_map values ('/cmdb/attributes/search', 'get', 'cmdb.manage');
insert into awf_url_auth_map values ('/cmdb/attributes/search', 'get', 'cmdb.view');
insert into awf_url_auth_map values ('/cmdb/attributes/{id}/edit', 'get', 'cmdb.manage');
insert into awf_url_auth_map values ('/cmdb/attributes/{id}/view', 'get', 'cmdb.manage');
insert into awf_url_auth_map values ('/cmdb/attributes/{id}/view', 'get', 'cmdb.view');
insert into awf_url_auth_map values ('/cmdb/cis', 'post', 'cmdb.manage');
insert into awf_url_auth_map values ('/cmdb/cis', 'post', 'cmdb.view');
insert into awf_url_auth_map values ('/cmdb/cis/search', 'get', 'cmdb.manage');
insert into awf_url_auth_map values ('/cmdb/cis/search', 'get', 'cmdb.view');
insert into awf_url_auth_map values ('/cmdb/cis/{id}/view', 'get', 'cmdb.manage');
insert into awf_url_auth_map values ('/cmdb/cis/{id}/view', 'get', 'cmdb.view');
insert into awf_url_auth_map values ('/cmdb/class/edit', 'get', 'cmdb.manage');
insert into awf_url_auth_map values ('/cmdb/class/view-pop/attributes', 'get', 'cmdb.manage');
insert into awf_url_auth_map values ('/cmdb/class/view-pop/attributes', 'get', 'cmdb.view');
insert into awf_url_auth_map values ('/cmdb/icons', 'get', 'cmdb.manage');
insert into awf_url_auth_map values ('/cmdb/icons', 'get', 'cmdb.view');
insert into awf_url_auth_map values ('/cmdb/types/edit', 'get', 'cmdb.manage');
insert into awf_url_auth_map values ('/codes/edit', 'get', 'system.manage');
insert into awf_url_auth_map values ('/custom-codes', 'get', 'system.manage');
insert into awf_url_auth_map values ('/custom-codes/new', 'get', 'system.manage');
insert into awf_url_auth_map values ('/custom-codes/search', 'get', 'system.manage');
insert into awf_url_auth_map values ('/custom-codes/{id}/edit', 'get', 'system.manage');
insert into awf_url_auth_map values ('/custom-codes/{id}/view', 'get', 'system.manage');
insert into awf_url_auth_map values ('/dashboard/view', 'get', 'general');
insert into awf_url_auth_map values ('/documents/{id}/edit', 'get', 'general');
insert into awf_url_auth_map values ('/documents/{id}/print', 'get', 'general');
insert into awf_url_auth_map values ('/archives', 'get', 'general');
insert into awf_url_auth_map values ('/archives', 'get', 'portal.manage');
insert into awf_url_auth_map values ('/archives/new', 'get', 'portal.manage');
insert into awf_url_auth_map values ('/archives/search', 'get', 'general');
insert into awf_url_auth_map values ('/archives/search', 'get', 'portal.manage');
insert into awf_url_auth_map values ('/archives/{id}/edit', 'get', 'portal.manage');
insert into awf_url_auth_map values ('/archives/{id}/view', 'get', 'general');
insert into awf_url_auth_map values ('/archives/{id}/view', 'get', 'portal.manage');
insert into awf_url_auth_map values ('/faqs', 'get', 'general');
insert into awf_url_auth_map values ('/faqs', 'get', 'portal.manage');
insert into awf_url_auth_map values ('/faqs/new', 'get', 'portal.manage');
insert into awf_url_auth_map values ('/faqs/search', 'get', 'general');
insert into awf_url_auth_map values ('/faqs/search', 'get', 'portal.manage');
insert into awf_url_auth_map values ('/faqs/{id}/edit', 'get', 'portal.manage');
insert into awf_url_auth_map values ('/faqs/{id}/view', 'get', 'general');
insert into awf_url_auth_map values ('/faqs/{id}/view', 'get', 'portal.manage');
insert into awf_url_auth_map values ('/forms', 'get', 'workflow.manage');
insert into awf_url_auth_map values ('/forms/search', 'get', 'workflow.manage');
insert into awf_url_auth_map values ('/forms/{id}/edit', 'get', 'workflow.manage');
insert into awf_url_auth_map values ('/forms/{id}/preview', 'get', 'workflow.manage');
insert into awf_url_auth_map values ('/forms/{id}/view', 'get', 'workflow.manage');
insert into awf_url_auth_map values ('/resources', 'get', 'workflow.manage');
insert into awf_url_auth_map values ('/notices', 'get', 'general');
insert into awf_url_auth_map values ('/notices', 'get', 'portal.manage');
insert into awf_url_auth_map values ('/notices/new', 'get', 'portal.manage');
insert into awf_url_auth_map values ('/notices/search', 'get', 'general');
insert into awf_url_auth_map values ('/notices/search', 'get', 'portal.manage');
insert into awf_url_auth_map values ('/notices/{id}/edit', 'get', 'portal.manage');
insert into awf_url_auth_map values ('/notices/{id}/view', 'get', 'general');
insert into awf_url_auth_map values ('/notices/{id}/view', 'get', 'portal.manage');
insert into awf_url_auth_map values ('/numberingPatterns', 'get', 'workflow.manage');
insert into awf_url_auth_map values ('/numberingPatterns/new', 'get', 'workflow.manage');
insert into awf_url_auth_map values ('/numberingPatterns/search', 'get', 'workflow.manage');
insert into awf_url_auth_map values ('/numberingPatterns/{id}/edit', 'get', 'workflow.manage');
insert into awf_url_auth_map values ('/numberingPatterns/{id}/view', 'get', 'workflow.manage');
insert into awf_url_auth_map values ('/numberingRules', 'get', 'workflow.manage');
insert into awf_url_auth_map values ('/numberingRules/new', 'get', 'workflow.manage');
insert into awf_url_auth_map values ('/numberingRules/search', 'get', 'workflow.manage');
insert into awf_url_auth_map values ('/numberingRules/{id}/edit', 'get', 'workflow.manage');
insert into awf_url_auth_map values ('/numberingRules/{id}/view', 'get', 'workflow.manage');
insert into awf_url_auth_map values ('/oauth/{service}/login', 'get', 'workflow.manage');
insert into awf_url_auth_map values ('/organizations/edit', 'get', 'workflow.manage');
insert into awf_url_auth_map values ('/process/{id}/edit', 'get', 'workflow.manage');
insert into awf_url_auth_map values ('/process/{id}/view', 'get', 'workflow.manage');
insert into awf_url_auth_map values ('/processes', 'get', 'workflow.manage');
insert into awf_url_auth_map values ('/processes/search', 'get', 'workflow.manage');
insert into awf_url_auth_map values ('/rest/auths', 'get', 'system.manage');
insert into awf_url_auth_map values ('/rest/auths', 'post', 'system.manage');
insert into awf_url_auth_map values ('/rest/auths/{id}', 'get', 'system.manage');
insert into awf_url_auth_map values ('/rest/auths/{id}', 'put', 'system.manage');
insert into awf_url_auth_map values ('/rest/auths/{id}', 'delete', 'system.manage');
insert into awf_url_auth_map values ('/rest/boards', 'put', 'system.manage');
insert into awf_url_auth_map values ('/rest/boards', 'put', 'portal.manage');
insert into awf_url_auth_map values ('/rest/boards', 'post', 'system.manage');
insert into awf_url_auth_map values ('/rest/boards', 'post', 'portal.manage');
insert into awf_url_auth_map values ('/rest/boards/{id}/view', 'get', 'system.manage');
insert into awf_url_auth_map values ('/rest/boards/{id}/view', 'get', 'portal.manage');
insert into awf_url_auth_map values ('/rest/boards/{id}', 'delete', 'system.manage');
insert into awf_url_auth_map values ('/rest/boards/{id}', 'delete', 'portal.manage');
insert into awf_url_auth_map values ('/rest/boards/articles', 'put', 'general');
insert into awf_url_auth_map values ('/rest/boards/articles', 'put', 'portal.manage');
insert into awf_url_auth_map values ('/rest/boards/articles', 'post', 'general');
insert into awf_url_auth_map values ('/rest/boards/articles', 'post', 'portal.manage');
insert into awf_url_auth_map values ('/rest/boards/articles/comments', 'put', 'general');
insert into awf_url_auth_map values ('/rest/boards/articles/comments', 'put', 'portal.manage');
insert into awf_url_auth_map values ('/rest/boards/articles/comments', 'post', 'general');
insert into awf_url_auth_map values ('/rest/boards/articles/comments', 'post', 'portal.manage');
insert into awf_url_auth_map values ('/rest/boards/articles/comments/{id}', 'delete', 'general');
insert into awf_url_auth_map values ('/rest/boards/articles/comments/{id}', 'delete', 'portal.manage');
insert into awf_url_auth_map values ('/rest/boards/articles/reply', 'post', 'general');
insert into awf_url_auth_map values ('/rest/boards/articles/reply', 'post', 'portal.manage');
insert into awf_url_auth_map values ('/rest/boards/articles/{id}', 'delete', 'general');
insert into awf_url_auth_map values ('/rest/boards/articles/{id}', 'delete', 'portal.manage');
insert into awf_url_auth_map values ('/rest/calendars', 'post', 'general');
insert into awf_url_auth_map values ('/rest/calendars/excel', 'post', 'general');
insert into awf_url_auth_map values ('/rest/calendars/template', 'get', 'general');
insert into awf_url_auth_map values ('/rest/calendars/{id}/templateUpload', 'post', 'general');
insert into awf_url_auth_map values ('/rest/calendars/{id}/repeat', 'post', 'general');
insert into awf_url_auth_map values ('/rest/calendars/{id}/repeat', 'put', 'general');
insert into awf_url_auth_map values ('/rest/calendars/{id}/repeat', 'delete', 'general');
insert into awf_url_auth_map values ('/rest/calendars/{id}/schedule', 'post', 'general');
insert into awf_url_auth_map values ('/rest/calendars/{id}/schedule', 'put', 'general');
insert into awf_url_auth_map values ('/rest/calendars/{id}/schedule', 'delete', 'general');
insert into awf_url_auth_map values ('/rest/cmdb/attributes', 'post', 'cmdb.manage');
insert into awf_url_auth_map values ('/rest/cmdb/attributes/{id}', 'put', 'cmdb.manage');
insert into awf_url_auth_map values ('/rest/cmdb/attributes/{id}', 'delete', 'cmdb.manage');
insert into awf_url_auth_map values ('/rest/cmdb/cis/excel', 'post', 'cmdb.manage');
insert into awf_url_auth_map values ('/rest/cmdb/cis/excel', 'post', 'cmdb.view');
insert into awf_url_auth_map values ('/rest/cmdb/cis/template', 'get', 'cmdb.manage');
insert into awf_url_auth_map values ('/rest/cmdb/cis/template', 'get', 'cmdb.view');
insert into awf_url_auth_map values ('/rest/cmdb/cis/templateUpload', 'post', 'cmdb.manage');
insert into awf_url_auth_map values ('/rest/cmdb/cis/templateUpload', 'post', 'cmdb.view');
insert into awf_url_auth_map values ('/rest/cmdb/classes', 'get', 'cmdb.manage');
insert into awf_url_auth_map values ('/rest/cmdb/classes', 'get', 'cmdb.view');
insert into awf_url_auth_map values ('/rest/cmdb/classes', 'post', 'cmdb.manage');
insert into awf_url_auth_map values ('/rest/cmdb/classes/{id}', 'get', 'cmdb.manage');
insert into awf_url_auth_map values ('/rest/cmdb/classes/{id}', 'get', 'cmdb.view');
insert into awf_url_auth_map values ('/rest/cmdb/classes/{id}', 'put', 'cmdb.manage');
insert into awf_url_auth_map values ('/rest/cmdb/classes/{id}', 'delete', 'cmdb.manage');
insert into awf_url_auth_map values ('/rest/cmdb/icons', 'post', 'cmdb.manage');
insert into awf_url_auth_map values ('/rest/cmdb/icons', 'put', 'cmdb.manage');
insert into awf_url_auth_map values ('/rest/cmdb/icons/{id}', 'delete', 'cmdb.manage');
insert into awf_url_auth_map values ('/rest/cmdb/types', 'get', 'cmdb.manage');
insert into awf_url_auth_map values ('/rest/cmdb/types', 'get', 'cmdb.view');
insert into awf_url_auth_map values ('/rest/cmdb/types', 'post', 'cmdb.manage');
insert into awf_url_auth_map values ('/rest/cmdb/types/{id}', 'get', 'cmdb.manage');
insert into awf_url_auth_map values ('/rest/cmdb/types/{id}', 'get', 'cmdb.view');
insert into awf_url_auth_map values ('/rest/cmdb/types/{id}', 'put', 'cmdb.manage');
insert into awf_url_auth_map values ('/rest/cmdb/types/{id}', 'delete', 'cmdb.manage');
insert into awf_url_auth_map values ('/rest/codes', 'get', 'system.manage');
insert into awf_url_auth_map values ('/rest/codes', 'post', 'system.manage');
insert into awf_url_auth_map values ('/rest/codes/{id}', 'get', 'system.manage');
insert into awf_url_auth_map values ('/rest/codes/{id}', 'put', 'system.manage');
insert into awf_url_auth_map values ('/rest/codes/{id}', 'delete', 'system.manage');
insert into awf_url_auth_map values ('/rest/codes/excel', 'get', 'system.manage');
insert into awf_url_auth_map values ('/rest/custom-codes', 'post', 'system.manage');
insert into awf_url_auth_map values ('/rest/custom-codes', 'put', 'system.manage');
insert into awf_url_auth_map values ('/rest/custom-codes/{id}', 'get', 'system.manage');
insert into awf_url_auth_map values ('/rest/custom-codes/{id}', 'delete', 'system.manage');
insert into awf_url_auth_map values ('/rest/documents', 'get', 'general');
insert into awf_url_auth_map values ('/rest/documents/{id}/data', 'get', 'general');
insert into awf_url_auth_map values ('/rest/documents/components/{id}/value', 'get', 'general');
insert into awf_url_auth_map values ('/rest/archives', 'post', 'portal.manage');
insert into awf_url_auth_map values ('/rest/archives', 'put', 'portal.manage');
insert into awf_url_auth_map values ('/rest/archives/{id}', 'delete', 'portal.manage');
insert into awf_url_auth_map values ('/rest/faqs', 'post', 'portal.manage');
insert into awf_url_auth_map values ('/rest/faqs/{id}', 'get', 'general');
insert into awf_url_auth_map values ('/rest/faqs/{id}', 'get', 'portal.manage');
insert into awf_url_auth_map values ('/rest/faqs/{id}', 'put', 'portal.manage');
insert into awf_url_auth_map values ('/rest/faqs/{id}', 'delete', 'portal.manage');
insert into awf_url_auth_map values ('/rest/forms', 'post', 'general');
insert into awf_url_auth_map values ('/rest/forms', 'post', 'portal.manage');
insert into awf_url_auth_map values ('/rest/forms/{id}', 'put', 'workflow.manage');
insert into awf_url_auth_map values ('/rest/forms/{id}', 'get', 'workflow.manage');
insert into awf_url_auth_map values ('/rest/forms/{id}', 'delete', 'workflow.manage');
insert into awf_url_auth_map values ('/rest/forms/{id}/data', 'get', 'workflow.manage');
insert into awf_url_auth_map values ('/rest/forms/{id}/data', 'put', 'workflow.manage');
insert into awf_url_auth_map values ('/rest/files', 'put', 'workflow.manage');
insert into awf_url_auth_map values ('/rest/files', 'post', 'workflow.manage');
insert into awf_url_auth_map values ('/rest/files/{id}', 'delete', 'workflow.manage');
insert into awf_url_auth_map values ('/rest/instances/{id}/schedule', 'get', 'general');
insert into awf_url_auth_map values ('/rest/instances/{id}/schedule', 'post', 'general');
insert into awf_url_auth_map values ('/rest/instances/{id}/schedule/{id}', 'delete', 'general');
insert into awf_url_auth_map values ('/rest/instances/{id}/viewer/', 'get', 'general');
insert into awf_url_auth_map values ('/rest/instances/{id}/viewer/', 'post', 'general');
insert into awf_url_auth_map values ('/rest/instances/{id}/viewer/{userkey}', 'delete', 'general');
insert into awf_url_auth_map values ('/rest/instances/{id}/viewer/{userkey}/read', 'post', 'general');
insert into awf_url_auth_map values ('/rest/notices', 'post', 'portal.manage');
insert into awf_url_auth_map values ('/rest/notices/{id}', 'delete', 'portal.manage');
insert into awf_url_auth_map values ('/rest/notices/{id}', 'put', 'portal.manage');
insert into awf_url_auth_map values ('/rest/numberingPatterns', 'get', 'workflow.manage');
insert into awf_url_auth_map values ('/rest/numberingPatterns', 'post', 'workflow.manage');
insert into awf_url_auth_map values ('/rest/numberingPatterns/{id}', 'get', 'workflow.manage');
insert into awf_url_auth_map values ('/rest/numberingPatterns/{id}', 'put', 'workflow.manage');
insert into awf_url_auth_map values ('/rest/numberingPatterns/{id}', 'delete', 'workflow.manage');
insert into awf_url_auth_map values ('/rest/numberingRules', 'get', 'workflow.manage');
insert into awf_url_auth_map values ('/rest/numberingRules', 'post', 'workflow.manage');
insert into awf_url_auth_map values ('/rest/numberingRules/{id}', 'get', 'workflow.manage');
insert into awf_url_auth_map values ('/rest/numberingRules/{id}', 'put', 'workflow.manage');
insert into awf_url_auth_map values ('/rest/numberingRules/{id}', 'delete', 'workflow.manage');
insert into awf_url_auth_map values ('/rest/organizations', 'get', 'workflow.manage');
insert into awf_url_auth_map values ('/rest/organizations/{id}', 'get', 'workflow.manage');
insert into awf_url_auth_map values ('/rest/organizations/{id}', 'put', 'workflow.manage');
insert into awf_url_auth_map values ('/rest/organizations/{id}', 'delete', 'workflow.manage');
insert into awf_url_auth_map values ('/rest/organizations', 'post', 'workflow.manage');
insert into awf_url_auth_map values ('/rest/plugins', 'get', 'general');
insert into awf_url_auth_map values ('/rest/plugins/{id}', 'post', 'general');
insert into awf_url_auth_map values ('/rest/process/{id}', 'delete', 'workflow.manage');
insert into awf_url_auth_map values ('/rest/process/{id}/data', 'get', 'workflow.manage');
insert into awf_url_auth_map values ('/rest/process/{id}/data', 'put', 'workflow.manage');
insert into awf_url_auth_map values ('/rest/processes', 'post', 'workflow.manage');
insert into awf_url_auth_map values ('/rest/process/{id}/simulation', 'put', 'workflow.manage');
insert into awf_url_auth_map values ('/rest/processes/all', 'get', 'workflow.manage');
insert into awf_url_auth_map values ('/rest/processes/{id}', 'put', 'workflow.manage');
insert into awf_url_auth_map values ('/rest/processes/{id}/data', 'get', 'workflow.manage');
insert into awf_url_auth_map values ('/rest/products/info', 'get', 'general');
insert into awf_url_auth_map values ('/rest/roles', 'get', 'system.manage');
insert into awf_url_auth_map values ('/rest/roles', 'post', 'system.manage');
insert into awf_url_auth_map values ('/rest/roles/excel', 'get', 'system.manage');
insert into awf_url_auth_map values ('/rest/roles/{id}', 'get', 'system.manage');
insert into awf_url_auth_map values ('/rest/roles/{id}', 'put', 'system.manage');
insert into awf_url_auth_map values ('/rest/roles/{id}', 'delete', 'system.manage');
insert into awf_url_auth_map values ('/rest/schedulers', 'post', 'system.manage');
insert into awf_url_auth_map values ('/rest/schedulers/{id}', 'put', 'system.manage');
insert into awf_url_auth_map values ('/rest/schedulers/{id}', 'delete', 'system.manage');
insert into awf_url_auth_map values ('/rest/schedulers/{id}/execute', 'post', 'system.manage');
insert into awf_url_auth_map values ('/rest/sla/metrics', 'get', 'sla.manage');
insert into awf_url_auth_map values ('/rest/sla/metrics', 'post', 'sla.manage');
insert into awf_url_auth_map values ('/rest/sla/metrics', 'put', 'sla.manage');
insert into awf_url_auth_map values ('/rest/sla/metrics/{id}/{year}', 'delete', 'sla.manage');
insert into awf_url_auth_map values ('/rest/sla/metrics/copy', 'post', 'sla.manage');
insert into awf_url_auth_map values ('/rest/sla/metrics/annual/excel', 'get', 'sla.manage');
insert into awf_url_auth_map values ('/rest/sla/metrics/annual/excel', 'get', 'sla.view');
insert into awf_url_auth_map values ('/rest/sla/metrics/{id}/preview', 'get', 'sla.manage');
insert into awf_url_auth_map values ('/rest/sla/metrics/{id}/preview', 'get', 'sla.view');
insert into awf_url_auth_map values ('/rest/sla/metric-manuals', 'post', 'sla.manage');
insert into awf_url_auth_map values ('/rest/sla/metric-manuals/{id}', 'delete', 'sla.manage');
insert into awf_url_auth_map values ('/rest/sla/metric-pools', 'post', 'sla.manage');
insert into awf_url_auth_map values ('/rest/sla/metric-pools/{id}', 'put', 'sla.manage');
insert into awf_url_auth_map values ('/rest/sla/metric-pools/{id}', 'delete', 'sla.manage');
insert into awf_url_auth_map values ('/rest/sla/metric-status', 'get', 'sla.manage');
insert into awf_url_auth_map values ('/rest/sla/metric-status', 'get', 'sla.view');
insert into awf_url_auth_map values ('/rest/sla/metric-status/list', 'get', 'sla.manage');
insert into awf_url_auth_map values ('/rest/sla/metric-status/list', 'get', 'sla.view');
insert into awf_url_auth_map values ('/rest/statistics/customChart', 'post', 'report.manage');
insert into awf_url_auth_map values ('/rest/statistics/customChart/{id}', 'get', 'report.manage');
insert into awf_url_auth_map values ('/rest/statistics/customChart/{id}', 'get', 'report.view');
insert into awf_url_auth_map values ('/rest/statistics/customChart/{id}', 'put', 'report.manage');
insert into awf_url_auth_map values ('/rest/statistics/customChart/{id}', 'delete', 'report.manage');
insert into awf_url_auth_map values ('/rest/statistics/customChart/{id}/preview', 'post', 'report.manage');
insert into awf_url_auth_map values ('/rest/statistics/customChart/{id}/preview', 'post', 'report.view');
insert into awf_url_auth_map values ('/rest/statistics/customReportTemplate', 'post', 'report.manage');
insert into awf_url_auth_map values ('/rest/statistics/customReportTemplate/charts', 'get', 'report.manage');
insert into awf_url_auth_map values ('/rest/statistics/customReportTemplate/charts', 'get', 'report.view');
insert into awf_url_auth_map values ('/rest/statistics/customReportTemplate/{id}', 'put', 'report.manage');
insert into awf_url_auth_map values ('/rest/statistics/customReportTemplate/{id}', 'post', 'report.manage');
insert into awf_url_auth_map values ('/rest/statistics/customReportTemplate/{id}', 'delete', 'report.manage');
insert into awf_url_auth_map values ('/rest/tokens/data', 'post', 'general');
insert into awf_url_auth_map values ('/rest/tokens/{id}/data', 'get', 'general');
insert into awf_url_auth_map values ('/rest/tokens/{id}/data', 'put', 'general');
insert into awf_url_auth_map values ('/rest/tokens/excel', 'get', 'general');
insert into awf_url_auth_map values ('/rest/users', 'post', 'system.manage');
insert into awf_url_auth_map values ('/rest/users/all', 'get', 'system.manage');
insert into awf_url_auth_map values ('/rest/users/{userkey}/all', 'put', 'system.manage');
insert into awf_url_auth_map values ('/rest/users/{userkey}/resetpassword', 'put', 'system.manage');
insert into awf_url_auth_map values ('/rest/users/excel', 'get', 'system.manage');
insert into awf_url_auth_map values ('/rest/workflows', 'post', 'workflow.manage');
insert into awf_url_auth_map values ('/rest/workflows/{id}', 'get', 'workflow.manage');
insert into awf_url_auth_map values ('/rest/workflows/{id}', 'put', 'workflow.manage');
insert into awf_url_auth_map values ('/rest/workflows/{id}', 'delete', 'workflow.manage');
insert into awf_url_auth_map values ('/rest/workflows/{id}/display', 'put', 'workflow.manage');
insert into awf_url_auth_map values ('/rest/workflows/{id}/export', 'get', 'workflow.manage');
insert into awf_url_auth_map values ('/rest/workflows/import', 'post', 'workflow.manage');
insert into awf_url_auth_map values ('/roles', 'get', 'system.manage');
insert into awf_url_auth_map values ('/roles/new', 'get', 'system.manage');
insert into awf_url_auth_map values ('/roles/search', 'get', 'system.manage');
insert into awf_url_auth_map values ('/roles/{id}/edit', 'get', 'system.manage');
insert into awf_url_auth_map values ('/roles/{id}/view', 'get', 'system.manage');
insert into awf_url_auth_map values ('/schedulers', 'get', 'system.manage');
insert into awf_url_auth_map values ('/schedulers/new', 'get', 'system.manage');
insert into awf_url_auth_map values ('/schedulers/search', 'get', 'system.manage');
insert into awf_url_auth_map values ('/schedulers/{id}/edit', 'get', 'system.manage');
insert into awf_url_auth_map values ('/schedulers/{id}/history', 'get', 'system.manage');
insert into awf_url_auth_map values ('/schedulers/{id}/view', 'get', 'system.manage');
insert into awf_url_auth_map values ('/sla/metrics', 'get', 'sla.manage');
insert into awf_url_auth_map values ('/sla/metrics', 'get', 'sla.view');
insert into awf_url_auth_map values ('/sla/metrics/search', 'get', 'sla.manage');
insert into awf_url_auth_map values ('/sla/metrics/search', 'get', 'sla.view');
insert into awf_url_auth_map values ('/sla/metrics/new', 'get', 'sla.manage');
insert into awf_url_auth_map values ('/sla/metrics/{id}/{year}/edit', 'get', 'sla.manage');
insert into awf_url_auth_map values ('/sla/metrics/{id}/{year}/view', 'get', 'sla.manage');
insert into awf_url_auth_map values ('/sla/metrics/{id}/{year}/view', 'get', 'sla.view');
insert into awf_url_auth_map values ('/sla/metrics/copy', 'get', 'sla.manage');
insert into awf_url_auth_map values ('/sla/metrics/copy', 'get', 'sla.view');
insert into awf_url_auth_map values ('/sla/metrics/annual/search', 'get', 'sla.manage');
insert into awf_url_auth_map values ('/sla/metrics/annual/search', 'get', 'sla.view');
insert into awf_url_auth_map values ('/sla/metrics/annual', 'get', 'sla.manage');
insert into awf_url_auth_map values ('/sla/metrics/annual', 'get', 'sla.view');
insert into awf_url_auth_map values ('/sla/metric-manuals/search', 'get', 'sla.manage');
insert into awf_url_auth_map values ('/sla/metric-manuals/search', 'get', 'sla.view');
insert into awf_url_auth_map values ('/sla/metric-manuals', 'get', 'sla.manage');
insert into awf_url_auth_map values ('/sla/metric-manuals', 'get', 'sla.view');
insert into awf_url_auth_map values ('/sla/metric-manuals/new', 'get', 'sla.manage');
insert into awf_url_auth_map values ('/sla/metric-pools/search', 'get', 'sla.manage');
insert into awf_url_auth_map values ('/sla/metric-pools/search', 'get', 'sla.view');
insert into awf_url_auth_map values ('/sla/metric-pools', 'get', 'sla.manage');
insert into awf_url_auth_map values ('/sla/metric-pools', 'get', 'sla.view');
insert into awf_url_auth_map values ('/sla/metric-pools/new', 'get', 'sla.manage');
insert into awf_url_auth_map values ('/sla/metric-pools/{id}/edit', 'get', 'sla.manage');
insert into awf_url_auth_map values ('/sla/metric-pools/{id}/view', 'get', 'sla.manage');
insert into awf_url_auth_map values ('/sla/metric-pools/{id}/view', 'get', 'sla.view');
insert into awf_url_auth_map values ('/sla/metric-status', 'get', 'sla.manage');
insert into awf_url_auth_map values ('/sla/metric-status', 'get', 'sla.view');
insert into awf_url_auth_map values ('/sla/metric-status/search', 'get', 'sla.manage');
insert into awf_url_auth_map values ('/sla/metric-status/search', 'get', 'sla.view');
insert into awf_url_auth_map values ('/statistics/basicChart/search', 'get', 'report.view');
insert into awf_url_auth_map values ('/statistics/basicReport/search', 'get', 'report.view');
insert into awf_url_auth_map values ('/statistics/customChart', 'get', 'report.manage');
insert into awf_url_auth_map values ('/statistics/customChart', 'get', 'report.view');
insert into awf_url_auth_map values ('/statistics/customChart/new', 'get', 'report.manage');
insert into awf_url_auth_map values ('/statistics/customChart/search', 'get', 'report.manage');
insert into awf_url_auth_map values ('/statistics/customChart/search', 'get', 'report.view');
insert into awf_url_auth_map values ('/statistics/customChart/{id}/edit', 'get', 'report.manage');
insert into awf_url_auth_map values ('/statistics/customChart/{id}/view', 'get', 'report.manage');
insert into awf_url_auth_map values ('/statistics/customChart/{id}/view', 'get', 'report.view');
insert into awf_url_auth_map values ('/statistics/customDashboardTemplate/edit', 'get', 'report.view');
insert into awf_url_auth_map values ('/statistics/customReport', 'get', 'report.manage');
insert into awf_url_auth_map values ('/statistics/customReport', 'get', 'report.view');
insert into awf_url_auth_map values ('/statistics/customReport/search', 'get', 'report.manage');
insert into awf_url_auth_map values ('/statistics/customReport/search', 'get', 'report.view');
insert into awf_url_auth_map values ('/statistics/customReport/{id}/view', 'get', 'report.manage');
insert into awf_url_auth_map values ('/statistics/customReport/{id}/view', 'get', 'report.view');
insert into awf_url_auth_map values ('/statistics/customReportTemplate', 'get', 'report.manage');
insert into awf_url_auth_map values ('/statistics/customReportTemplate', 'get', 'report.view');
insert into awf_url_auth_map values ('/statistics/customReportTemplate/new', 'get', 'report.manage');
insert into awf_url_auth_map values ('/statistics/customReportTemplate/preview', 'get', 'report.manage');
insert into awf_url_auth_map values ('/statistics/customReportTemplate/preview', 'get', 'report.view');
insert into awf_url_auth_map values ('/statistics/customReportTemplate/search', 'get', 'report.manage');
insert into awf_url_auth_map values ('/statistics/customReportTemplate/search', 'get', 'report.view');
insert into awf_url_auth_map values ('/statistics/customReportTemplate/{id}/edit', 'get', 'report.manage');
insert into awf_url_auth_map values ('/statistics/customReportTemplate/{id}/view', 'get', 'report.manage');
insert into awf_url_auth_map values ('/statistics/customReportTemplate/{id}/view', 'get', 'report.view');
insert into awf_url_auth_map values ('/statistics/dashboardTemplate/search', 'get', 'report.view');
insert into awf_url_auth_map values ('/tokens/view-pop/documents', 'get', 'general');
insert into awf_url_auth_map values ('/tokens/{id}/edit', 'get', 'general');
insert into awf_url_auth_map values ('/tokens/{id}/print', 'get', 'general');
insert into awf_url_auth_map values ('/tokens/{id}/view', 'get', 'general');
insert into awf_url_auth_map values ('/tokens/{id}/view-pop', 'get', 'general');
insert into awf_url_auth_map values ('/tokens/tokenTab', 'get', 'general');
insert into awf_url_auth_map values ('/users', 'get', 'system.manage');
insert into awf_url_auth_map values ('/users/new', 'get', 'system.manage');
insert into awf_url_auth_map values ('/users/search', 'get', 'system.manage');
insert into awf_url_auth_map values ('/users/{userkey}/edit', 'get', 'system.manage');
insert into awf_url_auth_map values ('/users/{userkey}/editself', 'get', 'system.manage');
insert into awf_url_auth_map values ('/users/{userkey}/view', 'get', 'system.manage');
insert into awf_url_auth_map values ('/workflows', 'get', 'workflow.manage');
insert into awf_url_auth_map values ('/workflows/new', 'get', 'workflow.manage');
insert into awf_url_auth_map values ('/workflows/search', 'get', 'workflow.manage');
insert into awf_url_auth_map values ('/workflows/{id}/display', 'get', 'workflow.manage');
insert into awf_url_auth_map values ('/workflows/{id}/edit', 'get', 'workflow.manage');
insert into awf_url_auth_map values ('/workflows/import', 'get', 'workflow.manage');
insert into awf_url_auth_map values ('/rest/workflows/workflowLink','post','workflow.manage');
insert into awf_url_auth_map values ('/workflows/workflowLink/{id}/edit', 'get', 'workflow.manage');
insert into awf_url_auth_map values ('/rest/workflows/workflowLink/{id}', 'delete', 'workflow.manage');
insert into awf_url_auth_map values ('/rest/workflows/workflowLink/{id}', 'put', 'workflow.manage');
insert into awf_url_auth_map values ('/calendars', 'get', 'general');
