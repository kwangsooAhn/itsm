/**
  속성 그룹 알림
 */
DROP TABLE IF EXISTS cmdb_class_notification cascade;

CREATE TABLE cmdb_class_notification
(
    class_id varchar(128) NOT NULL,
    attribute_id varchar(128) NOT NULL,
    attribute_order int NOT NULL,
    condition text,
    target_attribute_id varchar(128) NOT NULL,
    CONSTRAINT cmdb_class_notification_pk PRIMARY KEY (class_id, attribute_id, condition, target_attribute_id),
    CONSTRAINT cmdb_class_notification_fk1 FOREIGN KEY (class_id) REFERENCES cmdb_class (class_id),
    CONSTRAINT cmdb_class_notification_fk2 FOREIGN KEY (attribute_id) REFERENCES cmdb_attribute (attribute_id)
);

COMMENT ON TABLE cmdb_class_notification IS '속성 알람 설정 정보';
COMMENT ON COLUMN cmdb_class_notification.class_id IS '클래스아이디';
COMMENT ON COLUMN cmdb_class_notification.attribute_id IS '속성아이디';
COMMENT ON COLUMN cmdb_class_notification.attribute_order IS '순서';
COMMENT ON COLUMN cmdb_class_notification.condition IS '조건';
COMMENT ON COLUMN cmdb_class_notification.target_attribute_id IS '담당자';
