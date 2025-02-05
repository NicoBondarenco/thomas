package com.thomas.management.domain.token

import com.thomas.core.model.security.SecurityUser
import com.thomas.management.domain.model.data.RefreshTokenData
import com.thomas.management.domain.model.request.RefreshTokenRequest

interface Tokenizer {

    suspend fun generateAccessToken(
        securityUser: SecurityUser,
        durationSeconds: Long,
    ): String

    suspend fun generateRefreshToken(
        securityUser: SecurityUser,
        durationSeconds: Long,
    ): String

    suspend fun refreshTokenData(
        refreshToken: RefreshTokenRequest,
    ): RefreshTokenData

}
