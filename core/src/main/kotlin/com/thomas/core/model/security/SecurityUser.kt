package com.thomas.core.model.security

import com.thomas.core.context.SessionContextHolder.currentHub
import com.thomas.core.model.general.Gender
import com.thomas.core.model.security.SecurityRole.MASTER
import java.time.LocalDate
import java.util.UUID

data class SecurityUser(
    val userId: UUID,
    val firstName: String,
    val lastName: String,
    val mainEmail: String,
    val phoneNumber: String? = null,
    val profilePhoto: String? = null,
    val birthDate: LocalDate? = null,
    val userGender: Gender? = null,
    val isActive: Boolean,
    val userOrganization: SecurityOrganization,
    val userGroups: Set<SecurityGroup> = setOf(),
    val userHubs: Set<SecurityHub> = setOf(),
) {

    val fullName: String
        get() = "$firstName $lastName"

    val shortName: String
        get() = "$firstName ${lastName[0]}."

    val alternativeName: String
        get() = "${firstName[0]}. $lastName"

    val isMaster: Boolean
        get() = userOrganization.organizationRoles.contains(MASTER) || groupsHasMasterRole()

    val organizationRoles: Set<SecurityRole>
        get() = (userOrganization.organizationRoles + userGroups.organizationRoles()).distinct().toSet()

    val hubRoles: Set<SecurityRole>
        get() = currentHub?.let {
            (userHubs.hubRoles(it) + userGroups.hubRoles(it)).distinct().toSet()
        } ?: emptySet()

    val currentRoles: Set<SecurityRole>
        get() = (organizationRoles + hubRoles).distinct().toSet()

    private fun groupsHasMasterRole() = userGroups.any { group -> group.isMaster }

    private fun Set<SecurityHub>.hubRoles(
        hubId: UUID
    ): Set<SecurityRole> = this.firstOrNull { it.hubId == hubId }?.hubRoles ?: setOf()

    private fun Set<SecurityGroup>.organizationRoles() = this.map { it.groupOrganization.organizationRoles }.flatten()

    private fun Set<SecurityGroup>.hubRoles(
        hubId: UUID
    ) = this.map { it.groupHubs.hubRoles(hubId) }.flatten()

}
