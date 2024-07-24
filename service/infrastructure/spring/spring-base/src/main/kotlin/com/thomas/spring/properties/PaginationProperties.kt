package com.thomas.spring.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "pageable")
data class PaginationProperties(
    val default: PaginationDefaultProperties = PaginationDefaultProperties()
)

data class PaginationDefaultProperties(
    val pageNumber: Long = 1,
    val pageSize: Long = 10,
)
