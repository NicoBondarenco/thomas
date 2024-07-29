package com.thomas.authentication.data.repository

import com.thomas.authentication.data.entity.GroupAuthenticationEntity
import java.util.UUID

interface GroupAuthenticationRepository {

    fun one(id: UUID): GroupAuthenticationEntity?

    fun create(entity: GroupAuthenticationEntity): GroupAuthenticationEntity

    fun update(entity: GroupAuthenticationEntity): GroupAuthenticationEntity

    fun delete(id: UUID)

}
