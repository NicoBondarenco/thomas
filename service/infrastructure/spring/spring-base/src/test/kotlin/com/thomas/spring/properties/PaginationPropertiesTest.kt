package com.thomas.spring.properties

import com.thomas.spring.SpringContextBaseTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("pageable")
class PaginationPropertiesTest : SpringContextBaseTest() {

    @Autowired
    private lateinit var paginationProperties: PaginationProperties

    @Test
    fun `WHEN no pageable properties are informed THEN should use the default values`() {
        assertEquals(10, paginationProperties.default.pageSize)
        assertEquals(1, paginationProperties.default.pageNumber)
    }

}
