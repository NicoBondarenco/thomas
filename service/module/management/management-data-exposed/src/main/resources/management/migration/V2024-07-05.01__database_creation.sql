-- -----------------------------------------------------
-- Extensions and Functions
-- -----------------------------------------------------
CREATE EXTENSION IF NOT EXISTS "unaccent" SCHEMA "public";

CREATE OR REPLACE FUNCTION "public"."unaccented_lower"(text) RETURNS text
    LANGUAGE SQL
    IMMUTABLE AS
'SELECT public.unaccent(lower($1))';

-- -----------------------------------------------------
-- Schema vida-management
-- -----------------------------------------------------
CREATE SCHEMA "management";

-- -----------------------------------------------------
-- Table "management"."user"
-- -----------------------------------------------------
CREATE TABLE "management"."user"
(
    "id"              UUID                        NOT NULL,
    "first_name"      VARCHAR(250)                NOT NULL,
    "last_name"       VARCHAR(250)                NOT NULL,
    "main_email"      VARCHAR(250)                NOT NULL,
    "document_number" VARCHAR(250)                NOT NULL,
    "phone_number"    VARCHAR(250)                NULL,
    "profile_photo"   VARCHAR(250)                NULL,
    "birth_date"      DATE                        NULL,
    "user_gender"     VARCHAR(50)                 NULL,
    "is_active"       BOOLEAN                     NOT NULL,
    "creator_id"      UUID                        NOT NULL,
    "created_at"      TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    "updated_at"      TIMESTAMP(6) WITH TIME ZONE NOT NULL
);

ALTER TABLE "management"."user" ADD CONSTRAINT "pk_user" PRIMARY KEY ("id");

ALTER TABLE "management"."user" ADD CONSTRAINT "un_user_main_email" UNIQUE ("main_email");
ALTER TABLE "management"."user" ADD CONSTRAINT "un_user_document_number" UNIQUE ("document_number");

ALTER TABLE "management"."user" ADD CONSTRAINT "fk_user_creator" FOREIGN KEY ("creator_id") REFERENCES "management"."user" ("id") ON DELETE RESTRICT ON UPDATE RESTRICT;

CREATE INDEX "dx_user_creator_id" ON "management"."user" ("creator_id");
CREATE INDEX "dx_user_first_name" ON "management"."user" ("public"."unaccented_lower"("first_name"));
CREATE INDEX "dx_user_last_name" ON "management"."user" ("public"."unaccented_lower"("last_name"));
CREATE INDEX "dx_user_main_email" ON "management"."user" ("public"."unaccented_lower"("main_email"));
CREATE INDEX "dx_user_created_at" ON "management"."user" ("created_at");
CREATE INDEX "dx_user_updated_at" ON "management"."user" ("updated_at");

-- -----------------------------------------------------
-- Table "management"."group"
-- -----------------------------------------------------
CREATE TABLE "management"."group"
(
    "id"                UUID                        NOT NULL,
    "group_name"        VARCHAR(250)                NOT NULL,
    "group_description" VARCHAR(250)                NULL,
    "is_active"         BOOLEAN                     NOT NULL,
    "creator_id"        UUID                        NOT NULL,
    "created_at"        TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    "updated_at"        TIMESTAMP(6) WITH TIME ZONE NOT NULL
);

ALTER TABLE "management"."group" ADD CONSTRAINT "pk_group" PRIMARY KEY ("id");

ALTER TABLE "management"."group" ADD CONSTRAINT "un_group_group_name" UNIQUE ("group_name");

ALTER TABLE "management"."group" ADD CONSTRAINT "fk_group_creator" FOREIGN KEY ("creator_id") REFERENCES "management"."user" ("id") ON DELETE RESTRICT ON UPDATE RESTRICT;

CREATE INDEX "dx_group_creator_id" ON "management"."group" ("creator_id");
CREATE INDEX "dx_group_group_name" ON "management"."group" ("public"."unaccented_lower"("group_name"));
CREATE INDEX "dx_group_group_description" ON "management"."group" ("public"."unaccented_lower"("group_description"));
CREATE INDEX "dx_group_created_at" ON "management"."group" ("created_at");
CREATE INDEX "dx_group_updated_at" ON "management"."group" ("updated_at");

