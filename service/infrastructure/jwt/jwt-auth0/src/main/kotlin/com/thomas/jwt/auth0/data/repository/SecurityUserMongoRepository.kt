package com.thomas.jwt.auth0.data.repository

import com.thomas.core.model.security.SecurityUser
import com.thomas.jwt.configuration.JWTConfiguration
import java.util.UUID


class SecurityUserMongoRepository(
    configuration: JWTConfiguration
) {

    companion object {
        private const val ID_FIELD = "_id"
        private const val GROUP_FIELD = "userGroups"
        private const val GROUP_ALIAS = "groupList"
    }

    fun findSecurityUser(id: UUID): SecurityUser? = TODO()


}