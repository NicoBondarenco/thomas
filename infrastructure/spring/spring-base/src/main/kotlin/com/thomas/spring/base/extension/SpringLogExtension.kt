package com.thomas.spring.base.extension

import com.thomas.core.exception.ApplicationException
import io.github.oshai.kotlinlogging.KLogger
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY
import org.springframework.http.HttpStatusCode

private val WARN_STATUS_LIST: Set<Int> = setOf(
    UNPROCESSABLE_ENTITY.value(),
)

internal fun KLogger.logByStatus(ex: Throwable) {
    val code = if (ex is ApplicationException) {
        ex.type.toHttpStatus()
    } else {
        HttpStatus.INTERNAL_SERVER_ERROR
    }
    this.logByStatus(ex, code)
}

internal fun KLogger.logByStatus(ex: Throwable, code: HttpStatusCode) {

    when (code.value()) {
        in 500..599 -> {
            this.error(ex) { ex.logMessage() }
        }

        in WARN_STATUS_LIST -> {
            this.warn { ex.logMessage() }
        }

        else -> {
            this.debug { ex.logMessage() }
        }
    }
}


private fun Throwable.logMessage(): String = if (this is ApplicationException) {
    this.message.let {
        "$it\n" + (this.detail?.map { entry ->
            "\t${entry.key}\n\t\t${entry.value.joinToString("\n\t\t")}"
        }?.joinToString("\n") ?: "")
    }
} else {
    this.message ?: ""
}
