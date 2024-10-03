package com.thomas.management.data.repository

import com.thomas.core.generator.PersonGenerator
import com.thomas.core.model.pagination.PageRequestData
import com.thomas.core.model.pagination.PageResponse
import com.thomas.management.data.entity.UserGroupsEntity
import com.thomas.management.data.entity.UserEntity
import java.time.OffsetDateTime
import java.util.UUID

class UserRepositoryMock : UserRepository {

    private val users = mutableMapOf<UUID, UserGroupsEntity>()

    fun clear() {
        users.clear()
    }

    fun generateUsers(
        quantity: Int = 1,
    ): List<UserGroupsEntity> = (1..quantity).map {
        generateUserCompleteEntity().apply { users[id] = this }
    }

    private fun generateUserCompleteEntity() = PersonGenerator.generate().let {
        val id = UUID.randomUUID()
        UserGroupsEntity(
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
            userRoles = setOf(),
            userGroups = setOf(),
        )
    }

    private fun UserGroupsEntity.toUserEntity() = UserEntity(
        id = this.id,
        firstName = this.firstName,
        lastName = this.lastName,
        mainEmail = this.mainEmail,
        documentNumber = this.documentNumber,
        phoneNumber = this.phoneNumber,
        birthDate = this.birthDate,
        userGender = this.userGender,
        isActive = this.isActive,
        creatorId = this.creatorId,
    )

    override fun page(
        keywordText: String?,
        isActive: Boolean?,
        createdStart: OffsetDateTime?,
        createdEnd: OffsetDateTime?,
        updatedStart: OffsetDateTime?,
        updatedEnd: OffsetDateTime?,
        pageable: PageRequestData,
    ): PageResponse<UserEntity> = PageResponse.of(users.values.map { it.toUserEntity() }, pageable, users.size.toLong())

    override fun one(
        id: UUID,
    ): UserGroupsEntity? = users[id]

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

    override fun signup(
        entity: UserEntity,
    ): UserEntity = entity.apply {
        this.toUserCompleteEntity().apply {
            users[this.id] = this
        }
    }

    override fun create(
        entity: UserGroupsEntity,
    ): UserGroupsEntity = entity.apply {
        users[this.id] = this
    }

    override fun update(
        entity: UserGroupsEntity,
    ): UserGroupsEntity = entity.apply {
        users[this.id] = this
    }

    private fun UserEntity.toUserCompleteEntity() = UserGroupsEntity(
        id = this.id,
        firstName = this.firstName,
        lastName = this.lastName,
        mainEmail = this.mainEmail,
        documentNumber = this.documentNumber,
        phoneNumber = this.phoneNumber,
        profilePhoto = this.profilePhoto,
        birthDate = this.birthDate,
        userGender = this.userGender,
        isActive = this.isActive,
        creatorId = this.creatorId,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        userRoles = setOf(),
        userGroups = setOf(),
    )

}
