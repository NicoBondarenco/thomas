package com.thomas.management.data.exposed.repository

import com.thomas.core.extension.unaccentedLower
import com.thomas.core.model.pagination.PageRequest
import com.thomas.core.model.pagination.PageResponse
import com.thomas.core.model.security.SecurityRole
import com.thomas.exposed.expression.toLikeParameter
import com.thomas.exposed.expression.unaccentLower
import com.thomas.exposed.repository.ExposedRepository
import com.thomas.exposed.table.page
import com.thomas.management.data.entity.UserCompleteEntity
import com.thomas.management.data.entity.UserEntity
import com.thomas.management.data.exposed.extension.toUserCompleteEntity
import com.thomas.management.data.exposed.extension.toUserEntity
import com.thomas.management.data.exposed.extension.updateFromUserEntity
import com.thomas.management.data.exposed.mapping.GroupTable
import com.thomas.management.data.exposed.mapping.UserExposedEntity
import com.thomas.management.data.exposed.mapping.UserGroupExposedEntity
import com.thomas.management.data.exposed.mapping.UserGroupTable
import com.thomas.management.data.exposed.mapping.UserRoleExposedEntity
import com.thomas.management.data.exposed.mapping.UserRoleTable
import com.thomas.management.data.exposed.mapping.UserTable
import com.thomas.management.data.repository.UserRepository
import java.time.OffsetDateTime
import java.time.OffsetDateTime.now
import java.time.ZoneOffset.UTC
import java.util.UUID
import java.util.UUID.randomUUID
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.QueryParameter
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.greaterEq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.lessEq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.TextColumnType
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.or

class UserExposedRepository(
    database: Database
) : ExposedRepository(database), UserRepository {

    override fun page(
        keywordText: String?,
        isActive: Boolean?,
        createdStart: OffsetDateTime?,
        createdEnd: OffsetDateTime?,
        updatedStart: OffsetDateTime?,
        updatedEnd: OffsetDateTime?,
        pageable: PageRequest
    ): PageResponse<UserEntity> = transacted {
        UserTable.joined().page(
            pageable = pageable,
            predicates = listOfNotNull(
                keywordText?.let {
                    (UserTable.documentNumber like it.toLikeParameter()) or
                            (UserTable.firstName.unaccentLower() like it.unaccentedLower().toLikeParameter()) or
                            (UserTable.lastName.unaccentLower() like it.unaccentedLower().toLikeParameter()) or
                            (UserTable.mainEmail.unaccentLower() like it.unaccentedLower().toLikeParameter())
                },
                isActive?.let { UserTable.isActive eq it },
                createdStart?.let { UserTable.createdAt greaterEq it },
                createdEnd?.let { UserTable.createdAt lessEq it },
                updatedStart?.let { UserTable.updatedAt greaterEq it },
                updatedEnd?.let { UserTable.updatedAt lessEq it },
            ),
        ) {
            UserExposedEntity.wrapRow(it).toUserEntity()
        }
    }

    override fun one(
        id: UUID
    ): UserCompleteEntity? = transacted { findUser(id)?.toUserCompleteEntity() }

    override fun hasAnotherWithSameMainEmail(
        id: UUID,
        mainEmail: String
    ): Boolean = hasAnotherWithSameValue(id, UserTable.mainEmail, mainEmail)

    override fun hasAnotherWithSameDocumentNumber(
        id: UUID,
        documentNumber: String
    ): Boolean = hasAnotherWithSameValue(id, UserTable.documentNumber, documentNumber)

    override fun hasAnotherWithSamePhoneNumber(
        id: UUID,
        phoneNumber: String
    ): Boolean = hasAnotherWithSameValue(id, UserTable.phoneNumber, phoneNumber)

    private fun <T> hasAnotherWithSameValue(
        id: UUID,
        column: Expression<T>,
        value: String
    ): Boolean = transacted {
        UserExposedEntity.find {
            (UserTable.id neq id) and (column eq QueryParameter(value, TextColumnType()))
        }.any()
    }

    override fun signup(
        entity: UserEntity
    ): UserEntity = entity.apply {
        transacted {
            UserExposedEntity.new(entity.id) {
                updateFromUserEntity(entity)
            }
        }
    }

    override fun create(
        entity: UserCompleteEntity
    ): UserCompleteEntity = entity.upsert {
        UserExposedEntity.new(entity.id, upsertEntity(entity))
    }

    override fun update(
        entity: UserCompleteEntity,
    ): UserCompleteEntity = entity.upsert {
        findUser(entity.id)!!.apply(upsertEntity(entity))
    }

    private fun UserCompleteEntity.upsert(
        exposedEntity: (UserCompleteEntity) -> Unit
    ) = transacted {
        exposedEntity(this@upsert)
        setRoles(this@upsert.id, this@upsert.userRoles)
        setGroups(this@upsert.id, this@upsert.userGroups.map { it.id }.toSet())
        this@upsert
    }

    private fun upsertEntity(
        entity: UserCompleteEntity,
    ): UserExposedEntity.() -> Unit = {
        updateFromUserEntity(entity)
    }

    private fun setRoles(
        id: UUID,
        roles: Set<SecurityRole>
    ) {
        UserRoleTable.deleteWhere { userId eq id }
        roles.forEach {
            UserRoleExposedEntity.new(randomUUID()) {
                userId = EntityID(id, UserTable)
                roleAuthority = it
                createdAt = now(UTC)
                updatedAt = now(UTC)
            }
        }
    }

    private fun setGroups(
        id: UUID,
        groups: Set<UUID>
    ) {
        UserGroupTable.deleteWhere { userId eq id }
        groups.forEach { group ->
            UserGroupExposedEntity.new(randomUUID()) {
                userId = EntityID(id, UserTable)
                groupId = EntityID(group, GroupTable)
                createdAt = now(UTC)
                updatedAt = now(UTC)
            }
        }
    }

    private fun findUser(
        id: UUID
    ) = UserExposedEntity.findById(id)

}
