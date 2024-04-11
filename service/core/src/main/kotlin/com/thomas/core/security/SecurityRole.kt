package com.thomas.core.security

import com.thomas.core.i18n.BundleResolver
import com.thomas.core.security.SecurityRole.RoleStringsI18N.coreRolesDescription
import com.thomas.core.security.SecurityRole.RoleStringsI18N.coreRolesName
import com.thomas.core.security.SecurityRoleSubgroup.FINANCE_DATA
import com.thomas.core.security.SecurityRoleSubgroup.MANAGEMENT_GROUP
import com.thomas.core.security.SecurityRoleSubgroup.MANAGEMENT_USER
import com.thomas.core.security.SecurityRoleSubgroup.MASTER_SUBGROUP

enum class SecurityRole(
    val roleName: () -> String,
    val roleDescription: () -> String,
    val roleCode: Int,
    val roleOrder: Int,
    val roleSubgroup: SecurityRoleSubgroup,
    val roleDisplayable: Boolean
) {

    MASTER({ coreRolesName(MASTER.name.lowercase()) }, { coreRolesDescription(MASTER.name.lowercase()) }, 0, 1, MASTER_SUBGROUP, false),

    ROLE_USER_READ({ coreRolesName(ROLE_USER_READ.name.lowercase()) }, { coreRolesDescription(ROLE_USER_READ.name.lowercase()) }, 1, 1, MANAGEMENT_USER, true),
    ROLE_USER_CREATE({ coreRolesName(ROLE_USER_CREATE.name.lowercase()) }, { coreRolesDescription(ROLE_USER_CREATE.name.lowercase()) }, 2, 2, MANAGEMENT_USER, true),
    ROLE_USER_UPDATE({ coreRolesName(ROLE_USER_UPDATE.name.lowercase()) }, { coreRolesDescription(ROLE_USER_UPDATE.name.lowercase()) }, 3, 3, MANAGEMENT_USER, true),

    ROLE_GROUP_READ({ coreRolesName(ROLE_GROUP_READ.name.lowercase()) }, { coreRolesDescription(ROLE_GROUP_READ.name.lowercase()) }, 4, 1, MANAGEMENT_GROUP, true),
    ROLE_GROUP_CREATE({ coreRolesName(ROLE_GROUP_CREATE.name.lowercase()) }, { coreRolesDescription(ROLE_GROUP_CREATE.name.lowercase()) }, 5, 2, MANAGEMENT_GROUP, true),
    ROLE_GROUP_UPDATE({ coreRolesName(ROLE_GROUP_UPDATE.name.lowercase()) }, { coreRolesDescription(ROLE_GROUP_UPDATE.name.lowercase()) }, 6, 3, MANAGEMENT_GROUP, true),
    ROLE_GROUP_DELETE({ coreRolesName(ROLE_GROUP_DELETE.name.lowercase()) }, { coreRolesDescription(ROLE_GROUP_DELETE.name.lowercase()) }, 7, 4, MANAGEMENT_GROUP, true),

    ROLE_FINANCE_DATA({ coreRolesName(ROLE_FINANCE_DATA.name.lowercase()) }, { coreRolesDescription(ROLE_FINANCE_DATA.name.lowercase()) }, 8, 1, FINANCE_DATA, true);

    companion object {
        fun byCode(code: Int): SecurityRole? =
            entries.firstOrNull { it.roleCode == code }
    }

    object RoleStringsI18N : BundleResolver("strings/core-roles") {

        fun coreRolesName(role: String): String = coreRolesString(role, "name")

        fun coreRolesDescription(role: String): String = coreRolesString(role, "description")

        private fun coreRolesString(role: String, attribute: String): String = getFormattedMessage("core.roles.$role.$attribute")

    }

}
