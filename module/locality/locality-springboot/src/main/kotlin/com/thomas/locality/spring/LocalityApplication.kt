package com.thomas.locality.spring

import com.thomas.locality.spring.configuration.properties.DatabaseProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.thomas"])
@EnableConfigurationProperties(
    DatabaseProperties::class
)
class LocalityApplication

fun main(args: Array<String>) {
    runApplication<LocalityApplication>(*args)
}