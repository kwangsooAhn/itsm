/**
 * 파일확장자관리
 */
DROP TABLE IF EXISTS awf_file_name_extension cascade;

CREATE TABLE awf_file_name_extension
(
	file_name_extension varchar(128) NOT NULL,
	file_content_type varchar(128) NOT NULL,
	create_user_key varchar(128),
	create_dt timestamp,
	update_user_key varchar(128),
	update_dt timestamp,
	CONSTRAINT awf_file_name_extension_pk PRIMARY KEY (file_name_extension)
);

COMMENT ON TABLE awf_file_name_extension IS '파일확장자관리';
COMMENT ON COLUMN awf_file_name_extension.file_name_extension IS '파일확장자명';
COMMENT ON COLUMN awf_file_name_extension.file_content_type IS '파일컨텐트타입';
COMMENT ON COLUMN awf_file_name_extension.create_user_key IS '등록자';
COMMENT ON COLUMN awf_file_name_extension.create_dt IS '등록일시';
COMMENT ON COLUMN awf_file_name_extension.update_user_key IS '수정자';
COMMENT ON COLUMN awf_file_name_extension.update_dt IS '수정일시';

insert into awf_file_name_extension values ('TXT', 'text/plain', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_file_name_extension values ('PDF', 'application/pdf', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_file_name_extension values ('JPG', 'image/jpeg', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_file_name_extension values ('JPEG', 'image/jpeg', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_file_name_extension values ('PNG', 'image/png', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_file_name_extension values ('DOC', 'application/msword', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_file_name_extension values ('DOCX', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_file_name_extension values ('XLS', 'application/vnd.ms-excel', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_file_name_extension values ('XLSX', 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_file_name_extension values ('PPTX', 'application/vnd.openxmlformats-officedocument.presentationml.presentation', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_file_name_extension values ('ZIP', 'application/zip', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_file_name_extension values ('HWP', 'application/x-tika-msoffice', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
