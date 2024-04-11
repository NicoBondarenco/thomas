package com.thomas.core

import com.thomas.core.HttpApplicationException.Companion.badGateway
import com.thomas.core.HttpApplicationException.Companion.badRequest
import com.thomas.core.HttpApplicationException.Companion.bandwidthLimitExceeded
import com.thomas.core.HttpApplicationException.Companion.conflict
import com.thomas.core.HttpApplicationException.Companion.expectationFailed
import com.thomas.core.HttpApplicationException.Companion.failedDependency
import com.thomas.core.HttpApplicationException.Companion.forbidden
import com.thomas.core.HttpApplicationException.Companion.gatewayTimeout
import com.thomas.core.HttpApplicationException.Companion.gone
import com.thomas.core.HttpApplicationException.Companion.httpVersionNotSupported
import com.thomas.core.HttpApplicationException.Companion.iAmATeapot
import com.thomas.core.HttpApplicationException.Companion.insufficientStorage
import com.thomas.core.HttpApplicationException.Companion.internalServerError
import com.thomas.core.HttpApplicationException.Companion.lengthRequired
import com.thomas.core.HttpApplicationException.Companion.locked
import com.thomas.core.HttpApplicationException.Companion.loopDetected
import com.thomas.core.HttpApplicationException.Companion.methodNotAllowed
import com.thomas.core.HttpApplicationException.Companion.networkAuthenticationRequired
import com.thomas.core.HttpApplicationException.Companion.notAcceptable
import com.thomas.core.HttpApplicationException.Companion.notExtended
import com.thomas.core.HttpApplicationException.Companion.notFound
import com.thomas.core.HttpApplicationException.Companion.notImplemented
import com.thomas.core.HttpApplicationException.Companion.payloadTooLarge
import com.thomas.core.HttpApplicationException.Companion.paymentRequired
import com.thomas.core.HttpApplicationException.Companion.preconditionFailed
import com.thomas.core.HttpApplicationException.Companion.preconditionRequired
import com.thomas.core.HttpApplicationException.Companion.proxyAuthenticationRequired
import com.thomas.core.HttpApplicationException.Companion.requestHeaderFieldsTooLarge
import com.thomas.core.HttpApplicationException.Companion.requestTimeout
import com.thomas.core.HttpApplicationException.Companion.requestedRangeNotSatisfiable
import com.thomas.core.HttpApplicationException.Companion.serviceUnavailable
import com.thomas.core.HttpApplicationException.Companion.tooEarly
import com.thomas.core.HttpApplicationException.Companion.tooManyRequests
import com.thomas.core.HttpApplicationException.Companion.unauthorized
import com.thomas.core.HttpApplicationException.Companion.unavailableForLegalReasons
import com.thomas.core.HttpApplicationException.Companion.unprocessableEntity
import com.thomas.core.HttpApplicationException.Companion.unsupportedMediaType
import com.thomas.core.HttpApplicationException.Companion.upgradeRequired
import com.thomas.core.HttpApplicationException.Companion.uriTooLong
import com.thomas.core.HttpApplicationException.Companion.variantAlsoNegotiates
import com.thomas.core.model.http.HTTPStatus.BAD_GATEWAY
import com.thomas.core.model.http.HTTPStatus.BAD_REQUEST
import com.thomas.core.model.http.HTTPStatus.BANDWIDTH_LIMIT_EXCEEDED
import com.thomas.core.model.http.HTTPStatus.CONFLICT
import com.thomas.core.model.http.HTTPStatus.EXPECTATION_FAILED
import com.thomas.core.model.http.HTTPStatus.FAILED_DEPENDENCY
import com.thomas.core.model.http.HTTPStatus.FORBIDDEN
import com.thomas.core.model.http.HTTPStatus.GATEWAY_TIMEOUT
import com.thomas.core.model.http.HTTPStatus.GONE
import com.thomas.core.model.http.HTTPStatus.HTTP_VERSION_NOT_SUPPORTED
import com.thomas.core.model.http.HTTPStatus.INSUFFICIENT_STORAGE
import com.thomas.core.model.http.HTTPStatus.INTERNAL_SERVER_ERROR
import com.thomas.core.model.http.HTTPStatus.I_AM_A_TEAPOT
import com.thomas.core.model.http.HTTPStatus.LENGTH_REQUIRED
import com.thomas.core.model.http.HTTPStatus.LOCKED
import com.thomas.core.model.http.HTTPStatus.LOOP_DETECTED
import com.thomas.core.model.http.HTTPStatus.METHOD_NOT_ALLOWED
import com.thomas.core.model.http.HTTPStatus.NETWORK_AUTHENTICATION_REQUIRED
import com.thomas.core.model.http.HTTPStatus.NOT_ACCEPTABLE
import com.thomas.core.model.http.HTTPStatus.NOT_EXTENDED
import com.thomas.core.model.http.HTTPStatus.NOT_FOUND
import com.thomas.core.model.http.HTTPStatus.NOT_IMPLEMENTED
import com.thomas.core.model.http.HTTPStatus.PAYLOAD_TOO_LARGE
import com.thomas.core.model.http.HTTPStatus.PAYMENT_REQUIRED
import com.thomas.core.model.http.HTTPStatus.PRECONDITION_FAILED
import com.thomas.core.model.http.HTTPStatus.PRECONDITION_REQUIRED
import com.thomas.core.model.http.HTTPStatus.PROXY_AUTHENTICATION_REQUIRED
import com.thomas.core.model.http.HTTPStatus.REQUESTED_RANGE_NOT_SATISFIABLE
import com.thomas.core.model.http.HTTPStatus.REQUEST_HEADER_FIELDS_TOO_LARGE
import com.thomas.core.model.http.HTTPStatus.REQUEST_TIMEOUT
import com.thomas.core.model.http.HTTPStatus.SERVICE_UNAVAILABLE
import com.thomas.core.model.http.HTTPStatus.TOO_EARLY
import com.thomas.core.model.http.HTTPStatus.TOO_MANY_REQUESTS
import com.thomas.core.model.http.HTTPStatus.UNAUTHORIZED
import com.thomas.core.model.http.HTTPStatus.UNAVAILABLE_FOR_LEGAL_REASONS
import com.thomas.core.model.http.HTTPStatus.UNPROCESSABLE_ENTITY
import com.thomas.core.model.http.HTTPStatus.UNSUPPORTED_MEDIA_TYPE
import com.thomas.core.model.http.HTTPStatus.UPGRADE_REQUIRED
import com.thomas.core.model.http.HTTPStatus.URI_TOO_LONG
import com.thomas.core.model.http.HTTPStatus.VARIANT_ALSO_NEGOTIATES
import kotlin.test.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull

