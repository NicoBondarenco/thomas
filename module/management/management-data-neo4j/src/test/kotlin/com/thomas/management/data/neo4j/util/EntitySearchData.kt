package com.thomas.management.data.neo4j.util

import com.thomas.core.model.pagination.PageRequestPeriod
import com.thomas.core.model.pagination.PageResponse
import com.thomas.management.data.entity.info.BasicInfo
import java.util.UUID

abstract class EntitySearchData<T : BasicInfo> {

    abstract val keyword: String?
    abstract val isActive: Boolean?
    abstract val organizationId: UUID?
    abstract val pageable: PageRequestPeriod
    abstract val comparator: Comparator<T>

    fun page(
        entities: List<T>
    ): PageResponse<T> = PageResponse
        .of(contentList(entities, comparator), this.pageable, count(entities))

    private fun contentList(
        entities: List<T>,
        comparator: Comparator<T>
    ): List<T> = entities
        .filter { filter(it) }
        .sortedWith(comparator)
        .drop(((pageable.pageNumber - 1) * pageable.pageSize).toInt())
        .take(pageable.pageSize.toInt())

    private fun count(
        entities: List<T>
    ): Long = entities.count {
        filter(it)
    }.toLong()

    private fun filter(
        entity: T
    ): Boolean = filterOrganizationId(entity) &&
            filterKeyWord(entity) &&
            filterIsActive(entity) &&
            filterCreatedAtStart(entity) &&
            filterCreatedAtEnd(entity) &&
            filterUpdatedAtStart(entity) &&
            filterUpdatedAtEnd(entity)

    protected abstract fun entityOrganization(entity: T): UUID

    protected abstract fun filterKeyWord(entity: T): Boolean

    private fun filterOrganizationId(entity: T): Boolean = this.organizationId?.let {
        entityOrganization(entity) == it
    } ?: true

    private fun filterIsActive(entity: T): Boolean = this.isActive?.let {
        entity.isActive == it
    } ?: true

    private fun filterCreatedAtStart(entity: T): Boolean = this.pageable.createdStart?.let {
        entity.createdAt >= it
    } ?: true

    private fun filterCreatedAtEnd(entity: T): Boolean = this.pageable.createdEnd?.let {
        entity.createdAt <= it
    } ?: true

    private fun filterUpdatedAtStart(entity: T): Boolean = this.pageable.updatedStart?.let {
        entity.updatedAt >= it
    } ?: true

    private fun filterUpdatedAtEnd(entity: T): Boolean = this.pageable.updatedEnd?.let {
        entity.updatedAt <= it
    } ?: true

}
