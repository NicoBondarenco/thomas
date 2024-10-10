package com.thomas.core.model.security

import com.thomas.core.i18n.BundleResolver
import com.thomas.core.model.security.SecurityRole.RoleStringsI18N.coreRolesDescription
import com.thomas.core.model.security.SecurityRole.RoleStringsI18N.coreRolesName
import com.thomas.core.model.security.SecurityRoleCategory.HUB
import com.thomas.core.model.security.SecurityRoleCategory.ORGANIZATION
import com.thomas.core.model.security.SecurityRoleSubgroup.HUB_ACCOUNTING_COA
import com.thomas.core.model.security.SecurityRoleSubgroup.MASTER_SUBGROUP
import com.thomas.core.model.security.SecurityRoleSubgroup.ORGANIZATION_MANAGEMENT_GROUP
import com.thomas.core.model.security.SecurityRoleSubgroup.ORGANIZATION_MANAGEMENT_HUB
import com.thomas.core.model.security.SecurityRoleSubgroup.ORGANIZATION_MANAGEMENT_USER

enum class SecurityRole(
    val roleCode: Int,
    val roleOrder: Int,
    val roleSubgroup: SecurityRoleSubgroup,
    val roleDisplayable: Boolean,
    val roleCategory: SecurityRoleCategory,
) {

    MASTER(0, 1, MASTER_SUBGROUP, false, ORGANIZATION),

    ROLE_ORGANIZATION_USER_READ(1, 1, ORGANIZATION_MANAGEMENT_USER, true, ORGANIZATION),
    ROLE_ORGANIZATION_USER_CREATE(2, 2, ORGANIZATION_MANAGEMENT_USER, true, ORGANIZATION),
    ROLE_ORGANIZATION_USER_UPDATE(3, 3, ORGANIZATION_MANAGEMENT_USER, true, ORGANIZATION),

    ROLE_ORGANIZATION_GROUP_READ(4, 1, ORGANIZATION_MANAGEMENT_GROUP, true, ORGANIZATION),
    ROLE_ORGANIZATION_GROUP_CREATE(5, 2, ORGANIZATION_MANAGEMENT_GROUP, true, ORGANIZATION),
    ROLE_ORGANIZATION_GROUP_UPDATE(6, 3, ORGANIZATION_MANAGEMENT_GROUP, true, ORGANIZATION),
    ROLE_ORGANIZATION_GROUP_DELETE(7, 4, ORGANIZATION_MANAGEMENT_GROUP, true, ORGANIZATION),

    ROLE_ORGANIZATION_HUB_READ(8, 1, ORGANIZATION_MANAGEMENT_HUB, true, ORGANIZATION),
    ROLE_ORGANIZATION_HUB_CREATE(9, 2, ORGANIZATION_MANAGEMENT_HUB, true, ORGANIZATION),
    ROLE_ORGANIZATION_HUB_UPDATE(10, 3, ORGANIZATION_MANAGEMENT_HUB, true, ORGANIZATION),
    ROLE_ORGANIZATION_HUB_DELETE(11, 4, ORGANIZATION_MANAGEMENT_HUB, true, ORGANIZATION),

    ROLE_HUB_COA_READ(12, 1, HUB_ACCOUNTING_COA, true, HUB),
    ROLE_HUB_COA_CREATE(13, 2, HUB_ACCOUNTING_COA, true, HUB),
    ROLE_HUB_COA_UPDATE(14, 3, HUB_ACCOUNTING_COA, true, HUB),
    ROLE_HUB_COA_DELETE(15, 4, HUB_ACCOUNTING_COA, true, HUB);

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

        private fun coreRolesString(role: String, attribute: String): String = formattedMessage("security.role.$role.$attribute")

    }

}
