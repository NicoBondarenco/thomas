package com.thomas.management.data.neo4j.util

import com.thomas.core.extension.unaccentedLower
import com.thomas.core.model.pagination.PageRequestPeriod
import com.thomas.core.model.pagination.PageResponse
import com.thomas.management.data.entity.OrganizationEntity

data class OrganizationSearchData(
    val keyword: String?,
    val isActive: Boolean?,
    val pageable: PageRequestPeriod,
    val comparator: Comparator<OrganizationEntity>
) {

    fun page(
        entities: List<OrganizationEntity>
    ): PageResponse<OrganizationEntity> = PageResponse
        .of(contentList(entities, comparator), this.pageable, count(entities))

    private fun contentList(
        entities: List<OrganizationEntity>,
        comparator: Comparator<OrganizationEntity>
    ): List<OrganizationEntity> = entities
        .filter { filter(it) }
        .sortedWith(comparator)
        .drop(((pageable.pageNumber - 1) * pageable.pageSize).toInt())
        .take(pageable.pageSize.toInt())

    private fun count(
        entities: List<OrganizationEntity>
    ): Long = entities.count {
        filter(it)
    }.toLong()

    private fun filter(
        entity: OrganizationEntity
    ): Boolean = filterKeyWord(entity) &&
            filterIsActive(entity) &&
            filterCreatedAtStart(entity) &&
            filterCreatedAtEnd(entity) &&
            filterUpdatedAtStart(entity) &&
            filterUpdatedAtEnd(entity)

    private fun filterKeyWord(entity: OrganizationEntity): Boolean = this.keyword?.let {
        entity.organizationName.unaccentedLower().contains(it.unaccentedLower()) ||
                entity.mainEmail.unaccentedLower().contains(it.unaccentedLower()) ||
                entity.registrationNumber.unaccentedLower().contains(it.unaccentedLower()) ||
                (entity.fantasyName?.unaccentedLower()?.contains(it.unaccentedLower()) ?: false)
    } ?: true

    private fun filterIsActive(entity: OrganizationEntity): Boolean = this.isActive?.let {
        entity.isActive == it
    } ?: true

    private fun filterCreatedAtStart(entity: OrganizationEntity): Boolean = this.pageable.createdStart?.let {
        entity.createdAt >= it
    } ?: true

    private fun filterCreatedAtEnd(entity: OrganizationEntity): Boolean = this.pageable.createdEnd?.let {
        entity.createdAt <= it
    } ?: true

    private fun filterUpdatedAtStart(entity: OrganizationEntity): Boolean = this.pageable.updatedStart?.let {
        entity.updatedAt >= it
    } ?: true

    private fun filterUpdatedAtEnd(entity: OrganizationEntity): Boolean = this.pageable.updatedEnd?.let {
        entity.updatedAt <= it
    } ?: true

}
