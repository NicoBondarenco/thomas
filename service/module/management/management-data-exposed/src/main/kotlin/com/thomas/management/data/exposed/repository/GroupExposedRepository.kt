package com.thomas.management.data.exposed.repository

import com.thomas.core.extension.unaccentedLower
import com.thomas.core.model.pagination.PageRequest
import com.thomas.core.model.pagination.PageResponse
import com.thomas.core.model.security.SecurityRole
import com.thomas.exposed.expression.toLikeParameter
import com.thomas.exposed.expression.unaccentLower
import com.thomas.exposed.repository.ExposedRepository
import com.thomas.exposed.table.page
import com.thomas.management.data.entity.GroupCompleteEntity
import com.thomas.management.data.entity.GroupEntity
import com.thomas.management.data.exposed.extension.toGroupCompleteEntity
import com.thomas.management.data.exposed.extension.toGroupEntity
import com.thomas.management.data.exposed.extension.updateFromGroupEntity
import com.thomas.management.data.exposed.mapping.GroupExposedEntity
import com.thomas.management.data.exposed.mapping.GroupRoleExposedEntity
import com.thomas.management.data.exposed.mapping.GroupRoleTable
import com.thomas.management.data.exposed.mapping.GroupTable
import com.thomas.management.data.repository.GroupRepository
import java.time.OffsetDateTime
import java.time.OffsetDateTime.now
import java.time.ZoneOffset.UTC
import java.util.UUID
import java.util.UUID.randomUUID
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.greaterEq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.lessEq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.or

class GroupExposedRepository(
    database: Database
) : ExposedRepository(database), GroupRepository {

    override fun page(
        keywordText: String?,
        isActive: Boolean?,
        createdStart: OffsetDateTime?,
        createdEnd: OffsetDateTime?,
        updatedStart: OffsetDateTime?,
        updatedEnd: OffsetDateTime?,
        pageable: PageRequest
    ): PageResponse<GroupEntity> = transacted {
        GroupTable.joined().page(
            pageable = pageable,
            predicates = listOfNotNull(
                keywordText?.let {
                    (GroupTable.groupName.unaccentLower() like it.unaccentedLower().toLikeParameter()) or
                        (GroupTable.groupDescription.unaccentLower() like it.unaccentedLower().toLikeParameter())
                },
                isActive?.let { GroupTable.isActive eq it },
                createdStart?.let { GroupTable.createdAt greaterEq it },
                createdEnd?.let { GroupTable.createdAt lessEq it },
                updatedStart?.let { GroupTable.updatedAt greaterEq it },
                updatedEnd?.let { GroupTable.updatedAt lessEq it },
            ),
        ) {
            GroupExposedEntity.wrapRow(it).toGroupEntity()
        }
    }

    override fun one(
        id: UUID
    ): GroupCompleteEntity? = transacted {
        findGroup(id)?.toGroupCompleteEntity()
    }

    override fun allByIds(
        ids: Set<UUID>
    ): Set<GroupEntity> = transacted {
        GroupExposedEntity.forIds(ids.toList()).map { it.toGroupEntity() }.toSet()
    }

    override fun create(
        entity: GroupCompleteEntity
    ): GroupCompleteEntity = entity.upsert {
        GroupExposedEntity.new(entity.id, upsertEntity(entity))
    }

    override fun update(
        entity: GroupCompleteEntity,
    ): GroupCompleteEntity = entity.upsert {
        findGroup(entity.id)!!.apply(upsertEntity(entity))
    }

    override fun delete(
        id: UUID
    ) = transacted {
        GroupRoleTable.deleteWhere { groupId eq id }
        GroupTable.deleteWhere { GroupTable.id eq id }
        Unit
    }

    override fun hasAnotherWithSameGroupName(
        id: UUID,
        groupName: String
    ): Boolean = transacted {
        GroupExposedEntity.find {
            (GroupTable.id neq id) and (GroupTable.groupName eq groupName)
        }.any()
    }

    private fun GroupCompleteEntity.upsert(
        exposedEntity: (GroupCompleteEntity) -> Unit
    ) = transacted {
        exposedEntity(this@upsert)
        setRoles(this@upsert.id, this@upsert.groupRoles)
        this@upsert
    }

    private fun upsertEntity(
        entity: GroupCompleteEntity,
    ): GroupExposedEntity.() -> Unit = {
        updateFromGroupEntity(entity)
    }

    private fun setRoles(
        id: UUID,
        roles: Set<SecurityRole>
    ) {
        GroupRoleTable.deleteWhere { groupId eq id }
        roles.forEach {
            GroupRoleExposedEntity.new(randomUUID()) {
                groupId = EntityID(id, GroupTable)
                roleAuthority = it
                createdAt = now(UTC)
                updatedAt = now(UTC)
            }
        }
    }

    private fun findGroup(
        id: UUID
    ) = GroupExposedEntity.findById(id)

}
