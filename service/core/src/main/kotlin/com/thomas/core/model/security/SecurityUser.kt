package com.thomas.core.model.security

import com.thomas.core.model.general.Gender
import com.thomas.core.model.general.UserProfile
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
    val userProfile: UserProfile,
    val isActive: Boolean,
    val userRoles: List<SecurityRole> = listOf(),
    val userGroups: List<SecurityGroup> = listOf()
) {

    val fullName: String
        get() = "$firstName $lastName"

    val shortName: String
        get() = "$firstName ${lastName[0]}."

    val alternativeName: String
        get() = "${firstName[0]}. $lastName"

    val isMaster: Boolean
        get() = userRoles.any { it == MASTER } || groupsHasMasterRole()

    fun currentRoles(): List<SecurityRole> {
        val roles = mutableListOf<SecurityRole>()

        roles.addAll(userRoles)
        userGroups.forEach { roles.addAll(it.groupRoles) }

        return roles.distinct()
    }

    private fun groupsHasMasterRole() = userGroups.any { group -> group.groupRoles.any { it == MASTER } }

}
