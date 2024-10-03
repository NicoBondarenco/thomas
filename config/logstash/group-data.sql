SELECT "g"."id"::TEXT,
       "g"."group_name",
       "g"."group_description",
       "g"."is_active",
       "g"."creator_id"::TEXT,
       "g"."created_at",
       "g"."updated_at",
       COALESCE(ARRAY_AGG("r"."role_authority") FILTER (WHERE "r"."role_authority" IS NOT NULL), '{}') AS "group_roles"
FROM "management"."group" "g"
         LEFT JOIN "management"."group_role" "r" ON "g"."id" = "r"."group_id"
GROUP BY "g"."id",
         "g"."group_name",
         "g"."group_description",
         "g"."is_active",
         "g"."creator_id",
         "g"."created_at",
         "g"."updated_at"
