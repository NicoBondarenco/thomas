package com.thomas.core.security

import com.thomas.core.i18n.BundleResolver
import com.thomas.core.security.SecurityRoleGroup.RoleStringsI18N.coreRolesGroupDescription
import com.thomas.core.security.SecurityRoleGroup.RoleStringsI18N.coreRolesGroupName

enum class SecurityRoleGroup(
    val groupName: () -> String,
    val groupDescription: () -> String,
    val groupOrder: Int
) {

    MASTER({ coreRolesGroupName(MASTER.name.lowercase()) }, { coreRolesGroupDescription(MASTER.name.lowercase()) }, 0),
    MANAGEMENT({ coreRolesGroupName(MANAGEMENT.name.lowercase()) }, { coreRolesGroupDescription(MANAGEMENT.name.lowercase()) }, 1),
    FINANCE({ coreRolesGroupName(FINANCE.name.lowercase()) }, { coreRolesGroupDescription(FINANCE.name.lowercase()) }, 2);

    fun subgroups(): List<SecurityRoleSubgroup> =
        SecurityRoleSubgroup.entries
            .filter { it.subgroupGroup == this }
            .sortedBy { it.subgroupOrder }

    object RoleStringsI18N : BundleResolver("strings/core-roles-groups") {

        fun coreRolesGroupName(role: String): String = coreRoleGroupsString(role, "name")

        fun coreRolesGroupDescription(role: String): String = coreRoleGroupsString(role, "description")

        private fun coreRoleGroupsString(role: String, attribute: String): String = getFormattedMessage("core.role.group.$role.$attribute")

    }

}