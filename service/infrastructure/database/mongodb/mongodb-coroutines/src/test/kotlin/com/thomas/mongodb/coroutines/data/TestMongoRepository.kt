package com.thomas.mongodb.coroutines.data

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import com.thomas.core.coroutines.model.pagination.PageFlowResponse
import com.thomas.core.model.pagination.PageRequestData
import com.thomas.mongodb.coroutines.repository.MongoCoroutinesRepository
import java.time.OffsetDateTime
import kotlinx.coroutines.flow.Flow

class TestMongoRepository(
    database: MongoDatabase,
    collection: String
) : MongoCoroutinesRepository<TestMongoEntity>(
    database,
    collection,
    TestMongoEntity::class
) {

    fun all(): Flow<TestMongoEntity> = list()

    fun all(
        stringValues: List<String>,
        periodStart: OffsetDateTime,
        periodEnd: OffsetDateTime,
    ): Flow<TestMongoEntity> = list(
        Filters.and(
            Filters.`in`("stringValue", stringValues),
            Filters.gte("datetimeOffset", periodStart),
            Filters.lte("datetimeOffset", periodEnd),
        )
    )

    suspend fun page(
        pageable: PageRequestData,
    ): PageFlowResponse<TestMongoEntity> = paged(pageable)

    suspend fun page(
        stringValues: List<String>,
        periodStart: OffsetDateTime,
        periodEnd: OffsetDateTime,
        pageable: PageRequestData,
    ): PageFlowResponse<TestMongoEntity> = paged(
        pageable,
        Filters.and(
            Filters.`in`("stringValue", stringValues),
            Filters.gte("datetimeOffset", periodStart),
            Filters.lte("datetimeOffset", periodEnd),
        )
    )

}
