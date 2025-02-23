CREATE CONSTRAINT upk_string_props IF NOT EXISTS FOR (s:StringProps) REQUIRE s.id IS UNIQUE
CREATE CONSTRAINT upk_number_props IF NOT EXISTS FOR (s:NumberProps) REQUIRE s.id IS UNIQUE
CREATE CONSTRAINT upk_datetime_props IF NOT EXISTS FOR (s:DatetimeProps) REQUIRE s.id IS UNIQUE

CREATE CONSTRAINT upk_save_props IF NOT EXISTS FOR (s:SaveProps) REQUIRE s.id IS UNIQUE
CREATE CONSTRAINT unq_save_props_prop_name IF NOT EXISTS FOR (s:SaveProps) REQUIRE s.prop_name IS UNIQUE