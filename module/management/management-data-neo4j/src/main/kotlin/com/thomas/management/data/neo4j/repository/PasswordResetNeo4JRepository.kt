package com.thomas.management.data.neo4j.repository

import com.thomas.database.neo4j.filter.isEquals
import com.thomas.management.data.entity.PasswordResetEntity
import com.thomas.management.data.neo4j.model.mapper.toPasswordResetEntity
import com.thomas.management.data.neo4j.model.mapper.toPasswordResetNode
import com.thomas.management.data.neo4j.model.node.PasswordResetNode
import com.thomas.management.data.repository.PasswordResetRepository
import org.neo4j.ogm.cypher.Filters
import org.neo4j.ogm.session.SessionFactory

class PasswordResetNeo4JRepository(
    sessionFactory: SessionFactory
) : ManagementNeo4JRepository(sessionFactory), PasswordResetRepository {

    override suspend fun upsertToken(
        entity: PasswordResetEntity
    ): PasswordResetEntity = transaction { session ->
        session.delete(PasswordResetNode::class.java, Filters(isEquals(PasswordResetNode::userId, entity.userId)), false)
        entity.apply {
            session.save(this.toPasswordResetNode())
        }
    }

    override suspend fun findByToken(
        resetToken: String
    ): PasswordResetEntity? = sessionFactory.openSession()
        .loadAll(PasswordResetNode::class.java, Filters(isEquals(PasswordResetNode::resetToken, resetToken)))
        .firstOrNull()?.toPasswordResetEntity()

    override suspend fun deleteToken(
        resetToken: String
    ): Unit = transaction { session ->
        session.delete(PasswordResetNode::class.java, Filters(isEquals(PasswordResetNode::resetToken, resetToken)), false)
    }

}
