/**
 * 아바타
 */
DROP TABLE IF EXISTS awf_avatar cascade;

CREATE TABLE awf_avatar
(
    avatar_id varchar(128) NOT NULL,
    avatar_type varchar(100),
    avatar_value varchar(512),
    uploaded boolean DEFAULT 'false' NOT NULL,
    uploaded_location varchar(512),
    create_user_key varchar(128),
    create_dt timestamp,
    update_user_key varchar(128),
    update_dt timestamp,
    CONSTRAINT awf_avatar_pk PRIMARY KEY (avatar_id)
);

COMMENT ON TABLE awf_avatar IS '아바타';
COMMENT ON COLUMN awf_avatar.avatar_id IS '아바타 아이디';
COMMENT ON COLUMN awf_avatar.avatar_type IS '아바타 종류';
COMMENT ON COLUMN awf_avatar.avatar_value IS '아바타 value';
COMMENT ON COLUMN awf_avatar.uploaded IS '업로드 여부';
COMMENT ON COLUMN awf_avatar.uploaded_location IS '업로드 경로';
COMMENT ON COLUMN awf_avatar.create_user_key IS '등록자';
COMMENT ON COLUMN awf_avatar.create_dt IS '등록일';
COMMENT ON COLUMN awf_avatar.update_user_key IS '수정자';
COMMENT ON COLUMN awf_avatar.update_dt IS '수정일';

insert into awf_avatar values('0509e09412534a6e98f04ca79abb6424', 'FILE', 'profile_sample.jpg', false, '', '0509e09412534a6e98f04ca79abb6424', now(), null, null);
