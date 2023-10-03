-- ======================== --
--  ** INITIAL SQL CACM **  --
-- ======================== --

/** Set Unique Constraints **/
ALTER TABLE category ADD UNIQUE (name, deletion_token);
ALTER TABLE client_customer ADD UNIQUE (customer_id, deletion_token);
ALTER TABLE exception_definition ADD UNIQUE (name, deletion_token);
ALTER TABLE exception_schedule ADD UNIQUE (exception_definition_id, deletion_token);
ALTER TABLE incident_cause ADD UNIQUE (name, deletion_token);
ALTER TABLE privilege ADD UNIQUE (name, deletion_token);
ALTER TABLE privilege_mapping ADD UNIQUE (user_group_id, privilege_id, deletion_token);
ALTER TABLE "user" ADD UNIQUE (username, deletion_token);
ALTER TABLE "user" ADD UNIQUE (email, deletion_token);
ALTER TABLE user_categories ADD UNIQUE (user_id, categories_id);
ALTER TABLE user_group ADD UNIQUE (name, deletion_token);

/** Compulsory User Groups **/
SELECT setval('user_group_id_seq', 0, true);
INSERT INTO user_group (creation_timestamp, deleted, deletion_token, description, name, created_by)
VALUES (now(), false, 0, 'ADMIN', 'ADMIN', 0);
INSERT INTO user_group (creation_timestamp, deleted, deletion_token, description, name, created_by)
VALUES (now(), false, 0, 'ASSIGNEE', 'ASSIGNEE', 0);
INSERT INTO user_group (creation_timestamp, deleted, deletion_token, description, name, created_by)
VALUES (now(), false, 0, 'OWNER', 'OWNER', 0);
INSERT INTO user_group (creation_timestamp, deleted, deletion_token, description, name, created_by)
VALUES (now(), false, 0, 'AUDIT', 'AUDIT', 0);
INSERT INTO user_group (creation_timestamp, deleted, deletion_token, description, name, created_by)
VALUES (now(), false, 0, 'COMPLIANCE', 'COMPLIANCE', 0);

/** Admin User **/
SELECT setval('user_id_seq', 0, true);
INSERT INTO "user" (creation_timestamp, deleted, deletion_token, update_timestamp, name, password, username, email,
                    emp_number, telephone, user_group_id, created_by)
VALUES (now(), false, 0, null, 'Chathura Buddhika', '$2a$10$FoecgZ7JyXWjgx5Qc1a0bOuzTxUj9zBInbb0XUkS0jJJCTHylYs0u',
        'chathurabuddi', 'cgunapala@kpmg.com', '0000', '0000000000', 1, 1);

/** Privileges **/

/** Privileges Mappings To Admin User**/
