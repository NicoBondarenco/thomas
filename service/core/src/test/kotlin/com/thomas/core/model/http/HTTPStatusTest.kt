package com.thomas.core.model.http

import com.thomas.core.model.http.HTTPStatus.ACCEPTED
import com.thomas.core.model.http.HTTPStatus.CONTINUE
import com.thomas.core.model.http.HTTPStatus.CREATED
import com.thomas.core.model.http.HTTPStatus.Companion.byCode
import com.thomas.core.model.http.HTTPStatus.INTERNAL_SERVER_ERROR
import com.thomas.core.model.http.HTTPStatus.MOVED_PERMANENTLY
import com.thomas.core.model.http.HTTPStatus.MULTIPLE_CHOICES
import com.thomas.core.model.http.HTTPStatus.NOT_IMPLEMENTED
import com.thomas.core.model.http.HTTPStatus.OK
import com.thomas.core.model.http.HTTPStatus.SWITCHING_PROTOCOLS
import com.thomas.core.model.http.HTTPStatus.UNAVAILABLE_FOR_LEGAL_REASONS
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class HTTPStatusTest {

    @Test
    fun `Validate HTTP Status by Code`() {
        assertEquals(byCode(100), CONTINUE)
        assertEquals(byCode(101), SWITCHING_PROTOCOLS)

        assertEquals(byCode(200), OK)
        assertEquals(byCode(201), CREATED)
        assertEquals(byCode(202), ACCEPTED)

        assertEquals(byCode(300), MULTIPLE_CHOICES)
        assertEquals(byCode(301), MOVED_PERMANENTLY)

        assertEquals(byCode(451), UNAVAILABLE_FOR_LEGAL_REASONS)

        assertEquals(byCode(500), INTERNAL_SERVER_ERROR)
        assertEquals(byCode(501), NOT_IMPLEMENTED)
    }

    @Test
    fun `Validate HTTP Status by code with non existent`() {
        assertEquals(INTERNAL_SERVER_ERROR, byCode(700))
    }

}
