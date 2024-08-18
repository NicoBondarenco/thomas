package com.thomas.core.model.security

import com.thomas.core.i18n.BundleResolver
import com.thomas.core.model.security.SecurityRole.RoleStringsI18N.coreRolesDescription
import com.thomas.core.model.security.SecurityRole.RoleStringsI18N.coreRolesName
import com.thomas.core.model.security.SecurityRoleSubgroup.FINANCE_DATA
import com.thomas.core.model.security.SecurityRoleSubgroup.MANAGEMENT_GROUP
import com.thomas.core.model.security.SecurityRoleSubgroup.MANAGEMENT_USER
import com.thomas.core.model.security.SecurityRoleSubgroup.MASTER_SUBGROUP

enum class SecurityRole(
    val roleCode: Int,
    val roleOrder: Int,
    val roleSubgroup: SecurityRoleSubgroup,
    val roleDisplayable: Boolean
) {

    MASTER(0, 1, MASTER_SUBGROUP, false),

    ROLE_USER_READ(1, 1, MANAGEMENT_USER, true),
    ROLE_USER_CREATE(2, 2, MANAGEMENT_USER, true),
    ROLE_USER_UPDATE(3, 3, MANAGEMENT_USER, true),

    ROLE_GROUP_READ(4, 1, MANAGEMENT_GROUP, true),
    ROLE_GROUP_CREATE(5, 2, MANAGEMENT_GROUP, true),
    ROLE_GROUP_UPDATE(6, 3, MANAGEMENT_GROUP, true),
    ROLE_GROUP_DELETE(7, 4, MANAGEMENT_GROUP, true);

    companion object {
        fun byCode(code: Int): SecurityRole? =
            entries.firstOrNull { it.roleCode == code }
    }

    val roleName: String
        get() = coreRolesName(this.name.lowercase())

    val roleDescription: String
        get() = coreRolesDescription(this.name.lowercase())

    private object RoleStringsI18N : BundleResolver("strings/core-roles") {

        fun coreRolesName(role: String): String = coreRolesString(role, "name")

        fun coreRolesDescription(role: String): String = coreRolesString(role, "description")

        private fun coreRolesString(role: String, attribute: String): String = getFormattedMessage("security.role.$role.$attribute")

    }

}
