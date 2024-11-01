package com.thomas.management.domain.mock

import com.thomas.core.model.pagination.PageRequestPeriod
import com.thomas.core.model.pagination.PageResponse
import com.thomas.management.data.entity.OrganizationEntity
import com.thomas.management.data.entity.UnitEntity
import com.thomas.management.data.repository.OrganizationRepository
import com.thomas.management.data.repository.SignupRepository
import com.thomas.management.data.repository.UnitRepository
import com.thomas.management.data.repository.UserRepository
import com.thomas.management.domain.util.organizationEntity
import com.thomas.management.domain.util.unitEntity
import io.mockk.Call
import io.mockk.Invocation
import io.mockk.coEvery
import io.mockk.mockk
import java.util.UUID

internal val organizationNotFound: MutableList<UUID> = mutableListOf()
internal val organizationNames: MutableList<String> = mutableListOf()
internal val organizationRegistrations: MutableList<String> = mutableListOf()

internal val unitNotFound: MutableList<UUID> = mutableListOf()
internal val unitNames: MutableList<String> = mutableListOf()
internal val unitDocuments: MutableList<String> = mutableListOf()
internal val unitLimit: MutableList<UUID> = mutableListOf()

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
            organizationRegistrations.contains(secondArg() as String)
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

internal val unitRepositoryMock: UnitRepository
    get() = mockk<UnitRepository>().apply {
        coEvery { hasAnotherWithName(any(), any(), any()) } answers {
            unitNames.contains(thirdArg() as String)
        }
        coEvery { hasAnotherWithDocument(any(), any(), any()) } answers {
            unitDocuments.contains(thirdArg() as String)
        }
        coEvery { one(any(), any()) } answers {
            unitEntity.let {
                it.copy(
                    id = firstArg(),
                    unitOrganization = it.unitOrganization.copy(id = secondArg())
                ).takeIf { !unitNotFound.contains(firstArg()) }
            }
        }
        coEvery { create(any()) } answers {
            firstArg()
        }
        coEvery { update(any()) } answers {
            unitEntity.copy(id = (firstArg() as UnitEntity).id)
        }
        coEvery { delete(any()) } returns Unit
        coEvery { page(any<UUID>(), any<String>(), any<Boolean>(), any<PageRequestPeriod>()) } answers {
            val units = (1..10).map { unitEntity }
            PageResponse.of(units, it.fourthArg(), 10L)
        }
        coEvery { limitReached(any(), any()) } answers {
            unitLimit.contains(secondArg())
        }
    }

internal val userRepositoryMock: UserRepository
    get() = mockk<UserRepository>().apply {
        coEvery { hasAnotherWithEmail(any(), any()) } answers {
            userEmails.contains(secondArg() as String)
        }
        coEvery { hasAnotherWithDocument(any(), any(), any()) } answers { false }
    }

inline fun <reified T> Call.fourthArg() = invocation.fourthArg<T>()
inline fun <reified T> Invocation.fourthArg() = args[3] as T