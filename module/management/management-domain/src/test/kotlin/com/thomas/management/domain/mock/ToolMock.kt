package com.thomas.management.domain.mock

import com.thomas.management.domain.crypt.Hasher
import com.thomas.management.domain.model.data.RefreshTokenData
import com.thomas.management.domain.model.request.RefreshTokenRequest
import com.thomas.management.domain.crypt.Tokenizer
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.mockk
import java.util.UUID

internal val hasherMock: Hasher
    get() = mockk<Hasher>().apply {
        coEvery { generateSalt() } returns UUID.randomUUID().toString()
        coEvery { hash(any(), any()) } answers { "${firstArg() as String}${secondArg() as String}" }
    }

fun clearHasherMocks() = clearMocks(
    hasherMock,
    answers = false,
    recordedCalls = true,
    childMocks = false,
    verificationMarks = true,
    exclusionRules = false,
)

internal val tokenizerMock: Tokenizer
    get() = mockk<Tokenizer>().apply {
        coEvery { generateAccessToken(any()) } returns UUID.randomUUID().toString()
        coEvery { generateRefreshToken(any()) } returns UUID.randomUUID().toString()
        coEvery { accessTokenDuration() } returns 3600
        coEvery { refreshTokenDuration() } returns 3600
        coEvery { refreshTokenData(any()) } answers {
            firstArg<RefreshTokenRequest>().refreshToken.split("_").let {
                RefreshTokenData(
                    username = it[0],
                    organization = it[1],
                )
            }
        }
    }
