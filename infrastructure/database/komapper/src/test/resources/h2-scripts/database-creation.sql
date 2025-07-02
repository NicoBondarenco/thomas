CREATE SCHEMA IF NOT EXISTS "public";

CREATE ALIAS IF NOT EXISTS "public"."unaccented_lower" AS '
String unaccented_lower(String value) {
    String unaccented = java.text.Normalizer.normalize(value, java.text.Normalizer.Form.NFD).replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
    return unaccented.toLowerCase();
}
';

CREATE SCHEMA IF NOT EXISTS "komapper";

CREATE TABLE IF NOT EXISTS "komapper"."entity_test"
(
    "id"                      UUID                        NOT NULL,
    "string_required"         VARCHAR(250)                NOT NULL,
    "string_nullable"         VARCHAR(250) NULL,
    "integer_required"        INT                         NOT NULL,
    "integer_nullable"        INT NULL,
    "long_required"           BIGINT                      NOT NULL,
    "long_nullable"           BIGINT NULL,
    "double_required"         DECIMAL(13, 5)              NOT NULL,
    "double_nullable"         DECIMAL(13, 5) NULL,
    "biginteger_required"     BIGINT                      NOT NULL,
    "biginteger_nullable"     BIGINT NULL,
    "bigdecimal_required"     DECIMAL(18, 5)              NOT NULL,
    "bigdecimal_nullable"     DECIMAL(18, 5) NULL,
    "boolean_required"        BOOLEAN                     NOT NULL,
    "boolean_nullable"        BOOLEAN NULL,
    "enumeration_required"    VARCHAR(250)                NOT NULL,
    "enumeration_nullable"    VARCHAR(250) NULL,
    "localdate_required"      DATE                        NOT NULL,
    "localdate_nullable"      DATE NULL,
    "localdatetime_required"  TIMESTAMP(9) WITHOUT TIME ZONE                NOT NULL,
    "localdatetime_nullable"  TIMESTAMP(9) WITHOUT TIME ZONE NULL,
    "localtime_required"      TIME(9)                     NOT NULL,
    "localtime_nullable"      TIME(9) NULL,
    "offsetdatetime_required" TIMESTAMP(9) WITH TIME ZONE NOT NULL,
    "offsetdatetime_nullable" TIMESTAMP(9) WITH TIME ZONE NULL,
    "uuid_required"           UUID                        NOT NULL,
    "uuid_nullable"           UUID NULL,
    "created_at"              TIMESTAMP(9) WITH TIME ZONE NOT NULL,
    "updated_at"              TIMESTAMP(9) WITH TIME ZONE NOT NULL
);

ALTER TABLE "komapper"."entity_test" ADD CONSTRAINT IF NOT EXISTS "pk_entity_test" PRIMARY KEY ("id");

CREATE INDEX IF NOT EXISTS "dx_entity_test_created_at" ON "komapper"."entity_test" ("created_at");
CREATE INDEX IF NOT EXISTS "dx_entity_test_updated_at" ON "komapper"."entity_test" ("updated_at");
