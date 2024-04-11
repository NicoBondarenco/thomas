package com.thomas.core

import com.thomas.core.model.http.HTTPStatus
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

class HttpApplicationException(
    val status: HTTPStatus,
    message: String,
    val detail: Any? = "No details available",
    cause: Throwable? = null
) : RuntimeException(message, cause) {

    companion object {

        fun badRequest(message: String, detail: Any? = null, cause: Throwable? = null) = HttpApplicationException(BAD_REQUEST, message, detail, cause)

        fun unauthorized(message: String, detail: Any? = null, cause: Throwable? = null) = HttpApplicationException(UNAUTHORIZED, message, detail, cause)

        fun paymentRequired(message: String, detail: Any? = null, cause: Throwable? = null) = HttpApplicationException(PAYMENT_REQUIRED, message, detail, cause)

        fun forbidden(message: String, detail: Any? = null, cause: Throwable? = null) = HttpApplicationException(FORBIDDEN, message, detail, cause)

        fun notFound(message: String, detail: Any? = null, cause: Throwable? = null) = HttpApplicationException(NOT_FOUND, message, detail, cause)

        fun methodNotAllowed(message: String, detail: Any? = null, cause: Throwable? = null) = HttpApplicationException(METHOD_NOT_ALLOWED, message, detail, cause)

        fun notAcceptable(message: String, detail: Any? = null, cause: Throwable? = null) = HttpApplicationException(NOT_ACCEPTABLE, message, detail, cause)

        fun proxyAuthenticationRequired(message: String, detail: Any? = null, cause: Throwable? = null) = HttpApplicationException(PROXY_AUTHENTICATION_REQUIRED, message, detail, cause)

        fun requestTimeout(message: String, detail: Any? = null, cause: Throwable? = null) = HttpApplicationException(REQUEST_TIMEOUT, message, detail, cause)

        fun conflict(message: String, detail: Any? = null, cause: Throwable? = null) = HttpApplicationException(CONFLICT, message, detail, cause)

        fun gone(message: String, detail: Any? = null, cause: Throwable? = null) = HttpApplicationException(GONE, message, detail, cause)

        fun lengthRequired(message: String, detail: Any? = null, cause: Throwable? = null) = HttpApplicationException(LENGTH_REQUIRED, message, detail, cause)

        fun preconditionFailed(message: String, detail: Any? = null, cause: Throwable? = null) = HttpApplicationException(PRECONDITION_FAILED, message, detail, cause)

        fun payloadTooLarge(message: String, detail: Any? = null, cause: Throwable? = null) = HttpApplicationException(PAYLOAD_TOO_LARGE, message, detail, cause)

        fun uriTooLong(message: String, detail: Any? = null, cause: Throwable? = null) = HttpApplicationException(URI_TOO_LONG, message, detail, cause)

        fun unsupportedMediaType(message: String, detail: Any? = null, cause: Throwable? = null) = HttpApplicationException(UNSUPPORTED_MEDIA_TYPE, message, detail, cause)

        fun requestedRangeNotSatisfiable(message: String, detail: Any? = null, cause: Throwable? = null) = HttpApplicationException(REQUESTED_RANGE_NOT_SATISFIABLE, message, detail, cause)

        fun expectationFailed(message: String, detail: Any? = null, cause: Throwable? = null) = HttpApplicationException(EXPECTATION_FAILED, message, detail, cause)

        fun iAmATeapot(message: String, detail: Any? = null, cause: Throwable? = null) = HttpApplicationException(I_AM_A_TEAPOT, message, detail, cause)

        fun unprocessableEntity(message: String, detail: Any? = null, cause: Throwable? = null) = HttpApplicationException(UNPROCESSABLE_ENTITY, message, detail, cause)

        fun locked(message: String, detail: Any? = null, cause: Throwable? = null) = HttpApplicationException(LOCKED, message, detail, cause)

        fun failedDependency(message: String, detail: Any? = null, cause: Throwable? = null) = HttpApplicationException(FAILED_DEPENDENCY, message, detail, cause)

        fun tooEarly(message: String, detail: Any? = null, cause: Throwable? = null) = HttpApplicationException(TOO_EARLY, message, detail, cause)

        fun upgradeRequired(message: String, detail: Any? = null, cause: Throwable? = null) = HttpApplicationException(UPGRADE_REQUIRED, message, detail, cause)

        fun preconditionRequired(message: String, detail: Any? = null, cause: Throwable? = null) = HttpApplicationException(PRECONDITION_REQUIRED, message, detail, cause)

        fun tooManyRequests(message: String, detail: Any? = null, cause: Throwable? = null) = HttpApplicationException(TOO_MANY_REQUESTS, message, detail, cause)

        fun requestHeaderFieldsTooLarge(message: String, detail: Any? = null, cause: Throwable? = null) = HttpApplicationException(REQUEST_HEADER_FIELDS_TOO_LARGE, message, detail, cause)

        fun unavailableForLegalReasons(message: String, detail: Any? = null, cause: Throwable? = null) = HttpApplicationException(UNAVAILABLE_FOR_LEGAL_REASONS, message, detail, cause)

        fun internalServerError(message: String, detail: Any? = null, cause: Throwable? = null) = HttpApplicationException(INTERNAL_SERVER_ERROR, message, detail, cause)

        fun notImplemented(message: String, detail: Any? = null, cause: Throwable? = null) = HttpApplicationException(NOT_IMPLEMENTED, message, detail, cause)

        fun badGateway(message: String, detail: Any? = null, cause: Throwable? = null) = HttpApplicationException(BAD_GATEWAY, message, detail, cause)

        fun serviceUnavailable(message: String, detail: Any? = null, cause: Throwable? = null) = HttpApplicationException(SERVICE_UNAVAILABLE, message, detail, cause)

        fun gatewayTimeout(message: String, detail: Any? = null, cause: Throwable? = null) = HttpApplicationException(GATEWAY_TIMEOUT, message, detail, cause)

        fun httpVersionNotSupported(message: String, detail: Any? = null, cause: Throwable? = null) = HttpApplicationException(HTTP_VERSION_NOT_SUPPORTED, message, detail, cause)

        fun variantAlsoNegotiates(message: String, detail: Any? = null, cause: Throwable? = null) = HttpApplicationException(VARIANT_ALSO_NEGOTIATES, message, detail, cause)

        fun insufficientStorage(message: String, detail: Any? = null, cause: Throwable? = null) = HttpApplicationException(INSUFFICIENT_STORAGE, message, detail, cause)

        fun loopDetected(message: String, detail: Any? = null, cause: Throwable? = null) = HttpApplicationException(LOOP_DETECTED, message, detail, cause)

        fun bandwidthLimitExceeded(message: String, detail: Any? = null, cause: Throwable? = null) = HttpApplicationException(BANDWIDTH_LIMIT_EXCEEDED, message, detail, cause)

        fun notExtended(message: String, detail: Any? = null, cause: Throwable? = null) = HttpApplicationException(NOT_EXTENDED, message, detail, cause)

        fun networkAuthenticationRequired(message: String, detail: Any? = null, cause: Throwable? = null) = HttpApplicationException(NETWORK_AUTHENTICATION_REQUIRED, message, detail, cause)

    }


}