class HTTPApplicationExceptionTest {

    @Test
    fun `HTTPApplicationException Defaults`(){
        HttpApplicationException(NOT_FOUND, "Error Message").apply {
            assertEquals("No details available", this.detail)
            assertNull(this.cause)
        }
    }

    @Test
    fun `HTTPApplicationException Parameters`(){
        HttpApplicationException(NOT_FOUND, "Error Message", "Error Detail", Exception("Error Exception")).apply {
            assertEquals("Error Detail", this.detail)
            assertNotNull(this.cause)
            assertEquals("Error Exception", this.cause!!.message)
        }
    }

    @Test
    fun `Validate companion methods default`() {

        badRequest("Error message").apply {
            assertEquals(BAD_REQUEST, this.status)
            assertNull(this.detail)
            assertNull(this.cause)
        }

        unauthorized("Error message").apply {
            assertEquals(UNAUTHORIZED, this.status)
            assertNull(this.detail)
            assertNull(this.cause)
        }

        paymentRequired("Error message").apply {
            assertEquals(PAYMENT_REQUIRED, this.status)
            assertNull(this.detail)
            assertNull(this.cause)
        }

        forbidden("Error message").apply {
            assertEquals(FORBIDDEN, this.status)
            assertNull(this.detail)
            assertNull(this.cause)
        }

        notFound("Error message").apply {
            assertEquals(NOT_FOUND, this.status)
            assertNull(this.detail)
            assertNull(this.cause)
        }

        methodNotAllowed("Error message").apply {
            assertEquals(METHOD_NOT_ALLOWED, this.status)
            assertNull(this.detail)
            assertNull(this.cause)
        }

        notAcceptable("Error message").apply {
            assertEquals(NOT_ACCEPTABLE, this.status)
            assertNull(this.detail)
            assertNull(this.cause)
        }

        proxyAuthenticationRequired("Error message").apply {
            assertEquals(PROXY_AUTHENTICATION_REQUIRED, this.status)
            assertNull(this.detail)
            assertNull(this.cause)
        }

        requestTimeout("Error message").apply {
            assertEquals(REQUEST_TIMEOUT, this.status)
            assertNull(this.detail)
            assertNull(this.cause)
        }

        conflict("Error message").apply {
            assertEquals(CONFLICT, this.status)
            assertNull(this.detail)
            assertNull(this.cause)
        }

        gone("Error message").apply {
            assertEquals(GONE, this.status)
            assertNull(this.detail)
            assertNull(this.cause)
        }

        lengthRequired("Error message").apply {
            assertEquals(LENGTH_REQUIRED, this.status)
            assertNull(this.detail)
            assertNull(this.cause)
        }

        preconditionFailed("Error message").apply {
            assertEquals(PRECONDITION_FAILED, this.status)
            assertNull(this.detail)
            assertNull(this.cause)
        }

        payloadTooLarge("Error message").apply {
            assertEquals(PAYLOAD_TOO_LARGE, this.status)
            assertNull(this.detail)
            assertNull(this.cause)
        }

        uriTooLong("Error message").apply {
            assertEquals(URI_TOO_LONG, this.status)
            assertNull(this.detail)
            assertNull(this.cause)
        }

        unsupportedMediaType("Error message").apply {
            assertEquals(UNSUPPORTED_MEDIA_TYPE, this.status)
            assertNull(this.detail)
            assertNull(this.cause)
        }

        requestedRangeNotSatisfiable("Error message").apply {
            assertEquals(REQUESTED_RANGE_NOT_SATISFIABLE, this.status)
            assertNull(this.detail)
            assertNull(this.cause)
        }

        expectationFailed("Error message").apply {
            assertEquals(EXPECTATION_FAILED, this.status)
            assertNull(this.detail)
            assertNull(this.cause)
        }

        iAmATeapot("Error message").apply {
            assertEquals(I_AM_A_TEAPOT, this.status)
            assertNull(this.detail)
            assertNull(this.cause)
        }

        unprocessableEntity("Error message").apply {
            assertEquals(UNPROCESSABLE_ENTITY, this.status)
            assertNull(this.detail)
            assertNull(this.cause)
        }

        locked("Error message").apply {
            assertEquals(LOCKED, this.status)
            assertNull(this.detail)
            assertNull(this.cause)
        }

        failedDependency("Error message").apply {
            assertEquals(FAILED_DEPENDENCY, this.status)
            assertNull(this.detail)
            assertNull(this.cause)
        }

        tooEarly("Error message").apply {
            assertEquals(TOO_EARLY, this.status)
            assertNull(this.detail)
            assertNull(this.cause)
        }

        upgradeRequired("Error message").apply {
            assertEquals(UPGRADE_REQUIRED, this.status)
            assertNull(this.detail)
            assertNull(this.cause)
        }

        preconditionRequired("Error message").apply {
            assertEquals(PRECONDITION_REQUIRED, this.status)
            assertNull(this.detail)
            assertNull(this.cause)
        }

        tooManyRequests("Error message").apply {
            assertEquals(TOO_MANY_REQUESTS, this.status)
            assertNull(this.detail)
            assertNull(this.cause)
        }

        requestHeaderFieldsTooLarge("Error message").apply {
            assertEquals(REQUEST_HEADER_FIELDS_TOO_LARGE, this.status)
            assertNull(this.detail)
            assertNull(this.cause)
        }

        unavailableForLegalReasons("Error message").apply {
            assertEquals(UNAVAILABLE_FOR_LEGAL_REASONS, this.status)
            assertNull(this.detail)
            assertNull(this.cause)
        }

        internalServerError("Error message").apply {
            assertEquals(INTERNAL_SERVER_ERROR, this.status)
            assertNull(this.detail)
            assertNull(this.cause)
        }

        notImplemented("Error message").apply {
            assertEquals(NOT_IMPLEMENTED, this.status)
            assertNull(this.detail)
            assertNull(this.cause)
        }

        badGateway("Error message").apply {
            assertEquals(BAD_GATEWAY, this.status)
            assertNull(this.detail)
            assertNull(this.cause)
        }

        serviceUnavailable("Error message").apply {
            assertEquals(SERVICE_UNAVAILABLE, this.status)
            assertNull(this.detail)
            assertNull(this.cause)
        }

        gatewayTimeout("Error message").apply {
            assertEquals(GATEWAY_TIMEOUT, this.status)
            assertNull(this.detail)
            assertNull(this.cause)
        }

        httpVersionNotSupported("Error message").apply {
            assertEquals(HTTP_VERSION_NOT_SUPPORTED, this.status)
            assertNull(this.detail)
            assertNull(this.cause)
        }

        variantAlsoNegotiates("Error message").apply {
            assertEquals(VARIANT_ALSO_NEGOTIATES, this.status)
            assertNull(this.detail)
            assertNull(this.cause)
        }

        insufficientStorage("Error message").apply {
            assertEquals(INSUFFICIENT_STORAGE, this.status)
            assertNull(this.detail)
            assertNull(this.cause)
        }

        loopDetected("Error message").apply {
            assertEquals(LOOP_DETECTED, this.status)
            assertNull(this.detail)
            assertNull(this.cause)
        }

        bandwidthLimitExceeded("Error message").apply {
            assertEquals(BANDWIDTH_LIMIT_EXCEEDED, this.status)
            assertNull(this.detail)
            assertNull(this.cause)
        }

        notExtended("Error message").apply {
            assertEquals(NOT_EXTENDED, this.status)
            assertNull(this.detail)
            assertNull(this.cause)
        }

        networkAuthenticationRequired("Error message").apply {
            assertEquals(NETWORK_AUTHENTICATION_REQUIRED, this.status)
            assertNull(this.detail)
            assertNull(this.cause)
        }

    }

