package com.thomas.core.model.security

import com.thomas.core.model.security.SecurityUnitRoleSubgroup.ACCOUNTING_COA

enum class SecurityUnitRole(
    override val roleCode: Int,
    override val roleOrder: Int,
    override val roleSubgroup: SecurityUnitRoleSubgroup,
) : SecurityRole<SecurityUnitRole, SecurityUnitRoleSubgroup, SecurityUnitRoleGroup> {

    COA_READ(0, 1, ACCOUNTING_COA),
    COA_CREATE(1, 2, ACCOUNTING_COA),
    COA_UPDATE(2, 3, ACCOUNTING_COA),
    COA_DELETE(3, 4, ACCOUNTING_COA);

    companion object {
        fun byCode(code: Int): SecurityUnitRole? =
            entries.firstOrNull { it.roleCode == code }
    }

}
