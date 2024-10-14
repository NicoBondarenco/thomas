package com.thomas.core.model.security

import com.thomas.core.model.security.SecurityRoleGroupCategory.MEMBER
import kotlin.reflect.KClass

enum class SecurityMemberRoleGroup(
    override val groupOrder: Int
) : SecurityRoleGroup<SecurityMemberRole, SecurityMemberRoleSubgroup, SecurityMemberRoleGroup> {

    ACCOUNTING(0);

    override val kclass: KClass<SecurityMemberRoleSubgroup> = SecurityMemberRoleSubgroup::class

    override val groupCategory: SecurityRoleGroupCategory = MEMBER

}
