package com.thomas.spring

import com.thomas.core.exception.ErrorType
import com.thomas.spring.configuration.TestRestTemplateBuilder
import com.thomas.spring.data.response.ExceptionResponse
import com.thomas.spring.extension.toHttpStatus
import com.thomas.spring.i18n.SpringMessageI18N.exceptionInvalidArgumentParameterErrorsMessage
import com.thomas.spring.i18n.SpringMessageI18N.requestRequestParameterValidationConvertError
import com.thomas.spring.properties.PaginationProperties
import com.thomas.spring.resource.ExceptionRequest
import com.thomas.spring.resource.ValidateRequest
import com.thomas.spring.util.TokenBuilder
import com.thomas.spring.util.activeUser
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.HttpStatus.OK
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
class SpringContextTest : SpringContextBaseTest() {

    @Autowired
    private lateinit var tokenBuilder: TokenBuilder

    @Autowired
    private lateinit var restBuilder: TestRestTemplateBuilder

    @Autowired
    private lateinit var paginationProperties: PaginationProperties

    @LocalServerPort
    private var port: Int = -1

    @Test
    fun `Context loads`() {
        assertTrue(MONGO_CONTAINER.isRunning)
    }

    @Test
    fun `WHEN pageable properties are informed THEN should use the informed values`() {
        assertEquals(20, paginationProperties.default.pageSize)
        assertEquals(1, paginationProperties.default.pageNumber)
    }

    @ParameterizedTest
    @EnumSource(ErrorType::class)
    fun `WHEN detailed exception is thrown on controller THEN should map to correct HTTP error code`(type: ErrorType) {
        val token = tokenBuilder.generateToken(activeUser)
        val response = restBuilder.testRestTemplate(port, token)
            .getForEntity("/test/typed/$type", ExceptionResponse::class.java)

        assertEquals(type.toHttpStatus(), response.statusCode)
    }

    @ParameterizedTest
    @EnumSource(ErrorType::class)
    fun `WHEN detailed exception is thrown on service THEN should map to correct HTTP error code`(type: ErrorType) {
        val token = tokenBuilder.generateToken(activeUser)
        val response = restBuilder.testRestTemplate(port, token)
            .getForEntity("/test/service/$type", ExceptionResponse::class.java)

        assertEquals(type.toHttpStatus(), response.statusCode)
    }

    @Test
    fun `WHEN common exception is thrown on service THEN should map to correct HTTP error code`() {
        val token = tokenBuilder.generateToken(activeUser)
        val response = restBuilder.testRestTemplate(port, token)
            .getForEntity("/test/common", ExceptionResponse::class.java)

        assertEquals(INTERNAL_SERVER_ERROR, response.statusCode)
    }

    @Test
    fun `WHEN token is invalid THEN should return not authorized`() {
        val response = restBuilder.testRestTemplate(port, null)
            .getForEntity("/test/common", ExceptionResponse::class.java)

        assertEquals(UNAUTHORIZED, response.statusCode)
    }

    @Test
    fun `WHEN token is not bearer THEN should return not authorized`() {
        val token = tokenBuilder.generateToken(activeUser)
        val response = restBuilder.testRestTemplate(port, token, "qwerty")
            .getForEntity("/test/common", ExceptionResponse::class.java)

        assertEquals(UNAUTHORIZED, response.statusCode)
    }

    @Test
    fun `WHEN required parameter has invalid value THEN should return bad request`() {
        val token = tokenBuilder.generateToken(activeUser)
        val response = restBuilder.testRestTemplate(port, token, locale = null)
            .getForEntity("/test/arguments?key=qwerty", ExceptionResponse::class.java)

        assertEquals(BAD_REQUEST, response.statusCode)
        assertEquals(requestRequestParameterValidationConvertError("key", "qwerty"), response.body?.message)
    }

    @Test
    fun `WHEN required parameter is not sent THEN should return bad request`() {
        val token = tokenBuilder.generateToken(activeUser)
        val response = restBuilder.testRestTemplate(port, token, locale = null)
            .getForEntity("/test/arguments?key=", ExceptionResponse::class.java)

        assertEquals(BAD_REQUEST, response.statusCode)
        assertEquals(requestRequestParameterValidationConvertError("key", ""), response.body?.message)
    }

    @Test
    fun `WHEN body parameter has wrong type THEN should return bad request`() {
        val token = tokenBuilder.generateToken(activeUser)
        val response = restBuilder.testRestTemplate(port, token, locale = null)
            .postForEntity("/test/data", ExceptionRequest("qwerty", "qwerty"), ExceptionResponse::class.java)

        assertEquals(BAD_REQUEST, response.statusCode)
        assertEquals(exceptionInvalidArgumentParameterErrorsMessage(), response.body?.message)
    }

    @Test
    fun `WHEN body parameter is not valid THEN should return bad request`() {
        val token = tokenBuilder.generateToken(activeUser)
        val response = restBuilder.testRestTemplate(port, token, locale = null)
            .postForEntity("/test/validate", ValidateRequest("", 18), ExceptionResponse::class.java)

        assertEquals(BAD_REQUEST, response.statusCode)
        assertEquals(exceptionInvalidArgumentParameterErrorsMessage(), response.body?.message)
        assertEquals(2, (response.body?.detail as? Map<String, List<String>>)?.get("email")?.size)
        assertEquals(1, (response.body?.detail as? Map<String, List<String>>)?.get("quantity")?.size)
    }

    @Test
    fun `WHEN language is not informed THEN should use root locale`() {
        val token = tokenBuilder.generateToken(activeUser)
        val response = restBuilder.testRestTemplate(port, token, locale = null)
            .getForEntity("/test/locale", String::class.java)

        assertEquals(OK, response.statusCode)
        assertEquals("und", response.body)
    }

    @Test
    fun `WHEN language informed is invalid THEN should use root locale`() {
        val token = tokenBuilder.generateToken(activeUser)
        val response = restBuilder.testRestTemplate(port, token, locale = "qwerty")
            .getForEntity("/test/locale", String::class.java)

        assertEquals(OK, response.statusCode)
        assertEquals("und", response.body)
    }

    @Test
    fun `WHEN language informed is on i18n THEN should use the locale`() {
        val token = tokenBuilder.generateToken(activeUser)
        val response = restBuilder.testRestTemplate(port, token, locale = "pt-BR")
            .getForEntity("/test/locale", String::class.java)

        assertEquals(OK, response.statusCode)
        assertEquals("pt-BR", response.body)
    }

}
