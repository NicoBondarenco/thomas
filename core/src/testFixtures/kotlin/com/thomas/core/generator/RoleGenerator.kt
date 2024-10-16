package com.thomas.core.generator

import com.thomas.core.model.security.SecurityUnitRole
import com.thomas.core.model.security.SecurityOrganizationRole

object RoleGenerator {

    fun generateOrganizationRoles(
        quantity: Int = 4,
    ): Set<SecurityOrganizationRole> = SecurityOrganizationRole.entries.shuffled().take(quantity).toSet()

    fun generateUnitRoles(
        quantity: Int = 4,
    ): Set<SecurityUnitRole> = SecurityUnitRole.entries.shuffled().take(quantity).toSet()

}
