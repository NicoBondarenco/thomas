package com.thomas.management.data.neo4j.repository

import com.thomas.management.data.neo4j.util.purge
import com.thomas.management.data.neo4j.util.runScript
import com.thomas.management.data.repository.OrganizationRepository
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.neo4j.ogm.config.Configuration
import org.neo4j.ogm.session.SessionFactory

@TestInstance(PER_CLASS)
abstract class Neo4JRepositoryTest {

    protected val configuration: Configuration = Configuration.Builder()
        .uri("bolt://localhost:7687")
        .credentials("neo4j", "Meruss@453822")
        .database("neo4j")
        .connectionLivenessCheckTimeout(10000)
        .verifyConnection(true)
        .connectionPoolSize(20)
        .useNativeTypes()
        .build()

    protected lateinit var sessionFactory: SessionFactory

    protected lateinit var organizationRepository: OrganizationRepository

    @BeforeAll
    fun beforeAll() {
        sessionFactory = SessionFactory(configuration, "com.thomas.management.data.neo4j.model.node")
        organizationRepository = OrganizationNeo4JRepository(sessionFactory)
        sessionFactory.runScript(file = "setup")
    }

    @AfterAll
    fun afterAll() {
        sessionFactory.runScript(file = "teardown")
    }

    @AfterEach
    fun afterEach() {
        sessionFactory.purge()
    }

}