package com.thomas.management.data.neo4j.repository

import com.thomas.database.neo4j.repository.Neo4JRepository
import org.neo4j.ogm.session.SessionFactory

abstract class ManagementNeo4JRepository(
    sessionFactory: SessionFactory,
    protected val defaultDepth: Int = 5
) : Neo4JRepository(sessionFactory)