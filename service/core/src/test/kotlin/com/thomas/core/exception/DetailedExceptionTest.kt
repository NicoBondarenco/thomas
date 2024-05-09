package com.thomas.core.exception

import com.thomas.core.exception.ErrorType.APPLICATION_ERROR
import com.thomas.core.exception.ErrorType.INVALID_ENTITY
import com.thomas.core.exception.ErrorType.INVALID_PARAMETER
import com.thomas.core.exception.ErrorType.NOT_FOUND
import com.thomas.core.exception.ErrorType.UNAUTHENTICATED_USER
import com.thomas.core.exception.ErrorType.UNAUTHORIZED_ACTION
import com.thomas.core.i18n.CoreMessageI18N.exceptionDetailedExceptionMessageDefault
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class DetailedExceptionTest {

    companion object {
        @JvmStatic
        fun errorParameters(): List<Arguments> = listOf(
            Arguments.of("Message of UNAUTHENTICATED_USER", UNAUTHENTICATED_USER, IllegalArgumentException(), "Detail Message"),
            Arguments.of("Message of UNAUTHORIZED_ACTION", UNAUTHORIZED_ACTION, NullPointerException(), mapOf("Message" to "Detail Message")),
            Arguments.of("Message of INVALID_ENTITY", INVALID_ENTITY, ClassNotFoundException(), listOf("Detail Message")),
            Arguments.of("Message of INVALID_PARAMETER", INVALID_PARAMETER, NoSuchMethodException(), INVALID_PARAMETER),
            Arguments.of("Message of NOT_FOUND", NOT_FOUND, ArithmeticException(), ErrorDetail()),
            Arguments.of("Message of APPLICATION_ERROR", APPLICATION_ERROR, null, null),
        )
    }

    @Test
    fun `When DetailException is thrown without parameters then the defaults should be used`() {
        val exception = assertThrows<DetailedException> {
            throw object : DetailedException() {}
        }

        assertEquals(exceptionDetailedExceptionMessageDefault(), exception.message)
        assertEquals(APPLICATION_ERROR, exception.type)
        assertNull(exception.detail)
        assertNull(exception.cause)
    }

    @ParameterizedTest
    @MethodSource("errorParameters")
    fun `When DetailException is thrown with parameters then the values should be used`(
        message: String,
        type: ErrorType,
        cause: Throwable?,
        detail: Any?,
    ) {
        val exception = assertThrows<DetailedException> {
            throw object : DetailedException(message, type, detail, cause) {}
        }

        assertEquals(message, exception.message)
        assertEquals(type, exception.type)
        assertEquals(detail, exception.detail)
        assertEquals(cause, exception.cause)
    }


    private data class ErrorDetail(
        val message: String = "Error detail",
    )

}