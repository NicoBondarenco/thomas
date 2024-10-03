package com.thomas.mongodb.properties

import com.mongodb.ServerApiVersion
import com.mongodb.ServerApiVersion.V1

data class MongoDatabaseProperties(
    val connectionUrl: String = "",
    val databaseName: String = "",
    val connectionUsername: String = "",
    val connectionPassword: String = "",
    val apiVersion: ServerApiVersion = V1
)
