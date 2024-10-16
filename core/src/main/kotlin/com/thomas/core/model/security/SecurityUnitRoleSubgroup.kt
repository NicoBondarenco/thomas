package com.thomas.core.model.security

import com.thomas.core.model.security.SecurityUnitRoleGroup.ACCOUNTING
import kotlin.reflect.KClass

enum class SecurityUnitRoleSubgroup(
    override val subgroupGroup: SecurityUnitRoleGroup,
    override val subgroupOrder: Int
) : SecurityRoleSubgroup<SecurityUnitRole, SecurityUnitRoleSubgroup, SecurityUnitRoleGroup> {

    ACCOUNTING_COA(ACCOUNTING, 0);

    override val kclass: KClass<SecurityUnitRole> = SecurityUnitRole::class

}
