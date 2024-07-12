INSERT INTO "management"."user" ("id", "first_name", "last_name", "main_email", "document_number", "phone_number", "profile_photo", "birth_date", "user_gender", "is_active", "creator_id", "created_at", "updated_at") VALUES ('436d12f3-d23e-4346-b847-37188521a968', 'Anthony', 'Stark', 'iron.man@avengers.com', '10775576042', '16988776655', null, '1990-04-28', 'CIS_MALE', true, '436d12f3-d23e-4346-b847-37188521a968', NOW(), NOW());

INSERT INTO "management"."group"("id", "group_name", "group_description", "is_active", "creator_id", "created_at", "updated_at") VALUES ('dc25382c-6fe3-4ee5-a12e-59c8fd3b5442', 'The Avengers', 'The Avengers - Protectors of the Earth', true, '436d12f3-d23e-4346-b847-37188521a968', NOW(), NOW());
INSERT INTO "management"."group"("id", "group_name", "group_description", "is_active", "creator_id", "created_at", "updated_at") VALUES ('1f85e146-32ac-4fdd-a3ae-e97a55978c99', 'Team Iron Man', null, true, '436d12f3-d23e-4346-b847-37188521a968', NOW(), NOW());
INSERT INTO "management"."group"("id", "group_name", "group_description", "is_active", "creator_id", "created_at", "updated_at") VALUES ('b63a98f7-f111-4171-98cb-3f9e25a80890', 'Team Captain America', null, true, '436d12f3-d23e-4346-b847-37188521a968', NOW(), NOW());
INSERT INTO "management"."group"("id", "group_name", "group_description", "is_active", "creator_id", "created_at", "updated_at") VALUES ('a1400649-ab5c-4501-a4c9-4614608afd4b', 'X-Men', 'The Xavier Institute', true, '436d12f3-d23e-4346-b847-37188521a968', NOW(), NOW());
INSERT INTO "management"."group"("id", "group_name", "group_description", "is_active", "creator_id", "created_at", "updated_at") VALUES ('096e5d76-b40b-4668-9515-68963d0db7ca', 'Guardians of the Galaxy', 'Protectors of the Space', true, '436d12f3-d23e-4346-b847-37188521a968', NOW(), NOW());

INSERT INTO "management"."group_role" ("id", "group_id", "role_authority", "created_at", "updated_at") VALUES ('604ef3f9-8380-46e3-921a-8317d2b64e17', 'dc25382c-6fe3-4ee5-a12e-59c8fd3b5442', 'ROLE_USER_READ', NOW(), NOW());
INSERT INTO "management"."group_role" ("id", "group_id", "role_authority", "created_at", "updated_at") VALUES ('36030a90-313e-4f4c-91f1-f5332a95155b', 'dc25382c-6fe3-4ee5-a12e-59c8fd3b5442', 'ROLE_GROUP_READ', NOW(), NOW());
INSERT INTO "management"."group_role" ("id", "group_id", "role_authority", "created_at", "updated_at") VALUES ('653deea0-e65e-450d-8785-d1cc9d33f17f', '096e5d76-b40b-4668-9515-68963d0db7ca', 'ROLE_GROUP_READ', NOW(), NOW());
INSERT INTO "management"."group_role" ("id", "group_id", "role_authority", "created_at", "updated_at") VALUES ('4bbc855b-9aef-44b2-90ab-485cebadd190', '096e5d76-b40b-4668-9515-68963d0db7ca', 'ROLE_GROUP_CREATE', NOW(), NOW());
INSERT INTO "management"."group_role" ("id", "group_id", "role_authority", "created_at", "updated_at") VALUES ('c6a2209a-92c9-4092-8026-559235133008', '096e5d76-b40b-4668-9515-68963d0db7ca', 'ROLE_GROUP_UPDATE', NOW(), NOW());
INSERT INTO "management"."group_role" ("id", "group_id", "role_authority", "created_at", "updated_at") VALUES ('bb96a0cf-84e4-4817-8adc-bd069044cf80', '096e5d76-b40b-4668-9515-68963d0db7ca', 'ROLE_GROUP_DELETE', NOW(), NOW());
INSERT INTO "management"."group_role" ("id", "group_id", "role_authority", "created_at", "updated_at") VALUES ('49e55224-ad5a-4d32-8880-4d15e936d053', 'b63a98f7-f111-4171-98cb-3f9e25a80890', 'ROLE_GROUP_READ', NOW(), NOW());
INSERT INTO "management"."group_role" ("id", "group_id", "role_authority", "created_at", "updated_at") VALUES ('2af6adf2-5888-44cf-a70b-52ab4bb286d6', 'b63a98f7-f111-4171-98cb-3f9e25a80890', 'ROLE_GROUP_CREATE', NOW(), NOW());
INSERT INTO "management"."group_role" ("id", "group_id", "role_authority", "created_at", "updated_at") VALUES ('65ad58ba-38a7-4e1a-8d31-9d3db6ccd05d', 'b63a98f7-f111-4171-98cb-3f9e25a80890', 'ROLE_GROUP_UPDATE', NOW(), NOW());
INSERT INTO "management"."group_role" ("id", "group_id", "role_authority", "created_at", "updated_at") VALUES ('4855c184-58fe-4574-9277-29cb61373752', 'b63a98f7-f111-4171-98cb-3f9e25a80890', 'ROLE_GROUP_DELETE', NOW(), NOW());