CREATE CONSTRAINT upk_organization IF NOT EXISTS FOR (o:Organization) REQUIRE o.id IS UNIQUE

CREATE TEXT INDEX txi_organization_organization_name IF NOT EXISTS FOR (o:Organization) ON (o.organization_name)

CREATE TEXT INDEX txi_organization_fantasy_name IF NOT EXISTS FOR (o:Organization) ON (o.fantasy_name)

CREATE CONSTRAINT unq_organization_registration_number IF NOT EXISTS FOR (o:Organization) REQUIRE o.registration_number IS UNIQUE
CREATE TEXT INDEX txi_organization_registration_number IF NOT EXISTS FOR (o:Organization) ON (o.registration_number)

CREATE TEXT INDEX txi_organization_main_email IF NOT EXISTS FOR (o:Organization) ON (o.main_email)

CREATE INDEX idx_organization_is_active IF NOT EXISTS FOR (o:Organization) ON (o.is_active)

CREATE INDEX idx_organization_created_at IF NOT EXISTS FOR (o:Organization) ON (o.created_at)
CREATE INDEX idx_organization_updated_at IF NOT EXISTS FOR (o:Organization) ON (o.updated_at)