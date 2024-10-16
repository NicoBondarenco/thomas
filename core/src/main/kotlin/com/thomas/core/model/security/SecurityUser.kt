package com.thomas.core.model.security

import com.thomas.core.context.SessionContextHolder.currentUnit
import com.thomas.core.model.general.Gender
import com.thomas.core.model.security.SecurityOrganizationRole.ORGANIZATION_ALL
import java.time.LocalDate
import java.util.UUID

data class SecurityUser(
    val userId: UUID,
    val firstName: String,
    val lastName: String,
    val mainEmail: String,
    val phoneNumber: String?,
    val profilePhoto: String?,
    val birthDate: LocalDate?,
    val userGender: Gender?,
    val isActive: Boolean,
    val userOrganization: SecurityOrganization,
    val userGroups: Set<SecurityGroup>,
    val userUnits: Set<SecurityUnit>,
) {

    val fullName: String
        get() = "$firstName $lastName"

    val shortName: String
        get() = "$firstName ${lastName[0]}."

    val alternativeName: String
        get() = "${firstName[0]}. $lastName"

    val isMaster: Boolean
        get() = userOrganization.organizationRoles.contains(ORGANIZATION_ALL) || groupsHasMasterRole()

    val organizationRoles: Set<SecurityOrganizationRole>
        get() = (userOrganization.organizationRoles + userGroups.organizationRoles()).distinct().toSet()

    val unitRoles: Set<SecurityUnitRole>
        get() = currentUnit?.let {
            (userUnits.unitRoles(it) + userGroups.unitRoles(it)).distinct().toSet()
        } ?: emptySet()

    val currentRoles: Set<SecurityRole<*, *, *>>
        get() = (organizationRoles + unitRoles).distinct().toSet()

    private fun groupsHasMasterRole() = userGroups.any { group -> group.isMaster }

    private fun Set<SecurityUnit>.unitRoles(
        unitId: UUID
    ): Set<SecurityUnitRole> = this.firstOrNull { it.unitId == unitId }?.unitRoles ?: setOf()

    private fun Set<SecurityGroup>.organizationRoles() = this.map { it.groupOrganization.organizationRoles }.flatten()

    private fun Set<SecurityGroup>.unitRoles(
        unitId: UUID
    ) = this.map { it.groupUnits.unitRoles(unitId) }.flatten()

}
