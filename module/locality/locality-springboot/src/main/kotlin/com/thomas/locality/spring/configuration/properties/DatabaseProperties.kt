package com.thomas.locality.spring.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "locality.database")
data class DatabaseProperties @ConstructorBinding constructor(
    val host: String,
    val port: Int,
    val database: String,
    val username: String,
    val password: String,
    val schema: String,
    val pool: DatabasePoolProperties,
)

data class DatabasePoolProperties(
    val initialSize: Int,
    val maxSize: Int,
    val idleTime: Long,
    val creationTimeout: Long,
    val acquireTimeout: Long,
    val maxLifetime: Long,
    val validationQuery: String,
)