package com.thomas.authentication.data.repository

import com.thomas.authentication.data.entity.RefreshTokenCompleteEntity
import com.thomas.authentication.data.entity.RefreshTokenEntity

interface RefreshTokenRepository {

    fun findByToken(token: String): RefreshTokenCompleteEntity?

    fun upsert(entity: RefreshTokenEntity): RefreshTokenEntity

}
