/**
 * 문서 캘린더
 */
DROP TABLE IF EXISTS awf_calendar_document cascade;

CREATE TABLE awf_calendar_document
(
    calendar_id   varchar(128) NOT NULL,
    calendar_name varchar(100),
    create_dt     timestamp,
    CONSTRAINT awf_calendar_document_pk PRIMARY KEY (calendar_id),
    CONSTRAINT awf_calendar_document_fk FOREIGN KEY (calendar_id) REFERENCES awf_calendar (calendar_id),
    CONSTRAINT awf_calendar_document_uk UNIQUE (calendar_name)
);

COMMENT ON TABLE awf_calendar_document IS '문서 캘린더';
COMMENT ON COLUMN awf_calendar_document.calendar_id IS '캘린더아이디';
COMMENT ON COLUMN awf_calendar_document.calendar_name IS '캘린더이름';
COMMENT ON COLUMN awf_calendar_document.create_dt IS '등록일';

--기본 데이터
insert into awf_calendar_document values ('9c1320817c0d3112616d1df8df480002', '문서', now());
