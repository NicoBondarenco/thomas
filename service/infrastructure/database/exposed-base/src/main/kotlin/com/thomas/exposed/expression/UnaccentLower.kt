package com.thomas.exposed.expression

import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.Function
import org.jetbrains.exposed.sql.QueryBuilder
import org.jetbrains.exposed.sql.TextColumnType
import org.jetbrains.exposed.sql.append

class UnaccentLower<T : String?>(
    private val expr: Expression<T>
) : Function<T>(TextColumnType()) {

    override fun toQueryBuilder(
        queryBuilder: QueryBuilder
    ): Unit = queryBuilder {
        append("unaccented_lower(", expr, ")")
    }

}
