package com.thomas.exposed.table

import com.thomas.exposed.model.auditable.ParentTable
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ExposedTableTest {

    @Test
    fun `Primary key name should be table name with pk_ prefix`() {
        assertEquals("pk_parent", ParentTable.primaryKey.name)
    }

    @Test
    fun `Primary key column should be the column id`() {
        assertEquals(ParentTable.id, ParentTable.primaryKey.columns.first())
    }

}
