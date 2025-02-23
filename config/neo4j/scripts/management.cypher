//Create unique constraint automatically creates a range index
CREATE CONSTRAINT upk_organization FOR (o:Organization) REQUIRE o.id IS UNIQUE

CREATE TEXT INDEX txi_organization_organization_name FOR (o:Organization) ON (o.organization_name)

CREATE TEXT INDEX txi_organization_fantasy_name FOR (o:Organization) ON (o.fantasy_name)

CREATE CONSTRAINT unq_organization_registration_number FOR (o:Organization) REQUIRE o.registration_number IS UNIQUE
CREATE TEXT INDEX txi_organization_registration_number FOR (o:Organization) ON (o.registration_number)

CREATE TEXT INDEX txi_organization_main_email FOR (o:Organization) ON (o.main_email)

CREATE INDEX idx_organization_is_active FOR (o:Organization) ON (o.is_active)

CREATE INDEX idx_organization_created_at FOR (o:Organization) ON (o.created_at)
CREATE INDEX idx_organization_updated_at FOR (o:Organization) ON (o.updated_at)

//--------------------------------------------------------------------------------------------------------------------------------------

DROP CONSTRAINT upk_organization

DROP INDEX txi_organization_organization_name

DROP INDEX txi_organization_fantasy_name

DROP CONSTRAINT unq_organization_registration_number
DROP INDEX txi_organization_registration_number

DROP INDEX txi_organization_main_email

DROP INDEX idx_organization_is_active

DROP INDEX idx_organization_created_at
DROP INDEX idx_organization_updated_at



