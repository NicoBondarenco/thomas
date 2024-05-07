package com.thomas.exposed.repository

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.transaction

abstract class ExposedRepository(
    private val database: Database
) {

    protected fun <T> transacted(
        statement: Transaction.() -> T
    ): T = transaction(database, statement)

}
