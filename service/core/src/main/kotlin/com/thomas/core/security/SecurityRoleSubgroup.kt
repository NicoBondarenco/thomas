package com.thomas.core.security

import com.thomas.core.i18n.BundleResolver
import com.thomas.core.security.SecurityRoleGroup.FINANCE
import com.thomas.core.security.SecurityRoleGroup.MANAGEMENT
import com.thomas.core.security.SecurityRoleGroup.MASTER
import com.thomas.core.security.SecurityRoleSubgroup.RoleStringsI18N.coreRolesSubgroupDescription
import com.thomas.core.security.SecurityRoleSubgroup.RoleStringsI18N.coreRolesSubgroupName

enum class SecurityRoleSubgroup(
    val subgroupGroup: SecurityRoleGroup,
    val subgroupOrder: Int
) {

    MASTER_SUBGROUP(MASTER, 0),
    MANAGEMENT_USER(MANAGEMENT, 1),
    MANAGEMENT_GROUP(MANAGEMENT, 2),
    FINANCE_DATA(FINANCE, 3);

    val subgroupName: String
        get() = coreRolesSubgroupName(this.name.lowercase())

    val subgroupDescription: String
        get() = coreRolesSubgroupDescription(this.name.lowercase())

    fun roles(): List<SecurityRole> =
        SecurityRole.entries
            .filter { it.roleSubgroup == this }
            .sortedBy { it.roleOrder }

    object RoleStringsI18N : BundleResolver("strings/core-roles-subgroups") {

        fun coreRolesSubgroupName(role: String): String = coreRoleSubgroupsString(role, "name")

        fun coreRolesSubgroupDescription(role: String): String = coreRoleSubgroupsString(role, "description")

        private fun coreRoleSubgroupsString(role: String, attribute: String): String = getFormattedMessage("core.role.subgroup.$role.$attribute")

    }

}