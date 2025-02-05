//Create unique constraint automaticaaly creates a range index
CREATE CONSTRAINT upk_organization FOR (o:Organization) REQUIRE o.id IS UNIQUE
CREATE CONSTRAINT tpk_organization FOR (o:Organization) REQUIRE o.id IS :: STRING

CREATE CONSTRAINT tco_organization_organization_name FOR (o:Organization) REQUIRE o.organization_name IS :: STRING
CREATE TEXT INDEX itx_organization_organization_name FOR (o:Organization) ON (o.organization_name)

CREATE CONSTRAINT tco_organization_fantasy_name FOR (o:Organization) REQUIRE o.fantasy_name IS :: STRING
CREATE TEXT INDEX txi_organization_fantasy_name FOR (o:Organization) ON (o.fantasy_name)

CREATE CONSTRAINT unq_organization_registration_number FOR (o:Organization) REQUIRE o.registration_number IS UNIQUE
CREATE CONSTRAINT tco_organization_registration_number FOR (o:Organization) REQUIRE o.registration_number IS :: STRING
CREATE TEXT INDEX txi_organization_registration_number FOR (o:Organization) ON (o.registration_number)