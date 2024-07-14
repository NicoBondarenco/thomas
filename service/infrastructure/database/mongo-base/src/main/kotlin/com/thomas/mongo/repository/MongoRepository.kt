package com.thomas.mongo.repository

import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import com.mongodb.client.model.FindOneAndReplaceOptions
import com.mongodb.client.model.ReturnDocument.AFTER
import com.mongodb.client.model.Sorts
import com.mongodb.client.model.Sorts.ascending
import com.mongodb.client.model.Sorts.descending
import com.thomas.core.model.pagination.PageRequest
import com.thomas.core.model.pagination.PageResponse
import com.thomas.core.model.pagination.PageSort
import com.thomas.core.model.pagination.PageSortDirection.ASC
import java.util.UUID
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import org.bson.conversions.Bson

abstract class MongoRepository<T : Any>(
    mongoDatabase: MongoDatabase,
    collectionName: String,
    entityClass: KClass<T>
) {

    companion object {
        @JvmStatic
        protected val OBJECT_ID_PARAMETER_NAME = "_id"
    }

    protected val collection: MongoCollection<T> = mongoDatabase.getCollection(collectionName, entityClass.java)

    fun one(
        id: UUID
    ): T? = list(Filters.eq(OBJECT_ID_PARAMETER_NAME, id)).firstOrNull()

    protected fun list(
        filter: Bson = Filters.empty()
    ): List<T> = collection.find(filter).toList()

    protected fun paged(
        pageable: PageRequest,
        filter: Bson = Filters.empty(),
    ): PageResponse<T> {
        val search = collection.find(filter)
        val total = search.count().toLong()
        val items = search
            .sort(pageable.orderBy())
            .skip(((pageable.pageNumber - 1) * pageable.pageSize).toInt())
            .limit(pageable.pageSize.toInt())
            .toList()
        return PageResponse.of(items, pageable, total)
    }

    fun save(
        id: UUID,
        entity: T,
    ): T = collection.findOneAndReplace(
        Filters.eq(OBJECT_ID_PARAMETER_NAME, id),
        entity,
        FindOneAndReplaceOptions().upsert(true).returnDocument(AFTER)
    )!!

    private fun PageRequest.orderBy() = this.pageSort.toOrderBy()

    private fun List<PageSort>.toOrderBy() = Sorts.orderBy(this.map { it.toSort() })

    private fun PageSort.toSort() = if (this.sortDirection == ASC) {
        ascending(this.sortField)
    } else {
        descending(this.sortField)
    }

    protected val <T> KProperty<T>.mongoParameter
        get() = this.name.mongoParameter

    protected val String.mongoParameter
        get() = "\$$this"

}
