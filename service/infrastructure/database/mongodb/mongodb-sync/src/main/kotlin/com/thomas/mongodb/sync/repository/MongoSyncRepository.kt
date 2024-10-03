package com.thomas.mongodb.sync.repository

import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import com.mongodb.client.model.FindOneAndReplaceOptions
import com.mongodb.client.model.ReturnDocument.AFTER
import com.thomas.core.model.pagination.PageRequestData
import com.thomas.core.model.pagination.PageResponse
import com.thomas.mongodb.extension.orderBy
import java.util.UUID
import kotlin.reflect.KClass
import org.bson.conversions.Bson

abstract class MongoSyncRepository<T : Any>(
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
        pageable: PageRequestData,
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

}
