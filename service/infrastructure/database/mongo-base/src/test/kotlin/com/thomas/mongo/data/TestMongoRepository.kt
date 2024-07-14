package com.thomas.mongo.data

import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import com.thomas.core.model.pagination.PageRequest
import com.thomas.core.model.pagination.PageResponse
import com.thomas.mongo.repository.MongoRepository
import java.time.OffsetDateTime

class TestMongoRepository(
    database: MongoDatabase,
    collection: String
) : MongoRepository<TestMongoEntity>(
    database,
    collection,
    TestMongoEntity::class
) {

    fun all(): List<TestMongoEntity> = list()

    fun all(
        stringValues: List<String>,
        periodStart: OffsetDateTime,
        periodEnd: OffsetDateTime,
    ): List<TestMongoEntity> = list(
        Filters.and(
            Filters.`in`("stringValue", stringValues),
            Filters.gte("datetimeOffset", periodStart),
            Filters.lte("datetimeOffset", periodEnd),
        )
    )

    fun page(
        pageable: PageRequest,
    ): PageResponse<TestMongoEntity> = paged(pageable)

    fun page(
        stringValues: List<String>,
        periodStart: OffsetDateTime,
        periodEnd: OffsetDateTime,
        pageable: PageRequest,
    ): PageResponse<TestMongoEntity> = paged(
        pageable,
        Filters.and(
            Filters.`in`("stringValue", stringValues),
            Filters.gte("datetimeOffset", periodStart),
            Filters.lte("datetimeOffset", periodEnd),
        )
    )

}
