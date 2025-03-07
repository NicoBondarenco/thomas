package com.thomas.management.data.neo4j.util

import com.thomas.core.extension.unaccentedLower
import com.thomas.core.model.pagination.PageRequestPeriod
import com.thomas.core.model.pagination.PageResponse
import com.thomas.management.data.entity.UnitEntity
import java.util.UUID

data class UnitSearchData(
    val keyword: String?,
    val isActive: Boolean?,
    val organizationId: UUID,
    val pageable: PageRequestPeriod,
    val comparator: Comparator<UnitEntity>
) {

    fun page(
        entities: List<UnitEntity>
    ): PageResponse<UnitEntity> = PageResponse
        .of(contentList(entities, comparator), this.pageable, count(entities))

    private fun contentList(
        entities: List<UnitEntity>,
        comparator: Comparator<UnitEntity>
    ): List<UnitEntity> = entities
        .filter { filter(it) }
        .sortedWith(comparator)
        .drop(((pageable.pageNumber - 1) * pageable.pageSize).toInt())
        .take(pageable.pageSize.toInt())

    private fun count(
        entities: List<UnitEntity>
    ): Long = entities.count {
        filter(it)
    }.toLong()

    private fun filter(
        entity: UnitEntity
    ): Boolean = filterOrganizationId(entity) &&
            filterKeyWord(entity) &&
            filterIsActive(entity) &&
            filterCreatedAtStart(entity) &&
            filterCreatedAtEnd(entity) &&
            filterUpdatedAtStart(entity) &&
            filterUpdatedAtEnd(entity)

    private fun filterOrganizationId(entity: UnitEntity): Boolean = this.organizationId.let {
        entity.unitOrganization.id == it
    }

    private fun filterKeyWord(entity: UnitEntity): Boolean = this.keyword?.let {
        entity.unitName.unaccentedLower().contains(it.unaccentedLower()) ||
                entity.mainEmail.unaccentedLower().contains(it.unaccentedLower()) ||
                entity.documentNumber.unaccentedLower().contains(it.unaccentedLower()) ||
                (entity.fantasyName?.unaccentedLower()?.contains(it.unaccentedLower()) ?: false)
    } ?: true

    private fun filterIsActive(entity: UnitEntity): Boolean = this.isActive?.let {
        entity.isActive == it
    } ?: true

    private fun filterCreatedAtStart(entity: UnitEntity): Boolean = this.pageable.createdStart?.let {
        entity.createdAt >= it
    } ?: true

    private fun filterCreatedAtEnd(entity: UnitEntity): Boolean = this.pageable.createdEnd?.let {
        entity.createdAt <= it
    } ?: true

    private fun filterUpdatedAtStart(entity: UnitEntity): Boolean = this.pageable.updatedStart?.let {
        entity.updatedAt >= it
    } ?: true

    private fun filterUpdatedAtEnd(entity: UnitEntity): Boolean = this.pageable.updatedEnd?.let {
        entity.updatedAt <= it
    } ?: true

}
