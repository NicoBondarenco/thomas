package com.thomas.management.domain

import com.thomas.core.model.security.SecurityOrganizationRole.MASTER_ROLE
import com.thomas.core.model.security.SecurityOrganizationRole.ORGANIZATION_ALL
import com.thomas.core.model.security.SecurityOrganizationRole.UNIT_CREATE
import com.thomas.core.model.security.SecurityOrganizationRole.UNIT_DELETE
import com.thomas.core.model.security.SecurityOrganizationRole.UNIT_READ
import com.thomas.core.model.security.SecurityOrganizationRole.UNIT_UPDATE
import com.thomas.core.model.security.SecurityRole

val organizationUpsertRoles = arrayOf<SecurityRole<*, *, *>>(
    MASTER_ROLE,
)

val unitReadRoles = arrayOf<SecurityRole<*, *, *>>(
    MASTER_ROLE,
    ORGANIZATION_ALL,
    UNIT_READ,
)

val unitCreateRoles = arrayOf<SecurityRole<*, *, *>>(
    MASTER_ROLE,
    ORGANIZATION_ALL,
    UNIT_CREATE,
)

val unitUpdateRoles = arrayOf<SecurityRole<*, *, *>>(
    MASTER_ROLE,
    ORGANIZATION_ALL,
    UNIT_UPDATE,
)

val unitDeleteRoles = arrayOf<SecurityRole<*, *, *>>(
    MASTER_ROLE,
    ORGANIZATION_ALL,
    UNIT_DELETE,
)
