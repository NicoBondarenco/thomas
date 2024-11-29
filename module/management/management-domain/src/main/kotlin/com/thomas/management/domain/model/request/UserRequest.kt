package com.thomas.management.domain.model.request

import com.thomas.core.model.security.SecurityUnitRole
import java.util.UUID

interface UserRequest {

    val userGroups: Set<UUID>

    val userUnits: Map<UUID, Set<SecurityUnitRole>>

}