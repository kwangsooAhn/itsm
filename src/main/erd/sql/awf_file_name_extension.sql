/**
 * 파일확장자관리
 */
DROP TABLE IF EXISTS awf_file_name_extension cascade;

CREATE TABLE awf_file_name_extension
(
	file_name_extension varchar(128) NOT NULL,
	create_user_key varchar(128) NOT NULL,
	create_dt timestamp NOT NULL,
	update_user_key varchar(128),
	update_dt timestamp,
	CONSTRAINT awf_file_name_extension_pk PRIMARY KEY (file_name_extension)
);

COMMENT ON TABLE awf_file_name_extension IS '파일확장자관리';
COMMENT ON COLUMN awf_file_name_extension.file_name_extension IS '파일확장자명';
COMMENT ON COLUMN awf_file_name_extension.create_user_key IS '등록자';
COMMENT ON COLUMN awf_file_name_extension.create_dt IS '등록일';
COMMENT ON COLUMN awf_file_name_extension.update_user_key IS '수정자';
COMMENT ON COLUMN awf_file_name_extension.update_dt IS '수정일';

insert into awf_file_name_extension values ('PDF', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_file_name_extension values ('JPG', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_file_name_extension values ('TXT', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_file_name_extension values ('DOC', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_file_name_extension values ('XLS', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_file_name_extension values ('PPTX', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_file_name_extension values ('ZIP', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_file_name_extension values ('HWP', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
insert into awf_file_name_extension values ('PNG', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
