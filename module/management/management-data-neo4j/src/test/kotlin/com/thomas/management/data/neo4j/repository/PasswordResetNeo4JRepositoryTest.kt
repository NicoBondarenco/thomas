package com.thomas.management.data.neo4j.repository

import com.thomas.database.neo4j.filter.isEquals
import com.thomas.management.data.entity.PasswordResetEntity
import com.thomas.management.data.entity.passwordResetEntity
import com.thomas.management.data.neo4j.model.mapper.toPasswordResetEntity
import com.thomas.management.data.neo4j.model.node.PasswordResetNode
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.neo4j.ogm.cypher.Filters
import org.neo4j.ogm.session.SessionFactory

class PasswordResetNeo4JRepositoryTest : ManagementFunSpec<PasswordResetNeo4JRepository>(
    body = {

        initNodes(
            mapOf(
                PasswordResetNode::class to { (it as PasswordResetNode).toPasswordResetEntity() },
            )
        )

        context(name = "Insert Reset Token") {
            val entity = passwordResetEntity
            repository.upsertToken(entity)

            val nodes = sessionFactory.openSession().loadAll(PasswordResetNode::class.java, Filters(isEquals(PasswordResetNode::userId, entity.userId)))
            nodes.size shouldBe 1

            val node = nodes.first()
            node.toPasswordResetEntity() shouldBe entity
        }

        context(name = "Update Reset Token", script = "/scripts/reset/data.cypher") {
            val existent = entities(PasswordResetEntity::class).shuffled().first()
            val resets = entities(PasswordResetEntity::class).filter { it.userId == existent.userId }
            resets.size shouldBe 1

            val entity = passwordResetEntity.copy(userId = existent.userId)
            repository.upsertToken(entity)

            val nodes = sessionFactory.openSession().loadAll(PasswordResetNode::class.java, Filters(isEquals(PasswordResetNode::userId, entity.userId)))
            nodes.size shouldBe 1

            val node = nodes.first()
            node.toPasswordResetEntity() shouldBe entity
        }

        context(name = "Find by Token", script = "/scripts/reset/data.cypher") {
            val existent = entities(PasswordResetEntity::class).shuffled().first()

            val entity = repository.findByToken(existent.resetToken)
            entity shouldNotBe null
            entity shouldBe existent
        }

    }
) {

    override fun createRepository(
        sessionFactory: SessionFactory
    ): PasswordResetNeo4JRepository = PasswordResetNeo4JRepository(sessionFactory)

}