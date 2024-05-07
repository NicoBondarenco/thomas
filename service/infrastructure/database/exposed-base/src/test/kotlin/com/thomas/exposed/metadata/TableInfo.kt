package com.thomas.exposed.metadata

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

abstract class TableInfo(name: String) : Table(name) {
    abstract val schema: Column<String>
    abstract val table: Column<String>
}

object PostgreSQLTableInfo : TableInfo("information_schema.tables") {
    override val schema = varchar("table_schema", 250)
    override val table = varchar("table_name", 250)
}

data class TableMetadata(
    val schema: String,
    val table: String,
)
