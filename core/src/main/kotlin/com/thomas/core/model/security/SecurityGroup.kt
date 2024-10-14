package com.thomas.core.model.security

import com.thomas.core.model.security.SecurityOrganizationRole.MASTER
import java.util.UUID

data class SecurityGroup(
    val groupId: UUID,
    val groupName: String,
    val groupOrganization: SecurityOrganization,
    val groupMembers: Set<SecurityMember> = setOf(),
) {

    val isMaster: Boolean
        get() = groupOrganization.organizationRoles.contains(MASTER)

}
