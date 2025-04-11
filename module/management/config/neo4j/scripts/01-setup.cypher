// --------------------------------------------------------------------------------------
// ORGANIZATION NODE DEFINITION
// --------------------------------------------------------------------------------------

CREATE CONSTRAINT upk_organization FOR (o:Organization) REQUIRE o.id IS UNIQUE;

CREATE TEXT INDEX txi_organization_organization_name FOR (o:Organization) ON (o.organization_name);

CREATE TEXT INDEX txi_organization_fantasy_name FOR (o:Organization) ON (o.fantasy_name);

CREATE CONSTRAINT unq_organization_registration_number FOR (o:Organization) REQUIRE o.registration_number IS UNIQUE;
CREATE TEXT INDEX txi_organization_registration_number FOR (o:Organization) ON (o.registration_number);

CREATE TEXT INDEX txi_organization_main_email FOR (o:Organization) ON (o.main_email);

CREATE INDEX idx_organization_is_active FOR (o:Organization) ON (o.is_active);

CREATE INDEX idx_organization_created_at FOR (o:Organization) ON (o.created_at);
CREATE INDEX idx_organization_updated_at FOR (o:Organization) ON (o.updated_at);

// --------------------------------------------------------------------------------------
// UNIT NODE DEFINITION
// --------------------------------------------------------------------------------------

CREATE CONSTRAINT upk_unit FOR (u:Unit) REQUIRE u.id IS UNIQUE;

CREATE TEXT INDEX txi_unit_unit_name FOR (u:Unit) ON (u.unit_name);

CREATE TEXT INDEX txi_unit_fantasy_name FOR (u:Unit) ON (u.fantasy_name);

CREATE TEXT INDEX txi_unit_document_number FOR (u:Unit) ON (u.document_number);

CREATE TEXT INDEX txi_unit_main_email FOR (u:Unit) ON (u.main_email);

CREATE INDEX idx_unit_is_active FOR (u:Unit) ON (u.is_active);

CREATE INDEX idx_unit_created_at FOR (u:Unit) ON (u.created_at);
CREATE INDEX idx_unit_updated_at FOR (u:Unit) ON (u.updated_at);

// --------------------------------------------------------------------------------------
// GROUP NODE DEFINITION
// --------------------------------------------------------------------------------------

CREATE CONSTRAINT upk_group FOR (g:Group) REQUIRE g.id IS UNIQUE;

CREATE TEXT INDEX txi_group_group_name FOR (g:Group) ON (g.group_name);

CREATE TEXT INDEX txi_group_group_description FOR (g:Group) ON (g.group_description);

CREATE INDEX idx_group_is_active FOR (g:Group) ON (g.is_active);

CREATE INDEX idx_group_created_at FOR (g:Group) ON (g.created_at);
CREATE INDEX idx_group_updated_at FOR (g:Group) ON (g.updated_at);

// --------------------------------------------------------------------------------------
// USER NODE DEFINITION
// --------------------------------------------------------------------------------------

CREATE CONSTRAINT upk_user FOR (u:User) REQUIRE u.id IS UNIQUE;

CREATE TEXT INDEX txi_user_first_name FOR (u:User) ON (u.first_name);
CREATE TEXT INDEX txi_user_last_name FOR (u:User) ON (u.last_name);
CREATE TEXT INDEX txi_user_document_number FOR (u:User) ON (u.document_number);
CREATE TEXT INDEX txi_user_main_email FOR (u:User) ON (u.main_email);

CREATE INDEX idx_user_is_active FOR (u:User) ON (u.is_active);

CREATE INDEX idx_user_created_at FOR (u:User) ON (u.created_at);
CREATE INDEX idx_user_updated_at FOR (u:User) ON (u.updated_at);