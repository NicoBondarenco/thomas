package com.thomas.database.komapper.extension

import kotlin.reflect.KType
import org.komapper.r2dbc.R2dbcDataType
import org.komapper.r2dbc.R2dbcDataTypeProvider
import org.komapper.r2dbc.R2dbcDatabase
import org.komapper.tx.core.CoroutineTransactionOperator
import org.komapper.tx.core.TransactionAttribute.REQUIRES_NEW
import org.komapper.tx.core.TransactionProperty.IsolationLevel.READ_COMMITTED

suspend fun <R> R2dbcDatabase.writeTransaction(
    block: suspend (CoroutineTransactionOperator) -> R
) = this.withTransaction(REQUIRES_NEW, READ_COMMITTED, block)

suspend fun <R> R2dbcDatabase.readTransaction(
    block: suspend (CoroutineTransactionOperator) -> R
) = this.withTransaction(REQUIRES_NEW, READ_COMMITTED, block)

object EmptyR2dbcDataTypeProvider : R2dbcDataTypeProvider {
    override fun <T : Any> get(type: KType): R2dbcDataType<T>? = null
}