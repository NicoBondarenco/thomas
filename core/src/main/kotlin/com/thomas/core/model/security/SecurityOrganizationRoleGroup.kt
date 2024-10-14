package com.thomas.core.model.security

import com.thomas.core.model.security.SecurityRoleGroupCategory.ORGANIZATION
import kotlin.reflect.KClass

enum class SecurityOrganizationRoleGroup(
    override val groupOrder: Int
) : SecurityRoleGroup<SecurityOrganizationRole, SecurityOrganizationRoleSubgroup, SecurityOrganizationRoleGroup> {

    MASTER(0),
    MANAGEMENT(1);

    override val kclass: KClass<SecurityOrganizationRoleSubgroup> = SecurityOrganizationRoleSubgroup::class

    override val groupCategory: SecurityRoleGroupCategory = ORGANIZATION

}
