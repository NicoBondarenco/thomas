package com.thomas.core.extension

import java.util.UUID
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.Level.DEBUG
import org.apache.logging.log4j.Level.ERROR
import org.apache.logging.log4j.Level.INFO
import org.apache.logging.log4j.Level.TRACE
import org.apache.logging.log4j.Level.WARN
import org.apache.logging.log4j.kotlin.KotlinLogger

fun KotlinLogger.logTrace(message: String, parameters: Map<String, Any?> = mapOf()) {
    logParameterized(TRACE, null, message, parameters)
}

fun KotlinLogger.logDebug(message: String, parameters: Map<String, Any?> = mapOf()) {
    logParameterized(DEBUG, null, message, parameters)
}

fun KotlinLogger.logInfo(message: String, parameters: Map<String, Any?> = mapOf()) {
    logParameterized(INFO, null, message, parameters)
}

fun KotlinLogger.logWarn(message: String, parameters: Map<String, Any?> = mapOf()) {
    logParameterized(WARN, null, message, parameters)
}

fun KotlinLogger.logError(message: String, throwable: Throwable? = null, parameters: Map<String, Any?> = mapOf()) {
    logParameterized(ERROR, throwable, message, parameters)
}

fun KotlinLogger.logParameterized(
    level: Level,
    throwable: Throwable? = null,
    message: String,
    parameters: Map<String, Any?> = mapOf()
) {
    val traceId = Thread.currentThread().traceId()
    val method = Thread.currentThread().methodName()
    val params = parameters.toParametersString()
    this.log(level, "traceId=$traceId, method=${method}, message=$message, parameters=$params", throwable)
}

private fun Thread.traceId() = UUID.nameUUIDFromBytes(this.threadId().toString().encodeToByteArray())

private fun Thread.methodName() = this.stackTrace[2].methodName

private fun Map<String, Any?>.toParametersString() = "{${this.toParameters().joinToString(", ")}}"

private fun Map<String, Any?>.toParameters() = this.map { (key, value) -> "\"$key\": \"$value\"" }