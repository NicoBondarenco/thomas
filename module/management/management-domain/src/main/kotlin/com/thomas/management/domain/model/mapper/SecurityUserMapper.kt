package com.thomas.management.domain.model.mapper

import com.thomas.core.model.security.SecurityGroup
import com.thomas.core.model.security.SecurityOrganization
import com.thomas.core.model.security.SecurityOrganizationRole
import com.thomas.core.model.security.SecurityUnit
import com.thomas.core.model.security.SecurityUnitRole
import com.thomas.core.model.security.SecurityUser
import com.thomas.management.data.entity.GroupCompleteEntity
import com.thomas.management.data.entity.OrganizationEntity
import com.thomas.management.data.entity.UnitEntity
import com.thomas.management.data.entity.UserCompleteEntity
import kotlinx.coroutines.coroutineScope

suspend fun UserCompleteEntity.toSecurityUser() = coroutineScope {

    this@toSecurityUser.userData.let { user ->
        SecurityUser(
            userId = user.id,
            firstName = user.firstName,
            lastName = user.lastName,
            mainEmail = user.mainEmail,
            phoneNumber = user.mainPhone,
            profilePhoto = user.profilePhoto,
            birthDate = user.birthDate,
            userGender = user.userGender,
            isActive = user.isActive,
            userOrganization = user.userOrganization.toSecurityOrganization(user.organizationRoles),
            userGroups = this@toSecurityUser.userGroups.map { it.toSecurityGroup() }.toSet(),
            userUnits = this@toSecurityUser.userUnits.toSecurityUnit(),
        )
    }
}

private suspend fun OrganizationEntity.toSecurityOrganization(
    organizationRoles: Set<SecurityOrganizationRole>
) = coroutineScope {
    SecurityOrganization(
        organizationId = this@toSecurityOrganization.id,
        organizationName = this@toSecurityOrganization.organizationName,
        organizationRoles = organizationRoles,
    )
}

private suspend fun GroupCompleteEntity.toSecurityGroup() = coroutineScope {
    this@toSecurityGroup.groupData.let { group ->
        SecurityGroup(
            groupId = group.id,
            groupName = group.groupName,
            groupOrganization = group.groupOrganization.toSecurityOrganization(group.organizationRoles),
            groupUnits = this@toSecurityGroup.groupUnits.toSecurityUnit(),
        )
    }
}

private suspend fun Map<UnitEntity, Set<SecurityUnitRole>>.toSecurityUnit() = coroutineScope {
    this@toSecurityUnit.map { (unit, roles) ->
        SecurityUnit(
            unitId = unit.id,
            unitName = unit.unitName,
            unitRoles = roles,
        )
    }.toSet()
}
