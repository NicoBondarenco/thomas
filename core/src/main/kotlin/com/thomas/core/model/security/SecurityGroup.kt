package com.thomas.core.model.security

import com.thomas.core.model.security.SecurityRole.MASTER
import java.util.UUID

data class SecurityGroup(
    val groupId: UUID,
    val groupName: String,
    val groupOrganization: SecurityOrganization,
    val groupHubs: Set<SecurityHub> = setOf(),
){

    val isMaster: Boolean
        get() = groupOrganization.organizationRoles.contains(MASTER)

}
