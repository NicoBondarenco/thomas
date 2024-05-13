package com.thomas.spring.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "pageable")
data class PaginationProperties @ConstructorBinding constructor(
    val default: PaginationDefaultProperties
)

data class PaginationDefaultProperties(
    val pageNumber: Long = 1,
    val pageSize: Long = 10,
)
