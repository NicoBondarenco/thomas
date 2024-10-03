package com.thomas.mongodb.coroutines.repository

import com.mongodb.client.model.Filters
import com.mongodb.client.model.FindOneAndReplaceOptions
import com.mongodb.client.model.ReturnDocument.AFTER
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import com.thomas.core.coroutines.model.pagination.PageFlowResponse
import com.thomas.core.model.pagination.PageRequestData
import com.thomas.mongodb.extension.OBJECT_ID_PARAMETER_NAME
import com.thomas.mongodb.extension.orderBy
import java.util.UUID
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.firstOrNull
import org.bson.conversions.Bson

abstract class MongoCoroutinesRepository<T : Any>(
    mongoDatabase: MongoDatabase,
    collectionName: String,
    entityClass: KClass<T>
) {

    protected val collection: MongoCollection<T> = mongoDatabase.getCollection(collectionName, entityClass.java)

    suspend fun one(
        id: UUID
    ): T? = list(Filters.eq(OBJECT_ID_PARAMETER_NAME, id)).firstOrNull()

    protected fun list(
        filter: Bson = Filters.empty()
    ): Flow<T> = collection.find(filter)

    protected suspend fun paged(
        pageable: PageRequestData,
        filter: Bson = Filters.empty(),
    ): PageFlowResponse<T> {
        val search = collection.find(filter)
        val total = search.count().toLong()
        val items = search
            .sort(pageable.orderBy())
            .skip(((pageable.pageNumber - 1) * pageable.pageSize).toInt())
            .limit(pageable.pageSize.toInt())
        return PageFlowResponse.of(items, pageable, total)
    }

    suspend fun save(
        id: UUID,
        entity: T,
    ): T = collection.findOneAndReplace(
        Filters.eq(OBJECT_ID_PARAMETER_NAME, id),
        entity,
        FindOneAndReplaceOptions().upsert(true).returnDocument(AFTER)
    )!!

}
