package com.thomas.mongo.repository

import com.mongodb.client.model.Filters
import com.mongodb.client.model.FindOneAndReplaceOptions
import com.mongodb.client.model.ReturnDocument.AFTER
import com.mongodb.client.model.Sorts
import com.mongodb.client.model.Sorts.ascending
import com.mongodb.client.model.Sorts.descending
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import com.thomas.core.model.entity.BaseEntity
import com.thomas.core.model.pagination.PageRequest
import com.thomas.core.model.pagination.PageResponse
import com.thomas.core.model.pagination.PageSort
import com.thomas.core.model.pagination.PageSortDirection.ASC
import java.util.UUID
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.bson.conversions.Bson

abstract class MongoRepository<T : BaseEntity<T>>(
    mongoDatabase: MongoDatabase,
    collectionName: String,
    entityClass: KClass<T>
) {

    private val collection: MongoCollection<T> = mongoDatabase.getCollection(collectionName, entityClass.java)
    private val idAttribute = BaseEntity<*>::id.name

    fun one(
        id: UUID
    ): T? = list(Filters.eq(idAttribute, id)).firstOrNull()

    protected fun list(
        filter: Bson = Filters.empty()
    ): List<T> = runBlocking {
        collection.find(filter).toList()
    }

    protected fun paged(
        pageable: PageRequest,
        filter: Bson = Filters.empty(),
    ): PageResponse<T> = runBlocking {
        val search = collection.find(filter)
        val total = search.count().toLong()
        val items = search
            .sort(pageable.orderBy())
            .skip(((pageable.pageNumber - 1) * pageable.pageSize).toInt())
            .limit(pageable.pageSize.toInt())
            .toList()
        PageResponse.of(items, pageable, total)
    }

    fun save(entity: T): T = runBlocking {
        collection.findOneAndReplace(
            Filters.eq(idAttribute, entity.id),
            entity,
            FindOneAndReplaceOptions().upsert(true).returnDocument(AFTER)
        )!!
    }

    private fun PageRequest.orderBy() = this.pageSort.toOrderBy()

    private fun List<PageSort>.toOrderBy() = Sorts.orderBy(this.map { it.toSort() })

    private fun PageSort.toSort() = if (this.sortDirection == ASC) {
        ascending(this.sortField)
    } else {
        descending(this.sortField)
    }

}
