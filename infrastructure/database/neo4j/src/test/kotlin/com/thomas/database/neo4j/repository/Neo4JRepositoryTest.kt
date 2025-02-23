package com.thomas.database.neo4j.repository

import org.neo4j.ogm.session.SessionFactory

abstract class Neo4JRepositoryTest : BaseNeo4JRepositoryTest<TestNeo4JRepository>(
    nodesPackages = listOf("com.thomas.database.neo4j.node"),
    setupScript = "/setup.cypher",
    teardownScript = "/teardown.cypher",
) {

    override fun createRepository(
        sessionFactory: SessionFactory
    ): TestNeo4JRepository = TestNeo4JRepository(sessionFactory)

}
