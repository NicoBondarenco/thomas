CREATE CONSTRAINT upk_organization IF NOT EXISTS FOR (o:Organization) REQUIRE o.id IS UNIQUE

CREATE TEXT INDEX txi_organization_organization_name IF NOT EXISTS FOR (o:Organization) ON (o.organization_name)

CREATE TEXT INDEX txi_organization_fantasy_name IF NOT EXISTS FOR (o:Organization) ON (o.fantasy_name)

CREATE CONSTRAINT unq_organization_registration_number IF NOT EXISTS FOR (o:Organization) REQUIRE o.registration_number IS UNIQUE
CREATE TEXT INDEX txi_organization_registration_number IF NOT EXISTS FOR (o:Organization) ON (o.registration_number)

CREATE TEXT INDEX txi_organization_main_email IF NOT EXISTS FOR (o:Organization) ON (o.main_email)

CREATE INDEX idx_organization_is_active IF NOT EXISTS FOR (o:Organization) ON (o.is_active)

CREATE INDEX idx_organization_created_at IF NOT EXISTS FOR (o:Organization) ON (o.created_at)
CREATE INDEX idx_organization_updated_at IF NOT EXISTS FOR (o:Organization) ON (o.updated_at)

CREATE CONSTRAINT upk_unit IF NOT EXISTS FOR (u:Unit) REQUIRE u.id IS UNIQUE

CREATE TEXT INDEX txi_unit_unit_name IF NOT EXISTS FOR (u:Unit) ON (u.unit_name)

CREATE TEXT INDEX txi_unit_fantasy_name IF NOT EXISTS FOR (u:Unit) ON (u.fantasy_name)

CREATE CONSTRAINT unq_unit_document_number IF NOT EXISTS FOR (u:Unit) REQUIRE u.document_number IS UNIQUE
CREATE TEXT INDEX txi_unit_document_number IF NOT EXISTS FOR (u:Unit) ON (u.document_number)

CREATE TEXT INDEX txi_unit_main_email IF NOT EXISTS FOR (u:Unit) ON (u.main_email)

CREATE INDEX idx_unit_is_active IF NOT EXISTS FOR (u:Unit) ON (u.is_active)

CREATE INDEX idx_unit_created_at IF NOT EXISTS FOR (u:Unit) ON (u.created_at)
CREATE INDEX idx_unit_updated_at IF NOT EXISTS FOR (u:Unit) ON (u.updated_at)