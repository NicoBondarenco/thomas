package com.thomas.management.domain.model.request

import java.util.UUID

interface UserBaseRequest {

    val userGroups: Set<UUID>

}
