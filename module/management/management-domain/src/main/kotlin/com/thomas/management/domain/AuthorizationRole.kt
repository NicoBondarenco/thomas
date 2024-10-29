package com.thomas.management.domain

import com.thomas.core.model.security.SecurityOrganizationRole.MASTER_ROLE
import com.thomas.core.model.security.SecurityRole

val organizationUpsertRoles = arrayOf<SecurityRole<*, *, *>>(
    MASTER_ROLE
)