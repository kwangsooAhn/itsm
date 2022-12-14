/* Drop Sequences */

DROP SEQUENCE IF EXISTS awf_archive_seq cascade;
DROP SEQUENCE IF EXISTS awf_file_loc_seq cascade;
DROP SEQUENCE IF EXISTS hibernate_sequence cascade;
DROP SEQUENCE IF EXISTS portal_board_seq cascade;
DROP SEQUENCE IF EXISTS schedule_history_seq cascade;
DROP SEQUENCE IF EXISTS cmdb_ci_icon_file_seq cascade;

/* Create Sequences */
CREATE SEQUENCE awf_archive_seq INCREMENT 1 MINVALUE 1 START 1;
CREATE SEQUENCE awf_file_loc_seq INCREMENT 1 MINVALUE 1 START 1;
CREATE SEQUENCE hibernate_sequence INCREMENT 1 MINVALUE 1 START 1;
CREATE SEQUENCE portal_board_seq INCREMENT 1 MINVALUE 1 START 1;
CREATE SEQUENCE schedule_history_seq INCREMENT 1 MINVALUE 1 START 1;
CREATE SEQUENCE cmdb_ci_icon_file_seq INCREMENT 1 MINVALUE 1 START 16;