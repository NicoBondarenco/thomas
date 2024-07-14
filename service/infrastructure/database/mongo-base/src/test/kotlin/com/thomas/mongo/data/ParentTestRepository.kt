package com.thomas.mongo.data

import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Accumulators
import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Filters
import com.thomas.mongo.repository.MongoRepository
import java.util.UUID

class ParentTestRepository(
    database: MongoDatabase,
    collection: String
) : MongoRepository<ParentTestEntity>(
    database,
    collection,
    ParentTestEntity::class
) {

    companion object {
        private const val CHILD_COLLECTION_NAME = "child-collection"
        private const val CHILD_AGGREGATOR_NAME = "children"
    }

    private val fullCollection = database.getCollection(collection, FullTestEntity::class.java)

    fun findFullTestEntity(
        id: UUID
    ): FullTestEntity? = fullCollection.aggregate(
        listOf(
            Aggregates.lookup(
                CHILD_COLLECTION_NAME,
                ParentTestEntity::childIds.name,
                ChildTestEntity::id.name,
                CHILD_AGGREGATOR_NAME
            ),
            Aggregates.unwind(CHILD_AGGREGATOR_NAME.mongoParameter),
            Aggregates.match(
                Filters.and(
                    Filters.eq(ParentTestEntity::id.name, id),
                    Filters.eq("$CHILD_AGGREGATOR_NAME.${ChildTestEntity::isActive.name}", true)
                )
            ),
            Aggregates.group(
                OBJECT_ID_PARAMETER_NAME,
                Accumulators.first(FullTestEntity::parentId.name, ParentTestEntity::id.mongoParameter),
                Accumulators.first(FullTestEntity::parentName.name, ParentTestEntity::parentName.mongoParameter),
                Accumulators.push(FullTestEntity::childList.name, CHILD_AGGREGATOR_NAME.mongoParameter)
            )
        )
    ).firstOrNull()

}
