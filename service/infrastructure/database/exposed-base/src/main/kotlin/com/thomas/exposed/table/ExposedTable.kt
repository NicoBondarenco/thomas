package com.thomas.exposed.table

import java.util.UUID
import org.jetbrains.exposed.dao.id.IdTable

abstract class ExposedTable(
    schemaName: String,
    tableName: String,
) : IdTable<UUID>("$schemaName.$tableName") {

    final override val id = uuid("id").entityId()
    override val primaryKey = PrimaryKey(arrayOf(id), "pk_$tableName")

    val createdAt = timestampWithTimezone("created_at")
        .index("dx_${tableName}_created_at")

    val updatedAt = timestampWithTimezone("updated_at")
        .index("dx_${tableName}_updated_at")

}
