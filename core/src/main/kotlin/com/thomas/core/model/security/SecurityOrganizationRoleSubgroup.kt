package com.thomas.core.model.security

import com.thomas.core.model.security.SecurityOrganizationRoleGroup.MANAGEMENT
import com.thomas.core.model.security.SecurityOrganizationRoleGroup.ORGANIZATION
import kotlin.reflect.KClass

enum class SecurityOrganizationRoleSubgroup(
    override val subgroupGroup: SecurityOrganizationRoleGroup,
    override val subgroupOrder: Int
) : SecurityRoleSubgroup<SecurityOrganizationRole, SecurityOrganizationRoleSubgroup, SecurityOrganizationRoleGroup> {

    ORGANIZATION_SUBGROUP(ORGANIZATION, 0),
    MANAGEMENT_USER(MANAGEMENT, 1),
    MANAGEMENT_GROUP(MANAGEMENT, 2),
    MANAGEMENT_UNIT(MANAGEMENT, 3);

    override val kclass: KClass<SecurityOrganizationRole> = SecurityOrganizationRole::class

}
