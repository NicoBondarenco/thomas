package com.thomas.mongo.data

import com.mongodb.client.MongoDatabase
import com.thomas.mongo.repository.MongoRepository

class TestErrorRepository(
    database: MongoDatabase,
    collection: String
) : MongoRepository<TestErrorEntity>(
    database,
    collection,
    TestErrorEntity::class
)
