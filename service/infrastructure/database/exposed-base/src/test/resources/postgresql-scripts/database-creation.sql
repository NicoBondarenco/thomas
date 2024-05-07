-- -----------------------------------------------------
-- Extensions and Functions
-- -----------------------------------------------------
CREATE EXTENSION IF NOT EXISTS "unaccent" SCHEMA "public";

CREATE OR REPLACE FUNCTION "public"."unaccented_lower"(text) RETURNS text
    LANGUAGE SQL
    IMMUTABLE AS
'SELECT unaccent(lower($1))';

-- -----------------------------------------------------
-- Schema creation
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS "common";
CREATE SCHEMA IF NOT EXISTS "simple";

-- -----------------------------------------------------
-- Table "common"."auditable"
-- -----------------------------------------------------
CREATE TABLE "common"."auditable"
(
    "id"               UUID                           NOT NULL,
    "string_value"     VARCHAR(250)                   NOT NULL,
    "boolean_value"    BOOLEAN                        NOT NULL,
    "int_value"        INT                            NOT NULL,
    "long_value"       BIGINT                         NOT NULL,
    "big_decimal"      DECIMAL(18, 5)                 NOT NULL,
    "date_value"       DATE                           NOT NULL,
    "time_value"       TIME(9)                        NOT NULL,
    "datetime_value"   TIMESTAMP(9) WITHOUT TIME ZONE NOT NULL,
    "enum_value"       VARCHAR(250)                   NOT NULL,
    "uuid_empty"       UUID                           NULL,
    "string_empty"     VARCHAR(250)                   NULL,
    "boolean_empty"    BOOLEAN                        NULL,
    "int_empty"        INT                            NULL,
    "long_empty"       BIGINT                         NULL,
    "bigdecimal_empty" DECIMAL(18, 5)                 NULL,
    "date_empty"       DATE                           NULL,
    "time_empty"       TIME(9)                        NULL,
    "datetime_empty"   TIMESTAMP(9) WITHOUT TIME ZONE NULL,
    "enum_null"        VARCHAR(250)                   NULL,
    "created_at"       TIMESTAMP(9) WITH TIME ZONE    NOT NULL,
    "updated_at"       TIMESTAMP(9) WITH TIME ZONE    NOT NULL
);

ALTER TABLE "common"."auditable" ADD CONSTRAINT "pk_auditable" PRIMARY KEY ("id");

CREATE INDEX "dx_auditable_created_at" ON "common"."auditable" ("created_at");
CREATE INDEX "dx_auditable_updated_at" ON "common"."auditable" ("updated_at");

-- -----------------------------------------------------
-- Table "common"."non_auditable"
-- -----------------------------------------------------
CREATE TABLE "common"."non_auditable"
(
    "id"           UUID         NOT NULL,
    "string_value" VARCHAR(250) NOT NULL,
    "int_value"    INT          NOT NULL
);

ALTER TABLE "common"."non_auditable" ADD CONSTRAINT "pk_non_auditable" PRIMARY KEY ("id");

-- -----------------------------------------------------
-- Table "common"."searchable"
-- -----------------------------------------------------
CREATE TABLE "common"."searchable"
(
    "id"           UUID         NOT NULL,
    "string_value" VARCHAR(250) NOT NULL,
    "int_value"    INT          NOT NULL
);

ALTER TABLE "common"."searchable" ADD CONSTRAINT "pk_searchable" PRIMARY KEY ("id");

-- -----------------------------------------------------
-- Table "common"."parent"
-- -----------------------------------------------------
CREATE TABLE "common"."parent"
(
    "id"           UUID         NOT NULL,
    "string_value" VARCHAR(250) NOT NULL,
    "int_value"    INT          NOT NULL
);

ALTER TABLE "common"."parent" ADD CONSTRAINT "pk_parent" PRIMARY KEY ("id");

-- -----------------------------------------------------
-- Table "common"."child"
-- -----------------------------------------------------
CREATE TABLE "common"."child"
(
    "id"           UUID         NOT NULL,
    "string_value" VARCHAR(250) NOT NULL,
    "int_value"    INT          NOT NULL,
    "parent_id"    UUID         NOT NULL
);

ALTER TABLE "common"."child" ADD CONSTRAINT "pk_child" PRIMARY KEY ("id");
ALTER TABLE "common"."child" ADD CONSTRAINT "fk_child_parent" FOREIGN KEY ("parent_id") REFERENCES "common"."parent" ("id") ON DELETE RESTRICT ON UPDATE RESTRICT;

-- -----------------------------------------------------
-- Table "simple"."enumerated"
-- -----------------------------------------------------
CREATE TABLE "simple"."enumerated"
(
    "id"            UUID         NOT NULL,
    "value_enum"    VARCHAR(250) NOT NULL,
    "nullable_enum" VARCHAR(250) NULL
);

ALTER TABLE "simple"."enumerated" ADD CONSTRAINT "pk_enumerated" PRIMARY KEY ("id");

-- -----------------------------------------------------
-- Table "simple.simple_model"
-- -----------------------------------------------------
CREATE TABLE "simple.simple_model"
(
    "id"            UUID         NOT NULL,
    "string_value"  VARCHAR(250) NOT NULL,
    "boolean_value" BOOLEAN      NOT NULL,
    "int_value"     INT          NOT NULL
);

