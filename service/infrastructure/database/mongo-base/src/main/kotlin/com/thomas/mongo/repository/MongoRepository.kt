package com.thomas.mongo.repository

import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import com.thomas.core.model.entity.BaseEntity
import com.thomas.mongo.extension.toEntity
import com.thomas.mongo.extension.toUpsertDocument
import java.util.UUID
import kotlin.reflect.KClass
import org.bson.Document
import org.bson.conversions.Bson

abstract class MongoRepository<T : BaseEntity<T>>(
    protected val mongoDatabase: MongoDatabase,
    collectionName: String,
    protected val entityClass: KClass<T>
) {

    protected val collection: MongoCollection<Document> = mongoDatabase.getCollection(collectionName)

    fun save(entity: T): T = entity.apply {
        upsert(entity.id, this.toUpsertDocument())
    }

    fun findById(id: UUID): T? = findDocumentById(id)?.let {
        it.toEntity(entityClass as KClass<BaseEntity<*>>) as T
    }

    protected fun upsert(id: UUID, document: Bson) = document.apply {
        collection.updateOne(
            Filters.eq("_id", id.toString()),
            document,
            UpdateOptions().upsert(true)
        )
    }

    protected fun findDocumentById(id: UUID): Document? =
        collection.find(Filters.eq("_id", id.toString())).firstOrNull()

}