package com.thomas.management.data.neo4j.repository

import com.thomas.database.neo4j.repository.BaseNeo4JRepositoryTest
import com.thomas.database.neo4j.repository.Neo4JRepository
import org.neo4j.ogm.config.Configuration

abstract class ManagementNeo4JRepositoryTest<R : Neo4JRepository> : BaseNeo4JRepositoryTest<R>(
    nodesPackages = listOf("com.thomas.management.data.neo4j.model.node"),
    setupScript = "/scripts/setup.cypher",
    teardownScript = "/scripts/teardown.cypher",
) {

    override fun createConfiguration(): Configuration = Configuration.Builder()
        .uri("bolt://localhost:7687")
        .credentials("neo4j", "Meruss@453822")
        .database("neo4j")
        .connectionLivenessCheckTimeout(10000)
        .verifyConnection(true)
        .connectionPoolSize(20)
        .useNativeTypes()
        .build()

}