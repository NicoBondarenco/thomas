package com.thomas.core.model.security

import com.thomas.core.i18n.BundleResolver
import com.thomas.core.model.security.SecurityRoleGroup.ORGANIZATION_MANAGEMENT
import com.thomas.core.model.security.SecurityRoleGroup.MASTER
import com.thomas.core.model.security.SecurityRoleSubgroup.RoleStringsI18N.coreRolesSubgroupDescription
import com.thomas.core.model.security.SecurityRoleSubgroup.RoleStringsI18N.coreRolesSubgroupName

enum class SecurityRoleSubgroup(
    val subgroupGroup: SecurityRoleGroup,
    val subgroupOrder: Int
) {

    MASTER_SUBGROUP(MASTER, 0),
    ORGANIZATION_MANAGEMENT_USER(ORGANIZATION_MANAGEMENT, 1),
    ORGANIZATION_MANAGEMENT_GROUP(ORGANIZATION_MANAGEMENT, 2),
    ORGANIZATION_MANAGEMENT_HUB(ORGANIZATION_MANAGEMENT, 3),
    HUB_ACCOUNTING_COA(ORGANIZATION_MANAGEMENT, 4);

    val subgroupName: String
        get() = coreRolesSubgroupName(this.name.lowercase())

    val subgroupDescription: String
        get() = coreRolesSubgroupDescription(this.name.lowercase())

    fun roles(): List<SecurityRole> =
        SecurityRole.entries
            .filter { it.roleSubgroup == this }
            .sortedBy { it.roleOrder }

    private object RoleStringsI18N : BundleResolver("strings/core-roles-subgroups") {

        fun coreRolesSubgroupName(subgroup: String): String = coreRoleSubgroupsString(subgroup, "name")

        fun coreRolesSubgroupDescription(subgroup: String): String = coreRoleSubgroupsString(subgroup, "description")

        private fun coreRoleSubgroupsString(subgroup: String, attribute: String): String = formattedMessage("security.role-subgroup.$subgroup.$attribute")

    }

}
