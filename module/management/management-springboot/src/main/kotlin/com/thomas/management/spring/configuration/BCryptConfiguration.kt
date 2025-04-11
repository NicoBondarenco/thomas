package com.thomas.management.spring.configuration

import com.thomas.management.domain.crypt.Hasher
import com.thomas.management.spring.configuration.properties.BCryptProperties
import com.thomas.management.spring.crypt.bouncycastle.BCryptHasher
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(BCryptProperties::class)
class BCryptConfiguration {

    @Bean
    fun hasher(
        bCryptProperties: BCryptProperties,
    ): Hasher = BCryptHasher(bCryptProperties)

}