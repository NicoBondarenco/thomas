package com.thomas.management.domain.crypt

import com.thomas.core.model.security.SecurityUser
import com.thomas.management.domain.model.data.RefreshTokenData
import com.thomas.management.domain.model.request.RefreshTokenRequest

interface Tokenizer {

    suspend fun generateAccessToken(
        securityUser: SecurityUser,
    ): String

    suspend fun generateRefreshToken(
        securityUser: SecurityUser,
    ): String

    suspend fun accessTokenDuration(): Long

    suspend fun refreshTokenDuration(): Long

    suspend fun refreshTokenData(
        refreshToken: RefreshTokenRequest,
    ): RefreshTokenData

}
