package com.thomas.management.data.exposed.mapping

import com.thomas.exposed.table.ExposedTable

abstract class ManagementTable(
    tableName: String
) : ExposedTable(schemaName = "management", tableName = tableName)
