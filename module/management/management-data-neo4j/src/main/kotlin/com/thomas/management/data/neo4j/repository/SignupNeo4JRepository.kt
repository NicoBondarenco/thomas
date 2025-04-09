package com.thomas.management.data.neo4j.repository

import com.thomas.management.data.entity.SignupEntity
import com.thomas.management.data.neo4j.model.mapper.toUserNode
import com.thomas.management.data.repository.SignupRepository
import org.neo4j.ogm.session.SessionFactory

class SignupNeo4JRepository(
    sessionFactory: SessionFactory
) : ManagementNeo4JRepository(sessionFactory), SignupRepository {

    override suspend fun signup(
        entity: SignupEntity
    ): SignupEntity = transaction { session ->
        entity.apply {
            session.save(this.toUserNode())
        }
    }

}
