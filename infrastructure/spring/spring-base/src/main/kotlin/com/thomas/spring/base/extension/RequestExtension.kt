package com.thomas.spring.base.extension

import com.thomas.core.extension.toUUIDOrNull
import jakarta.servlet.http.HttpServletRequest
import java.util.Locale
import org.springframework.http.HttpHeaders.ACCEPT_LANGUAGE
import org.springframework.http.HttpHeaders.AUTHORIZATION

const val UNIT_HEADER = "Unit-ID"

internal fun HttpServletRequest.requestLocale() =
    this.getHeader(ACCEPT_LANGUAGE)?.let { tag ->
        Locale.forLanguageTag(tag).takeIf {
            it.isO3Country.isNotEmpty()
        }
    } ?: Locale.ROOT

internal fun HttpServletRequest.bearerToken() =
    this.getHeader(AUTHORIZATION)?.replaceFirst("Bearer", "")?.trim()

internal fun HttpServletRequest.unitId() =
    this.getHeader(UNIT_HEADER)?.toUUIDOrNull()
