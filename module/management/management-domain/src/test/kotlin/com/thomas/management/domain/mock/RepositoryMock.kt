package com.thomas.management.domain.mock

import com.thomas.management.data.repository.OrganizationRepository
import com.thomas.management.data.repository.SignupRepository
import com.thomas.management.data.repository.UserRepository
import io.mockk.coEvery
import io.mockk.mockk

internal val organizationNames: MutableList<String> = mutableListOf()

internal val organizationRegistration: MutableList<String> = mutableListOf()

internal val userEmails: MutableList<String> = mutableListOf()

internal val signupRepositoryMock: SignupRepository
    get() = mockk<SignupRepository>().apply {
        coEvery { signup(any()) } answers { firstArg() }
    }

internal val organizationRepositoryMock: OrganizationRepository
    get() = mockk<OrganizationRepository>().apply {
        coEvery { hasAnotherWithName(any(), any()) } answers {
            organizationNames.contains(secondArg() as String)
        }
        coEvery { hasAnotherWithRegistration(any(), any()) } answers {
            organizationRegistration.contains(secondArg() as String)
        }
    }

internal val userRepositoryMock: UserRepository
    get() = mockk<UserRepository>().apply {
        coEvery { hasAnotherWithEmail(any(), any()) } answers {
            userEmails.contains(secondArg() as String)
        }
        coEvery { hasAnotherWithDocument(any(), any(), any()) } answers { false }
    }
