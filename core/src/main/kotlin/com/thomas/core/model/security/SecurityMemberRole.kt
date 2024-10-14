package com.thomas.core.model.security

import com.thomas.core.model.security.SecurityMemberRoleSubgroup.ACCOUNTING_COA

enum class SecurityMemberRole(
    override val roleCode: Int,
    override val roleOrder: Int,
    override val roleSubgroup: SecurityMemberRoleSubgroup,
    override val roleDisplayable: Boolean,
) : SecurityRole<SecurityMemberRole, SecurityMemberRoleSubgroup, SecurityMemberRoleGroup> {

    COA_READ(0, 1, ACCOUNTING_COA, true),
    COA_CREATE(1, 2, ACCOUNTING_COA, true),
    COA_UPDATE(2, 3, ACCOUNTING_COA, true),
    COA_DELETE(3, 4, ACCOUNTING_COA, true);

    companion object {
        fun byCode(code: Int): SecurityMemberRole? =
            entries.firstOrNull { it.roleCode == code }
    }

}
