package com.thomas.management.spring.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "neo4j")
data class Neo4JProperties(
    val connectionUri: String = "",
    val databaseName: String = "",
    val databaseUsername: String = "",
    val databasePassword: String = "",
    val livenessCheck: Int = 0,
    val verifyConnection: Boolean = false,
    val poolSize: Int = 0,
    val nodePackages: List<String> = listOf(),
)
