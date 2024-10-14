package com.thomas.core.model.security

import com.thomas.core.model.security.SecurityOrganizationRoleSubgroup.MANAGEMENT_GROUP
import com.thomas.core.model.security.SecurityOrganizationRoleSubgroup.MANAGEMENT_MEMBER
import com.thomas.core.model.security.SecurityOrganizationRoleSubgroup.MANAGEMENT_USER
import com.thomas.core.model.security.SecurityOrganizationRoleSubgroup.MASTER_SUBGROUP

enum class SecurityOrganizationRole(
    override val roleCode: Int,
    override val roleOrder: Int,
    override val roleSubgroup: SecurityOrganizationRoleSubgroup,
    override val roleDisplayable: Boolean,
) : SecurityRole<SecurityOrganizationRole, SecurityOrganizationRoleSubgroup, SecurityOrganizationRoleGroup> {

    MASTER(0, 1, MASTER_SUBGROUP, false),

    USER_READ(1, 1, MANAGEMENT_USER, true),
    USER_CREATE(2, 2, MANAGEMENT_USER, true),
    USER_UPDATE(3, 3, MANAGEMENT_USER, true),

    GROUP_READ(4, 1, MANAGEMENT_GROUP, true),
    GROUP_CREATE(5, 2, MANAGEMENT_GROUP, true),
    GROUP_UPDATE(6, 3, MANAGEMENT_GROUP, true),
    GROUP_DELETE(7, 4, MANAGEMENT_GROUP, true),

    MEMBER_READ(8, 1, MANAGEMENT_MEMBER, true),
    MEMBER_CREATE(9, 2, MANAGEMENT_MEMBER, true),
    MEMBER_UPDATE(10, 3, MANAGEMENT_MEMBER, true),
    MEMBER_DELETE(11, 4, MANAGEMENT_MEMBER, true);

    companion object {
        fun byCode(code: Int): SecurityOrganizationRole? =
            entries.firstOrNull { it.roleCode == code }
    }

}
