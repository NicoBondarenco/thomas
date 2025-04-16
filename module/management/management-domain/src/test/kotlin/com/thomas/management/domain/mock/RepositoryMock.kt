package com.thomas.management.domain.mock

import com.thomas.core.model.pagination.PageRequestPeriod
import com.thomas.core.model.pagination.PageResponse
import com.thomas.core.util.StringUtils.randomPassword
import com.thomas.management.data.entity.GroupCompleteEntity
import com.thomas.management.data.entity.OrganizationEntity
import com.thomas.management.data.entity.UnitEntity
import com.thomas.management.data.entity.UserCompleteEntity
import com.thomas.management.data.entity.UserSimpleEntity
import com.thomas.management.data.entity.groupCompleteEntity
import com.thomas.management.data.entity.groupEntity
import com.thomas.management.data.entity.organizationEntity
import com.thomas.management.data.entity.passwordResetEntity
import com.thomas.management.data.entity.unitEntity
import com.thomas.management.data.entity.userCompleteEntity
import com.thomas.management.data.entity.userEntity
import com.thomas.management.data.repository.GroupRepository
import com.thomas.management.data.repository.OrganizationRepository
import com.thomas.management.data.repository.PasswordResetRepository
import com.thomas.management.data.repository.SignupRepository
import com.thomas.management.data.repository.UnitRepository
import com.thomas.management.data.repository.UserRepository
import io.mockk.Call
import io.mockk.Invocation
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.mockk
import java.time.OffsetDateTime.now
import java.time.ZoneOffset.UTC
import java.util.UUID

internal val organizationNotFound: MutableList<UUID> = mutableListOf()
internal val organizationNames: MutableList<String> = mutableListOf()
internal val organizationRegistrations: MutableList<String> = mutableListOf()

internal val unitNotFound: MutableList<UUID> = mutableListOf()
internal val unitNames: MutableList<String> = mutableListOf()
internal val unitDocuments: MutableList<String> = mutableListOf()
internal val unitLimit: MutableList<UUID> = mutableListOf()

internal val userNotFound: MutableList<UUID> = mutableListOf()
internal val userEmails: MutableList<String> = mutableListOf()
internal val userDocuments: MutableList<String> = mutableListOf()
internal val userLimit: MutableList<UUID> = mutableListOf()
internal val userGroups: MutableList<UUID> = mutableListOf()
internal val userUnits: MutableList<UUID> = mutableListOf()
internal val userInactiveStatus: MutableList<String> = mutableListOf()
internal val userInactiveOrganization: MutableList<String> = mutableListOf()
internal val userInvalidCredential: MutableList<String> = mutableListOf()
internal val userLogin: MutableList<UserCompleteEntity> = mutableListOf()

internal val passwordTokens: MutableList<String> = mutableListOf()
internal val expiredTokens: MutableList<String> = mutableListOf()

internal val groupNames: MutableList<String> = mutableListOf()
internal val groupUnits: MutableList<UUID> = mutableListOf()
internal val groupNotFound: MutableList<UUID> = mutableListOf()

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
        coEvery { allByIds(any(), any()) } answers {
            firstArg<Set<UUID>>().filter { !userUnits.contains(it) && !groupUnits.contains(it) }.map { id ->
                unitEntity.let {
                    it.copy(
                        id = id,
                        unitOrganization = it.unitOrganization.copy(id = secondArg()),
                    )
                }
            }.toSet()
        }
    }

