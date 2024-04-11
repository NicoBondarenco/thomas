package com.thomas.core.security

import com.thomas.core.i18n.BundleResolver
import com.thomas.core.security.SecurityRoleGroup.FINANCE
import com.thomas.core.security.SecurityRoleGroup.MANAGEMENT
import com.thomas.core.security.SecurityRoleGroup.MASTER
import com.thomas.core.security.SecurityRoleSubgroup.RoleStringsI18N.coreRolesSubgroupDescription
import com.thomas.core.security.SecurityRoleSubgroup.RoleStringsI18N.coreRolesSubgroupName

enum class SecurityRoleSubgroup(
    val subgroupName: () -> String,
    val subgroupDescription: () -> String,
    val subgroupGroup: SecurityRoleGroup,
    val subgroupOrder: Int
) {

    MASTER_SUBGROUP({ coreRolesSubgroupName(MASTER_SUBGROUP.name.lowercase()) }, { coreRolesSubgroupDescription(MASTER_SUBGROUP.name.lowercase()) }, MASTER, 0),
    MANAGEMENT_USER({ coreRolesSubgroupName(MANAGEMENT_USER.name.lowercase()) }, { coreRolesSubgroupDescription(MANAGEMENT_USER.name.lowercase()) }, MANAGEMENT, 1),
    MANAGEMENT_GROUP({ coreRolesSubgroupName(MANAGEMENT_GROUP.name.lowercase()) }, { coreRolesSubgroupDescription(MANAGEMENT_GROUP.name.lowercase()) }, MANAGEMENT, 2),
    FINANCE_DATA({ coreRolesSubgroupName(FINANCE_DATA.name.lowercase()) }, { coreRolesSubgroupDescription(FINANCE_DATA.name.lowercase()) }, FINANCE, 3);

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