package com.thomas.mongo.properties

import com.mongodb.ServerApiVersion

data class MongoDatabaseProperties(
    val connectionUrl: String,
    val databaseName: String,
    val connectionUsername: String,
    val connectionPassword: String,
    val apiVersion: ServerApiVersion
)
