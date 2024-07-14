package com.thomas.spring.configuration

import com.mongodb.client.MongoDatabase
import com.thomas.spring.properties.SecurityProperties
import com.thomas.spring.util.groupDocuments
import com.thomas.spring.util.userDocuments
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration

@Configuration
class DataManagementConfiguration(
    mongoDatabase: MongoDatabase,
    securityProperties: SecurityProperties,
) {

    private val userCollection = mongoDatabase.getCollection(securityProperties.jwt.userCollection)
    private val groupCollection = mongoDatabase.getCollection(securityProperties.jwt.groupCollection)

    @PostConstruct
    fun init() {
        insertMongoDocuments()
    }

    private fun insertMongoDocuments() {
        groupCollection.insertMany(groupDocuments)
        userCollection.insertMany(userDocuments)
    }

}
