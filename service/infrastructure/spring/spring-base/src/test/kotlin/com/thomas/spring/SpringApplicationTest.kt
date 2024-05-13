package com.thomas.spring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(
    scanBasePackages = ["com.thomas"]
)
class SpringApplicationTest

fun main(args: Array<String>) {
    runApplication<SpringApplicationTest>(*args)
}
