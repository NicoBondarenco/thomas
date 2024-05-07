package com.thomas.exposed.configuration

import com.thomas.exposed.dialect.AuditoryDialect
import com.thomas.exposed.properties.ExposedDatabaseProperties
import javax.sql.DataSource
import org.jetbrains.exposed.dao.EntityChange
import org.jetbrains.exposed.dao.EntityHook
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.DatabaseConfig
import org.jetbrains.exposed.sql.Schema
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.vendors.DatabaseDialect

object ExposedDatabaseFactory {

    fun create(
        datasource: DataSource,
        properties: ExposedDatabaseProperties,
    ): Database = Database.connect(
        datasource = datasource,
        databaseConfig = configuration(properties),
    ).configureAuditory()

    fun create(
        properties: ExposedDatabaseProperties
    ): Database = create(
        datasource = ExposedDatasourceFactory.create(properties),
        properties = properties,
    )

    private val ExposedDatabaseProperties.dialect
        get() = explicitDialect.vendorDialect(databaseAuditory)

    private fun configuration(
        properties: ExposedDatabaseProperties
    ) = DatabaseConfig.invoke {
        this.defaultSchema = Schema(properties.defaultSchema)
        this.explicitDialect = properties.dialect
        this.defaultIsolationLevel = properties.isolationLevel.isolationCode
        this.defaultRepetitionAttempts = properties.repetitionAttempts
    }

    private val DatabaseDialect.asAuditable: AuditoryDialect?
        get() = this.takeIf { it is AuditoryDialect } as? AuditoryDialect

    private fun Database.configureAuditory(): Database = this.apply {
        this.dialect.asAuditable?.let {
            transaction(this) {
                execInBatch(it.auditoryStatements())
            }
            configureEntityHook()
        }
    }

    private fun configureEntityHook() = EntityHook.subscribe { action ->
        TransactionManager.current().audit(action)
    }

    private fun Transaction.audit(
        action: EntityChange
    ) = this.db.dialect.asAuditable?.takeIf {
        !it.auditoryProperties.tablesExcluded.contains(action.tableAuditName())
    }?.insertStatement(action)?.let {
        this.exec(it)
    }

    private fun EntityChange.tableAuditName() = this
        .entityClass
        .table
        .tableName

}
