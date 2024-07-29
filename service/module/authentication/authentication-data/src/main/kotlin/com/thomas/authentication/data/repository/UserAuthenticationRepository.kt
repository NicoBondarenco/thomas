package com.thomas.authentication.data.repository

import com.thomas.authentication.data.entity.UserAuthenticationCompleteEntity
import com.thomas.authentication.data.entity.UserAuthenticationEntity
import java.util.UUID

interface UserAuthenticationRepository {

    fun one(id: UUID): UserAuthenticationEntity?

    fun create(entity: UserAuthenticationEntity): UserAuthenticationEntity

    fun update(entity: UserAuthenticationEntity): UserAuthenticationEntity

    fun findByUsername(username: String): UserAuthenticationCompleteEntity?

}
