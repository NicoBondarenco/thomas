CREATE EXTENSION IF NOT EXISTS "unaccent" SCHEMA "public";

CREATE OR REPLACE FUNCTION "public"."unaccented_lower"(text) RETURNS text
    LANGUAGE SQL
    IMMUTABLE AS
'SELECT public.unaccent(lower($1))';