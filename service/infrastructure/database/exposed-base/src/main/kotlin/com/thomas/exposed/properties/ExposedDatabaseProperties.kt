package com.thomas.exposed.properties

import com.thomas.exposed.properties.ExposedIsolationLevel.TRANSACTION_READ_COMMITTED

@Suppress("LongParameterList")
class ExposedDatabaseProperties(
    val driverClass: String,
    val connectionUrl: String,
    val databaseUsername: String,
    val databasePassword: String,
    val defaultSchema: String,
    val explicitDialect: ExposedDatabaseDialect,
    val connectionTimeout: Long = 20000,
    val idleTimeout: Long = 20000,
    val maximumLifetime: Long = 60000,
    val maximumPool: Int = 5,
    val isolationLevel: ExposedIsolationLevel = TRANSACTION_READ_COMMITTED,
    val repetitionAttempts: Int = 3,
    val databaseAuditory: ExposedAuditoryProperties? = null,
)
