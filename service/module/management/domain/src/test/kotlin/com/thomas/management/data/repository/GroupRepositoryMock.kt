package com.thomas.management.data.repository

import com.thomas.core.generator.GroupGenerator
import com.thomas.management.data.entity.GroupEntity
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

    override fun findByIds(
        ids: List<UUID>
    ): List<GroupEntity> = groups.values.filter { it.id in ids }

}
