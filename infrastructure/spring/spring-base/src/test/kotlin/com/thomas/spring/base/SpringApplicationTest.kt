package com.thomas.spring.base

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(
    scanBasePackages = ["com.thomas"]
)
open class SpringApplicationTest

fun main(args: Array<String>) {
    runApplication<SpringApplicationTest>(*args)
}
