package com.thomas.exposed.properties

import com.thomas.exposed.configuration.ExposedObjectMapperFactory.create
import com.thomas.exposed.dialect.H2AuditoryDialect
import com.thomas.exposed.dialect.PostgreSQLAuditoryDialect
import org.jetbrains.exposed.sql.vendors.H2Dialect
import org.jetbrains.exposed.sql.vendors.PostgreSQLDialect
import org.jetbrains.exposed.sql.vendors.VendorDialect

enum class ExposedDatabaseDialect(
    internal val vendorDialect: (ExposedAuditoryProperties?) -> VendorDialect
) {

    POSTGRESQL({ props ->
        props?.let {
            PostgreSQLAuditoryDialect(it, create())
        } ?: PostgreSQLDialect()
    }),
    H2({ props ->
        props?.let {
            H2AuditoryDialect(it, create())
        } ?: H2Dialect()
    }),

}