internal val userRepositoryMock: UserRepository
    get() = mockk<UserRepository>().apply {
        coEvery { page(any<String>(), any<Boolean>(), any<Boolean>(), any<UUID>(), any<PageRequestPeriod>()) } answers {
            val users = (1..10).map { userEntity }
            PageResponse.of(users, it.fourthArg(), 10L)
        }
        coEvery { one(any(), any(), any()) } answers {
            userCompleteEntity.let {
                it.copy(
                    id = firstArg(),
                    userOrganization = it.userOrganization.copy(id = secondArg()),
                ).takeIf { !userNotFound.contains(firstArg()) }
            }
        }
        coEvery { byId(any()) } answers {
            userEntity.let {
                it.copy(
                    id = firstArg(),
                ).takeIf { !userNotFound.contains(firstArg()) }
            }
        }
        coEvery { create(any()) } answers {
            firstArg<UserCompleteEntity>().copy()
        }
        coEvery { update(any()) } answers {
            firstArg<UserCompleteEntity>().copy()
        }
        coEvery { updateSimple(any()) } answers {
            firstArg<UserSimpleEntity>().copy()
        }
        coEvery { simpleByEmail(any()) } answers {
            userEntity.copy(mainEmail = firstArg()).takeIf { !userEmails.contains(firstArg()) }
        }
        coEvery { hasAnotherWithEmail(any(), any()) } answers {
            userEmails.contains(secondArg() as String)
        }
        coEvery { hasAnotherWithDocument(any(), any(), any()) } answers {
            userDocuments.contains(thirdArg() as String)
        }
        coEvery { limitReached(any(), any()) } answers {
            userLimit.contains(secondArg())
        }
        coEvery { findByUsername(any()) } answers {
            val username = firstArg() as String
            (userLogin.firstOrNull { it.mainEmail == username } ?: userCompleteEntity).let {
                it.copy(
                    mainEmail = username,
                    isActive = !userInactiveStatus.contains(username),
                    userOrganization = organizationEntity.copy(
                        isActive = !userInactiveOrganization.contains(username),
                    ),
                    passwordHash = "${it.passwordHash}${it.passwordSalt}".takeIf {
                        !userInvalidCredential.contains(username)
                    } ?: randomPassword(),
                )
            }.takeIf {
                !userNotFound.contains(it.id)
            }
        }
    }

internal val groupRepositoryMock: GroupRepository
    get() = mockk<GroupRepository>().apply {
        coEvery { page(any<String>(), any<Boolean>(), any<UUID>(), any<PageRequestPeriod>()) } answers {
            val groups = (1..10).map { groupEntity }
            PageResponse.of(groups, it.fourthArg(), 10L)
        }
        coEvery { one(any(), any()) } answers {
            groupCompleteEntity.let {
                it.copy(
                    id = firstArg(),
                    groupOrganization = it.groupOrganization.copy(id = secondArg()),
                ).takeIf { !groupNotFound.contains(firstArg()) }
            }
        }
        coEvery { create(any()) } answers {
            firstArg<GroupCompleteEntity>().copy()
        }
        coEvery { update(any()) } answers {
            firstArg<GroupCompleteEntity>().copy()
        }
        coEvery { delete(any()) } returns Unit
        coEvery { hasAnotherWithName(any(), any(), any()) } answers {
            groupNames.contains(thirdArg() as String)
        }
        coEvery { allByIds(any(), any()) } answers {
            firstArg<Set<UUID>>().filter { !userGroups.contains(it) }.map { id ->
                groupEntity.let {
                    it.copy(
                        id = id,
                        groupOrganization = it.groupOrganization.copy(id = secondArg()),
                    )
                }
            }.toSet()
        }
        coEvery { allFullByIds(any(), any()) } answers {
            firstArg<Set<UUID>>().filter { !userGroups.contains(it) }.map { id ->
                groupCompleteEntity.let {
                    it.copy(
                        id = id,
                        groupOrganization = it.groupOrganization.copy(id = secondArg()),
                    )
                }
            }.toSet()
        }
    }

internal val passwordRepositoryMock: PasswordResetRepository
    get() = mockk<PasswordResetRepository>().apply {
        coEvery { upsertToken(any()) } answers {
            firstArg()
        }

        coEvery { findByToken(any()) } answers {
            passwordResetEntity.copy(
                resetToken = firstArg()
            ).takeIf {
                !passwordTokens.contains(firstArg())
            }?.let {
                it.takeIf {
                    expiredTokens.contains(firstArg())
                }?.copy(expiresOn = now(UTC).minusHours(1)) ?: it
            }
        }
    }

inline fun <reified T> Call.fourthArg() = invocation.fourthArg<T>()
inline fun <reified T> Invocation.fourthArg() = args[3] as T

fun clearRepositoryMocks() {
    organizationNotFound.clear()
    organizationNames.clear()
    organizationRegistrations.clear()
    unitNotFound.clear()
    unitNames.clear()
    unitDocuments.clear()
    unitLimit.clear()
    userNotFound.clear()
    userEmails.clear()
    userDocuments.clear()
    userLimit.clear()
    userGroups.clear()
    userUnits.clear()
    passwordTokens.clear()
    clearMocks(
        signupRepositoryMock,
        organizationRepositoryMock,
        unitRepositoryMock,
        userRepositoryMock,
        groupRepositoryMock,
        passwordRepositoryMock,
        answers = false,
        recordedCalls = true,
        childMocks = false,
        verificationMarks = true,
        exclusionRules = false,
    )
}