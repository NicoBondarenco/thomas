package com.thomas.core.model.security

import com.thomas.core.model.security.SecurityMemberRoleGroup.ACCOUNTING
import kotlin.reflect.KClass

enum class SecurityMemberRoleSubgroup(
    override val subgroupGroup: SecurityMemberRoleGroup,
    override val subgroupOrder: Int
) : SecurityRoleSubgroup<SecurityMemberRole, SecurityMemberRoleSubgroup, SecurityMemberRoleGroup> {

    ACCOUNTING_COA(ACCOUNTING, 0);

    override val kclass: KClass<SecurityMemberRole> = SecurityMemberRole::class

}
