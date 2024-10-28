package com.thomas.management.domain.mock

import com.thomas.hasher.Hasher
import io.mockk.coEvery
import io.mockk.mockk
import java.util.UUID

internal val hasherMock: Hasher
    get() = mockk<Hasher>().apply {
        coEvery { generateSalt() } returns UUID.randomUUID().toString()
        coEvery { hash(any(), any()) } answers { "${firstArg() as String}${secondArg() as String}" }
    }