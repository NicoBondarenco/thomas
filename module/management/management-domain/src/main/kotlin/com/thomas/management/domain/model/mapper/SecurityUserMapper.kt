package com.thomas.management.domain.model.mapper

import com.thomas.core.model.security.SecurityGroup
import com.thomas.core.model.security.SecurityOrganization
import com.thomas.core.model.security.SecurityOrganizationRole
import com.thomas.core.model.security.SecurityUnit
import com.thomas.core.model.security.SecurityUser
import com.thomas.management.data.entity.GroupCompleteEntity
import com.thomas.management.data.entity.OrganizationEntity
import com.thomas.management.data.entity.UnitRoleEntity
import com.thomas.management.data.entity.UserCompleteEntity
import kotlinx.coroutines.coroutineScope

suspend fun UserCompleteEntity.toSecurityUser() = coroutineScope {

    this@toSecurityUser.let { user ->
        SecurityUser(
            userId = user.id,
            firstName = user.firstName,
            lastName = user.lastName,
            mainEmail = user.mainEmail,
            phoneNumber = user.mainPhone,
            profilePhoto = user.profilePhoto,
            birthDate = user.birthDate,
            userGender = user.userGender,
            userRace = user.userRace,
            userType = user.userType,
            isActive = user.isActive,
            securityOrganization = user.userOrganization.toSecurityOrganization(user.organizationRoles),
            userGroups = this@toSecurityUser.userGroups.map { it.toSecurityGroup() }.toSet(),
            securityUnits = this@toSecurityUser.userUnits.toSecurityUnit(),
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
    SecurityGroup(
        groupId = this@toSecurityGroup.id,
        groupName = this@toSecurityGroup.groupName,
        securityOrganization = this@toSecurityGroup.groupOrganization.toSecurityOrganization(this@toSecurityGroup.organizationRoles),
        securityUnits = this@toSecurityGroup.groupUnits.toSecurityUnit(),
    )
}

private suspend fun Set<UnitRoleEntity>.toSecurityUnit() = coroutineScope {
    this@toSecurityUnit.map {
        SecurityUnit(
            unitId = it.roleUnit.id,
            unitName = it.roleUnit.unitName,
            unitRoles = it.roleList,
        )
    }.toSet()
}
