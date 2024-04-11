package com.thomas.core.model.http

import com.thomas.core.model.http.HTTPStatus.ACCEPTED
import com.thomas.core.model.http.HTTPStatus.ALREADY_REPORTED
import com.thomas.core.model.http.HTTPStatus.BAD_GATEWAY
import com.thomas.core.model.http.HTTPStatus.BAD_REQUEST
import com.thomas.core.model.http.HTTPStatus.BANDWIDTH_LIMIT_EXCEEDED
import com.thomas.core.model.http.HTTPStatus.CHECKPOINT
import com.thomas.core.model.http.HTTPStatus.CONFLICT
import com.thomas.core.model.http.HTTPStatus.CONTINUE
import com.thomas.core.model.http.HTTPStatus.CREATED
import com.thomas.core.model.http.HTTPStatus.Companion.byCode
import com.thomas.core.model.http.HTTPStatus.DESTINATION_LOCKED
import com.thomas.core.model.http.HTTPStatus.EXPECTATION_FAILED
import com.thomas.core.model.http.HTTPStatus.FAILED_DEPENDENCY
import com.thomas.core.model.http.HTTPStatus.FORBIDDEN
import com.thomas.core.model.http.HTTPStatus.FOUND
import com.thomas.core.model.http.HTTPStatus.GATEWAY_TIMEOUT
import com.thomas.core.model.http.HTTPStatus.GONE
import com.thomas.core.model.http.HTTPStatus.HTTP_VERSION_NOT_SUPPORTED
import com.thomas.core.model.http.HTTPStatus.IM_USED
import com.thomas.core.model.http.HTTPStatus.INSUFFICIENT_SPACE_ON_RESOURCE
import com.thomas.core.model.http.HTTPStatus.INSUFFICIENT_STORAGE
import com.thomas.core.model.http.HTTPStatus.INTERNAL_SERVER_ERROR
import com.thomas.core.model.http.HTTPStatus.I_AM_A_TEAPOT
import com.thomas.core.model.http.HTTPStatus.LENGTH_REQUIRED
import com.thomas.core.model.http.HTTPStatus.LOCKED
import com.thomas.core.model.http.HTTPStatus.LOOP_DETECTED
import com.thomas.core.model.http.HTTPStatus.METHOD_FAILURE
import com.thomas.core.model.http.HTTPStatus.METHOD_NOT_ALLOWED
import com.thomas.core.model.http.HTTPStatus.MOVED_PERMANENTLY
import com.thomas.core.model.http.HTTPStatus.MULTIPLE_CHOICES
import com.thomas.core.model.http.HTTPStatus.MULTI_STATUS
import com.thomas.core.model.http.HTTPStatus.NETWORK_AUTHENTICATION_REQUIRED
import com.thomas.core.model.http.HTTPStatus.NON_AUTHORITATIVE_INFORMATION
import com.thomas.core.model.http.HTTPStatus.NOT_ACCEPTABLE
import com.thomas.core.model.http.HTTPStatus.NOT_EXTENDED
import com.thomas.core.model.http.HTTPStatus.NOT_FOUND
import com.thomas.core.model.http.HTTPStatus.NOT_IMPLEMENTED
import com.thomas.core.model.http.HTTPStatus.NOT_MODIFIED
import com.thomas.core.model.http.HTTPStatus.NO_CONTENT
import com.thomas.core.model.http.HTTPStatus.OK
import com.thomas.core.model.http.HTTPStatus.PARTIAL_CONTENT
import com.thomas.core.model.http.HTTPStatus.PAYLOAD_TOO_LARGE
import com.thomas.core.model.http.HTTPStatus.PAYMENT_REQUIRED
import com.thomas.core.model.http.HTTPStatus.PERMANENT_REDIRECT
import com.thomas.core.model.http.HTTPStatus.PRECONDITION_FAILED
import com.thomas.core.model.http.HTTPStatus.PRECONDITION_REQUIRED
import com.thomas.core.model.http.HTTPStatus.PROCESSING
import com.thomas.core.model.http.HTTPStatus.PROXY_AUTHENTICATION_REQUIRED
import com.thomas.core.model.http.HTTPStatus.REQUESTED_RANGE_NOT_SATISFIABLE
import com.thomas.core.model.http.HTTPStatus.REQUEST_HEADER_FIELDS_TOO_LARGE
import com.thomas.core.model.http.HTTPStatus.REQUEST_TIMEOUT
import com.thomas.core.model.http.HTTPStatus.RESET_CONTENT
import com.thomas.core.model.http.HTTPStatus.SEE_OTHER
import com.thomas.core.model.http.HTTPStatus.SERVICE_UNAVAILABLE
import com.thomas.core.model.http.HTTPStatus.SWITCHING_PROTOCOLS
import com.thomas.core.model.http.HTTPStatus.TEMPORARY_REDIRECT
import com.thomas.core.model.http.HTTPStatus.TOO_EARLY
import com.thomas.core.model.http.HTTPStatus.TOO_MANY_REQUESTS
import com.thomas.core.model.http.HTTPStatus.UNAUTHORIZED
import com.thomas.core.model.http.HTTPStatus.UNAVAILABLE_FOR_LEGAL_REASONS
import com.thomas.core.model.http.HTTPStatus.UNPROCESSABLE_ENTITY
import com.thomas.core.model.http.HTTPStatus.UNSUPPORTED_MEDIA_TYPE
import com.thomas.core.model.http.HTTPStatus.UPGRADE_REQUIRED
import com.thomas.core.model.http.HTTPStatus.URI_TOO_LONG
import com.thomas.core.model.http.HTTPStatus.USE_PROXY
import com.thomas.core.model.http.HTTPStatus.VARIANT_ALSO_NEGOTIATES
import com.thomas.core.model.http.HTTPStatusSeries.CLIENT_ERROR
import com.thomas.core.model.http.HTTPStatusSeries.INFORMATIONAL
import com.thomas.core.model.http.HTTPStatusSeries.REDIRECTION
import com.thomas.core.model.http.HTTPStatusSeries.SERVER_ERROR
import com.thomas.core.model.http.HTTPStatusSeries.SUCCESSFUL
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class HTTPStatusTest {

    @Test
    fun `Validate HTTP Status`() {
        assertEquals(100, CONTINUE.code)
        assertEquals(INFORMATIONAL, CONTINUE.series)
        assertEquals("Continue", CONTINUE.reason)

        assertEquals(101, SWITCHING_PROTOCOLS.code)
        assertEquals(INFORMATIONAL, SWITCHING_PROTOCOLS.series)
        assertEquals("Switching Protocols", SWITCHING_PROTOCOLS.reason)
        assertEquals(102, PROCESSING.code)
        assertEquals(INFORMATIONAL, PROCESSING.series)
        assertEquals("Processing", PROCESSING.reason)
        assertEquals(103, CHECKPOINT.code)
        assertEquals(INFORMATIONAL, CHECKPOINT.series)
        assertEquals("Checkpoint", CHECKPOINT.reason)

        assertEquals(200, OK.code)
        assertEquals(SUCCESSFUL, OK.series)
        assertEquals("OK", OK.reason)
        assertEquals(201, CREATED.code)
        assertEquals(SUCCESSFUL, CREATED.series)
        assertEquals("Created", CREATED.reason)
        assertEquals(202, ACCEPTED.code)
        assertEquals(SUCCESSFUL, ACCEPTED.series)
        assertEquals("Accepted", ACCEPTED.reason)
        assertEquals(203, NON_AUTHORITATIVE_INFORMATION.code)
        assertEquals(SUCCESSFUL, NON_AUTHORITATIVE_INFORMATION.series)
        assertEquals("Non-Authoritative Information", NON_AUTHORITATIVE_INFORMATION.reason)
        assertEquals(204, NO_CONTENT.code)
        assertEquals(SUCCESSFUL, NO_CONTENT.series)
        assertEquals("No Content", NO_CONTENT.reason)
        assertEquals(205, RESET_CONTENT.code)
        assertEquals(SUCCESSFUL, RESET_CONTENT.series)
        assertEquals("Reset Content", RESET_CONTENT.reason)
        assertEquals(206, PARTIAL_CONTENT.code)
        assertEquals(SUCCESSFUL, PARTIAL_CONTENT.series)
        assertEquals("Partial Content", PARTIAL_CONTENT.reason)
        assertEquals(207, MULTI_STATUS.code)
        assertEquals(SUCCESSFUL, MULTI_STATUS.series)
        assertEquals("Multi-Status", MULTI_STATUS.reason)
        assertEquals(208, ALREADY_REPORTED.code)
        assertEquals(SUCCESSFUL, ALREADY_REPORTED.series)
        assertEquals("Already Reported", ALREADY_REPORTED.reason)
        assertEquals(226, IM_USED.code)
        assertEquals(SUCCESSFUL, IM_USED.series)
        assertEquals("IM Used", IM_USED.reason)

        assertEquals(300, MULTIPLE_CHOICES.code)
        assertEquals(REDIRECTION, MULTIPLE_CHOICES.series)
        assertEquals("Multiple Choices", MULTIPLE_CHOICES.reason)
        assertEquals(301, MOVED_PERMANENTLY.code)
        assertEquals(REDIRECTION, MOVED_PERMANENTLY.series)
        assertEquals("Moved Permanently", MOVED_PERMANENTLY.reason)
        assertEquals(302, FOUND.code)
        assertEquals(REDIRECTION, FOUND.series)
        assertEquals("Found", FOUND.reason)
        assertEquals(303, SEE_OTHER.code)
        assertEquals(REDIRECTION, SEE_OTHER.series)
        assertEquals("See Other", SEE_OTHER.reason)
        assertEquals(304, NOT_MODIFIED.code)
        assertEquals(REDIRECTION, NOT_MODIFIED.series)
        assertEquals("Not Modified", NOT_MODIFIED.reason)
        assertEquals(305, USE_PROXY.code)
        assertEquals(REDIRECTION, USE_PROXY.series)
        assertEquals("Use Proxy", USE_PROXY.reason)
        assertEquals(307, TEMPORARY_REDIRECT.code)
        assertEquals(REDIRECTION, TEMPORARY_REDIRECT.series)
        assertEquals("Temporary Redirect", TEMPORARY_REDIRECT.reason)
        assertEquals(308, PERMANENT_REDIRECT.code)
        assertEquals(REDIRECTION, PERMANENT_REDIRECT.series)
        assertEquals("Permanent Redirect", PERMANENT_REDIRECT.reason)

        assertEquals(400, BAD_REQUEST.code)
        assertEquals(CLIENT_ERROR, BAD_REQUEST.series)
        assertEquals("Bad Request", BAD_REQUEST.reason)
        assertEquals(401, UNAUTHORIZED.code)
        assertEquals(CLIENT_ERROR, UNAUTHORIZED.series)
        assertEquals("Unauthorized", UNAUTHORIZED.reason)
        assertEquals(402, PAYMENT_REQUIRED.code)
        assertEquals(CLIENT_ERROR, PAYMENT_REQUIRED.series)
        assertEquals("Payment Required", PAYMENT_REQUIRED.reason)
        assertEquals(403, FORBIDDEN.code)
        assertEquals(CLIENT_ERROR, FORBIDDEN.series)
        assertEquals("Forbidden", FORBIDDEN.reason)
        assertEquals(404, NOT_FOUND.code)
        assertEquals(CLIENT_ERROR, NOT_FOUND.series)
        assertEquals("Not Found", NOT_FOUND.reason)
        assertEquals(405, METHOD_NOT_ALLOWED.code)
        assertEquals(CLIENT_ERROR, METHOD_NOT_ALLOWED.series)
        assertEquals("Method Not Allowed", METHOD_NOT_ALLOWED.reason)
        assertEquals(406, NOT_ACCEPTABLE.code)
        assertEquals(CLIENT_ERROR, NOT_ACCEPTABLE.series)
        assertEquals("Not Acceptable", NOT_ACCEPTABLE.reason)
        assertEquals(407, PROXY_AUTHENTICATION_REQUIRED.code)
        assertEquals(CLIENT_ERROR, PROXY_AUTHENTICATION_REQUIRED.series)
        assertEquals("Proxy Authentication Required", PROXY_AUTHENTICATION_REQUIRED.reason)
        assertEquals(408, REQUEST_TIMEOUT.code)
        assertEquals(CLIENT_ERROR, REQUEST_TIMEOUT.series)
        assertEquals("Request Timeout", REQUEST_TIMEOUT.reason)
        assertEquals(409, CONFLICT.code)
        assertEquals(CLIENT_ERROR, CONFLICT.series)
        assertEquals("Conflict", CONFLICT.reason)
        assertEquals(410, GONE.code)
        assertEquals(CLIENT_ERROR, GONE.series)
        assertEquals("Gone", GONE.reason)
        assertEquals(411, LENGTH_REQUIRED.code)
        assertEquals(CLIENT_ERROR, LENGTH_REQUIRED.series)
        assertEquals("Length Required", LENGTH_REQUIRED.reason)
        assertEquals(412, PRECONDITION_FAILED.code)
        assertEquals(CLIENT_ERROR, PRECONDITION_FAILED.series)
        assertEquals("Precondition Failed", PRECONDITION_FAILED.reason)
        assertEquals(413, PAYLOAD_TOO_LARGE.code)
        assertEquals(CLIENT_ERROR, PAYLOAD_TOO_LARGE.series)
        assertEquals("Payload Too Large", PAYLOAD_TOO_LARGE.reason)
        assertEquals(414, URI_TOO_LONG.code)
        assertEquals(CLIENT_ERROR, URI_TOO_LONG.series)
        assertEquals("URI Too Long", URI_TOO_LONG.reason)
        assertEquals(415, UNSUPPORTED_MEDIA_TYPE.code)
        assertEquals(CLIENT_ERROR, UNSUPPORTED_MEDIA_TYPE.series)
        assertEquals("Unsupported Media Type", UNSUPPORTED_MEDIA_TYPE.reason)
        assertEquals(416, REQUESTED_RANGE_NOT_SATISFIABLE.code)
        assertEquals(CLIENT_ERROR, REQUESTED_RANGE_NOT_SATISFIABLE.series)
        assertEquals("Requested range not satisfiable", REQUESTED_RANGE_NOT_SATISFIABLE.reason)
        assertEquals(417, EXPECTATION_FAILED.code)
        assertEquals(CLIENT_ERROR, EXPECTATION_FAILED.series)
        assertEquals("Expectation Failed", EXPECTATION_FAILED.reason)
        assertEquals(418, I_AM_A_TEAPOT.code)
        assertEquals(CLIENT_ERROR, I_AM_A_TEAPOT.series)
        assertEquals("I'm a teapot", I_AM_A_TEAPOT.reason)
        assertEquals(419, INSUFFICIENT_SPACE_ON_RESOURCE.code)
        assertEquals(CLIENT_ERROR, INSUFFICIENT_SPACE_ON_RESOURCE.series)
        assertEquals("Insufficient Space On Resource", INSUFFICIENT_SPACE_ON_RESOURCE.reason)
        assertEquals(420, METHOD_FAILURE.code)
        assertEquals(CLIENT_ERROR, METHOD_FAILURE.series)
        assertEquals("Method Failure", METHOD_FAILURE.reason)
        assertEquals(421, DESTINATION_LOCKED.code)
        assertEquals(CLIENT_ERROR, DESTINATION_LOCKED.series)
        assertEquals("Destination Locked", DESTINATION_LOCKED.reason)
        assertEquals(422, UNPROCESSABLE_ENTITY.code)
        assertEquals(CLIENT_ERROR, UNPROCESSABLE_ENTITY.series)
        assertEquals("Unprocessable Entity", UNPROCESSABLE_ENTITY.reason)
        assertEquals(423, LOCKED.code)
        assertEquals(CLIENT_ERROR, LOCKED.series)
        assertEquals("Locked", LOCKED.reason)
        assertEquals(424, FAILED_DEPENDENCY.code)
        assertEquals(CLIENT_ERROR, FAILED_DEPENDENCY.series)
        assertEquals("Failed Dependency", FAILED_DEPENDENCY.reason)
        assertEquals(425, TOO_EARLY.code)
        assertEquals(CLIENT_ERROR, TOO_EARLY.series)
        assertEquals("Too Early", TOO_EARLY.reason)
        assertEquals(426, UPGRADE_REQUIRED.code)
        assertEquals(CLIENT_ERROR, UPGRADE_REQUIRED.series)
        assertEquals("Upgrade Required", UPGRADE_REQUIRED.reason)
        assertEquals(428, PRECONDITION_REQUIRED.code)
        assertEquals(CLIENT_ERROR, PRECONDITION_REQUIRED.series)
        assertEquals("Precondition Required", PRECONDITION_REQUIRED.reason)
        assertEquals(429, TOO_MANY_REQUESTS.code)
        assertEquals(CLIENT_ERROR, TOO_MANY_REQUESTS.series)
        assertEquals("Too Many Requests", TOO_MANY_REQUESTS.reason)
        assertEquals(431, REQUEST_HEADER_FIELDS_TOO_LARGE.code)
        assertEquals(CLIENT_ERROR, REQUEST_HEADER_FIELDS_TOO_LARGE.series)
        assertEquals("Request Header Fields Too Large", REQUEST_HEADER_FIELDS_TOO_LARGE.reason)
        assertEquals(451, UNAVAILABLE_FOR_LEGAL_REASONS.code)
        assertEquals(CLIENT_ERROR, UNAVAILABLE_FOR_LEGAL_REASONS.series)
        assertEquals("Unavailable For Legal Reasons", UNAVAILABLE_FOR_LEGAL_REASONS.reason)

        assertEquals(500, INTERNAL_SERVER_ERROR.code)
        assertEquals(SERVER_ERROR, INTERNAL_SERVER_ERROR.series)
        assertEquals("Internal Server Error", INTERNAL_SERVER_ERROR.reason)
        assertEquals(501, NOT_IMPLEMENTED.code)
        assertEquals(SERVER_ERROR, NOT_IMPLEMENTED.series)
        assertEquals("Not Implemented", NOT_IMPLEMENTED.reason)
        assertEquals(502, BAD_GATEWAY.code)
        assertEquals(SERVER_ERROR, BAD_GATEWAY.series)
        assertEquals("Bad Gateway", BAD_GATEWAY.reason)
        assertEquals(503, SERVICE_UNAVAILABLE.code)
        assertEquals(SERVER_ERROR, SERVICE_UNAVAILABLE.series)
        assertEquals("Service Unavailable", SERVICE_UNAVAILABLE.reason)
        assertEquals(504, GATEWAY_TIMEOUT.code)
        assertEquals(SERVER_ERROR, GATEWAY_TIMEOUT.series)
        assertEquals("Gateway Timeout", GATEWAY_TIMEOUT.reason)
        assertEquals(505, HTTP_VERSION_NOT_SUPPORTED.code)
        assertEquals(SERVER_ERROR, HTTP_VERSION_NOT_SUPPORTED.series)
        assertEquals("HTTP Version not supported", HTTP_VERSION_NOT_SUPPORTED.reason)
        assertEquals(506, VARIANT_ALSO_NEGOTIATES.code)
        assertEquals(SERVER_ERROR, VARIANT_ALSO_NEGOTIATES.series)
        assertEquals("Variant Also Negotiates", VARIANT_ALSO_NEGOTIATES.reason)
        assertEquals(507, INSUFFICIENT_STORAGE.code)
        assertEquals(SERVER_ERROR, INSUFFICIENT_STORAGE.series)
        assertEquals("Insufficient Storage", INSUFFICIENT_STORAGE.reason)
        assertEquals(508, LOOP_DETECTED.code)
        assertEquals(SERVER_ERROR, LOOP_DETECTED.series)
        assertEquals("Loop Detected", LOOP_DETECTED.reason)
        assertEquals(509, BANDWIDTH_LIMIT_EXCEEDED.code)
        assertEquals(SERVER_ERROR, BANDWIDTH_LIMIT_EXCEEDED.series)
        assertEquals("Bandwidth Limit Exceeded", BANDWIDTH_LIMIT_EXCEEDED.reason)
        assertEquals(510, NOT_EXTENDED.code)
        assertEquals(SERVER_ERROR, NOT_EXTENDED.series)
        assertEquals("Not Extended", NOT_EXTENDED.reason)
        assertEquals(511, NETWORK_AUTHENTICATION_REQUIRED.code)
        assertEquals(SERVER_ERROR, NETWORK_AUTHENTICATION_REQUIRED.series)
        assertEquals("Network Authentication Required", NETWORK_AUTHENTICATION_REQUIRED.reason)
    }

    @Test
    fun `Validate HTTP Status by Code`() {
        assertEquals(byCode(100), CONTINUE)
        assertEquals(byCode(101), SWITCHING_PROTOCOLS)
        assertEquals(byCode(102), PROCESSING)
        assertEquals(byCode(103), CHECKPOINT)

        assertEquals(byCode(200), OK)
        assertEquals(byCode(201), CREATED)
        assertEquals(byCode(202), ACCEPTED)
        assertEquals(byCode(203), NON_AUTHORITATIVE_INFORMATION)
        assertEquals(byCode(204), NO_CONTENT)
        assertEquals(byCode(205), RESET_CONTENT)
        assertEquals(byCode(206), PARTIAL_CONTENT)
        assertEquals(byCode(207), MULTI_STATUS)
        assertEquals(byCode(208), ALREADY_REPORTED)
        assertEquals(byCode(226), IM_USED)

        assertEquals(byCode(300), MULTIPLE_CHOICES)
        assertEquals(byCode(301), MOVED_PERMANENTLY)
        assertEquals(byCode(302), FOUND)
        assertEquals(byCode(303), SEE_OTHER)
        assertEquals(byCode(304), NOT_MODIFIED)
        assertEquals(byCode(305), USE_PROXY)
        assertEquals(byCode(307), TEMPORARY_REDIRECT)
        assertEquals(byCode(308), PERMANENT_REDIRECT)

        assertEquals(byCode(400), BAD_REQUEST)
        assertEquals(byCode(401), UNAUTHORIZED)
        assertEquals(byCode(402), PAYMENT_REQUIRED)
        assertEquals(byCode(403), FORBIDDEN)
        assertEquals(byCode(404), NOT_FOUND)
        assertEquals(byCode(405), METHOD_NOT_ALLOWED)
        assertEquals(byCode(406), NOT_ACCEPTABLE)
        assertEquals(byCode(407), PROXY_AUTHENTICATION_REQUIRED)
        assertEquals(byCode(408), REQUEST_TIMEOUT)
        assertEquals(byCode(409), CONFLICT)
        assertEquals(byCode(410), GONE)
        assertEquals(byCode(411), LENGTH_REQUIRED)
        assertEquals(byCode(412), PRECONDITION_FAILED)
        assertEquals(byCode(413), PAYLOAD_TOO_LARGE)
        assertEquals(byCode(414), URI_TOO_LONG)
        assertEquals(byCode(415), UNSUPPORTED_MEDIA_TYPE)
        assertEquals(byCode(416), REQUESTED_RANGE_NOT_SATISFIABLE)
        assertEquals(byCode(417), EXPECTATION_FAILED)
        assertEquals(byCode(418), I_AM_A_TEAPOT)
        assertEquals(byCode(419), INSUFFICIENT_SPACE_ON_RESOURCE)
        assertEquals(byCode(420), METHOD_FAILURE)
        assertEquals(byCode(421), DESTINATION_LOCKED)
        assertEquals(byCode(422), UNPROCESSABLE_ENTITY)
        assertEquals(byCode(423), LOCKED)
        assertEquals(byCode(424), FAILED_DEPENDENCY)
        assertEquals(byCode(425), TOO_EARLY)
        assertEquals(byCode(426), UPGRADE_REQUIRED)
        assertEquals(byCode(428), PRECONDITION_REQUIRED)
        assertEquals(byCode(429), TOO_MANY_REQUESTS)
        assertEquals(byCode(431), REQUEST_HEADER_FIELDS_TOO_LARGE)
        assertEquals(byCode(451), UNAVAILABLE_FOR_LEGAL_REASONS)

        assertEquals(byCode(500), INTERNAL_SERVER_ERROR)
        assertEquals(byCode(501), NOT_IMPLEMENTED)
        assertEquals(byCode(502), BAD_GATEWAY)
        assertEquals(byCode(503), SERVICE_UNAVAILABLE)
        assertEquals(byCode(504), GATEWAY_TIMEOUT)
        assertEquals(byCode(505), HTTP_VERSION_NOT_SUPPORTED)
        assertEquals(byCode(506), VARIANT_ALSO_NEGOTIATES)
        assertEquals(byCode(507), INSUFFICIENT_STORAGE)
        assertEquals(byCode(508), LOOP_DETECTED)
        assertEquals(byCode(509), BANDWIDTH_LIMIT_EXCEEDED)
        assertEquals(byCode(510), NOT_EXTENDED)
        assertEquals(byCode(511), NETWORK_AUTHENTICATION_REQUIRED)
    }

    @Test
    fun `Validate HTTP Status by code with non existent`() {
        assertEquals(INTERNAL_SERVER_ERROR, byCode(700))
    }

}