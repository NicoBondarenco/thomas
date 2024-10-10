package com.thomas.core.generator

import com.thomas.core.model.security.SecurityRole
import com.thomas.core.model.security.SecurityRoleCategory

object RoleGenerator {

    fun generateRoles(
        category: SecurityRoleCategory,
        quantity: Int = 4,
    ): Set<SecurityRole> = SecurityRole.entries.filter { it.roleCategory == category }.shuffled().take(quantity).toSet()

}
