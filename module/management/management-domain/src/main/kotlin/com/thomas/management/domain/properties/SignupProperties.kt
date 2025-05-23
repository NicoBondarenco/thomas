package com.thomas.management.domain.properties

import com.thomas.core.model.general.UserType
import com.thomas.core.model.security.SecurityOrganizationRole

data class SignupProperties(
    val signupEnabled: Boolean,
    val maxUnits: Int,
    val maxUsers: Int,
    val defaultType: UserType,
    val defaultRoles: Set<SecurityOrganizationRole>,
)
