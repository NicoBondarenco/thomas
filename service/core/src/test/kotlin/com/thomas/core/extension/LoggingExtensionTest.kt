package com.thomas.core.extension

import java.math.BigDecimal
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.kotlin.Logging
import org.junit.jupiter.api.Test

class LoggingExtensionTest : Logging {

    @Test
    fun `Log Trace with class and method`() {
        val service = TraceTest()
        service.depth01()
        service.depth01(false)
    }

    @Test
    fun `Log Debug with class and method`() {
        val service = ServiceTest()
        service.logDebug()
        service.logDebug(false)
    }

    @Test
    fun `Log Info with class and method`() {
        val service = ServiceTest()
        service.logInfo()
        service.logInfo(false)
    }

    @Test
    fun `Log Warn with class and method`() {
        val service = ServiceTest()
        service.logWarn()
        service.logWarn(false)
    }

    @Test
    fun `Log Error with class and method`() {
        val service = ServiceTest()
        service.logError()
        service.logError(false)
    }

    @Test
    fun `Log Parametrized`() {
        logger.logParameterized(
            level = Level.INFO,
            message = "Error message"
        )
    }

    @Test
    fun `Log Parametrized Parameters`() {
        logger.logParameterized(
            level = Level.INFO,
            message = "Error message",
            parameters = mapOf(
                "first" to "bfbc3083-b26c-4e3a-8956-fd24fe6ce201",
                "second" to null
            )
        )
    }

    private class TraceTest : ServiceTest() {

        fun depth01(useMap: Boolean = true) {
            depth02(useMap)
        }

        fun depth02(useMap: Boolean = true) {
            depth03(useMap)
        }

        fun depth03(useMap: Boolean = true) {
            logTrace(useMap)
        }

    }

    private open class ServiceTest : Logging {

        fun logTrace(useMap: Boolean = true) {
            if (useMap) {
                logger.logTrace("Test Message")
            } else {
                logger.logTrace(
                    "Test Message",
                    mapOf(
                        "Null" to null,
                        "String" to "Qwerty",
                        "Integer" to 10,
                        "Boolean" to true,
                        "BigDecimal" to BigDecimal("156.79"),
                        "Double" to 43.20,
                    )
                )
            }
        }

        fun logDebug(useMap: Boolean = true) {
            if (useMap) {
                logger.logDebug("Test Message")
            } else {
                logger.logDebug(
                    "Test Message",
                    mapOf(
                        "Null" to null,
                        "String" to "Qwerty",
                        "Integer" to 10,
                        "Boolean" to true,
                        "BigDecimal" to BigDecimal("156.79"),
                        "Double" to 43.20,
                    )
                )
            }
        }

        fun logInfo(useMap: Boolean = true) {
            if (useMap) {
                logger.logInfo("Test Message")
            } else {
                logger.logInfo(
                    "Test Message",
                    mapOf(
                        "Null" to null,
                        "String" to "Qwerty",
                        "Integer" to 10,
                        "Boolean" to true,
                        "BigDecimal" to BigDecimal("156.79"),
                        "Double" to 43.20,
                    )
                )
            }
        }

        fun logWarn(useMap: Boolean = true) {
            if (useMap) {
                logger.logWarn("Test Message")
            } else {
                logger.logWarn(
                    "Test Message",
                    mapOf(
                        "Null" to null,
                        "String" to "Qwerty",
                        "Integer" to 10,
                        "Boolean" to true,
                        "BigDecimal" to BigDecimal("156.79"),
                        "Double" to 43.20,
                    )
                )
            }
        }

        fun logError(useMap: Boolean = true) {
            if (useMap) {
                logger.logError(
                    "Test Message",
                    Exception("Exception Message"),
                )
            } else {
                logger.logError(
                    "Test Message",
                    Exception("Exception Message"),
                    mapOf(
                        "Null" to null,
                        "String" to "Qwerty",
                        "Integer" to 10,
                        "Boolean" to true,
                        "BigDecimal" to BigDecimal("156.79"),
                        "Double" to 43.20,
                    )
                )
            }
        }

    }

}