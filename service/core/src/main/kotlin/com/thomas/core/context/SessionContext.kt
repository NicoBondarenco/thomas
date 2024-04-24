package com.thomas.core.context

import com.thomas.core.HttpApplicationException
import com.thomas.core.i18n.CoreMessageI18N.coreContextSessionUserNotLogged
import com.thomas.core.model.http.HTTPStatus.UNAUTHORIZED
import com.thomas.core.model.security.SecurityUser
import java.util.Locale
import java.util.Locale.ROOT

data class SessionContext(
    private val sessionProperties: MutableMap<String, String?> = mutableMapOf(),
) {

    private var _currentUser: SecurityUser? = null

    internal var currentToken: String? = null

    internal var currentLocale: Locale = ROOT

    internal var currentUser: SecurityUser
        get() = _currentUser ?: throw HttpApplicationException(UNAUTHORIZED, coreContextSessionUserNotLogged())
        set(value) {
            _currentUser = value
        }

    internal fun getProperty(property: String): String? = sessionProperties[property]

    internal fun setProperty(property: String, value: String?) {
        sessionProperties[property] = value
    }

    internal fun clear() {
        _currentUser = null
        currentLocale = ROOT
    }

}
