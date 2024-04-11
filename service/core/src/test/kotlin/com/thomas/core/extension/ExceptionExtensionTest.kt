package com.thomas.core.extension

import com.thomas.core.HttpApplicationException.Companion.badRequest
import com.thomas.core.i18n.CoreMessageI18N.coreExceptionResponseMessageNoMessage
import com.thomas.core.model.http.HTTPStatus.BAD_GATEWAY
import com.thomas.core.model.http.HTTPStatus.BAD_REQUEST
import com.thomas.core.model.http.HTTPStatus.INTERNAL_SERVER_ERROR
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class ExceptionExtensionTest {

    @Test
    fun `Throwable to ExceptionResponse`() {
        val exceptionResponse = Throwable("Simple exception").toExceptionResponse("/private/exception")
        assertEquals("Simple exception", exceptionResponse.message)
        assertEquals("/private/exception", exceptionResponse.path)
        assertEquals(INTERNAL_SERVER_ERROR, exceptionResponse.status)
        assertNull(exceptionResponse.detail)
    }

    @Test
    fun `HttpApplicationException to ExceptionResponse`() {
        val exceptionResponse = badRequest("HttpApplicationException BAD_REQUEST", mapOf("param" to "qwerty"))
            .toExceptionResponse("/private/http-application-exception", null)

        assertEquals("HttpApplicationException BAD_REQUEST", exceptionResponse.message)
        assertEquals("/private/http-application-exception", exceptionResponse.path)
        assertEquals(BAD_REQUEST, exceptionResponse.status)
        assertNotNull(exceptionResponse.detail)
        assertTrue(exceptionResponse.detail is Map<*, *>)
        assertEquals("qwerty", (exceptionResponse.detail as Map<*, *>)["param"])
    }

    @Test
    fun `Exception without message to ExceptionResponse with HttpServletRequest`() {
        val exceptionResponse = UnsupportedOperationException()
            .toExceptionResponse("/private/unsupported-operation-exception", null)

        assertEquals(coreExceptionResponseMessageNoMessage(), exceptionResponse.message)
        assertEquals("/private/unsupported-operation-exception", exceptionResponse.path)
        assertEquals(INTERNAL_SERVER_ERROR, exceptionResponse.status)
        assertNull(exceptionResponse.detail)
    }

    @Test
    fun `Exception without message to ExceptionResponse with HttpStatus`() {
        val exceptionResponse = UnsupportedOperationException()
            .toExceptionResponse("/private/unsupported-operation-exception", BAD_GATEWAY)

        assertEquals(coreExceptionResponseMessageNoMessage(), exceptionResponse.message)
        assertEquals("/private/unsupported-operation-exception", exceptionResponse.path)
        assertEquals(BAD_GATEWAY, exceptionResponse.status)
        assertNull(exceptionResponse.detail)
    }

}