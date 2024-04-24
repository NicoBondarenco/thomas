package com.thomas.core.model.pagination

data class PageRequest(
    val pageNumber: Long = 1,
    val pageSize: Long = 10,
    val pageSort: List<PageSort> = listOf()
)