ALTER TABLE "simple.simple_model" ADD CONSTRAINT "pk_simple_model" PRIMARY KEY ("id");

-- -----------------------------------------------------
-- Inserts Table "common"."searchable"
-- -----------------------------------------------------
INSERT INTO "common"."searchable" ("id", "string_value", "int_value") VALUES ('41cb5242-0523-4dea-a750-e91749a879dc', 'Abílio Fernandes Ortiz', 1);
INSERT INTO "common"."searchable" ("id", "string_value", "int_value") VALUES ('779b122c-6adf-48c2-aeee-bd6b6980ac41', 'Altair Lozano Neto', 2);
INSERT INTO "common"."searchable" ("id", "string_value", "int_value") VALUES ('e7a9f091-f9a8-429a-87bb-464485035037', 'Carla Joseana Medeiros', 3);
INSERT INTO "common"."searchable" ("id", "string_value", "int_value") VALUES ('6fe255a3-204b-4623-9910-1d3f433d0d50', 'Carlos José Alceu', 4);
INSERT INTO "common"."searchable" ("id", "string_value", "int_value") VALUES ('a2acd9bd-724d-4901-ade7-1b7a8683fb78', 'Clara Ferreira Couto', 5);
INSERT INTO "common"."searchable" ("id", "string_value", "int_value") VALUES ('59ae0705-6fd9-411e-a459-7eb22493a04c', 'Diego Jonathas Filho', 6);
INSERT INTO "common"."searchable" ("id", "string_value", "int_value") VALUES ('b5eae309-be81-479c-9ae5-ab15dd8ff2d1', 'Ederson Caldeira Madeira', 7);
INSERT INTO "common"."searchable" ("id", "string_value", "int_value") VALUES ('e0055f5d-9218-4396-bc65-5be41db53bc3', 'Francielle Thaíssa Barros', 8);
INSERT INTO "common"."searchable" ("id", "string_value", "int_value") VALUES ('2339f2e7-627f-4c80-9a17-5360ca660177', 'Gabriel Beltrão Filho', 9);
INSERT INTO "common"."searchable" ("id", "string_value", "int_value") VALUES ('9fffbfd3-0e60-4056-943e-f2c85b17685b', 'Joseandro Faria Lima', 10);
INSERT INTO "common"."searchable" ("id", "string_value", "int_value") VALUES ('219fb822-c1de-4845-95ce-95b2ba84b121', 'Joseane Dalva Galote', 11);
INSERT INTO "common"."searchable" ("id", "string_value", "int_value") VALUES ('1c2a867a-745d-422e-bf5c-2117177eb263', 'Josefa Alma Valença', 12);
INSERT INTO "common"."searchable" ("id", "string_value", "int_value") VALUES ('dfcbd418-e8e2-4d62-ae4d-5ed04367dde3', 'Joselias Espinoza Lovato', 13);
INSERT INTO "common"."searchable" ("id", "string_value", "int_value") VALUES ('3582eb4e-5ab9-4ca6-8a2e-8aec635250fa', 'Joseph Manoel Frias', 14);
INSERT INTO "common"."searchable" ("id", "string_value", "int_value") VALUES ('751f1a1c-d791-4362-8925-08ae258a280a', 'Josias Cláudio Elias', 15);
INSERT INTO "common"."searchable" ("id", "string_value", "int_value") VALUES ('f262fd58-7b0a-4a95-99f4-7b752a74ac50', 'José Silva Santos', 16);
INSERT INTO "common"."searchable" ("id", "string_value", "int_value") VALUES ('07529168-6967-46d0-9ff6-b3d7df9b4ef6', 'Joséaldo Mendonça Santos', 17);
INSERT INTO "common"."searchable" ("id", "string_value", "int_value") VALUES ('f04beb5b-52cf-457c-9e0b-16de8ec0db32', 'Lidiane Verônica Cervantes', 18);
INSERT INTO "common"."searchable" ("id", "string_value", "int_value") VALUES ('d18fc57f-f3f8-4df1-9b15-c9615cbab495', 'Luana Furtado Branco', 19);
INSERT INTO "common"."searchable" ("id", "string_value", "int_value") VALUES ('09ae4e7d-6ed9-41ba-9899-f0757811c699', 'Manuel Faria Gama', 20);
INSERT INTO "common"."searchable" ("id", "string_value", "int_value") VALUES ('76d3cc16-a7ff-43fc-b83a-69444f69d296', 'Maria Joséfina Almeida', 21);
INSERT INTO "common"."searchable" ("id", "string_value", "int_value") VALUES ('4f22d78e-2d0a-4681-99b0-9351af547e6a', 'Nicole Molina José', 22);
INSERT INTO "common"."searchable" ("id", "string_value", "int_value") VALUES ('0ab06b76-d7b6-4138-8197-2055578e8d06', 'Rodrigo Camacho Dias', 23);
INSERT INTO "common"."searchable" ("id", "string_value", "int_value") VALUES ('14847e53-88e9-44f6-b4ef-4cc392f02033', 'Sirojosé Barnabé Sandoval', 24);
INSERT INTO "common"."searchable" ("id", "string_value", "int_value") VALUES ('5bcd5ef9-4a2b-4729-b3cb-407758ba5747', 'Thiago Marcos Delgado', 25);