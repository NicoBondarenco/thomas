package com.thomas.management.spring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.thomas"])
class ManagementApplication

fun main(args: Array<String>) {
    runApplication<ManagementApplication>(*args)
}
