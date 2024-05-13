package com.thomas.spring.resource

import com.thomas.core.context.SessionContextHolder.currentLocale
import com.thomas.core.exception.DetailedException
import com.thomas.core.exception.ErrorType
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.noContent
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test")
class SpringTestController(
    private val exceptionService: SpringTestService
) {

    @GetMapping("/typed/{type}")
    fun typed(
        @PathVariable("type") type: ErrorType
    ): ResponseEntity<Any> = throw object : DetailedException(
        type = type
    ) {}

    @GetMapping("/service/{type}")
    fun service(
        @PathVariable("type") type: ErrorType
    ): ResponseEntity<Any> = exceptionService.exceptionService(type).let {
        noContent().build()
    }

    @GetMapping("/common")
    fun common(): ResponseEntity<Any> = throw Exception()

    @GetMapping("/empty")
    fun empty(): ResponseEntity<Any> = noContent().build()

    @GetMapping("/arguments")
    fun arguments(
        @RequestParam(value = "key", required = true) key: Int,
    ): ResponseEntity<Any> = noContent().build()

    @PostMapping("/data")
    fun data(
        @RequestBody data: ExceptionModel,
    ): ResponseEntity<Any> = noContent().build()

    @PostMapping("/validate")
    fun validate(
        @Valid @RequestBody data: ValidateRequest,
    ): ResponseEntity<Any> = noContent().build()

    @GetMapping("/locale")
    fun locale(): ResponseEntity<Any> = ok(currentLocale.toLanguageTag())

}
