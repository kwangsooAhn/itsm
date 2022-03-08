/**
 * 플러그인 테이블
 */
DROP TABLE IF EXISTS awf_plugin cascade;

create table awf_plugin
(
    plugin_id varchar(100) NOT NULL,
    plugin_name varchar(100) NOT NULL,
    plugin_type varchar(100) NOT NULL,
    plugin_location varchar(1024) NOT NULL,
    plugin_command varchar(2048) NOT NULL,
    CONSTRAINT awf_plugin_pk PRIMARY KEY (plugin_id)
);

COMMENT ON TABLE public.awf_plugin IS '플러그인 마스터';
COMMENT ON COLUMN awf_plugin.plugin_id IS '플러그인 아이디';
COMMENT ON COLUMN awf_plugin.plugin_name IS '플러그인 이름';
COMMENT ON COLUMN awf_plugin.plugin_type IS '플러그인 타입';
COMMENT ON COLUMN awf_plugin.plugin_location IS '플러그인 위치';
COMMENT ON COLUMN awf_plugin.plugin_command IS '플러그인 실행 명령어';

insert into awf_plugin values ('api.focs', '방화벽-FOCS', 'api', '/focs', 'java -jar focs.jar');
