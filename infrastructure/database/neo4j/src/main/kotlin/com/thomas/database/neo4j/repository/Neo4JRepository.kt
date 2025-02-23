package com.thomas.database.neo4j.repository

import com.thomas.core.extension.withSessionContextIO
import org.neo4j.ogm.session.Session
import org.neo4j.ogm.session.SessionFactory

abstract class Neo4JRepository(
    protected val sessionFactory: SessionFactory
) {

    protected suspend fun <T> transaction(block: (session: Session) -> T): T = withSessionContextIO {
        sessionFactory.openSession().let { session ->
            session.beginTransaction()
            runCatching {
                block(session)
            }.onFailure {
                session.transaction.rollback()
            }.onSuccess {
                session.transaction.commit()
            }
        }.getOrThrow()
    }

}
