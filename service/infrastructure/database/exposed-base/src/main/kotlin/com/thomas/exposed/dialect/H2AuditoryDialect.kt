package com.thomas.exposed.dialect

import com.fasterxml.jackson.databind.ObjectMapper
import com.thomas.exposed.properties.ExposedAuditoryProperties
import org.jetbrains.exposed.sql.vendors.H2Dialect

internal class H2AuditoryDialect(
    override val auditoryProperties: ExposedAuditoryProperties,
    override val mapper: ObjectMapper,
) : H2Dialect(), AuditoryDialect
