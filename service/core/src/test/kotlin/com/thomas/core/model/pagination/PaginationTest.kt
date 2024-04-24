package com.thomas.core.model.pagination

import com.thomas.core.model.pagination.PageSortDirection.ASC
import com.thomas.core.model.pagination.PageSortDirection.DESC
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class PaginationTest {

    @Test
    fun `Create a page response from page request`() {
        val response = PageResponse.of(
            listOf("qwerty"),
            PageRequest(3, 10, listOf(PageSort("qwerty", ASC))),
            50
        )

        assertFalse(response.firstPage)
        assertFalse(response.lastPage)
        assertEquals(3L, response.pageNumber)
        assertEquals(10L, response.pageSize)
        assertEquals(5L, response.totalPages)
        assertEquals(50L, response.totalItems)
        assertEquals(1, response.sortedBy.size)
        assertEquals("qwerty", response.sortedBy[0].sortField)
        assertEquals(ASC, response.sortedBy[0].sortDirection)
    }

    @Test
    fun `Check if first page is set correctly`() {
        val response = PageResponse.of(
            listOf("qwerty"),
            PageRequest(1, 10, listOf(PageSort("qwerty", DESC))),
            50
        )

        assertTrue(response.firstPage)
        assertFalse(response.lastPage)
        assertEquals(1L, response.pageNumber)
        assertEquals(10L, response.pageSize)
        assertEquals(5L, response.totalPages)
        assertEquals(50L, response.totalItems)
        assertEquals(1, response.sortedBy.size)
        assertEquals("qwerty", response.sortedBy[0].sortField)
        assertEquals(DESC, response.sortedBy[0].sortDirection)
    }

    @Test
    fun `Check if last page is set correctly`() {
        val response = PageResponse.of(
            listOf("qwerty"),
            PageRequest(5, 10, listOf()),
            50
        )

        assertFalse(response.firstPage)
        assertTrue(response.lastPage)
        assertEquals(5L, response.pageNumber)
        assertEquals(10L, response.pageSize)
        assertEquals(5L, response.totalPages)
        assertEquals(50L, response.totalItems)
        assertTrue(response.sortedBy.isEmpty())
    }

    @Test
    fun `Check page calculation`() {
        val response = PageResponse.of(
            listOf("qwerty"),
            PageRequest(5, 10, listOf()),
            51
        )

        assertFalse(response.firstPage)
        assertFalse(response.lastPage)
        assertEquals(5L, response.pageNumber)
        assertEquals(10L, response.pageSize)
        assertEquals(6L, response.totalPages)
        assertEquals(51L, response.totalItems)
        assertTrue(response.sortedBy.isEmpty())
    }

    @Test
    fun `Check if map is converting correctly`() {
        val response = PageResponse.of(
            listOf("qwerty", "zxcv", "882589c9-f441-4d4a-8095-5a27b7dc726f"),
            PageRequest(5, 10),
            50
        ).map { it.length }

        assertEquals(3, response.contentList.size)
        assertTrue(response.contentList.contains(6))
        assertTrue(response.contentList.contains(4))
        assertTrue(response.contentList.contains(36))
    }

}
