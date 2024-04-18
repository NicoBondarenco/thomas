package com.thomas.mongo.data

import com.mongodb.client.MongoDatabase
import com.thomas.mongo.repository.MongoRepository

class TestMongoRepository(
    database: MongoDatabase,
    collection: String
) : MongoRepository<TestMongoEntity>(
    database,
    collection,
    TestMongoEntity::class
)
