package com.thomas.management.spring.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "token")
data class TokenProperties (
    val accessDuration: Long = 0,
    val refreshDuration: Long = 0,
)