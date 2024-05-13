package com.thomas.spring.extension

import org.apache.commons.logging.Log
import org.springframework.http.HttpStatusCode

internal fun Log.logByStatus(ex: Throwable, code: HttpStatusCode) {
    if (code.is5xxServerError) {
        this.error(ex.message, ex)
    } else {
        this.debug(ex.message, ex)
    }
}