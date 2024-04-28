package com.thomas.core.extension

import com.thomas.core.i18n.CoreMessageI18N.exceptionExceptionResponseErrorMessageDefaultMessage
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

internal class ExceptionExtensionTest {

    @Test
    fun `Throwable to ExceptionResponse`() {
        val exceptionResponse = Throwable("Simple exception").toExceptionResponse("/private/exception")
        assertEquals("Simple exception", exceptionResponse.message)
        assertEquals("/private/exception", exceptionResponse.path)
        assertEquals("SERVER_ERROR", exceptionResponse.status)
        assertNull(exceptionResponse.detail)
    }

    @Test
    fun `Exception without message to ExceptionResponse with HttpServletRequest`() {
        val exceptionResponse = UnsupportedOperationException()
            .toExceptionResponse(
                uri = "/private/unsupported-operation-exception",
                status = "unsupported-exception",
                code = 555,
                details = "UnsupportedOperationException"
            )

        assertEquals(exceptionExceptionResponseErrorMessageDefaultMessage(), exceptionResponse.message)
        assertEquals("/private/unsupported-operation-exception", exceptionResponse.path)
        assertEquals("unsupported-exception", exceptionResponse.status)
        assertEquals(555, exceptionResponse.code)
        assertEquals("UnsupportedOperationException", exceptionResponse.detail)
    }

}
