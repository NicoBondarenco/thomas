package com.thomas.management.spring.properties

import com.thomas.exposed.properties.ExposedDatabaseProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "management")
data class ManagementProperties @ConstructorBinding constructor(
    val database: ExposedDatabaseProperties
)
