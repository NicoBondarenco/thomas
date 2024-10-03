package com.thomas.mongodb.sync.data

import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import com.thomas.core.model.pagination.PageRequestData
import com.thomas.core.model.pagination.PageResponse
import com.thomas.mongodb.sync.repository.MongoSyncRepository
import java.time.OffsetDateTime

class TestMongoRepository(
    database: MongoDatabase,
    collection: String
) : MongoSyncRepository<TestMongoEntity>(
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
        pageable: PageRequestData,
    ): PageResponse<TestMongoEntity> = paged(pageable)

    fun page(
        stringValues: List<String>,
        periodStart: OffsetDateTime,
        periodEnd: OffsetDateTime,
        pageable: PageRequestData,
    ): PageResponse<TestMongoEntity> = paged(
        pageable,
        Filters.and(
            Filters.`in`("stringValue", stringValues),
            Filters.gte("datetimeOffset", periodStart),
            Filters.lte("datetimeOffset", periodEnd),
        )
    )

}
