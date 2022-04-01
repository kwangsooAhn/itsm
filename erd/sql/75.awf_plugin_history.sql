/**
 * 플러그인 이력 테이블
 */
DROP TABLE IF EXISTS awf_plugin_history cascade;

CREATE TABLE awf_plugin_history
(
    history_id varchar(128) NOT NULL,
    plugin_id varchar(100) NOT NULL,
    startdt timestamp NULL,
    enddt timestamp NULL,
    plugin_param text NULL,
    plugin_result varchar(100) NULL,
    message text NULL,
    plugin_data text NULL,
    CONSTRAINT awf_plugin_history_pk PRIMARY KEY (history_id)
);

COMMENT ON TABLE awf_plugin_history IS '플러그인 처리 이력';
COMMENT ON COLUMN awf_plugin_history.history_id IS '이력 아이디';
COMMENT ON COLUMN awf_plugin_history.plugin_id IS '플러그인 아이디';
COMMENT ON COLUMN awf_plugin_history.startdt IS '실행시간';
COMMENT ON COLUMN awf_plugin_history.enddt IS '종료시간';
COMMENT ON COLUMN awf_plugin_history.plugin_param IS '파라미터';
COMMENT ON COLUMN awf_plugin_history.plugin_result IS '결과';
COMMENT ON COLUMN awf_plugin_history.message IS '결과 메시지';
COMMENT ON COLUMN awf_plugin_history.plugin_data IS '플러그인 데이터';

-- public.awf_plugin_history foreign keys
ALTER TABLE awf_plugin_history ADD CONSTRAINT awf_plugin_history_fk FOREIGN KEY (plugin_id) REFERENCES awf_plugin(plugin_id);
