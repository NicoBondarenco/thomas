package com.thomas.exposed.metadata

import com.thomas.exposed.configuration.AUDITORY_SCHEMA_NAME
import com.thomas.exposed.table.timestampWithTimezone
import java.util.UUID
import org.jetbrains.exposed.dao.id.IdTable

abstract class AuditoryTable(
    tableName: String,
) : IdTable<UUID>("$AUDITORY_SCHEMA_NAME.$tableName") {

    final override val id = uuid("id").entityId()
    override val primaryKey by lazy { super.primaryKey ?: PrimaryKey(arrayOf(id), "pk_$tableName") }

    val actionType = varchar("action_type", 250)
        .index("dx_${tableName}_action_type")

    val userId = uuid("user_id")
        .index("dx_${tableName}_user_id")

    val entityId = varchar("entity_id", 250)
        .index("dx_${tableName}_entity_id")

    val entitySnapshot = text("entity_snapshot", eagerLoading = true)
        .nullable()
        .index("dx_${tableName}_entity_snapshot")

    val createdAt = timestampWithTimezone("created_at")
        .index("dx_${tableName}_created_at")

}
