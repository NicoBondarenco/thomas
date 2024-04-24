package com.thomas.mongo.data

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import com.thomas.mongo.repository.MongoRepository

class TestErrorRepository(
    database: MongoDatabase,
    collection: String
) : MongoRepository<TestErrorEntity>(
    database,
    collection,
    TestErrorEntity::class
)
