DROP CONSTRAINT upk_organization IF EXISTS

DROP INDEX txi_organization_organization_name IF EXISTS

DROP INDEX txi_organization_fantasy_name IF EXISTS

DROP CONSTRAINT unq_organization_registration_number IF EXISTS
DROP INDEX txi_organization_registration_number IF EXISTS

DROP INDEX txi_organization_main_email IF EXISTS

DROP INDEX idx_organization_is_active IF EXISTS

DROP INDEX idx_organization_created_at IF EXISTS
DROP INDEX idx_organization_updated_at IF EXISTS