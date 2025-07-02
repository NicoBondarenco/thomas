-- -----------------------------------------------------
-- Extensions and Functions
-- -----------------------------------------------------
CREATE
EXTENSION IF NOT EXISTS "unaccent" SCHEMA "public";

CREATE
OR REPLACE FUNCTION "public"."unaccented_lower"(text) RETURNS text
    LANGUAGE SQL
    IMMUTABLE AS
'SELECT public.unaccent(lower($1))';

-- -----------------------------------------------------
-- Schema vida-management
-- -----------------------------------------------------
CREATE SCHEMA "locality";

-- -----------------------------------------------------
-- Table "management"."user"
-- -----------------------------------------------------
CREATE TABLE "locality"."address"
(
    "id"                   UUID                        NOT NULL,
    "zipcode_number"       VARCHAR(250)                NOT NULL,
    "address_street"       VARCHAR(250)                    NULL,
    "address_complement"   VARCHAR(250)                    NULL,
    "address_unit"         VARCHAR(250)                    NULL,
    "address_neighborhood" VARCHAR(250)                    NULL,
    "address_city"         VARCHAR(250)                NOT NULL,
    "address_state"        VARCHAR(250)                NOT NULL,
    "city_code"            VARCHAR(250)                    NULL,
    "reference_code"       VARCHAR(250)                    NULL,
    "phone_code"           VARCHAR(250)                    NULL,
    "federal_code"         VARCHAR(250)                    NULL,
    "created_at"           TIMESTAMP(9) WITH TIME ZONE NOT NULL,
    "updated_at"           TIMESTAMP(9) WITH TIME ZONE NOT NULL
);

ALTER TABLE "locality"."address" ADD CONSTRAINT "pk_locality_address" PRIMARY KEY ("id");
ALTER TABLE "locality"."address" ADD CONSTRAINT "un_locality_address_zipcode_number" UNIQUE ("zipcode_number");

CREATE INDEX "dx_locality_address_address_street"   ON "locality"."address" ("public"."unaccented_lower"("address_street"));
CREATE INDEX "dx_locality_address_address_city"     ON "locality"."address" ("public"."unaccented_lower"("address_city"));
CREATE INDEX "dx_locality_address_address_state"    ON "locality"."address" ("address_state");
CREATE INDEX "dx_locality_address_city_code"        ON "locality"."address" ("city_code");
CREATE INDEX "dx_locality_address_reference_code"   ON "locality"."address" ("reference_code");
CREATE INDEX "dx_locality_address_phone_code"       ON "locality"."address" ("phone_code");
CREATE INDEX "dx_locality_address_federal_code"     ON "locality"."address" ("federal_code");
CREATE INDEX "dx_locality_address_created_at"       ON "locality"."address" ("created_at");
CREATE INDEX "dx_locality_address_updated_at"       ON "locality"."address" ("updated_at");
