package com.thomas.spring.base.resource

import com.thomas.core.context.SessionContextHolder.currentLocale
import com.thomas.core.context.SessionContextHolder.currentUnit
import com.thomas.core.exception.ApplicationException
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

@Suppress("UnusedParameter")
@RestController
@RequestMapping("/test")
open class SpringTestController(
    private val exceptionService: SpringTestService
) {

    @GetMapping("/typed/{type}")
    open fun typed(
        @PathVariable("type") type: ErrorType
    ): ResponseEntity<Any> = throw object : ApplicationException(
        type = type
    ) {}

    @GetMapping("/service/{type}")
    open fun service(
        @PathVariable("type") type: ErrorType
    ): ResponseEntity<Any> = exceptionService.exceptionService(type).let {
        noContent().build()
    }

    @Suppress("TooGenericExceptionThrown")
    @GetMapping("/common")
    open fun common(): ResponseEntity<Any> = throw Exception()

    @GetMapping("/empty")
    open fun empty(): ResponseEntity<Any> = noContent().build()

    @GetMapping("/arguments")
    open fun arguments(
        @RequestParam(value = "key", required = true) key: Int,
    ): ResponseEntity<Any> = noContent().build()

    @PostMapping("/data")
    open fun data(
        @RequestBody data: ExceptionModel,
    ): ResponseEntity<Any> = noContent().build()

    @PostMapping("/validate")
    open fun validate(
        @Valid @RequestBody data: ValidateRequest,
    ): ResponseEntity<Any> = noContent().build()

    @GetMapping("/locale")
    open fun locale(): ResponseEntity<Any> = ok(currentLocale.toLanguageTag())

    @GetMapping("/unit")
    open fun unit(): ResponseEntity<Any> = ok(currentUnit)

}
