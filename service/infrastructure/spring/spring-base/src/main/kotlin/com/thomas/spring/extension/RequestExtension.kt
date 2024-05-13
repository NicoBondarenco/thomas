package com.thomas.spring.extension

import jakarta.servlet.http.HttpServletRequest
import java.util.Locale
import org.springframework.http.HttpHeaders.ACCEPT_LANGUAGE

internal fun HttpServletRequest.requestLocale() =
    this.getHeader(ACCEPT_LANGUAGE)?.let { tag ->
        Locale.forLanguageTag(tag).takeIf {
            it.isO3Country.isNotEmpty()
        }
    } ?: Locale.ROOT
