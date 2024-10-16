package com.thomas.core.model.security

import com.thomas.core.model.security.SecurityOrganizationRoleSubgroup.MANAGEMENT_GROUP
import com.thomas.core.model.security.SecurityOrganizationRoleSubgroup.MANAGEMENT_UNIT
import com.thomas.core.model.security.SecurityOrganizationRoleSubgroup.MANAGEMENT_USER
import com.thomas.core.model.security.SecurityOrganizationRoleSubgroup.ORGANIZATION_SUBGROUP

enum class SecurityOrganizationRole(
    override val roleCode: Int,
    override val roleOrder: Int,
    override val roleSubgroup: SecurityOrganizationRoleSubgroup,
) : SecurityRole<SecurityOrganizationRole, SecurityOrganizationRoleSubgroup, SecurityOrganizationRoleGroup> {

    ORGANIZATION_ALL(0, 1, ORGANIZATION_SUBGROUP),

    USER_READ(1, 1, MANAGEMENT_USER),
    USER_CREATE(2, 2, MANAGEMENT_USER),
    USER_UPDATE(3, 3, MANAGEMENT_USER),

    GROUP_READ(4, 1, MANAGEMENT_GROUP),
    GROUP_CREATE(5, 2, MANAGEMENT_GROUP),
    GROUP_UPDATE(6, 3, MANAGEMENT_GROUP),
    GROUP_DELETE(7, 4, MANAGEMENT_GROUP),

    UNIT_READ(8, 1, MANAGEMENT_UNIT),
    UNIT_CREATE(9, 2, MANAGEMENT_UNIT),
    UNIT_UPDATE(10, 3, MANAGEMENT_UNIT),
    UNIT_DELETE(11, 4, MANAGEMENT_UNIT);

    companion object {
        fun byCode(code: Int): SecurityOrganizationRole? =
            entries.firstOrNull { it.roleCode == code }
    }

}
