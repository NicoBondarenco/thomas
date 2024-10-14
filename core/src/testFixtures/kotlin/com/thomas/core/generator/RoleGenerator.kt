package com.thomas.core.generator

import com.thomas.core.model.security.SecurityMemberRole
import com.thomas.core.model.security.SecurityOrganizationRole

object RoleGenerator {

    fun generateOrganizationRoles(
        quantity: Int = 4,
    ): Set<SecurityOrganizationRole> = SecurityOrganizationRole.entries.shuffled().take(quantity).toSet()

    fun generateMemberRoles(
        quantity: Int = 4,
    ): Set<SecurityMemberRole> = SecurityMemberRole.entries.shuffled().take(quantity).toSet()

}
