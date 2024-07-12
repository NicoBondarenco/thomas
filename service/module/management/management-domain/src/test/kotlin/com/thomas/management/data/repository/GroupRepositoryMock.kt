package com.thomas.management.data.repository

import com.thomas.core.generator.GroupGenerator
import com.thomas.core.model.pagination.PageRequest
import com.thomas.core.model.pagination.PageResponse
import com.thomas.management.data.entity.GroupCompleteEntity
import com.thomas.management.data.entity.GroupEntity
import com.thomas.management.requests.toGroupEntity
import java.time.OffsetDateTime
import java.util.UUID

class GroupRepositoryMock : GroupRepository {

    private val groups = mutableMapOf<UUID, GroupCompleteEntity>()

    fun clear() = groups.clear()

    fun generateGroups(
        quantity: Int = 1,
    ): List<GroupCompleteEntity> = (1..quantity).map {
        generateGroupEntity().apply { groups[id] = this }
    }

    private fun generateGroupEntity() = GroupGenerator.generate().let {
        GroupCompleteEntity(
            groupName = it.groupName,
            groupDescription = it.groupDescription,
            isActive = it.isActive,
            creatorId = UUID.randomUUID(),
            groupRoles = setOf(),
        )
    }

    override fun page(
        keywordText: String?,
        isActive: Boolean?,
        createdStart: OffsetDateTime?,
        createdEnd: OffsetDateTime?,
        updatedStart: OffsetDateTime?,
        updatedEnd: OffsetDateTime?,
        pageable: PageRequest
    ): PageResponse<GroupEntity> = PageResponse.of(groups.values.map { it.toGroupEntity() }, pageable, groups.size.toLong())

    override fun one(
        id: UUID
    ): GroupCompleteEntity? = groups[id]

    override fun allByIds(
        ids: Set<UUID>
    ): Set<GroupEntity> = groups.values.filter {
        it.id in ids
    }.map {
        it.toGroupEntity()
    }.toSet()

    override fun create(
        entity: GroupCompleteEntity
    ): GroupCompleteEntity = entity.apply {
        groups[this.id] = this
    }

    override fun update(
        entity: GroupCompleteEntity
    ): GroupCompleteEntity = entity.apply {
        groups[this.id] = this
    }

    override fun delete(
        id: UUID
    ) {
        groups.remove(id)
    }

    override fun hasAnotherWithSameGroupName(
        id: UUID,
        groupName: String
    ): Boolean = groups.values.any { it.id != id && it.groupName == groupName }

}
