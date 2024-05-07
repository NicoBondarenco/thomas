package com.thomas.exposed.dialect

import com.fasterxml.jackson.databind.ObjectMapper
import com.thomas.exposed.properties.ExposedAuditoryProperties
import org.jetbrains.exposed.sql.vendors.PostgreSQLDialect

internal class PostgreSQLAuditoryDialect(
    override val auditoryProperties: ExposedAuditoryProperties,
    override val mapper: ObjectMapper,
) : PostgreSQLDialect(), AuditoryDialect
