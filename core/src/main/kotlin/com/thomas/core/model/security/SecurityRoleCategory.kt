package com.thomas.core.model.security

import com.thomas.core.i18n.BundleResolver
import com.thomas.core.model.security.SecurityRoleCategory.RoleCategoryStringsI18N.coreRolesCategoryDescription
import com.thomas.core.model.security.SecurityRoleCategory.RoleCategoryStringsI18N.coreRolesCategoryName

enum class SecurityRoleCategory {

    ORGANIZATION,
    HUB;

    val categoryName: String
        get() = coreRolesCategoryName(this.name.lowercase())

    val categoryDescription: String
        get() = coreRolesCategoryDescription(this.name.lowercase())

    private object RoleCategoryStringsI18N : BundleResolver("strings/core-roles-categories") {

        fun coreRolesCategoryName(category: String): String = coreRoleCategoryString(category, "name")

        fun coreRolesCategoryDescription(category: String): String = coreRoleCategoryString(category, "description")

        private fun coreRoleCategoryString(category: String, attribute: String): String = formattedMessage("security.role-category.$category.$attribute")

    }

}
