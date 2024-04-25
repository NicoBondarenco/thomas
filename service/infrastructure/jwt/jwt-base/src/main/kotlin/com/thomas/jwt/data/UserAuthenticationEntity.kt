package com.thomas.jwt.data

import com.thomas.core.model.entity.BaseEntity
import java.util.UUID

data class UserAuthenticationEntity(
    override val id: UUID
) : BaseEntity<UserAuthenticationEntity>()
