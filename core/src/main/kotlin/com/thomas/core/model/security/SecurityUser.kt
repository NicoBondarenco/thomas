package com.thomas.core.model.security

import com.thomas.core.context.SessionContextHolder.currentMember
import com.thomas.core.model.general.Gender
import com.thomas.core.model.security.SecurityOrganizationRole.MASTER
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
    val userMembers: Set<SecurityMember> = setOf(),
) {

    val fullName: String
        get() = "$firstName $lastName"

    val shortName: String
        get() = "$firstName ${lastName[0]}."

    val alternativeName: String
        get() = "${firstName[0]}. $lastName"

    val isMaster: Boolean
        get() = userOrganization.organizationRoles.contains(MASTER) || groupsHasMasterRole()

    val organizationRoles: Set<SecurityOrganizationRole>
        get() = (userOrganization.organizationRoles + userGroups.organizationRoles()).distinct().toSet()

    val memberRoles: Set<SecurityMemberRole>
        get() = currentMember?.let {
            (userMembers.memberRoles(it) + userGroups.memberRoles(it)).distinct().toSet()
        } ?: emptySet()

    val currentRoles: Set<SecurityRole<*, *, *>>
        get() = (organizationRoles + memberRoles).distinct().toSet()

    private fun groupsHasMasterRole() = userGroups.any { group -> group.isMaster }

    private fun Set<SecurityMember>.memberRoles(
        memberId: UUID
    ): Set<SecurityMemberRole> = this.firstOrNull { it.memberId == memberId }?.memberRoles ?: setOf()

    private fun Set<SecurityGroup>.organizationRoles() = this.map { it.groupOrganization.organizationRoles }.flatten()

    private fun Set<SecurityGroup>.memberRoles(
        memberId: UUID
    ) = this.map { it.groupMembers.memberRoles(memberId) }.flatten()

}