-- -----------------------------------------------------
-- Table "management"."user_role"
-- -----------------------------------------------------
CREATE TABLE "management"."user_role"
(
    "id"             UUID                        NOT NULL,
    "user_id"        UUID                        NOT NULL,
    "role_authority" VARCHAR(250)                NOT NULL,
    "created_at"     TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    "updated_at"     TIMESTAMP(6) WITH TIME ZONE NOT NULL
);

ALTER TABLE "management"."user_role" ADD CONSTRAINT "pk_user_role" PRIMARY KEY ("id");

ALTER TABLE "management"."user_role" ADD CONSTRAINT "un_user_role_user_id_role_authority" UNIQUE ("user_id", "role_authority");

ALTER TABLE "management"."user_role" ADD CONSTRAINT "fk_user_role_user" FOREIGN KEY ("user_id") REFERENCES "management"."user" ("id") ON DELETE RESTRICT ON UPDATE RESTRICT;

CREATE INDEX "dx_user_role_user_id" ON "management"."user_role" ("user_id");
CREATE INDEX "dx_user_role_role_authority" ON "management"."user_role" ("role_authority");
CREATE INDEX "dx_user_role_created_at" ON "management"."user_role" ("created_at");
CREATE INDEX "dx_user_role_updated_at" ON "management"."user_role" ("updated_at");

-- -----------------------------------------------------
-- Table "management"."group_role"
-- -----------------------------------------------------
CREATE TABLE "management"."group_role"
(
    "id"             UUID                        NOT NULL,
    "group_id"       UUID                        NOT NULL,
    "role_authority" VARCHAR(250)                NOT NULL,
    "created_at"     TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    "updated_at"     TIMESTAMP(6) WITH TIME ZONE NOT NULL
);

ALTER TABLE "management"."group_role" ADD CONSTRAINT "pk_group_role" PRIMARY KEY ("id");

ALTER TABLE "management"."group_role" ADD CONSTRAINT "un_group_role_group_id_role_authority" UNIQUE ("group_id", "role_authority");

ALTER TABLE "management"."group_role" ADD CONSTRAINT "fk_group_role_group" FOREIGN KEY ("group_id") REFERENCES "management"."group" ("id") ON DELETE RESTRICT ON UPDATE RESTRICT;

CREATE INDEX "dx_group_role_group_id" ON "management"."group_role" ("group_id");
CREATE INDEX "dx_group_role_role_authority" ON "management"."group_role" ("role_authority");
CREATE INDEX "dx_group_role_created_at" ON "management"."group_role" ("created_at");
CREATE INDEX "dx_group_role_updated_at" ON "management"."group_role" ("updated_at");

-- -----------------------------------------------------
-- Table "management"."user_group"
-- -----------------------------------------------------
CREATE TABLE "management"."user_group"
(
    "id"         UUID                        NOT NULL,
    "user_id"    UUID                        NOT NULL,
    "group_id"   UUID                        NOT NULL,
    "created_at" TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    "updated_at" TIMESTAMP(6) WITH TIME ZONE NOT NULL
);

ALTER TABLE "management"."user_group" ADD CONSTRAINT "pk_user_group" PRIMARY KEY ("id");

ALTER TABLE "management"."user_group" ADD CONSTRAINT "un_user_group_user_id_group_id" UNIQUE ("user_id", "group_id");

ALTER TABLE "management"."user_group" ADD CONSTRAINT "fk_user_group_user" FOREIGN KEY ("user_id") REFERENCES "management"."user" ("id") ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE "management"."user_group" ADD CONSTRAINT "fk_user_group_group" FOREIGN KEY ("group_id") REFERENCES "management"."group" ("id") ON DELETE RESTRICT ON UPDATE RESTRICT;

CREATE INDEX "dx_user_group_user_id" ON "management"."user_group" ("user_id");
CREATE INDEX "dx_user_group_group_id" ON "management"."user_group" ("group_id");
CREATE INDEX "dx_user_group_created_at" ON "management"."user_group" ("created_at");
CREATE INDEX "dx_user_group_updated_at" ON "management"."user_group" ("updated_at");
