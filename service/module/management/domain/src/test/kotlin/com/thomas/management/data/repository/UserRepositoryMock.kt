package com.thomas.management.data.repository

import com.thomas.core.generator.PersonGenerator
import com.thomas.core.model.pagination.PageRequest
import com.thomas.core.model.pagination.PageResponse
import com.thomas.management.data.entity.GroupEntity
import com.thomas.management.data.entity.UserEntity
import com.thomas.management.data.entity.UserGroupsEntity
import java.time.OffsetDateTime
import java.util.UUID

class UserRepositoryMock : UserRepository {

    private val users = mutableMapOf<UUID, UserEntity>()

    fun clear() = users.clear()

    fun generateUsers(
        quantity: Int = 1,
    ): List<UserEntity> = (1..quantity).map {
        generateUserEntity().apply { users[id] = this }
    }

    private fun generateUserEntity() = PersonGenerator.generate().let {
        val id = UUID.randomUUID()
        UserEntity(
            id = id,
            firstName = it.firstName,
            lastName = it.lastName,
            mainEmail = it.mainEmail,
            documentNumber = it.documentNumber,
            phoneNumber = it.phoneNumber,
            birthDate = it.birthDate,
            userGender = it.userGender,
            isActive = listOf(true, false).random(),
            creatorId = id,
            userRoles = listOf(),
        )
    }

    override fun page(
        keywordText: String?,
        isActive: Boolean?,
        createdStart: OffsetDateTime?,
        createdEnd: OffsetDateTime?,
        updatedStart: OffsetDateTime?,
        updatedEnd: OffsetDateTime?,
        pageable: PageRequest,
    ): PageResponse<UserEntity> = PageResponse.of(users.values.toList(), pageable, users.size.toLong())

    override fun findById(
        id: UUID,
    ): UserEntity? = users[id]

    override fun findByIdWithGroups(
        id: UUID,
    ): UserGroupsEntity? = users[id]?.let { UserGroupsEntity(it, listOf()) }

    override fun hasAnotherWithSameMainEmail(
        id: UUID,
        mainEmail: String,
    ): Boolean = users.values.any { it.id != id && it.mainEmail == mainEmail }

    override fun hasAnotherWithSameDocumentNumber(
        id: UUID,
        documentNumber: String,
    ): Boolean = users.values.any { it.id != id && it.documentNumber == documentNumber }

    override fun hasAnotherWithSamePhoneNumber(
        id: UUID,
        phoneNumber: String,
    ): Boolean = users.values.any { it.id != id && it.phoneNumber == phoneNumber }

    override fun create(
        entity: UserEntity,
        groups: List<GroupEntity>,
    ): UserGroupsEntity = UserGroupsEntity(entity, groups).apply {
        users[entity.id] = entity
    }

    override fun update(
        entity: UserEntity,
        groups: List<GroupEntity>,
    ): UserGroupsEntity = UserGroupsEntity(entity, groups).apply {
        users[entity.id] = entity
    }

    override fun update(
        entity: UserEntity,
    ): UserEntity = entity.apply {
        users[this.id] = this
    }

}
