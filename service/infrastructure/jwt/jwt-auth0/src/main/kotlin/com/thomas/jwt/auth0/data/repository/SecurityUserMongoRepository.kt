package com.thomas.jwt.auth0.data.repository

import com.thomas.core.model.security.SecurityUser
import java.util.UUID

class SecurityUserMongoRepository {

//    companion object {
//        private const val ID_FIELD = "_id"
//        private const val GROUP_FIELD = "userGroups"
//        private const val GROUP_ALIAS = "groupList"
//    }

    fun findSecurityUser(id: UUID): SecurityUser? = TODO("not implemented $id")

}
