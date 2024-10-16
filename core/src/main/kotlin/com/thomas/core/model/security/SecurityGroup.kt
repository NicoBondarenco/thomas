package com.thomas.core.model.security

import com.thomas.core.model.security.SecurityOrganizationRole.ORGANIZATION_ALL
import java.util.UUID

data class SecurityGroup(
    val groupId: UUID,
    val groupName: String,
    val groupOrganization: SecurityOrganization,
    val groupUnits: Set<SecurityUnit>,
) {

    val isMaster: Boolean
        get() = groupOrganization.organizationRoles.contains(ORGANIZATION_ALL)

}
