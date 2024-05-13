package com.thomas.spring.configuration

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import com.thomas.spring.properties.SecurityProperties
import com.thomas.spring.util.groupDocuments
import com.thomas.spring.util.userDocuments
import jakarta.annotation.PostConstruct
import kotlinx.coroutines.runBlocking
import org.bson.Document
import org.springframework.context.annotation.Configuration

@Configuration
class DataManagementConfiguration(
    mongoDatabase: MongoDatabase,
    securityProperties: SecurityProperties,
) {

    private val userCollection = mongoDatabase.getCollection<Document>(securityProperties.jwt.userCollection)
    private val groupCollection = mongoDatabase.getCollection<Document>(securityProperties.jwt.groupCollection)

    @PostConstruct
    fun init() {
        insertMongoDocuments()
    }

    private fun insertMongoDocuments() = runBlocking {
        groupCollection.insertMany(groupDocuments)
        userCollection.insertMany(userDocuments)
    }

}
