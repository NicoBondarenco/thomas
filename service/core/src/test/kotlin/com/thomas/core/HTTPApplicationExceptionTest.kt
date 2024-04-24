package com.thomas.core

import com.thomas.core.model.http.HTTPStatus
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

class HTTPApplicationExceptionTest {

    @ParameterizedTest
    @EnumSource(HTTPStatus::class)
    fun `HTTPApplicationException Defaults`(status: HTTPStatus) {
        HttpApplicationException(status, "Error Message").apply {
            assertEquals(null, this.detail)
            assertNull(this.cause)
        }
    }

    @ParameterizedTest
    @EnumSource(HTTPStatus::class)
    fun `HTTPApplicationException Parameters`(status: HTTPStatus) {
        HttpApplicationException(status, "Error Message", "Error Detail", Exception("Error Exception")).apply {
            assertEquals("Error Detail", this.detail)
            assertNotNull(this.cause)
            assertEquals("Error Exception", this.cause!!.message)
        }
    }

}
