package com.thomas.core.model.security

import com.thomas.core.context.SessionContextHolder.currentUnit
import com.thomas.core.model.security.SecurityOrganizationRole.MASTER_ROLE
import com.thomas.core.model.security.SecurityOrganizationRole.ORGANIZATION_ALL
import com.thomas.core.model.security.SecurityUnitRole.UNIT_ALL
import java.util.UUID

interface SecurityInfo {

    companion object {
        private val MASTER_ROLES_SET = setOf(MASTER_ROLE)
        private val ADM_ROLES_SET = setOf(MASTER_ROLE, ORGANIZATION_ALL)
        private val UNIT_ADM_ROLES_SET = setOf(MASTER_ROLE, ORGANIZATION_ALL, UNIT_ALL)
    }

    val securityOrganization: SecurityOrganization
    val securityUnits: Set<SecurityUnit>

    val organizationRoles: Set<SecurityOrganizationRole>
        get() = securityOrganization.organizationRoles

    val unitRoles: Set<SecurityUnitRole>
        get() = currentUnit?.let {
            securityUnits.firstOrNull { u -> u.unitId == it }?.unitRoles
        }?: setOf()

    val unitsRoles: Map<UUID, Set<SecurityUnitRole>>
        get() = securityUnits.associate { u -> u.unitId to u.unitRoles }

    val isMaster: Boolean
        get() = organizationRoles.intersect(MASTER_ROLES_SET).isNotEmpty()

    val isAdministrator: Boolean
        get() = organizationRoles.intersect(ADM_ROLES_SET).isNotEmpty()

    val isUnitAdministrator: Boolean
        get() = (organizationRoles + unitRoles).intersect(UNIT_ADM_ROLES_SET).isNotEmpty()

    val currentRoles: Set<SecurityRole<*, *, *>>
        get() = (organizationRoles + unitRoles).distinct().toSet()

}