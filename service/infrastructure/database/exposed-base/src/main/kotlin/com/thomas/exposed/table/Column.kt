package com.thomas.exposed.table

import java.time.OffsetDateTime
import org.jetbrains.exposed.dao.ColumnWithTransform
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

fun Table.timestampWithTimezone(
    name: String,
    precision: Int = 9
): Column<OffsetDateTime> = registerColumn(
    name,
    OffsetDateTimeColumnType(precision)
)

inline fun <reified TReal : Enum<TReal>> Column<String>.enum() = ColumnWithTransform(
    column = this,
    toColumn = { it.name },
    toReal = { enumValueOf(it) as TReal }
)

inline fun <reified TReal : Enum<TReal>> Column<String?>.nullableEnum() = ColumnWithTransform(
    column = this,
    toColumn = { it?.name },
    toReal = { c -> c?.let { enumValueOf(it) as TReal } }
)
