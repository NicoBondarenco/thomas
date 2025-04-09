package com.thomas.spring.base.resolver

import com.thomas.spring.base.extension.requestLocale
import jakarta.servlet.http.HttpServletRequest
import java.util.Locale
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver

class RequestLocaleResolver : AcceptHeaderLocaleResolver() {

    override fun resolveLocale(
        request: HttpServletRequest
    ): Locale = request.requestLocale()

}
