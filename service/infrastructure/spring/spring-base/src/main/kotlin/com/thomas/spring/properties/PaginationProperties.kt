package com.thomas.spring.properties

data class PaginationProperties(
    val default : PaginationDefaultProperties = PaginationDefaultProperties()
)

data class PaginationDefaultProperties(
    val pageNumber: Long = 1,
    val pageSize: Long = 10,
)
