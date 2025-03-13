package com.thomas.management.data.neo4j.util

import com.thomas.core.extension.unaccentedLower
import com.thomas.core.model.pagination.PageRequestPeriod
import com.thomas.core.model.pagination.PageResponse
import com.thomas.management.data.entity.GroupCompleteEntity
import java.util.UUID

data class GroupSearchData(
    val keyword: String?,
    val isActive: Boolean?,
    val organizationId: UUID,
    val pageable: PageRequestPeriod,
    val comparator: Comparator<GroupCompleteEntity>
) {

    fun page(
        entities: List<GroupCompleteEntity>
    ): PageResponse<GroupCompleteEntity> = PageResponse
        .of(contentList(entities, comparator), this.pageable, count(entities))

    private fun contentList(
        entities: List<GroupCompleteEntity>,
        comparator: Comparator<GroupCompleteEntity>
    ): List<GroupCompleteEntity> = entities
        .filter { filter(it) }
        .sortedWith(comparator)
        .drop(((pageable.pageNumber - 1) * pageable.pageSize).toInt())
        .take(pageable.pageSize.toInt())

    private fun count(
        entities: List<GroupCompleteEntity>
    ): Long = entities.count {
        filter(it)
    }.toLong()

    private fun filter(
        entity: GroupCompleteEntity
    ): Boolean = filterOrganizationId(entity) &&
            filterKeyWord(entity) &&
            filterIsActive(entity) &&
            filterCreatedAtStart(entity) &&
            filterCreatedAtEnd(entity) &&
            filterUpdatedAtStart(entity) &&
            filterUpdatedAtEnd(entity)

    private fun filterOrganizationId(entity: GroupCompleteEntity): Boolean = this.organizationId.let {
        entity.groupOrganization.id == it
    }

    private fun filterKeyWord(entity: GroupCompleteEntity): Boolean = this.keyword?.let {
        entity.groupName.unaccentedLower().contains(it.unaccentedLower()) ||
                (entity.groupDescription?.unaccentedLower()?.contains(it.unaccentedLower()) ?: false)
    } ?: true

    private fun filterIsActive(entity: GroupCompleteEntity): Boolean = this.isActive?.let {
        entity.isActive == it
    } ?: true

    private fun filterCreatedAtStart(entity: GroupCompleteEntity): Boolean = this.pageable.createdStart?.let {
        entity.createdAt >= it
    } ?: true

    private fun filterCreatedAtEnd(entity: GroupCompleteEntity): Boolean = this.pageable.createdEnd?.let {
        entity.createdAt <= it
    } ?: true

    private fun filterUpdatedAtStart(entity: GroupCompleteEntity): Boolean = this.pageable.updatedStart?.let {
        entity.updatedAt >= it
    } ?: true

    private fun filterUpdatedAtEnd(entity: GroupCompleteEntity): Boolean = this.pageable.updatedEnd?.let {
        entity.updatedAt <= it
    } ?: true

}
