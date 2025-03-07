package com.thomas.database.neo4j.extension

import org.junit.jupiter.api.Assertions.fail
import org.neo4j.ogm.session.Session
import org.neo4j.ogm.session.SessionFactory

fun SessionFactory.purge(): Session = this.runQueries { this.purgeDatabase() }

fun SessionFactory.runScript(
    file: String,
): Session = this::class.java.getResourceAsStream(file).bufferedReader().readText().let { script ->
    this.runQueries {
        this.query(script.clearScript(), mapOf<String, Any>())
    }
}

private fun String.clearScript() = this
    .replace(Regex("\r\n\r\n"), "\r\n")
    .replace(Regex("//.*\r\n"), "")
    .replace(Regex("//.*\n"), "")
    .replace(Regex("\n\n"), "\n")

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