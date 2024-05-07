package com.thomas.exposed.dialect

import com.fasterxml.jackson.databind.ObjectMapper
import com.thomas.core.context.SessionContextHolder.currentUser
import com.thomas.exposed.configuration.AUDITORY_SCHEMA_NAME
import com.thomas.exposed.configuration.AUDITORY_TABLE_SUFFIX
import com.thomas.exposed.properties.ExposedAuditoryProperties
import java.time.OffsetDateTime.now
import java.time.ZoneOffset.UTC
import java.time.format.DateTimeFormatter
import java.util.UUID
import java.util.UUID.randomUUID
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityChange
import org.jetbrains.exposed.dao.EntityChangeType
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.toEntity
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.reflections.Reflections

@Suppress("TooManyFunctions")
internal interface AuditoryDialect {

    companion object {
        private val DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSSSSX")
    }

    val auditoryProperties: ExposedAuditoryProperties

    val mapper: ObjectMapper

    fun createSchemaStatements(): List<String> = listOf(
        "CREATE SCHEMA IF NOT EXISTS \"$AUDITORY_SCHEMA_NAME\";"
    )

    fun createTableStatements(
        table: String
    ): List<String> = listOf(
        """
            CREATE TABLE IF NOT EXISTS "$AUDITORY_SCHEMA_NAME"."$table$AUDITORY_TABLE_SUFFIX"
            (
                "id"              UUID                        NOT NULL CONSTRAINT "pk_$table$AUDITORY_TABLE_SUFFIX" PRIMARY KEY,
                "action_type"     VARCHAR(250)                NOT NULL,
                "user_id"         UUID                        NOT NULL,
                "entity_id"       VARCHAR(250)                NOT NULL,
                "entity_snapshot" TEXT                        NULL,
                "created_at"      TIMESTAMP(9) WITH TIME ZONE NOT NULL
            );
        """.trimIndent()
    ) + createIndexesStatements(AUDITORY_SCHEMA_NAME, table)

    private fun createIndexesStatements(
        schema: String,
        table: String
    ): List<String> = listOf(
        "action_type",
        "user_id",
        "entity_id",
        "created_at",
    ).map { createIndexStatement(schema, table, it) }

    private fun createIndexStatement(
        schema: String,
        table: String,
        field: String,
    ) = "CREATE INDEX IF NOT EXISTS \"dx_$table${AUDITORY_TABLE_SUFFIX}_${field}\" ON \"$schema\".\"$table$AUDITORY_TABLE_SUFFIX\" (\"$field\");"

    fun insertTableStatement(
        table: String,
        actionType: EntityChangeType,
        userId: UUID,
        entityId: Any,
        entitySnapshot: String?,
    ): String = """
        INSERT INTO "$AUDITORY_SCHEMA_NAME"."$table$AUDITORY_TABLE_SUFFIX" 
        ("id", "action_type", "user_id", "entity_id", "entity_snapshot", "created_at")
        VALUES ('${randomUUID()}', '${actionType.action}', '$userId', '$entityId', ${entitySnapshot.auditValue()}, '${createAt()}');
    """.trimIndent()

    fun auditoryStatements(): List<String> =
        createSchemaStatements() + auditoryProperties.auditableTables().map {
            createTableStatements(it)
        }.flatten()

    fun insertStatement(
        action: EntityChange
    ) = this.insertTableStatement(
        action.tableNameWithoutSchema(),
        action.changeType,
        currentUser.userId,
        action.entityId(),
        action.entityJson(mapper)
    )

    private fun ExposedAuditoryProperties.auditableTables() = Reflections(tablesPackage)
        .getSubTypesOf(Table::class.java)
        .mapNotNull { it.kotlin.objectInstance }
        .filterNot { tablesExcluded.contains(it.tableName) }
        .map { it.nameInDatabaseCaseUnquoted() }

    private fun EntityChange.tableNameWithoutSchema() = this
        .entityClass
        .table
        .nameInDatabaseCaseUnquoted()

    private fun EntityChange.entityId() = this.entityId.value.toString()

    private fun EntityChange.entityJson(
        mapper: ObjectMapper
    ) = this.entityValues()?.let { mapper.writeValueAsString(it) }

    private fun EntityChange.entityValues() = this.toEntity()
        ?.readValues
        ?.toEntityValues()

    @Suppress(names = ["UNCHECKED_CAST"])
    private fun EntityChange.toEntity() = this.toEntity(this.entityClass as EntityClass<UUID, Entity<UUID>>)

    private fun ResultRow.toEntityValues(): Map<String, Any?> = this.fieldIndex
        .mapValues { this.getOrNull(it.key) }
        .mapKeys { (it.key as Column).name }

    private val EntityChangeType.action
        get() = this.name.uppercase()

    private fun String?.auditValue() = this?.let { "'$it'" } ?: "NULL"

    private fun createAt() = DATETIME_FORMATTER.format(now(UTC))

}
