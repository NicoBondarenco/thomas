package com.thomas.mongodb.extension

import com.thomas.core.model.pagination.PageRequestData
import com.thomas.core.model.pagination.PageSort
import com.thomas.core.model.pagination.PageSortDirection.ASC
import com.thomas.core.model.pagination.PageSortDirection.DESC
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PageRequestExtensionTest {

    @Test
    fun `PageRequest to MongoBD sort`() {
        val orderBy = PageRequestData(
            pageSort = listOf(PageSort("field_one", ASC), PageSort("field_two", DESC))
        ).orderBy()

        assertEquals(1, orderBy.toBsonDocument()["field_one"]!!.asInt32().value)
        assertEquals(-1, orderBy.toBsonDocument()["field_two"]!!.asInt32().value)
    }

}
