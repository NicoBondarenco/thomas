package com.thomas.core.model.http

import com.thomas.core.model.http.HTTPStatusSeries.CLIENT_ERROR
import com.thomas.core.model.http.HTTPStatusSeries.INFORMATIONAL
import com.thomas.core.model.http.HTTPStatusSeries.REDIRECTION
import com.thomas.core.model.http.HTTPStatusSeries.SERVER_ERROR
import com.thomas.core.model.http.HTTPStatusSeries.SUCCESSFUL

enum class HTTPStatus(
    val code: Int,
    val series: HTTPStatusSeries,
    val reason: String
) {

    CONTINUE(100, INFORMATIONAL, "Continue"),
    SWITCHING_PROTOCOLS(101, INFORMATIONAL, "Switching Protocols"),
    PROCESSING(102, INFORMATIONAL, "Processing"),
    CHECKPOINT(103, INFORMATIONAL, "Checkpoint"),

    OK(200, SUCCESSFUL, "OK"),
    CREATED(201, SUCCESSFUL, "Created"),
    ACCEPTED(202, SUCCESSFUL, "Accepted"),
    NON_AUTHORITATIVE_INFORMATION(203, SUCCESSFUL, "Non-Authoritative Information"),
    NO_CONTENT(204, SUCCESSFUL, "No Content"),
    RESET_CONTENT(205, SUCCESSFUL, "Reset Content"),
    PARTIAL_CONTENT(206, SUCCESSFUL, "Partial Content"),
    MULTI_STATUS(207, SUCCESSFUL, "Multi-Status"),
    ALREADY_REPORTED(208, SUCCESSFUL, "Already Reported"),
    IM_USED(226, SUCCESSFUL, "IM Used"),

    MULTIPLE_CHOICES(300, REDIRECTION, "Multiple Choices"),
    MOVED_PERMANENTLY(301, REDIRECTION, "Moved Permanently"),
    FOUND(302, REDIRECTION, "Found"),
    SEE_OTHER(303, REDIRECTION, "See Other"),
    NOT_MODIFIED(304, REDIRECTION, "Not Modified"),
    USE_PROXY(305, REDIRECTION, "Use Proxy"),
    TEMPORARY_REDIRECT(307, REDIRECTION, "Temporary Redirect"),
    PERMANENT_REDIRECT(308, REDIRECTION, "Permanent Redirect"),

    BAD_REQUEST(400, CLIENT_ERROR, "Bad Request"),
    UNAUTHORIZED(401, CLIENT_ERROR, "Unauthorized"),
    PAYMENT_REQUIRED(402, CLIENT_ERROR, "Payment Required"),
    FORBIDDEN(403, CLIENT_ERROR, "Forbidden"),
    NOT_FOUND(404, CLIENT_ERROR, "Not Found"),
    METHOD_NOT_ALLOWED(405, CLIENT_ERROR, "Method Not Allowed"),
    NOT_ACCEPTABLE(406, CLIENT_ERROR, "Not Acceptable"),
    PROXY_AUTHENTICATION_REQUIRED(407, CLIENT_ERROR, "Proxy Authentication Required"),
    REQUEST_TIMEOUT(408, CLIENT_ERROR, "Request Timeout"),
    CONFLICT(409, CLIENT_ERROR, "Conflict"),
    GONE(410, CLIENT_ERROR, "Gone"),
    LENGTH_REQUIRED(411, CLIENT_ERROR, "Length Required"),
    PRECONDITION_FAILED(412, CLIENT_ERROR, "Precondition Failed"),
    PAYLOAD_TOO_LARGE(413, CLIENT_ERROR, "Payload Too Large"),
    URI_TOO_LONG(414, CLIENT_ERROR, "URI Too Long"),
    UNSUPPORTED_MEDIA_TYPE(415, CLIENT_ERROR, "Unsupported Media Type"),
    REQUESTED_RANGE_NOT_SATISFIABLE(416, CLIENT_ERROR, "Requested range not satisfiable"),
    EXPECTATION_FAILED(417, CLIENT_ERROR, "Expectation Failed"),
    I_AM_A_TEAPOT(418, CLIENT_ERROR, "I'm a teapot"),
    INSUFFICIENT_SPACE_ON_RESOURCE(419, CLIENT_ERROR, "Insufficient Space On Resource"),
    METHOD_FAILURE(420, CLIENT_ERROR, "Method Failure"),
    DESTINATION_LOCKED(421, CLIENT_ERROR, "Destination Locked"),
    UNPROCESSABLE_ENTITY(422, CLIENT_ERROR, "Unprocessable Entity"),
    LOCKED(423, CLIENT_ERROR, "Locked"),
    FAILED_DEPENDENCY(424, CLIENT_ERROR, "Failed Dependency"),
    TOO_EARLY(425, CLIENT_ERROR, "Too Early"),
    UPGRADE_REQUIRED(426, CLIENT_ERROR, "Upgrade Required"),
    PRECONDITION_REQUIRED(428, CLIENT_ERROR, "Precondition Required"),
    TOO_MANY_REQUESTS(429, CLIENT_ERROR, "Too Many Requests"),
    REQUEST_HEADER_FIELDS_TOO_LARGE(431, CLIENT_ERROR, "Request Header Fields Too Large"),
    UNAVAILABLE_FOR_LEGAL_REASONS(451, CLIENT_ERROR, "Unavailable For Legal Reasons"),

    INTERNAL_SERVER_ERROR(500, SERVER_ERROR, "Internal Server Error"),
    NOT_IMPLEMENTED(501, SERVER_ERROR, "Not Implemented"),
    BAD_GATEWAY(502, SERVER_ERROR, "Bad Gateway"),
    SERVICE_UNAVAILABLE(503, SERVER_ERROR, "Service Unavailable"),
    GATEWAY_TIMEOUT(504, SERVER_ERROR, "Gateway Timeout"),
    HTTP_VERSION_NOT_SUPPORTED(505, SERVER_ERROR, "HTTP Version not supported"),
    VARIANT_ALSO_NEGOTIATES(506, SERVER_ERROR, "Variant Also Negotiates"),
    INSUFFICIENT_STORAGE(507, SERVER_ERROR, "Insufficient Storage"),
    LOOP_DETECTED(508, SERVER_ERROR, "Loop Detected"),
    BANDWIDTH_LIMIT_EXCEEDED(509, SERVER_ERROR, "Bandwidth Limit Exceeded"),
    NOT_EXTENDED(510, SERVER_ERROR, "Not Extended"),
    NETWORK_AUTHENTICATION_REQUIRED(511, SERVER_ERROR, "Network Authentication Required");

    companion object {
        fun byCode(code: Int) =
            entries.firstOrNull { it.code == code }
                ?: INTERNAL_SERVER_ERROR
    }

}