    @Test
    fun `Validate companion methods parameters`() {

        badRequest("Error message", "Error Detail", Exception("Error Exception")).apply {
            assertEquals(BAD_REQUEST, this.status)
            assertEquals("Error Detail", this.detail)
            assertNotNull(this.cause)
            assertEquals("Error Exception", this.cause!!.message)
        }

        unauthorized("Error message", "Error Detail", Exception("Error Exception")).apply {
            assertEquals(UNAUTHORIZED, this.status)
            assertEquals("Error Detail", this.detail)
            assertNotNull(this.cause)
            assertEquals("Error Exception", this.cause!!.message)
        }

        paymentRequired("Error message", "Error Detail", Exception("Error Exception")).apply {
            assertEquals(PAYMENT_REQUIRED, this.status)
            assertEquals("Error Detail", this.detail)
            assertNotNull(this.cause)
            assertEquals("Error Exception", this.cause!!.message)
        }

        forbidden("Error message", "Error Detail", Exception("Error Exception")).apply {
            assertEquals(FORBIDDEN, this.status)
            assertEquals("Error Detail", this.detail)
            assertNotNull(this.cause)
            assertEquals("Error Exception", this.cause!!.message)
        }

        notFound("Error message", "Error Detail", Exception("Error Exception")).apply {
            assertEquals(NOT_FOUND, this.status)
            assertEquals("Error Detail", this.detail)
            assertNotNull(this.cause)
            assertEquals("Error Exception", this.cause!!.message)
        }

        methodNotAllowed("Error message", "Error Detail", Exception("Error Exception")).apply {
            assertEquals(METHOD_NOT_ALLOWED, this.status)
            assertEquals("Error Detail", this.detail)
            assertNotNull(this.cause)
            assertEquals("Error Exception", this.cause!!.message)
        }

        notAcceptable("Error message", "Error Detail", Exception("Error Exception")).apply {
            assertEquals(NOT_ACCEPTABLE, this.status)
            assertEquals("Error Detail", this.detail)
            assertNotNull(this.cause)
            assertEquals("Error Exception", this.cause!!.message)
        }

        proxyAuthenticationRequired("Error message", "Error Detail", Exception("Error Exception")).apply {
            assertEquals(PROXY_AUTHENTICATION_REQUIRED, this.status)
            assertEquals("Error Detail", this.detail)
            assertNotNull(this.cause)
            assertEquals("Error Exception", this.cause!!.message)
        }

        requestTimeout("Error message", "Error Detail", Exception("Error Exception")).apply {
            assertEquals(REQUEST_TIMEOUT, this.status)
            assertEquals("Error Detail", this.detail)
            assertNotNull(this.cause)
            assertEquals("Error Exception", this.cause!!.message)
        }

        conflict("Error message", "Error Detail", Exception("Error Exception")).apply {
            assertEquals(CONFLICT, this.status)
            assertEquals("Error Detail", this.detail)
            assertNotNull(this.cause)
            assertEquals("Error Exception", this.cause!!.message)
        }

        gone("Error message", "Error Detail", Exception("Error Exception")).apply {
            assertEquals(GONE, this.status)
            assertEquals("Error Detail", this.detail)
            assertNotNull(this.cause)
            assertEquals("Error Exception", this.cause!!.message)
        }

        lengthRequired("Error message", "Error Detail", Exception("Error Exception")).apply {
            assertEquals(LENGTH_REQUIRED, this.status)
            assertEquals("Error Detail", this.detail)
            assertNotNull(this.cause)
            assertEquals("Error Exception", this.cause!!.message)
        }

        preconditionFailed("Error message", "Error Detail", Exception("Error Exception")).apply {
            assertEquals(PRECONDITION_FAILED, this.status)
            assertEquals("Error Detail", this.detail)
            assertNotNull(this.cause)
            assertEquals("Error Exception", this.cause!!.message)
        }

        payloadTooLarge("Error message", "Error Detail", Exception("Error Exception")).apply {
            assertEquals(PAYLOAD_TOO_LARGE, this.status)
            assertEquals("Error Detail", this.detail)
            assertNotNull(this.cause)
            assertEquals("Error Exception", this.cause!!.message)
        }

        uriTooLong("Error message", "Error Detail", Exception("Error Exception")).apply {
            assertEquals(URI_TOO_LONG, this.status)
            assertEquals("Error Detail", this.detail)
            assertNotNull(this.cause)
            assertEquals("Error Exception", this.cause!!.message)
        }

        unsupportedMediaType("Error message", "Error Detail", Exception("Error Exception")).apply {
            assertEquals(UNSUPPORTED_MEDIA_TYPE, this.status)
            assertEquals("Error Detail", this.detail)
            assertNotNull(this.cause)
            assertEquals("Error Exception", this.cause!!.message)
        }

        requestedRangeNotSatisfiable("Error message", "Error Detail", Exception("Error Exception")).apply {
            assertEquals(REQUESTED_RANGE_NOT_SATISFIABLE, this.status)
            assertEquals("Error Detail", this.detail)
            assertNotNull(this.cause)
            assertEquals("Error Exception", this.cause!!.message)
        }

        expectationFailed("Error message", "Error Detail", Exception("Error Exception")).apply {
            assertEquals(EXPECTATION_FAILED, this.status)
            assertEquals("Error Detail", this.detail)
            assertNotNull(this.cause)
            assertEquals("Error Exception", this.cause!!.message)
        }

        iAmATeapot("Error message", "Error Detail", Exception("Error Exception")).apply {
            assertEquals(I_AM_A_TEAPOT, this.status)
            assertEquals("Error Detail", this.detail)
            assertNotNull(this.cause)
            assertEquals("Error Exception", this.cause!!.message)
        }

        unprocessableEntity("Error message", "Error Detail", Exception("Error Exception")).apply {
            assertEquals(UNPROCESSABLE_ENTITY, this.status)
            assertEquals("Error Detail", this.detail)
            assertNotNull(this.cause)
            assertEquals("Error Exception", this.cause!!.message)
        }

        locked("Error message", "Error Detail", Exception("Error Exception")).apply {
            assertEquals(LOCKED, this.status)
            assertEquals("Error Detail", this.detail)
            assertNotNull(this.cause)
            assertEquals("Error Exception", this.cause!!.message)
        }

        failedDependency("Error message", "Error Detail", Exception("Error Exception")).apply {
            assertEquals(FAILED_DEPENDENCY, this.status)
            assertEquals("Error Detail", this.detail)
            assertNotNull(this.cause)
            assertEquals("Error Exception", this.cause!!.message)
        }

        tooEarly("Error message", "Error Detail", Exception("Error Exception")).apply {
            assertEquals(TOO_EARLY, this.status)
            assertEquals("Error Detail", this.detail)
            assertNotNull(this.cause)
            assertEquals("Error Exception", this.cause!!.message)
        }

        upgradeRequired("Error message", "Error Detail", Exception("Error Exception")).apply {
            assertEquals(UPGRADE_REQUIRED, this.status)
            assertEquals("Error Detail", this.detail)
            assertNotNull(this.cause)
            assertEquals("Error Exception", this.cause!!.message)
        }

        preconditionRequired("Error message", "Error Detail", Exception("Error Exception")).apply {
            assertEquals(PRECONDITION_REQUIRED, this.status)
            assertEquals("Error Detail", this.detail)
            assertNotNull(this.cause)
            assertEquals("Error Exception", this.cause!!.message)
        }

        tooManyRequests("Error message", "Error Detail", Exception("Error Exception")).apply {
            assertEquals(TOO_MANY_REQUESTS, this.status)
            assertEquals("Error Detail", this.detail)
            assertNotNull(this.cause)
            assertEquals("Error Exception", this.cause!!.message)
        }

        requestHeaderFieldsTooLarge("Error message", "Error Detail", Exception("Error Exception")).apply {
            assertEquals(REQUEST_HEADER_FIELDS_TOO_LARGE, this.status)
            assertEquals("Error Detail", this.detail)
            assertNotNull(this.cause)
            assertEquals("Error Exception", this.cause!!.message)
        }

        unavailableForLegalReasons("Error message", "Error Detail", Exception("Error Exception")).apply {
            assertEquals(UNAVAILABLE_FOR_LEGAL_REASONS, this.status)
            assertEquals("Error Detail", this.detail)
            assertNotNull(this.cause)
            assertEquals("Error Exception", this.cause!!.message)
        }

        internalServerError("Error message", "Error Detail", Exception("Error Exception")).apply {
            assertEquals(INTERNAL_SERVER_ERROR, this.status)
            assertEquals("Error Detail", this.detail)
            assertNotNull(this.cause)
            assertEquals("Error Exception", this.cause!!.message)
        }

        notImplemented("Error message", "Error Detail", Exception("Error Exception")).apply {
            assertEquals(NOT_IMPLEMENTED, this.status)
            assertEquals("Error Detail", this.detail)
            assertNotNull(this.cause)
            assertEquals("Error Exception", this.cause!!.message)
        }

        badGateway("Error message", "Error Detail", Exception("Error Exception")).apply {
            assertEquals(BAD_GATEWAY, this.status)
            assertEquals("Error Detail", this.detail)
            assertNotNull(this.cause)
            assertEquals("Error Exception", this.cause!!.message)
        }

        serviceUnavailable("Error message", "Error Detail", Exception("Error Exception")).apply {
            assertEquals(SERVICE_UNAVAILABLE, this.status)
            assertEquals("Error Detail", this.detail)
            assertNotNull(this.cause)
            assertEquals("Error Exception", this.cause!!.message)
        }

        gatewayTimeout("Error message", "Error Detail", Exception("Error Exception")).apply {
            assertEquals(GATEWAY_TIMEOUT, this.status)
            assertEquals("Error Detail", this.detail)
            assertNotNull(this.cause)
            assertEquals("Error Exception", this.cause!!.message)
        }

        httpVersionNotSupported("Error message", "Error Detail", Exception("Error Exception")).apply {
            assertEquals(HTTP_VERSION_NOT_SUPPORTED, this.status)
            assertEquals("Error Detail", this.detail)
            assertNotNull(this.cause)
            assertEquals("Error Exception", this.cause!!.message)
        }

        variantAlsoNegotiates("Error message", "Error Detail", Exception("Error Exception")).apply {
            assertEquals(VARIANT_ALSO_NEGOTIATES, this.status)
            assertEquals("Error Detail", this.detail)
            assertNotNull(this.cause)
            assertEquals("Error Exception", this.cause!!.message)
        }

        insufficientStorage("Error message", "Error Detail", Exception("Error Exception")).apply {
            assertEquals(INSUFFICIENT_STORAGE, this.status)
            assertEquals("Error Detail", this.detail)
            assertNotNull(this.cause)
            assertEquals("Error Exception", this.cause!!.message)
        }

        loopDetected("Error message", "Error Detail", Exception("Error Exception")).apply {
            assertEquals(LOOP_DETECTED, this.status)
            assertEquals("Error Detail", this.detail)
            assertNotNull(this.cause)
            assertEquals("Error Exception", this.cause!!.message)
        }

        bandwidthLimitExceeded("Error message", "Error Detail", Exception("Error Exception")).apply {
            assertEquals(BANDWIDTH_LIMIT_EXCEEDED, this.status)
            assertEquals("Error Detail", this.detail)
            assertNotNull(this.cause)
            assertEquals("Error Exception", this.cause!!.message)
        }

        notExtended("Error message", "Error Detail", Exception("Error Exception")).apply {
            assertEquals(NOT_EXTENDED, this.status)
            assertEquals("Error Detail", this.detail)
            assertNotNull(this.cause)
            assertEquals("Error Exception", this.cause!!.message)
        }

        networkAuthenticationRequired("Error message", "Error Detail", Exception("Error Exception")).apply {
            assertEquals(NETWORK_AUTHENTICATION_REQUIRED, this.status)
            assertEquals("Error Detail", this.detail)
            assertNotNull(this.cause)
            assertEquals("Error Exception", this.cause!!.message)
        }

    }

}