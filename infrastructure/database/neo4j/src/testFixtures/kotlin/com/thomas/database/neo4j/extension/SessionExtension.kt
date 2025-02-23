package com.thomas.database.neo4j.extension

import org.junit.jupiter.api.Assertions.fail
import org.neo4j.ogm.session.Session
import org.neo4j.ogm.session.SessionFactory

fun SessionFactory.purge(): Session = this.runQueries { this.purgeDatabase() }

fun SessionFactory.runScript(
    file: String,
): Session = this::class.java.getResourceAsStream(file).bufferedReader().readLines().filter {
    it.trim().isNotEmpty()
}.let { lines ->
    this.runQueries {
        lines.forEach { line ->
            this.query(line, mapOf<String, Any>())
        }
    }
}

fun SessionFactory.runQueries(
    queries: Session.() -> Unit
): Session = this.openSession().apply {
    this.beginTransaction()
    runCatching {
        this.queries()
    }.onFailure {
        this.transaction.rollback()
        fail(it)
    }.onSuccess {
        this.transaction.commit()
    }
}