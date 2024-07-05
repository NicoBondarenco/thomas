package com.thomas.management.data.repository

import com.thomas.core.generator.GroupGenerator
import com.thomas.core.model.pagination.PageRequest
import com.thomas.core.model.pagination.PageResponse
import com.thomas.management.data.entity.GroupEntity
import java.time.OffsetDateTime
import java.util.UUID

class GroupRepositoryMock : GroupRepository {

    private val groups = mutableMapOf<UUID, GroupEntity>()

    fun clear() = groups.clear()

    fun generateGroups(
        quantity: Int = 1,
    ): List<GroupEntity> = (1..quantity).map {
        generateGroupEntity().apply { groups[id] = this }
    }

    private fun generateGroupEntity() = GroupGenerator.generate().let {
        GroupEntity(
            groupName = it.groupName,
            groupDescription = it.groupDescription,
            isActive = it.isActive,
            creatorId = UUID.randomUUID(),
            groupRoles = listOf(),
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
    ): PageResponse<GroupEntity> = PageResponse.of(groups.values.toList(), pageable, groups.size.toLong())

    override fun findById(
        id: UUID
    ): GroupEntity? = groups[id]

    override fun findByIds(
        ids: List<UUID>
    ): List<GroupEntity> = groups.values.filter { it.id in ids }

    override fun create(
        entity: GroupEntity
    ): GroupEntity = entity.apply { groups[this.id] = this }

    override fun update(
        entity: GroupEntity
    ): GroupEntity = entity.apply { groups[this.id] = this }

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
