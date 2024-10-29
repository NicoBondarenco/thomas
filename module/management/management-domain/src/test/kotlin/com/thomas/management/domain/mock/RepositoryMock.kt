package com.thomas.management.domain.mock

import com.thomas.core.model.pagination.PageRequestPeriod
import com.thomas.core.model.pagination.PageResponse
import com.thomas.management.data.entity.OrganizationEntity
import com.thomas.management.data.repository.OrganizationRepository
import com.thomas.management.data.repository.SignupRepository
import com.thomas.management.data.repository.UserRepository
import com.thomas.management.domain.util.organizationEntity
import io.mockk.coEvery
import io.mockk.mockk
import java.util.UUID

internal val organizationNotFound: MutableList<UUID> = mutableListOf()

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
        coEvery { one(any()) } answers {
            organizationEntity.copy(id = firstArg()).takeIf { !organizationNotFound.contains(firstArg()) }
        }
        coEvery { create(any()) } answers {
            firstArg()
        }
        coEvery { update(any()) } answers {
            organizationEntity.copy(id = (firstArg() as OrganizationEntity).id)
        }
        coEvery { page(any<String>(), any<Boolean>(), any<PageRequestPeriod>()) } answers {
            val organizations = (1..10).map { organizationEntity }
            PageResponse.of(organizations, thirdArg(), 10L)
        }
    }

internal val userRepositoryMock: UserRepository
    get() = mockk<UserRepository>().apply {
        coEvery { hasAnotherWithEmail(any(), any()) } answers {
            userEmails.contains(secondArg() as String)
        }
        coEvery { hasAnotherWithDocument(any(), any(), any()) } answers { false }
    }
