package com.thomas.core.model.security

import com.thomas.core.i18n.BundleResolver
import com.thomas.core.model.security.SecurityRoleGroup.RoleStringsI18N.coreRolesGroupDescription
import com.thomas.core.model.security.SecurityRoleGroup.RoleStringsI18N.coreRolesGroupName

enum class SecurityRoleGroup(
    val groupOrder: Int
) {

    MASTER(0),
    MANAGEMENT(1);

    val groupName: String
        get() = coreRolesGroupName(this.name.lowercase())

    val groupDescription: String
        get() = coreRolesGroupDescription(this.name.lowercase())

    fun subgroups(): List<SecurityRoleSubgroup> =
        SecurityRoleSubgroup.entries
            .filter { it.subgroupGroup == this }
            .sortedBy { it.subgroupOrder }

    object RoleStringsI18N : BundleResolver("strings/core-roles-groups") {

        fun coreRolesGroupName(role: String): String = coreRoleGroupsString(role, "name")

        fun coreRolesGroupDescription(role: String): String = coreRoleGroupsString(role, "description")

        private fun coreRoleGroupsString(role: String, attribute: String): String = getFormattedMessage("security.role-group.$role.$attribute")

    }

}
